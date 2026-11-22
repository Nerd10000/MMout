/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ import net.minecraft.world.level.block.entity.CalibratedSculkSensorBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*    */ 
/*    */ public class CalibratedSculkSensorBlock extends SculkSensorBlock {
/* 23 */   public static final MapCodec<CalibratedSculkSensorBlock> CODEC = simpleCodec(CalibratedSculkSensorBlock::new);
/*    */   
/*    */   public MapCodec<CalibratedSculkSensorBlock> codec() {
/* 27 */     return CODEC;
/*    */   }
/*    */   
/* 30 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*    */   
/*    */   public CalibratedSculkSensorBlock(BlockBehaviour.Properties properties) {
/* 33 */     super(properties);
/* 34 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)Direction.NORTH));
/*    */   }
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 39 */     return (BlockEntity)new CalibratedSculkSensorBlockEntity(worldPosition, blockState);
/*    */   }
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 44 */     if (!level.isClientSide())
/* 45 */       return createTickerHelper(type, BlockEntityTypes.CALIBRATED_SCULK_SENSOR, (innerLevel, pos, state, entity) -> VibrationSystem.Ticker.tick(innerLevel, entity.getVibrationData(), entity.getVibrationUser())); 
/* 48 */     return null;
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 53 */     return (BlockState)super.getStateForPlacement(context).setValue((Property)FACING, (Comparable)context.getHorizontalDirection());
/*    */   }
/*    */   
/*    */   public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 58 */     return (direction != state.getValue((Property)FACING)) ? ownSignal(state, level, pos) : 0;
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 63 */     super.createBlockStateDefinition(builder);
/* 64 */     builder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */   
/*    */   public BlockState rotate(BlockState state, Rotation rotation) {
/* 70 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */   
/*    */   public BlockState mirror(BlockState state, Mirror mirror) {
/* 75 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */   
/*    */   public int getActiveTicks() {
/* 80 */     return 10;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CalibratedSculkSensorBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */