/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.predicates.BlockPredicate;
/*     */ import net.minecraft.advancements.predicates.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.predicates.FoodPredicate;
/*     */ import net.minecraft.advancements.predicates.GameTypePredicate;
/*     */ import net.minecraft.advancements.predicates.ItemPredicate;
/*     */ import net.minecraft.advancements.predicates.LocationPredicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.advancements.predicates.TagPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityTypePredicate;
/*     */ import net.minecraft.advancements.predicates.entity.MovementPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.PlayerPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.ConstantFloat;
/*     */ import net.minecraft.util.valueproviders.FloatProvider;
/*     */ import net.minecraft.util.valueproviders.UniformFloat;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.effects.AddValue;
/*     */ import net.minecraft.world.item.enchantment.effects.AllOf;
/*     */ import net.minecraft.world.item.enchantment.effects.ApplyEntityImpulse;
/*     */ import net.minecraft.world.item.enchantment.effects.ApplyExhaustion;
/*     */ import net.minecraft.world.item.enchantment.effects.ApplyMobEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.ChangeItemDamage;
/*     */ import net.minecraft.world.item.enchantment.effects.DamageEntity;
/*     */ import net.minecraft.world.item.enchantment.effects.DamageImmunity;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.ExplodeEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.Ignite;
/*     */ import net.minecraft.world.item.enchantment.effects.MultiplyValue;
/*     */ import net.minecraft.world.item.enchantment.effects.PlaySoundEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.RemoveBinomial;
/*     */ import net.minecraft.world.item.enchantment.effects.ReplaceDisk;
/*     */ import net.minecraft.world.item.enchantment.effects.SetValue;
/*     */ import net.minecraft.world.item.enchantment.effects.SpawnParticlesEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.SummonEntityEffect;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.EnchantmentActiveCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.MatchTool;
/*     */ import net.minecraft.world.level.storage.loot.predicates.WeatherCheck;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.EnchantmentLevelProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Enchantments {
/* 100 */   public static final ResourceKey<Enchantment> PROTECTION = key("protection");
/*     */   
/* 101 */   public static final ResourceKey<Enchantment> FIRE_PROTECTION = key("fire_protection");
/*     */   
/* 102 */   public static final ResourceKey<Enchantment> FEATHER_FALLING = key("feather_falling");
/*     */   
/* 103 */   public static final ResourceKey<Enchantment> BLAST_PROTECTION = key("blast_protection");
/*     */   
/* 104 */   public static final ResourceKey<Enchantment> PROJECTILE_PROTECTION = key("projectile_protection");
/*     */   
/* 105 */   public static final ResourceKey<Enchantment> RESPIRATION = key("respiration");
/*     */   
/* 106 */   public static final ResourceKey<Enchantment> AQUA_AFFINITY = key("aqua_affinity");
/*     */   
/* 107 */   public static final ResourceKey<Enchantment> THORNS = key("thorns");
/*     */   
/* 108 */   public static final ResourceKey<Enchantment> DEPTH_STRIDER = key("depth_strider");
/*     */   
/* 109 */   public static final ResourceKey<Enchantment> FROST_WALKER = key("frost_walker");
/*     */   
/* 110 */   public static final ResourceKey<Enchantment> BINDING_CURSE = key("binding_curse");
/*     */   
/* 111 */   public static final ResourceKey<Enchantment> SOUL_SPEED = key("soul_speed");
/*     */   
/* 112 */   public static final ResourceKey<Enchantment> SWIFT_SNEAK = key("swift_sneak");
/*     */   
/* 115 */   public static final ResourceKey<Enchantment> SHARPNESS = key("sharpness");
/*     */   
/* 116 */   public static final ResourceKey<Enchantment> SMITE = key("smite");
/*     */   
/* 117 */   public static final ResourceKey<Enchantment> BANE_OF_ARTHROPODS = key("bane_of_arthropods");
/*     */   
/* 118 */   public static final ResourceKey<Enchantment> KNOCKBACK = key("knockback");
/*     */   
/* 119 */   public static final ResourceKey<Enchantment> FIRE_ASPECT = key("fire_aspect");
/*     */   
/* 120 */   public static final ResourceKey<Enchantment> LOOTING = key("looting");
/*     */   
/* 121 */   public static final ResourceKey<Enchantment> SWEEPING_EDGE = key("sweeping_edge");
/*     */   
/* 124 */   public static final ResourceKey<Enchantment> EFFICIENCY = key("efficiency");
/*     */   
/* 125 */   public static final ResourceKey<Enchantment> SILK_TOUCH = key("silk_touch");
/*     */   
/* 126 */   public static final ResourceKey<Enchantment> UNBREAKING = key("unbreaking");
/*     */   
/* 127 */   public static final ResourceKey<Enchantment> FORTUNE = key("fortune");
/*     */   
/* 130 */   public static final ResourceKey<Enchantment> POWER = key("power");
/*     */   
/* 131 */   public static final ResourceKey<Enchantment> PUNCH = key("punch");
/*     */   
/* 132 */   public static final ResourceKey<Enchantment> FLAME = key("flame");
/*     */   
/* 133 */   public static final ResourceKey<Enchantment> INFINITY = key("infinity");
/*     */   
/* 136 */   public static final ResourceKey<Enchantment> LUCK_OF_THE_SEA = key("luck_of_the_sea");
/*     */   
/* 137 */   public static final ResourceKey<Enchantment> LURE = key("lure");
/*     */   
/* 140 */   public static final ResourceKey<Enchantment> LOYALTY = key("loyalty");
/*     */   
/* 141 */   public static final ResourceKey<Enchantment> IMPALING = key("impaling");
/*     */   
/* 142 */   public static final ResourceKey<Enchantment> RIPTIDE = key("riptide");
/*     */   
/* 143 */   public static final ResourceKey<Enchantment> CHANNELING = key("channeling");
/*     */   
/* 146 */   public static final ResourceKey<Enchantment> MULTISHOT = key("multishot");
/*     */   
/* 147 */   public static final ResourceKey<Enchantment> QUICK_CHARGE = key("quick_charge");
/*     */   
/* 148 */   public static final ResourceKey<Enchantment> PIERCING = key("piercing");
/*     */   
/* 151 */   public static final ResourceKey<Enchantment> DENSITY = key("density");
/*     */   
/* 152 */   public static final ResourceKey<Enchantment> BREACH = key("breach");
/*     */   
/* 153 */   public static final ResourceKey<Enchantment> WIND_BURST = key("wind_burst");
/*     */   
/* 156 */   public static final ResourceKey<Enchantment> LUNGE = key("lunge");
/*     */   
/* 159 */   public static final ResourceKey<Enchantment> MENDING = key("mending");
/*     */   
/* 160 */   public static final ResourceKey<Enchantment> VANISHING_CURSE = key("vanishing_curse");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<Enchantment> context) {
/* 163 */     HolderGetter<DamageType> damageTypes = context.lookup(Registries.DAMAGE_TYPE);
/* 164 */     HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
/* 165 */     HolderGetter<Item> items = context.lookup(Registries.ITEM);
/* 166 */     HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
/* 167 */     HolderGetter<EntityType<?>> entityTypes = context.lookup(Registries.ENTITY_TYPE);
/* 170 */     register(context, PROTECTION, 
/* 171 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), 10, 4, Enchantment.dynamicCost(1, 11), Enchantment.dynamicCost(12, 11), 1, new EquipmentSlotGroup[] { EquipmentSlotGroup.ARMOR })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
/* 173 */         .withEffect(EnchantmentEffectComponents.DAMAGE_PROTECTION, new AddValue(
/*     */             
/* 175 */             LevelBasedValue.perLevel(1.0F)), 
/* 176 */           DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY)))));
/* 179 */     register(context, FIRE_PROTECTION, 
/* 180 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), 5, 4, Enchantment.dynamicCost(10, 8), Enchantment.dynamicCost(18, 8), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.ARMOR })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
/* 182 */         .withEffect(EnchantmentEffectComponents.DAMAGE_PROTECTION, new AddValue(
/*     */             
/* 184 */             LevelBasedValue.perLevel(2.0F)), 
/* 185 */           (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] { DamageSourceCondition.hasDamageSource(
/* 187 */                 DamageSourcePredicate.Builder.damageType()
/* 188 */                 .tag(TagPredicate.is(DamageTypeTags.IS_FIRE))
/* 189 */                 .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))) })).withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 195 */             Identifier.withDefaultNamespace("enchantment.fire_protection"), Attributes.BURNING_TIME, LevelBasedValue.perLevel(-0.15F), AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));
/* 198 */     register(context, FEATHER_FALLING, 
/* 199 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE), 5, 4, Enchantment.dynamicCost(5, 6), Enchantment.dynamicCost(11, 6), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.ARMOR })).withEffect(EnchantmentEffectComponents.DAMAGE_PROTECTION, new AddValue(
/*     */             
/* 202 */             LevelBasedValue.perLevel(3.0F)), 
/* 203 */           DamageSourceCondition.hasDamageSource(
/* 204 */             DamageSourcePredicate.Builder.damageType()
/* 205 */             .tag(TagPredicate.is(DamageTypeTags.IS_FALL))
/* 206 */             .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY)))));
/* 210 */     register(context, BLAST_PROTECTION, 
/* 211 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), 2, 4, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(13, 8), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.ARMOR })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
/* 213 */         .withEffect(EnchantmentEffectComponents.DAMAGE_PROTECTION, new AddValue(
/*     */             
/* 215 */             LevelBasedValue.perLevel(2.0F)), 
/* 216 */           DamageSourceCondition.hasDamageSource(
/* 217 */             DamageSourcePredicate.Builder.damageType()
/* 218 */             .tag(TagPredicate.is(DamageTypeTags.IS_EXPLOSION))
/* 219 */             .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))))
/*     */         
/* 222 */         .withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 224 */             Identifier.withDefaultNamespace("enchantment.blast_protection"), Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, LevelBasedValue.perLevel(0.15F), AttributeModifier.Operation.ADD_VALUE)));
/* 227 */     register(context, PROJECTILE_PROTECTION, 
/* 228 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), 5, 4, Enchantment.dynamicCost(3, 6), Enchantment.dynamicCost(9, 6), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.ARMOR })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
/* 230 */         .withEffect(EnchantmentEffectComponents.DAMAGE_PROTECTION, new AddValue(
/*     */             
/* 232 */             LevelBasedValue.perLevel(2.0F)), 
/* 233 */           DamageSourceCondition.hasDamageSource(
/* 234 */             DamageSourcePredicate.Builder.damageType()
/* 235 */             .tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))
/* 236 */             .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY)))));
/* 241 */     register(context, RESPIRATION, 
/* 242 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(10, 10), Enchantment.dynamicCost(40, 10), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.HEAD })).withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 245 */             Identifier.withDefaultNamespace("enchantment.respiration"), Attributes.OXYGEN_BONUS, LevelBasedValue.perLevel(1.0F), AttributeModifier.Operation.ADD_VALUE)));
/* 248 */     register(context, AQUA_AFFINITY, 
/* 249 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE), 2, 1, Enchantment.constantCost(1), Enchantment.constantCost(41), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.HEAD })).withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 252 */             Identifier.withDefaultNamespace("enchantment.aqua_affinity"), Attributes.SUBMERGED_MINING_SPEED, LevelBasedValue.perLevel(4.0F), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)));
/* 255 */     register(context, THORNS, 
/* 256 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), (HolderSet<Item>)items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE), 1, 3, Enchantment.dynamicCost(10, 20), Enchantment.dynamicCost(60, 20), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.ANY })).withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.VICTIM, EnchantmentTarget.ATTACKER, 
/*     */           
/* 261 */           AllOf.entityEffects(new EnchantmentEntityEffect[] { (EnchantmentEntityEffect)new DamageEntity(LevelBasedValue.constant(1.0F), LevelBasedValue.constant(5.0F), (Holder)damageTypes.getOrThrow(DamageTypes.THORNS)), (EnchantmentEntityEffect)new ChangeItemDamage(
/* 263 */                 LevelBasedValue.constant(2.0F)) }), LootItemRandomChanceCondition.randomChance(
/* 266 */             (NumberProvider)EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))));
/* 271 */     register(context, DEPTH_STRIDER, 
/* 272 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(10, 10), Enchantment.dynamicCost(25, 10), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.FEET })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.BOOTS_EXCLUSIVE))
/* 274 */         .withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 276 */             Identifier.withDefaultNamespace("enchantment.depth_strider"), Attributes.WATER_MOVEMENT_EFFICIENCY, LevelBasedValue.perLevel(0.33333334F), AttributeModifier.Operation.ADD_VALUE)));
/* 279 */     register(context, FROST_WALKER, 
/* 280 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE), 2, 2, Enchantment.dynamicCost(10, 10), Enchantment.dynamicCost(25, 10), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.FEET })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.BOOTS_EXCLUSIVE))
/* 282 */         .<DamageImmunity>withEffect(EnchantmentEffectComponents.DAMAGE_IMMUNITY, DamageImmunity.INSTANCE, 
/*     */           
/* 285 */           DamageSourceCondition.hasDamageSource(
/* 286 */             DamageSourcePredicate.Builder.damageType()
/* 287 */             .tag(TagPredicate.is(DamageTypeTags.BURN_FROM_STEPPING))
/* 288 */             .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))))
/*     */         
/* 291 */         .withEffect(EnchantmentEffectComponents.LOCATION_CHANGED, new ReplaceDisk(new LevelBasedValue.Clamped(
/*     */               
/* 294 */               LevelBasedValue.perLevel(3.0F, 1.0F), 0.0F, 16.0F), 
/* 295 */             LevelBasedValue.constant(1.0F), new Vec3i(0, -1, 0), 
/*     */             
/* 297 */             Optional.of(
/* 298 */               BlockPredicate.allOf(new BlockPredicate[] { BlockPredicate.matchesTag(new Vec3i(0, 1, 0), BlockTags.AIR), 
/* 300 */                   BlockPredicate.matchesBlocks(new Block[] { Blocks.WATER }), BlockPredicate.matchesFluids(new Fluid[] { (Fluid)Fluids.WATER }), BlockPredicate.unobstructed() })), (BlockStateProvider)BlockStateProvider.simple(Blocks.FROSTED_ICE), 
/* 306 */             Optional.of(GameEvent.BLOCK_PLACE)), 
/*     */           
/* 308 */           (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] { LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnGround(Boolean.valueOf(true)))), 
/* 310 */               InvertedLootItemCondition.invert(
/* 311 */                 LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
/* 312 */                   .vehicle(EntityPredicate.Builder.entity()))) })));
/* 318 */     register(context, BINDING_CURSE, 
/* 319 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.EQUIPPABLE_ENCHANTABLE), 1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.ARMOR })).withEffect(EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE));
/* 329 */     EntityPredicate.Builder soulSpeedEffectCondition = EntityPredicate.Builder.entity().periodicTick(5).flags(EntityFlagsPredicate.Builder.flags().setIsFlying(Boolean.valueOf(false)).setOnGround(Boolean.valueOf(true))).moving(MovementPredicate.horizontalSpeed(MinMaxBounds.Doubles.atLeast(9.999999747378752E-6D))).movementAffectedBy(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, BlockTags.SOUL_SPEED_BLOCKS)));
/* 331 */     AllOfCondition.Builder soulSpeedMovementCondition = AllOfCondition.allOf(new LootItemCondition.Builder[] { InvertedLootItemCondition.invert(
/* 333 */             LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
/* 334 */               .vehicle(EntityPredicate.Builder.entity()))), 
/*     */           
/* 337 */           (LootItemCondition.Builder)AnyOfCondition.anyOf(new LootItemCondition.Builder[] { (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] { EnchantmentActiveCheck.enchantmentActiveCheck(), 
/*     */                   
/* 341 */                   LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setIsFlying(Boolean.valueOf(false)))), 
/* 342 */                   (LootItemCondition.Builder)AnyOfCondition.anyOf(new LootItemCondition.Builder[] { LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().movementAffectedBy(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, BlockTags.SOUL_SPEED_BLOCKS)))), 
/* 344 */                       LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnGround(Boolean.valueOf(false))).build()) }) }), (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] { EnchantmentActiveCheck.enchantmentInactiveCheck(), 
/* 349 */                   LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
/* 350 */                     .movementAffectedBy(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, BlockTags.SOUL_SPEED_BLOCKS)))
/* 351 */                     .flags(EntityFlagsPredicate.Builder.flags().setIsFlying(Boolean.valueOf(false)))) }) }) });
/* 357 */     register(context, SOUL_SPEED, 
/* 358 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE), 1, 3, Enchantment.dynamicCost(10, 10), Enchantment.dynamicCost(25, 10), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.FEET })).withEffect(EnchantmentEffectComponents.LOCATION_CHANGED, 
/*     */           
/* 361 */           AllOf.locationBasedEffects(new EnchantmentLocationBasedEffect[] { (EnchantmentLocationBasedEffect)new EnchantmentAttributeEffect(Identifier.withDefaultNamespace("enchantment.soul_speed"), Attributes.MOVEMENT_SPEED, LevelBasedValue.perLevel(0.0405F, 0.0105F), AttributeModifier.Operation.ADD_VALUE), (EnchantmentLocationBasedEffect)new EnchantmentAttributeEffect(
/* 363 */                 Identifier.withDefaultNamespace("enchantment.soul_speed"), Attributes.MOVEMENT_EFFICIENCY, LevelBasedValue.constant(1.0F), AttributeModifier.Operation.ADD_VALUE) }), (LootItemCondition.Builder)soulSpeedMovementCondition).withEffect(EnchantmentEffectComponents.LOCATION_CHANGED, new ChangeItemDamage(
/*     */             
/* 369 */             LevelBasedValue.constant(1.0F)), 
/* 370 */           (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] { LootItemRandomChanceCondition.randomChance((NumberProvider)EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.constant(0.04F))), 
/* 372 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
/* 373 */                 .flags(EntityFlagsPredicate.Builder.flags().setOnGround(Boolean.valueOf(true)))
/* 374 */                 .movementAffectedBy(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, BlockTags.SOUL_SPEED_BLOCKS)))) })).withEffect(EnchantmentEffectComponents.TICK, new SpawnParticlesEffect((ParticleOptions)ParticleTypes.SOUL, 
/*     */             
/* 380 */             SpawnParticlesEffect.inBoundingBox(), SpawnParticlesEffect.offsetFromEntityPosition(0.1F), SpawnParticlesEffect.movementScaled(-0.2F), SpawnParticlesEffect.fixedVelocity((FloatProvider)ConstantFloat.of(0.1F)), (FloatProvider)ConstantFloat.of(1.0F)), 
/* 381 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, soulSpeedEffectCondition))
/*     */         
/* 383 */         .withEffect(EnchantmentEffectComponents.TICK, new PlaySoundEffect(
/*     */             
/* 385 */             List.of(SoundEvents.SOUL_ESCAPE), (FloatProvider)ConstantFloat.of(0.6F), (FloatProvider)UniformFloat.of(0.6F, 1.0F)), 
/* 386 */           (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] { LootItemRandomChanceCondition.randomChance(0.35F), 
/* 388 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, soulSpeedEffectCondition) })));
/* 392 */     register(context, SWIFT_SNEAK, 
/* 393 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE), 1, 3, Enchantment.dynamicCost(25, 25), Enchantment.dynamicCost(75, 25), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.LEGS })).withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 396 */             Identifier.withDefaultNamespace("enchantment.swift_sneak"), Attributes.SNEAKING_SPEED, LevelBasedValue.perLevel(0.15F), AttributeModifier.Operation.ADD_VALUE)));
/* 401 */     register(context, SHARPNESS, 
/* 402 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE), (HolderSet<Item>)items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE), 10, 5, Enchantment.dynamicCost(1, 11), Enchantment.dynamicCost(21, 11), 1, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
/* 404 */         .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(
/*     */             
/* 406 */             LevelBasedValue.perLevel(1.0F, 0.5F))));
/* 409 */     register(context, SMITE, 
/* 410 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE), (HolderSet<Item>)items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE), 5, 5, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(25, 8), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
/* 412 */         .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(
/*     */             
/* 414 */             LevelBasedValue.perLevel(2.5F)), 
/* 415 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityTypeTags.SENSITIVE_TO_SMITE)))));
/* 418 */     register(context, BANE_OF_ARTHROPODS, 
/* 419 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE), (HolderSet<Item>)items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE), 5, 5, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(25, 8), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
/* 421 */         .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(
/*     */             
/* 423 */             LevelBasedValue.perLevel(2.5F)), 
/* 424 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, 
/*     */             
/* 426 */             EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS))))
/*     */         
/* 429 */         .withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM, new ApplyMobEffect(
/*     */             
/* 433 */             (HolderSet)HolderSet.direct(new Holder[] { MobEffects.SLOWNESS }, ), LevelBasedValue.constant(1.5F), LevelBasedValue.perLevel(1.5F, 0.5F), LevelBasedValue.constant(3.0F), LevelBasedValue.constant(3.0F)), 
/* 434 */           (LootItemCondition.Builder)LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, 
/*     */             
/* 436 */             EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS)))
/* 437 */           .and(
/* 438 */             DamageSourceCondition.hasDamageSource(
/* 439 */               DamageSourcePredicate.Builder.damageType().isDirect(true)))));
/* 444 */     register(context, KNOCKBACK, 
/* 445 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE), 5, 2, Enchantment.dynamicCost(5, 20), Enchantment.dynamicCost(55, 20), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).withEffect(EnchantmentEffectComponents.KNOCKBACK, new AddValue(
/*     */             
/* 448 */             LevelBasedValue.perLevel(1.0F))));
/* 451 */     register(context, FIRE_ASPECT, 
/* 452 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.FIRE_ASPECT_ENCHANTABLE), (HolderSet<Item>)items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE), 2, 2, Enchantment.dynamicCost(10, 20), Enchantment.dynamicCost(60, 20), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM, new Ignite(
/*     */             
/* 457 */             LevelBasedValue.perLevel(4.0F)), 
/* 458 */           DamageSourceCondition.hasDamageSource(
/* 459 */             DamageSourcePredicate.Builder.damageType().isDirect(true))));
/* 463 */     register(context, LOOTING, 
/* 464 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).withEffect(EnchantmentEffectComponents.EQUIPMENT_DROPS, EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM, new AddValue(
/*     */             
/* 469 */             LevelBasedValue.perLevel(0.01F)), 
/* 470 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityTypes.PLAYER)))));
/* 473 */     register(context, SWEEPING_EDGE, 
/* 474 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.SWEEPING_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(5, 9), Enchantment.dynamicCost(20, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 477 */             Identifier.withDefaultNamespace("enchantment.sweeping_edge"), Attributes.SWEEPING_DAMAGE_RATIO, new LevelBasedValue.Fraction(LevelBasedValue.perLevel(1.0F), LevelBasedValue.perLevel(2.0F, 1.0F)), AttributeModifier.Operation.ADD_VALUE)));
/* 482 */     register(context, EFFICIENCY, 
/* 483 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.MINING_ENCHANTABLE), 10, 5, Enchantment.dynamicCost(1, 10), Enchantment.dynamicCost(51, 10), 1, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
/*     */             
/* 486 */             Identifier.withDefaultNamespace("enchantment.efficiency"), Attributes.MINING_EFFICIENCY, new LevelBasedValue.LevelsSquared(1.0F), AttributeModifier.Operation.ADD_VALUE)));
/* 489 */     register(context, SILK_TOUCH, 
/* 490 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.MINING_LOOT_ENCHANTABLE), 1, 1, Enchantment.constantCost(15), Enchantment.constantCost(65), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.MINING_EXCLUSIVE))
/* 492 */         .withEffect(EnchantmentEffectComponents.BLOCK_EXPERIENCE, new SetValue(
/*     */             
/* 494 */             LevelBasedValue.constant(0.0F))));
/* 497 */     register(context, UNBREAKING, 
/* 498 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE), 5, 3, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(55, 8), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.ANY })).withEffect(EnchantmentEffectComponents.ITEM_DAMAGE, new RemoveBinomial(new LevelBasedValue.Fraction(
/*     */               
/* 502 */               LevelBasedValue.perLevel(2.0F), LevelBasedValue.perLevel(10.0F, 5.0F))), 
/* 503 */           MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, ItemTags.ARMOR_ENCHANTABLE)))
/*     */         
/* 505 */         .withEffect(EnchantmentEffectComponents.ITEM_DAMAGE, new RemoveBinomial(new LevelBasedValue.Fraction(
/*     */               
/* 507 */               LevelBasedValue.perLevel(1.0F), LevelBasedValue.perLevel(2.0F, 1.0F))), 
/* 508 */           InvertedLootItemCondition.invert(
/* 509 */             MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, ItemTags.ARMOR_ENCHANTABLE)))));
/* 514 */     register(context, FORTUNE, 
/* 515 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.MINING_LOOT_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.MINING_EXCLUSIVE)));
/* 520 */     register(context, POWER, 
/* 521 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.BOW_ENCHANTABLE), 10, 5, Enchantment.dynamicCost(1, 10), Enchantment.dynamicCost(16, 10), 1, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(
/*     */             
/* 524 */             LevelBasedValue.perLevel(1.0F, 0.5F)), 
/* 525 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, EntityPredicate.Builder.entity().of(entityTypes, EntityTypeTags.ARROWS).build())));
/* 528 */     register(context, PUNCH, 
/* 529 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.BOW_ENCHANTABLE), 2, 2, Enchantment.dynamicCost(12, 20), Enchantment.dynamicCost(37, 20), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).withEffect(EnchantmentEffectComponents.KNOCKBACK, new AddValue(
/*     */             
/* 532 */             LevelBasedValue.perLevel(1.0F)), 
/* 533 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, EntityPredicate.Builder.entity().of(entityTypes, EntityTypeTags.ARROWS).build())));
/* 536 */     register(context, FLAME, 
/* 537 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.BOW_ENCHANTABLE), 2, 1, Enchantment.constantCost(20), Enchantment.constantCost(50), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).withEffect(EnchantmentEffectComponents.PROJECTILE_SPAWNED, new Ignite(
/*     */             
/* 540 */             LevelBasedValue.constant(100.0F))));
/* 543 */     register(context, INFINITY, 
/* 544 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.BOW_ENCHANTABLE), 1, 1, Enchantment.constantCost(20), Enchantment.constantCost(50), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.BOW_EXCLUSIVE))
/* 546 */         .withEffect(EnchantmentEffectComponents.AMMO_USE, new SetValue(
/*     */             
/* 548 */             LevelBasedValue.constant(0.0F)), 
/* 549 */           MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, new ItemLike[] { (ItemLike)Items.ARROW }))));
/* 554 */     register(context, LUCK_OF_THE_SEA, 
/* 555 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.FISHING_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).withEffect(EnchantmentEffectComponents.FISHING_LUCK_BONUS, new AddValue(
/*     */             
/* 558 */             LevelBasedValue.perLevel(1.0F))));
/* 561 */     register(context, LURE, 
/* 562 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.FISHING_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).withEffect(EnchantmentEffectComponents.FISHING_TIME_REDUCTION, new AddValue(
/*     */             
/* 565 */             LevelBasedValue.perLevel(5.0F))));
/* 570 */     register(context, LOYALTY, 
/* 571 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE), 5, 3, Enchantment.dynamicCost(12, 7), Enchantment.constantCost(50), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).withEffect(EnchantmentEffectComponents.TRIDENT_RETURN_ACCELERATION, new AddValue(
/*     */             
/* 574 */             LevelBasedValue.perLevel(1.0F))));
/* 577 */     register(context, IMPALING, 
/* 578 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE), 2, 5, Enchantment.dynamicCost(1, 8), Enchantment.dynamicCost(21, 8), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
/* 580 */         .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(
/*     */             
/* 582 */             LevelBasedValue.perLevel(2.5F)), 
/* 583 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityTypeTags.SENSITIVE_TO_IMPALING)).build())));
/* 586 */     register(context, RIPTIDE, 
/* 587 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(17, 7), Enchantment.constantCost(50), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.HAND })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.RIPTIDE_EXCLUSIVE))
/* 589 */         .withSpecialEffect(EnchantmentEffectComponents.TRIDENT_SPIN_ATTACK_STRENGTH, new AddValue(
/*     */             
/* 591 */             LevelBasedValue.perLevel(1.5F, 0.75F)))
/*     */         
/* 593 */         .withSpecialEffect(EnchantmentEffectComponents.TRIDENT_SOUND, 
/*     */           
/* 595 */           List.of(SoundEvents.TRIDENT_RIPTIDE_1, SoundEvents.TRIDENT_RIPTIDE_2, SoundEvents.TRIDENT_RIPTIDE_3)));
/* 603 */     register(context, LUNGE, 
/* 604 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.LUNGE_ENCHANTABLE), 5, 3, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(25, 8), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.HAND })).withEffect(EnchantmentEffectComponents.POST_PIERCING_ATTACK, 
/*     */           
/* 607 */           AllOf.entityEffects(new EnchantmentEntityEffect[] { (EnchantmentEntityEffect)new ChangeItemDamage(new LevelBasedValue.Constant(1.0F)), (EnchantmentEntityEffect)new ApplyExhaustion(LevelBasedValue.perLevel(4.0F)), (EnchantmentEntityEffect)new ApplyEntityImpulse(new Vec3(0.0D, 0.0D, 1.0D), new Vec3(1.0D, 0.0D, 1.0D), 
/* 610 */                 LevelBasedValue.perLevel(0.458F)), (EnchantmentEntityEffect)new PlaySoundEffect(
/* 611 */                 List.of(SoundEvents.LUNGE_1, SoundEvents.LUNGE_2, SoundEvents.LUNGE_3), (FloatProvider)ConstantFloat.of(1.0F), (FloatProvider)ConstantFloat.of(1.0F)) }), (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] { InvertedLootItemCondition.invert(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().vehicle(EntityPredicate.Builder.entity()))), 
/* 615 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setIsFallFlying(Boolean.valueOf(false)))), 
/* 616 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setIsInWater(Boolean.valueOf(false)))), 
/* 617 */               (LootItemCondition.Builder)AnyOfCondition.anyOf(new LootItemCondition.Builder[] { InvertedLootItemCondition.invert(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().player(PlayerPredicate.Builder.player().build()))), 
/* 619 */                   LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().player(PlayerPredicate.Builder.player().setGameType(GameTypePredicate.of(new GameType[] { GameType.CREATIVE })).build())), 
/* 620 */                   LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().player(PlayerPredicate.Builder.player().setFood(FoodPredicate.Builder.food().withLevel(MinMaxBounds.Ints.atLeast(Mth.floor(6.0F) + 1)).build()).build())) }) })));
/* 626 */     register(context, CHANNELING, 
/* 627 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE), 1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM, 
/*     */           
/* 632 */           AllOf.entityEffects(new EnchantmentEntityEffect[] { (EnchantmentEntityEffect)new SummonEntityEffect((HolderSet)HolderSet.direct(new Holder[] { (Holder)EntityTypes.LIGHTNING_BOLT.builtInRegistryHolder() }, ), false), (EnchantmentEntityEffect)new PlaySoundEffect(
/* 634 */                 List.of(SoundEvents.TRIDENT_THUNDER), (FloatProvider)ConstantFloat.of(5.0F), (FloatProvider)ConstantFloat.of(1.0F)) }), (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] { (LootItemCondition.Builder)WeatherCheck.weather().setThundering(true), 
/* 638 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().located(LocationPredicate.Builder.location().setCanSeeSky(true))), 
/* 639 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, EntityPredicate.Builder.entity().of(entityTypes, EntityTypes.TRIDENT)) })).withEffect(EnchantmentEffectComponents.HIT_BLOCK, 
/*     */           
/* 644 */           AllOf.entityEffects(new EnchantmentEntityEffect[] { (EnchantmentEntityEffect)new SummonEntityEffect((HolderSet)HolderSet.direct(new Holder[] { (Holder)EntityTypes.LIGHTNING_BOLT.builtInRegistryHolder() }, ), false), (EnchantmentEntityEffect)new PlaySoundEffect(
/* 646 */                 List.of(SoundEvents.TRIDENT_THUNDER), (FloatProvider)ConstantFloat.of(5.0F), (FloatProvider)ConstantFloat.of(1.0F)) }), (LootItemCondition.Builder)AllOfCondition.allOf(new LootItemCondition.Builder[] { (LootItemCondition.Builder)WeatherCheck.weather().setThundering(true), 
/* 650 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().of(entityTypes, EntityTypes.TRIDENT)), 
/* 651 */               LocationCheck.checkLocation(LocationPredicate.Builder.location().setCanSeeSky(true).setBlock(BlockPredicate.Builder.block().of(blocks, BlockTags.LIGHTNING_RODS))) })));
/* 657 */     register(context, MULTISHOT, 
/* 658 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE), 2, 1, Enchantment.constantCost(20), Enchantment.constantCost(50), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.CROSSBOW_EXCLUSIVE))
/* 660 */         .withEffect(EnchantmentEffectComponents.PROJECTILE_COUNT, new AddValue(
/*     */             
/* 662 */             LevelBasedValue.perLevel(2.0F)))
/*     */         
/* 664 */         .withEffect(EnchantmentEffectComponents.PROJECTILE_SPREAD, new AddValue(
/*     */             
/* 666 */             LevelBasedValue.perLevel(10.0F))));
/* 669 */     register(context, QUICK_CHARGE, 
/* 670 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE), 5, 3, Enchantment.dynamicCost(12, 20), Enchantment.constantCost(50), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND, EquipmentSlotGroup.OFFHAND })).withSpecialEffect(EnchantmentEffectComponents.CROSSBOW_CHARGE_TIME, new AddValue(
/*     */             
/* 673 */             LevelBasedValue.perLevel(-0.25F)))
/*     */         
/* 675 */         .withSpecialEffect(EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS, 
/*     */           
/* 677 */           List.of(new CrossbowItem.ChargingSounds(
/* 678 */               Optional.of(SoundEvents.CROSSBOW_QUICK_CHARGE_1), Optional.empty(), Optional.of(SoundEvents.CROSSBOW_LOADING_END)), new CrossbowItem.ChargingSounds(
/* 679 */               Optional.of(SoundEvents.CROSSBOW_QUICK_CHARGE_2), Optional.empty(), Optional.of(SoundEvents.CROSSBOW_LOADING_END)), new CrossbowItem.ChargingSounds(
/* 680 */               Optional.of(SoundEvents.CROSSBOW_QUICK_CHARGE_3), Optional.empty(), Optional.of(SoundEvents.CROSSBOW_LOADING_END)))));
/* 684 */     register(context, PIERCING, 
/* 685 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE), 10, 4, Enchantment.dynamicCost(1, 10), Enchantment.constantCost(50), 1, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.CROSSBOW_EXCLUSIVE))
/* 687 */         .withEffect(EnchantmentEffectComponents.PROJECTILE_PIERCING, new AddValue(
/*     */             
/* 689 */             LevelBasedValue.perLevel(1.0F))));
/* 694 */     register(context, DENSITY, 
/* 695 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.MACE_ENCHANTABLE), 5, 5, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(25, 8), 2, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
/* 697 */         .withEffect(EnchantmentEffectComponents.SMASH_DAMAGE_PER_FALLEN_BLOCK, new AddValue(
/*     */             
/* 699 */             LevelBasedValue.perLevel(0.5F))));
/* 702 */     register(context, BREACH, 
/* 703 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.MACE_ENCHANTABLE), 2, 4, Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).exclusiveWith((HolderSet<Enchantment>)enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
/* 705 */         .withEffect(EnchantmentEffectComponents.ARMOR_EFFECTIVENESS, new AddValue(
/*     */             
/* 707 */             LevelBasedValue.perLevel(-0.15F))));
/* 710 */     register(context, WIND_BURST, 
/* 711 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.MACE_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.MAINHAND })).withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER, EnchantmentTarget.ATTACKER, new ExplodeEffect(false, 
/*     */             
/* 718 */             Optional.empty(), 
/* 719 */             Optional.of(LevelBasedValue.lookup(List.of(Float.valueOf(1.2F), Float.valueOf(1.75F), Float.valueOf(2.2F)), LevelBasedValue.perLevel(1.5F, 0.35F))), blocks
/* 720 */             .get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()), Vec3.ZERO, 
/*     */             
/* 722 */             LevelBasedValue.constant(3.5F), false, Level.ExplosionInteraction.TRIGGER, (ParticleOptions)ParticleTypes.GUST_EMITTER_SMALL, (ParticleOptions)ParticleTypes.GUST_EMITTER_LARGE, 
/*     */             
/* 727 */             WeightedList.of(), (Holder)SoundEvents.WIND_CHARGE_BURST), 
/*     */           
/* 730 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, 
/*     */             
/* 732 */             EntityPredicate.Builder.entity()
/* 733 */             .flags(EntityFlagsPredicate.Builder.flags()
/* 734 */               .setIsFlying(Boolean.valueOf(false)))
/*     */             
/* 736 */             .moving(
/* 737 */               MovementPredicate.fallDistance(MinMaxBounds.Doubles.atLeast(1.5D))))));
/* 744 */     register(context, MENDING, 
/* 745 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE), 2, 1, Enchantment.dynamicCost(25, 25), Enchantment.dynamicCost(75, 25), 4, new EquipmentSlotGroup[] { EquipmentSlotGroup.ANY })).withEffect(EnchantmentEffectComponents.REPAIR_WITH_XP, new MultiplyValue(
/*     */             
/* 748 */             LevelBasedValue.constant(2.0F))));
/* 751 */     register(context, VANISHING_CURSE, 
/* 752 */         Enchantment.enchantment(Enchantment.definition((HolderSet<Item>)items.getOrThrow(ItemTags.VANISHING_ENCHANTABLE), 1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 8, new EquipmentSlotGroup[] { EquipmentSlotGroup.ANY })).withEffect(EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP));
/*     */   }
/*     */   
/*     */   private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
/* 760 */     context.register(key, builder.build(key.identifier()));
/*     */   }
/*     */   
/*     */   private static ResourceKey<Enchantment> key(String id) {
/* 764 */     return ResourceKey.create(Registries.ENCHANTMENT, Identifier.withDefaultNamespace(id));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\Enchantments.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */