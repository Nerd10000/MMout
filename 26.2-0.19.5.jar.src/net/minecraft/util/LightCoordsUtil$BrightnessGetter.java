/*     */ package net.minecraft.util;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface BrightnessGetter {
/*     */   public static final BrightnessGetter DEFAULT;
/*     */   
/*     */   static {
/* 117 */     DEFAULT = ((level, pos) -> {
/*     */         int sky = level.getBrightness(LightLayer.SKY, pos);
/*     */         int block = level.getBrightness(LightLayer.BLOCK, pos);
/*     */         return LightCoordsUtil.pack(block, sky);
/*     */       });
/*     */   }
/*     */   
/*     */   int packedBrightness(BlockAndLightGetter paramBlockAndLightGetter, BlockPos paramBlockPos);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\LightCoordsUtil$BrightnessGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */