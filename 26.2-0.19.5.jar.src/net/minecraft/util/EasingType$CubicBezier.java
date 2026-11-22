/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ public final class CubicBezier implements EasingType {
/*     */   public static final Codec<CubicBezier> CODEC;
/*     */   
/*     */   private static final int NEWTON_RAPHSON_ITERATIONS = 4;
/*     */   
/*     */   private static final float MAX_STEP = 0.25F;
/*     */   
/*     */   private final EasingType.CubicBezierControls controls;
/*     */   
/*     */   private final CubicCurve xCurve;
/*     */   
/*     */   private final CubicCurve yCurve;
/*     */   
/*     */   static {
/*  71 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EasingType.CubicBezierControls.CODEC.fieldOf("cubic_bezier").forGetter(())).apply((Applicative)i, CubicBezier::new));
/*     */   }
/*     */   
/*     */   public CubicBezier(EasingType.CubicBezierControls controls) {
/*  84 */     this.controls = controls;
/*  85 */     this.xCurve = curveFromControls(controls.x1, controls.x2);
/*  86 */     this.yCurve = curveFromControls(controls.y1, controls.y2);
/*     */   }
/*     */   
/*     */   private static CubicCurve curveFromControls(float v1, float v2) {
/*  92 */     return new CubicCurve(3.0F * v1 - 3.0F * v2 + 1.0F, -6.0F * v1 + 3.0F * v2, 3.0F * v1);
/*     */   }
/*     */   
/*     */   public float apply(float x) {
/* 101 */     return this.yCurve.sample(solveT(x));
/*     */   }
/*     */   
/*     */   private float solveT(float x) {
/* 106 */     float t = x;
/* 107 */     for (int i = 0; i < 4; i++) {
/* 108 */       float error = this.xCurve.sample(t) - x;
/* 109 */       if (Math.abs(error) < 1.0E-5F)
/* 110 */         return t; 
/* 113 */       float gradient = this.xCurve.sampleGradient(t);
/* 114 */       if (gradient < 1.0E-5F)
/*     */         break; 
/* 117 */       t -= Mth.clamp(error / gradient, -0.25F, 0.25F);
/*     */     } 
/* 121 */     return solveTBisect(x, t);
/*     */   }
/*     */   
/*     */   private float solveTBisect(float x, float initialT) {
/* 126 */     float t0 = 0.0F;
/* 127 */     float t1 = 1.0F;
/* 128 */     float t = initialT;
/* 129 */     while (t0 < t1) {
/* 130 */       float error = this.xCurve.sample(t) - x;
/* 131 */       if (Math.abs(error) < 1.0E-5F)
/* 132 */         return t; 
/* 134 */       if (error < 0.0F) {
/* 135 */         t0 = t;
/*     */       } else {
/* 137 */         t1 = t;
/*     */       } 
/* 139 */       t = (t1 + t0) / 2.0F;
/*     */     } 
/* 141 */     return t;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 146 */     if (obj instanceof CubicBezier) {
/* 146 */       CubicBezier bezier = (CubicBezier)obj;
/* 146 */       if (this.controls.equals(bezier.controls));
/*     */     } 
/* 146 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 151 */     return this.controls.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 156 */     return "CubicBezier(" + this.controls.x1 + ", " + this.controls.y1 + ", " + this.controls.x2 + ", " + this.controls.y2 + ")";
/*     */   }
/*     */   
/*     */   private static final class CubicCurve extends Record {
/*     */     private final float a;
/*     */     
/*     */     private final float b;
/*     */     
/*     */     private final float c;
/*     */     
/*     */     private CubicCurve(float a, float b, float c) {
/* 159 */       this.a = a;
/* 159 */       this.b = b;
/* 159 */       this.c = c;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #159	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #159	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #159	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public float a() {
/* 159 */       return this.a;
/*     */     }
/*     */     
/*     */     public float b() {
/* 159 */       return this.b;
/*     */     }
/*     */     
/*     */     public float c() {
/* 159 */       return this.c;
/*     */     }
/*     */     
/*     */     public float sample(float t) {
/* 161 */       return ((this.a * t + this.b) * t + this.c) * t;
/*     */     }
/*     */     
/*     */     public float sampleGradient(float t) {
/* 165 */       return (3.0F * this.a * t + 2.0F * this.b) * t + this.c;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\EasingType$CubicBezier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */