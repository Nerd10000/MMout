/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.SideChainPart;
/*     */ 
/*     */ public final class Neighbors extends Record {
/*     */   private final SideChainPartBlock block;
/*     */   
/*     */   private final LevelAccessor level;
/*     */   
/*     */   private final Direction facing;
/*     */   
/*     */   private final BlockPos center;
/*     */   
/*     */   private final Map<BlockPos, SideChainPartBlock.Neighbor> cache;
/*     */   
/*     */   public Neighbors(SideChainPartBlock block, LevelAccessor level, Direction facing, BlockPos center, Map<BlockPos, SideChainPartBlock.Neighbor> cache) {
/* 117 */     this.block = block;
/* 117 */     this.level = level;
/* 117 */     this.facing = facing;
/* 117 */     this.center = center;
/* 117 */     this.cache = cache;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #117	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #117	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #117	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public SideChainPartBlock block() {
/* 117 */     return this.block;
/*     */   }
/*     */   
/*     */   public LevelAccessor level() {
/* 117 */     return this.level;
/*     */   }
/*     */   
/*     */   public Direction facing() {
/* 117 */     return this.facing;
/*     */   }
/*     */   
/*     */   public BlockPos center() {
/* 117 */     return this.center;
/*     */   }
/*     */   
/*     */   public Map<BlockPos, SideChainPartBlock.Neighbor> cache() {
/* 117 */     return this.cache;
/*     */   }
/*     */   
/*     */   private boolean isConnectableToThisBlock(BlockState neighbor) {
/* 120 */     return (this.block.isConnectable(neighbor) && this.block.getFacing(neighbor) == this.facing);
/*     */   }
/*     */   
/*     */   private SideChainPartBlock.Neighbor createNewNeighbor(BlockPos pos) {
/* 124 */     BlockState neighbor = this.level.getBlockState(pos);
/* 125 */     SideChainPart part = isConnectableToThisBlock(neighbor) ? this.block.getSideChainPart(neighbor) : null;
/* 126 */     return (part == null) ? new SideChainPartBlock.EmptyNeighbor(pos) : new SideChainPartBlock.SideChainNeighbor(this.level, this.block, pos, part);
/*     */   }
/*     */   
/*     */   private SideChainPartBlock.Neighbor getOrCreateNeighbor(Direction dir, Integer steps) {
/* 130 */     return this.cache.computeIfAbsent(this.center.relative(dir, steps.intValue()), this::createNewNeighbor);
/*     */   }
/*     */   
/*     */   public SideChainPartBlock.Neighbor left(int steps) {
/* 134 */     return getOrCreateNeighbor(this.facing.getClockWise(), Integer.valueOf(steps));
/*     */   }
/*     */   
/*     */   public SideChainPartBlock.Neighbor right(int steps) {
/* 138 */     return getOrCreateNeighbor(this.facing.getCounterClockWise(), Integer.valueOf(steps));
/*     */   }
/*     */   
/*     */   public SideChainPartBlock.Neighbor left() {
/* 142 */     return left(1);
/*     */   }
/*     */   
/*     */   public SideChainPartBlock.Neighbor right() {
/* 146 */     return right(1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SideChainPartBlock$Neighbors.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */