/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ 
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.ai.control.LookControl;
/*      */ 
/*      */ class BeeLookControl extends LookControl {
/*      */   public BeeLookControl(Mob mob) {
/* 1089 */     super(mob);
/*      */   }
/*      */   
/*      */   public void tick() {
/* 1094 */     if (Bee.this.isAngry())
/*      */       return; 
/* 1097 */     super.tick();
/*      */   }
/*      */   
/*      */   protected boolean resetXRotOnTick() {
/* 1102 */     return !Bee.this.beePollinateGoal.isPollinating();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$BeeLookControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */