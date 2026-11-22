/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.UUID;
/*     */ import java.util.function.IntPredicate;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.math.Fraction;
/*     */ import org.apache.commons.lang3.math.NumberUtils;
/*     */ import org.joml.Math;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class Mth {
/*     */   private static final long UUID_VERSION = 61440L;
/*     */   
/*     */   private static final long UUID_VERSION_TYPE_4 = 16384L;
/*     */   
/*     */   private static final long UUID_VARIANT = -4611686018427387904L;
/*     */   
/*     */   private static final long UUID_VARIANT_2 = -9223372036854775808L;
/*     */   
/*     */   public static final float PI = 3.1415927F;
/*     */   
/*     */   public static final float HALF_PI = 1.5707964F;
/*     */   
/*     */   public static final float TWO_PI = 6.2831855F;
/*     */   
/*     */   public static final float DEG_TO_RAD = 0.017453292F;
/*     */   
/*     */   public static final float RAD_TO_DEG = 57.295776F;
/*     */   
/*     */   public static final float EPSILON = 1.0E-5F;
/*     */   
/*  35 */   public static final float SQRT_OF_TWO = sqrt(2.0F);
/*     */   
/*  37 */   public static final Vector3fc Y_AXIS = (Vector3fc)new Vector3f(0.0F, 1.0F, 0.0F);
/*     */   
/*  38 */   public static final Vector3fc X_AXIS = (Vector3fc)new Vector3f(1.0F, 0.0F, 0.0F);
/*     */   
/*  39 */   public static final Vector3fc Z_AXIS = (Vector3fc)new Vector3f(0.0F, 0.0F, 1.0F);
/*     */   
/*     */   private static final int SIN_QUANTIZATION = 65536;
/*     */   
/*     */   private static final int SIN_MASK = 65535;
/*     */   
/*     */   private static final int COS_OFFSET = 16384;
/*     */   
/*     */   private static final double SIN_SCALE = 10430.378350470453D;
/*     */   
/*     */   private static final float[] SIN;
/*     */   
/*     */   static {
/*  46 */     SIN = Util.<float[]>make(new float[65536], sin -> {
/*     */           for (int i = 0; i < sin.length; i++)
/*     */             sin[i] = (float)Math.sin(i / 10430.378350470453D); 
/*     */         });
/*     */   }
/*     */   
/*     */   public static float sin(double i) {
/*  53 */     return SIN[(int)((long)(i * 10430.378350470453D) & 0xFFFFL)];
/*     */   }
/*     */   
/*     */   public static float cos(double i) {
/*  57 */     return SIN[(int)((long)(i * 10430.378350470453D + 16384.0D) & 0xFFFFL)];
/*     */   }
/*     */   
/*     */   public static float sqrt(float x) {
/*  61 */     return (float)Math.sqrt(x);
/*     */   }
/*     */   
/*     */   public static int floor(float v) {
/*  65 */     return (int)Math.floor(v);
/*     */   }
/*     */   
/*     */   public static int floor(double v) {
/*  69 */     return (int)Math.floor(v);
/*     */   }
/*     */   
/*     */   public static long lfloor(double v) {
/*  73 */     return (long)Math.floor(v);
/*     */   }
/*     */   
/*     */   public static float abs(float v) {
/*  77 */     return Math.abs(v);
/*     */   }
/*     */   
/*     */   public static int abs(int v) {
/*  81 */     return Math.abs(v);
/*     */   }
/*     */   
/*     */   public static int ceil(float v) {
/*  85 */     return (int)Math.ceil(v);
/*     */   }
/*     */   
/*     */   public static int ceil(double v) {
/*  89 */     return (int)Math.ceil(v);
/*     */   }
/*     */   
/*     */   public static long ceilLong(double v) {
/*  93 */     return (long)Math.ceil(v);
/*     */   }
/*     */   
/*     */   public static int clamp(int value, int min, int max) {
/*  97 */     return Math.min(Math.max(value, min), max);
/*     */   }
/*     */   
/*     */   public static long clamp(long value, long min, long max) {
/* 101 */     return Math.min(Math.max(value, min), max);
/*     */   }
/*     */   
/*     */   public static float clamp(float value, float min, float max) {
/* 105 */     if (value < min)
/* 106 */       return min; 
/* 108 */     return Math.min(value, max);
/*     */   }
/*     */   
/*     */   public static double clamp(double value, double min, double max) {
/* 112 */     if (value < min)
/* 113 */       return min; 
/* 115 */     return Math.min(value, max);
/*     */   }
/*     */   
/*     */   public static double clampedLerp(double factor, double min, double max) {
/* 121 */     if (factor < 0.0D)
/* 122 */       return min; 
/* 124 */     if (factor > 1.0D)
/* 125 */       return max; 
/* 127 */     return lerp(factor, min, max);
/*     */   }
/*     */   
/*     */   public static float clampedLerp(float factor, float min, float max) {
/* 131 */     if (factor < 0.0F)
/* 132 */       return min; 
/* 134 */     if (factor > 1.0F)
/* 135 */       return max; 
/* 137 */     return lerp(factor, min, max);
/*     */   }
/*     */   
/*     */   public static int absMax(int a, int b) {
/* 141 */     return Math.max(Math.abs(a), Math.abs(b));
/*     */   }
/*     */   
/*     */   public static float absMax(float a, float b) {
/* 145 */     return Math.max(Math.abs(a), Math.abs(b));
/*     */   }
/*     */   
/*     */   public static double absMax(double a, double b) {
/* 149 */     return Math.max(Math.abs(a), Math.abs(b));
/*     */   }
/*     */   
/*     */   public static int chessboardDistance(int x0, int z0, int x1, int z1) {
/* 153 */     return absMax(x1 - x0, z1 - z0);
/*     */   }
/*     */   
/*     */   public static int floorDiv(int a, int b) {
/* 157 */     return Math.floorDiv(a, b);
/*     */   }
/*     */   
/*     */   public static int nextInt(RandomSource random, int minInclusive, int maxInclusive) {
/* 161 */     if (minInclusive >= maxInclusive)
/* 162 */       return minInclusive; 
/* 164 */     return random.nextInt(maxInclusive - minInclusive + 1) + minInclusive;
/*     */   }
/*     */   
/*     */   public static float nextFloat(RandomSource random, float min, float max) {
/* 168 */     if (min >= max)
/* 169 */       return min; 
/* 171 */     return random.nextFloat() * (max - min) + min;
/*     */   }
/*     */   
/*     */   public static double nextDouble(RandomSource random, double min, double max) {
/* 175 */     if (min >= max)
/* 176 */       return min; 
/* 178 */     return random.nextDouble() * (max - min) + min;
/*     */   }
/*     */   
/*     */   public static boolean equal(float a, float b) {
/* 182 */     return (Math.abs(b - a) < 1.0E-5F);
/*     */   }
/*     */   
/*     */   public static boolean equal(double a, double b) {
/* 186 */     return (Math.abs(b - a) < 9.999999747378752E-6D);
/*     */   }
/*     */   
/*     */   public static int positiveModulo(int input, int mod) {
/* 190 */     return Math.floorMod(input, mod);
/*     */   }
/*     */   
/*     */   public static float positiveModulo(float input, float mod) {
/* 194 */     return (input % mod + mod) % mod;
/*     */   }
/*     */   
/*     */   public static double positiveModulo(double input, double mod) {
/* 198 */     return (input % mod + mod) % mod;
/*     */   }
/*     */   
/*     */   public static boolean isMultipleOf(int dividend, int divisor) {
/* 202 */     return (dividend % divisor == 0);
/*     */   }
/*     */   
/*     */   public static byte packDegrees(float angle) {
/* 207 */     return (byte)floor(angle * 256.0F / 360.0F);
/*     */   }
/*     */   
/*     */   public static float unpackDegrees(byte rot) {
/* 212 */     return (rot * 360) / 256.0F;
/*     */   }
/*     */   
/*     */   public static int wrapDegrees(int angle) {
/* 217 */     int normalizedAngle = angle % 360;
/* 218 */     if (normalizedAngle >= 180)
/* 219 */       normalizedAngle -= 360; 
/* 221 */     if (normalizedAngle < -180)
/* 222 */       normalizedAngle += 360; 
/* 224 */     return normalizedAngle;
/*     */   }
/*     */   
/*     */   public static float wrapDegrees(long angle) {
/* 229 */     float normalizedAngle = (float)(angle % 360L);
/* 230 */     if (normalizedAngle >= 180.0F)
/* 231 */       normalizedAngle -= 360.0F; 
/* 233 */     if (normalizedAngle < -180.0F)
/* 234 */       normalizedAngle += 360.0F; 
/* 236 */     return normalizedAngle;
/*     */   }
/*     */   
/*     */   public static float wrapDegrees(float angle) {
/* 241 */     float normalizedAngle = angle % 360.0F;
/* 242 */     if (normalizedAngle >= 180.0F)
/* 243 */       normalizedAngle -= 360.0F; 
/* 245 */     if (normalizedAngle < -180.0F)
/* 246 */       normalizedAngle += 360.0F; 
/* 248 */     return normalizedAngle;
/*     */   }
/*     */   
/*     */   public static double wrapDegrees(double angle) {
/* 253 */     double normalizedAngle = angle % 360.0D;
/* 254 */     if (normalizedAngle >= 180.0D)
/* 255 */       normalizedAngle -= 360.0D; 
/* 257 */     if (normalizedAngle < -180.0D)
/* 258 */       normalizedAngle += 360.0D; 
/* 260 */     return normalizedAngle;
/*     */   }
/*     */   
/*     */   public static float wrapDegrees90(float angle) {
/* 265 */     float normalizedAngle = angle % 90.0F;
/* 266 */     if (normalizedAngle >= 45.0F)
/* 267 */       normalizedAngle -= 90.0F; 
/* 269 */     if (normalizedAngle < -45.0F)
/* 270 */       normalizedAngle += 90.0F; 
/* 272 */     return normalizedAngle;
/*     */   }
/*     */   
/*     */   public static float degreesDifference(float fromAngle, float toAngle) {
/* 278 */     return wrapDegrees(toAngle - fromAngle);
/*     */   }
/*     */   
/*     */   public static float degreesDifferenceAbs(float angleA, float angleB) {
/* 284 */     return abs(degreesDifference(angleA, angleB));
/*     */   }
/*     */   
/*     */   public static float rotateIfNecessary(float baseAngle, float targetAngle, float maxAngleDiff) {
/* 291 */     float deltaAngle = degreesDifference(baseAngle, targetAngle);
/* 292 */     float deltaAngleClamped = clamp(deltaAngle, -maxAngleDiff, maxAngleDiff);
/* 293 */     return targetAngle - deltaAngleClamped;
/*     */   }
/*     */   
/*     */   public static float approach(float current, float target, float increment) {
/* 297 */     increment = abs(increment);
/* 299 */     if (current < target)
/* 300 */       return clamp(current + increment, current, target); 
/* 302 */     return clamp(current - increment, target, current);
/*     */   }
/*     */   
/*     */   public static float approachDegrees(float current, float target, float increment) {
/* 307 */     float difference = degreesDifference(current, target);
/* 308 */     return approach(current, current + difference, increment);
/*     */   }
/*     */   
/*     */   public static int getInt(String input, int def) {
/* 312 */     return NumberUtils.toInt(input, def);
/*     */   }
/*     */   
/*     */   public static int smallestEncompassingPowerOfTwo(int input) {
/* 317 */     int result = input - 1;
/* 318 */     result |= result >> 1;
/* 319 */     result |= result >> 2;
/* 320 */     result |= result >> 4;
/* 321 */     result |= result >> 8;
/* 322 */     result |= result >> 16;
/* 323 */     return result + 1;
/*     */   }
/*     */   
/*     */   public static int smallestSquareSide(int itemCount) {
/* 327 */     if (itemCount < 0)
/* 328 */       throw new IllegalArgumentException("itemCount must be greater than or equal to zero"); 
/* 330 */     return ceil(Math.sqrt(itemCount));
/*     */   }
/*     */   
/*     */   public static boolean isPowerOfTwo(int input) {
/* 335 */     return (input != 0 && (input & input - 1) == 0);
/*     */   }
/*     */   
/*     */   public static boolean isPowerOfTwo(long input) {
/* 339 */     return (input != 0L && (input & input - 1L) == 0L);
/*     */   }
/*     */   
/* 343 */   private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] { 
/* 343 */       0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 
/* 343 */       20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 
/* 343 */       21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 
/* 343 */       10, 9 };
/*     */   
/*     */   private static final double ONE_SIXTH = 0.16666666666666666D;
/*     */   
/*     */   private static final int FRAC_EXP = 8;
/*     */   
/*     */   private static final int LUT_SIZE = 257;
/*     */   
/*     */   public static int ceillog2(int input) {
/* 348 */     input = isPowerOfTwo(input) ? input : smallestEncompassingPowerOfTwo(input);
/* 349 */     return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)(input * 125613361L >> 27L) & 0x1F];
/*     */   }
/*     */   
/*     */   public static int log2(int input) {
/* 353 */     return ceillog2(input) - (isPowerOfTwo(input) ? 0 : 1);
/*     */   }
/*     */   
/*     */   public static float frac(float num) {
/* 357 */     return num - floor(num);
/*     */   }
/*     */   
/*     */   public static double frac(double num) {
/* 361 */     return num - lfloor(num);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static long getSeed(Vec3i vec) {
/* 366 */     return getSeed(vec.getX(), vec.getY(), vec.getZ());
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static long getSeed(int x, int y, int z) {
/* 374 */     long seed = (x * 3129871) ^ z * 116129781L ^ y;
/* 375 */     seed = seed * seed * 42317861L + seed * 11L;
/* 376 */     return seed >> 16L;
/*     */   }
/*     */   
/*     */   public static UUID createInsecureUUID(RandomSource random) {
/* 380 */     long most = random.nextLong() & 0xFFFFFFFFFFFF0FFFL | 0x4000L;
/* 381 */     long least = random.nextLong() & 0x3FFFFFFFFFFFFFFFL | Long.MIN_VALUE;
/* 382 */     return new UUID(most, least);
/*     */   }
/*     */   
/*     */   public static double inverseLerp(double value, double min, double max) {
/* 386 */     return (value - min) / (max - min);
/*     */   }
/*     */   
/*     */   public static float inverseLerp(float value, float min, float max) {
/* 390 */     return (value - min) / (max - min);
/*     */   }
/*     */   
/*     */   public static boolean rayIntersectsAABB(Vec3 rayStart, Vec3 rayDir, AABB aabb) {
/* 395 */     double centerX = (aabb.minX + aabb.maxX) * 0.5D;
/* 396 */     double boxExtentX = (aabb.maxX - aabb.minX) * 0.5D;
/* 397 */     double diffX = rayStart.x - centerX;
/* 398 */     if (Math.abs(diffX) > boxExtentX && diffX * rayDir.x >= 0.0D)
/* 399 */       return false; 
/* 402 */     double centerY = (aabb.minY + aabb.maxY) * 0.5D;
/* 403 */     double boxExtentY = (aabb.maxY - aabb.minY) * 0.5D;
/* 404 */     double diffY = rayStart.y - centerY;
/* 405 */     if (Math.abs(diffY) > boxExtentY && diffY * rayDir.y >= 0.0D)
/* 406 */       return false; 
/* 409 */     double centerZ = (aabb.minZ + aabb.maxZ) * 0.5D;
/* 410 */     double boxExtentZ = (aabb.maxZ - aabb.minZ) * 0.5D;
/* 411 */     double diffZ = rayStart.z - centerZ;
/* 412 */     if (Math.abs(diffZ) > boxExtentZ && diffZ * rayDir.z >= 0.0D)
/* 413 */       return false; 
/* 416 */     double andrewWooDiffX = Math.abs(rayDir.x);
/* 417 */     double andrewWooDiffY = Math.abs(rayDir.y);
/* 418 */     double andrewWooDiffZ = Math.abs(rayDir.z);
/* 420 */     double f = rayDir.y * diffZ - rayDir.z * diffY;
/* 421 */     if (Math.abs(f) > boxExtentY * andrewWooDiffZ + boxExtentZ * andrewWooDiffY)
/* 422 */       return false; 
/* 425 */     f = rayDir.z * diffX - rayDir.x * diffZ;
/* 426 */     if (Math.abs(f) > boxExtentX * andrewWooDiffZ + boxExtentZ * andrewWooDiffX)
/* 427 */       return false; 
/* 430 */     f = rayDir.x * diffY - rayDir.y * diffX;
/* 432 */     return (Math.abs(f) < boxExtentX * andrewWooDiffY + boxExtentY * andrewWooDiffX);
/*     */   }
/*     */   
/*     */   public static double atan2(double y, double x) {
/* 436 */     double d2 = x * x + y * y;
/* 439 */     if (Double.isNaN(d2))
/* 440 */       return Double.NaN; 
/* 444 */     boolean negY = (y < 0.0D);
/* 445 */     if (negY)
/* 446 */       y = -y; 
/* 448 */     boolean negX = (x < 0.0D);
/* 449 */     if (negX)
/* 450 */       x = -x; 
/* 452 */     boolean steep = (y > x);
/* 453 */     if (steep) {
/* 454 */       double t = x;
/* 456 */       x = y;
/* 457 */       y = t;
/*     */     } 
/* 461 */     double rinv = fastInvSqrt(d2);
/* 462 */     x *= rinv;
/* 463 */     y *= rinv;
/* 472 */     double yp = FRAC_BIAS + y;
/* 473 */     int index = (int)Double.doubleToRawLongBits(yp);
/* 476 */     double phi = ASIN_TAB[index];
/* 477 */     double cPhi = COS_TAB[index];
/* 481 */     double sPhi = yp - FRAC_BIAS;
/* 482 */     double sd = y * cPhi - x * sPhi;
/* 485 */     double d = (6.0D + sd * sd) * sd * 0.16666666666666666D;
/* 486 */     double theta = phi + d;
/* 489 */     if (steep)
/* 490 */       theta = 1.5707963267948966D - theta; 
/* 492 */     if (negX)
/* 493 */       theta = Math.PI - theta; 
/* 495 */     if (negY)
/* 496 */       theta = -theta; 
/* 499 */     return theta;
/*     */   }
/*     */   
/*     */   public static float invSqrt(float x) {
/* 503 */     return Math.invsqrt(x);
/*     */   }
/*     */   
/*     */   public static double invSqrt(double x) {
/* 507 */     return Math.invsqrt(x);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static double fastInvSqrt(double x) {
/* 516 */     double xhalf = 0.5D * x;
/* 517 */     long i = Double.doubleToRawLongBits(x);
/* 518 */     i = 6910469410427058090L - (i >> 1L);
/* 519 */     x = Double.longBitsToDouble(i);
/* 520 */     x *= 1.5D - xhalf * x * x;
/* 521 */     return x;
/*     */   }
/*     */   
/*     */   public static float fastInvCubeRoot(float x) {
/* 525 */     int i = Float.floatToIntBits(x);
/* 526 */     i = 1419967116 - i / 3;
/* 527 */     float y = Float.intBitsToFloat(i);
/* 528 */     y = 0.6666667F * y + 1.0F / 3.0F * y * y * x;
/* 529 */     y = 0.6666667F * y + 1.0F / 3.0F * y * y * x;
/* 530 */     return y;
/*     */   }
/*     */   
/* 536 */   private static final double FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
/*     */   
/* 537 */   private static final double[] ASIN_TAB = new double[257];
/*     */   
/* 538 */   private static final double[] COS_TAB = new double[257];
/*     */   
/*     */   static {
/* 542 */     for (int ind = 0; ind < 257; ind++) {
/* 543 */       double v = ind / 256.0D;
/* 544 */       double asinv = Math.asin(v);
/* 545 */       COS_TAB[ind] = Math.cos(asinv);
/* 546 */       ASIN_TAB[ind] = asinv;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int hsvToRgb(float hue, float saturation, float value) {
/* 551 */     return hsvToArgb(hue, saturation, value, 0);
/*     */   }
/*     */   
/*     */   public static int hsvToArgb(float hue, float saturation, float value, int alpha) {
/*     */     float red, green, blue;
/* 555 */     int h = (int)(hue * 6.0F) % 6;
/* 556 */     float f = hue * 6.0F - h;
/* 557 */     float p = value * (1.0F - saturation);
/* 558 */     float q = value * (1.0F - f * saturation);
/* 559 */     float t = value * (1.0F - (1.0F - f) * saturation);
/* 565 */     switch (h) {
/*     */       case 0:
/* 567 */         red = value;
/* 568 */         green = t;
/* 569 */         blue = p;
/*     */         break;
/*     */       case 1:
/* 572 */         red = q;
/* 573 */         green = value;
/* 574 */         blue = p;
/*     */         break;
/*     */       case 2:
/* 577 */         red = p;
/* 578 */         green = value;
/* 579 */         blue = t;
/*     */         break;
/*     */       case 3:
/* 582 */         red = p;
/* 583 */         green = q;
/* 584 */         blue = value;
/*     */         break;
/*     */       case 4:
/* 587 */         red = t;
/* 588 */         green = p;
/* 589 */         blue = value;
/*     */         break;
/*     */       case 5:
/* 592 */         red = value;
/* 593 */         green = p;
/* 594 */         blue = q;
/*     */         break;
/*     */       default:
/* 597 */         throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
/*     */     } 
/* 600 */     return ARGB.color(alpha, 
/* 601 */         clamp((int)(red * 255.0F), 0, 255), 
/* 602 */         clamp((int)(green * 255.0F), 0, 255), 
/* 603 */         clamp((int)(blue * 255.0F), 0, 255));
/*     */   }
/*     */   
/*     */   public static int murmurHash3Mixer(int hash) {
/* 609 */     hash ^= hash >>> 16;
/* 610 */     hash *= -2048144789;
/* 611 */     hash ^= hash >>> 13;
/* 612 */     hash *= -1028477387;
/* 613 */     hash ^= hash >>> 16;
/* 614 */     return hash;
/*     */   }
/*     */   
/*     */   public static int binarySearch(int from, int to, IntPredicate condition) {
/* 628 */     int len = to - from;
/* 629 */     while (len > 0) {
/* 630 */       int half = len / 2;
/* 631 */       int middle = from + half;
/* 632 */       if (condition.test(middle)) {
/* 634 */         len = half;
/*     */         continue;
/*     */       } 
/* 636 */       from = middle + 1;
/* 637 */       len -= half + 1;
/*     */     } 
/* 640 */     return from;
/*     */   }
/*     */   
/*     */   public static int lerpInt(float alpha1, int p0, int p1) {
/* 644 */     return p0 + floor(alpha1 * (p1 - p0));
/*     */   }
/*     */   
/*     */   public static int lerpDiscrete(float alpha1, int p0, int p1) {
/* 649 */     int delta = p1 - p0;
/* 650 */     return p0 + floor(alpha1 * (delta - 1)) + ((alpha1 > 0.0F) ? 1 : 0);
/*     */   }
/*     */   
/*     */   public static float lerp(float alpha1, float p0, float p1) {
/* 654 */     return p0 + alpha1 * (p1 - p0);
/*     */   }
/*     */   
/*     */   public static Vec3 lerp(double alpha, Vec3 p1, Vec3 p2) {
/* 658 */     return new Vec3(lerp(alpha, p1.x, p2.x), 
/* 659 */         lerp(alpha, p1.y, p2.y), 
/* 660 */         lerp(alpha, p1.z, p2.z));
/*     */   }
/*     */   
/*     */   public static double lerp(double alpha1, double p0, double p1) {
/* 664 */     return p0 + alpha1 * (p1 - p0);
/*     */   }
/*     */   
/*     */   public static double lerp2(double alpha1, double alpha2, double x00, double x10, double x01, double x11) {
/* 668 */     return lerp(alpha2, 
/*     */         
/* 670 */         lerp(alpha1, x00, x10), 
/* 671 */         lerp(alpha1, x01, x11));
/*     */   }
/*     */   
/*     */   public static double lerp3(double alpha1, double alpha2, double alpha3, double x000, double x100, double x010, double x110, double x001, double x101, double x011, double x111) {
/* 680 */     return lerp(alpha3, 
/*     */         
/* 682 */         lerp2(alpha1, alpha2, x000, x100, x010, x110), 
/* 683 */         lerp2(alpha1, alpha2, x001, x101, x011, x111));
/*     */   }
/*     */   
/*     */   public static float catmullrom(float alpha, float p0, float p1, float p2, float p3) {
/* 688 */     return 0.5F * (2.0F * p1 + (p2 - p0) * alpha + (2.0F * p0 - 5.0F * p1 + 4.0F * p2 - p3) * alpha * alpha + (3.0F * p1 - p0 - 3.0F * p2 + p3) * alpha * alpha * alpha);
/*     */   }
/*     */   
/*     */   public static double smoothstep(double x) {
/* 699 */     return x * x * x * (x * (x * 6.0D - 15.0D) + 10.0D);
/*     */   }
/*     */   
/*     */   public static double smoothstepDerivative(double x) {
/* 704 */     return 30.0D * x * x * (x - 1.0D) * (x - 1.0D);
/*     */   }
/*     */   
/*     */   public static int sign(double number) {
/* 708 */     if (number == 0.0D)
/* 709 */       return 0; 
/* 711 */     return (number > 0.0D) ? 1 : -1;
/*     */   }
/*     */   
/*     */   public static float rotLerp(float a, float from, float to) {
/* 715 */     return from + a * wrapDegrees(to - from);
/*     */   }
/*     */   
/*     */   public static double rotLerp(double a, double from, double to) {
/* 719 */     return from + a * wrapDegrees(to - from);
/*     */   }
/*     */   
/*     */   public static float rotLerpRad(float a, float from, float to) {
/* 723 */     float diff = to - from;
/* 724 */     while (diff < -3.1415927F)
/* 725 */       diff += 6.2831855F; 
/* 727 */     while (diff >= 3.1415927F)
/* 728 */       diff -= 6.2831855F; 
/* 730 */     return from + a * diff;
/*     */   }
/*     */   
/*     */   public static float triangleWave(float index, float period) {
/* 740 */     return (Math.abs(index % period - period * 0.5F) - period * 0.25F) / period * 0.25F;
/*     */   }
/*     */   
/*     */   public static float square(float x) {
/* 744 */     return x * x;
/*     */   }
/*     */   
/*     */   public static float cube(float x) {
/* 748 */     return x * x * x;
/*     */   }
/*     */   
/*     */   public static double square(double x) {
/* 752 */     return x * x;
/*     */   }
/*     */   
/*     */   public static int square(int x) {
/* 756 */     return x * x;
/*     */   }
/*     */   
/*     */   public static long square(long x) {
/* 760 */     return x * x;
/*     */   }
/*     */   
/*     */   public static double clampedMap(double value, double fromMin, double fromMax, double toMin, double toMax) {
/* 766 */     return clampedLerp(inverseLerp(value, fromMin, fromMax), toMin, toMax);
/*     */   }
/*     */   
/*     */   public static float clampedMap(float value, float fromMin, float fromMax, float toMin, float toMax) {
/* 770 */     return clampedLerp(inverseLerp(value, fromMin, fromMax), toMin, toMax);
/*     */   }
/*     */   
/*     */   public static double map(double value, double fromMin, double fromMax, double toMin, double toMax) {
/* 775 */     return lerp(inverseLerp(value, fromMin, fromMax), toMin, toMax);
/*     */   }
/*     */   
/*     */   public static float map(float value, float fromMin, float fromMax, float toMin, float toMax) {
/* 779 */     return lerp(inverseLerp(value, fromMin, fromMax), toMin, toMax);
/*     */   }
/*     */   
/*     */   public static double wobble(double coord) {
/* 783 */     return coord + (2.0D * RandomSource.createThreadLocalInstance(floor(coord * 3000.0D)).nextDouble() - 1.0D) * 1.0E-7D / 2.0D;
/*     */   }
/*     */   
/*     */   public static int roundToward(int input, int multiple) {
/* 787 */     return positiveCeilDiv(input, multiple) * multiple;
/*     */   }
/*     */   
/*     */   public static long roundToward(long input, long multiple) {
/* 791 */     return positiveCeilDiv(input, multiple) * multiple;
/*     */   }
/*     */   
/*     */   public static int positiveCeilDiv(int input, int divisor) {
/* 795 */     return -Math.floorDiv(-input, divisor);
/*     */   }
/*     */   
/*     */   public static long positiveCeilDiv(long input, long divisor) {
/* 799 */     return -Math.floorDiv(-input, divisor);
/*     */   }
/*     */   
/*     */   public static int randomBetweenInclusive(RandomSource random, int min, int maxInclusive) {
/* 803 */     return random.nextInt(maxInclusive - min + 1) + min;
/*     */   }
/*     */   
/*     */   public static float randomBetween(RandomSource random, float min, float maxExclusive) {
/* 807 */     return random.nextFloat() * (maxExclusive - min) + min;
/*     */   }
/*     */   
/*     */   public static float normal(RandomSource random, float mean, float deviation) {
/* 812 */     return mean + (float)random.nextGaussian() * deviation;
/*     */   }
/*     */   
/*     */   public static double lengthSquared(double x, double y) {
/* 816 */     return x * x + y * y;
/*     */   }
/*     */   
/*     */   public static double length(double x, double y) {
/* 820 */     return Math.sqrt(lengthSquared(x, y));
/*     */   }
/*     */   
/*     */   public static float length(float x, float y) {
/* 824 */     return (float)Math.sqrt(lengthSquared(x, y));
/*     */   }
/*     */   
/*     */   public static double lengthSquared(double x, double y, double z) {
/* 828 */     return x * x + y * y + z * z;
/*     */   }
/*     */   
/*     */   public static double length(double x, double y, double z) {
/* 832 */     return Math.sqrt(lengthSquared(x, y, z));
/*     */   }
/*     */   
/*     */   public static float lengthSquared(float x, float y, float z) {
/* 836 */     return x * x + y * y + z * z;
/*     */   }
/*     */   
/*     */   public static int quantize(double value, int quantizeResolution) {
/* 841 */     return floor(value / quantizeResolution) * quantizeResolution;
/*     */   }
/*     */   
/*     */   public static IntStream outFromOrigin(int origin, int lowerBound, int upperBound) {
/* 845 */     return outFromOrigin(origin, lowerBound, upperBound, 1);
/*     */   }
/*     */   
/*     */   public static IntStream outFromOrigin(int origin, int lowerBound, int upperBound, int stepSize) {
/* 851 */     if (lowerBound > upperBound)
/* 852 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "upperBound %d expected to be > lowerBound %d", new Object[] { Integer.valueOf(upperBound), Integer.valueOf(lowerBound) })); 
/* 855 */     if (stepSize < 1)
/* 856 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "step size expected to be >= 1, was %d", new Object[] { Integer.valueOf(stepSize) })); 
/* 859 */     int clampedOrigin = clamp(origin, lowerBound, upperBound);
/* 860 */     return IntStream.iterate(clampedOrigin, cursor -> {
/*     */           int currentDistance = Math.abs(clampedOrigin - cursor);
/* 862 */           return (clampedOrigin - currentDistance >= lowerBound || clampedOrigin + currentDistance <= upperBound);
/*     */         }cursor -> {
/*     */           boolean previousWasNegative = (cursor <= clampedOrigin);
/*     */           int currentDistance = Math.abs(clampedOrigin - cursor);
/*     */           boolean canMovePositive = (clampedOrigin + currentDistance + stepSize <= upperBound);
/*     */           if (!previousWasNegative || !canMovePositive) {
/*     */             int attemptedStep = clampedOrigin - currentDistance - (previousWasNegative ? stepSize : 0);
/*     */             if (attemptedStep >= lowerBound)
/*     */               return attemptedStep; 
/*     */           } 
/*     */           return clampedOrigin + currentDistance + stepSize;
/*     */         });
/*     */   }
/*     */   
/*     */   public static Quaternionf rotationAroundAxis(Vector3fc axis, Quaternionf rotation, Quaternionf result) {
/* 880 */     float projectedLength = axis.dot(rotation.x, rotation.y, rotation.z);
/* 881 */     return result.set(axis.x() * projectedLength, axis.y() * projectedLength, axis.z() * projectedLength, rotation.w).normalize();
/*     */   }
/*     */   
/*     */   public static int mulAndTruncate(Fraction fraction, int factor) {
/* 885 */     return fraction.getNumerator() * factor / fraction.getDenominator();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Mth.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */