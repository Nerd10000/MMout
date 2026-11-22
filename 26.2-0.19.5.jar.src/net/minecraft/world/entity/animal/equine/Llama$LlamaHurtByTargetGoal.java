/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ 
/*     */ class LlamaHurtByTargetGoal extends HurtByTargetGoal {
/*     */   public LlamaHurtByTargetGoal(Llama llama) {
/* 507 */     super((PathfinderMob)llama, new Class[0]);
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 512 */     Mob mob = this.mob;
/* 512 */     if (mob instanceof Llama) {
/* 512 */       Llama llama = (Llama)mob;
/* 513 */       if (llama.didSpit) {
/* 514 */         llama.setDidSpit(false);
/* 515 */         return false;
/*     */       } 
/*     */     } 
/* 518 */     return super.canContinueToUse();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\equine\Llama$LlamaHurtByTargetGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */