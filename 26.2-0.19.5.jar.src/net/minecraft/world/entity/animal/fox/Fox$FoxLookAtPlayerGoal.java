/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*      */ 
/*      */ class FoxLookAtPlayerGoal extends LookAtPlayerGoal {
/*      */   public FoxLookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance) {
/* 1563 */     super(mob, lookAtType, lookDistance);
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/* 1568 */     return (super.canUse() && !Fox.this.isFaceplanted() && !Fox.this.isInterested());
/*      */   }
/*      */   
/*      */   public boolean canContinueToUse() {
/* 1573 */     return (super.canContinueToUse() && !Fox.this.isFaceplanted() && !Fox.this.isInterested());
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxLookAtPlayerGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */