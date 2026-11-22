/*     */ package net.minecraft.server.chase;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.nio.channels.ClosedByInterruptException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.server.commands.ChaseCommand;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChaseServer {
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final String serverBindAddress;
/*     */   
/*     */   private final int serverPort;
/*     */   
/*     */   private final PlayerList playerList;
/*     */   
/*     */   private final int broadcastIntervalMs;
/*     */   
/*     */   private volatile boolean wantsToRun;
/*     */   
/*     */   private ServerSocket serverSocket;
/*     */   
/*  36 */   private final CopyOnWriteArrayList<Socket> clientSockets = new CopyOnWriteArrayList<>();
/*     */   
/*     */   public ChaseServer(String serverBindAddress, int serverPort, PlayerList playerList, int broadcastIntervalMs) {
/*  39 */     this.serverBindAddress = serverBindAddress;
/*  40 */     this.serverPort = serverPort;
/*  41 */     this.playerList = playerList;
/*  42 */     this.broadcastIntervalMs = broadcastIntervalMs;
/*     */   }
/*     */   
/*     */   public void start() throws IOException {
/*  46 */     if (this.serverSocket != null && !this.serverSocket.isClosed()) {
/*  47 */       LOGGER.warn("Remote control server was asked to start, but it is already running. Will ignore.");
/*     */       return;
/*     */     } 
/*  50 */     this.wantsToRun = true;
/*  51 */     this.serverSocket = new ServerSocket(this.serverPort, 50, InetAddress.getByName(this.serverBindAddress));
/*  52 */     Thread acceptor = new Thread(this::runAcceptor, "chase-server-acceptor");
/*  53 */     acceptor.setDaemon(true);
/*  54 */     acceptor.start();
/*  56 */     Thread sender = new Thread(this::runSender, "chase-server-sender");
/*  57 */     sender.setDaemon(true);
/*  58 */     sender.start();
/*     */   }
/*     */   
/*     */   private void runSender() {
/*  62 */     PlayerPosition oldPlayerPosition = null;
/*  63 */     while (this.wantsToRun) {
/*  64 */       if (!this.clientSockets.isEmpty()) {
/*  65 */         PlayerPosition playerPosition = getPlayerPosition();
/*  66 */         if (playerPosition != null && !playerPosition.equals(oldPlayerPosition)) {
/*  67 */           oldPlayerPosition = playerPosition;
/*  68 */           byte[] messageBytes = playerPosition.format().getBytes(StandardCharsets.US_ASCII);
/*  69 */           for (Socket clientSocket : this.clientSockets) {
/*  70 */             if (clientSocket.isClosed())
/*     */               continue; 
/*  73 */             Util.ioPool().execute(() -> {
/*     */                   try {
/*     */                     OutputStream output = clientSocket.getOutputStream();
/*     */                     output.write(messageBytes);
/*     */                     output.flush();
/*  78 */                   } catch (IOException e) {
/*     */                     LOGGER.info("Remote control client socket got an IO exception and will be closed", e);
/*     */                     IOUtils.closeQuietly(clientSocket);
/*     */                   } 
/*     */                 });
/*     */           } 
/*     */         } 
/*  85 */         List<Socket> closed = (List<Socket>)this.clientSockets.stream().filter(Socket::isClosed).collect(Collectors.toList());
/*  86 */         this.clientSockets.removeAll(closed);
/*     */       } 
/*  88 */       if (this.wantsToRun)
/*     */         try {
/*  90 */           Thread.sleep(this.broadcastIntervalMs);
/*  91 */         } catch (InterruptedException interruptedException) {} 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() {
/*  98 */     this.wantsToRun = false;
/* 100 */     IOUtils.closeQuietly(this.serverSocket);
/* 101 */     this.serverSocket = null;
/*     */   }
/*     */   
/*     */   private void runAcceptor() {
/*     */     try {
/* 106 */       while (this.wantsToRun) {
/* 107 */         if (this.serverSocket != null) {
/* 108 */           LOGGER.info("Remote control server is listening for connections on port {}", Integer.valueOf(this.serverPort));
/* 109 */           Socket clientSocket = this.serverSocket.accept();
/* 110 */           LOGGER.info("Remote control server received client connection on port {}", Integer.valueOf(clientSocket.getPort()));
/* 111 */           this.clientSockets.add(clientSocket);
/*     */         } 
/*     */       } 
/* 114 */     } catch (ClosedByInterruptException e) {
/* 115 */       if (this.wantsToRun)
/* 116 */         LOGGER.info("Remote control server closed by interrupt"); 
/* 118 */     } catch (IOException e) {
/* 119 */       if (this.wantsToRun)
/* 120 */         LOGGER.error("Remote control server closed because of an IO exception", e); 
/*     */     } finally {
/* 123 */       IOUtils.closeQuietly(this.serverSocket);
/*     */     } 
/* 125 */     LOGGER.info("Remote control server is now stopped");
/* 126 */     this.wantsToRun = false;
/*     */   }
/*     */   
/*     */   private PlayerPosition getPlayerPosition() {
/* 130 */     List<ServerPlayer> players = this.playerList.getPlayers();
/* 131 */     if (players.isEmpty())
/* 132 */       return null; 
/* 134 */     ServerPlayer player = players.get(0);
/* 135 */     String dimensionName = (String)ChaseCommand.DIMENSION_NAMES.inverse().get(player.level().dimension());
/* 136 */     if (dimensionName == null)
/* 137 */       return null; 
/* 139 */     return new PlayerPosition(dimensionName, player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
/*     */   }
/*     */   
/*     */   private static final class PlayerPosition extends Record {
/*     */     private final String dimensionName;
/*     */     
/*     */     private final double x;
/*     */     
/*     */     private final double y;
/*     */     
/*     */     private final double z;
/*     */     
/*     */     private final float yRot;
/*     */     
/*     */     private final float xRot;
/*     */     
/*     */     private PlayerPosition(String dimensionName, double x, double y, double z, float yRot, float xRot) {
/* 142 */       this.dimensionName = dimensionName;
/* 142 */       this.x = x;
/* 142 */       this.y = y;
/* 142 */       this.z = z;
/* 142 */       this.yRot = yRot;
/* 142 */       this.xRot = xRot;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String dimensionName() {
/* 142 */       return this.dimensionName;
/*     */     }
/*     */     
/*     */     public double x() {
/* 142 */       return this.x;
/*     */     }
/*     */     
/*     */     public double y() {
/* 142 */       return this.y;
/*     */     }
/*     */     
/*     */     public double z() {
/* 142 */       return this.z;
/*     */     }
/*     */     
/*     */     public float yRot() {
/* 142 */       return this.yRot;
/*     */     }
/*     */     
/*     */     public float xRot() {
/* 142 */       return this.xRot;
/*     */     }
/*     */     
/*     */     private String format() {
/* 144 */       return String.format(Locale.ROOT, "t %s %.2f %.2f %.2f %.2f %.2f\n", new Object[] { this.dimensionName, Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z), Float.valueOf(this.yRot), Float.valueOf(this.xRot) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\chase\ChaseServer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */