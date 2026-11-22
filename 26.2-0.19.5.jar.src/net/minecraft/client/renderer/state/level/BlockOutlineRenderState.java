/*   */ package net.minecraft.client.renderer.state.level;
/*   */ 
/*   */ import net.minecraft.core.BlockPos;
/*   */ import net.minecraft.world.phys.shapes.VoxelShape;
/*   */ 
/*   */ public final class BlockOutlineRenderState extends Record {
/*   */   private final BlockPos pos;
/*   */   
/*   */   private final boolean isTranslucent;
/*   */   
/*   */   private final boolean highContrast;
/*   */   
/*   */   private final VoxelShape shape;
/*   */   
/*   */   private final VoxelShape collisionShape;
/*   */   
/*   */   private final VoxelShape occlusionShape;
/*   */   
/*   */   private final VoxelShape interactionShape;
/*   */   
/*   */   public VoxelShape interactionShape() {
/* 7 */     return this.interactionShape;
/*   */   }
/*   */   
/*   */   public VoxelShape occlusionShape() {
/* 7 */     return this.occlusionShape;
/*   */   }
/*   */   
/*   */   public VoxelShape collisionShape() {
/* 7 */     return this.collisionShape;
/*   */   }
/*   */   
/*   */   public VoxelShape shape() {
/* 7 */     return this.shape;
/*   */   }
/*   */   
/*   */   public boolean highContrast() {
/* 7 */     return this.highContrast;
/*   */   }
/*   */   
/*   */   public boolean isTranslucent() {
/* 7 */     return this.isTranslucent;
/*   */   }
/*   */   
/*   */   public BlockPos pos() {
/* 7 */     return this.pos;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;
/*   */   }
/*   */   
/*   */   public BlockOutlineRenderState(BlockPos pos, boolean isTranslucent, boolean highContrast, VoxelShape shape, VoxelShape collisionShape, VoxelShape occlusionShape, VoxelShape interactionShape) {
/* 7 */     this.pos = pos;
/* 7 */     this.isTranslucent = isTranslucent;
/* 7 */     this.highContrast = highContrast;
/* 7 */     this.shape = shape;
/* 7 */     this.collisionShape = collisionShape;
/* 7 */     this.occlusionShape = occlusionShape;
/* 7 */     this.interactionShape = interactionShape;
/*   */   }
/*   */   
/*   */   public BlockOutlineRenderState(BlockPos pos, boolean isTranslucent, boolean highContrast, VoxelShape shape) {
/* 9 */     this(pos, isTranslucent, highContrast, shape, null, null, null);
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\level\BlockOutlineRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */