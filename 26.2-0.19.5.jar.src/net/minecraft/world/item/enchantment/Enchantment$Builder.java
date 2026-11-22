/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class Builder {
/*     */   private final Enchantment.EnchantmentDefinition definition;
/*     */   
/* 500 */   private HolderSet<Enchantment> exclusiveSet = HolderSet.empty();
/*     */   
/* 501 */   private final Map<DataComponentType<?>, List<?>> effectLists = new HashMap<>();
/*     */   
/* 502 */   private final DataComponentMap.Builder effectMapBuilder = DataComponentMap.builder();
/*     */   
/*     */   public Builder(Enchantment.EnchantmentDefinition definition) {
/* 505 */     this.definition = definition;
/*     */   }
/*     */   
/*     */   public Builder exclusiveWith(HolderSet<Enchantment> set) {
/* 509 */     this.exclusiveSet = set;
/* 510 */     return this;
/*     */   }
/*     */   
/*     */   public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> type, E effect, LootItemCondition.Builder condition) {
/* 514 */     getEffectsList(type).add(new ConditionalEffect<>(effect, Optional.of(condition.build())));
/* 515 */     return this;
/*     */   }
/*     */   
/*     */   public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> type, E effect) {
/* 519 */     getEffectsList(type).add(new ConditionalEffect<>(effect, Optional.empty()));
/* 520 */     return this;
/*     */   }
/*     */   
/*     */   public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> type, EnchantmentTarget enchanted, EnchantmentTarget affected, E effect, LootItemCondition.Builder condition) {
/* 524 */     getEffectsList(type).add(new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.of(condition.build())));
/* 525 */     return this;
/*     */   }
/*     */   
/*     */   public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> type, EnchantmentTarget enchanted, EnchantmentTarget affected, E effect) {
/* 529 */     getEffectsList(type).add(new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.empty()));
/* 530 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withEffect(DataComponentType<List<EnchantmentAttributeEffect>> type, EnchantmentAttributeEffect effect) {
/* 534 */     getEffectsList(type).add(effect);
/* 535 */     return this;
/*     */   }
/*     */   
/*     */   public <E> Builder withSpecialEffect(DataComponentType<E> type, E effect) {
/* 540 */     this.effectMapBuilder.set(type, effect);
/* 541 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withEffect(DataComponentType<Unit> type) {
/* 545 */     this.effectMapBuilder.set(type, Unit.INSTANCE);
/* 546 */     return this;
/*     */   }
/*     */   
/*     */   private <E> List<E> getEffectsList(DataComponentType<List<E>> type) {
/* 551 */     return (List<E>)this.effectLists.computeIfAbsent(type, k -> {
/*     */           ArrayList<E> newList = new ArrayList<>();
/*     */           this.effectMapBuilder.set(type, newList);
/*     */           return newList;
/*     */         });
/*     */   }
/*     */   
/*     */   public Enchantment build(Identifier descriptionKey) {
/* 559 */     return new Enchantment((Component)Component.translatable(Util.makeDescriptionId("enchantment", descriptionKey)), this.definition, this.exclusiveSet, this.effectMapBuilder.build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\Enchantment$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */