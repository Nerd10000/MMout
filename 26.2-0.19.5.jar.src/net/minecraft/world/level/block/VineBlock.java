/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class VineBlock extends Block {
/*  26 */   public static final MapCodec<VineBlock> CODEC = simpleCodec(VineBlock::new);
/*     */   
/*     */   public MapCodec<VineBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */   
/*  33 */   public static final BooleanProperty UP = PipeBlock.UP;
/*     */   
/*  34 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/*     */   
/*  35 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/*     */   
/*  36 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/*     */   
/*  37 */   public static final BooleanProperty WEST = PipeBlock.WEST;
/*     */   
/*     */   public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   static {
/*  39 */     PROPERTY_BY_DIRECTION = (Map<Direction, BooleanProperty>)PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(e -> (e.getKey() != Direction.DOWN)).collect(Util.toMap());
/*     */   }
/*     */   
/*     */   public VineBlock(BlockBehaviour.Properties properties) {
/*  44 */     super(properties);
/*  45 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)UP, Boolean.valueOf(false))).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false)));
/*  47 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  51 */     Map<Direction, VoxelShape> shapes = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*  53 */     return getShapeForEachState(state -> {
/*     */           VoxelShape shape = Shapes.empty();
/*     */           for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
/*     */             if (((Boolean)state.getValue((Property)entry.getValue())).booleanValue())
/*     */               shape = Shapes.or(shape, (VoxelShape)shapes.get(entry.getKey())); 
/*     */           } 
/*     */           return shape.isEmpty() ? Shapes.block() : shape;
/*     */         });
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  68 */     return this.shapes.apply(state);
/*     */   }
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState state) {
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  78 */     return hasFaces(getUpdatedState(state, (BlockGetter)level, pos));
/*     */   }
/*     */   
/*     */   private boolean hasFaces(BlockState blockState) {
/*  82 */     return (countFaces(blockState) > 0);
/*     */   }
/*     */   
/*     */   private int countFaces(BlockState blockState) {
/*  86 */     int count = 0;
/*  87 */     for (BooleanProperty property : PROPERTY_BY_DIRECTION.values()) {
/*  88 */       if (((Boolean)blockState.getValue((Property)property)).booleanValue())
/*  89 */         count++; 
/*     */     } 
/*  93 */     return count;
/*     */   }
/*     */   
/*     */   private boolean canSupportAtFace(BlockGetter level, BlockPos pos, Direction direction) {
/*  97 */     if (direction == Direction.DOWN)
/*  98 */       return false; 
/* 101 */     BlockPos relative = pos.relative(direction);
/* 102 */     if (isAcceptableNeighbour(level, relative, direction))
/* 103 */       return true; 
/* 106 */     if (direction.getAxis() != Direction.Axis.Y) {
/* 108 */       BooleanProperty property = PROPERTY_BY_DIRECTION.get(direction);
/* 109 */       BlockState aboveState = level.getBlockState(pos.above());
/* 110 */       return (aboveState.is(this) && ((Boolean)aboveState.getValue((Property)property)).booleanValue());
/*     */     } 
/* 112 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isAcceptableNeighbour(BlockGetter level, BlockPos neighbourPos, Direction directionToNeighbour) {
/* 116 */     return MultifaceBlock.canAttachTo(level, directionToNeighbour, neighbourPos, level.getBlockState(neighbourPos));
/*     */   }
/*     */   
/*     */   private BlockState getUpdatedState(BlockState state, BlockGetter level, BlockPos pos) {
/* 120 */     BlockPos abovePos = pos.above();
/* 121 */     if (((Boolean)state.getValue((Property)UP)).booleanValue())
/* 122 */       state = (BlockState)state.setValue((Property)UP, Boolean.valueOf(isAcceptableNeighbour(level, abovePos, Direction.DOWN))); 
/* 126 */     BlockState aboveState = null;
/* 127 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 128 */       BooleanProperty property = getPropertyForFace(direction);
/* 130 */       if (((Boolean)state.getValue((Property)property)).booleanValue()) {
/* 131 */         boolean canSupport = canSupportAtFace(level, pos, direction);
/* 132 */         if (!canSupport) {
/* 133 */           if (aboveState == null)
/* 134 */             aboveState = level.getBlockState(abovePos); 
/* 136 */           canSupport = (aboveState.is(this) && ((Boolean)aboveState.getValue((Property)property)).booleanValue());
/*     */         } 
/* 138 */         state = (BlockState)state.setValue((Property)property, Boolean.valueOf(canSupport));
/*     */       } 
/*     */     } 
/* 141 */     return state;
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 146 */     if (directionToNeighbour == Direction.DOWN)
/* 147 */       return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random); 
/* 150 */     BlockState blockState = getUpdatedState(state, (BlockGetter)level, pos);
/* 152 */     if (!hasFaces(blockState))
/* 153 */       return Blocks.AIR.defaultBlockState(); 
/* 156 */     return blockState;
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 161 */     if (!((Boolean)level.getGameRules().get(GameRules.SPREAD_VINES)).booleanValue())
/*     */       return; 
/* 164 */     if (random.nextInt(4) != 0)
/*     */       return; 
/* 168 */     Direction testDirection = Direction.getRandom(random);
/* 170 */     BlockPos abovePos = pos.above();
/* 171 */     if (testDirection.getAxis().isHorizontal() && !((Boolean)state.getValue((Property)getPropertyForFace(testDirection))).booleanValue()) {
/* 172 */       if (!canSpread((BlockGetter)level, pos))
/*     */         return; 
/* 176 */       BlockPos testPos = pos.relative(testDirection);
/* 178 */       BlockState edgeState = level.getBlockState(testPos);
/* 179 */       if (edgeState.isAir()) {
/* 181 */         Direction cwDirection = testDirection.getClockWise();
/* 182 */         Direction ccwDirection = testDirection.getCounterClockWise();
/* 185 */         boolean cwHasConnectingFace = ((Boolean)state.getValue((Property)getPropertyForFace(cwDirection))).booleanValue();
/* 186 */         boolean ccwHasConnectingFace = ((Boolean)state.getValue((Property)getPropertyForFace(ccwDirection))).booleanValue();
/* 188 */         BlockPos cwTestPos = testPos.relative(cwDirection);
/* 189 */         BlockPos ccwTestPos = testPos.relative(ccwDirection);
/* 191 */         if (cwHasConnectingFace && isAcceptableNeighbour((BlockGetter)level, cwTestPos, cwDirection)) {
/* 192 */           level.setBlock(testPos, (BlockState)defaultBlockState().setValue((Property)getPropertyForFace(cwDirection), Boolean.valueOf(true)), 2);
/* 193 */         } else if (ccwHasConnectingFace && isAcceptableNeighbour((BlockGetter)level, ccwTestPos, ccwDirection)) {
/* 194 */           level.setBlock(testPos, (BlockState)defaultBlockState().setValue((Property)getPropertyForFace(ccwDirection), Boolean.valueOf(true)), 2);
/*     */         } else {
/* 197 */           Direction opposite = testDirection.getOpposite();
/* 198 */           if (cwHasConnectingFace && level.isEmptyBlock(cwTestPos) && isAcceptableNeighbour((BlockGetter)level, pos.relative(cwDirection), opposite)) {
/* 199 */             level.setBlock(cwTestPos, (BlockState)defaultBlockState().setValue((Property)getPropertyForFace(opposite), Boolean.valueOf(true)), 2);
/* 200 */           } else if (ccwHasConnectingFace && level.isEmptyBlock(ccwTestPos) && isAcceptableNeighbour((BlockGetter)level, pos.relative(ccwDirection), opposite)) {
/* 201 */             level.setBlock(ccwTestPos, (BlockState)defaultBlockState().setValue((Property)getPropertyForFace(opposite), Boolean.valueOf(true)), 2);
/* 204 */           } else if (random.nextFloat() < 0.05D && isAcceptableNeighbour((BlockGetter)level, testPos.above(), Direction.UP)) {
/* 205 */             level.setBlock(testPos, (BlockState)defaultBlockState().setValue((Property)UP, Boolean.valueOf(true)), 2);
/*     */           } 
/*     */         } 
/* 209 */       } else if (isAcceptableNeighbour((BlockGetter)level, testPos, testDirection)) {
/* 211 */         level.setBlock(pos, (BlockState)state.setValue((Property)getPropertyForFace(testDirection), Boolean.valueOf(true)), 2);
/*     */       } 
/*     */       return;
/*     */     } 
/* 216 */     if (testDirection == Direction.UP && pos.getY() < level.getMaxY()) {
/* 217 */       if (canSupportAtFace((BlockGetter)level, pos, testDirection)) {
/* 218 */         level.setBlock(pos, (BlockState)state.setValue((Property)UP, Boolean.valueOf(true)), 2);
/*     */         return;
/*     */       } 
/* 221 */       if (level.isEmptyBlock(abovePos)) {
/* 222 */         if (!canSpread((BlockGetter)level, pos))
/*     */           return; 
/* 227 */         BlockState aboveState = state;
/* 228 */         for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 229 */           if (random.nextBoolean() || !isAcceptableNeighbour((BlockGetter)level, abovePos.relative(direction), direction))
/* 230 */             aboveState = (BlockState)aboveState.setValue((Property)getPropertyForFace(direction), Boolean.valueOf(false)); 
/*     */         } 
/* 233 */         if (hasHorizontalConnection(aboveState))
/* 234 */           level.setBlock(abovePos, aboveState, 2); 
/*     */         return;
/*     */       } 
/*     */     } 
/* 239 */     if (pos.getY() > level.getMinY()) {
/* 241 */       BlockPos belowPos = pos.below();
/* 242 */       BlockState belowState = level.getBlockState(belowPos);
/* 244 */       if (belowState.isAir() || belowState.is(this)) {
/* 245 */         BlockState before = belowState.isAir() ? defaultBlockState() : belowState;
/* 246 */         BlockState after = copyRandomFaces(state, before, random);
/* 247 */         if (before != after && hasHorizontalConnection(after))
/* 248 */           level.setBlock(belowPos, after, 2); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private BlockState copyRandomFaces(BlockState from, BlockState to, RandomSource random) {
/* 255 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 256 */       if (random.nextBoolean()) {
/* 257 */         BooleanProperty propertyForFace = getPropertyForFace(direction);
/* 258 */         if (((Boolean)from.getValue((Property)propertyForFace)).booleanValue())
/* 259 */           to = (BlockState)to.setValue((Property)propertyForFace, Boolean.valueOf(true)); 
/*     */       } 
/*     */     } 
/* 263 */     return to;
/*     */   }
/*     */   
/*     */   private boolean hasHorizontalConnection(BlockState state) {
/* 267 */     return (((Boolean)state.getValue((Property)NORTH)).booleanValue() || ((Boolean)state.getValue((Property)EAST)).booleanValue() || ((Boolean)state.getValue((Property)SOUTH)).booleanValue() || ((Boolean)state.getValue((Property)WEST)).booleanValue());
/*     */   }
/*     */   
/*     */   private boolean canSpread(BlockGetter level, BlockPos pos) {
/* 271 */     int radius = 4;
/* 273 */     Iterable<BlockPos> iterable = BlockPos.betweenClosed(pos
/* 274 */         .getX() - 4, pos.getY() - 1, pos.getZ() - 4, pos
/* 275 */         .getX() + 4, pos.getY() + 1, pos.getZ() + 4);
/* 278 */     int max = 5;
/* 279 */     for (BlockPos blockPos : iterable) {
/* 280 */       if (level.getBlockState(blockPos).is(this) && 
/* 281 */         --max <= 0)
/* 282 */         return false; 
/*     */     } 
/* 286 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/* 291 */     BlockState clickedState = context.getLevel().getBlockState(context.getClickedPos());
/* 292 */     if (clickedState.is(this))
/* 293 */       return (countFaces(clickedState) < PROPERTY_BY_DIRECTION.size()); 
/* 296 */     return super.canBeReplaced(state, context);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 301 */     BlockState clickedState = context.getLevel().getBlockState(context.getClickedPos());
/* 302 */     boolean clickedVine = clickedState.is(this);
/* 303 */     BlockState result = clickedVine ? clickedState : defaultBlockState();
/* 305 */     for (Direction direction : context.getNearestLookingDirections()) {
/* 306 */       if (direction != Direction.DOWN) {
/* 307 */         BooleanProperty face = getPropertyForFace(direction);
/* 308 */         boolean faceOccupied = (clickedVine && ((Boolean)clickedState.getValue((Property)face)).booleanValue());
/* 309 */         if (!faceOccupied && canSupportAtFace((BlockGetter)context.getLevel(), context.getClickedPos(), direction))
/* 310 */           return (BlockState)result.setValue((Property)face, Boolean.valueOf(true)); 
/*     */       } 
/*     */     } 
/* 315 */     return clickedVine ? result : null;
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 320 */     builder.add(new Property[] { (Property)UP, (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST });
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 325 */     switch (rotation) {
/*     */       case LEFT_RIGHT:
/*     */       
/*     */       case FRONT_BACK:
/*     */       
/*     */       case null:
/*     */       
/*     */     } 
/* 325 */     return 
/*     */       
/* 329 */       state;
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 335 */     switch (mirror) {
/*     */       case LEFT_RIGHT:
/* 337 */         return (BlockState)((BlockState)state.setValue((Property)NORTH, state.getValue((Property)SOUTH))).setValue((Property)SOUTH, state.getValue((Property)NORTH));
/*     */       case FRONT_BACK:
/* 340 */         return (BlockState)((BlockState)state.setValue((Property)EAST, state.getValue((Property)WEST))).setValue((Property)WEST, state.getValue((Property)EAST));
/*     */     } 
/* 345 */     return super.mirror(state, mirror);
/*     */   }
/*     */   
/*     */   public static BooleanProperty getPropertyForFace(Direction direction) {
/* 349 */     return PROPERTY_BY_DIRECTION.get(direction);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\VineBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */