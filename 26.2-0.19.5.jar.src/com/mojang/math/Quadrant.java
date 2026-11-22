/*    */ package com.mojang.math;
/*    */ 
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public enum Quadrant {
/*  9 */   R0(0, OctahedralGroup.IDENTITY, OctahedralGroup.IDENTITY, OctahedralGroup.IDENTITY),
/* 10 */   R90(1, OctahedralGroup.BLOCK_ROT_X_90, OctahedralGroup.BLOCK_ROT_Y_90, OctahedralGroup.BLOCK_ROT_Z_90),
/* 11 */   R180(2, OctahedralGroup.BLOCK_ROT_X_180, OctahedralGroup.BLOCK_ROT_Y_180, OctahedralGroup.BLOCK_ROT_Z_180),
/* 12 */   R270(3, OctahedralGroup.BLOCK_ROT_X_270, OctahedralGroup.BLOCK_ROT_Y_270, OctahedralGroup.BLOCK_ROT_Z_270);
/*    */   
/*    */   public static final Codec<Quadrant> CODEC;
/*    */   
/*    */   public final int shift;
/*    */   
/*    */   public final OctahedralGroup rotationX;
/*    */   
/*    */   public final OctahedralGroup rotationY;
/*    */   
/*    */   public final OctahedralGroup rotationZ;
/*    */   
/*    */   static {
/* 15 */     CODEC = Codec.INT.comapFlatMap(degrees -> {
/*    */           switch (Mth.positiveModulo(degrees.intValue(), 360)) {
/*    */             case 0:
/*    */             
/*    */             case 90:
/*    */             
/*    */             case 180:
/*    */             
/*    */             case 270:
/*    */             
/*    */           } 
/*    */           return DataResult.error(());
/*    */         }quadrant -> {
/*    */           switch (quadrant.ordinal()) {
/*    */             default:
/*    */               throw new MatchException(null, null);
/*    */             case 0:
/*    */             
/*    */             case 1:
/*    */             
/*    */             case 2:
/*    */             
/*    */             case 3:
/*    */               break;
/*    */           } 
/*    */           return Integer.valueOf(270);
/*    */         });
/*    */   }
/*    */   
/*    */   Quadrant(int shift, OctahedralGroup rotationX, OctahedralGroup rotationY, OctahedralGroup rotationZ) {
/* 37 */     this.shift = shift;
/* 38 */     this.rotationX = rotationX;
/* 39 */     this.rotationY = rotationY;
/* 40 */     this.rotationZ = rotationZ;
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public static Quadrant parseJson(int degrees) {
/* 45 */     switch (Mth.positiveModulo(degrees, 360)) {
/*    */       case 0:
/*    */       
/*    */       case 90:
/*    */       
/*    */       case 180:
/*    */       
/*    */       case 270:
/*    */       
/*    */     } 
/* 51 */     throw new JsonParseException("Invalid rotation " + degrees + " found, only 0/90/180/270 allowed");
/*    */   }
/*    */   
/*    */   public static OctahedralGroup fromXYAngles(Quadrant xRotation, Quadrant yRotation) {
/* 56 */     return yRotation.rotationY.compose(xRotation.rotationX);
/*    */   }
/*    */   
/*    */   public static OctahedralGroup fromXYZAngles(Quadrant xRotation, Quadrant yRotation, Quadrant zRotation) {
/* 60 */     return zRotation.rotationZ.compose(yRotation.rotationY.compose(xRotation.rotationX));
/*    */   }
/*    */   
/*    */   public int rotateVertexIndex(int index) {
/* 64 */     return (index + this.shift) % 4;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\math\Quadrant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */