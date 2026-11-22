/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ 
/*    */ class PointCharge {
/*    */   private final BlockPos pos;
/*    */   
/*    */   private final double charge;
/*    */   
/*    */   public PointCharge(BlockPos pos, double charge) {
/* 15 */     this.pos = pos;
/* 16 */     this.charge = charge;
/*    */   }
/*    */   
/*    */   public double getPotentialChange(BlockPos pos) {
/* 20 */     double distSqr = this.pos.distSqr((Vec3i)pos);
/* 21 */     if (distSqr == 0.0D)
/* 23 */       return Double.POSITIVE_INFINITY; 
/* 25 */     return this.charge / Math.sqrt(distSqr);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\PotentialCalculator$PointCharge.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */