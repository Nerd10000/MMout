/*      */ package net.minecraft.world.level.block.state;
/*      */ 
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.kinds.Applicative;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.ToIntFunction;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.TypedInstance;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.resources.DependantName;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.MenuProvider;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.Projectile;
/*      */ import net.minecraft.world.flag.FeatureElement;
/*      */ import net.minecraft.world.flag.FeatureFlag;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.flag.FeatureFlags;
/*      */ import net.minecraft.world.item.DyeColor;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.context.BlockPlaceContext;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.EmptyBlockGetter;
/*      */ import net.minecraft.world.level.Explosion;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.ScheduledTickAccess;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.EntityBlock;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.RenderShape;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.SupportType;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*      */ import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.material.Fluids;
/*      */ import net.minecraft.world.level.material.MapColor;
/*      */ import net.minecraft.world.level.material.PushReaction;
/*      */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*      */ import net.minecraft.world.level.redstone.Orientation;
/*      */ import net.minecraft.world.level.storage.loot.LootParams;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ 
/*      */ public abstract class BlockBehaviour implements FeatureElement {
/*   90 */   protected static final Direction[] UPDATE_SHAPE_ORDER = new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP };
/*      */   
/*      */   protected final boolean hasCollision;
/*      */   
/*      */   protected final float explosionResistance;
/*      */   
/*      */   protected final boolean isRandomlyTicking;
/*      */   
/*      */   protected final SoundType soundType;
/*      */   
/*      */   protected final float friction;
/*      */   
/*      */   protected final float speedFactor;
/*      */   
/*      */   protected final float jumpFactor;
/*      */   
/*      */   protected final float bounceRestitution;
/*      */   
/*      */   protected final boolean dynamicShape;
/*      */   
/*      */   protected final FeatureFlagSet requiredFeatures;
/*      */   
/*      */   protected final Properties properties;
/*      */   
/*      */   protected final Optional<ResourceKey<LootTable>> drops;
/*      */   
/*      */   protected final String descriptionId;
/*      */   
/*      */   public BlockBehaviour(Properties properties) {
/*  108 */     this.hasCollision = properties.hasCollision;
/*  109 */     this.drops = properties.effectiveDrops();
/*  110 */     this.descriptionId = properties.effectiveDescriptionId();
/*  111 */     this.explosionResistance = properties.explosionResistance;
/*  112 */     this.isRandomlyTicking = properties.isRandomlyTicking;
/*  113 */     this.soundType = properties.soundType;
/*  114 */     this.friction = properties.friction;
/*  115 */     this.speedFactor = properties.speedFactor;
/*  116 */     this.jumpFactor = properties.jumpFactor;
/*  117 */     this.bounceRestitution = properties.bounceRestitution;
/*  118 */     this.dynamicShape = properties.dynamicShape;
/*  119 */     this.requiredFeatures = properties.requiredFeatures;
/*  120 */     this.properties = properties;
/*      */   }
/*      */   
/*      */   public Properties properties() {
/*  124 */     return this.properties;
/*      */   }
/*      */   
/*      */   protected static <B extends Block> RecordCodecBuilder<B, Properties> propertiesCodec() {
/*  134 */     return Properties.CODEC.fieldOf("properties").forGetter(BlockBehaviour::properties);
/*      */   }
/*      */   
/*      */   public static <B extends Block> MapCodec<B> simpleCodec(Function<Properties, B> constructor) {
/*  138 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)propertiesCodec()).apply((Applicative)i, constructor));
/*      */   }
/*      */   
/*      */   protected void updateIndirectNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos, int updateFlags, int updateLimit) {}
/*      */   
/*      */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/*  146 */     switch (type) {
/*      */       case LAND:
/*  146 */         return 
/*  147 */           !state.isCollisionShapeFullBlock((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
/*      */       case WATER:
/*      */       
/*      */       case AIR:
/*  149 */         return !state.isCollisionShapeFullBlock((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
/*      */     } 
/*      */     return false;
/*      */   }
/*      */   
/*      */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  162 */     return state;
/*      */   }
/*      */   
/*      */   protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
/*  166 */     return false;
/*      */   }
/*      */   
/*      */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {}
/*      */   
/*      */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {}
/*      */   
/*      */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {}
/*      */   
/*      */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/*  182 */     if (state.isAir() || explosion.getBlockInteraction() == Explosion.BlockInteraction.TRIGGER_BLOCK)
/*      */       return; 
/*  186 */     Block block = state.getBlock();
/*  187 */     boolean doDropExperienceHack = explosion.getIndirectSourceEntity() instanceof Player;
/*  189 */     if (block.dropFromExplosion(explosion)) {
/*  190 */       BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
/*  195 */       LootParams.Builder params = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)pos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity).withOptionalParameter(LootContextParams.THIS_ENTITY, explosion.getDirectSourceEntity());
/*  197 */       if (explosion.getBlockInteraction() == Explosion.BlockInteraction.DESTROY_WITH_DECAY)
/*  198 */         params.withParameter(LootContextParams.EXPLOSION_RADIUS, Float.valueOf(explosion.radius())); 
/*  201 */       state.spawnAfterBreak(level, pos, ItemStack.EMPTY, doDropExperienceHack);
/*  202 */       state.getDrops(params).forEach(stack -> onHit.accept(stack, pos));
/*      */     } 
/*  205 */     level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
/*  206 */     block.wasExploded(level, pos, explosion);
/*      */   }
/*      */   
/*      */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  212 */     return (InteractionResult)InteractionResult.PASS;
/*      */   }
/*      */   
/*      */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  217 */     return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*      */   }
/*      */   
/*      */   protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
/*  221 */     return false;
/*      */   }
/*      */   
/*      */   protected RenderShape getRenderShape(BlockState state) {
/*  225 */     return RenderShape.MODEL;
/*      */   }
/*      */   
/*      */   protected boolean useShapeForLightOcclusion(BlockState state) {
/*  229 */     return false;
/*      */   }
/*      */   
/*      */   protected FluidState getFluidState(BlockState state) {
/*  233 */     return Fluids.EMPTY.defaultFluidState();
/*      */   }
/*      */   
/*      */   protected boolean hasAnalogOutputSignal(BlockState state) {
/*  237 */     return false;
/*      */   }
/*      */   
/*      */   protected float getMaxHorizontalOffset() {
/*  241 */     return 0.25F;
/*      */   }
/*      */   
/*      */   protected float getMaxVerticalOffset() {
/*  245 */     return 0.2F;
/*      */   }
/*      */   
/*      */   public FeatureFlagSet requiredFeatures() {
/*  250 */     return this.requiredFeatures;
/*      */   }
/*      */   
/*      */   protected boolean shouldChangedStateKeepBlockEntity(BlockState oldState) {
/*  254 */     return false;
/*      */   }
/*      */   
/*      */   protected BlockState rotate(BlockState state, Rotation rotation) {
/*  258 */     return state;
/*      */   }
/*      */   
/*      */   protected BlockState mirror(BlockState state, Mirror mirror) {
/*  262 */     return state;
/*      */   }
/*      */   
/*      */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/*  271 */     return (state.canBeReplaced() && (context.getItemInHand().isEmpty() || !context.getItemInHand().is(asItem())));
/*      */   }
/*      */   
/*      */   protected boolean canBeReplaced(BlockState state, Fluid fluid) {
/*  275 */     return (state.canBeReplaced() || !state.isSolid());
/*      */   }
/*      */   
/*      */   protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
/*  279 */     if (this.drops.isEmpty())
/*  280 */       return Collections.emptyList(); 
/*  282 */     LootParams lootParams = params.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
/*  283 */     ServerLevel level = lootParams.getLevel();
/*  284 */     LootTable table = level.getServer().reloadableRegistries().getLootTable(this.drops.get());
/*  285 */     return (List<ItemStack>)table.getRandomItems(lootParams);
/*      */   }
/*      */   
/*      */   protected long getSeed(BlockState state, BlockPos pos) {
/*  289 */     return Mth.getSeed((Vec3i)pos);
/*      */   }
/*      */   
/*      */   protected VoxelShape getOcclusionShape(BlockState state) {
/*  293 */     return state.getShape((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
/*      */   }
/*      */   
/*      */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/*  297 */     return getCollisionShape(state, level, pos, CollisionContext.empty());
/*      */   }
/*      */   
/*      */   protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
/*  301 */     return Shapes.empty();
/*      */   }
/*      */   
/*      */   protected int getLightDampening(BlockState state) {
/*  307 */     if (state.isSolidRender())
/*  308 */       return 15; 
/*  310 */     return state.propagatesSkylightDown() ? 0 : 1;
/*      */   }
/*      */   
/*      */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
/*  314 */     return null;
/*      */   }
/*      */   
/*      */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  318 */     return true;
/*      */   }
/*      */   
/*      */   protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
/*  322 */     return state.isCollisionShapeFullBlock(level, pos) ? 0.2F : 1.0F;
/*      */   }
/*      */   
/*      */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/*  326 */     return 0;
/*      */   }
/*      */   
/*      */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  330 */     return Shapes.block();
/*      */   }
/*      */   
/*      */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  334 */     return this.hasCollision ? state.getShape(level, pos) : Shapes.empty();
/*      */   }
/*      */   
/*      */   protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
/*  338 */     return Shapes.block();
/*      */   }
/*      */   
/*      */   protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
/*  342 */     return Block.isShapeFullBlock(state.getCollisionShape(level, pos));
/*      */   }
/*      */   
/*      */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  346 */     return getCollisionShape(state, level, pos, context);
/*      */   }
/*      */   
/*      */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {}
/*      */   
/*      */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {}
/*      */   
/*      */   protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
/*  356 */     float destroySpeed = state.getDestroySpeed(level, pos);
/*  357 */     if (destroySpeed == -1.0F)
/*  358 */       return 0.0F; 
/*  360 */     int modifier = player.hasCorrectToolForDrops(state) ? 30 : 100;
/*  361 */     return player.getDestroySpeed(state) / destroySpeed / modifier;
/*      */   }
/*      */   
/*      */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {}
/*      */   
/*      */   protected void attack(BlockState state, Level level, BlockPos pos, Player player) {}
/*      */   
/*      */   protected boolean isSignalSource(BlockState state) {
/*  371 */     return false;
/*      */   }
/*      */   
/*      */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  375 */     return ownSignal(state, level, pos);
/*      */   }
/*      */   
/*      */   protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
/*  379 */     return 0;
/*      */   }
/*      */   
/*      */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {}
/*      */   
/*      */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  388 */     return 0;
/*      */   }
/*      */   
/*      */   public final Optional<ResourceKey<LootTable>> getLootTable() {
/*  392 */     return this.drops;
/*      */   }
/*      */   
/*      */   public final String getDescriptionId() {
/*  396 */     return this.descriptionId;
/*      */   }
/*      */   
/*      */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {}
/*      */   
/*      */   protected boolean propagatesSkylightDown(BlockState state) {
/*  403 */     return (!Block.isShapeFullBlock(state.getShape((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) && state.getFluidState().isEmpty());
/*      */   }
/*      */   
/*      */   protected boolean isRandomlyTicking(BlockState state) {
/*  407 */     return this.isRandomlyTicking;
/*      */   }
/*      */   
/*      */   protected SoundType getSoundType(BlockState state) {
/*  411 */     return this.soundType;
/*      */   }
/*      */   
/*      */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/*  416 */     return new ItemStack((ItemLike)asItem());
/*      */   }
/*      */   
/*      */   public enum OffsetType {
/*  424 */     NONE, XZ, XYZ;
/*      */   }
/*      */   
/*      */   public MapColor defaultMapColor() {
/*  430 */     return this.properties.mapColor.apply(asBlock().defaultBlockState());
/*      */   }
/*      */   
/*      */   public float defaultDestroyTime() {
/*  434 */     return this.properties.destroyTime;
/*      */   }
/*      */   
/*      */   protected abstract MapCodec<? extends Block> codec();
/*      */   
/*      */   public abstract Item asItem();
/*      */   
/*      */   protected abstract Block asBlock();
/*      */   
/*      */   public static class Properties {
/*  439 */     public static final Codec<Properties> CODEC = MapCodec.unitCodec(() -> of());
/*      */     
/*      */     private Function<BlockState, MapColor> mapColor = state -> MapColor.NONE;
/*      */     
/*      */     private boolean hasCollision = true;
/*      */     
/*  444 */     private SoundType soundType = SoundType.STONE;
/*      */     
/*      */     private ToIntFunction<BlockState> lightEmission = state -> 0;
/*      */     
/*      */     private float explosionResistance;
/*      */     
/*      */     private float destroyTime;
/*      */     
/*      */     private boolean requiresCorrectToolForDrops;
/*      */     
/*      */     private boolean isRandomlyTicking;
/*      */     
/*  450 */     private float friction = 0.6F;
/*      */     
/*  451 */     private float speedFactor = 1.0F;
/*      */     
/*  452 */     private float jumpFactor = 1.0F;
/*      */     
/*      */     private float bounceRestitution;
/*      */     
/*      */     private ResourceKey<Block> id;
/*      */     
/*      */     private DependantName<Block, Optional<ResourceKey<LootTable>>> drops;
/*      */     
/*      */     private DependantName<Block, String> descriptionId;
/*      */     
/*      */     private boolean canOcclude;
/*      */     
/*      */     private boolean isAir;
/*      */     
/*      */     private boolean ignitedByLava;
/*      */     
/*      */     @Deprecated
/*      */     private boolean liquid;
/*      */     
/*      */     @Deprecated
/*      */     private boolean forceSolidOff;
/*      */     
/*      */     private boolean forceSolidOn;
/*      */     
/*      */     private PushReaction pushReaction;
/*      */     
/*      */     private boolean spawnTerrainParticles;
/*      */     
/*      */     private NoteBlockInstrument instrument;
/*      */     
/*      */     private boolean replaceable;
/*      */     
/*      */     private BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn;
/*      */     
/*      */     private BlockBehaviour.StatePredicate isRedstoneConductor;
/*      */     
/*      */     private BlockBehaviour.StatePredicate isSuffocating;
/*      */     
/*      */     private BlockBehaviour.StatePredicate isViewBlocking;
/*      */     
/*      */     private BlockBehaviour.PostProcess postProcess;
/*      */     
/*      */     private Predicate<BlockState> emissiveRendering;
/*      */     
/*      */     private boolean dynamicShape;
/*      */     
/*      */     private FeatureFlagSet requiredFeatures;
/*      */     
/*      */     private BlockBehaviour.OffsetFunction offsetFunction;
/*      */     
/*      */     private Properties() {
/*  456 */       this.drops = (id -> Optional.of(ResourceKey.create(Registries.LOOT_TABLE, id.identifier().withPrefix("blocks/"))));
/*  457 */       this.descriptionId = (id -> Util.makeDescriptionId("block", id.identifier()));
/*  458 */       this.canOcclude = true;
/*  466 */       this.pushReaction = PushReaction.NORMAL;
/*  467 */       this.spawnTerrainParticles = true;
/*  468 */       this.instrument = NoteBlockInstrument.HARP;
/*  471 */       this.isValidSpawn = ((state, level, pos, entityType) -> 
/*  472 */         (state.isFaceSturdy(level, pos, Direction.UP) && state.getLightEmission() < 14));
/*  474 */       this.isRedstoneConductor = BlockBehaviour.BlockStateBase::isCollisionShapeFullBlock;
/*  476 */       this.isSuffocating = ((state, level, pos) -> 
/*  477 */         (state.blocksMotion() && state.isCollisionShapeFullBlock(level, pos)));
/*  479 */       this.isViewBlocking = this.isSuffocating;
/*  480 */       this.postProcess = ((state, level, pos) -> null);
/*  481 */       this.emissiveRendering = (param1BlockState -> false);
/*  484 */       this.requiredFeatures = FeatureFlags.VANILLA_SET;
/*      */     }
/*      */     
/*      */     public static Properties of() {
/*  492 */       return new Properties();
/*      */     }
/*      */     
/*      */     public static Properties ofFullCopy(BlockBehaviour block) {
/*  502 */       Properties copyTo = ofLegacyCopy(block);
/*  503 */       Properties copyFrom = block.properties;
/*  505 */       copyTo.jumpFactor = copyFrom.jumpFactor;
/*  506 */       copyTo.isRedstoneConductor = copyFrom.isRedstoneConductor;
/*  507 */       copyTo.isValidSpawn = copyFrom.isValidSpawn;
/*  508 */       copyTo.postProcess = copyFrom.postProcess;
/*  509 */       copyTo.isSuffocating = copyFrom.isSuffocating;
/*  510 */       copyTo.isViewBlocking = copyFrom.isViewBlocking;
/*  511 */       copyTo.drops = copyFrom.drops;
/*  512 */       copyTo.descriptionId = copyFrom.descriptionId;
/*  514 */       return copyTo;
/*      */     }
/*      */     
/*      */     @Deprecated
/*      */     public static Properties ofLegacyCopy(BlockBehaviour block) {
/*  521 */       Properties copyTo = new Properties();
/*  522 */       Properties copyFrom = block.properties;
/*  524 */       copyTo.destroyTime = copyFrom.destroyTime;
/*  525 */       copyTo.explosionResistance = copyFrom.explosionResistance;
/*  526 */       copyTo.hasCollision = copyFrom.hasCollision;
/*  527 */       copyTo.isRandomlyTicking = copyFrom.isRandomlyTicking;
/*  528 */       copyTo.lightEmission = copyFrom.lightEmission;
/*  529 */       copyTo.mapColor = copyFrom.mapColor;
/*  530 */       copyTo.soundType = copyFrom.soundType;
/*  531 */       copyTo.friction = copyFrom.friction;
/*  532 */       copyTo.speedFactor = copyFrom.speedFactor;
/*  533 */       copyTo.bounceRestitution = copyFrom.bounceRestitution;
/*  534 */       copyTo.dynamicShape = copyFrom.dynamicShape;
/*  535 */       copyTo.canOcclude = copyFrom.canOcclude;
/*  536 */       copyTo.isAir = copyFrom.isAir;
/*  537 */       copyTo.ignitedByLava = copyFrom.ignitedByLava;
/*  538 */       copyTo.liquid = copyFrom.liquid;
/*  539 */       copyTo.forceSolidOff = copyFrom.forceSolidOff;
/*  540 */       copyTo.forceSolidOn = copyFrom.forceSolidOn;
/*  541 */       copyTo.pushReaction = copyFrom.pushReaction;
/*  542 */       copyTo.requiresCorrectToolForDrops = copyFrom.requiresCorrectToolForDrops;
/*  543 */       copyTo.offsetFunction = copyFrom.offsetFunction;
/*  544 */       copyTo.spawnTerrainParticles = copyFrom.spawnTerrainParticles;
/*  545 */       copyTo.requiredFeatures = copyFrom.requiredFeatures;
/*  546 */       copyTo.emissiveRendering = copyFrom.emissiveRendering;
/*  547 */       copyTo.instrument = copyFrom.instrument;
/*  548 */       copyTo.replaceable = copyFrom.replaceable;
/*  550 */       return copyTo;
/*      */     }
/*      */     
/*      */     public Properties mapColor(DyeColor dyeColor) {
/*  554 */       this.mapColor = (state -> dyeColor.getMapColor());
/*  555 */       return this;
/*      */     }
/*      */     
/*      */     public Properties mapColor(MapColor mapColor) {
/*  559 */       this.mapColor = (state -> mapColor);
/*  560 */       return this;
/*      */     }
/*      */     
/*      */     public Properties mapColor(Function<BlockState, MapColor> mapColor) {
/*  564 */       this.mapColor = mapColor;
/*  565 */       return this;
/*      */     }
/*      */     
/*      */     public Properties noCollision() {
/*  569 */       this.hasCollision = false;
/*  570 */       this.canOcclude = false;
/*  571 */       return this;
/*      */     }
/*      */     
/*      */     public Properties noOcclusion() {
/*  575 */       this.canOcclude = false;
/*  576 */       return this;
/*      */     }
/*      */     
/*      */     public Properties friction(float friction) {
/*  580 */       this.friction = friction;
/*  581 */       return this;
/*      */     }
/*      */     
/*      */     public Properties speedFactor(float speedFactor) {
/*  585 */       this.speedFactor = speedFactor;
/*  586 */       return this;
/*      */     }
/*      */     
/*      */     public Properties jumpFactor(float jumpFactor) {
/*  590 */       this.jumpFactor = jumpFactor;
/*  591 */       return this;
/*      */     }
/*      */     
/*      */     public Properties bounceRestitution(float bounceRestitution) {
/*  595 */       this.bounceRestitution = bounceRestitution;
/*  596 */       return this;
/*      */     }
/*      */     
/*      */     public Properties sound(SoundType soundType) {
/*  600 */       this.soundType = soundType;
/*  601 */       return this;
/*      */     }
/*      */     
/*      */     public Properties lightLevel(ToIntFunction<BlockState> lightEmission) {
/*  605 */       this.lightEmission = lightEmission;
/*  606 */       return this;
/*      */     }
/*      */     
/*      */     public Properties strength(float destroyTime, float explosionResistance) {
/*  610 */       return destroyTime(destroyTime).explosionResistance(explosionResistance);
/*      */     }
/*      */     
/*      */     public Properties instabreak() {
/*  614 */       return strength(0.0F);
/*      */     }
/*      */     
/*      */     public Properties strength(float destroyTime) {
/*  618 */       strength(destroyTime, destroyTime);
/*  619 */       return this;
/*      */     }
/*      */     
/*      */     public Properties randomTicks() {
/*  623 */       this.isRandomlyTicking = true;
/*  624 */       return this;
/*      */     }
/*      */     
/*      */     public Properties dynamicShape() {
/*  628 */       this.dynamicShape = true;
/*  629 */       return this;
/*      */     }
/*      */     
/*      */     public Properties noLootTable() {
/*  635 */       this.drops = DependantName.fixed(Optional.empty());
/*  636 */       return this;
/*      */     }
/*      */     
/*      */     public Properties overrideLootTable(Optional<ResourceKey<LootTable>> table) {
/*  640 */       this.drops = DependantName.fixed(table);
/*  641 */       return this;
/*      */     }
/*      */     
/*      */     protected Optional<ResourceKey<LootTable>> effectiveDrops() {
/*  645 */       return (Optional<ResourceKey<LootTable>>)this.drops.get(Objects.<ResourceKey>requireNonNull(this.id, "Block id not set"));
/*      */     }
/*      */     
/*      */     public Properties ignitedByLava() {
/*  649 */       this.ignitedByLava = true;
/*  650 */       return this;
/*      */     }
/*      */     
/*      */     public Properties liquid() {
/*  654 */       this.liquid = true;
/*  655 */       return this;
/*      */     }
/*      */     
/*      */     public Properties forceSolidOn() {
/*  660 */       this.forceSolidOn = true;
/*  661 */       return this;
/*      */     }
/*      */     
/*      */     @Deprecated
/*      */     public Properties forceSolidOff() {
/*  668 */       this.forceSolidOff = true;
/*  669 */       return this;
/*      */     }
/*      */     
/*      */     public Properties pushReaction(PushReaction pushReaction) {
/*  673 */       this.pushReaction = pushReaction;
/*  674 */       return this;
/*      */     }
/*      */     
/*      */     public Properties air() {
/*  678 */       this.isAir = true;
/*  679 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn) {
/*  683 */       this.isValidSpawn = isValidSpawn;
/*  684 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isRedstoneConductor(BlockBehaviour.StatePredicate isRedstoneConductor) {
/*  688 */       this.isRedstoneConductor = isRedstoneConductor;
/*  689 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isSuffocating(BlockBehaviour.StatePredicate isSuffocating) {
/*  693 */       this.isSuffocating = isSuffocating;
/*  694 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isViewBlocking(BlockBehaviour.StatePredicate isViewBlocking) {
/*  698 */       this.isViewBlocking = isViewBlocking;
/*  699 */       return this;
/*      */     }
/*      */     
/*      */     public Properties postProcess(BlockBehaviour.PostProcess postProcess) {
/*  704 */       this.postProcess = postProcess;
/*  705 */       return this;
/*      */     }
/*      */     
/*      */     public Properties emissiveRendering(Predicate<BlockState> emissiveRendering) {
/*  709 */       this.emissiveRendering = emissiveRendering;
/*  710 */       return this;
/*      */     }
/*      */     
/*      */     public Properties requiresCorrectToolForDrops() {
/*  714 */       this.requiresCorrectToolForDrops = true;
/*  715 */       return this;
/*      */     }
/*      */     
/*      */     public Properties destroyTime(float destroyTime) {
/*  719 */       this.destroyTime = destroyTime;
/*  720 */       return this;
/*      */     }
/*      */     
/*      */     public Properties explosionResistance(float explosionResistance) {
/*  724 */       this.explosionResistance = Math.max(0.0F, explosionResistance);
/*  725 */       return this;
/*      */     }
/*      */     
/*      */     public Properties offsetType(BlockBehaviour.OffsetType offsetType) {
/*  729 */       switch (offsetType.ordinal()) {
/*      */         default:
/*  729 */           throw new MatchException(null, null);
/*      */         case 0:
/*      */         
/*      */         case 2:
/*      */         
/*      */         case 1:
/*      */           break;
/*      */       } 
/*  729 */       this.offsetFunction = ((state, pos) -> {
/*      */           Block block = state.getBlock();
/*      */           long seed = Mth.getSeed(pos.getX(), 0, pos.getZ());
/*      */           float maxHorizontalOffset = block.getMaxHorizontalOffset();
/*      */           double x = Mth.clamp((((float)(seed & 0xFL) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
/*      */           double z = Mth.clamp((((float)(seed >> 8L & 0xFL) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
/*      */           return new Vec3(x, 0.0D, z);
/*      */         });
/*  751 */       return this;
/*      */     }
/*      */     
/*      */     public Properties noTerrainParticles() {
/*  755 */       this.spawnTerrainParticles = false;
/*  756 */       return this;
/*      */     }
/*      */     
/*      */     public Properties requiredFeatures(FeatureFlag... flags) {
/*  760 */       this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/*  761 */       return this;
/*      */     }
/*      */     
/*      */     public Properties instrument(NoteBlockInstrument instrument) {
/*  765 */       this.instrument = instrument;
/*  766 */       return this;
/*      */     }
/*      */     
/*      */     public Properties replaceable() {
/*  770 */       this.replaceable = true;
/*  771 */       return this;
/*      */     }
/*      */     
/*      */     public Properties setId(ResourceKey<Block> id) {
/*  775 */       this.id = id;
/*  776 */       return this;
/*      */     }
/*      */     
/*      */     public Properties overrideDescription(String descriptionId) {
/*  780 */       this.descriptionId = DependantName.fixed(descriptionId);
/*  781 */       return this;
/*      */     }
/*      */     
/*      */     protected String effectiveDescriptionId() {
/*  785 */       return (String)this.descriptionId.get(Objects.<ResourceKey>requireNonNull(this.id, "Block id not set"));
/*      */     }
/*      */   }
/*      */   
/*      */   public static abstract class BlockStateBase extends StateHolder<Block, BlockState> implements TypedInstance<Block> {
/*  790 */     private static final Direction[] DIRECTIONS = Direction.values();
/*      */     
/*      */     private static final VoxelShape[] EMPTY_OCCLUSION_SHAPES;
/*      */     
/*      */     private static final VoxelShape[] FULL_BLOCK_OCCLUSION_SHAPES;
/*      */     
/*      */     private final int lightEmission;
/*      */     
/*      */     private final boolean useShapeForLightOcclusion;
/*      */     
/*      */     private final boolean isAir;
/*      */     
/*      */     private final boolean ignitedByLava;
/*      */     
/*      */     @Deprecated
/*      */     private final boolean liquid;
/*      */     
/*      */     @Deprecated
/*      */     private boolean legacySolid;
/*      */     
/*      */     private final PushReaction pushReaction;
/*      */     
/*      */     private final MapColor mapColor;
/*      */     
/*      */     private final float destroySpeed;
/*      */     
/*      */     private final boolean requiresCorrectToolForDrops;
/*      */     
/*      */     private final boolean canOcclude;
/*      */     
/*      */     private final BlockBehaviour.StatePredicate isRedstoneConductor;
/*      */     
/*      */     private final BlockBehaviour.StatePredicate isSuffocating;
/*      */     
/*      */     private final BlockBehaviour.StatePredicate isViewBlocking;
/*      */     
/*      */     private final BlockBehaviour.PostProcess postProcess;
/*      */     
/*      */     private final Predicate<BlockState> emissiveRendering;
/*      */     
/*      */     private final BlockBehaviour.OffsetFunction offsetFunction;
/*      */     
/*      */     private final boolean spawnTerrainParticles;
/*      */     
/*      */     private final NoteBlockInstrument instrument;
/*      */     
/*      */     private final boolean replaceable;
/*      */     
/*      */     private Cache cache;
/*      */     
/*      */     static {
/*  791 */       EMPTY_OCCLUSION_SHAPES = (VoxelShape[])Util.make(new VoxelShape[DIRECTIONS.length], s -> Arrays.fill((Object[])s, Shapes.empty()));
/*  792 */       FULL_BLOCK_OCCLUSION_SHAPES = (VoxelShape[])Util.make(new VoxelShape[DIRECTIONS.length], s -> Arrays.fill((Object[])s, Shapes.block()));
/*      */     }
/*      */     
/*  818 */     private FluidState fluidState = Fluids.EMPTY.defaultFluidState();
/*      */     
/*      */     private boolean isRandomlyTicking;
/*      */     
/*      */     private boolean solidRender;
/*      */     
/*      */     private VoxelShape occlusionShape;
/*      */     
/*      */     private VoxelShape[] occlusionShapesByFace;
/*      */     
/*      */     private boolean propagatesSkylightDown;
/*      */     
/*      */     private int lightDampening;
/*      */     
/*      */     protected BlockStateBase(Block owner, Property<?>[] propertyKeys, Comparable<?>[] propertyValues) {
/*  827 */       super(owner, propertyKeys, propertyValues);
/*  828 */       BlockBehaviour.Properties properties = owner.properties;
/*  830 */       this.lightEmission = properties.lightEmission.applyAsInt(asState());
/*  831 */       this.useShapeForLightOcclusion = owner.useShapeForLightOcclusion(asState());
/*  832 */       this.isAir = properties.isAir;
/*  833 */       this.ignitedByLava = properties.ignitedByLava;
/*  834 */       this.liquid = properties.liquid;
/*  835 */       this.pushReaction = properties.pushReaction;
/*  836 */       this.mapColor = properties.mapColor.apply(asState());
/*  837 */       this.destroySpeed = properties.destroyTime;
/*  838 */       this.requiresCorrectToolForDrops = properties.requiresCorrectToolForDrops;
/*  839 */       this.canOcclude = properties.canOcclude;
/*  840 */       this.isRedstoneConductor = properties.isRedstoneConductor;
/*  841 */       this.isSuffocating = properties.isSuffocating;
/*  842 */       this.isViewBlocking = properties.isViewBlocking;
/*  843 */       this.postProcess = properties.postProcess;
/*  844 */       this.emissiveRendering = properties.emissiveRendering;
/*  845 */       this.offsetFunction = properties.offsetFunction;
/*  846 */       this.spawnTerrainParticles = properties.spawnTerrainParticles;
/*  847 */       this.instrument = properties.instrument;
/*  848 */       this.replaceable = properties.replaceable;
/*      */     }
/*      */     
/*      */     private boolean calculateSolid() {
/*  855 */       if (this.owner.properties.forceSolidOn)
/*  856 */         return true; 
/*  858 */       if (this.owner.properties.forceSolidOff)
/*  859 */         return false; 
/*  861 */       if (this.cache == null)
/*  862 */         return false; 
/*  864 */       VoxelShape shape = this.cache.collisionShape;
/*  865 */       if (shape.isEmpty())
/*  866 */         return false; 
/*  868 */       AABB bounds = shape.bounds();
/*  869 */       if (bounds.getSize() >= 0.7291666666666666D)
/*  870 */         return true; 
/*  872 */       if (bounds.getYsize() >= 1.0D)
/*  873 */         return true; 
/*  875 */       return false;
/*      */     }
/*      */     
/*      */     public void initCache() {
/*  879 */       this.fluidState = this.owner.getFluidState(asState());
/*  880 */       this.isRandomlyTicking = this.owner.isRandomlyTicking(asState());
/*  881 */       if (!getBlock().hasDynamicShape())
/*  882 */         this.cache = new Cache(asState()); 
/*  884 */       this.legacySolid = calculateSolid();
/*  886 */       this.occlusionShape = this.canOcclude ? this.owner.getOcclusionShape(asState()) : Shapes.empty();
/*  887 */       this.solidRender = Block.isShapeFullBlock(this.occlusionShape);
/*  889 */       if (this.occlusionShape.isEmpty()) {
/*  890 */         this.occlusionShapesByFace = EMPTY_OCCLUSION_SHAPES;
/*  891 */       } else if (this.solidRender) {
/*  892 */         this.occlusionShapesByFace = FULL_BLOCK_OCCLUSION_SHAPES;
/*      */       } else {
/*  894 */         this.occlusionShapesByFace = new VoxelShape[DIRECTIONS.length];
/*  895 */         for (Direction direction : DIRECTIONS)
/*  896 */           this.occlusionShapesByFace[direction.ordinal()] = this.occlusionShape.getFaceShape(direction); 
/*      */       } 
/*  900 */       this.propagatesSkylightDown = this.owner.propagatesSkylightDown(asState());
/*  901 */       this.lightDampening = this.owner.getLightDampening(asState());
/*      */     }
/*      */     
/*      */     public Block getBlock() {
/*  905 */       return this.owner;
/*      */     }
/*      */     
/*      */     public Holder<Block> typeHolder() {
/*  910 */       return (Holder<Block>)getBlock().builtInRegistryHolder();
/*      */     }
/*      */     
/*      */     @Deprecated
/*      */     public boolean blocksMotion() {
/*  924 */       Block block = getBlock();
/*  925 */       return (block != Blocks.COBWEB && block != Blocks.BAMBOO_SAPLING && isSolid());
/*      */     }
/*      */     
/*      */     @Deprecated
/*      */     public boolean isSolid() {
/*  941 */       return this.legacySolid;
/*      */     }
/*      */     
/*      */     public boolean isValidSpawn(BlockGetter level, BlockPos pos, EntityType<?> type) {
/*  945 */       return (getBlock()).properties.isValidSpawn.test(asState(), level, pos, type);
/*      */     }
/*      */     
/*      */     public boolean propagatesSkylightDown() {
/*  949 */       return this.propagatesSkylightDown;
/*      */     }
/*      */     
/*      */     public int getLightDampening() {
/*  954 */       return this.lightDampening;
/*      */     }
/*      */     
/*      */     public VoxelShape getFaceOcclusionShape(Direction direction) {
/*  958 */       return this.occlusionShapesByFace[direction.ordinal()];
/*      */     }
/*      */     
/*      */     public VoxelShape getOcclusionShape() {
/*  962 */       return this.occlusionShape;
/*      */     }
/*      */     
/*      */     public boolean hasLargeCollisionShape() {
/*  967 */       return (this.cache == null || this.cache.largeCollisionShape);
/*      */     }
/*      */     
/*      */     public boolean useShapeForLightOcclusion() {
/*  971 */       return this.useShapeForLightOcclusion;
/*      */     }
/*      */     
/*      */     public int getLightEmission() {
/*  975 */       return this.lightEmission;
/*      */     }
/*      */     
/*      */     public boolean isAir() {
/*  979 */       return this.isAir;
/*      */     }
/*      */     
/*      */     public boolean ignitedByLava() {
/*  983 */       return this.ignitedByLava;
/*      */     }
/*      */     
/*      */     @Deprecated
/*      */     public boolean liquid() {
/*  989 */       return this.liquid;
/*      */     }
/*      */     
/*      */     public MapColor getMapColor(BlockGetter level, BlockPos pos) {
/*  993 */       return this.mapColor;
/*      */     }
/*      */     
/*      */     public BlockState rotate(Rotation rotation) {
/*  997 */       return getBlock().rotate(asState(), rotation);
/*      */     }
/*      */     
/*      */     public BlockState mirror(Mirror mirror) {
/* 1001 */       return getBlock().mirror(asState(), mirror);
/*      */     }
/*      */     
/*      */     public RenderShape getRenderShape() {
/* 1005 */       return getBlock().getRenderShape(asState());
/*      */     }
/*      */     
/*      */     public boolean emissiveRendering() {
/* 1009 */       return this.emissiveRendering.test(asState());
/*      */     }
/*      */     
/*      */     public float getShadeBrightness(BlockGetter level, BlockPos pos) {
/* 1013 */       return getBlock().getShadeBrightness(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public boolean isRedstoneConductor(BlockGetter level, BlockPos pos) {
/* 1017 */       return this.isRedstoneConductor.test(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public boolean isSignalSource() {
/* 1021 */       return getBlock().isSignalSource(asState());
/*      */     }
/*      */     
/*      */     public int getOwnSignal(BlockGetter level, BlockPos pos) {
/* 1025 */       return getBlock().ownSignal(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public int getSignal(BlockGetter level, BlockPos pos, Direction direction) {
/* 1029 */       return getBlock().getSignal(asState(), level, pos, direction);
/*      */     }
/*      */     
/*      */     public boolean hasAnalogOutputSignal() {
/* 1033 */       return getBlock().hasAnalogOutputSignal(asState());
/*      */     }
/*      */     
/*      */     public int getAnalogOutputSignal(Level level, BlockPos pos, Direction direction) {
/* 1037 */       return getBlock().getAnalogOutputSignal(asState(), level, pos, direction);
/*      */     }
/*      */     
/*      */     public float getDestroySpeed(BlockGetter level, BlockPos pos) {
/* 1041 */       return this.destroySpeed;
/*      */     }
/*      */     
/*      */     public float getDestroyProgress(Player player, BlockGetter level, BlockPos pos) {
/* 1045 */       return getBlock().getDestroyProgress(asState(), player, level, pos);
/*      */     }
/*      */     
/*      */     public int getDirectSignal(BlockGetter level, BlockPos pos, Direction direction) {
/* 1049 */       return getBlock().getDirectSignal(asState(), level, pos, direction);
/*      */     }
/*      */     
/*      */     public PushReaction getPistonPushReaction() {
/* 1053 */       return this.pushReaction;
/*      */     }
/*      */     
/*      */     public boolean isSolidRender() {
/* 1057 */       return this.solidRender;
/*      */     }
/*      */     
/*      */     public boolean canOcclude() {
/* 1061 */       return this.canOcclude;
/*      */     }
/*      */     
/*      */     public boolean skipRendering(BlockState neighborState, Direction direction) {
/* 1065 */       return getBlock().skipRendering(asState(), neighborState, direction);
/*      */     }
/*      */     
/*      */     public VoxelShape getShape(BlockGetter level, BlockPos pos) {
/* 1069 */       return getShape(level, pos, CollisionContext.empty());
/*      */     }
/*      */     
/*      */     public VoxelShape getShape(BlockGetter level, BlockPos pos, CollisionContext context) {
/* 1073 */       return getBlock().getShape(asState(), level, pos, context);
/*      */     }
/*      */     
/*      */     public VoxelShape getCollisionShape(BlockGetter level, BlockPos pos) {
/* 1077 */       if (this.cache != null)
/* 1078 */         return this.cache.collisionShape; 
/* 1080 */       return getCollisionShape(level, pos, CollisionContext.empty());
/*      */     }
/*      */     
/*      */     public VoxelShape getCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context) {
/* 1084 */       return getBlock().getCollisionShape(asState(), level, pos, context);
/*      */     }
/*      */     
/*      */     public VoxelShape getEntityInsideCollisionShape(BlockGetter level, BlockPos pos, Entity entity) {
/* 1088 */       return getBlock().getEntityInsideCollisionShape(asState(), level, pos, entity);
/*      */     }
/*      */     
/*      */     public VoxelShape getBlockSupportShape(BlockGetter level, BlockPos pos) {
/* 1092 */       return getBlock().getBlockSupportShape(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public VoxelShape getVisualShape(BlockGetter level, BlockPos pos, CollisionContext context) {
/* 1096 */       return getBlock().getVisualShape(asState(), level, pos, context);
/*      */     }
/*      */     
/*      */     public VoxelShape getInteractionShape(BlockGetter level, BlockPos pos) {
/* 1100 */       return getBlock().getInteractionShape(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public final boolean entityCanStandOn(BlockGetter level, BlockPos pos, Entity entity) {
/* 1104 */       return entityCanStandOnFace(level, pos, entity, Direction.UP);
/*      */     }
/*      */     
/*      */     public final boolean entityCanStandOnFace(BlockGetter level, BlockPos pos, Entity entity, Direction faceDirection) {
/* 1108 */       return Block.isFaceFull(getCollisionShape(level, pos, CollisionContext.of(entity)), faceDirection);
/*      */     }
/*      */     
/*      */     public Vec3 getOffset(BlockPos pos) {
/* 1112 */       BlockBehaviour.OffsetFunction function = this.offsetFunction;
/* 1113 */       if (function != null)
/* 1114 */         return function.evaluate(asState(), pos); 
/* 1116 */       return Vec3.ZERO;
/*      */     }
/*      */     
/*      */     public boolean hasOffsetFunction() {
/* 1120 */       return (this.offsetFunction != null);
/*      */     }
/*      */     
/*      */     public boolean triggerEvent(Level level, BlockPos pos, int b0, int b1) {
/* 1124 */       return getBlock().triggerEvent(asState(), level, pos, b0, b1);
/*      */     }
/*      */     
/*      */     public void handleNeighborChanged(Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 1130 */       getBlock().neighborChanged(asState(), level, pos, block, orientation, movedByPiston);
/*      */     }
/*      */     
/*      */     public final void updateNeighbourShapes(LevelAccessor level, BlockPos pos, int updateFlags) {
/* 1134 */       updateNeighbourShapes(level, pos, updateFlags, 512);
/*      */     }
/*      */     
/*      */     public final void updateNeighbourShapes(LevelAccessor level, BlockPos pos, int updateFlags, int updateLimit) {
/* 1138 */       BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 1139 */       for (Direction direction : BlockBehaviour.UPDATE_SHAPE_ORDER) {
/* 1140 */         blockPos.setWithOffset((Vec3i)pos, direction);
/* 1141 */         level.neighborShapeChanged(direction.getOpposite(), (BlockPos)blockPos, pos, asState(), updateFlags, updateLimit);
/*      */       } 
/*      */     }
/*      */     
/*      */     public final void updateIndirectNeighbourShapes(LevelAccessor level, BlockPos pos, int updateFlags) {
/* 1146 */       updateIndirectNeighbourShapes(level, pos, updateFlags, 512);
/*      */     }
/*      */     
/*      */     public void updateIndirectNeighbourShapes(LevelAccessor level, BlockPos pos, int updateFlags, int updateLimit) {
/* 1150 */       getBlock().updateIndirectNeighbourShapes(asState(), level, pos, updateFlags, updateLimit);
/*      */     }
/*      */     
/*      */     public void onPlace(Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 1154 */       getBlock().onPlace(asState(), level, pos, oldState, movedByPiston);
/*      */     }
/*      */     
/*      */     public void affectNeighborsAfterRemoval(ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 1158 */       getBlock().affectNeighborsAfterRemoval(asState(), level, pos, movedByPiston);
/*      */     }
/*      */     
/*      */     public void onExplosionHit(ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 1162 */       getBlock().onExplosionHit(asState(), level, pos, explosion, onHit);
/*      */     }
/*      */     
/*      */     public void tick(ServerLevel level, BlockPos pos, RandomSource random) {
/* 1166 */       getBlock().tick(asState(), level, pos, random);
/*      */     }
/*      */     
/*      */     public void randomTick(ServerLevel level, BlockPos pos, RandomSource random) {
/* 1170 */       getBlock().randomTick(asState(), level, pos, random);
/*      */     }
/*      */     
/*      */     public void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 1174 */       getBlock().entityInside(asState(), level, pos, entity, effectApplier, isPrecise);
/*      */     }
/*      */     
/*      */     public void spawnAfterBreak(ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 1178 */       getBlock().spawnAfterBreak(asState(), level, pos, tool, dropExperience);
/*      */     }
/*      */     
/*      */     public List<ItemStack> getDrops(LootParams.Builder params) {
/* 1182 */       return getBlock().getDrops(asState(), params);
/*      */     }
/*      */     
/*      */     public InteractionResult useItemOn(ItemStack itemStack, Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 1186 */       return getBlock().useItemOn(itemStack, asState(), level, hitResult.getBlockPos(), player, hand, hitResult);
/*      */     }
/*      */     
/*      */     public InteractionResult useWithoutItem(Level level, Player player, BlockHitResult hitResult) {
/* 1190 */       return getBlock().useWithoutItem(asState(), level, hitResult.getBlockPos(), player, hitResult);
/*      */     }
/*      */     
/*      */     public void attack(Level level, BlockPos pos, Player player) {
/* 1194 */       getBlock().attack(asState(), level, pos, player);
/*      */     }
/*      */     
/*      */     public boolean isSuffocating(BlockGetter level, BlockPos pos) {
/* 1198 */       return this.isSuffocating.test(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public boolean isViewBlocking(BlockGetter level, BlockPos pos) {
/* 1202 */       return this.isViewBlocking.test(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public BlockState updateShape(LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 1206 */       return getBlock().updateShape(asState(), level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*      */     }
/*      */     
/*      */     public boolean isPathfindable(PathComputationType type) {
/* 1210 */       return getBlock().isPathfindable(asState(), type);
/*      */     }
/*      */     
/*      */     public boolean canBeReplaced(BlockPlaceContext context) {
/* 1214 */       return getBlock().canBeReplaced(asState(), context);
/*      */     }
/*      */     
/*      */     public boolean canBeReplaced(Fluid fluid) {
/* 1218 */       return getBlock().canBeReplaced(asState(), fluid);
/*      */     }
/*      */     
/*      */     public boolean canBeReplaced() {
/* 1222 */       return this.replaceable;
/*      */     }
/*      */     
/*      */     public boolean canSurvive(LevelReader level, BlockPos pos) {
/* 1226 */       return getBlock().canSurvive(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public BlockPos getPostProcessPos(BlockGetter level, BlockPos pos) {
/* 1230 */       return this.postProcess.getPostProcessPos(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public MenuProvider getMenuProvider(Level level, BlockPos pos) {
/* 1234 */       return getBlock().getMenuProvider(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public boolean is(TagKey<Block> tag, Predicate<BlockStateBase> predicate) {
/* 1238 */       return (is(tag) && predicate.test(this));
/*      */     }
/*      */     
/*      */     public boolean hasBlockEntity() {
/* 1242 */       return getBlock() instanceof EntityBlock;
/*      */     }
/*      */     
/*      */     public boolean shouldChangedStateKeepBlockEntity(BlockState oldState) {
/* 1246 */       return getBlock().shouldChangedStateKeepBlockEntity(oldState);
/*      */     }
/*      */     
/*      */     public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockEntityType<T> type) {
/* 1250 */       if (getBlock() instanceof EntityBlock)
/* 1251 */         return ((EntityBlock)getBlock()).getTicker(level, asState(), type); 
/* 1253 */       return null;
/*      */     }
/*      */     
/*      */     public FluidState getFluidState() {
/* 1257 */       return this.fluidState;
/*      */     }
/*      */     
/*      */     public boolean isRandomlyTicking() {
/* 1261 */       return this.isRandomlyTicking;
/*      */     }
/*      */     
/*      */     public long getSeed(BlockPos pos) {
/* 1265 */       return getBlock().getSeed(asState(), pos);
/*      */     }
/*      */     
/*      */     public SoundType getSoundType() {
/* 1269 */       return getBlock().getSoundType(asState());
/*      */     }
/*      */     
/*      */     public void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile entity) {
/* 1273 */       getBlock().onProjectileHit(level, state, blockHit, entity);
/*      */     }
/*      */     
/*      */     public boolean isFaceSturdy(BlockGetter level, BlockPos pos, Direction direction) {
/* 1277 */       return isFaceSturdy(level, pos, direction, SupportType.FULL);
/*      */     }
/*      */     
/*      */     public boolean isFaceSturdy(BlockGetter level, BlockPos pos, Direction direction, SupportType supportType) {
/* 1281 */       if (this.cache != null)
/* 1282 */         return this.cache.isFaceSturdy(direction, supportType); 
/* 1284 */       return supportType.isSupporting(asState(), level, pos, direction);
/*      */     }
/*      */     
/*      */     public boolean isCollisionShapeFullBlock(BlockGetter level, BlockPos pos) {
/* 1288 */       if (this.cache != null)
/* 1289 */         return this.cache.isCollisionShapeFullBlock; 
/* 1291 */       return getBlock().isCollisionShapeFullBlock(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, boolean includeData) {
/* 1295 */       return getBlock().getCloneItemStack(level, pos, asState(), includeData);
/*      */     }
/*      */     
/*      */     public boolean requiresCorrectToolForDrops() {
/* 1301 */       return this.requiresCorrectToolForDrops;
/*      */     }
/*      */     
/*      */     public boolean shouldSpawnTerrainParticles() {
/* 1305 */       return this.spawnTerrainParticles;
/*      */     }
/*      */     
/*      */     public NoteBlockInstrument instrument() {
/* 1309 */       return this.instrument;
/*      */     }
/*      */     
/*      */     protected abstract BlockState asState();
/*      */     
/*      */     private static final class Cache {
/* 1313 */       private static final Direction[] DIRECTIONS = Direction.values();
/*      */       
/* 1314 */       private static final int SUPPORT_TYPE_COUNT = (SupportType.values()).length;
/*      */       
/*      */       public final VoxelShape collisionShape;
/*      */       
/*      */       public final boolean largeCollisionShape;
/*      */       
/*      */       private final boolean[] faceSturdy;
/*      */       
/*      */       public final boolean isCollisionShapeFullBlock;
/*      */       
/*      */       private Cache(BlockState state) {
/* 1321 */         Block block = state.getBlock();
/* 1323 */         this.collisionShape = block.getCollisionShape(state, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
/* 1324 */         if (!this.collisionShape.isEmpty() && state.hasOffsetFunction())
/* 1325 */           throw new IllegalStateException(String.format(Locale.ROOT, "%s has a collision shape and an offset type, but is not marked as dynamicShape in its properties.", new Object[] { BuiltInRegistries.BLOCK.getKey(block) })); 
/* 1327 */         this.largeCollisionShape = Arrays.<Direction.Axis>stream(Direction.Axis.values()).anyMatch(axis -> (this.collisionShape.min(axis) < 0.0D || this.collisionShape.max(axis) > 1.0D));
/* 1328 */         this.faceSturdy = new boolean[DIRECTIONS.length * SUPPORT_TYPE_COUNT];
/* 1329 */         for (Direction direction : DIRECTIONS) {
/* 1330 */           for (SupportType type : SupportType.values())
/* 1331 */             this.faceSturdy[getFaceSupportIndex(direction, type)] = type.isSupporting(state, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, direction); 
/*      */         } 
/* 1334 */         this.isCollisionShapeFullBlock = Block.isShapeFullBlock(state.getCollisionShape((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
/*      */       }
/*      */       
/*      */       public boolean isFaceSturdy(Direction direction, SupportType supportType) {
/* 1338 */         return this.faceSturdy[getFaceSupportIndex(direction, supportType)];
/*      */       }
/*      */       
/*      */       private static int getFaceSupportIndex(Direction direction, SupportType supportType) {
/* 1342 */         return direction.ordinal() * SUPPORT_TYPE_COUNT + supportType.ordinal();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class Cache {
/*      */     private static final Direction[] DIRECTIONS = Direction.values();
/*      */     
/*      */     private static final int SUPPORT_TYPE_COUNT = (SupportType.values()).length;
/*      */     
/*      */     public final VoxelShape collisionShape;
/*      */     
/*      */     public final boolean largeCollisionShape;
/*      */     
/*      */     private final boolean[] faceSturdy;
/*      */     
/*      */     public final boolean isCollisionShapeFullBlock;
/*      */     
/*      */     private Cache(BlockState state) {
/*      */       Block block = state.getBlock();
/*      */       this.collisionShape = block.getCollisionShape(state, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
/*      */       if (!this.collisionShape.isEmpty() && state.hasOffsetFunction())
/*      */         throw new IllegalStateException(String.format(Locale.ROOT, "%s has a collision shape and an offset type, but is not marked as dynamicShape in its properties.", new Object[] { BuiltInRegistries.BLOCK.getKey(block) })); 
/*      */       this.largeCollisionShape = Arrays.<Direction.Axis>stream(Direction.Axis.values()).anyMatch(axis -> (this.collisionShape.min(axis) < 0.0D || this.collisionShape.max(axis) > 1.0D));
/*      */       this.faceSturdy = new boolean[DIRECTIONS.length * SUPPORT_TYPE_COUNT];
/*      */       for (Direction direction : DIRECTIONS) {
/*      */         for (SupportType type : SupportType.values())
/*      */           this.faceSturdy[getFaceSupportIndex(direction, type)] = type.isSupporting(state, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, direction); 
/*      */       } 
/*      */       this.isCollisionShapeFullBlock = Block.isShapeFullBlock(state.getCollisionShape((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
/*      */     }
/*      */     
/*      */     public boolean isFaceSturdy(Direction direction, SupportType supportType) {
/*      */       return this.faceSturdy[getFaceSupportIndex(direction, supportType)];
/*      */     }
/*      */     
/*      */     private static int getFaceSupportIndex(Direction direction, SupportType supportType) {
/* 1342 */       return direction.ordinal() * SUPPORT_TYPE_COUNT + supportType.ordinal();
/*      */     }
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface PostProcess {
/*      */     BlockPos getPostProcessPos(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface StateArgumentPredicate<A> {
/*      */     boolean test(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos, A param1A);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface OffsetFunction {
/*      */     Vec3 evaluate(BlockState param1BlockState, BlockPos param1BlockPos);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface StatePredicate {
/*      */     boolean test(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\BlockBehaviour.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */