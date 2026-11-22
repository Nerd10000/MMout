/*   */ package net.minecraft.gizmos;
/*   */ 
/*   */ import net.minecraft.util.ARGB;
/*   */ import net.minecraft.world.phys.Vec3;
/*   */ 
/*   */ public final class PointGizmo extends Record implements Gizmo {
/*   */   private final Vec3 pos;
/*   */   
/*   */   private final int color;
/*   */   
/*   */   private final float size;
/*   */   
/*   */   public PointGizmo(Vec3 pos, int color, float size) {
/* 6 */     this.pos = pos;
/* 6 */     this.color = color;
/* 6 */     this.size = size;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/PointGizmo;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/gizmos/PointGizmo;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/PointGizmo;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/gizmos/PointGizmo;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/PointGizmo;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/gizmos/PointGizmo;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public Vec3 pos() {
/* 6 */     return this.pos;
/*   */   }
/*   */   
/*   */   public int color() {
/* 6 */     return this.color;
/*   */   }
/*   */   
/*   */   public float size() {
/* 6 */     return this.size;
/*   */   }
/*   */   
/*   */   public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
/* 9 */     primitives.addPoint(this.pos, ARGB.multiplyAlpha(this.color, alphaMultiplier), this.size);
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gizmos\PointGizmo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */