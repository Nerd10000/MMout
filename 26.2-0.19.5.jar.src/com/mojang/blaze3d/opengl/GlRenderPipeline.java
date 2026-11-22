/*   */ package com.mojang.blaze3d.opengl;
/*   */ 
/*   */ import com.mojang.blaze3d.pipeline.CompiledRenderPipeline;
/*   */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*   */ 
/*   */ public final class GlRenderPipeline extends Record implements CompiledRenderPipeline {
/*   */   private final RenderPipeline info;
/*   */   
/*   */   private final GlProgram program;
/*   */   
/*   */   public GlRenderPipeline(RenderPipeline info, GlProgram program) {
/* 6 */     this.info = info;
/* 6 */     this.program = program;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/GlRenderPipeline;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlRenderPipeline;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlRenderPipeline;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public RenderPipeline info() {
/* 6 */     return this.info;
/*   */   }
/*   */   
/*   */   public GlProgram program() {
/* 6 */     return this.program;
/*   */   }
/*   */   
/*   */   public boolean isValid() {
/* 9 */     return (this.program != GlProgram.INVALID_PROGRAM);
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlRenderPipeline.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */