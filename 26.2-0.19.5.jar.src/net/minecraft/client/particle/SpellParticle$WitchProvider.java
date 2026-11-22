/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class WitchProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public WitchProvider(SpriteSet sprite) {
/* 105 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 110 */     SpellParticle particle = new SpellParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/* 111 */     float randBrightness = random.nextFloat() * 0.5F + 0.35F;
/* 112 */     particle.setColor(1.0F * randBrightness, 0.0F * randBrightness, 1.0F * randBrightness);
/* 113 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SpellParticle$WitchProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */