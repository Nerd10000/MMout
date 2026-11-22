/*    */ package net.minecraft.client.renderer.state.gui;
/*    */ 
/*    */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.gui.render.TextureSetup;
/*    */ import org.joml.Matrix3x2fc;
/*    */ 
/*    */ public final class ColoredRectangleRenderState extends Record implements GuiElementRenderState {
/*    */   private final RenderPipeline pipeline;
/*    */   
/*    */   private final TextureSetup textureSetup;
/*    */   
/*    */   private final Matrix3x2fc pose;
/*    */   
/*    */   private final int x0;
/*    */   
/*    */   private final int y0;
/*    */   
/*    */   private final int x1;
/*    */   
/*    */   private final int y1;
/*    */   
/*    */   private final int col1;
/*    */   
/*    */   private final int col2;
/*    */   
/*    */   private final ScreenRectangle scissorArea;
/*    */   
/*    */   private final ScreenRectangle bounds;
/*    */   
/*    */   public ColoredRectangleRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2fc pose, int x0, int y0, int x1, int y1, int col1, int col2, ScreenRectangle scissorArea, ScreenRectangle bounds) {
/* 10 */     this.pipeline = pipeline;
/* 10 */     this.textureSetup = textureSetup;
/* 10 */     this.pose = pose;
/* 10 */     this.x0 = x0;
/* 10 */     this.y0 = y0;
/* 10 */     this.x1 = x1;
/* 10 */     this.y1 = y1;
/* 10 */     this.col1 = col1;
/* 10 */     this.col2 = col2;
/* 10 */     this.scissorArea = scissorArea;
/* 10 */     this.bounds = bounds;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/gui/ColoredRectangleRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/ColoredRectangleRenderState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/gui/ColoredRectangleRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/ColoredRectangleRenderState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/gui/ColoredRectangleRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/state/gui/ColoredRectangleRenderState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public RenderPipeline pipeline() {
/* 10 */     return this.pipeline;
/*    */   }
/*    */   
/*    */   public TextureSetup textureSetup() {
/* 10 */     return this.textureSetup;
/*    */   }
/*    */   
/*    */   public Matrix3x2fc pose() {
/* 10 */     return this.pose;
/*    */   }
/*    */   
/*    */   public int x0() {
/* 10 */     return this.x0;
/*    */   }
/*    */   
/*    */   public int y0() {
/* 10 */     return this.y0;
/*    */   }
/*    */   
/*    */   public int x1() {
/* 10 */     return this.x1;
/*    */   }
/*    */   
/*    */   public int y1() {
/* 10 */     return this.y1;
/*    */   }
/*    */   
/*    */   public int col1() {
/* 10 */     return this.col1;
/*    */   }
/*    */   
/*    */   public int col2() {
/* 10 */     return this.col2;
/*    */   }
/*    */   
/*    */   public ScreenRectangle scissorArea() {
/* 10 */     return this.scissorArea;
/*    */   }
/*    */   
/*    */   public ScreenRectangle bounds() {
/* 10 */     return this.bounds;
/*    */   }
/*    */   
/*    */   public ColoredRectangleRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2fc pose, int x0, int y0, int x1, int y1, int col1, int col2, ScreenRectangle scissorArea) {
/* 13 */     this(pipeline, textureSetup, pose, x0, y0, x1, y1, col1, col2, scissorArea, getBounds(x0, y0, x1, y1, pose, scissorArea));
/*    */   }
/*    */   
/*    */   public void buildVertices(VertexConsumer vertexConsumer) {
/* 18 */     vertexConsumer.addVertexWith2DPose(pose(), x0(), y0()).setColor(col1());
/* 19 */     vertexConsumer.addVertexWith2DPose(pose(), x0(), y1()).setColor(col2());
/* 20 */     vertexConsumer.addVertexWith2DPose(pose(), x1(), y1()).setColor(col2());
/* 21 */     vertexConsumer.addVertexWith2DPose(pose(), x1(), y0()).setColor(col1());
/*    */   }
/*    */   
/*    */   private static ScreenRectangle getBounds(int x0, int y0, int x1, int y1, Matrix3x2fc pose, ScreenRectangle scissorArea) {
/* 25 */     ScreenRectangle bounds = (new ScreenRectangle(x0, y0, x1 - x0, y1 - y0)).transformMaxBounds(pose);
/* 26 */     return (scissorArea != null) ? scissorArea.intersection(bounds) : bounds;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\gui\ColoredRectangleRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */