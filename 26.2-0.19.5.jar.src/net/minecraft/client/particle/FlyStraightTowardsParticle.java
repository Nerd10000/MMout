/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class FlyStraightTowardsParticle extends SingleQuadParticle {
/*    */   private final double xStart;
/*    */   
/*    */   private final double yStart;
/*    */   
/*    */   private final double zStart;
/*    */   
/*    */   private final int startColor;
/*    */   
/*    */   private final int endColor;
/*    */   
/*    */   private FlyStraightTowardsParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, int startColor, int endColor, TextureAtlasSprite sprite) {
/* 21 */     super(level, x, y, z, sprite);
/* 23 */     this.xd = xd;
/* 24 */     this.yd = yd;
/* 25 */     this.zd = zd;
/* 26 */     this.xStart = x;
/* 27 */     this.yStart = y;
/* 28 */     this.zStart = z;
/* 29 */     this.xo = x + xd;
/* 30 */     this.yo = y + yd;
/* 31 */     this.zo = z + zd;
/* 32 */     this.x = this.xo;
/* 33 */     this.y = this.yo;
/* 34 */     this.z = this.zo;
/* 36 */     this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.2F);
/* 37 */     this.hasPhysics = false;
/* 39 */     this.lifetime = (int)(this.random.nextFloat() * 5.0F) + 25;
/* 41 */     this.startColor = startColor;
/* 42 */     this.endColor = endColor;
/*    */   }
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 47 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */   
/*    */   public void move(double xa, double ya, double za) {}
/*    */   
/*    */   public int getLightCoords(float a) {
/* 57 */     return LightCoordsUtil.withBlock(super.getLightCoords(a), 15);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 62 */     this.xo = this.x;
/* 63 */     this.yo = this.y;
/* 64 */     this.zo = this.z;
/* 66 */     if (this.age++ >= this.lifetime) {
/* 67 */       remove();
/*    */       return;
/*    */     } 
/* 71 */     float normalizedAge = this.age / this.lifetime;
/* 72 */     float posAlpha = 1.0F - normalizedAge;
/* 74 */     this.x = this.xStart + this.xd * posAlpha;
/* 75 */     this.y = this.yStart + this.yd * posAlpha;
/* 76 */     this.z = this.zStart + this.zd * posAlpha;
/* 78 */     int color = ARGB.srgbLerp(normalizedAge, this.startColor, this.endColor);
/* 80 */     setColor(
/* 81 */         ARGB.red(color) / 255.0F, 
/* 82 */         ARGB.green(color) / 255.0F, 
/* 83 */         ARGB.blue(color) / 255.0F);
/* 85 */     setAlpha(ARGB.alpha(color) / 255.0F);
/*    */   }
/*    */   
/*    */   public static class OminousSpawnProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public OminousSpawnProvider(SpriteSet sprite) {
/* 92 */       this.sprite = sprite;
/*    */     }
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 97 */       FlyStraightTowardsParticle particle = new FlyStraightTowardsParticle(level, x, y, z, xAux, yAux, zAux, -12210434, -1, this.sprite.get(random));
/* 98 */       particle.scale(Mth.randomBetween(level.getRandom(), 3.0F, 5.0F));
/* 99 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FlyStraightTowardsParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */