/*    */ package com.mojang.blaze3d.pipeline;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.CompareOp;
/*    */ 
/*    */ public final class DepthStencilState extends Record {
/*    */   private final CompareOp depthTest;
/*    */   
/*    */   private final boolean writeDepth;
/*    */   
/*    */   private final float depthBiasScaleFactor;
/*    */   
/*    */   private final float depthBiasConstant;
/*    */   
/*    */   public float depthBiasConstant() {
/*  5 */     return this.depthBiasConstant;
/*    */   }
/*    */   
/*    */   public float depthBiasScaleFactor() {
/*  5 */     return this.depthBiasScaleFactor;
/*    */   }
/*    */   
/*    */   public boolean writeDepth() {
/*  5 */     return this.writeDepth;
/*    */   }
/*    */   
/*    */   public CompareOp depthTest() {
/*  5 */     return this.depthTest;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/pipeline/DepthStencilState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/pipeline/DepthStencilState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/pipeline/DepthStencilState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/DepthStencilState;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/pipeline/DepthStencilState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/DepthStencilState;
/*    */   }
/*    */   
/*    */   public DepthStencilState(CompareOp depthTest, boolean writeDepth, float depthBiasScaleFactor, float depthBiasConstant) {
/*  5 */     this.depthTest = depthTest;
/*  5 */     this.writeDepth = writeDepth;
/*  5 */     this.depthBiasScaleFactor = depthBiasScaleFactor;
/*  5 */     this.depthBiasConstant = depthBiasConstant;
/*    */   }
/*    */   
/* 11 */   public static final DepthStencilState DEFAULT = new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true);
/*    */   
/*    */   public DepthStencilState(CompareOp depthTest, boolean depthWrite) {
/* 14 */     this(depthTest, depthWrite, 0.0F, 0.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\pipeline\DepthStencilState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */