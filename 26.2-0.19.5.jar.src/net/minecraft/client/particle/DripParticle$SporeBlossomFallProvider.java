/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class SporeBlossomFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public SporeBlossomFallProvider(SpriteSet sprite) {
/* 408 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 413 */     DripParticle particle = new DripParticle.FallingParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
/* 414 */     particle.lifetime = (int)(64.0F / Mth.randomBetween(particle.random, 0.1F, 0.9F));
/* 415 */     particle.gravity = 0.005F;
/* 416 */     particle.setColor(0.32F, 0.5F, 0.22F);
/* 417 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$SporeBlossomFallProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */