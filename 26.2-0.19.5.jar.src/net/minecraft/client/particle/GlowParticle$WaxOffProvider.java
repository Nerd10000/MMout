/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class WaxOffProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private static final double SPEED_FACTOR = 0.01D;
/*     */   
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public WaxOffProvider(SpriteSet sprite) {
/*  96 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 101 */     GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.0D, 0.0D, 0.0D, this.sprite);
/* 102 */     glowParticle.setColor(1.0F, 0.9F, 1.0F);
/* 104 */     glowParticle.setParticleSpeed(xAux * 0.01D / 2.0D, yAux * 0.01D, zAux * 0.01D / 2.0D);
/* 110 */     int minLifespan = 10;
/* 111 */     int maxLifespan = 40;
/* 112 */     glowParticle.setLifetime(random.nextInt(30) + 10);
/* 113 */     return glowParticle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\GlowParticle$WaxOffProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */