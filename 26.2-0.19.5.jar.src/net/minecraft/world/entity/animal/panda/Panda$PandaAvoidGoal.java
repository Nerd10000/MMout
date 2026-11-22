/*      */ package net.minecraft.world.entity.animal.panda;
/*      */ 
/*      */ import net.minecraft.world.entity.EntitySelector;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*      */ 
/*      */ class PandaAvoidGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
/*      */   private final Panda panda;
/*      */   
/*      */   public PandaAvoidGoal(Panda panda, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 1021 */     super((PathfinderMob)panda, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_SPECTATORS);
/* 1023 */     this.panda = panda;
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/* 1028 */     return (this.panda.isWorried() && this.panda.canPerformAction() && super.canUse());
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\panda\Panda$PandaAvoidGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */