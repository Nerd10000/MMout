/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.GeyserParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class Provider implements ParticleProvider<GeyserParticleOptions> {
/*     */   private final SpriteSet sprites;
/*     */   
/*     */   public Provider(SpriteSet sprites) {
/* 100 */     this.sprites = sprites;
/*     */   }
/*     */   
/*     */   public Particle createParticle(GeyserParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 105 */     double randomX = x + ((random.nextFloat() - 0.5F) * 0.2F);
/* 106 */     double randomY = y + random.nextFloat();
/* 107 */     double randomZ = z + ((random.nextFloat() - 0.5F) * 0.2F);
/* 108 */     return new GeyserPlumeParticle(level, randomX, randomY, randomZ, xAux, yAux, zAux, options, this.sprites);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\GeyserPlumeParticle$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */