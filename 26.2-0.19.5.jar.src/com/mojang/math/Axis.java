/*    */ package com.mojang.math;
/*    */ 
/*    */ import org.joml.Quaternionf;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface Axis {
/*    */   public static final Axis XN;
/*    */   
/*    */   public static final Axis XP;
/*    */   
/*    */   public static final Axis YN;
/*    */   
/*    */   public static final Axis YP;
/*    */   
/*    */   public static final Axis ZN;
/*    */   
/*    */   public static final Axis ZP;
/*    */   
/*    */   static {
/*  9 */     XN = (angle -> (new Quaternionf()).rotationX(-angle));
/* 10 */     XP = (angle -> (new Quaternionf()).rotationX(angle));
/* 11 */     YN = (angle -> (new Quaternionf()).rotationY(-angle));
/* 12 */     YP = (angle -> (new Quaternionf()).rotationY(angle));
/* 13 */     ZN = (angle -> (new Quaternionf()).rotationZ(-angle));
/* 14 */     ZP = (angle -> (new Quaternionf()).rotationZ(angle));
/*    */   }
/*    */   
/*    */   static Axis of(Vector3f vector) {
/* 17 */     return angle -> (new Quaternionf()).rotationAxis(angle, (Vector3fc)vector);
/*    */   }
/*    */   
/*    */   default Quaternionf rotationDegrees(float angle) {
/* 23 */     return rotation(angle * 0.017453292F);
/*    */   }
/*    */   
/*    */   Quaternionf rotation(float paramFloat);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\math\Axis.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */