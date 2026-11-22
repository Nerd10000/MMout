/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ public class Direct extends GlBuffer {
/*     */   private boolean closed;
/*     */   
/*     */   private final DirectStateAccess dsa;
/*     */   
/*     */   protected final int mappingFlags;
/*     */   
/*     */   protected ByteBuffer mappedBuffer;
/*     */   
/*     */   protected Direct(DirectStateAccess dsa, int usage, long size, int handle, boolean canPersistentMap) {
/*  65 */     super(usage, size, handle, canPersistentMap);
/*  67 */     if (canPersistentMap && (usage & 0x3) != 0)
/*  68 */       map(0L, size, ((usage & 0x1) != 0), ((usage & 0x2) != 0)); 
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/*  74 */     return this.closed;
/*     */   }
/*     */   
/*     */   public void close() {
/*  79 */     if (this.closed)
/*     */       return; 
/*  82 */     this.closed = true;
/*  83 */     if (this.canPersistentMap && (usage() & 0x3) != 0)
/*  84 */       unmap(); 
/*  86 */     if (this.mappingRefCount != 0)
/*  87 */       throw new IllegalStateException("Attempt to close a mapped buffer"); 
/*  89 */     GlStateManager._glDeleteBuffers(handle());
/*  90 */     MEMORY_POOL.free(handle());
/*     */   }
/*     */   
/*     */   public GpuBufferSlice.MappedView map(long offset, long length, boolean read, boolean write) {
/*  97 */     if (isClosed())
/*  98 */       throw new IllegalStateException("Buffer already closed"); 
/* 100 */     if (!read && !write)
/* 101 */       throw new IllegalArgumentException("At least read or write must be true"); 
/* 103 */     if (read && (usage() & 0x1) == 0)
/* 104 */       throw new IllegalStateException("Buffer is not readable"); 
/* 106 */     if (write && (usage() & 0x2) == 0)
/* 107 */       throw new IllegalStateException("Buffer is not writable"); 
/* 109 */     if (offset + length > size())
/* 110 */       throw new IllegalArgumentException("Cannot map more data than this buffer can hold (attempting to map " + length + " bytes at offset " + offset + " from " + size() + " size buffer)"); 
/* 113 */     if (offset > 2147483647L || length > 2147483647L)
/* 114 */       throw new IllegalArgumentException("Mapping buffers larger than 2GB is not supported"); 
/* 116 */     if (offset < 0L || length < 0L)
/* 117 */       throw new IllegalArgumentException("Offset or length must be positive integer values"); 
/* 120 */     this.mappingRefCount++;
/* 121 */     if (this.mappedBuffer == null) {
/* 123 */       GlStateManager.clearGlErrors();
/* 124 */       this.mappedBuffer = this.dsa.mapBufferRange(handle(), 0L, size(), this.mappingFlags, usage());
/* 125 */       if (this.mappedBuffer == null)
/* 126 */         throw new IllegalStateException("Failed to map buffer"); 
/*     */     } 
/* 130 */     return new GpuBufferSlice.MappedView(slice(offset, length), MemoryUtil.memSlice(this.mappedBuffer, (int)offset, (int)length), new Runnable() {
/*     */           private boolean closed = false;
/*     */           
/*     */           public void run() {
/* 135 */             if (this.closed)
/*     */               return; 
/* 138 */             this.closed = true;
/* 139 */             if ((GlBuffer.Direct.this.mappingFlags & 0x10) != 0)
/* 140 */               GlBuffer.Direct.this.dsa.flushMappedBufferRange(GlBuffer.Direct.this.handle(), GlBuffer.Direct.this.slice().offset(), GlBuffer.Direct.this.slice().length(), GlBuffer.Direct.this.usage()); 
/* 142 */             GlBuffer.Direct.this.unmap();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void unmap() {
/* 148 */     this.mappingRefCount--;
/* 149 */     if (this.mappingRefCount == 0) {
/* 150 */       this.dsa.unmapBuffer(handle(), usage());
/* 151 */       this.mappedBuffer = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlBuffer$Direct.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */