/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public final class ShadowPiece extends Record {
/*    */   private final float relativeX;
/*    */   
/*    */   private final float relativeY;
/*    */   
/*    */   private final float relativeZ;
/*    */   
/*    */   private final VoxelShape shapeBelow;
/*    */   
/*    */   private final float alpha;
/*    */   
/*    */   public ShadowPiece(float relativeX, float relativeY, float relativeZ, VoxelShape shapeBelow, float alpha) {
/* 62 */     this.relativeX = relativeX;
/* 62 */     this.relativeY = relativeY;
/* 62 */     this.relativeZ = relativeZ;
/* 62 */     this.shapeBelow = shapeBelow;
/* 62 */     this.alpha = alpha;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/state/EntityRenderState$ShadowPiece;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/state/EntityRenderState$ShadowPiece;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/state/EntityRenderState$ShadowPiece;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/state/EntityRenderState$ShadowPiece;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/state/EntityRenderState$ShadowPiece;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/entity/state/EntityRenderState$ShadowPiece;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float relativeX() {
/* 62 */     return this.relativeX;
/*    */   }
/*    */   
/*    */   public float relativeY() {
/* 62 */     return this.relativeY;
/*    */   }
/*    */   
/*    */   public float relativeZ() {
/* 62 */     return this.relativeZ;
/*    */   }
/*    */   
/*    */   public VoxelShape shapeBelow() {
/* 62 */     return this.shapeBelow;
/*    */   }
/*    */   
/*    */   public float alpha() {
/* 62 */     return this.alpha;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\EntityRenderState$ShadowPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */