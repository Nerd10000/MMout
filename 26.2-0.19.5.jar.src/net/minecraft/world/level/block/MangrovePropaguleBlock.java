/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
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
/*     */ import net.minecraft.world.level.block.grower.TreeGrower;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MangrovePropaguleBlock extends SaplingBlock implements SimpleWaterloggedBlock {
/*     */   public static final MapCodec<MangrovePropaguleBlock> CODEC;
/*     */   
/*     */   static {
/*  28 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TreeGrower.CODEC.fieldOf("tree").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, MangrovePropaguleBlock::new));
/*     */   }
/*     */   
/*     */   public MapCodec<MangrovePropaguleBlock> codec() {
/*  35 */     return CODEC;
/*     */   }
/*     */   
/*  38 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
/*     */   
/*     */   public static final int MAX_AGE = 4;
/*     */   
/*  41 */   private static final int[] SHAPE_MIN_Y = new int[] { 13, 10, 7, 3, 0 };
/*     */   
/*     */   private static final VoxelShape[] SHAPE_PER_AGE;
/*     */   
/*     */   static {
/*  42 */     SHAPE_PER_AGE = Block.boxes(4, age -> Block.column(2.0D, SHAPE_MIN_Y[age], 16.0D));
/*     */   }
/*     */   
/*  44 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  45 */   public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
/*     */   
/*     */   public MangrovePropaguleBlock(TreeGrower treeGrower, BlockBehaviour.Properties properties) {
/*  48 */     super(treeGrower, properties);
/*  49 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
/*  50 */         .setValue((Property)STAGE, Integer.valueOf(0)))
/*  51 */         .setValue((Property)AGE, Integer.valueOf(0)))
/*  52 */         .setValue((Property)WATERLOGGED, Boolean.valueOf(false)))
/*  53 */         .setValue((Property)HANGING, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  59 */     builder.add(new Property[] { (Property)STAGE }).add(new Property[] { (Property)AGE }).add(new Property[] { (Property)WATERLOGGED }).add(new Property[] { (Property)HANGING });
/*     */   }
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/*  64 */     return state.is(BlockTags.SUPPORTS_MANGROVE_PROPAGULE);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  69 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  70 */     boolean isWaterSource = replacedFluidState.is(Fluids.WATER);
/*  71 */     return (BlockState)((BlockState)super.getStateForPlacement(context).setValue((Property)WATERLOGGED, Boolean.valueOf(isWaterSource))).setValue((Property)AGE, Integer.valueOf(4));
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  76 */     int age = ((Boolean)state.getValue((Property)HANGING)).booleanValue() ? ((Integer)state.getValue((Property)AGE)).intValue() : 4;
/*  77 */     return SHAPE_PER_AGE[age].move(state.getOffset(pos));
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  82 */     if (isHanging(state))
/*  83 */       return level.getBlockState(pos.above()).is(BlockTags.SUPPORTS_HANGING_MANGROVE_PROPAGULE); 
/*  85 */     return super.canSurvive(state, level, pos);
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  90 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/*  91 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/*  93 */     if (directionToNeighbour == Direction.UP && !state.canSurvive(level, pos))
/*  94 */       return Blocks.AIR.defaultBlockState(); 
/*  96 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 101 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 102 */       return Fluids.WATER.getSource(false); 
/* 104 */     return super.getFluidState(state);
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 109 */     if (!isHanging(state)) {
/* 112 */       if (random.nextInt(7) == 0)
/* 113 */         advanceTree(level, pos, state, random); 
/*     */       return;
/*     */     } 
/* 118 */     if (!isFullyGrown(state))
/* 119 */       level.setBlock(pos, (BlockState)state.cycle((Property)AGE), 2); 
/*     */   }
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 125 */     return (!isHanging(state) || !isFullyGrown(state));
/*     */   }
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 130 */     return isHanging(state) ? (!isFullyGrown(state)) : super.isBonemealSuccess(level, random, pos, state);
/*     */   }
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 135 */     if (isHanging(state) && !isFullyGrown(state)) {
/* 136 */       level.setBlock(pos, (BlockState)state.cycle((Property)AGE), 2);
/*     */     } else {
/* 138 */       super.performBonemeal(level, random, pos, state);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isHanging(BlockState state) {
/* 143 */     return ((Boolean)state.getValue((Property)HANGING)).booleanValue();
/*     */   }
/*     */   
/*     */   private static boolean isFullyGrown(BlockState state) {
/* 147 */     return (((Integer)state.getValue((Property)AGE)).intValue() == 4);
/*     */   }
/*     */   
/*     */   public static BlockState createNewHangingPropagule() {
/* 151 */     return createNewHangingPropagule(0);
/*     */   }
/*     */   
/*     */   public static BlockState createNewHangingPropagule(int age) {
/* 155 */     return (BlockState)((BlockState)Blocks.MANGROVE_PROPAGULE.defaultBlockState()
/* 156 */       .setValue((Property)HANGING, Boolean.valueOf(true)))
/* 157 */       .setValue((Property)AGE, Integer.valueOf(age));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\MangrovePropaguleBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */