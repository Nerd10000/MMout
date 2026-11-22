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
/*     */ public class DripstoneWaterHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprite;
/*     */   
/*     */   public DripstoneWaterHangProvider(SpriteSet sprite) {
/* 332 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 337 */     DripParticle particle = new DripParticle.DripHangParticle(level, x, y, z, (Fluid)Fluids.WATER, (ParticleOptions)ParticleTypes.FALLING_DRIPSTONE_WATER, this.sprite.get(random));
/* 338 */     particle.setColor(0.2F, 0.3F, 1.0F);
/* 339 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$DripstoneWaterHangProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */