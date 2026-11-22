/*     */ package net.minecraft.client.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MulticastSocket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ 
/*     */ public class LanServerDetector extends Thread {
/*     */   private final LanServerDetection.LanServerList serverList;
/*     */   
/*     */   private final InetAddress pingGroup;
/*     */   
/*     */   private final MulticastSocket socket;
/*     */   
/*     */   public LanServerDetector(LanServerDetection.LanServerList serverList) throws IOException {
/*  67 */     super("LanServerDetector #" + LanServerDetection.UNIQUE_THREAD_ID.incrementAndGet());
/*  68 */     this.serverList = serverList;
/*  69 */     setDaemon(true);
/*  70 */     setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LanServerDetection.LOGGER));
/*  72 */     this.socket = new MulticastSocket(4445);
/*  73 */     this.pingGroup = InetAddress.getByName("224.0.2.60");
/*  74 */     this.socket.setSoTimeout(5000);
/*  75 */     this.socket.joinGroup(this.pingGroup);
/*     */   }
/*     */   
/*     */   public void run() {
/*  81 */     byte[] buf = new byte[1024];
/*  83 */     while (!isInterrupted()) {
/*  84 */       DatagramPacket packet = new DatagramPacket(buf, buf.length);
/*     */       try {
/*  86 */         this.socket.receive(packet);
/*  87 */       } catch (SocketTimeoutException ignored) {
/*     */         continue;
/*  89 */       } catch (IOException e) {
/*  90 */         LanServerDetection.LOGGER.error("Couldn't ping server", e);
/*     */         break;
/*     */       } 
/*  94 */       String received = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
/*  95 */       LanServerDetection.LOGGER.debug("{}: {}", packet.getAddress(), received);
/*  96 */       this.serverList.addServer(received, packet.getAddress());
/*     */     } 
/*     */     try {
/* 100 */       this.socket.leaveGroup(this.pingGroup);
/* 101 */     } catch (IOException iOException) {}
/* 103 */     this.socket.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\server\LanServerDetection$LanServerDetector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */