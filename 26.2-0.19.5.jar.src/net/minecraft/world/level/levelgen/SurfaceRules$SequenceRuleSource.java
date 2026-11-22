/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ final class SequenceRuleSource extends Record implements SurfaceRules.RuleSource {
/*     */   private final List<SurfaceRules.RuleSource> sequence;
/*     */   
/*     */   private SequenceRuleSource(List<SurfaceRules.RuleSource> sequence) {
/* 847 */     this.sequence = sequence;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #847	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #847	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #847	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public List<SurfaceRules.RuleSource> sequence() {
/* 847 */     return this.sequence;
/*     */   }
/*     */   
/* 848 */   private static final MapCodec<SequenceRuleSource> CODEC = SurfaceRules.RuleSource.CODEC.listOf().xmap(SequenceRuleSource::new, SequenceRuleSource::sequence).fieldOf("sequence");
/*     */   
/*     */   public MapCodec<SequenceRuleSource> codec() {
/* 852 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
/* 857 */     if (this.sequence.size() == 1)
/* 858 */       return ((SurfaceRules.RuleSource)this.sequence.get(0)).apply(context); 
/* 861 */     ImmutableList.Builder<SurfaceRules.SurfaceRule> builder = ImmutableList.builder();
/* 862 */     for (SurfaceRules.RuleSource rule : this.sequence)
/* 863 */       builder.add(rule.apply(context)); 
/* 865 */     return new SurfaceRules.SequenceRule((List<SurfaceRules.SurfaceRule>)builder.build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$SequenceRuleSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */