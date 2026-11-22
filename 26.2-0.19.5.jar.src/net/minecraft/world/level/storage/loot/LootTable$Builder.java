/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ 
/*     */ public class Builder implements FunctionUserBuilder<LootTable.Builder> {
/* 214 */   private final ImmutableList.Builder<LootPool> pools = ImmutableList.builder();
/*     */   
/* 216 */   private final ImmutableList.Builder<LootItemFunction> functions = ImmutableList.builder();
/*     */   
/* 218 */   private ContextKeySet paramSet = LootTable.DEFAULT_PARAM_SET;
/*     */   
/* 219 */   private Optional<Identifier> randomSequence = Optional.empty();
/*     */   
/*     */   public Builder withPool(LootPool.Builder pool) {
/* 222 */     this.pools.add(pool.build());
/* 223 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setParamSet(ContextKeySet paramSet) {
/* 227 */     this.paramSet = paramSet;
/* 228 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setRandomSequence(Identifier key) {
/* 232 */     this.randomSequence = Optional.of(key);
/* 233 */     return this;
/*     */   }
/*     */   
/*     */   public Builder apply(LootItemFunction.Builder function) {
/* 238 */     this.functions.add(function.build());
/* 239 */     return this;
/*     */   }
/*     */   
/*     */   public Builder unwrap() {
/* 244 */     return this;
/*     */   }
/*     */   
/*     */   public LootTable build() {
/* 248 */     return new LootTable(this.paramSet, this.randomSequence, (List<LootPool>)this.pools.build(), (List<LootItemFunction>)this.functions.build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootTable$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */