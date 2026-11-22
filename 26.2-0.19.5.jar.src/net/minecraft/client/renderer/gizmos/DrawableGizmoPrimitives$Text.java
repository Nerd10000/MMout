/*    */ package net.minecraft.client.renderer.gizmos;
/*    */ 
/*    */ import net.minecraft.gizmos.TextGizmo;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class Text extends Record {
/*    */   private final Vec3 pos;
/*    */   
/*    */   private final String text;
/*    */   
/*    */   private final TextGizmo.Style style;
/*    */   
/*    */   public Text(Vec3 pos, String text, TextGizmo.Style style) {
/* 70 */     this.pos = pos;
/* 70 */     this.text = text;
/* 70 */     this.style = style;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives$Text;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 pos() {
/* 70 */     return this.pos;
/*    */   }
/*    */   
/*    */   public String text() {
/* 70 */     return this.text;
/*    */   }
/*    */   
/*    */   public TextGizmo.Style style() {
/* 70 */     return this.style;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\gizmos\DrawableGizmoPrimitives$Text.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */