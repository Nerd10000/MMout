/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ final class SequenceRule extends Record implements SurfaceRules.SurfaceRule {
/*     */   private final List<SurfaceRules.SurfaceRule> rules;
/*     */   
/*     */   private SequenceRule(List<SurfaceRules.SurfaceRule> rules) {
/* 442 */     this.rules = rules;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #442	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #442	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #442	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public List<SurfaceRules.SurfaceRule> rules() {
/* 442 */     return this.rules;
/*     */   }
/*     */   
/*     */   public BlockState tryApply(int blockX, int blockY, int blockZ) {
/* 445 */     for (SurfaceRules.SurfaceRule rule : this.rules) {
/* 446 */       BlockState state = rule.tryApply(blockX, blockY, blockZ);
/* 447 */       if (state != null)
/* 448 */         return state; 
/*     */     } 
/* 451 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$SequenceRule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */