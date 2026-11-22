/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ final class GlAllocation extends Record {
/*    */   private final GlBuffer glBuffer;
/*    */   
/*    */   private final long hostBuffer;
/*    */   
/*    */   private GlAllocation(GlBuffer glBuffer, long hostBuffer) {
/* 64 */     this.glBuffer = glBuffer;
/* 64 */     this.hostBuffer = hostBuffer;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public GlBuffer glBuffer() {
/* 64 */     return this.glBuffer;
/*    */   }
/*    */   
/*    */   public long hostBuffer() {
/* 64 */     return this.hostBuffer;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlTransientMemory$Fallback$GlAllocation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */