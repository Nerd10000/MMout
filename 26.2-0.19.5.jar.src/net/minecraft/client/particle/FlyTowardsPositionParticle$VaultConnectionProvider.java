/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class VaultConnectionProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public VaultConnectionProvider(SpriteSet sprite) {
/* 139 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 144 */     FlyTowardsPositionParticle particle = new FlyTowardsPositionParticle(level, x, y, z, xAux, yAux, zAux, true, new Particle.LifetimeAlpha(0.0F, 0.6F, 0.25F, 1.0F), this.sprite.get(random));
/* 145 */     particle.scale(1.5F);
/* 146 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FlyTowardsPositionParticle$VaultConnectionProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */