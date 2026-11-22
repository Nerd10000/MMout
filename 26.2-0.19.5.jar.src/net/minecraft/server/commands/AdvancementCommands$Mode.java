/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ enum Mode {
/* 316 */   ONLY(false, false),
/* 317 */   THROUGH(true, true),
/* 318 */   FROM(false, true),
/* 319 */   UNTIL(true, false),
/* 320 */   EVERYTHING(true, true);
/*     */   
/*     */   private final boolean parents;
/*     */   
/*     */   private final boolean children;
/*     */   
/*     */   Mode(boolean parents, boolean children) {
/* 327 */     this.parents = parents;
/* 328 */     this.children = children;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\AdvancementCommands$Mode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */