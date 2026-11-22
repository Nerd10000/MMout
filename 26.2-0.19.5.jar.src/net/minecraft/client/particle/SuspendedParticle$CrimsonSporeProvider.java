/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class CrimsonSporeProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public CrimsonSporeProvider(SpriteSet sprite) {
/* 84 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 89 */     double xa = random.nextGaussian() * 9.999999974752427E-7D;
/* 90 */     double ya = random.nextGaussian() * 9.999999747378752E-5D;
/* 91 */     double za = random.nextGaussian() * 9.999999974752427E-7D;
/* 92 */     SuspendedParticle particle = new SuspendedParticle(level, x, y, z, xa, ya, za, this.sprite.get(random));
/* 93 */     particle.setColor(0.9F, 0.4F, 0.5F);
/* 94 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SuspendedParticle$CrimsonSporeProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */