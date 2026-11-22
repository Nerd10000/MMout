/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class SparkProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprites;
/*     */   
/*     */   public SparkProvider(SpriteSet sprites) {
/* 345 */     this.sprites = sprites;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 350 */     FireworkParticles.SparkParticle particle = new FireworkParticles.SparkParticle(level, x, y, z, xAux, yAux, zAux, (Minecraft.getInstance()).particleEngine, this.sprites);
/* 351 */     particle.setAlpha(0.99F);
/* 352 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FireworkParticles$SparkProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */