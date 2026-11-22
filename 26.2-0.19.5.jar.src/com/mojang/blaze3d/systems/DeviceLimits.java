/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ 
/*    */ public final class DeviceLimits extends Record {
/*    */   private final int maxAnisotropy;
/*    */   
/*    */   private final int minUniformOffsetAlignment;
/*    */   
/*    */   private final int maxTextureSize;
/*    */   
/*    */   private final long maxMemoryAllocationSize;
/*    */   
/*    */   private final int maxMultiDrawDirectInterleavedDrawCount;
/*    */   
/*    */   private final int maxColorAttachments;
/*    */   
/*    */   public DeviceLimits(int maxAnisotropy, int minUniformOffsetAlignment, int maxTextureSize, long maxMemoryAllocationSize, int maxMultiDrawDirectInterleavedDrawCount, int maxColorAttachments) {
/* 12 */     this.maxAnisotropy = maxAnisotropy;
/* 12 */     this.minUniformOffsetAlignment = minUniformOffsetAlignment;
/* 12 */     this.maxTextureSize = maxTextureSize;
/* 12 */     this.maxMemoryAllocationSize = maxMemoryAllocationSize;
/* 12 */     this.maxMultiDrawDirectInterleavedDrawCount = maxMultiDrawDirectInterleavedDrawCount;
/* 12 */     this.maxColorAttachments = maxColorAttachments;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/DeviceLimits;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/DeviceLimits;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/DeviceLimits;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/DeviceLimits;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/DeviceLimits;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/systems/DeviceLimits;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int maxAnisotropy() {
/* 12 */     return this.maxAnisotropy;
/*    */   }
/*    */   
/*    */   public int minUniformOffsetAlignment() {
/* 12 */     return this.minUniformOffsetAlignment;
/*    */   }
/*    */   
/*    */   public int maxTextureSize() {
/* 12 */     return this.maxTextureSize;
/*    */   }
/*    */   
/*    */   public long maxMemoryAllocationSize() {
/* 12 */     return this.maxMemoryAllocationSize;
/*    */   }
/*    */   
/*    */   public int maxMultiDrawDirectInterleavedDrawCount() {
/* 12 */     return this.maxMultiDrawDirectInterleavedDrawCount;
/*    */   }
/*    */   
/*    */   public int maxColorAttachments() {
/* 12 */     return this.maxColorAttachments;
/*    */   }
/*    */   
/*    */   public int maxTextureSizeForFormat(GpuFormat format) {
/* 21 */     return Integer.highestOneBit(Math.min(this.maxTextureSize, (int)Math.sqrt(this.maxMemoryAllocationSize / format.blockSize())));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\DeviceLimits.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */