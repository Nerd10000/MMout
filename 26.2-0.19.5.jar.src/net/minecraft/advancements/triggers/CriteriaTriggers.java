/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class CriteriaTriggers {
/* 10 */   public static final Codec<CriterionTrigger<?>> CODEC = BuiltInRegistries.TRIGGER_TYPES.byNameCodec();
/*    */   
/* 12 */   public static final ImpossibleTrigger IMPOSSIBLE = register("impossible", new ImpossibleTrigger());
/*    */   
/* 13 */   public static final KilledTrigger PLAYER_KILLED_ENTITY = register("player_killed_entity", new KilledTrigger());
/*    */   
/* 14 */   public static final KilledTrigger ENTITY_KILLED_PLAYER = register("entity_killed_player", new KilledTrigger());
/*    */   
/* 15 */   public static final EnterBlockTrigger ENTER_BLOCK = register("enter_block", new EnterBlockTrigger());
/*    */   
/* 16 */   public static final InventoryChangeTrigger INVENTORY_CHANGED = register("inventory_changed", new InventoryChangeTrigger());
/*    */   
/* 17 */   public static final RecipeUnlockedTrigger RECIPE_UNLOCKED = register("recipe_unlocked", new RecipeUnlockedTrigger());
/*    */   
/* 18 */   public static final PlayerHurtEntityTrigger PLAYER_HURT_ENTITY = register("player_hurt_entity", new PlayerHurtEntityTrigger());
/*    */   
/* 19 */   public static final EntityHurtPlayerTrigger ENTITY_HURT_PLAYER = register("entity_hurt_player", new EntityHurtPlayerTrigger());
/*    */   
/* 20 */   public static final EnchantedItemTrigger ENCHANTED_ITEM = register("enchanted_item", new EnchantedItemTrigger());
/*    */   
/* 21 */   public static final FilledBucketTrigger FILLED_BUCKET = register("filled_bucket", new FilledBucketTrigger());
/*    */   
/* 22 */   public static final BrewedPotionTrigger BREWED_POTION = register("brewed_potion", new BrewedPotionTrigger());
/*    */   
/* 23 */   public static final ConstructBeaconTrigger CONSTRUCT_BEACON = register("construct_beacon", new ConstructBeaconTrigger());
/*    */   
/* 24 */   public static final UsedEnderEyeTrigger USED_ENDER_EYE = register("used_ender_eye", new UsedEnderEyeTrigger());
/*    */   
/* 25 */   public static final SummonedEntityTrigger SUMMONED_ENTITY = register("summoned_entity", new SummonedEntityTrigger());
/*    */   
/* 26 */   public static final BredAnimalsTrigger BRED_ANIMALS = register("bred_animals", new BredAnimalsTrigger());
/*    */   
/* 27 */   public static final PlayerTrigger LOCATION = register("location", new PlayerTrigger());
/*    */   
/* 28 */   public static final PlayerTrigger SLEPT_IN_BED = register("slept_in_bed", new PlayerTrigger());
/*    */   
/* 29 */   public static final CuredZombieVillagerTrigger CURED_ZOMBIE_VILLAGER = register("cured_zombie_villager", new CuredZombieVillagerTrigger());
/*    */   
/* 30 */   public static final TradeTrigger TRADE = register("villager_trade", new TradeTrigger());
/*    */   
/* 31 */   public static final ItemDurabilityTrigger ITEM_DURABILITY_CHANGED = register("item_durability_changed", new ItemDurabilityTrigger());
/*    */   
/* 32 */   public static final LevitationTrigger LEVITATION = register("levitation", new LevitationTrigger());
/*    */   
/* 33 */   public static final ChangeDimensionTrigger CHANGED_DIMENSION = register("changed_dimension", new ChangeDimensionTrigger());
/*    */   
/* 34 */   public static final PlayerTrigger TICK = register("tick", new PlayerTrigger());
/*    */   
/* 35 */   public static final TameAnimalTrigger TAME_ANIMAL = register("tame_animal", new TameAnimalTrigger());
/*    */   
/* 36 */   public static final ItemUsedOnLocationTrigger PLACED_BLOCK = register("placed_block", new ItemUsedOnLocationTrigger());
/*    */   
/* 37 */   public static final ConsumeItemTrigger CONSUME_ITEM = register("consume_item", new ConsumeItemTrigger());
/*    */   
/* 38 */   public static final EffectsChangedTrigger EFFECTS_CHANGED = register("effects_changed", new EffectsChangedTrigger());
/*    */   
/* 39 */   public static final UsedTotemTrigger USED_TOTEM = register("used_totem", new UsedTotemTrigger());
/*    */   
/* 40 */   public static final DistanceTrigger NETHER_TRAVEL = register("nether_travel", new DistanceTrigger());
/*    */   
/* 41 */   public static final FishingRodHookedTrigger FISHING_ROD_HOOKED = register("fishing_rod_hooked", new FishingRodHookedTrigger());
/*    */   
/* 42 */   public static final ChanneledLightningTrigger CHANNELED_LIGHTNING = register("channeled_lightning", new ChanneledLightningTrigger());
/*    */   
/* 43 */   public static final ShotCrossbowTrigger SHOT_CROSSBOW = register("shot_crossbow", new ShotCrossbowTrigger());
/*    */   
/* 44 */   public static final SpearMobsTrigger SPEAR_MOBS_TRIGGER = register("spear_mobs", new SpearMobsTrigger());
/*    */   
/* 45 */   public static final KilledByArrowTrigger KILLED_BY_ARROW = register("killed_by_arrow", new KilledByArrowTrigger());
/*    */   
/* 46 */   public static final PlayerTrigger RAID_WIN = register("hero_of_the_village", new PlayerTrigger());
/*    */   
/* 47 */   public static final PlayerTrigger RAID_OMEN = register("voluntary_exile", new PlayerTrigger());
/*    */   
/* 48 */   public static final SlideDownBlockTrigger HONEY_BLOCK_SLIDE = register("slide_down_block", new SlideDownBlockTrigger());
/*    */   
/* 49 */   public static final BeeNestDestroyedTrigger BEE_NEST_DESTROYED = register("bee_nest_destroyed", new BeeNestDestroyedTrigger());
/*    */   
/* 50 */   public static final TargetBlockTrigger TARGET_BLOCK_HIT = register("target_hit", new TargetBlockTrigger());
/*    */   
/* 51 */   public static final ItemUsedOnLocationTrigger ITEM_USED_ON_BLOCK = register("item_used_on_block", new ItemUsedOnLocationTrigger());
/*    */   
/* 52 */   public static final DefaultBlockInteractionTrigger DEFAULT_BLOCK_USE = register("default_block_use", new DefaultBlockInteractionTrigger());
/*    */   
/* 53 */   public static final AnyBlockInteractionTrigger ANY_BLOCK_USE = register("any_block_use", new AnyBlockInteractionTrigger());
/*    */   
/* 54 */   public static final LootTableTrigger GENERATE_LOOT = register("player_generates_container_loot", new LootTableTrigger());
/*    */   
/* 55 */   public static final PickedUpItemTrigger THROWN_ITEM_PICKED_UP_BY_ENTITY = register("thrown_item_picked_up_by_entity", new PickedUpItemTrigger());
/*    */   
/* 56 */   public static final PickedUpItemTrigger THROWN_ITEM_PICKED_UP_BY_PLAYER = register("thrown_item_picked_up_by_player", new PickedUpItemTrigger());
/*    */   
/* 57 */   public static final PlayerInteractTrigger PLAYER_INTERACTED_WITH_ENTITY = register("player_interacted_with_entity", new PlayerInteractTrigger());
/*    */   
/* 58 */   public static final PlayerInteractTrigger PLAYER_SHEARED_EQUIPMENT = register("player_sheared_equipment", new PlayerInteractTrigger());
/*    */   
/* 59 */   public static final StartRidingTrigger START_RIDING_TRIGGER = register("started_riding", new StartRidingTrigger());
/*    */   
/* 60 */   public static final LightningStrikeTrigger LIGHTNING_STRIKE = register("lightning_strike", new LightningStrikeTrigger());
/*    */   
/* 61 */   public static final UsingItemTrigger USING_ITEM = register("using_item", new UsingItemTrigger());
/*    */   
/* 62 */   public static final DistanceTrigger FALL_FROM_HEIGHT = register("fall_from_height", new DistanceTrigger());
/*    */   
/* 63 */   public static final DistanceTrigger RIDE_ENTITY_IN_LAVA_TRIGGER = register("ride_entity_in_lava", new DistanceTrigger());
/*    */   
/* 64 */   public static final KilledTrigger KILL_MOB_NEAR_SCULK_CATALYST = register("kill_mob_near_sculk_catalyst", new KilledTrigger());
/*    */   
/* 65 */   public static final ItemUsedOnLocationTrigger ALLAY_DROP_ITEM_ON_BLOCK = register("allay_drop_item_on_block", new ItemUsedOnLocationTrigger());
/*    */   
/* 66 */   public static final PlayerTrigger AVOID_VIBRATION = register("avoid_vibration", new PlayerTrigger());
/*    */   
/* 67 */   public static final RecipeCraftedTrigger RECIPE_CRAFTED = register("recipe_crafted", new RecipeCraftedTrigger());
/*    */   
/* 68 */   public static final RecipeCraftedTrigger CRAFTER_RECIPE_CRAFTED = register("crafter_recipe_crafted", new RecipeCraftedTrigger());
/*    */   
/* 69 */   public static final FallAfterExplosionTrigger FALL_AFTER_EXPLOSION = register("fall_after_explosion", new FallAfterExplosionTrigger());
/*    */   
/*    */   private static <T extends CriterionTrigger<?>> T register(String name, T criterion) {
/* 72 */     return (T)Registry.register(BuiltInRegistries.TRIGGER_TYPES, name, criterion);
/*    */   }
/*    */   
/*    */   public static CriterionTrigger<?> bootstrap(Registry<CriterionTrigger<?>> registry) {
/* 76 */     return IMPOSSIBLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\CriteriaTriggers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */