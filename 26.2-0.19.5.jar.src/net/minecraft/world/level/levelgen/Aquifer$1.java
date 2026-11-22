/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ class null implements Aquifer {
/*    */   public BlockState computeSubstance(DensityFunction.FunctionContext context, double density) {
/* 56 */     if (density > 0.0D)
/* 57 */       return null; 
/* 59 */     return fluidRule.computeFluid(context.blockX(), context.blockY(), context.blockZ()).at(context.blockY());
/*    */   }
/*    */   
/*    */   public boolean shouldScheduleFluidUpdate() {
/* 64 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\Aquifer$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */