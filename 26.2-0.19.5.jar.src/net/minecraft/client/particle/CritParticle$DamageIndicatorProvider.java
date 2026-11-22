/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class DamageIndicatorProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public DamageIndicatorProvider(SpriteSet sprite) {
/* 84 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 89 */     CritParticle particle = new CritParticle(level, x, y, z, xAux, yAux + 1.0D, zAux, this.sprite.get(random));
/* 90 */     particle.setLifetime(20);
/* 91 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\CritParticle$DamageIndicatorProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */