/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
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
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BambooLeaves;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BambooStalkBlock extends Block implements BonemealableBlock {
/*  27 */   public static final MapCodec<BambooStalkBlock> CODEC = simpleCodec(BambooStalkBlock::new);
/*     */   
/*     */   public MapCodec<BambooStalkBlock> codec() {
/*  31 */     return CODEC;
/*     */   }
/*     */   
/*  34 */   private static final VoxelShape SHAPE_SMALL = Block.column(6.0D, 0.0D, 16.0D);
/*     */   
/*  35 */   private static final VoxelShape SHAPE_LARGE = Block.column(10.0D, 0.0D, 16.0D);
/*     */   
/*  36 */   private static final VoxelShape SHAPE_COLLISION = Block.column(3.0D, 0.0D, 16.0D);
/*     */   
/*  38 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
/*     */   
/*  39 */   public static final EnumProperty<BambooLeaves> LEAVES = BlockStateProperties.BAMBOO_LEAVES;
/*     */   
/*  40 */   public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
/*     */   
/*     */   public static final int MAX_HEIGHT = 16;
/*     */   
/*     */   public static final int STAGE_GROWING = 0;
/*     */   
/*     */   public static final int STAGE_DONE_GROWING = 1;
/*     */   
/*     */   public static final int AGE_THIN_BAMBOO = 0;
/*     */   
/*     */   public static final int AGE_THICK_BAMBOO = 1;
/*     */   
/*     */   public BambooStalkBlock(BlockBehaviour.Properties properties) {
/*  49 */     super(properties);
/*  50 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0))).setValue((Property)LEAVES, (Comparable)BambooLeaves.NONE)).setValue((Property)STAGE, Integer.valueOf(0)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  55 */     builder.add(new Property[] { (Property)AGE, (Property)LEAVES, (Property)STAGE });
/*     */   }
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState state) {
/*  60 */     return true;
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  65 */     VoxelShape shape = (state.getValue((Property)LEAVES) == BambooLeaves.LARGE) ? SHAPE_LARGE : SHAPE_SMALL;
/*  66 */     return shape.move(state.getOffset(pos));
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/*  71 */     return false;
/*     */   }
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  76 */     return SHAPE_COLLISION.move(state.getOffset(pos));
/*     */   }
/*     */   
/*     */   protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
/*  81 */     return false;
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  86 */     FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  87 */     if (!fluidState.isEmpty())
/*  88 */       return null; 
/*  91 */     BlockState belowState = context.getLevel().getBlockState(context.getClickedPos().below());
/*  92 */     if (belowState.is(BlockTags.SUPPORTS_BAMBOO)) {
/*  93 */       if (belowState.is(Blocks.BAMBOO_SAPLING))
/*  94 */         return (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(0)); 
/*  95 */       if (belowState.is(Blocks.BAMBOO)) {
/*  96 */         int age = (((Integer)belowState.getValue((Property)AGE)).intValue() > 0) ? 1 : 0;
/*  97 */         return (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(age));
/*     */       } 
/*  99 */       BlockState aboveState = context.getLevel().getBlockState(context.getClickedPos().above());
/* 100 */       if (aboveState.is(Blocks.BAMBOO))
/* 101 */         return (BlockState)defaultBlockState().setValue((Property)AGE, aboveState.getValue((Property)AGE)); 
/* 103 */       return Blocks.BAMBOO_SAPLING.defaultBlockState();
/*     */     } 
/* 108 */     return null;
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 113 */     if (!state.canSurvive((LevelReader)level, pos))
/* 114 */       level.destroyBlock(pos, true); 
/*     */   }
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState state) {
/* 120 */     return (((Integer)state.getValue((Property)STAGE)).intValue() == 0);
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 125 */     if (((Integer)state.getValue((Property)STAGE)).intValue() != 0)
/*     */       return; 
/* 129 */     if (random.nextInt(3) == 0 && level.isEmptyBlock(pos.above()) && level.getRawBrightness(pos.above(), 0) >= 9) {
/* 130 */       int height = getHeightBelowUpToMax((BlockGetter)level, pos) + 1;
/* 131 */       if (height < 16)
/* 132 */         growBamboo(state, (Level)level, pos, random, height); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 139 */     return level.getBlockState(pos.below()).is(BlockTags.SUPPORTS_BAMBOO);
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 144 */     if (!state.canSurvive(level, pos))
/* 145 */       ticks.scheduleTick(pos, this, 1); 
/* 148 */     if (directionToNeighbour == Direction.UP && 
/* 149 */       neighbourState.is(Blocks.BAMBOO) && ((Integer)neighbourState.getValue((Property)AGE)).intValue() > ((Integer)state.getValue((Property)AGE)).intValue())
/* 150 */       return (BlockState)state.cycle((Property)AGE); 
/* 154 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 159 */     int heightAbove = getHeightAboveUpToMax((BlockGetter)level, pos);
/* 160 */     int heightBelow = getHeightBelowUpToMax((BlockGetter)level, pos);
/* 161 */     BlockPos growthPos = pos.above(heightAbove + 1);
/* 162 */     return (heightAbove + heightBelow + 1 < 16 && ((Integer)level
/* 163 */       .getBlockState(pos.above(heightAbove)).getValue((Property)STAGE)).intValue() != 1 && level
/* 164 */       .isInsideBuildHeight(growthPos) && level
/* 165 */       .isEmptyBlock(growthPos));
/*     */   }
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 170 */     return true;
/*     */   }
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 175 */     int heightAbove = getHeightAboveUpToMax((BlockGetter)level, pos);
/* 176 */     int heightBelow = getHeightBelowUpToMax((BlockGetter)level, pos);
/* 177 */     int totalHeight = heightAbove + heightBelow + 1;
/* 179 */     int newBamboo = 1 + random.nextInt(2);
/* 180 */     for (int i = 0; i < newBamboo; i++) {
/* 181 */       BlockPos topPos = pos.above(heightAbove);
/* 182 */       BlockState topState = level.getBlockState(topPos);
/* 183 */       BlockPos growthPos = topPos.above();
/* 184 */       if (totalHeight >= 16 || ((Integer)topState.getValue((Property)STAGE)).intValue() == 1 || !level.isEmptyBlock(growthPos) || level.isOutsideBuildHeight(growthPos))
/*     */         return; 
/* 188 */       growBamboo(topState, (Level)level, topPos, random, totalHeight);
/* 190 */       heightAbove++;
/* 191 */       totalHeight++;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void growBamboo(BlockState state, Level level, BlockPos pos, RandomSource random, int height) {
/* 196 */     BlockState belowState = level.getBlockState(pos.below());
/* 197 */     BlockPos twoBelowPos = pos.below(2);
/* 198 */     BlockState twoBelowState = level.getBlockState(twoBelowPos);
/* 200 */     BambooLeaves leaves = BambooLeaves.NONE;
/* 201 */     if (height >= 1)
/* 202 */       if (!belowState.is(Blocks.BAMBOO) || belowState.getValue((Property)LEAVES) == BambooLeaves.NONE) {
/* 203 */         leaves = BambooLeaves.SMALL;
/* 204 */       } else if (belowState.is(Blocks.BAMBOO) && belowState.getValue((Property)LEAVES) != BambooLeaves.NONE) {
/* 205 */         leaves = BambooLeaves.LARGE;
/* 207 */         if (twoBelowState.is(Blocks.BAMBOO)) {
/* 208 */           level.setBlock(pos.below(), (BlockState)belowState.setValue((Property)LEAVES, (Comparable)BambooLeaves.SMALL), 3);
/* 209 */           level.setBlock(twoBelowPos, (BlockState)twoBelowState.setValue((Property)LEAVES, (Comparable)BambooLeaves.NONE), 3);
/*     */         } 
/*     */       }  
/* 214 */     int age = (((Integer)state.getValue((Property)AGE)).intValue() == 1 || twoBelowState.is(Blocks.BAMBOO)) ? 1 : 0;
/* 215 */     int stage = ((height >= 11 && random.nextFloat() < 0.25F) || height == 15) ? 1 : 0;
/* 216 */     level.setBlock(pos.above(), (BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(age))).setValue((Property)LEAVES, (Comparable)leaves)).setValue((Property)STAGE, Integer.valueOf(stage)), 3);
/*     */   }
/*     */   
/*     */   protected int getHeightAboveUpToMax(BlockGetter level, BlockPos pos) {
/* 220 */     int height = 0;
/* 221 */     while (height < 16 && level.getBlockState(pos.above(height + 1)).is(Blocks.BAMBOO))
/* 222 */       height++; 
/* 224 */     return height;
/*     */   }
/*     */   
/*     */   protected int getHeightBelowUpToMax(BlockGetter level, BlockPos pos) {
/* 228 */     int height = 0;
/* 229 */     while (height < 16 && level.getBlockState(pos.below(height + 1)).is(Blocks.BAMBOO))
/* 230 */       height++; 
/* 232 */     return height;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BambooStalkBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */