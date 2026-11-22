/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class CrumblingProvider implements ParticleProvider<BlockParticleOption> {
/*     */   public Particle createParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  95 */     Particle particle = TerrainParticle.createTerrainParticle(options, level, x, y, z, xAux, yAux, zAux);
/*  96 */     if (particle != null) {
/*  97 */       particle.setParticleSpeed(0.0D, 0.0D, 0.0D);
/*  98 */       particle.setLifetime(random.nextInt(10) + 1);
/*     */     } 
/* 100 */     return particle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\TerrainParticle$CrumblingProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */