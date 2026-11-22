/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*    */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*    */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*    */ 
/*    */ public class RestrictSunGoal extends Goal {
/*    */   private final PathfinderMob mob;
/*    */   
/*    */   public RestrictSunGoal(PathfinderMob mob) {
/* 12 */     this.mob = mob;
/*    */   }
/*    */   
/*    */   public boolean canUse() {
/* 17 */     return (this.mob.level().isBrightOutside() && this.mob.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && GoalUtils.hasGroundPathNavigation((Mob)this.mob));
/*    */   }
/*    */   
/*    */   public void start() {
/* 22 */     PathNavigation pathNavigation = this.mob.getNavigation();
/* 22 */     if (pathNavigation instanceof GroundPathNavigation) {
/* 22 */       GroundPathNavigation groundPathNavigation = (GroundPathNavigation)pathNavigation;
/* 23 */       groundPathNavigation.setAvoidSun(true);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void stop() {
/* 29 */     if (GoalUtils.hasGroundPathNavigation((Mob)this.mob)) {
/* 29 */       PathNavigation pathNavigation = this.mob.getNavigation();
/* 29 */       if (pathNavigation instanceof GroundPathNavigation) {
/* 29 */         GroundPathNavigation groundPathNavigation = (GroundPathNavigation)pathNavigation;
/* 30 */         groundPathNavigation.setAvoidSun(false);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\RestrictSunGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */