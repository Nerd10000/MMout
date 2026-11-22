/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.TrailParticleOption;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class Provider implements ParticleProvider<TrailParticleOption> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public Provider(SpriteSet sprite) {
/* 62 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(TrailParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 67 */     TrailParticle particle = new TrailParticle(level, x, y, z, xAux, yAux, zAux, options.target(), options.color(), this.sprite.get(random));
/* 68 */     particle.setLifetime(options.duration());
/* 70 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\TrailParticle$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */