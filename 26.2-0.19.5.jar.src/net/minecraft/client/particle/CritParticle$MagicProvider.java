/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class MagicProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public MagicProvider(SpriteSet sprite) {
/* 68 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 73 */     CritParticle particle = new CritParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 74 */     particle.rCol *= 0.3F;
/* 75 */     particle.gCol *= 0.8F;
/* 76 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\CritParticle$MagicProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */