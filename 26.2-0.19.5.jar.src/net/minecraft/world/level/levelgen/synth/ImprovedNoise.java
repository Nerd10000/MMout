/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public final class ImprovedNoise {
/*     */   private static final float SHIFT_UP_EPSILON = 1.0E-7F;
/*     */   
/*     */   private final byte[] p;
/*     */   
/*     */   public final double xo;
/*     */   
/*     */   public final double yo;
/*     */   
/*     */   public final double zo;
/*     */   
/*     */   public ImprovedNoise(RandomSource random) {
/*  17 */     this.xo = random.nextDouble() * 256.0D;
/*  18 */     this.yo = random.nextDouble() * 256.0D;
/*  19 */     this.zo = random.nextDouble() * 256.0D;
/*  21 */     this.p = new byte[256];
/*     */     int i;
/*  23 */     for (i = 0; i < 256; i++)
/*  24 */       this.p[i] = (byte)i; 
/*  27 */     for (i = 0; i < 256; i++) {
/*  28 */       int offset = random.nextInt(256 - i);
/*  29 */       byte tmp = this.p[i];
/*  30 */       this.p[i] = this.p[i + offset];
/*  31 */       this.p[i + offset] = tmp;
/*     */     } 
/*     */   }
/*     */   
/*     */   public double noise(double _x, double _y, double _z) {
/*  37 */     return noise(_x, _y, _z, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public double noise(double _x, double _y, double _z, double yScale, double yFudge) {
/*  43 */     double yrFudge, x = _x + this.xo;
/*  44 */     double y = _y + this.yo;
/*  45 */     double z = _z + this.zo;
/*  47 */     int xf = Mth.floor(x);
/*  48 */     int yf = Mth.floor(y);
/*  49 */     int zf = Mth.floor(z);
/*  52 */     double xr = x - xf;
/*  53 */     double yr = y - yf;
/*  54 */     double zr = z - zf;
/*  58 */     if (yScale != 0.0D) {
/*     */       double fudgeLimit;
/*  61 */       if (yFudge >= 0.0D && yFudge < yr) {
/*  62 */         fudgeLimit = yFudge;
/*     */       } else {
/*  64 */         fudgeLimit = yr;
/*     */       } 
/*  67 */       yrFudge = Mth.floor(fudgeLimit / yScale + 1.0000000116860974E-7D) * yScale;
/*     */     } else {
/*  69 */       yrFudge = 0.0D;
/*     */     } 
/*  73 */     return sampleAndLerp(xf, yf, zf, xr, yr - yrFudge, zr, yr);
/*     */   }
/*     */   
/*     */   public double noiseWithDerivative(double _x, double _y, double _z, double[] derivativeOut) {
/*  77 */     double x = _x + this.xo;
/*  78 */     double y = _y + this.yo;
/*  79 */     double z = _z + this.zo;
/*  81 */     int xf = Mth.floor(x);
/*  82 */     int yf = Mth.floor(y);
/*  83 */     int zf = Mth.floor(z);
/*  86 */     double xr = x - xf;
/*  87 */     double yr = y - yf;
/*  88 */     double zr = z - zf;
/*  90 */     return sampleWithDerivative(xf, yf, zf, xr, yr, zr, derivativeOut);
/*     */   }
/*     */   
/*     */   private static double gradDot(int hash, double x, double y, double z) {
/*  94 */     return SimplexNoise.dot(SimplexNoise.GRADIENT[hash & 0xF], x, y, z);
/*     */   }
/*     */   
/*     */   private int p(int x) {
/*  98 */     return this.p[x & 0xFF] & 0xFF;
/*     */   }
/*     */   
/*     */   private double sampleAndLerp(int x, int y, int z, double xr, double yr, double zr, double yrOriginal) {
/* 104 */     int x0 = p(x);
/* 105 */     int x1 = p(x + 1);
/* 107 */     int xy00 = p(x0 + y);
/* 108 */     int xy01 = p(x0 + y + 1);
/* 109 */     int xy10 = p(x1 + y);
/* 110 */     int xy11 = p(x1 + y + 1);
/* 118 */     double d000 = gradDot(p(xy00 + z), xr, yr, zr);
/* 119 */     double d100 = gradDot(p(xy10 + z), xr - 1.0D, yr, zr);
/* 120 */     double d010 = gradDot(p(xy01 + z), xr, yr - 1.0D, zr);
/* 121 */     double d110 = gradDot(p(xy11 + z), xr - 1.0D, yr - 1.0D, zr);
/* 122 */     double d001 = gradDot(p(xy00 + z + 1), xr, yr, zr - 1.0D);
/* 123 */     double d101 = gradDot(p(xy10 + z + 1), xr - 1.0D, yr, zr - 1.0D);
/* 124 */     double d011 = gradDot(p(xy01 + z + 1), xr, yr - 1.0D, zr - 1.0D);
/* 125 */     double d111 = gradDot(p(xy11 + z + 1), xr - 1.0D, yr - 1.0D, zr - 1.0D);
/* 128 */     double xAlpha = Mth.smoothstep(xr);
/* 129 */     double yAlpha = Mth.smoothstep(yrOriginal);
/* 130 */     double zAlpha = Mth.smoothstep(zr);
/* 133 */     return Mth.lerp3(xAlpha, yAlpha, zAlpha, d000, d100, d010, d110, d001, d101, d011, d111);
/*     */   }
/*     */   
/*     */   private double sampleWithDerivative(int x, int y, int z, double xr, double yr, double zr, double[] derivativeOut) {
/* 139 */     int x0 = p(x);
/* 140 */     int x1 = p(x + 1);
/* 142 */     int xy00 = p(x0 + y);
/* 143 */     int xy01 = p(x0 + y + 1);
/* 144 */     int xy10 = p(x1 + y);
/* 145 */     int xy11 = p(x1 + y + 1);
/* 150 */     int p000 = p(xy00 + z);
/* 151 */     int p100 = p(xy10 + z);
/* 152 */     int p010 = p(xy01 + z);
/* 153 */     int p110 = p(xy11 + z);
/* 154 */     int p001 = p(xy00 + z + 1);
/* 155 */     int p101 = p(xy10 + z + 1);
/* 156 */     int p011 = p(xy01 + z + 1);
/* 157 */     int p111 = p(xy11 + z + 1);
/* 159 */     int[] g000 = SimplexNoise.GRADIENT[p000 & 0xF];
/* 160 */     int[] g100 = SimplexNoise.GRADIENT[p100 & 0xF];
/* 161 */     int[] g010 = SimplexNoise.GRADIENT[p010 & 0xF];
/* 162 */     int[] g110 = SimplexNoise.GRADIENT[p110 & 0xF];
/* 163 */     int[] g001 = SimplexNoise.GRADIENT[p001 & 0xF];
/* 164 */     int[] g101 = SimplexNoise.GRADIENT[p101 & 0xF];
/* 165 */     int[] g011 = SimplexNoise.GRADIENT[p011 & 0xF];
/* 166 */     int[] g111 = SimplexNoise.GRADIENT[p111 & 0xF];
/* 168 */     double d000 = SimplexNoise.dot(g000, xr, yr, zr);
/* 169 */     double d100 = SimplexNoise.dot(g100, xr - 1.0D, yr, zr);
/* 170 */     double d010 = SimplexNoise.dot(g010, xr, yr - 1.0D, zr);
/* 171 */     double d110 = SimplexNoise.dot(g110, xr - 1.0D, yr - 1.0D, zr);
/* 172 */     double d001 = SimplexNoise.dot(g001, xr, yr, zr - 1.0D);
/* 173 */     double d101 = SimplexNoise.dot(g101, xr - 1.0D, yr, zr - 1.0D);
/* 174 */     double d011 = SimplexNoise.dot(g011, xr, yr - 1.0D, zr - 1.0D);
/* 175 */     double d111 = SimplexNoise.dot(g111, xr - 1.0D, yr - 1.0D, zr - 1.0D);
/* 177 */     double xAlpha = Mth.smoothstep(xr);
/* 178 */     double yAlpha = Mth.smoothstep(yr);
/* 179 */     double zAlpha = Mth.smoothstep(zr);
/* 181 */     double d1x = Mth.lerp3(xAlpha, yAlpha, zAlpha, g000[0], g100[0], g010[0], g110[0], g001[0], g101[0], g011[0], g111[0]);
/* 189 */     double d1y = Mth.lerp3(xAlpha, yAlpha, zAlpha, g000[1], g100[1], g010[1], g110[1], g001[1], g101[1], g011[1], g111[1]);
/* 197 */     double d1z = Mth.lerp3(xAlpha, yAlpha, zAlpha, g000[2], g100[2], g010[2], g110[2], g001[2], g101[2], g011[2], g111[2]);
/* 205 */     double d2x = Mth.lerp2(yAlpha, zAlpha, d100 - d000, d110 - d010, d101 - d001, d111 - d011);
/* 213 */     double d2y = Mth.lerp2(zAlpha, xAlpha, d010 - d000, d011 - d001, d110 - d100, d111 - d101);
/* 221 */     double d2z = Mth.lerp2(xAlpha, yAlpha, d001 - d000, d101 - d100, d011 - d010, d111 - d110);
/* 229 */     double xSD = Mth.smoothstepDerivative(xr);
/* 230 */     double ySD = Mth.smoothstepDerivative(yr);
/* 231 */     double zSD = Mth.smoothstepDerivative(zr);
/* 233 */     double dX = d1x + xSD * d2x;
/* 234 */     double dY = d1y + ySD * d2y;
/* 235 */     double dZ = d1z + zSD * d2z;
/* 237 */     derivativeOut[0] = derivativeOut[0] + dX;
/* 238 */     derivativeOut[1] = derivativeOut[1] + dY;
/* 239 */     derivativeOut[2] = derivativeOut[2] + dZ;
/* 242 */     return Mth.lerp3(xAlpha, yAlpha, zAlpha, d000, d100, d010, d110, d001, d101, d011, d111);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void parityConfigString(StringBuilder sb) {
/* 247 */     NoiseUtils.parityNoiseOctaveConfigString(sb, this.xo, this.yo, this.zo, this.p);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\synth\ImprovedNoise.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */