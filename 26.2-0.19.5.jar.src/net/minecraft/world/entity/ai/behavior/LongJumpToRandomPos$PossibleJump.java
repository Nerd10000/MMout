/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ 
/*     */ public final class PossibleJump extends Record {
/*     */   private final BlockPos targetPos;
/*     */   
/*     */   private final int weight;
/*     */   
/*     */   public PossibleJump(BlockPos targetPos, int weight) {
/* 214 */     this.targetPos = targetPos;
/* 214 */     this.weight = weight;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/behavior/LongJumpToRandomPos$PossibleJump;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #214	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/LongJumpToRandomPos$PossibleJump;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/behavior/LongJumpToRandomPos$PossibleJump;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #214	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/LongJumpToRandomPos$PossibleJump;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/behavior/LongJumpToRandomPos$PossibleJump;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #214	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/behavior/LongJumpToRandomPos$PossibleJump;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockPos targetPos() {
/* 214 */     return this.targetPos;
/*     */   }
/*     */   
/*     */   public int weight() {
/* 214 */     return this.weight;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\LongJumpToRandomPos$PossibleJump.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */