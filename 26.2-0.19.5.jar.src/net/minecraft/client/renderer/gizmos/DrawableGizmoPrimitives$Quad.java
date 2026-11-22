/*    */ package net.minecraft.client.renderer.gizmos;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class Quad extends Record {
/*    */   private final Vec3 a;
/*    */   
/*    */   private final Vec3 b;
/*    */   
/*    */   private final Vec3 c;
/*    */   
/*    */   private final Vec3 d;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   public Quad(Vec3 a, Vec3 b, Vec3 c, Vec3 d, int color) {
/* 68 */     this.a = a;
/* 68 */     this.b = b;
/* 68 */     this.c = c;
/* 68 */     this.d = d;
/* 68 */     this.color = color;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #68	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #68	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #68	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Quad;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 a() {
/* 68 */     return this.a;
/*    */   }
/*    */   
/*    */   public Vec3 b() {
/* 68 */     return this.b;
/*    */   }
/*    */   
/*    */   public Vec3 c() {
/* 68 */     return this.c;
/*    */   }
/*    */   
/*    */   public Vec3 d() {
/* 68 */     return this.d;
/*    */   }
/*    */   
/*    */   public int color() {
/* 68 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\gizmos\DrawableGizmoPrimitives$Quad.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */