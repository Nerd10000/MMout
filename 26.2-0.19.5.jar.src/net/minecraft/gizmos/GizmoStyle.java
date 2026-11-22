/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ import net.minecraft.util.ARGB;
/*    */ 
/*    */ public final class GizmoStyle extends Record {
/*    */   private final int stroke;
/*    */   
/*    */   private final float strokeWidth;
/*    */   
/*    */   private final int fill;
/*    */   
/*    */   private static final float DEFAULT_WIDTH = 2.5F;
/*    */   
/*    */   public GizmoStyle(int stroke, float strokeWidth, int fill) {
/*  5 */     this.stroke = stroke;
/*  5 */     this.strokeWidth = strokeWidth;
/*  5 */     this.fill = fill;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/GizmoStyle;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/GizmoStyle;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/GizmoStyle;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/GizmoStyle;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/GizmoStyle;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/GizmoStyle;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int stroke() {
/*  5 */     return this.stroke;
/*    */   }
/*    */   
/*    */   public float strokeWidth() {
/*  5 */     return this.strokeWidth;
/*    */   }
/*    */   
/*    */   public int fill() {
/*  5 */     return this.fill;
/*    */   }
/*    */   
/*    */   public static GizmoStyle stroke(int argb) {
/*  9 */     return new GizmoStyle(argb, 2.5F, 0);
/*    */   }
/*    */   
/*    */   public static GizmoStyle stroke(int argb, float width) {
/* 13 */     return new GizmoStyle(argb, width, 0);
/*    */   }
/*    */   
/*    */   public static GizmoStyle fill(int argb) {
/* 17 */     return new GizmoStyle(0, 0.0F, argb);
/*    */   }
/*    */   
/*    */   public static GizmoStyle strokeAndFill(int stroke, float strokeWidth, int fill) {
/* 21 */     return new GizmoStyle(stroke, strokeWidth, fill);
/*    */   }
/*    */   
/*    */   public boolean hasFill() {
/* 25 */     return (this.fill != 0);
/*    */   }
/*    */   
/*    */   public boolean hasStroke() {
/* 29 */     return (this.stroke != 0 && this.strokeWidth > 0.0F);
/*    */   }
/*    */   
/*    */   public int multipliedStroke(float alphaMultiplier) {
/* 33 */     return ARGB.multiplyAlpha(this.stroke, alphaMultiplier);
/*    */   }
/*    */   
/*    */   public int multipliedFill(float alphaMultiplier) {
/* 37 */     return ARGB.multiplyAlpha(this.fill, alphaMultiplier);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gizmos\GizmoStyle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */