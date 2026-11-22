/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*     */ 
/*     */ class LlamaAttackWolfGoal extends NearestAttackableTargetGoal<Wolf> {
/*     */   public LlamaAttackWolfGoal(Llama llama) {
/* 524 */     super((Mob)llama, Wolf.class, 16, false, true, (target, level) -> !((Wolf)target).isTame());
/*     */   }
/*     */   
/*     */   protected double getFollowDistance() {
/* 529 */     return super.getFollowDistance() * 0.25D;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\equine\Llama$LlamaAttackWolfGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */