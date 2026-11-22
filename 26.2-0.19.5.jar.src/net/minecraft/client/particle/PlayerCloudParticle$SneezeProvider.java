/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SneezeProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   public SneezeProvider(SpriteSet sprites) {
/* 82 */     this.sprites = sprites;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 87 */     PlayerCloudParticle particle = new PlayerCloudParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
/* 88 */     particle.setColor(0.22F, 1.0F, 0.53F);
/* 89 */     particle.setAlpha(0.4F);
/* 90 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\PlayerCloudParticle$SneezeProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */