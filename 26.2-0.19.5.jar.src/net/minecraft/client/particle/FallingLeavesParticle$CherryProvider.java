/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class CherryProvider implements ParticleProvider<SimpleParticleType> {
/*     */   private final SpriteSet sprites;
/*     */   
/*     */   public CherryProvider(SpriteSet sprites) {
/* 128 */     this.sprites = sprites;
/*     */   }
/*     */   
/*     */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 133 */     return new FallingLeavesParticle(level, x, y, z, this.sprites.get(random), 0.25F, 2.0F, false, true, 1.0F, 0.0F);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FallingLeavesParticle$CherryProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */