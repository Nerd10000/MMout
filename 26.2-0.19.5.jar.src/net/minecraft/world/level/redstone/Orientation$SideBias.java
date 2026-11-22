/*     */ package net.minecraft.world.level.redstone;
/*     */ 
/*     */ public enum SideBias {
/* 244 */   LEFT("left"),
/* 245 */   RIGHT("right");
/*     */   
/*     */   private final String name;
/*     */   
/*     */   SideBias(String name) {
/* 250 */     this.name = name;
/*     */   }
/*     */   
/*     */   public SideBias getOpposite() {
/* 254 */     return (this == LEFT) ? RIGHT : LEFT;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 259 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\redstone\Orientation$SideBias.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */