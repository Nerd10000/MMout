/*     */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ 
/*     */ public class Builder {
/*     */   private final BlockStateProvider fallback;
/*     */   
/*  83 */   private final List<RuleBasedStateProvider.Rule> rules = new ArrayList<>();
/*     */   
/*     */   public Builder(BlockStateProvider fallback) {
/*  86 */     this.fallback = fallback;
/*     */   }
/*     */   
/*     */   public Builder ifTrueThenProvide(BlockPredicate ifTrue, BlockStateProvider thenProvide) {
/*  90 */     this.rules.add(new RuleBasedStateProvider.Rule(ifTrue, thenProvide));
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public Builder ifTrueThenProvide(BlockPredicate ifTrue, Block thenProvide) {
/*  95 */     this.rules.add(new RuleBasedStateProvider.Rule(ifTrue, BlockStateProvider.simple(thenProvide)));
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public Builder ifTrueThenProvide(BlockPredicate ifTrue, BlockState thenProvide) {
/* 100 */     this.rules.add(new RuleBasedStateProvider.Rule(ifTrue, BlockStateProvider.simple(thenProvide)));
/* 101 */     return this;
/*     */   }
/*     */   
/*     */   public RuleBasedStateProvider build() {
/* 105 */     return new RuleBasedStateProvider(this.fallback, this.rules);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\RuleBasedStateProvider$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */