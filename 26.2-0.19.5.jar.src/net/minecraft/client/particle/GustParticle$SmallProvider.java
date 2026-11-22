/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SmallProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   public SmallProvider(SpriteSet sprites) {
/* 56 */     this.sprites = sprites;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 61 */     Particle particle = new GustParticle(level, x, y, z, this.sprites);
/* 62 */     particle.scale(0.15F);
/* 63 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\GustParticle$SmallProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */