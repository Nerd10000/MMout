/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ShriekParticleOption;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import org.joml.Quaternionf;
/*    */ 
/*    */ public class ShriekParticle extends SingleQuadParticle {
/*    */   private static final float MAGICAL_X_ROT = 1.0472F;
/*    */   
/*    */   private int delay;
/*    */   
/*    */   private ShriekParticle(ClientLevel level, double x, double y, double z, int delay, TextureAtlasSprite sprite) {
/* 20 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
/* 22 */     this.quadSize = 0.85F;
/* 24 */     this.delay = delay;
/* 25 */     this.lifetime = 30;
/* 26 */     this.gravity = 0.0F;
/* 28 */     this.xd = 0.0D;
/* 29 */     this.yd = 0.1D;
/* 30 */     this.zd = 0.0D;
/*    */   }
/*    */   
/*    */   public float getQuadSize(float a) {
/* 35 */     return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 0.75F, 0.0F, 1.0F);
/*    */   }
/*    */   
/*    */   public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
/* 40 */     if (this.delay > 0)
/*    */       return; 
/* 44 */     this.alpha = 1.0F - Mth.clamp((this.age + partialTickTime) / this.lifetime, 0.0F, 1.0F);
/* 46 */     Quaternionf rotation = new Quaternionf();
/* 47 */     rotation.rotationX(-1.0472F);
/* 48 */     extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
/* 50 */     rotation.rotationYXZ(-3.1415927F, 1.0472F, 0.0F);
/* 51 */     extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
/*    */   }
/*    */   
/*    */   public int getLightCoords(float a) {
/* 56 */     return LightCoordsUtil.withBlock(super.getLightCoords(a), 15);
/*    */   }
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 61 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 66 */     if (this.delay > 0) {
/* 67 */       this.delay--;
/*    */       return;
/*    */     } 
/* 71 */     super.tick();
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<ShriekParticleOption> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 78 */       this.sprite = sprite;
/*    */     }
/*    */     
/*    */     public Particle createParticle(ShriekParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 83 */       ShriekParticle particle = new ShriekParticle(level, x, y, z, options.getDelay(), this.sprite.get(random));
/* 84 */       particle.setAlpha(1.0F);
/* 85 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ShriekParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */