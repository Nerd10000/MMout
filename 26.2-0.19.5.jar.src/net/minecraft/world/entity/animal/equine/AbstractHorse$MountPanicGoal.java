/*      */ package net.minecraft.world.entity.animal.equine;
/*      */ 
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*      */ 
/*      */ class MountPanicGoal extends PanicGoal {
/*      */   public MountPanicGoal(double speedModifier) {
/* 1085 */     super((PathfinderMob)paramAbstractHorse, speedModifier);
/*      */   }
/*      */   
/*      */   public boolean shouldPanic() {
/* 1090 */     return (!AbstractHorse.this.isMobControlled() && super.shouldPanic());
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\equine\AbstractHorse$MountPanicGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */