/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Set;
/*     */ import org.lwjgl.opengl.ARBBufferStorage;
/*     */ import org.lwjgl.opengl.ARBDirectStateAccess;
/*     */ import org.lwjgl.opengl.GL33C;
/*     */ import org.lwjgl.opengl.GLCapabilities;
/*     */ 
/*     */ public abstract class DirectStateAccess {
/*     */   public static DirectStateAccess create(GLCapabilities capabilities, Set<String> enabledExtensions, GlHeuristics heuristics) {
/*  18 */     if (capabilities.GL_ARB_direct_state_access && GlDevice.USE_GL_ARB_direct_state_access && !heuristics.isGlOnDx12()) {
/*  19 */       enabledExtensions.add("GL_ARB_direct_state_access");
/*  20 */       return new Core();
/*     */     } 
/*  22 */     return new Emulated();
/*     */   }
/*     */   
/*     */   public abstract int createBuffer();
/*     */   
/*     */   public abstract void bufferData(int paramInt1, long paramLong, int paramInt2);
/*     */   
/*     */   public abstract void bufferData(int paramInt1, ByteBuffer paramByteBuffer, int paramInt2);
/*     */   
/*     */   public abstract void bufferSubData(int paramInt1, long paramLong, ByteBuffer paramByteBuffer, int paramInt2);
/*     */   
/*     */   public abstract void bufferStorage(int paramInt1, long paramLong, int paramInt2);
/*     */   
/*     */   public abstract void bufferStorage(int paramInt1, ByteBuffer paramByteBuffer, int paramInt2);
/*     */   
/*     */   public abstract ByteBuffer mapBufferRange(int paramInt1, long paramLong1, long paramLong2, int paramInt2, int paramInt3);
/*     */   
/*     */   public abstract void unmapBuffer(int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract int createFrameBufferObject();
/*     */   
/*     */   public abstract void bindFrameBufferTextures(int paramInt1, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt2, int paramInt3, int paramInt4);
/*     */   
/*     */   public void bindFrameBufferTextures(int fbo, int color, int depth, int mipLevel, int bindSlot) {
/*  47 */     bindFrameBufferTextures(fbo, new int[] { color }, new int[] { mipLevel }, depth, mipLevel, bindSlot);
/*     */   }
/*     */   
/*     */   public abstract void blitFrameBuffers(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12);
/*     */   
/*     */   public abstract void flushMappedBufferRange(int paramInt1, long paramLong1, long paramLong2, int paramInt2);
/*     */   
/*     */   public abstract void copyBufferSubData(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);
/*     */   
/*     */   private static class Core extends DirectStateAccess {
/*     */     public int createBuffer() {
/*  59 */       GlStateManager.incrementTrackedBuffers();
/*  60 */       return ARBDirectStateAccess.glCreateBuffers();
/*     */     }
/*     */     
/*     */     public void bufferData(int buffer, long size, int usage) {
/*  65 */       ARBDirectStateAccess.glNamedBufferData(buffer, size, GlConst.bufferUsageToGlEnum(usage));
/*     */     }
/*     */     
/*     */     public void bufferData(int buffer, ByteBuffer data, int usage) {
/*  70 */       ARBDirectStateAccess.glNamedBufferData(buffer, data, GlConst.bufferUsageToGlEnum(usage));
/*     */     }
/*     */     
/*     */     public void bufferSubData(int buffer, long offset, ByteBuffer data, int usage) {
/*  75 */       ARBDirectStateAccess.glNamedBufferSubData(buffer, offset, data);
/*     */     }
/*     */     
/*     */     public void bufferStorage(int buffer, long size, int usage) {
/*  80 */       ARBDirectStateAccess.glNamedBufferStorage(buffer, size, GlConst.bufferUsageToGlFlag(usage));
/*     */     }
/*     */     
/*     */     public void bufferStorage(int buffer, ByteBuffer data, int usage) {
/*  85 */       ARBDirectStateAccess.glNamedBufferStorage(buffer, data, GlConst.bufferUsageToGlFlag(usage));
/*     */     }
/*     */     
/*     */     public ByteBuffer mapBufferRange(int buffer, long offset, long length, int flags, int usage) {
/*  90 */       return ARBDirectStateAccess.glMapNamedBufferRange(buffer, offset, length, flags);
/*     */     }
/*     */     
/*     */     public void unmapBuffer(int buffer, int usage) {
/*  95 */       ARBDirectStateAccess.glUnmapNamedBuffer(buffer);
/*     */     }
/*     */     
/*     */     public int createFrameBufferObject() {
/* 100 */       return ARBDirectStateAccess.glCreateFramebuffers();
/*     */     }
/*     */     
/*     */     public void bindFrameBufferTextures(int fbo, int[] color, int[] colorMipLevels, int depth, int depthMipLevel, int bindSlot) {
/* 105 */       for (int i = 0; i < color.length; i++)
/* 106 */         ARBDirectStateAccess.glNamedFramebufferTexture(fbo, 36064 + i, color[i], colorMipLevels[i]); 
/* 108 */       ARBDirectStateAccess.glNamedFramebufferTexture(fbo, 36096, depth, depthMipLevel);
/* 109 */       if (bindSlot != 0)
/* 110 */         GlStateManager._glBindFramebuffer(bindSlot, fbo); 
/*     */     }
/*     */     
/*     */     public void blitFrameBuffers(int source, int dest, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
/* 116 */       ARBDirectStateAccess.glBlitNamedFramebuffer(source, dest, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
/*     */     }
/*     */     
/*     */     public void flushMappedBufferRange(int handle, long offset, long length, int usage) {
/* 121 */       ARBDirectStateAccess.glFlushMappedNamedBufferRange(handle, offset, length);
/*     */     }
/*     */     
/*     */     public void copyBufferSubData(int source, int target, long sourceOffset, long targetOffset, long length) {
/* 126 */       ARBDirectStateAccess.glCopyNamedBufferSubData(source, target, sourceOffset, targetOffset, length);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Emulated extends DirectStateAccess {
/*     */     public int createBuffer() {
/* 134 */       return GlStateManager._glGenBuffers();
/*     */     }
/*     */     
/*     */     public void bufferData(int buffer, long size, int usage) {
/* 139 */       int target = GlUtil.selectBufferBindTarget(usage);
/* 140 */       GlStateManager._glBindBuffer(target, buffer);
/* 141 */       GlStateManager._glBufferData(target, size, GlConst.bufferUsageToGlEnum(usage));
/* 142 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */     
/*     */     public void bufferData(int buffer, ByteBuffer data, int usage) {
/* 147 */       int target = GlUtil.selectBufferBindTarget(usage);
/* 148 */       GlStateManager._glBindBuffer(target, buffer);
/* 149 */       GlStateManager._glBufferData(target, data, GlConst.bufferUsageToGlEnum(usage));
/* 150 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */     
/*     */     public void bufferSubData(int buffer, long offset, ByteBuffer data, int usage) {
/* 155 */       int target = GlUtil.selectBufferBindTarget(usage);
/* 156 */       GlStateManager._glBindBuffer(target, buffer);
/* 157 */       GlStateManager._glBufferSubData(target, offset, data);
/* 158 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */     
/*     */     public void bufferStorage(int buffer, long size, int usage) {
/* 163 */       int target = GlUtil.selectBufferBindTarget(usage);
/* 164 */       GlStateManager._glBindBuffer(target, buffer);
/* 165 */       ARBBufferStorage.glBufferStorage(target, size, GlConst.bufferUsageToGlFlag(usage));
/* 166 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */     
/*     */     public void bufferStorage(int buffer, ByteBuffer data, int usage) {
/* 171 */       int target = GlUtil.selectBufferBindTarget(usage);
/* 172 */       GlStateManager._glBindBuffer(target, buffer);
/* 173 */       ARBBufferStorage.glBufferStorage(target, data, GlConst.bufferUsageToGlFlag(usage));
/* 174 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */     
/*     */     public ByteBuffer mapBufferRange(int buffer, long offset, long length, int access, int usage) {
/* 179 */       int target = GlUtil.selectBufferBindTarget(usage);
/* 180 */       GlStateManager._glBindBuffer(target, buffer);
/* 181 */       ByteBuffer byteBuffer = GlStateManager._glMapBufferRange(target, offset, length, access);
/* 182 */       GlStateManager._glBindBuffer(target, 0);
/* 183 */       return byteBuffer;
/*     */     }
/*     */     
/*     */     public void unmapBuffer(int buffer, int usage) {
/* 188 */       int target = GlUtil.selectBufferBindTarget(usage);
/* 189 */       GlStateManager._glBindBuffer(target, buffer);
/* 190 */       GlStateManager._glUnmapBuffer(target);
/* 191 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */     
/*     */     public void flushMappedBufferRange(int buffer, long offset, long length, int usage) {
/* 196 */       int target = GlUtil.selectBufferBindTarget(usage);
/* 197 */       GlStateManager._glBindBuffer(target, buffer);
/* 198 */       GL33C.glFlushMappedBufferRange(target, offset, length);
/* 199 */       GlStateManager._glBindBuffer(target, 0);
/*     */     }
/*     */     
/*     */     public void copyBufferSubData(int source, int target, long sourceOffset, long targetOffset, long length) {
/* 204 */       GlStateManager._glBindBuffer(36662, source);
/* 205 */       GlStateManager._glBindBuffer(36663, target);
/* 206 */       GL33C.glCopyBufferSubData(36662, 36663, sourceOffset, targetOffset, length);
/* 207 */       GlStateManager._glBindBuffer(36662, 0);
/* 208 */       GlStateManager._glBindBuffer(36663, 0);
/*     */     }
/*     */     
/*     */     public int createFrameBufferObject() {
/* 213 */       return GlStateManager.glGenFramebuffers();
/*     */     }
/*     */     
/*     */     public void bindFrameBufferTextures(int fbo, int[] color, int[] colorMipLevels, int depth, int depthMipLevel, int bindSlot) {
/* 218 */       int tempBindSlot = (bindSlot == 0) ? 36009 : bindSlot;
/* 219 */       int oldFbo = GlStateManager.getFrameBuffer(tempBindSlot);
/* 220 */       GlStateManager._glBindFramebuffer(tempBindSlot, fbo);
/* 221 */       for (int i = 0; i < color.length; i++)
/* 222 */         GlStateManager._glFramebufferTexture2D(tempBindSlot, 36064 + i, 3553, color[i], colorMipLevels[i]); 
/* 224 */       GlStateManager._glFramebufferTexture2D(tempBindSlot, 36096, 3553, depth, depthMipLevel);
/* 225 */       if (bindSlot == 0)
/* 226 */         GlStateManager._glBindFramebuffer(tempBindSlot, oldFbo); 
/*     */     }
/*     */     
/*     */     public void blitFrameBuffers(int source, int dest, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
/* 232 */       int oldRead = GlStateManager.getFrameBuffer(36008);
/* 233 */       int oldDraw = GlStateManager.getFrameBuffer(36009);
/* 234 */       GlStateManager._glBindFramebuffer(36008, source);
/* 235 */       GlStateManager._glBindFramebuffer(36009, dest);
/* 236 */       GlStateManager._glBlitFrameBuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
/* 237 */       GlStateManager._glBindFramebuffer(36008, oldRead);
/* 238 */       GlStateManager._glBindFramebuffer(36009, oldDraw);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\DirectStateAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */