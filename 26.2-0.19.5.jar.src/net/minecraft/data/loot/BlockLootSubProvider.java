/*     */ package net.minecraft.data.loot;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.advancements.predicates.BlockPredicate;
/*     */ import net.minecraft.advancements.predicates.DataComponentMatchers;
/*     */ import net.minecraft.advancements.predicates.EnchantmentPredicate;
/*     */ import net.minecraft.advancements.predicates.ItemPredicate;
/*     */ import net.minecraft.advancements.predicates.LocationPredicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.advancements.predicates.StatePropertiesPredicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.component.predicates.EnchantmentsPredicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.BeehiveBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CandleBlock;
/*     */ import net.minecraft.world.level.block.CaveVines;
/*     */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.DoublePlantBlock;
/*     */ import net.minecraft.world.level.block.FlowerPotBlock;
/*     */ import net.minecraft.world.level.block.MossyCarpetBlock;
/*     */ import net.minecraft.world.level.block.MultifaceBlock;
/*     */ import net.minecraft.world.level.block.SegmentableBlock;
/*     */ import net.minecraft.world.level.block.SlabBlock;
/*     */ import net.minecraft.world.level.block.StemBlock;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SlabType;
/*     */ import net.minecraft.world.level.storage.loot.IntRange;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
/*     */ import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
/*     */ import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
/*     */ import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LimitCount;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.MatchTool;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public abstract class BlockLootSubProvider implements LootTableSubProvider {
/*     */   protected final HolderLookup.Provider registries;
/*     */   
/*     */   private final Set<Item> explosionResistant;
/*     */   
/*     */   private final FeatureFlagSet enabledFeatures;
/*     */   
/*     */   protected LootItemCondition.Builder hasSilkTouch() {
/*  85 */     return MatchTool.toolMatches(ItemPredicate.Builder.item().withComponents(
/*  86 */           DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, (DataComponentPredicate)EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate((Holder)this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))).build()));
/*     */   }
/*     */   
/*     */   protected LootItemCondition.Builder doesNotHaveSilkTouch() {
/*  91 */     return hasSilkTouch().invert();
/*     */   }
/*     */   
/*     */   protected LootItemCondition.Builder hasShears() {
/*  95 */     return MatchTool.toolMatches(ItemPredicate.Builder.item().of((HolderGetter)this.registries.lookupOrThrow(Registries.ITEM), new ItemLike[] { (ItemLike)Items.SHEARS }));
/*     */   }
/*     */   
/*     */   private LootItemCondition.Builder hasShearsOrSilkTouch() {
/* 101 */     return (LootItemCondition.Builder)hasShears().or(hasSilkTouch());
/*     */   }
/*     */   
/*     */   private LootItemCondition.Builder doesNotHaveShearsOrSilkTouch() {
/* 105 */     return hasShearsOrSilkTouch().invert();
/*     */   }
/*     */   
/* 111 */   private final Map<ResourceKey<LootTable>, LootTable.Builder> map = new HashMap<>();
/*     */   
/* 113 */   protected static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[] { 0.05F, 0.0625F, 0.083333336F, 0.1F };
/*     */   
/* 114 */   private static final float[] NORMAL_LEAVES_STICK_CHANCES = new float[] { 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F };
/*     */   
/*     */   protected BlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures, HolderLookup.Provider registries) {
/* 117 */     this.explosionResistant = explosionResistant;
/* 118 */     this.enabledFeatures = enabledFeatures;
/* 119 */     this.registries = registries;
/*     */   }
/*     */   
/*     */   protected <T extends FunctionUserBuilder<T>> T applyExplosionDecay(ItemLike type, FunctionUserBuilder<T> builder) {
/* 123 */     if (!this.explosionResistant.contains(type.asItem()))
/* 124 */       return (T)builder.apply((LootItemFunction.Builder)ApplyExplosionDecay.explosionDecay()); 
/* 127 */     return (T)builder.unwrap();
/*     */   }
/*     */   
/*     */   protected <T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike type, ConditionUserBuilder<T> builder) {
/* 131 */     if (!this.explosionResistant.contains(type.asItem()))
/* 132 */       return (T)builder.when(ExplosionCondition.survivesExplosion()); 
/* 135 */     return (T)builder.unwrap();
/*     */   }
/*     */   
/*     */   public LootTable.Builder createSingleItemTable(ItemLike drop) {
/* 139 */     return LootTable.lootTable()
/* 140 */       .withPool(applyExplosionCondition(drop, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 141 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 142 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(drop))));
/*     */   }
/*     */   
/*     */   private static LootTable.Builder createSelfDropDispatchTable(Block original, LootItemCondition.Builder condition, LootPoolEntryContainer.Builder<?> entry) {
/* 147 */     return LootTable.lootTable()
/* 148 */       .withPool(LootPool.lootPool()
/* 149 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 150 */         .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)original)
/* 151 */           .when(condition))
/* 152 */           .otherwise(entry)));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createSilkTouchDispatchTable(Block original, LootPoolEntryContainer.Builder<?> entry) {
/* 158 */     return createSelfDropDispatchTable(original, hasSilkTouch(), entry);
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createShearsDispatchTable(Block original, LootPoolEntryContainer.Builder<?> entry) {
/* 162 */     return createSelfDropDispatchTable(original, hasShears(), entry);
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createSilkTouchOrShearsDispatchTable(Block original, LootPoolEntryContainer.Builder<?> entry) {
/* 166 */     return createSelfDropDispatchTable(original, hasShearsOrSilkTouch(), entry);
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createSingleItemTableWithSilkTouch(Block original, ItemLike drop) {
/* 170 */     return createSilkTouchDispatchTable(original, applyExplosionCondition((ItemLike)original, (ConditionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(drop)));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createSingleItemTable(ItemLike drop, NumberProvider count) {
/* 174 */     return LootTable.lootTable()
/* 175 */       .withPool(LootPool.lootPool()
/* 176 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 177 */         .add(applyExplosionDecay(drop, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(drop).apply((LootItemFunction.Builder)SetItemCountFunction.setCount(count)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createSingleItemTableWithSilkTouch(Block original, ItemLike drop, NumberProvider count) {
/* 182 */     return createSilkTouchDispatchTable(original, applyExplosionDecay((ItemLike)original, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(drop).apply((LootItemFunction.Builder)SetItemCountFunction.setCount(count))));
/*     */   }
/*     */   
/*     */   private LootTable.Builder createSilkTouchOnlyTable(ItemLike drop) {
/* 186 */     return LootTable.lootTable()
/* 187 */       .withPool(LootPool.lootPool()
/* 188 */         .when(hasSilkTouch())
/* 189 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 190 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(drop)));
/*     */   }
/*     */   
/*     */   private LootTable.Builder createPotFlowerItemTable(ItemLike flower) {
/* 195 */     return LootTable.lootTable()
/* 196 */       .withPool(applyExplosionCondition((ItemLike)Blocks.FLOWER_POT, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 197 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 198 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.FLOWER_POT))))
/*     */       
/* 200 */       .withPool(applyExplosionCondition(flower, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 201 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 202 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(flower))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createSlabItemTable(Block slab) {
/* 207 */     return LootTable.lootTable()
/* 208 */       .withPool(LootPool.lootPool()
/* 209 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 210 */         .add(applyExplosionDecay((ItemLike)slab, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)slab)
/* 211 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)).when(
/* 212 */                 (LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SlabBlock.TYPE, (Comparable)SlabType.DOUBLE)))))));
/*     */   }
/*     */   
/*     */   protected <T extends Comparable<T> & net.minecraft.util.StringRepresentable> LootTable.Builder createSinglePropConditionTable(Block drop, Property<T> property, T value) {
/* 219 */     return LootTable.lootTable()
/* 220 */       .withPool(applyExplosionCondition((ItemLike)drop, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 221 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 222 */           .add(LootItem.lootTableItem((ItemLike)drop)
/* 223 */             .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(drop).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, (Comparable)value))))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createNameableBlockEntityTable(Block drop) {
/* 229 */     return LootTable.lootTable()
/* 230 */       .withPool(applyExplosionCondition((ItemLike)drop, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 231 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 232 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)drop)
/* 233 */             .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 234 */               .include(DataComponents.CUSTOM_NAME)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createShulkerBoxDrop(Block shulkerBox) {
/* 240 */     return LootTable.lootTable()
/* 241 */       .withPool(applyExplosionCondition((ItemLike)shulkerBox, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 242 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 243 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)shulkerBox)
/* 244 */             .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 245 */               .include(DataComponents.CUSTOM_NAME)
/* 246 */               .include(DataComponents.CONTAINER)
/* 247 */               .include(DataComponents.LOCK)
/* 248 */               .include(DataComponents.CONTAINER_LOOT)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createCopperOreDrops(Block block) {
/* 255 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 256 */     return createSilkTouchDispatchTable(block, 
/* 257 */         applyExplosionDecay((ItemLike)block, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.RAW_COPPER)
/* 258 */           .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F)))
/* 259 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addOreBonusCount((Holder)enchantments.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createLapisOreDrops(Block block) {
/* 265 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 266 */     return createSilkTouchDispatchTable(block, 
/* 267 */         applyExplosionDecay((ItemLike)block, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI)
/* 268 */           .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F)))
/* 269 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addOreBonusCount((Holder)enchantments.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createRedstoneOreDrops(Block block) {
/* 275 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 276 */     return createSilkTouchDispatchTable(block, 
/* 277 */         applyExplosionDecay((ItemLike)block, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.REDSTONE)
/* 278 */           .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 5.0F)))
/* 279 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount((Holder)enchantments.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createBannerDrop(Block original) {
/* 285 */     return LootTable.lootTable()
/* 286 */       .withPool(applyExplosionCondition((ItemLike)original, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 287 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 288 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)original)
/* 289 */             .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 290 */               .include(DataComponents.CUSTOM_NAME)
/* 291 */               .include(DataComponents.ITEM_NAME)
/* 292 */               .include(DataComponents.TOOLTIP_DISPLAY)
/* 293 */               .include(DataComponents.BANNER_PATTERNS)
/* 294 */               .include(DataComponents.RARITY)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createBeeNestDrop(Block original) {
/* 301 */     return LootTable.lootTable()
/* 302 */       .withPool(LootPool.lootPool()
/* 303 */         .when(hasSilkTouch())
/* 304 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 305 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)original)
/* 306 */           .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 307 */             .include(DataComponents.BEES))
/*     */           
/* 309 */           .apply((LootItemFunction.Builder)CopyBlockState.copyState(original).copy((Property)BeehiveBlock.HONEY_LEVEL))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createBeeHiveDrop(Block original) {
/* 315 */     return LootTable.lootTable()
/* 316 */       .withPool(LootPool.lootPool()
/* 317 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 318 */         .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)original)
/* 319 */           .when(hasSilkTouch()))
/* 320 */           .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 321 */             .include(DataComponents.BEES))
/*     */           
/* 323 */           .apply((LootItemFunction.Builder)CopyBlockState.copyState(original).copy((Property)BeehiveBlock.HONEY_LEVEL))
/* 324 */           .otherwise((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)original))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createCaveVinesDrop(Block original) {
/* 330 */     return LootTable.lootTable()
/* 331 */       .withPool(LootPool.lootPool()
/* 332 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOW_BERRIES))
/* 333 */         .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(original).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)CaveVines.BERRIES, true))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createCopperGolemStatueBlock(Block block) {
/* 338 */     return LootTable.lootTable()
/* 339 */       .withPool(applyExplosionCondition((ItemLike)block, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 340 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 341 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)block)
/* 342 */             .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 343 */               .include(DataComponents.CUSTOM_NAME))
/* 344 */             .apply((LootItemFunction.Builder)CopyBlockState.copyState(block).copy((Property)CopperGolemStatueBlock.POSE)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createOreDrop(Block original, Item drop) {
/* 350 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 351 */     return createSilkTouchDispatchTable(original, 
/* 352 */         applyExplosionDecay((ItemLike)original, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)drop)
/* 353 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addOreBonusCount((Holder)enchantments.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createMushroomBlockDrop(Block original, ItemLike drop) {
/* 359 */     return createSilkTouchDispatchTable(original, applyExplosionDecay((ItemLike)original, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(drop)
/* 360 */           .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(-6.0F, 2.0F)))
/* 361 */           .apply((LootItemFunction.Builder)LimitCount.limitCount(IntRange.lowerBound(0)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createGrassDrops(Block original) {
/* 367 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 368 */     return createShearsDispatchTable(original, applyExplosionDecay((ItemLike)original, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS)
/* 369 */           .when(LootItemRandomChanceCondition.randomChance(0.125F)))
/* 370 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), 2))));
/*     */   }
/*     */   
/*     */   public LootTable.Builder createStemDrops(Block block, Item drop) {
/* 376 */     return LootTable.lootTable()
/* 377 */       .withPool(applyExplosionDecay((ItemLike)block, (FunctionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 378 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 379 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)drop)
/* 380 */             .apply(StemBlock.AGE.getPossibleValues(), age -> SetItemCountFunction.setCount((NumberProvider)BinomialDistributionGenerator.binomial(3, (age.intValue() + 1) / 15.0F)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)StemBlock.AGE, age.intValue())))))));
/*     */   }
/*     */   
/*     */   public LootTable.Builder createAttachedStemDrops(Block block, Item drop) {
/* 386 */     return LootTable.lootTable()
/* 387 */       .withPool(applyExplosionDecay((ItemLike)block, (FunctionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 388 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 389 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)drop)
/* 390 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)BinomialDistributionGenerator.binomial(3, 0.53333336F))))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createShearsOnlyDrop(ItemLike drop) {
/* 396 */     return LootTable.lootTable()
/* 397 */       .withPool(LootPool.lootPool()
/* 398 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 399 */         .when(hasShears())
/* 400 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(drop)));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createShearsOrSilkTouchOnlyDrop(ItemLike drop) {
/* 405 */     return LootTable.lootTable()
/* 406 */       .withPool(LootPool.lootPool()
/* 407 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 408 */         .when(hasShearsOrSilkTouch())
/* 409 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(drop)));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createMultifaceBlockDrops(Block block, LootItemCondition.Builder condition) {
/* 414 */     return LootTable.lootTable()
/* 415 */       .withPool(LootPool.lootPool()
/* 416 */         .add(applyExplosionDecay((ItemLike)block, 
/* 417 */             (FunctionUserBuilder<LootPoolEntryContainer.Builder>)((LootPoolSingletonContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)block)
/* 418 */             .when(condition))
/* 419 */             .apply((Object[])Direction.values(), dir -> SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F), true).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)MultifaceBlock.getFaceProperty(dir), true)))))
/* 420 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(-1.0F), true)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createMultifaceBlockDrops(Block block) {
/* 426 */     return LootTable.lootTable()
/* 427 */       .withPool(LootPool.lootPool()
/* 428 */         .add(applyExplosionDecay((ItemLike)block, 
/* 429 */             (FunctionUserBuilder<LootPoolEntryContainer.Builder>)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)block)
/* 430 */             .apply((Object[])Direction.values(), dir -> SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F), true).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)MultifaceBlock.getFaceProperty(dir), true)))))
/* 431 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(-1.0F), true)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createMossyCarpetBlockDrops(Block block) {
/* 437 */     return LootTable.lootTable()
/* 438 */       .withPool(LootPool.lootPool()
/* 439 */         .add(applyExplosionDecay((ItemLike)block, 
/* 440 */             (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)block)
/* 441 */             .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)MossyCarpetBlock.BASE, true))))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createLeavesDrops(Block original, Block sapling, float... saplingChances) {
/* 447 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 448 */     return createSilkTouchOrShearsDispatchTable(original, ((LootPoolSingletonContainer.Builder)
/* 449 */         applyExplosionCondition((ItemLike)original, (ConditionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)sapling)))
/* 450 */         .when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), saplingChances)))
/*     */       
/* 452 */       .withPool(LootPool.lootPool()
/* 453 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 454 */         .when(doesNotHaveShearsOrSilkTouch())
/* 455 */         .add(((LootPoolSingletonContainer.Builder)applyExplosionDecay((ItemLike)original, (FunctionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.STICK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/* 456 */           .when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), NORMAL_LEAVES_STICK_CHANCES))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createOakLeavesDrops(Block original, Block sapling, float... saplingChances) {
/* 462 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 463 */     return 
/* 464 */       createLeavesDrops(original, sapling, saplingChances)
/* 465 */       .withPool(LootPool.lootPool()
/* 466 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 467 */         .when(doesNotHaveShearsOrSilkTouch())
/* 468 */         .add(((LootPoolSingletonContainer.Builder)applyExplosionCondition((ItemLike)original, (ConditionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.APPLE)))
/* 469 */           .when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), new float[] { 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F }))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createMangroveLeavesDrops(Block block) {
/* 475 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 476 */     return createSilkTouchOrShearsDispatchTable(block, ((LootPoolSingletonContainer.Builder)
/* 477 */         applyExplosionDecay((ItemLike)Blocks.MANGROVE_LEAVES, (FunctionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.STICK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/* 478 */         .when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), NORMAL_LEAVES_STICK_CHANCES)));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createCropDrops(Block original, Item cropDrop, Item seedDrop, LootItemCondition.Builder isMaxAge) {
/* 483 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 484 */     return applyExplosionDecay((ItemLike)original, (FunctionUserBuilder<LootTable.Builder>)LootTable.lootTable()
/* 485 */         .withPool(LootPool.lootPool()
/* 486 */           .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)cropDrop)
/* 487 */             .when(isMaxAge))
/* 488 */             .otherwise((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)seedDrop))))
/*     */         
/* 491 */         .withPool(LootPool.lootPool()
/* 492 */           .when(isMaxAge)
/* 493 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)seedDrop).apply((LootItemFunction.Builder)ApplyBonusCount.addBonusBinomialDistributionCount((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createDoublePlantShearsDrop(Block block) {
/* 499 */     return LootTable.lootTable().withPool(LootPool.lootPool()
/* 500 */         .when(hasShears())
/* 501 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)block).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createDoublePlantWithSeedDrops(Block block, Block drop) {
/* 505 */     HolderLookup.RegistryLookup<Block> blocks = this.registries.lookupOrThrow(Registries.BLOCK);
/* 509 */     AlternativesEntry.Builder builder = ((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)drop).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F))).when(hasShears())).otherwise(((LootPoolSingletonContainer.Builder)applyExplosionCondition((ItemLike)block, (ConditionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS)))
/* 510 */         .when(LootItemRandomChanceCondition.randomChance(0.125F)));
/* 515 */     return LootTable.lootTable()
/* 516 */       .withPool(
/* 517 */         LootPool.lootPool()
/* 518 */         .add((LootPoolEntryContainer.Builder)builder)
/* 519 */         .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.LOWER)))
/* 520 */         .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)blocks, new Block[] { block }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.UPPER))), new BlockPos(0, 1, 0))))
/*     */       
/* 522 */       .withPool(
/* 523 */         LootPool.lootPool()
/* 524 */         .add((LootPoolEntryContainer.Builder)builder)
/* 525 */         .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.UPPER)))
/* 526 */         .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)blocks, new Block[] { block }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.LOWER))), new BlockPos(0, -1, 0))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createCandleDrops(Block block) {
/* 531 */     return LootTable.lootTable()
/* 532 */       .withPool(LootPool.lootPool()
/* 533 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 534 */         .add(applyExplosionDecay((ItemLike)block, LootItem.lootTableItem((ItemLike)block)
/* 535 */             .apply(List.of(Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)), count -> SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(count.intValue())).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)CandleBlock.CANDLES, count.intValue())))))));
/*     */   }
/*     */   
/*     */   public LootTable.Builder createSegmentedBlockDrops(Block block) {
/* 541 */     if (block instanceof SegmentableBlock) {
/* 541 */       SegmentableBlock segmentableBlock = (SegmentableBlock)block;
/* 542 */       return LootTable.lootTable()
/* 543 */         .withPool(LootPool.lootPool()
/* 544 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 545 */           .add(applyExplosionDecay((ItemLike)block, LootItem.lootTableItem((ItemLike)block)
/* 546 */               .apply(IntStream.rangeClosed(1, 4).boxed().toList(), count -> SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(count.intValue())).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)segmentableBlock.getSegmentAmountProperty(), count.intValue())))))));
/*     */     } 
/* 550 */     return noDrop();
/*     */   }
/*     */   
/*     */   protected static LootTable.Builder createCandleCakeDrops(Block candle) {
/* 554 */     return LootTable.lootTable()
/* 555 */       .withPool(LootPool.lootPool()
/* 556 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 557 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)candle)));
/*     */   }
/*     */   
/*     */   public static LootTable.Builder noDrop() {
/* 562 */     return LootTable.lootTable();
/*     */   }
/*     */   
/*     */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/* 569 */     generate();
/* 571 */     Set<ResourceKey<LootTable>> seen = new HashSet<>();
/* 572 */     for (Iterator<Block> iterator = BuiltInRegistries.BLOCK.iterator(); iterator.hasNext(); ) {
/* 572 */       Block block = iterator.next();
/* 573 */       if (!block.isEnabled(this.enabledFeatures))
/*     */         continue; 
/* 576 */       block.getLootTable().ifPresent(lootTable -> {
/*     */             if (seen.add(lootTable)) {
/*     */               LootTable.Builder builder = this.map.remove(lootTable);
/*     */               if (builder == null)
/*     */                 throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", new Object[] { lootTable.identifier(), BuiltInRegistries.BLOCK.getKey(block) })); 
/*     */               output.accept(lootTable, builder);
/*     */             } 
/*     */           });
/*     */     } 
/* 587 */     if (!this.map.isEmpty())
/* 588 */       throw new IllegalStateException("Created block loot tables for non-blocks: " + String.valueOf(this.map.keySet())); 
/*     */   }
/*     */   
/*     */   protected void addNetherVinesDropTable(Block vineBlock, Block plantBlock) {
/* 593 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 594 */     LootTable.Builder builder = createSilkTouchOrShearsDispatchTable(vineBlock, 
/* 595 */         LootItem.lootTableItem((ItemLike)vineBlock).when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), new float[] { 0.33F, 0.55F, 0.77F, 1.0F })));
/* 596 */     add(vineBlock, builder);
/* 597 */     add(plantBlock, builder);
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createDoorTable(Block block) {
/* 601 */     return createSinglePropConditionTable(block, (Property<DoubleBlockHalf>)DoorBlock.HALF, DoubleBlockHalf.LOWER);
/*     */   }
/*     */   
/*     */   protected void dropPottedContents(Block potted) {
/* 605 */     add(potted, block -> createPotFlowerItemTable((ItemLike)((FlowerPotBlock)block).getPotted()));
/*     */   }
/*     */   
/*     */   protected void otherWhenSilkTouch(Block block, Block other) {
/* 609 */     add(block, createSilkTouchOnlyTable((ItemLike)other));
/*     */   }
/*     */   
/*     */   protected void dropOther(Block block, ItemLike drop) {
/* 613 */     add(block, createSingleItemTable(drop));
/*     */   }
/*     */   
/*     */   protected void dropWhenSilkTouch(Block block) {
/* 617 */     otherWhenSilkTouch(block, block);
/*     */   }
/*     */   
/*     */   protected void dropSelf(Block block) {
/* 621 */     dropOther(block, (ItemLike)block);
/*     */   }
/*     */   
/*     */   protected void add(Block block, Function<Block, LootTable.Builder> builder) {
/* 625 */     add(block, builder.apply(block));
/*     */   }
/*     */   
/*     */   protected void add(Block block, LootTable.Builder builder) {
/* 629 */     this.map.put((ResourceKey<LootTable>)block.getLootTable().orElseThrow(() -> new IllegalStateException("Block " + String.valueOf(block) + " does not have loot table")), builder);
/*     */   }
/*     */   
/*     */   protected abstract void generate();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\BlockLootSubProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */