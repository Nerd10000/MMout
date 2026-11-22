/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ShriekParticleOption;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class Provider implements ParticleProvider<ShriekParticleOption> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public Provider(SpriteSet sprite) {
/* 78 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(ShriekParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 83 */     ShriekParticle particle = new ShriekParticle(level, x, y, z, options.getDelay(), this.sprite.get(random));
/* 84 */     particle.setAlpha(1.0F);
/* 85 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ShriekParticle$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */