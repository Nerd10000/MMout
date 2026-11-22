/*      */ package net.minecraft.client.data.models;
/*      */ 
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.math.Quadrant;
/*      */ import com.mojang.math.Transformation;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.IntStream;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.client.color.item.GrassColorSource;
/*      */ import net.minecraft.client.color.item.ItemTintSource;
/*      */ import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
/*      */ import net.minecraft.client.data.models.blockstates.ConditionBuilder;
/*      */ import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
/*      */ import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
/*      */ import net.minecraft.client.data.models.blockstates.PropertyDispatch;
/*      */ import net.minecraft.client.data.models.model.ItemModelUtils;
/*      */ import net.minecraft.client.data.models.model.ModelInstance;
/*      */ import net.minecraft.client.data.models.model.ModelLocationUtils;
/*      */ import net.minecraft.client.data.models.model.ModelTemplate;
/*      */ import net.minecraft.client.data.models.model.ModelTemplates;
/*      */ import net.minecraft.client.data.models.model.TextureMapping;
/*      */ import net.minecraft.client.data.models.model.TextureSlot;
/*      */ import net.minecraft.client.data.models.model.TexturedModel;
/*      */ import net.minecraft.client.renderer.MultiblockChestResources;
/*      */ import net.minecraft.client.renderer.block.dispatch.Variant;
/*      */ import net.minecraft.client.renderer.block.dispatch.VariantMutator;
/*      */ import net.minecraft.client.renderer.block.dispatch.multipart.CombinedCondition;
/*      */ import net.minecraft.client.renderer.block.dispatch.multipart.Condition;
/*      */ import net.minecraft.client.renderer.blockentity.BannerRenderer;
/*      */ import net.minecraft.client.renderer.blockentity.ConduitRenderer;
/*      */ import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
/*      */ import net.minecraft.client.renderer.item.ItemModel;
/*      */ import net.minecraft.client.renderer.special.BannerSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.ChestSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.ConduitSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.CopperGolemStatueSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.DecoratedPotSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.PlayerHeadSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.ShulkerBoxSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.SkullSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*      */ import net.minecraft.client.resources.model.sprite.Material;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.FrontAndTop;
/*      */ import net.minecraft.data.BlockFamilies;
/*      */ import net.minecraft.data.BlockFamily;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.util.random.Weighted;
/*      */ import net.minecraft.util.random.WeightedList;
/*      */ import net.minecraft.world.item.DyeColor;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.level.block.BannerBlock;
/*      */ import net.minecraft.world.level.block.BeehiveBlock;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.ChiseledBookShelfBlock;
/*      */ import net.minecraft.world.level.block.ColorCollection;
/*      */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*      */ import net.minecraft.world.level.block.CrafterBlock;
/*      */ import net.minecraft.world.level.block.CreakingHeartBlock;
/*      */ import net.minecraft.world.level.block.DriedGhastBlock;
/*      */ import net.minecraft.world.level.block.HangingMossBlock;
/*      */ import net.minecraft.world.level.block.LayeredCauldronBlock;
/*      */ import net.minecraft.world.level.block.LightBlock;
/*      */ import net.minecraft.world.level.block.MangrovePropaguleBlock;
/*      */ import net.minecraft.world.level.block.MossyCarpetBlock;
/*      */ import net.minecraft.world.level.block.MultifaceBlock;
/*      */ import net.minecraft.world.level.block.PitcherCropBlock;
/*      */ import net.minecraft.world.level.block.SkullBlock;
/*      */ import net.minecraft.world.level.block.SnifferEggBlock;
/*      */ import net.minecraft.world.level.block.TestBlock;
/*      */ import net.minecraft.world.level.block.VaultBlock;
/*      */ import net.minecraft.world.level.block.WeatheringCopper;
/*      */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
/*      */ import net.minecraft.world.level.block.entity.vault.VaultState;
/*      */ import net.minecraft.world.level.block.state.StateHolder;
/*      */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*      */ import net.minecraft.world.level.block.state.properties.BambooLeaves;
/*      */ import net.minecraft.world.level.block.state.properties.BedPart;
/*      */ import net.minecraft.world.level.block.state.properties.BellAttachType;
/*      */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*      */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*      */ import net.minecraft.world.level.block.state.properties.ComparatorMode;
/*      */ import net.minecraft.world.level.block.state.properties.CreakingHeartState;
/*      */ import net.minecraft.world.level.block.state.properties.DoorHingeSide;
/*      */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*      */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*      */ import net.minecraft.world.level.block.state.properties.Half;
/*      */ import net.minecraft.world.level.block.state.properties.PistonType;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.block.state.properties.RailShape;
/*      */ import net.minecraft.world.level.block.state.properties.RedstoneSide;
/*      */ import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
/*      */ import net.minecraft.world.level.block.state.properties.SideChainPart;
/*      */ import net.minecraft.world.level.block.state.properties.SlabType;
/*      */ import net.minecraft.world.level.block.state.properties.SpeleothemThickness;
/*      */ import net.minecraft.world.level.block.state.properties.StairsShape;
/*      */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*      */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*      */ import net.minecraft.world.level.block.state.properties.Tilt;
/*      */ import net.minecraft.world.level.block.state.properties.WallSide;
/*      */ import org.joml.Quaternionf;
/*      */ import org.joml.Quaternionfc;
/*      */ import org.joml.Vector3f;
/*      */ import org.joml.Vector3fc;
/*      */ 
/*      */ public class BlockModelGenerators {
/*      */   private final Consumer<BlockModelDefinitionGenerator> blockStateOutput;
/*      */   
/*      */   private final ItemModelOutput itemModelOutput;
/*      */   
/*      */   private final BiConsumer<Identifier, ModelInstance> modelOutput;
/*      */   
/*  140 */   private static final List<Block> NON_ORIENTABLE_TRAPDOOR = List.of(Blocks.OAK_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.IRON_TRAPDOOR);
/*      */   
/*      */   public static final VariantMutator NOP;
/*      */   
/*      */   static {
/*  142 */     NOP = (v -> v);
/*      */   }
/*      */   
/*  144 */   public static final VariantMutator UV_LOCK = VariantMutator.UV_LOCK.withValue(Boolean.valueOf(true));
/*      */   
/*  146 */   public static final VariantMutator X_ROT_90 = VariantMutator.X_ROT.withValue(Quadrant.R90);
/*      */   
/*  147 */   public static final VariantMutator X_ROT_180 = VariantMutator.X_ROT.withValue(Quadrant.R180);
/*      */   
/*  148 */   public static final VariantMutator X_ROT_270 = VariantMutator.X_ROT.withValue(Quadrant.R270);
/*      */   
/*  150 */   public static final VariantMutator Y_ROT_90 = VariantMutator.Y_ROT.withValue(Quadrant.R90);
/*      */   
/*  151 */   public static final VariantMutator Y_ROT_180 = VariantMutator.Y_ROT.withValue(Quadrant.R180);
/*      */   
/*  152 */   public static final VariantMutator Y_ROT_270 = VariantMutator.Y_ROT.withValue(Quadrant.R270);
/*      */   
/*      */   private static final Function<ConditionBuilder, ConditionBuilder> FLOWER_BED_MODEL_1_SEGMENT_CONDITION;
/*      */   
/*      */   private static final Function<ConditionBuilder, ConditionBuilder> FLOWER_BED_MODEL_2_SEGMENT_CONDITION;
/*      */   
/*      */   private static final Function<ConditionBuilder, ConditionBuilder> FLOWER_BED_MODEL_3_SEGMENT_CONDITION;
/*      */   
/*      */   private static final Function<ConditionBuilder, ConditionBuilder> FLOWER_BED_MODEL_4_SEGMENT_CONDITION;
/*      */   
/*      */   private static final Function<ConditionBuilder, ConditionBuilder> LEAF_LITTER_MODEL_1_SEGMENT_CONDITION;
/*      */   
/*      */   private static final Function<ConditionBuilder, ConditionBuilder> LEAF_LITTER_MODEL_2_SEGMENT_CONDITION;
/*      */   
/*      */   private static final Function<ConditionBuilder, ConditionBuilder> LEAF_LITTER_MODEL_3_SEGMENT_CONDITION;
/*      */   
/*      */   private static final Function<ConditionBuilder, ConditionBuilder> LEAF_LITTER_MODEL_4_SEGMENT_CONDITION;
/*      */   
/*      */   static {
/*  154 */     FLOWER_BED_MODEL_1_SEGMENT_CONDITION = (condition -> condition);
/*  155 */     FLOWER_BED_MODEL_2_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.FLOWER_AMOUNT, Integer.valueOf(2), (Comparable[])new Integer[] { Integer.valueOf(3), Integer.valueOf(4) }));
/*  156 */     FLOWER_BED_MODEL_3_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.FLOWER_AMOUNT, Integer.valueOf(3), (Comparable[])new Integer[] { Integer.valueOf(4) }));
/*  157 */     FLOWER_BED_MODEL_4_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.FLOWER_AMOUNT, Integer.valueOf(4)));
/*  159 */     LEAF_LITTER_MODEL_1_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.SEGMENT_AMOUNT, Integer.valueOf(1)));
/*  160 */     LEAF_LITTER_MODEL_2_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.SEGMENT_AMOUNT, Integer.valueOf(2), (Comparable[])new Integer[] { Integer.valueOf(3) }));
/*  161 */     LEAF_LITTER_MODEL_3_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.SEGMENT_AMOUNT, Integer.valueOf(3)));
/*  162 */     LEAF_LITTER_MODEL_4_SEGMENT_CONDITION = (condition -> condition.term((Property)BlockStateProperties.SEGMENT_AMOUNT, Integer.valueOf(4)));
/*      */   }
/*      */   
/*  164 */   private static final Transformation SKULL_TRANSFORM = new Transformation((Vector3fc)new Vector3f(0.5F, 0.0F, 0.5F), (Quaternionfc)(new Quaternionf())
/*      */       
/*  166 */       .rotationX(3.1415927F), null, null);
/*      */   
/*  176 */   private static final Map<Block, BlockStateGeneratorSupplier> FULL_BLOCK_MODEL_CUSTOM_GENERATORS = Map.of(Blocks.STONE, BlockModelGenerators::createMirroredCubeGenerator, Blocks.DEEPSLATE, BlockModelGenerators::createMirroredColumnGenerator, Blocks.MUD_BRICKS, BlockModelGenerators::createNorthWestMirroredCubeGenerator);
/*      */   
/*  182 */   private static final PropertyDispatch<VariantMutator> ROTATION_FACING = (PropertyDispatch<VariantMutator>)PropertyDispatch.modify((Property)BlockStateProperties.FACING)
/*  183 */     .select((Comparable)Direction.DOWN, X_ROT_90)
/*  184 */     .select((Comparable)Direction.UP, X_ROT_270)
/*  185 */     .select((Comparable)Direction.NORTH, NOP)
/*  186 */     .select((Comparable)Direction.SOUTH, Y_ROT_180)
/*  187 */     .select((Comparable)Direction.WEST, Y_ROT_270)
/*  188 */     .select((Comparable)Direction.EAST, Y_ROT_90);
/*      */   
/*  190 */   private static final PropertyDispatch<VariantMutator> ROTATIONS_COLUMN_WITH_FACING = (PropertyDispatch<VariantMutator>)PropertyDispatch.modify((Property)BlockStateProperties.FACING)
/*  191 */     .select((Comparable)Direction.DOWN, X_ROT_180)
/*  192 */     .select((Comparable)Direction.UP, NOP)
/*  193 */     .select((Comparable)Direction.NORTH, X_ROT_90)
/*  194 */     .select((Comparable)Direction.SOUTH, X_ROT_90.then(Y_ROT_180))
/*  195 */     .select((Comparable)Direction.WEST, X_ROT_90.then(Y_ROT_270))
/*  196 */     .select((Comparable)Direction.EAST, X_ROT_90.then(Y_ROT_90));
/*      */   
/*  198 */   private static final PropertyDispatch<VariantMutator> ROTATION_TORCH = (PropertyDispatch<VariantMutator>)PropertyDispatch.modify((Property)BlockStateProperties.HORIZONTAL_FACING)
/*  199 */     .select((Comparable)Direction.EAST, NOP)
/*  200 */     .select((Comparable)Direction.SOUTH, Y_ROT_90)
/*  201 */     .select((Comparable)Direction.WEST, Y_ROT_180)
/*  202 */     .select((Comparable)Direction.NORTH, Y_ROT_270);
/*      */   
/*  204 */   private static final PropertyDispatch<VariantMutator> ROTATION_HORIZONTAL_FACING_ALT = (PropertyDispatch<VariantMutator>)PropertyDispatch.modify((Property)BlockStateProperties.HORIZONTAL_FACING)
/*  205 */     .select((Comparable)Direction.SOUTH, NOP)
/*  206 */     .select((Comparable)Direction.WEST, Y_ROT_90)
/*  207 */     .select((Comparable)Direction.NORTH, Y_ROT_180)
/*  208 */     .select((Comparable)Direction.EAST, Y_ROT_270);
/*      */   
/*  210 */   private static final PropertyDispatch<VariantMutator> ROTATION_HORIZONTAL_FACING = (PropertyDispatch<VariantMutator>)PropertyDispatch.modify((Property)BlockStateProperties.HORIZONTAL_FACING)
/*  211 */     .select((Comparable)Direction.EAST, Y_ROT_90)
/*  212 */     .select((Comparable)Direction.SOUTH, Y_ROT_180)
/*  213 */     .select((Comparable)Direction.WEST, Y_ROT_270)
/*  214 */     .select((Comparable)Direction.NORTH, NOP);
/*      */   
/*      */   private static final Map<Block, TexturedModel> TEXTURED_MODELS;
/*      */   
/*      */   private static Variant plainModel(Identifier model) {
/*  217 */     return new Variant(model);
/*      */   }
/*      */   
/*      */   private static MultiVariant variant(Variant variant) {
/*  221 */     return new MultiVariant(WeightedList.of(variant));
/*      */   }
/*      */   
/*      */   private static MultiVariant variants(Variant... variant) {
/*  225 */     return new MultiVariant(WeightedList.of(Arrays.<Variant>stream(variant).map(v -> new Weighted(v, 1)).toList()));
/*      */   }
/*      */   
/*      */   private static MultiVariant plainVariant(Identifier model) {
/*  229 */     return variant(plainModel(model));
/*      */   }
/*      */   
/*      */   private static ConditionBuilder condition() {
/*  233 */     return new ConditionBuilder();
/*      */   }
/*      */   
/*      */   @SafeVarargs
/*      */   private static <T extends Enum<T> & net.minecraft.util.StringRepresentable> ConditionBuilder condition(EnumProperty<T> property, T term, T... additionalTerms) {
/*  238 */     return condition().term((Property)property, (Comparable)term, (Comparable[])additionalTerms);
/*      */   }
/*      */   
/*      */   private static ConditionBuilder condition(BooleanProperty property, boolean term) {
/*  242 */     return condition().term((Property)property, Boolean.valueOf(term));
/*      */   }
/*      */   
/*      */   private static Condition or(ConditionBuilder... terms) {
/*  246 */     return (Condition)new CombinedCondition(CombinedCondition.Operation.OR, Stream.<ConditionBuilder>of(terms).map(ConditionBuilder::build).toList());
/*      */   }
/*      */   
/*      */   private static Condition and(ConditionBuilder... terms) {
/*  250 */     return (Condition)new CombinedCondition(CombinedCondition.Operation.AND, Stream.<ConditionBuilder>of(terms).map(ConditionBuilder::build).toList());
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createMirroredCubeGenerator(Block block, Variant normal, TextureMapping mapping, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  254 */     Variant mirrored = plainModel(ModelTemplates.CUBE_MIRRORED_ALL.create(block, mapping, modelOutput));
/*  255 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block, createRotatedVariants(normal, mirrored));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createNorthWestMirroredCubeGenerator(Block block, Variant normal, TextureMapping mapping, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  259 */     MultiVariant northWestMirrored = plainVariant(ModelTemplates.CUBE_NORTH_WEST_MIRRORED_ALL.create(block, mapping, modelOutput));
/*  260 */     return (BlockModelDefinitionGenerator)createSimpleBlock(block, northWestMirrored);
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createMirroredColumnGenerator(Block block, Variant normal, TextureMapping mapping, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  264 */     Variant mirrored = plainModel(ModelTemplates.CUBE_COLUMN_MIRRORED.create(block, mapping, modelOutput));
/*  265 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block, createRotatedVariants(normal, mirrored))
/*  266 */       .with(createRotatedPillar());
/*      */   }
/*      */   
/*      */   static {
/*  291 */     TEXTURED_MODELS = (Map<Block, TexturedModel>)ImmutableMap.builder().put(Blocks.SANDSTONE, TexturedModel.TOP_BOTTOM_WITH_WALL.get(Blocks.SANDSTONE)).put(Blocks.RED_SANDSTONE, TexturedModel.TOP_BOTTOM_WITH_WALL.get(Blocks.RED_SANDSTONE)).put(Blocks.SMOOTH_SANDSTONE, TexturedModel.createAllSame(TextureMapping.getBlockTexture(Blocks.SANDSTONE, "_top"))).put(Blocks.SMOOTH_RED_SANDSTONE, TexturedModel.createAllSame(TextureMapping.getBlockTexture(Blocks.RED_SANDSTONE, "_top"))).put(Blocks.CUT_SANDSTONE, TexturedModel.COLUMN.get(Blocks.SANDSTONE).updateTextures(m -> m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CUT_SANDSTONE)))).put(Blocks.CUT_RED_SANDSTONE, TexturedModel.COLUMN.get(Blocks.RED_SANDSTONE).updateTextures(m -> m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CUT_RED_SANDSTONE)))).put(Blocks.QUARTZ_BLOCK, TexturedModel.COLUMN.get(Blocks.QUARTZ_BLOCK)).put(Blocks.SMOOTH_QUARTZ, TexturedModel.createAllSame(TextureMapping.getBlockTexture(Blocks.QUARTZ_BLOCK, "_bottom"))).put(Blocks.BLACKSTONE, TexturedModel.COLUMN_WITH_WALL.get(Blocks.BLACKSTONE)).put(Blocks.DEEPSLATE, TexturedModel.COLUMN_WITH_WALL.get(Blocks.DEEPSLATE)).put(Blocks.CHISELED_QUARTZ_BLOCK, TexturedModel.COLUMN.get(Blocks.CHISELED_QUARTZ_BLOCK).updateTextures(m -> m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CHISELED_QUARTZ_BLOCK)))).put(Blocks.CHISELED_SANDSTONE, TexturedModel.COLUMN.get(Blocks.CHISELED_SANDSTONE).updateTextures(m -> {
/*      */             m.put(TextureSlot.END, TextureMapping.getBlockTexture(Blocks.SANDSTONE, "_top"));
/*      */             m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CHISELED_SANDSTONE));
/*      */           })).put(Blocks.CHISELED_RED_SANDSTONE, TexturedModel.COLUMN.get(Blocks.CHISELED_RED_SANDSTONE).updateTextures(m -> {
/*      */             m.put(TextureSlot.END, TextureMapping.getBlockTexture(Blocks.RED_SANDSTONE, "_top"));
/*      */             m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CHISELED_RED_SANDSTONE));
/*  291 */           })).put(Blocks.CHISELED_TUFF_BRICKS, TexturedModel.COLUMN_WITH_WALL.get(Blocks.CHISELED_TUFF_BRICKS)).put(Blocks.CHISELED_TUFF, TexturedModel.COLUMN_WITH_WALL.get(Blocks.CHISELED_TUFF)).build();
/*      */   }
/*      */   
/*      */   public BlockModelGenerators(Consumer<BlockModelDefinitionGenerator> blockStateOutput, ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  294 */     this.blockStateOutput = blockStateOutput;
/*  295 */     this.itemModelOutput = itemModelOutput;
/*  296 */     this.modelOutput = modelOutput;
/*      */   }
/*      */   
/*      */   private void registerSimpleItemModel(Item item, Identifier model) {
/*  300 */     this.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
/*      */   }
/*      */   
/*      */   private void registerSimpleItemModel(Block block, Identifier model) {
/*  304 */     this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
/*      */   }
/*      */   
/*      */   private void registerSimpleTintedItemModel(Block block, Identifier model, ItemTintSource tint) {
/*  308 */     this.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(model, new ItemTintSource[] { tint }));
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModel(Item item) {
/*  312 */     return ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), this.modelOutput);
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModelWithBlockTexture(Item item, Block block) {
/*  316 */     return ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(block), this.modelOutput);
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModelWithBlockTexture(Item item, Block block, String suffix) {
/*  320 */     return ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(TextureMapping.getBlockTexture(block, suffix)), this.modelOutput);
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModelWithBlockTextureAndOverlay(Item item, Block block, String overlaySuffix) {
/*  324 */     Material base = TextureMapping.getBlockTexture(block);
/*  325 */     Material overlay = TextureMapping.getBlockTexture(block, overlaySuffix);
/*  326 */     return ModelTemplates.TWO_LAYERED_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layered(base, overlay), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void registerSimpleFlatItemModel(Item item) {
/*  330 */     registerSimpleItemModel(item, createFlatItemModel(item));
/*      */   }
/*      */   
/*      */   private void registerSimpleFlatItemModel(Block block) {
/*  334 */     Item blockItem = block.asItem();
/*  335 */     if (blockItem != Items.AIR)
/*  336 */       registerSimpleItemModel(blockItem, createFlatItemModelWithBlockTexture(blockItem, block)); 
/*      */   }
/*      */   
/*      */   private void registerSimpleFlatItemModel(Block block, String suffix) {
/*  341 */     Item blockItem = block.asItem();
/*  342 */     if (blockItem != Items.AIR)
/*  343 */       registerSimpleItemModel(blockItem, createFlatItemModelWithBlockTexture(blockItem, block, suffix)); 
/*      */   }
/*      */   
/*      */   private void registerTwoLayerFlatItemModel(Block block, String overlaySuffix) {
/*  348 */     Item blockItem = block.asItem();
/*  349 */     if (blockItem != Items.AIR) {
/*  350 */       Identifier model = createFlatItemModelWithBlockTextureAndOverlay(blockItem, block, overlaySuffix);
/*  351 */       registerSimpleItemModel(blockItem, model);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static MultiVariant createRotatedVariants(Variant base) {
/*  356 */     return variants(new Variant[] { base, base
/*      */           
/*  358 */           .with(Y_ROT_90), base
/*  359 */           .with(Y_ROT_180), base
/*  360 */           .with(Y_ROT_270) });
/*      */   }
/*      */   
/*      */   private static MultiVariant createRotatedVariants(Variant normal, Variant mirrored) {
/*  365 */     return variants(new Variant[] { normal, mirrored, normal
/*      */           
/*  368 */           .with(Y_ROT_180), mirrored
/*  369 */           .with(Y_ROT_180) });
/*      */   }
/*      */   
/*      */   private static PropertyDispatch<MultiVariant> createBooleanModelDispatch(BooleanProperty property, MultiVariant onTrue, MultiVariant onFalse) {
/*  374 */     return (PropertyDispatch<MultiVariant>)PropertyDispatch.initial((Property)property)
/*  375 */       .select(Boolean.valueOf(true), onTrue)
/*  376 */       .select(Boolean.valueOf(false), onFalse);
/*      */   }
/*      */   
/*      */   private void createRotatedMirroredVariantBlock(Block block) {
/*  380 */     Variant normal = plainModel(TexturedModel.CUBE.create(block, this.modelOutput));
/*  381 */     Variant mirrored = plainModel(TexturedModel.CUBE_MIRRORED.create(block, this.modelOutput));
/*  382 */     this.blockStateOutput.accept(
/*  383 */         MultiVariantGenerator.dispatch(block, createRotatedVariants(normal, mirrored)));
/*      */   }
/*      */   
/*      */   private void createRotatedVariantBlock(Block block) {
/*  388 */     Variant normal = plainModel(TexturedModel.CUBE.create(block, this.modelOutput));
/*  389 */     this.blockStateOutput.accept(
/*  390 */         MultiVariantGenerator.dispatch(block, createRotatedVariants(normal)));
/*      */   }
/*      */   
/*      */   private void createBrushableBlock(Block block) {
/*  395 */     this.blockStateOutput.accept(
/*  396 */         MultiVariantGenerator.dispatch(block)
/*  397 */         .with(
/*  398 */           PropertyDispatch.initial((Property)BlockStateProperties.DUSTED)
/*  399 */           .generate(dustProgress -> {
/*      */               String suffix = "_" + dustProgress;
/*      */               Material texture = TextureMapping.getBlockTexture(block, suffix);
/*      */               Identifier model = ModelTemplates.CUBE_ALL.createWithSuffix(block, suffix, (new TextureMapping()).put(TextureSlot.ALL, texture), this.modelOutput);
/*      */               return plainVariant(model);
/*      */             })));
/*  408 */     registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block, "_0"));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createButton(Block block, MultiVariant normal, MultiVariant pressed) {
/*  412 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  413 */       .with(
/*  414 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.POWERED)
/*  415 */         .select(Boolean.valueOf(false), normal)
/*  416 */         .select(Boolean.valueOf(true), pressed))
/*      */       
/*  418 */       .with(
/*  419 */         (PropertyDispatch)PropertyDispatch.modify((Property)BlockStateProperties.ATTACH_FACE, (Property)BlockStateProperties.HORIZONTAL_FACING)
/*  420 */         .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.EAST, Y_ROT_90)
/*  421 */         .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.WEST, Y_ROT_270)
/*  422 */         .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.SOUTH, Y_ROT_180)
/*  423 */         .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.NORTH, NOP)
/*      */         
/*  425 */         .select((Comparable)AttachFace.WALL, (Comparable)Direction.EAST, Y_ROT_90.then(X_ROT_90).then(UV_LOCK))
/*  426 */         .select((Comparable)AttachFace.WALL, (Comparable)Direction.WEST, Y_ROT_270.then(X_ROT_90).then(UV_LOCK))
/*  427 */         .select((Comparable)AttachFace.WALL, (Comparable)Direction.SOUTH, Y_ROT_180.then(X_ROT_90).then(UV_LOCK))
/*  428 */         .select((Comparable)AttachFace.WALL, (Comparable)Direction.NORTH, X_ROT_90.then(UV_LOCK))
/*      */         
/*  430 */         .select((Comparable)AttachFace.CEILING, (Comparable)Direction.EAST, Y_ROT_270.then(X_ROT_180))
/*  431 */         .select((Comparable)AttachFace.CEILING, (Comparable)Direction.WEST, Y_ROT_90.then(X_ROT_180))
/*  432 */         .select((Comparable)AttachFace.CEILING, (Comparable)Direction.SOUTH, X_ROT_180)
/*  433 */         .select((Comparable)AttachFace.CEILING, (Comparable)Direction.NORTH, Y_ROT_180.then(X_ROT_180)));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createDoor(Block block, MultiVariant bottomLeft, MultiVariant bottomLeftOpen, MultiVariant bottomRight, MultiVariant bottomRightOpen, MultiVariant topLeft, MultiVariant topLeftOpen, MultiVariant topRight, MultiVariant topRightOpen) {
/*  438 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  439 */       .with(
/*  440 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.DOUBLE_BLOCK_HALF, (Property)BlockStateProperties.DOOR_HINGE, (Property)BlockStateProperties.OPEN)
/*      */         
/*  442 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(false), bottomLeft)
/*  443 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(false), bottomLeft.with(Y_ROT_90))
/*  444 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(false), bottomLeft.with(Y_ROT_180))
/*  445 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(false), bottomLeft.with(Y_ROT_270))
/*      */         
/*  447 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(false), bottomRight)
/*  448 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(false), bottomRight.with(Y_ROT_90))
/*  449 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(false), bottomRight.with(Y_ROT_180))
/*  450 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(false), bottomRight.with(Y_ROT_270))
/*      */         
/*  452 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(true), bottomLeftOpen.with(Y_ROT_90))
/*  453 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(true), bottomLeftOpen.with(Y_ROT_180))
/*  454 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(true), bottomLeftOpen.with(Y_ROT_270))
/*  455 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(true), bottomLeftOpen)
/*      */         
/*  457 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(true), bottomRightOpen.with(Y_ROT_270))
/*  458 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(true), bottomRightOpen)
/*  459 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(true), bottomRightOpen.with(Y_ROT_90))
/*  460 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.LOWER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(true), bottomRightOpen.with(Y_ROT_180))
/*      */         
/*  463 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(false), topLeft)
/*  464 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(false), topLeft.with(Y_ROT_90))
/*  465 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(false), topLeft.with(Y_ROT_180))
/*  466 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(false), topLeft.with(Y_ROT_270))
/*      */         
/*  468 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(false), topRight)
/*  469 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(false), topRight.with(Y_ROT_90))
/*  470 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(false), topRight.with(Y_ROT_180))
/*  471 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(false), topRight.with(Y_ROT_270))
/*      */         
/*  473 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(true), topLeftOpen.with(Y_ROT_90))
/*  474 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(true), topLeftOpen.with(Y_ROT_180))
/*  475 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(true), topLeftOpen.with(Y_ROT_270))
/*  476 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(true), topLeftOpen)
/*      */         
/*  478 */         .select((Comparable)Direction.EAST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(true), topRightOpen.with(Y_ROT_270))
/*  479 */         .select((Comparable)Direction.SOUTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(true), topRightOpen)
/*  480 */         .select((Comparable)Direction.WEST, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(true), topRightOpen.with(Y_ROT_90))
/*  481 */         .select((Comparable)Direction.NORTH, (Comparable)DoubleBlockHalf.UPPER, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(true), topRightOpen.with(Y_ROT_180)));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createCustomFence(Block block, MultiVariant post, MultiVariant north, MultiVariant east, MultiVariant south, MultiVariant west) {
/*  486 */     return (BlockModelDefinitionGenerator)MultiPartGenerator.multiPart(block)
/*  487 */       .with(post)
/*  488 */       .with(condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true)), north)
/*  489 */       .with(condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(true)), east)
/*  490 */       .with(condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true)), south)
/*  491 */       .with(condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), west);
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createFence(Block block, MultiVariant post, MultiVariant side) {
/*  495 */     return (BlockModelDefinitionGenerator)MultiPartGenerator.multiPart(block)
/*  496 */       .with(post)
/*  497 */       .with(condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true)), side.with(UV_LOCK))
/*  498 */       .with(condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(true)), side.with(Y_ROT_90).with(UV_LOCK))
/*  499 */       .with(condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true)), side.with(Y_ROT_180).with(UV_LOCK))
/*  500 */       .with(condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), side.with(Y_ROT_270).with(UV_LOCK));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createWall(Block block, MultiVariant post, MultiVariant lowSide, MultiVariant tallSide) {
/*  504 */     return (BlockModelDefinitionGenerator)MultiPartGenerator.multiPart(block)
/*  505 */       .with(condition().term((Property)BlockStateProperties.UP, Boolean.valueOf(true)), post)
/*      */       
/*  507 */       .with(condition().term((Property)BlockStateProperties.NORTH_WALL, (Comparable)WallSide.LOW), lowSide.with(UV_LOCK))
/*  508 */       .with(condition().term((Property)BlockStateProperties.EAST_WALL, (Comparable)WallSide.LOW), lowSide.with(Y_ROT_90).with(UV_LOCK))
/*  509 */       .with(condition().term((Property)BlockStateProperties.SOUTH_WALL, (Comparable)WallSide.LOW), lowSide.with(Y_ROT_180).with(UV_LOCK))
/*  510 */       .with(condition().term((Property)BlockStateProperties.WEST_WALL, (Comparable)WallSide.LOW), lowSide.with(Y_ROT_270).with(UV_LOCK))
/*      */       
/*  512 */       .with(condition().term((Property)BlockStateProperties.NORTH_WALL, (Comparable)WallSide.TALL), tallSide.with(UV_LOCK))
/*  513 */       .with(condition().term((Property)BlockStateProperties.EAST_WALL, (Comparable)WallSide.TALL), tallSide.with(Y_ROT_90).with(UV_LOCK))
/*  514 */       .with(condition().term((Property)BlockStateProperties.SOUTH_WALL, (Comparable)WallSide.TALL), tallSide.with(Y_ROT_180).with(UV_LOCK))
/*  515 */       .with(condition().term((Property)BlockStateProperties.WEST_WALL, (Comparable)WallSide.TALL), tallSide.with(Y_ROT_270).with(UV_LOCK));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createFenceGate(Block block, MultiVariant open, MultiVariant closed, MultiVariant openWall, MultiVariant closedWall, boolean uvLock) {
/*  519 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  520 */       .with(
/*  521 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.IN_WALL, (Property)BlockStateProperties.OPEN)
/*  522 */         .select(Boolean.valueOf(false), Boolean.valueOf(false), closed)
/*  523 */         .select(Boolean.valueOf(true), Boolean.valueOf(false), closedWall)
/*  524 */         .select(Boolean.valueOf(false), Boolean.valueOf(true), open)
/*  525 */         .select(Boolean.valueOf(true), Boolean.valueOf(true), openWall))
/*      */       
/*  527 */       .with(uvLock ? UV_LOCK : NOP)
/*  528 */       .with(ROTATION_HORIZONTAL_FACING_ALT);
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createStairs(Block block, MultiVariant inner, MultiVariant straight, MultiVariant outer) {
/*  532 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  533 */       .with(
/*  534 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.HALF, (Property)BlockStateProperties.STAIRS_SHAPE)
/*  535 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.STRAIGHT, straight)
/*  536 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.STRAIGHT, straight.with(Y_ROT_180).with(UV_LOCK))
/*  537 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.STRAIGHT, straight.with(Y_ROT_90).with(UV_LOCK))
/*  538 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.STRAIGHT, straight.with(Y_ROT_270).with(UV_LOCK))
/*  539 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_RIGHT, outer)
/*  540 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_RIGHT, outer.with(Y_ROT_180).with(UV_LOCK))
/*  541 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_RIGHT, outer.with(Y_ROT_90).with(UV_LOCK))
/*  542 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_RIGHT, outer.with(Y_ROT_270).with(UV_LOCK))
/*  543 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_LEFT, outer.with(Y_ROT_270).with(UV_LOCK))
/*  544 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_LEFT, outer.with(Y_ROT_90).with(UV_LOCK))
/*  545 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_LEFT, outer)
/*  546 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_LEFT, outer.with(Y_ROT_180).with(UV_LOCK))
/*  547 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_RIGHT, inner)
/*  548 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_RIGHT, inner.with(Y_ROT_180).with(UV_LOCK))
/*  549 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_RIGHT, inner.with(Y_ROT_90).with(UV_LOCK))
/*  550 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_RIGHT, inner.with(Y_ROT_270).with(UV_LOCK))
/*  551 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_LEFT, inner.with(Y_ROT_270).with(UV_LOCK))
/*  552 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_LEFT, inner.with(Y_ROT_90).with(UV_LOCK))
/*  553 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_LEFT, inner)
/*  554 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_LEFT, inner.with(Y_ROT_180).with(UV_LOCK))
/*  555 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.STRAIGHT, straight.with(X_ROT_180).with(UV_LOCK))
/*  556 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.STRAIGHT, straight.with(X_ROT_180).with(Y_ROT_180).with(UV_LOCK))
/*  557 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.STRAIGHT, straight.with(X_ROT_180).with(Y_ROT_90).with(UV_LOCK))
/*  558 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.STRAIGHT, straight.with(X_ROT_180).with(Y_ROT_270).with(UV_LOCK))
/*  559 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_RIGHT, outer.with(X_ROT_180).with(Y_ROT_90).with(UV_LOCK))
/*  560 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_RIGHT, outer.with(X_ROT_180).with(Y_ROT_270).with(UV_LOCK))
/*  561 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_RIGHT, outer.with(X_ROT_180).with(Y_ROT_180).with(UV_LOCK))
/*  562 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_RIGHT, outer.with(X_ROT_180).with(UV_LOCK))
/*  563 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_LEFT, outer.with(X_ROT_180).with(UV_LOCK))
/*  564 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_LEFT, outer.with(X_ROT_180).with(Y_ROT_180).with(UV_LOCK))
/*  565 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_LEFT, outer.with(X_ROT_180).with(Y_ROT_90).with(UV_LOCK))
/*  566 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_LEFT, outer.with(X_ROT_180).with(Y_ROT_270).with(UV_LOCK))
/*  567 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_RIGHT, inner.with(X_ROT_180).with(Y_ROT_90).with(UV_LOCK))
/*  568 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_RIGHT, inner.with(X_ROT_180).with(Y_ROT_270).with(UV_LOCK))
/*  569 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_RIGHT, inner.with(X_ROT_180).with(Y_ROT_180).with(UV_LOCK))
/*  570 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_RIGHT, inner.with(X_ROT_180).with(UV_LOCK))
/*  571 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_LEFT, inner.with(X_ROT_180).with(UV_LOCK))
/*  572 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_LEFT, inner.with(X_ROT_180).with(Y_ROT_180).with(UV_LOCK))
/*  573 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_LEFT, inner.with(X_ROT_180).with(Y_ROT_90).with(UV_LOCK))
/*  574 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_LEFT, inner.with(X_ROT_180).with(Y_ROT_270).with(UV_LOCK)));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createOrientableTrapdoor(Block block, MultiVariant top, MultiVariant bottom, MultiVariant open) {
/*  579 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  580 */       .with(
/*  581 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.HALF, (Property)BlockStateProperties.OPEN)
/*  582 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, Boolean.valueOf(false), bottom)
/*  583 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, Boolean.valueOf(false), bottom.with(Y_ROT_180))
/*  584 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, Boolean.valueOf(false), bottom.with(Y_ROT_90))
/*  585 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, Boolean.valueOf(false), bottom.with(Y_ROT_270))
/*  586 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, Boolean.valueOf(false), top)
/*  587 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, Boolean.valueOf(false), top.with(Y_ROT_180))
/*  588 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, Boolean.valueOf(false), top.with(Y_ROT_90))
/*  589 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, Boolean.valueOf(false), top.with(Y_ROT_270))
/*  590 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, Boolean.valueOf(true), open)
/*  591 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, Boolean.valueOf(true), open.with(Y_ROT_180))
/*  592 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, Boolean.valueOf(true), open.with(Y_ROT_90))
/*  593 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, Boolean.valueOf(true), open.with(Y_ROT_270))
/*  594 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, Boolean.valueOf(true), open.with(X_ROT_180).with(Y_ROT_180))
/*  595 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, Boolean.valueOf(true), open.with(X_ROT_180))
/*  596 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, Boolean.valueOf(true), open.with(X_ROT_180).with(Y_ROT_270))
/*  597 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, Boolean.valueOf(true), open.with(X_ROT_180).with(Y_ROT_90)));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createTrapdoor(Block block, MultiVariant top, MultiVariant bottom, MultiVariant open) {
/*  602 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  603 */       .with(
/*  604 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.HALF, (Property)BlockStateProperties.OPEN)
/*  605 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, Boolean.valueOf(false), bottom)
/*  606 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, Boolean.valueOf(false), bottom)
/*  607 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, Boolean.valueOf(false), bottom)
/*  608 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, Boolean.valueOf(false), bottom)
/*  609 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, Boolean.valueOf(false), top)
/*  610 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, Boolean.valueOf(false), top)
/*  611 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, Boolean.valueOf(false), top)
/*  612 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, Boolean.valueOf(false), top)
/*  613 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, Boolean.valueOf(true), open)
/*  614 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, Boolean.valueOf(true), open.with(Y_ROT_180))
/*  615 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, Boolean.valueOf(true), open.with(Y_ROT_90))
/*  616 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, Boolean.valueOf(true), open.with(Y_ROT_270))
/*  617 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, Boolean.valueOf(true), open)
/*  618 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, Boolean.valueOf(true), open.with(Y_ROT_180))
/*  619 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, Boolean.valueOf(true), open.with(Y_ROT_90))
/*  620 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, Boolean.valueOf(true), open.with(Y_ROT_270)));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createBed(Block block, MultiVariant headModel, MultiVariant footModel) {
/*  625 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  626 */       .with(
/*  627 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.BED_PART)
/*  628 */         .select((Comparable)Direction.NORTH, (Comparable)BedPart.HEAD, headModel)
/*  629 */         .select((Comparable)Direction.SOUTH, (Comparable)BedPart.HEAD, headModel.with(Y_ROT_180))
/*  630 */         .select((Comparable)Direction.EAST, (Comparable)BedPart.HEAD, headModel.with(Y_ROT_90))
/*  631 */         .select((Comparable)Direction.WEST, (Comparable)BedPart.HEAD, headModel.with(Y_ROT_270))
/*  632 */         .select((Comparable)Direction.NORTH, (Comparable)BedPart.FOOT, footModel)
/*  633 */         .select((Comparable)Direction.SOUTH, (Comparable)BedPart.FOOT, footModel.with(Y_ROT_180))
/*  634 */         .select((Comparable)Direction.EAST, (Comparable)BedPart.FOOT, footModel.with(Y_ROT_90))
/*  635 */         .select((Comparable)Direction.WEST, (Comparable)BedPart.FOOT, footModel.with(Y_ROT_270)));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createSign(Block block, MultiVariant rot0, MultiVariant rot1, MultiVariant rot2, MultiVariant rot3) {
/*  640 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  641 */       .with(
/*  642 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.ROTATION_16)
/*  643 */         .select(Integer.valueOf(0), rot0)
/*  644 */         .select(Integer.valueOf(1), rot1)
/*  645 */         .select(Integer.valueOf(2), rot2)
/*  646 */         .select(Integer.valueOf(3), rot3)
/*  647 */         .select(Integer.valueOf(4), rot0.with(Y_ROT_90))
/*  648 */         .select(Integer.valueOf(5), rot1.with(Y_ROT_90))
/*  649 */         .select(Integer.valueOf(6), rot2.with(Y_ROT_90))
/*  650 */         .select(Integer.valueOf(7), rot3.with(Y_ROT_90))
/*  651 */         .select(Integer.valueOf(8), rot0.with(Y_ROT_180))
/*  652 */         .select(Integer.valueOf(9), rot1.with(Y_ROT_180))
/*  653 */         .select(Integer.valueOf(10), rot2.with(Y_ROT_180))
/*  654 */         .select(Integer.valueOf(11), rot3.with(Y_ROT_180))
/*  655 */         .select(Integer.valueOf(12), rot0.with(Y_ROT_270))
/*  656 */         .select(Integer.valueOf(13), rot1.with(Y_ROT_270))
/*  657 */         .select(Integer.valueOf(14), rot2.with(Y_ROT_270))
/*  658 */         .select(Integer.valueOf(15), rot3.with(Y_ROT_270)));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createHangingSign(Block block, MultiVariant rot0, MultiVariant rot1, MultiVariant rot2, MultiVariant rot3, MultiVariant attachedRot0, MultiVariant attachedRot1, MultiVariant attachedRot2, MultiVariant attachedRot3) {
/*  673 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  674 */       .with(
/*  675 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.ATTACHED, (Property)BlockStateProperties.ROTATION_16)
/*  676 */         .select(Boolean.valueOf(false), Integer.valueOf(0), rot0)
/*  677 */         .select(Boolean.valueOf(false), Integer.valueOf(1), rot1)
/*  678 */         .select(Boolean.valueOf(false), Integer.valueOf(2), rot2)
/*  679 */         .select(Boolean.valueOf(false), Integer.valueOf(3), rot3)
/*  680 */         .select(Boolean.valueOf(false), Integer.valueOf(4), rot0.with(Y_ROT_90))
/*  681 */         .select(Boolean.valueOf(false), Integer.valueOf(5), rot1.with(Y_ROT_90))
/*  682 */         .select(Boolean.valueOf(false), Integer.valueOf(6), rot2.with(Y_ROT_90))
/*  683 */         .select(Boolean.valueOf(false), Integer.valueOf(7), rot3.with(Y_ROT_90))
/*  684 */         .select(Boolean.valueOf(false), Integer.valueOf(8), rot0.with(Y_ROT_180))
/*  685 */         .select(Boolean.valueOf(false), Integer.valueOf(9), rot1.with(Y_ROT_180))
/*  686 */         .select(Boolean.valueOf(false), Integer.valueOf(10), rot2.with(Y_ROT_180))
/*  687 */         .select(Boolean.valueOf(false), Integer.valueOf(11), rot3.with(Y_ROT_180))
/*  688 */         .select(Boolean.valueOf(false), Integer.valueOf(12), rot0.with(Y_ROT_270))
/*  689 */         .select(Boolean.valueOf(false), Integer.valueOf(13), rot1.with(Y_ROT_270))
/*  690 */         .select(Boolean.valueOf(false), Integer.valueOf(14), rot2.with(Y_ROT_270))
/*  691 */         .select(Boolean.valueOf(false), Integer.valueOf(15), rot3.with(Y_ROT_270))
/*  692 */         .select(Boolean.valueOf(true), Integer.valueOf(0), attachedRot0)
/*  693 */         .select(Boolean.valueOf(true), Integer.valueOf(1), attachedRot1)
/*  694 */         .select(Boolean.valueOf(true), Integer.valueOf(2), attachedRot2)
/*  695 */         .select(Boolean.valueOf(true), Integer.valueOf(3), attachedRot3)
/*  696 */         .select(Boolean.valueOf(true), Integer.valueOf(4), attachedRot0.with(Y_ROT_90))
/*  697 */         .select(Boolean.valueOf(true), Integer.valueOf(5), attachedRot1.with(Y_ROT_90))
/*  698 */         .select(Boolean.valueOf(true), Integer.valueOf(6), attachedRot2.with(Y_ROT_90))
/*  699 */         .select(Boolean.valueOf(true), Integer.valueOf(7), attachedRot3.with(Y_ROT_90))
/*  700 */         .select(Boolean.valueOf(true), Integer.valueOf(8), attachedRot0.with(Y_ROT_180))
/*  701 */         .select(Boolean.valueOf(true), Integer.valueOf(9), attachedRot1.with(Y_ROT_180))
/*  702 */         .select(Boolean.valueOf(true), Integer.valueOf(10), attachedRot2.with(Y_ROT_180))
/*  703 */         .select(Boolean.valueOf(true), Integer.valueOf(11), attachedRot3.with(Y_ROT_180))
/*  704 */         .select(Boolean.valueOf(true), Integer.valueOf(12), attachedRot0.with(Y_ROT_270))
/*  705 */         .select(Boolean.valueOf(true), Integer.valueOf(13), attachedRot1.with(Y_ROT_270))
/*  706 */         .select(Boolean.valueOf(true), Integer.valueOf(14), attachedRot2.with(Y_ROT_270))
/*  707 */         .select(Boolean.valueOf(true), Integer.valueOf(15), attachedRot3.with(Y_ROT_270)));
/*      */   }
/*      */   
/*      */   private static MultiVariantGenerator createSimpleBlock(Block block, MultiVariant variant) {
/*  712 */     return MultiVariantGenerator.dispatch(block, variant);
/*      */   }
/*      */   
/*      */   private static PropertyDispatch<VariantMutator> createRotatedPillar() {
/*  716 */     return (PropertyDispatch<VariantMutator>)PropertyDispatch.modify((Property)BlockStateProperties.AXIS)
/*  717 */       .select((Comparable)Direction.Axis.Y, NOP)
/*  718 */       .select((Comparable)Direction.Axis.Z, X_ROT_90)
/*  719 */       .select((Comparable)Direction.Axis.X, X_ROT_90.then(Y_ROT_90));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createPillarBlockUVLocked(Block block, TextureMapping mapping, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  723 */     MultiVariant xAxisModel = plainVariant(ModelTemplates.CUBE_COLUMN_UV_LOCKED_X.create(block, mapping, modelOutput));
/*  724 */     MultiVariant yAxisModel = plainVariant(ModelTemplates.CUBE_COLUMN_UV_LOCKED_Y.create(block, mapping, modelOutput));
/*  725 */     MultiVariant zAxisModel = plainVariant(ModelTemplates.CUBE_COLUMN_UV_LOCKED_Z.create(block, mapping, modelOutput));
/*  727 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  728 */       .with(
/*  729 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.AXIS)
/*  730 */         .select((Comparable)Direction.Axis.X, xAxisModel)
/*  731 */         .select((Comparable)Direction.Axis.Y, yAxisModel)
/*  732 */         .select((Comparable)Direction.Axis.Z, zAxisModel));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createAxisAlignedPillarBlock(Block block, MultiVariant model) {
/*  737 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block, model).with(createRotatedPillar());
/*      */   }
/*      */   
/*      */   private void createAxisAlignedPillarBlockCustomModel(Block block, MultiVariant model) {
/*  741 */     this.blockStateOutput.accept(
/*  742 */         createAxisAlignedPillarBlock(block, model));
/*      */   }
/*      */   
/*      */   private void createAxisAlignedPillarBlock(Block block, TexturedModel.Provider modelProvider) {
/*  747 */     MultiVariant model = plainVariant(modelProvider.create(block, this.modelOutput));
/*  748 */     this.blockStateOutput.accept(
/*  749 */         createAxisAlignedPillarBlock(block, model));
/*      */   }
/*      */   
/*      */   private void createHorizontallyRotatedBlock(Block block, TexturedModel.Provider modelProvider) {
/*  754 */     MultiVariant model = plainVariant(modelProvider.create(block, this.modelOutput));
/*  755 */     this.blockStateOutput.accept(
/*  756 */         MultiVariantGenerator.dispatch(block, model)
/*  757 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createRotatedPillarWithHorizontalVariant(Block block, MultiVariant model, MultiVariant horizontalModel) {
/*  762 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  763 */       .with(
/*  764 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.AXIS)
/*  765 */         .select((Comparable)Direction.Axis.Y, model)
/*  766 */         .select((Comparable)Direction.Axis.Z, horizontalModel.with(X_ROT_90))
/*  767 */         .select((Comparable)Direction.Axis.X, horizontalModel.with(X_ROT_90).with(Y_ROT_90)));
/*      */   }
/*      */   
/*      */   private void createRotatedPillarWithHorizontalVariant(Block block, TexturedModel.Provider verticalProvider, TexturedModel.Provider horizontalProvider) {
/*  772 */     MultiVariant model = plainVariant(verticalProvider.create(block, this.modelOutput));
/*  773 */     MultiVariant horizontalModel = plainVariant(horizontalProvider.create(block, this.modelOutput));
/*  774 */     this.blockStateOutput.accept(
/*  775 */         createRotatedPillarWithHorizontalVariant(block, model, horizontalModel));
/*      */   }
/*      */   
/*      */   private void createCreakingHeart(Block block) {
/*  780 */     MultiVariant model = plainVariant(TexturedModel.COLUMN_ALT.create(block, this.modelOutput));
/*  781 */     MultiVariant horizontalModel = plainVariant(TexturedModel.COLUMN_HORIZONTAL_ALT.create(block, this.modelOutput));
/*  782 */     MultiVariant activeModel = plainVariant(createCreakingHeartModel(TexturedModel.COLUMN_ALT, block, "_awake"));
/*  783 */     MultiVariant activeHorizontalModel = plainVariant(createCreakingHeartModel(TexturedModel.COLUMN_HORIZONTAL_ALT, block, "_awake"));
/*  784 */     MultiVariant dormantModel = plainVariant(createCreakingHeartModel(TexturedModel.COLUMN_ALT, block, "_dormant"));
/*  785 */     MultiVariant dormantHorizontalModel = plainVariant(createCreakingHeartModel(TexturedModel.COLUMN_HORIZONTAL_ALT, block, "_dormant"));
/*  786 */     this.blockStateOutput.accept(
/*  787 */         MultiVariantGenerator.dispatch(block)
/*  788 */         .with(
/*  789 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.AXIS, (Property)CreakingHeartBlock.STATE)
/*  790 */           .select((Comparable)Direction.Axis.Y, (Comparable)CreakingHeartState.UPROOTED, model)
/*  791 */           .select((Comparable)Direction.Axis.Z, (Comparable)CreakingHeartState.UPROOTED, horizontalModel.with(X_ROT_90))
/*  792 */           .select((Comparable)Direction.Axis.X, (Comparable)CreakingHeartState.UPROOTED, horizontalModel.with(X_ROT_90).with(Y_ROT_90))
/*  793 */           .select((Comparable)Direction.Axis.Y, (Comparable)CreakingHeartState.DORMANT, dormantModel)
/*  794 */           .select((Comparable)Direction.Axis.Z, (Comparable)CreakingHeartState.DORMANT, dormantHorizontalModel.with(X_ROT_90))
/*  795 */           .select((Comparable)Direction.Axis.X, (Comparable)CreakingHeartState.DORMANT, dormantHorizontalModel.with(X_ROT_90).with(Y_ROT_90))
/*  796 */           .select((Comparable)Direction.Axis.Y, (Comparable)CreakingHeartState.AWAKE, activeModel)
/*  797 */           .select((Comparable)Direction.Axis.Z, (Comparable)CreakingHeartState.AWAKE, activeHorizontalModel.with(X_ROT_90))
/*  798 */           .select((Comparable)Direction.Axis.X, (Comparable)CreakingHeartState.AWAKE, activeHorizontalModel.with(X_ROT_90).with(Y_ROT_90))));
/*      */   }
/*      */   
/*      */   private Identifier createCreakingHeartModel(TexturedModel.Provider provider, Block block, String suffix) {
/*  804 */     return provider.updateTexture(t -> t.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, suffix)).put(TextureSlot.END, TextureMapping.getBlockTexture(block, "_top" + suffix)))
/*      */       
/*  807 */       .createWithSuffix(block, suffix, this.modelOutput);
/*      */   }
/*      */   
/*      */   private Identifier createSuffixedVariant(Block block, String suffix, ModelTemplate template, Function<Material, TextureMapping> textureMapping) {
/*  811 */     return template.createWithSuffix(block, suffix, textureMapping.apply(TextureMapping.getBlockTexture(block, suffix)), this.modelOutput);
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createPressurePlate(Block block, MultiVariant off, MultiVariant on) {
/*  815 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  816 */       .with(createBooleanModelDispatch(BlockStateProperties.POWERED, on, off));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createSlab(Block block, MultiVariant bottom, MultiVariant top, MultiVariant full) {
/*  820 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(block)
/*  821 */       .with(
/*  822 */         (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.SLAB_TYPE)
/*  823 */         .select((Comparable)SlabType.BOTTOM, bottom)
/*  824 */         .select((Comparable)SlabType.TOP, top)
/*  825 */         .select((Comparable)SlabType.DOUBLE, full));
/*      */   }
/*      */   
/*      */   private void createTrivialCube(Block block) {
/*  830 */     createTrivialBlock(block, TexturedModel.CUBE);
/*      */   }
/*      */   
/*      */   private void createTrivialBlock(Block block, TexturedModel.Provider modelProvider) {
/*  834 */     this.blockStateOutput.accept(
/*  835 */         createSimpleBlock(block, plainVariant(modelProvider.create(block, this.modelOutput))));
/*      */   }
/*      */   
/*      */   public void createTintedLeaves(Block block, TexturedModel.Provider modelProvider, int tintColor) {
/*  840 */     Identifier blockModel = modelProvider.create(block, this.modelOutput);
/*  841 */     this.blockStateOutput.accept(
/*  842 */         createSimpleBlock(block, plainVariant(blockModel)));
/*  844 */     registerSimpleTintedItemModel(block, blockModel, ItemModelUtils.constantTint(tintColor));
/*      */   }
/*      */   
/*      */   private void createVine() {
/*  848 */     createMultifaceBlockStates(Blocks.VINE);
/*  850 */     Identifier itemModel = createFlatItemModelWithBlockTexture(Items.VINE, Blocks.VINE);
/*  851 */     registerSimpleTintedItemModel(Blocks.VINE, itemModel, ItemModelUtils.constantTint(-12012264));
/*      */   }
/*      */   
/*      */   private void createItemWithGrassTint(Block block) {
/*  855 */     Identifier itemModel = createFlatItemModelWithBlockTexture(block.asItem(), block);
/*  856 */     registerSimpleTintedItemModel(block, itemModel, (ItemTintSource)new GrassColorSource());
/*      */   }
/*      */   
/*  859 */   private static final Map<BlockFamily.Variant, BiConsumer<BlockFamilyProvider, Block>> SHAPE_CONSUMERS = (Map<BlockFamily.Variant, BiConsumer<BlockFamilyProvider, Block>>)ImmutableMap.builder()
/*  860 */     .put(BlockFamily.Variant.BUTTON, BlockFamilyProvider::button)
/*  861 */     .put(BlockFamily.Variant.DOOR, BlockFamilyProvider::door)
/*  862 */     .put(BlockFamily.Variant.CHISELED, BlockFamilyProvider::fullBlockVariant)
/*  863 */     .put(BlockFamily.Variant.CRACKED, BlockFamilyProvider::fullBlockVariant)
/*  864 */     .put(BlockFamily.Variant.CUSTOM_FENCE, BlockFamilyProvider::customFence)
/*  865 */     .put(BlockFamily.Variant.FENCE, BlockFamilyProvider::fence)
/*  866 */     .put(BlockFamily.Variant.CUSTOM_FENCE_GATE, BlockFamilyProvider::customFenceGate)
/*  867 */     .put(BlockFamily.Variant.FENCE_GATE, BlockFamilyProvider::fenceGate)
/*  868 */     .put(BlockFamily.Variant.SIGN, BlockFamilyProvider::sign)
/*  869 */     .put(BlockFamily.Variant.CUSTOM_HANGING_SIGN, BlockFamilyProvider::customHangingSign)
/*  870 */     .put(BlockFamily.Variant.HANGING_SIGN, BlockFamilyProvider::hangingSign)
/*  871 */     .put(BlockFamily.Variant.SLAB, BlockFamilyProvider::slab)
/*  872 */     .put(BlockFamily.Variant.STAIRS, BlockFamilyProvider::stairs)
/*  873 */     .put(BlockFamily.Variant.PRESSURE_PLATE, BlockFamilyProvider::pressurePlate)
/*  874 */     .put(BlockFamily.Variant.TRAPDOOR, BlockFamilyProvider::trapdoor)
/*  875 */     .put(BlockFamily.Variant.WALL, BlockFamilyProvider::wall)
/*  876 */     .put(BlockFamily.Variant.BRICKS, BlockFamilyProvider::fullBlockVariant)
/*  877 */     .put(BlockFamily.Variant.TILES, BlockFamilyProvider::fullBlockVariant)
/*  878 */     .put(BlockFamily.Variant.COBBLED, BlockFamilyProvider::fullBlockVariant)
/*  879 */     .put(BlockFamily.Variant.PILLAR, BlockFamilyProvider::pillar)
/*  880 */     .build();
/*      */   
/*      */   private class BlockFamilyProvider {
/*      */     private final TextureMapping mapping;
/*      */     
/*  884 */     private final Map<ModelTemplate, Identifier> models = new HashMap<>();
/*      */     
/*      */     private BlockFamily family;
/*      */     
/*      */     private Variant fullBlock;
/*      */     
/*  888 */     private final Set<Block> skipGeneratingModelsFor = new HashSet<>();
/*      */     
/*      */     public BlockFamilyProvider(TextureMapping mapping) {
/*  891 */       this.mapping = mapping;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider fullBlock(Block block, ModelTemplate template) {
/*  895 */       this.fullBlock = BlockModelGenerators.plainModel(template.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  896 */       if (BlockModelGenerators.FULL_BLOCK_MODEL_CUSTOM_GENERATORS.containsKey(block)) {
/*  897 */         BlockModelGenerators.this.blockStateOutput.accept(((BlockModelGenerators.BlockStateGeneratorSupplier)BlockModelGenerators.FULL_BLOCK_MODEL_CUSTOM_GENERATORS.get(block)).create(block, this.fullBlock, this.mapping, BlockModelGenerators.this.modelOutput));
/*      */       } else {
/*  899 */         BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.variant(this.fullBlock)));
/*      */       } 
/*  901 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider donateModelTo(Block donor, Block copyTo) {
/*  905 */       Identifier donorModelLocation = ModelLocationUtils.getModelLocation(donor);
/*  906 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(copyTo, BlockModelGenerators.plainVariant(donorModelLocation)));
/*  907 */       BlockModelGenerators.this.itemModelOutput.copy(donor.asItem(), copyTo.asItem());
/*  908 */       this.skipGeneratingModelsFor.add(copyTo);
/*  909 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider button(Block block) {
/*  913 */       MultiVariant normal = BlockModelGenerators.plainVariant(ModelTemplates.BUTTON.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  914 */       MultiVariant pressed = BlockModelGenerators.plainVariant(ModelTemplates.BUTTON_PRESSED.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  915 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createButton(block, normal, pressed));
/*  917 */       Identifier inventory = ModelTemplates.BUTTON_INVENTORY.create(block, this.mapping, BlockModelGenerators.this.modelOutput);
/*  918 */       BlockModelGenerators.this.registerSimpleItemModel(block, inventory);
/*  919 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider wall(Block block) {
/*  923 */       MultiVariant post = BlockModelGenerators.plainVariant(ModelTemplates.WALL_POST.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  924 */       MultiVariant low = BlockModelGenerators.plainVariant(ModelTemplates.WALL_LOW_SIDE.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  925 */       MultiVariant high = BlockModelGenerators.plainVariant(ModelTemplates.WALL_TALL_SIDE.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  926 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createWall(block, post, low, high));
/*  928 */       Identifier inventory = ModelTemplates.WALL_INVENTORY.create(block, this.mapping, BlockModelGenerators.this.modelOutput);
/*  929 */       BlockModelGenerators.this.registerSimpleItemModel(block, inventory);
/*  930 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider customFence(Block block) {
/*  934 */       TextureMapping mapping = TextureMapping.customParticle(block);
/*  936 */       MultiVariant post = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_POST.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  937 */       MultiVariant north = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_NORTH.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  938 */       MultiVariant east = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_EAST.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  939 */       MultiVariant south = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_SOUTH.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  940 */       MultiVariant west = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_WEST.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  941 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createCustomFence(block, post, north, east, south, west));
/*  943 */       Identifier inventory = ModelTemplates.CUSTOM_FENCE_INVENTORY.create(block, mapping, BlockModelGenerators.this.modelOutput);
/*  944 */       BlockModelGenerators.this.registerSimpleItemModel(block, inventory);
/*  945 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider fence(Block block) {
/*  949 */       MultiVariant post = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_POST.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  950 */       MultiVariant side = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_SIDE.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  951 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createFence(block, post, side));
/*  953 */       Identifier inventory = ModelTemplates.FENCE_INVENTORY.create(block, this.mapping, BlockModelGenerators.this.modelOutput);
/*  954 */       BlockModelGenerators.this.registerSimpleItemModel(block, inventory);
/*  955 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider customFenceGate(Block block) {
/*  959 */       TextureMapping mapping = TextureMapping.customParticle(block);
/*  961 */       MultiVariant open = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_OPEN.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  962 */       MultiVariant closed = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_CLOSED.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  963 */       MultiVariant openWall = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_WALL_OPEN.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  964 */       MultiVariant closedWall = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_WALL_CLOSED.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  965 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createFenceGate(block, open, closed, openWall, closedWall, false));
/*  966 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider fenceGate(Block block) {
/*  970 */       MultiVariant open = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_OPEN.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  971 */       MultiVariant closed = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_CLOSED.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  972 */       MultiVariant openWall = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_WALL_OPEN.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  973 */       MultiVariant closedWall = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_WALL_CLOSED.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  974 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createFenceGate(block, open, closed, openWall, closedWall, true));
/*  975 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider pressurePlate(Block block) {
/*  979 */       MultiVariant off = BlockModelGenerators.plainVariant(ModelTemplates.PRESSURE_PLATE_UP.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  980 */       MultiVariant on = BlockModelGenerators.plainVariant(ModelTemplates.PRESSURE_PLATE_DOWN.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  981 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createPressurePlate(block, off, on));
/*  982 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider sign(Block sign) {
/*  986 */       if (this.family == null)
/*  987 */         throw new IllegalStateException("Family not defined"); 
/*  991 */       TextureMapping mapping = (new TextureMapping()).put(TextureSlot.ALL, TextureMapping.getBlockTexture(sign)).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(this.family.getBaseBlock()));
/*  993 */       MultiVariant standingRot0 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_0.create(ModelLocationUtils.getModelLocation(sign, "_rot_0"), mapping, BlockModelGenerators.this.modelOutput));
/*  994 */       MultiVariant standingRot1 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_1.create(ModelLocationUtils.getModelLocation(sign, "_rot_1"), mapping, BlockModelGenerators.this.modelOutput));
/*  995 */       MultiVariant standingRot2 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_2.create(ModelLocationUtils.getModelLocation(sign, "_rot_2"), mapping, BlockModelGenerators.this.modelOutput));
/*  996 */       MultiVariant standingRot3 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_3.create(ModelLocationUtils.getModelLocation(sign, "_rot_3"), mapping, BlockModelGenerators.this.modelOutput));
/*  997 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSign(sign, standingRot0, standingRot1, standingRot2, standingRot3));
/*  999 */       Block wallSign = (Block)this.family.getVariants().get(BlockFamily.Variant.WALL_SIGN);
/* 1000 */       MultiVariant wallModel = BlockModelGenerators.plainVariant(ModelTemplates.WALL_SIGN.create(wallSign, mapping, BlockModelGenerators.this.modelOutput));
/* 1001 */       BlockModelGenerators.this.blockStateOutput.accept(
/* 1002 */           MultiVariantGenerator.dispatch(wallSign, wallModel)
/* 1003 */           .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT));
/* 1006 */       BlockModelGenerators.this.registerSimpleFlatItemModel(sign.asItem());
/* 1007 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider customHangingSign(Block hangingSign) {
/* 1011 */       return hangingSign(hangingSign, this.family.getBaseBlock(), BlockFamily.Variant.CUSTOM_WALL_HANGING_SIGN);
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider hangingSign(Block hangingSign) {
/* 1015 */       return hangingSign(hangingSign, this.family.get(BlockFamily.Variant.STRIPPED_LOG), BlockFamily.Variant.WALL_HANGING_SIGN);
/*      */     }
/*      */     
/*      */     private BlockFamilyProvider hangingSign(Block hangingSign, Block particleBlock, BlockFamily.Variant wallVarient) {
/* 1021 */       TextureMapping mapping = (new TextureMapping()).put(TextureSlot.ALL, TextureMapping.getBlockTexture(hangingSign)).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(particleBlock));
/* 1023 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createHangingSign(hangingSign, 
/*      */             
/* 1025 */             BlockModelGenerators.plainVariant(ModelTemplates.HANGING_SIGN_ROT_0.create(ModelLocationUtils.getModelLocation(hangingSign, "_rot_0"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1026 */             BlockModelGenerators.plainVariant(ModelTemplates.HANGING_SIGN_ROT_1.create(ModelLocationUtils.getModelLocation(hangingSign, "_rot_1"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1027 */             BlockModelGenerators.plainVariant(ModelTemplates.HANGING_SIGN_ROT_2.create(ModelLocationUtils.getModelLocation(hangingSign, "_rot_2"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1028 */             BlockModelGenerators.plainVariant(ModelTemplates.HANGING_SIGN_ROT_3.create(ModelLocationUtils.getModelLocation(hangingSign, "_rot_3"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1029 */             BlockModelGenerators.plainVariant(ModelTemplates.ATTACHED_HANGING_SIGN_ROT_0.create(ModelLocationUtils.getModelLocation(hangingSign, "_attached_rot_0"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1030 */             BlockModelGenerators.plainVariant(ModelTemplates.ATTACHED_HANGING_SIGN_ROT_1.create(ModelLocationUtils.getModelLocation(hangingSign, "_attached_rot_1"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1031 */             BlockModelGenerators.plainVariant(ModelTemplates.ATTACHED_HANGING_SIGN_ROT_2.create(ModelLocationUtils.getModelLocation(hangingSign, "_attached_rot_2"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1032 */             BlockModelGenerators.plainVariant(ModelTemplates.ATTACHED_HANGING_SIGN_ROT_3.create(ModelLocationUtils.getModelLocation(hangingSign, "_attached_rot_3"), mapping, BlockModelGenerators.this.modelOutput))));
/* 1035 */       Block wallSign = this.family.get(wallVarient);
/* 1036 */       MultiVariant wallModel = BlockModelGenerators.plainVariant(ModelTemplates.WALL_HANGING_SIGN.create(wallSign, mapping, BlockModelGenerators.this.modelOutput));
/* 1037 */       BlockModelGenerators.this.blockStateOutput.accept(
/* 1038 */           MultiVariantGenerator.dispatch(wallSign, wallModel)
/* 1039 */           .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT));
/* 1042 */       BlockModelGenerators.this.registerSimpleFlatItemModel(hangingSign.asItem());
/* 1043 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider slab(Block slab) {
/* 1047 */       if (this.fullBlock == null)
/* 1048 */         throw new IllegalStateException("Full block not generated yet"); 
/* 1050 */       Identifier bottom = getOrCreateModel(ModelTemplates.SLAB_BOTTOM, slab);
/* 1051 */       MultiVariant top = BlockModelGenerators.plainVariant(getOrCreateModel(ModelTemplates.SLAB_TOP, slab));
/* 1053 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSlab(slab, BlockModelGenerators.plainVariant(bottom), top, BlockModelGenerators.variant(this.fullBlock)));
/* 1054 */       BlockModelGenerators.this.registerSimpleItemModel(slab, bottom);
/* 1055 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider stairs(Block stairs) {
/* 1059 */       MultiVariant inner = BlockModelGenerators.plainVariant(getOrCreateModel(ModelTemplates.STAIRS_INNER, stairs));
/* 1060 */       Identifier straight = getOrCreateModel(ModelTemplates.STAIRS_STRAIGHT, stairs);
/* 1061 */       MultiVariant outer = BlockModelGenerators.plainVariant(getOrCreateModel(ModelTemplates.STAIRS_OUTER, stairs));
/* 1063 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createStairs(stairs, inner, BlockModelGenerators.plainVariant(straight), outer));
/* 1064 */       BlockModelGenerators.this.registerSimpleItemModel(stairs, straight);
/* 1065 */       return this;
/*      */     }
/*      */     
/*      */     private BlockFamilyProvider fullBlockVariant(Block variant) {
/* 1069 */       TexturedModel model = BlockModelGenerators.TEXTURED_MODELS.getOrDefault(variant, TexturedModel.CUBE.get(variant));
/* 1070 */       MultiVariant variantModel = BlockModelGenerators.plainVariant(model.create(variant, BlockModelGenerators.this.modelOutput));
/* 1071 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(variant, variantModel));
/* 1072 */       return this;
/*      */     }
/*      */     
/*      */     private BlockFamilyProvider pillar(Block variant) {
/* 1076 */       MultiVariant verticalModel = BlockModelGenerators.plainVariant(TexturedModel.COLUMN.create(variant, BlockModelGenerators.this.modelOutput));
/* 1077 */       MultiVariant horizontalModel = BlockModelGenerators.plainVariant(TexturedModel.COLUMN_HORIZONTAL.create(variant, BlockModelGenerators.this.modelOutput));
/* 1078 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createRotatedPillarWithHorizontalVariant(variant, verticalModel, horizontalModel));
/* 1079 */       return this;
/*      */     }
/*      */     
/*      */     private BlockFamilyProvider door(Block door) {
/* 1083 */       BlockModelGenerators.this.createDoor(door);
/* 1084 */       return this;
/*      */     }
/*      */     
/*      */     private void trapdoor(Block result) {
/* 1088 */       if (BlockModelGenerators.NON_ORIENTABLE_TRAPDOOR.contains(result)) {
/* 1089 */         BlockModelGenerators.this.createTrapdoor(result);
/*      */       } else {
/* 1091 */         BlockModelGenerators.this.createOrientableTrapdoor(result);
/*      */       } 
/*      */     }
/*      */     
/*      */     private Identifier getOrCreateModel(ModelTemplate modelTemplate, Block block) {
/* 1096 */       return this.models.computeIfAbsent(modelTemplate, template -> template.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider generateFor(BlockFamily family) {
/* 1100 */       this.family = family;
/* 1101 */       family.getVariants().forEach((variant, result) -> {
/*      */             boolean modelAlreadyRegisteredAsAnotherFamilyBase = BlockFamilies.getAllFamilies().anyMatch(());
/*      */             if (this.skipGeneratingModelsFor.contains(result) || modelAlreadyRegisteredAsAnotherFamilyBase)
/*      */               return; 
/*      */             BiConsumer<BlockFamilyProvider, Block> consumer = BlockModelGenerators.SHAPE_CONSUMERS.get(variant);
/*      */             if (consumer != null)
/*      */               consumer.accept(this, result); 
/*      */           });
/* 1111 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   private BlockFamilyProvider family(Block block) {
/* 1116 */     TexturedModel model = TEXTURED_MODELS.getOrDefault(block, TexturedModel.CUBE.get(block));
/* 1117 */     return (new BlockFamilyProvider(model.getMapping())).fullBlock(block, model.getTemplate());
/*      */   }
/*      */   
/*      */   private void createDoor(Block door) {
/* 1121 */     TextureMapping mapping = TextureMapping.door(door);
/* 1122 */     MultiVariant doorBottomLeft = plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT.create(door, mapping, this.modelOutput));
/* 1123 */     MultiVariant doorBottomLeftOpen = plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN.create(door, mapping, this.modelOutput));
/* 1124 */     MultiVariant doorBottomRight = plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT.create(door, mapping, this.modelOutput));
/* 1125 */     MultiVariant doorBottomRightOpen = plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.create(door, mapping, this.modelOutput));
/* 1126 */     MultiVariant doorTopLeft = plainVariant(ModelTemplates.DOOR_TOP_LEFT.create(door, mapping, this.modelOutput));
/* 1127 */     MultiVariant doorTopLeftOpen = plainVariant(ModelTemplates.DOOR_TOP_LEFT_OPEN.create(door, mapping, this.modelOutput));
/* 1128 */     MultiVariant doorTopRight = plainVariant(ModelTemplates.DOOR_TOP_RIGHT.create(door, mapping, this.modelOutput));
/* 1129 */     MultiVariant doorTopRightOpen = plainVariant(ModelTemplates.DOOR_TOP_RIGHT_OPEN.create(door, mapping, this.modelOutput));
/* 1131 */     registerSimpleFlatItemModel(door.asItem());
/* 1132 */     this.blockStateOutput.accept(createDoor(door, doorBottomLeft, doorBottomLeftOpen, doorBottomRight, doorBottomRightOpen, doorTopLeft, doorTopLeftOpen, doorTopRight, doorTopRightOpen));
/*      */   }
/*      */   
/*      */   private void copyDoorModel(Block donor, Block acceptor) {
/* 1136 */     MultiVariant doorBottomLeft = plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT.getDefaultModelLocation(donor));
/* 1137 */     MultiVariant doorBottomLeftOpen = plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN.getDefaultModelLocation(donor));
/* 1138 */     MultiVariant doorBottomRight = plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT.getDefaultModelLocation(donor));
/* 1139 */     MultiVariant doorBottomRightOpen = plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.getDefaultModelLocation(donor));
/* 1140 */     MultiVariant doorTopLeft = plainVariant(ModelTemplates.DOOR_TOP_LEFT.getDefaultModelLocation(donor));
/* 1141 */     MultiVariant doorTopLeftOpen = plainVariant(ModelTemplates.DOOR_TOP_LEFT_OPEN.getDefaultModelLocation(donor));
/* 1142 */     MultiVariant doorTopRight = plainVariant(ModelTemplates.DOOR_TOP_RIGHT.getDefaultModelLocation(donor));
/* 1143 */     MultiVariant doorTopRightOpen = plainVariant(ModelTemplates.DOOR_TOP_RIGHT_OPEN.getDefaultModelLocation(donor));
/* 1145 */     this.itemModelOutput.copy(donor.asItem(), acceptor.asItem());
/* 1146 */     this.blockStateOutput.accept(createDoor(acceptor, doorBottomLeft, doorBottomLeftOpen, doorBottomRight, doorBottomRightOpen, doorTopLeft, doorTopLeftOpen, doorTopRight, doorTopRightOpen));
/*      */   }
/*      */   
/*      */   private void createOrientableTrapdoor(Block trapdoor) {
/* 1150 */     TextureMapping mapping = TextureMapping.defaultTexture(trapdoor);
/* 1151 */     MultiVariant top = plainVariant(ModelTemplates.ORIENTABLE_TRAPDOOR_TOP.create(trapdoor, mapping, this.modelOutput));
/* 1152 */     Identifier bottom = ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM.create(trapdoor, mapping, this.modelOutput);
/* 1153 */     MultiVariant open = plainVariant(ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN.create(trapdoor, mapping, this.modelOutput));
/* 1155 */     this.blockStateOutput.accept(createOrientableTrapdoor(trapdoor, top, plainVariant(bottom), open));
/* 1156 */     registerSimpleItemModel(trapdoor, bottom);
/*      */   }
/*      */   
/*      */   private void createTrapdoor(Block trapdoor) {
/* 1160 */     TextureMapping mapping = TextureMapping.defaultTexture(trapdoor);
/* 1161 */     MultiVariant top = plainVariant(ModelTemplates.TRAPDOOR_TOP.create(trapdoor, mapping, this.modelOutput));
/* 1162 */     Identifier bottom = ModelTemplates.TRAPDOOR_BOTTOM.create(trapdoor, mapping, this.modelOutput);
/* 1163 */     MultiVariant open = plainVariant(ModelTemplates.TRAPDOOR_OPEN.create(trapdoor, mapping, this.modelOutput));
/* 1165 */     this.blockStateOutput.accept(createTrapdoor(trapdoor, top, plainVariant(bottom), open));
/* 1166 */     registerSimpleItemModel(trapdoor, bottom);
/*      */   }
/*      */   
/*      */   private void copyTrapdoorModel(Block donor, Block acceptor) {
/* 1170 */     MultiVariant top = plainVariant(ModelTemplates.TRAPDOOR_TOP.getDefaultModelLocation(donor));
/* 1171 */     MultiVariant bottom = plainVariant(ModelTemplates.TRAPDOOR_BOTTOM.getDefaultModelLocation(donor));
/* 1172 */     MultiVariant open = plainVariant(ModelTemplates.TRAPDOOR_OPEN.getDefaultModelLocation(donor));
/* 1174 */     this.itemModelOutput.copy(donor.asItem(), acceptor.asItem());
/* 1175 */     this.blockStateOutput.accept(createTrapdoor(acceptor, top, bottom, open));
/*      */   }
/*      */   
/*      */   private void createBigDripLeafBlock() {
/* 1179 */     MultiVariant noTilt = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BIG_DRIPLEAF));
/* 1180 */     MultiVariant partialTilt = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BIG_DRIPLEAF, "_partial_tilt"));
/* 1181 */     MultiVariant fullTilt = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BIG_DRIPLEAF, "_full_tilt"));
/* 1183 */     this.blockStateOutput.accept(
/* 1184 */         MultiVariantGenerator.dispatch(Blocks.BIG_DRIPLEAF)
/* 1185 */         .with(
/* 1186 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.TILT)
/* 1187 */           .select((Comparable)Tilt.NONE, noTilt)
/* 1188 */           .select((Comparable)Tilt.UNSTABLE, noTilt)
/* 1189 */           .select((Comparable)Tilt.PARTIAL, partialTilt)
/* 1190 */           .select((Comparable)Tilt.FULL, fullTilt))
/*      */         
/* 1192 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */   
/*      */   private class WoodProvider {
/*      */     private final TextureMapping logMapping;
/*      */     
/*      */     public WoodProvider(TextureMapping logMapping) {
/* 1200 */       this.logMapping = logMapping;
/*      */     }
/*      */     
/*      */     public WoodProvider wood(Block block) {
/* 1204 */       TextureMapping woodMapping = this.logMapping.copyAndUpdate(TextureSlot.END, this.logMapping.get(TextureSlot.SIDE));
/* 1205 */       Identifier model = ModelTemplates.CUBE_COLUMN.create(block, woodMapping, BlockModelGenerators.this.modelOutput);
/* 1206 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(block, BlockModelGenerators.plainVariant(model)));
/* 1207 */       BlockModelGenerators.this.registerSimpleItemModel(block, model);
/* 1208 */       return this;
/*      */     }
/*      */     
/*      */     public WoodProvider log(Block block) {
/* 1212 */       Identifier model = ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, BlockModelGenerators.this.modelOutput);
/* 1213 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(block, BlockModelGenerators.plainVariant(model)));
/* 1214 */       BlockModelGenerators.this.registerSimpleItemModel(block, model);
/* 1215 */       return this;
/*      */     }
/*      */     
/*      */     public WoodProvider logWithHorizontal(Block block) {
/* 1219 */       Identifier model = ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, BlockModelGenerators.this.modelOutput);
/* 1220 */       MultiVariant horizontalModel = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(block, this.logMapping, BlockModelGenerators.this.modelOutput));
/* 1221 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createRotatedPillarWithHorizontalVariant(block, BlockModelGenerators.plainVariant(model), horizontalModel));
/* 1222 */       BlockModelGenerators.this.registerSimpleItemModel(block, model);
/* 1223 */       return this;
/*      */     }
/*      */     
/*      */     public WoodProvider logUVLocked(Block block) {
/* 1227 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createPillarBlockUVLocked(block, this.logMapping, BlockModelGenerators.this.modelOutput));
/* 1228 */       BlockModelGenerators.this.registerSimpleItemModel(block, ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, BlockModelGenerators.this.modelOutput));
/* 1229 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   private WoodProvider woodProvider(Block log) {
/* 1234 */     return new WoodProvider(TextureMapping.logColumn(log));
/*      */   }
/*      */   
/*      */   private void createNonTemplateModelBlock(Block block) {
/* 1238 */     createNonTemplateModelBlock(block, block);
/*      */   }
/*      */   
/*      */   private void createNonTemplateModelBlock(Block block, Block donor) {
/* 1242 */     this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelLocationUtils.getModelLocation(donor))));
/*      */   }
/*      */   
/*      */   private enum PlantType {
/* 1246 */     TINTED((String)ModelTemplates.TINTED_CROSS, ModelTemplates.TINTED_FLOWER_POT_CROSS, false),
/* 1247 */     NOT_TINTED((String)ModelTemplates.CROSS, ModelTemplates.FLOWER_POT_CROSS, false),
/* 1248 */     EMISSIVE_NOT_TINTED((String)ModelTemplates.CROSS_EMISSIVE, ModelTemplates.FLOWER_POT_CROSS_EMISSIVE, true);
/*      */     
/*      */     private final ModelTemplate blockTemplate;
/*      */     
/*      */     private final ModelTemplate flowerPotTemplate;
/*      */     
/*      */     private final boolean isEmissive;
/*      */     
/*      */     PlantType(ModelTemplate blockTemplate, ModelTemplate flowerPotTemplate, boolean isEmissive) {
/* 1256 */       this.blockTemplate = blockTemplate;
/* 1257 */       this.flowerPotTemplate = flowerPotTemplate;
/* 1258 */       this.isEmissive = isEmissive;
/*      */     }
/*      */     
/*      */     public ModelTemplate getCross() {
/* 1262 */       return this.blockTemplate;
/*      */     }
/*      */     
/*      */     public ModelTemplate getCrossPot() {
/* 1266 */       return this.flowerPotTemplate;
/*      */     }
/*      */     
/*      */     public Identifier createItemModel(BlockModelGenerators generator, Block block) {
/* 1270 */       Item blockItem = block.asItem();
/* 1271 */       if (this.isEmissive)
/* 1272 */         return generator.createFlatItemModelWithBlockTextureAndOverlay(blockItem, block, "_emissive"); 
/* 1274 */       return generator.createFlatItemModelWithBlockTexture(blockItem, block);
/*      */     }
/*      */     
/*      */     public TextureMapping getTextureMapping(Block block) {
/* 1279 */       return this.isEmissive ? TextureMapping.crossEmissive(block) : TextureMapping.cross(block);
/*      */     }
/*      */     
/*      */     public TextureMapping getPlantTextureMapping(Block standAlone) {
/* 1283 */       return this.isEmissive ? TextureMapping.plantEmissive(standAlone) : TextureMapping.plant(standAlone);
/*      */     }
/*      */   }
/*      */   
/*      */   private void createCrossBlockWithDefaultItem(Block block, PlantType plantType) {
/* 1288 */     registerSimpleItemModel(block.asItem(), plantType.createItemModel(this, block));
/* 1289 */     createCrossBlock(block, plantType);
/*      */   }
/*      */   
/*      */   private void createCrossBlockWithDefaultItem(Block block, PlantType plantType, TextureMapping textures) {
/* 1293 */     registerSimpleFlatItemModel(block);
/* 1294 */     createCrossBlock(block, plantType, textures);
/*      */   }
/*      */   
/*      */   private void createCrossBlock(Block block, PlantType plantType) {
/* 1298 */     TextureMapping textures = plantType.getTextureMapping(block);
/* 1299 */     createCrossBlock(block, plantType, textures);
/*      */   }
/*      */   
/*      */   private void createCrossBlock(Block block, PlantType plantType, TextureMapping textures) {
/* 1303 */     MultiVariant model = plainVariant(plantType.getCross().create(block, textures, this.modelOutput));
/* 1304 */     this.blockStateOutput.accept(createSimpleBlock(block, model));
/*      */   }
/*      */   
/*      */   private void createCrossBlock(Block block, PlantType plantType, Property<Integer> property, int... stages) {
/* 1308 */     if (property.getPossibleValues().size() != stages.length)
/* 1309 */       throw new IllegalArgumentException("missing values for property: " + String.valueOf(property)); 
/* 1312 */     registerSimpleFlatItemModel(block.asItem());
/* 1313 */     this.blockStateOutput.accept(
/* 1314 */         MultiVariantGenerator.dispatch(block)
/* 1315 */         .with(PropertyDispatch.initial(property)
/* 1316 */           .generate(i -> {
/*      */               String suffix = "_stage" + stages[i.intValue()];
/*      */               TextureMapping texture = TextureMapping.cross(TextureMapping.getBlockTexture(block, suffix));
/*      */               return plainVariant(plantType.getCross().createWithSuffix(block, suffix, texture, this.modelOutput));
/*      */             })));
/*      */   }
/*      */   
/*      */   private void createPlantWithDefaultItem(Block standAlone, Block potted, PlantType plantType) {
/* 1326 */     registerSimpleItemModel(standAlone.asItem(), plantType.createItemModel(this, standAlone));
/* 1327 */     createPlant(standAlone, potted, plantType);
/*      */   }
/*      */   
/*      */   private void createPlant(Block standAlone, Block potted, PlantType plantType) {
/* 1331 */     createCrossBlock(standAlone, plantType);
/* 1333 */     TextureMapping textures = plantType.getPlantTextureMapping(standAlone);
/* 1334 */     MultiVariant model = plainVariant(plantType.getCrossPot().create(potted, textures, this.modelOutput));
/* 1335 */     this.blockStateOutput.accept(createSimpleBlock(potted, model));
/*      */   }
/*      */   
/*      */   private void createCoralFans(Block fan, Block wallFan) {
/* 1339 */     TexturedModel fanTemplate = TexturedModel.CORAL_FAN.get(fan);
/* 1341 */     MultiVariant fanModel = plainVariant(fanTemplate.create(fan, this.modelOutput));
/* 1342 */     this.blockStateOutput.accept(createSimpleBlock(fan, fanModel));
/* 1344 */     MultiVariant wallFanModel = plainVariant(ModelTemplates.CORAL_WALL_FAN.create(wallFan, fanTemplate.getMapping(), this.modelOutput));
/* 1345 */     this.blockStateOutput.accept(MultiVariantGenerator.dispatch(wallFan, wallFanModel).with(ROTATION_HORIZONTAL_FACING));
/* 1347 */     registerSimpleFlatItemModel(fan);
/*      */   }
/*      */   
/*      */   private void createStems(Block growingStem, Block attachedStem) {
/* 1351 */     registerSimpleFlatItemModel(growingStem.asItem());
/* 1352 */     TextureMapping growingMapping = TextureMapping.stem(growingStem);
/* 1353 */     TextureMapping attachedMapping = TextureMapping.attachedStem(growingStem, attachedStem);
/* 1355 */     MultiVariant attachedStemModel = plainVariant(ModelTemplates.ATTACHED_STEM.create(attachedStem, attachedMapping, this.modelOutput));
/* 1356 */     this.blockStateOutput.accept(
/* 1357 */         MultiVariantGenerator.dispatch(attachedStem, attachedStemModel)
/* 1358 */         .with((PropertyDispatch)PropertyDispatch.modify((Property)BlockStateProperties.HORIZONTAL_FACING)
/* 1359 */           .select((Comparable)Direction.WEST, NOP)
/* 1360 */           .select((Comparable)Direction.SOUTH, Y_ROT_270)
/* 1361 */           .select((Comparable)Direction.NORTH, Y_ROT_90)
/* 1362 */           .select((Comparable)Direction.EAST, Y_ROT_180)));
/* 1365 */     this.blockStateOutput.accept(
/* 1366 */         MultiVariantGenerator.dispatch(growingStem)
/* 1367 */         .with(
/* 1368 */           PropertyDispatch.initial((Property)BlockStateProperties.AGE_7)
/* 1369 */           .generate(i -> plainVariant(ModelTemplates.STEMS[i.intValue()].create(growingStem, growingMapping, this.modelOutput)))));
/*      */   }
/*      */   
/*      */   private void createPitcherPlant() {
/* 1375 */     Block block = Blocks.PITCHER_PLANT;
/* 1376 */     registerSimpleFlatItemModel(block.asItem());
/* 1377 */     MultiVariant topModel = plainVariant(ModelLocationUtils.getModelLocation(block, "_top"));
/* 1378 */     MultiVariant bottomModel = plainVariant(ModelLocationUtils.getModelLocation(block, "_bottom"));
/* 1379 */     createDoubleBlock(block, topModel, bottomModel);
/*      */   }
/*      */   
/*      */   private void createPitcherCrop() {
/* 1383 */     Block block = Blocks.PITCHER_CROP;
/* 1385 */     registerSimpleFlatItemModel(block.asItem());
/* 1387 */     this.blockStateOutput.accept(
/* 1388 */         MultiVariantGenerator.dispatch(block)
/* 1389 */         .with(
/* 1390 */           PropertyDispatch.initial((Property)PitcherCropBlock.AGE, (Property)BlockStateProperties.DOUBLE_BLOCK_HALF)
/* 1391 */           .generate((age, shape) -> {
/*      */               switch (shape) {
/*      */                 default:
/*      */                   throw new MatchException(null, null);
/*      */                 case DOWN_NORTH:
/*      */                 
/*      */                 case DOWN_SOUTH:
/*      */                   break;
/*      */               } 
/*      */               return plainVariant(ModelLocationUtils.getModelLocation(block, "_bottom_stage_" + age));
/*      */             })));
/*      */   }
/*      */   
/*      */   private void createCoral(Block plant, Block deadPlant, Block block, Block deadBlock, Block fan, Block deadFan, Block wallFan, Block deadWallFan) {
/* 1401 */     createCrossBlockWithDefaultItem(plant, PlantType.NOT_TINTED);
/* 1402 */     createCrossBlockWithDefaultItem(deadPlant, PlantType.NOT_TINTED);
/* 1404 */     createTrivialCube(block);
/* 1405 */     createTrivialCube(deadBlock);
/* 1407 */     createCoralFans(fan, wallFan);
/* 1408 */     createCoralFans(deadFan, deadWallFan);
/*      */   }
/*      */   
/*      */   private void createDoublePlant(Block block, PlantType plantType) {
/* 1412 */     MultiVariant topModel = plainVariant(createSuffixedVariant(block, "_top", plantType.getCross(), TextureMapping::cross));
/* 1413 */     MultiVariant bottomModel = plainVariant(createSuffixedVariant(block, "_bottom", plantType.getCross(), TextureMapping::cross));
/* 1414 */     createDoubleBlock(block, topModel, bottomModel);
/*      */   }
/*      */   
/*      */   private void createDoublePlantWithDefaultItem(Block block, PlantType plantType) {
/* 1418 */     registerSimpleFlatItemModel(block, "_top");
/* 1419 */     createDoublePlant(block, plantType);
/*      */   }
/*      */   
/*      */   private void createTintedDoublePlant(Block block) {
/* 1423 */     Identifier itemModel = createFlatItemModelWithBlockTexture(block.asItem(), block, "_top");
/* 1424 */     registerSimpleTintedItemModel(block, itemModel, (ItemTintSource)new GrassColorSource());
/* 1425 */     createDoublePlant(block, PlantType.TINTED);
/*      */   }
/*      */   
/*      */   private void createSunflower() {
/* 1429 */     registerSimpleFlatItemModel(Blocks.SUNFLOWER, "_front");
/* 1430 */     MultiVariant topModel = plainVariant(ModelLocationUtils.getModelLocation(Blocks.SUNFLOWER, "_top"));
/* 1431 */     MultiVariant bottomModel = plainVariant(createSuffixedVariant(Blocks.SUNFLOWER, "_bottom", PlantType.NOT_TINTED.getCross(), TextureMapping::cross));
/* 1432 */     createDoubleBlock(Blocks.SUNFLOWER, topModel, bottomModel);
/*      */   }
/*      */   
/*      */   private void createTallSeagrass() {
/* 1436 */     MultiVariant topModel = plainVariant(createSuffixedVariant(Blocks.TALL_SEAGRASS, "_top", ModelTemplates.SEAGRASS, TextureMapping::defaultTexture));
/* 1437 */     MultiVariant bottomModel = plainVariant(createSuffixedVariant(Blocks.TALL_SEAGRASS, "_bottom", ModelTemplates.SEAGRASS, TextureMapping::defaultTexture));
/* 1438 */     createDoubleBlock(Blocks.TALL_SEAGRASS, topModel, bottomModel);
/*      */   }
/*      */   
/*      */   private void createSmallDripleaf() {
/* 1442 */     MultiVariant topModel = plainVariant(ModelLocationUtils.getModelLocation(Blocks.SMALL_DRIPLEAF, "_top"));
/* 1443 */     MultiVariant bottomModel = plainVariant(ModelLocationUtils.getModelLocation(Blocks.SMALL_DRIPLEAF, "_bottom"));
/* 1444 */     this.blockStateOutput.accept(
/* 1445 */         MultiVariantGenerator.dispatch(Blocks.SMALL_DRIPLEAF)
/* 1446 */         .with(
/* 1447 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.DOUBLE_BLOCK_HALF)
/* 1448 */           .select((Comparable)DoubleBlockHalf.LOWER, bottomModel)
/* 1449 */           .select((Comparable)DoubleBlockHalf.UPPER, topModel))
/*      */         
/* 1451 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */   
/*      */   private void createDoubleBlock(Block block, MultiVariant topModel, MultiVariant bottomModel) {
/* 1456 */     this.blockStateOutput.accept(
/* 1457 */         MultiVariantGenerator.dispatch(block)
/* 1458 */         .with(
/* 1459 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.DOUBLE_BLOCK_HALF)
/* 1460 */           .select((Comparable)DoubleBlockHalf.LOWER, bottomModel)
/* 1461 */           .select((Comparable)DoubleBlockHalf.UPPER, topModel)));
/*      */   }
/*      */   
/*      */   private void createPassiveRail(Block block) {
/* 1467 */     TextureMapping texture = TextureMapping.rail(block);
/* 1468 */     TextureMapping cornerTexture = TextureMapping.rail(TextureMapping.getBlockTexture(block, "_corner"));
/* 1470 */     MultiVariant flat = plainVariant(ModelTemplates.RAIL_FLAT.create(block, texture, this.modelOutput));
/* 1471 */     MultiVariant curved = plainVariant(ModelTemplates.RAIL_CURVED.create(block, cornerTexture, this.modelOutput));
/* 1472 */     MultiVariant risingNE = plainVariant(ModelTemplates.RAIL_RAISED_NE.create(block, texture, this.modelOutput));
/* 1473 */     MultiVariant risingSW = plainVariant(ModelTemplates.RAIL_RAISED_SW.create(block, texture, this.modelOutput));
/* 1475 */     registerSimpleFlatItemModel(block);
/* 1476 */     this.blockStateOutput.accept(
/* 1477 */         MultiVariantGenerator.dispatch(block)
/* 1478 */         .with(
/* 1479 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.RAIL_SHAPE)
/* 1480 */           .select((Comparable)RailShape.NORTH_SOUTH, flat)
/* 1481 */           .select((Comparable)RailShape.EAST_WEST, flat.with(Y_ROT_90))
/*      */           
/* 1483 */           .select((Comparable)RailShape.ASCENDING_EAST, risingNE.with(Y_ROT_90))
/* 1484 */           .select((Comparable)RailShape.ASCENDING_WEST, risingSW.with(Y_ROT_90))
/* 1485 */           .select((Comparable)RailShape.ASCENDING_NORTH, risingNE)
/* 1486 */           .select((Comparable)RailShape.ASCENDING_SOUTH, risingSW)
/*      */           
/* 1488 */           .select((Comparable)RailShape.SOUTH_EAST, curved)
/* 1489 */           .select((Comparable)RailShape.SOUTH_WEST, curved.with(Y_ROT_90))
/* 1490 */           .select((Comparable)RailShape.NORTH_WEST, curved.with(Y_ROT_180))
/* 1491 */           .select((Comparable)RailShape.NORTH_EAST, curved.with(Y_ROT_270))));
/*      */   }
/*      */   
/*      */   private void createActiveRail(Block block) {
/* 1497 */     MultiVariant flat = plainVariant(createSuffixedVariant(block, "", ModelTemplates.RAIL_FLAT, TextureMapping::rail));
/* 1498 */     MultiVariant risingNE = plainVariant(createSuffixedVariant(block, "", ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail));
/* 1499 */     MultiVariant risingSW = plainVariant(createSuffixedVariant(block, "", ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
/* 1501 */     MultiVariant flatOn = plainVariant(createSuffixedVariant(block, "_on", ModelTemplates.RAIL_FLAT, TextureMapping::rail));
/* 1502 */     MultiVariant risingNEOn = plainVariant(createSuffixedVariant(block, "_on", ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail));
/* 1503 */     MultiVariant risingSWOn = plainVariant(createSuffixedVariant(block, "_on", ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
/* 1505 */     registerSimpleFlatItemModel(block);
/* 1506 */     this.blockStateOutput.accept(
/* 1507 */         MultiVariantGenerator.dispatch(block)
/* 1508 */         .with(
/* 1509 */           PropertyDispatch.initial((Property)BlockStateProperties.POWERED, (Property)BlockStateProperties.RAIL_SHAPE_STRAIGHT)
/* 1510 */           .generate((powered, railShape) -> {
/*      */               switch (railShape) {
/*      */                 case DOWN_NORTH:
/*      */                   return powered.booleanValue() ? flatOn : flat;
/*      */                 case DOWN_SOUTH:
/*      */                 
/*      */                 case DOWN_WEST:
/*      */                 
/*      */                 case DOWN_EAST:
/*      */                 
/*      */                 case UP_NORTH:
/*      */                   return powered.booleanValue() ? risingNEOn : risingNE;
/*      */                 case UP_SOUTH:
/*      */                   return powered.booleanValue() ? risingSWOn : risingSW;
/*      */               } 
/*      */               throw new UnsupportedOperationException("Fix you generator!");
/*      */             })));
/*      */   }
/*      */   
/*      */   private void createAirLikeBlock(Block block, Item particleItem) {
/* 1524 */     MultiVariant dummyModel = plainVariant(ModelTemplates.PARTICLE_ONLY.create(block, TextureMapping.particleFromItem(particleItem), this.modelOutput));
/* 1525 */     this.blockStateOutput.accept(createSimpleBlock(block, dummyModel));
/*      */   }
/*      */   
/*      */   private void createAirLikeBlock(Block block, Material particle) {
/* 1529 */     MultiVariant dummyModel = plainVariant(ModelTemplates.PARTICLE_ONLY.create(block, TextureMapping.particle(particle), this.modelOutput));
/* 1530 */     this.blockStateOutput.accept(createSimpleBlock(block, dummyModel));
/*      */   }
/*      */   
/*      */   private MultiVariant createParticleOnlyBlockModel(Block block, Block particleDonor) {
/* 1535 */     return plainVariant(ModelTemplates.PARTICLE_ONLY.create(block, TextureMapping.particle(particleDonor), this.modelOutput));
/*      */   }
/*      */   
/*      */   public void createParticleOnlyBlock(Block block, Block particleDonor) {
/* 1540 */     this.blockStateOutput.accept(createSimpleBlock(block, createParticleOnlyBlockModel(block, particleDonor)));
/*      */   }
/*      */   
/*      */   private void createParticleOnlyBlock(Block block) {
/* 1544 */     createParticleOnlyBlock(block, block);
/*      */   }
/*      */   
/*      */   private void createFullAndCarpetBlocks(Block block, Block carpet) {
/* 1548 */     createTrivialCube(block);
/* 1551 */     MultiVariant model = plainVariant(TexturedModel.CARPET.get(block).create(carpet, this.modelOutput));
/* 1552 */     this.blockStateOutput.accept(createSimpleBlock(carpet, model));
/*      */   }
/*      */   
/*      */   private void createLeafLitter(Block block) {
/* 1556 */     MultiVariant model1 = plainVariant(TexturedModel.LEAF_LITTER_1.create(block, this.modelOutput));
/* 1557 */     MultiVariant model2 = plainVariant(TexturedModel.LEAF_LITTER_2.create(block, this.modelOutput));
/* 1558 */     MultiVariant model3 = plainVariant(TexturedModel.LEAF_LITTER_3.create(block, this.modelOutput));
/* 1559 */     MultiVariant model4 = plainVariant(TexturedModel.LEAF_LITTER_4.create(block, this.modelOutput));
/* 1560 */     registerSimpleFlatItemModel(block.asItem());
/* 1561 */     createSegmentedBlock(block, model1, LEAF_LITTER_MODEL_1_SEGMENT_CONDITION, model2, LEAF_LITTER_MODEL_2_SEGMENT_CONDITION, model3, LEAF_LITTER_MODEL_3_SEGMENT_CONDITION, model4, LEAF_LITTER_MODEL_4_SEGMENT_CONDITION);
/*      */   }
/*      */   
/*      */   private void createFlowerBed(Block flowerbed) {
/* 1565 */     MultiVariant model1 = plainVariant(TexturedModel.FLOWERBED_1.create(flowerbed, this.modelOutput));
/* 1566 */     MultiVariant model2 = plainVariant(TexturedModel.FLOWERBED_2.create(flowerbed, this.modelOutput));
/* 1567 */     MultiVariant model3 = plainVariant(TexturedModel.FLOWERBED_3.create(flowerbed, this.modelOutput));
/* 1568 */     MultiVariant model4 = plainVariant(TexturedModel.FLOWERBED_4.create(flowerbed, this.modelOutput));
/* 1569 */     registerSimpleFlatItemModel(flowerbed.asItem());
/* 1570 */     createSegmentedBlock(flowerbed, model1, FLOWER_BED_MODEL_1_SEGMENT_CONDITION, model2, FLOWER_BED_MODEL_2_SEGMENT_CONDITION, model3, FLOWER_BED_MODEL_3_SEGMENT_CONDITION, model4, FLOWER_BED_MODEL_4_SEGMENT_CONDITION);
/*      */   }
/*      */   
/*      */   private void createSegmentedBlock(Block segmentedProperty, MultiVariant model1, Function<ConditionBuilder, ConditionBuilder> model1SegmentCondition, MultiVariant model2, Function<ConditionBuilder, ConditionBuilder> model2SegmentCondition, MultiVariant model3, Function<ConditionBuilder, ConditionBuilder> model3SegmentCondition, MultiVariant model4, Function<ConditionBuilder, ConditionBuilder> model4SegmentCondition) {
/* 1578 */     this.blockStateOutput.accept(
/* 1579 */         MultiPartGenerator.multiPart(segmentedProperty)
/* 1580 */         .with(model1SegmentCondition
/* 1581 */           .apply(condition()
/* 1582 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.NORTH)), model1)
/*      */         
/* 1585 */         .with(model1SegmentCondition
/* 1586 */           .apply(condition()
/* 1587 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.EAST)), model1
/* 1588 */           .with(Y_ROT_90))
/*      */         
/* 1590 */         .with(model1SegmentCondition
/* 1591 */           .apply(condition()
/* 1592 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.SOUTH)), model1
/* 1593 */           .with(Y_ROT_180))
/*      */         
/* 1595 */         .with(model1SegmentCondition
/* 1596 */           .apply(condition()
/* 1597 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.WEST)), model1
/* 1598 */           .with(Y_ROT_270))
/*      */         
/* 1600 */         .with(model2SegmentCondition
/* 1601 */           .apply(condition()
/* 1602 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.NORTH)), model2)
/*      */         
/* 1605 */         .with(model2SegmentCondition
/* 1606 */           .apply(condition()
/* 1607 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.EAST)), model2
/* 1608 */           .with(Y_ROT_90))
/*      */         
/* 1610 */         .with(model2SegmentCondition
/* 1611 */           .apply(condition()
/* 1612 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.SOUTH)), model2
/* 1613 */           .with(Y_ROT_180))
/*      */         
/* 1615 */         .with(model2SegmentCondition
/* 1616 */           .apply(condition()
/* 1617 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.WEST)), model2
/* 1618 */           .with(Y_ROT_270))
/*      */         
/* 1620 */         .with(model3SegmentCondition
/* 1621 */           .apply(condition()
/* 1622 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.NORTH)), model3)
/*      */         
/* 1625 */         .with(model3SegmentCondition
/* 1626 */           .apply(condition()
/* 1627 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.EAST)), model3
/* 1628 */           .with(Y_ROT_90))
/*      */         
/* 1630 */         .with(model3SegmentCondition
/* 1631 */           .apply(condition()
/* 1632 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.SOUTH)), model3
/* 1633 */           .with(Y_ROT_180))
/*      */         
/* 1635 */         .with(model3SegmentCondition
/* 1636 */           .apply(condition()
/* 1637 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.WEST)), model3
/* 1638 */           .with(Y_ROT_270))
/*      */         
/* 1640 */         .with(model4SegmentCondition
/* 1641 */           .apply(condition()
/* 1642 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.NORTH)), model4)
/*      */         
/* 1645 */         .with(model4SegmentCondition
/* 1646 */           .apply(condition()
/* 1647 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.EAST)), model4
/* 1648 */           .with(Y_ROT_90))
/*      */         
/* 1650 */         .with(model4SegmentCondition
/* 1651 */           .apply(condition()
/* 1652 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.SOUTH)), model4
/* 1653 */           .with(Y_ROT_180))
/*      */         
/* 1655 */         .with(model4SegmentCondition
/* 1656 */           .apply(condition()
/* 1657 */             .term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.WEST)), model4
/* 1658 */           .with(Y_ROT_270)));
/*      */   }
/*      */   
/*      */   private void createColoredBlockWithRandomRotations(TexturedModel.Provider modelProvider, List<Block> blocks) {
/* 1664 */     for (Block block : blocks) {
/* 1665 */       Variant model = plainModel(modelProvider.create(block, this.modelOutput));
/* 1666 */       this.blockStateOutput.accept(
/* 1667 */           MultiVariantGenerator.dispatch(block, createRotatedVariants(model)));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void createColoredBlockWithStateRotations(TexturedModel.Provider modelProvider, List<Block> blocks) {
/* 1673 */     for (Block block : blocks) {
/* 1674 */       MultiVariant model = plainVariant(modelProvider.create(block, this.modelOutput));
/* 1675 */       this.blockStateOutput.accept(
/* 1676 */           MultiVariantGenerator.dispatch(block, model)
/* 1677 */           .with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void createGlassBlocks(Block block, Block pane) {
/* 1683 */     createTrivialBlock(block, TexturedModel.CUBE.updateTexture(mapping -> mapping.forceAllTranslucent()));
/* 1685 */     TextureMapping paneMapping = TextureMapping.pane(block, pane).forceAllTranslucent();
/* 1686 */     MultiVariant post = plainVariant(ModelTemplates.STAINED_GLASS_PANE_POST.create(pane, paneMapping, this.modelOutput));
/* 1687 */     MultiVariant side = plainVariant(ModelTemplates.STAINED_GLASS_PANE_SIDE.create(pane, paneMapping, this.modelOutput));
/* 1688 */     MultiVariant sideAlt = plainVariant(ModelTemplates.STAINED_GLASS_PANE_SIDE_ALT.create(pane, paneMapping, this.modelOutput));
/* 1689 */     MultiVariant noSide = plainVariant(ModelTemplates.STAINED_GLASS_PANE_NOSIDE.create(pane, paneMapping, this.modelOutput));
/* 1690 */     MultiVariant noSideAlt = plainVariant(ModelTemplates.STAINED_GLASS_PANE_NOSIDE_ALT.create(pane, paneMapping, this.modelOutput));
/* 1692 */     Item paneItem = pane.asItem();
/* 1693 */     registerSimpleItemModel(paneItem, createFlatItemModelWithBlockTexture(paneItem, block));
/* 1695 */     this.blockStateOutput.accept(
/* 1696 */         MultiPartGenerator.multiPart(pane)
/* 1697 */         .with(post)
/* 1698 */         .with(condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true)), side)
/* 1699 */         .with(condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(true)), side.with(Y_ROT_90))
/* 1700 */         .with(condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true)), sideAlt)
/* 1701 */         .with(condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), sideAlt.with(Y_ROT_90))
/*      */         
/* 1703 */         .with(condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false)), noSide)
/* 1704 */         .with(condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(false)), noSideAlt)
/* 1705 */         .with(condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false)), noSideAlt.with(Y_ROT_90))
/* 1706 */         .with(condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), noSide.with(Y_ROT_270)));
/*      */   }
/*      */   
/*      */   private void createCommandBlock(Block block) {
/* 1711 */     TextureMapping normalTextures = TextureMapping.commandBlock(block);
/* 1713 */     MultiVariant normalModel = plainVariant(ModelTemplates.COMMAND_BLOCK.create(block, normalTextures, this.modelOutput));
/* 1714 */     MultiVariant conditionalModel = plainVariant(createSuffixedVariant(block, "_conditional", ModelTemplates.COMMAND_BLOCK, id -> normalTextures.copyAndUpdate(TextureSlot.SIDE, id)));
/* 1716 */     this.blockStateOutput.accept(
/* 1717 */         MultiVariantGenerator.dispatch(block)
/* 1718 */         .with(createBooleanModelDispatch(BlockStateProperties.CONDITIONAL, conditionalModel, normalModel))
/* 1719 */         .with(ROTATION_FACING));
/*      */   }
/*      */   
/*      */   private void createAnvil(Block block) {
/* 1724 */     MultiVariant anvilModel = plainVariant(TexturedModel.ANVIL.create(block, this.modelOutput));
/* 1725 */     this.blockStateOutput.accept(
/* 1726 */         createSimpleBlock(block, anvilModel)
/* 1727 */         .with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */   }
/*      */   
/*      */   private static MultiVariant createBambooModels(int age) {
/* 1732 */     String ageSuffix = "_age" + age;
/* 1733 */     return new MultiVariant(WeightedList.of((List)IntStream.range(1, 5)
/* 1734 */           .mapToObj(i -> new Weighted(plainModel(ModelLocationUtils.getModelLocation(Blocks.BAMBOO, "" + i + i)), 1))
/* 1735 */           .collect(Collectors.toList())));
/*      */   }
/*      */   
/*      */   private void createBamboo() {
/* 1739 */     this.blockStateOutput.accept(
/* 1740 */         MultiPartGenerator.multiPart(Blocks.BAMBOO)
/* 1741 */         .with(condition().term((Property)BlockStateProperties.AGE_1, Integer.valueOf(0)), createBambooModels(0))
/* 1742 */         .with(condition().term((Property)BlockStateProperties.AGE_1, Integer.valueOf(1)), createBambooModels(1))
/* 1743 */         .with(condition().term((Property)BlockStateProperties.BAMBOO_LEAVES, (Comparable)BambooLeaves.SMALL), plainVariant(ModelLocationUtils.getModelLocation(Blocks.BAMBOO, "_small_leaves")))
/* 1744 */         .with(condition().term((Property)BlockStateProperties.BAMBOO_LEAVES, (Comparable)BambooLeaves.LARGE), plainVariant(ModelLocationUtils.getModelLocation(Blocks.BAMBOO, "_large_leaves"))));
/*      */   }
/*      */   
/*      */   private void createBarrel() {
/* 1749 */     Material openTop = TextureMapping.getBlockTexture(Blocks.BARREL, "_top_open");
/* 1751 */     MultiVariant closedModel = plainVariant(TexturedModel.CUBE_TOP_BOTTOM.create(Blocks.BARREL, this.modelOutput));
/* 1752 */     MultiVariant openModel = plainVariant(TexturedModel.CUBE_TOP_BOTTOM.get(Blocks.BARREL).updateTextures(t -> t.put(TextureSlot.TOP, openTop)).createWithSuffix(Blocks.BARREL, "_open", this.modelOutput));
/* 1753 */     this.blockStateOutput.accept(
/* 1754 */         MultiVariantGenerator.dispatch(Blocks.BARREL)
/* 1755 */         .with(
/* 1756 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.OPEN)
/* 1757 */           .select(Boolean.valueOf(false), closedModel)
/* 1758 */           .select(Boolean.valueOf(true), openModel))
/*      */         
/* 1760 */         .with(ROTATIONS_COLUMN_WITH_FACING));
/*      */   }
/*      */   
/*      */   private static <T extends Comparable<T>> PropertyDispatch<MultiVariant> createEmptyOrFullDispatch(Property<T> property, T threshold, MultiVariant fullModel, MultiVariant emptyModel) {
/* 1765 */     return PropertyDispatch.initial(property)
/* 1766 */       .generate(value -> {
/*      */           boolean isFull = (value.compareTo(threshold) >= 0);
/*      */           return isFull ? fullModel : emptyModel;
/*      */         });
/*      */   }
/*      */   
/*      */   private void createBeeNest(Block block, Function<Block, TextureMapping> mappingFunction) {
/* 1773 */     TextureMapping emptyMapping = ((TextureMapping)mappingFunction.apply(block)).copyForced(TextureSlot.SIDE, TextureSlot.PARTICLE);
/* 1774 */     TextureMapping fullMapping = emptyMapping.copyAndUpdate(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front_honey"));
/* 1776 */     Identifier emptyModel = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.createWithSuffix(block, "_empty", emptyMapping, this.modelOutput);
/* 1777 */     Identifier fullModel = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.createWithSuffix(block, "_honey", fullMapping, this.modelOutput);
/* 1779 */     this.itemModelOutput.accept(block.asItem(), ItemModelUtils.selectBlockItemProperty((Property)BeehiveBlock.HONEY_LEVEL, 
/*      */           
/* 1781 */           ItemModelUtils.plainModel(emptyModel), 
/* 1782 */           Map.of(
/* 1783 */             Integer.valueOf(5), ItemModelUtils.plainModel(fullModel))));
/* 1787 */     this.blockStateOutput.accept(
/* 1788 */         MultiVariantGenerator.dispatch(block)
/* 1789 */         .with(createEmptyOrFullDispatch((Property<Integer>)BeehiveBlock.HONEY_LEVEL, Integer.valueOf(5), plainVariant(fullModel), plainVariant(emptyModel)))
/* 1790 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */   
/*      */   private void createCropBlock(Block block, Property<Integer> property, int... stages) {
/* 1795 */     registerSimpleFlatItemModel(block.asItem());
/* 1797 */     if (property.getPossibleValues().size() != stages.length)
/* 1798 */       throw new IllegalArgumentException(); 
/* 1801 */     Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();
/* 1802 */     this.blockStateOutput.accept(
/* 1803 */         MultiVariantGenerator.dispatch(block)
/* 1804 */         .with(
/* 1805 */           PropertyDispatch.initial(property)
/* 1806 */           .generate(i -> {
/*      */               int stage = stages[i.intValue()];
/*      */               return plainVariant((Identifier)models.computeIfAbsent(stage, ()));
/*      */             })));
/*      */   }
/*      */   
/*      */   private void createBell() {
/* 1815 */     MultiVariant floor = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BELL, "_floor"));
/* 1816 */     MultiVariant ceiling = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BELL, "_ceiling"));
/* 1817 */     MultiVariant wall = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BELL, "_wall"));
/* 1818 */     MultiVariant betweenWalls = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BELL, "_between_walls"));
/* 1820 */     registerSimpleFlatItemModel(Items.BELL);
/* 1821 */     this.blockStateOutput.accept(
/* 1822 */         MultiVariantGenerator.dispatch(Blocks.BELL)
/* 1823 */         .with(
/* 1824 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.BELL_ATTACHMENT)
/* 1825 */           .select((Comparable)Direction.NORTH, (Comparable)BellAttachType.FLOOR, floor)
/* 1826 */           .select((Comparable)Direction.SOUTH, (Comparable)BellAttachType.FLOOR, floor.with(Y_ROT_180))
/* 1827 */           .select((Comparable)Direction.EAST, (Comparable)BellAttachType.FLOOR, floor.with(Y_ROT_90))
/* 1828 */           .select((Comparable)Direction.WEST, (Comparable)BellAttachType.FLOOR, floor.with(Y_ROT_270))
/*      */           
/* 1830 */           .select((Comparable)Direction.NORTH, (Comparable)BellAttachType.CEILING, ceiling)
/* 1831 */           .select((Comparable)Direction.SOUTH, (Comparable)BellAttachType.CEILING, ceiling.with(Y_ROT_180))
/* 1832 */           .select((Comparable)Direction.EAST, (Comparable)BellAttachType.CEILING, ceiling.with(Y_ROT_90))
/* 1833 */           .select((Comparable)Direction.WEST, (Comparable)BellAttachType.CEILING, ceiling.with(Y_ROT_270))
/*      */           
/* 1835 */           .select((Comparable)Direction.NORTH, (Comparable)BellAttachType.SINGLE_WALL, wall.with(Y_ROT_270))
/* 1836 */           .select((Comparable)Direction.SOUTH, (Comparable)BellAttachType.SINGLE_WALL, wall.with(Y_ROT_90))
/* 1837 */           .select((Comparable)Direction.EAST, (Comparable)BellAttachType.SINGLE_WALL, wall)
/* 1838 */           .select((Comparable)Direction.WEST, (Comparable)BellAttachType.SINGLE_WALL, wall.with(Y_ROT_180))
/*      */           
/* 1840 */           .select((Comparable)Direction.SOUTH, (Comparable)BellAttachType.DOUBLE_WALL, betweenWalls.with(Y_ROT_90))
/* 1841 */           .select((Comparable)Direction.NORTH, (Comparable)BellAttachType.DOUBLE_WALL, betweenWalls.with(Y_ROT_270))
/* 1842 */           .select((Comparable)Direction.EAST, (Comparable)BellAttachType.DOUBLE_WALL, betweenWalls)
/* 1843 */           .select((Comparable)Direction.WEST, (Comparable)BellAttachType.DOUBLE_WALL, betweenWalls.with(Y_ROT_180))));
/*      */   }
/*      */   
/*      */   private void createGrindstone() {
/* 1849 */     this.blockStateOutput.accept(
/* 1850 */         MultiVariantGenerator.dispatch(Blocks.GRINDSTONE, plainVariant(ModelLocationUtils.getModelLocation(Blocks.GRINDSTONE)))
/* 1851 */         .with(
/* 1852 */           (PropertyDispatch)PropertyDispatch.modify((Property)BlockStateProperties.ATTACH_FACE, (Property)BlockStateProperties.HORIZONTAL_FACING)
/* 1853 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.NORTH, NOP)
/* 1854 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.EAST, Y_ROT_90)
/* 1855 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.SOUTH, Y_ROT_180)
/* 1856 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.WEST, Y_ROT_270)
/*      */           
/* 1858 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.NORTH, X_ROT_90)
/* 1859 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.EAST, X_ROT_90.then(Y_ROT_90))
/* 1860 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.SOUTH, X_ROT_90.then(Y_ROT_180))
/* 1861 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.WEST, X_ROT_90.then(Y_ROT_270))
/*      */           
/* 1863 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.SOUTH, X_ROT_180)
/* 1864 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.WEST, X_ROT_180.then(Y_ROT_90))
/* 1865 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.NORTH, X_ROT_180.then(Y_ROT_180))
/* 1866 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.EAST, X_ROT_180.then(Y_ROT_270))));
/*      */   }
/*      */   
/*      */   private void createFurnace(Block furnace, TexturedModel.Provider provider) {
/* 1872 */     MultiVariant normalModel = plainVariant(provider.create(furnace, this.modelOutput));
/* 1874 */     Material frontTexture = TextureMapping.getBlockTexture(furnace, "_front_on");
/* 1875 */     MultiVariant litModel = plainVariant(provider.get(furnace).updateTextures(t -> t.put(TextureSlot.FRONT, frontTexture)).createWithSuffix(furnace, "_on", this.modelOutput));
/* 1877 */     this.blockStateOutput.accept(
/* 1878 */         MultiVariantGenerator.dispatch(furnace)
/* 1879 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, litModel, normalModel))
/* 1880 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */   
/*      */   private void createCampfires(Block... campFires) {
/* 1885 */     MultiVariant offModel = plainVariant(ModelLocationUtils.decorateBlockModelLocation("campfire_off"));
/* 1887 */     for (Block campFire : campFires) {
/* 1888 */       MultiVariant litModel = plainVariant(ModelTemplates.CAMPFIRE.create(campFire, TextureMapping.campfire(campFire), this.modelOutput));
/* 1890 */       registerSimpleFlatItemModel(campFire.asItem());
/* 1891 */       this.blockStateOutput.accept(
/* 1892 */           MultiVariantGenerator.dispatch(campFire)
/* 1893 */           .with(createBooleanModelDispatch(BlockStateProperties.LIT, litModel, offModel))
/* 1894 */           .with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void createAzalea(Block block) {
/* 1900 */     MultiVariant model = plainVariant(ModelTemplates.AZALEA.create(block, TextureMapping.cubeTop(block), this.modelOutput));
/* 1901 */     this.blockStateOutput.accept(createSimpleBlock(block, model));
/*      */   }
/*      */   
/*      */   private void createPottedAzalea(Block block) {
/*      */     MultiVariant model;
/* 1906 */     if (block == Blocks.POTTED_FLOWERING_AZALEA) {
/* 1907 */       model = plainVariant(ModelTemplates.POTTED_FLOWERING_AZALEA.create(block, TextureMapping.pottedAzalea(block), this.modelOutput));
/*      */     } else {
/* 1909 */       model = plainVariant(ModelTemplates.POTTED_AZALEA.create(block, TextureMapping.pottedAzalea(block), this.modelOutput));
/*      */     } 
/* 1911 */     this.blockStateOutput.accept(createSimpleBlock(block, model));
/*      */   }
/*      */   
/*      */   private void createBookshelf() {
/* 1915 */     TextureMapping textures = TextureMapping.column(TextureMapping.getBlockTexture(Blocks.BOOKSHELF), TextureMapping.getBlockTexture(Blocks.OAK_PLANKS));
/* 1916 */     MultiVariant model = plainVariant(ModelTemplates.CUBE_COLUMN.create(Blocks.BOOKSHELF, textures, this.modelOutput));
/* 1917 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.BOOKSHELF, model));
/*      */   }
/*      */   
/*      */   private void createRedstoneWire() {
/* 1921 */     registerSimpleFlatItemModel(Items.REDSTONE);
/* 1922 */     this.blockStateOutput.accept(
/* 1923 */         MultiPartGenerator.multiPart(Blocks.REDSTONE_WIRE)
/* 1924 */         .with(
/* 1925 */           or(new ConditionBuilder[] { condition()
/* 1927 */               .term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.NONE)
/* 1928 */               .term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.NONE)
/* 1929 */               .term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.NONE)
/* 1930 */               .term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.NONE), 
/* 1931 */               condition()
/* 1932 */               .term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP }).term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP }), condition()
/* 1935 */               .term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP }).term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP }), condition()
/* 1938 */               .term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP }).term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP }), condition()
/* 1941 */               .term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP }).term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP }) }), plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_dot")))
/*      */         
/* 1946 */         .with(
/* 1947 */           condition()
/* 1948 */           .term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP }), plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side0")))
/*      */         
/* 1951 */         .with(
/* 1952 */           condition()
/* 1953 */           .term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP }), plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side_alt0")))
/*      */         
/* 1956 */         .with(
/* 1957 */           condition()
/* 1958 */           .term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP }), plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side_alt1")).with(Y_ROT_270))
/*      */         
/* 1961 */         .with(
/* 1962 */           condition()
/* 1963 */           .term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP }), plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side1")).with(Y_ROT_270))
/*      */         
/* 1966 */         .with(
/* 1967 */           condition().term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.UP), 
/* 1968 */           plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")))
/*      */         
/* 1970 */         .with(
/* 1971 */           condition().term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.UP), 
/* 1972 */           plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")).with(Y_ROT_90))
/*      */         
/* 1974 */         .with(
/* 1975 */           condition().term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.UP), 
/* 1976 */           plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")).with(Y_ROT_180))
/*      */         
/* 1978 */         .with(
/* 1979 */           condition().term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.UP), 
/* 1980 */           plainVariant(ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")).with(Y_ROT_270)));
/*      */   }
/*      */   
/*      */   private void createComparator() {
/* 1986 */     registerSimpleFlatItemModel(Items.COMPARATOR);
/* 1987 */     this.blockStateOutput.accept(
/* 1988 */         MultiVariantGenerator.dispatch(Blocks.COMPARATOR)
/* 1989 */         .with(
/* 1990 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.MODE_COMPARATOR, (Property)BlockStateProperties.POWERED)
/* 1991 */           .select((Comparable)ComparatorMode.COMPARE, Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPARATOR)))
/* 1992 */           .select((Comparable)ComparatorMode.COMPARE, Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPARATOR, "_on")))
/* 1993 */           .select((Comparable)ComparatorMode.SUBTRACT, Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPARATOR, "_subtract")))
/* 1994 */           .select((Comparable)ComparatorMode.SUBTRACT, Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPARATOR, "_on_subtract"))))
/*      */         
/* 1996 */         .with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */   }
/*      */   
/*      */   private void createSmoothStoneSlab() {
/* 2001 */     TextureMapping smoothStoneTextures = TextureMapping.cube(Blocks.SMOOTH_STONE);
/* 2002 */     TextureMapping smoothStoneSlabTextures = TextureMapping.column(
/* 2003 */         TextureMapping.getBlockTexture(Blocks.SMOOTH_STONE_SLAB, "_side"), smoothStoneTextures
/* 2004 */         .get(TextureSlot.TOP));
/* 2006 */     MultiVariant bottom = plainVariant(ModelTemplates.SLAB_BOTTOM.create(Blocks.SMOOTH_STONE_SLAB, smoothStoneSlabTextures, this.modelOutput));
/* 2007 */     MultiVariant top = plainVariant(ModelTemplates.SLAB_TOP.create(Blocks.SMOOTH_STONE_SLAB, smoothStoneSlabTextures, this.modelOutput));
/* 2008 */     MultiVariant doubleSlab = plainVariant(ModelTemplates.CUBE_COLUMN.createWithOverride(Blocks.SMOOTH_STONE_SLAB, "_double", smoothStoneSlabTextures, this.modelOutput));
/* 2009 */     this.blockStateOutput.accept(createSlab(Blocks.SMOOTH_STONE_SLAB, bottom, top, doubleSlab));
/* 2010 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.SMOOTH_STONE, plainVariant(ModelTemplates.CUBE_ALL.create(Blocks.SMOOTH_STONE, smoothStoneTextures, this.modelOutput))));
/*      */   }
/*      */   
/*      */   private void createBrewingStand() {
/* 2014 */     registerSimpleFlatItemModel(Items.BREWING_STAND);
/* 2015 */     this.blockStateOutput.accept(
/* 2016 */         MultiPartGenerator.multiPart(Blocks.BREWING_STAND)
/* 2017 */         .with(plainVariant(ModelLocationUtils.getModelLocation(Blocks.BREWING_STAND)))
/* 2018 */         .with(condition().term((Property)BlockStateProperties.HAS_BOTTLE_0, Boolean.valueOf(true)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.BREWING_STAND, "_bottle0")))
/* 2019 */         .with(condition().term((Property)BlockStateProperties.HAS_BOTTLE_1, Boolean.valueOf(true)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.BREWING_STAND, "_bottle1")))
/* 2020 */         .with(condition().term((Property)BlockStateProperties.HAS_BOTTLE_2, Boolean.valueOf(true)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.BREWING_STAND, "_bottle2")))
/*      */         
/* 2022 */         .with(condition().term((Property)BlockStateProperties.HAS_BOTTLE_0, Boolean.valueOf(false)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.BREWING_STAND, "_empty0")))
/* 2023 */         .with(condition().term((Property)BlockStateProperties.HAS_BOTTLE_1, Boolean.valueOf(false)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.BREWING_STAND, "_empty1")))
/* 2024 */         .with(condition().term((Property)BlockStateProperties.HAS_BOTTLE_2, Boolean.valueOf(false)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.BREWING_STAND, "_empty2"))));
/*      */   }
/*      */   
/*      */   private void createMushroomBlock(Block block) {
/* 2029 */     MultiVariant skin = plainVariant(ModelTemplates.SINGLE_FACE.create(block, TextureMapping.defaultTexture(block), this.modelOutput));
/* 2030 */     MultiVariant skinless = plainVariant(ModelLocationUtils.decorateBlockModelLocation("mushroom_block_inside"));
/* 2032 */     this.blockStateOutput.accept(
/* 2033 */         MultiPartGenerator.multiPart(block)
/* 2034 */         .with(condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true)), skin)
/* 2035 */         .with(condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(true)), skin.with(Y_ROT_90).with(UV_LOCK))
/* 2036 */         .with(condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true)), skin.with(Y_ROT_180).with(UV_LOCK))
/* 2037 */         .with(condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), skin.with(Y_ROT_270).with(UV_LOCK))
/* 2038 */         .with(condition().term((Property)BlockStateProperties.UP, Boolean.valueOf(true)), skin.with(X_ROT_270).with(UV_LOCK))
/* 2039 */         .with(condition().term((Property)BlockStateProperties.DOWN, Boolean.valueOf(true)), skin.with(X_ROT_90).with(UV_LOCK))
/*      */         
/* 2041 */         .with(condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false)), skinless)
/* 2042 */         .with(condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(false)), skinless.with(Y_ROT_90))
/* 2043 */         .with(condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false)), skinless.with(Y_ROT_180))
/* 2044 */         .with(condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), skinless.with(Y_ROT_270))
/* 2045 */         .with(condition().term((Property)BlockStateProperties.UP, Boolean.valueOf(false)), skinless.with(X_ROT_270))
/* 2046 */         .with(condition().term((Property)BlockStateProperties.DOWN, Boolean.valueOf(false)), skinless.with(X_ROT_90)));
/* 2049 */     registerSimpleItemModel(block, TexturedModel.CUBE.createWithSuffix(block, "_inventory", this.modelOutput));
/*      */   }
/*      */   
/*      */   private void createCakeBlock() {
/* 2053 */     registerSimpleFlatItemModel(Items.CAKE);
/* 2054 */     this.blockStateOutput.accept(
/* 2055 */         MultiVariantGenerator.dispatch(Blocks.CAKE)
/* 2056 */         .with(
/* 2057 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.BITES)
/* 2058 */           .select(Integer.valueOf(0), plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE)))
/* 2059 */           .select(Integer.valueOf(1), plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice1")))
/* 2060 */           .select(Integer.valueOf(2), plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice2")))
/* 2061 */           .select(Integer.valueOf(3), plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice3")))
/* 2062 */           .select(Integer.valueOf(4), plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice4")))
/* 2063 */           .select(Integer.valueOf(5), plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice5")))
/* 2064 */           .select(Integer.valueOf(6), plainVariant(ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice6")))));
/*      */   }
/*      */   
/*      */   private void createCartographyTable() {
/* 2077 */     TextureMapping mapping = (new TextureMapping()).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side3")).put(TextureSlot.DOWN, TextureMapping.getBlockTexture(Blocks.DARK_OAK_PLANKS)).put(TextureSlot.UP, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_top")).put(TextureSlot.NORTH, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side3")).put(TextureSlot.EAST, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side3")).put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side1")).put(TextureSlot.WEST, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side2"));
/* 2079 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.CARTOGRAPHY_TABLE, plainVariant(ModelTemplates.CUBE.create(Blocks.CARTOGRAPHY_TABLE, mapping, this.modelOutput))));
/*      */   }
/*      */   
/*      */   private void createSmithingTable() {
/* 2090 */     TextureMapping mapping = (new TextureMapping()).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_front")).put(TextureSlot.DOWN, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_bottom")).put(TextureSlot.UP, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_top")).put(TextureSlot.NORTH, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_front")).put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_front")).put(TextureSlot.EAST, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_side")).put(TextureSlot.WEST, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_side"));
/* 2092 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.SMITHING_TABLE, plainVariant(ModelTemplates.CUBE.create(Blocks.SMITHING_TABLE, mapping, this.modelOutput))));
/*      */   }
/*      */   
/*      */   private void createCraftingTableLike(Block block, Block bottomBlock, BiFunction<Block, Block, TextureMapping> mappingProvider) {
/* 2096 */     TextureMapping mapping = mappingProvider.apply(block, bottomBlock);
/* 2097 */     this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE.create(block, mapping, this.modelOutput))));
/*      */   }
/*      */   
/*      */   private void createPumpkins() {
/* 2101 */     TextureMapping pumpkinTextures = TextureMapping.column(Blocks.PUMPKIN);
/* 2103 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.PUMPKIN, plainVariant(ModelLocationUtils.getModelLocation(Blocks.PUMPKIN))));
/* 2105 */     createPumpkinVariant(Blocks.CARVED_PUMPKIN, pumpkinTextures);
/* 2106 */     createPumpkinVariant(Blocks.JACK_O_LANTERN, pumpkinTextures);
/*      */   }
/*      */   
/*      */   private void createPumpkinVariant(Block block, TextureMapping textures) {
/* 2110 */     MultiVariant model = plainVariant(ModelTemplates.CUBE_ORIENTABLE.create(block, textures.copyAndUpdate(TextureSlot.FRONT, TextureMapping.getBlockTexture(block)), this.modelOutput));
/* 2111 */     this.blockStateOutput.accept(
/* 2112 */         MultiVariantGenerator.dispatch(block, model)
/* 2113 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */   
/*      */   private void createCauldrons() {
/* 2118 */     registerSimpleFlatItemModel(Items.CAULDRON);
/* 2119 */     createNonTemplateModelBlock(Blocks.CAULDRON);
/* 2121 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.LAVA_CAULDRON, plainVariant(ModelTemplates.CAULDRON_FULL.create(Blocks.LAVA_CAULDRON, TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.LAVA, "_still")), this.modelOutput))));
/* 2123 */     this.blockStateOutput.accept(
/* 2124 */         MultiVariantGenerator.dispatch(Blocks.WATER_CAULDRON)
/* 2125 */         .with(
/* 2126 */           (PropertyDispatch)PropertyDispatch.initial((Property)LayeredCauldronBlock.LEVEL)
/* 2127 */           .select(Integer.valueOf(1), plainVariant(ModelTemplates.CAULDRON_LEVEL1.createWithSuffix(Blocks.WATER_CAULDRON, "_level1", TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.WATER, "_still")), this.modelOutput)))
/* 2128 */           .select(Integer.valueOf(2), plainVariant(ModelTemplates.CAULDRON_LEVEL2.createWithSuffix(Blocks.WATER_CAULDRON, "_level2", TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.WATER, "_still")), this.modelOutput)))
/* 2129 */           .select(Integer.valueOf(3), plainVariant(ModelTemplates.CAULDRON_FULL.createWithSuffix(Blocks.WATER_CAULDRON, "_full", TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.WATER, "_still")), this.modelOutput)))));
/* 2133 */     this.blockStateOutput.accept(
/* 2134 */         MultiVariantGenerator.dispatch(Blocks.POWDER_SNOW_CAULDRON)
/* 2135 */         .with(
/* 2136 */           (PropertyDispatch)PropertyDispatch.initial((Property)LayeredCauldronBlock.LEVEL)
/* 2137 */           .select(Integer.valueOf(1), plainVariant(ModelTemplates.CAULDRON_LEVEL1.createWithSuffix(Blocks.POWDER_SNOW_CAULDRON, "_level1", TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.POWDER_SNOW)), this.modelOutput)))
/* 2138 */           .select(Integer.valueOf(2), plainVariant(ModelTemplates.CAULDRON_LEVEL2.createWithSuffix(Blocks.POWDER_SNOW_CAULDRON, "_level2", TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.POWDER_SNOW)), this.modelOutput)))
/* 2139 */           .select(Integer.valueOf(3), plainVariant(ModelTemplates.CAULDRON_FULL.createWithSuffix(Blocks.POWDER_SNOW_CAULDRON, "_full", TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.POWDER_SNOW)), this.modelOutput)))));
/*      */   }
/*      */   
/*      */   private void createChorusFlower() {
/* 2145 */     TextureMapping aliveTextures = TextureMapping.defaultTexture(Blocks.CHORUS_FLOWER);
/* 2146 */     MultiVariant aliveModel = plainVariant(ModelTemplates.CHORUS_FLOWER.create(Blocks.CHORUS_FLOWER, aliveTextures, this.modelOutput));
/* 2147 */     MultiVariant deadModel = plainVariant(createSuffixedVariant(Blocks.CHORUS_FLOWER, "_dead", ModelTemplates.CHORUS_FLOWER, id -> aliveTextures.copyAndUpdate(TextureSlot.TEXTURE, id)));
/* 2149 */     this.blockStateOutput.accept(
/* 2150 */         MultiVariantGenerator.dispatch(Blocks.CHORUS_FLOWER)
/* 2151 */         .with(createEmptyOrFullDispatch((Property<Integer>)BlockStateProperties.AGE_5, Integer.valueOf(5), deadModel, aliveModel)));
/*      */   }
/*      */   
/*      */   private void createCrafterBlock() {
/* 2156 */     MultiVariant off = plainVariant(ModelLocationUtils.getModelLocation(Blocks.CRAFTER));
/* 2157 */     MultiVariant triggeredLocation = plainVariant(ModelLocationUtils.getModelLocation(Blocks.CRAFTER, "_triggered"));
/* 2158 */     MultiVariant craftingLocation = plainVariant(ModelLocationUtils.getModelLocation(Blocks.CRAFTER, "_crafting"));
/* 2159 */     MultiVariant craftingTriggeredLocation = plainVariant(ModelLocationUtils.getModelLocation(Blocks.CRAFTER, "_crafting_triggered"));
/* 2161 */     this.blockStateOutput.accept(
/* 2162 */         MultiVariantGenerator.dispatch(Blocks.CRAFTER)
/* 2163 */         .with(
/* 2164 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.TRIGGERED, (Property)CrafterBlock.CRAFTING)
/* 2165 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), off)
/* 2166 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), craftingTriggeredLocation)
/* 2167 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), triggeredLocation)
/* 2168 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), craftingLocation))
/*      */         
/* 2170 */         .with(
/* 2171 */           PropertyDispatch.modify((Property)BlockStateProperties.ORIENTATION)
/* 2172 */           .generate(BlockModelGenerators::applyRotation)));
/*      */   }
/*      */   
/*      */   private void createDispenserBlock(Block block) {
/* 2181 */     TextureMapping horizontalTextures = (new TextureMapping()).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.FURNACE, "_top")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.FURNACE, "_side")).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front"));
/* 2185 */     TextureMapping verticalTextures = (new TextureMapping()).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.FURNACE, "_top")).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front_vertical"));
/* 2187 */     MultiVariant horizontalModel = plainVariant(ModelTemplates.CUBE_ORIENTABLE.create(block, horizontalTextures, this.modelOutput));
/* 2188 */     MultiVariant verticalModel = plainVariant(ModelTemplates.CUBE_ORIENTABLE_VERTICAL.create(block, verticalTextures, this.modelOutput));
/* 2190 */     this.blockStateOutput.accept(
/* 2191 */         MultiVariantGenerator.dispatch(block)
/* 2192 */         .with(
/* 2193 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.FACING)
/* 2194 */           .select((Comparable)Direction.DOWN, verticalModel.with(X_ROT_180))
/* 2195 */           .select((Comparable)Direction.UP, verticalModel)
/*      */           
/* 2197 */           .select((Comparable)Direction.NORTH, horizontalModel)
/* 2198 */           .select((Comparable)Direction.EAST, horizontalModel.with(Y_ROT_90))
/* 2199 */           .select((Comparable)Direction.SOUTH, horizontalModel.with(Y_ROT_180))
/* 2200 */           .select((Comparable)Direction.WEST, horizontalModel.with(Y_ROT_270))));
/*      */   }
/*      */   
/*      */   private void createEndPortalFrame() {
/* 2207 */     MultiVariant empty = plainVariant(ModelLocationUtils.getModelLocation(Blocks.END_PORTAL_FRAME));
/* 2208 */     MultiVariant filled = plainVariant(ModelLocationUtils.getModelLocation(Blocks.END_PORTAL_FRAME, "_filled"));
/* 2210 */     this.blockStateOutput.accept(
/* 2211 */         MultiVariantGenerator.dispatch(Blocks.END_PORTAL_FRAME)
/* 2212 */         .with(
/* 2213 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.EYE)
/* 2214 */           .select(Boolean.valueOf(false), empty)
/* 2215 */           .select(Boolean.valueOf(true), filled))
/*      */         
/* 2217 */         .with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */   }
/*      */   
/*      */   private void createChorusPlant() {
/* 2224 */     MultiVariant side = plainVariant(ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_side"));
/* 2225 */     Variant noside = plainModel(ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside"));
/* 2226 */     Variant noside1 = plainModel(ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside1"));
/* 2227 */     Variant noside2 = plainModel(ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside2"));
/* 2228 */     Variant noside3 = plainModel(ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside3"));
/* 2230 */     Variant nosideUvLock = noside.with(UV_LOCK);
/* 2231 */     Variant noside1uvLock = noside1.with(UV_LOCK);
/* 2232 */     Variant noside2uvLock = noside2.with(UV_LOCK);
/* 2233 */     Variant noside3uvLock = noside3.with(UV_LOCK);
/* 2235 */     this.blockStateOutput.accept(
/* 2236 */         MultiPartGenerator.multiPart(Blocks.CHORUS_PLANT)
/* 2237 */         .with(condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true)), side)
/* 2238 */         .with(condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(true)), side.with(Y_ROT_90).with(UV_LOCK))
/* 2239 */         .with(condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true)), side.with(Y_ROT_180).with(UV_LOCK))
/* 2240 */         .with(condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), side.with(Y_ROT_270).with(UV_LOCK))
/* 2241 */         .with(condition().term((Property)BlockStateProperties.UP, Boolean.valueOf(true)), side.with(X_ROT_270).with(UV_LOCK))
/* 2242 */         .with(condition().term((Property)BlockStateProperties.DOWN, Boolean.valueOf(true)), side.with(X_ROT_90).with(UV_LOCK))
/*      */         
/* 2244 */         .with(condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false)), new MultiVariant(
/* 2245 */             WeightedList.of(new Weighted[] { new Weighted(noside, 2), new Weighted(noside1, 1), new Weighted(noside2, 1), new Weighted(noside3, 1) }))).with(condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(false)), new MultiVariant(
/* 2254 */             WeightedList.of(new Weighted[] { new Weighted(noside1uvLock.with(Y_ROT_90), 1), new Weighted(noside2uvLock
/* 2256 */                   .with(Y_ROT_90), 1), new Weighted(noside3uvLock
/* 2257 */                   .with(Y_ROT_90), 1), new Weighted(nosideUvLock
/* 2258 */                   .with(Y_ROT_90), 2) }))).with(condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false)), new MultiVariant(
/* 2263 */             WeightedList.of(new Weighted[] { new Weighted(noside2uvLock.with(Y_ROT_180), 1), new Weighted(noside3uvLock
/* 2265 */                   .with(Y_ROT_180), 1), new Weighted(nosideUvLock
/* 2266 */                   .with(Y_ROT_180), 2), new Weighted(noside1uvLock
/* 2267 */                   .with(Y_ROT_180), 1) }))).with(condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), new MultiVariant(
/* 2272 */             WeightedList.of(new Weighted[] { new Weighted(noside3uvLock.with(Y_ROT_270), 1), new Weighted(nosideUvLock
/* 2274 */                   .with(Y_ROT_270), 2), new Weighted(noside1uvLock
/* 2275 */                   .with(Y_ROT_270), 1), new Weighted(noside2uvLock
/* 2276 */                   .with(Y_ROT_270), 1) }))).with(condition().term((Property)BlockStateProperties.UP, Boolean.valueOf(false)), new MultiVariant(
/* 2281 */             WeightedList.of(new Weighted[] { new Weighted(nosideUvLock.with(X_ROT_270), 2), new Weighted(noside3uvLock
/* 2283 */                   .with(X_ROT_270), 1), new Weighted(noside1uvLock
/* 2284 */                   .with(X_ROT_270), 1), new Weighted(noside2uvLock
/* 2285 */                   .with(X_ROT_270), 1) }))).with(condition().term((Property)BlockStateProperties.DOWN, Boolean.valueOf(false)), new MultiVariant(
/* 2290 */             WeightedList.of(new Weighted[] { new Weighted(noside3uvLock.with(X_ROT_90), 1), new Weighted(noside2uvLock
/* 2292 */                   .with(X_ROT_90), 1), new Weighted(noside1uvLock
/* 2293 */                   .with(X_ROT_90), 1), new Weighted(nosideUvLock
/* 2294 */                   .with(X_ROT_90), 2) }))));
/*      */   }
/*      */   
/*      */   private void createComposter() {
/* 2301 */     this.blockStateOutput.accept(
/* 2302 */         MultiPartGenerator.multiPart(Blocks.COMPOSTER)
/* 2303 */         .with(plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPOSTER)))
/* 2304 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(1)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPOSTER, "_contents1")))
/* 2305 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(2)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPOSTER, "_contents2")))
/* 2306 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(3)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPOSTER, "_contents3")))
/* 2307 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(4)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPOSTER, "_contents4")))
/* 2308 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(5)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPOSTER, "_contents5")))
/* 2309 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(6)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPOSTER, "_contents6")))
/* 2310 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(7)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPOSTER, "_contents7")))
/* 2311 */         .with(condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(8)), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COMPOSTER, "_contents_ready"))));
/*      */   }
/*      */   
/*      */   private void createCopperBulb(Block copperBulb) {
/* 2316 */     MultiVariant baseModel = plainVariant(ModelTemplates.CUBE_ALL.create(copperBulb, TextureMapping.cube(copperBulb), this.modelOutput));
/* 2317 */     MultiVariant baseModelPowered = plainVariant(createSuffixedVariant(copperBulb, "_powered", ModelTemplates.CUBE_ALL, TextureMapping::cube));
/* 2318 */     MultiVariant litModel = plainVariant(createSuffixedVariant(copperBulb, "_lit", ModelTemplates.CUBE_ALL, TextureMapping::cube));
/* 2319 */     MultiVariant litModelPowered = plainVariant(createSuffixedVariant(copperBulb, "_lit_powered", ModelTemplates.CUBE_ALL, TextureMapping::cube));
/* 2321 */     this.blockStateOutput.accept(createCopperBulb(copperBulb, baseModel, litModel, baseModelPowered, litModelPowered));
/*      */   }
/*      */   
/*      */   private static BlockModelDefinitionGenerator createCopperBulb(Block copperBulb, MultiVariant baseModel, MultiVariant litModel, MultiVariant baseModelPowered, MultiVariant litModelPowered) {
/* 2325 */     return (BlockModelDefinitionGenerator)MultiVariantGenerator.dispatch(copperBulb)
/* 2326 */       .with(
/* 2327 */         PropertyDispatch.initial((Property)BlockStateProperties.LIT, (Property)BlockStateProperties.POWERED)
/* 2328 */         .generate((emittingLight, powered) -> emittingLight.booleanValue() ? (powered.booleanValue() ? litModelPowered : litModel) : (powered.booleanValue() ? baseModelPowered : baseModel)));
/*      */   }
/*      */   
/*      */   private void copyCopperBulbModel(Block donor, Block acceptor) {
/* 2338 */     MultiVariant baseModel = plainVariant(ModelLocationUtils.getModelLocation(donor));
/* 2339 */     MultiVariant baseModelPowered = plainVariant(ModelLocationUtils.getModelLocation(donor, "_powered"));
/* 2340 */     MultiVariant litModel = plainVariant(ModelLocationUtils.getModelLocation(donor, "_lit"));
/* 2341 */     MultiVariant litModelPowered = plainVariant(ModelLocationUtils.getModelLocation(donor, "_lit_powered"));
/* 2343 */     this.itemModelOutput.copy(donor.asItem(), acceptor.asItem());
/* 2344 */     this.blockStateOutput.accept(createCopperBulb(acceptor, baseModel, litModel, baseModelPowered, litModelPowered));
/*      */   }
/*      */   
/*      */   private void createAmethystCluster(Block clusterBlock) {
/* 2348 */     MultiVariant model = plainVariant(ModelTemplates.CROSS.create(clusterBlock, TextureMapping.cross(clusterBlock), this.modelOutput));
/* 2349 */     this.blockStateOutput.accept(
/* 2350 */         MultiVariantGenerator.dispatch(clusterBlock, model)
/* 2351 */         .with(ROTATIONS_COLUMN_WITH_FACING));
/*      */   }
/*      */   
/*      */   private void createAmethystClusters() {
/* 2356 */     createAmethystCluster(Blocks.SMALL_AMETHYST_BUD);
/* 2357 */     createAmethystCluster(Blocks.MEDIUM_AMETHYST_BUD);
/* 2358 */     createAmethystCluster(Blocks.LARGE_AMETHYST_BUD);
/* 2359 */     createAmethystCluster(Blocks.AMETHYST_CLUSTER);
/*      */   }
/*      */   
/*      */   private void createSpeleothem(Block block) {
/* 2363 */     PropertyDispatch.C2<MultiVariant, Direction, SpeleothemThickness> generator = PropertyDispatch.initial((Property)BlockStateProperties.VERTICAL_DIRECTION, (Property)BlockStateProperties.SPELEOTHEM_THICKNESS);
/* 2364 */     for (SpeleothemThickness speleothemThickness : SpeleothemThickness.values())
/* 2365 */       generator.select((Comparable)Direction.UP, (Comparable)speleothemThickness, createSpeleothemVariant(Direction.UP, speleothemThickness, block)); 
/* 2367 */     for (SpeleothemThickness speleothemThickness : SpeleothemThickness.values())
/* 2368 */       generator.select((Comparable)Direction.DOWN, (Comparable)speleothemThickness, createSpeleothemVariant(Direction.DOWN, speleothemThickness, block)); 
/* 2370 */     this.blockStateOutput.accept(
/* 2371 */         MultiVariantGenerator.dispatch(block)
/* 2372 */         .with((PropertyDispatch)generator));
/*      */   }
/*      */   
/*      */   private MultiVariant createSpeleothemVariant(Direction direction, SpeleothemThickness speleothemThickness, Block block) {
/* 2377 */     String suffix = "_" + direction.getSerializedName() + "_" + speleothemThickness.getSerializedName();
/* 2378 */     TextureMapping texture = TextureMapping.cross(TextureMapping.getBlockTexture(block, suffix));
/* 2379 */     return plainVariant(ModelTemplates.POINTED_DRIPSTONE.createWithSuffix(block, suffix, texture, this.modelOutput));
/*      */   }
/*      */   
/*      */   private void createNyliumBlock(Block block) {
/* 2386 */     TextureMapping mapping = (new TextureMapping()).put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.NETHERRACK)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(block)).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"));
/* 2388 */     this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.create(block, mapping, this.modelOutput))));
/*      */   }
/*      */   
/*      */   private void createDaylightDetector() {
/* 2392 */     Material sideTexture = TextureMapping.getBlockTexture(Blocks.DAYLIGHT_DETECTOR, "_side");
/* 2393 */     TextureMapping normalTextures = (new TextureMapping()).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.DAYLIGHT_DETECTOR, "_top")).put(TextureSlot.SIDE, sideTexture);
/* 2394 */     TextureMapping invertedTextures = (new TextureMapping()).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.DAYLIGHT_DETECTOR, "_inverted_top")).put(TextureSlot.SIDE, sideTexture);
/* 2396 */     this.blockStateOutput.accept(
/* 2397 */         MultiVariantGenerator.dispatch(Blocks.DAYLIGHT_DETECTOR)
/* 2398 */         .with(
/* 2399 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.INVERTED)
/* 2400 */           .select(Boolean.valueOf(false), plainVariant(ModelTemplates.DAYLIGHT_DETECTOR.create(Blocks.DAYLIGHT_DETECTOR, normalTextures, this.modelOutput)))
/* 2401 */           .select(Boolean.valueOf(true), plainVariant(ModelTemplates.DAYLIGHT_DETECTOR.create(ModelLocationUtils.getModelLocation(Blocks.DAYLIGHT_DETECTOR, "_inverted"), invertedTextures, this.modelOutput)))));
/*      */   }
/*      */   
/*      */   private void createRotatableColumn(Block block) {
/* 2407 */     this.blockStateOutput.accept(
/* 2408 */         MultiVariantGenerator.dispatch(block, plainVariant(ModelLocationUtils.getModelLocation(block)))
/* 2409 */         .with(ROTATIONS_COLUMN_WITH_FACING));
/*      */   }
/*      */   
/*      */   private void createLightningRod(Block block, Block waxedBlock) {
/* 2414 */     MultiVariant on = plainVariant(ModelLocationUtils.getModelLocation((Block)Blocks.LIGHTNING_ROD.weathering().unaffected(), "_on"));
/* 2415 */     MultiVariant off = plainVariant(ModelTemplates.LIGHTNING_ROD.create(block, TextureMapping.defaultTexture(block), this.modelOutput));
/* 2417 */     this.blockStateOutput.accept(
/* 2418 */         MultiVariantGenerator.dispatch(block)
/* 2419 */         .with(createBooleanModelDispatch(BlockStateProperties.POWERED, on, off))
/* 2420 */         .with(ROTATIONS_COLUMN_WITH_FACING));
/* 2422 */     this.blockStateOutput.accept(
/* 2423 */         MultiVariantGenerator.dispatch(waxedBlock)
/* 2424 */         .with(createBooleanModelDispatch(BlockStateProperties.POWERED, on, off))
/* 2425 */         .with(ROTATIONS_COLUMN_WITH_FACING));
/* 2427 */     this.itemModelOutput.copy(block.asItem(), waxedBlock.asItem());
/*      */   }
/*      */   
/*      */   private void createFarmland() {
/* 2431 */     TextureMapping dryTextures = (new TextureMapping()).put(TextureSlot.DIRT, TextureMapping.getBlockTexture(Blocks.DIRT)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.FARMLAND));
/* 2432 */     TextureMapping moistTextures = (new TextureMapping()).put(TextureSlot.DIRT, TextureMapping.getBlockTexture(Blocks.DIRT)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.FARMLAND, "_moist"));
/* 2434 */     MultiVariant dryModel = plainVariant(ModelTemplates.FARMLAND.create(Blocks.FARMLAND, dryTextures, this.modelOutput));
/* 2435 */     MultiVariant moistModel = plainVariant(ModelTemplates.FARMLAND.create(ModelLocationUtils.getModelLocation(Blocks.FARMLAND, "_moist"), moistTextures, this.modelOutput));
/* 2437 */     this.blockStateOutput.accept(
/* 2438 */         MultiVariantGenerator.dispatch(Blocks.FARMLAND)
/* 2439 */         .with(createEmptyOrFullDispatch((Property<Integer>)BlockStateProperties.MOISTURE, Integer.valueOf(7), moistModel, dryModel)));
/*      */   }
/*      */   
/*      */   private MultiVariant createFloorFireModels(Block block) {
/* 2444 */     return variants(new Variant[] { plainModel(ModelTemplates.FIRE_FLOOR.create(ModelLocationUtils.getModelLocation(block, "_floor0"), TextureMapping.fire0(block), this.modelOutput)), 
/* 2446 */           plainModel(ModelTemplates.FIRE_FLOOR.create(ModelLocationUtils.getModelLocation(block, "_floor1"), TextureMapping.fire1(block), this.modelOutput)) });
/*      */   }
/*      */   
/*      */   private MultiVariant createSideFireModels(Block block) {
/* 2451 */     return variants(new Variant[] { plainModel(ModelTemplates.FIRE_SIDE.create(ModelLocationUtils.getModelLocation(block, "_side0"), TextureMapping.fire0(block), this.modelOutput)), 
/* 2453 */           plainModel(ModelTemplates.FIRE_SIDE.create(ModelLocationUtils.getModelLocation(block, "_side1"), TextureMapping.fire1(block), this.modelOutput)), 
/*      */           
/* 2455 */           plainModel(ModelTemplates.FIRE_SIDE_ALT.create(ModelLocationUtils.getModelLocation(block, "_side_alt0"), TextureMapping.fire0(block), this.modelOutput)), 
/* 2456 */           plainModel(ModelTemplates.FIRE_SIDE_ALT.create(ModelLocationUtils.getModelLocation(block, "_side_alt1"), TextureMapping.fire1(block), this.modelOutput)) });
/*      */   }
/*      */   
/*      */   private MultiVariant createTopFireModels(Block block) {
/* 2461 */     return variants(new Variant[] { plainModel(ModelTemplates.FIRE_UP.create(ModelLocationUtils.getModelLocation(block, "_up0"), TextureMapping.fire0(block), this.modelOutput)), 
/* 2463 */           plainModel(ModelTemplates.FIRE_UP.create(ModelLocationUtils.getModelLocation(block, "_up1"), TextureMapping.fire1(block), this.modelOutput)), 
/* 2464 */           plainModel(ModelTemplates.FIRE_UP_ALT.create(ModelLocationUtils.getModelLocation(block, "_up_alt0"), TextureMapping.fire0(block), this.modelOutput)), 
/* 2465 */           plainModel(ModelTemplates.FIRE_UP_ALT.create(ModelLocationUtils.getModelLocation(block, "_up_alt1"), TextureMapping.fire1(block), this.modelOutput)) });
/*      */   }
/*      */   
/*      */   private void createFire() {
/* 2475 */     ConditionBuilder noSides = condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false)).term((Property)BlockStateProperties.EAST, Boolean.valueOf(false)).term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false)).term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)).term((Property)BlockStateProperties.UP, Boolean.valueOf(false));
/* 2476 */     MultiVariant floorFireModels = createFloorFireModels(Blocks.FIRE);
/* 2477 */     MultiVariant sideFireModels = createSideFireModels(Blocks.FIRE);
/* 2478 */     MultiVariant topFireModels = createTopFireModels(Blocks.FIRE);
/* 2480 */     this.blockStateOutput.accept(
/* 2481 */         MultiPartGenerator.multiPart(Blocks.FIRE)
/* 2482 */         .with(noSides, floorFireModels)
/*      */         
/* 2486 */         .with(
/* 2487 */           or(new ConditionBuilder[] { condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true)), noSides }), sideFireModels)
/*      */         
/* 2490 */         .with(
/* 2491 */           or(new ConditionBuilder[] { condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(true)), noSides }), sideFireModels
/* 2492 */           .with(Y_ROT_90))
/*      */         
/* 2494 */         .with(
/* 2495 */           or(new ConditionBuilder[] { condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true)), noSides }), sideFireModels
/* 2496 */           .with(Y_ROT_180))
/*      */         
/* 2498 */         .with(
/* 2499 */           or(new ConditionBuilder[] { condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), noSides }), sideFireModels
/* 2500 */           .with(Y_ROT_270))
/*      */         
/* 2502 */         .with(
/* 2503 */           condition().term((Property)BlockStateProperties.UP, Boolean.valueOf(true)), topFireModels));
/*      */   }
/*      */   
/*      */   private void createSoulFire() {
/* 2510 */     MultiVariant floorFireModels = createFloorFireModels(Blocks.SOUL_FIRE);
/* 2511 */     MultiVariant sideFireModels = createSideFireModels(Blocks.SOUL_FIRE);
/* 2513 */     this.blockStateOutput.accept(
/* 2514 */         MultiPartGenerator.multiPart(Blocks.SOUL_FIRE)
/* 2515 */         .with(floorFireModels)
/* 2516 */         .with(sideFireModels)
/* 2517 */         .with(sideFireModels.with(Y_ROT_90))
/* 2518 */         .with(sideFireModels.with(Y_ROT_180))
/* 2519 */         .with(sideFireModels.with(Y_ROT_270)));
/*      */   }
/*      */   
/*      */   private void createLantern(Block block) {
/* 2524 */     MultiVariant ground = plainVariant(TexturedModel.LANTERN.create(block, this.modelOutput));
/* 2525 */     MultiVariant hanging = plainVariant(TexturedModel.HANGING_LANTERN.create(block, this.modelOutput));
/* 2527 */     registerSimpleFlatItemModel(block.asItem());
/* 2528 */     this.blockStateOutput.accept(
/* 2529 */         MultiVariantGenerator.dispatch(block)
/* 2530 */         .with(createBooleanModelDispatch(BlockStateProperties.HANGING, hanging, ground)));
/*      */   }
/*      */   
/*      */   private void createCopperLantern(Block unwaxed, Block waxed) {
/* 2535 */     Identifier ground = TexturedModel.LANTERN.create(unwaxed, this.modelOutput);
/* 2536 */     Identifier hanging = TexturedModel.HANGING_LANTERN.create(unwaxed, this.modelOutput);
/* 2538 */     registerSimpleFlatItemModel(unwaxed.asItem());
/* 2539 */     this.itemModelOutput.copy(unwaxed.asItem(), waxed.asItem());
/* 2540 */     this.blockStateOutput.accept(
/* 2541 */         MultiVariantGenerator.dispatch(unwaxed)
/* 2542 */         .with(createBooleanModelDispatch(BlockStateProperties.HANGING, plainVariant(hanging), plainVariant(ground))));
/* 2544 */     this.blockStateOutput.accept(
/* 2545 */         MultiVariantGenerator.dispatch(waxed)
/* 2546 */         .with(createBooleanModelDispatch(BlockStateProperties.HANGING, plainVariant(hanging), plainVariant(ground))));
/*      */   }
/*      */   
/*      */   private void createCopperChain(Block unwaxed, Block waxed) {
/* 2551 */     MultiVariant model = plainVariant(TexturedModel.CHAIN.create(unwaxed, this.modelOutput));
/* 2552 */     createAxisAlignedPillarBlockCustomModel(unwaxed, model);
/* 2553 */     createAxisAlignedPillarBlockCustomModel(waxed, model);
/*      */   }
/*      */   
/*      */   private void createMuddyMangroveRoots() {
/* 2557 */     TextureMapping textures = TextureMapping.column(TextureMapping.getBlockTexture(Blocks.MUDDY_MANGROVE_ROOTS, "_side"), TextureMapping.getBlockTexture(Blocks.MUDDY_MANGROVE_ROOTS, "_top"));
/* 2558 */     MultiVariant model = plainVariant(ModelTemplates.CUBE_COLUMN.create(Blocks.MUDDY_MANGROVE_ROOTS, textures, this.modelOutput));
/* 2559 */     this.blockStateOutput.accept(createAxisAlignedPillarBlock(Blocks.MUDDY_MANGROVE_ROOTS, model));
/*      */   }
/*      */   
/*      */   private void createMangrovePropagule() {
/* 2563 */     registerSimpleFlatItemModel(Items.MANGROVE_PROPAGULE);
/* 2565 */     Block block = Blocks.MANGROVE_PROPAGULE;
/* 2567 */     MultiVariant plantedModel = plainVariant(ModelLocationUtils.getModelLocation(block));
/* 2568 */     this.blockStateOutput.accept(
/* 2569 */         MultiVariantGenerator.dispatch(Blocks.MANGROVE_PROPAGULE)
/* 2570 */         .with(
/* 2571 */           PropertyDispatch.initial((Property)MangrovePropaguleBlock.HANGING, (Property)MangrovePropaguleBlock.AGE)
/* 2572 */           .generate((hanging, age) -> hanging.booleanValue() ? plainVariant(ModelLocationUtils.getModelLocation(block, "_hanging_" + age)) : plantedModel)));
/*      */   }
/*      */   
/*      */   private void createFrostedIce() {
/* 2582 */     this.blockStateOutput.accept(
/* 2583 */         MultiVariantGenerator.dispatch(Blocks.FROSTED_ICE)
/* 2584 */         .with(
/* 2585 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.AGE_3)
/* 2586 */           .select(Integer.valueOf(0), plainVariant(createSuffixedVariant(Blocks.FROSTED_ICE, "_0", ModelTemplates.CUBE_ALL, TextureMapping::cube)))
/* 2587 */           .select(Integer.valueOf(1), plainVariant(createSuffixedVariant(Blocks.FROSTED_ICE, "_1", ModelTemplates.CUBE_ALL, TextureMapping::cube)))
/* 2588 */           .select(Integer.valueOf(2), plainVariant(createSuffixedVariant(Blocks.FROSTED_ICE, "_2", ModelTemplates.CUBE_ALL, TextureMapping::cube)))
/* 2589 */           .select(Integer.valueOf(3), plainVariant(createSuffixedVariant(Blocks.FROSTED_ICE, "_3", ModelTemplates.CUBE_ALL, TextureMapping::cube)))));
/*      */   }
/*      */   
/*      */   private void createGrassBlocks() {
/* 2595 */     Material bottomTexture = TextureMapping.getBlockTexture(Blocks.DIRT);
/* 2600 */     TextureMapping snowyMapping = (new TextureMapping()).put(TextureSlot.BOTTOM, bottomTexture).copyForced(TextureSlot.BOTTOM, TextureSlot.PARTICLE).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_top")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_snow"));
/* 2602 */     MultiVariant snowyGrass = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.GRASS_BLOCK, "_snow", snowyMapping, this.modelOutput));
/* 2604 */     Identifier plainGrassModel = ModelLocationUtils.getModelLocation(Blocks.GRASS_BLOCK);
/* 2605 */     createGrassLikeBlock(Blocks.GRASS_BLOCK, createRotatedVariants(plainModel(plainGrassModel)), snowyGrass);
/* 2606 */     registerSimpleTintedItemModel(Blocks.GRASS_BLOCK, plainGrassModel, (ItemTintSource)new GrassColorSource());
/* 2608 */     MultiVariant myceliumModel = createRotatedVariants(plainModel(TexturedModel.CUBE_TOP_BOTTOM.get(Blocks.MYCELIUM).updateTextures(m -> m.put(TextureSlot.BOTTOM, bottomTexture)).create(Blocks.MYCELIUM, this.modelOutput)));
/* 2609 */     createGrassLikeBlock(Blocks.MYCELIUM, myceliumModel, snowyGrass);
/* 2611 */     MultiVariant podzolModel = createRotatedVariants(plainModel(TexturedModel.CUBE_TOP_BOTTOM.get(Blocks.PODZOL).updateTextures(m -> m.put(TextureSlot.BOTTOM, bottomTexture)).create(Blocks.PODZOL, this.modelOutput)));
/* 2612 */     createGrassLikeBlock(Blocks.PODZOL, podzolModel, snowyGrass);
/*      */   }
/*      */   
/*      */   private void createGrassLikeBlock(Block block, MultiVariant normal, MultiVariant snowy) {
/* 2616 */     this.blockStateOutput.accept(
/* 2617 */         MultiVariantGenerator.dispatch(block)
/* 2618 */         .with(
/* 2619 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.SNOWY)
/* 2620 */           .select(Boolean.valueOf(true), snowy)
/* 2621 */           .select(Boolean.valueOf(false), normal)));
/*      */   }
/*      */   
/*      */   private void createCocoa() {
/* 2627 */     registerSimpleFlatItemModel(Items.COCOA_BEANS);
/* 2628 */     this.blockStateOutput.accept(
/* 2629 */         MultiVariantGenerator.dispatch(Blocks.COCOA)
/* 2630 */         .with(
/* 2631 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.AGE_2)
/* 2632 */           .select(Integer.valueOf(0), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COCOA, "_stage0")))
/* 2633 */           .select(Integer.valueOf(1), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COCOA, "_stage1")))
/* 2634 */           .select(Integer.valueOf(2), plainVariant(ModelLocationUtils.getModelLocation(Blocks.COCOA, "_stage2"))))
/*      */         
/* 2636 */         .with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */   }
/*      */   
/*      */   private void createDirtPath() {
/* 2641 */     Variant model = plainModel(ModelLocationUtils.getModelLocation(Blocks.DIRT_PATH));
/* 2642 */     this.blockStateOutput.accept(
/* 2643 */         MultiVariantGenerator.dispatch(Blocks.DIRT_PATH, createRotatedVariants(model)));
/*      */   }
/*      */   
/*      */   private void createWeightedPressurePlate(Block block, Block appearance) {
/* 2648 */     TextureMapping textures = TextureMapping.defaultTexture(appearance);
/* 2649 */     MultiVariant up = plainVariant(ModelTemplates.PRESSURE_PLATE_UP.create(block, textures, this.modelOutput));
/* 2650 */     MultiVariant down = plainVariant(ModelTemplates.PRESSURE_PLATE_DOWN.create(block, textures, this.modelOutput));
/* 2652 */     this.blockStateOutput.accept(
/* 2653 */         MultiVariantGenerator.dispatch(block)
/* 2654 */         .with(createEmptyOrFullDispatch((Property<Integer>)BlockStateProperties.POWER, Integer.valueOf(1), down, up)));
/*      */   }
/*      */   
/*      */   private void createHopper() {
/* 2659 */     MultiVariant downBlock = plainVariant(ModelLocationUtils.getModelLocation(Blocks.HOPPER));
/* 2660 */     MultiVariant sideBlock = plainVariant(ModelLocationUtils.getModelLocation(Blocks.HOPPER, "_side"));
/* 2662 */     registerSimpleFlatItemModel(Items.HOPPER);
/* 2663 */     this.blockStateOutput.accept(
/* 2664 */         MultiVariantGenerator.dispatch(Blocks.HOPPER)
/* 2665 */         .with(
/* 2666 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.FACING_HOPPER)
/* 2667 */           .select((Comparable)Direction.DOWN, downBlock)
/* 2668 */           .select((Comparable)Direction.NORTH, sideBlock)
/* 2669 */           .select((Comparable)Direction.EAST, sideBlock.with(Y_ROT_90))
/* 2670 */           .select((Comparable)Direction.SOUTH, sideBlock.with(Y_ROT_180))
/* 2671 */           .select((Comparable)Direction.WEST, sideBlock.with(Y_ROT_270))));
/*      */   }
/*      */   
/*      */   private void copyModel(Block donor, Block acceptor) {
/* 2678 */     MultiVariant model = plainVariant(ModelLocationUtils.getModelLocation(donor));
/* 2679 */     this.blockStateOutput.accept(MultiVariantGenerator.dispatch(acceptor, model));
/* 2680 */     this.itemModelOutput.copy(donor.asItem(), acceptor.asItem());
/*      */   }
/*      */   
/*      */   private void createBarsAndItem(Block block) {
/* 2684 */     TextureMapping textures = TextureMapping.bars(block);
/* 2685 */     createBars(block, ModelTemplates.BARS_POST_ENDS
/*      */         
/* 2687 */         .create(block, textures, this.modelOutput), ModelTemplates.BARS_POST
/* 2688 */         .create(block, textures, this.modelOutput), ModelTemplates.BARS_CAP
/* 2689 */         .create(block, textures, this.modelOutput), ModelTemplates.BARS_CAP_ALT
/* 2690 */         .create(block, textures, this.modelOutput), ModelTemplates.BARS_POST_SIDE
/* 2691 */         .create(block, textures, this.modelOutput), ModelTemplates.BARS_POST_SIDE_ALT
/* 2692 */         .create(block, textures, this.modelOutput));
/* 2694 */     registerSimpleFlatItemModel(block);
/*      */   }
/*      */   
/*      */   private void createBarsAndItem(Block unwaxed, Block waxed) {
/* 2698 */     TextureMapping textures = TextureMapping.bars(unwaxed);
/* 2699 */     Identifier postEndResource = ModelTemplates.BARS_POST_ENDS.create(unwaxed, textures, this.modelOutput);
/* 2700 */     Identifier postResource = ModelTemplates.BARS_POST.create(unwaxed, textures, this.modelOutput);
/* 2701 */     Identifier capResource = ModelTemplates.BARS_CAP.create(unwaxed, textures, this.modelOutput);
/* 2702 */     Identifier capAltResource = ModelTemplates.BARS_CAP_ALT.create(unwaxed, textures, this.modelOutput);
/* 2703 */     Identifier sideResource = ModelTemplates.BARS_POST_SIDE.create(unwaxed, textures, this.modelOutput);
/* 2704 */     Identifier sideAltResource = ModelTemplates.BARS_POST_SIDE_ALT.create(unwaxed, textures, this.modelOutput);
/* 2706 */     createBars(unwaxed, postEndResource, postResource, capResource, capAltResource, sideResource, sideAltResource);
/* 2707 */     createBars(waxed, postEndResource, postResource, capResource, capAltResource, sideResource, sideAltResource);
/* 2709 */     registerSimpleFlatItemModel(unwaxed);
/* 2710 */     this.itemModelOutput.copy(unwaxed.asItem(), waxed.asItem());
/*      */   }
/*      */   
/*      */   private void createBars(Block block, Identifier postEndResource, Identifier postResource, Identifier capResource, Identifier capAltResource, Identifier sideResource, Identifier sideAltResource) {
/* 2714 */     MultiVariant postEnds = plainVariant(postEndResource);
/* 2715 */     MultiVariant post = plainVariant(postResource);
/* 2716 */     MultiVariant cap = plainVariant(capResource);
/* 2717 */     MultiVariant capAlt = plainVariant(capAltResource);
/* 2718 */     MultiVariant side = plainVariant(sideResource);
/* 2719 */     MultiVariant sideAlt = plainVariant(sideAltResource);
/* 2721 */     this.blockStateOutput.accept(
/* 2722 */         MultiPartGenerator.multiPart(block)
/* 2723 */         .with(postEnds)
/* 2724 */         .with(
/* 2725 */           condition()
/* 2726 */           .term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false))
/* 2727 */           .term((Property)BlockStateProperties.EAST, Boolean.valueOf(false))
/* 2728 */           .term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false))
/* 2729 */           .term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), post)
/*      */         
/* 2732 */         .with(
/* 2733 */           condition()
/* 2734 */           .term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true))
/* 2735 */           .term((Property)BlockStateProperties.EAST, Boolean.valueOf(false))
/* 2736 */           .term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false))
/* 2737 */           .term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), cap)
/*      */         
/* 2740 */         .with(
/* 2741 */           condition()
/* 2742 */           .term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false))
/* 2743 */           .term((Property)BlockStateProperties.EAST, Boolean.valueOf(true))
/* 2744 */           .term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false))
/* 2745 */           .term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), cap
/* 2746 */           .with(Y_ROT_90))
/*      */         
/* 2748 */         .with(
/* 2749 */           condition()
/* 2750 */           .term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false))
/* 2751 */           .term((Property)BlockStateProperties.EAST, Boolean.valueOf(false))
/* 2752 */           .term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true))
/* 2753 */           .term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), capAlt)
/*      */         
/* 2756 */         .with(
/* 2757 */           condition()
/* 2758 */           .term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false))
/* 2759 */           .term((Property)BlockStateProperties.EAST, Boolean.valueOf(false))
/* 2760 */           .term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false))
/* 2761 */           .term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), capAlt
/* 2762 */           .with(Y_ROT_90))
/*      */         
/* 2764 */         .with(condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true)), side)
/* 2765 */         .with(condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(true)), side.with(Y_ROT_90))
/* 2766 */         .with(condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true)), sideAlt)
/* 2767 */         .with(condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), sideAlt.with(Y_ROT_90)));
/*      */   }
/*      */   
/*      */   private void createNonTemplateHorizontalBlock(Block block) {
/* 2772 */     this.blockStateOutput.accept(
/* 2773 */         MultiVariantGenerator.dispatch(block, plainVariant(ModelLocationUtils.getModelLocation(block)))
/* 2774 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */   
/*      */   private void createLever() {
/* 2779 */     MultiVariant off = plainVariant(ModelLocationUtils.getModelLocation(Blocks.LEVER));
/* 2780 */     MultiVariant on = plainVariant(ModelLocationUtils.getModelLocation(Blocks.LEVER, "_on"));
/* 2782 */     registerSimpleFlatItemModel(Blocks.LEVER);
/* 2783 */     this.blockStateOutput.accept(
/* 2784 */         MultiVariantGenerator.dispatch(Blocks.LEVER)
/* 2785 */         .with(
/* 2786 */           createBooleanModelDispatch(BlockStateProperties.POWERED, off, on))
/*      */         
/* 2788 */         .with(
/* 2789 */           (PropertyDispatch)PropertyDispatch.modify((Property)BlockStateProperties.ATTACH_FACE, (Property)BlockStateProperties.HORIZONTAL_FACING)
/* 2790 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.NORTH, X_ROT_180.then(Y_ROT_180))
/* 2791 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.EAST, X_ROT_180.then(Y_ROT_270))
/* 2792 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.SOUTH, X_ROT_180)
/* 2793 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.WEST, X_ROT_180.then(Y_ROT_90))
/*      */           
/* 2795 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.NORTH, NOP)
/* 2796 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.EAST, Y_ROT_90)
/* 2797 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.SOUTH, Y_ROT_180)
/* 2798 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.WEST, Y_ROT_270)
/*      */           
/* 2800 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.NORTH, X_ROT_90)
/* 2801 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.EAST, X_ROT_90.then(Y_ROT_90))
/* 2802 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.SOUTH, X_ROT_90.then(Y_ROT_180))
/* 2803 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.WEST, X_ROT_90.then(Y_ROT_270))));
/*      */   }
/*      */   
/*      */   private void createLilyPad() {
/* 2809 */     Identifier itemModel = createFlatItemModelWithBlockTexture(Items.LILY_PAD, Blocks.LILY_PAD);
/* 2810 */     registerSimpleTintedItemModel(Blocks.LILY_PAD, itemModel, ItemModelUtils.constantTint(-9321636));
/* 2811 */     Variant blockModel = plainModel(ModelLocationUtils.getModelLocation(Blocks.LILY_PAD));
/* 2812 */     this.blockStateOutput.accept(
/* 2813 */         MultiVariantGenerator.dispatch(Blocks.LILY_PAD, createRotatedVariants(blockModel)));
/*      */   }
/*      */   
/*      */   private void createFrogspawnBlock() {
/* 2818 */     registerSimpleFlatItemModel(Blocks.FROGSPAWN);
/* 2819 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.FROGSPAWN, plainVariant(ModelLocationUtils.getModelLocation(Blocks.FROGSPAWN))));
/*      */   }
/*      */   
/*      */   private void createNetherPortalBlock() {
/* 2823 */     this.blockStateOutput.accept(
/* 2824 */         MultiVariantGenerator.dispatch(Blocks.NETHER_PORTAL)
/* 2825 */         .with(
/* 2826 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.HORIZONTAL_AXIS)
/* 2827 */           .select((Comparable)Direction.Axis.X, plainVariant(ModelLocationUtils.getModelLocation(Blocks.NETHER_PORTAL, "_ns")))
/* 2828 */           .select((Comparable)Direction.Axis.Z, plainVariant(ModelLocationUtils.getModelLocation(Blocks.NETHER_PORTAL, "_ew")))));
/*      */   }
/*      */   
/*      */   private void createNetherrack() {
/* 2834 */     Variant model = plainModel(TexturedModel.CUBE.create(Blocks.NETHERRACK, this.modelOutput));
/* 2835 */     this.blockStateOutput.accept(
/* 2836 */         MultiVariantGenerator.dispatch(Blocks.NETHERRACK, 
/* 2837 */           variants(new Variant[] { 
/* 2839 */               model, model.with(X_ROT_90), model
/* 2840 */               .with(X_ROT_180), model
/* 2841 */               .with(X_ROT_270), model
/*      */               
/* 2843 */               .with(Y_ROT_90), model
/* 2844 */               .with(Y_ROT_90.then(X_ROT_90)), model
/* 2845 */               .with(Y_ROT_90.then(X_ROT_180)), model
/* 2846 */               .with(Y_ROT_90.then(X_ROT_270)), model
/*      */               
/* 2848 */               .with(Y_ROT_180), model
/* 2849 */               .with(Y_ROT_180.then(X_ROT_90)), 
/* 2849 */               model
/* 2850 */               .with(Y_ROT_180.then(X_ROT_180)), model
/* 2851 */               .with(Y_ROT_180.then(X_ROT_270)), model
/*      */               
/* 2853 */               .with(Y_ROT_270), model
/* 2854 */               .with(Y_ROT_270.then(X_ROT_90)), model
/* 2855 */               .with(Y_ROT_270.then(X_ROT_180)), model
/* 2856 */               .with(Y_ROT_270.then(X_ROT_270)) })));
/*      */   }
/*      */   
/*      */   private void createObserver() {
/* 2863 */     MultiVariant off = plainVariant(ModelLocationUtils.getModelLocation(Blocks.OBSERVER));
/* 2864 */     MultiVariant on = plainVariant(ModelLocationUtils.getModelLocation(Blocks.OBSERVER, "_on"));
/* 2866 */     this.blockStateOutput.accept(
/* 2867 */         MultiVariantGenerator.dispatch(Blocks.OBSERVER)
/* 2868 */         .with(createBooleanModelDispatch(BlockStateProperties.POWERED, on, off))
/* 2869 */         .with(ROTATION_FACING));
/*      */   }
/*      */   
/*      */   private void createPistons() {
/* 2876 */     TextureMapping commonMapping = (new TextureMapping()).put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.PISTON, "_bottom")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.PISTON, "_side"));
/* 2878 */     Material topSticky = TextureMapping.getBlockTexture(Blocks.PISTON, "_top_sticky");
/* 2879 */     Material top = TextureMapping.getBlockTexture(Blocks.PISTON, "_top");
/* 2881 */     TextureMapping stickyTextures = commonMapping.copyAndUpdate(TextureSlot.PLATFORM, topSticky);
/* 2882 */     TextureMapping normalTextures = commonMapping.copyAndUpdate(TextureSlot.PLATFORM, top);
/* 2884 */     MultiVariant extendedPiston = plainVariant(ModelLocationUtils.getModelLocation(Blocks.PISTON, "_base"));
/* 2886 */     createPistonVariant(Blocks.PISTON, extendedPiston, normalTextures);
/* 2887 */     createPistonVariant(Blocks.STICKY_PISTON, extendedPiston, stickyTextures);
/* 2889 */     Identifier normalInventory = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.PISTON, "_inventory", commonMapping.copyAndUpdate(TextureSlot.TOP, top), this.modelOutput);
/* 2890 */     Identifier stickyInventory = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.STICKY_PISTON, "_inventory", commonMapping.copyAndUpdate(TextureSlot.TOP, topSticky), this.modelOutput);
/* 2892 */     registerSimpleItemModel(Blocks.PISTON, normalInventory);
/* 2893 */     registerSimpleItemModel(Blocks.STICKY_PISTON, stickyInventory);
/*      */   }
/*      */   
/*      */   private void createPistonVariant(Block block, MultiVariant extended, TextureMapping textures) {
/* 2897 */     MultiVariant retracted = plainVariant(ModelTemplates.PISTON.create(block, textures, this.modelOutput));
/* 2898 */     this.blockStateOutput.accept(
/* 2899 */         MultiVariantGenerator.dispatch(block)
/* 2900 */         .with(createBooleanModelDispatch(BlockStateProperties.EXTENDED, extended, retracted))
/* 2901 */         .with(ROTATION_FACING));
/*      */   }
/*      */   
/*      */   private void createPistonHeads() {
/* 2908 */     TextureMapping commonMapping = (new TextureMapping()).put(TextureSlot.UNSTICKY, TextureMapping.getBlockTexture(Blocks.PISTON, "_top")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.PISTON, "_side"));
/* 2910 */     TextureMapping stickyTextures = commonMapping.copyAndUpdate(TextureSlot.PLATFORM, TextureMapping.getBlockTexture(Blocks.PISTON, "_top_sticky"));
/* 2911 */     TextureMapping normalTextures = commonMapping.copyAndUpdate(TextureSlot.PLATFORM, TextureMapping.getBlockTexture(Blocks.PISTON, "_top"));
/* 2913 */     this.blockStateOutput.accept(
/* 2914 */         MultiVariantGenerator.dispatch(Blocks.PISTON_HEAD)
/* 2915 */         .with(
/* 2916 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.SHORT, (Property)BlockStateProperties.PISTON_TYPE)
/* 2917 */           .select(Boolean.valueOf(false), (Comparable)PistonType.DEFAULT, plainVariant(ModelTemplates.PISTON_HEAD.createWithSuffix(Blocks.PISTON, "_head", normalTextures, this.modelOutput)))
/* 2918 */           .select(Boolean.valueOf(false), (Comparable)PistonType.STICKY, plainVariant(ModelTemplates.PISTON_HEAD.createWithSuffix(Blocks.PISTON, "_head_sticky", stickyTextures, this.modelOutput)))
/* 2919 */           .select(Boolean.valueOf(true), (Comparable)PistonType.DEFAULT, plainVariant(ModelTemplates.PISTON_HEAD_SHORT.createWithSuffix(Blocks.PISTON, "_head_short", normalTextures, this.modelOutput)))
/* 2920 */           .select(Boolean.valueOf(true), (Comparable)PistonType.STICKY, plainVariant(ModelTemplates.PISTON_HEAD_SHORT.createWithSuffix(Blocks.PISTON, "_head_short_sticky", stickyTextures, this.modelOutput))))
/*      */         
/* 2922 */         .with(ROTATION_FACING));
/*      */   }
/*      */   
/*      */   private void createTrialSpawner() {
/* 2929 */     Block block = Blocks.TRIAL_SPAWNER;
/* 2930 */     TextureMapping inactiveTextures = TextureMapping.trialSpawner(block, "_side_inactive", "_top_inactive");
/* 2931 */     TextureMapping activeTextures = TextureMapping.trialSpawner(block, "_side_active", "_top_active");
/* 2932 */     TextureMapping ejectingRewardTextures = TextureMapping.trialSpawner(block, "_side_active", "_top_ejecting_reward");
/* 2933 */     TextureMapping ominousInactiveTextures = TextureMapping.trialSpawner(block, "_side_inactive_ominous", "_top_inactive_ominous");
/* 2934 */     TextureMapping ominousActiveTextures = TextureMapping.trialSpawner(block, "_side_active_ominous", "_top_active_ominous");
/* 2935 */     TextureMapping ominousEjectingRewardTextures = TextureMapping.trialSpawner(block, "_side_active_ominous", "_top_ejecting_reward_ominous");
/* 2937 */     Identifier inactiveModel = ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.create(block, inactiveTextures, this.modelOutput);
/* 2938 */     MultiVariant inactive = plainVariant(inactiveModel);
/* 2939 */     MultiVariant active = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_active", activeTextures, this.modelOutput));
/* 2940 */     MultiVariant ejectingReward = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_ejecting_reward", ejectingRewardTextures, this.modelOutput));
/* 2941 */     MultiVariant ominousInactive = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_inactive_ominous", ominousInactiveTextures, this.modelOutput));
/* 2942 */     MultiVariant ominousActive = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_active_ominous", ominousActiveTextures, this.modelOutput));
/* 2943 */     MultiVariant ominousEjectingReward = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_ejecting_reward_ominous", ominousEjectingRewardTextures, this.modelOutput));
/* 2945 */     registerSimpleItemModel(block, inactiveModel);
/* 2947 */     this.blockStateOutput.accept(
/* 2948 */         MultiVariantGenerator.dispatch(block)
/* 2949 */         .with(
/* 2950 */           PropertyDispatch.initial((Property)BlockStateProperties.TRIAL_SPAWNER_STATE, (Property)BlockStateProperties.OMINOUS)
/* 2951 */           .generate((state, ominous) -> {
/*      */               switch (state) {
/*      */                 default:
/*      */                   throw new MatchException(null, null);
/*      */                 case DOWN_NORTH:
/*      */                 case DOWN_SOUTH:
/*      */                   return ominous.booleanValue() ? ominousInactive : inactive;
/*      */                 case DOWN_WEST:
/*      */                 case DOWN_EAST:
/*      */                 case UP_NORTH:
/*      */                   return ominous.booleanValue() ? ominousActive : active;
/*      */                 case UP_SOUTH:
/*      */                   break;
/*      */               } 
/*      */               return ominous.booleanValue() ? ominousEjectingReward : ejectingReward;
/*      */             })));
/*      */   }
/*      */   
/*      */   private void createVault() {
/* 2962 */     Block block = Blocks.VAULT;
/* 2963 */     TextureMapping inactiveTextures = TextureMapping.vault(block, "_front_off", "_side_off", "_top", "_bottom");
/* 2964 */     TextureMapping activeTextures = TextureMapping.vault(block, "_front_on", "_side_on", "_top", "_bottom");
/* 2965 */     TextureMapping unlockingTextures = TextureMapping.vault(block, "_front_ejecting", "_side_on", "_top", "_bottom");
/* 2966 */     TextureMapping ejectingRewardTextures = TextureMapping.vault(block, "_front_ejecting", "_side_on", "_top_ejecting", "_bottom");
/* 2968 */     Identifier inactiveModel = ModelTemplates.VAULT.create(block, inactiveTextures, this.modelOutput);
/* 2969 */     MultiVariant inactive = plainVariant(inactiveModel);
/* 2970 */     MultiVariant active = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_active", activeTextures, this.modelOutput));
/* 2971 */     MultiVariant unlocking = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_unlocking", unlockingTextures, this.modelOutput));
/* 2972 */     MultiVariant ejectingReward = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_ejecting_reward", ejectingRewardTextures, this.modelOutput));
/* 2974 */     TextureMapping inactiveTexturesOminous = TextureMapping.vault(block, "_front_off_ominous", "_side_off_ominous", "_top_ominous", "_bottom_ominous");
/* 2975 */     TextureMapping activeTexturesOminous = TextureMapping.vault(block, "_front_on_ominous", "_side_on_ominous", "_top_ominous", "_bottom_ominous");
/* 2976 */     TextureMapping unlockingTexturesOminous = TextureMapping.vault(block, "_front_ejecting_ominous", "_side_on_ominous", "_top_ominous", "_bottom_ominous");
/* 2977 */     TextureMapping ejectingRewardTexturesOminous = TextureMapping.vault(block, "_front_ejecting_ominous", "_side_on_ominous", "_top_ejecting_ominous", "_bottom_ominous");
/* 2979 */     MultiVariant inactiveOminous = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_ominous", inactiveTexturesOminous, this.modelOutput));
/* 2980 */     MultiVariant activeOminous = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_active_ominous", activeTexturesOminous, this.modelOutput));
/* 2981 */     MultiVariant unlockingOminous = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_unlocking_ominous", unlockingTexturesOminous, this.modelOutput));
/* 2982 */     MultiVariant ejectingRewardOminous = plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_ejecting_reward_ominous", ejectingRewardTexturesOminous, this.modelOutput));
/* 2984 */     registerSimpleItemModel(block, inactiveModel);
/* 2985 */     this.blockStateOutput.accept(
/* 2986 */         MultiVariantGenerator.dispatch(block)
/* 2987 */         .with(
/* 2988 */           PropertyDispatch.initial(VaultBlock.STATE, (Property)VaultBlock.OMINOUS)
/* 2989 */           .generate((state, ominous) -> {
/*      */               switch (state) {
/*      */                 default:
/*      */                   throw new MatchException(null, null);
/*      */                 case DOWN_NORTH:
/*      */                   return ominous.booleanValue() ? inactiveOminous : inactive;
/*      */                 case DOWN_SOUTH:
/*      */                   return ominous.booleanValue() ? activeOminous : active;
/*      */                 case DOWN_WEST:
/*      */                   return ominous.booleanValue() ? unlockingOminous : unlocking;
/*      */                 case DOWN_EAST:
/*      */                   break;
/*      */               } 
/*      */               return ominous.booleanValue() ? ejectingRewardOminous : ejectingReward;
/* 2996 */             })).with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */   
/*      */   private void createSculkSensor() {
/* 3001 */     Identifier inactiveModel = ModelLocationUtils.getModelLocation(Blocks.SCULK_SENSOR, "_inactive");
/* 3002 */     MultiVariant inactive = plainVariant(inactiveModel);
/* 3003 */     MultiVariant active = plainVariant(ModelLocationUtils.getModelLocation(Blocks.SCULK_SENSOR, "_active"));
/* 3004 */     registerSimpleItemModel(Blocks.SCULK_SENSOR, inactiveModel);
/* 3005 */     this.blockStateOutput.accept(
/* 3006 */         MultiVariantGenerator.dispatch(Blocks.SCULK_SENSOR)
/* 3007 */         .with(
/* 3008 */           PropertyDispatch.initial((Property)BlockStateProperties.SCULK_SENSOR_PHASE)
/* 3009 */           .generate(phase -> (phase == SculkSensorPhase.ACTIVE || phase == SculkSensorPhase.COOLDOWN) ? active : inactive)));
/*      */   }
/*      */   
/*      */   private void createCalibratedSculkSensor() {
/* 3015 */     Identifier inactiveModel = ModelLocationUtils.getModelLocation(Blocks.CALIBRATED_SCULK_SENSOR, "_inactive");
/* 3016 */     MultiVariant inactive = plainVariant(inactiveModel);
/* 3017 */     MultiVariant active = plainVariant(ModelLocationUtils.getModelLocation(Blocks.CALIBRATED_SCULK_SENSOR, "_active"));
/* 3018 */     registerSimpleItemModel(Blocks.CALIBRATED_SCULK_SENSOR, inactiveModel);
/* 3019 */     this.blockStateOutput.accept(
/* 3020 */         MultiVariantGenerator.dispatch(Blocks.CALIBRATED_SCULK_SENSOR)
/* 3021 */         .with(
/* 3022 */           PropertyDispatch.initial((Property)BlockStateProperties.SCULK_SENSOR_PHASE)
/* 3023 */           .generate(phase -> (phase == SculkSensorPhase.ACTIVE || phase == SculkSensorPhase.COOLDOWN) ? active : inactive))
/*      */         
/* 3025 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */   
/*      */   private void createSculkShrieker() {
/* 3030 */     Identifier sculkShriekerModel = ModelTemplates.SCULK_SHRIEKER.create(Blocks.SCULK_SHRIEKER, TextureMapping.sculkShrieker(false), this.modelOutput);
/* 3031 */     MultiVariant sculkShrieker = plainVariant(sculkShriekerModel);
/* 3032 */     MultiVariant sculkShriekerCanSummon = plainVariant(ModelTemplates.SCULK_SHRIEKER.createWithSuffix(Blocks.SCULK_SHRIEKER, "_can_summon", TextureMapping.sculkShrieker(true), this.modelOutput));
/* 3034 */     registerSimpleItemModel(Blocks.SCULK_SHRIEKER, sculkShriekerModel);
/* 3035 */     this.blockStateOutput.accept(
/* 3036 */         MultiVariantGenerator.dispatch(Blocks.SCULK_SHRIEKER)
/* 3037 */         .with(createBooleanModelDispatch(BlockStateProperties.CAN_SUMMON, sculkShriekerCanSummon, sculkShrieker)));
/*      */   }
/*      */   
/*      */   private void createScaffolding() {
/* 3042 */     Identifier stableModel = ModelLocationUtils.getModelLocation(Blocks.SCAFFOLDING, "_stable");
/* 3043 */     MultiVariant stable = plainVariant(stableModel);
/* 3044 */     MultiVariant unstable = plainVariant(ModelLocationUtils.getModelLocation(Blocks.SCAFFOLDING, "_unstable"));
/* 3045 */     registerSimpleItemModel(Blocks.SCAFFOLDING, stableModel);
/* 3046 */     this.blockStateOutput.accept(
/* 3047 */         MultiVariantGenerator.dispatch(Blocks.SCAFFOLDING)
/* 3048 */         .with(createBooleanModelDispatch(BlockStateProperties.BOTTOM, unstable, stable)));
/*      */   }
/*      */   
/*      */   private void createCaveVines() {
/* 3053 */     MultiVariant offHead = plainVariant(createSuffixedVariant(Blocks.CAVE_VINES, "", ModelTemplates.CROSS, TextureMapping::cross));
/* 3054 */     MultiVariant onHead = plainVariant(createSuffixedVariant(Blocks.CAVE_VINES, "_lit", ModelTemplates.CROSS, TextureMapping::cross));
/* 3056 */     this.blockStateOutput.accept(
/* 3057 */         MultiVariantGenerator.dispatch(Blocks.CAVE_VINES)
/* 3058 */         .with(createBooleanModelDispatch(BlockStateProperties.BERRIES, onHead, offHead)));
/* 3061 */     MultiVariant offBody = plainVariant(createSuffixedVariant(Blocks.CAVE_VINES_PLANT, "", ModelTemplates.CROSS, TextureMapping::cross));
/* 3062 */     MultiVariant onBody = plainVariant(createSuffixedVariant(Blocks.CAVE_VINES_PLANT, "_lit", ModelTemplates.CROSS, TextureMapping::cross));
/* 3064 */     this.blockStateOutput.accept(
/* 3065 */         MultiVariantGenerator.dispatch(Blocks.CAVE_VINES_PLANT)
/* 3066 */         .with(createBooleanModelDispatch(BlockStateProperties.BERRIES, onBody, offBody)));
/*      */   }
/*      */   
/*      */   private void createRedstoneLamp() {
/* 3071 */     MultiVariant off = plainVariant(TexturedModel.CUBE.create(Blocks.REDSTONE_LAMP, this.modelOutput));
/* 3072 */     MultiVariant on = plainVariant(createSuffixedVariant(Blocks.REDSTONE_LAMP, "_on", ModelTemplates.CUBE_ALL, TextureMapping::cube));
/* 3074 */     this.blockStateOutput.accept(
/* 3075 */         MultiVariantGenerator.dispatch(Blocks.REDSTONE_LAMP)
/* 3076 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, on, off)));
/*      */   }
/*      */   
/*      */   private void createNormalTorch(Block ground, Block wall) {
/* 3081 */     TextureMapping textures = TextureMapping.torch(ground);
/* 3083 */     this.blockStateOutput.accept(createSimpleBlock(ground, plainVariant(ModelTemplates.TORCH.create(ground, textures, this.modelOutput))));
/* 3085 */     this.blockStateOutput.accept(
/* 3086 */         MultiVariantGenerator.dispatch(wall, plainVariant(ModelTemplates.WALL_TORCH.create(wall, textures, this.modelOutput)))
/* 3087 */         .with(ROTATION_TORCH));
/* 3089 */     registerSimpleFlatItemModel(ground);
/*      */   }
/*      */   
/*      */   private void createRedstoneTorch() {
/* 3093 */     TextureMapping onTextures = TextureMapping.torch(Blocks.REDSTONE_TORCH);
/* 3094 */     TextureMapping offTextures = TextureMapping.torch(TextureMapping.getBlockTexture(Blocks.REDSTONE_TORCH, "_off"));
/* 3096 */     MultiVariant groundModelOn = plainVariant(ModelTemplates.REDSTONE_TORCH.create(Blocks.REDSTONE_TORCH, onTextures, this.modelOutput));
/* 3097 */     MultiVariant groundModelOff = plainVariant(ModelTemplates.TORCH_UNLIT.createWithSuffix(Blocks.REDSTONE_TORCH, "_off", offTextures, this.modelOutput));
/* 3099 */     this.blockStateOutput.accept(
/* 3100 */         MultiVariantGenerator.dispatch(Blocks.REDSTONE_TORCH)
/* 3101 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, groundModelOn, groundModelOff)));
/* 3104 */     MultiVariant wallModelOn = plainVariant(ModelTemplates.REDSTONE_WALL_TORCH.create(Blocks.REDSTONE_WALL_TORCH, onTextures, this.modelOutput));
/* 3105 */     MultiVariant wallModelOff = plainVariant(ModelTemplates.WALL_TORCH_UNLIT.createWithSuffix(Blocks.REDSTONE_WALL_TORCH, "_off", offTextures, this.modelOutput));
/* 3107 */     this.blockStateOutput.accept(
/* 3108 */         MultiVariantGenerator.dispatch(Blocks.REDSTONE_WALL_TORCH)
/* 3109 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, wallModelOn, wallModelOff))
/* 3110 */         .with(ROTATION_TORCH));
/* 3112 */     registerSimpleFlatItemModel(Blocks.REDSTONE_TORCH);
/*      */   }
/*      */   
/*      */   private void createRepeater() {
/* 3116 */     registerSimpleFlatItemModel(Items.REPEATER);
/* 3117 */     this.blockStateOutput.accept(
/* 3118 */         MultiVariantGenerator.dispatch(Blocks.REPEATER)
/* 3119 */         .with(
/* 3120 */           PropertyDispatch.initial((Property)BlockStateProperties.DELAY, (Property)BlockStateProperties.LOCKED, (Property)BlockStateProperties.POWERED)
/* 3121 */           .generate((delay, locked, powered) -> {
/*      */               StringBuilder suffix = new StringBuilder();
/*      */               suffix.append('_').append(delay).append("tick");
/*      */               if (powered.booleanValue())
/*      */                 suffix.append("_on"); 
/*      */               if (locked.booleanValue())
/*      */                 suffix.append("_locked"); 
/*      */               return plainVariant(ModelLocationUtils.getModelLocation(Blocks.REPEATER, suffix.toString()));
/* 3133 */             })).with(ROTATION_HORIZONTAL_FACING_ALT));
/*      */   }
/*      */   
/*      */   private void createSeaPickle() {
/* 3138 */     registerSimpleFlatItemModel(Items.SEA_PICKLE);
/* 3140 */     this.blockStateOutput.accept(
/* 3141 */         MultiVariantGenerator.dispatch(Blocks.SEA_PICKLE)
/* 3142 */         .with(
/* 3143 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.PICKLES, (Property)BlockStateProperties.WATERLOGGED)
/* 3144 */           .select(Integer.valueOf(1), Boolean.valueOf(false), createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("dead_sea_pickle"))))
/* 3145 */           .select(Integer.valueOf(2), Boolean.valueOf(false), createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("two_dead_sea_pickles"))))
/* 3146 */           .select(Integer.valueOf(3), Boolean.valueOf(false), createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("three_dead_sea_pickles"))))
/* 3147 */           .select(Integer.valueOf(4), Boolean.valueOf(false), createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("four_dead_sea_pickles"))))
/*      */           
/* 3149 */           .select(Integer.valueOf(1), Boolean.valueOf(true), createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("sea_pickle"))))
/* 3150 */           .select(Integer.valueOf(2), Boolean.valueOf(true), createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("two_sea_pickles"))))
/* 3151 */           .select(Integer.valueOf(3), Boolean.valueOf(true), createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("three_sea_pickles"))))
/* 3152 */           .select(Integer.valueOf(4), Boolean.valueOf(true), createRotatedVariants(plainModel(ModelLocationUtils.decorateBlockModelLocation("four_sea_pickles"))))));
/*      */   }
/*      */   
/*      */   private void createSnowBlocks() {
/* 3159 */     TextureMapping textures = TextureMapping.cube(Blocks.SNOW);
/* 3160 */     MultiVariant snowModel = plainVariant(ModelTemplates.CUBE_ALL.create(Blocks.SNOW_BLOCK, textures, this.modelOutput));
/* 3162 */     this.blockStateOutput.accept(
/* 3163 */         MultiVariantGenerator.dispatch(Blocks.SNOW)
/* 3164 */         .with(
/* 3165 */           PropertyDispatch.initial((Property)BlockStateProperties.LAYERS)
/* 3166 */           .generate(level -> (level.intValue() < 8) ? plainVariant(ModelLocationUtils.getModelLocation(Blocks.SNOW, "_height" + level.intValue() * 2)) : snowModel)));
/* 3170 */     registerSimpleItemModel(Blocks.SNOW, ModelLocationUtils.getModelLocation(Blocks.SNOW, "_height2"));
/* 3171 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.SNOW_BLOCK, snowModel));
/*      */   }
/*      */   
/*      */   private void createStonecutter() {
/* 3175 */     this.blockStateOutput.accept(
/* 3176 */         MultiVariantGenerator.dispatch(Blocks.STONECUTTER, plainVariant(ModelLocationUtils.getModelLocation(Blocks.STONECUTTER)))
/* 3177 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */   
/*      */   private void createStructureBlock() {
/* 3182 */     Identifier inventory = TexturedModel.CUBE.create(Blocks.STRUCTURE_BLOCK, this.modelOutput);
/* 3183 */     registerSimpleItemModel(Blocks.STRUCTURE_BLOCK, inventory);
/* 3185 */     this.blockStateOutput.accept(
/* 3186 */         MultiVariantGenerator.dispatch(Blocks.STRUCTURE_BLOCK)
/* 3187 */         .with(
/* 3188 */           PropertyDispatch.initial((Property)BlockStateProperties.STRUCTUREBLOCK_MODE)
/* 3189 */           .generate(model -> plainVariant(createSuffixedVariant(Blocks.STRUCTURE_BLOCK, "_" + model.getSerializedName(), ModelTemplates.CUBE_ALL, TextureMapping::cube)))));
/*      */   }
/*      */   
/*      */   private void createTestBlock() {
/* 3195 */     Map<TestBlockMode, Identifier> variantIds = new HashMap<>();
/* 3196 */     for (TestBlockMode mode : TestBlockMode.values())
/* 3197 */       variantIds.put(mode, createSuffixedVariant(Blocks.TEST_BLOCK, "_" + mode.getSerializedName(), ModelTemplates.CUBE_ALL, TextureMapping::cube)); 
/* 3200 */     this.blockStateOutput.accept(
/* 3201 */         MultiVariantGenerator.dispatch(Blocks.TEST_BLOCK)
/* 3202 */         .with(
/* 3203 */           PropertyDispatch.initial((Property)BlockStateProperties.TEST_BLOCK_MODE)
/* 3204 */           .generate(mode -> plainVariant((Identifier)variantIds.get(mode)))));
/* 3208 */     this.itemModelOutput.accept(Items.TEST_BLOCK, ItemModelUtils.selectBlockItemProperty((Property)TestBlock.MODE, 
/*      */           
/* 3210 */           ItemModelUtils.plainModel(variantIds.get(TestBlockMode.START)), 
/* 3211 */           Map.of(TestBlockMode.FAIL, 
/* 3212 */             ItemModelUtils.plainModel(variantIds.get(TestBlockMode.FAIL)), TestBlockMode.LOG, 
/* 3213 */             ItemModelUtils.plainModel(variantIds.get(TestBlockMode.LOG)), TestBlockMode.ACCEPT, 
/* 3214 */             ItemModelUtils.plainModel(variantIds.get(TestBlockMode.ACCEPT)))));
/*      */   }
/*      */   
/*      */   private void createSweetBerryBush() {
/* 3220 */     registerSimpleFlatItemModel(Items.SWEET_BERRIES);
/* 3221 */     this.blockStateOutput.accept(
/* 3222 */         MultiVariantGenerator.dispatch(Blocks.SWEET_BERRY_BUSH)
/* 3223 */         .with(
/* 3224 */           PropertyDispatch.initial((Property)BlockStateProperties.AGE_3)
/* 3225 */           .generate(age -> plainVariant(createSuffixedVariant(Blocks.SWEET_BERRY_BUSH, "_stage" + age, ModelTemplates.CROSS, TextureMapping::cross)))));
/*      */   }
/*      */   
/*      */   private void createTripwire() {
/* 3233 */     registerSimpleFlatItemModel(Items.STRING);
/* 3234 */     this.blockStateOutput.accept(
/* 3235 */         MultiVariantGenerator.dispatch(Blocks.TRIPWIRE)
/* 3236 */         .with(
/* 3237 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.ATTACHED, (Property)BlockStateProperties.EAST, (Property)BlockStateProperties.NORTH, (Property)BlockStateProperties.SOUTH, (Property)BlockStateProperties.WEST)
/*      */           
/* 3239 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ns")))
/*      */           
/* 3242 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")).with(Y_ROT_90))
/* 3243 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")))
/* 3244 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")).with(Y_ROT_180))
/* 3245 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")).with(Y_ROT_270))
/*      */           
/* 3248 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")))
/* 3249 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")).with(Y_ROT_90))
/* 3250 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")).with(Y_ROT_180))
/* 3251 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")).with(Y_ROT_270))
/* 3252 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ns")))
/* 3253 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ns")).with(Y_ROT_90))
/*      */           
/* 3256 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")))
/* 3257 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")).with(Y_ROT_90))
/* 3258 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")).with(Y_ROT_180))
/* 3259 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")).with(Y_ROT_270))
/*      */           
/* 3262 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nsew")))
/*      */           
/* 3265 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ns")))
/*      */           
/* 3268 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")))
/* 3269 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")).with(Y_ROT_180))
/* 3270 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")).with(Y_ROT_90))
/* 3271 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")).with(Y_ROT_270))
/*      */           
/* 3274 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")))
/* 3275 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")).with(Y_ROT_90))
/* 3276 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")).with(Y_ROT_180))
/* 3277 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")).with(Y_ROT_270))
/* 3278 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ns")))
/* 3279 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ns")).with(Y_ROT_90))
/*      */           
/* 3282 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")))
/* 3283 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")).with(Y_ROT_90))
/* 3284 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")).with(Y_ROT_180))
/* 3285 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")).with(Y_ROT_270))
/*      */           
/* 3288 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nsew")))));
/*      */   }
/*      */   
/*      */   private void createTripwireHook() {
/* 3294 */     registerSimpleFlatItemModel(Blocks.TRIPWIRE_HOOK);
/* 3295 */     this.blockStateOutput.accept(
/* 3296 */         MultiVariantGenerator.dispatch(Blocks.TRIPWIRE_HOOK)
/* 3297 */         .with(
/* 3298 */           PropertyDispatch.initial((Property)BlockStateProperties.ATTACHED, (Property)BlockStateProperties.POWERED)
/* 3299 */           .generate((attached, powered) -> plainVariant(ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE_HOOK, (attached.booleanValue() ? "_attached" : "") + (attached.booleanValue() ? "_attached" : "")))))
/*      */         
/* 3301 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */   
/*      */   private Variant createTurtleEggModel(int count, String hatchProgress, TextureMapping texture) {
/* 3306 */     switch (count) {
/*      */       case 1:
/*      */       
/*      */       case 2:
/*      */       
/*      */       case 3:
/*      */       
/*      */       case 4:
/*      */       
/*      */     } 
/* 3315 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   private Variant createTurtleEggModel(int eggs, int hatch) {
/* 3320 */     switch (hatch) {
/*      */       case 0:
/*      */       
/*      */       case 1:
/*      */       
/*      */       case 2:
/*      */       
/*      */     } 
/* 3326 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   private void createTurtleEgg() {
/* 3331 */     registerSimpleFlatItemModel(Items.TURTLE_EGG);
/* 3332 */     this.blockStateOutput.accept(
/* 3333 */         MultiVariantGenerator.dispatch(Blocks.TURTLE_EGG)
/* 3334 */         .with(
/* 3335 */           PropertyDispatch.initial((Property)BlockStateProperties.EGGS, (Property)BlockStateProperties.HATCH)
/* 3336 */           .generate((eggs, hatch) -> createRotatedVariants(createTurtleEggModel(eggs.intValue(), hatch.intValue())))));
/*      */   }
/*      */   
/*      */   private void createDriedGhastBlock() {
/* 3342 */     Identifier driedGhast = ModelLocationUtils.getModelLocation(Blocks.DRIED_GHAST, "_hydration_0");
/* 3343 */     registerSimpleItemModel(Blocks.DRIED_GHAST, driedGhast);
/* 3345 */     Function<Integer, Identifier> createModel = stage -> {
/*      */         switch (stage.intValue()) {
/*      */           case 1:
/*      */           
/*      */           case 2:
/*      */           
/*      */           case 3:
/*      */           
/*      */         } 
/*      */         String suffix = "_hydration_0";
/*      */         TextureMapping texture = TextureMapping.driedGhast(suffix);
/*      */         return ModelTemplates.DRIED_GHAST.createWithSuffix(Blocks.DRIED_GHAST, suffix, texture, this.modelOutput);
/*      */       };
/* 3358 */     this.blockStateOutput.accept(
/* 3359 */         MultiVariantGenerator.dispatch(Blocks.DRIED_GHAST)
/* 3360 */         .with(PropertyDispatch.initial((Property)DriedGhastBlock.HYDRATION_LEVEL)
/* 3361 */           .generate(stage -> plainVariant(createModel.apply(stage))))
/* 3362 */         .with(ROTATION_HORIZONTAL_FACING));
/*      */   }
/*      */   
/*      */   private void createSnifferEgg() {
/* 3367 */     registerSimpleFlatItemModel(Items.SNIFFER_EGG);
/* 3369 */     this.blockStateOutput.accept(
/* 3370 */         MultiVariantGenerator.dispatch(Blocks.SNIFFER_EGG)
/* 3371 */         .with(PropertyDispatch.initial((Property)SnifferEggBlock.HATCH)
/* 3372 */           .generate(stage -> {
/*      */               switch (stage.intValue()) {
/*      */                 case 1:
/*      */                 
/*      */                 case 2:
/*      */                 
/*      */               } 
/*      */               String suffix = "_not_cracked";
/*      */               TextureMapping texture = TextureMapping.snifferEgg(suffix);
/*      */               return plainVariant(ModelTemplates.SNIFFER_EGG.createWithSuffix(Blocks.SNIFFER_EGG, suffix, texture, this.modelOutput));
/*      */             })));
/*      */   }
/*      */   
/*      */   private void createMultiface(Block block) {
/* 3388 */     registerSimpleFlatItemModel(block);
/* 3389 */     createMultifaceBlockStates(block);
/*      */   }
/*      */   
/*      */   private void createMultiface(Block block, Item item) {
/* 3393 */     registerSimpleFlatItemModel(item);
/* 3394 */     createMultifaceBlockStates(block);
/*      */   }
/*      */   
/* 3399 */   private static final Map<Direction, VariantMutator> MULTIFACE_GENERATOR = (Map<Direction, VariantMutator>)ImmutableMap.of(Direction.NORTH, NOP, Direction.EAST, Y_ROT_90
/*      */       
/* 3401 */       .then(UV_LOCK), Direction.SOUTH, Y_ROT_180
/* 3402 */       .then(UV_LOCK), Direction.WEST, Y_ROT_270
/* 3403 */       .then(UV_LOCK), Direction.UP, X_ROT_270
/* 3404 */       .then(UV_LOCK), Direction.DOWN, X_ROT_90
/* 3405 */       .then(UV_LOCK));
/*      */   
/*      */   private static <T extends Property<?>> Map<T, VariantMutator> selectMultifaceProperties(StateHolder<?, ?> holder, Function<Direction, T> converter) {
/* 3410 */     ImmutableMap.Builder<T, VariantMutator> result = ImmutableMap.builderWithExpectedSize(MULTIFACE_GENERATOR.size());
/* 3411 */     MULTIFACE_GENERATOR.forEach((direction, mutator) -> {
/*      */           Property property = converter.apply(direction);
/*      */           if (holder.hasProperty(property))
/*      */             result.put(property, mutator); 
/*      */         });
/* 3417 */     return (Map<T, VariantMutator>)result.build();
/*      */   }
/*      */   
/*      */   private void createMultifaceBlockStates(Block block) {
/* 3421 */     Map<Property<Boolean>, VariantMutator> directionProperties = selectMultifaceProperties((StateHolder<?, ?>)block.defaultBlockState(), MultifaceBlock::getFaceProperty);
/* 3423 */     ConditionBuilder noFaces = condition();
/* 3424 */     directionProperties.forEach((property, mutator) -> noFaces.term(property, Boolean.valueOf(false)));
/* 3426 */     MultiVariant model = plainVariant(ModelLocationUtils.getModelLocation(block));
/* 3428 */     MultiPartGenerator generator = MultiPartGenerator.multiPart(block);
/* 3429 */     directionProperties.forEach((property, mutator) -> {
/*      */           generator.with(condition().term(property, Boolean.valueOf(true)), model.with(mutator));
/*      */           generator.with(noFaces, model.with(mutator));
/*      */         });
/* 3434 */     this.blockStateOutput.accept(generator);
/*      */   }
/*      */   
/*      */   private void createMossyCarpet(Block block) {
/* 3438 */     Map<Property<WallSide>, VariantMutator> directionProperties = selectMultifaceProperties((StateHolder<?, ?>)block.defaultBlockState(), MossyCarpetBlock::getPropertyForFace);
/* 3440 */     ConditionBuilder noFaces = condition().term((Property)MossyCarpetBlock.BASE, Boolean.valueOf(false));
/* 3441 */     directionProperties.forEach((property, mutator) -> noFaces.term(property, (Comparable)WallSide.NONE));
/* 3443 */     MultiVariant modelCarpet = plainVariant(TexturedModel.CARPET.create(block, this.modelOutput));
/* 3444 */     MultiVariant modelSideTall = plainVariant(TexturedModel.MOSSY_CARPET_SIDE.get(block).updateTextures(m -> m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_tall"))).createWithSuffix(block, "_side_tall", this.modelOutput));
/* 3445 */     MultiVariant modelSideSmall = plainVariant(TexturedModel.MOSSY_CARPET_SIDE.get(block).updateTextures(m -> m.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_small"))).createWithSuffix(block, "_side_small", this.modelOutput));
/* 3447 */     MultiPartGenerator generator = MultiPartGenerator.multiPart(block);
/* 3448 */     generator.with(condition().term((Property)MossyCarpetBlock.BASE, Boolean.valueOf(true)), modelCarpet);
/* 3449 */     generator.with(noFaces, modelCarpet);
/* 3450 */     directionProperties.forEach((property, mutator) -> {
/*      */           generator.with(condition().term(property, (Comparable)WallSide.TALL), modelSideTall.with(mutator));
/*      */           generator.with(condition().term(property, (Comparable)WallSide.LOW), modelSideSmall.with(mutator));
/*      */           generator.with(noFaces, modelSideTall.with(mutator));
/*      */         });
/* 3456 */     this.blockStateOutput.accept(generator);
/*      */   }
/*      */   
/*      */   private void createHangingMoss(Block block) {
/* 3460 */     registerSimpleFlatItemModel(block);
/* 3461 */     this.blockStateOutput.accept(
/* 3462 */         MultiVariantGenerator.dispatch(block)
/* 3463 */         .with(
/* 3464 */           PropertyDispatch.initial((Property)HangingMossBlock.TIP)
/* 3465 */           .generate(isTip -> {
/*      */               String suffix = isTip.booleanValue() ? "_tip" : "";
/*      */               TextureMapping texture = TextureMapping.cross(TextureMapping.getBlockTexture(block, suffix));
/*      */               return plainVariant(PlantType.NOT_TINTED.getCross().createWithSuffix(block, suffix, texture, this.modelOutput));
/*      */             })));
/*      */   }
/*      */   
/*      */   private void createSculkCatalyst() {
/* 3475 */     Material bottom = TextureMapping.getBlockTexture(Blocks.SCULK_CATALYST, "_bottom");
/* 3479 */     TextureMapping defaultTextureMap = (new TextureMapping()).put(TextureSlot.BOTTOM, bottom).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.SCULK_CATALYST, "_top")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.SCULK_CATALYST, "_side"));
/* 3484 */     TextureMapping bloomTextureMap = (new TextureMapping()).put(TextureSlot.BOTTOM, bottom).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.SCULK_CATALYST, "_top_bloom")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.SCULK_CATALYST, "_side_bloom"));
/* 3486 */     Identifier defaultModel = ModelTemplates.CUBE_BOTTOM_TOP.create(Blocks.SCULK_CATALYST, defaultTextureMap, this.modelOutput);
/* 3487 */     MultiVariant defaultVariant = plainVariant(defaultModel);
/* 3488 */     MultiVariant bloom = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.SCULK_CATALYST, "_bloom", bloomTextureMap, this.modelOutput));
/* 3490 */     this.blockStateOutput.accept(
/* 3491 */         MultiVariantGenerator.dispatch(Blocks.SCULK_CATALYST)
/* 3492 */         .with(
/* 3493 */           PropertyDispatch.initial((Property)BlockStateProperties.BLOOM)
/* 3494 */           .generate(pulse -> pulse.booleanValue() ? bloom : defaultVariant)));
/* 3497 */     registerSimpleItemModel(Blocks.SCULK_CATALYST, defaultModel);
/*      */   }
/*      */   
/*      */   private void createShelf(Block block, Block particle) {
/* 3501 */     TextureMapping mapping = (new TextureMapping()).put(TextureSlot.ALL, TextureMapping.getBlockTexture(block)).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(particle));
/* 3502 */     MultiPartGenerator generator = MultiPartGenerator.multiPart(block);
/* 3504 */     addShelfPart(block, mapping, generator, ModelTemplates.SHELF_BODY, null, null);
/* 3505 */     addShelfPart(block, mapping, generator, ModelTemplates.SHELF_UNPOWERED, Boolean.valueOf(false), null);
/* 3507 */     addShelfPart(block, mapping, generator, ModelTemplates.SHELF_UNCONNECTED, Boolean.valueOf(true), SideChainPart.UNCONNECTED);
/* 3508 */     addShelfPart(block, mapping, generator, ModelTemplates.SHELF_LEFT, Boolean.valueOf(true), SideChainPart.LEFT);
/* 3509 */     addShelfPart(block, mapping, generator, ModelTemplates.SHELF_CENTER, Boolean.valueOf(true), SideChainPart.CENTER);
/* 3510 */     addShelfPart(block, mapping, generator, ModelTemplates.SHELF_RIGHT, Boolean.valueOf(true), SideChainPart.RIGHT);
/* 3512 */     this.blockStateOutput.accept(generator);
/* 3513 */     registerSimpleItemModel(block, ModelTemplates.SHELF_INVENTORY.create(block, mapping, this.modelOutput));
/*      */   }
/*      */   
/*      */   private void addShelfPart(Block block, TextureMapping mapping, MultiPartGenerator generator, ModelTemplate template, Boolean isPowered, SideChainPart sideChainPart) {
/* 3517 */     MultiVariant variant = plainVariant(template.create(block, mapping, this.modelOutput));
/* 3518 */     forEachHorizontalDirection((direction, rotation) -> generator.with(shelfCondition(direction, isPowered, sideChainPart), variant.with(rotation)));
/*      */   }
/*      */   
/*      */   private static void forEachHorizontalDirection(BiConsumer<Direction, VariantMutator> consumer) {
/* 3523 */     List.<Pair>of(
/* 3524 */         Pair.of(Direction.NORTH, NOP), 
/* 3525 */         Pair.of(Direction.EAST, Y_ROT_90), 
/* 3526 */         Pair.of(Direction.SOUTH, Y_ROT_180), 
/* 3527 */         Pair.of(Direction.WEST, Y_ROT_270))
/* 3528 */       .forEach(pair -> {
/*      */           Direction direction = (Direction)pair.getFirst();
/*      */           VariantMutator rotation = (VariantMutator)pair.getSecond();
/*      */           consumer.accept(direction, rotation);
/*      */         });
/*      */   }
/*      */   
/*      */   private static Condition shelfCondition(Direction direction, Boolean isPowered, SideChainPart sideChainPart) {
/* 3536 */     ConditionBuilder facing = condition(BlockStateProperties.HORIZONTAL_FACING, direction, new Direction[0]);
/* 3537 */     if (isPowered == null)
/* 3538 */       return facing.build(); 
/* 3541 */     ConditionBuilder powered = condition(BlockStateProperties.POWERED, isPowered.booleanValue());
/* 3542 */     return (sideChainPart != null) ? 
/* 3543 */       and(new ConditionBuilder[] { facing, powered, condition(BlockStateProperties.SIDE_CHAIN_PART, sideChainPart, new SideChainPart[0]) }) : and(new ConditionBuilder[] { facing, powered });
/*      */   }
/*      */   
/*      */   private void createChiseledBookshelf() {
/* 3548 */     Block block = Blocks.CHISELED_BOOKSHELF;
/* 3549 */     MultiVariant body = plainVariant(ModelLocationUtils.getModelLocation(block));
/* 3550 */     MultiPartGenerator multiPartGenerator = MultiPartGenerator.multiPart(block);
/* 3552 */     forEachHorizontalDirection((direction, rotation) -> {
/*      */           Condition facingCondition = condition().term((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)direction).build();
/*      */           multiPartGenerator.with(facingCondition, body.with(rotation).with(UV_LOCK));
/*      */           addSlotStateAndRotationVariants(multiPartGenerator, facingCondition, rotation);
/*      */         });
/* 3558 */     this.blockStateOutput.accept(multiPartGenerator);
/* 3559 */     registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block, "_inventory"));
/* 3560 */     CHISELED_BOOKSHELF_SLOT_MODEL_CACHE.clear();
/*      */   }
/*      */   
/*      */   private void addSlotStateAndRotationVariants(MultiPartGenerator multiPartGenerator, Condition facingCondition, VariantMutator mutator) {
/* 3564 */     List.<Pair>of(
/* 3565 */         Pair.of(ChiseledBookShelfBlock.SLOT_0_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_LEFT), 
/* 3566 */         Pair.of(ChiseledBookShelfBlock.SLOT_1_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_MID), 
/* 3567 */         Pair.of(ChiseledBookShelfBlock.SLOT_2_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_RIGHT), 
/* 3568 */         Pair.of(ChiseledBookShelfBlock.SLOT_3_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_LEFT), 
/* 3569 */         Pair.of(ChiseledBookShelfBlock.SLOT_4_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_MID), 
/* 3570 */         Pair.of(ChiseledBookShelfBlock.SLOT_5_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_RIGHT))
/* 3571 */       .forEach(pair -> {
/*      */           BooleanProperty stateProperty = (BooleanProperty)pair.getFirst();
/*      */           ModelTemplate modelTemplate = (ModelTemplate)pair.getSecond();
/*      */           addBookSlotModel(multiPartGenerator, facingCondition, mutator, stateProperty, modelTemplate, true);
/*      */           addBookSlotModel(multiPartGenerator, facingCondition, mutator, stateProperty, modelTemplate, false);
/*      */         });
/*      */   }
/*      */   
/*      */   private static final class BookSlotModelCacheKey extends Record {
/*      */     private final ModelTemplate template;
/*      */     
/*      */     private final String modelSuffix;
/*      */     
/*      */     private BookSlotModelCacheKey(ModelTemplate template, String modelSuffix) {
/* 3579 */       this.template = template;
/* 3579 */       this.modelSuffix = modelSuffix;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #3579	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #3579	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #3579	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public ModelTemplate template() {
/* 3579 */       return this.template;
/*      */     }
/*      */     
/*      */     public String modelSuffix() {
/* 3579 */       return this.modelSuffix;
/*      */     }
/*      */   }
/*      */   
/* 3582 */   private static final Map<BookSlotModelCacheKey, Identifier> CHISELED_BOOKSHELF_SLOT_MODEL_CACHE = new HashMap<>();
/*      */   
/*      */   private void addBookSlotModel(MultiPartGenerator multiPartGenerator, Condition facingCondition, VariantMutator mutator, BooleanProperty stateProperty, ModelTemplate template, boolean isSlotOccupied) {
/* 3585 */     String suffix = isSlotOccupied ? "_occupied" : "_empty";
/* 3586 */     TextureMapping mapping = (new TextureMapping()).put(TextureSlot.TEXTURE, TextureMapping.getBlockTexture(Blocks.CHISELED_BOOKSHELF, suffix));
/* 3587 */     BookSlotModelCacheKey cacheKey = new BookSlotModelCacheKey(template, suffix);
/* 3588 */     MultiVariant model = plainVariant(CHISELED_BOOKSHELF_SLOT_MODEL_CACHE.computeIfAbsent(cacheKey, key -> template.createWithSuffix(Blocks.CHISELED_BOOKSHELF, suffix, mapping, this.modelOutput)));
/* 3590 */     multiPartGenerator.with((Condition)new CombinedCondition(CombinedCondition.Operation.AND, 
/*      */           
/* 3593 */           List.of(facingCondition, 
/*      */             
/* 3595 */             condition().term((Property)stateProperty, Boolean.valueOf(isSlotOccupied)).build())), model
/*      */         
/* 3598 */         .with(mutator));
/*      */   }
/*      */   
/*      */   private void createMagmaBlock() {
/* 3603 */     Material texture = new Material(Identifier.withDefaultNamespace("block/magma"));
/* 3604 */     MultiVariant model = plainVariant(ModelTemplates.CUBE_ALL.create(Blocks.MAGMA_BLOCK, TextureMapping.cube(texture), this.modelOutput));
/* 3605 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.MAGMA_BLOCK, model));
/*      */   }
/*      */   
/*      */   private void createShulkerBox(Block block, DyeColor color) {
/* 3609 */     createParticleOnlyBlock(block);
/* 3611 */     Item item = block.asItem();
/* 3612 */     Identifier baseModel = ModelTemplates.SHULKER_BOX_INVENTORY.create(item, TextureMapping.particle(block), this.modelOutput);
/* 3613 */     Transformation transformation = ShulkerBoxRenderer.modelTransform(Direction.UP);
/* 3616 */     ItemModel.Unbaked itemModel = (color != null) ? ItemModelUtils.specialModel(baseModel, transformation, (SpecialModelRenderer.Unbaked)new ShulkerBoxSpecialRenderer.Unbaked(color)) : ItemModelUtils.specialModel(baseModel, transformation, (SpecialModelRenderer.Unbaked)new ShulkerBoxSpecialRenderer.Unbaked());
/* 3618 */     this.itemModelOutput.accept(item, itemModel);
/*      */   }
/*      */   
/*      */   private void createGrowingPlant(Block kelp, Block kelpPlant, PlantType type) {
/* 3622 */     createCrossBlock(kelp, type);
/* 3623 */     createCrossBlock(kelpPlant, type);
/*      */   }
/*      */   
/*      */   private void createInfestedStone() {
/* 3627 */     Identifier normalModel = ModelLocationUtils.getModelLocation(Blocks.STONE);
/* 3628 */     Variant normal = plainModel(normalModel);
/* 3629 */     Variant mirrored = plainModel(ModelLocationUtils.getModelLocation(Blocks.STONE, "_mirrored"));
/* 3630 */     this.blockStateOutput.accept(
/* 3631 */         MultiVariantGenerator.dispatch(Blocks.INFESTED_STONE, createRotatedVariants(normal, mirrored)));
/* 3633 */     registerSimpleItemModel(Blocks.INFESTED_STONE, normalModel);
/*      */   }
/*      */   
/*      */   private void createInfestedDeepslate() {
/* 3637 */     Identifier normalModel = ModelLocationUtils.getModelLocation(Blocks.DEEPSLATE);
/* 3638 */     Variant normal = plainModel(normalModel);
/* 3639 */     Variant mirrored = plainModel(ModelLocationUtils.getModelLocation(Blocks.DEEPSLATE, "_mirrored"));
/* 3640 */     this.blockStateOutput.accept(
/* 3641 */         MultiVariantGenerator.dispatch(Blocks.INFESTED_DEEPSLATE, createRotatedVariants(normal, mirrored)).with(createRotatedPillar()));
/* 3643 */     registerSimpleItemModel(Blocks.INFESTED_DEEPSLATE, normalModel);
/*      */   }
/*      */   
/*      */   private void createNetherRoots(Block roots, Block pottedRoots) {
/* 3647 */     createCrossBlockWithDefaultItem(roots, PlantType.NOT_TINTED);
/* 3648 */     TextureMapping textures = TextureMapping.plant(TextureMapping.getBlockTexture(roots, "_pot"));
/* 3649 */     MultiVariant model = plainVariant(PlantType.NOT_TINTED.getCrossPot().create(pottedRoots, textures, this.modelOutput));
/* 3650 */     this.blockStateOutput.accept(createSimpleBlock(pottedRoots, model));
/*      */   }
/*      */   
/*      */   private void createRespawnAnchor() {
/* 3654 */     Material bottom = TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_bottom");
/* 3655 */     Material topOff = TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_top_off");
/* 3656 */     Material topOn = TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_top");
/* 3657 */     Identifier[] chargeLevelModels = new Identifier[5];
/* 3658 */     for (int i = 0; i < 5; i++) {
/* 3662 */       TextureMapping mapping = (new TextureMapping()).put(TextureSlot.BOTTOM, bottom).put(TextureSlot.TOP, (i == 0) ? topOff : topOn).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_side" + i));
/* 3663 */       chargeLevelModels[i] = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.RESPAWN_ANCHOR, "_" + i, mapping, this.modelOutput);
/*      */     } 
/* 3666 */     this.blockStateOutput.accept(
/* 3667 */         MultiVariantGenerator.dispatch(Blocks.RESPAWN_ANCHOR)
/* 3668 */         .with(
/* 3669 */           PropertyDispatch.initial((Property)BlockStateProperties.RESPAWN_ANCHOR_CHARGES)
/* 3670 */           .generate(i -> plainVariant(chargeLevelModels[i.intValue()]))));
/* 3673 */     registerSimpleItemModel(Blocks.RESPAWN_ANCHOR, chargeLevelModels[0]);
/*      */   }
/*      */   
/*      */   private static VariantMutator applyRotation(FrontAndTop orientation) {
/* 3677 */     switch (orientation) {
/*      */       default:
/* 3677 */         throw new MatchException(null, null);
/*      */       case DOWN_NORTH:
/*      */       
/*      */       case DOWN_SOUTH:
/*      */       
/*      */       case DOWN_WEST:
/*      */       
/*      */       case DOWN_EAST:
/*      */       
/*      */       case UP_NORTH:
/*      */       
/*      */       case UP_SOUTH:
/*      */       
/*      */       case UP_WEST:
/*      */       
/*      */       case UP_EAST:
/*      */       
/*      */       case NORTH_UP:
/*      */       
/*      */       case SOUTH_UP:
/*      */       
/*      */       case WEST_UP:
/*      */       
/*      */       case EAST_UP:
/*      */         break;
/*      */     } 
/* 3677 */     return 
/*      */       
/* 3689 */       Y_ROT_90;
/*      */   }
/*      */   
/*      */   private void createJigsaw() {
/* 3694 */     Material front = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_top");
/* 3695 */     Material back = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_bottom");
/* 3696 */     Material side = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_side");
/* 3697 */     Material lock = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_lock");
/* 3708 */     TextureMapping mapping = (new TextureMapping()).put(TextureSlot.DOWN, side).put(TextureSlot.WEST, side).put(TextureSlot.EAST, side).put(TextureSlot.PARTICLE, front).put(TextureSlot.NORTH, front).put(TextureSlot.SOUTH, back).put(TextureSlot.UP, lock);
/* 3710 */     this.blockStateOutput.accept(
/* 3711 */         MultiVariantGenerator.dispatch(Blocks.JIGSAW, plainVariant(ModelTemplates.CUBE_DIRECTIONAL.create(Blocks.JIGSAW, mapping, this.modelOutput)))
/* 3712 */         .with(
/* 3713 */           PropertyDispatch.modify((Property)BlockStateProperties.ORIENTATION)
/* 3714 */           .generate(BlockModelGenerators::applyRotation)));
/*      */   }
/*      */   
/*      */   private void createPetrifiedOakSlab() {
/* 3720 */     Block fullBlock = Blocks.OAK_PLANKS;
/* 3721 */     MultiVariant fullBlockModel = plainVariant(ModelLocationUtils.getModelLocation(fullBlock));
/* 3722 */     TextureMapping fullBlockTextures = TextureMapping.cube(fullBlock);
/* 3723 */     Block petrifiedSlab = Blocks.PETRIFIED_OAK_SLAB;
/* 3724 */     MultiVariant petrifiedSlabBottom = plainVariant(ModelTemplates.SLAB_BOTTOM.create(petrifiedSlab, fullBlockTextures, this.modelOutput));
/* 3725 */     MultiVariant petrifiedSlabTop = plainVariant(ModelTemplates.SLAB_TOP.create(petrifiedSlab, fullBlockTextures, this.modelOutput));
/* 3726 */     this.blockStateOutput.accept(createSlab(petrifiedSlab, petrifiedSlabBottom, petrifiedSlabTop, fullBlockModel));
/*      */   }
/*      */   
/*      */   private void createHead(Block standAlone, Block wall, SkullBlock.Type skullType, Identifier itemBase) {
/* 3730 */     MultiVariant blockModel = plainVariant(ModelLocationUtils.decorateBlockModelLocation("skull"));
/* 3731 */     this.blockStateOutput.accept(createSimpleBlock(standAlone, blockModel));
/* 3732 */     this.blockStateOutput.accept(createSimpleBlock(wall, blockModel));
/* 3734 */     if (skullType == SkullBlock.Types.PLAYER) {
/* 3735 */       this.itemModelOutput.accept(standAlone.asItem(), ItemModelUtils.specialModel(itemBase, SKULL_TRANSFORM, (SpecialModelRenderer.Unbaked)new PlayerHeadSpecialRenderer.Unbaked()));
/*      */     } else {
/* 3737 */       this.itemModelOutput.accept(standAlone.asItem(), ItemModelUtils.specialModel(itemBase, SKULL_TRANSFORM, (SpecialModelRenderer.Unbaked)new SkullSpecialRenderer.Unbaked(skullType)));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void createHeads() {
/* 3742 */     Identifier defaultHeadItemBase = ModelLocationUtils.decorateItemModelLocation("template_skull");
/* 3743 */     createHead(Blocks.CREEPER_HEAD, Blocks.CREEPER_WALL_HEAD, (SkullBlock.Type)SkullBlock.Types.CREEPER, defaultHeadItemBase);
/* 3744 */     createHead(Blocks.PLAYER_HEAD, Blocks.PLAYER_WALL_HEAD, (SkullBlock.Type)SkullBlock.Types.PLAYER, defaultHeadItemBase);
/* 3745 */     createHead(Blocks.ZOMBIE_HEAD, Blocks.ZOMBIE_WALL_HEAD, (SkullBlock.Type)SkullBlock.Types.ZOMBIE, defaultHeadItemBase);
/* 3746 */     createHead(Blocks.SKELETON_SKULL, Blocks.SKELETON_WALL_SKULL, (SkullBlock.Type)SkullBlock.Types.SKELETON, defaultHeadItemBase);
/* 3747 */     createHead(Blocks.WITHER_SKELETON_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL, (SkullBlock.Type)SkullBlock.Types.WITHER_SKELETON, defaultHeadItemBase);
/* 3748 */     createHead(Blocks.PIGLIN_HEAD, Blocks.PIGLIN_WALL_HEAD, (SkullBlock.Type)SkullBlock.Types.PIGLIN, defaultHeadItemBase);
/* 3751 */     createHead(Blocks.DRAGON_HEAD, Blocks.DRAGON_WALL_HEAD, (SkullBlock.Type)SkullBlock.Types.DRAGON, ModelLocationUtils.getModelLocation(Items.DRAGON_HEAD));
/*      */   }
/*      */   
/*      */   private void createCopperGolemStatues() {
/* 3755 */     WeatheringCopper.WeatherState.forEach(state -> {
/*      */           createCopperGolemStatue((Block)Blocks.COPPER_GOLEM_STATUE.weathering().pick(state), (Block)Blocks.COPPER_BLOCK.weathering().pick(state), state);
/*      */           copyModel((Block)Blocks.COPPER_GOLEM_STATUE.weathering().pick(state), (Block)Blocks.COPPER_GOLEM_STATUE.waxed().pick(state));
/*      */         });
/*      */   }
/*      */   
/*      */   private void createCopperGolemStatue(Block block, Block particle, WeatheringCopper.WeatherState state) {
/* 3762 */     MultiVariant blockModel = plainVariant(ModelTemplates.PARTICLE_ONLY.create(block, TextureMapping.particle(TextureMapping.getBlockTexture(particle)), this.modelOutput));
/* 3763 */     Identifier itemBase = ModelLocationUtils.decorateItemModelLocation("template_copper_golem_statue");
/* 3764 */     this.blockStateOutput.accept(createSimpleBlock(block, blockModel));
/* 3765 */     this.itemModelOutput.accept(block.asItem(), ItemModelUtils.selectBlockItemProperty(new Transformation((Vector3fc)new Vector3f(0.5F, 1.5F, 0.5F), null, (Vector3fc)new Vector3f(1.0F, -1.0F, -1.0F), null), (Property)CopperGolemStatueBlock.POSE, 
/*      */           
/* 3773 */           ItemModelUtils.specialModel(itemBase, (SpecialModelRenderer.Unbaked)new CopperGolemStatueSpecialRenderer.Unbaked(state, CopperGolemStatueBlock.Pose.STANDING)), 
/*      */           
/* 3777 */           Map.of(CopperGolemStatueBlock.Pose.SITTING, 
/* 3778 */             ItemModelUtils.specialModel(itemBase, (SpecialModelRenderer.Unbaked)new CopperGolemStatueSpecialRenderer.Unbaked(state, CopperGolemStatueBlock.Pose.SITTING)), CopperGolemStatueBlock.Pose.STAR, 
/* 3779 */             ItemModelUtils.specialModel(itemBase, (SpecialModelRenderer.Unbaked)new CopperGolemStatueSpecialRenderer.Unbaked(state, CopperGolemStatueBlock.Pose.STAR)), CopperGolemStatueBlock.Pose.RUNNING, 
/* 3780 */             ItemModelUtils.specialModel(itemBase, (SpecialModelRenderer.Unbaked)new CopperGolemStatueSpecialRenderer.Unbaked(state, CopperGolemStatueBlock.Pose.RUNNING)))));
/*      */   }
/*      */   
/*      */   private void createBanner(DyeColor baseColor) {
/* 3784 */     Block standAlone = (Block)Blocks.BANNER.pick(baseColor);
/* 3785 */     Block wall = (Block)Blocks.WALL_BANNER.pick(baseColor);
/* 3786 */     MultiVariant blockModel = plainVariant(ModelLocationUtils.decorateBlockModelLocation("banner"));
/* 3787 */     Identifier itemModel = ModelLocationUtils.decorateItemModelLocation("template_banner");
/* 3788 */     this.blockStateOutput.accept(createSimpleBlock(standAlone, blockModel));
/* 3789 */     this.blockStateOutput.accept(createSimpleBlock(wall, blockModel));
/* 3790 */     Item item = standAlone.asItem();
/* 3791 */     this.itemModelOutput.accept(item, ItemModelUtils.specialModel(itemModel, (Transformation)BannerRenderer.TRANSFORMATIONS.freeTransformations(0), (SpecialModelRenderer.Unbaked)new BannerSpecialRenderer.Unbaked(baseColor, BannerBlock.AttachmentType.GROUND)));
/*      */   }
/*      */   
/*      */   private void createChest(Block block, Block particle, Identifier texture, boolean hasGiftVariant) {
/* 3795 */     createParticleOnlyBlock(block, particle);
/* 3797 */     Item chestItem = block.asItem();
/* 3798 */     Identifier itemModelBase = ModelTemplates.CHEST_INVENTORY.create(chestItem, TextureMapping.particle(particle), this.modelOutput);
/* 3799 */     ItemModel.Unbaked plainModel = ItemModelUtils.specialModel(itemModelBase, (SpecialModelRenderer.Unbaked)new ChestSpecialRenderer.Unbaked(texture));
/* 3801 */     if (hasGiftVariant) {
/* 3802 */       ItemModel.Unbaked giftModel = ItemModelUtils.specialModel(itemModelBase, (SpecialModelRenderer.Unbaked)new ChestSpecialRenderer.Unbaked((Identifier)ChestSpecialRenderer.CHRISTMAS.single()));
/* 3803 */       this.itemModelOutput.accept(chestItem, ItemModelUtils.isXmas(giftModel, plainModel));
/*      */     } else {
/* 3808 */       this.itemModelOutput.accept(chestItem, plainModel);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void createChest(Block block, Block particle, MultiblockChestResources<Identifier> textures, boolean hasGiftVariant) {
/* 3813 */     createChest(block, particle, (Identifier)textures.single(), hasGiftVariant);
/*      */   }
/*      */   
/*      */   private void createChests() {
/* 3817 */     createChest(Blocks.CHEST, Blocks.OAK_PLANKS, ChestSpecialRenderer.REGULAR, true);
/* 3818 */     createChest(Blocks.TRAPPED_CHEST, Blocks.OAK_PLANKS, ChestSpecialRenderer.TRAPPED, true);
/* 3819 */     createChest(Blocks.ENDER_CHEST, Blocks.OBSIDIAN, ChestSpecialRenderer.ENDER_CHEST, false);
/*      */   }
/*      */   
/*      */   private void createCopperChests() {
/* 3823 */     WeatheringCopper.WeatherState.forEach(state -> {
/*      */           createChest((Block)Blocks.COPPER_CHEST.weathering().pick(state), (Block)Blocks.COPPER_BLOCK.weathering().pick(state), (MultiblockChestResources<Identifier>)ChestSpecialRenderer.COPPER.pick(state), false);
/*      */           copyModel((Block)Blocks.COPPER_CHEST.weathering().pick(state), (Block)Blocks.COPPER_CHEST.waxed().pick(state));
/*      */         });
/*      */   }
/*      */   
/*      */   private void createBed(DyeColor dyeColor) {
/* 3830 */     Block bed = (Block)Blocks.BED.pick(dyeColor);
/* 3831 */     Identifier head = ModelTemplates.BED_HEAD.createWithSuffix(bed, "_" + String.valueOf(BedPart.HEAD), TextureMapping.bed(bed, BedPart.HEAD), this.modelOutput);
/* 3832 */     Identifier foot = ModelTemplates.BED_FOOT.createWithSuffix(bed, "_" + String.valueOf(BedPart.FOOT), TextureMapping.bed(bed, BedPart.FOOT), this.modelOutput);
/* 3833 */     MultiVariant blockModelHead = plainVariant(head);
/* 3834 */     MultiVariant blockModelFoot = plainVariant(foot);
/* 3835 */     this.blockStateOutput.accept(createBed(bed, blockModelHead, blockModelFoot));
/* 3837 */     Transformation footTransformation = new Transformation((Vector3fc)new Vector3f(0.0F, 0.0F, 1.0F), null, null, null);
/* 3838 */     ItemModel.Unbaked itemModelHead = ItemModelUtils.plainModel(head);
/* 3839 */     ItemModel.Unbaked itemModelFoot = ItemModelUtils.plainModel(foot, footTransformation);
/* 3840 */     this.itemModelOutput.accept(bed.asItem(), ItemModelUtils.composite(new ItemModel.Unbaked[] { itemModelHead, itemModelFoot }));
/*      */   }
/*      */   
/*      */   private void generateSimpleSpecialItemModel(Block block, Optional<Transformation> transformation, SpecialModelRenderer.Unbaked<?> specialModel) {
/* 3844 */     Item item = block.asItem();
/* 3845 */     Identifier harcodedModelBase = ModelLocationUtils.getModelLocation(item);
/* 3846 */     this.itemModelOutput.accept(item, ItemModelUtils.specialModel(harcodedModelBase, transformation, specialModel));
/*      */   }
/*      */   
/*      */   public void run() {
/* 3850 */     BlockFamilies.getAllFamilies().filter(BlockFamily::shouldGenerateModel).forEach(blockFamily -> family(blockFamily.getBaseBlock()).generateFor(blockFamily));
/* 3855 */     WeatheringCopper.WeatherState.forEach(state -> family((Block)Blocks.CUT_COPPER.weathering().pick(state)).generateFor((BlockFamily)BlockFamilies.CUT_COPPER.weathering().pick(state)).donateModelTo((Block)Blocks.CUT_COPPER.weathering().pick(state), (Block)Blocks.CUT_COPPER.waxed().pick(state)).donateModelTo((Block)Blocks.CHISELED_COPPER.weathering().pick(state), (Block)Blocks.CHISELED_COPPER.waxed().pick(state)).generateFor((BlockFamily)BlockFamilies.CUT_COPPER.waxed().pick(state)));
/* 3862 */     Blocks.COPPER_BULB.zipUnwaxedWaxed((unwaxed, waxed) -> {
/*      */           createCopperBulb(unwaxed);
/*      */           copyCopperBulbModel(unwaxed, waxed);
/*      */         });
/* 3867 */     createNonTemplateModelBlock(Blocks.AIR);
/* 3868 */     createNonTemplateModelBlock(Blocks.CAVE_AIR, Blocks.AIR);
/* 3869 */     createNonTemplateModelBlock(Blocks.VOID_AIR, Blocks.AIR);
/* 3870 */     createNonTemplateModelBlock(Blocks.BEACON);
/* 3871 */     createNonTemplateModelBlock(Blocks.CACTUS);
/* 3872 */     createNonTemplateModelBlock(Blocks.BUBBLE_COLUMN, Blocks.WATER);
/* 3873 */     createNonTemplateModelBlock(Blocks.DRAGON_EGG);
/* 3874 */     createNonTemplateModelBlock(Blocks.DRIED_KELP_BLOCK);
/* 3875 */     createNonTemplateModelBlock(Blocks.ENCHANTING_TABLE);
/* 3876 */     createNonTemplateModelBlock(Blocks.FLOWER_POT);
/* 3877 */     registerSimpleFlatItemModel(Items.FLOWER_POT);
/* 3878 */     createNonTemplateModelBlock(Blocks.HONEY_BLOCK);
/* 3879 */     createNonTemplateModelBlock(Blocks.WATER);
/* 3880 */     createNonTemplateModelBlock(Blocks.LAVA);
/* 3881 */     createNonTemplateModelBlock(Blocks.SLIME_BLOCK);
/* 3882 */     registerSimpleFlatItemModel(Items.IRON_CHAIN);
/* 3884 */     Items.COPPER_CHAIN.zipUnwaxedWaxed(this::createCopperChainItem);
/* 3886 */     ColorCollection.zipApply(Blocks.DYED_CANDLE, Blocks.DYED_CANDLE_CAKE, this::createCandleAndCandleCake);
/* 3887 */     createCandleAndCandleCake(Blocks.CANDLE, Blocks.CANDLE_CAKE);
/* 3889 */     createNonTemplateModelBlock(Blocks.POTTED_BAMBOO);
/* 3890 */     createNonTemplateModelBlock(Blocks.POTTED_CACTUS);
/* 3891 */     createNonTemplateModelBlock(Blocks.POWDER_SNOW);
/* 3892 */     createNonTemplateModelBlock(Blocks.SPORE_BLOSSOM);
/* 3893 */     createAzalea(Blocks.AZALEA);
/* 3894 */     createAzalea(Blocks.FLOWERING_AZALEA);
/* 3895 */     createPottedAzalea(Blocks.POTTED_AZALEA);
/* 3896 */     createPottedAzalea(Blocks.POTTED_FLOWERING_AZALEA);
/* 3897 */     createCaveVines();
/* 3898 */     createFullAndCarpetBlocks(Blocks.MOSS_BLOCK, Blocks.MOSS_CARPET);
/* 3899 */     createMossyCarpet(Blocks.PALE_MOSS_CARPET);
/* 3900 */     createHangingMoss(Blocks.PALE_HANGING_MOSS);
/* 3901 */     createTrivialCube(Blocks.PALE_MOSS_BLOCK);
/* 3902 */     createFlowerBed(Blocks.PINK_PETALS);
/* 3903 */     createFlowerBed(Blocks.WILDFLOWERS);
/* 3904 */     createLeafLitter(Blocks.LEAF_LITTER);
/* 3905 */     createCrossBlock(Blocks.FIREFLY_BUSH, PlantType.EMISSIVE_NOT_TINTED);
/* 3906 */     registerSimpleFlatItemModel(Items.FIREFLY_BUSH);
/* 3908 */     createAirLikeBlock(Blocks.BARRIER, Items.BARRIER);
/* 3909 */     registerSimpleFlatItemModel(Items.BARRIER);
/* 3910 */     createLightBlock();
/* 3911 */     createAirLikeBlock(Blocks.STRUCTURE_VOID, Items.STRUCTURE_VOID);
/* 3912 */     registerSimpleFlatItemModel(Items.STRUCTURE_VOID);
/* 3913 */     createAirLikeBlock(Blocks.MOVING_PISTON, TextureMapping.getBlockTexture(Blocks.PISTON, "_side"));
/* 3915 */     createTrivialCube(Blocks.COAL_ORE);
/* 3916 */     createTrivialCube(Blocks.DEEPSLATE_COAL_ORE);
/* 3917 */     createTrivialCube(Blocks.COAL_BLOCK);
/* 3918 */     createTrivialCube(Blocks.DIAMOND_ORE);
/* 3919 */     createTrivialCube(Blocks.DEEPSLATE_DIAMOND_ORE);
/* 3920 */     createTrivialCube(Blocks.DIAMOND_BLOCK);
/* 3921 */     createTrivialCube(Blocks.EMERALD_ORE);
/* 3922 */     createTrivialCube(Blocks.DEEPSLATE_EMERALD_ORE);
/* 3923 */     createTrivialCube(Blocks.EMERALD_BLOCK);
/* 3924 */     createTrivialCube(Blocks.GOLD_ORE);
/* 3925 */     createTrivialCube(Blocks.NETHER_GOLD_ORE);
/* 3926 */     createTrivialCube(Blocks.DEEPSLATE_GOLD_ORE);
/* 3927 */     createTrivialCube(Blocks.GOLD_BLOCK);
/* 3928 */     createTrivialCube(Blocks.IRON_ORE);
/* 3929 */     createTrivialCube(Blocks.DEEPSLATE_IRON_ORE);
/* 3930 */     createTrivialCube(Blocks.IRON_BLOCK);
/* 3931 */     createTrivialBlock(Blocks.ANCIENT_DEBRIS, TexturedModel.COLUMN);
/* 3932 */     createTrivialCube(Blocks.NETHERITE_BLOCK);
/* 3933 */     createTrivialCube(Blocks.LAPIS_ORE);
/* 3934 */     createTrivialCube(Blocks.DEEPSLATE_LAPIS_ORE);
/* 3935 */     createTrivialCube(Blocks.LAPIS_BLOCK);
/* 3936 */     createTrivialCube(Blocks.RESIN_BLOCK);
/* 3937 */     createTrivialCube(Blocks.NETHER_QUARTZ_ORE);
/* 3938 */     createTrivialCube(Blocks.REDSTONE_ORE);
/* 3939 */     createTrivialCube(Blocks.DEEPSLATE_REDSTONE_ORE);
/* 3940 */     createTrivialCube(Blocks.REDSTONE_BLOCK);
/* 3941 */     createTrivialCube(Blocks.GILDED_BLACKSTONE);
/* 3943 */     createTrivialCube(Blocks.BLUE_ICE);
/* 3944 */     createTrivialCube(Blocks.CLAY);
/* 3945 */     createTrivialCube(Blocks.COARSE_DIRT);
/* 3946 */     createTrivialCube(Blocks.CRYING_OBSIDIAN);
/* 3947 */     createTrivialCube(Blocks.GLOWSTONE);
/* 3948 */     createTrivialCube(Blocks.GRAVEL);
/* 3949 */     createTrivialCube(Blocks.HONEYCOMB_BLOCK);
/* 3950 */     createTrivialCube(Blocks.ICE);
/* 3951 */     createTrivialBlock(Blocks.JUKEBOX, TexturedModel.CUBE_TOP);
/* 3952 */     createTrivialBlock(Blocks.LODESTONE, TexturedModel.COLUMN);
/* 3953 */     createTrivialBlock(Blocks.MELON, TexturedModel.COLUMN);
/* 3954 */     createNonTemplateModelBlock(Blocks.MANGROVE_ROOTS);
/* 3955 */     createNonTemplateModelBlock(Blocks.POTTED_MANGROVE_PROPAGULE);
/* 3956 */     createTrivialCube(Blocks.NETHER_WART_BLOCK);
/* 3957 */     createTrivialCube(Blocks.NOTE_BLOCK);
/* 3958 */     createTrivialCube(Blocks.PACKED_ICE);
/* 3959 */     createTrivialCube(Blocks.OBSIDIAN);
/* 3960 */     createTrivialCube(Blocks.SEA_LANTERN);
/* 3961 */     createTrivialCube(Blocks.SHROOMLIGHT);
/* 3962 */     createTrivialCube(Blocks.SOUL_SAND);
/* 3963 */     createTrivialCube(Blocks.SOUL_SOIL);
/* 3964 */     createTrivialBlock(Blocks.SPAWNER, TexturedModel.CUBE_INNER_FACES);
/* 3965 */     createCreakingHeart(Blocks.CREAKING_HEART);
/* 3966 */     createTrivialCube(Blocks.SPONGE);
/* 3967 */     createTrivialBlock(Blocks.SEAGRASS, TexturedModel.SEAGRASS);
/* 3968 */     registerSimpleFlatItemModel(Items.SEAGRASS);
/* 3969 */     createTrivialBlock(Blocks.TNT, TexturedModel.CUBE_TOP_BOTTOM);
/* 3970 */     createTrivialBlock(Blocks.TARGET, TexturedModel.COLUMN);
/* 3971 */     createTrivialCube(Blocks.WARPED_WART_BLOCK);
/* 3972 */     createTrivialCube(Blocks.WET_SPONGE);
/* 3973 */     createTrivialCube(Blocks.AMETHYST_BLOCK);
/* 3974 */     createTrivialCube(Blocks.BUDDING_AMETHYST);
/* 3975 */     createTrivialCube(Blocks.CALCITE);
/* 3976 */     createTrivialCube(Blocks.DRIPSTONE_BLOCK);
/* 3977 */     createTrivialCube(Blocks.RAW_IRON_BLOCK);
/* 3978 */     createTrivialCube(Blocks.RAW_COPPER_BLOCK);
/* 3979 */     createTrivialCube(Blocks.RAW_GOLD_BLOCK);
/* 3980 */     createRotatedMirroredVariantBlock(Blocks.SCULK);
/* 3981 */     createNonTemplateModelBlock(Blocks.HEAVY_CORE);
/* 3983 */     createPetrifiedOakSlab();
/* 3985 */     createTrivialCube(Blocks.COPPER_ORE);
/* 3986 */     createTrivialCube(Blocks.DEEPSLATE_COPPER_ORE);
/* 3987 */     Blocks.COPPER_BLOCK.zipUnwaxedWaxed((unwaxed, paramBlock1) -> createTrivialCube(unwaxed));
/* 3988 */     Blocks.COPPER_BLOCK.zipUnwaxedWaxed(this::copyModel);
/* 3989 */     Blocks.COPPER_DOOR.zipUnwaxedWaxed((unwaxed, paramBlock1) -> createDoor(unwaxed));
/* 3990 */     Blocks.COPPER_DOOR.zipUnwaxedWaxed(this::copyDoorModel);
/* 3991 */     Blocks.COPPER_TRAPDOOR.zipUnwaxedWaxed((unwaxed, paramBlock1) -> createTrapdoor(unwaxed));
/* 3992 */     Blocks.COPPER_TRAPDOOR.zipUnwaxedWaxed(this::copyTrapdoorModel);
/* 3993 */     Blocks.COPPER_GRATE.zipUnwaxedWaxed((unwaxed, paramBlock1) -> createTrivialCube(unwaxed));
/* 3994 */     Blocks.COPPER_GRATE.zipUnwaxedWaxed(this::copyModel);
/* 3995 */     Blocks.LIGHTNING_ROD.zipUnwaxedWaxed(this::createLightningRod);
/* 3997 */     createWeightedPressurePlate(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.GOLD_BLOCK);
/* 3998 */     createWeightedPressurePlate(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.IRON_BLOCK);
/* 4000 */     createShelf(Blocks.ACACIA_SHELF, Blocks.STRIPPED_ACACIA_LOG);
/* 4001 */     createShelf(Blocks.BAMBOO_SHELF, Blocks.STRIPPED_BAMBOO_BLOCK);
/* 4002 */     createShelf(Blocks.BIRCH_SHELF, Blocks.STRIPPED_BIRCH_LOG);
/* 4003 */     createShelf(Blocks.CHERRY_SHELF, Blocks.STRIPPED_CHERRY_LOG);
/* 4004 */     createShelf(Blocks.CRIMSON_SHELF, Blocks.STRIPPED_CRIMSON_STEM);
/* 4005 */     createShelf(Blocks.DARK_OAK_SHELF, Blocks.STRIPPED_DARK_OAK_LOG);
/* 4006 */     createShelf(Blocks.JUNGLE_SHELF, Blocks.STRIPPED_JUNGLE_LOG);
/* 4007 */     createShelf(Blocks.MANGROVE_SHELF, Blocks.STRIPPED_MANGROVE_LOG);
/* 4008 */     createShelf(Blocks.OAK_SHELF, Blocks.STRIPPED_OAK_LOG);
/* 4009 */     createShelf(Blocks.PALE_OAK_SHELF, Blocks.STRIPPED_PALE_OAK_LOG);
/* 4010 */     createShelf(Blocks.SPRUCE_SHELF, Blocks.STRIPPED_SPRUCE_LOG);
/* 4011 */     createShelf(Blocks.WARPED_SHELF, Blocks.STRIPPED_WARPED_STEM);
/* 4013 */     createAmethystClusters();
/* 4014 */     createBookshelf();
/* 4015 */     createChiseledBookshelf();
/* 4016 */     createBrewingStand();
/* 4017 */     createCakeBlock();
/* 4018 */     createCampfires(new Block[] { Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE });
/* 4019 */     createCartographyTable();
/* 4020 */     createCauldrons();
/* 4021 */     createChorusFlower();
/* 4022 */     createChorusPlant();
/* 4023 */     createComposter();
/* 4024 */     createDaylightDetector();
/* 4025 */     createEndPortalFrame();
/* 4026 */     createRotatableColumn(Blocks.END_ROD);
/* 4027 */     createFarmland();
/* 4028 */     createFire();
/* 4029 */     createSoulFire();
/* 4030 */     createFrostedIce();
/* 4031 */     createGrassBlocks();
/* 4032 */     createCocoa();
/* 4033 */     createDirtPath();
/* 4034 */     createGrindstone();
/* 4035 */     createHopper();
/* 4037 */     createBarsAndItem(Blocks.IRON_BARS);
/* 4038 */     Blocks.COPPER_BARS.zipUnwaxedWaxed(this::createBarsAndItem);
/* 4040 */     createLever();
/* 4041 */     createLilyPad();
/* 4042 */     createNetherPortalBlock();
/* 4043 */     createNetherrack();
/* 4044 */     createObserver();
/* 4045 */     createPistons();
/* 4046 */     createPistonHeads();
/* 4047 */     createScaffolding();
/* 4048 */     createRedstoneTorch();
/* 4049 */     createRedstoneLamp();
/* 4050 */     createRepeater();
/* 4051 */     createSeaPickle();
/* 4052 */     createSmithingTable();
/* 4053 */     createSnowBlocks();
/* 4054 */     createStonecutter();
/* 4055 */     createStructureBlock();
/* 4056 */     createSweetBerryBush();
/* 4057 */     createTestBlock();
/* 4058 */     createTrivialCube(Blocks.TEST_INSTANCE_BLOCK);
/* 4059 */     createTripwire();
/* 4060 */     createTripwireHook();
/* 4061 */     createTurtleEgg();
/* 4062 */     createSnifferEgg();
/* 4063 */     createDriedGhastBlock();
/* 4064 */     createVine();
/* 4065 */     createMultiface(Blocks.GLOW_LICHEN);
/* 4066 */     createMultiface(Blocks.SCULK_VEIN);
/* 4067 */     createMultiface(Blocks.RESIN_CLUMP, Items.RESIN_CLUMP);
/* 4068 */     createMagmaBlock();
/* 4069 */     createJigsaw();
/* 4070 */     createSculkSensor();
/* 4071 */     createCalibratedSculkSensor();
/* 4072 */     createSculkShrieker();
/* 4073 */     createFrogspawnBlock();
/* 4074 */     createMangrovePropagule();
/* 4075 */     createMuddyMangroveRoots();
/* 4076 */     createTrialSpawner();
/* 4077 */     createVault();
/* 4079 */     createNonTemplateHorizontalBlock(Blocks.LADDER);
/* 4080 */     registerSimpleFlatItemModel(Blocks.LADDER);
/* 4081 */     createNonTemplateHorizontalBlock(Blocks.LECTERN);
/* 4083 */     createBigDripLeafBlock();
/* 4084 */     createNonTemplateHorizontalBlock(Blocks.BIG_DRIPLEAF_STEM);
/* 4086 */     createNormalTorch(Blocks.TORCH, Blocks.WALL_TORCH);
/* 4087 */     createNormalTorch(Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH);
/* 4088 */     createNormalTorch(Blocks.COPPER_TORCH, Blocks.COPPER_WALL_TORCH);
/* 4090 */     createCraftingTableLike(Blocks.CRAFTING_TABLE, Blocks.OAK_PLANKS, TextureMapping::craftingTable);
/* 4091 */     createCraftingTableLike(Blocks.FLETCHING_TABLE, Blocks.BIRCH_PLANKS, TextureMapping::fletchingTable);
/* 4093 */     createNyliumBlock(Blocks.CRIMSON_NYLIUM);
/* 4094 */     createNyliumBlock(Blocks.WARPED_NYLIUM);
/* 4096 */     createDispenserBlock(Blocks.DISPENSER);
/* 4097 */     createDispenserBlock(Blocks.DROPPER);
/* 4098 */     createCrafterBlock();
/* 4100 */     createLantern(Blocks.LANTERN);
/* 4101 */     createLantern(Blocks.SOUL_LANTERN);
/* 4103 */     Blocks.COPPER_LANTERN.zipUnwaxedWaxed(this::createCopperLantern);
/* 4105 */     createAxisAlignedPillarBlockCustomModel(Blocks.IRON_CHAIN, plainVariant(TexturedModel.CHAIN.create(Blocks.IRON_CHAIN, this.modelOutput)));
/* 4107 */     Blocks.COPPER_CHAIN.zipUnwaxedWaxed(this::createCopperChain);
/* 4109 */     createAxisAlignedPillarBlock(Blocks.BASALT, TexturedModel.COLUMN);
/* 4110 */     createAxisAlignedPillarBlock(Blocks.POLISHED_BASALT, TexturedModel.COLUMN);
/* 4111 */     createTrivialCube(Blocks.SMOOTH_BASALT);
/* 4112 */     createAxisAlignedPillarBlock(Blocks.BONE_BLOCK, TexturedModel.COLUMN);
/* 4113 */     createRotatedVariantBlock(Blocks.DIRT);
/* 4114 */     createRotatedVariantBlock(Blocks.ROOTED_DIRT);
/* 4115 */     createRotatedVariantBlock(Blocks.SAND);
/* 4116 */     createBrushableBlock(Blocks.SUSPICIOUS_SAND);
/* 4117 */     createBrushableBlock(Blocks.SUSPICIOUS_GRAVEL);
/* 4118 */     createRotatedVariantBlock(Blocks.RED_SAND);
/* 4119 */     createRotatedMirroredVariantBlock(Blocks.BEDROCK);
/* 4120 */     createTrivialBlock(Blocks.REINFORCED_DEEPSLATE, TexturedModel.CUBE_TOP_BOTTOM);
/* 4121 */     createRotatedPillarWithHorizontalVariant(Blocks.HAY_BLOCK, TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);
/* 4122 */     createRotatedPillarWithHorizontalVariant(Blocks.OCHRE_FROGLIGHT, TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);
/* 4123 */     createRotatedPillarWithHorizontalVariant(Blocks.VERDANT_FROGLIGHT, TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);
/* 4124 */     createRotatedPillarWithHorizontalVariant(Blocks.PEARLESCENT_FROGLIGHT, TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);
/* 4126 */     createHorizontallyRotatedBlock(Blocks.LOOM, TexturedModel.ORIENTABLE);
/* 4128 */     createPumpkins();
/* 4129 */     createBeeNest(Blocks.BEE_NEST, TextureMapping::orientableCube);
/* 4130 */     createBeeNest(Blocks.BEEHIVE, TextureMapping::orientableCubeSameEnds);
/* 4133 */     createCropBlock(Blocks.BEETROOTS, (Property<Integer>)BlockStateProperties.AGE_3, new int[] { 0, 1, 2, 3 });
/* 4134 */     createCropBlock(Blocks.CARROTS, (Property<Integer>)BlockStateProperties.AGE_7, new int[] { 0, 0, 1, 1, 2, 2, 2, 3 });
/* 4135 */     createCropBlock(Blocks.NETHER_WART, (Property<Integer>)BlockStateProperties.AGE_3, new int[] { 0, 1, 1, 2 });
/* 4136 */     createCropBlock(Blocks.POTATOES, (Property<Integer>)BlockStateProperties.AGE_7, new int[] { 0, 0, 1, 1, 2, 2, 2, 3 });
/* 4137 */     createCropBlock(Blocks.WHEAT, (Property<Integer>)BlockStateProperties.AGE_7, new int[] { 0, 1, 2, 3, 4, 5, 6, 7 });
/* 4138 */     createCrossBlock(Blocks.TORCHFLOWER_CROP, PlantType.NOT_TINTED, (Property<Integer>)BlockStateProperties.AGE_1, new int[] { 0, 1 });
/* 4139 */     createPitcherCrop();
/* 4140 */     createPitcherPlant();
/* 4142 */     DyeColor.VALUES.forEach(this::createBanner);
/* 4144 */     DyeColor.VALUES.forEach(this::createBed);
/* 4146 */     createHeads();
/* 4148 */     createChests();
/* 4150 */     createCopperChests();
/* 4152 */     createShulkerBox(Blocks.SHULKER_BOX, null);
/* 4153 */     ColorCollection.zipApply(ColorCollection.VALUES, Blocks.DYED_SHULKER_BOX, (color, box) -> createShulkerBox(box, color));
/* 4155 */     createCopperGolemStatues();
/* 4157 */     createParticleOnlyBlock(Blocks.CONDUIT);
/* 4158 */     generateSimpleSpecialItemModel(Blocks.CONDUIT, Optional.of(ConduitRenderer.DEFAULT_TRANSFORMATION), (SpecialModelRenderer.Unbaked<?>)new ConduitSpecialRenderer.Unbaked());
/* 4160 */     createParticleOnlyBlock(Blocks.DECORATED_POT, Blocks.TERRACOTTA);
/* 4161 */     generateSimpleSpecialItemModel(Blocks.DECORATED_POT, Optional.empty(), (SpecialModelRenderer.Unbaked<?>)new DecoratedPotSpecialRenderer.Unbaked());
/* 4163 */     createParticleOnlyBlock(Blocks.END_PORTAL, Blocks.OBSIDIAN);
/* 4164 */     createParticleOnlyBlock(Blocks.END_GATEWAY, Blocks.OBSIDIAN);
/* 4166 */     createTrivialCube(Blocks.AZALEA_LEAVES);
/* 4167 */     createTrivialCube(Blocks.FLOWERING_AZALEA_LEAVES);
/* 4168 */     Blocks.CONCRETE.forEach(this::createTrivialCube);
/* 4170 */     createColoredBlockWithRandomRotations(TexturedModel.CUBE, Blocks.CONCRETE_POWDER.asList());
/* 4172 */     createTrivialCube(Blocks.POTENT_SULFUR);
/* 4174 */     createTrivialCube(Blocks.TERRACOTTA);
/* 4175 */     Blocks.DYED_TERRACOTTA.forEach(this::createTrivialCube);
/* 4177 */     createTrivialCube(Blocks.TINTED_GLASS);
/* 4178 */     createGlassBlocks(Blocks.GLASS, Blocks.GLASS_PANE);
/* 4180 */     ColorCollection.zipApply(Blocks.STAINED_GLASS, Blocks.STAINED_GLASS_PANE, this::createGlassBlocks);
/* 4182 */     createColoredBlockWithStateRotations(TexturedModel.GLAZED_TERRACOTTA, Blocks.GLAZED_TERRACOTTA.asList());
/* 4184 */     ColorCollection.zipApply(Blocks.WOOL, Blocks.CARPET, this::createFullAndCarpetBlocks);
/* 4186 */     createTrivialCube(Blocks.MUD);
/* 4187 */     createTrivialCube(Blocks.PACKED_MUD);
/* 4189 */     createPlant(Blocks.FERN, Blocks.POTTED_FERN, PlantType.TINTED);
/* 4190 */     createItemWithGrassTint(Blocks.FERN);
/* 4191 */     createPlantWithDefaultItem(Blocks.DANDELION, Blocks.POTTED_DANDELION, PlantType.NOT_TINTED);
/* 4192 */     createPlantWithDefaultItem(Blocks.GOLDEN_DANDELION, Blocks.POTTED_GOLDEN_DANDELION, PlantType.NOT_TINTED);
/* 4193 */     createPlantWithDefaultItem(Blocks.POPPY, Blocks.POTTED_POPPY, PlantType.NOT_TINTED);
/* 4194 */     createPlantWithDefaultItem(Blocks.OPEN_EYEBLOSSOM, Blocks.POTTED_OPEN_EYEBLOSSOM, PlantType.EMISSIVE_NOT_TINTED);
/* 4195 */     createPlantWithDefaultItem(Blocks.CLOSED_EYEBLOSSOM, Blocks.POTTED_CLOSED_EYEBLOSSOM, PlantType.NOT_TINTED);
/* 4196 */     createPlantWithDefaultItem(Blocks.BLUE_ORCHID, Blocks.POTTED_BLUE_ORCHID, PlantType.NOT_TINTED);
/* 4197 */     createPlantWithDefaultItem(Blocks.ALLIUM, Blocks.POTTED_ALLIUM, PlantType.NOT_TINTED);
/* 4198 */     createPlantWithDefaultItem(Blocks.AZURE_BLUET, Blocks.POTTED_AZURE_BLUET, PlantType.NOT_TINTED);
/* 4199 */     createPlantWithDefaultItem(Blocks.RED_TULIP, Blocks.POTTED_RED_TULIP, PlantType.NOT_TINTED);
/* 4200 */     createPlantWithDefaultItem(Blocks.ORANGE_TULIP, Blocks.POTTED_ORANGE_TULIP, PlantType.NOT_TINTED);
/* 4201 */     createPlantWithDefaultItem(Blocks.WHITE_TULIP, Blocks.POTTED_WHITE_TULIP, PlantType.NOT_TINTED);
/* 4202 */     createPlantWithDefaultItem(Blocks.PINK_TULIP, Blocks.POTTED_PINK_TULIP, PlantType.NOT_TINTED);
/* 4203 */     createPlantWithDefaultItem(Blocks.OXEYE_DAISY, Blocks.POTTED_OXEYE_DAISY, PlantType.NOT_TINTED);
/* 4204 */     createPlantWithDefaultItem(Blocks.CORNFLOWER, Blocks.POTTED_CORNFLOWER, PlantType.NOT_TINTED);
/* 4205 */     createPlantWithDefaultItem(Blocks.LILY_OF_THE_VALLEY, Blocks.POTTED_LILY_OF_THE_VALLEY, PlantType.NOT_TINTED);
/* 4206 */     createPlantWithDefaultItem(Blocks.WITHER_ROSE, Blocks.POTTED_WITHER_ROSE, PlantType.NOT_TINTED);
/* 4207 */     createPlantWithDefaultItem(Blocks.RED_MUSHROOM, Blocks.POTTED_RED_MUSHROOM, PlantType.NOT_TINTED);
/* 4208 */     createPlantWithDefaultItem(Blocks.BROWN_MUSHROOM, Blocks.POTTED_BROWN_MUSHROOM, PlantType.NOT_TINTED);
/* 4209 */     createPlantWithDefaultItem(Blocks.DEAD_BUSH, Blocks.POTTED_DEAD_BUSH, PlantType.NOT_TINTED);
/* 4210 */     createPlantWithDefaultItem(Blocks.TORCHFLOWER, Blocks.POTTED_TORCHFLOWER, PlantType.NOT_TINTED);
/* 4212 */     createSpeleothem(Blocks.POINTED_DRIPSTONE);
/* 4213 */     createSpeleothem(Blocks.SULFUR_SPIKE);
/* 4215 */     createMushroomBlock(Blocks.BROWN_MUSHROOM_BLOCK);
/* 4216 */     createMushroomBlock(Blocks.RED_MUSHROOM_BLOCK);
/* 4217 */     createMushroomBlock(Blocks.MUSHROOM_STEM);
/* 4219 */     createCrossBlock(Blocks.SHORT_GRASS, PlantType.TINTED);
/* 4220 */     createItemWithGrassTint(Blocks.SHORT_GRASS);
/* 4221 */     createCrossBlockWithDefaultItem(Blocks.SHORT_DRY_GRASS, PlantType.NOT_TINTED);
/* 4222 */     createCrossBlockWithDefaultItem(Blocks.TALL_DRY_GRASS, PlantType.NOT_TINTED);
/* 4223 */     createCrossBlock(Blocks.BUSH, PlantType.TINTED);
/* 4224 */     createItemWithGrassTint(Blocks.BUSH);
/* 4225 */     createCrossBlock(Blocks.SUGAR_CANE, PlantType.TINTED);
/* 4226 */     registerSimpleFlatItemModel(Items.SUGAR_CANE);
/* 4227 */     createGrowingPlant(Blocks.KELP, Blocks.KELP_PLANT, PlantType.NOT_TINTED);
/* 4228 */     registerSimpleFlatItemModel(Items.KELP);
/* 4229 */     createCrossBlock(Blocks.HANGING_ROOTS, PlantType.NOT_TINTED);
/* 4230 */     createGrowingPlant(Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT, PlantType.NOT_TINTED);
/* 4231 */     createGrowingPlant(Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT, PlantType.NOT_TINTED);
/* 4232 */     registerSimpleFlatItemModel(Blocks.WEEPING_VINES, "_plant");
/* 4233 */     registerSimpleFlatItemModel(Blocks.TWISTING_VINES, "_plant");
/* 4234 */     createCrossBlockWithDefaultItem(Blocks.BAMBOO_SAPLING, PlantType.TINTED, TextureMapping.cross(TextureMapping.getBlockTexture(Blocks.BAMBOO, "_stage0")));
/* 4235 */     createBamboo();
/* 4236 */     createCrossBlockWithDefaultItem(Blocks.CACTUS_FLOWER, PlantType.NOT_TINTED);
/* 4237 */     createCrossBlockWithDefaultItem(Blocks.COBWEB, PlantType.NOT_TINTED);
/* 4238 */     createDoublePlantWithDefaultItem(Blocks.LILAC, PlantType.NOT_TINTED);
/* 4239 */     createDoublePlantWithDefaultItem(Blocks.ROSE_BUSH, PlantType.NOT_TINTED);
/* 4240 */     createDoublePlantWithDefaultItem(Blocks.PEONY, PlantType.NOT_TINTED);
/* 4241 */     createTintedDoublePlant(Blocks.TALL_GRASS);
/* 4242 */     createTintedDoublePlant(Blocks.LARGE_FERN);
/* 4244 */     createSunflower();
/* 4245 */     createTallSeagrass();
/* 4246 */     createSmallDripleaf();
/* 4248 */     createCoral(Blocks.TUBE_CORAL, Blocks.DEAD_TUBE_CORAL, Blocks.TUBE_CORAL_BLOCK, Blocks.DEAD_TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL_FAN, Blocks.DEAD_TUBE_CORAL_FAN, Blocks.TUBE_CORAL_WALL_FAN, Blocks.DEAD_TUBE_CORAL_WALL_FAN);
/* 4249 */     createCoral(Blocks.BRAIN_CORAL, Blocks.DEAD_BRAIN_CORAL, Blocks.BRAIN_CORAL_BLOCK, Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL_FAN, Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.BRAIN_CORAL_WALL_FAN, Blocks.DEAD_BRAIN_CORAL_WALL_FAN);
/* 4250 */     createCoral(Blocks.BUBBLE_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.BUBBLE_CORAL_BLOCK, Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.BUBBLE_CORAL_WALL_FAN, Blocks.DEAD_BUBBLE_CORAL_WALL_FAN);
/* 4251 */     createCoral(Blocks.FIRE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.FIRE_CORAL_BLOCK, Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.FIRE_CORAL_WALL_FAN, Blocks.DEAD_FIRE_CORAL_WALL_FAN);
/* 4252 */     createCoral(Blocks.HORN_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.HORN_CORAL_BLOCK, Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.HORN_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN, Blocks.HORN_CORAL_WALL_FAN, Blocks.DEAD_HORN_CORAL_WALL_FAN);
/* 4254 */     createStems(Blocks.MELON_STEM, Blocks.ATTACHED_MELON_STEM);
/* 4255 */     createStems(Blocks.PUMPKIN_STEM, Blocks.ATTACHED_PUMPKIN_STEM);
/* 4257 */     woodProvider(Blocks.MANGROVE_LOG).logWithHorizontal(Blocks.MANGROVE_LOG).wood(Blocks.MANGROVE_WOOD);
/* 4258 */     woodProvider(Blocks.STRIPPED_MANGROVE_LOG).logWithHorizontal(Blocks.STRIPPED_MANGROVE_LOG).wood(Blocks.STRIPPED_MANGROVE_WOOD);
/* 4259 */     createTintedLeaves(Blocks.MANGROVE_LEAVES, TexturedModel.LEAVES, -7158200);
/* 4261 */     woodProvider(Blocks.ACACIA_LOG).logWithHorizontal(Blocks.ACACIA_LOG).wood(Blocks.ACACIA_WOOD);
/* 4262 */     woodProvider(Blocks.STRIPPED_ACACIA_LOG).logWithHorizontal(Blocks.STRIPPED_ACACIA_LOG).wood(Blocks.STRIPPED_ACACIA_WOOD);
/* 4263 */     createPlantWithDefaultItem(Blocks.ACACIA_SAPLING, Blocks.POTTED_ACACIA_SAPLING, PlantType.NOT_TINTED);
/* 4264 */     createTintedLeaves(Blocks.ACACIA_LEAVES, TexturedModel.LEAVES, -12012264);
/* 4266 */     woodProvider(Blocks.CHERRY_LOG).logUVLocked(Blocks.CHERRY_LOG).wood(Blocks.CHERRY_WOOD);
/* 4267 */     woodProvider(Blocks.STRIPPED_CHERRY_LOG).logUVLocked(Blocks.STRIPPED_CHERRY_LOG).wood(Blocks.STRIPPED_CHERRY_WOOD);
/* 4268 */     createPlantWithDefaultItem(Blocks.CHERRY_SAPLING, Blocks.POTTED_CHERRY_SAPLING, PlantType.NOT_TINTED);
/* 4269 */     createTrivialBlock(Blocks.CHERRY_LEAVES, TexturedModel.LEAVES);
/* 4271 */     woodProvider(Blocks.BIRCH_LOG).logWithHorizontal(Blocks.BIRCH_LOG).wood(Blocks.BIRCH_WOOD);
/* 4272 */     woodProvider(Blocks.STRIPPED_BIRCH_LOG).logWithHorizontal(Blocks.STRIPPED_BIRCH_LOG).wood(Blocks.STRIPPED_BIRCH_WOOD);
/* 4273 */     createPlantWithDefaultItem(Blocks.BIRCH_SAPLING, Blocks.POTTED_BIRCH_SAPLING, PlantType.NOT_TINTED);
/* 4274 */     createTintedLeaves(Blocks.BIRCH_LEAVES, TexturedModel.LEAVES, -8345771);
/* 4276 */     woodProvider(Blocks.OAK_LOG).logWithHorizontal(Blocks.OAK_LOG).wood(Blocks.OAK_WOOD);
/* 4277 */     woodProvider(Blocks.STRIPPED_OAK_LOG).logWithHorizontal(Blocks.STRIPPED_OAK_LOG).wood(Blocks.STRIPPED_OAK_WOOD);
/* 4278 */     createPlantWithDefaultItem(Blocks.OAK_SAPLING, Blocks.POTTED_OAK_SAPLING, PlantType.NOT_TINTED);
/* 4279 */     createTintedLeaves(Blocks.OAK_LEAVES, TexturedModel.LEAVES, -12012264);
/* 4281 */     woodProvider(Blocks.SPRUCE_LOG).logWithHorizontal(Blocks.SPRUCE_LOG).wood(Blocks.SPRUCE_WOOD);
/* 4282 */     woodProvider(Blocks.STRIPPED_SPRUCE_LOG).logWithHorizontal(Blocks.STRIPPED_SPRUCE_LOG).wood(Blocks.STRIPPED_SPRUCE_WOOD);
/* 4283 */     createPlantWithDefaultItem(Blocks.SPRUCE_SAPLING, Blocks.POTTED_SPRUCE_SAPLING, PlantType.NOT_TINTED);
/* 4284 */     createTintedLeaves(Blocks.SPRUCE_LEAVES, TexturedModel.LEAVES, -10380959);
/* 4286 */     woodProvider(Blocks.DARK_OAK_LOG).logWithHorizontal(Blocks.DARK_OAK_LOG).wood(Blocks.DARK_OAK_WOOD);
/* 4287 */     woodProvider(Blocks.STRIPPED_DARK_OAK_LOG).logWithHorizontal(Blocks.STRIPPED_DARK_OAK_LOG).wood(Blocks.STRIPPED_DARK_OAK_WOOD);
/* 4288 */     createPlantWithDefaultItem(Blocks.DARK_OAK_SAPLING, Blocks.POTTED_DARK_OAK_SAPLING, PlantType.NOT_TINTED);
/* 4289 */     createTintedLeaves(Blocks.DARK_OAK_LEAVES, TexturedModel.LEAVES, -12012264);
/* 4291 */     woodProvider(Blocks.PALE_OAK_LOG).logWithHorizontal(Blocks.PALE_OAK_LOG).wood(Blocks.PALE_OAK_WOOD);
/* 4292 */     woodProvider(Blocks.STRIPPED_PALE_OAK_LOG).logWithHorizontal(Blocks.STRIPPED_PALE_OAK_LOG).wood(Blocks.STRIPPED_PALE_OAK_WOOD);
/* 4293 */     createPlantWithDefaultItem(Blocks.PALE_OAK_SAPLING, Blocks.POTTED_PALE_OAK_SAPLING, PlantType.NOT_TINTED);
/* 4294 */     createTrivialBlock(Blocks.PALE_OAK_LEAVES, TexturedModel.LEAVES);
/* 4296 */     woodProvider(Blocks.JUNGLE_LOG).logWithHorizontal(Blocks.JUNGLE_LOG).wood(Blocks.JUNGLE_WOOD);
/* 4297 */     woodProvider(Blocks.STRIPPED_JUNGLE_LOG).logWithHorizontal(Blocks.STRIPPED_JUNGLE_LOG).wood(Blocks.STRIPPED_JUNGLE_WOOD);
/* 4298 */     createPlantWithDefaultItem(Blocks.JUNGLE_SAPLING, Blocks.POTTED_JUNGLE_SAPLING, PlantType.NOT_TINTED);
/* 4299 */     createTintedLeaves(Blocks.JUNGLE_LEAVES, TexturedModel.LEAVES, -12012264);
/* 4301 */     woodProvider(Blocks.CRIMSON_STEM).log(Blocks.CRIMSON_STEM).wood(Blocks.CRIMSON_HYPHAE);
/* 4302 */     woodProvider(Blocks.STRIPPED_CRIMSON_STEM).log(Blocks.STRIPPED_CRIMSON_STEM).wood(Blocks.STRIPPED_CRIMSON_HYPHAE);
/* 4303 */     createPlantWithDefaultItem(Blocks.CRIMSON_FUNGUS, Blocks.POTTED_CRIMSON_FUNGUS, PlantType.NOT_TINTED);
/* 4304 */     createNetherRoots(Blocks.CRIMSON_ROOTS, Blocks.POTTED_CRIMSON_ROOTS);
/* 4306 */     woodProvider(Blocks.WARPED_STEM).log(Blocks.WARPED_STEM).wood(Blocks.WARPED_HYPHAE);
/* 4307 */     woodProvider(Blocks.STRIPPED_WARPED_STEM).log(Blocks.STRIPPED_WARPED_STEM).wood(Blocks.STRIPPED_WARPED_HYPHAE);
/* 4308 */     createPlantWithDefaultItem(Blocks.WARPED_FUNGUS, Blocks.POTTED_WARPED_FUNGUS, PlantType.NOT_TINTED);
/* 4309 */     createNetherRoots(Blocks.WARPED_ROOTS, Blocks.POTTED_WARPED_ROOTS);
/* 4311 */     woodProvider(Blocks.BAMBOO_BLOCK).logUVLocked(Blocks.BAMBOO_BLOCK);
/* 4312 */     woodProvider(Blocks.STRIPPED_BAMBOO_BLOCK).logUVLocked(Blocks.STRIPPED_BAMBOO_BLOCK);
/* 4314 */     createCrossBlock(Blocks.NETHER_SPROUTS, PlantType.NOT_TINTED);
/* 4315 */     registerSimpleFlatItemModel(Items.NETHER_SPROUTS);
/* 4317 */     createDoor(Blocks.IRON_DOOR);
/* 4318 */     createTrapdoor(Blocks.IRON_TRAPDOOR);
/* 4320 */     createSmoothStoneSlab();
/* 4322 */     createPassiveRail(Blocks.RAIL);
/* 4323 */     createActiveRail(Blocks.POWERED_RAIL);
/* 4324 */     createActiveRail(Blocks.DETECTOR_RAIL);
/* 4325 */     createActiveRail(Blocks.ACTIVATOR_RAIL);
/* 4327 */     createComparator();
/* 4329 */     createCommandBlock(Blocks.COMMAND_BLOCK);
/* 4330 */     createCommandBlock(Blocks.REPEATING_COMMAND_BLOCK);
/* 4331 */     createCommandBlock(Blocks.CHAIN_COMMAND_BLOCK);
/* 4333 */     createAnvil(Blocks.ANVIL);
/* 4334 */     createAnvil(Blocks.CHIPPED_ANVIL);
/* 4335 */     createAnvil(Blocks.DAMAGED_ANVIL);
/* 4337 */     createBarrel();
/* 4338 */     createBell();
/* 4340 */     createFurnace(Blocks.FURNACE, TexturedModel.ORIENTABLE_ONLY_TOP);
/* 4341 */     createFurnace(Blocks.BLAST_FURNACE, TexturedModel.ORIENTABLE_ONLY_TOP);
/* 4342 */     createFurnace(Blocks.SMOKER, TexturedModel.ORIENTABLE);
/* 4344 */     createRedstoneWire();
/* 4346 */     createRespawnAnchor();
/* 4347 */     createSculkCatalyst();
/* 4349 */     copyModel(Blocks.CHISELED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS);
/* 4350 */     copyModel(Blocks.COBBLESTONE, Blocks.INFESTED_COBBLESTONE);
/* 4351 */     copyModel(Blocks.CRACKED_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS);
/* 4352 */     copyModel(Blocks.MOSSY_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS);
/* 4353 */     createInfestedStone();
/* 4354 */     copyModel(Blocks.STONE_BRICKS, Blocks.INFESTED_STONE_BRICKS);
/* 4355 */     createInfestedDeepslate();
/*      */   }
/*      */   
/*      */   private void createLightBlock() {
/* 4359 */     ItemModel.Unbaked base = ItemModelUtils.plainModel(createFlatItemModel(Items.LIGHT));
/* 4361 */     Map<Integer, ItemModel.Unbaked> overrides = new HashMap<>(16);
/* 4363 */     PropertyDispatch.C1<MultiVariant, Integer> light = PropertyDispatch.initial((Property)BlockStateProperties.LEVEL);
/* 4364 */     for (int i = 0; i <= 15; i++) {
/* 4365 */       String suffix = String.format(Locale.ROOT, "_%02d", new Object[] { Integer.valueOf(i) });
/* 4366 */       Material texture = TextureMapping.getItemTexture(Items.LIGHT, suffix);
/* 4367 */       light.select(Integer.valueOf(i), plainVariant(ModelTemplates.PARTICLE_ONLY.createWithSuffix(Blocks.LIGHT, suffix, TextureMapping.particle(texture), this.modelOutput)));
/* 4368 */       ItemModel.Unbaked overrideItem = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(Items.LIGHT, suffix), TextureMapping.layer0(texture), this.modelOutput));
/* 4369 */       overrides.put(Integer.valueOf(i), overrideItem);
/*      */     } 
/* 4372 */     this.itemModelOutput.accept(Items.LIGHT, ItemModelUtils.selectBlockItemProperty((Property)LightBlock.LEVEL, base, overrides));
/* 4378 */     this.blockStateOutput.accept(
/* 4379 */         MultiVariantGenerator.dispatch(Blocks.LIGHT)
/* 4380 */         .with((PropertyDispatch)light));
/*      */   }
/*      */   
/*      */   private void createCopperChainItem(Item unwaxed, Item waxed) {
/* 4385 */     Identifier model = createFlatItemModel(unwaxed);
/* 4386 */     registerSimpleItemModel(unwaxed, model);
/* 4387 */     registerSimpleItemModel(waxed, model);
/*      */   }
/*      */   
/*      */   private void createCandleAndCandleCake(Block candleBlock, Block candleCakeBlock) {
/* 4391 */     registerSimpleFlatItemModel(candleBlock.asItem());
/* 4393 */     TextureMapping candleTexture = TextureMapping.cube(TextureMapping.getBlockTexture(candleBlock));
/* 4394 */     TextureMapping candleLitTexture = TextureMapping.cube(TextureMapping.getBlockTexture(candleBlock, "_lit"));
/* 4396 */     MultiVariant oneCandle = plainVariant(ModelTemplates.CANDLE.createWithSuffix(candleBlock, "_one_candle", candleTexture, this.modelOutput));
/* 4397 */     MultiVariant twoCandles = plainVariant(ModelTemplates.TWO_CANDLES.createWithSuffix(candleBlock, "_two_candles", candleTexture, this.modelOutput));
/* 4398 */     MultiVariant threeCandles = plainVariant(ModelTemplates.THREE_CANDLES.createWithSuffix(candleBlock, "_three_candles", candleTexture, this.modelOutput));
/* 4399 */     MultiVariant fourCandles = plainVariant(ModelTemplates.FOUR_CANDLES.createWithSuffix(candleBlock, "_four_candles", candleTexture, this.modelOutput));
/* 4401 */     MultiVariant oneCandleLit = plainVariant(ModelTemplates.CANDLE.createWithSuffix(candleBlock, "_one_candle_lit", candleLitTexture, this.modelOutput));
/* 4402 */     MultiVariant twoCandlesLit = plainVariant(ModelTemplates.TWO_CANDLES.createWithSuffix(candleBlock, "_two_candles_lit", candleLitTexture, this.modelOutput));
/* 4403 */     MultiVariant threeCandlesLit = plainVariant(ModelTemplates.THREE_CANDLES.createWithSuffix(candleBlock, "_three_candles_lit", candleLitTexture, this.modelOutput));
/* 4404 */     MultiVariant fourCandlesLit = plainVariant(ModelTemplates.FOUR_CANDLES.createWithSuffix(candleBlock, "_four_candles_lit", candleLitTexture, this.modelOutput));
/* 4406 */     this.blockStateOutput.accept(
/* 4407 */         MultiVariantGenerator.dispatch(candleBlock)
/* 4408 */         .with(
/* 4409 */           (PropertyDispatch)PropertyDispatch.initial((Property)BlockStateProperties.CANDLES, (Property)BlockStateProperties.LIT)
/* 4410 */           .select(Integer.valueOf(1), Boolean.valueOf(false), oneCandle)
/* 4411 */           .select(Integer.valueOf(2), Boolean.valueOf(false), twoCandles)
/* 4412 */           .select(Integer.valueOf(3), Boolean.valueOf(false), threeCandles)
/* 4413 */           .select(Integer.valueOf(4), Boolean.valueOf(false), fourCandles)
/* 4414 */           .select(Integer.valueOf(1), Boolean.valueOf(true), oneCandleLit)
/* 4415 */           .select(Integer.valueOf(2), Boolean.valueOf(true), twoCandlesLit)
/* 4416 */           .select(Integer.valueOf(3), Boolean.valueOf(true), threeCandlesLit)
/* 4417 */           .select(Integer.valueOf(4), Boolean.valueOf(true), fourCandlesLit)));
/* 4421 */     MultiVariant candleCake = plainVariant(ModelTemplates.CANDLE_CAKE.create(candleCakeBlock, TextureMapping.candleCake(candleBlock, false), this.modelOutput));
/* 4422 */     MultiVariant litCandleCake = plainVariant(ModelTemplates.CANDLE_CAKE.createWithSuffix(candleCakeBlock, "_lit", TextureMapping.candleCake(candleBlock, true), this.modelOutput));
/* 4423 */     this.blockStateOutput.accept(
/* 4424 */         MultiVariantGenerator.dispatch(candleCakeBlock)
/* 4425 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, litCandleCake, candleCake)));
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   private static interface BlockStateGeneratorSupplier {
/*      */     BlockModelDefinitionGenerator create(Block param1Block, Variant param1Variant, TextureMapping param1TextureMapping, BiConsumer<Identifier, ModelInstance> param1BiConsumer);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\BlockModelGenerators.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */