/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ 
/*    */ class TrackIteration {
/* 56 */   public double movementLeft = 0.0D;
/*    */   
/*    */   public boolean firstIteration = true;
/*    */   
/*    */   public boolean hasGainedSlopeSpeed = false;
/*    */   
/*    */   public boolean hasHalted = false;
/*    */   
/*    */   public boolean hasBoosted = false;
/*    */   
/*    */   public boolean shouldIterate() {
/* 63 */     return (this.firstIteration || this.movementLeft > 9.999999747378752E-6D);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\minecart\NewMinecartBehavior$TrackIteration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */