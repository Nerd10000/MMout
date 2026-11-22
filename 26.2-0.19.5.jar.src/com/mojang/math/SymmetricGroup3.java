/*     */ package com.mojang.math;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix3fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3i;
/*     */ 
/*     */ public enum SymmetricGroup3 {
/*  30 */   P123(0, 1, 2),
/*  31 */   P213(1, 0, 2),
/*  32 */   P132(0, 2, 1),
/*  33 */   P312(2, 0, 1),
/*  34 */   P231(1, 2, 0),
/*  35 */   P321(2, 1, 0);
/*     */   
/*     */   private final int p0;
/*     */   
/*     */   private final int p1;
/*     */   
/*     */   private final int p2;
/*     */   
/*     */   private final Matrix3fc transformation;
/*     */   
/*     */   private static final SymmetricGroup3[][] CAYLEY_TABLE;
/*     */   
/*     */   private static final SymmetricGroup3[] INVERSE_TABLE;
/*     */   
/*     */   SymmetricGroup3(int p0, int p1, int p2) {
/*  45 */     this.p0 = p0;
/*  46 */     this.p1 = p1;
/*  47 */     this.p2 = p2;
/*  48 */     this
/*     */       
/*  52 */       .transformation = (Matrix3fc)(new Matrix3f()).zero().set(permute(0), 0, 1.0F).set(permute(1), 1, 1.0F).set(permute(2), 2, 1.0F);
/*     */   }
/*     */   
/*     */   static {
/*  55 */     CAYLEY_TABLE = (SymmetricGroup3[][])Util.make(() -> {
/*     */           SymmetricGroup3[] values = values();
/*     */           SymmetricGroup3[][] table = new SymmetricGroup3[values.length][values.length];
/*     */           for (SymmetricGroup3 first : values) {
/*     */             for (SymmetricGroup3 second : values) {
/*     */               int p0 = first.permute(second.p0);
/*     */               int p1 = first.permute(second.p1);
/*     */               int p2 = first.permute(second.p2);
/*     */               SymmetricGroup3 result = Arrays.<SymmetricGroup3>stream(values).filter(()).findFirst().get();
/*     */               table[first.ordinal()][second.ordinal()] = result;
/*     */             } 
/*     */           } 
/*     */           return table;
/*     */         });
/*  73 */     INVERSE_TABLE = (SymmetricGroup3[])Util.make(() -> {
/*     */           SymmetricGroup3[] values = values();
/*     */           return (SymmetricGroup3[])Arrays.<SymmetricGroup3>stream(values).map(()).toArray(());
/*     */         });
/*     */   }
/*     */   
/*     */   public SymmetricGroup3 compose(SymmetricGroup3 that) {
/*  81 */     return CAYLEY_TABLE[ordinal()][that.ordinal()];
/*     */   }
/*     */   
/*     */   public SymmetricGroup3 inverse() {
/*  85 */     return INVERSE_TABLE[ordinal()];
/*     */   }
/*     */   
/*     */   public int permute(int i) {
/*  89 */     switch (i) {
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */     } 
/*  93 */     throw new IllegalArgumentException("Must be 0, 1 or 2, but got " + i);
/*     */   }
/*     */   
/*     */   public Direction.Axis permuteAxis(Direction.Axis axis) {
/*  98 */     return Direction.Axis.VALUES[permute(axis.ordinal())];
/*     */   }
/*     */   
/*     */   public Vector3f permuteVector(Vector3f v) {
/* 102 */     float v0 = v.get(this.p0);
/* 103 */     float v1 = v.get(this.p1);
/* 104 */     float v2 = v.get(this.p2);
/* 105 */     return v.set(v0, v1, v2);
/*     */   }
/*     */   
/*     */   public Vector3i permuteVector(Vector3i v) {
/* 109 */     int v0 = v.get(this.p0);
/* 110 */     int v1 = v.get(this.p1);
/* 111 */     int v2 = v.get(this.p2);
/* 112 */     return v.set(v0, v1, v2);
/*     */   }
/*     */   
/*     */   public Matrix3fc transformation() {
/* 116 */     return this.transformation;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\math\SymmetricGroup3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */