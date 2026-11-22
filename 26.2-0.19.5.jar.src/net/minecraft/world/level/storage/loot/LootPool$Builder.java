/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ 
/*     */ public class Builder implements FunctionUserBuilder<LootPool.Builder>, ConditionUserBuilder<LootPool.Builder> {
/* 114 */   private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*     */   
/* 115 */   private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/*     */   
/* 116 */   private final ImmutableList.Builder<LootItemFunction> functions = ImmutableList.builder();
/*     */   
/* 117 */   private NumberProvider rolls = (NumberProvider)ConstantValue.exactly(1.0F);
/*     */   
/* 118 */   private NumberProvider bonusRolls = (NumberProvider)ConstantValue.exactly(0.0F);
/*     */   
/*     */   public Builder setRolls(NumberProvider rolls) {
/* 121 */     this.rolls = rolls;
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public Builder unwrap() {
/* 127 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setBonusRolls(NumberProvider bonusRolls) {
/* 131 */     this.bonusRolls = bonusRolls;
/* 132 */     return this;
/*     */   }
/*     */   
/*     */   public Builder add(LootPoolEntryContainer.Builder<?> entry) {
/* 136 */     this.entries.add(entry.build());
/* 137 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addAll(List<? extends LootPoolSingletonContainer.Builder<?>> entries) {
/* 141 */     for (LootPoolEntryContainer.Builder<?> entry : entries)
/* 142 */       add(entry); 
/* 144 */     return this;
/*     */   }
/*     */   
/*     */   public Builder when(LootItemCondition.Builder condition) {
/* 149 */     this.conditions.add(condition.build());
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   public Builder apply(LootItemFunction.Builder function) {
/* 155 */     this.functions.add(function.build());
/* 156 */     return this;
/*     */   }
/*     */   
/*     */   public LootPool build() {
/* 160 */     return new LootPool((List<LootPoolEntryContainer>)this.entries.build(), (List<LootItemCondition>)this.conditions.build(), (List<LootItemFunction>)this.functions.build(), this.rolls, this.bonusRolls);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootPool$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */