/*    */ package com.mojang.blaze3d.buffers;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ public final class MappedView extends Record implements AutoCloseable {
/*    */   private final GpuBufferSlice slice;
/*    */   
/*    */   private final ByteBuffer data;
/*    */   
/*    */   private final Runnable onClose;
/*    */   
/*    */   public MappedView(GpuBufferSlice slice, ByteBuffer data, Runnable onClose) {
/* 20 */     this.slice = slice;
/* 20 */     this.data = data;
/* 20 */     this.onClose = onClose;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/buffers/GpuBufferSlice$MappedView;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/buffers/GpuBufferSlice$MappedView;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/buffers/GpuBufferSlice$MappedView;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/buffers/GpuBufferSlice$MappedView;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/buffers/GpuBufferSlice$MappedView;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/buffers/GpuBufferSlice$MappedView;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public GpuBufferSlice slice() {
/* 20 */     return this.slice;
/*    */   }
/*    */   
/*    */   public ByteBuffer data() {
/* 20 */     return this.data;
/*    */   }
/*    */   
/*    */   public Runnable onClose() {
/* 20 */     return this.onClose;
/*    */   }
/*    */   
/*    */   public void close() {
/* 23 */     this.onClose.run();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\buffers\GpuBufferSlice$MappedView.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */