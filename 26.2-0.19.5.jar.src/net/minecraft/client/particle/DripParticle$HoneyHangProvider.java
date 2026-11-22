/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class HoneyHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public HoneyHangProvider(SpriteSet sprite) {
/* 283 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 288 */     DripParticle.DripHangParticle particle = new DripParticle.DripHangParticle(level, x, y, z, Fluids.EMPTY, (ParticleOptions)ParticleTypes.FALLING_HONEY, this.sprite.get(random));
/* 289 */     particle.gravity *= 0.01F;
/* 290 */     particle.lifetime = 100;
/* 291 */     particle.setColor(0.622F, 0.508F, 0.082F);
/* 292 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$HoneyHangProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */