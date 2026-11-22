/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.IdMapper;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Block extends BlockBehaviour implements ItemLike {
/*  88 */   public static final MapCodec<Block> CODEC = simpleCodec(Block::new);
/*     */   
/*  90 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  92 */   private final Holder.Reference<Block> builtInRegistryHolder = BuiltInRegistries.BLOCK.createIntrusiveHolder(this);
/*     */   
/*  94 */   public static final IdMapper<BlockState> BLOCK_STATE_REGISTRY = new IdMapper();
/*     */   
/*  96 */   private static final LoadingCache<VoxelShape, Boolean> SHAPE_FULL_BLOCK_CACHE = CacheBuilder.newBuilder()
/*  97 */     .maximumSize(512L)
/*  98 */     .weakKeys()
/*  99 */     .build(new CacheLoader<VoxelShape, Boolean>() {
/*     */         public Boolean load(VoxelShape shape) {
/* 102 */           return Boolean.valueOf(!Shapes.joinIsNotEmpty(Shapes.block(), shape, BooleanOp.NOT_SAME));
/*     */         }
/*     */       });
/*     */   
/*     */   public static final int UPDATE_NEIGHBORS = 1;
/*     */   
/*     */   public static final int UPDATE_CLIENTS = 2;
/*     */   
/*     */   public static final int UPDATE_INVISIBLE = 4;
/*     */   
/*     */   public static final int UPDATE_IMMEDIATE = 8;
/*     */   
/*     */   public static final int UPDATE_KNOWN_SHAPE = 16;
/*     */   
/*     */   public static final int UPDATE_SUPPRESS_DROPS = 32;
/*     */   
/*     */   public static final int UPDATE_MOVE_BY_PISTON = 64;
/*     */   
/*     */   public static final int UPDATE_SKIP_SHAPE_UPDATE_ON_WIRE = 128;
/*     */   
/*     */   public static final int UPDATE_SKIP_BLOCK_ENTITY_SIDEEFFECTS = 256;
/*     */   
/*     */   public static final int UPDATE_SKIP_ON_PLACE = 512;
/*     */   
/*     */   public static final int UPDATE_NONE = 260;
/*     */   
/*     */   public static final int UPDATE_ALL = 3;
/*     */   
/*     */   public static final int UPDATE_ALL_IMMEDIATE = 11;
/*     */   
/*     */   public static final int UPDATE_SKIP_ALL_SIDEEFFECTS = 816;
/*     */   
/*     */   public static final float INDESTRUCTIBLE = -1.0F;
/*     */   
/*     */   public static final float INSTANT = 0.0F;
/*     */   
/*     */   public static final int UPDATE_LIMIT = 512;
/*     */   
/*     */   protected final StateDefinition<Block, BlockState> stateDefinition;
/*     */   
/*     */   private BlockState defaultBlockState;
/*     */   
/*     */   private Item item;
/*     */   
/*     */   private static final int CACHE_SIZE = 256;
/*     */   
/*     */   private static final ThreadLocal<Object2ByteLinkedOpenHashMap<ShapePairKey>> OCCLUSION_CACHE;
/*     */   
/*     */   protected MapCodec<? extends Block> codec() {
/* 133 */     return CODEC;
/*     */   }
/*     */   
/*     */   public static int getId(BlockState blockState) {
/* 137 */     if (blockState == null)
/* 138 */       return 0; 
/* 140 */     int id = BLOCK_STATE_REGISTRY.getId(blockState);
/* 141 */     return (id == -1) ? 0 : id;
/*     */   }
/*     */   
/*     */   public static BlockState stateById(int idWithData) {
/* 145 */     BlockState state = (BlockState)BLOCK_STATE_REGISTRY.byId(idWithData);
/* 146 */     return (state == null) ? Blocks.AIR.defaultBlockState() : state;
/*     */   }
/*     */   
/*     */   public static Block byItem(Item item) {
/* 150 */     if (item instanceof BlockItem) {
/* 150 */       BlockItem blockItem = (BlockItem)item;
/* 151 */       return blockItem.getBlock();
/*     */     } 
/* 154 */     return Blocks.AIR;
/*     */   }
/*     */   
/*     */   public static BlockState pushEntitiesUp(BlockState state, BlockState newState, LevelAccessor level, BlockPos pos) {
/* 158 */     VoxelShape offsetShape = Shapes.joinUnoptimized(state.getCollisionShape((BlockGetter)level, pos), newState.getCollisionShape((BlockGetter)level, pos), BooleanOp.ONLY_SECOND).move((Vec3i)pos);
/* 159 */     if (offsetShape.isEmpty())
/* 160 */       return newState; 
/* 162 */     List<Entity> collidingEntities = level.getEntities(null, offsetShape.bounds());
/* 163 */     for (Entity collidingEntity : collidingEntities) {
/* 165 */       double offset = Shapes.collide(Direction.Axis.Y, collidingEntity.getBoundingBox().move(0.0D, 1.0D, 0.0D), List.of(offsetShape), -1.0D);
/* 166 */       collidingEntity.teleportRelative(0.0D, 1.0D + offset, 0.0D);
/*     */     } 
/* 168 */     return newState;
/*     */   }
/*     */   
/*     */   public static VoxelShape box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
/* 172 */     return Shapes.box(minX / 16.0D, minY / 16.0D, minZ / 16.0D, maxX / 16.0D, maxY / 16.0D, maxZ / 16.0D);
/*     */   }
/*     */   
/*     */   public static VoxelShape[] boxes(int endInclusive, IntFunction<VoxelShape> voxelShapeFactory) {
/* 176 */     return (VoxelShape[])IntStream.rangeClosed(0, endInclusive).<VoxelShape>mapToObj(voxelShapeFactory).toArray(x$0 -> new VoxelShape[x$0]);
/*     */   }
/*     */   
/*     */   public static VoxelShape cube(double size) {
/* 180 */     return cube(size, size, size);
/*     */   }
/*     */   
/*     */   public static VoxelShape cube(double sizeX, double sizeY, double sizeZ) {
/* 184 */     double halfY = sizeY / 2.0D;
/* 185 */     return column(sizeX, sizeZ, 8.0D - halfY, 8.0D + halfY);
/*     */   }
/*     */   
/*     */   public static VoxelShape column(double sizeXZ, double minY, double maxY) {
/* 189 */     return column(sizeXZ, sizeXZ, minY, maxY);
/*     */   }
/*     */   
/*     */   public static VoxelShape column(double sizeX, double sizeZ, double minY, double maxY) {
/* 193 */     double halfX = sizeX / 2.0D;
/* 194 */     double halfZ = sizeZ / 2.0D;
/* 195 */     return box(8.0D - halfX, minY, 8.0D - halfZ, 8.0D + halfX, maxY, 8.0D + halfZ);
/*     */   }
/*     */   
/*     */   public static VoxelShape boxZ(double sizeXY, double minZ, double maxZ) {
/* 199 */     return boxZ(sizeXY, sizeXY, minZ, maxZ);
/*     */   }
/*     */   
/*     */   public static VoxelShape boxZ(double sizeX, double sizeY, double minZ, double maxZ) {
/* 203 */     double halfY = sizeY / 2.0D;
/* 204 */     return boxZ(sizeX, 8.0D - halfY, 8.0D + halfY, minZ, maxZ);
/*     */   }
/*     */   
/*     */   public static VoxelShape boxZ(double sizeX, double minY, double maxY, double minZ, double maxZ) {
/* 208 */     double halfX = sizeX / 2.0D;
/* 209 */     return box(8.0D - halfX, minY, minZ, 8.0D + halfX, maxY, maxZ);
/*     */   }
/*     */   
/*     */   public static BlockState updateFromNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos) {
/* 213 */     BlockState newState = state;
/* 215 */     BlockPos.MutableBlockPos neighbourPos = new BlockPos.MutableBlockPos();
/* 216 */     for (Direction direction : UPDATE_SHAPE_ORDER) {
/* 217 */       neighbourPos.setWithOffset((Vec3i)pos, direction);
/* 218 */       newState = newState.updateShape((LevelReader)level, (ScheduledTickAccess)level, pos, direction, (BlockPos)neighbourPos, level.getBlockState((BlockPos)neighbourPos), level.getRandom());
/*     */     } 
/* 221 */     return newState;
/*     */   }
/*     */   
/*     */   public static void updateOrDestroy(BlockState blockState, BlockState newState, LevelAccessor level, BlockPos blockPos, int updateFlags) {
/* 225 */     updateOrDestroy(blockState, newState, level, blockPos, updateFlags, 512);
/*     */   }
/*     */   
/*     */   public static void updateOrDestroy(BlockState blockState, BlockState newState, LevelAccessor level, BlockPos blockPos, int updateFlags, int updateLimit) {
/* 229 */     if (newState != blockState)
/* 230 */       if (newState.isAir()) {
/* 231 */         if (!level.isClientSide())
/* 232 */           level.destroyBlock(blockPos, ((updateFlags & 0x20) == 0), null, updateLimit); 
/*     */       } else {
/* 235 */         level.setBlock(blockPos, newState, updateFlags & 0xFFFFFFDF, updateLimit);
/*     */       }  
/*     */   }
/*     */   
/*     */   public Block(BlockBehaviour.Properties properties) {
/* 246 */     super(properties);
/* 247 */     StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder(this);
/* 248 */     createBlockStateDefinition(builder);
/* 250 */     this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
/* 251 */     registerDefaultState((BlockState)this.stateDefinition.any());
/* 253 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 254 */       String className = getClass().getSimpleName();
/* 255 */       if (!className.endsWith("Block"))
/* 256 */         LOGGER.error("Block classes should end with Block and {} doesn't.", className); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isExceptionForConnection(BlockState state) {
/* 262 */     return (state.getBlock() instanceof LeavesBlock || state
/* 263 */       .is(Blocks.BARRIER) || state
/* 264 */       .is(Blocks.CARVED_PUMPKIN) || state
/* 265 */       .is(Blocks.JACK_O_LANTERN) || state
/* 266 */       .is(Blocks.MELON) || state
/* 267 */       .is(Blocks.PUMPKIN) || state
/* 268 */       .is(BlockTags.SHULKER_BOXES));
/*     */   }
/*     */   
/*     */   protected static boolean dropFromBlockInteractLootTable(ServerLevel level, ResourceKey<LootTable> key, BlockState interactedBlockState, BlockEntity interactedBlockEntity, ItemInstance tool, Entity interactingEntity, BiConsumer<ServerLevel, ItemStack> consumer) {
/* 273 */     return dropFromLootTable(level, key, params -> params.withParameter(LootContextParams.BLOCK_STATE, interactedBlockState).withOptionalParameter(LootContextParams.BLOCK_ENTITY, interactedBlockEntity).withOptionalParameter(LootContextParams.INTERACTING_ENTITY, interactingEntity).withOptionalParameter(LootContextParams.TOOL, tool).create(LootContextParamSets.BLOCK_INTERACT), consumer);
/*     */   }
/*     */   
/*     */   protected static boolean dropFromLootTable(ServerLevel level, ResourceKey<LootTable> key, Function<LootParams.Builder, LootParams> paramsBuilder, BiConsumer<ServerLevel, ItemStack> consumer) {
/* 287 */     LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(key);
/* 288 */     LootParams params = paramsBuilder.apply(new LootParams.Builder(level));
/* 289 */     ObjectArrayList objectArrayList = lootTable.getRandomItems(params);
/* 290 */     if (!objectArrayList.isEmpty()) {
/* 291 */       objectArrayList.forEach(stack -> consumer.accept(level, stack));
/* 292 */       return true;
/*     */     } 
/* 294 */     return false;
/*     */   }
/*     */   
/*     */   private static final class ShapePairKey extends Record {
/*     */     private final VoxelShape first;
/*     */     
/*     */     private final VoxelShape second;
/*     */     
/*     */     private ShapePairKey(VoxelShape first, VoxelShape second) {
/* 297 */       this.first = first;
/* 297 */       this.second = second;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/Block$ShapePairKey;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #297	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/Block$ShapePairKey;
/*     */     }
/*     */     
/*     */     public VoxelShape first() {
/* 297 */       return this.first;
/*     */     }
/*     */     
/*     */     public VoxelShape second() {
/* 297 */       return this.second;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 301 */       if (o instanceof ShapePairKey) {
/* 301 */         ShapePairKey that = (ShapePairKey)o;
/* 301 */         if (this.first == that.first && this.second == that.second);
/*     */       } 
/* 301 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 306 */       return System.identityHashCode(this.first) * 31 + System.identityHashCode(this.second);
/*     */     }
/*     */   }
/*     */   
/*     */   static {
/* 311 */     OCCLUSION_CACHE = ThreadLocal.withInitial(() -> {
/*     */           Object2ByteLinkedOpenHashMap<ShapePairKey> map = new Object2ByteLinkedOpenHashMap<ShapePairKey>(256, 0.25F) {
/*     */               protected void rehash(int newN) {}
/*     */             };
/*     */           map.defaultReturnValue(127);
/*     */           return map;
/*     */         });
/*     */   }
/*     */   
/*     */   public static boolean shouldRenderFace(BlockState state, BlockState neighborState, Direction direction) {
/* 323 */     VoxelShape occluder = neighborState.getFaceOcclusionShape(direction.getOpposite());
/* 324 */     if (occluder == Shapes.block())
/* 325 */       return false; 
/* 328 */     if (state.skipRendering(neighborState, direction))
/* 329 */       return false; 
/* 332 */     if (occluder == Shapes.empty())
/* 333 */       return true; 
/* 336 */     VoxelShape shape = state.getFaceOcclusionShape(direction);
/* 338 */     if (shape == Shapes.empty())
/* 339 */       return true; 
/* 342 */     ShapePairKey key = new ShapePairKey(shape, occluder);
/* 343 */     Object2ByteLinkedOpenHashMap<ShapePairKey> cache = OCCLUSION_CACHE.get();
/* 344 */     byte cached = cache.getAndMoveToFirst(key);
/* 345 */     if (cached != Byte.MAX_VALUE)
/* 346 */       return (cached != 0); 
/* 348 */     boolean result = Shapes.joinIsNotEmpty(shape, occluder, BooleanOp.ONLY_FIRST);
/* 349 */     if (cache.size() == 256)
/* 350 */       cache.removeLastByte(); 
/* 352 */     cache.putAndMoveToFirst(key, (byte)(result ? 1 : 0));
/* 353 */     return result;
/*     */   }
/*     */   
/*     */   public static boolean canSupportRigidBlock(BlockGetter level, BlockPos below) {
/* 357 */     return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP, SupportType.RIGID);
/*     */   }
/*     */   
/*     */   public static boolean canSupportCenter(LevelReader level, BlockPos belowPos, Direction direction) {
/* 361 */     BlockState state = level.getBlockState(belowPos);
/* 363 */     if (direction == Direction.DOWN && state.is(BlockTags.UNSTABLE_BOTTOM_CENTER))
/* 364 */       return false; 
/* 367 */     return state.isFaceSturdy((BlockGetter)level, belowPos, direction, SupportType.CENTER);
/*     */   }
/*     */   
/*     */   public static boolean isFaceFull(VoxelShape shape, Direction direction) {
/* 371 */     VoxelShape faceShape = shape.getFaceShape(direction);
/* 372 */     return isShapeFullBlock(faceShape);
/*     */   }
/*     */   
/*     */   public static boolean isShapeFullBlock(VoxelShape shape) {
/* 376 */     return ((Boolean)SHAPE_FULL_BLOCK_CACHE.getUnchecked(shape)).booleanValue();
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {}
/*     */   
/*     */   public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {}
/*     */   
/*     */   public static List<ItemStack> getDrops(BlockState state, ServerLevel level, BlockPos pos, BlockEntity blockEntity) {
/* 390 */     LootParams.Builder params = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)pos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
/* 391 */     return state.getDrops(params);
/*     */   }
/*     */   
/*     */   public static List<ItemStack> getDrops(BlockState state, ServerLevel level, BlockPos pos, BlockEntity blockEntity, Entity breaker, ItemInstance tool) {
/* 399 */     LootParams.Builder params = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)pos)).withParameter(LootContextParams.TOOL, tool).withOptionalParameter(LootContextParams.THIS_ENTITY, breaker).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
/* 400 */     return state.getDrops(params);
/*     */   }
/*     */   
/*     */   public static void dropResources(BlockState state, Level level, BlockPos pos) {
/* 404 */     if (level instanceof ServerLevel) {
/* 404 */       ServerLevel serverLevel = (ServerLevel)level;
/* 405 */       getDrops(state, serverLevel, pos, (BlockEntity)null).forEach(stack -> popResource(level, pos, stack));
/* 406 */       state.spawnAfterBreak(serverLevel, pos, ItemStack.EMPTY, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void dropResources(BlockState state, LevelAccessor level, BlockPos pos, BlockEntity blockEntity) {
/* 411 */     if (level instanceof ServerLevel) {
/* 411 */       ServerLevel serverLevel = (ServerLevel)level;
/* 412 */       getDrops(state, serverLevel, pos, blockEntity).forEach(stack -> popResource((Level)serverLevel, pos, stack));
/* 413 */       state.spawnAfterBreak(serverLevel, pos, ItemStack.EMPTY, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void dropResources(BlockState state, Level level, BlockPos pos, BlockEntity blockEntity, Entity breaker, ItemStack tool) {
/* 418 */     if (level instanceof ServerLevel) {
/* 418 */       ServerLevel serverLevel = (ServerLevel)level;
/* 419 */       getDrops(state, serverLevel, pos, blockEntity, breaker, (ItemInstance)tool).forEach(stack -> popResource(level, pos, stack));
/* 420 */       state.spawnAfterBreak(serverLevel, pos, tool, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void popResource(Level level, BlockPos pos, ItemStack itemStack) {
/* 425 */     double halfHeight = EntityTypes.ITEM.getHeight() / 2.0D;
/* 427 */     RandomSource random = level.getRandom();
/* 428 */     double x = pos.getX() + 0.5D + Mth.nextDouble(random, -0.25D, 0.25D);
/* 429 */     double y = pos.getY() + 0.5D + Mth.nextDouble(random, -0.25D, 0.25D) - halfHeight;
/* 430 */     double z = pos.getZ() + 0.5D + Mth.nextDouble(random, -0.25D, 0.25D);
/* 432 */     popResource(level, () -> new ItemEntity(level, x, y, z, itemStack), itemStack);
/*     */   }
/*     */   
/*     */   public static void popResourceFromFace(Level level, BlockPos pos, Direction face, ItemStack itemStack) {
/* 436 */     int stepX = face.getStepX();
/* 437 */     int stepY = face.getStepY();
/* 438 */     int stepZ = face.getStepZ();
/* 440 */     double halfWidth = EntityTypes.ITEM.getWidth() / 2.0D;
/* 441 */     double halfHeight = EntityTypes.ITEM.getHeight() / 2.0D;
/* 443 */     RandomSource random = level.getRandom();
/* 444 */     double x = pos.getX() + 0.5D + ((stepX == 0) ? Mth.nextDouble(random, -0.25D, 0.25D) : (stepX * (0.5D + halfWidth)));
/* 445 */     double y = pos.getY() + 0.5D + ((stepY == 0) ? Mth.nextDouble(random, -0.25D, 0.25D) : (stepY * (0.5D + halfHeight))) - halfHeight;
/* 446 */     double z = pos.getZ() + 0.5D + ((stepZ == 0) ? Mth.nextDouble(random, -0.25D, 0.25D) : (stepZ * (0.5D + halfWidth)));
/* 448 */     double deltaX = (stepX == 0) ? Mth.nextDouble(random, -0.1D, 0.1D) : (stepX * 0.1D);
/* 449 */     double deltaY = (stepY == 0) ? Mth.nextDouble(random, 0.0D, 0.1D) : (stepY * 0.1D + 0.1D);
/* 450 */     double deltaZ = (stepZ == 0) ? Mth.nextDouble(random, -0.1D, 0.1D) : (stepZ * 0.1D);
/* 452 */     popResource(level, () -> new ItemEntity(level, x, y, z, itemStack, deltaX, deltaY, deltaZ), itemStack);
/*     */   }
/*     */   
/*     */   private static void popResource(Level level, Supplier<ItemEntity> entityFactory, ItemStack itemStack) {
/* 456 */     if (level instanceof ServerLevel) {
/* 456 */       ServerLevel serverLevel = (ServerLevel)level;
/* 456 */       if (!itemStack.isEmpty() && ((Boolean)serverLevel.getGameRules().get(GameRules.BLOCK_DROPS)).booleanValue()) {
/* 460 */         ItemEntity entity = entityFactory.get();
/* 461 */         entity.setDefaultPickUpDelay();
/* 462 */         level.addFreshEntity((Entity)entity);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void popExperience(ServerLevel level, BlockPos pos, int amount) {
/* 466 */     if (((Boolean)level.getGameRules().get(GameRules.BLOCK_DROPS)).booleanValue())
/* 467 */       ExperienceOrb.award(level, Vec3.atCenterOf((Vec3i)pos), amount); 
/*     */   }
/*     */   
/*     */   public float getExplosionResistance() {
/* 472 */     return this.explosionResistance;
/*     */   }
/*     */   
/*     */   public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {}
/*     */   
/*     */   public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {}
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 489 */     return defaultBlockState();
/*     */   }
/*     */   
/*     */   public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack destroyedWith) {
/* 494 */     player.awardStat(Stats.BLOCK_MINED.get(this));
/* 495 */     player.causeFoodExhaustion(0.005F);
/* 496 */     dropResources(state, level, pos, blockEntity, (Entity)player, destroyedWith);
/*     */   }
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {}
/*     */   
/*     */   public boolean isPossibleToRespawnInThis(BlockState state) {
/* 504 */     return (!state.isSolid() && !state.liquid());
/*     */   }
/*     */   
/*     */   public MutableComponent getName() {
/* 508 */     return Component.translatable(getDescriptionId());
/*     */   }
/*     */   
/*     */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
/* 513 */     entity.causeFallDamage(fallDistance, 1.0F, entity.damageSources().fall());
/*     */   }
/*     */   
/*     */   public float getBounceRestitution() {
/* 517 */     return this.bounceRestitution;
/*     */   }
/*     */   
/*     */   public float getFriction() {
/* 521 */     return this.friction;
/*     */   }
/*     */   
/*     */   public float getSpeedFactor() {
/* 525 */     return this.speedFactor;
/*     */   }
/*     */   
/*     */   public float getJumpFactor() {
/* 529 */     return this.jumpFactor;
/*     */   }
/*     */   
/*     */   protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
/* 533 */     level.levelEvent((Entity)player, 2001, pos, getId(state));
/*     */   }
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 538 */     spawnDestroyParticles(level, player, pos, state);
/* 540 */     if (state.is(BlockTags.GUARDED_BY_PIGLINS) && level instanceof ServerLevel) {
/* 540 */       ServerLevel serverLevel = (ServerLevel)level;
/* 541 */       PiglinAi.angerNearbyPiglins(serverLevel, player, false);
/*     */     } 
/* 543 */     level.gameEvent((Holder)GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of((Entity)player, state));
/* 544 */     return state;
/*     */   }
/*     */   
/*     */   public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation) {}
/*     */   
/*     */   public boolean dropFromExplosion(Explosion explosion) {
/* 551 */     return true;
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {}
/*     */   
/*     */   public StateDefinition<Block, BlockState> getStateDefinition() {
/* 558 */     return this.stateDefinition;
/*     */   }
/*     */   
/*     */   protected final void registerDefaultState(BlockState state) {
/* 562 */     this.defaultBlockState = state;
/*     */   }
/*     */   
/*     */   public final BlockState defaultBlockState() {
/* 566 */     return this.defaultBlockState;
/*     */   }
/*     */   
/*     */   public final BlockState withPropertiesOf(BlockState source) {
/* 570 */     BlockState result = defaultBlockState();
/* 571 */     for (Property<?> property : (Iterable<Property<?>>)source.getBlock().getStateDefinition().getProperties()) {
/* 572 */       if (result.hasProperty(property))
/* 573 */         result = copyProperty(source, result, property); 
/*     */     } 
/* 576 */     return result;
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) {
/* 581 */     return (BlockState)to.setValue(property, from.getValue(property));
/*     */   }
/*     */   
/*     */   public Item asItem() {
/* 586 */     if (this.item == null)
/* 587 */       this.item = Item.byBlock(this); 
/* 589 */     return this.item;
/*     */   }
/*     */   
/*     */   public boolean hasDynamicShape() {
/* 593 */     return this.dynamicShape;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 598 */     return "Block{" + BuiltInRegistries.BLOCK.wrapAsHolder(this).getRegisteredName() + "}";
/*     */   }
/*     */   
/*     */   protected Block asBlock() {
/* 603 */     return this;
/*     */   }
/*     */   
/*     */   protected Function<BlockState, VoxelShape> getShapeForEachState(Function<BlockState, VoxelShape> shapeCalculator) {
/* 608 */     Objects.requireNonNull((ImmutableMap)this.stateDefinition.getPossibleStates().stream().collect(ImmutableMap.toImmutableMap(Function.identity(), shapeCalculator)));
/* 608 */     return (ImmutableMap)this.stateDefinition.getPossibleStates().stream().collect(ImmutableMap.toImmutableMap(Function.identity(), shapeCalculator))::get;
/*     */   }
/*     */   
/*     */   protected Function<BlockState, VoxelShape> getShapeForEachState(Function<BlockState, VoxelShape> shapeCalculator, Property<?>... ignoredProperties) {
/* 612 */     Map<? extends Property<?>, Object> defaults = (Map<? extends Property<?>, Object>)Arrays.<Property<?>>stream(ignoredProperties).collect(Collectors.toMap(k -> k, k -> k.getPossibleValues().getFirst()));
/* 616 */     ImmutableMap<BlockState, VoxelShape> map = (ImmutableMap<BlockState, VoxelShape>)this.stateDefinition.getPossibleStates().stream().filter(state -> defaults.entrySet().stream().allMatch(())).collect(ImmutableMap.toImmutableMap(Function.identity(), shapeCalculator));
/* 618 */     return blockState -> {
/*     */         for (Map.Entry<? extends Property<?>, Object> entry : (Iterable<Map.Entry<? extends Property<?>, Object>>)defaults.entrySet())
/*     */           blockState = setValueHelper(blockState, (Property<Comparable>)entry.getKey(), entry.getValue()); 
/*     */         return (VoxelShape)map.get(blockState);
/*     */       };
/*     */   }
/*     */   
/*     */   private static <S extends net.minecraft.world.level.block.state.StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(S state, Property<T> property, Object value) {
/* 628 */     return (S)state.setValue(property, (Comparable)value);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Holder.Reference<Block> builtInRegistryHolder() {
/* 634 */     return this.builtInRegistryHolder;
/*     */   }
/*     */   
/*     */   protected void tryDropExperience(ServerLevel level, BlockPos pos, ItemStack tool, IntProvider xpRange) {
/* 638 */     int experience = EnchantmentHelper.processBlockExperience(level, tool, xpRange.sample(level.getRandom()));
/* 639 */     if (experience > 0)
/* 640 */       popExperience(level, pos, experience); 
/*     */   }
/*     */   
/*     */   @Retention(RetentionPolicy.CLASS)
/*     */   @Target({ElementType.TYPE_USE})
/*     */   public static @interface UpdateFlags {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\Block.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */