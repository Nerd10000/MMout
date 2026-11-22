/*    */ package net.minecraft.client.renderer.gizmos;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class Line extends Record {
/*    */   private final Vec3 start;
/*    */   
/*    */   private final Vec3 end;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   private final float width;
/*    */   
/*    */   public Line(Vec3 start, Vec3 end, int color, float width) {
/* 64 */     this.start = start;
/* 64 */     this.end = end;
/* 64 */     this.color = color;
/* 64 */     this.width = width;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Line;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 start() {
/* 64 */     return this.start;
/*    */   }
/*    */   
/*    */   public Vec3 end() {
/* 64 */     return this.end;
/*    */   }
/*    */   
/*    */   public int color() {
/* 64 */     return this.color;
/*    */   }
/*    */   
/*    */   public float width() {
/* 64 */     return this.width;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\gizmos\DrawableGizmoPrimitives$Line.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */