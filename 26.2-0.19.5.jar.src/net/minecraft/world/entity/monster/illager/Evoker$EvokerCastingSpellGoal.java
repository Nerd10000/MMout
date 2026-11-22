/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ class EvokerCastingSpellGoal extends SpellcasterIllager.SpellcasterCastingSpellGoal {
/*     */   public void tick() {
/* 143 */     if (Evoker.this.getTarget() != null) {
/* 144 */       Evoker.this.getLookControl().setLookAt((Entity)Evoker.this.getTarget(), Evoker.this.getMaxHeadYRot(), Evoker.this.getMaxHeadXRot());
/* 145 */     } else if (Evoker.this.getWololoTarget() != null) {
/* 146 */       Evoker.this.getLookControl().setLookAt((Entity)Evoker.this.getWololoTarget(), Evoker.this.getMaxHeadYRot(), Evoker.this.getMaxHeadXRot());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\Evoker$EvokerCastingSpellGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */