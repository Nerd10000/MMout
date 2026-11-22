/*     */ package net.minecraft.server;
/*     */ 
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ 
/*     */ class null extends Thread {
/*     */   null(String name) {
/* 229 */     super(name);
/*     */   }
/*     */   
/*     */   public void run() {
/* 232 */     dedicatedServer.halt(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\Main$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */