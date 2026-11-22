/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.PowerParticleOption;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class Provider implements ParticleProvider<PowerParticleOption> {
/*     */   private final SpriteSet sprites;
/*     */   
/*     */   public Provider(SpriteSet sprites) {
/*  96 */     this.sprites = sprites;
/*     */   }
/*     */   
/*     */   public Particle createParticle(PowerParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 101 */     DragonBreathParticle particle = new DragonBreathParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
/* 102 */     particle.setPower(options.getPower());
/* 103 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DragonBreathParticle$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */