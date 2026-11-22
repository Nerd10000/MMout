/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SeagrassBlock extends VegetationBlock implements BonemealableBlock, LiquidBlockContainer {
/*  27 */   public static final MapCodec<SeagrassBlock> CODEC = simpleCodec(SeagrassBlock::new);
/*     */   
/*     */   public MapCodec<SeagrassBlock> codec() {
/*  31 */     return CODEC;
/*     */   }
/*     */   
/*  34 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 12.0D);
/*     */   
/*     */   protected SeagrassBlock(BlockBehaviour.Properties properties) {
/*  37 */     super(properties);
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  42 */     return SHAPE;
/*     */   }
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/*  47 */     return (state.isFaceSturdy(level, pos, Direction.UP) && !state.is(BlockTags.CANNOT_SUPPORT_SEAGRASS));
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  52 */     FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  54 */     if (fluidState.is(FluidTags.WATER) && fluidState.isFull())
/*  55 */       return super.getStateForPlacement(context); 
/*  57 */     return null;
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  62 */     BlockState result = super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*  63 */     if (!result.isAir())
/*  64 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/*  66 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/*  71 */     return level.getBlockState(pos.above()).is(Blocks.WATER);
/*     */   }
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/*  76 */     return true;
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  81 */     return Fluids.WATER.getSource(false);
/*     */   }
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/*  86 */     BlockState lowerState = Blocks.TALL_SEAGRASS.defaultBlockState();
/*  87 */     BlockState upperState = (BlockState)lowerState.setValue((Property)TallSeagrassBlock.HALF, (Comparable)DoubleBlockHalf.UPPER);
/*  88 */     BlockPos above = pos.above();
/*  89 */     level.setBlock(pos, lowerState, 2);
/*  90 */     level.setBlock(above, upperState, 2);
/*     */   }
/*     */   
/*     */   public boolean canPlaceLiquid(LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
/*  95 */     return false;
/*     */   }
/*     */   
/*     */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
/* 100 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SeagrassBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */