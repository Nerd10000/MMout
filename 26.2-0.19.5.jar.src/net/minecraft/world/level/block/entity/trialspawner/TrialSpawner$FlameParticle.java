/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ 
/*     */ public enum FlameParticle {
/* 383 */   NORMAL(ParticleTypes.FLAME),
/* 384 */   OMINOUS(ParticleTypes.SOUL_FIRE_FLAME);
/*     */   
/*     */   public final SimpleParticleType particleType;
/*     */   
/*     */   FlameParticle(SimpleParticleType particleType) {
/* 389 */     this.particleType = particleType;
/*     */   }
/*     */   
/*     */   public static FlameParticle decode(int data) {
/* 393 */     FlameParticle[] values = values();
/* 394 */     if (data > values.length || data < 0)
/* 395 */       return NORMAL; 
/* 397 */     return values[data];
/*     */   }
/*     */   
/*     */   public int encode() {
/* 401 */     return ordinal();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawner$FlameParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */