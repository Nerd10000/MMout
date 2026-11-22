/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class TintedLeavesProvider implements ParticleProvider<ColorParticleOption> {
/*     */   private final SpriteSet sprites;
/*     */   
/*     */   public TintedLeavesProvider(SpriteSet sprites) {
/* 154 */     this.sprites = sprites;
/*     */   }
/*     */   
/*     */   public Particle createParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 159 */     FallingLeavesParticle particle = new FallingLeavesParticle(level, x, y, z, this.sprites.get(random), 0.07F, 10.0F, true, false, 2.0F, 0.021F);
/* 160 */     particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
/* 161 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FallingLeavesParticle$TintedLeavesProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */