/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TallSeagrassBlock extends DoublePlantBlock implements LiquidBlockContainer {
/* 25 */   public static final MapCodec<TallSeagrassBlock> CODEC = simpleCodec(TallSeagrassBlock::new);
/*    */   
/*    */   public MapCodec<TallSeagrassBlock> codec() {
/* 29 */     return CODEC;
/*    */   }
/*    */   
/* 32 */   public static final EnumProperty<DoubleBlockHalf> HALF = DoublePlantBlock.HALF;
/*    */   
/* 34 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 16.0D);
/*    */   
/*    */   public TallSeagrassBlock(BlockBehaviour.Properties properties) {
/* 37 */     super(properties);
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 42 */     return SHAPE;
/*    */   }
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 47 */     return (state.isFaceSturdy(level, pos, Direction.UP) && !state.is(BlockTags.CANNOT_SUPPORT_SEAGRASS));
/*    */   }
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 52 */     return new ItemStack(Blocks.SEAGRASS);
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 57 */     BlockState state = super.getStateForPlacement(context);
/* 59 */     if (state != null) {
/* 60 */       FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos().above());
/* 61 */       if (fluidState.is(FluidTags.WATER) && fluidState.isFull())
/* 62 */         return state; 
/*    */     } 
/* 66 */     return null;
/*    */   }
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 71 */     if (state.getValue((Property)HALF) == DoubleBlockHalf.UPPER) {
/* 72 */       BlockState belowState = level.getBlockState(pos.below());
/* 73 */       return (belowState.is(this) && belowState.getValue((Property)HALF) == DoubleBlockHalf.LOWER);
/*    */     } 
/* 76 */     FluidState fluidState = level.getFluidState(pos);
/* 77 */     return (super.canSurvive(state, level, pos) && fluidState.is(FluidTags.WATER) && fluidState.isFull());
/*    */   }
/*    */   
/*    */   protected FluidState getFluidState(BlockState state) {
/* 82 */     return Fluids.WATER.getSource(false);
/*    */   }
/*    */   
/*    */   public boolean canPlaceLiquid(LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
/* 87 */     return false;
/*    */   }
/*    */   
/*    */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
/* 92 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\TallSeagrassBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */