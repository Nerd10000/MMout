/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FireBlock extends BaseFireBlock {
/*  30 */   public static final MapCodec<FireBlock> CODEC = simpleCodec(FireBlock::new);
/*     */   
/*     */   public static final int MAX_AGE = 15;
/*     */   
/*     */   public MapCodec<FireBlock> codec() {
/*  34 */     return CODEC;
/*     */   }
/*     */   
/*  38 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
/*     */   
/*  40 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/*     */   
/*  41 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/*     */   
/*  42 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/*     */   
/*  43 */   public static final BooleanProperty WEST = PipeBlock.WEST;
/*     */   
/*  44 */   public static final BooleanProperty UP = PipeBlock.UP;
/*     */   
/*     */   public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   private static final int IGNITE_INSTANT = 60;
/*     */   
/*     */   private static final int IGNITE_EASY = 30;
/*     */   
/*     */   private static final int IGNITE_MEDIUM = 15;
/*     */   
/*     */   private static final int IGNITE_HARD = 5;
/*     */   
/*     */   private static final int BURN_INSTANT = 100;
/*     */   
/*     */   private static final int BURN_EASY = 60;
/*     */   
/*     */   private static final int BURN_MEDIUM = 20;
/*     */   
/*     */   private static final int BURN_HARD = 5;
/*     */   
/*     */   static {
/*  48 */     PROPERTY_BY_DIRECTION = (Map<Direction, BooleanProperty>)PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(e -> (e.getKey() != Direction.DOWN)).collect(Util.toMap());
/*     */   }
/*     */   
/*  65 */   private final Object2IntMap<Block> igniteOdds = (Object2IntMap<Block>)new Object2IntOpenHashMap();
/*     */   
/*  66 */   private final Object2IntMap<Block> burnOdds = (Object2IntMap<Block>)new Object2IntOpenHashMap();
/*     */   
/*     */   public FireBlock(BlockBehaviour.Properties properties) {
/*  69 */     super(properties, 1.0F);
/*  70 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0))).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)UP, Boolean.valueOf(false)));
/*  72 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  76 */     Map<Direction, VoxelShape> shapes = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*  78 */     return getShapeForEachState(state -> {
/*     */           VoxelShape shape = Shapes.empty();
/*     */           for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
/*     */             if (((Boolean)state.getValue((Property)entry.getValue())).booleanValue())
/*     */               shape = Shapes.or(shape, (VoxelShape)shapes.get(entry.getKey())); 
/*     */           } 
/*     */           return shape.isEmpty() ? SHAPE : shape;
/*  78 */         }(Property<?>[])new Property[] { (Property)AGE });
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  93 */     if (canSurvive(state, level, pos))
/*  94 */       return getStateWithAge(level, pos, ((Integer)state.getValue((Property)AGE)).intValue()); 
/*  97 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 102 */     return this.shapes.apply(state);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 107 */     return getStateForPlacement((BlockGetter)context.getLevel(), context.getClickedPos());
/*     */   }
/*     */   
/*     */   protected BlockState getStateForPlacement(BlockGetter level, BlockPos pos) {
/* 113 */     BlockPos below = pos.below();
/* 114 */     BlockState belowState = level.getBlockState(below);
/* 115 */     if (canBurn(belowState) || belowState.isFaceSturdy(level, below, Direction.UP))
/* 116 */       return defaultBlockState(); 
/* 119 */     BlockState result = defaultBlockState();
/* 120 */     for (Direction direction : Direction.values()) {
/* 121 */       BooleanProperty property = PROPERTY_BY_DIRECTION.get(direction);
/* 122 */       if (property != null)
/* 123 */         result = (BlockState)result.setValue((Property)property, Boolean.valueOf(canBurn(level.getBlockState(pos.relative(direction))))); 
/*     */     } 
/* 127 */     return result;
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 132 */     BlockPos below = pos.below();
/* 133 */     return (level.getBlockState(below).isFaceSturdy((BlockGetter)level, below, Direction.UP) || isValidFireLocation((BlockGetter)level, pos));
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 139 */     level.scheduleTick(pos, this, getFireTickDelay(level.getRandom()));
/* 141 */     if (!level.canSpreadFireAround(pos))
/*     */       return; 
/* 146 */     if (!state.canSurvive((LevelReader)level, pos))
/* 147 */       level.removeBlock(pos, false); 
/* 150 */     BlockState belowState = level.getBlockState(pos.below());
/* 151 */     boolean infiniBurn = belowState.is(level.dimensionType().infiniburn());
/* 153 */     int age = ((Integer)state.getValue((Property)AGE)).intValue();
/* 154 */     if (!infiniBurn && level.isRaining() && isNearRain((Level)level, pos) && random.nextFloat() < 0.2F + age * 0.03F) {
/* 155 */       level.removeBlock(pos, false);
/*     */       return;
/*     */     } 
/* 159 */     int newAge = Math.min(15, age + random.nextInt(3) / 2);
/* 160 */     if (age != newAge) {
/* 161 */       state = (BlockState)state.setValue((Property)AGE, Integer.valueOf(newAge));
/* 162 */       level.setBlock(pos, state, 260);
/*     */     } 
/* 165 */     if (!infiniBurn) {
/* 166 */       if (!isValidFireLocation((BlockGetter)level, pos)) {
/* 167 */         BlockPos below = pos.below();
/* 168 */         if (!level.getBlockState(below).isFaceSturdy((BlockGetter)level, below, Direction.UP) || age > 3)
/* 169 */           level.removeBlock(pos, false); 
/*     */         return;
/*     */       } 
/* 175 */       if (age == 15 && random.nextInt(4) == 0 && !canBurn(level.getBlockState(pos.below()))) {
/* 176 */         level.removeBlock(pos, false);
/*     */         return;
/*     */       } 
/*     */     } 
/* 181 */     boolean increasedBurnout = ((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, pos)).booleanValue();
/* 182 */     int extra = increasedBurnout ? -50 : 0;
/* 184 */     checkBurnOut((Level)level, pos.east(), 300 + extra, random, age);
/* 185 */     checkBurnOut((Level)level, pos.west(), 300 + extra, random, age);
/* 186 */     checkBurnOut((Level)level, pos.below(), 250 + extra, random, age);
/* 187 */     checkBurnOut((Level)level, pos.above(), 250 + extra, random, age);
/* 188 */     checkBurnOut((Level)level, pos.north(), 300 + extra, random, age);
/* 189 */     checkBurnOut((Level)level, pos.south(), 300 + extra, random, age);
/* 191 */     BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();
/* 192 */     for (int xx = -1; xx <= 1; xx++) {
/* 193 */       for (int zz = -1; zz <= 1; zz++) {
/* 194 */         for (int yy = -1; yy <= 4; yy++) {
/* 195 */           if (xx != 0 || yy != 0 || zz != 0) {
/* 199 */             int rate = 100;
/* 200 */             if (yy > 1)
/* 201 */               rate += (yy - 1) * 100; 
/* 204 */             testPos.setWithOffset((Vec3i)pos, xx, yy, zz);
/* 205 */             int igniteOdds = getIgniteOdds((LevelReader)level, (BlockPos)testPos);
/* 206 */             if (igniteOdds > 0) {
/* 210 */               int odds = (igniteOdds + 40 + level.getDifficulty().getId() * 7) / (age + 30);
/* 211 */               if (increasedBurnout)
/* 212 */                 odds /= 2; 
/* 214 */               if (odds > 0 && random.nextInt(rate) <= odds && (
/* 215 */                 !level.isRaining() || !isNearRain((Level)level, (BlockPos)testPos))) {
/* 219 */                 int spreadAge = Math.min(15, age + random.nextInt(5) / 4);
/* 220 */                 level.setBlock((BlockPos)testPos, getStateWithAge((LevelReader)level, (BlockPos)testPos, spreadAge), 3);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isNearRain(Level level, BlockPos testPos) {
/* 228 */     return (level.isRainingAt(testPos) || level.isRainingAt(testPos.west()) || level.isRainingAt(testPos.east()) || level.isRainingAt(testPos.north()) || level.isRainingAt(testPos.south()));
/*     */   }
/*     */   
/*     */   private int getBurnOdds(BlockState state) {
/* 232 */     if (state.hasProperty((Property)BlockStateProperties.WATERLOGGED) && ((Boolean)state.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue())
/* 233 */       return 0; 
/* 235 */     return this.burnOdds.getInt(state.getBlock());
/*     */   }
/*     */   
/*     */   private int getIgniteOdds(BlockState state) {
/* 239 */     if (state.hasProperty((Property)BlockStateProperties.WATERLOGGED) && ((Boolean)state.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue())
/* 240 */       return 0; 
/* 242 */     return this.igniteOdds.getInt(state.getBlock());
/*     */   }
/*     */   
/*     */   private void checkBurnOut(Level level, BlockPos pos, int chance, RandomSource random, int age) {
/* 246 */     int odds = getBurnOdds(level.getBlockState(pos));
/* 247 */     if (random.nextInt(chance) < odds) {
/* 248 */       BlockState oldState = level.getBlockState(pos);
/* 250 */       if (random.nextInt(age + 10) < 5 && !level.isRainingAt(pos)) {
/* 251 */         int newAge = Math.min(age + random.nextInt(5) / 4, 15);
/* 252 */         level.setBlock(pos, getStateWithAge((LevelReader)level, pos, newAge), 3);
/*     */       } else {
/* 254 */         level.removeBlock(pos, false);
/*     */       } 
/* 257 */       Block block = oldState.getBlock();
/* 258 */       if (block instanceof TntBlock)
/* 259 */         TntBlock.prime(level, pos); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private BlockState getStateWithAge(LevelReader level, BlockPos pos, int age) {
/* 265 */     BlockState stateForPlacement = getState((BlockGetter)level, pos);
/* 266 */     if (stateForPlacement.is(Blocks.FIRE))
/* 267 */       return (BlockState)stateForPlacement.setValue((Property)AGE, Integer.valueOf(age)); 
/* 270 */     return stateForPlacement;
/*     */   }
/*     */   
/*     */   private boolean isValidFireLocation(BlockGetter level, BlockPos pos) {
/* 274 */     for (Direction direction : Direction.values()) {
/* 275 */       if (canBurn(level.getBlockState(pos.relative(direction))))
/* 276 */         return true; 
/*     */     } 
/* 280 */     return false;
/*     */   }
/*     */   
/*     */   private int getIgniteOdds(LevelReader level, BlockPos pos) {
/* 284 */     if (!level.isEmptyBlock(pos))
/* 285 */       return 0; 
/* 288 */     int odds = 0;
/* 289 */     for (Direction direction : Direction.values()) {
/* 290 */       BlockState blockState = level.getBlockState(pos.relative(direction));
/* 291 */       odds = Math.max(getIgniteOdds(blockState), odds);
/*     */     } 
/* 294 */     return odds;
/*     */   }
/*     */   
/*     */   protected boolean canBurn(BlockState state) {
/* 299 */     return (getIgniteOdds(state) > 0);
/*     */   }
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 304 */     super.onPlace(state, level, pos, oldState, movedByPiston);
/* 306 */     level.scheduleTick(pos, this, getFireTickDelay(level.getRandom()));
/*     */   }
/*     */   
/*     */   private static int getFireTickDelay(RandomSource random) {
/* 310 */     return 30 + random.nextInt(10);
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 315 */     builder.add(new Property[] { (Property)AGE, (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST, (Property)UP });
/*     */   }
/*     */   
/*     */   private void setFlammable(Block block, int igniteOdds, int burnOdds) {
/* 319 */     this.igniteOdds.put(block, igniteOdds);
/* 320 */     this.burnOdds.put(block, burnOdds);
/*     */   }
/*     */   
/*     */   public static void bootStrap() {
/* 324 */     FireBlock fire = (FireBlock)Blocks.FIRE;
/* 325 */     fire.setFlammable(Blocks.OAK_PLANKS, 5, 20);
/* 326 */     fire.setFlammable(Blocks.SPRUCE_PLANKS, 5, 20);
/* 327 */     fire.setFlammable(Blocks.BIRCH_PLANKS, 5, 20);
/* 328 */     fire.setFlammable(Blocks.JUNGLE_PLANKS, 5, 20);
/* 329 */     fire.setFlammable(Blocks.ACACIA_PLANKS, 5, 20);
/* 330 */     fire.setFlammable(Blocks.CHERRY_PLANKS, 5, 20);
/* 331 */     fire.setFlammable(Blocks.DARK_OAK_PLANKS, 5, 20);
/* 332 */     fire.setFlammable(Blocks.PALE_OAK_PLANKS, 5, 20);
/* 333 */     fire.setFlammable(Blocks.MANGROVE_PLANKS, 5, 20);
/* 334 */     fire.setFlammable(Blocks.BAMBOO_PLANKS, 5, 20);
/* 335 */     fire.setFlammable(Blocks.BAMBOO_MOSAIC, 5, 20);
/* 336 */     fire.setFlammable(Blocks.OAK_SLAB, 5, 20);
/* 337 */     fire.setFlammable(Blocks.SPRUCE_SLAB, 5, 20);
/* 338 */     fire.setFlammable(Blocks.BIRCH_SLAB, 5, 20);
/* 339 */     fire.setFlammable(Blocks.JUNGLE_SLAB, 5, 20);
/* 340 */     fire.setFlammable(Blocks.ACACIA_SLAB, 5, 20);
/* 341 */     fire.setFlammable(Blocks.CHERRY_SLAB, 5, 20);
/* 342 */     fire.setFlammable(Blocks.DARK_OAK_SLAB, 5, 20);
/* 343 */     fire.setFlammable(Blocks.PALE_OAK_SLAB, 5, 20);
/* 344 */     fire.setFlammable(Blocks.MANGROVE_SLAB, 5, 20);
/* 345 */     fire.setFlammable(Blocks.BAMBOO_SLAB, 5, 20);
/* 346 */     fire.setFlammable(Blocks.BAMBOO_MOSAIC_SLAB, 5, 20);
/* 347 */     fire.setFlammable(Blocks.OAK_FENCE_GATE, 5, 20);
/* 348 */     fire.setFlammable(Blocks.SPRUCE_FENCE_GATE, 5, 20);
/* 349 */     fire.setFlammable(Blocks.BIRCH_FENCE_GATE, 5, 20);
/* 350 */     fire.setFlammable(Blocks.JUNGLE_FENCE_GATE, 5, 20);
/* 351 */     fire.setFlammable(Blocks.ACACIA_FENCE_GATE, 5, 20);
/* 352 */     fire.setFlammable(Blocks.CHERRY_FENCE_GATE, 5, 20);
/* 353 */     fire.setFlammable(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
/* 354 */     fire.setFlammable(Blocks.PALE_OAK_FENCE_GATE, 5, 20);
/* 355 */     fire.setFlammable(Blocks.MANGROVE_FENCE_GATE, 5, 20);
/* 356 */     fire.setFlammable(Blocks.BAMBOO_FENCE_GATE, 5, 20);
/* 357 */     fire.setFlammable(Blocks.OAK_FENCE, 5, 20);
/* 358 */     fire.setFlammable(Blocks.SPRUCE_FENCE, 5, 20);
/* 359 */     fire.setFlammable(Blocks.BIRCH_FENCE, 5, 20);
/* 360 */     fire.setFlammable(Blocks.JUNGLE_FENCE, 5, 20);
/* 361 */     fire.setFlammable(Blocks.ACACIA_FENCE, 5, 20);
/* 362 */     fire.setFlammable(Blocks.CHERRY_FENCE, 5, 20);
/* 363 */     fire.setFlammable(Blocks.DARK_OAK_FENCE, 5, 20);
/* 364 */     fire.setFlammable(Blocks.PALE_OAK_FENCE, 5, 20);
/* 365 */     fire.setFlammable(Blocks.MANGROVE_FENCE, 5, 20);
/* 366 */     fire.setFlammable(Blocks.BAMBOO_FENCE, 5, 20);
/* 367 */     fire.setFlammable(Blocks.OAK_STAIRS, 5, 20);
/* 368 */     fire.setFlammable(Blocks.BIRCH_STAIRS, 5, 20);
/* 369 */     fire.setFlammable(Blocks.SPRUCE_STAIRS, 5, 20);
/* 370 */     fire.setFlammable(Blocks.JUNGLE_STAIRS, 5, 20);
/* 371 */     fire.setFlammable(Blocks.ACACIA_STAIRS, 5, 20);
/* 372 */     fire.setFlammable(Blocks.CHERRY_STAIRS, 5, 20);
/* 373 */     fire.setFlammable(Blocks.DARK_OAK_STAIRS, 5, 20);
/* 374 */     fire.setFlammable(Blocks.PALE_OAK_STAIRS, 5, 20);
/* 375 */     fire.setFlammable(Blocks.MANGROVE_STAIRS, 5, 20);
/* 376 */     fire.setFlammable(Blocks.BAMBOO_STAIRS, 5, 20);
/* 377 */     fire.setFlammable(Blocks.BAMBOO_MOSAIC_STAIRS, 5, 20);
/* 378 */     fire.setFlammable(Blocks.OAK_LOG, 5, 5);
/* 379 */     fire.setFlammable(Blocks.SPRUCE_LOG, 5, 5);
/* 380 */     fire.setFlammable(Blocks.BIRCH_LOG, 5, 5);
/* 381 */     fire.setFlammable(Blocks.JUNGLE_LOG, 5, 5);
/* 382 */     fire.setFlammable(Blocks.ACACIA_LOG, 5, 5);
/* 383 */     fire.setFlammable(Blocks.CHERRY_LOG, 5, 5);
/* 384 */     fire.setFlammable(Blocks.PALE_OAK_LOG, 5, 5);
/* 385 */     fire.setFlammable(Blocks.DARK_OAK_LOG, 5, 5);
/* 386 */     fire.setFlammable(Blocks.MANGROVE_LOG, 5, 5);
/* 387 */     fire.setFlammable(Blocks.BAMBOO_BLOCK, 5, 5);
/* 388 */     fire.setFlammable(Blocks.STRIPPED_OAK_LOG, 5, 5);
/* 389 */     fire.setFlammable(Blocks.STRIPPED_SPRUCE_LOG, 5, 5);
/* 390 */     fire.setFlammable(Blocks.STRIPPED_BIRCH_LOG, 5, 5);
/* 391 */     fire.setFlammable(Blocks.STRIPPED_JUNGLE_LOG, 5, 5);
/* 392 */     fire.setFlammable(Blocks.STRIPPED_ACACIA_LOG, 5, 5);
/* 393 */     fire.setFlammable(Blocks.STRIPPED_CHERRY_LOG, 5, 5);
/* 394 */     fire.setFlammable(Blocks.STRIPPED_DARK_OAK_LOG, 5, 5);
/* 395 */     fire.setFlammable(Blocks.STRIPPED_PALE_OAK_LOG, 5, 5);
/* 396 */     fire.setFlammable(Blocks.STRIPPED_MANGROVE_LOG, 5, 5);
/* 397 */     fire.setFlammable(Blocks.STRIPPED_BAMBOO_BLOCK, 5, 5);
/* 398 */     fire.setFlammable(Blocks.STRIPPED_OAK_WOOD, 5, 5);
/* 399 */     fire.setFlammable(Blocks.STRIPPED_SPRUCE_WOOD, 5, 5);
/* 400 */     fire.setFlammable(Blocks.STRIPPED_BIRCH_WOOD, 5, 5);
/* 401 */     fire.setFlammable(Blocks.STRIPPED_JUNGLE_WOOD, 5, 5);
/* 402 */     fire.setFlammable(Blocks.STRIPPED_ACACIA_WOOD, 5, 5);
/* 403 */     fire.setFlammable(Blocks.STRIPPED_CHERRY_WOOD, 5, 5);
/* 404 */     fire.setFlammable(Blocks.STRIPPED_DARK_OAK_WOOD, 5, 5);
/* 405 */     fire.setFlammable(Blocks.STRIPPED_PALE_OAK_WOOD, 5, 5);
/* 406 */     fire.setFlammable(Blocks.STRIPPED_MANGROVE_WOOD, 5, 5);
/* 407 */     fire.setFlammable(Blocks.OAK_WOOD, 5, 5);
/* 408 */     fire.setFlammable(Blocks.SPRUCE_WOOD, 5, 5);
/* 409 */     fire.setFlammable(Blocks.BIRCH_WOOD, 5, 5);
/* 410 */     fire.setFlammable(Blocks.JUNGLE_WOOD, 5, 5);
/* 411 */     fire.setFlammable(Blocks.ACACIA_WOOD, 5, 5);
/* 412 */     fire.setFlammable(Blocks.CHERRY_WOOD, 5, 5);
/* 413 */     fire.setFlammable(Blocks.PALE_OAK_WOOD, 5, 5);
/* 414 */     fire.setFlammable(Blocks.DARK_OAK_WOOD, 5, 5);
/* 415 */     fire.setFlammable(Blocks.MANGROVE_WOOD, 5, 5);
/* 416 */     fire.setFlammable(Blocks.MANGROVE_ROOTS, 5, 20);
/* 417 */     fire.setFlammable(Blocks.OAK_LEAVES, 30, 60);
/* 418 */     fire.setFlammable(Blocks.SPRUCE_LEAVES, 30, 60);
/* 419 */     fire.setFlammable(Blocks.BIRCH_LEAVES, 30, 60);
/* 420 */     fire.setFlammable(Blocks.JUNGLE_LEAVES, 30, 60);
/* 421 */     fire.setFlammable(Blocks.ACACIA_LEAVES, 30, 60);
/* 422 */     fire.setFlammable(Blocks.CHERRY_LEAVES, 30, 60);
/* 423 */     fire.setFlammable(Blocks.DARK_OAK_LEAVES, 30, 60);
/* 424 */     fire.setFlammable(Blocks.PALE_OAK_LEAVES, 30, 60);
/* 425 */     fire.setFlammable(Blocks.MANGROVE_LEAVES, 30, 60);
/* 426 */     fire.setFlammable(Blocks.BOOKSHELF, 30, 20);
/* 427 */     fire.setFlammable(Blocks.TNT, 15, 100);
/* 428 */     fire.setFlammable(Blocks.SHORT_GRASS, 60, 100);
/* 429 */     fire.setFlammable(Blocks.FERN, 60, 100);
/* 430 */     fire.setFlammable(Blocks.DEAD_BUSH, 60, 100);
/* 431 */     fire.setFlammable(Blocks.SHORT_DRY_GRASS, 60, 100);
/* 432 */     fire.setFlammable(Blocks.TALL_DRY_GRASS, 60, 100);
/* 433 */     fire.setFlammable(Blocks.SUNFLOWER, 60, 100);
/* 434 */     fire.setFlammable(Blocks.LILAC, 60, 100);
/* 435 */     fire.setFlammable(Blocks.ROSE_BUSH, 60, 100);
/* 436 */     fire.setFlammable(Blocks.PEONY, 60, 100);
/* 437 */     fire.setFlammable(Blocks.TALL_GRASS, 60, 100);
/* 438 */     fire.setFlammable(Blocks.LARGE_FERN, 60, 100);
/* 439 */     fire.setFlammable(Blocks.DANDELION, 60, 100);
/* 440 */     fire.setFlammable(Blocks.GOLDEN_DANDELION, 60, 100);
/* 441 */     fire.setFlammable(Blocks.POPPY, 60, 100);
/* 442 */     fire.setFlammable(Blocks.OPEN_EYEBLOSSOM, 60, 100);
/* 443 */     fire.setFlammable(Blocks.CLOSED_EYEBLOSSOM, 60, 100);
/* 444 */     fire.setFlammable(Blocks.BLUE_ORCHID, 60, 100);
/* 445 */     fire.setFlammable(Blocks.ALLIUM, 60, 100);
/* 446 */     fire.setFlammable(Blocks.AZURE_BLUET, 60, 100);
/* 447 */     fire.setFlammable(Blocks.RED_TULIP, 60, 100);
/* 448 */     fire.setFlammable(Blocks.ORANGE_TULIP, 60, 100);
/* 449 */     fire.setFlammable(Blocks.WHITE_TULIP, 60, 100);
/* 450 */     fire.setFlammable(Blocks.PINK_TULIP, 60, 100);
/* 451 */     fire.setFlammable(Blocks.OXEYE_DAISY, 60, 100);
/* 452 */     fire.setFlammable(Blocks.CORNFLOWER, 60, 100);
/* 453 */     fire.setFlammable(Blocks.LILY_OF_THE_VALLEY, 60, 100);
/* 454 */     fire.setFlammable(Blocks.TORCHFLOWER, 60, 100);
/* 455 */     fire.setFlammable(Blocks.PITCHER_PLANT, 60, 100);
/* 456 */     fire.setFlammable(Blocks.WITHER_ROSE, 60, 100);
/* 457 */     fire.setFlammable(Blocks.PINK_PETALS, 60, 100);
/* 458 */     fire.setFlammable(Blocks.WILDFLOWERS, 60, 100);
/* 459 */     fire.setFlammable(Blocks.LEAF_LITTER, 60, 100);
/* 460 */     fire.setFlammable(Blocks.CACTUS_FLOWER, 60, 100);
/* 461 */     Blocks.WOOL.forEach(block -> fire.setFlammable(block, 30, 60));
/* 462 */     fire.setFlammable(Blocks.VINE, 15, 100);
/* 463 */     fire.setFlammable(Blocks.COAL_BLOCK, 5, 5);
/* 464 */     fire.setFlammable(Blocks.HAY_BLOCK, 60, 20);
/* 465 */     fire.setFlammable(Blocks.TARGET, 15, 20);
/* 466 */     Blocks.CARPET.forEach(block -> fire.setFlammable(block, 60, 20));
/* 467 */     fire.setFlammable(Blocks.PALE_MOSS_BLOCK, 5, 100);
/* 468 */     fire.setFlammable(Blocks.PALE_MOSS_CARPET, 5, 100);
/* 469 */     fire.setFlammable(Blocks.PALE_HANGING_MOSS, 5, 100);
/* 470 */     fire.setFlammable(Blocks.DRIED_KELP_BLOCK, 30, 60);
/* 471 */     fire.setFlammable(Blocks.BAMBOO, 60, 60);
/* 472 */     fire.setFlammable(Blocks.SCAFFOLDING, 60, 60);
/* 473 */     fire.setFlammable(Blocks.LECTERN, 30, 20);
/* 474 */     fire.setFlammable(Blocks.COMPOSTER, 5, 20);
/* 475 */     fire.setFlammable(Blocks.SWEET_BERRY_BUSH, 60, 100);
/* 476 */     fire.setFlammable(Blocks.BEEHIVE, 5, 20);
/* 477 */     fire.setFlammable(Blocks.BEE_NEST, 30, 20);
/* 478 */     fire.setFlammable(Blocks.AZALEA_LEAVES, 30, 60);
/* 479 */     fire.setFlammable(Blocks.FLOWERING_AZALEA_LEAVES, 30, 60);
/* 480 */     fire.setFlammable(Blocks.CAVE_VINES, 15, 60);
/* 481 */     fire.setFlammable(Blocks.CAVE_VINES_PLANT, 15, 60);
/* 482 */     fire.setFlammable(Blocks.SPORE_BLOSSOM, 60, 100);
/* 483 */     fire.setFlammable(Blocks.AZALEA, 30, 60);
/* 484 */     fire.setFlammable(Blocks.FLOWERING_AZALEA, 30, 60);
/* 485 */     fire.setFlammable(Blocks.BIG_DRIPLEAF, 60, 100);
/* 486 */     fire.setFlammable(Blocks.BIG_DRIPLEAF_STEM, 60, 100);
/* 487 */     fire.setFlammable(Blocks.SMALL_DRIPLEAF, 60, 100);
/* 488 */     fire.setFlammable(Blocks.HANGING_ROOTS, 30, 60);
/* 489 */     fire.setFlammable(Blocks.GLOW_LICHEN, 15, 100);
/* 490 */     fire.setFlammable(Blocks.FIREFLY_BUSH, 60, 100);
/* 491 */     fire.setFlammable(Blocks.BUSH, 60, 100);
/* 492 */     fire.setFlammable(Blocks.ACACIA_SHELF, 30, 20);
/* 493 */     fire.setFlammable(Blocks.BAMBOO_SHELF, 30, 20);
/* 494 */     fire.setFlammable(Blocks.BIRCH_SHELF, 30, 20);
/* 495 */     fire.setFlammable(Blocks.CHERRY_SHELF, 30, 20);
/* 496 */     fire.setFlammable(Blocks.DARK_OAK_SHELF, 30, 20);
/* 497 */     fire.setFlammable(Blocks.JUNGLE_SHELF, 30, 20);
/* 498 */     fire.setFlammable(Blocks.MANGROVE_SHELF, 30, 20);
/* 499 */     fire.setFlammable(Blocks.OAK_SHELF, 30, 20);
/* 500 */     fire.setFlammable(Blocks.PALE_OAK_SHELF, 30, 20);
/* 501 */     fire.setFlammable(Blocks.SPRUCE_SHELF, 30, 20);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\FireBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */