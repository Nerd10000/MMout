/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class FireflyProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public FireflyProvider(SpriteSet sprite) {
/* 77 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 82 */     FireflyParticle particle = new FireflyParticle(level, x, y, z, 0.5D - random.nextDouble(), random.nextBoolean() ? yAux : -yAux, 0.5D - random.nextDouble(), this.sprite.get(random));
/* 83 */     particle.setLifetime(random.nextIntBetweenInclusive(200, 300));
/* 84 */     particle.scale(1.5F);
/* 85 */     particle.setAlpha(0.0F);
/* 86 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FireflyParticle$FireflyProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */