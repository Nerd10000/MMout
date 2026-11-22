/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.ticks.SavedTick;
/*     */ 
/*     */ public final class PackedTicks extends Record {
/*     */   private final List<SavedTick<Block>> blocks;
/*     */   
/*     */   private final List<SavedTick<Fluid>> fluids;
/*     */   
/*     */   public PackedTicks(List<SavedTick<Block>> blocks, List<SavedTick<Fluid>> fluids) {
/* 354 */     this.blocks = blocks;
/* 354 */     this.fluids = fluids;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #354	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #354	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #354	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public List<SavedTick<Block>> blocks() {
/* 354 */     return this.blocks;
/*     */   }
/*     */   
/*     */   public List<SavedTick<Fluid>> fluids() {
/* 354 */     return this.fluids;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\ChunkAccess$PackedTicks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */