/*    */ package com.mojang.blaze3d;
/*    */ 
/*    */ public enum PrimitiveTopology {
/*  4 */   LINES(2, 2, false),
/*  5 */   DEBUG_LINES(2, 2, false),
/*  6 */   DEBUG_LINE_STRIP(2, 1, true),
/*  7 */   POINTS(1, 1, false),
/*  8 */   TRIANGLES(3, 3, false),
/*  9 */   TRIANGLE_STRIP(3, 1, true),
/* 10 */   TRIANGLE_FAN(3, 1, true),
/* 11 */   QUADS(4, 4, false);
/*    */   
/*    */   public final int primitiveLength;
/*    */   
/*    */   public final int primitiveStride;
/*    */   
/*    */   public final boolean connectedPrimitives;
/*    */   
/*    */   PrimitiveTopology(int primitiveLength, int primitiveStride, boolean connectedPrimitives) {
/* 18 */     this.primitiveLength = primitiveLength;
/* 19 */     this.primitiveStride = primitiveStride;
/* 20 */     this.connectedPrimitives = connectedPrimitives;
/*    */   }
/*    */   
/*    */   public int indexCount(int vertexCount) {
/* 24 */     switch (ordinal()) {
/*    */       case 1:
/*    */       case 2:
/*    */       case 3:
/*    */       case 4:
/*    */       case 5:
/*    */       case 6:
/*    */       
/*    */       case 0:
/*    */       case 7:
/*    */       
/*    */     } 
/* 24 */     int indexCount = 
/*    */       
/* 27 */       0;
/* 29 */     return indexCount;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\PrimitiveTopology.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */