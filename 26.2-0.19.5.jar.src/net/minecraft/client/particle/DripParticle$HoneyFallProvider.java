/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class HoneyFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public HoneyFallProvider(SpriteSet sprite) {
/* 300 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 305 */     DripParticle particle = new DripParticle.HoneyFallAndLandParticle(level, x, y, z, Fluids.EMPTY, (ParticleOptions)ParticleTypes.LANDING_HONEY, this.sprite.get(random));
/* 306 */     particle.gravity = 0.01F;
/* 307 */     particle.setColor(0.582F, 0.448F, 0.082F);
/* 308 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$HoneyFallProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */