/*     */ package net.minecraft.server.chase;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.StringReader;
/*     */ import java.net.Socket;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Optional;
/*     */ import java.util.Scanner;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.commands.ChaseCommand;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChaseClient {
/*  37 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int RECONNECT_INTERVAL_SECONDS = 5;
/*     */   
/*     */   private final String serverHost;
/*     */   
/*     */   private final int serverPort;
/*     */   
/*     */   private final MinecraftServer server;
/*     */   
/*     */   private volatile boolean wantsToRun;
/*     */   
/*     */   private Socket socket;
/*     */   
/*     */   private Thread thread;
/*     */   
/*     */   public ChaseClient(String serverHost, int serverPort, MinecraftServer server) {
/*  50 */     this.serverHost = serverHost;
/*  51 */     this.serverPort = serverPort;
/*  52 */     this.server = server;
/*     */   }
/*     */   
/*     */   public void start() {
/*  56 */     if (this.thread != null && this.thread.isAlive())
/*  57 */       LOGGER.warn("Remote control client was asked to start, but it is already running. Will ignore."); 
/*  59 */     this.wantsToRun = true;
/*  60 */     this.thread = new Thread(this::run, "chase-client");
/*  61 */     this.thread.setDaemon(true);
/*  62 */     this.thread.start();
/*     */   }
/*     */   
/*     */   public void stop() {
/*  66 */     this.wantsToRun = false;
/*  68 */     IOUtils.closeQuietly(this.socket);
/*  69 */     this.socket = null;
/*  70 */     this.thread = null;
/*     */   }
/*     */   
/*     */   public void run() {
/*  74 */     String serverAddress = this.serverHost + ":" + this.serverHost;
/*  75 */     while (this.wantsToRun) {
/*     */       try {
/*  77 */         LOGGER.info("Connecting to remote control server {}", serverAddress);
/*  78 */         this.socket = new Socket(this.serverHost, this.serverPort);
/*  79 */         LOGGER.info("Connected to remote control server! Will continuously execute the command broadcasted by that server.");
/*     */         try {
/*  81 */           BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.US_ASCII));
/*     */           try {
/*  82 */             while (this.wantsToRun) {
/*  83 */               String message = input.readLine();
/*  84 */               if (message == null) {
/*  86 */                 LOGGER.warn("Lost connection to remote control server {}. Will retry in {}s.", serverAddress, Integer.valueOf(5));
/*     */                 break;
/*     */               } 
/*  89 */               handleMessage(message);
/*     */             } 
/*  91 */             input.close();
/*     */           } catch (Throwable throwable) {
/*     */             try {
/*     */               input.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             } 
/*     */             throw throwable;
/*     */           } 
/*  91 */         } catch (IOException err) {
/*  92 */           LOGGER.warn("Lost connection to remote control server {}. Will retry in {}s.", serverAddress, Integer.valueOf(5));
/*     */         } 
/*  94 */       } catch (IOException e) {
/*  95 */         LOGGER.warn("Failed to connect to remote control server {}. Will retry in {}s.", serverAddress, Integer.valueOf(5));
/*     */       } 
/*  98 */       if (this.wantsToRun)
/*     */         try {
/* 100 */           Thread.sleep(5000L);
/* 101 */         } catch (InterruptedException interruptedException) {} 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleMessage(String message) {
/*     */     try {
/* 108 */       Scanner scanner = new Scanner(new StringReader(message));
/*     */       try {
/* 109 */         scanner.useLocale(Locale.ROOT);
/* 110 */         String head = scanner.next();
/* 111 */         if ("t".equals(head)) {
/* 112 */           handleTeleport(scanner);
/*     */         } else {
/* 114 */           LOGGER.warn("Unknown message type '{}'", head);
/*     */         } 
/* 116 */         scanner.close();
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           scanner.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/* 116 */     } catch (NoSuchElementException e) {
/* 118 */       LOGGER.warn("Could not parse message '{}', ignoring", message);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleTeleport(Scanner scanner) {
/* 123 */     parseTarget(scanner).ifPresent(target -> executeCommand(String.format(Locale.ROOT, "execute in %s run tp @s %.3f %.3f %.3f %.3f %.3f", new Object[] { target.level.identifier(), Double.valueOf(target.pos.x), Double.valueOf(target.pos.y), Double.valueOf(target.pos.z), Float.valueOf(target.rot.y), Float.valueOf(target.rot.x) })));
/*     */   }
/*     */   
/*     */   private static final class TeleportTarget extends Record {
/*     */     private final ResourceKey<Level> level;
/*     */     
/*     */     private final Vec3 pos;
/*     */     
/*     */     private final Vec2 rot;
/*     */     
/*     */     private TeleportTarget(ResourceKey<Level> level, Vec3 pos, Vec2 rot) {
/* 128 */       this.level = level;
/* 128 */       this.pos = pos;
/* 128 */       this.rot = rot;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #128	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #128	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #128	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ResourceKey<Level> level() {
/* 128 */       return this.level;
/*     */     }
/*     */     
/*     */     public Vec3 pos() {
/* 128 */       return this.pos;
/*     */     }
/*     */     
/*     */     public Vec2 rot() {
/* 128 */       return this.rot;
/*     */     }
/*     */   }
/*     */   
/*     */   private static Optional<TeleportTarget> parseTarget(Scanner scanner) {
/* 131 */     ResourceKey<Level> levelType = (ResourceKey<Level>)ChaseCommand.DIMENSION_NAMES.get(scanner.next());
/* 132 */     if (levelType == null)
/* 133 */       return Optional.empty(); 
/* 136 */     float x = scanner.nextFloat();
/* 137 */     float y = scanner.nextFloat();
/* 138 */     float z = scanner.nextFloat();
/* 139 */     float yRot = scanner.nextFloat();
/* 140 */     float xRot = scanner.nextFloat();
/* 141 */     return Optional.of(new TeleportTarget(levelType, new Vec3(x, y, z), new Vec2(xRot, yRot)));
/*     */   }
/*     */   
/*     */   private void executeCommand(String command) {
/* 145 */     this.server.execute(() -> {
/*     */           List<ServerPlayer> players = this.server.getPlayerList().getPlayers();
/*     */           if (players.isEmpty())
/*     */             return; 
/*     */           ServerPlayer player = players.get(0);
/*     */           ServerLevel level = this.server.overworld();
/*     */           CommandSourceStack commandSourceStack = new CommandSourceStack(player.commandSource(), Vec3.atLowerCornerOf((Vec3i)level.getRespawnData().pos()), Vec2.ZERO, level, (PermissionSet)LevelBasedPermissionSet.OWNER, "", CommonComponents.EMPTY, this.server, (Entity)player);
/*     */           Commands commands = this.server.getCommands();
/*     */           commands.performPrefixedCommand(commandSourceStack, command);
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\chase\ChaseClient.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */