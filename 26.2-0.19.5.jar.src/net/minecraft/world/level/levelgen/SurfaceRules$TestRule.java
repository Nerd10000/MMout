/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ final class TestRule extends Record implements SurfaceRules.SurfaceRule {
/*     */   private final SurfaceRules.Condition condition;
/*     */   
/*     */   private final SurfaceRules.SurfaceRule followup;
/*     */   
/*     */   private TestRule(SurfaceRules.Condition condition, SurfaceRules.SurfaceRule followup) {
/* 432 */     this.condition = condition;
/* 432 */     this.followup = followup;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #432	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #432	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #432	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public SurfaceRules.Condition condition() {
/* 432 */     return this.condition;
/*     */   }
/*     */   
/*     */   public SurfaceRules.SurfaceRule followup() {
/* 432 */     return this.followup;
/*     */   }
/*     */   
/*     */   public BlockState tryApply(int blockX, int blockY, int blockZ) {
/* 435 */     if (!this.condition.test())
/* 436 */       return null; 
/* 438 */     return this.followup.tryApply(blockX, blockY, blockZ);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$TestRule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */