/*     */ package net.minecraft.world.entity.animal.feline;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class CatTemptGoal extends TemptGoal {
/*     */   private Player selectedPlayer;
/*     */   
/*     */   private final Cat cat;
/*     */   
/*     */   public CatTemptGoal(Cat mob, double speedModifier, Predicate<ItemStack> items, boolean canScare) {
/* 547 */     super((PathfinderMob)mob, speedModifier, items, canScare);
/* 548 */     this.cat = mob;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 553 */     super.tick();
/* 555 */     if (this.selectedPlayer == null && this.mob.getRandom().nextInt(adjustedTickDelay(600)) == 0) {
/* 556 */       this.selectedPlayer = this.player;
/* 557 */     } else if (this.mob.getRandom().nextInt(adjustedTickDelay(500)) == 0) {
/* 558 */       this.selectedPlayer = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean canScare() {
/* 564 */     if (this.selectedPlayer != null && this.selectedPlayer.equals(this.player))
/* 565 */       return false; 
/* 568 */     return super.canScare();
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 573 */     return (super.canUse() && !this.cat.isTame());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\feline\Cat$CatTemptGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */