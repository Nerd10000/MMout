/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class HoneyLandProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public HoneyLandProvider(SpriteSet sprite) {
/* 316 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 321 */     DripParticle particle = new DripParticle.DripLandParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
/* 322 */     particle.lifetime = (int)(128.0D / (random.nextFloat() * 0.8D + 0.2D));
/* 323 */     particle.setColor(0.522F, 0.408F, 0.082F);
/* 324 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$HoneyLandProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */