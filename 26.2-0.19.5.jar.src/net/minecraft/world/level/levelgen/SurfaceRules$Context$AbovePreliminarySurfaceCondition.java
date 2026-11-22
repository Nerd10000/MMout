/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ final class AbovePreliminarySurfaceCondition implements SurfaceRules.Condition {
/*     */   public boolean test() {
/* 230 */     return (SurfaceRules.Context.this.blockY >= SurfaceRules.Context.this.getMinSurfaceLevel());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Context$AbovePreliminarySurfaceCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */