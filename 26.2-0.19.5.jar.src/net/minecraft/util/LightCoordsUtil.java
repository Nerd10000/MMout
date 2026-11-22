/*     */ package net.minecraft.util;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class LightCoordsUtil {
/*     */   public static final int FULL_BRIGHT = 15728880;
/*     */   
/*     */   public static final int FULL_SKY = 15728640;
/*     */   
/*     */   private static final int MAX_SMOOTH_LIGHT_LEVEL = 240;
/*     */   
/*     */   public static int pack(int block, int sky) {
/*  18 */     return block << 4 | sky << 20;
/*     */   }
/*     */   
/*     */   public static int block(int packed) {
/*  22 */     return packed >> 4 & 0xF;
/*     */   }
/*     */   
/*     */   public static int sky(int packed) {
/*  26 */     return packed >> 20 & 0xF;
/*     */   }
/*     */   
/*     */   public static int withBlock(int coords, int block) {
/*  30 */     return coords & 0xFF0000 | block << 4;
/*     */   }
/*     */   
/*     */   public static int smoothPack(int block, int sky) {
/*  34 */     return block & 0xFF | (sky & 0xFF) << 16;
/*     */   }
/*     */   
/*     */   public static int smoothBlock(int packed) {
/*  38 */     return packed & 0xFF;
/*     */   }
/*     */   
/*     */   public static int smoothSky(int packed) {
/*  42 */     return packed >> 16 & 0xFF;
/*     */   }
/*     */   
/*     */   public static int addSmoothBlockEmission(int lightCoords, float blockLightEmission) {
/*  46 */     blockLightEmission = Mth.clamp(blockLightEmission, 0.0F, 1.0F);
/*  47 */     int emittedBlock = (int)(Mth.clamp(blockLightEmission, 0.0F, 1.0F) * 240.0F);
/*  48 */     int block = Math.min(smoothBlock(lightCoords) + emittedBlock, 240);
/*  49 */     return smoothPack(block, smoothSky(lightCoords));
/*     */   }
/*     */   
/*     */   public static int max(int coords1, int coords2) {
/*  53 */     int block1 = block(coords1);
/*  54 */     int block2 = block(coords2);
/*  55 */     int sky1 = sky(coords1);
/*  56 */     int sky2 = sky(coords2);
/*  58 */     return pack(Math.max(block1, block2), Math.max(sky1, sky2));
/*     */   }
/*     */   
/*     */   public static int lightCoordsWithEmission(int lightCoords, int emission) {
/*  63 */     if (emission == 0)
/*  64 */       return lightCoords; 
/*  66 */     int sky = Math.max(sky(lightCoords), emission);
/*  67 */     int block = Math.max(block(lightCoords), emission);
/*  68 */     return pack(block, sky);
/*     */   }
/*     */   
/*     */   public static int smoothBlend(int neighbor1, int neighbor2, int neighbor3, int center) {
/*  73 */     if (sky(center) > 2 || block(center) > 2) {
/*  74 */       if (neighbor1 == 0) {
/*  74 */         neighbor1 = center;
/*  75 */       } else if (sky(neighbor1) == 0) {
/*  75 */         neighbor1 |= center & 0xFF0000;
/*     */       } 
/*  76 */       if (neighbor2 == 0) {
/*  76 */         neighbor2 = center;
/*  77 */       } else if (sky(neighbor2) == 0) {
/*  77 */         neighbor2 |= center & 0xFF0000;
/*     */       } 
/*  78 */       if (neighbor3 == 0) {
/*  78 */         neighbor3 = center;
/*  79 */       } else if (sky(neighbor3) == 0) {
/*  79 */         neighbor3 |= center & 0xFF0000;
/*     */       } 
/*     */     } 
/*  81 */     return neighbor1 + neighbor2 + neighbor3 + center >> 2 & 0xFF00FF;
/*     */   }
/*     */   
/*     */   public static int smoothWeightedBlend(int coords1, int coords2, int coords3, int coords4, float weight1, float weight2, float weight3, float weight4) {
/*  86 */     int sky = (int)(smoothSky(coords1) * weight1 + smoothSky(coords2) * weight2 + smoothSky(coords3) * weight3 + smoothSky(coords4) * weight4);
/*  87 */     int block = (int)(smoothBlock(coords1) * weight1 + smoothBlock(coords2) * weight2 + smoothBlock(coords3) * weight3 + smoothBlock(coords4) * weight4);
/*  88 */     return smoothPack(block, sky);
/*     */   }
/*     */   
/*     */   public static int getLightCoords(BlockAndLightGetter level, BlockPos pos) {
/*  92 */     return getLightCoords(BrightnessGetter.DEFAULT, level, level.getBlockState(pos), pos);
/*     */   }
/*     */   
/*     */   public static int getLightCoords(BrightnessGetter brightnessGetter, BlockAndLightGetter level, BlockState state, BlockPos pos) {
/*  98 */     if (state.emissiveRendering())
/* 101 */       return 15728880; 
/* 104 */     int packedBrightness = brightnessGetter.packedBrightness(level, pos);
/* 106 */     int block = block(packedBrightness);
/* 108 */     int blockSelfEmission = state.getLightEmission();
/* 109 */     if (block < blockSelfEmission)
/* 110 */       return withBlock(packedBrightness, blockSelfEmission); 
/* 112 */     return packedBrightness;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface BrightnessGetter {
/*     */     public static final BrightnessGetter DEFAULT;
/*     */     
/*     */     static {
/* 117 */       DEFAULT = ((level, pos) -> {
/*     */           int sky = level.getBrightness(LightLayer.SKY, pos);
/*     */           int block = level.getBrightness(LightLayer.BLOCK, pos);
/*     */           return LightCoordsUtil.pack(block, sky);
/*     */         });
/*     */     }
/*     */     
/*     */     int packedBrightness(BlockAndLightGetter param1BlockAndLightGetter, BlockPos param1BlockPos);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\LightCoordsUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */