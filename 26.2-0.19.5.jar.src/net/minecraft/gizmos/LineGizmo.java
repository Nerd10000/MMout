/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class LineGizmo extends Record implements Gizmo {
/*    */   private final Vec3 start;
/*    */   
/*    */   private final Vec3 end;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   private final float width;
/*    */   
/*    */   public static final float DEFAULT_WIDTH = 3.0F;
/*    */   
/*    */   public LineGizmo(Vec3 start, Vec3 end, int color, float width) {
/*  6 */     this.start = start;
/*  6 */     this.end = end;
/*  6 */     this.color = color;
/*  6 */     this.width = width;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/LineGizmo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/LineGizmo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/LineGizmo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/LineGizmo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/LineGizmo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/LineGizmo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 start() {
/*  6 */     return this.start;
/*    */   }
/*    */   
/*    */   public Vec3 end() {
/*  6 */     return this.end;
/*    */   }
/*    */   
/*    */   public int color() {
/*  6 */     return this.color;
/*    */   }
/*    */   
/*    */   public float width() {
/*  6 */     return this.width;
/*    */   }
/*    */   
/*    */   public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
/* 11 */     primitives.addLine(this.start, this.end, ARGB.multiplyAlpha(this.color, alphaMultiplier), this.width);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gizmos\LineGizmo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */