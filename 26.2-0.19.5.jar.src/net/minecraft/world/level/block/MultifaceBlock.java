/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MultifaceBlock extends Block implements SimpleWaterloggedBlock {
/*  36 */   public static final MapCodec<MultifaceBlock> CODEC = simpleCodec(MultifaceBlock::new);
/*     */   
/*     */   protected MapCodec<? extends MultifaceBlock> codec() {
/*  40 */     return CODEC;
/*     */   }
/*     */   
/*  43 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  45 */   private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
/*     */   
/*  47 */   protected static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   private final boolean canRotate;
/*     */   
/*     */   private final boolean canMirrorX;
/*     */   
/*     */   private final boolean canMirrorZ;
/*     */   
/*     */   public MultifaceBlock(BlockBehaviour.Properties properties) {
/*  56 */     super(properties);
/*  57 */     registerDefaultState(getDefaultMultifaceState(this.stateDefinition));
/*  58 */     this.shapes = makeShapes();
/*  60 */     this.canRotate = Direction.Plane.HORIZONTAL.stream().allMatch(this::isFaceSupported);
/*  61 */     this.canMirrorX = (Direction.Plane.HORIZONTAL.stream().filter((Predicate)Direction.Axis.X).filter(this::isFaceSupported).count() % 2L == 0L);
/*  62 */     this.canMirrorZ = (Direction.Plane.HORIZONTAL.stream().filter((Predicate)Direction.Axis.Z).filter(this::isFaceSupported).count() % 2L == 0L);
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  66 */     Map<Direction, VoxelShape> shapes = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*  68 */     return getShapeForEachState(state -> {
/*     */           VoxelShape shape = Shapes.empty();
/*     */           for (Direction direction : DIRECTIONS) {
/*     */             if (hasFace(state, direction))
/*     */               shape = Shapes.or(shape, (VoxelShape)shapes.get(direction)); 
/*     */           } 
/*     */           return shape.isEmpty() ? Shapes.block() : shape;
/*  68 */         }(Property<?>[])new Property[] { (Property)WATERLOGGED });
/*     */   }
/*     */   
/*     */   public static Set<Direction> availableFaces(BlockState state) {
/*  80 */     if (!(state.getBlock() instanceof MultifaceBlock))
/*  81 */       return Set.of(); 
/*  83 */     Set<Direction> faces = EnumSet.noneOf(Direction.class);
/*  84 */     for (Direction direction : Direction.values()) {
/*  85 */       if (hasFace(state, direction))
/*  86 */         faces.add(direction); 
/*     */     } 
/*  89 */     return faces;
/*     */   }
/*     */   
/*     */   public static Set<Direction> unpack(byte data) {
/*  93 */     Set<Direction> presentDirections = EnumSet.noneOf(Direction.class);
/*  94 */     for (Direction direction : Direction.values()) {
/*  95 */       if ((data & (byte)(1 << direction.ordinal())) > 0)
/*  96 */         presentDirections.add(direction); 
/*     */     } 
/*  99 */     return presentDirections;
/*     */   }
/*     */   
/*     */   public static byte pack(Collection<Direction> directions) {
/* 103 */     byte code = 0;
/* 104 */     for (Direction direction : directions)
/* 105 */       code = (byte)(code | 1 << direction.ordinal()); 
/* 107 */     return code;
/*     */   }
/*     */   
/*     */   protected boolean isFaceSupported(Direction faceDirection) {
/* 112 */     return true;
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 117 */     for (Direction direction : DIRECTIONS) {
/* 118 */       if (isFaceSupported(direction))
/* 119 */         builder.add(new Property[] { (Property)getFaceProperty(direction) }); 
/*     */     } 
/* 122 */     builder.add(new Property[] { (Property)WATERLOGGED });
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 128 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 129 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 132 */     if (!hasAnyFace(state))
/* 133 */       return Blocks.AIR.defaultBlockState(); 
/* 136 */     if (!hasFace(state, directionToNeighbour) || canAttachTo((BlockGetter)level, directionToNeighbour, neighbourPos, neighbourState))
/* 137 */       return state; 
/* 139 */     return removeFace(state, getFaceProperty(directionToNeighbour));
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 144 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 145 */       return Fluids.WATER.getSource(false); 
/* 147 */     return super.getFluidState(state);
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 152 */     return this.shapes.apply(state);
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 157 */     boolean hasAtLeastOneFace = false;
/* 158 */     for (Direction directionToNeighbour : DIRECTIONS) {
/* 159 */       if (hasFace(state, directionToNeighbour)) {
/* 162 */         if (!canAttachTo((BlockGetter)level, pos, directionToNeighbour))
/* 163 */           return false; 
/* 165 */         hasAtLeastOneFace = true;
/*     */       } 
/*     */     } 
/* 167 */     return hasAtLeastOneFace;
/*     */   }
/*     */   
/*     */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/* 172 */     return (!context.getItemInHand().is(asItem()) || hasAnyVacantFace(state));
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 177 */     Level level = context.getLevel();
/* 178 */     BlockPos placePos = context.getClickedPos();
/* 179 */     BlockState oldState = level.getBlockState(placePos);
/* 180 */     return Arrays.<Direction>stream(context.getNearestLookingDirections())
/* 181 */       .map(direction -> getStateForPlacement(oldState, (BlockGetter)level, placePos, direction))
/* 182 */       .filter(Objects::nonNull)
/* 183 */       .findFirst()
/* 184 */       .orElse(null);
/*     */   }
/*     */   
/*     */   public boolean isValidStateForPlacement(BlockGetter level, BlockState oldState, BlockPos placementPos, Direction placementDirection) {
/* 188 */     if (!isFaceSupported(placementDirection) || (oldState.is(this) && hasFace(oldState, placementDirection)))
/* 189 */       return false; 
/* 191 */     BlockPos neighbourPos = placementPos.relative(placementDirection);
/* 192 */     return canAttachTo(level, placementDirection, neighbourPos, level.getBlockState(neighbourPos));
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockState oldState, BlockGetter level, BlockPos placementPos, Direction placementDirection) {
/*     */     BlockState newState;
/* 196 */     if (!isValidStateForPlacement(level, oldState, placementPos, placementDirection))
/* 197 */       return null; 
/* 201 */     if (oldState.is(this)) {
/* 203 */       newState = oldState;
/* 204 */     } else if (oldState.getFluidState().isSourceOfType((Fluid)Fluids.WATER)) {
/* 205 */       newState = (BlockState)defaultBlockState().setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
/*     */     } else {
/* 207 */       newState = defaultBlockState();
/*     */     } 
/* 210 */     return (BlockState)newState.setValue((Property)getFaceProperty(placementDirection), Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 215 */     if (!this.canRotate)
/* 216 */       return state; 
/* 219 */     Objects.requireNonNull(rotation);
/* 219 */     return mapDirections(state, rotation::rotate);
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 224 */     if (mirror == Mirror.FRONT_BACK && !this.canMirrorX)
/* 225 */       return state; 
/* 227 */     if (mirror == Mirror.LEFT_RIGHT && !this.canMirrorZ)
/* 228 */       return state; 
/* 231 */     Objects.requireNonNull(mirror);
/* 231 */     return mapDirections(state, mirror::mirror);
/*     */   }
/*     */   
/*     */   private BlockState mapDirections(BlockState state, Function<Direction, Direction> mapping) {
/* 235 */     BlockState newState = state;
/* 236 */     for (Direction direction : DIRECTIONS) {
/* 237 */       if (isFaceSupported(direction))
/* 238 */         newState = (BlockState)newState.setValue((Property)getFaceProperty(mapping.apply(direction)), state.getValue((Property)getFaceProperty(direction))); 
/*     */     } 
/* 241 */     return newState;
/*     */   }
/*     */   
/*     */   public static boolean hasFace(BlockState state, Direction faceDirection) {
/* 245 */     BooleanProperty property = getFaceProperty(faceDirection);
/* 246 */     return ((Boolean)state.getValueOrElse((Property)property, Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */   
/*     */   public static boolean canAttachTo(BlockGetter level, BlockPos pos, Direction directionTowardsNeighbour) {
/* 250 */     BlockPos neighbourPos = pos.relative(directionTowardsNeighbour);
/* 251 */     BlockState blockState = level.getBlockState(neighbourPos);
/* 252 */     return canAttachTo(level, directionTowardsNeighbour, neighbourPos, blockState);
/*     */   }
/*     */   
/*     */   public static boolean canAttachTo(BlockGetter level, Direction directionTowardsNeighbour, BlockPos neighbourPos, BlockState neighbourState) {
/* 256 */     return (Block.isFaceFull(neighbourState.getBlockSupportShape(level, neighbourPos), directionTowardsNeighbour.getOpposite()) || 
/* 257 */       Block.isFaceFull(neighbourState.getCollisionShape(level, neighbourPos), directionTowardsNeighbour.getOpposite()));
/*     */   }
/*     */   
/*     */   private static BlockState removeFace(BlockState state, BooleanProperty property) {
/* 261 */     BlockState newState = (BlockState)state.setValue((Property)property, Boolean.valueOf(false));
/* 262 */     if (hasAnyFace(newState))
/* 263 */       return newState; 
/* 266 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */   
/*     */   public static BooleanProperty getFaceProperty(Direction faceDirection) {
/* 270 */     return PROPERTY_BY_DIRECTION.get(faceDirection);
/*     */   }
/*     */   
/*     */   private static BlockState getDefaultMultifaceState(StateDefinition<Block, BlockState> stateDefinition) {
/* 274 */     BlockState state = (BlockState)((BlockState)stateDefinition.any()).setValue((Property)WATERLOGGED, Boolean.valueOf(false));
/* 275 */     for (BooleanProperty faceProperty : PROPERTY_BY_DIRECTION.values())
/* 276 */       state = (BlockState)state.trySetValue((Property)faceProperty, Boolean.valueOf(false)); 
/* 278 */     return state;
/*     */   }
/*     */   
/*     */   protected static boolean hasAnyFace(BlockState state) {
/* 282 */     for (Direction direction : DIRECTIONS) {
/* 283 */       if (hasFace(state, direction))
/* 284 */         return true; 
/*     */     } 
/* 287 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean hasAnyVacantFace(BlockState state) {
/* 291 */     for (Direction direction : DIRECTIONS) {
/* 292 */       if (!hasFace(state, direction))
/* 293 */         return true; 
/*     */     } 
/* 296 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\MultifaceBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */