/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class BaseRailBlock extends Block implements SimpleWaterloggedBlock {
/*  27 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  29 */   private static final VoxelShape SHAPE_FLAT = Block.column(16.0D, 0.0D, 2.0D);
/*     */   
/*  30 */   private static final VoxelShape SHAPE_SLOPE = Block.column(16.0D, 0.0D, 8.0D);
/*     */   
/*     */   private final boolean isStraight;
/*     */   
/*     */   public static boolean isRail(Level level, BlockPos pos) {
/*  35 */     return isRail(level.getBlockState(pos));
/*     */   }
/*     */   
/*     */   public static boolean isRail(BlockState state) {
/*  39 */     return (state.is(BlockTags.RAILS) && state.getBlock() instanceof BaseRailBlock);
/*     */   }
/*     */   
/*     */   protected BaseRailBlock(boolean isStraight, BlockBehaviour.Properties properties) {
/*  43 */     super(properties);
/*  44 */     this.isStraight = isStraight;
/*     */   }
/*     */   
/*     */   protected abstract MapCodec<? extends BaseRailBlock> codec();
/*     */   
/*     */   public boolean isStraight() {
/*  51 */     return this.isStraight;
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  56 */     return ((RailShape)state.getValue(getShapeProperty())).isSlope() ? SHAPE_SLOPE : SHAPE_FLAT;
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  61 */     return canSupportRigidBlock((BlockGetter)level, pos.below());
/*     */   }
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/*  66 */     if (oldState.is(state.getBlock()))
/*     */       return; 
/*  69 */     updateState(state, level, pos, movedByPiston);
/*     */   }
/*     */   
/*     */   protected BlockState updateState(BlockState state, Level level, BlockPos pos, boolean movedByPiston) {
/*  73 */     state = updateDir(level, pos, state, true);
/*  75 */     if (this.isStraight)
/*  76 */       level.neighborChanged(state, pos, this, null, movedByPiston); 
/*  79 */     return state;
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  84 */     if (level.isClientSide() || !level.getBlockState(pos).is(this))
/*     */       return; 
/*  88 */     RailShape shape = (RailShape)state.getValue(getShapeProperty());
/*  90 */     if (shouldBeRemoved(pos, level, shape)) {
/*  91 */       dropResources(state, level, pos);
/*  92 */       level.removeBlock(pos, movedByPiston);
/*     */     } else {
/*  94 */       updateState(state, level, pos, block);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean shouldBeRemoved(BlockPos pos, Level level, RailShape shape) {
/*  99 */     if (!canSupportRigidBlock((BlockGetter)level, pos.below()))
/* 100 */       return true; 
/* 102 */     switch (shape) {
/*     */       case LEFT_RIGHT:
/* 102 */         return 
/* 103 */           !canSupportRigidBlock((BlockGetter)level, pos.east());
/*     */       case FRONT_BACK:
/* 104 */         return !canSupportRigidBlock((BlockGetter)level, pos.west());
/*     */       case null:
/* 105 */         return !canSupportRigidBlock((BlockGetter)level, pos.north());
/*     */       case null:
/* 106 */         return !canSupportRigidBlock((BlockGetter)level, pos.south());
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   protected void updateState(BlockState state, Level level, BlockPos pos, Block block) {}
/*     */   
/*     */   protected BlockState updateDir(Level level, BlockPos pos, BlockState state, boolean first) {
/* 115 */     if (level.isClientSide())
/* 116 */       return state; 
/* 118 */     RailShape current = (RailShape)state.getValue(getShapeProperty());
/* 119 */     return (new RailState(level, pos, state)).place(level.hasNeighborSignal(pos), first, current).getState();
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 124 */     if (movedByPiston)
/*     */       return; 
/* 128 */     if (((RailShape)state.getValue(getShapeProperty())).isSlope())
/* 129 */       level.updateNeighborsAt(pos.above(), this); 
/* 132 */     if (this.isStraight) {
/* 133 */       level.updateNeighborsAt(pos, this);
/* 134 */       level.updateNeighborsAt(pos.below(), this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 140 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 141 */     boolean isWaterSource = replacedFluidState.is(Fluids.WATER);
/* 142 */     BlockState state = defaultBlockState();
/* 143 */     Direction direction = context.getHorizontalDirection();
/* 144 */     boolean isEastWest = (direction == Direction.EAST || direction == Direction.WEST);
/* 145 */     return (BlockState)((BlockState)state.setValue(getShapeProperty(), isEastWest ? (Comparable)RailShape.EAST_WEST : (Comparable)RailShape.NORTH_SOUTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(isWaterSource));
/*     */   }
/*     */   
/*     */   public abstract Property<RailShape> getShapeProperty();
/*     */   
/*     */   protected RailShape rotate(RailShape shape, Rotation rotation) {
/* 151 */     switch (rotation) {
/*     */       case LEFT_RIGHT:
/* 152 */         switch (shape) {
/*     */           default:
/* 152 */             throw new MatchException(null, null);
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case LEFT_RIGHT:
/*     */           
/*     */           case FRONT_BACK:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */             break;
/*     */         } 
/*     */       case FRONT_BACK:
/* 164 */         switch (shape) {
/*     */           default:
/* 164 */             throw new MatchException(null, null);
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case LEFT_RIGHT:
/*     */           
/*     */           case FRONT_BACK:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */             break;
/*     */         } 
/*     */       case null:
/* 176 */         switch (shape) {
/*     */           default:
/* 176 */             throw new MatchException(null, null);
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case LEFT_RIGHT:
/*     */           
/*     */           case FRONT_BACK:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */             break;
/*     */         } 
/*     */     } 
/* 188 */     return shape;
/*     */   }
/*     */   
/*     */   protected RailShape mirror(RailShape shape, Mirror mirror) {
/* 193 */     switch (mirror) {
/*     */       case LEFT_RIGHT:
/* 194 */         switch (shape) {
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */         } 
/*     */       case FRONT_BACK:
/* 203 */         switch (shape) {
/*     */           case LEFT_RIGHT:
/*     */           
/*     */           case FRONT_BACK:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */         } 
/*     */     } 
/* 212 */     return shape;
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 218 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 219 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 221 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 226 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 227 */       return Fluids.WATER.getSource(false); 
/* 229 */     return super.getFluidState(state);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BaseRailBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */