/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SpellParticleOption;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class InstantProvider implements ParticleProvider<SpellParticleOption> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public InstantProvider(SpriteSet sprite) {
/* 121 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SpellParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 126 */     SpellParticle particle = new SpellParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/* 127 */     particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
/* 128 */     particle.setPower(options.getPower());
/* 129 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SpellParticle$InstantProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */