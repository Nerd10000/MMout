/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.VibrationParticleOption;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class Provider implements ParticleProvider<VibrationParticleOption> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public Provider(SpriteSet sprite) {
/* 112 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(VibrationParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 117 */     VibrationSignalParticle particle = new VibrationSignalParticle(level, x, y, z, options.getDestination(), options.getArrivalInTicks(), this.sprite.get(random));
/* 118 */     particle.setAlpha(1.0F);
/* 119 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\VibrationSignalParticle$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */