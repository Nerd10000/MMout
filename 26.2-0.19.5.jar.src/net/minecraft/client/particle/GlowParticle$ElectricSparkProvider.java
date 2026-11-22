/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class ElectricSparkProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private static final double SPEED_FACTOR = 0.25D;
/*     */   
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public ElectricSparkProvider(SpriteSet sprite) {
/* 122 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 127 */     GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.0D, 0.0D, 0.0D, this.sprite);
/* 128 */     glowParticle.setColor(1.0F, 0.9F, 1.0F);
/* 130 */     glowParticle.setParticleSpeed(xAux * 0.25D, yAux * 0.25D, zAux * 0.25D);
/* 136 */     int minLifespan = 2;
/* 137 */     int maxLifespan = 4;
/* 138 */     glowParticle.setLifetime(random.nextInt(2) + 2);
/* 139 */     return glowParticle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\GlowParticle$ElectricSparkProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */