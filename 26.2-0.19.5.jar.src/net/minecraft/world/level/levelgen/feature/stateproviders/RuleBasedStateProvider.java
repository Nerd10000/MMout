/*     */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ 
/*     */ public class RuleBasedStateProvider extends BlockStateProvider {
/*     */   public static final MapCodec<RuleBasedStateProvider> CODEC;
/*     */   
/*     */   private final BlockStateProvider fallback;
/*     */   
/*     */   private final List<Rule> rules;
/*     */   
/*     */   static {
/*  19 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockStateProvider.CODEC.optionalFieldOf("fallback").forGetter(()), (App)Rule.CODEC.listOf().fieldOf("rules").forGetter(())).apply((Applicative)i, RuleBasedStateProvider::new));
/*     */   }
/*     */   
/*     */   public RuleBasedStateProvider(BlockStateProvider fallback, List<Rule> rules) {
/*  28 */     this.fallback = fallback;
/*  29 */     this.rules = rules;
/*     */   }
/*     */   
/*     */   private RuleBasedStateProvider(Optional<BlockStateProvider> fallback, List<Rule> rules) {
/*  33 */     this(fallback.orElse(null), rules);
/*     */   }
/*     */   
/*     */   public static RuleBasedStateProvider ifTrueThenProvide(BlockPredicate ifTrue, Block thenProvide) {
/*  37 */     return ifTrueThenProvide(ifTrue, BlockStateProvider.simple(thenProvide));
/*     */   }
/*     */   
/*     */   public static RuleBasedStateProvider ifTrueThenProvide(BlockPredicate ifTrue, BlockStateProvider thenProvide) {
/*  41 */     return new RuleBasedStateProvider((BlockStateProvider)null, List.of(new Rule(ifTrue, thenProvide)));
/*     */   }
/*     */   
/*     */   protected BlockStateProviderType<?> type() {
/*  46 */     return BlockStateProviderType.RULE_BASED_STATE_PROVIDER;
/*     */   }
/*     */   
/*     */   public BlockState getState(WorldGenLevel level, RandomSource random, BlockPos pos) {
/*  51 */     BlockState result = getOptionalState(level, random, pos);
/*  52 */     return (result != null) ? result : level.getBlockState(pos);
/*     */   }
/*     */   
/*     */   public BlockState getOptionalState(WorldGenLevel level, RandomSource random, BlockPos pos) {
/*  57 */     for (Rule rule : this.rules) {
/*  58 */       if (rule.ifTrue().test(level, pos))
/*  59 */         return rule.then().getState(level, random, pos); 
/*     */     } 
/*  63 */     return (this.fallback == null) ? null : this.fallback.getState(level, random, pos);
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/*  67 */     return new Builder(null);
/*     */   }
/*     */   
/*     */   public static Builder builder(BlockStateProvider fallback) {
/*  71 */     return new Builder(fallback);
/*     */   }
/*     */   
/*     */   public static final class Rule extends Record {
/*     */     private final BlockPredicate ifTrue;
/*     */     
/*     */     private final BlockStateProvider then;
/*     */     
/*     */     public static final Codec<Rule> CODEC;
/*     */     
/*     */     public Rule(BlockPredicate ifTrue, BlockStateProvider then) {
/*  74 */       this.ifTrue = ifTrue;
/*  74 */       this.then = then;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedStateProvider$Rule;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedStateProvider$Rule;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedStateProvider$Rule;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedStateProvider$Rule;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedStateProvider$Rule;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedStateProvider$Rule;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockPredicate ifTrue() {
/*  74 */       return this.ifTrue;
/*     */     }
/*     */     
/*     */     public BlockStateProvider then() {
/*  74 */       return this.then;
/*     */     }
/*     */     
/*     */     static {
/*  75 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockPredicate.CODEC.fieldOf("if_true").forGetter(Rule::ifTrue), (App)BlockStateProvider.CODEC.fieldOf("then").forGetter(Rule::then)).apply((Applicative)i, Rule::new));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final BlockStateProvider fallback;
/*     */     
/*  83 */     private final List<RuleBasedStateProvider.Rule> rules = new ArrayList<>();
/*     */     
/*     */     public Builder(BlockStateProvider fallback) {
/*  86 */       this.fallback = fallback;
/*     */     }
/*     */     
/*     */     public Builder ifTrueThenProvide(BlockPredicate ifTrue, BlockStateProvider thenProvide) {
/*  90 */       this.rules.add(new RuleBasedStateProvider.Rule(ifTrue, thenProvide));
/*  91 */       return this;
/*     */     }
/*     */     
/*     */     public Builder ifTrueThenProvide(BlockPredicate ifTrue, Block thenProvide) {
/*  95 */       this.rules.add(new RuleBasedStateProvider.Rule(ifTrue, BlockStateProvider.simple(thenProvide)));
/*  96 */       return this;
/*     */     }
/*     */     
/*     */     public Builder ifTrueThenProvide(BlockPredicate ifTrue, BlockState thenProvide) {
/* 100 */       this.rules.add(new RuleBasedStateProvider.Rule(ifTrue, BlockStateProvider.simple(thenProvide)));
/* 101 */       return this;
/*     */     }
/*     */     
/*     */     public RuleBasedStateProvider build() {
/* 105 */       return new RuleBasedStateProvider(this.fallback, this.rules);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\RuleBasedStateProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */