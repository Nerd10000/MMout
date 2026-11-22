/*    */ package com.mojang.blaze3d;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.VertexSorting;
/*    */ import org.joml.Matrix4f;
/*    */ 
/*    */ public enum ProjectionType {
/*    */   PERSPECTIVE, ORTHOGRAPHIC;
/*    */   
/*    */   private final VertexSorting vertexSorting;
/*    */   
/*    */   private final LayeringTransform layeringTransform;
/*    */   
/*    */   static {
/*  7 */     PERSPECTIVE = new ProjectionType("PERSPECTIVE", 0, VertexSorting.DISTANCE_TO_ORIGIN, (matrix, bias) -> matrix.scale(1.0F - bias / 4096.0F));
/*  8 */     ORTHOGRAPHIC = new ProjectionType("ORTHOGRAPHIC", 1, VertexSorting.ORTHOGRAPHIC_Z, (matrix, bias) -> matrix.translate(0.0F, 0.0F, bias / 512.0F));
/*    */   }
/*    */   
/*    */   ProjectionType(VertexSorting vertexSorting, LayeringTransform layeringTransform) {
/* 15 */     this.vertexSorting = vertexSorting;
/* 16 */     this.layeringTransform = layeringTransform;
/*    */   }
/*    */   
/*    */   public VertexSorting vertexSorting() {
/* 20 */     return this.vertexSorting;
/*    */   }
/*    */   
/*    */   public void applyLayeringTransform(Matrix4f matrix, float bias) {
/* 24 */     this.layeringTransform.apply(matrix, bias);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   private static interface LayeringTransform {
/*    */     void apply(Matrix4f param1Matrix4f, float param1Float);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\ProjectionType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */