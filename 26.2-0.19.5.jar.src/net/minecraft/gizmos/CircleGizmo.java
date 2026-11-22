/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class CircleGizmo extends Record implements Gizmo {
/*    */   private final Vec3 pos;
/*    */   
/*    */   private final float radius;
/*    */   
/*    */   private final GizmoStyle style;
/*    */   
/*    */   private static final int CIRCLE_VERTICES = 20;
/*    */   
/*    */   private static final float SEGMENT_SIZE_RADIANS = 0.31415927F;
/*    */   
/*    */   public CircleGizmo(Vec3 pos, float radius, GizmoStyle style) {
/*  6 */     this.pos = pos;
/*  6 */     this.radius = radius;
/*  6 */     this.style = style;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/CircleGizmo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/CircleGizmo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/CircleGizmo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/CircleGizmo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/CircleGizmo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/CircleGizmo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 pos() {
/*  6 */     return this.pos;
/*    */   }
/*    */   
/*    */   public float radius() {
/*  6 */     return this.radius;
/*    */   }
/*    */   
/*    */   public GizmoStyle style() {
/*  6 */     return this.style;
/*    */   }
/*    */   
/*    */   public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
/* 12 */     if (!this.style.hasStroke() && !this.style.hasFill())
/*    */       return; 
/* 16 */     Vec3[] points = new Vec3[21];
/* 17 */     for (int i = 0; i < 20; i++) {
/* 18 */       float theta = i * 0.31415927F;
/* 19 */       Vec3 point = this.pos.add((float)(this.radius * Math.cos(theta)), 0.0D, (float)(this.radius * Math.sin(theta)));
/* 20 */       points[i] = point;
/*    */     } 
/* 22 */     points[20] = points[0];
/* 24 */     if (this.style.hasFill()) {
/* 25 */       int color = this.style.multipliedFill(alphaMultiplier);
/* 26 */       primitives.addTriangleFan(points, color);
/*    */     } 
/* 28 */     if (this.style.hasStroke()) {
/* 29 */       int color = this.style.multipliedStroke(alphaMultiplier);
/* 30 */       for (int j = 0; j < 20; j++)
/* 31 */         primitives.addLine(points[j], points[j + 1], color, this.style.strokeWidth()); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gizmos\CircleGizmo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */