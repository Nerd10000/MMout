/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ final class BlockStatePairKey extends Record {
/*     */   private final BlockState first;
/*     */   
/*     */   private final BlockState second;
/*     */   
/*     */   private final Direction direction;
/*     */   
/*     */   private BlockStatePairKey(BlockState first, BlockState second, Direction direction) {
/* 215 */     this.first = first;
/* 215 */     this.second = second;
/* 215 */     this.direction = direction;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/material/FlowingFluid$BlockStatePairKey;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #215	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/material/FlowingFluid$BlockStatePairKey;
/*     */   }
/*     */   
/*     */   public BlockState first() {
/* 215 */     return this.first;
/*     */   }
/*     */   
/*     */   public BlockState second() {
/* 215 */     return this.second;
/*     */   }
/*     */   
/*     */   public Direction direction() {
/* 215 */     return this.direction;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 223 */     if (o instanceof BlockStatePairKey) {
/* 223 */       BlockStatePairKey that = (BlockStatePairKey)o;
/* 223 */       if (this.first == that.first && this.second == that.second && this.direction == that.direction);
/*     */     } 
/* 223 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 228 */     int result = System.identityHashCode(this.first);
/* 229 */     result = 31 * result + System.identityHashCode(this.second);
/* 230 */     result = 31 * result + this.direction.hashCode();
/* 231 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\material\FlowingFluid$BlockStatePairKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */