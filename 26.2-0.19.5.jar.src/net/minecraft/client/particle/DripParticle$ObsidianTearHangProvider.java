/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class ObsidianTearHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public ObsidianTearHangProvider(SpriteSet sprite) {
/* 425 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 430 */     DripParticle.DripHangParticle particle = new DripParticle.DripHangParticle(level, x, y, z, Fluids.EMPTY, (ParticleOptions)ParticleTypes.FALLING_OBSIDIAN_TEAR, this.sprite.get(random));
/* 431 */     particle.isGlowing = true;
/* 432 */     particle.gravity *= 0.01F;
/* 433 */     particle.lifetime = 100;
/* 434 */     particle.setColor(0.51171875F, 0.03125F, 0.890625F);
/* 435 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$ObsidianTearHangProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */