/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.GpuQueryPool;
/*    */ import java.util.OptionalLong;
/*    */ import org.lwjgl.opengl.GL33C;
/*    */ 
/*    */ public class GlQueryPool implements GpuQueryPool {
/*    */   private final int[] handles;
/*    */   
/*    */   private boolean closed;
/*    */   
/*    */   public GlQueryPool(int size) {
/* 13 */     this.handles = new int[size];
/* 14 */     GL33C.glGenQueries(this.handles);
/*    */   }
/*    */   
/*    */   public int size() {
/* 19 */     return this.handles.length;
/*    */   }
/*    */   
/*    */   public OptionalLong getValue(int index) {
/* 24 */     int handle = this.handles[index];
/* 25 */     if (GL33C.glGetQueryObjecti(handle, 34919) == 0)
/* 26 */       return OptionalLong.empty(); 
/* 29 */     return OptionalLong.of(GL33C.glGetQueryObjectui64(handle, 34918));
/*    */   }
/*    */   
/*    */   public OptionalLong[] getValues(int index, int count) {
/* 34 */     if (index + count > this.handles.length)
/* 35 */       throw new IndexOutOfBoundsException("getValues would read out-of-bounds for an array of " + count + " starting at " + index + ", when total size is " + this.handles.length); 
/* 37 */     OptionalLong[] result = new OptionalLong[count];
/* 40 */     for (int i = 0; i < count; i++)
/* 41 */       result[i] = getValue(index + i); 
/* 44 */     return result;
/*    */   }
/*    */   
/*    */   protected void writeTimestamp(int index) {
/* 48 */     GL33C.glQueryCounter(this.handles[index], 36392);
/*    */   }
/*    */   
/*    */   public void close() {
/* 53 */     if (!this.closed) {
/* 54 */       this.closed = true;
/* 55 */       GL33C.glDeleteQueries(this.handles);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlQueryPool.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */