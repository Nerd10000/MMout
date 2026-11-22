/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*      */ 
/*      */ class FoxPanicGoal extends PanicGoal {
/*      */   public FoxPanicGoal(double speedModifier) {
/* 1348 */     super((PathfinderMob)paramFox, speedModifier);
/*      */   }
/*      */   
/*      */   public boolean shouldPanic() {
/* 1353 */     return (!Fox.this.isDefending() && super.shouldPanic());
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxPanicGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */