/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SnowyBlock extends Block {
/* 17 */   public static final MapCodec<SnowyBlock> CODEC = simpleCodec(SnowyBlock::new);
/*    */   
/*    */   protected MapCodec<? extends SnowyBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */   
/* 24 */   public static final BooleanProperty SNOWY = BlockStateProperties.SNOWY;
/*    */   
/*    */   protected SnowyBlock(BlockBehaviour.Properties properties) {
/* 27 */     super(properties);
/* 28 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)SNOWY, Boolean.valueOf(false)));
/*    */   }
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 33 */     if (directionToNeighbour == Direction.UP)
/* 34 */       return (BlockState)state.setValue((Property)SNOWY, Boolean.valueOf(isSnowySetting(neighbourState))); 
/* 36 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 41 */     BlockState aboveState = context.getLevel().getBlockState(context.getClickedPos().above());
/* 42 */     return (BlockState)defaultBlockState().setValue((Property)SNOWY, Boolean.valueOf(isSnowySetting(aboveState)));
/*    */   }
/*    */   
/*    */   protected static boolean isSnowySetting(BlockState aboveState) {
/* 46 */     return aboveState.is(BlockTags.SNOW);
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 51 */     builder.add(new Property[] { (Property)SNOWY });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SnowyBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */