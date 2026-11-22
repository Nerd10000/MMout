/*    */ package com.mojang.blaze3d.buffers;
/*    */ 
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ 
/*    */ public abstract class GpuBuffer implements AutoCloseable {
/*    */   public static final int USAGE_MAP_READ = 1;
/*    */   
/*    */   public static final int USAGE_MAP_WRITE = 2;
/*    */   
/*    */   public static final int USAGE_HINT_CLIENT_STORAGE = 4;
/*    */   
/*    */   public static final int USAGE_COPY_DST = 8;
/*    */   
/*    */   public static final int USAGE_COPY_SRC = 16;
/*    */   
/*    */   public static final int USAGE_VERTEX = 32;
/*    */   
/*    */   public static final int USAGE_INDEX = 64;
/*    */   
/*    */   public static final int USAGE_UNIFORM = 128;
/*    */   
/*    */   public static final int USAGE_UNIFORM_TEXEL_BUFFER = 256;
/*    */   
/*    */   public static final int USAGE_INDIRECT_PARAMETERS = 512;
/*    */   
/*    */   private final int usage;
/*    */   
/*    */   private final long size;
/*    */   
/*    */   private final GpuBufferSlice defaultSlice;
/*    */   
/*    */   public GpuBuffer(int usage, long size) {
/* 46 */     this.size = size;
/* 47 */     this.usage = usage;
/* 48 */     this.defaultSlice = new GpuBufferSlice(this, 0L, size);
/*    */   }
/*    */   
/*    */   public long size() {
/* 52 */     return this.size;
/*    */   }
/*    */   
/*    */   public int usage() {
/* 56 */     return this.usage;
/*    */   }
/*    */   
/*    */   public abstract boolean isClosed();
/*    */   
/*    */   public abstract void close();
/*    */   
/*    */   public GpuBufferSlice slice(long offset, long length) {
/* 65 */     if (offset < 0L || length < 0L || offset + length > this.size)
/* 66 */       throw new IllegalArgumentException("Offset of " + offset + " and length " + length + " would put new slice outside buffer's range (of 0," + length + ")"); 
/* 68 */     return new GpuBufferSlice(this, offset, length);
/*    */   }
/*    */   
/*    */   public GpuBufferSlice slice() {
/* 72 */     return this.defaultSlice;
/*    */   }
/*    */   
/*    */   public GpuBufferSlice.MappedView map(boolean read, boolean write) {
/* 77 */     return map(0L, this.size, read, write);
/*    */   }
/*    */   
/*    */   public abstract GpuBufferSlice.MappedView map(long paramLong1, long paramLong2, boolean paramBoolean1, boolean paramBoolean2);
/*    */   
/*    */   @Retention(RetentionPolicy.CLASS)
/*    */   @Target({ElementType.TYPE_USE})
/*    */   public static @interface Usage {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\buffers\GpuBuffer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */