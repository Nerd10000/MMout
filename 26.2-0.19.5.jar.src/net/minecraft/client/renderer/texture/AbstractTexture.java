/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.AddressMode;
/*    */ import com.mojang.blaze3d.textures.FilterMode;
/*    */ import com.mojang.blaze3d.textures.GpuSampler;
/*    */ import com.mojang.blaze3d.textures.GpuTexture;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ 
/*    */ public abstract class AbstractTexture implements AutoCloseable {
/*    */   protected GpuTexture texture;
/*    */   
/*    */   protected GpuTextureView textureView;
/*    */   
/* 14 */   protected GpuSampler sampler = RenderSystem.getSamplerCache().getSampler(AddressMode.REPEAT, AddressMode.REPEAT, FilterMode.NEAREST, FilterMode.LINEAR, false);
/*    */   
/*    */   public void close() {
/* 18 */     releaseTextures();
/*    */   }
/*    */   
/*    */   protected void releaseTextures() {
/* 22 */     if (this.texture != null) {
/* 23 */       this.texture.close();
/* 24 */       this.texture = null;
/*    */     } 
/* 26 */     if (this.textureView != null) {
/* 27 */       this.textureView.close();
/* 28 */       this.textureView = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public GpuTexture getTexture() {
/* 33 */     if (this.texture == null)
/* 34 */       throw new IllegalStateException("Texture does not exist, can't get it before something initializes it"); 
/* 36 */     return this.texture;
/*    */   }
/*    */   
/*    */   public GpuTextureView getTextureView() {
/* 40 */     if (this.textureView == null)
/* 41 */       throw new IllegalStateException("Texture view does not exist, can't get it before something initializes it"); 
/* 43 */     return this.textureView;
/*    */   }
/*    */   
/*    */   public GpuSampler getSampler() {
/* 47 */     return this.sampler;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\AbstractTexture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */