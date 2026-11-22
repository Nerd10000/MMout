/*     */ package net.minecraft.data.advancements.packs;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.AdvancementType;
/*     */ import net.minecraft.advancements.predicates.BlockPredicate;
/*     */ import net.minecraft.advancements.predicates.DataComponentMatchers;
/*     */ import net.minecraft.advancements.predicates.EnchantmentPredicate;
/*     */ import net.minecraft.advancements.predicates.ItemPredicate;
/*     */ import net.minecraft.advancements.predicates.LocationPredicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.advancements.predicates.entity.EntityEquipmentPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*     */ import net.minecraft.advancements.triggers.BeeNestDestroyedTrigger;
/*     */ import net.minecraft.advancements.triggers.BredAnimalsTrigger;
/*     */ import net.minecraft.advancements.triggers.ConsumeItemTrigger;
/*     */ import net.minecraft.advancements.triggers.EffectsChangedTrigger;
/*     */ import net.minecraft.advancements.triggers.FilledBucketTrigger;
/*     */ import net.minecraft.advancements.triggers.FishingRodHookedTrigger;
/*     */ import net.minecraft.advancements.triggers.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.triggers.ItemUsedOnLocationTrigger;
/*     */ import net.minecraft.advancements.triggers.PickedUpItemTrigger;
/*     */ import net.minecraft.advancements.triggers.PlayerInteractTrigger;
/*     */ import net.minecraft.advancements.triggers.StartRidingTrigger;
/*     */ import net.minecraft.advancements.triggers.TameAnimalTrigger;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentExactPredicate;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.component.predicates.EnchantmentsPredicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.animal.feline.CatVariant;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfVariant;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class VanillaHusbandryAdvancements implements AdvancementSubProvider {
/*  82 */   public static final List<EntityType<?>> BREEDABLE_ANIMALS = List.of((EntityType<?>[])new EntityType[] { 
/*  82 */         EntityTypes.HORSE, EntityTypes.DONKEY, EntityTypes.MULE, EntityTypes.SHEEP, EntityTypes.COW, EntityTypes.MOOSHROOM, EntityTypes.PIG, EntityTypes.CHICKEN, EntityTypes.WOLF, EntityTypes.OCELOT, 
/*  82 */         EntityTypes.RABBIT, EntityTypes.LLAMA, EntityTypes.CAT, EntityTypes.PANDA, EntityTypes.FOX, EntityTypes.BEE, EntityTypes.HOGLIN, EntityTypes.STRIDER, EntityTypes.GOAT, EntityTypes.AXOLOTL, 
/*  82 */         EntityTypes.CAMEL, EntityTypes.ARMADILLO, EntityTypes.NAUTILUS });
/*     */   
/* 109 */   public static final List<EntityType<?>> INDIRECTLY_BREEDABLE_ANIMALS = List.of(EntityTypes.TURTLE, EntityTypes.FROG, EntityTypes.SNIFFER);
/*     */   
/* 115 */   private static final Item[] FISH = new Item[] { Items.COD, Items.TROPICAL_FISH, Items.PUFFERFISH, Items.SALMON };
/*     */   
/* 122 */   private static final Item[] FISH_BUCKETS = new Item[] { Items.COD_BUCKET, Items.TROPICAL_FISH_BUCKET, Items.PUFFERFISH_BUCKET, Items.SALMON_BUCKET };
/*     */   
/* 129 */   private static final Item[] EDIBLE_ITEMS = new Item[] { 
/* 129 */       Items.APPLE, Items.MUSHROOM_STEW, Items.BREAD, Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.COD, Items.SALMON, Items.TROPICAL_FISH, 
/* 129 */       Items.PUFFERFISH, Items.COOKED_COD, Items.COOKED_SALMON, Items.COOKIE, Items.MELON_SLICE, Items.BEEF, Items.COOKED_BEEF, Items.CHICKEN, Items.COOKED_CHICKEN, Items.ROTTEN_FLESH, 
/* 129 */       Items.SPIDER_EYE, Items.CARROT, Items.POTATO, Items.BAKED_POTATO, Items.POISONOUS_POTATO, Items.GOLDEN_CARROT, Items.PUMPKIN_PIE, Items.RABBIT, Items.COOKED_RABBIT, Items.RABBIT_STEW, 
/* 129 */       Items.MUTTON, Items.COOKED_MUTTON, Items.CHORUS_FRUIT, Items.BEETROOT, Items.BEETROOT_SOUP, Items.DRIED_KELP, Items.SUSPICIOUS_STEW, Items.SWEET_BERRIES, Items.HONEY_BOTTLE, Items.GLOW_BERRIES };
/*     */   
/* 172 */   public static final Item[] WAX_SCRAPING_TOOLS = new Item[] { Items.WOODEN_AXE, Items.GOLDEN_AXE, Items.STONE_AXE, Items.COPPER_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE };
/*     */   
/*     */   private static final Comparator<Holder.Reference<?>> HOLDER_KEY_COMPARATOR;
/*     */   
/*     */   static {
/* 182 */     HOLDER_KEY_COMPARATOR = Comparator.comparing(e -> e.key().identifier());
/*     */   }
/*     */   
/*     */   public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
/* 186 */     HolderLookup.RegistryLookup registryLookup1 = registries.lookupOrThrow(Registries.ENTITY_TYPE);
/* 187 */     HolderLookup.RegistryLookup registryLookup2 = registries.lookupOrThrow(Registries.ITEM);
/* 188 */     HolderLookup.RegistryLookup registryLookup3 = registries.lookupOrThrow(Registries.BLOCK);
/* 189 */     HolderLookup.RegistryLookup registryLookup4 = registries.lookupOrThrow(Registries.FROG_VARIANT);
/* 190 */     HolderLookup.RegistryLookup registryLookup5 = registries.lookupOrThrow(Registries.CAT_VARIANT);
/* 191 */     HolderLookup.RegistryLookup registryLookup6 = registries.lookupOrThrow(Registries.WOLF_VARIANT);
/* 193 */     HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 197 */     AdvancementHolder root = Advancement.Builder.advancement().display((ItemLike)Blocks.HAY_BLOCK, (Component)Component.translatable("advancements.husbandry.root.title"), (Component)Component.translatable("advancements.husbandry.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/husbandry"), AdvancementType.TASK, false, false, false).addCriterion("consumed_item", ConsumeItemTrigger.TriggerInstance.usedItem()).save(output, "husbandry/root");
/* 210 */     AdvancementHolder plantSeed = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.WHEAT, (Component)Component.translatable("advancements.husbandry.plant_seed.title"), (Component)Component.translatable("advancements.husbandry.plant_seed.description"), null, AdvancementType.TASK, true, true, false).requirements(AdvancementRequirements.Strategy.OR).addCriterion("wheat", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.WHEAT)).addCriterion("pumpkin_stem", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.PUMPKIN_STEM)).addCriterion("melon_stem", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.MELON_STEM)).addCriterion("beetroots", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.BEETROOTS)).addCriterion("nether_wart", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.NETHER_WART)).addCriterion("torchflower", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.TORCHFLOWER_CROP)).addCriterion("pitcher_pod", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.PITCHER_CROP)).save(output, "husbandry/plant_seed");
/* 212 */     Advancement.Builder.advancement()
/* 213 */       .parent(root)
/* 214 */       .display((ItemLike)Items.TNT, (Component)Component.translatable("advancements.husbandry.uh_oh.title"), (Component)Component.translatable("advancements.husbandry.uh_oh.description"), null, AdvancementType.TASK, true, true, false)
/* 215 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 216 */       .addCriterion("pick_up_dropped_tnt", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByEntity(
/* 217 */           Optional.empty(), 
/* 218 */           Optional.of(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.SULFUR_CUBE_ARCHETYPE_EXPLOSIVE).build()), 
/* 219 */           Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.SULFUR_CUBE).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(false)))))))
/*     */       
/* 221 */       .addCriterion("give_tnt_directly", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
/* 222 */           Optional.empty(), 
/* 223 */           ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.SULFUR_CUBE_ARCHETYPE_EXPLOSIVE), 
/* 224 */           Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.SULFUR_CUBE).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(false)))))))
/*     */       
/* 226 */       .save(output, "husbandry/uh_oh");
/* 233 */     AdvancementHolder breedAnAnimalAdvancement = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.WHEAT, (Component)Component.translatable("advancements.husbandry.breed_an_animal.title"), (Component)Component.translatable("advancements.husbandry.breed_an_animal.description"), null, AdvancementType.TASK, true, true, false).requirements(AdvancementRequirements.Strategy.OR).addCriterion("bred", BredAnimalsTrigger.TriggerInstance.bredAnimals()).save(output, "husbandry/breed_an_animal");
/* 235 */     createBreedAllAnimalsAdvancement(breedAnAnimalAdvancement, output, (HolderGetter<EntityType<?>>)registryLookup1, BREEDABLE_ANIMALS.stream(), INDIRECTLY_BREEDABLE_ANIMALS.stream());
/* 237 */     addFood(Advancement.Builder.advancement(), (HolderGetter<Item>)registryLookup2)
/* 238 */       .parent(plantSeed)
/* 239 */       .display((ItemLike)Items.APPLE, (Component)Component.translatable("advancements.husbandry.balanced_diet.title"), (Component)Component.translatable("advancements.husbandry.balanced_diet.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 240 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 241 */       .save(output, "husbandry/balanced_diet");
/* 243 */     Advancement.Builder.advancement()
/* 244 */       .parent(plantSeed)
/* 245 */       .display((ItemLike)Items.NETHERITE_HOE, (Component)Component.translatable("advancements.husbandry.netherite_hoe.title"), (Component)Component.translatable("advancements.husbandry.netherite_hoe.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 246 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 247 */       .addCriterion("netherite_hoe", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.NETHERITE_HOE })).save(output, "husbandry/obtain_netherite_hoe");
/* 254 */     AdvancementHolder tameAnAnimal = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.LEAD, (Component)Component.translatable("advancements.husbandry.tame_an_animal.title"), (Component)Component.translatable("advancements.husbandry.tame_an_animal.description"), null, AdvancementType.TASK, true, true, false).addCriterion("tamed_animal", TameAnimalTrigger.TriggerInstance.tamedAnimal()).save(output, "husbandry/tame_an_animal");
/* 260 */     AdvancementHolder fishyBusiness = addFish(Advancement.Builder.advancement(), (HolderGetter<Item>)registryLookup2).parent(root).requirements(AdvancementRequirements.Strategy.OR).display((ItemLike)Items.FISHING_ROD, (Component)Component.translatable("advancements.husbandry.fishy_business.title"), (Component)Component.translatable("advancements.husbandry.fishy_business.description"), null, AdvancementType.TASK, true, true, false).save(output, "husbandry/fishy_business");
/* 266 */     AdvancementHolder tacticalFishing = addFishBuckets(Advancement.Builder.advancement(), (HolderGetter<Item>)registryLookup2).parent(fishyBusiness).requirements(AdvancementRequirements.Strategy.OR).display((ItemLike)Items.PUFFERFISH_BUCKET, (Component)Component.translatable("advancements.husbandry.tactical_fishing.title"), (Component)Component.translatable("advancements.husbandry.tactical_fishing.description"), null, AdvancementType.TASK, true, true, false).save(output, "husbandry/tactical_fishing");
/* 273 */     AdvancementHolder theCutestPredetor = Advancement.Builder.advancement().parent(tacticalFishing).requirements(AdvancementRequirements.Strategy.OR).addCriterion(BuiltInRegistries.ITEM.getKey(Items.AXOLOTL_BUCKET).getPath(), FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.AXOLOTL_BUCKET }))).display((ItemLike)Items.AXOLOTL_BUCKET, (Component)Component.translatable("advancements.husbandry.axolotl_in_a_bucket.title"), (Component)Component.translatable("advancements.husbandry.axolotl_in_a_bucket.description"), null, AdvancementType.TASK, true, true, false).save(output, "husbandry/axolotl_in_a_bucket");
/* 275 */     Advancement.Builder.advancement()
/* 276 */       .parent(theCutestPredetor)
/* 277 */       .addCriterion("kill_axolotl_target", EffectsChangedTrigger.TriggerInstance.gotEffectsFrom(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.AXOLOTL)))
/* 278 */       .display((ItemLike)Items.TROPICAL_FISH_BUCKET, (Component)Component.translatable("advancements.husbandry.kill_axolotl_target.title"), (Component)Component.translatable("advancements.husbandry.kill_axolotl_target.description"), null, AdvancementType.TASK, true, true, false)
/* 279 */       .save(output, "husbandry/kill_axolotl_target");
/* 281 */     addCatVariants(Advancement.Builder.advancement(), (HolderLookup<CatVariant>)registryLookup5)
/* 282 */       .parent(tameAnAnimal)
/* 283 */       .display((ItemLike)Items.COD, (Component)Component.translatable("advancements.husbandry.complete_catalogue.title"), (Component)Component.translatable("advancements.husbandry.complete_catalogue.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 284 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 285 */       .save(output, "husbandry/complete_catalogue");
/* 287 */     addTamedWolfVariants(Advancement.Builder.advancement(), (HolderLookup<WolfVariant>)registryLookup6)
/* 288 */       .parent(tameAnAnimal)
/* 289 */       .display((ItemLike)Items.BONE, (Component)Component.translatable("advancements.husbandry.whole_pack.title"), (Component)Component.translatable("advancements.husbandry.whole_pack.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 290 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 291 */       .save(output, "husbandry/whole_pack");
/* 297 */     AdvancementHolder safelyHarvestHoney = Advancement.Builder.advancement().parent(root).addCriterion("safely_harvest_honey", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, BlockTags.BEEHIVES)).setSmokey(true), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.GLASS_BOTTLE }))).display((ItemLike)Items.HONEY_BOTTLE, (Component)Component.translatable("advancements.husbandry.safely_harvest_honey.title"), (Component)Component.translatable("advancements.husbandry.safely_harvest_honey.description"), null, AdvancementType.TASK, true, true, false).save(output, "husbandry/safely_harvest_honey");
/* 303 */     AdvancementHolder waxOn = Advancement.Builder.advancement().parent(safelyHarvestHoney).display((ItemLike)Items.HONEYCOMB, (Component)Component.translatable("advancements.husbandry.wax_on.title"), (Component)Component.translatable("advancements.husbandry.wax_on.description"), null, AdvancementType.TASK, true, true, false).addCriterion("wax_on", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, ((BiMap)HoneycombItem.WAXABLES.get()).keySet())), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.HONEYCOMB }))).save(output, "husbandry/wax_on");
/* 305 */     Advancement.Builder.advancement()
/* 306 */       .parent(waxOn)
/* 307 */       .display((ItemLike)Items.STONE_AXE, (Component)Component.translatable("advancements.husbandry.wax_off.title"), (Component)Component.translatable("advancements.husbandry.wax_off.description"), null, AdvancementType.TASK, true, true, false)
/* 308 */       .addCriterion("wax_off", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, ((BiMap)HoneycombItem.WAX_OFF_BY_BLOCK.get()).keySet())), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, (ItemLike[])WAX_SCRAPING_TOOLS)))
/* 309 */       .save(output, "husbandry/wax_off");
/* 315 */     AdvancementHolder tadpoleInABucket = Advancement.Builder.advancement().parent(root).addCriterion(BuiltInRegistries.ITEM.getKey(Items.TADPOLE_BUCKET).getPath(), FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.TADPOLE_BUCKET }))).display((ItemLike)Items.TADPOLE_BUCKET, (Component)Component.translatable("advancements.husbandry.tadpole_in_a_bucket.title"), (Component)Component.translatable("advancements.husbandry.tadpole_in_a_bucket.description"), null, AdvancementType.TASK, true, true, false).save(output, "husbandry/tadpole_in_a_bucket");
/* 320 */     AdvancementHolder allFrogsOnALeash = addLeashedFrogVariants((HolderGetter<EntityType<?>>)registryLookup1, (HolderGetter<Item>)registryLookup2, (HolderLookup<FrogVariant>)registryLookup4, Advancement.Builder.advancement()).parent(tadpoleInABucket).display((ItemLike)Items.LEAD, (Component)Component.translatable("advancements.husbandry.leash_all_frog_variants.title"), (Component)Component.translatable("advancements.husbandry.leash_all_frog_variants.description"), null, AdvancementType.TASK, true, true, false).save(output, "husbandry/leash_all_frog_variants");
/* 322 */     Advancement.Builder.advancement()
/* 323 */       .parent(allFrogsOnALeash)
/* 324 */       .display((ItemLike)Items.VERDANT_FROGLIGHT, (Component)Component.translatable("advancements.husbandry.froglights.title"), (Component)Component.translatable("advancements.husbandry.froglights.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 325 */       .addCriterion("froglights", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.OCHRE_FROGLIGHT, (ItemLike)Items.PEARLESCENT_FROGLIGHT, (ItemLike)Items.VERDANT_FROGLIGHT })).save(output, "husbandry/froglights");
/* 328 */     Advancement.Builder.advancement()
/* 329 */       .parent(root)
/* 330 */       .addCriterion("silk_touch_nest", 
/* 331 */         BeeNestDestroyedTrigger.TriggerInstance.destroyedBeeNest(Blocks.BEE_NEST, 
/*     */           
/* 333 */           ItemPredicate.Builder.item()
/* 334 */           .withComponents(DataComponentMatchers.Builder.components()
/* 335 */             .partial(DataComponentPredicates.ENCHANTMENTS, (DataComponentPredicate)EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate((Holder)enchantments.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))).build()), 
/*     */           
/* 337 */           MinMaxBounds.Ints.exactly(3)))
/*     */       
/* 340 */       .display((ItemLike)Blocks.BEE_NEST, (Component)Component.translatable("advancements.husbandry.silk_touch_nest.title"), (Component)Component.translatable("advancements.husbandry.silk_touch_nest.description"), null, AdvancementType.TASK, true, true, false)
/* 341 */       .save(output, "husbandry/silk_touch_nest");
/* 343 */     Advancement.Builder.advancement()
/* 344 */       .parent(root)
/* 345 */       .display((ItemLike)Items.OAK_BOAT, (Component)Component.translatable("advancements.husbandry.ride_a_boat_with_a_goat.title"), (Component)Component.translatable("advancements.husbandry.ride_a_boat_with_a_goat.description"), null, AdvancementType.TASK, true, true, false)
/* 346 */       .addCriterion("ride_a_boat_with_a_goat", 
/* 347 */         StartRidingTrigger.TriggerInstance.playerStartsRiding(
/* 348 */           EntityPredicate.Builder.entity().vehicle(
/* 349 */             EntityPredicate.Builder.entity()
/* 350 */             .of((HolderGetter)registryLookup1, EntityTypeTags.BOAT)
/* 351 */             .passenger(
/* 352 */               EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.GOAT)))))
/*     */       
/* 357 */       .save(output, "husbandry/ride_a_boat_with_a_goat");
/* 359 */     Advancement.Builder.advancement()
/* 360 */       .parent(root)
/* 361 */       .display((ItemLike)Items.GLOW_INK_SAC, (Component)Component.translatable("advancements.husbandry.make_a_sign_glow.title"), (Component)Component.translatable("advancements.husbandry.make_a_sign_glow.description"), null, AdvancementType.TASK, true, true, false)
/* 362 */       .addCriterion("make_a_sign_glow", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, BlockTags.ALL_SIGNS)), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.GLOW_INK_SAC }))).save(output, "husbandry/make_a_sign_glow");
/* 369 */     AdvancementHolder itemDeliveredToPlayer = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.COOKIE, (Component)Component.translatable("advancements.husbandry.allay_deliver_item_to_player.title"), (Component)Component.translatable("advancements.husbandry.allay_deliver_item_to_player.description"), null, AdvancementType.TASK, true, true, true).addCriterion("allay_deliver_item_to_player", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByPlayer(Optional.empty(), Optional.empty(), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.ALLAY))))).save(output, "husbandry/allay_deliver_item_to_player");
/* 371 */     Advancement.Builder.advancement()
/* 372 */       .parent(itemDeliveredToPlayer)
/* 373 */       .display((ItemLike)Items.NOTE_BLOCK, (Component)Component.translatable("advancements.husbandry.allay_deliver_cake_to_note_block.title"), (Component)Component.translatable("advancements.husbandry.allay_deliver_cake_to_note_block.description"), null, AdvancementType.TASK, true, true, true)
/* 374 */       .addCriterion("allay_deliver_cake_to_note_block", ItemUsedOnLocationTrigger.TriggerInstance.allayDropItemOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.NOTE_BLOCK })), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.CAKE }))).save(output, "husbandry/allay_deliver_cake_to_note_block");
/* 381 */     AdvancementHolder obtainSnifferEgg = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.SNIFFER_EGG, (Component)Component.translatable("advancements.husbandry.obtain_sniffer_egg.title"), (Component)Component.translatable("advancements.husbandry.obtain_sniffer_egg.description"), null, AdvancementType.TASK, true, true, true).addCriterion("obtain_sniffer_egg", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.SNIFFER_EGG })).save(output, "husbandry/obtain_sniffer_egg");
/* 392 */     AdvancementHolder feedSnifflet = Advancement.Builder.advancement().parent(obtainSnifferEgg).display((ItemLike)Items.TORCHFLOWER_SEEDS, (Component)Component.translatable("advancements.husbandry.feed_snifflet.title"), (Component)Component.translatable("advancements.husbandry.feed_snifflet.description"), null, AdvancementType.TASK, true, true, true).addCriterion("feed_snifflet", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.SNIFFER_FOOD), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.SNIFFER).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(true))))))).save(output, "husbandry/feed_snifflet");
/* 394 */     Advancement.Builder.advancement()
/* 395 */       .parent(feedSnifflet)
/* 396 */       .display((ItemLike)Items.PITCHER_POD, (Component)Component.translatable("advancements.husbandry.plant_any_sniffer_seed.title"), (Component)Component.translatable("advancements.husbandry.plant_any_sniffer_seed.description"), null, AdvancementType.TASK, true, true, true)
/* 397 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 398 */       .addCriterion("torchflower", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.TORCHFLOWER_CROP))
/* 399 */       .addCriterion("pitcher_pod", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.PITCHER_CROP))
/* 400 */       .save(output, "husbandry/plant_any_sniffer_seed");
/* 402 */     Advancement.Builder.advancement()
/* 403 */       .parent(tameAnAnimal)
/* 404 */       .display((ItemLike)Items.SHEARS, (Component)Component.translatable("advancements.husbandry.remove_wolf_armor.title"), (Component)Component.translatable("advancements.husbandry.remove_wolf_armor.description"), null, AdvancementType.TASK, true, true, false)
/* 405 */       .addCriterion("remove_wolf_armor", PlayerInteractTrigger.TriggerInstance.equipmentSheared(
/* 406 */           ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.WOLF_ARMOR }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.WOLF)))))
/* 408 */       .save(output, "husbandry/remove_wolf_armor");
/* 410 */     Advancement.Builder.advancement()
/* 411 */       .parent(tameAnAnimal)
/* 412 */       .display((ItemLike)Items.WOLF_ARMOR, (Component)Component.translatable("advancements.husbandry.repair_wolf_armor.title"), (Component)Component.translatable("advancements.husbandry.repair_wolf_armor.description"), null, AdvancementType.TASK, true, true, false)
/* 413 */       .addCriterion("repair_wolf_armor", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
/* 414 */           ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.ARMADILLO_SCUTE }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.WOLF).equipment(
/* 416 */                 EntityEquipmentPredicate.Builder.equipment().body(
/* 417 */                   ItemPredicate.Builder.item()
/* 418 */                   .of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.WOLF_ARMOR }).withComponents(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.DAMAGE, Integer.valueOf(0))).build())))))))
/*     */       
/* 423 */       .save(output, "husbandry/repair_wolf_armor");
/* 425 */     Advancement.Builder.advancement()
/* 426 */       .parent(root)
/* 427 */       .display((ItemLike)Items.DRIED_GHAST, (Component)Component.translatable("advancements.husbandry.place_dried_ghast_in_water.title"), (Component)Component.translatable("advancements.husbandry.place_dried_ghast_in_water.description"), null, AdvancementType.TASK, true, true, false)
/* 428 */       .addCriterion("place_dried_ghast_in_water", ItemUsedOnLocationTrigger.TriggerInstance.placedBlockWithProperties(Blocks.DRIED_GHAST, (Property)BlockStateProperties.WATERLOGGED, true))
/* 429 */       .save(output, "husbandry/place_dried_ghast_in_water");
/*     */   }
/*     */   
/*     */   public static AdvancementHolder createBreedAllAnimalsAdvancement(AdvancementHolder parent, Consumer<AdvancementHolder> output, HolderGetter<EntityType<?>> entityTypes, Stream<EntityType<?>> breedable, Stream<EntityType<?>> indirectlyBreedable) {
/* 433 */     return addBreedable(Advancement.Builder.advancement(), breedable, entityTypes, indirectlyBreedable)
/* 434 */       .parent(parent)
/* 435 */       .display((ItemLike)Items.GOLDEN_CARROT, (Component)Component.translatable("advancements.husbandry.breed_all_animals.title"), (Component)Component.translatable("advancements.husbandry.breed_all_animals.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 436 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 437 */       .save(output, "husbandry/bred_all_animals");
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addLeashedFrogVariants(HolderGetter<EntityType<?>> entityTypes, HolderGetter<Item> items, HolderLookup<FrogVariant> frogVariants, Advancement.Builder advancement) {
/* 441 */     sortedVariants(frogVariants).forEach(frogVariant -> advancement.addCriterion(frogVariant.key().identifier().toString(), PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(items, new ItemLike[] { (ItemLike)Items.LEAD }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(entityTypes, EntityTypes.FROG).components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.FROG_VARIANT, frogVariant)).build()))))));
/* 454 */     return advancement;
/*     */   }
/*     */   
/*     */   private static <T> Stream<Holder.Reference<T>> sortedVariants(HolderLookup<T> variants) {
/* 458 */     return (Stream)variants.listElements().sorted(HOLDER_KEY_COMPARATOR);
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addFood(Advancement.Builder advancement, HolderGetter<Item> items) {
/* 462 */     for (Item food : EDIBLE_ITEMS)
/* 463 */       advancement.addCriterion(BuiltInRegistries.ITEM.getKey(food).getPath(), ConsumeItemTrigger.TriggerInstance.usedItem(items, (ItemLike)food)); 
/* 465 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addBreedable(Advancement.Builder advancement, Stream<EntityType<?>> breedable, HolderGetter<EntityType<?>> entityTypes, Stream<EntityType<?>> indirectlyBreedable) {
/* 469 */     breedable.forEach(animal -> advancement.addCriterion(EntityType.getKey(animal).toString(), BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().of(entityTypes, animal))));
/* 472 */     indirectlyBreedable.forEach(animal -> advancement.addCriterion(EntityType.getKey(animal).toString(), BredAnimalsTrigger.TriggerInstance.bredAnimals(Optional.of(EntityPredicate.Builder.entity().of(entityTypes, animal).build()), Optional.of(EntityPredicate.Builder.entity().of(entityTypes, animal).build()), Optional.empty())));
/* 475 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addFishBuckets(Advancement.Builder advancement, HolderGetter<Item> items) {
/* 479 */     for (Item bucket : FISH_BUCKETS) {
/* 480 */       advancement.addCriterion(BuiltInRegistries.ITEM.getKey(bucket).getPath(), FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of(items, new ItemLike[] { (ItemLike)bucket })));
/*     */     } 
/* 482 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addFish(Advancement.Builder advancement, HolderGetter<Item> items) {
/* 486 */     for (Item fish : FISH) {
/* 487 */       advancement.addCriterion(BuiltInRegistries.ITEM.getKey(fish).getPath(), FishingRodHookedTrigger.TriggerInstance.fishedItem(Optional.empty(), Optional.empty(), Optional.of(ItemPredicate.Builder.item().of(items, new ItemLike[] { (ItemLike)fish }).build())));
/*     */     } 
/* 489 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addCatVariants(Advancement.Builder advancement, HolderLookup<CatVariant> catVariants) {
/* 493 */     sortedVariants(catVariants).forEach(v -> advancement.addCriterion(v.key().identifier().toString(), TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().components(DataComponentExactPredicate.expect(DataComponents.CAT_VARIANT, v)))));
/* 502 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addTamedWolfVariants(Advancement.Builder advancement, HolderLookup<WolfVariant> wolfVariants) {
/* 506 */     sortedVariants(wolfVariants).forEach(v -> advancement.addCriterion(v.key().identifier().toString(), TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().components(DataComponentExactPredicate.expect(DataComponents.WOLF_VARIANT, v)))));
/* 515 */     return advancement;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\advancements\packs\VanillaHusbandryAdvancements.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */