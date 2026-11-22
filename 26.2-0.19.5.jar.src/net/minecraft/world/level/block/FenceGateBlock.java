/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FenceGateBlock extends HorizontalDirectionalBlock {
/*     */   public static final MapCodec<FenceGateBlock> CODEC;
/*     */   
/*     */   static {
/*  41 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WoodType.CODEC.fieldOf("wood_type").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, FenceGateBlock::new));
/*     */   }
/*     */   
/*     */   public MapCodec<FenceGateBlock> codec() {
/*  48 */     return CODEC;
/*     */   }
/*     */   
/*  51 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*     */   
/*  52 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*  53 */   public static final BooleanProperty IN_WALL = BlockStateProperties.IN_WALL;
/*     */   
/*  55 */   private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Block.cube(16.0D, 16.0D, 4.0D));
/*     */   
/*     */   private static final Map<Direction.Axis, VoxelShape> SHAPES_WALL;
/*     */   
/*     */   static {
/*  56 */     SHAPES_WALL = Maps.newEnumMap(Util.mapValues(SHAPES, v -> Shapes.join(v, Block.column(16.0D, 13.0D, 16.0D), BooleanOp.ONLY_FIRST)));
/*     */   }
/*     */   
/*  58 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_COLLISION = Shapes.rotateHorizontalAxis(Block.column(16.0D, 4.0D, 0.0D, 24.0D));
/*     */   
/*  59 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_SUPPORT = Shapes.rotateHorizontalAxis(Block.column(16.0D, 4.0D, 5.0D, 24.0D));
/*     */   
/*  61 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_OCCLUSION = Shapes.rotateHorizontalAxis(Shapes.or(
/*  62 */         Block.box(0.0D, 5.0D, 7.0D, 2.0D, 16.0D, 9.0D), 
/*  63 */         Block.box(14.0D, 5.0D, 7.0D, 16.0D, 16.0D, 9.0D)));
/*     */   
/*     */   private static final Map<Direction.Axis, VoxelShape> SHAPE_OCCLUSION_WALL;
/*     */   
/*     */   private final WoodType type;
/*     */   
/*     */   static {
/*  66 */     SHAPE_OCCLUSION_WALL = Maps.newEnumMap(Util.mapValues(SHAPE_OCCLUSION, v -> v.move(0.0D, -0.1875D, 0.0D).optimize()));
/*     */   }
/*     */   
/*     */   public FenceGateBlock(WoodType type, BlockBehaviour.Properties properties) {
/*  71 */     super(properties.sound(type.soundType()));
/*  72 */     this.type = type;
/*  74 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)OPEN, Boolean.valueOf(false))).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)IN_WALL, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  79 */     Direction.Axis axis = ((Direction)state.getValue((Property)FACING)).getAxis();
/*  80 */     return (((Boolean)state.getValue((Property)IN_WALL)).booleanValue() ? SHAPES_WALL : SHAPES).get(axis);
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  85 */     Direction.Axis axis = directionToNeighbour.getAxis();
/*  86 */     if (((Direction)state.getValue((Property)FACING)).getClockWise().getAxis() == axis) {
/*  87 */       boolean inWall = (isWall(neighbourState) || isWall(level.getBlockState(pos.relative(directionToNeighbour.getOpposite()))));
/*  88 */       return (BlockState)state.setValue((Property)IN_WALL, Boolean.valueOf(inWall));
/*     */     } 
/*  91 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/*  96 */     Direction.Axis axis = ((Direction)state.getValue((Property)FACING)).getAxis();
/*  98 */     return ((Boolean)state.getValue((Property)OPEN)).booleanValue() ? Shapes.empty() : SHAPE_SUPPORT.get(axis);
/*     */   }
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 103 */     Direction.Axis axis = ((Direction)state.getValue((Property)FACING)).getAxis();
/* 104 */     return ((Boolean)state.getValue((Property)OPEN)).booleanValue() ? Shapes.empty() : SHAPE_COLLISION.get(axis);
/*     */   }
/*     */   
/*     */   protected VoxelShape getOcclusionShape(BlockState state) {
/* 109 */     Direction.Axis axis = ((Direction)state.getValue((Property)FACING)).getAxis();
/* 110 */     return (((Boolean)state.getValue((Property)IN_WALL)).booleanValue() ? SHAPE_OCCLUSION_WALL : SHAPE_OCCLUSION).get(axis);
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 115 */     switch (type) {
/*     */       case LAND:
/*     */       
/*     */       case WATER:
/*     */       
/*     */       case AIR:
/*     */       
/*     */     } 
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 125 */     Level level = context.getLevel();
/* 126 */     BlockPos pos = context.getClickedPos();
/* 128 */     boolean isOpen = level.hasNeighborSignal(pos);
/* 129 */     Direction direction = context.getHorizontalDirection();
/* 131 */     Direction.Axis axis = direction.getAxis();
/* 133 */     boolean inWall = ((axis == Direction.Axis.Z && (isWall(level.getBlockState(pos.west())) || isWall(level.getBlockState(pos.east())))) || (axis == Direction.Axis.X && (isWall(level.getBlockState(pos.north())) || isWall(level.getBlockState(pos.south())))));
/* 134 */     return (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)direction)).setValue((Property)OPEN, Boolean.valueOf(isOpen))).setValue((Property)POWERED, Boolean.valueOf(isOpen))).setValue((Property)IN_WALL, Boolean.valueOf(inWall));
/*     */   }
/*     */   
/*     */   private boolean isWall(BlockState state) {
/* 138 */     return state.is(BlockTags.WALLS);
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 143 */     if (((Boolean)state.getValue((Property)OPEN)).booleanValue()) {
/* 144 */       state = (BlockState)state.setValue((Property)OPEN, Boolean.valueOf(false));
/* 145 */       level.setBlock(pos, state, 10);
/*     */     } else {
/* 148 */       Direction direction = player.getDirection();
/* 149 */       if (state.getValue((Property)FACING) == direction.getOpposite())
/* 150 */         state = (BlockState)state.setValue((Property)FACING, (Comparable)direction); 
/* 152 */       state = (BlockState)state.setValue((Property)OPEN, Boolean.valueOf(true));
/* 153 */       level.setBlock(pos, state, 10);
/*     */     } 
/* 156 */     boolean opens = ((Boolean)state.getValue((Property)OPEN)).booleanValue();
/* 158 */     level.playSound((Entity)player, pos, opens ? this.type.fenceGateOpen() : this.type.fenceGateClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
/* 159 */     level.gameEvent((Entity)player, opens ? (Holder)GameEvent.BLOCK_OPEN : (Holder)GameEvent.BLOCK_CLOSE, pos);
/* 160 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 165 */     if (explosion.canTriggerBlocks() && !((Boolean)state.getValue((Property)POWERED)).booleanValue()) {
/* 166 */       boolean open = ((Boolean)state.getValue((Property)OPEN)).booleanValue();
/* 167 */       level.setBlockAndUpdate(pos, (BlockState)state.setValue((Property)OPEN, Boolean.valueOf(!open)));
/* 169 */       level.playSound(null, pos, open ? this.type.fenceGateClose() : this.type.fenceGateOpen(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
/* 170 */       level.gameEvent(open ? (Holder)GameEvent.BLOCK_CLOSE : (Holder)GameEvent.BLOCK_OPEN, pos, GameEvent.Context.of(state));
/*     */     } 
/* 172 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 177 */     if (level.isClientSide())
/*     */       return; 
/* 181 */     boolean hasPower = level.hasNeighborSignal(pos);
/* 182 */     if (((Boolean)state.getValue((Property)POWERED)).booleanValue() != hasPower) {
/* 183 */       level.setBlock(pos, (BlockState)((BlockState)state.setValue((Property)POWERED, Boolean.valueOf(hasPower))).setValue((Property)OPEN, Boolean.valueOf(hasPower)), 2);
/* 184 */       if (((Boolean)state.getValue((Property)OPEN)).booleanValue() != hasPower) {
/* 185 */         level.playSound(null, pos, hasPower ? this.type.fenceGateOpen() : this.type.fenceGateClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
/* 186 */         level.gameEvent(null, hasPower ? (Holder)GameEvent.BLOCK_OPEN : (Holder)GameEvent.BLOCK_CLOSE, pos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 193 */     builder.add(new Property[] { (Property)FACING, (Property)OPEN, (Property)POWERED, (Property)IN_WALL });
/*     */   }
/*     */   
/*     */   public static boolean connectsToDirection(BlockState state, Direction direction) {
/* 197 */     return (((Direction)state.getValue((Property)FACING)).getAxis() == direction.getClockWise().getAxis());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\FenceGateBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */