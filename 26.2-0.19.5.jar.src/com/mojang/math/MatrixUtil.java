/*     */ package com.mojang.math;
/*     */ 
/*     */ import org.joml.Math;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix3fc;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public class MatrixUtil {
/*  12 */   private static final float G = 3.0F + 2.0F * Math.sqrt(2.0F);
/*     */   
/*  14 */   private static final GivensParameters PI_4 = GivensParameters.fromPositiveAngle(0.7853982F);
/*     */   
/*     */   public static Matrix4f mulComponentWise(Matrix4f m, float factor) {
/*  20 */     return m.set(m
/*  21 */         .m00() * factor, m.m01() * factor, m.m02() * factor, m.m03() * factor, m
/*  22 */         .m10() * factor, m.m11() * factor, m.m12() * factor, m.m13() * factor, m
/*  23 */         .m20() * factor, m.m21() * factor, m.m22() * factor, m.m23() * factor, m
/*  24 */         .m30() * factor, m.m31() * factor, m.m32() * factor, m.m33() * factor);
/*     */   }
/*     */   
/*     */   private static GivensParameters approxGivensQuat(float a11, float a12, float a22) {
/*  30 */     float ch = 2.0F * (a11 - a22);
/*  31 */     float sh = a12;
/*  34 */     if (G * sh * sh < ch * ch)
/*  35 */       return GivensParameters.fromUnnormalized(sh, ch); 
/*  37 */     return PI_4;
/*     */   }
/*     */   
/*     */   private static GivensParameters qrGivensQuat(float a1, float a2) {
/*  42 */     float p = (float)Math.hypot(a1, a2);
/*  43 */     float sh = (p > 1.0E-6F) ? a2 : 0.0F;
/*  44 */     float ch = Math.abs(a1) + Math.max(p, 1.0E-6F);
/*  45 */     if (a1 < 0.0F) {
/*  46 */       float f = sh;
/*  47 */       sh = ch;
/*  48 */       ch = f;
/*     */     } 
/*  50 */     return GivensParameters.fromUnnormalized(sh, ch);
/*     */   }
/*     */   
/*     */   private static void similarityTransform(Matrix3f a, Matrix3f q) {
/*  55 */     a.mul((Matrix3fc)q);
/*  57 */     q.transpose();
/*  59 */     q.mul((Matrix3fc)a);
/*  61 */     a.set((Matrix3fc)q);
/*     */   }
/*     */   
/*     */   private static void stepJacobi(Matrix3f m, Matrix3f tmpMat, Quaternionf tmpQ, Quaternionf output) {
/*  66 */     if (m.m01 * m.m01 + m.m10 * m.m10 > 1.0E-6F) {
/*  67 */       GivensParameters p = approxGivensQuat(m.m00, 0.5F * (m.m01 + m.m10), m.m11);
/*  69 */       Quaternionf qt = p.aroundZ(tmpQ);
/*  70 */       output.mul((Quaternionfc)qt);
/*  72 */       p.aroundZ(tmpMat);
/*  73 */       similarityTransform(m, tmpMat);
/*     */     } 
/*  76 */     if (m.m02 * m.m02 + m.m20 * m.m20 > 1.0E-6F) {
/*  78 */       GivensParameters p = approxGivensQuat(m.m00, 0.5F * (m.m02 + m.m20), m.m22).inverse();
/*  80 */       Quaternionf qt = p.aroundY(tmpQ);
/*  81 */       output.mul((Quaternionfc)qt);
/*  83 */       p.aroundY(tmpMat);
/*  84 */       similarityTransform(m, tmpMat);
/*     */     } 
/*  87 */     if (m.m12 * m.m12 + m.m21 * m.m21 > 1.0E-6F) {
/*  88 */       GivensParameters p = approxGivensQuat(m.m11, 0.5F * (m.m12 + m.m21), m.m22);
/*  90 */       Quaternionf qt = p.aroundX(tmpQ);
/*  91 */       output.mul((Quaternionfc)qt);
/*  93 */       p.aroundX(tmpMat);
/*  94 */       similarityTransform(m, tmpMat);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void eigenvalueJacobi(Matrix3f inOut, int steps, Quaternionf result) {
/* 107 */     result.identity();
/* 108 */     Matrix3f scratchMat = new Matrix3f();
/* 109 */     Quaternionf scratchQ = new Quaternionf();
/* 111 */     for (int i = 0; i < steps; i++)
/* 112 */       stepJacobi(inOut, scratchMat, scratchQ, result); 
/* 115 */     result.normalize();
/*     */   }
/*     */   
/*     */   public static void svdDecompose(Matrix4fc input, Vector3f t, Quaternionf u, Vector3f s, Quaternionf v) {
/* 124 */     float scaleFactor = 1.0F / input.m33();
/* 125 */     svdDecompose((new Matrix3f(input)).scale(scaleFactor), u, s, v);
/* 126 */     input.getTranslation(t).mul(scaleFactor);
/*     */   }
/*     */   
/*     */   private static void svdDecompose(Matrix3f input, Quaternionf u, Vector3f s, Quaternionf v) {
/* 132 */     Matrix3f scratch = new Matrix3f((Matrix3fc)input);
/* 133 */     scratch.transpose();
/* 134 */     scratch.mul((Matrix3fc)input);
/* 136 */     eigenvalueJacobi(scratch, 5, v);
/* 142 */     float columnScaleSquare0 = scratch.m00;
/* 143 */     float columnScaleSquare1 = scratch.m11;
/* 147 */     boolean zeroColumn0 = (columnScaleSquare0 < 1.0E-6D);
/* 148 */     boolean zeroColumn1 = (columnScaleSquare1 < 1.0E-6D);
/* 152 */     Matrix3f u012s = input.rotate((Quaternionfc)v);
/* 182 */     u.identity();
/* 184 */     Quaternionf tmpQ = new Quaternionf();
/* 187 */     if (zeroColumn0) {
/* 188 */       p = qrGivensQuat(u012s.m11, -u012s.m10);
/*     */     } else {
/* 190 */       p = qrGivensQuat(u012s.m00, u012s.m01);
/*     */     } 
/* 193 */     Quaternionf qt0 = p.aroundZ(tmpQ);
/* 194 */     Matrix3f u12s = p.aroundZ(scratch);
/* 198 */     u.mul((Quaternionfc)qt0);
/* 199 */     u12s.transpose().mul((Matrix3fc)u012s);
/* 201 */     scratch = u012s;
/* 206 */     if (zeroColumn0) {
/* 207 */       p = qrGivensQuat(u12s.m22, -u12s.m20);
/*     */     } else {
/* 209 */       p = qrGivensQuat(u12s.m00, u12s.m02);
/*     */     } 
/* 212 */     GivensParameters p = p.inverse();
/* 214 */     Quaternionf qt1 = p.aroundY(tmpQ);
/* 216 */     Matrix3f u2s = p.aroundY(scratch);
/* 219 */     u.mul((Quaternionfc)qt1);
/* 220 */     u2s.transpose().mul((Matrix3fc)u12s);
/* 222 */     scratch = u12s;
/* 225 */     if (zeroColumn1) {
/* 226 */       p = qrGivensQuat(u2s.m22, -u2s.m21);
/*     */     } else {
/* 228 */       p = qrGivensQuat(u2s.m11, u2s.m12);
/*     */     } 
/* 231 */     Quaternionf qt2 = p.aroundX(tmpQ);
/* 232 */     Matrix3f sm = p.aroundX(scratch);
/* 235 */     u.mul((Quaternionfc)qt2);
/* 236 */     sm.transpose().mul((Matrix3fc)u2s);
/* 239 */     s.set(sm.m00, sm.m11, sm.m22);
/* 242 */     v.conjugate();
/*     */   }
/*     */   
/*     */   public static boolean checkPropertyRaw(Matrix4fc matrix, int property) {
/* 246 */     return ((matrix.properties() & property) != 0);
/*     */   }
/*     */   
/*     */   public static boolean checkProperty(Matrix4fc matrix, int property) {
/* 250 */     if (checkPropertyRaw(matrix, property))
/* 251 */       return true; 
/* 255 */     if (matrix instanceof Matrix4f) {
/* 255 */       Matrix4f mutableMatrix = (Matrix4f)matrix;
/* 258 */       int currentProperties = mutableMatrix.properties();
/* 259 */       mutableMatrix.determineProperties();
/* 260 */       mutableMatrix.assume(mutableMatrix.properties() | currentProperties);
/* 261 */       return checkPropertyRaw(matrix, property);
/*     */     } 
/* 264 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isIdentity(Matrix4fc matrix) {
/* 268 */     return checkProperty(matrix, 4);
/*     */   }
/*     */   
/*     */   public static boolean isPureTranslation(Matrix4fc matrix) {
/* 272 */     return checkProperty(matrix, 8);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\math\MatrixUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */