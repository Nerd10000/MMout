/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SoulParticle extends RisingParticle {
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   protected boolean isGlowing;
/*    */   
/*    */   private SoulParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
/* 14 */     super(level, x, y, z, xd, yd, zd, sprites.first());
/* 16 */     this.sprites = sprites;
/* 17 */     scale(1.5F);
/* 19 */     setSpriteFromAge(sprites);
/*    */   }
/*    */   
/*    */   public int getLightCoords(float a) {
/* 24 */     if (this.isGlowing)
/* 25 */       return LightCoordsUtil.withBlock(super.getLightCoords(a), 15); 
/* 28 */     return super.getLightCoords(a);
/*    */   }
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 33 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 38 */     super.tick();
/* 39 */     setSpriteFromAge(this.sprites);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 46 */       this.sprite = sprite;
/*    */     }
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 51 */       SoulParticle particle = new SoulParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/* 52 */       particle.setAlpha(1.0F);
/* 53 */       return particle;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class EmissiveProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public EmissiveProvider(SpriteSet sprite) {
/* 61 */       this.sprite = sprite;
/*    */     }
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 66 */       SoulParticle particle = new SoulParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/* 67 */       particle.setAlpha(1.0F);
/* 68 */       particle.isGlowing = true;
/* 69 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SoulParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */