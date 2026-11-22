/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ import com.mojang.blaze3d.textures.GpuTexture;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class GlTexture extends GpuTexture implements FrameBufferAttachment {
/*    */   private static final int EMPTY = -1;
/*    */   
/*    */   protected final int id;
/*    */   
/*    */   private final FrameBufferCache frameBufferCache;
/*    */   
/* 15 */   private final List<FrameBufferCache.CacheKey> fboKeys = new ArrayList<>();
/*    */   
/*    */   protected boolean closed;
/*    */   
/*    */   private int views;
/*    */   
/*    */   protected GlTexture(int usage, String label, GpuFormat format, int width, int height, int depthOrLayers, int mipLevels, int id, FrameBufferCache frameBufferCache) {
/* 21 */     super(usage, label, format, width, height, depthOrLayers, mipLevels);
/* 22 */     this.id = id;
/* 23 */     this.frameBufferCache = frameBufferCache;
/*    */   }
/*    */   
/*    */   public void close() {
/* 28 */     if (this.closed)
/*    */       return; 
/* 31 */     this.closed = true;
/* 32 */     if (this.views == 0)
/* 33 */       destroyImmediately(); 
/*    */   }
/*    */   
/*    */   private void destroyImmediately() {
/* 38 */     while (!this.fboKeys.isEmpty())
/* 39 */       this.frameBufferCache.destroyFbo(this.fboKeys.getLast()); 
/* 41 */     GlStateManager._deleteTexture(this.id);
/*    */   }
/*    */   
/*    */   public boolean isClosed() {
/* 46 */     return this.closed;
/*    */   }
/*    */   
/*    */   public int glId() {
/* 51 */     return this.id;
/*    */   }
/*    */   
/*    */   public int fboMipLevel() {
/* 56 */     return 0;
/*    */   }
/*    */   
/*    */   public void addAssociatedFbo(FrameBufferCache.CacheKey fboKey) {
/* 61 */     this.fboKeys.add(fboKey);
/*    */   }
/*    */   
/*    */   public void removeAssociatedFbo(FrameBufferCache.CacheKey fboKey) {
/* 66 */     this.fboKeys.remove(fboKey);
/*    */   }
/*    */   
/*    */   public void addViews() {
/* 70 */     this.views++;
/*    */   }
/*    */   
/*    */   public void removeViews() {
/* 74 */     this.views--;
/* 76 */     if (this.closed && this.views == 0)
/* 77 */       destroyImmediately(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlTexture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */