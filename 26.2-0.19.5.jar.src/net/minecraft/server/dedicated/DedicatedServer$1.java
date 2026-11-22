/*     */ package net.minecraft.server.dedicated;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ 
/*     */ class null extends Thread {
/*     */   null(String name) {
/* 140 */     super(name);
/*     */   }
/*     */   
/*     */   public void run() {
/* 143 */     BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
/*     */     try {
/*     */       String line;
/* 146 */       while (!DedicatedServer.this.isStopped() && DedicatedServer.this.isRunning() && (line = reader.readLine()) != null)
/* 147 */         DedicatedServer.this.handleConsoleInput(line, DedicatedServer.this.createCommandSourceStack()); 
/* 149 */     } catch (IOException e) {
/* 150 */       DedicatedServer.LOGGER.error("Exception handling console input", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dedicated\DedicatedServer$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */