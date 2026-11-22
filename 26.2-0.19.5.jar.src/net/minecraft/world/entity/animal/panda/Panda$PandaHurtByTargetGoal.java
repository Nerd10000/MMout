/*      */ package net.minecraft.world.entity.animal.panda;
/*      */ 
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*      */ 
/*      */ class PandaHurtByTargetGoal extends HurtByTargetGoal {
/*      */   private final Panda panda;
/*      */   
/*      */   public PandaHurtByTargetGoal(Panda mob, Class<?>... ignoreDamageFromTheseTypes) {
/* 1148 */     super((PathfinderMob)mob, ignoreDamageFromTheseTypes);
/* 1149 */     this.panda = mob;
/*      */   }
/*      */   
/*      */   public boolean canContinueToUse() {
/* 1154 */     if (this.panda.gotBamboo || this.panda.didBite) {
/* 1155 */       this.panda.setTarget(null);
/* 1156 */       return false;
/*      */     } 
/* 1158 */     return super.canContinueToUse();
/*      */   }
/*      */   
/*      */   protected void alertOther(Mob other, LivingEntity hurtByMob) {
/* 1163 */     if (other instanceof Panda && other.isAggressive())
/* 1164 */       other.setTarget(hurtByMob); 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\panda\Panda$PandaHurtByTargetGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */