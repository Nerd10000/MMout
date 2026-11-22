/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class DolphinSpeedProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public DolphinSpeedProvider(SpriteSet sprite) {
/* 103 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 108 */     SuspendedTownParticle particle = new SuspendedTownParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 109 */     particle.setColor(0.3F, 0.5F, 1.0F);
/* 110 */     particle.setAlpha(1.0F - random.nextFloat() * 0.7F);
/* 111 */     particle.setLifetime(particle.getLifetime() / 2);
/* 112 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SuspendedTownParticle$DolphinSpeedProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */