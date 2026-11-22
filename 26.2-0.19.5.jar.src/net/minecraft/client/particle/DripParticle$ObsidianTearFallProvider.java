/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class ObsidianTearFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public ObsidianTearFallProvider(SpriteSet sprite) {
/* 443 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 448 */     DripParticle particle = new DripParticle.FallAndLandParticle(level, x, y, z, Fluids.EMPTY, (ParticleOptions)ParticleTypes.LANDING_OBSIDIAN_TEAR, this.sprite.get(random));
/* 449 */     particle.isGlowing = true;
/* 450 */     particle.gravity = 0.01F;
/* 451 */     particle.setColor(0.51171875F, 0.03125F, 0.890625F);
/* 452 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$ObsidianTearFallProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */