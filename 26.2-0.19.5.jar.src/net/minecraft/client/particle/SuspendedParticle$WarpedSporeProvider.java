/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class WarpedSporeProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public WarpedSporeProvider(SpriteSet sprite) {
/* 102 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 107 */     double ya = random.nextFloat() * -1.9D * random.nextFloat() * 0.1D;
/* 108 */     SuspendedParticle particle = new SuspendedParticle(level, x, y, z, 0.0D, ya, 0.0D, this.sprite.get(random));
/* 109 */     particle.setColor(0.1F, 0.1F, 0.3F);
/* 110 */     particle.setSize(0.001F, 0.001F);
/* 111 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SuspendedParticle$WarpedSporeProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */