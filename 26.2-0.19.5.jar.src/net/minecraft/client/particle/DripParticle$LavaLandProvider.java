/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class LavaLandProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public LavaLandProvider(SpriteSet sprite) {
/* 268 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 273 */     DripParticle particle = new DripParticle.DripLandParticle(level, x, y, z, (Fluid)Fluids.LAVA, this.sprite.get(random));
/* 274 */     particle.setColor(1.0F, 0.2857143F, 0.083333336F);
/* 275 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$LavaLandProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */