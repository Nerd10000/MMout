/*     */ package net.minecraft.data.advancements.packs;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.AdvancementType;
/*     */ import net.minecraft.advancements.predicates.BlockPredicate;
/*     */ import net.minecraft.advancements.predicates.DamagePredicate;
/*     */ import net.minecraft.advancements.predicates.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.predicates.DataComponentMatchers;
/*     */ import net.minecraft.advancements.predicates.DistancePredicate;
/*     */ import net.minecraft.advancements.predicates.ItemPredicate;
/*     */ import net.minecraft.advancements.predicates.LocationPredicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.advancements.predicates.StatePropertiesPredicate;
/*     */ import net.minecraft.advancements.predicates.TagPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityEquipmentPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.LightningBoltPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.PlayerPredicate;
/*     */ import net.minecraft.advancements.triggers.ChanneledLightningTrigger;
/*     */ import net.minecraft.advancements.triggers.Criterion;
/*     */ import net.minecraft.advancements.triggers.DistanceTrigger;
/*     */ import net.minecraft.advancements.triggers.FallAfterExplosionTrigger;
/*     */ import net.minecraft.advancements.triggers.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.triggers.ItemUsedOnLocationTrigger;
/*     */ import net.minecraft.advancements.triggers.KilledByArrowTrigger;
/*     */ import net.minecraft.advancements.triggers.KilledTrigger;
/*     */ import net.minecraft.advancements.triggers.LightningStrikeTrigger;
/*     */ import net.minecraft.advancements.triggers.LootTableTrigger;
/*     */ import net.minecraft.advancements.triggers.PlayerHurtEntityTrigger;
/*     */ import net.minecraft.advancements.triggers.PlayerInteractTrigger;
/*     */ import net.minecraft.advancements.triggers.PlayerTrigger;
/*     */ import net.minecraft.advancements.triggers.RecipeCraftedTrigger;
/*     */ import net.minecraft.advancements.triggers.ShotCrossbowTrigger;
/*     */ import net.minecraft.advancements.triggers.SlideDownBlockTrigger;
/*     */ import net.minecraft.advancements.triggers.SpearMobsTrigger;
/*     */ import net.minecraft.advancements.triggers.SummonedEntityTrigger;
/*     */ import net.minecraft.advancements.triggers.TargetBlockTrigger;
/*     */ import net.minecraft.advancements.triggers.TradeTrigger;
/*     */ import net.minecraft.advancements.triggers.UsedTotemTrigger;
/*     */ import net.minecraft.advancements.triggers.UsingItemTrigger;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.component.predicates.JukeboxPlayablePredicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*     */ import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ComparatorBlock;
/*     */ import net.minecraft.world.level.block.CopperBulbBlock;
/*     */ import net.minecraft.world.level.block.CreakingHeartBlock;
/*     */ import net.minecraft.world.level.block.VaultBlock;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.PotDecorations;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.CreakingHeartState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VanillaAdventureAdvancements implements AdvancementSubProvider {
/* 143 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int DISTANCE_FROM_BOTTOM_TO_TOP = 384;
/*     */   
/*     */   private static final int Y_COORDINATE_AT_TOP = 320;
/*     */   
/*     */   private static final int Y_COORDINATE_AT_BOTTOM = -64;
/*     */   
/*     */   private static final int BEDROCK_THICKNESS = 5;
/*     */   
/* 152 */   private static final Map<MobCategory, Set<EntityType<?>>> EXCEPTIONS_BY_EXPECTED_CATEGORIES = Map.of(MobCategory.MONSTER, 
/* 153 */       Set.of(EntityTypes.GIANT, EntityTypes.ILLUSIONER, EntityTypes.WARDEN, EntityTypes.SULFUR_CUBE));
/*     */   
/* 162 */   private static final List<EntityType<?>> MOBS_TO_KILL = Arrays.asList((EntityType<?>[])new EntityType[] { 
/* 162 */         EntityTypes.BLAZE, EntityTypes.BOGGED, EntityTypes.BREEZE, EntityTypes.CAMEL_HUSK, EntityTypes.CAVE_SPIDER, EntityTypes.CREAKING, EntityTypes.CREEPER, EntityTypes.DROWNED, EntityTypes.ELDER_GUARDIAN, EntityTypes.ENDER_DRAGON, 
/* 162 */         EntityTypes.ENDERMAN, EntityTypes.ENDERMITE, EntityTypes.EVOKER, EntityTypes.GHAST, EntityTypes.GUARDIAN, EntityTypes.HOGLIN, EntityTypes.HUSK, EntityTypes.MAGMA_CUBE, EntityTypes.PARCHED, EntityTypes.PHANTOM, 
/* 162 */         EntityTypes.PIGLIN, EntityTypes.PIGLIN_BRUTE, EntityTypes.PILLAGER, EntityTypes.RAVAGER, EntityTypes.SHULKER, EntityTypes.SILVERFISH, EntityTypes.SKELETON, EntityTypes.SLIME, EntityTypes.SPIDER, EntityTypes.STRAY, 
/* 162 */         EntityTypes.VEX, EntityTypes.VINDICATOR, EntityTypes.WITCH, EntityTypes.WITHER_SKELETON, EntityTypes.WITHER, EntityTypes.ZOGLIN, EntityTypes.ZOMBIE_VILLAGER, EntityTypes.ZOMBIE, EntityTypes.ZOMBIE_HORSE, EntityTypes.ZOMBIFIED_PIGLIN, 
/* 162 */         EntityTypes.ZOMBIE_NAUTILUS });
/*     */   
/*     */   private static Criterion<LightningStrikeTrigger.TriggerInstance> fireCountAndBystander(MinMaxBounds.Ints fireCount, Optional<EntityPredicate> bystander) {
/* 207 */     return LightningStrikeTrigger.TriggerInstance.lightningStrike(
/* 208 */         Optional.of(EntityPredicate.Builder.entity()
/* 209 */           .distance(DistancePredicate.absolute(MinMaxBounds.Doubles.atMost(30.0D)))
/* 210 */           .lightingBolt(LightningBoltPredicate.blockSetOnFire(fireCount))
/* 211 */           .build()), bystander);
/*     */   }
/*     */   
/*     */   private static Criterion<UsingItemTrigger.TriggerInstance> lookAtThroughItem(EntityPredicate.Builder lookingAt, ItemPredicate.Builder with) {
/* 217 */     return UsingItemTrigger.TriggerInstance.lookingAt(
/* 218 */         EntityPredicate.Builder.entity().player(
/* 219 */           PlayerPredicate.Builder.player().setLookingAt(lookingAt)
/*     */           
/* 221 */           .build()), with);
/*     */   }
/*     */   
/*     */   public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
/* 229 */     HolderLookup.RegistryLookup registryLookup1 = registries.lookupOrThrow(Registries.ENTITY_TYPE);
/* 230 */     HolderLookup.RegistryLookup registryLookup2 = registries.lookupOrThrow(Registries.ITEM);
/* 231 */     HolderLookup.RegistryLookup registryLookup3 = registries.lookupOrThrow(Registries.BLOCK);
/* 238 */     AdvancementHolder root = Advancement.Builder.advancement().display((ItemLike)Items.MAP, (Component)Component.translatable("advancements.adventure.root.title"), (Component)Component.translatable("advancements.adventure.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"), AdvancementType.TASK, false, false, false).requirements(AdvancementRequirements.Strategy.OR).addCriterion("killed_something", KilledTrigger.TriggerInstance.playerKilledEntity()).addCriterion("killed_by_something", KilledTrigger.TriggerInstance.entityKilledPlayer()).save(output, "adventure/root");
/* 244 */     AdvancementHolder sleepInBed = Advancement.Builder.advancement().parent(root).display((ItemLike)Blocks.BED.red(), (Component)Component.translatable("advancements.adventure.sleep_in_bed.title"), (Component)Component.translatable("advancements.adventure.sleep_in_bed.description"), null, AdvancementType.TASK, true, true, false).addCriterion("slept_in_bed", PlayerTrigger.TriggerInstance.sleptInBed()).save(output, "adventure/sleep_in_bed");
/* 246 */     createAdventuringTime(registries, output, sleepInBed, MultiNoiseBiomeSourceParameterList.Preset.OVERWORLD);
/* 252 */     AdvancementHolder trade = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.EMERALD, (Component)Component.translatable("advancements.adventure.trade.title"), (Component)Component.translatable("advancements.adventure.trade.description"), null, AdvancementType.TASK, true, true, false).addCriterion("traded", TradeTrigger.TriggerInstance.tradedWithVillager()).save(output, "adventure/trade");
/* 254 */     Advancement.Builder.advancement()
/* 255 */       .parent(trade)
/* 256 */       .display((ItemLike)Items.EMERALD, (Component)Component.translatable("advancements.adventure.trade_at_world_height.title"), (Component)Component.translatable("advancements.adventure.trade_at_world_height.description"), null, AdvancementType.TASK, true, true, false)
/* 257 */       .addCriterion("trade_at_world_height", TradeTrigger.TriggerInstance.tradedWithVillager(EntityPredicate.Builder.entity().located(LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atLeast(319.0D)))))
/* 258 */       .save(output, "adventure/trade_at_world_height");
/* 260 */     AdvancementHolder killAMob = createMonsterHunterAdvancement(root, output, (HolderGetter<EntityType<?>>)registryLookup1, validateMobsToKill(MOBS_TO_KILL, (HolderLookup<EntityType<?>>)registryLookup1));
/* 266 */     AdvancementHolder shootArrow = Advancement.Builder.advancement().parent(killAMob).display((ItemLike)Items.BOW, (Component)Component.translatable("advancements.adventure.shoot_arrow.title"), (Component)Component.translatable("advancements.adventure.shoot_arrow.description"), null, AdvancementType.TASK, true, true, false).addCriterion("shot_arrow", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntityWithDamage(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypeTags.ARROWS))))).save(output, "adventure/shoot_arrow");
/* 272 */     AdvancementHolder throwTrident = Advancement.Builder.advancement().parent(killAMob).display((ItemLike)Items.TRIDENT, (Component)Component.translatable("advancements.adventure.throw_trident.title"), (Component)Component.translatable("advancements.adventure.throw_trident.description"), null, AdvancementType.TASK, true, true, false).addCriterion("shot_trident", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntityWithDamage(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.TRIDENT))))).save(output, "adventure/throw_trident");
/* 274 */     Advancement.Builder.advancement()
/* 275 */       .parent(throwTrident)
/* 276 */       .display((ItemLike)Items.TRIDENT, (Component)Component.translatable("advancements.adventure.very_very_frightening.title"), (Component)Component.translatable("advancements.adventure.very_very_frightening.description"), null, AdvancementType.TASK, true, true, false)
/* 277 */       .addCriterion("struck_villager", ChanneledLightningTrigger.TriggerInstance.channeledLightning(new EntityPredicate.Builder[] { EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.VILLAGER) })).save(output, "adventure/very_very_frightening");
/* 280 */     Advancement.Builder.advancement()
/* 281 */       .parent(trade)
/* 282 */       .display((ItemLike)Blocks.CARVED_PUMPKIN, (Component)Component.translatable("advancements.adventure.summon_iron_golem.title"), (Component)Component.translatable("advancements.adventure.summon_iron_golem.description"), null, AdvancementType.GOAL, true, true, false)
/* 283 */       .addCriterion("summoned_golem", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.IRON_GOLEM)))
/* 284 */       .save(output, "adventure/summon_iron_golem");
/* 286 */     Advancement.Builder.advancement()
/* 287 */       .parent(shootArrow)
/* 288 */       .display((ItemLike)Items.ARROW, (Component)Component.translatable("advancements.adventure.sniper_duel.title"), (Component)Component.translatable("advancements.adventure.sniper_duel.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 289 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 290 */       .addCriterion("killed_skeleton", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.SKELETON).distance(DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(50.0D))), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))))
/* 291 */       .save(output, "adventure/sniper_duel");
/* 293 */     Advancement.Builder.advancement()
/* 294 */       .parent(killAMob)
/* 295 */       .display((ItemLike)Items.TOTEM_OF_UNDYING, (Component)Component.translatable("advancements.adventure.totem_of_undying.title"), (Component)Component.translatable("advancements.adventure.totem_of_undying.description"), null, AdvancementType.GOAL, true, true, false)
/* 296 */       .addCriterion("used_totem", UsedTotemTrigger.TriggerInstance.usedTotem((HolderGetter)registryLookup2, (ItemLike)Items.TOTEM_OF_UNDYING))
/* 297 */       .save(output, "adventure/totem_of_undying");
/* 299 */     Advancement.Builder.advancement()
/* 300 */       .parent(killAMob)
/* 301 */       .display((ItemLike)Items.IRON_SPEAR, (Component)Component.translatable("advancements.adventure.spear_many_mobs.title"), (Component)Component.translatable("advancements.adventure.spear_many_mobs.description"), null, AdvancementType.GOAL, true, true, false)
/* 302 */       .addCriterion("spear_many_mobs", SpearMobsTrigger.TriggerInstance.spearMobs(5))
/* 303 */       .save(output, "adventure/spear_many_mobs");
/* 309 */     AdvancementHolder olBetsy = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.CROSSBOW, (Component)Component.translatable("advancements.adventure.ol_betsy.title"), (Component)Component.translatable("advancements.adventure.ol_betsy.description"), null, AdvancementType.TASK, true, true, false).addCriterion("shot_crossbow", ShotCrossbowTrigger.TriggerInstance.shotCrossbow((HolderGetter)registryLookup2, (ItemLike)Items.CROSSBOW)).save(output, "adventure/ol_betsy");
/* 311 */     Advancement.Builder.advancement()
/* 312 */       .parent(olBetsy)
/* 313 */       .display((ItemLike)Items.CROSSBOW, (Component)Component.translatable("advancements.adventure.whos_the_pillager_now.title"), (Component)Component.translatable("advancements.adventure.whos_the_pillager_now.description"), null, AdvancementType.TASK, true, true, false)
/* 314 */       .addCriterion("kill_pillager", KilledByArrowTrigger.TriggerInstance.crossbowKilled((HolderGetter)registryLookup2, new EntityPredicate.Builder[] { EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.PILLAGER) })).save(output, "adventure/whos_the_pillager_now");
/* 317 */     Advancement.Builder.advancement()
/* 318 */       .parent(olBetsy)
/* 319 */       .display((ItemLike)Items.CROSSBOW, (Component)Component.translatable("advancements.adventure.two_birds_one_arrow.title"), (Component)Component.translatable("advancements.adventure.two_birds_one_arrow.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 320 */       .rewards(AdvancementRewards.Builder.experience(65))
/* 321 */       .addCriterion("two_birds", KilledByArrowTrigger.TriggerInstance.crossbowKilled((HolderGetter)registryLookup2, new EntityPredicate.Builder[] { EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.PHANTOM), EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.PHANTOM) })).save(output, "adventure/two_birds_one_arrow");
/* 324 */     Advancement.Builder.advancement()
/* 325 */       .parent(olBetsy)
/* 326 */       .display((ItemLike)Items.CROSSBOW, (Component)Component.translatable("advancements.adventure.arbalistic.title"), (Component)Component.translatable("advancements.adventure.arbalistic.description"), null, AdvancementType.CHALLENGE, true, true, true)
/* 327 */       .rewards(AdvancementRewards.Builder.experience(85))
/* 328 */       .addCriterion("arbalistic", KilledByArrowTrigger.TriggerInstance.crossbowKilled((HolderGetter)registryLookup2, MinMaxBounds.Ints.exactly(5)))
/* 329 */       .save(output, "adventure/arbalistic");
/* 331 */     HolderLookup.RegistryLookup<BannerPattern> patternLookup = registries.lookupOrThrow(Registries.BANNER_PATTERN);
/* 336 */     AdvancementHolder raidOmen = Advancement.Builder.advancement().parent(root).display(Raid.getOminousBannerTemplate((HolderGetter)patternLookup), (Component)Component.translatable("advancements.adventure.voluntary_exile.title"), (Component)Component.translatable("advancements.adventure.voluntary_exile.description"), null, AdvancementType.TASK, true, true, true).addCriterion("voluntary_exile", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypeTags.RAIDERS).equipment(EntityEquipmentPredicate.captainPredicate((HolderGetter)registryLookup2, (HolderGetter)patternLookup)))).save(output, "adventure/voluntary_exile");
/* 338 */     Advancement.Builder.advancement()
/* 339 */       .parent(raidOmen)
/* 340 */       .display(Raid.getOminousBannerTemplate((HolderGetter)patternLookup), (Component)Component.translatable("advancements.adventure.hero_of_the_village.title"), (Component)Component.translatable("advancements.adventure.hero_of_the_village.description"), null, AdvancementType.CHALLENGE, true, true, true)
/* 341 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 342 */       .addCriterion("hero_of_the_village", PlayerTrigger.TriggerInstance.raidWon())
/* 343 */       .save(output, "adventure/hero_of_the_village");
/* 345 */     Advancement.Builder.advancement()
/* 346 */       .parent(root)
/* 347 */       .display((ItemLike)Items.HONEY_BLOCK, (Component)Component.translatable("advancements.adventure.honey_block_slide.title"), (Component)Component.translatable("advancements.adventure.honey_block_slide.description"), null, AdvancementType.TASK, true, true, false)
/* 348 */       .addCriterion("honey_block_slide", SlideDownBlockTrigger.TriggerInstance.slidesDownBlock(Blocks.HONEY_BLOCK))
/* 349 */       .save(output, "adventure/honey_block_slide");
/* 351 */     Advancement.Builder.advancement()
/* 352 */       .parent(shootArrow)
/* 353 */       .display((ItemLike)Items.TARGET, (Component)Component.translatable("advancements.adventure.bullseye.title"), (Component)Component.translatable("advancements.adventure.bullseye.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 354 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 355 */       .addCriterion("bullseye", TargetBlockTrigger.TriggerInstance.targetHit(MinMaxBounds.Ints.exactly(15), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().distance(DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(30.0D)))))))
/* 356 */       .save(output, "adventure/bullseye");
/* 358 */     Advancement.Builder.advancement()
/* 359 */       .parent(sleepInBed)
/* 360 */       .display((ItemLike)Items.LEATHER_BOOTS, (Component)Component.translatable("advancements.adventure.walk_on_powder_snow_with_leather_boots.title"), (Component)Component.translatable("advancements.adventure.walk_on_powder_snow_with_leather_boots.description"), null, AdvancementType.TASK, true, true, false)
/* 361 */       .addCriterion("walk_on_powder_snow_with_leather_boots", PlayerTrigger.TriggerInstance.walkOnBlockWithEquipment((HolderGetter)registryLookup3, (HolderGetter)registryLookup2, Blocks.POWDER_SNOW, Items.LEATHER_BOOTS))
/* 362 */       .save(output, "adventure/walk_on_powder_snow_with_leather_boots");
/* 364 */     Advancement.Builder.advancement()
/* 365 */       .parent(root)
/* 366 */       .display((ItemLike)Items.LIGHTNING_ROD.weathering().unaffected(), (Component)Component.translatable("advancements.adventure.lightning_rod_with_villager_no_fire.title"), (Component)Component.translatable("advancements.adventure.lightning_rod_with_villager_no_fire.description"), null, AdvancementType.TASK, true, true, false)
/* 367 */       .addCriterion("lightning_rod_with_villager_no_fire", fireCountAndBystander(MinMaxBounds.Ints.exactly(0), Optional.of(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.VILLAGER).build())))
/* 368 */       .save(output, "adventure/lightning_rod_with_villager_no_fire");
/* 374 */     AdvancementHolder isItABird = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.SPYGLASS, (Component)Component.translatable("advancements.adventure.spyglass_at_parrot.title"), (Component)Component.translatable("advancements.adventure.spyglass_at_parrot.description"), null, AdvancementType.TASK, true, true, false).addCriterion("spyglass_at_parrot", lookAtThroughItem(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.PARROT), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.SPYGLASS }))).save(output, "adventure/spyglass_at_parrot");
/* 380 */     AdvancementHolder isItABalloon = Advancement.Builder.advancement().parent(isItABird).display((ItemLike)Items.SPYGLASS, (Component)Component.translatable("advancements.adventure.spyglass_at_ghast.title"), (Component)Component.translatable("advancements.adventure.spyglass_at_ghast.description"), null, AdvancementType.TASK, true, true, false).addCriterion("spyglass_at_ghast", lookAtThroughItem(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.GHAST), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.SPYGLASS }))).save(output, "adventure/spyglass_at_ghast");
/* 382 */     Advancement.Builder.advancement()
/* 383 */       .parent(sleepInBed)
/* 384 */       .display((ItemLike)Items.JUKEBOX, (Component)Component.translatable("advancements.adventure.play_jukebox_in_meadows.title"), (Component)Component.translatable("advancements.adventure.play_jukebox_in_meadows.description"), null, AdvancementType.TASK, true, true, false)
/* 385 */       .addCriterion("play_jukebox_in_meadows", 
/* 386 */         ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
/* 387 */           LocationPredicate.Builder.location()
/* 388 */           .setBiomes((HolderSet)HolderSet.direct(new Holder[] { (Holder)registries.lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.MEADOW) }, )).setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.JUKEBOX })), ItemPredicate.Builder.item()
/* 391 */           .withComponents(DataComponentMatchers.Builder.components().partial(DataComponentPredicates.JUKEBOX_PLAYABLE, (DataComponentPredicate)JukeboxPlayablePredicate.any()).build())))
/*     */       
/* 394 */       .save(output, "adventure/play_jukebox_in_meadows");
/* 396 */     Advancement.Builder.advancement()
/* 397 */       .parent(isItABalloon)
/* 398 */       .display((ItemLike)Items.SPYGLASS, (Component)Component.translatable("advancements.adventure.spyglass_at_dragon.title"), (Component)Component.translatable("advancements.adventure.spyglass_at_dragon.description"), null, AdvancementType.TASK, true, true, false)
/* 399 */       .addCriterion("spyglass_at_dragon", lookAtThroughItem(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.ENDER_DRAGON), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.SPYGLASS }))).save(output, "adventure/spyglass_at_dragon");
/* 402 */     Advancement.Builder.advancement()
/* 403 */       .parent(root)
/* 404 */       .display((ItemLike)Items.WATER_BUCKET, (Component)Component.translatable("advancements.adventure.fall_from_world_height.title"), (Component)Component.translatable("advancements.adventure.fall_from_world_height.description"), null, AdvancementType.TASK, true, true, false)
/* 405 */       .addCriterion("fall_from_world_height", 
/* 406 */         DistanceTrigger.TriggerInstance.fallFromHeight(
/* 407 */           EntityPredicate.Builder.entity().located(LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atMost(-59.0D))), 
/* 408 */           DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(379.0D)), 
/* 409 */           LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atLeast(319.0D))))
/*     */       
/* 412 */       .save(output, "adventure/fall_from_world_height");
/* 414 */     Advancement.Builder.advancement()
/* 415 */       .parent(killAMob)
/* 416 */       .display((ItemLike)Blocks.SCULK_CATALYST, (Component)Component.translatable("advancements.adventure.kill_mob_near_sculk_catalyst.title"), (Component)Component.translatable("advancements.adventure.kill_mob_near_sculk_catalyst.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 417 */       .addCriterion("kill_mob_near_sculk_catalyst", KilledTrigger.TriggerInstance.playerKilledEntityNearSculkCatalyst())
/* 418 */       .save(output, "adventure/kill_mob_near_sculk_catalyst");
/* 420 */     Advancement.Builder.advancement()
/* 421 */       .parent(root)
/* 422 */       .display((ItemLike)Blocks.SCULK_SENSOR, (Component)Component.translatable("advancements.adventure.avoid_vibration.title"), (Component)Component.translatable("advancements.adventure.avoid_vibration.description"), null, AdvancementType.TASK, true, true, false)
/* 423 */       .addCriterion("avoid_vibration", PlayerTrigger.TriggerInstance.avoidVibration())
/* 424 */       .save(output, "adventure/avoid_vibration");
/* 429 */     AdvancementHolder respectingTheRemnants = respectingTheRemnantsCriterions((HolderGetter<Item>)registryLookup2, Advancement.Builder.advancement()).parent(root).display((ItemLike)Items.BRUSH, (Component)Component.translatable("advancements.adventure.salvage_sherd.title"), (Component)Component.translatable("advancements.adventure.salvage_sherd.description"), null, AdvancementType.TASK, true, true, false).save(output, "adventure/salvage_sherd");
/* 431 */     Advancement.Builder.advancement()
/* 432 */       .parent(respectingTheRemnants)
/* 433 */       .display(
/* 434 */         DecoratedPotBlockEntity.createDecoratedPotTemplate(new PotDecorations(Optional.empty(), Optional.of(Items.HEART_POTTERY_SHERD), Optional.empty(), Optional.of(Items.EXPLORER_POTTERY_SHERD))), 
/* 435 */         (Component)Component.translatable("advancements.adventure.craft_decorated_pot_using_only_sherds.title"), 
/* 436 */         (Component)Component.translatable("advancements.adventure.craft_decorated_pot_using_only_sherds.description"), null, AdvancementType.TASK, true, true, false)
/*     */       
/* 443 */       .addCriterion("pot_crafted_using_only_sherds", 
/* 444 */         RecipeCraftedTrigger.TriggerInstance.craftedItem(
/* 445 */           ResourceKey.create(Registries.RECIPE, Identifier.withDefaultNamespace("decorated_pot")), 
/* 446 */           List.of(
/* 447 */             ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.DECORATED_POT_SHERDS), 
/* 448 */             ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.DECORATED_POT_SHERDS), 
/* 449 */             ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.DECORATED_POT_SHERDS), 
/* 450 */             ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.DECORATED_POT_SHERDS))))
/*     */       
/* 454 */       .save(output, "adventure/craft_decorated_pot_using_only_sherds");
/* 459 */     AdvancementHolder craftingANewLook = craftingANewLook(Advancement.Builder.advancement()).parent(root).display((ItemLike)Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, (Component)Component.translatable("advancements.adventure.trim_with_any_armor_pattern.title"), (Component)Component.translatable("advancements.adventure.trim_with_any_armor_pattern.description"), null, AdvancementType.TASK, true, true, false).save(output, "adventure/trim_with_any_armor_pattern");
/* 461 */     smithingWithStyle(Advancement.Builder.advancement())
/* 462 */       .parent(craftingANewLook)
/* 463 */       .display((ItemLike)Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, (Component)Component.translatable("advancements.adventure.trim_with_all_exclusive_armor_patterns.title"), (Component)Component.translatable("advancements.adventure.trim_with_all_exclusive_armor_patterns.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 464 */       .rewards(AdvancementRewards.Builder.experience(150))
/* 465 */       .save(output, "adventure/trim_with_all_exclusive_armor_patterns");
/* 467 */     Advancement.Builder.advancement()
/* 468 */       .parent(root)
/* 469 */       .display((ItemLike)Items.CHISELED_BOOKSHELF, (Component)Component.translatable("advancements.adventure.read_power_from_chiseled_bookshelf.title"), (Component)Component.translatable("advancements.adventure.read_power_from_chiseled_bookshelf.description"), null, AdvancementType.TASK, true, true, false)
/* 470 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 471 */       .addCriterion("chiseled_bookshelf", placedBlockReadByComparator((HolderGetter<Block>)registryLookup3, Blocks.CHISELED_BOOKSHELF))
/* 472 */       .addCriterion("comparator", placedComparatorReadingBlock((HolderGetter<Block>)registryLookup3, Blocks.CHISELED_BOOKSHELF))
/* 473 */       .save(output, "adventure/read_power_of_chiseled_bookshelf");
/* 475 */     Advancement.Builder.advancement()
/* 476 */       .parent(root)
/* 477 */       .display((ItemLike)Items.ARMADILLO_SCUTE, (Component)Component.translatable("advancements.adventure.brush_armadillo.title"), (Component)Component.translatable("advancements.adventure.brush_armadillo.description"), null, AdvancementType.TASK, true, true, false)
/* 478 */       .addCriterion("brush_armadillo", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
/* 479 */           ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.BRUSH }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.ARMADILLO)))))
/*     */       
/* 482 */       .save(output, "adventure/brush_armadillo");
/* 488 */     AdvancementHolder trialsEdition = Advancement.Builder.advancement().parent(root).display((ItemLike)Blocks.CHISELED_TUFF, (Component)Component.translatable("advancements.adventure.minecraft_trials_edition.title"), (Component)Component.translatable("advancements.adventure.minecraft_trials_edition.description"), null, AdvancementType.TASK, true, true, false).addCriterion("minecraft_trials_edition", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure((Holder)registries.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.TRIAL_CHAMBERS)))).save(output, "adventure/minecraft_trials_edition");
/* 490 */     Advancement.Builder.advancement()
/* 491 */       .parent(trialsEdition)
/* 492 */       .display((ItemLike)Items.COPPER_BULB.weathering().unaffected(), (Component)Component.translatable("advancements.adventure.lighten_up.title"), (Component)Component.translatable("advancements.adventure.lighten_up.description"), null, AdvancementType.TASK, true, true, false)
/* 493 */       .addCriterion("lighten_up", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { (Block)Blocks.COPPER_BULB
/* 494 */                 .weathering().exposed(), (Block)Blocks.COPPER_BULB
/* 495 */                 .weathering().weathered(), (Block)Blocks.COPPER_BULB
/* 496 */                 .weathering().oxidized(), (Block)Blocks.COPPER_BULB
/*     */                 
/* 498 */                 .waxed().exposed(), (Block)Blocks.COPPER_BULB
/* 499 */                 .waxed().weathered(), (Block)Blocks.COPPER_BULB
/* 500 */                 .waxed().oxidized() }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)CopperBulbBlock.LIT, true))), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, (ItemLike[])VanillaHusbandryAdvancements.WAX_SCRAPING_TOOLS)))
/* 502 */       .save(output, "adventure/lighten_up");
/* 508 */     AdvancementHolder underLockAndKey = Advancement.Builder.advancement().parent(trialsEdition).display((ItemLike)Items.TRIAL_KEY, (Component)Component.translatable("advancements.adventure.under_lock_and_key.title"), (Component)Component.translatable("advancements.adventure.under_lock_and_key.description"), null, AdvancementType.TASK, true, true, false).addCriterion("under_lock_and_key", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.VAULT }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)VaultBlock.OMINOUS, false))), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.TRIAL_KEY }))).save(output, "adventure/under_lock_and_key");
/* 510 */     Advancement.Builder.advancement()
/* 511 */       .parent(underLockAndKey)
/* 512 */       .display((ItemLike)Items.OMINOUS_TRIAL_KEY, (Component)Component.translatable("advancements.adventure.revaulting.title"), (Component)Component.translatable("advancements.adventure.revaulting.description"), null, AdvancementType.GOAL, true, true, false)
/* 513 */       .addCriterion("revaulting", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.VAULT }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)VaultBlock.OMINOUS, true))), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.OMINOUS_TRIAL_KEY }))).save(output, "adventure/revaulting");
/* 516 */     Advancement.Builder.advancement()
/* 517 */       .parent(trialsEdition)
/* 518 */       .display((ItemLike)Items.WIND_CHARGE, (Component)Component.translatable("advancements.adventure.blowback.title"), (Component)Component.translatable("advancements.adventure.blowback.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 519 */       .rewards(AdvancementRewards.Builder.experience(40))
/* 520 */       .addCriterion("blowback", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.BREEZE), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.BREEZE_WIND_CHARGE))))
/* 521 */       .save(output, "adventure/blowback");
/* 523 */     Advancement.Builder.advancement()
/* 524 */       .parent(root)
/* 525 */       .display((ItemLike)Items.CRAFTER, (Component)Component.translatable("advancements.adventure.crafters_crafting_crafters.title"), (Component)Component.translatable("advancements.adventure.crafters_crafting_crafters.description"), null, AdvancementType.TASK, true, true, false)
/* 526 */       .addCriterion("crafter_crafted_crafter", RecipeCraftedTrigger.TriggerInstance.crafterCraftedItem(ResourceKey.create(Registries.RECIPE, Identifier.withDefaultNamespace("crafter"))))
/* 527 */       .save(output, "adventure/crafters_crafting_crafters");
/* 529 */     Advancement.Builder.advancement()
/* 530 */       .parent(root)
/* 531 */       .display((ItemLike)Items.LODESTONE, (Component)Component.translatable("advancements.adventure.use_lodestone.title"), (Component)Component.translatable("advancements.adventure.use_lodestone.description"), null, AdvancementType.TASK, true, true, false)
/* 532 */       .addCriterion("use_lodestone", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.LODESTONE })), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.COMPASS }))).save(output, "adventure/use_lodestone");
/* 535 */     Advancement.Builder.advancement()
/* 536 */       .parent(trialsEdition)
/* 537 */       .display((ItemLike)Items.WIND_CHARGE, (Component)Component.translatable("advancements.adventure.who_needs_rockets.title"), (Component)Component.translatable("advancements.adventure.who_needs_rockets.description"), null, AdvancementType.TASK, true, true, false)
/* 538 */       .addCriterion("who_needs_rockets", FallAfterExplosionTrigger.TriggerInstance.fallAfterExplosion(DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(7.0D)), EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.WIND_CHARGE)))
/* 539 */       .save(output, "adventure/who_needs_rockets");
/* 541 */     Advancement.Builder.advancement()
/* 542 */       .parent(trialsEdition)
/* 543 */       .display((ItemLike)Items.MACE, (Component)Component.translatable("advancements.adventure.overoverkill.title"), (Component)Component.translatable("advancements.adventure.overoverkill.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 544 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 545 */       .addCriterion("overoverkill", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntityWithDamage(DamagePredicate.Builder.damageInstance().dealtDamage(MinMaxBounds.Doubles.atLeast(100.0D)).type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_MACE_SMASH)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.PLAYER).equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.MACE }))))))).save(output, "adventure/overoverkill");
/* 548 */     Advancement.Builder.advancement()
/* 549 */       .parent(root)
/* 550 */       .display((ItemLike)Blocks.CREAKING_HEART, (Component)Component.translatable("advancements.adventure.heart_transplanter.title"), (Component)Component.translatable("advancements.adventure.heart_transplanter.description"), null, AdvancementType.TASK, true, true, false)
/* 551 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 552 */       .addCriterion("place_creaking_heart_dormant", ItemUsedOnLocationTrigger.TriggerInstance.placedBlockWithProperties(Blocks.CREAKING_HEART, (Property)BlockStateProperties.CREAKING_HEART_STATE, (Comparable)CreakingHeartState.DORMANT))
/* 553 */       .addCriterion("place_creaking_heart_awake", ItemUsedOnLocationTrigger.TriggerInstance.placedBlockWithProperties(Blocks.CREAKING_HEART, (Property)BlockStateProperties.CREAKING_HEART_STATE, (Comparable)CreakingHeartState.AWAKE))
/* 554 */       .addCriterion("place_pale_oak_log", placedBlockActivatesCreakingHeart((HolderGetter<Block>)registryLookup3, BlockTags.PALE_OAK_LOGS))
/* 555 */       .save(output, "adventure/heart_transplanter");
/*     */   }
/*     */   
/*     */   public static AdvancementHolder createMonsterHunterAdvancement(AdvancementHolder parent, Consumer<AdvancementHolder> output, HolderGetter<EntityType<?>> entityTypes, List<EntityType<?>> mobsToKill) {
/* 563 */     AdvancementHolder killAMob = addMobsToKill(Advancement.Builder.advancement(), entityTypes, mobsToKill).parent(parent).display((ItemLike)Items.IRON_SWORD, (Component)Component.translatable("advancements.adventure.kill_a_mob.title"), (Component)Component.translatable("advancements.adventure.kill_a_mob.description"), null, AdvancementType.TASK, true, true, false).requirements(AdvancementRequirements.Strategy.OR).save(output, "adventure/kill_a_mob");
/* 565 */     addMobsToKill(Advancement.Builder.advancement(), entityTypes, mobsToKill)
/* 566 */       .parent(killAMob)
/* 567 */       .display((ItemLike)Items.DIAMOND_SWORD, (Component)Component.translatable("advancements.adventure.kill_all_mobs.title"), (Component)Component.translatable("advancements.adventure.kill_all_mobs.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 568 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 569 */       .save(output, "adventure/kill_all_mobs");
/* 571 */     return killAMob;
/*     */   }
/*     */   
/*     */   private static Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedBlockReadByComparator(HolderGetter<Block> blocks, Block block) {
/* 580 */     LootItemCondition.Builder[] conditions = (LootItemCondition.Builder[])ComparatorBlock.FACING.getPossibleValues().stream().map(direction -> {
/*     */           StatePropertiesPredicate.Builder comparatorProperties = StatePropertiesPredicate.Builder.properties().hasProperty((Property)ComparatorBlock.FACING, (Comparable)direction);
/*     */           BlockPredicate.Builder comparatorTest = BlockPredicate.Builder.block().of(blocks, new Block[] { Blocks.COMPARATOR }).setProperties(comparatorProperties);
/*     */           return LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(comparatorTest), new BlockPos(direction.getOpposite().getUnitVec3i()));
/* 580 */         }).toArray(x$0 -> new LootItemCondition.Builder[x$0]);
/* 582 */     return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(new LootItemCondition.Builder[] { (LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block), 
/* 584 */           (LootItemCondition.Builder)AnyOfCondition.anyOf(conditions) });
/*     */   }
/*     */   
/*     */   private static Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedComparatorReadingBlock(HolderGetter<Block> blocks, Block block) {
/* 594 */     LootItemCondition.Builder[] conditions = (LootItemCondition.Builder[])ComparatorBlock.FACING.getPossibleValues().stream().map(direction -> {
/*     */           StatePropertiesPredicate.Builder comparatorProperties = StatePropertiesPredicate.Builder.properties().hasProperty((Property)ComparatorBlock.FACING, (Comparable)direction);
/*     */           LootItemBlockStatePropertyCondition.Builder comparatorTest = (new LootItemBlockStatePropertyCondition.Builder(Blocks.COMPARATOR)).setProperties(comparatorProperties);
/*     */           LootItemCondition.Builder blockTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, new Block[] { block })), new BlockPos(direction.getUnitVec3i()));
/*     */           return AllOfCondition.allOf(new LootItemCondition.Builder[] { (LootItemCondition.Builder)comparatorTest, blockTest });
/* 594 */         }).toArray(x$0 -> new LootItemCondition.Builder[x$0]);
/* 596 */     return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(new LootItemCondition.Builder[] { (LootItemCondition.Builder)AnyOfCondition.anyOf(conditions) });
/*     */   }
/*     */   
/*     */   private static Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedBlockActivatesCreakingHeart(HolderGetter<Block> blocks, TagKey<Block> block) {
/* 617 */     LootItemCondition.Builder[] conditions = (LootItemCondition.Builder[])Stream.<Direction>of(Direction.values()).map(direction -> {
/*     */           StatePropertiesPredicate.Builder creakingHeartProperties = StatePropertiesPredicate.Builder.properties().hasProperty((Property)CreakingHeartBlock.AXIS, (Comparable)direction.getAxis());
/*     */           BlockPredicate.Builder placedPaleOakLogBlock = BlockPredicate.Builder.block().of(blocks, block).setProperties(creakingHeartProperties);
/*     */           Vec3i blockOffset = direction.getUnitVec3i();
/*     */           LootItemCondition.Builder placedPaleOakLogTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(placedPaleOakLogBlock));
/*     */           LootItemCondition.Builder creakingHeartBlockTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, new Block[] { Blocks.CREAKING_HEART }).setProperties(creakingHeartProperties)), new BlockPos(blockOffset));
/*     */           LootItemCondition.Builder existingPaleOakLogTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(placedPaleOakLogBlock), new BlockPos(blockOffset.multiply(2)));
/*     */           return AllOfCondition.allOf(new LootItemCondition.Builder[] { placedPaleOakLogTest, creakingHeartBlockTest, existingPaleOakLogTest });
/* 617 */         }).toArray(x$0 -> new LootItemCondition.Builder[x$0]);
/* 619 */     return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(new LootItemCondition.Builder[] { (LootItemCondition.Builder)AnyOfCondition.anyOf(conditions) });
/*     */   }
/*     */   
/*     */   private static Advancement.Builder smithingWithStyle(Advancement.Builder advancement) {
/* 625 */     advancement.requirements(AdvancementRequirements.Strategy.AND);
/* 627 */     Set<Item> required = Set.of(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 638 */     VanillaRecipeProvider.smithingTrims().filter(trim -> required.contains(trim.template())).forEach(trimTemplate -> advancement.addCriterion("armor_trimmed_" + String.valueOf(trimTemplate.recipeId().identifier()), RecipeCraftedTrigger.TriggerInstance.craftedItem(trimTemplate.recipeId())));
/* 642 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder craftingANewLook(Advancement.Builder advancement) {
/* 646 */     advancement.requirements(AdvancementRequirements.Strategy.OR);
/* 648 */     VanillaRecipeProvider.smithingTrims().map(VanillaRecipeProvider.TrimTemplate::recipeId).forEach(recipeId -> advancement.addCriterion("armor_trimmed_" + String.valueOf(recipeId.identifier()), RecipeCraftedTrigger.TriggerInstance.craftedItem(recipeId)));
/* 652 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder respectingTheRemnantsCriterions(HolderGetter<Item> items, Advancement.Builder advancement) {
/* 656 */     List<Pair<String, Criterion<LootTableTrigger.TriggerInstance>>> lootCriteria = List.of(
/* 657 */         Pair.of("desert_pyramid", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY)), 
/* 658 */         Pair.of("desert_well", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY)), 
/* 659 */         Pair.of("ocean_ruin_cold", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY)), 
/* 660 */         Pair.of("ocean_ruin_warm", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY)), 
/* 661 */         Pair.of("trail_ruins_rare", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE)), 
/* 662 */         Pair.of("trail_ruins_common", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON)));
/* 664 */     lootCriteria.forEach(p -> advancement.addCriterion((String)p.getFirst(), (Criterion)p.getSecond()));
/* 666 */     String hasSherdCriterion = "has_sherd";
/* 667 */     advancement.addCriterion("has_sherd", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemPredicate.Builder[] { ItemPredicate.Builder.item().of(items, ItemTags.DECORATED_POT_SHERDS) }));
/* 669 */     advancement.requirements(new AdvancementRequirements(List.of(lootCriteria
/* 670 */             .stream().map(Pair::getFirst).toList(), 
/* 671 */             List.of("has_sherd"))));
/* 674 */     return advancement;
/*     */   }
/*     */   
/*     */   protected static void createAdventuringTime(HolderLookup.Provider registries, Consumer<AdvancementHolder> output, AdvancementHolder sleepInBed, MultiNoiseBiomeSourceParameterList.Preset preset) {
/* 678 */     addBiomes(Advancement.Builder.advancement(), registries, preset.usedBiomes().toList())
/* 679 */       .parent(sleepInBed)
/* 680 */       .display((ItemLike)Items.DIAMOND_BOOTS, (Component)Component.translatable("advancements.adventure.adventuring_time.title"), (Component)Component.translatable("advancements.adventure.adventuring_time.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 681 */       .rewards(AdvancementRewards.Builder.experience(500))
/* 682 */       .save(output, "adventure/adventuring_time");
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addMobsToKill(Advancement.Builder advancement, HolderGetter<EntityType<?>> entityTypes, List<EntityType<?>> mobsToKill) {
/* 686 */     mobsToKill.forEach(mob -> advancement.addCriterion(BuiltInRegistries.ENTITY_TYPE.getKey(mob).toString(), KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entityTypes, mob))));
/* 687 */     return advancement;
/*     */   }
/*     */   
/*     */   protected static Advancement.Builder addBiomes(Advancement.Builder advancement, HolderLookup.Provider registries, List<ResourceKey<Biome>> explorableBiomes) {
/* 691 */     HolderLookup.RegistryLookup registryLookup = registries.lookupOrThrow(Registries.BIOME);
/* 692 */     for (ResourceKey<Biome> biome : explorableBiomes)
/* 693 */       advancement.addCriterion(biome.identifier().toString(), PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inBiome((Holder)registryLookup.getOrThrow(biome)))); 
/* 695 */     return advancement;
/*     */   }
/*     */   
/*     */   private static List<EntityType<?>> validateMobsToKill(List<EntityType<?>> data, HolderLookup<EntityType<?>> entityTypes) {
/* 699 */     List<String> errors = new ArrayList<>();
/* 700 */     Set<? extends EntityType<?>> mobsToKill = Set.copyOf(data);
/* 703 */     Set<MobCategory> specifiedCategories = (Set<MobCategory>)mobsToKill.stream().map(EntityType::getCategory).collect(Collectors.toSet());
/* 704 */     Sets.SetView setView1 = Sets.symmetricDifference(EXCEPTIONS_BY_EXPECTED_CATEGORIES
/* 705 */         .keySet(), specifiedCategories);
/* 708 */     if (!setView1.isEmpty())
/* 709 */       errors.add("Found EntityType with MobCategory only in either expected exceptions or kill_all_mobs advancement: " + (String)setView1
/* 710 */           .stream().map(Object::toString).sorted().collect(Collectors.joining(", "))); 
/* 715 */     Sets.SetView setView2 = Sets.intersection((Set)EXCEPTIONS_BY_EXPECTED_CATEGORIES
/* 716 */         .values().stream().flatMap(Collection::stream).collect(Collectors.toSet()), mobsToKill);
/* 720 */     if (!setView2.isEmpty())
/* 721 */       errors.add("Found EntityType in both expected exceptions and kill_all_mobs advancement: " + (String)setView2
/* 722 */           .stream().map(Object::toString).sorted().collect(Collectors.joining(", "))); 
/* 729 */     Objects.requireNonNull(mobsToKill);
/* 730 */     Map<MobCategory, Set<EntityType<?>>> doNotKillByCategory = (Map<MobCategory, Set<EntityType<?>>>)entityTypes.listElements().map(Holder.Reference::value).filter(Predicate.not(mobsToKill::contains)).collect(Collectors.groupingBy(EntityType::getCategory, Collectors.toSet()));
/* 732 */     EXCEPTIONS_BY_EXPECTED_CATEGORIES.forEach((exceptedCategory, exceptedTypes) -> {
/*     */           Sets.SetView setView = Sets.difference((Set)doNotKillByCategory.getOrDefault(exceptedCategory, Set.of()), exceptedTypes);
/*     */           if (!setView.isEmpty())
/*     */             errors.add(String.format(Locale.ROOT, "Found (new?) EntityType with MobCategory %s which are in neither expected exceptions nor kill_all_mobs advancement: %s", new Object[] { exceptedCategory, setView.stream().map(Object::toString).sorted().collect(Collectors.joining(", ")) })); 
/*     */         });
/* 746 */     if (!errors.isEmpty()) {
/* 747 */       Objects.requireNonNull(LOGGER);
/* 747 */       errors.forEach(LOGGER::error);
/* 748 */       throw new IllegalStateException("Found inconsistencies with kill_all_mobs advancement");
/*     */     } 
/* 751 */     return data;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\advancements\packs\VanillaAdventureAdvancements.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */