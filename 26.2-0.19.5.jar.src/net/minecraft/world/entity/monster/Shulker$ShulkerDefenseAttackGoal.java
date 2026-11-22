/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ class ShulkerDefenseAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
/*     */   public ShulkerDefenseAttackGoal(Shulker mob) {
/* 771 */     super((Mob)mob, LivingEntity.class, 10, true, false, (input, level) -> input instanceof Enemy);
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 776 */     if (this.mob.getTeam() == null)
/* 777 */       return false; 
/* 779 */     return super.canUse();
/*     */   }
/*     */   
/*     */   protected AABB getTargetSearchArea(double followDistance) {
/* 784 */     Direction attachFace = ((Shulker)this.mob).getAttachFace();
/* 785 */     if (attachFace.getAxis() == Direction.Axis.X)
/* 786 */       return this.mob.getBoundingBox().inflate(4.0D, followDistance, followDistance); 
/* 788 */     if (attachFace.getAxis() == Direction.Axis.Z)
/* 789 */       return this.mob.getBoundingBox().inflate(followDistance, followDistance, 4.0D); 
/* 791 */     return this.mob.getBoundingBox().inflate(followDistance, 4.0D, followDistance);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Shulker$ShulkerDefenseAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */