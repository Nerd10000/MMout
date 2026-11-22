/*     */ package net.minecraft.util;
/*     */ 
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector4f;
/*     */ 
/*     */ public class ARGB {
/*     */   private static final int LINEAR_CHANNEL_DEPTH = 1024;
/*     */   
/*     */   private static final short[] SRGB_TO_LINEAR;
/*     */   
/*     */   private static final byte[] LINEAR_TO_SRGB;
/*     */   
/*     */   static {
/*  11 */     SRGB_TO_LINEAR = Util.<short[]>make(new short[256], lookup -> {
/*     */           for (int i = 0; i < lookup.length; i++) {
/*     */             float channel = i / 255.0F;
/*     */             lookup[i] = (short)Math.round(computeSrgbToLinear(channel) * 1023.0F);
/*     */           } 
/*     */         });
/*  17 */     LINEAR_TO_SRGB = Util.<byte[]>make(new byte[1024], lookup -> {
/*     */           for (int i = 0; i < lookup.length; i++) {
/*     */             float channel = i / 1023.0F;
/*     */             lookup[i] = (byte)Math.round(computeLinearToSrgb(channel) * 255.0F);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static float computeSrgbToLinear(float x) {
/*  26 */     if (x >= 0.04045F)
/*  27 */       return (float)Math.pow((x + 0.055D) / 1.055D, 2.4D); 
/*  29 */     return x / 12.92F;
/*     */   }
/*     */   
/*     */   private static float computeLinearToSrgb(float x) {
/*  34 */     if (x >= 0.0031308F)
/*  35 */       return (float)(1.055D * Math.pow(x, 0.4166666666666667D) - 0.055D); 
/*  37 */     return 12.92F * x;
/*     */   }
/*     */   
/*     */   public static float srgbToLinearChannel(int srgb) {
/*  42 */     return SRGB_TO_LINEAR[srgb] / 1023.0F;
/*     */   }
/*     */   
/*     */   public static int linearToSrgbChannel(float linear) {
/*  46 */     return LINEAR_TO_SRGB[Mth.floor(linear * 1023.0F)] & 0xFF;
/*     */   }
/*     */   
/*     */   public static int meanLinear(int srgb1, int srgb2, int srgb3, int srgb4) {
/*  51 */     return color((
/*  52 */         alpha(srgb1) + alpha(srgb2) + alpha(srgb3) + alpha(srgb4)) / 4, 
/*  53 */         linearChannelMean(red(srgb1), red(srgb2), red(srgb3), red(srgb4)), 
/*  54 */         linearChannelMean(green(srgb1), green(srgb2), green(srgb3), green(srgb4)), 
/*  55 */         linearChannelMean(blue(srgb1), blue(srgb2), blue(srgb3), blue(srgb4)));
/*     */   }
/*     */   
/*     */   private static int linearChannelMean(int c1, int c2, int c3, int c4) {
/*  60 */     int linear = (SRGB_TO_LINEAR[c1] + SRGB_TO_LINEAR[c2] + SRGB_TO_LINEAR[c3] + SRGB_TO_LINEAR[c4]) / 4;
/*  61 */     return LINEAR_TO_SRGB[linear] & 0xFF;
/*     */   }
/*     */   
/*     */   public static int alpha(int color) {
/*  65 */     return color >>> 24;
/*     */   }
/*     */   
/*     */   public static int red(int color) {
/*  69 */     return color >> 16 & 0xFF;
/*     */   }
/*     */   
/*     */   public static int green(int color) {
/*  73 */     return color >> 8 & 0xFF;
/*     */   }
/*     */   
/*     */   public static int blue(int color) {
/*  77 */     return color & 0xFF;
/*     */   }
/*     */   
/*     */   public static int color(int alpha, int red, int green, int blue) {
/*  81 */     return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
/*     */   }
/*     */   
/*     */   public static int color(int red, int green, int blue) {
/*  85 */     return color(255, red, green, blue);
/*     */   }
/*     */   
/*     */   public static int color(Vec3 vec) {
/*  89 */     return color(as8BitChannel((float)vec.x()), as8BitChannel((float)vec.y()), as8BitChannel((float)vec.z()));
/*     */   }
/*     */   
/*     */   public static int multiply(int lhs, int rhs) {
/*  93 */     if (lhs == -1)
/*  94 */       return rhs; 
/*  95 */     if (rhs == -1)
/*  96 */       return lhs; 
/*  98 */     return color(
/*  99 */         alpha(lhs) * alpha(rhs) / 255, 
/* 100 */         red(lhs) * red(rhs) / 255, 
/* 101 */         green(lhs) * green(rhs) / 255, 
/* 102 */         blue(lhs) * blue(rhs) / 255);
/*     */   }
/*     */   
/*     */   public static int addRgb(int lhs, int rhs) {
/* 107 */     return color(
/* 108 */         alpha(lhs), 
/* 109 */         Math.min(red(lhs) + red(rhs), 255), 
/* 110 */         Math.min(green(lhs) + green(rhs), 255), 
/* 111 */         Math.min(blue(lhs) + blue(rhs), 255));
/*     */   }
/*     */   
/*     */   public static int subtractRgb(int lhs, int rhs) {
/* 116 */     return color(
/* 117 */         alpha(lhs), 
/* 118 */         Math.max(red(lhs) - red(rhs), 0), 
/* 119 */         Math.max(green(lhs) - green(rhs), 0), 
/* 120 */         Math.max(blue(lhs) - blue(rhs), 0));
/*     */   }
/*     */   
/*     */   public static int multiplyAlpha(int color, float alphaMultiplier) {
/* 125 */     if (color == 0 || alphaMultiplier <= 0.0F)
/* 126 */       return 0; 
/* 127 */     if (alphaMultiplier >= 1.0F)
/* 128 */       return color; 
/* 130 */     return color(alphaFloat(color) * alphaMultiplier, color);
/*     */   }
/*     */   
/*     */   public static int scaleRGB(int color, float scale) {
/* 134 */     return scaleRGB(color, scale, scale, scale);
/*     */   }
/*     */   
/*     */   public static int scaleRGB(int color, float scaleR, float scaleG, float scaleB) {
/* 138 */     return color(
/* 139 */         alpha(color), 
/* 140 */         Math.clamp((int)(red(color) * scaleR), 0, 255), 
/* 141 */         Math.clamp((int)(green(color) * scaleG), 0, 255), 
/* 142 */         Math.clamp((int)(blue(color) * scaleB), 0, 255));
/*     */   }
/*     */   
/*     */   public static int scaleRGB(int color, int scale) {
/* 147 */     return color(
/* 148 */         alpha(color), 
/* 149 */         Math.clamp(red(color) * scale / 255L, 0, 255), 
/* 150 */         Math.clamp(green(color) * scale / 255L, 0, 255), 
/* 151 */         Math.clamp(blue(color) * scale / 255L, 0, 255));
/*     */   }
/*     */   
/*     */   public static int greyscale(int color) {
/* 156 */     int greyscale = (int)(red(color) * 0.3F + green(color) * 0.59F + blue(color) * 0.11F);
/* 157 */     return color(alpha(color), greyscale, greyscale, greyscale);
/*     */   }
/*     */   
/*     */   public static int alphaBlend(int destination, int source) {
/* 161 */     int destinationAlpha = alpha(destination);
/* 162 */     int sourceAlpha = alpha(source);
/* 163 */     if (sourceAlpha == 255)
/* 164 */       return source; 
/* 165 */     if (sourceAlpha == 0)
/* 166 */       return destination; 
/* 168 */     int alpha = sourceAlpha + destinationAlpha * (255 - sourceAlpha) / 255;
/* 169 */     return color(alpha, 
/* 170 */         alphaBlendChannel(alpha, sourceAlpha, red(destination), red(source)), 
/* 171 */         alphaBlendChannel(alpha, sourceAlpha, green(destination), green(source)), 
/* 172 */         alphaBlendChannel(alpha, sourceAlpha, blue(destination), blue(source)));
/*     */   }
/*     */   
/*     */   private static int alphaBlendChannel(int resultAlpha, int sourceAlpha, int destination, int source) {
/* 178 */     return (source * sourceAlpha + destination * (resultAlpha - sourceAlpha)) / resultAlpha;
/*     */   }
/*     */   
/*     */   public static int srgbLerp(float alpha, int p0, int p1) {
/* 185 */     int a = Mth.lerpInt(alpha, alpha(p0), alpha(p1));
/* 186 */     int red = Mth.lerpInt(alpha, red(p0), red(p1));
/* 187 */     int green = Mth.lerpInt(alpha, green(p0), green(p1));
/* 188 */     int blue = Mth.lerpInt(alpha, blue(p0), blue(p1));
/* 189 */     return color(a, red, green, blue);
/*     */   }
/*     */   
/*     */   public static int linearLerp(float alpha, int p0, int p1) {
/* 195 */     return color(
/* 196 */         Mth.lerpInt(alpha, alpha(p0), alpha(p1)), LINEAR_TO_SRGB[
/* 197 */           Mth.lerpInt(alpha, SRGB_TO_LINEAR[red(p0)], SRGB_TO_LINEAR[red(p1)])] & 0xFF, LINEAR_TO_SRGB[
/* 198 */           Mth.lerpInt(alpha, SRGB_TO_LINEAR[green(p0)], SRGB_TO_LINEAR[green(p1)])] & 0xFF, LINEAR_TO_SRGB[
/* 199 */           Mth.lerpInt(alpha, SRGB_TO_LINEAR[blue(p0)], SRGB_TO_LINEAR[blue(p1)])] & 0xFF);
/*     */   }
/*     */   
/*     */   public static int opaque(int color) {
/* 204 */     return color | 0xFF000000;
/*     */   }
/*     */   
/*     */   public static int transparent(int color) {
/* 208 */     return color & 0xFFFFFF;
/*     */   }
/*     */   
/*     */   public static int color(int alpha, int rgb) {
/* 212 */     return alpha << 24 | rgb & 0xFFFFFF;
/*     */   }
/*     */   
/*     */   public static int color(float alpha, int rgb) {
/* 216 */     return as8BitChannel(alpha) << 24 | rgb & 0xFFFFFF;
/*     */   }
/*     */   
/*     */   public static int white(float alpha) {
/* 220 */     return as8BitChannel(alpha) << 24 | 0xFFFFFF;
/*     */   }
/*     */   
/*     */   public static int white(int alpha) {
/* 224 */     return alpha << 24 | 0xFFFFFF;
/*     */   }
/*     */   
/*     */   public static int black(float alpha) {
/* 228 */     return as8BitChannel(alpha) << 24;
/*     */   }
/*     */   
/*     */   public static int black(int alpha) {
/* 232 */     return alpha << 24;
/*     */   }
/*     */   
/*     */   public static int gray(float brightness) {
/* 236 */     int channel = as8BitChannel(brightness);
/* 237 */     return color(channel, channel, channel);
/*     */   }
/*     */   
/*     */   public static int colorFromFloat(float alpha, float red, float green, float blue) {
/* 241 */     return color(
/* 242 */         as8BitChannel(alpha), 
/* 243 */         as8BitChannel(red), 
/* 244 */         as8BitChannel(green), 
/* 245 */         as8BitChannel(blue));
/*     */   }
/*     */   
/*     */   public static Vector3f vector3fFromRGB24(int color) {
/* 250 */     return new Vector3f(redFloat(color), greenFloat(color), blueFloat(color));
/*     */   }
/*     */   
/*     */   public static Vector4f vector4fFromARGB32(int color) {
/* 254 */     return new Vector4f(redFloat(color), greenFloat(color), blueFloat(color), alphaFloat(color));
/*     */   }
/*     */   
/*     */   public static Vector4f setVector4fFromARGB32(Vector4f dest, int color) {
/* 258 */     return dest.set(redFloat(color), greenFloat(color), blueFloat(color), alphaFloat(color));
/*     */   }
/*     */   
/*     */   public static int average(int lhs, int rhs) {
/* 262 */     return color((
/* 263 */         alpha(lhs) + alpha(rhs)) / 2, (
/* 264 */         red(lhs) + red(rhs)) / 2, (
/* 265 */         green(lhs) + green(rhs)) / 2, (
/* 266 */         blue(lhs) + blue(rhs)) / 2);
/*     */   }
/*     */   
/*     */   public static int as8BitChannel(float value) {
/* 271 */     return Mth.floor(value * 255.0F);
/*     */   }
/*     */   
/*     */   public static float alphaFloat(int color) {
/* 275 */     return from8BitChannel(alpha(color));
/*     */   }
/*     */   
/*     */   public static float redFloat(int color) {
/* 279 */     return from8BitChannel(red(color));
/*     */   }
/*     */   
/*     */   public static float greenFloat(int color) {
/* 283 */     return from8BitChannel(green(color));
/*     */   }
/*     */   
/*     */   public static float blueFloat(int color) {
/* 287 */     return from8BitChannel(blue(color));
/*     */   }
/*     */   
/*     */   private static float from8BitChannel(int value) {
/* 291 */     return value / 255.0F;
/*     */   }
/*     */   
/*     */   public static int toABGR(int color) {
/* 295 */     return color & 0xFF00FF00 | (color & 0xFF0000) >> 16 | (color & 0xFF) << 16;
/*     */   }
/*     */   
/*     */   public static int fromABGR(int color) {
/* 299 */     return toABGR(color);
/*     */   }
/*     */   
/*     */   public static int setBrightness(int color, float brightness) {
/*     */     float saturation, hue;
/* 303 */     int red = red(color);
/* 304 */     int green = green(color);
/* 305 */     int blue = blue(color);
/* 306 */     int alpha = alpha(color);
/* 309 */     int rgbMax = Math.max(Math.max(red, green), blue);
/* 310 */     int rgbMin = Math.min(Math.min(red, green), blue);
/* 311 */     float rgbConstantRange = (rgbMax - rgbMin);
/* 314 */     if (rgbMax != 0) {
/* 315 */       saturation = rgbConstantRange / rgbMax;
/*     */     } else {
/* 317 */       saturation = 0.0F;
/*     */     } 
/* 321 */     if (saturation == 0.0F) {
/* 322 */       hue = 0.0F;
/*     */     } else {
/* 324 */       float constantRed = (rgbMax - red) / rgbConstantRange;
/* 325 */       float constantGreen = (rgbMax - green) / rgbConstantRange;
/* 326 */       float constantBlue = (rgbMax - blue) / rgbConstantRange;
/* 328 */       if (red == rgbMax) {
/* 329 */         hue = constantBlue - constantGreen;
/* 330 */       } else if (green == rgbMax) {
/* 331 */         hue = 2.0F + constantRed - constantBlue;
/*     */       } else {
/* 333 */         hue = 4.0F + constantGreen - constantRed;
/*     */       } 
/* 336 */       hue /= 6.0F;
/* 338 */       if (hue < 0.0F)
/* 339 */         hue++; 
/*     */     } 
/* 344 */     if (saturation == 0.0F) {
/* 345 */       red = green = blue = Math.round(brightness * 255.0F);
/* 346 */       return color(alpha, red, green, blue);
/*     */     } 
/* 349 */     float colorWheelSegment = (hue - (float)Math.floor(hue)) * 6.0F;
/* 350 */     float colorWheelOffset = colorWheelSegment - (float)Math.floor(colorWheelSegment);
/* 351 */     float primaryColor = brightness * (1.0F - saturation);
/* 352 */     float secondaryColor = brightness * (1.0F - saturation * colorWheelOffset);
/* 353 */     float tertiaryColor = brightness * (1.0F - saturation * (1.0F - colorWheelOffset));
/* 355 */     switch ((int)colorWheelSegment) {
/*     */       case 0:
/* 357 */         red = Math.round(brightness * 255.0F);
/* 358 */         green = Math.round(tertiaryColor * 255.0F);
/* 359 */         blue = Math.round(primaryColor * 255.0F);
/*     */         break;
/*     */       case 1:
/* 362 */         red = Math.round(secondaryColor * 255.0F);
/* 363 */         green = Math.round(brightness * 255.0F);
/* 364 */         blue = Math.round(primaryColor * 255.0F);
/*     */         break;
/*     */       case 2:
/* 367 */         red = Math.round(primaryColor * 255.0F);
/* 368 */         green = Math.round(brightness * 255.0F);
/* 369 */         blue = Math.round(tertiaryColor * 255.0F);
/*     */         break;
/*     */       case 3:
/* 372 */         red = Math.round(primaryColor * 255.0F);
/* 373 */         green = Math.round(secondaryColor * 255.0F);
/* 374 */         blue = Math.round(brightness * 255.0F);
/*     */         break;
/*     */       case 4:
/* 377 */         red = Math.round(tertiaryColor * 255.0F);
/* 378 */         green = Math.round(primaryColor * 255.0F);
/* 379 */         blue = Math.round(brightness * 255.0F);
/*     */         break;
/*     */       case 5:
/* 382 */         red = Math.round(brightness * 255.0F);
/* 383 */         green = Math.round(primaryColor * 255.0F);
/* 384 */         blue = Math.round(secondaryColor * 255.0F);
/*     */         break;
/*     */     } 
/* 388 */     return color(alpha, red, green, blue);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ARGB.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */