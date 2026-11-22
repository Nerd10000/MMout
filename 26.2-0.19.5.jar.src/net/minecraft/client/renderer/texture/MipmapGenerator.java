/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.platform.TextureUtil;
/*     */ import com.mojang.blaze3d.platform.Transparency;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ public class MipmapGenerator {
/*     */   private static final String ITEM_PREFIX = "item/";
/*     */   
/*     */   private static final float ALPHA_CUTOFF = 0.5F;
/*     */   
/*     */   private static final float STRICT_ALPHA_CUTOFF = 0.3F;
/*     */   
/*     */   private static float alphaTestCoverage(NativeImage image, float alphaRef, float alphaScale) {
/*  21 */     int width = image.getWidth();
/*  22 */     int height = image.getHeight();
/*  24 */     float coverage = 0.0F;
/*  25 */     int subsample_count = 4;
/*  26 */     for (int y = 0; y < height - 1; y++) {
/*  27 */       for (int x = 0; x < width - 1; x++) {
/*  28 */         float alpha00 = Math.clamp(ARGB.alphaFloat(image.getPixel(x, y)) * alphaScale, 0.0F, 1.0F);
/*  29 */         float alpha10 = Math.clamp(ARGB.alphaFloat(image.getPixel(x + 1, y)) * alphaScale, 0.0F, 1.0F);
/*  30 */         float alpha01 = Math.clamp(ARGB.alphaFloat(image.getPixel(x, y + 1)) * alphaScale, 0.0F, 1.0F);
/*  31 */         float alpha11 = Math.clamp(ARGB.alphaFloat(image.getPixel(x + 1, y + 1)) * alphaScale, 0.0F, 1.0F);
/*  33 */         float texelCoverage = 0.0F;
/*  34 */         for (int subsample_y = 0; subsample_y < 4; subsample_y++) {
/*  35 */           float fy = (subsample_y + 0.5F) / 4.0F;
/*  36 */           for (int subsample_x = 0; subsample_x < 4; subsample_x++) {
/*  37 */             float fx = (subsample_x + 0.5F) / 4.0F;
/*  38 */             float alpha = alpha00 * (1.0F - fx) * (1.0F - fy) + alpha10 * fx * (1.0F - fy) + alpha01 * (1.0F - fx) * fy + alpha11 * fx * fy;
/*  39 */             if (alpha > alphaRef)
/*  40 */               texelCoverage++; 
/*     */           } 
/*     */         } 
/*  44 */         coverage += texelCoverage / 16.0F;
/*     */       } 
/*     */     } 
/*  48 */     return coverage / ((width - 1) * (height - 1));
/*     */   }
/*     */   
/*     */   private static void scaleAlphaToCoverage(NativeImage image, float desiredCoverage, float alphaRef, float alphaCutoffBias) {
/*  52 */     float minAlphaScale = 0.0F;
/*  53 */     float maxAlphaScale = 4.0F;
/*  54 */     float alphaScale = 1.0F;
/*  55 */     float bestAlphaScale = 1.0F;
/*  56 */     float bestError = Float.MAX_VALUE;
/*  57 */     int width = image.getWidth();
/*  58 */     int height = image.getHeight();
/*  60 */     for (int i = 0; i < 5; i++) {
/*  61 */       float currentCoverage = alphaTestCoverage(image, alphaRef, alphaScale);
/*  63 */       float error = Math.abs(currentCoverage - desiredCoverage);
/*  64 */       if (error < bestError) {
/*  65 */         bestError = error;
/*  66 */         bestAlphaScale = alphaScale;
/*     */       } 
/*  69 */       if (currentCoverage < desiredCoverage) {
/*  70 */         minAlphaScale = alphaScale;
/*  71 */       } else if (currentCoverage > desiredCoverage) {
/*  72 */         maxAlphaScale = alphaScale;
/*     */       } else {
/*     */         break;
/*     */       } 
/*  77 */       alphaScale = (minAlphaScale + maxAlphaScale) * 0.5F;
/*     */     } 
/*  80 */     for (int y = 0; y < height; y++) {
/*  81 */       for (int x = 0; x < width; x++) {
/*  82 */         int pixel = image.getPixel(x, y);
/*  83 */         float alpha = ARGB.alphaFloat(pixel);
/*  84 */         alpha = alpha * bestAlphaScale + alphaCutoffBias + 0.025F;
/*  85 */         alpha = Math.clamp(alpha, 0.0F, 1.0F);
/*  86 */         image.setPixel(x, y, ARGB.color(alpha, pixel));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static NativeImage[] generateMipLevels(Identifier name, NativeImage[] currentMips, int newMipLevel, MipmapStrategy mipmapStrategy, float alphaCutoffBias, Transparency transparency) {
/*  92 */     if (mipmapStrategy == MipmapStrategy.AUTO)
/*  93 */       mipmapStrategy = transparency.hasTransparent() ? MipmapStrategy.CUTOUT : MipmapStrategy.MEAN; 
/*  96 */     if (currentMips.length == 1 && !name.getPath().startsWith("item/"))
/*  97 */       if (mipmapStrategy == MipmapStrategy.CUTOUT || mipmapStrategy == MipmapStrategy.STRICT_CUTOUT) {
/*  98 */         TextureUtil.solidify(currentMips[0]);
/*  99 */       } else if (mipmapStrategy == MipmapStrategy.DARK_CUTOUT) {
/* 100 */         TextureUtil.fillEmptyAreasWithDarkColor(currentMips[0]);
/*     */       }  
/* 104 */     if (newMipLevel + 1 <= currentMips.length)
/* 105 */       return currentMips; 
/* 108 */     NativeImage[] result = new NativeImage[newMipLevel + 1];
/* 110 */     result[0] = currentMips[0];
/* 112 */     boolean isCutoutMip = (mipmapStrategy == MipmapStrategy.CUTOUT || mipmapStrategy == MipmapStrategy.STRICT_CUTOUT || mipmapStrategy == MipmapStrategy.DARK_CUTOUT);
/* 113 */     float cutoutRef = (mipmapStrategy == MipmapStrategy.STRICT_CUTOUT) ? 0.3F : 0.5F;
/* 114 */     float originalCoverage = isCutoutMip ? alphaTestCoverage(currentMips[0], cutoutRef, 1.0F) : 0.0F;
/* 116 */     for (int level = 1; level <= newMipLevel; level++) {
/* 117 */       if (level < currentMips.length) {
/* 118 */         result[level] = currentMips[level];
/*     */       } else {
/* 120 */         NativeImage lastData = result[level - 1];
/* 121 */         NativeImage data = new NativeImage(lastData.getWidth() >> 1, lastData.getHeight() >> 1, false);
/* 123 */         int width = data.getWidth();
/* 124 */         int height = data.getHeight();
/* 126 */         for (int x = 0; x < width; x++) {
/* 127 */           for (int y = 0; y < height; y++) {
/* 128 */             int color, color1 = lastData.getPixel(x * 2 + 0, y * 2 + 0);
/* 129 */             int color2 = lastData.getPixel(x * 2 + 1, y * 2 + 0);
/* 130 */             int color3 = lastData.getPixel(x * 2 + 0, y * 2 + 1);
/* 131 */             int color4 = lastData.getPixel(x * 2 + 1, y * 2 + 1);
/* 133 */             if (mipmapStrategy == MipmapStrategy.DARK_CUTOUT) {
/* 134 */               color = darkenedAlphaBlend(color1, color2, color3, color4);
/*     */             } else {
/* 136 */               color = ARGB.meanLinear(color1, color2, color3, color4);
/*     */             } 
/* 138 */             data.setPixel(x, y, color);
/*     */           } 
/*     */         } 
/* 142 */         result[level] = data;
/*     */       } 
/* 145 */       if (isCutoutMip)
/* 146 */         scaleAlphaToCoverage(result[level], originalCoverage, cutoutRef, alphaCutoffBias); 
/*     */     } 
/* 150 */     return result;
/*     */   }
/*     */   
/*     */   private static int darkenedAlphaBlend(int color1, int color2, int color3, int color4) {
/* 158 */     float aTotal = 0.0F;
/* 159 */     float rTotal = 0.0F;
/* 160 */     float gTotal = 0.0F;
/* 161 */     float bTotal = 0.0F;
/* 163 */     if (ARGB.alpha(color1) != 0) {
/* 164 */       aTotal += ARGB.srgbToLinearChannel(ARGB.alpha(color1));
/* 165 */       rTotal += ARGB.srgbToLinearChannel(ARGB.red(color1));
/* 166 */       gTotal += ARGB.srgbToLinearChannel(ARGB.green(color1));
/* 167 */       bTotal += ARGB.srgbToLinearChannel(ARGB.blue(color1));
/*     */     } 
/* 169 */     if (ARGB.alpha(color2) != 0) {
/* 170 */       aTotal += ARGB.srgbToLinearChannel(ARGB.alpha(color2));
/* 171 */       rTotal += ARGB.srgbToLinearChannel(ARGB.red(color2));
/* 172 */       gTotal += ARGB.srgbToLinearChannel(ARGB.green(color2));
/* 173 */       bTotal += ARGB.srgbToLinearChannel(ARGB.blue(color2));
/*     */     } 
/* 175 */     if (ARGB.alpha(color3) != 0) {
/* 176 */       aTotal += ARGB.srgbToLinearChannel(ARGB.alpha(color3));
/* 177 */       rTotal += ARGB.srgbToLinearChannel(ARGB.red(color3));
/* 178 */       gTotal += ARGB.srgbToLinearChannel(ARGB.green(color3));
/* 179 */       bTotal += ARGB.srgbToLinearChannel(ARGB.blue(color3));
/*     */     } 
/* 181 */     if (ARGB.alpha(color4) != 0) {
/* 182 */       aTotal += ARGB.srgbToLinearChannel(ARGB.alpha(color4));
/* 183 */       rTotal += ARGB.srgbToLinearChannel(ARGB.red(color4));
/* 184 */       gTotal += ARGB.srgbToLinearChannel(ARGB.green(color4));
/* 185 */       bTotal += ARGB.srgbToLinearChannel(ARGB.blue(color4));
/*     */     } 
/* 187 */     aTotal /= 4.0F;
/* 188 */     rTotal /= 4.0F;
/* 189 */     gTotal /= 4.0F;
/* 190 */     bTotal /= 4.0F;
/* 192 */     return ARGB.color(
/* 193 */         ARGB.linearToSrgbChannel(aTotal), 
/* 194 */         ARGB.linearToSrgbChannel(rTotal), 
/* 195 */         ARGB.linearToSrgbChannel(gTotal), 
/* 196 */         ARGB.linearToSrgbChannel(bTotal));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\MipmapGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */