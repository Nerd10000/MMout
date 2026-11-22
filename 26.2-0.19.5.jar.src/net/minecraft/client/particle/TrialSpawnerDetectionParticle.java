/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class TrialSpawnerDetectionParticle extends SingleQuadParticle {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   private static final int BASE_LIFETIME = 8;
/*    */   
/*    */   protected TrialSpawnerDetectionParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, float scale, SpriteSet sprites) {
/* 15 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprites.first());
/* 16 */     this.sprites = sprites;
/* 17 */     this.friction = 0.96F;
/* 18 */     this.gravity = -0.1F;
/* 19 */     this.speedUpWhenYMotionIsBlocked = true;
/* 21 */     this.xd *= 0.0D;
/* 22 */     this.yd *= 0.9D;
/* 23 */     this.zd *= 0.0D;
/* 24 */     this.xd += xa;
/* 25 */     this.yd += ya;
/* 26 */     this.zd += za;
/* 28 */     this.quadSize *= 0.75F * scale;
/* 30 */     this.lifetime = (int)(8.0F / Mth.randomBetween(this.random, 0.5F, 1.0F) * scale);
/* 31 */     this.lifetime = Math.max(this.lifetime, 1);
/* 32 */     setSpriteFromAge(sprites);
/* 33 */     this.hasPhysics = true;
/*    */   }
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 38 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */   
/*    */   public int getLightCoords(float a) {
/* 43 */     return LightCoordsUtil.withBlock(super.getLightCoords(a), 15);
/*    */   }
/*    */   
/*    */   public SingleQuadParticle.FacingCameraMode getFacingCameraMode() {
/* 48 */     return SingleQuadParticle.FacingCameraMode.LOOKAT_Y;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 53 */     super.tick();
/* 54 */     setSpriteFromAge(this.sprites);
/*    */   }
/*    */   
/*    */   public float getQuadSize(float a) {
/* 59 */     return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 66 */       this.sprites = sprites;
/*    */     }
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 71 */       return new TrialSpawnerDetectionParticle(level, x, y, z, xAux, yAux, zAux, 1.5F, this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\TrialSpawnerDetectionParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */