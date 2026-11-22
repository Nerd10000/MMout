/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.ai.control.LookControl;
/*      */ 
/*      */ public class FoxLookControl extends LookControl {
/*      */   public FoxLookControl() {
/* 1520 */     super((Mob)Fox.this);
/*      */   }
/*      */   
/*      */   public void tick() {
/* 1525 */     if (!Fox.this.isSleeping())
/* 1526 */       super.tick(); 
/*      */   }
/*      */   
/*      */   protected boolean resetXRotOnTick() {
/* 1532 */     return (!Fox.this.isPouncing() && !Fox.this.isCrouching() && !Fox.this.isInterested() && !Fox.this.isFaceplanted());
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxLookControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */