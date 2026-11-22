/*     */ package net.minecraft.data.advancements.packs;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.AdvancementType;
/*     */ import net.minecraft.advancements.predicates.BlockPredicate;
/*     */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*     */ import net.minecraft.advancements.predicates.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.predicates.DistancePredicate;
/*     */ import net.minecraft.advancements.predicates.ItemPredicate;
/*     */ import net.minecraft.advancements.predicates.LocationPredicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.advancements.predicates.MobEffectsPredicate;
/*     */ import net.minecraft.advancements.predicates.StatePropertiesPredicate;
/*     */ import net.minecraft.advancements.predicates.TagPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityEquipmentPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*     */ import net.minecraft.advancements.triggers.BrewedPotionTrigger;
/*     */ import net.minecraft.advancements.triggers.ChangeDimensionTrigger;
/*     */ import net.minecraft.advancements.triggers.ConstructBeaconTrigger;
/*     */ import net.minecraft.advancements.triggers.DistanceTrigger;
/*     */ import net.minecraft.advancements.triggers.EffectsChangedTrigger;
/*     */ import net.minecraft.advancements.triggers.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.triggers.ItemDurabilityTrigger;
/*     */ import net.minecraft.advancements.triggers.ItemUsedOnLocationTrigger;
/*     */ import net.minecraft.advancements.triggers.KilledTrigger;
/*     */ import net.minecraft.advancements.triggers.LootTableTrigger;
/*     */ import net.minecraft.advancements.triggers.PickedUpItemTrigger;
/*     */ import net.minecraft.advancements.triggers.PlayerInteractTrigger;
/*     */ import net.minecraft.advancements.triggers.PlayerTrigger;
/*     */ import net.minecraft.advancements.triggers.SummonedEntityTrigger;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.RespawnAnchorBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ 
/*     */ public class VanillaNetherAdvancements implements AdvancementSubProvider {
/*     */   public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
/*  70 */     HolderLookup.RegistryLookup registryLookup1 = registries.lookupOrThrow(Registries.ENTITY_TYPE);
/*  71 */     HolderLookup.RegistryLookup registryLookup2 = registries.lookupOrThrow(Registries.ITEM);
/*  72 */     HolderLookup.RegistryLookup registryLookup3 = registries.lookupOrThrow(Registries.BLOCK);
/*  77 */     AdvancementHolder root = Advancement.Builder.advancement().display((ItemLike)Blocks.RED_NETHER_BRICKS, (Component)Component.translatable("advancements.nether.root.title"), (Component)Component.translatable("advancements.nether.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/nether"), AdvancementType.TASK, false, false, false).addCriterion("entered_nether", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER)).save(output, "nether/root");
/*  84 */     AdvancementHolder returnToSender = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.FIRE_CHARGE, (Component)Component.translatable("advancements.nether.return_to_sender.title"), (Component)Component.translatable("advancements.nether.return_to_sender.description"), null, AdvancementType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(50)).addCriterion("killed_ghast", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.GHAST), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.FIREBALL)))).save(output, "nether/return_to_sender");
/*  90 */     AdvancementHolder findFortress = Advancement.Builder.advancement().parent(root).display((ItemLike)Blocks.NETHER_BRICKS, (Component)Component.translatable("advancements.nether.find_fortress.title"), (Component)Component.translatable("advancements.nether.find_fortress.description"), null, AdvancementType.TASK, true, true, false).addCriterion("fortress", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure((Holder)registries.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.FORTRESS)))).save(output, "nether/find_fortress");
/*  92 */     Advancement.Builder.advancement()
/*  93 */       .parent(root)
/*  94 */       .display((ItemLike)Items.MAP, (Component)Component.translatable("advancements.nether.fast_travel.title"), (Component)Component.translatable("advancements.nether.fast_travel.description"), null, AdvancementType.CHALLENGE, true, true, false)
/*  95 */       .rewards(AdvancementRewards.Builder.experience(100))
/*  96 */       .addCriterion("travelled", DistanceTrigger.TriggerInstance.travelledThroughNether(DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(7000.0D))))
/*  97 */       .save(output, "nether/fast_travel");
/*  99 */     Advancement.Builder.advancement()
/* 100 */       .parent(returnToSender)
/* 101 */       .display((ItemLike)Items.GHAST_TEAR, (Component)Component.translatable("advancements.nether.uneasy_alliance.title"), (Component)Component.translatable("advancements.nether.uneasy_alliance.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 102 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 103 */       .addCriterion("killed_ghast", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.GHAST).located(LocationPredicate.Builder.inDimension(Level.OVERWORLD))))
/* 104 */       .save(output, "nether/uneasy_alliance");
/* 110 */     AdvancementHolder getWitherSkull = Advancement.Builder.advancement().parent(findFortress).display((ItemLike)Blocks.WITHER_SKELETON_SKULL, (Component)Component.translatable("advancements.nether.get_wither_skull.title"), (Component)Component.translatable("advancements.nether.get_wither_skull.description"), null, AdvancementType.TASK, true, true, false).addCriterion("wither_skull", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.WITHER_SKELETON_SKULL })).save(output, "nether/get_wither_skull");
/* 116 */     AdvancementHolder summonWither = Advancement.Builder.advancement().parent(getWitherSkull).display((ItemLike)Items.NETHER_STAR, (Component)Component.translatable("advancements.nether.summon_wither.title"), (Component)Component.translatable("advancements.nether.summon_wither.description"), null, AdvancementType.TASK, true, true, false).addCriterion("summoned", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.WITHER))).save(output, "nether/summon_wither");
/* 122 */     AdvancementHolder obtainBlazeRod = Advancement.Builder.advancement().parent(findFortress).display((ItemLike)Items.BLAZE_ROD, (Component)Component.translatable("advancements.nether.obtain_blaze_rod.title"), (Component)Component.translatable("advancements.nether.obtain_blaze_rod.description"), null, AdvancementType.TASK, true, true, false).addCriterion("blaze_rod", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.BLAZE_ROD })).save(output, "nether/obtain_blaze_rod");
/* 128 */     AdvancementHolder createBeacon = Advancement.Builder.advancement().parent(summonWither).display((ItemLike)Blocks.BEACON, (Component)Component.translatable("advancements.nether.create_beacon.title"), (Component)Component.translatable("advancements.nether.create_beacon.description"), null, AdvancementType.TASK, true, true, false).addCriterion("beacon", ConstructBeaconTrigger.TriggerInstance.constructedBeacon(MinMaxBounds.Ints.atLeast(1))).save(output, "nether/create_beacon");
/* 130 */     Advancement.Builder.advancement()
/* 131 */       .parent(createBeacon)
/* 132 */       .display((ItemLike)Blocks.BEACON, (Component)Component.translatable("advancements.nether.create_full_beacon.title"), (Component)Component.translatable("advancements.nether.create_full_beacon.description"), null, AdvancementType.GOAL, true, true, false)
/* 133 */       .addCriterion("beacon", ConstructBeaconTrigger.TriggerInstance.constructedBeacon(MinMaxBounds.Ints.exactly(4)))
/* 134 */       .save(output, "nether/create_full_beacon");
/* 140 */     AdvancementHolder brewPotion = Advancement.Builder.advancement().parent(obtainBlazeRod).display((ItemLike)Items.POTION, (Component)Component.translatable("advancements.nether.brew_potion.title"), (Component)Component.translatable("advancements.nether.brew_potion.description"), null, AdvancementType.TASK, true, true, false).addCriterion("potion", BrewedPotionTrigger.TriggerInstance.brewedPotion()).save(output, "nether/brew_potion");
/* 147 */     AdvancementHolder allPotions = Advancement.Builder.advancement().parent(brewPotion).display((ItemLike)Items.MILK_BUCKET, (Component)Component.translatable("advancements.nether.all_potions.title"), (Component)Component.translatable("advancements.nether.all_potions.description"), null, AdvancementType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(100)).addCriterion("all_effects", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(MobEffects.SPEED).and(MobEffects.SLOWNESS).and(MobEffects.STRENGTH).and(MobEffects.JUMP_BOOST).and(MobEffects.REGENERATION).and(MobEffects.FIRE_RESISTANCE).and(MobEffects.WATER_BREATHING).and(MobEffects.INVISIBILITY).and(MobEffects.NIGHT_VISION).and(MobEffects.WEAKNESS).and(MobEffects.POISON).and(MobEffects.SLOW_FALLING).and(MobEffects.RESISTANCE).and(MobEffects.OOZING).and(MobEffects.INFESTED).and(MobEffects.WIND_CHARGED).and(MobEffects.WEAVING))).save(output, "nether/all_potions");
/* 149 */     Advancement.Builder.advancement()
/* 150 */       .parent(allPotions)
/* 151 */       .display((ItemLike)Items.BUCKET, (Component)Component.translatable("advancements.nether.all_effects.title"), (Component)Component.translatable("advancements.nether.all_effects.description"), null, AdvancementType.CHALLENGE, true, true, true)
/* 152 */       .rewards(AdvancementRewards.Builder.experience(1000))
/* 153 */       .addCriterion("all_effects", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(MobEffects.SPEED).and(MobEffects.SLOWNESS).and(MobEffects.STRENGTH).and(MobEffects.JUMP_BOOST).and(MobEffects.REGENERATION).and(MobEffects.FIRE_RESISTANCE).and(MobEffects.WATER_BREATHING).and(MobEffects.INVISIBILITY).and(MobEffects.NIGHT_VISION).and(MobEffects.WEAKNESS).and(MobEffects.POISON).and(MobEffects.WITHER).and(MobEffects.HASTE).and(MobEffects.MINING_FATIGUE).and(MobEffects.LEVITATION).and(MobEffects.GLOWING).and(MobEffects.ABSORPTION).and(MobEffects.HUNGER).and(MobEffects.NAUSEA).and(MobEffects.RESISTANCE).and(MobEffects.SLOW_FALLING).and(MobEffects.CONDUIT_POWER).and(MobEffects.DOLPHINS_GRACE).and(MobEffects.BLINDNESS).and(MobEffects.BAD_OMEN).and(MobEffects.HERO_OF_THE_VILLAGE).and(MobEffects.DARKNESS).and(MobEffects.OOZING).and(MobEffects.INFESTED).and(MobEffects.WIND_CHARGED).and(MobEffects.WEAVING).and(MobEffects.TRIAL_OMEN).and(MobEffects.RAID_OMEN).and(MobEffects.BREATH_OF_THE_NAUTILUS)))
/* 154 */       .save(output, "nether/all_effects");
/* 160 */     AdvancementHolder obtainAncientDebris = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.ANCIENT_DEBRIS, (Component)Component.translatable("advancements.nether.obtain_ancient_debris.title"), (Component)Component.translatable("advancements.nether.obtain_ancient_debris.description"), null, AdvancementType.TASK, true, true, false).addCriterion("ancient_debris", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.ANCIENT_DEBRIS })).save(output, "nether/obtain_ancient_debris");
/* 162 */     Advancement.Builder.advancement()
/* 163 */       .parent(obtainAncientDebris)
/* 164 */       .display((ItemLike)Items.NETHERITE_CHESTPLATE, (Component)Component.translatable("advancements.nether.netherite_armor.title"), (Component)Component.translatable("advancements.nether.netherite_armor.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 165 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 166 */       .addCriterion("netherite_armor", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.NETHERITE_HELMET, (ItemLike)Items.NETHERITE_CHESTPLATE, (ItemLike)Items.NETHERITE_LEGGINGS, (ItemLike)Items.NETHERITE_BOOTS })).save(output, "nether/netherite_armor");
/* 173 */     AdvancementHolder obtainCryingObsidian = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.CRYING_OBSIDIAN, (Component)Component.translatable("advancements.nether.obtain_crying_obsidian.title"), (Component)Component.translatable("advancements.nether.obtain_crying_obsidian.description"), null, AdvancementType.TASK, true, true, false).addCriterion("crying_obsidian", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.CRYING_OBSIDIAN })).save(output, "nether/obtain_crying_obsidian");
/* 175 */     Advancement.Builder.advancement()
/* 176 */       .parent(obtainCryingObsidian)
/* 177 */       .display((ItemLike)Items.RESPAWN_ANCHOR, (Component)Component.translatable("advancements.nether.charge_respawn_anchor.title"), (Component)Component.translatable("advancements.nether.charge_respawn_anchor.description"), null, AdvancementType.TASK, true, true, false)
/* 178 */       .addCriterion("charge_respawn_anchor", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.RESPAWN_ANCHOR }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)RespawnAnchorBlock.CHARGE, 4))), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Blocks.GLOWSTONE }))).save(output, "nether/charge_respawn_anchor");
/* 190 */     AdvancementHolder rideStrider = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.WARPED_FUNGUS_ON_A_STICK, (Component)Component.translatable("advancements.nether.ride_strider.title"), (Component)Component.translatable("advancements.nether.ride_strider.description"), null, AdvancementType.TASK, true, true, false).addCriterion("used_warped_fungus_on_a_stick", ItemDurabilityTrigger.TriggerInstance.changedDurability(Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().vehicle(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.STRIDER)))), Optional.of(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.WARPED_FUNGUS_ON_A_STICK }).build()), MinMaxBounds.Ints.ANY)).save(output, "nether/ride_strider");
/* 192 */     Advancement.Builder.advancement()
/* 193 */       .parent(rideStrider)
/* 194 */       .display((ItemLike)Items.WARPED_FUNGUS_ON_A_STICK, (Component)Component.translatable("advancements.nether.ride_strider_in_overworld_lava.title"), (Component)Component.translatable("advancements.nether.ride_strider_in_overworld_lava.description"), null, AdvancementType.TASK, true, true, false)
/* 195 */       .addCriterion("ride_entity_distance", DistanceTrigger.TriggerInstance.rideEntityInLava(
/* 196 */           EntityPredicate.Builder.entity().located(LocationPredicate.Builder.inDimension(Level.OVERWORLD)).vehicle(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.STRIDER)), 
/* 197 */           DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(50.0D))))
/*     */       
/* 199 */       .save(output, "nether/ride_strider_in_overworld_lava");
/* 201 */     VanillaAdventureAdvancements.addBiomes(Advancement.Builder.advancement(), registries, MultiNoiseBiomeSourceParameterList.Preset.NETHER.usedBiomes().toList())
/* 202 */       .parent(rideStrider)
/* 203 */       .display((ItemLike)Items.NETHERITE_BOOTS, (Component)Component.translatable("advancements.nether.explore_nether.title"), (Component)Component.translatable("advancements.nether.explore_nether.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 204 */       .rewards(AdvancementRewards.Builder.experience(500))
/* 205 */       .save(output, "nether/explore_nether");
/* 211 */     AdvancementHolder findBastion = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.POLISHED_BLACKSTONE_BRICKS, (Component)Component.translatable("advancements.nether.find_bastion.title"), (Component)Component.translatable("advancements.nether.find_bastion.description"), null, AdvancementType.TASK, true, true, false).addCriterion("bastion", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure((Holder)registries.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.BASTION_REMNANT)))).save(output, "nether/find_bastion");
/* 213 */     Advancement.Builder.advancement()
/* 214 */       .parent(findBastion)
/* 215 */       .display((ItemLike)Blocks.CHEST, (Component)Component.translatable("advancements.nether.loot_bastion.title"), (Component)Component.translatable("advancements.nether.loot_bastion.description"), null, AdvancementType.TASK, true, true, false)
/* 216 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 217 */       .addCriterion("loot_bastion_other", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.BASTION_OTHER))
/* 218 */       .addCriterion("loot_bastion_treasure", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.BASTION_TREASURE))
/* 219 */       .addCriterion("loot_bastion_hoglin_stable", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.BASTION_HOGLIN_STABLE))
/* 220 */       .addCriterion("loot_bastion_bridge", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.BASTION_BRIDGE))
/* 221 */       .save(output, "nether/loot_bastion");
/* 223 */     Optional<ContextAwarePredicate> distractPiglinPlayerArmorPredicate = Optional.of(ContextAwarePredicate.create(new LootItemCondition[] { LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().head(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.PIGLIN_SAFE_ARMOR)))).invert().build(), 
/* 225 */             LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().chest(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.PIGLIN_SAFE_ARMOR)))).invert().build(), 
/* 226 */             LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().legs(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.PIGLIN_SAFE_ARMOR)))).invert().build(), 
/* 227 */             LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().feet(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.PIGLIN_SAFE_ARMOR)))).invert().build() }));
/* 230 */     Advancement.Builder.advancement()
/* 231 */       .parent(root)
/* 232 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 233 */       .display((ItemLike)Items.GOLD_INGOT, (Component)Component.translatable("advancements.nether.distract_piglin.title"), (Component)Component.translatable("advancements.nether.distract_piglin.description"), null, AdvancementType.TASK, true, true, false)
/* 234 */       .addCriterion("distract_piglin", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByEntity(distractPiglinPlayerArmorPredicate, 
/*     */           
/* 236 */           Optional.of(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.PIGLIN_LOVED).build()), 
/* 237 */           Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.PIGLIN).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(false)))))))
/*     */       
/* 239 */       .addCriterion("distract_piglin_directly", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(distractPiglinPlayerArmorPredicate, 
/*     */           
/* 241 */           ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)PiglinAi.BARTERING_ITEM }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.PIGLIN).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(false)))))))
/*     */       
/* 244 */       .save(output, "nether/distract_piglin");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\advancements\packs\VanillaNetherAdvancements.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */