/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BellBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BellAttachType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BellBlock extends BaseEntityBlock {
/*  46 */   public static final MapCodec<BellBlock> CODEC = simpleCodec(BellBlock::new);
/*     */   
/*     */   public MapCodec<BellBlock> codec() {
/*  50 */     return CODEC;
/*     */   }
/*     */   
/*  53 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  54 */   public static final EnumProperty<BellAttachType> ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
/*     */   
/*  55 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*  57 */   private static final VoxelShape BELL_SHAPE = Shapes.or(
/*  58 */       Block.column(6.0D, 6.0D, 13.0D), 
/*  59 */       Block.column(8.0D, 4.0D, 6.0D));
/*     */   
/*  62 */   private static final VoxelShape SHAPE_CEILING = Shapes.or(BELL_SHAPE, Block.column(2.0D, 13.0D, 16.0D));
/*     */   
/*  63 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_FLOOR = Shapes.rotateHorizontalAxis(Block.cube(16.0D, 16.0D, 8.0D));
/*     */   
/*  64 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_DOUBLE_WALL = Shapes.rotateHorizontalAxis(Shapes.or(BELL_SHAPE, Block.column(2.0D, 16.0D, 13.0D, 15.0D)));
/*     */   
/*  65 */   private static final Map<Direction, VoxelShape> SHAPE_SINGLE_WALL = Shapes.rotateHorizontal(Shapes.or(BELL_SHAPE, Block.boxZ(2.0D, 13.0D, 15.0D, 0.0D, 13.0D)));
/*     */   
/*     */   public static final int EVENT_BELL_RING = 1;
/*     */   
/*     */   public BellBlock(BlockBehaviour.Properties properties) {
/*  70 */     super(properties);
/*  71 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)ATTACHMENT, (Comparable)BellAttachType.FLOOR)).setValue((Property)POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  76 */     boolean signal = level.hasNeighborSignal(pos);
/*  78 */     if (signal != ((Boolean)state.getValue((Property)POWERED)).booleanValue()) {
/*  79 */       if (signal)
/*  80 */         attemptToRing(level, pos, (Direction)null); 
/*  82 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, Boolean.valueOf(signal)), 3);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult hitResult, Projectile projectile) {
/*  88 */     Entity owner = projectile.getOwner();
/*  89 */     Player player = (Player)owner, playerOwner = (owner instanceof Player) ? player : null;
/*  90 */     onHit(level, state, hitResult, playerOwner, true);
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  95 */     return onHit(level, state, hitResult, player, true) ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public boolean onHit(Level level, BlockState state, BlockHitResult hitResult, Player player, boolean requireHitFromCorrectSide) {
/*  99 */     Direction direction = hitResult.getDirection();
/* 100 */     BlockPos blockPos = hitResult.getBlockPos();
/* 101 */     boolean properHit = (!requireHitFromCorrectSide || isProperHit(state, direction, (hitResult.getLocation()).y - blockPos.getY()));
/* 102 */     if (properHit) {
/* 103 */       boolean didRing = attemptToRing((Entity)player, level, blockPos, direction);
/* 104 */       if (didRing && player != null)
/* 105 */         player.awardStat(Stats.BELL_RING); 
/* 107 */       return true;
/*     */     } 
/* 109 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isProperHit(BlockState state, Direction clickedDirection, double clickY) {
/* 113 */     if (clickedDirection.getAxis() == Direction.Axis.Y || clickY > 0.8123999834060669D)
/* 114 */       return false; 
/* 117 */     Direction facing = (Direction)state.getValue((Property)FACING);
/* 118 */     BellAttachType attachType = (BellAttachType)state.getValue((Property)ATTACHMENT);
/* 120 */     switch (attachType) {
/*     */       case FLOOR:
/* 120 */         return 
/* 121 */           (facing.getAxis() == clickedDirection.getAxis());
/*     */       case SINGLE_WALL:
/*     */       case DOUBLE_WALL:
/* 122 */         return (facing.getAxis() != clickedDirection.getAxis());
/*     */       case CEILING:
/*     */       
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   public boolean attemptToRing(Level level, BlockPos pos, Direction direction) {
/* 129 */     return attemptToRing((Entity)null, level, pos, direction);
/*     */   }
/*     */   
/*     */   public boolean attemptToRing(Entity ringingEntity, Level level, BlockPos pos, Direction direction) {
/* 133 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 134 */     if (!level.isClientSide() && blockEntity instanceof BellBlockEntity) {
/* 134 */       BellBlockEntity bellBlockEntity = (BellBlockEntity)blockEntity;
/* 135 */       if (direction == null)
/* 136 */         direction = (Direction)level.getBlockState(pos).getValue((Property)FACING); 
/* 138 */       bellBlockEntity.onHit(direction);
/* 139 */       level.playSound(null, pos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 1.0F);
/* 140 */       level.gameEvent(ringingEntity, (Holder)GameEvent.BLOCK_CHANGE, pos);
/* 141 */       return true;
/*     */     } 
/* 143 */     return false;
/*     */   }
/*     */   
/*     */   private VoxelShape getVoxelShape(BlockState state) {
/* 147 */     Direction facing = (Direction)state.getValue((Property)FACING);
/* 148 */     switch ((BellAttachType)state.getValue((Property)ATTACHMENT)) {
/*     */       default:
/* 148 */         throw new MatchException(null, null);
/*     */       case FLOOR:
/*     */       
/*     */       case CEILING:
/*     */       
/*     */       case SINGLE_WALL:
/*     */       
/*     */       case DOUBLE_WALL:
/*     */         break;
/*     */     } 
/* 148 */     return 
/*     */       
/* 152 */       SHAPE_DOUBLE_WALL.get(facing.getAxis());
/*     */   }
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 158 */     return getVoxelShape(state);
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 163 */     return getVoxelShape(state);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 169 */     Direction clickedFace = context.getClickedFace();
/* 170 */     BlockPos pos = context.getClickedPos();
/* 171 */     Level level = context.getLevel();
/* 172 */     Direction.Axis axis = clickedFace.getAxis();
/* 174 */     if (axis == Direction.Axis.Y) {
/* 175 */       BlockState state = (BlockState)((BlockState)defaultBlockState().setValue((Property)ATTACHMENT, (clickedFace == Direction.DOWN) ? (Comparable)BellAttachType.CEILING : (Comparable)BellAttachType.FLOOR)).setValue((Property)FACING, (Comparable)context.getHorizontalDirection());
/* 177 */       if (state.canSurvive((LevelReader)context.getLevel(), pos))
/* 178 */         return state; 
/*     */     } else {
/* 183 */       boolean doubleAttached = ((axis == Direction.Axis.X && level.getBlockState(pos.west()).isFaceSturdy((BlockGetter)level, pos.west(), Direction.EAST) && level.getBlockState(pos.east()).isFaceSturdy((BlockGetter)level, pos.east(), Direction.WEST)) || (axis == Direction.Axis.Z && level.getBlockState(pos.north()).isFaceSturdy((BlockGetter)level, pos.north(), Direction.SOUTH) && level.getBlockState(pos.south()).isFaceSturdy((BlockGetter)level, pos.south(), Direction.NORTH)));
/* 185 */       BlockState state = (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)clickedFace.getOpposite())).setValue((Property)ATTACHMENT, doubleAttached ? (Comparable)BellAttachType.DOUBLE_WALL : (Comparable)BellAttachType.SINGLE_WALL);
/* 187 */       if (state.canSurvive((LevelReader)context.getLevel(), context.getClickedPos()))
/* 188 */         return state; 
/* 190 */       boolean canAttachBelow = level.getBlockState(pos.below()).isFaceSturdy((BlockGetter)level, pos.below(), Direction.UP);
/* 192 */       state = (BlockState)state.setValue((Property)ATTACHMENT, canAttachBelow ? (Comparable)BellAttachType.FLOOR : (Comparable)BellAttachType.CEILING);
/* 194 */       if (state.canSurvive((LevelReader)context.getLevel(), context.getClickedPos()))
/* 195 */         return state; 
/*     */     } 
/* 200 */     return null;
/*     */   }
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 205 */     if (explosion.canTriggerBlocks())
/* 206 */       attemptToRing((Level)level, pos, (Direction)null); 
/* 208 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 213 */     BellAttachType attachment = (BellAttachType)state.getValue((Property)ATTACHMENT);
/* 215 */     Direction connectedDirection = getConnectedDirection(state).getOpposite();
/* 216 */     if (connectedDirection == directionToNeighbour && !state.canSurvive(level, pos) && attachment != BellAttachType.DOUBLE_WALL)
/* 217 */       return Blocks.AIR.defaultBlockState(); 
/* 220 */     if (directionToNeighbour.getAxis() == ((Direction)state.getValue((Property)FACING)).getAxis()) {
/* 221 */       if (attachment == BellAttachType.DOUBLE_WALL && !neighbourState.isFaceSturdy((BlockGetter)level, neighbourPos, directionToNeighbour))
/* 222 */         return (BlockState)((BlockState)state.setValue((Property)ATTACHMENT, (Comparable)BellAttachType.SINGLE_WALL)).setValue((Property)FACING, (Comparable)directionToNeighbour.getOpposite()); 
/* 223 */       if (attachment == BellAttachType.SINGLE_WALL && connectedDirection.getOpposite() == directionToNeighbour && neighbourState.isFaceSturdy((BlockGetter)level, neighbourPos, (Direction)state.getValue((Property)FACING)))
/* 224 */         return (BlockState)state.setValue((Property)ATTACHMENT, (Comparable)BellAttachType.DOUBLE_WALL); 
/*     */     } 
/* 228 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 233 */     Direction connectionDir = getConnectedDirection(state).getOpposite();
/* 235 */     if (connectionDir == Direction.UP)
/* 236 */       return Block.canSupportCenter(level, pos.above(), Direction.DOWN); 
/* 238 */     return FaceAttachedHorizontalDirectionalBlock.canAttach(level, pos, connectionDir);
/*     */   }
/*     */   
/*     */   private static Direction getConnectedDirection(BlockState state) {
/* 243 */     switch ((BellAttachType)state.getValue((Property)ATTACHMENT)) {
/*     */       case CEILING:
/*     */       
/*     */       case FLOOR:
/*     */       
/*     */     } 
/* 243 */     return (
/*     */       
/* 246 */       (Direction)state.getValue((Property)FACING)).getOpposite();
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 252 */     builder.add(new Property[] { (Property)FACING, (Property)ATTACHMENT, (Property)POWERED });
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 257 */     return (BlockEntity)new BellBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 262 */     return createTickerHelper(type, BlockEntityTypes.BELL, level.isClientSide() ? BellBlockEntity::clientTick : BellBlockEntity::serverTick);
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 267 */     return false;
/*     */   }
/*     */   
/*     */   public BlockState rotate(BlockState state, Rotation rotation) {
/* 272 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   public BlockState mirror(BlockState state, Mirror mirror) {
/* 277 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BellBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */