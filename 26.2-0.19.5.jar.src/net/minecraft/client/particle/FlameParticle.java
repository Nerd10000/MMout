/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class FlameParticle extends RisingParticle {
/*    */   private FlameParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite sprite) {
/* 11 */     super(level, x, y, z, xd, yd, zd, sprite);
/*    */   }
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 16 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */   
/*    */   public void move(double xa, double ya, double za) {
/* 21 */     setBoundingBox(getBoundingBox().move(xa, ya, za));
/* 22 */     setLocationFromBoundingbox();
/*    */   }
/*    */   
/*    */   public float getQuadSize(float a) {
/* 27 */     float s = (this.age + a) / this.lifetime;
/* 28 */     return this.quadSize * (1.0F - s * s * 0.5F);
/*    */   }
/*    */   
/*    */   public int getLightCoords(float a) {
/* 33 */     return LightCoordsUtil.addSmoothBlockEmission(super.getLightCoords(a), (this.age + a) / this.lifetime);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 40 */       this.sprite = sprite;
/*    */     }
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 45 */       FlameParticle particle = new FlameParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 46 */       return particle;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class SmallFlameProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public SmallFlameProvider(SpriteSet sprite) {
/* 54 */       this.sprite = sprite;
/*    */     }
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 59 */       FlameParticle particle = new FlameParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 60 */       particle.scale(0.5F);
/* 61 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FlameParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */