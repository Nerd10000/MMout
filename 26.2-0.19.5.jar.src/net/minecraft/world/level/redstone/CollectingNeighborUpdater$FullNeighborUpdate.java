/*     */ package net.minecraft.world.level.redstone;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ final class FullNeighborUpdate extends Record implements CollectingNeighborUpdater.NeighborUpdates {
/*     */   private final BlockState state;
/*     */   
/*     */   private final BlockPos pos;
/*     */   
/*     */   private final Block block;
/*     */   
/*     */   private final Orientation orientation;
/*     */   
/*     */   private final boolean movedByPiston;
/*     */   
/*     */   private FullNeighborUpdate(BlockState state, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 122 */     this.state = state;
/* 122 */     this.pos = pos;
/* 122 */     this.block = block;
/* 122 */     this.orientation = orientation;
/* 122 */     this.movedByPiston = movedByPiston;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$FullNeighborUpdate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #122	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$FullNeighborUpdate;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$FullNeighborUpdate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #122	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$FullNeighborUpdate;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$FullNeighborUpdate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #122	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$FullNeighborUpdate;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockState state() {
/* 122 */     return this.state;
/*     */   }
/*     */   
/*     */   public BlockPos pos() {
/* 122 */     return this.pos;
/*     */   }
/*     */   
/*     */   public Block block() {
/* 122 */     return this.block;
/*     */   }
/*     */   
/*     */   public Orientation orientation() {
/* 122 */     return this.orientation;
/*     */   }
/*     */   
/*     */   public boolean movedByPiston() {
/* 122 */     return this.movedByPiston;
/*     */   }
/*     */   
/*     */   public boolean runNext(Level level) {
/* 125 */     NeighborUpdater.executeUpdate(level, this.state, this.pos, this.block, this.orientation, this.movedByPiston);
/* 126 */     return false;
/*     */   }
/*     */   
/*     */   public void forEachUpdatedPos(Consumer<BlockPos> output) {
/* 131 */     output.accept(this.pos);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\redstone\CollectingNeighborUpdater$FullNeighborUpdate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */