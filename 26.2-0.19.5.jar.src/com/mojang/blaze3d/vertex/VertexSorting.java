/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ import com.google.common.primitives.Floats;
/*    */ import it.unimi.dsi.fastutil.ints.IntArrays;
/*    */ import java.util.Objects;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public interface VertexSorting {
/*  9 */   public static final VertexSorting DISTANCE_TO_ORIGIN = byDistance(0.0F, 0.0F, 0.0F);
/*    */   
/*    */   public static final VertexSorting ORTHOGRAPHIC_Z;
/*    */   
/*    */   static {
/* 10 */     ORTHOGRAPHIC_Z = byDistance(point -> -point.z());
/*    */   }
/*    */   
/*    */   static VertexSorting byDistance(float x, float y, float z) {
/* 13 */     return byDistance((Vector3fc)new Vector3f(x, y, z));
/*    */   }
/*    */   
/*    */   static VertexSorting byDistance(Vector3fc origin) {
/* 17 */     Objects.requireNonNull(origin);
/* 17 */     return byDistance(origin::distanceSquared);
/*    */   }
/*    */   
/*    */   static VertexSorting byDistance(DistanceFunction function) {
/* 21 */     return values -> {
/*    */         Vector3f scratch = new Vector3f();
/*    */         float[] keys = new float[values.size()];
/*    */         int[] indices = new int[values.size()];
/*    */         for (int i = 0; i < values.size(); i++) {
/*    */           keys[i] = function.apply(values.get(i, scratch));
/*    */           indices[i] = i;
/*    */         } 
/*    */         IntArrays.mergeSort(indices, ());
/*    */         return indices;
/*    */       };
/*    */   }
/*    */   
/*    */   int[] sort(CompactVectorArray paramCompactVectorArray);
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface DistanceFunction {
/*    */     float apply(Vector3f param1Vector3f);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\VertexSorting.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */