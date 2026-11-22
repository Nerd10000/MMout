/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class LootPool implements Validatable {
/*     */   public static final Codec<LootPool> CODEC;
/*     */   
/*     */   private final List<LootPoolEntryContainer> entries;
/*     */   
/*     */   private final List<LootItemCondition> conditions;
/*     */   
/*     */   private final Predicate<LootContext> compositeCondition;
/*     */   
/*     */   private final List<LootItemFunction> functions;
/*     */   
/*     */   private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
/*     */   
/*     */   private final NumberProvider rolls;
/*     */   
/*     */   private final NumberProvider bonusRolls;
/*     */   
/*     */   static {
/*  31 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)LootPoolEntries.CODEC.listOf().fieldOf("entries").forGetter(()), (App)LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions", List.of()).forGetter(()), (App)LootItemFunctions.ROOT_CODEC.listOf().optionalFieldOf("functions", List.of()).forGetter(()), (App)NumberProviders.CODEC.fieldOf("rolls").forGetter(()), (App)NumberProviders.CODEC.optionalFieldOf("bonus_rolls", ConstantValue.exactly(0.0F)).forGetter(())).apply((Applicative)i, LootPool::new));
/*     */   }
/*     */   
/*     */   private LootPool(List<LootPoolEntryContainer> entries, List<LootItemCondition> conditions, List<LootItemFunction> functions, NumberProvider rolls, NumberProvider bonusRolls) {
/*  48 */     this.entries = entries;
/*  49 */     this.conditions = conditions;
/*  50 */     this.compositeCondition = Util.allOf(conditions);
/*  51 */     this.functions = functions;
/*  52 */     this.compositeFunction = LootItemFunctions.compose(functions);
/*  53 */     this.rolls = rolls;
/*  54 */     this.bonusRolls = bonusRolls;
/*     */   }
/*     */   
/*     */   private void addRandomItem(Consumer<ItemStack> result, LootContext context) {
/*  58 */     RandomSource random = context.getRandom();
/*  59 */     List<LootPoolEntry> validEntries = Lists.newArrayList();
/*  60 */     MutableInt totalWeight = new MutableInt();
/*  61 */     for (LootPoolEntryContainer entry : this.entries) {
/*  62 */       entry.expand(context, e -> {
/*     */             int weight = e.getWeight(context.getLuck());
/*     */             if (weight > 0) {
/*     */               validEntries.add(e);
/*     */               totalWeight.add(weight);
/*     */             } 
/*     */           });
/*     */     } 
/*  71 */     int entryCount = validEntries.size();
/*  72 */     if (totalWeight.intValue() == 0 || entryCount == 0)
/*     */       return; 
/*  76 */     if (entryCount == 1) {
/*  77 */       ((LootPoolEntry)validEntries.get(0)).createItemStack(result, context);
/*     */       return;
/*     */     } 
/*  81 */     int index = random.nextInt(totalWeight.intValue());
/*  82 */     for (LootPoolEntry entry : validEntries) {
/*  83 */       index -= entry.getWeight(context.getLuck());
/*  84 */       if (index < 0) {
/*  85 */         entry.createItemStack(result, context);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addRandomItems(Consumer<ItemStack> result, LootContext context) {
/*  92 */     if (!this.compositeCondition.test(context))
/*     */       return; 
/*  96 */     Consumer<ItemStack> decoratedConsumer = LootItemFunction.decorate(this.compositeFunction, result, context);
/*  98 */     int count = this.rolls.getInt(context) + Mth.floor(this.bonusRolls.getFloat(context) * context.getLuck());
/*  99 */     for (int i = 0; i < count; i++)
/* 100 */       addRandomItem(decoratedConsumer, context); 
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext output) {
/* 106 */     Validatable.validate(output, "conditions", (List)this.conditions);
/* 107 */     Validatable.validate(output, "functions", (List)this.functions);
/* 108 */     Validatable.validate(output, "entries", (List)this.entries);
/* 109 */     Validatable.validate(output, "rolls", (Validatable)this.rolls);
/* 110 */     Validatable.validate(output, "bonus_rolls", (Validatable)this.bonusRolls);
/*     */   }
/*     */   
/*     */   public static class Builder implements FunctionUserBuilder<Builder>, ConditionUserBuilder<Builder> {
/* 114 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*     */     
/* 115 */     private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/*     */     
/* 116 */     private final ImmutableList.Builder<LootItemFunction> functions = ImmutableList.builder();
/*     */     
/* 117 */     private NumberProvider rolls = (NumberProvider)ConstantValue.exactly(1.0F);
/*     */     
/* 118 */     private NumberProvider bonusRolls = (NumberProvider)ConstantValue.exactly(0.0F);
/*     */     
/*     */     public Builder setRolls(NumberProvider rolls) {
/* 121 */       this.rolls = rolls;
/* 122 */       return this;
/*     */     }
/*     */     
/*     */     public Builder unwrap() {
/* 127 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setBonusRolls(NumberProvider bonusRolls) {
/* 131 */       this.bonusRolls = bonusRolls;
/* 132 */       return this;
/*     */     }
/*     */     
/*     */     public Builder add(LootPoolEntryContainer.Builder<?> entry) {
/* 136 */       this.entries.add(entry.build());
/* 137 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addAll(List<? extends LootPoolSingletonContainer.Builder<?>> entries) {
/* 141 */       for (LootPoolEntryContainer.Builder<?> entry : entries)
/* 142 */         add(entry); 
/* 144 */       return this;
/*     */     }
/*     */     
/*     */     public Builder when(LootItemCondition.Builder condition) {
/* 149 */       this.conditions.add(condition.build());
/* 150 */       return this;
/*     */     }
/*     */     
/*     */     public Builder apply(LootItemFunction.Builder function) {
/* 155 */       this.functions.add(function.build());
/* 156 */       return this;
/*     */     }
/*     */     
/*     */     public LootPool build() {
/* 160 */       return new LootPool((List<LootPoolEntryContainer>)this.entries.build(), (List<LootItemCondition>)this.conditions.build(), (List<LootItemFunction>)this.functions.build(), this.rolls, this.bonusRolls);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder lootPool() {
/* 165 */     return new Builder();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootPool.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */