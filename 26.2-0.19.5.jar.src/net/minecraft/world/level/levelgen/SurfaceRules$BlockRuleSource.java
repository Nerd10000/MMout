/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ final class BlockRuleSource extends Record implements SurfaceRules.RuleSource {
/*     */   private final BlockState resultState;
/*     */   
/*     */   private final SurfaceRules.StateRule rule;
/*     */   
/*     */   private BlockRuleSource(BlockState resultState, SurfaceRules.StateRule rule) {
/* 812 */     this.resultState = resultState;
/* 812 */     this.rule = rule;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #812	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #812	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #812	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockState resultState() {
/* 812 */     return this.resultState;
/*     */   }
/*     */   
/*     */   public SurfaceRules.StateRule rule() {
/* 812 */     return this.rule;
/*     */   }
/*     */   
/* 813 */   private static final MapCodec<BlockRuleSource> CODEC = BlockState.CODEC.xmap(BlockRuleSource::new, BlockRuleSource::resultState).fieldOf("result_state");
/*     */   
/*     */   private BlockRuleSource(BlockState state) {
/* 816 */     this(state, new SurfaceRules.StateRule(state));
/*     */   }
/*     */   
/*     */   public MapCodec<BlockRuleSource> codec() {
/* 821 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
/* 826 */     return this.rule;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$BlockRuleSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */