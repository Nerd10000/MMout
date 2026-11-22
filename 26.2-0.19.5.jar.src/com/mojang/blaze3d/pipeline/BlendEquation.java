/*   */ package com.mojang.blaze3d.pipeline;
/*   */ 
/*   */ import com.mojang.blaze3d.platform.BlendFactor;
/*   */ import com.mojang.blaze3d.platform.BlendOp;
/*   */ 
/*   */ public final class BlendEquation extends Record {
/*   */   private final BlendFactor sourceFactor;
/*   */   
/*   */   private final BlendFactor destFactor;
/*   */   
/*   */   private final BlendOp op;
/*   */   
/*   */   public BlendEquation(BlendFactor sourceFactor, BlendFactor destFactor, BlendOp op) {
/* 6 */     this.sourceFactor = sourceFactor;
/* 6 */     this.destFactor = destFactor;
/* 6 */     this.op = op;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/pipeline/BlendEquation;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/BlendEquation;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/pipeline/BlendEquation;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/BlendEquation;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/pipeline/BlendEquation;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/blaze3d/pipeline/BlendEquation;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public BlendFactor sourceFactor() {
/* 6 */     return this.sourceFactor;
/*   */   }
/*   */   
/*   */   public BlendFactor destFactor() {
/* 6 */     return this.destFactor;
/*   */   }
/*   */   
/*   */   public BlendOp op() {
/* 6 */     return this.op;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\pipeline\BlendEquation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */