/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ final class StagedAllocationEntry<T> extends Record implements AutoCloseable {
/*    */   private final StagingBuffer.BufferHandle buffer;
/*    */   
/*    */   private final UberGpuBuffer.UploadCallback<T> callback;
/*    */   
/*    */   private StagedAllocationEntry(StagingBuffer.BufferHandle buffer, UberGpuBuffer.UploadCallback<T> callback) {
/* 31 */     this.buffer = buffer;
/* 31 */     this.callback = callback;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vertex/UberGpuBuffer$StagedAllocationEntry<TT;>;
/*    */   }
/*    */   
/*    */   public StagingBuffer.BufferHandle buffer() {
/* 31 */     return this.buffer;
/*    */   }
/*    */   
/*    */   public UberGpuBuffer.UploadCallback<T> callback() {
/* 31 */     return this.callback;
/*    */   }
/*    */   
/*    */   public void close() {
/* 37 */     this.buffer.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\UberGpuBuffer$StagedAllocationEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */