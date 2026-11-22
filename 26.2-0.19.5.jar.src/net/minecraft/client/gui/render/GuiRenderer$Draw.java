/*     */ package net.minecraft.client.gui.render;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.renderer.StagedVertexBuffer;
/*     */ 
/*     */ final class Draw extends Record {
/*     */   private final StagedVertexBuffer.Draw draw;
/*     */   
/*     */   private final RenderPipeline pipeline;
/*     */   
/*     */   private final TextureSetup textureSetup;
/*     */   
/*     */   private final ScreenRectangle scissorArea;
/*     */   
/*     */   private Draw(StagedVertexBuffer.Draw draw, RenderPipeline pipeline, TextureSetup textureSetup, ScreenRectangle scissorArea) {
/* 108 */     this.draw = draw;
/* 108 */     this.pipeline = pipeline;
/* 108 */     this.textureSetup = textureSetup;
/* 108 */     this.scissorArea = scissorArea;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/GuiRenderer$Draw;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #108	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/render/GuiRenderer$Draw;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/GuiRenderer$Draw;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #108	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/render/GuiRenderer$Draw;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/GuiRenderer$Draw;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #108	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/render/GuiRenderer$Draw;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public StagedVertexBuffer.Draw draw() {
/* 108 */     return this.draw;
/*     */   }
/*     */   
/*     */   public RenderPipeline pipeline() {
/* 108 */     return this.pipeline;
/*     */   }
/*     */   
/*     */   public TextureSetup textureSetup() {
/* 108 */     return this.textureSetup;
/*     */   }
/*     */   
/*     */   public ScreenRectangle scissorArea() {
/* 108 */     return this.scissorArea;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\GuiRenderer$Draw.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */