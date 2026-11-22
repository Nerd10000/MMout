/*      */ package net.minecraft.world.entity.animal.panda;
/*      */ 
/*      */ import net.minecraft.tags.DamageTypeTags;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*      */ 
/*      */ class PandaPanicGoal extends PanicGoal {
/*      */   private final Panda panda;
/*      */   
/*      */   public PandaPanicGoal(Panda mob, double speedModifier) {
/* 1174 */     super((PathfinderMob)mob, speedModifier, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES);
/* 1175 */     this.panda = mob;
/*      */   }
/*      */   
/*      */   public boolean canContinueToUse() {
/* 1180 */     if (this.panda.isSitting()) {
/* 1181 */       this.panda.getNavigation().stop();
/* 1182 */       return false;
/*      */     } 
/* 1184 */     return super.canContinueToUse();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\panda\Panda$PandaPanicGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */