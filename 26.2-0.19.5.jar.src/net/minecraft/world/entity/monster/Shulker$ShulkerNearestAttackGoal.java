/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ class ShulkerNearestAttackGoal extends NearestAttackableTargetGoal<Player> {
/*     */   public ShulkerNearestAttackGoal(Shulker mob) {
/* 745 */     super((Mob)mob, Player.class, true);
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 750 */     if (Shulker.this.level().getDifficulty() == Difficulty.PEACEFUL)
/* 751 */       return false; 
/* 753 */     return super.canUse();
/*     */   }
/*     */   
/*     */   protected AABB getTargetSearchArea(double followDistance) {
/* 758 */     Direction attachFace = ((Shulker)this.mob).getAttachFace();
/* 759 */     if (attachFace.getAxis() == Direction.Axis.X)
/* 760 */       return this.mob.getBoundingBox().inflate(4.0D, followDistance, followDistance); 
/* 762 */     if (attachFace.getAxis() == Direction.Axis.Z)
/* 763 */       return this.mob.getBoundingBox().inflate(followDistance, followDistance, 4.0D); 
/* 765 */     return this.mob.getBoundingBox().inflate(followDistance, 4.0D, followDistance);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Shulker$ShulkerNearestAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */