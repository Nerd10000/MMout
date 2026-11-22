/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ 
/*     */ final class FluidInfo extends Record {
/*     */   private final BlockPos pos;
/*     */   
/*     */   private final Fluid fluid;
/*     */   
/*     */   private final BlockState sourceState;
/*     */   
/*     */   private FluidInfo(BlockPos pos, Fluid fluid, BlockState sourceState) {
/* 270 */     this.pos = pos;
/* 270 */     this.fluid = fluid;
/* 270 */     this.sourceState = sourceState;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #270	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #270	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #270	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockPos pos() {
/* 270 */     return this.pos;
/*     */   }
/*     */   
/*     */   public Fluid fluid() {
/* 270 */     return this.fluid;
/*     */   }
/*     */   
/*     */   public BlockState sourceState() {
/* 270 */     return this.sourceState;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\PointedDripstoneBlock$FluidInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */