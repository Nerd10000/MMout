/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SignalProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   public SignalProvider(SpriteSet sprites) {
/* 70 */     this.sprites = sprites;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 75 */     CampfireSmokeParticle particle = new CampfireSmokeParticle(level, x, y, z, xAux, yAux, zAux, true, this.sprites.get(random));
/* 76 */     particle.setAlpha(0.95F);
/* 77 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\CampfireSmokeParticle$SignalProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */