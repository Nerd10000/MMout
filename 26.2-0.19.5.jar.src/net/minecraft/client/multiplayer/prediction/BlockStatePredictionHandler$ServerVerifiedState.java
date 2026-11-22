/*    */ package net.minecraft.client.multiplayer.prediction;
/*    */ 
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ class ServerVerifiedState {
/*    */   private final Vec3 playerPos;
/*    */   
/*    */   private int sequence;
/*    */   
/*    */   private BlockState blockState;
/*    */   
/*    */   private ServerVerifiedState(int sequence, BlockState blockState, Vec3 playerPos) {
/* 79 */     this.sequence = sequence;
/* 80 */     this.blockState = blockState;
/* 81 */     this.playerPos = playerPos;
/*    */   }
/*    */   
/*    */   private ServerVerifiedState setSequence(int sequence) {
/* 85 */     this.sequence = sequence;
/* 86 */     return this;
/*    */   }
/*    */   
/*    */   private void setBlockState(BlockState blockState) {
/* 90 */     this.blockState = blockState;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\prediction\BlockStatePredictionHandler$ServerVerifiedState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */