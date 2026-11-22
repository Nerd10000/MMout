/*    */ package net.minecraft.client.renderer.gizmos;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class Point extends Record {
/*    */   private final Vec3 pos;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   private final float size;
/*    */   
/*    */   public Point(Vec3 pos, int color, float size) {
/* 72 */     this.pos = pos;
/* 72 */     this.color = color;
/* 72 */     this.size = size;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #72	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #72	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #72	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Point;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 pos() {
/* 72 */     return this.pos;
/*    */   }
/*    */   
/*    */   public int color() {
/* 72 */     return this.color;
/*    */   }
/*    */   
/*    */   public float size() {
/* 72 */     return this.size;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\gizmos\DrawableGizmoPrimitives$Point.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */