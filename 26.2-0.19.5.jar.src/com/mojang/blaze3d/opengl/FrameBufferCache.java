/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ public class FrameBufferCache {
/*  12 */   private final Object2IntMap<CacheKey> cache = (Object2IntMap<CacheKey>)new Object2IntOpenHashMap();
/*     */   
/*     */   public int getFbo(DirectStateAccess dsa, List<FrameBufferAttachment> colorTextures, FrameBufferAttachment depthTexture) {
/*  15 */     CacheKey cacheKey = new CacheKey(colorTextures, depthTexture);
/*  16 */     return this.cache.computeIfAbsent(cacheKey, paramObject -> createFbo(cacheKey, dsa, colorTextures, depthTexture));
/*     */   }
/*     */   
/*     */   private int createFbo(CacheKey key, DirectStateAccess dsa, List<FrameBufferAttachment> colorAttachments, FrameBufferAttachment depthAttachment) {
/*  20 */     int fbo = dsa.createFrameBufferObject();
/*  22 */     int colorAttachmentCount = colorAttachments.size();
/*  23 */     int[] colorIds = new int[colorAttachmentCount];
/*  24 */     int[] mipLevels = new int[colorAttachmentCount];
/*  25 */     for (int i = 0; i < colorAttachmentCount; i++) {
/*  26 */       FrameBufferAttachment attachment = colorAttachments.get(i);
/*  27 */       if (attachment != null) {
/*  28 */         colorIds[i] = attachment.glId();
/*  29 */         mipLevels[i] = attachment.fboMipLevel();
/*  30 */         attachment.addAssociatedFbo(key);
/*     */       } else {
/*  32 */         colorIds[i] = 0;
/*  33 */         mipLevels[i] = 0;
/*     */       } 
/*     */     } 
/*  37 */     if (depthAttachment != null)
/*  38 */       depthAttachment.addAssociatedFbo(key); 
/*  41 */     dsa.bindFrameBufferTextures(fbo, colorIds, mipLevels, (depthAttachment == null) ? 0 : depthAttachment.glId(), (depthAttachment == null) ? 0 : depthAttachment.fboMipLevel(), 0);
/*  42 */     return fbo;
/*     */   }
/*     */   
/*     */   public void destroyFbo(CacheKey key) {
/*  46 */     if (!this.cache.containsKey(key))
/*     */       return; 
/*  49 */     for (FrameBufferAttachment associatedAttachment : key.associatedAttachments) {
/*  50 */       if (associatedAttachment != null)
/*  51 */         associatedAttachment.removeAssociatedFbo(key); 
/*     */     } 
/*  54 */     int fboId = this.cache.removeInt(key);
/*  55 */     GlStateManager._glDeleteFramebuffers(fboId);
/*     */   }
/*     */   
/*     */   public static class CacheKey {
/*     */     private final int[] data;
/*     */     
/*     */     private final int hash;
/*     */     
/*     */     public final List<FrameBufferAttachment> associatedAttachments;
/*     */     
/*     */     public CacheKey(List<FrameBufferAttachment> colorAttachments, FrameBufferAttachment depthAttachment) {
/*  65 */       int colorAttachmentCount = colorAttachments.size();
/*  66 */       this.data = new int[(colorAttachmentCount + ((depthAttachment != null) ? 1 : 0)) * 2];
/*  67 */       for (int i = 0; i < colorAttachmentCount; i++) {
/*  68 */         FrameBufferAttachment attachment = colorAttachments.get(i);
/*  69 */         if (attachment != null) {
/*  70 */           this.data[i * 2] = attachment.glId();
/*  71 */           this.data[i * 2 + 1] = attachment.fboMipLevel();
/*     */         } else {
/*  73 */           this.data[i * 2] = 0;
/*  74 */           this.data[i * 2 + 1] = 0;
/*     */         } 
/*     */       } 
/*  77 */       this.associatedAttachments = new ArrayList<>(colorAttachments);
/*  78 */       if (depthAttachment != null) {
/*  79 */         this.data[colorAttachmentCount * 2] = depthAttachment.glId();
/*  80 */         this.data[colorAttachmentCount * 2 + 1] = depthAttachment.fboMipLevel();
/*  81 */         this.associatedAttachments.add(depthAttachment);
/*     */       } 
/*  83 */       this.hash = Arrays.hashCode(this.data);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/*  88 */       return this.hash;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/*     */       CacheKey other;
/*  93 */       if (this == obj)
/*  94 */         return true; 
/*  96 */       if (obj instanceof CacheKey) {
/*  96 */         other = (CacheKey)obj;
/*     */       } else {
/*  97 */         return false;
/*     */       } 
/* 100 */       if (this.hash != other.hash)
/* 101 */         return false; 
/* 104 */       return Arrays.equals(this.data, other.data);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\FrameBufferCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */