/*     */ package com.mojang.math;
/*     */ 
/*     */ import org.joml.Math;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Quaternionf;
/*     */ 
/*     */ public final class GivensParameters extends Record {
/*     */   private final float sinHalf;
/*     */   
/*     */   private final float cosHalf;
/*     */   
/*     */   public GivensParameters(float sinHalf, float cosHalf) {
/*  12 */     this.sinHalf = sinHalf;
/*  12 */     this.cosHalf = cosHalf;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/math/GivensParameters;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/math/GivensParameters;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/math/GivensParameters;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/math/GivensParameters;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/math/GivensParameters;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/math/GivensParameters;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float sinHalf() {
/*  12 */     return this.sinHalf;
/*     */   }
/*     */   
/*     */   public float cosHalf() {
/*  12 */     return this.cosHalf;
/*     */   }
/*     */   
/*     */   public static GivensParameters fromUnnormalized(float sinHalf, float cosHalf) {
/*  14 */     float w = Math.invsqrt(sinHalf * sinHalf + cosHalf * cosHalf);
/*  15 */     return new GivensParameters(w * sinHalf, w * cosHalf);
/*     */   }
/*     */   
/*     */   public static GivensParameters fromPositiveAngle(float angle) {
/*  22 */     float sin = Math.sin(angle / 2.0F);
/*  23 */     float cos = Math.cosFromSin(sin, angle / 2.0F);
/*  24 */     return new GivensParameters(sin, cos);
/*     */   }
/*     */   
/*     */   public GivensParameters inverse() {
/*  28 */     return new GivensParameters(-this.sinHalf, this.cosHalf);
/*     */   }
/*     */   
/*     */   public Quaternionf aroundX(Quaternionf input) {
/*  32 */     return input.set(this.sinHalf, 0.0F, 0.0F, this.cosHalf);
/*     */   }
/*     */   
/*     */   public Quaternionf aroundY(Quaternionf input) {
/*  36 */     return input.set(0.0F, this.sinHalf, 0.0F, this.cosHalf);
/*     */   }
/*     */   
/*     */   public Quaternionf aroundZ(Quaternionf input) {
/*  40 */     return input.set(0.0F, 0.0F, this.sinHalf, this.cosHalf);
/*     */   }
/*     */   
/*     */   public float cos() {
/*  45 */     return this.cosHalf * this.cosHalf - this.sinHalf * this.sinHalf;
/*     */   }
/*     */   
/*     */   public float sin() {
/*  50 */     return 2.0F * this.sinHalf * this.cosHalf;
/*     */   }
/*     */   
/*     */   public Matrix3f aroundX(Matrix3f input) {
/*  55 */     input.m01 = 0.0F;
/*  56 */     input.m02 = 0.0F;
/*  57 */     input.m10 = 0.0F;
/*  58 */     input.m20 = 0.0F;
/*  60 */     float c = cos();
/*  61 */     float s = sin();
/*  63 */     input.m11 = c;
/*  64 */     input.m22 = c;
/*  66 */     input.m12 = s;
/*  67 */     input.m21 = -s;
/*  69 */     input.m00 = 1.0F;
/*  70 */     return input;
/*     */   }
/*     */   
/*     */   public Matrix3f aroundY(Matrix3f input) {
/*  75 */     input.m01 = 0.0F;
/*  76 */     input.m10 = 0.0F;
/*  77 */     input.m12 = 0.0F;
/*  78 */     input.m21 = 0.0F;
/*  80 */     float c = cos();
/*  81 */     float s = sin();
/*  83 */     input.m00 = c;
/*  84 */     input.m22 = c;
/*  86 */     input.m02 = -s;
/*  87 */     input.m20 = s;
/*  89 */     input.m11 = 1.0F;
/*  90 */     return input;
/*     */   }
/*     */   
/*     */   public Matrix3f aroundZ(Matrix3f input) {
/*  95 */     input.m02 = 0.0F;
/*  96 */     input.m12 = 0.0F;
/*  97 */     input.m20 = 0.0F;
/*  98 */     input.m21 = 0.0F;
/* 100 */     float c = cos();
/* 101 */     float s = sin();
/* 103 */     input.m00 = c;
/* 104 */     input.m11 = c;
/* 106 */     input.m01 = s;
/* 107 */     input.m10 = -s;
/* 109 */     input.m22 = 1.0F;
/* 110 */     return input;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\math\GivensParameters.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */