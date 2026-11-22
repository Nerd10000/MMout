/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class ObsidianTearLandProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public ObsidianTearLandProvider(SpriteSet sprite) {
/* 460 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 465 */     DripParticle particle = new DripParticle.DripLandParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
/* 466 */     particle.isGlowing = true;
/* 467 */     particle.lifetime = (int)(28.0D / (random.nextFloat() * 0.8D + 0.2D));
/* 468 */     particle.setColor(0.51171875F, 0.03125F, 0.890625F);
/* 469 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$ObsidianTearLandProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */