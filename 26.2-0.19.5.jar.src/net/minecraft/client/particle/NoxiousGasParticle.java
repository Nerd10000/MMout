/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class NoxiousGasParticle extends BaseAshSmokeParticle {
/*    */   private final float fadeOutStartingPoint;
/*    */   
/*    */   protected NoxiousGasParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, float scale, SpriteSet sprites) {
/* 11 */     super(level, x, y, z, 0.1F, 0.1F, 0.1F, xa, ya, za, scale, sprites, 0.3F, 5, -0.02F, true);
/* 12 */     this.rCol = 1.0F;
/* 13 */     this.gCol = 1.0F;
/* 14 */     this.bCol = 1.0F;
/* 15 */     this.lifetime = (int)(6.0D / (this.random.nextFloat() * 0.5D + 0.5D) * scale);
/* 16 */     this.fadeOutStartingPoint = this.lifetime / 2.0F;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 21 */     super.tick();
/* 23 */     if (this.age > this.fadeOutStartingPoint) {
/* 24 */       float framesSinceFadeOutStart = this.age - this.fadeOutStartingPoint;
/* 25 */       setAlpha((this.lifetime - framesSinceFadeOutStart) / this.lifetime);
/*    */     } 
/*    */   }
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 31 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 38 */       this.sprites = sprites;
/*    */     }
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 43 */       return new NoxiousGasParticle(level, x, y, z, xAux, yAux, zAux, 3.0F, this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\NoxiousGasParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */