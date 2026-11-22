/*     */ package net.minecraft.client.main;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ 
/*     */ class null extends Thread {
/*     */   null(String name) {
/* 256 */     super(name);
/*     */   }
/*     */   
/*     */   public void run() {
/* 259 */     Minecraft instance = Minecraft.getInstance();
/* 260 */     if (instance == null)
/*     */       return; 
/* 264 */     IntegratedServer server = instance.getSingleplayerServer();
/* 265 */     if (server != null)
/* 266 */       server.halt(true); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\main\Main$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */