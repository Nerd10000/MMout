/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ForNonPathfinders extends TemptGoal {
/*     */   public ForNonPathfinders(Mob mob, double speedModifier, Predicate<ItemStack> items, boolean canScare, double stopDistance) {
/* 132 */     super(mob, speedModifier, items, canScare, stopDistance);
/*     */   }
/*     */   
/*     */   protected void stopNavigation() {
/* 137 */     this.mob.getMoveControl().setWait();
/*     */   }
/*     */   
/*     */   protected void navigateTowards(Player player) {
/* 142 */     Vec3 target = player.getEyePosition().subtract(this.mob.position()).scale(this.mob.getRandom().nextDouble()).add(this.mob.position());
/* 143 */     this.mob.getMoveControl().setWantedPosition(target.x, target.y, target.z, this.speedModifier);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\TemptGoal$ForNonPathfinders.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */