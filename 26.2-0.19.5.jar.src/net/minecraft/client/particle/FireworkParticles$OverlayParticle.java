/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class OverlayParticle extends SingleQuadParticle {
/*     */   private OverlayParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
/* 304 */     super(level, x, y, z, sprite);
/* 305 */     this.lifetime = 4;
/*     */   }
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/* 310 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*     */   }
/*     */   
/*     */   public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
/* 315 */     setAlpha(0.6F - (this.age + partialTickTime - 1.0F) * 0.25F * 0.5F);
/* 316 */     super.extract(particleTypeRenderState, camera, partialTickTime);
/*     */   }
/*     */   
/*     */   public float getQuadSize(float a) {
/* 321 */     return 7.1F * Mth.sin(((this.age + a - 1.0F) * 0.25F * 3.1415927F));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\FireworkParticles$OverlayParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */