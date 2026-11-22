/*     */ package net.minecraft.core.registries;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntitySubPredicates;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.advancements.triggers.CriterionTrigger;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfos;
/*     */ import net.minecraft.core.DefaultedMappedRegistry;
/*     */ import net.minecraft.core.DefaultedRegistry;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.RegistrationInfo;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.core.component.DataComponentInitializers;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.particles.ParticleType;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.gametest.framework.BuiltinTestFunctions;
/*     */ import net.minecraft.gametest.framework.GameTestHelper;
/*     */ import net.minecraft.gametest.framework.GameTestInstance;
/*     */ import net.minecraft.gametest.framework.TestEnvironmentDefinition;
/*     */ import net.minecraft.network.chat.numbers.NumberFormatType;
/*     */ import net.minecraft.network.chat.numbers.NumberFormatTypes;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.Bootstrap;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.server.dialog.DialogTypes;
/*     */ import net.minecraft.server.dialog.action.Action;
/*     */ import net.minecraft.server.dialog.action.ActionTypes;
/*     */ import net.minecraft.server.dialog.body.DialogBody;
/*     */ import net.minecraft.server.dialog.body.DialogBodyTypes;
/*     */ import net.minecraft.server.dialog.input.InputControl;
/*     */ import net.minecraft.server.dialog.input.InputControlTypes;
/*     */ import net.minecraft.server.jsonrpc.IncomingRpcMethod;
/*     */ import net.minecraft.server.jsonrpc.IncomingRpcMethods;
/*     */ import net.minecraft.server.jsonrpc.OutgoingRpcMethod;
/*     */ import net.minecraft.server.jsonrpc.OutgoingRpcMethods;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.server.permissions.Permission;
/*     */ import net.minecraft.server.permissions.PermissionCheck;
/*     */ import net.minecraft.server.permissions.PermissionCheckTypes;
/*     */ import net.minecraft.server.permissions.PermissionTypes;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.debug.DebugSubscription;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.valueproviders.FloatProvider;
/*     */ import net.minecraft.util.valueproviders.FloatProviders;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.IntProviders;
/*     */ import net.minecraft.world.attribute.AttributeType;
/*     */ import net.minecraft.world.attribute.AttributeTypes;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.entity.variant.SpawnCondition;
/*     */ import net.minecraft.world.entity.variant.SpawnConditions;
/*     */ import net.minecraft.world.inventory.MenuType;
/*     */ import net.minecraft.world.item.CreativeModeTab;
/*     */ import net.minecraft.world.item.CreativeModeTabs;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.item.consume_effects.ConsumeEffect;
/*     */ import net.minecraft.world.item.crafting.RecipeBookCategories;
/*     */ import net.minecraft.world.item.crafting.RecipeBookCategory;
/*     */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*     */ import net.minecraft.world.item.crafting.RecipeSerializers;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplays;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplays;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
/*     */ import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
/*     */ import net.minecraft.world.item.enchantment.providers.EnchantmentProviderTypes;
/*     */ import net.minecraft.world.item.slot.SlotSource;
/*     */ import net.minecraft.world.item.slot.SlotSources;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.BiomeSources;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.BlockTypes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotPattern;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerators;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.PositionSourceType;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.levelgen.DensityFunctions;
/*     */ import net.minecraft.world.level.levelgen.SurfaceRules;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
/*     */ import net.minecraft.world.level.levelgen.carver.WorldCarver;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBindings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorTypes;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecorationType;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.NbtProviders;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProviders;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class BuiltInRegistries {
/* 175 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/* 176 */   private static final Map<Identifier, Supplier<?>> LOADERS = Maps.newLinkedHashMap();
/*     */   
/* 178 */   private static final WritableRegistry<WritableRegistry<?>> WRITABLE_REGISTRY = (WritableRegistry<WritableRegistry<?>>)new MappedRegistry(ResourceKey.createRegistryKey(Registries.ROOT_REGISTRY_NAME), Lifecycle.stable());
/*     */   
/* 180 */   public static final DataComponentInitializers DATA_COMPONENT_INITIALIZERS = new DataComponentInitializers();
/*     */   
/* 184 */   public static final DefaultedRegistry<GameEvent> GAME_EVENT = registerDefaulted(Registries.GAME_EVENT, "step", GameEvent::bootstrap);
/*     */   
/* 185 */   public static final Registry<SoundEvent> SOUND_EVENT = registerSimple(Registries.SOUND_EVENT, registry -> SoundEvents.ITEM_PICKUP);
/*     */   
/* 186 */   public static final DefaultedRegistry<Fluid> FLUID = registerDefaultedWithIntrusiveHolders(Registries.FLUID, "empty", registry -> Fluids.EMPTY);
/*     */   
/* 187 */   public static final Registry<MobEffect> MOB_EFFECT = registerSimple(Registries.MOB_EFFECT, MobEffects::bootstrap);
/*     */   
/* 188 */   public static final DefaultedRegistry<Block> BLOCK = registerDefaultedWithIntrusiveHolders(Registries.BLOCK, "air", registry -> Blocks.AIR);
/*     */   
/* 189 */   public static final Registry<DebugSubscription<?>> DEBUG_SUBSCRIPTION = registerSimple(Registries.DEBUG_SUBSCRIPTION, DebugSubscriptions::bootstrap);
/*     */   
/* 190 */   public static final DefaultedRegistry<EntityType<?>> ENTITY_TYPE = registerDefaultedWithIntrusiveHolders(Registries.ENTITY_TYPE, "pig", registry -> EntityTypes.PIG);
/*     */   
/* 191 */   public static final DefaultedRegistry<Item> ITEM = registerDefaultedWithIntrusiveHolders(Registries.ITEM, "air", registry -> Items.AIR);
/*     */   
/* 192 */   public static final Registry<Potion> POTION = registerSimple(Registries.POTION, Potions::bootstrap);
/*     */   
/* 193 */   public static final Registry<ParticleType<?>> PARTICLE_TYPE = registerSimple(Registries.PARTICLE_TYPE, registry -> ParticleTypes.BLOCK);
/*     */   
/* 194 */   public static final Registry<BlockEntityType<?>> BLOCK_ENTITY_TYPE = registerSimpleWithIntrusiveHolders(Registries.BLOCK_ENTITY_TYPE, registry -> BlockEntityTypes.FURNACE);
/*     */   
/* 195 */   public static final Registry<Identifier> CUSTOM_STAT = registerSimple(Registries.CUSTOM_STAT, registry -> Stats.JUMP);
/*     */   
/* 196 */   public static final DefaultedRegistry<ChunkStatus> CHUNK_STATUS = registerDefaulted(Registries.CHUNK_STATUS, "empty", registry -> ChunkStatus.EMPTY);
/*     */   
/* 197 */   public static final Registry<RuleTestType<?>> RULE_TEST = registerSimple(Registries.RULE_TEST, registry -> RuleTestType.ALWAYS_TRUE_TEST);
/*     */   
/* 198 */   public static final Registry<RuleBlockEntityModifierType<?>> RULE_BLOCK_ENTITY_MODIFIER = registerSimple(Registries.RULE_BLOCK_ENTITY_MODIFIER, registry -> RuleBlockEntityModifierType.PASSTHROUGH);
/*     */   
/* 199 */   public static final Registry<PosRuleTestType<?>> POS_RULE_TEST = registerSimple(Registries.POS_RULE_TEST, registry -> PosRuleTestType.ALWAYS_TRUE_TEST);
/*     */   
/* 200 */   public static final Registry<MenuType<?>> MENU = registerSimple(Registries.MENU, registry -> MenuType.ANVIL);
/*     */   
/* 201 */   public static final Registry<RecipeType<?>> RECIPE_TYPE = registerSimple(Registries.RECIPE_TYPE, registry -> RecipeType.CRAFTING);
/*     */   
/* 202 */   public static final Registry<RecipeSerializer<?>> RECIPE_SERIALIZER = registerSimple(Registries.RECIPE_SERIALIZER, RecipeSerializers::bootstrap);
/*     */   
/* 203 */   public static final Registry<Attribute> ATTRIBUTE = registerSimple(Registries.ATTRIBUTE, Attributes::bootstrap);
/*     */   
/* 204 */   public static final Registry<PositionSourceType<?>> POSITION_SOURCE_TYPE = registerSimple(Registries.POSITION_SOURCE_TYPE, registry -> PositionSourceType.BLOCK);
/*     */   
/* 205 */   public static final Registry<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPE = registerSimple(Registries.COMMAND_ARGUMENT_TYPE, ArgumentTypeInfos::bootstrap);
/*     */   
/* 206 */   public static final Registry<StatType<?>> STAT_TYPE = registerSimple(Registries.STAT_TYPE, registry -> Stats.ITEM_USED);
/*     */   
/* 207 */   public static final DefaultedRegistry<VillagerType> VILLAGER_TYPE = registerDefaulted(Registries.VILLAGER_TYPE, "plains", VillagerType::bootstrap);
/*     */   
/* 208 */   public static final DefaultedRegistry<VillagerProfession> VILLAGER_PROFESSION = registerDefaulted(Registries.VILLAGER_PROFESSION, "none", VillagerProfession::bootstrap);
/*     */   
/* 209 */   public static final Registry<PoiType> POINT_OF_INTEREST_TYPE = registerSimple(Registries.POINT_OF_INTEREST_TYPE, PoiTypes::bootstrap);
/*     */   
/* 210 */   public static final DefaultedRegistry<MemoryModuleType<?>> MEMORY_MODULE_TYPE = registerDefaulted(Registries.MEMORY_MODULE_TYPE, "dummy", registry -> MemoryModuleType.DUMMY);
/*     */   
/* 211 */   public static final DefaultedRegistry<SensorType<?>> SENSOR_TYPE = registerDefaulted(Registries.SENSOR_TYPE, "dummy", registry -> SensorType.DUMMY);
/*     */   
/* 212 */   public static final Registry<Activity> ACTIVITY = registerSimple(Registries.ACTIVITY, registry -> Activity.IDLE);
/*     */   
/* 213 */   public static final Registry<MapCodec<? extends LootPoolEntryContainer>> LOOT_POOL_ENTRY_TYPE = registerSimple(Registries.LOOT_POOL_ENTRY_TYPE, LootPoolEntries::bootstrap);
/*     */   
/* 214 */   public static final Registry<MapCodec<? extends LootItemFunction>> LOOT_FUNCTION_TYPE = registerSimple(Registries.LOOT_FUNCTION_TYPE, LootItemFunctions::bootstrap);
/*     */   
/* 215 */   public static final Registry<MapCodec<? extends LootItemCondition>> LOOT_CONDITION_TYPE = registerSimple(Registries.LOOT_CONDITION_TYPE, LootItemConditions::bootstrap);
/*     */   
/* 216 */   public static final Registry<MapCodec<? extends NumberProvider>> LOOT_NUMBER_PROVIDER_TYPE = registerSimple(Registries.LOOT_NUMBER_PROVIDER_TYPE, NumberProviders::bootstrap);
/*     */   
/* 217 */   public static final Registry<MapCodec<? extends NbtProvider>> LOOT_NBT_PROVIDER_TYPE = registerSimple(Registries.LOOT_NBT_PROVIDER_TYPE, NbtProviders::bootstrap);
/*     */   
/* 218 */   public static final Registry<MapCodec<? extends ScoreboardNameProvider>> LOOT_SCORE_PROVIDER_TYPE = registerSimple(Registries.LOOT_SCORE_PROVIDER_TYPE, ScoreboardNameProviders::bootstrap);
/*     */   
/* 219 */   public static final Registry<MapCodec<? extends FloatProvider>> FLOAT_PROVIDER_TYPE = registerSimple(Registries.FLOAT_PROVIDER_TYPE, FloatProviders::bootstrap);
/*     */   
/* 220 */   public static final Registry<MapCodec<? extends IntProvider>> INT_PROVIDER_TYPE = registerSimple(Registries.INT_PROVIDER_TYPE, IntProviders::bootstrap);
/*     */   
/* 221 */   public static final Registry<HeightProviderType<?>> HEIGHT_PROVIDER_TYPE = registerSimple(Registries.HEIGHT_PROVIDER_TYPE, registry -> HeightProviderType.CONSTANT);
/*     */   
/* 222 */   public static final Registry<BlockPredicateType<?>> BLOCK_PREDICATE_TYPE = registerSimple(Registries.BLOCK_PREDICATE_TYPE, registry -> BlockPredicateType.NOT);
/*     */   
/* 223 */   public static final Registry<WorldCarver<?>> CARVER = registerSimple(Registries.CARVER, registry -> WorldCarver.CAVE);
/*     */   
/* 224 */   public static final Registry<Feature<?>> FEATURE = registerSimple(Registries.FEATURE, registry -> Feature.ORE);
/*     */   
/* 225 */   public static final Registry<StructurePlacementType<?>> STRUCTURE_PLACEMENT = registerSimple(Registries.STRUCTURE_PLACEMENT, registry -> StructurePlacementType.RANDOM_SPREAD);
/*     */   
/* 226 */   public static final Registry<StructurePieceType> STRUCTURE_PIECE = registerSimple(Registries.STRUCTURE_PIECE, registry -> StructurePieceType.MINE_SHAFT_ROOM);
/*     */   
/* 227 */   public static final Registry<StructureType<?>> STRUCTURE_TYPE = registerSimple(Registries.STRUCTURE_TYPE, registry -> StructureType.JIGSAW);
/*     */   
/* 228 */   public static final Registry<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = registerSimple(Registries.PLACEMENT_MODIFIER_TYPE, registry -> PlacementModifierType.COUNT);
/*     */   
/* 229 */   public static final Registry<BlockStateProviderType<?>> BLOCKSTATE_PROVIDER_TYPE = registerSimple(Registries.BLOCK_STATE_PROVIDER_TYPE, registry -> BlockStateProviderType.SIMPLE_STATE_PROVIDER);
/*     */   
/* 230 */   public static final Registry<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = registerSimple(Registries.FOLIAGE_PLACER_TYPE, registry -> FoliagePlacerType.BLOB_FOLIAGE_PLACER);
/*     */   
/* 231 */   public static final Registry<TrunkPlacerType<?>> TRUNK_PLACER_TYPE = registerSimple(Registries.TRUNK_PLACER_TYPE, registry -> TrunkPlacerType.STRAIGHT_TRUNK_PLACER);
/*     */   
/* 232 */   public static final Registry<RootPlacerType<?>> ROOT_PLACER_TYPE = registerSimple(Registries.ROOT_PLACER_TYPE, registry -> RootPlacerType.MANGROVE_ROOT_PLACER);
/*     */   
/* 233 */   public static final Registry<TreeDecoratorType<?>> TREE_DECORATOR_TYPE = registerSimple(Registries.TREE_DECORATOR_TYPE, registry -> TreeDecoratorType.LEAVE_VINE);
/*     */   
/* 234 */   public static final Registry<FeatureSizeType<?>> FEATURE_SIZE_TYPE = registerSimple(Registries.FEATURE_SIZE_TYPE, registry -> FeatureSizeType.TWO_LAYERS_FEATURE_SIZE);
/*     */   
/* 235 */   public static final Registry<MapCodec<? extends BiomeSource>> BIOME_SOURCE = registerSimple(Registries.BIOME_SOURCE, BiomeSources::bootstrap);
/*     */   
/* 236 */   public static final Registry<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATOR = registerSimple(Registries.CHUNK_GENERATOR, ChunkGenerators::bootstrap);
/*     */   
/* 237 */   public static final Registry<MapCodec<? extends SurfaceRules.ConditionSource>> MATERIAL_CONDITION = registerSimple(Registries.MATERIAL_CONDITION, SurfaceRules.ConditionSource::bootstrap);
/*     */   
/* 238 */   public static final Registry<MapCodec<? extends SurfaceRules.RuleSource>> MATERIAL_RULE = registerSimple(Registries.MATERIAL_RULE, SurfaceRules.RuleSource::bootstrap);
/*     */   
/* 239 */   public static final Registry<MapCodec<? extends DensityFunction>> DENSITY_FUNCTION_TYPE = registerSimple(Registries.DENSITY_FUNCTION_TYPE, DensityFunctions::bootstrap);
/*     */   
/* 240 */   public static final Registry<MapCodec<? extends Block>> BLOCK_TYPE = registerSimple(Registries.BLOCK_TYPE, BlockTypes::bootstrap);
/*     */   
/* 241 */   public static final Registry<MapCodec<? extends StructureProcessor>> STRUCTURE_PROCESSOR = registerSimple(Registries.STRUCTURE_PROCESSOR, StructureProcessorTypes::bootstrap);
/*     */   
/* 242 */   public static final Registry<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT = registerSimple(Registries.STRUCTURE_POOL_ELEMENT, registry -> StructurePoolElementType.EMPTY);
/*     */   
/* 243 */   public static final Registry<MapCodec<? extends PoolAliasBinding>> POOL_ALIAS_BINDING_TYPE = registerSimple(Registries.POOL_ALIAS_BINDING, PoolAliasBindings::bootstrap);
/*     */   
/* 244 */   public static final Registry<DecoratedPotPattern> DECORATED_POT_PATTERN = registerSimple(Registries.DECORATED_POT_PATTERN, DecoratedPotPatterns::bootstrap);
/*     */   
/* 245 */   public static final Registry<CreativeModeTab> CREATIVE_MODE_TAB = registerSimple(Registries.CREATIVE_MODE_TAB, CreativeModeTabs::bootstrap);
/*     */   
/* 246 */   public static final Registry<CriterionTrigger<?>> TRIGGER_TYPES = registerSimple(Registries.TRIGGER_TYPE, CriteriaTriggers::bootstrap);
/*     */   
/* 247 */   public static final Registry<NumberFormatType<?>> NUMBER_FORMAT_TYPE = registerSimple(Registries.NUMBER_FORMAT_TYPE, NumberFormatTypes::bootstrap);
/*     */   
/* 248 */   public static final Registry<DataComponentType<?>> DATA_COMPONENT_TYPE = registerSimple(Registries.DATA_COMPONENT_TYPE, DataComponents::bootstrap);
/*     */   
/* 249 */   public static final Registry<GameRule<?>> GAME_RULE = registerSimple(Registries.GAME_RULE, GameRules::bootstrap);
/*     */   
/* 250 */   public static final Registry<Codec<? extends EntitySubPredicate>> ENTITY_SUB_PREDICATE_TYPE = registerSimple(Registries.ENTITY_SUB_PREDICATE_TYPE, EntitySubPredicates::bootstrap);
/*     */   
/* 251 */   public static final Registry<DataComponentPredicate.Type<?>> DATA_COMPONENT_PREDICATE_TYPE = registerSimple(Registries.DATA_COMPONENT_PREDICATE_TYPE, DataComponentPredicates::bootstrap);
/*     */   
/* 252 */   public static final Registry<MapDecorationType> MAP_DECORATION_TYPE = registerSimple(Registries.MAP_DECORATION_TYPE, MapDecorationTypes::bootstrap);
/*     */   
/* 253 */   public static final Registry<DataComponentType<?>> ENCHANTMENT_EFFECT_COMPONENT_TYPE = registerSimple(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, EnchantmentEffectComponents::bootstrap);
/*     */   
/* 254 */   public static final Registry<MapCodec<? extends LevelBasedValue>> ENCHANTMENT_LEVEL_BASED_VALUE_TYPE = registerSimple(Registries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE, LevelBasedValue::bootstrap);
/*     */   
/* 255 */   public static final Registry<MapCodec<? extends EnchantmentEntityEffect>> ENCHANTMENT_ENTITY_EFFECT_TYPE = registerSimple(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, EnchantmentEntityEffect::bootstrap);
/*     */   
/* 256 */   public static final Registry<MapCodec<? extends EnchantmentLocationBasedEffect>> ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE = registerSimple(Registries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE, EnchantmentLocationBasedEffect::bootstrap);
/*     */   
/* 257 */   public static final Registry<MapCodec<? extends EnchantmentValueEffect>> ENCHANTMENT_VALUE_EFFECT_TYPE = registerSimple(Registries.ENCHANTMENT_VALUE_EFFECT_TYPE, EnchantmentValueEffect::bootstrap);
/*     */   
/* 258 */   public static final Registry<MapCodec<? extends EnchantmentProvider>> ENCHANTMENT_PROVIDER_TYPE = registerSimple(Registries.ENCHANTMENT_PROVIDER_TYPE, EnchantmentProviderTypes::bootstrap);
/*     */   
/* 259 */   public static final Registry<ConsumeEffect.Type<?>> CONSUME_EFFECT_TYPE = registerSimple(Registries.CONSUME_EFFECT_TYPE, registry -> ConsumeEffect.Type.APPLY_EFFECTS);
/*     */   
/* 260 */   public static final Registry<RecipeDisplay.Type<?>> RECIPE_DISPLAY = registerSimple(Registries.RECIPE_DISPLAY, RecipeDisplays::bootstrap);
/*     */   
/* 261 */   public static final Registry<SlotDisplay.Type<?>> SLOT_DISPLAY = registerSimple(Registries.SLOT_DISPLAY, SlotDisplays::bootstrap);
/*     */   
/* 262 */   public static final Registry<RecipeBookCategory> RECIPE_BOOK_CATEGORY = registerSimple(Registries.RECIPE_BOOK_CATEGORY, RecipeBookCategories::bootstrap);
/*     */   
/* 263 */   public static final Registry<TicketType> TICKET_TYPE = registerSimple(Registries.TICKET_TYPE, registry -> TicketType.UNKNOWN);
/*     */   
/* 264 */   public static final Registry<IncomingRpcMethod<?, ?>> INCOMING_RPC_METHOD = registerSimple(Registries.INCOMING_RPC_METHOD, IncomingRpcMethods::bootstrap);
/*     */   
/* 265 */   public static final Registry<OutgoingRpcMethod<?, ?>> OUTGOING_RPC_METHOD = registerSimple(Registries.OUTGOING_RPC_METHOD, registry -> OutgoingRpcMethods.SERVER_STARTED);
/*     */   
/* 266 */   public static final Registry<MapCodec<? extends TestEnvironmentDefinition<?>>> TEST_ENVIRONMENT_DEFINITION_TYPE = registerSimple(Registries.TEST_ENVIRONMENT_DEFINITION_TYPE, TestEnvironmentDefinition::bootstrap);
/*     */   
/* 267 */   public static final Registry<MapCodec<? extends GameTestInstance>> TEST_INSTANCE_TYPE = registerSimple(Registries.TEST_INSTANCE_TYPE, GameTestInstance::bootstrap);
/*     */   
/* 268 */   public static final Registry<MapCodec<? extends SpawnCondition>> SPAWN_CONDITION_TYPE = registerSimple(Registries.SPAWN_CONDITION_TYPE, SpawnConditions::bootstrap);
/*     */   
/* 269 */   public static final Registry<MapCodec<? extends Dialog>> DIALOG_TYPE = registerSimple(Registries.DIALOG_TYPE, DialogTypes::bootstrap);
/*     */   
/* 270 */   public static final Registry<MapCodec<? extends Action>> DIALOG_ACTION_TYPE = registerSimple(Registries.DIALOG_ACTION_TYPE, ActionTypes::bootstrap);
/*     */   
/* 271 */   public static final Registry<MapCodec<? extends InputControl>> INPUT_CONTROL_TYPE = registerSimple(Registries.INPUT_CONTROL_TYPE, InputControlTypes::bootstrap);
/*     */   
/* 272 */   public static final Registry<MapCodec<? extends DialogBody>> DIALOG_BODY_TYPE = registerSimple(Registries.DIALOG_BODY_TYPE, DialogBodyTypes::bootstrap);
/*     */   
/* 273 */   public static final Registry<MapCodec<? extends Permission>> PERMISSION_TYPE = registerSimple(Registries.PERMISSION_TYPE, PermissionTypes::bootstrap);
/*     */   
/* 274 */   public static final Registry<MapCodec<? extends PermissionCheck>> PERMISSION_CHECK_TYPE = registerSimple(Registries.PERMISSION_CHECK_TYPE, PermissionCheckTypes::bootstrap);
/*     */   
/* 275 */   public static final Registry<EnvironmentAttribute<?>> ENVIRONMENT_ATTRIBUTE = registerSimple(Registries.ENVIRONMENT_ATTRIBUTE, EnvironmentAttributes::bootstrap);
/*     */   
/* 276 */   public static final Registry<AttributeType<?>> ATTRIBUTE_TYPE = registerSimple(Registries.ATTRIBUTE_TYPE, AttributeTypes::bootstrap);
/*     */   
/* 277 */   public static final Registry<MapCodec<? extends SlotSource>> SLOT_SOURCE_TYPE = registerSimple(Registries.SLOT_SOURCE_TYPE, SlotSources::bootstrap);
/*     */   
/* 280 */   public static final Registry<Consumer<GameTestHelper>> TEST_FUNCTION = registerSimple(Registries.TEST_FUNCTION, BuiltinTestFunctions::bootstrap);
/*     */   
/* 282 */   public static final Registry<? extends Registry<?>> REGISTRY = (Registry)WRITABLE_REGISTRY;
/*     */   
/*     */   private static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> name, RegistryBootstrap<T> loader) {
/* 285 */     return (Registry<T>)internalRegister(name, new MappedRegistry(name, Lifecycle.stable(), false), loader);
/*     */   }
/*     */   
/*     */   private static <T> Registry<T> registerSimpleWithIntrusiveHolders(ResourceKey<? extends Registry<T>> name, RegistryBootstrap<T> loader) {
/* 289 */     return (Registry<T>)internalRegister(name, new MappedRegistry(name, Lifecycle.stable(), true), loader);
/*     */   }
/*     */   
/*     */   private static <T> DefaultedRegistry<T> registerDefaulted(ResourceKey<? extends Registry<T>> name, String defaultKey, RegistryBootstrap<T> loader) {
/* 293 */     return (DefaultedRegistry<T>)internalRegister(name, new DefaultedMappedRegistry(defaultKey, name, Lifecycle.stable(), false), loader);
/*     */   }
/*     */   
/*     */   private static <T> DefaultedRegistry<T> registerDefaultedWithIntrusiveHolders(ResourceKey<? extends Registry<T>> name, String defaultKey, RegistryBootstrap<T> loader) {
/* 297 */     return (DefaultedRegistry<T>)internalRegister(name, new DefaultedMappedRegistry(defaultKey, name, Lifecycle.stable(), true), loader);
/*     */   }
/*     */   
/*     */   private static <T, R extends WritableRegistry<T>> R internalRegister(ResourceKey<? extends Registry<T>> name, R registry, RegistryBootstrap<T> loader) {
/* 302 */     Bootstrap.checkBootstrapCalled(() -> "registry " + String.valueOf(name.identifier()));
/* 303 */     Identifier key = name.identifier();
/* 304 */     LOADERS.put(key, () -> loader.run((Registry)registry));
/* 306 */     WRITABLE_REGISTRY.register(name, registry, RegistrationInfo.BUILT_IN);
/* 307 */     return registry;
/*     */   }
/*     */   
/*     */   public static void bootStrap() {
/* 311 */     createContents();
/* 312 */     freeze();
/* 313 */     validate(REGISTRY);
/*     */   }
/*     */   
/*     */   private static void createContents() {
/* 317 */     LOADERS.forEach((key, value) -> {
/*     */           if (value.get() == null)
/*     */             LOGGER.error("Unable to bootstrap registry '{}'", key); 
/*     */         });
/*     */   }
/*     */   
/*     */   private static void freeze() {
/* 326 */     REGISTRY.freeze();
/* 327 */     for (Registry<?> registry : REGISTRY) {
/* 328 */       bindBootstrappedTagsToEmpty(registry);
/* 329 */       registry.freeze();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T extends Registry<?>> void validate(Registry<T> registry) {
/* 334 */     registry.forEach(r -> {
/*     */           if (r.keySet().isEmpty())
/*     */             Util.logAndPauseIfInIde("Registry '" + String.valueOf(registry.getKey(r)) + "' was empty after loading"); 
/*     */           if (r instanceof DefaultedRegistry) {
/*     */             Identifier key = ((DefaultedRegistry)r).getDefaultKey();
/*     */             Objects.requireNonNull(r.getValue(key), "Missing default of DefaultedMappedRegistry: " + String.valueOf(key));
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public static <T> HolderGetter<T> acquireBootstrapRegistrationLookup(Registry<T> registry) {
/* 352 */     return ((WritableRegistry)registry).createRegistrationLookup();
/*     */   }
/*     */   
/*     */   private static void bindBootstrappedTagsToEmpty(Registry<?> registry) {
/* 356 */     ((MappedRegistry)registry).bindAllTagsToEmpty();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface RegistryBootstrap<T> {
/*     */     Object run(Registry<T> param1Registry);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\registries\BuiltInRegistries.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */