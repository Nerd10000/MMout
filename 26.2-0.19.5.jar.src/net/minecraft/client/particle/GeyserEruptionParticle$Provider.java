/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.GeyserParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class Provider implements ParticleProvider<GeyserParticleOptions> {
/*    */   public Particle createParticle(GeyserParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 66 */     return new GeyserEruptionParticle(level, x, y, z, xAux, yAux, zAux, options);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\GeyserEruptionParticle$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */