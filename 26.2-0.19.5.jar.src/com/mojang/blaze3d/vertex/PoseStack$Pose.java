/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.math.MatrixUtil;
/*     */ import com.mojang.math.Transformation;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix3fc;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public final class Pose {
/*  89 */   private final Matrix4f pose = new Matrix4f();
/*     */   
/*  90 */   private final Matrix3f normal = new Matrix3f();
/*     */   
/*     */   private boolean trustedNormals = true;
/*     */   
/*     */   private void computeNormalMatrix() {
/*  97 */     this.normal.set((Matrix4fc)this.pose).invert().transpose();
/*  98 */     this.trustedNormals = false;
/*     */   }
/*     */   
/*     */   public void set(Pose pose) {
/* 102 */     this.pose.set((Matrix4fc)pose.pose);
/* 103 */     this.normal.set((Matrix3fc)pose.normal);
/* 104 */     this.trustedNormals = pose.trustedNormals;
/*     */   }
/*     */   
/*     */   public Matrix4f pose() {
/* 108 */     return this.pose;
/*     */   }
/*     */   
/*     */   public Matrix3f normal() {
/* 114 */     return this.normal;
/*     */   }
/*     */   
/*     */   public Vector3f transformNormal(Vector3fc normal, Vector3f destination) {
/* 118 */     return transformNormal(normal.x(), normal.y(), normal.z(), destination);
/*     */   }
/*     */   
/*     */   public Vector3f transformNormal(float x, float y, float z, Vector3f destination) {
/* 122 */     Vector3f result = this.normal.transform(x, y, z, destination);
/* 123 */     return this.trustedNormals ? result : result.normalize();
/*     */   }
/*     */   
/*     */   public Matrix4f translate(float xo, float yo, float zo) {
/* 127 */     return this.pose.translate(xo, yo, zo);
/*     */   }
/*     */   
/*     */   public void scale(float xScale, float yScale, float zScale) {
/* 131 */     this.pose.scale(xScale, yScale, zScale);
/* 134 */     if (Math.abs(xScale) == Math.abs(yScale) && Math.abs(yScale) == Math.abs(zScale)) {
/* 135 */       if (xScale < 0.0F || yScale < 0.0F || zScale < 0.0F)
/* 136 */         this.normal.scale(Math.signum(xScale), Math.signum(yScale), Math.signum(zScale)); 
/*     */       return;
/*     */     } 
/* 141 */     this.normal.scale(1.0F / xScale, 1.0F / yScale, 1.0F / zScale);
/* 142 */     this.trustedNormals = false;
/*     */   }
/*     */   
/*     */   public void rotate(Quaternionfc by) {
/* 146 */     this.pose.rotate(by);
/* 147 */     this.normal.rotate(by);
/*     */   }
/*     */   
/*     */   public void rotateAround(Quaternionfc rotation, float pivotX, float pivotY, float pivotZ) {
/* 151 */     this.pose.rotateAround(rotation, pivotX, pivotY, pivotZ);
/* 152 */     this.normal.rotate(rotation);
/*     */   }
/*     */   
/*     */   public void setIdentity() {
/* 156 */     this.pose.identity();
/* 157 */     this.normal.identity();
/* 158 */     this.trustedNormals = true;
/*     */   }
/*     */   
/*     */   public void mulPose(Matrix4fc matrix) {
/* 162 */     this.pose.mul(matrix);
/* 163 */     if (!MatrixUtil.isPureTranslation(matrix))
/* 165 */       if (MatrixUtil.checkPropertyRaw(matrix, 16)) {
/* 167 */         this.normal.mul((Matrix3fc)new Matrix3f(matrix));
/*     */       } else {
/* 169 */         computeNormalMatrix();
/*     */       }  
/*     */   }
/*     */   
/*     */   public void mulPose(Transformation transformation) {
/* 175 */     mulPose(transformation.getMatrix());
/*     */   }
/*     */   
/*     */   public Pose copy() {
/* 179 */     Pose pose = new Pose();
/* 180 */     pose.set(this);
/* 181 */     return pose;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\PoseStack$Pose.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */