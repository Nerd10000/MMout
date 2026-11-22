/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ public class CacheKey {
/*     */   private final int[] data;
/*     */   
/*     */   private final int hash;
/*     */   
/*     */   public final List<FrameBufferAttachment> associatedAttachments;
/*     */   
/*     */   public CacheKey(List<FrameBufferAttachment> colorAttachments, FrameBufferAttachment depthAttachment) {
/*  65 */     int colorAttachmentCount = colorAttachments.size();
/*  66 */     this.data = new int[(colorAttachmentCount + ((depthAttachment != null) ? 1 : 0)) * 2];
/*  67 */     for (int i = 0; i < colorAttachmentCount; i++) {
/*  68 */       FrameBufferAttachment attachment = colorAttachments.get(i);
/*  69 */       if (attachment != null) {
/*  70 */         this.data[i * 2] = attachment.glId();
/*  71 */         this.data[i * 2 + 1] = attachment.fboMipLevel();
/*     */       } else {
/*  73 */         this.data[i * 2] = 0;
/*  74 */         this.data[i * 2 + 1] = 0;
/*     */       } 
/*     */     } 
/*  77 */     this.associatedAttachments = new ArrayList<>(colorAttachments);
/*  78 */     if (depthAttachment != null) {
/*  79 */       this.data[colorAttachmentCount * 2] = depthAttachment.glId();
/*  80 */       this.data[colorAttachmentCount * 2 + 1] = depthAttachment.fboMipLevel();
/*  81 */       this.associatedAttachments.add(depthAttachment);
/*     */     } 
/*  83 */     this.hash = Arrays.hashCode(this.data);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  88 */     return this.hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/*     */     CacheKey other;
/*  93 */     if (this == obj)
/*  94 */       return true; 
/*  96 */     if (obj instanceof CacheKey) {
/*  96 */       other = (CacheKey)obj;
/*     */     } else {
/*  97 */       return false;
/*     */     } 
/* 100 */     if (this.hash != other.hash)
/* 101 */       return false; 
/* 104 */     return Arrays.equals(this.data, other.data);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\FrameBufferCache$CacheKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */