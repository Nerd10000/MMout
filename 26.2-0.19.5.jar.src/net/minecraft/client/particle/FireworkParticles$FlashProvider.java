/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class FlashProvider implements ParticleProvider<ColorParticleOption> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public FlashProvider(SpriteSet sprite) {
/* 329 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 334 */     FireworkParticles.OverlayParticle particle = new FireworkParticles.OverlayParticle(level, x, y, z, this.sprite.get(random));
/* 335 */     particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
/* 336 */     particle.setAlpha(options.getAlpha());
/* 337 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FireworkParticles$FlashProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */