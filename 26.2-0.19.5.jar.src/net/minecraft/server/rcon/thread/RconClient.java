/*     */ package net.minecraft.server.rcon.thread;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.rcon.PktUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RconClient extends GenericThread {
/*  17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int SERVERDATA_AUTH = 3;
/*     */   
/*     */   private static final int SERVERDATA_EXECCOMMAND = 2;
/*     */   
/*     */   private static final int SERVERDATA_RESPONSE_VALUE = 0;
/*     */   
/*     */   private static final int SERVERDATA_AUTH_RESPONSE = 2;
/*     */   
/*     */   private static final int SERVERDATA_AUTH_FAILURE = -1;
/*     */   
/*     */   private boolean authed;
/*     */   
/*     */   private final Socket client;
/*     */   
/*  25 */   private final byte[] buf = new byte[1460];
/*     */   
/*     */   private final String rconPassword;
/*     */   
/*     */   private final ServerInterface serverInterface;
/*     */   
/*     */   public RconClient(ServerInterface serverInterface, String rconPassword, Socket socket) {
/*  30 */     super("RCON Client " + String.valueOf(socket.getInetAddress()));
/*  31 */     this.serverInterface = serverInterface;
/*  32 */     this.client = socket;
/*     */     try {
/*  35 */       this.client.setSoTimeout(0);
/*  36 */     } catch (Exception ignored) {
/*  37 */       this.running = false;
/*     */     } 
/*  40 */     this.rconPassword = rconPassword;
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     try {
/*  46 */       while (this.running) {
/*     */         String password;
/*  47 */         BufferedInputStream inputStream = new BufferedInputStream(this.client.getInputStream());
/*  48 */         int read = inputStream.read(this.buf, 0, 1460);
/*  50 */         if (10 > read)
/*     */           return; 
/*  54 */         int offset = 0;
/*  55 */         int pktsize = PktUtils.intFromByteArray(this.buf, 0, read);
/*  56 */         if (pktsize != read - 4)
/*     */           return; 
/*  60 */         offset += 4;
/*  61 */         int requestid = PktUtils.intFromByteArray(this.buf, offset, read);
/*  62 */         offset += 4;
/*  64 */         int cmd = PktUtils.intFromByteArray(this.buf, offset);
/*  65 */         offset += 4;
/*  66 */         switch (cmd) {
/*     */           case 3:
/*  68 */             password = PktUtils.stringFromByteArray(this.buf, offset, read);
/*  69 */             offset += password.length();
/*  70 */             if (!password.isEmpty() && password.equals(this.rconPassword)) {
/*  71 */               this.authed = true;
/*  72 */               send(requestid, 2, "");
/*     */               continue;
/*     */             } 
/*  74 */             this.authed = false;
/*  75 */             sendAuthFailure();
/*     */             continue;
/*     */           case 2:
/*  79 */             if (this.authed) {
/*  80 */               String command = PktUtils.stringFromByteArray(this.buf, offset, read);
/*     */               try {
/*  82 */                 sendCmdResponse(requestid, this.serverInterface.runCommand(command));
/*  83 */               } catch (Exception e) {
/*  84 */                 sendCmdResponse(requestid, "Error executing: " + command + " (" + e.getMessage() + ")");
/*     */               } 
/*     */               continue;
/*     */             } 
/*  87 */             sendAuthFailure();
/*     */             continue;
/*     */         } 
/*  90 */         sendCmdResponse(requestid, String.format(Locale.ROOT, "Unknown request %s", new Object[] { Integer.toHexString(cmd) }));
/*     */       } 
/*  93 */     } catch (IOException iOException) {
/*     */     
/*  94 */     } catch (Exception e) {
/*  95 */       LOGGER.error("Exception whilst parsing RCON input", e);
/*     */     } finally {
/*  97 */       closeSocket();
/*  98 */       LOGGER.info("Thread {} shutting down", this.name);
/*  99 */       this.running = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void send(int requestid, int cmd, String str) throws IOException {
/* 106 */     ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1248);
/* 107 */     DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
/* 108 */     byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
/* 109 */     dataOutputStream.writeInt(Integer.reverseBytes(bytes.length + 10));
/* 110 */     dataOutputStream.writeInt(Integer.reverseBytes(requestid));
/* 111 */     dataOutputStream.writeInt(Integer.reverseBytes(cmd));
/* 112 */     dataOutputStream.write(bytes);
/* 113 */     dataOutputStream.write(0);
/* 114 */     dataOutputStream.write(0);
/* 115 */     this.client.getOutputStream().write(outputStream.toByteArray());
/*     */   }
/*     */   
/*     */   private void sendAuthFailure() throws IOException {
/* 119 */     send(-1, 2, "");
/*     */   }
/*     */   
/*     */   private void sendCmdResponse(int requestid, String response) throws IOException {
/* 123 */     int len = response.length();
/*     */     do {
/* 126 */       int dataLen = (4096 <= len) ? 4096 : len;
/* 127 */       send(requestid, 0, response.substring(0, dataLen));
/* 128 */       response = response.substring(dataLen);
/* 129 */       len = response.length();
/* 130 */     } while (0 != len);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 138 */     this.running = false;
/* 139 */     closeSocket();
/* 140 */     super.stop();
/*     */   }
/*     */   
/*     */   private void closeSocket() {
/*     */     try {
/* 145 */       this.client.close();
/* 146 */     } catch (IOException e) {
/* 147 */       LOGGER.warn("Failed to close socket", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\rcon\thread\RconClient.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */