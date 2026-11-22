/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.BlockParticleOption;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class DustPillarProvider implements ParticleProvider<BlockParticleOption> {
/*    */   public Particle createParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 83 */     Particle particle = TerrainParticle.createTerrainParticle(options, level, x, y, z, xAux, yAux, zAux);
/* 84 */     if (particle != null) {
/* 85 */       particle.setParticleSpeed(random.nextGaussian() / 30.0D, yAux + random.nextGaussian() / 2.0D, random.nextGaussian() / 30.0D);
/* 86 */       particle.setLifetime(random.nextInt(20) + 20);
/*    */     } 
/* 88 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\TerrainParticle$DustPillarProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */