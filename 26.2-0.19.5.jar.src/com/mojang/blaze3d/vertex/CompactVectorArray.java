/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class CompactVectorArray {
/*    */   private static final int VECTOR_SIZE = 3;
/*    */   
/*    */   private static final int OFFSET_X = 0;
/*    */   
/*    */   private static final int OFFSET_Y = 1;
/*    */   
/*    */   private static final int OFFSET_Z = 2;
/*    */   
/*    */   private final float[] contents;
/*    */   
/*    */   public CompactVectorArray(int count) {
/* 16 */     this.contents = new float[3 * count];
/*    */   }
/*    */   
/*    */   public int size() {
/* 20 */     return this.contents.length / 3;
/*    */   }
/*    */   
/*    */   public void set(int index, Vector3fc v) {
/* 24 */     set(index, v.x(), v.y(), v.z());
/*    */   }
/*    */   
/*    */   public void set(int index, float x, float y, float z) {
/* 28 */     this.contents[3 * index + 0] = x;
/* 29 */     this.contents[3 * index + 1] = y;
/* 30 */     this.contents[3 * index + 2] = z;
/*    */   }
/*    */   
/*    */   public Vector3f get(int index, Vector3f output) {
/* 34 */     return output.set(this.contents[3 * index + 0], this.contents[3 * index + 1], this.contents[3 * index + 2]);
/*    */   }
/*    */   
/*    */   public float getX(int index) {
/* 42 */     return this.contents[3 * index + 0];
/*    */   }
/*    */   
/*    */   public float getY(int index) {
/* 46 */     return this.contents[3 * index + 1];
/*    */   }
/*    */   
/*    */   public float getZ(int index) {
/* 50 */     return this.contents[3 * index + 2];
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\CompactVectorArray.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */