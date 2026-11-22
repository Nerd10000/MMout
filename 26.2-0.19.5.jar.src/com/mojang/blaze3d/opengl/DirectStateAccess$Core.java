/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.lwjgl.opengl.ARBDirectStateAccess;
/*     */ 
/*     */ class Core extends DirectStateAccess {
/*     */   public int createBuffer() {
/*  59 */     GlStateManager.incrementTrackedBuffers();
/*  60 */     return ARBDirectStateAccess.glCreateBuffers();
/*     */   }
/*     */   
/*     */   public void bufferData(int buffer, long size, int usage) {
/*  65 */     ARBDirectStateAccess.glNamedBufferData(buffer, size, GlConst.bufferUsageToGlEnum(usage));
/*     */   }
/*     */   
/*     */   public void bufferData(int buffer, ByteBuffer data, int usage) {
/*  70 */     ARBDirectStateAccess.glNamedBufferData(buffer, data, GlConst.bufferUsageToGlEnum(usage));
/*     */   }
/*     */   
/*     */   public void bufferSubData(int buffer, long offset, ByteBuffer data, int usage) {
/*  75 */     ARBDirectStateAccess.glNamedBufferSubData(buffer, offset, data);
/*     */   }
/*     */   
/*     */   public void bufferStorage(int buffer, long size, int usage) {
/*  80 */     ARBDirectStateAccess.glNamedBufferStorage(buffer, size, GlConst.bufferUsageToGlFlag(usage));
/*     */   }
/*     */   
/*     */   public void bufferStorage(int buffer, ByteBuffer data, int usage) {
/*  85 */     ARBDirectStateAccess.glNamedBufferStorage(buffer, data, GlConst.bufferUsageToGlFlag(usage));
/*     */   }
/*     */   
/*     */   public ByteBuffer mapBufferRange(int buffer, long offset, long length, int flags, int usage) {
/*  90 */     return ARBDirectStateAccess.glMapNamedBufferRange(buffer, offset, length, flags);
/*     */   }
/*     */   
/*     */   public void unmapBuffer(int buffer, int usage) {
/*  95 */     ARBDirectStateAccess.glUnmapNamedBuffer(buffer);
/*     */   }
/*     */   
/*     */   public int createFrameBufferObject() {
/* 100 */     return ARBDirectStateAccess.glCreateFramebuffers();
/*     */   }
/*     */   
/*     */   public void bindFrameBufferTextures(int fbo, int[] color, int[] colorMipLevels, int depth, int depthMipLevel, int bindSlot) {
/* 105 */     for (int i = 0; i < color.length; i++)
/* 106 */       ARBDirectStateAccess.glNamedFramebufferTexture(fbo, 36064 + i, color[i], colorMipLevels[i]); 
/* 108 */     ARBDirectStateAccess.glNamedFramebufferTexture(fbo, 36096, depth, depthMipLevel);
/* 109 */     if (bindSlot != 0)
/* 110 */       GlStateManager._glBindFramebuffer(bindSlot, fbo); 
/*     */   }
/*     */   
/*     */   public void blitFrameBuffers(int source, int dest, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
/* 116 */     ARBDirectStateAccess.glBlitNamedFramebuffer(source, dest, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
/*     */   }
/*     */   
/*     */   public void flushMappedBufferRange(int handle, long offset, long length, int usage) {
/* 121 */     ARBDirectStateAccess.glFlushMappedNamedBufferRange(handle, offset, length);
/*     */   }
/*     */   
/*     */   public void copyBufferSubData(int source, int target, long sourceOffset, long targetOffset, long length) {
/* 126 */     ARBDirectStateAccess.glCopyNamedBufferSubData(source, target, sourceOffset, targetOffset, length);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\DirectStateAccess$Core.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */