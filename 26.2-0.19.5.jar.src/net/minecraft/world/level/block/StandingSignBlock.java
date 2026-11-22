/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*    */ import net.minecraft.world.level.block.state.properties.WoodType;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class StandingSignBlock extends SignBlock implements PlainSignBlock {
/*    */   public static final MapCodec<StandingSignBlock> CODEC;
/*    */   
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WoodType.CODEC.fieldOf("wood_type").forGetter(SignBlock::type), (App)propertiesCodec()).apply((Applicative)i, StandingSignBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<StandingSignBlock> codec() {
/* 28 */     return CODEC;
/*    */   }
/*    */   
/* 31 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*    */   
/*    */   public StandingSignBlock(WoodType type, BlockBehaviour.Properties properties) {
/* 34 */     super(type, properties.sound(type.soundType()));
/* 35 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ROTATION, Integer.valueOf(8))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 40 */     return level.getBlockState(pos.below()).isSolid();
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 45 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 46 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)ROTATION, Integer.valueOf(RotationSegment.convertToSegment(context.getRotation() + 180.0F)))).setValue((Property)WATERLOGGED, Boolean.valueOf(replacedFluidState.is(Fluids.WATER)));
/*    */   }
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 51 */     if (directionToNeighbour == Direction.DOWN && !canSurvive(state, level, pos))
/* 52 */       return Blocks.AIR.defaultBlockState(); 
/* 54 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */   
/*    */   public float getYRotationDegrees(BlockState state) {
/* 59 */     return RotationSegment.convertToDegrees(((Integer)state.getValue((Property)ROTATION)).intValue());
/*    */   }
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 64 */     return (BlockState)state.setValue((Property)ROTATION, Integer.valueOf(rotation.rotate(((Integer)state.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */   
/*    */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 69 */     return (BlockState)state.setValue((Property)ROTATION, Integer.valueOf(mirror.mirror(((Integer)state.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 74 */     builder.add(new Property[] { (Property)ROTATION, (Property)WATERLOGGED });
/*    */   }
/*    */   
/*    */   public PlainSignBlock.Attachment attachmentPoint(BlockState state) {
/* 79 */     return PlainSignBlock.Attachment.GROUND;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\StandingSignBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */