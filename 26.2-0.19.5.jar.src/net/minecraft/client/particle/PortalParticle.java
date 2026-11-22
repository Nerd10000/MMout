/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class PortalParticle extends SingleQuadParticle {
/*    */   private final double xStart;
/*    */   
/*    */   private final double yStart;
/*    */   
/*    */   private final double zStart;
/*    */   
/*    */   protected PortalParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite sprite) {
/* 15 */     super(level, x, y, z, sprite);
/* 17 */     this.xd = xd;
/* 18 */     this.yd = yd;
/* 19 */     this.zd = zd;
/* 20 */     this.x = x;
/* 21 */     this.y = y;
/* 22 */     this.z = z;
/* 23 */     this.xStart = this.x;
/* 24 */     this.yStart = this.y;
/* 25 */     this.zStart = this.z;
/* 27 */     this.quadSize = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F);
/* 29 */     float br = this.random.nextFloat() * 0.6F + 0.4F;
/* 30 */     this.rCol = br * 0.9F;
/* 31 */     this.gCol = br * 0.3F;
/* 32 */     this.bCol = br;
/* 34 */     this.lifetime = (int)(this.random.nextFloat() * 10.0F) + 40;
/*    */   }
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 39 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */   
/*    */   public void move(double xa, double ya, double za) {
/* 44 */     setBoundingBox(getBoundingBox().move(xa, ya, za));
/* 45 */     setLocationFromBoundingbox();
/*    */   }
/*    */   
/*    */   public float getQuadSize(float a) {
/* 50 */     float s = (this.age + a) / this.lifetime;
/* 51 */     s = 1.0F - s;
/* 52 */     s *= s;
/* 53 */     s = 1.0F - s;
/* 54 */     return this.quadSize * s;
/*    */   }
/*    */   
/*    */   public int getLightCoords(float a) {
/* 59 */     float brightness = this.age / this.lifetime;
/* 60 */     brightness *= brightness;
/* 61 */     brightness *= brightness;
/* 62 */     return LightCoordsUtil.addSmoothBlockEmission(super.getLightCoords(a), brightness);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 67 */     this.xo = this.x;
/* 68 */     this.yo = this.y;
/* 69 */     this.zo = this.z;
/* 71 */     if (this.age++ >= this.lifetime) {
/* 72 */       remove();
/*    */       return;
/*    */     } 
/* 76 */     float pos = this.age / this.lifetime;
/* 77 */     float a = pos;
/* 78 */     pos = -pos + pos * pos * 2.0F;
/* 79 */     pos = 1.0F - pos;
/* 81 */     this.x = this.xStart + this.xd * pos;
/* 82 */     this.y = this.yStart + this.yd * pos + (1.0F - a);
/* 83 */     this.z = this.zStart + this.zd * pos;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 90 */       this.sprite = sprite;
/*    */     }
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 95 */       PortalParticle particle = new PortalParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 96 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\PortalParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */