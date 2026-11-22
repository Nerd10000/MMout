/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class NectarFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public NectarFallProvider(SpriteSet sprite) {
/* 391 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 396 */     DripParticle particle = new DripParticle.FallingParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
/* 397 */     particle.lifetime = (int)(16.0D / (random.nextFloat() * 0.8D + 0.2D));
/* 398 */     particle.gravity = 0.007F;
/* 399 */     particle.setColor(0.92F, 0.782F, 0.72F);
/* 400 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$NectarFallProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */