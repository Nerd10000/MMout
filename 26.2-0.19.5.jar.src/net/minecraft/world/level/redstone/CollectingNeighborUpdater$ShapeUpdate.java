/*     */ package net.minecraft.world.level.redstone;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ final class ShapeUpdate extends Record implements CollectingNeighborUpdater.NeighborUpdates {
/*     */   private final Direction direction;
/*     */   
/*     */   private final BlockState neighborState;
/*     */   
/*     */   private final BlockPos pos;
/*     */   
/*     */   private final BlockPos neighborPos;
/*     */   
/*     */   private final int updateFlags;
/*     */   
/*     */   private final int updateLimit;
/*     */   
/*     */   private ShapeUpdate(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, int updateFlags, int updateLimit) {
/* 183 */     this.direction = direction;
/* 183 */     this.neighborState = neighborState;
/* 183 */     this.pos = pos;
/* 183 */     this.neighborPos = neighborPos;
/* 183 */     this.updateFlags = updateFlags;
/* 183 */     this.updateLimit = updateLimit;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #183	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #183	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #183	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Direction direction() {
/* 183 */     return this.direction;
/*     */   }
/*     */   
/*     */   public BlockState neighborState() {
/* 183 */     return this.neighborState;
/*     */   }
/*     */   
/*     */   public BlockPos pos() {
/* 183 */     return this.pos;
/*     */   }
/*     */   
/*     */   public BlockPos neighborPos() {
/* 183 */     return this.neighborPos;
/*     */   }
/*     */   
/*     */   public int updateFlags() {
/* 183 */     return this.updateFlags;
/*     */   }
/*     */   
/*     */   public int updateLimit() {
/* 183 */     return this.updateLimit;
/*     */   }
/*     */   
/*     */   public boolean runNext(Level level) {
/* 186 */     NeighborUpdater.executeShapeUpdate((LevelAccessor)level, this.direction, this.pos, this.neighborPos, this.neighborState, this.updateFlags, this.updateLimit);
/* 187 */     return false;
/*     */   }
/*     */   
/*     */   public void forEachUpdatedPos(Consumer<BlockPos> output) {
/* 192 */     output.accept(this.pos);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\redstone\CollectingNeighborUpdater$ShapeUpdate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */