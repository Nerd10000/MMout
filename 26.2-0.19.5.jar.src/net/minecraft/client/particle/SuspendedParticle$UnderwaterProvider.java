/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class UnderwaterProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public UnderwaterProvider(SpriteSet sprite) {
/* 47 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 52 */     SuspendedParticle particle = new SuspendedParticle(level, x, y, z, this.sprite.get(random));
/* 53 */     particle.setColor(0.4F, 0.4F, 0.7F);
/* 54 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SuspendedParticle$UnderwaterProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */