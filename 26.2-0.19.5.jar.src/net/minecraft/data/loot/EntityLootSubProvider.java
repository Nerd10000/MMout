/*     */ package net.minecraft.data.loot;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.advancements.predicates.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.predicates.DataComponentMatchers;
/*     */ import net.minecraft.advancements.predicates.EnchantmentPredicate;
/*     */ import net.minecraft.advancements.predicates.ItemPredicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.advancements.predicates.entity.EntityEquipmentPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.SheepPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.component.DataComponentExactPredicate;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.component.predicates.EnchantmentsPredicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.block.ColorCollection;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*     */ import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ 
/*     */ public abstract class EntityLootSubProvider implements LootTableSubProvider {
/*     */   protected final HolderLookup.Provider registries;
/*     */   
/*     */   private final FeatureFlagSet allowed;
/*     */   
/*     */   private final FeatureFlagSet required;
/*     */   
/*     */   private final Map<EntityType<?>, Map<ResourceKey<LootTable>, LootTable.Builder>> map;
/*     */   
/*     */   protected final AnyOfCondition.Builder shouldSmeltLoot() {
/*  56 */     HolderLookup.RegistryLookup<Enchantment> enchantmentsRegistry = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*  57 */     return AnyOfCondition.anyOf(new LootItemCondition.Builder[] { LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, 
/*  59 */             EntityPredicate.Builder.entity()
/*  60 */             .flags(EntityFlagsPredicate.Builder.flags().setOnFire(Boolean.valueOf(true)))), 
/*     */           
/*  62 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, 
/*     */             
/*  64 */             EntityPredicate.Builder.entity()
/*  65 */             .equipment(EntityEquipmentPredicate.Builder.equipment()
/*  66 */               .mainhand(ItemPredicate.Builder.item()
/*  67 */                 .withComponents(DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, (DataComponentPredicate)EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate((HolderSet)enchantmentsRegistry.getOrThrow(EnchantmentTags.SMELTS_LOOT), MinMaxBounds.Ints.ANY)))).build())))) });
/*     */   }
/*     */   
/*     */   protected EntityLootSubProvider(FeatureFlagSet enabledFeatures, HolderLookup.Provider registries) {
/*  77 */     this(enabledFeatures, enabledFeatures, registries);
/*     */   }
/*     */   
/*     */   protected EntityLootSubProvider(FeatureFlagSet allowed, FeatureFlagSet required, HolderLookup.Provider registries) {
/* 107 */     this.map = Maps.newHashMap();
/*     */     this.allowed = allowed;
/*     */     this.required = required;
/*     */     this.registries = registries;
/*     */   }
/*     */   
/*     */   public static LootPool.Builder createSheepDispatchPool(ColorCollection<ResourceKey<LootTable>> tableNames) {
/*     */     AlternativesEntry.Builder variants = AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[0]);
/*     */     for (DyeColor color : DyeColor.VALUES)
/*     */       variants = variants.otherwise(NestedLootTable.lootTableReference((ResourceKey)tableNames.pick(color)).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentExactPredicate.expect(DataComponents.SHEEP_COLOR, color)).sheep(SheepPredicate.hasWool())))); 
/*     */     return LootPool.lootPool().add((LootPoolEntryContainer.Builder)variants);
/*     */   }
/*     */   
/*     */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/* 113 */     generate();
/* 115 */     Set<ResourceKey<LootTable>> seen = new HashSet<>();
/* 116 */     BuiltInRegistries.ENTITY_TYPE.listElements().forEach(holder -> {
/*     */           EntityType<?> type = (EntityType)holder.value();
/*     */           if (!type.isEnabled(this.allowed))
/*     */             return; 
/*     */           Optional<ResourceKey<LootTable>> defaultLootTable = type.getDefaultLootTable();
/*     */           if (defaultLootTable.isPresent()) {
/*     */             Map<ResourceKey<LootTable>, LootTable.Builder> builders = this.map.remove(type);
/*     */             if (type.isEnabled(this.required) && (builders == null || !builders.containsKey(defaultLootTable.get())))
/*     */               throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", new Object[] { defaultLootTable.get(), holder.key().identifier() })); 
/*     */             if (builders != null)
/*     */               builders.forEach(()); 
/*     */           } else {
/*     */             Map<ResourceKey<LootTable>, LootTable.Builder> builders = this.map.remove(type);
/*     */             if (builders != null)
/*     */               throw new IllegalStateException(String.format(Locale.ROOT, "Weird loottables '%s' for '%s', not a LivingEntity so should not have loot", new Object[] { builders.keySet().stream().map(()).collect(Collectors.joining(",")), holder.key().identifier() })); 
/*     */           } 
/*     */         });
/* 147 */     if (!this.map.isEmpty())
/* 148 */       throw new IllegalStateException("Created loot tables for entities not supported by datapack: " + String.valueOf(this.map.keySet())); 
/*     */   }
/*     */   
/*     */   protected LootItemCondition.Builder killedByFrog(HolderGetter<EntityType<?>> entityTypes) {
/* 153 */     return DamageSourceCondition.hasDamageSource(
/* 154 */         DamageSourcePredicate.Builder.damageType().source(
/* 155 */           EntityPredicate.Builder.entity().of(entityTypes, EntityTypes.FROG)));
/*     */   }
/*     */   
/*     */   protected LootItemCondition.Builder killedByFrogVariant(HolderGetter<EntityType<?>> entityTypes, HolderGetter<FrogVariant> frogVariants, ResourceKey<FrogVariant> variant) {
/* 161 */     return DamageSourceCondition.hasDamageSource(
/* 162 */         DamageSourcePredicate.Builder.damageType().source(
/* 163 */           EntityPredicate.Builder.entity()
/* 164 */           .of(entityTypes, EntityTypes.FROG)
/* 165 */           .components(DataComponentExactPredicate.expect(DataComponents.FROG_VARIANT, frogVariants.getOrThrow(variant)))));
/*     */   }
/*     */   
/*     */   protected void add(EntityType<?> type, LootTable.Builder builder) {
/* 171 */     add(type, (ResourceKey<LootTable>)type.getDefaultLootTable().orElseThrow(() -> new IllegalStateException("Entity " + String.valueOf(type) + " has no loot table")), builder);
/*     */   }
/*     */   
/*     */   protected void add(EntityType<?> type, ResourceKey<LootTable> lootTable, LootTable.Builder builder) {
/* 175 */     ((Map<ResourceKey<LootTable>, LootTable.Builder>)this.map.computeIfAbsent(type, k -> new HashMap<>())).put(lootTable, builder);
/*     */   }
/*     */   
/*     */   public abstract void generate();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\EntityLootSubProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */