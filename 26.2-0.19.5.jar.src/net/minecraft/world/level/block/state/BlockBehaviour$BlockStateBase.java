/*      */ package net.minecraft.world.level.block.state;
/*      */ 
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.Predicate;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.TypedInstance;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.tags.TagKey;
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
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.context.BlockPlaceContext;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.EmptyBlockGetter;
/*      */ import net.minecraft.world.level.Explosion;
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
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ 
/*      */ public abstract class BlockStateBase extends StateHolder<Block, BlockState> implements TypedInstance<Block> {
/*  790 */   private static final Direction[] DIRECTIONS = Direction.values();
/*      */   
/*      */   private static final VoxelShape[] EMPTY_OCCLUSION_SHAPES;
/*      */   
/*      */   private static final VoxelShape[] FULL_BLOCK_OCCLUSION_SHAPES;
/*      */   
/*      */   private final int lightEmission;
/*      */   
/*      */   private final boolean useShapeForLightOcclusion;
/*      */   
/*      */   private final boolean isAir;
/*      */   
/*      */   private final boolean ignitedByLava;
/*      */   
/*      */   @Deprecated
/*      */   private final boolean liquid;
/*      */   
/*      */   @Deprecated
/*      */   private boolean legacySolid;
/*      */   
/*      */   private final PushReaction pushReaction;
/*      */   
/*      */   private final MapColor mapColor;
/*      */   
/*      */   private final float destroySpeed;
/*      */   
/*      */   private final boolean requiresCorrectToolForDrops;
/*      */   
/*      */   private final boolean canOcclude;
/*      */   
/*      */   private final BlockBehaviour.StatePredicate isRedstoneConductor;
/*      */   
/*      */   private final BlockBehaviour.StatePredicate isSuffocating;
/*      */   
/*      */   private final BlockBehaviour.StatePredicate isViewBlocking;
/*      */   
/*      */   private final BlockBehaviour.PostProcess postProcess;
/*      */   
/*      */   private final Predicate<BlockState> emissiveRendering;
/*      */   
/*      */   private final BlockBehaviour.OffsetFunction offsetFunction;
/*      */   
/*      */   private final boolean spawnTerrainParticles;
/*      */   
/*      */   private final NoteBlockInstrument instrument;
/*      */   
/*      */   private final boolean replaceable;
/*      */   
/*      */   private Cache cache;
/*      */   
/*      */   static {
/*  791 */     EMPTY_OCCLUSION_SHAPES = (VoxelShape[])Util.make(new VoxelShape[DIRECTIONS.length], s -> Arrays.fill((Object[])s, Shapes.empty()));
/*  792 */     FULL_BLOCK_OCCLUSION_SHAPES = (VoxelShape[])Util.make(new VoxelShape[DIRECTIONS.length], s -> Arrays.fill((Object[])s, Shapes.block()));
/*      */   }
/*      */   
/*  818 */   private FluidState fluidState = Fluids.EMPTY.defaultFluidState();
/*      */   
/*      */   private boolean isRandomlyTicking;
/*      */   
/*      */   private boolean solidRender;
/*      */   
/*      */   private VoxelShape occlusionShape;
/*      */   
/*      */   private VoxelShape[] occlusionShapesByFace;
/*      */   
/*      */   private boolean propagatesSkylightDown;
/*      */   
/*      */   private int lightDampening;
/*      */   
/*      */   protected BlockStateBase(Block owner, Property<?>[] propertyKeys, Comparable<?>[] propertyValues) {
/*  827 */     super(owner, propertyKeys, propertyValues);
/*  828 */     BlockBehaviour.Properties properties = owner.properties;
/*  830 */     this.lightEmission = properties.lightEmission.applyAsInt(asState());
/*  831 */     this.useShapeForLightOcclusion = owner.useShapeForLightOcclusion(asState());
/*  832 */     this.isAir = properties.isAir;
/*  833 */     this.ignitedByLava = properties.ignitedByLava;
/*  834 */     this.liquid = properties.liquid;
/*  835 */     this.pushReaction = properties.pushReaction;
/*  836 */     this.mapColor = properties.mapColor.apply(asState());
/*  837 */     this.destroySpeed = properties.destroyTime;
/*  838 */     this.requiresCorrectToolForDrops = properties.requiresCorrectToolForDrops;
/*  839 */     this.canOcclude = properties.canOcclude;
/*  840 */     this.isRedstoneConductor = properties.isRedstoneConductor;
/*  841 */     this.isSuffocating = properties.isSuffocating;
/*  842 */     this.isViewBlocking = properties.isViewBlocking;
/*  843 */     this.postProcess = properties.postProcess;
/*  844 */     this.emissiveRendering = properties.emissiveRendering;
/*  845 */     this.offsetFunction = properties.offsetFunction;
/*  846 */     this.spawnTerrainParticles = properties.spawnTerrainParticles;
/*  847 */     this.instrument = properties.instrument;
/*  848 */     this.replaceable = properties.replaceable;
/*      */   }
/*      */   
/*      */   private boolean calculateSolid() {
/*  855 */     if (this.owner.properties.forceSolidOn)
/*  856 */       return true; 
/*  858 */     if (this.owner.properties.forceSolidOff)
/*  859 */       return false; 
/*  861 */     if (this.cache == null)
/*  862 */       return false; 
/*  864 */     VoxelShape shape = this.cache.collisionShape;
/*  865 */     if (shape.isEmpty())
/*  866 */       return false; 
/*  868 */     AABB bounds = shape.bounds();
/*  869 */     if (bounds.getSize() >= 0.7291666666666666D)
/*  870 */       return true; 
/*  872 */     if (bounds.getYsize() >= 1.0D)
/*  873 */       return true; 
/*  875 */     return false;
/*      */   }
/*      */   
/*      */   public void initCache() {
/*  879 */     this.fluidState = this.owner.getFluidState(asState());
/*  880 */     this.isRandomlyTicking = this.owner.isRandomlyTicking(asState());
/*  881 */     if (!getBlock().hasDynamicShape())
/*  882 */       this.cache = new Cache(asState()); 
/*  884 */     this.legacySolid = calculateSolid();
/*  886 */     this.occlusionShape = this.canOcclude ? this.owner.getOcclusionShape(asState()) : Shapes.empty();
/*  887 */     this.solidRender = Block.isShapeFullBlock(this.occlusionShape);
/*  889 */     if (this.occlusionShape.isEmpty()) {
/*  890 */       this.occlusionShapesByFace = EMPTY_OCCLUSION_SHAPES;
/*  891 */     } else if (this.solidRender) {
/*  892 */       this.occlusionShapesByFace = FULL_BLOCK_OCCLUSION_SHAPES;
/*      */     } else {
/*  894 */       this.occlusionShapesByFace = new VoxelShape[DIRECTIONS.length];
/*  895 */       for (Direction direction : DIRECTIONS)
/*  896 */         this.occlusionShapesByFace[direction.ordinal()] = this.occlusionShape.getFaceShape(direction); 
/*      */     } 
/*  900 */     this.propagatesSkylightDown = this.owner.propagatesSkylightDown(asState());
/*  901 */     this.lightDampening = this.owner.getLightDampening(asState());
/*      */   }
/*      */   
/*      */   public Block getBlock() {
/*  905 */     return this.owner;
/*      */   }
/*      */   
/*      */   public Holder<Block> typeHolder() {
/*  910 */     return (Holder<Block>)getBlock().builtInRegistryHolder();
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public boolean blocksMotion() {
/*  924 */     Block block = getBlock();
/*  925 */     return (block != Blocks.COBWEB && block != Blocks.BAMBOO_SAPLING && isSolid());
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public boolean isSolid() {
/*  941 */     return this.legacySolid;
/*      */   }
/*      */   
/*      */   public boolean isValidSpawn(BlockGetter level, BlockPos pos, EntityType<?> type) {
/*  945 */     return (getBlock()).properties.isValidSpawn.test(asState(), level, pos, type);
/*      */   }
/*      */   
/*      */   public boolean propagatesSkylightDown() {
/*  949 */     return this.propagatesSkylightDown;
/*      */   }
/*      */   
/*      */   public int getLightDampening() {
/*  954 */     return this.lightDampening;
/*      */   }
/*      */   
/*      */   public VoxelShape getFaceOcclusionShape(Direction direction) {
/*  958 */     return this.occlusionShapesByFace[direction.ordinal()];
/*      */   }
/*      */   
/*      */   public VoxelShape getOcclusionShape() {
/*  962 */     return this.occlusionShape;
/*      */   }
/*      */   
/*      */   public boolean hasLargeCollisionShape() {
/*  967 */     return (this.cache == null || this.cache.largeCollisionShape);
/*      */   }
/*      */   
/*      */   public boolean useShapeForLightOcclusion() {
/*  971 */     return this.useShapeForLightOcclusion;
/*      */   }
/*      */   
/*      */   public int getLightEmission() {
/*  975 */     return this.lightEmission;
/*      */   }
/*      */   
/*      */   public boolean isAir() {
/*  979 */     return this.isAir;
/*      */   }
/*      */   
/*      */   public boolean ignitedByLava() {
/*  983 */     return this.ignitedByLava;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public boolean liquid() {
/*  989 */     return this.liquid;
/*      */   }
/*      */   
/*      */   public MapColor getMapColor(BlockGetter level, BlockPos pos) {
/*  993 */     return this.mapColor;
/*      */   }
/*      */   
/*      */   public BlockState rotate(Rotation rotation) {
/*  997 */     return getBlock().rotate(asState(), rotation);
/*      */   }
/*      */   
/*      */   public BlockState mirror(Mirror mirror) {
/* 1001 */     return getBlock().mirror(asState(), mirror);
/*      */   }
/*      */   
/*      */   public RenderShape getRenderShape() {
/* 1005 */     return getBlock().getRenderShape(asState());
/*      */   }
/*      */   
/*      */   public boolean emissiveRendering() {
/* 1009 */     return this.emissiveRendering.test(asState());
/*      */   }
/*      */   
/*      */   public float getShadeBrightness(BlockGetter level, BlockPos pos) {
/* 1013 */     return getBlock().getShadeBrightness(asState(), level, pos);
/*      */   }
/*      */   
/*      */   public boolean isRedstoneConductor(BlockGetter level, BlockPos pos) {
/* 1017 */     return this.isRedstoneConductor.test(asState(), level, pos);
/*      */   }
/*      */   
/*      */   public boolean isSignalSource() {
/* 1021 */     return getBlock().isSignalSource(asState());
/*      */   }
/*      */   
/*      */   public int getOwnSignal(BlockGetter level, BlockPos pos) {
/* 1025 */     return getBlock().ownSignal(asState(), level, pos);
/*      */   }
/*      */   
/*      */   public int getSignal(BlockGetter level, BlockPos pos, Direction direction) {
/* 1029 */     return getBlock().getSignal(asState(), level, pos, direction);
/*      */   }
/*      */   
/*      */   public boolean hasAnalogOutputSignal() {
/* 1033 */     return getBlock().hasAnalogOutputSignal(asState());
/*      */   }
/*      */   
/*      */   public int getAnalogOutputSignal(Level level, BlockPos pos, Direction direction) {
/* 1037 */     return getBlock().getAnalogOutputSignal(asState(), level, pos, direction);
/*      */   }
/*      */   
/*      */   public float getDestroySpeed(BlockGetter level, BlockPos pos) {
/* 1041 */     return this.destroySpeed;
/*      */   }
/*      */   
/*      */   public float getDestroyProgress(Player player, BlockGetter level, BlockPos pos) {
/* 1045 */     return getBlock().getDestroyProgress(asState(), player, level, pos);
/*      */   }
/*      */   
/*      */   public int getDirectSignal(BlockGetter level, BlockPos pos, Direction direction) {
/* 1049 */     return getBlock().getDirectSignal(asState(), level, pos, direction);
/*      */   }
/*      */   
/*      */   public PushReaction getPistonPushReaction() {
/* 1053 */     return this.pushReaction;
/*      */   }
/*      */   
/*      */   public boolean isSolidRender() {
/* 1057 */     return this.solidRender;
/*      */   }
/*      */   
/*      */   public boolean canOcclude() {
/* 1061 */     return this.canOcclude;
/*      */   }
/*      */   
/*      */   public boolean skipRendering(BlockState neighborState, Direction direction) {
/* 1065 */     return getBlock().skipRendering(asState(), neighborState, direction);
/*      */   }
/*      */   
/*      */   public VoxelShape getShape(BlockGetter level, BlockPos pos) {
/* 1069 */     return getShape(level, pos, CollisionContext.empty());
/*      */   }
/*      */   
/*      */   public VoxelShape getShape(BlockGetter level, BlockPos pos, CollisionContext context) {
/* 1073 */     return getBlock().getShape(asState(), level, pos, context);
/*      */   }
/*      */   
/*      */   public VoxelShape getCollisionShape(BlockGetter level, BlockPos pos) {
/* 1077 */     if (this.cache != null)
/* 1078 */       return this.cache.collisionShape; 
/* 1080 */     return getCollisionShape(level, pos, CollisionContext.empty());
/*      */   }
/*      */   
/*      */   public VoxelShape getCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context) {
/* 1084 */     return getBlock().getCollisionShape(asState(), level, pos, context);
/*      */   }
/*      */   
/*      */   public VoxelShape getEntityInsideCollisionShape(BlockGetter level, BlockPos pos, Entity entity) {
/* 1088 */     return getBlock().getEntityInsideCollisionShape(asState(), level, pos, entity);
/*      */   }
/*      */   
/*      */   public VoxelShape getBlockSupportShape(BlockGetter level, BlockPos pos) {
/* 1092 */     return getBlock().getBlockSupportShape(asState(), level, pos);
/*      */   }
/*      */   
/*      */   public VoxelShape getVisualShape(BlockGetter level, BlockPos pos, CollisionContext context) {
/* 1096 */     return getBlock().getVisualShape(asState(), level, pos, context);
/*      */   }
/*      */   
/*      */   public VoxelShape getInteractionShape(BlockGetter level, BlockPos pos) {
/* 1100 */     return getBlock().getInteractionShape(asState(), level, pos);
/*      */   }
/*      */   
/*      */   public final boolean entityCanStandOn(BlockGetter level, BlockPos pos, Entity entity) {
/* 1104 */     return entityCanStandOnFace(level, pos, entity, Direction.UP);
/*      */   }
/*      */   
/*      */   public final boolean entityCanStandOnFace(BlockGetter level, BlockPos pos, Entity entity, Direction faceDirection) {
/* 1108 */     return Block.isFaceFull(getCollisionShape(level, pos, CollisionContext.of(entity)), faceDirection);
/*      */   }
/*      */   
/*      */   public Vec3 getOffset(BlockPos pos) {
/* 1112 */     BlockBehaviour.OffsetFunction function = this.offsetFunction;
/* 1113 */     if (function != null)
/* 1114 */       return function.evaluate(asState(), pos); 
/* 1116 */     return Vec3.ZERO;
/*      */   }
/*      */   
/*      */   public boolean hasOffsetFunction() {
/* 1120 */     return (this.offsetFunction != null);
/*      */   }
/*      */   
/*      */   public boolean triggerEvent(Level level, BlockPos pos, int b0, int b1) {
/* 1124 */     return getBlock().triggerEvent(asState(), level, pos, b0, b1);
/*      */   }
/*      */   
/*      */   public void handleNeighborChanged(Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 1130 */     getBlock().neighborChanged(asState(), level, pos, block, orientation, movedByPiston);
/*      */   }
/*      */   
/*      */   public final void updateNeighbourShapes(LevelAccessor level, BlockPos pos, int updateFlags) {
/* 1134 */     updateNeighbourShapes(level, pos, updateFlags, 512);
/*      */   }
/*      */   
/*      */   public final void updateNeighbourShapes(LevelAccessor level, BlockPos pos, int updateFlags, int updateLimit) {
/* 1138 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 1139 */     for (Direction direction : BlockBehaviour.UPDATE_SHAPE_ORDER) {
/* 1140 */       blockPos.setWithOffset((Vec3i)pos, direction);
/* 1141 */       level.neighborShapeChanged(direction.getOpposite(), (BlockPos)blockPos, pos, asState(), updateFlags, updateLimit);
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void updateIndirectNeighbourShapes(LevelAccessor level, BlockPos pos, int updateFlags) {
/* 1146 */     updateIndirectNeighbourShapes(level, pos, updateFlags, 512);
/*      */   }
/*      */   
/*      */   public void updateIndirectNeighbourShapes(LevelAccessor level, BlockPos pos, int updateFlags, int updateLimit) {
/* 1150 */     getBlock().updateIndirectNeighbourShapes(asState(), level, pos, updateFlags, updateLimit);
/*      */   }
/*      */   
/*      */   public void onPlace(Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 1154 */     getBlock().onPlace(asState(), level, pos, oldState, movedByPiston);
/*      */   }
/*      */   
/*      */   public void affectNeighborsAfterRemoval(ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 1158 */     getBlock().affectNeighborsAfterRemoval(asState(), level, pos, movedByPiston);
/*      */   }
/*      */   
/*      */   public void onExplosionHit(ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 1162 */     getBlock().onExplosionHit(asState(), level, pos, explosion, onHit);
/*      */   }
/*      */   
/*      */   public void tick(ServerLevel level, BlockPos pos, RandomSource random) {
/* 1166 */     getBlock().tick(asState(), level, pos, random);
/*      */   }
/*      */   
/*      */   public void randomTick(ServerLevel level, BlockPos pos, RandomSource random) {
/* 1170 */     getBlock().randomTick(asState(), level, pos, random);
/*      */   }
/*      */   
/*      */   public void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 1174 */     getBlock().entityInside(asState(), level, pos, entity, effectApplier, isPrecise);
/*      */   }
/*      */   
/*      */   public void spawnAfterBreak(ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 1178 */     getBlock().spawnAfterBreak(asState(), level, pos, tool, dropExperience);
/*      */   }
/*      */   
/*      */   public List<ItemStack> getDrops(LootParams.Builder params) {
/* 1182 */     return getBlock().getDrops(asState(), params);
/*      */   }
/*      */   
/*      */   public InteractionResult useItemOn(ItemStack itemStack, Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 1186 */     return getBlock().useItemOn(itemStack, asState(), level, hitResult.getBlockPos(), player, hand, hitResult);
/*      */   }
/*      */   
/*      */   public InteractionResult useWithoutItem(Level level, Player player, BlockHitResult hitResult) {
/* 1190 */     return getBlock().useWithoutItem(asState(), level, hitResult.getBlockPos(), player, hitResult);
/*      */   }
/*      */   
/*      */   public void attack(Level level, BlockPos pos, Player player) {
/* 1194 */     getBlock().attack(asState(), level, pos, player);
/*      */   }
/*      */   
/*      */   public boolean isSuffocating(BlockGetter level, BlockPos pos) {
/* 1198 */     return this.isSuffocating.test(asState(), level, pos);
/*      */   }
/*      */   
/*      */   public boolean isViewBlocking(BlockGetter level, BlockPos pos) {
/* 1202 */     return this.isViewBlocking.test(asState(), level, pos);
/*      */   }
/*      */   
/*      */   public BlockState updateShape(LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 1206 */     return getBlock().updateShape(asState(), level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*      */   }
/*      */   
/*      */   public boolean isPathfindable(PathComputationType type) {
/* 1210 */     return getBlock().isPathfindable(asState(), type);
/*      */   }
/*      */   
/*      */   public boolean canBeReplaced(BlockPlaceContext context) {
/* 1214 */     return getBlock().canBeReplaced(asState(), context);
/*      */   }
/*      */   
/*      */   public boolean canBeReplaced(Fluid fluid) {
/* 1218 */     return getBlock().canBeReplaced(asState(), fluid);
/*      */   }
/*      */   
/*      */   public boolean canBeReplaced() {
/* 1222 */     return this.replaceable;
/*      */   }
/*      */   
/*      */   public boolean canSurvive(LevelReader level, BlockPos pos) {
/* 1226 */     return getBlock().canSurvive(asState(), level, pos);
/*      */   }
/*      */   
/*      */   public BlockPos getPostProcessPos(BlockGetter level, BlockPos pos) {
/* 1230 */     return this.postProcess.getPostProcessPos(asState(), level, pos);
/*      */   }
/*      */   
/*      */   public MenuProvider getMenuProvider(Level level, BlockPos pos) {
/* 1234 */     return getBlock().getMenuProvider(asState(), level, pos);
/*      */   }
/*      */   
/*      */   public boolean is(TagKey<Block> tag, Predicate<BlockStateBase> predicate) {
/* 1238 */     return (is(tag) && predicate.test(this));
/*      */   }
/*      */   
/*      */   public boolean hasBlockEntity() {
/* 1242 */     return getBlock() instanceof EntityBlock;
/*      */   }
/*      */   
/*      */   public boolean shouldChangedStateKeepBlockEntity(BlockState oldState) {
/* 1246 */     return getBlock().shouldChangedStateKeepBlockEntity(oldState);
/*      */   }
/*      */   
/*      */   public <T extends net.minecraft.world.level.block.entity.BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockEntityType<T> type) {
/* 1250 */     if (getBlock() instanceof EntityBlock)
/* 1251 */       return ((EntityBlock)getBlock()).getTicker(level, asState(), type); 
/* 1253 */     return null;
/*      */   }
/*      */   
/*      */   public FluidState getFluidState() {
/* 1257 */     return this.fluidState;
/*      */   }
/*      */   
/*      */   public boolean isRandomlyTicking() {
/* 1261 */     return this.isRandomlyTicking;
/*      */   }
/*      */   
/*      */   public long getSeed(BlockPos pos) {
/* 1265 */     return getBlock().getSeed(asState(), pos);
/*      */   }
/*      */   
/*      */   public SoundType getSoundType() {
/* 1269 */     return getBlock().getSoundType(asState());
/*      */   }
/*      */   
/*      */   public void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile entity) {
/* 1273 */     getBlock().onProjectileHit(level, state, blockHit, entity);
/*      */   }
/*      */   
/*      */   public boolean isFaceSturdy(BlockGetter level, BlockPos pos, Direction direction) {
/* 1277 */     return isFaceSturdy(level, pos, direction, SupportType.FULL);
/*      */   }
/*      */   
/*      */   public boolean isFaceSturdy(BlockGetter level, BlockPos pos, Direction direction, SupportType supportType) {
/* 1281 */     if (this.cache != null)
/* 1282 */       return this.cache.isFaceSturdy(direction, supportType); 
/* 1284 */     return supportType.isSupporting(asState(), level, pos, direction);
/*      */   }
/*      */   
/*      */   public boolean isCollisionShapeFullBlock(BlockGetter level, BlockPos pos) {
/* 1288 */     if (this.cache != null)
/* 1289 */       return this.cache.isCollisionShapeFullBlock; 
/* 1291 */     return getBlock().isCollisionShapeFullBlock(asState(), level, pos);
/*      */   }
/*      */   
/*      */   public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, boolean includeData) {
/* 1295 */     return getBlock().getCloneItemStack(level, pos, asState(), includeData);
/*      */   }
/*      */   
/*      */   public boolean requiresCorrectToolForDrops() {
/* 1301 */     return this.requiresCorrectToolForDrops;
/*      */   }
/*      */   
/*      */   public boolean shouldSpawnTerrainParticles() {
/* 1305 */     return this.spawnTerrainParticles;
/*      */   }
/*      */   
/*      */   public NoteBlockInstrument instrument() {
/* 1309 */     return this.instrument;
/*      */   }
/*      */   
/*      */   protected abstract BlockState asState();
/*      */   
/*      */   private static final class Cache {
/* 1313 */     private static final Direction[] DIRECTIONS = Direction.values();
/*      */     
/* 1314 */     private static final int SUPPORT_TYPE_COUNT = (SupportType.values()).length;
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
/* 1321 */       Block block = state.getBlock();
/* 1323 */       this.collisionShape = block.getCollisionShape(state, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
/* 1324 */       if (!this.collisionShape.isEmpty() && state.hasOffsetFunction())
/* 1325 */         throw new IllegalStateException(String.format(Locale.ROOT, "%s has a collision shape and an offset type, but is not marked as dynamicShape in its properties.", new Object[] { BuiltInRegistries.BLOCK.getKey(block) })); 
/* 1327 */       this.largeCollisionShape = Arrays.<Direction.Axis>stream(Direction.Axis.values()).anyMatch(axis -> (this.collisionShape.min(axis) < 0.0D || this.collisionShape.max(axis) > 1.0D));
/* 1328 */       this.faceSturdy = new boolean[DIRECTIONS.length * SUPPORT_TYPE_COUNT];
/* 1329 */       for (Direction direction : DIRECTIONS) {
/* 1330 */         for (SupportType type : SupportType.values())
/* 1331 */           this.faceSturdy[getFaceSupportIndex(direction, type)] = type.isSupporting(state, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, direction); 
/*      */       } 
/* 1334 */       this.isCollisionShapeFullBlock = Block.isShapeFullBlock(state.getCollisionShape((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
/*      */     }
/*      */     
/*      */     public boolean isFaceSturdy(Direction direction, SupportType supportType) {
/* 1338 */       return this.faceSturdy[getFaceSupportIndex(direction, supportType)];
/*      */     }
/*      */     
/*      */     private static int getFaceSupportIndex(Direction direction, SupportType supportType) {
/* 1342 */       return direction.ordinal() * SUPPORT_TYPE_COUNT + supportType.ordinal();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\BlockBehaviour$BlockStateBase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */