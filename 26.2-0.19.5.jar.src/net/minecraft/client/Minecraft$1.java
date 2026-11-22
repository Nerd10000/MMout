/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.ClientShutdownWatchdog;
/*     */ import net.minecraft.client.main.GameConfig;
/*     */ 
/*     */ class null implements Runnable {
/*     */   private boolean threadStarted;
/*     */   
/*     */   public void run() {
/* 558 */     if (!this.threadStarted) {
/* 559 */       this.threadStarted = true;
/* 561 */       ClientShutdownWatchdog.startShutdownWatchdog("window close callback", false, Minecraft.this, gameConfig, Minecraft.this.gameThread.threadId());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\Minecraft$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */