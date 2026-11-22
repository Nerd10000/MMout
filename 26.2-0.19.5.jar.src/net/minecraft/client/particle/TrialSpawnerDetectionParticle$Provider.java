/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class Provider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   public Provider(SpriteSet sprites) {
/* 66 */     this.sprites = sprites;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 71 */     return new TrialSpawnerDetectionParticle(level, x, y, z, xAux, yAux, zAux, 1.5F, this.sprites);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\TrialSpawnerDetectionParticle$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */