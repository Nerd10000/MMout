/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ 
/*      */ class FoxFloatGoal extends FloatGoal {
/*      */   public FoxFloatGoal() {
/* 1385 */     super((Mob)paramFox);
/*      */   }
/*      */   
/*      */   public void start() {
/* 1390 */     super.start();
/* 1391 */     Fox.this.clearStates();
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/* 1396 */     return ((Fox.this.isInWater() && Fox.this.getFluidHeight(FluidTags.WATER) > 0.25D) || Fox.this.isInLava());
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxFloatGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */