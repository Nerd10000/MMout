/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ public interface PlayerRideableJumping extends PlayerRideable {
/*    */   void onPlayerJump(int paramInt);
/*    */   
/*    */   boolean canJump();
/*    */   
/*    */   void handleStartJump(int paramInt);
/*    */   
/*    */   void handleStopJump();
/*    */   
/*    */   default int getJumpCooldown() {
/* 13 */     return 0;
/*    */   }
/*    */   
/*    */   default float getPlayerJumpPendingScale(int jumpAmount) {
/* 17 */     return (jumpAmount >= 90) ? 1.0F : (0.4F + 0.4F * jumpAmount / 90.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\PlayerRideableJumping.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */