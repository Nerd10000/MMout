/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ final class GlAllocation extends Record {
/*     */   private final int glBuffer;
/*     */   
/*     */   private final long hostPtr;
/*     */   
/*     */   private final long size;
/*     */   
/*     */   private GlAllocation(int glBuffer, long hostPtr, long size) {
/* 218 */     this.glBuffer = glBuffer;
/* 218 */     this.hostPtr = hostPtr;
/* 218 */     this.size = size;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #218	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #218	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #218	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int glBuffer() {
/* 218 */     return this.glBuffer;
/*     */   }
/*     */   
/*     */   public long hostPtr() {
/* 218 */     return this.hostPtr;
/*     */   }
/*     */   
/*     */   public long size() {
/* 218 */     return this.size;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlTransientMemory$PersistentMapping$GlAllocation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */