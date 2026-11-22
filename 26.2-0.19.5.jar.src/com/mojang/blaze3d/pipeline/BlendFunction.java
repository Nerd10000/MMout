/*    */ package com.mojang.blaze3d.pipeline;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.BlendFactor;
/*    */ import com.mojang.blaze3d.platform.BlendOp;
/*    */ 
/*    */ public final class BlendFunction extends Record {
/*    */   private final BlendEquation color;
/*    */   
/*    */   private final BlendEquation alpha;
/*    */   
/*    */   public BlendFunction(BlendEquation color, BlendEquation alpha) {
/*  6 */     this.color = color;
/*  6 */     this.alpha = alpha;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/pipeline/BlendFunction;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/BlendFunction;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/pipeline/BlendFunction;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/BlendFunction;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/pipeline/BlendFunction;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/pipeline/BlendFunction;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlendEquation color() {
/*  6 */     return this.color;
/*    */   }
/*    */   
/*    */   public BlendEquation alpha() {
/*  6 */     return this.alpha;
/*    */   }
/*    */   
/*  7 */   public static final BlendFunction LIGHTNING = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE);
/*    */   
/*  8 */   public static final BlendFunction GLINT = new BlendFunction(BlendFactor.SRC_COLOR, BlendFactor.ONE, BlendFactor.ZERO, BlendFactor.ONE);
/*    */   
/*  9 */   public static final BlendFunction OVERLAY = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE, BlendFactor.ONE, BlendFactor.ZERO);
/*    */   
/* 10 */   public static final BlendFunction TRANSLUCENT = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA, BlendFactor.ONE, BlendFactor.ONE_MINUS_SRC_ALPHA);
/*    */   
/* 11 */   public static final BlendFunction TRANSLUCENT_PREMULTIPLIED_ALPHA = new BlendFunction(BlendFactor.ONE, BlendFactor.ONE_MINUS_SRC_ALPHA, BlendFactor.ONE, BlendFactor.ONE_MINUS_SRC_ALPHA);
/*    */   
/* 12 */   public static final BlendFunction ADDITIVE = new BlendFunction(BlendFactor.ONE, BlendFactor.ONE);
/*    */   
/* 13 */   public static final BlendFunction ENTITY_OUTLINE_BLIT = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA, BlendFactor.ZERO, BlendFactor.ONE);
/*    */   
/* 14 */   public static final BlendFunction INVERT = new BlendFunction(BlendFactor.ONE_MINUS_DST_COLOR, BlendFactor.ONE_MINUS_SRC_COLOR, BlendFactor.ONE, BlendFactor.ZERO);
/*    */   
/*    */   public BlendFunction(BlendFactor srcColorFactor, BlendFactor dstColorFactor, BlendOp colorOp, BlendFactor srcAlphaFactor, BlendFactor dstAlphaFactor, BlendOp alphaOp) {
/* 17 */     this(new BlendEquation(srcColorFactor, dstColorFactor, colorOp), new BlendEquation(srcAlphaFactor, dstAlphaFactor, alphaOp));
/*    */   }
/*    */   
/*    */   public BlendFunction(BlendFactor srcColorFactor, BlendFactor dstColorFactor, BlendFactor srcAlphaFactor, BlendFactor dstAlphaFactor) {
/* 21 */     this(srcColorFactor, dstColorFactor, BlendOp.ADD, srcAlphaFactor, dstAlphaFactor, BlendOp.ADD);
/*    */   }
/*    */   
/*    */   public BlendFunction(BlendEquation equation) {
/* 25 */     this(equation, equation);
/*    */   }
/*    */   
/*    */   public BlendFunction(BlendFactor srcFactor, BlendFactor dstFactor, BlendOp op) {
/* 29 */     this(new BlendEquation(srcFactor, dstFactor, op));
/*    */   }
/*    */   
/*    */   public BlendFunction(BlendFactor srcFactor, BlendFactor dstFactor) {
/* 33 */     this(new BlendEquation(srcFactor, dstFactor, BlendOp.ADD));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\pipeline\BlendFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */