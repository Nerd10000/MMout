/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public final class FluidStatus extends Record {
/*     */   private final int fluidLevel;
/*     */   
/*     */   private final BlockState fluidType;
/*     */   
/*     */   public FluidStatus(int fluidLevel, BlockState fluidType) {
/* 715 */     this.fluidLevel = fluidLevel;
/* 715 */     this.fluidType = fluidType;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/Aquifer$FluidStatus;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #715	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/Aquifer$FluidStatus;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/Aquifer$FluidStatus;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #715	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/Aquifer$FluidStatus;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/Aquifer$FluidStatus;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #715	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/Aquifer$FluidStatus;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int fluidLevel() {
/* 715 */     return this.fluidLevel;
/*     */   }
/*     */   
/*     */   public BlockState fluidType() {
/* 715 */     return this.fluidType;
/*     */   }
/*     */   
/*     */   public BlockState at(int blockY) {
/* 721 */     return (blockY < this.fluidLevel) ? this.fluidType : Blocks.AIR.defaultBlockState();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\Aquifer$FluidStatus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */