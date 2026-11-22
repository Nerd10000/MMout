/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class ScrapeProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private static final double SPEED_FACTOR = 0.01D;
/*     */   
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public ScrapeProvider(SpriteSet sprite) {
/* 148 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 153 */     GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.0D, 0.0D, 0.0D, this.sprite);
/* 154 */     if (random.nextBoolean()) {
/* 155 */       glowParticle.setColor(0.29F, 0.58F, 0.51F);
/*     */     } else {
/* 157 */       glowParticle.setColor(0.43F, 0.77F, 0.62F);
/*     */     } 
/* 160 */     glowParticle.setParticleSpeed(xAux * 0.01D, yAux * 0.01D, zAux * 0.01D);
/* 166 */     int minLifespan = 10;
/* 167 */     int maxLifespan = 40;
/* 168 */     glowParticle.setLifetime(random.nextInt(30) + 10);
/* 169 */     return glowParticle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\GlowParticle$ScrapeProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */