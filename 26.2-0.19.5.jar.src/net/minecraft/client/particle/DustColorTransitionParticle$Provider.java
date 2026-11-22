/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.DustColorTransitionOptions;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class Provider implements ParticleProvider<DustColorTransitionOptions> {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   public Provider(SpriteSet sprites) {
/* 45 */     this.sprites = sprites;
/*    */   }
/*    */   
/*    */   public Particle createParticle(DustColorTransitionOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 50 */     return new DustColorTransitionParticle(level, x, y, z, xAux, yAux, zAux, options, this.sprites);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DustColorTransitionParticle$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */