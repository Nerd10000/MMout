/*     */ package net.minecraft.world.entity.monster.cubemob;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class SulfurCubeTemptGoal extends TemptGoal.ForNonPathfinders {
/*     */   public SulfurCubeTemptGoal(Mob mob, double speedModifier, Predicate<ItemStack> items, boolean canScare, double stopDistance) {
/* 616 */     super(mob, speedModifier, items, canScare, stopDistance);
/* 617 */     setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   protected void stopNavigation() {
/* 622 */     MoveControl moveControl = this.mob.getMoveControl();
/* 622 */     if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/* 622 */       AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/* 623 */       cubeMobMoveControl.setWantedMovement(0.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void navigateTowards(Player player) {
/* 629 */     this.mob.lookAt((Entity)player, 10.0F, 10.0F);
/* 630 */     MoveControl moveControl = this.mob.getMoveControl();
/* 630 */     if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/* 630 */       AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/* 631 */       cubeMobMoveControl.setDirection(this.mob.getYRot(), true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\cubemob\SulfurCube$SulfurCubeTemptGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */