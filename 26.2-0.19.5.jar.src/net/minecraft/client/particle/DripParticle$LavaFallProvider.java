/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class LavaFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public LavaFallProvider(SpriteSet sprite) {
/* 253 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 258 */     DripParticle particle = new DripParticle.FallAndLandParticle(level, x, y, z, (Fluid)Fluids.LAVA, (ParticleOptions)ParticleTypes.LANDING_LAVA, this.sprite.get(random));
/* 259 */     particle.setColor(1.0F, 0.2857143F, 0.083333336F);
/* 260 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$LavaFallProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */