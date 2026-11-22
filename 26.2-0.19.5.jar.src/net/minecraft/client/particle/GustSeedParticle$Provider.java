/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class Provider implements ParticleProvider<SimpleParticleType> {
/*    */   private final double scale;
/*    */   
/*    */   private final int lifetime;
/*    */   
/*    */   private final int tickDelayInBetween;
/*    */   
/*    */   public Provider(double scale, int lifetime, int tickDelayInBetween) {
/* 42 */     this.scale = scale;
/* 43 */     this.lifetime = lifetime;
/* 44 */     this.tickDelayInBetween = tickDelayInBetween;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 49 */     return new GustSeedParticle(level, x, y, z, this.scale, this.lifetime, this.tickDelayInBetween);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\GustSeedParticle$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */