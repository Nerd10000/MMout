/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.CalibratedSculkSensorBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*    */ 
/*    */ public class VibrationUser extends SculkSensorBlockEntity.VibrationUser {
/*    */   public VibrationUser(BlockPos blockPos) {
/* 27 */     super(blockPos);
/*    */   }
/*    */   
/*    */   public int getListenerRadius() {
/* 32 */     return 16;
/*    */   }
/*    */   
/*    */   public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
/* 37 */     int comparisonType = getBackSignal((Level)level, this.blockPos, CalibratedSculkSensorBlockEntity.this.getBlockState());
/* 39 */     if (comparisonType != 0 && VibrationSystem.getGameEventFrequency(event) != comparisonType)
/* 40 */       return false; 
/* 43 */     return super.canReceiveVibration(level, pos, event, context);
/*    */   }
/*    */   
/*    */   private int getBackSignal(Level level, BlockPos pos, BlockState state) {
/* 47 */     Direction direction = ((Direction)state.getValue((Property)CalibratedSculkSensorBlock.FACING)).getOpposite();
/* 48 */     return level.getSignal(pos.relative(direction), direction);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\CalibratedSculkSensorBlockEntity$VibrationUser.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */