/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ComposterFillProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public ComposterFillProvider(SpriteSet sprite) {
/* 87 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 92 */     SuspendedTownParticle particle = new SuspendedTownParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 93 */     particle.setColor(1.0F, 1.0F, 1.0F);
/* 94 */     particle.setLifetime(3 + level.getRandom().nextInt(5));
/* 95 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SuspendedTownParticle$ComposterFillProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */