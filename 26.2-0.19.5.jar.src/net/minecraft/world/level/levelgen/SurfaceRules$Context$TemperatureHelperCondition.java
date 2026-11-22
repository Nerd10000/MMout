/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ 
/*     */ class TemperatureHelperCondition extends SurfaceRules.LazyYCondition {
/*     */   private TemperatureHelperCondition(SurfaceRules.Context context) {
/* 236 */     super(context);
/*     */   }
/*     */   
/*     */   protected boolean compute() {
/* 241 */     return ((Biome)this.context.getBiome().value()).coldEnoughToSnow((BlockPos)this.context.pos.set(this.context.blockX, this.context.blockY, this.context.blockZ), this.context.getSeaLevel());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Context$TemperatureHelperCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */