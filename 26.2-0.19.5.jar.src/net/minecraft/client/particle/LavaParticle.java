/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class LavaParticle extends SingleQuadParticle {
/*    */   private LavaParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
/* 13 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
/* 14 */     this.gravity = 0.75F;
/* 15 */     this.friction = 0.999F;
/* 16 */     this.xd *= 0.800000011920929D;
/* 17 */     this.yd *= 0.800000011920929D;
/* 18 */     this.zd *= 0.800000011920929D;
/* 19 */     this.yd = (this.random.nextFloat() * 0.4F + 0.05F);
/* 21 */     this.quadSize *= this.random.nextFloat() * 2.0F + 0.2F;
/* 23 */     this.lifetime = (int)(16.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*    */   }
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 28 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */   
/*    */   public int getLightCoords(float a) {
/* 33 */     return LightCoordsUtil.withBlock(super.getLightCoords(a), 15);
/*    */   }
/*    */   
/*    */   public float getQuadSize(float a) {
/* 38 */     float s = (this.age + a) / this.lifetime;
/* 39 */     return this.quadSize * (1.0F - s * s);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 44 */     super.tick();
/* 45 */     if (!this.removed) {
/* 46 */       float odds = this.age / this.lifetime;
/* 47 */       if (this.random.nextFloat() > odds)
/* 48 */         this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, this.x, this.y, this.z, this.xd, this.yd, this.zd); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 57 */       this.sprite = sprite;
/*    */     }
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 62 */       LavaParticle particle = new LavaParticle(level, x, y, z, this.sprite.get(random));
/* 63 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\LavaParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */