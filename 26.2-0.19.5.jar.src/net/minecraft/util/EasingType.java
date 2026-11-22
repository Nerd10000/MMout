/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ public interface EasingType {
/*  12 */   public static final ExtraCodecs.LateBoundIdMapper<String, EasingType> SIMPLE_REGISTRY = new ExtraCodecs.LateBoundIdMapper<>();
/*     */   
/*     */   public static final Codec<EasingType> CODEC;
/*     */   
/*     */   static {
/*  14 */     CODEC = Codec.either(SIMPLE_REGISTRY.codec((Codec<String>)Codec.STRING), CubicBezier.CODEC).xmap(Either::unwrap, easing -> {
/*     */           CubicBezier bezier = (CubicBezier)easing;
/*     */           return (easing instanceof CubicBezier) ? Either.right(bezier) : Either.left(easing);
/*     */         });
/*     */   }
/*     */   
/*  19 */   public static final EasingType CONSTANT = registerSimple("constant", x -> 0.0F);
/*     */   
/*     */   public static final EasingType LINEAR;
/*     */   
/*     */   static {
/*  20 */     LINEAR = registerSimple("linear", x -> x);
/*     */   }
/*     */   
/*  22 */   public static final EasingType IN_BACK = registerSimple("in_back", Ease::inBack);
/*     */   
/*  23 */   public static final EasingType IN_BOUNCE = registerSimple("in_bounce", Ease::inBounce);
/*     */   
/*  24 */   public static final EasingType IN_CIRC = registerSimple("in_circ", Ease::inCirc);
/*     */   
/*  25 */   public static final EasingType IN_CUBIC = registerSimple("in_cubic", Ease::inCubic);
/*     */   
/*  26 */   public static final EasingType IN_ELASTIC = registerSimple("in_elastic", Ease::inElastic);
/*     */   
/*  27 */   public static final EasingType IN_EXPO = registerSimple("in_expo", Ease::inExpo);
/*     */   
/*  28 */   public static final EasingType IN_QUAD = registerSimple("in_quad", Ease::inQuad);
/*     */   
/*  29 */   public static final EasingType IN_QUART = registerSimple("in_quart", Ease::inQuart);
/*     */   
/*  30 */   public static final EasingType IN_QUINT = registerSimple("in_quint", Ease::inQuint);
/*     */   
/*  31 */   public static final EasingType IN_SINE = registerSimple("in_sine", Ease::inSine);
/*     */   
/*  33 */   public static final EasingType IN_OUT_BACK = registerSimple("in_out_back", Ease::inOutBack);
/*     */   
/*  34 */   public static final EasingType IN_OUT_BOUNCE = registerSimple("in_out_bounce", Ease::inOutBounce);
/*     */   
/*  35 */   public static final EasingType IN_OUT_CIRC = registerSimple("in_out_circ", Ease::inOutCirc);
/*     */   
/*  36 */   public static final EasingType IN_OUT_CUBIC = registerSimple("in_out_cubic", Ease::inOutCubic);
/*     */   
/*  37 */   public static final EasingType IN_OUT_ELASTIC = registerSimple("in_out_elastic", Ease::inOutElastic);
/*     */   
/*  38 */   public static final EasingType IN_OUT_EXPO = registerSimple("in_out_expo", Ease::inOutExpo);
/*     */   
/*  39 */   public static final EasingType IN_OUT_QUAD = registerSimple("in_out_quad", Ease::inOutQuad);
/*     */   
/*  40 */   public static final EasingType IN_OUT_QUART = registerSimple("in_out_quart", Ease::inOutQuart);
/*     */   
/*  41 */   public static final EasingType IN_OUT_QUINT = registerSimple("in_out_quint", Ease::inOutQuint);
/*     */   
/*  42 */   public static final EasingType IN_OUT_SINE = registerSimple("in_out_sine", Ease::inOutSine);
/*     */   
/*  44 */   public static final EasingType OUT_BACK = registerSimple("out_back", Ease::outBack);
/*     */   
/*  45 */   public static final EasingType OUT_BOUNCE = registerSimple("out_bounce", Ease::outBounce);
/*     */   
/*  46 */   public static final EasingType OUT_CIRC = registerSimple("out_circ", Ease::outCirc);
/*     */   
/*  47 */   public static final EasingType OUT_CUBIC = registerSimple("out_cubic", Ease::outCubic);
/*     */   
/*  48 */   public static final EasingType OUT_ELASTIC = registerSimple("out_elastic", Ease::outElastic);
/*     */   
/*  49 */   public static final EasingType OUT_EXPO = registerSimple("out_expo", Ease::outExpo);
/*     */   
/*  50 */   public static final EasingType OUT_QUAD = registerSimple("out_quad", Ease::outQuad);
/*     */   
/*  51 */   public static final EasingType OUT_QUART = registerSimple("out_quart", Ease::outQuart);
/*     */   
/*  52 */   public static final EasingType OUT_QUINT = registerSimple("out_quint", Ease::outQuint);
/*     */   
/*  53 */   public static final EasingType OUT_SINE = registerSimple("out_sine", Ease::outSine);
/*     */   
/*     */   static EasingType registerSimple(String id, EasingType easing) {
/*  56 */     SIMPLE_REGISTRY.put(id, easing);
/*  57 */     return easing;
/*     */   }
/*     */   
/*     */   static EasingType cubicBezier(float x1, float y1, float x2, float y2) {
/*  61 */     return new CubicBezier(new CubicBezierControls(x1, y1, x2, y2));
/*     */   }
/*     */   
/*     */   static EasingType symmetricCubicBezier(float x1, float y1) {
/*  65 */     return cubicBezier(x1, y1, 1.0F - x1, 1.0F - y1);
/*     */   }
/*     */   
/*     */   float apply(float paramFloat);
/*     */   
/*     */   public static final class CubicBezier implements EasingType {
/*     */     public static final Codec<CubicBezier> CODEC;
/*     */     
/*     */     private static final int NEWTON_RAPHSON_ITERATIONS = 4;
/*     */     
/*     */     private static final float MAX_STEP = 0.25F;
/*     */     
/*     */     private final EasingType.CubicBezierControls controls;
/*     */     
/*     */     private final CubicCurve xCurve;
/*     */     
/*     */     private final CubicCurve yCurve;
/*     */     
/*     */     static {
/*  71 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EasingType.CubicBezierControls.CODEC.fieldOf("cubic_bezier").forGetter(())).apply((Applicative)i, CubicBezier::new));
/*     */     }
/*     */     
/*     */     public CubicBezier(EasingType.CubicBezierControls controls) {
/*  84 */       this.controls = controls;
/*  85 */       this.xCurve = curveFromControls(controls.x1, controls.x2);
/*  86 */       this.yCurve = curveFromControls(controls.y1, controls.y2);
/*     */     }
/*     */     
/*     */     private static CubicCurve curveFromControls(float v1, float v2) {
/*  92 */       return new CubicCurve(3.0F * v1 - 3.0F * v2 + 1.0F, -6.0F * v1 + 3.0F * v2, 3.0F * v1);
/*     */     }
/*     */     
/*     */     public float apply(float x) {
/* 101 */       return this.yCurve.sample(solveT(x));
/*     */     }
/*     */     
/*     */     private float solveT(float x) {
/* 106 */       float t = x;
/* 107 */       for (int i = 0; i < 4; i++) {
/* 108 */         float error = this.xCurve.sample(t) - x;
/* 109 */         if (Math.abs(error) < 1.0E-5F)
/* 110 */           return t; 
/* 113 */         float gradient = this.xCurve.sampleGradient(t);
/* 114 */         if (gradient < 1.0E-5F)
/*     */           break; 
/* 117 */         t -= Mth.clamp(error / gradient, -0.25F, 0.25F);
/*     */       } 
/* 121 */       return solveTBisect(x, t);
/*     */     }
/*     */     
/*     */     private float solveTBisect(float x, float initialT) {
/* 126 */       float t0 = 0.0F;
/* 127 */       float t1 = 1.0F;
/* 128 */       float t = initialT;
/* 129 */       while (t0 < t1) {
/* 130 */         float error = this.xCurve.sample(t) - x;
/* 131 */         if (Math.abs(error) < 1.0E-5F)
/* 132 */           return t; 
/* 134 */         if (error < 0.0F) {
/* 135 */           t0 = t;
/*     */         } else {
/* 137 */           t1 = t;
/*     */         } 
/* 139 */         t = (t1 + t0) / 2.0F;
/*     */       } 
/* 141 */       return t;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/* 146 */       if (obj instanceof CubicBezier) {
/* 146 */         CubicBezier bezier = (CubicBezier)obj;
/* 146 */         if (this.controls.equals(bezier.controls));
/*     */       } 
/* 146 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 151 */       return this.controls.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 156 */       return "CubicBezier(" + this.controls.x1 + ", " + this.controls.y1 + ", " + this.controls.x2 + ", " + this.controls.y2 + ")";
/*     */     }
/*     */     
/*     */     private static final class CubicCurve extends Record {
/*     */       private final float a;
/*     */       
/*     */       private final float b;
/*     */       
/*     */       private final float c;
/*     */       
/*     */       private CubicCurve(float a, float b, float c) {
/* 159 */         this.a = a;
/* 159 */         this.b = b;
/* 159 */         this.c = c;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #159	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #159	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #159	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public float a() {
/* 159 */         return this.a;
/*     */       }
/*     */       
/*     */       public float b() {
/* 159 */         return this.b;
/*     */       }
/*     */       
/*     */       public float c() {
/* 159 */         return this.c;
/*     */       }
/*     */       
/*     */       public float sample(float t) {
/* 161 */         return ((this.a * t + this.b) * t + this.c) * t;
/*     */       }
/*     */       
/*     */       public float sampleGradient(float t) {
/* 165 */         return (3.0F * this.a * t + 2.0F * this.b) * t + this.c;
/*     */       }
/*     */     }
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
/*     */       this.a = a;
/*     */       this.b = b;
/*     */       this.c = c;
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
/*     */       return this.a;
/*     */     }
/*     */     
/*     */     public float b() {
/*     */       return this.b;
/*     */     }
/*     */     
/*     */     public float c() {
/*     */       return this.c;
/*     */     }
/*     */     
/*     */     public float sample(float t) {
/*     */       return ((this.a * t + this.b) * t + this.c) * t;
/*     */     }
/*     */     
/*     */     public float sampleGradient(float t) {
/* 165 */       return (3.0F * this.a * t + 2.0F * this.b) * t + this.c;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class CubicBezierControls extends Record {
/*     */     private final float x1;
/*     */     
/*     */     private final float y1;
/*     */     
/*     */     private final float x2;
/*     */     
/*     */     private final float y2;
/*     */     
/*     */     public static final Codec<CubicBezierControls> CODEC;
/*     */     
/*     */     public CubicBezierControls(float x1, float y1, float x2, float y2) {
/* 170 */       this.x1 = x1;
/* 170 */       this.y1 = y1;
/* 170 */       this.x2 = x2;
/* 170 */       this.y2 = y2;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/EasingType$CubicBezierControls;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #170	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezierControls;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/EasingType$CubicBezierControls;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #170	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezierControls;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/EasingType$CubicBezierControls;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #170	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/EasingType$CubicBezierControls;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public float x1() {
/* 170 */       return this.x1;
/*     */     }
/*     */     
/*     */     public float y1() {
/* 170 */       return this.y1;
/*     */     }
/*     */     
/*     */     public float x2() {
/* 170 */       return this.x2;
/*     */     }
/*     */     
/*     */     public float y2() {
/* 170 */       return this.y2;
/*     */     }
/*     */     
/*     */     static {
/* 177 */       CODEC = Codec.FLOAT.listOf(4, 4).xmap(floats -> new CubicBezierControls(((Float)floats.get(0)).floatValue(), ((Float)floats.get(1)).floatValue(), ((Float)floats.get(2)).floatValue(), ((Float)floats.get(3)).floatValue()), controls -> List.of(Float.valueOf(controls.x1), Float.valueOf(controls.y1), Float.valueOf(controls.x2), Float.valueOf(controls.y2))).validate(CubicBezierControls::validate);
/*     */     }
/*     */     
/*     */     private DataResult<CubicBezierControls> validate() {
/* 180 */       if (this.x1 < 0.0F || this.x1 > 1.0F)
/* 181 */         return DataResult.error(() -> "x1 must be in range [0; 1]"); 
/* 182 */       if (this.x2 < 0.0F || this.x2 > 1.0F)
/* 183 */         return DataResult.error(() -> "x2 must be in range [0; 1]"); 
/* 185 */       return DataResult.success(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\EasingType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */