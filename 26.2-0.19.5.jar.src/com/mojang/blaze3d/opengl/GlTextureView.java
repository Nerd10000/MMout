/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.textures.GpuTexture;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class GlTextureView extends GpuTextureView implements FrameBufferAttachment {
/*    */   private static final int EMPTY = -1;
/*    */   
/*    */   private boolean closed;
/*    */   
/*    */   private final FrameBufferCache frameBufferCache;
/*    */   
/* 14 */   private final List<FrameBufferCache.CacheKey> fboKeys = new ArrayList<>();
/*    */   
/*    */   protected GlTextureView(GlTexture texture, int baseMipLevel, int mipLevels, FrameBufferCache frameBufferCache) {
/* 17 */     super(texture, baseMipLevel, mipLevels);
/* 18 */     texture.addViews();
/* 19 */     this.frameBufferCache = frameBufferCache;
/*    */   }
/*    */   
/*    */   public boolean isClosed() {
/* 24 */     return this.closed;
/*    */   }
/*    */   
/*    */   public void close() {
/* 29 */     if (!this.closed) {
/* 30 */       this.closed = true;
/* 31 */       texture().removeViews();
/* 33 */       while (!this.fboKeys.isEmpty())
/* 34 */         this.frameBufferCache.destroyFbo(this.fboKeys.getLast()); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public GlTexture texture() {
/* 41 */     return (GlTexture)super.texture();
/*    */   }
/*    */   
/*    */   public int glId() {
/* 46 */     return (texture()).id;
/*    */   }
/*    */   
/*    */   public int fboMipLevel() {
/* 51 */     return baseMipLevel();
/*    */   }
/*    */   
/*    */   public void addAssociatedFbo(FrameBufferCache.CacheKey fboKey) {
/* 56 */     this.fboKeys.add(fboKey);
/*    */   }
/*    */   
/*    */   public void removeAssociatedFbo(FrameBufferCache.CacheKey fboKey) {
/* 61 */     this.fboKeys.remove(fboKey);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlTextureView.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */