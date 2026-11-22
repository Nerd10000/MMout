/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ enum Mode {
/* 286 */   FORCE(true),
/* 287 */   MOVE(true),
/* 288 */   NORMAL(false);
/*     */   
/*     */   private final boolean canOverlap;
/*     */   
/*     */   Mode(boolean canOverlap) {
/* 294 */     this.canOverlap = canOverlap;
/*     */   }
/*     */   
/*     */   public boolean canOverlap() {
/* 298 */     return this.canOverlap;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\CloneCommands$Mode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */