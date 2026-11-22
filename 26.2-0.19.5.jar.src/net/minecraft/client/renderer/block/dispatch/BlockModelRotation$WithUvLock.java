/*    */ package net.minecraft.client.renderer.block.dispatch;
/*    */ 
/*    */ import com.mojang.math.Transformation;
/*    */ import net.minecraft.core.Direction;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ final class WithUvLock extends Record implements ModelState {
/*    */   private final BlockModelRotation parent;
/*    */   
/*    */   private WithUvLock(BlockModelRotation parent) {
/* 59 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/BlockModelRotation$WithUvLock;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #59	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/BlockModelRotation$WithUvLock;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/BlockModelRotation$WithUvLock;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #59	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/BlockModelRotation$WithUvLock;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockModelRotation parent() {
/* 59 */     return this.parent;
/*    */   }
/*    */   
/*    */   public Transformation transformation() {
/* 62 */     return this.parent.transformation;
/*    */   }
/*    */   
/*    */   public Matrix4fc faceTransformation(Direction face) {
/* 67 */     return this.parent.faceMapping.getOrDefault(face, NO_TRANSFORM);
/*    */   }
/*    */   
/*    */   public Matrix4fc inverseFaceTransformation(Direction face) {
/* 72 */     return this.parent.inverseFaceMapping.getOrDefault(face, NO_TRANSFORM);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 77 */     return "uvLocked[" + this.parent.orientation.getSerializedName() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\BlockModelRotation$WithUvLock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */