/*    */ package net.minecraft.client.renderer.state.gui;
/*    */ 
/*    */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.gui.render.TextureSetup;
/*    */ import net.minecraft.util.Mth;
/*    */ import org.joml.Matrix3x2f;
/*    */ import org.joml.Matrix3x2fc;
/*    */ 
/*    */ public final class TiledBlitRenderState extends Record implements GuiElementRenderState {
/*    */   private final RenderPipeline pipeline;
/*    */   
/*    */   private final TextureSetup textureSetup;
/*    */   
/*    */   private final Matrix3x2f pose;
/*    */   
/*    */   private final int tileWidth;
/*    */   
/*    */   private final int tileHeight;
/*    */   
/*    */   private final int x0;
/*    */   
/*    */   private final int y0;
/*    */   
/*    */   private final int x1;
/*    */   
/*    */   private final int y1;
/*    */   
/*    */   private final float u0;
/*    */   
/*    */   private final float u1;
/*    */   
/*    */   private final float v0;
/*    */   
/*    */   private final float v1;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   private final ScreenRectangle scissorArea;
/*    */   
/*    */   private final ScreenRectangle bounds;
/*    */   
/*    */   public TiledBlitRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose, int tileWidth, int tileHeight, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color, ScreenRectangle scissorArea, ScreenRectangle bounds) {
/* 11 */     this.pipeline = pipeline;
/* 11 */     this.textureSetup = textureSetup;
/* 11 */     this.pose = pose;
/* 11 */     this.tileWidth = tileWidth;
/* 11 */     this.tileHeight = tileHeight;
/* 11 */     this.x0 = x0;
/* 11 */     this.y0 = y0;
/* 11 */     this.x1 = x1;
/* 11 */     this.y1 = y1;
/* 11 */     this.u0 = u0;
/* 11 */     this.u1 = u1;
/* 11 */     this.v0 = v0;
/* 11 */     this.v1 = v1;
/* 11 */     this.color = color;
/* 11 */     this.scissorArea = scissorArea;
/* 11 */     this.bounds = bounds;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/gui/TiledBlitRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/TiledBlitRenderState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/gui/TiledBlitRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/TiledBlitRenderState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/gui/TiledBlitRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/state/gui/TiledBlitRenderState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public RenderPipeline pipeline() {
/* 11 */     return this.pipeline;
/*    */   }
/*    */   
/*    */   public TextureSetup textureSetup() {
/* 11 */     return this.textureSetup;
/*    */   }
/*    */   
/*    */   public Matrix3x2f pose() {
/* 11 */     return this.pose;
/*    */   }
/*    */   
/*    */   public int tileWidth() {
/* 11 */     return this.tileWidth;
/*    */   }
/*    */   
/*    */   public int tileHeight() {
/* 11 */     return this.tileHeight;
/*    */   }
/*    */   
/*    */   public int x0() {
/* 11 */     return this.x0;
/*    */   }
/*    */   
/*    */   public int y0() {
/* 11 */     return this.y0;
/*    */   }
/*    */   
/*    */   public int x1() {
/* 11 */     return this.x1;
/*    */   }
/*    */   
/*    */   public int y1() {
/* 11 */     return this.y1;
/*    */   }
/*    */   
/*    */   public float u0() {
/* 11 */     return this.u0;
/*    */   }
/*    */   
/*    */   public float u1() {
/* 11 */     return this.u1;
/*    */   }
/*    */   
/*    */   public float v0() {
/* 11 */     return this.v0;
/*    */   }
/*    */   
/*    */   public float v1() {
/* 11 */     return this.v1;
/*    */   }
/*    */   
/*    */   public int color() {
/* 11 */     return this.color;
/*    */   }
/*    */   
/*    */   public ScreenRectangle scissorArea() {
/* 11 */     return this.scissorArea;
/*    */   }
/*    */   
/*    */   public ScreenRectangle bounds() {
/* 11 */     return this.bounds;
/*    */   }
/*    */   
/*    */   public TiledBlitRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose, int tileWidth, int tileHeight, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color, ScreenRectangle scissorArea) {
/* 14 */     this(pipeline, textureSetup, pose, tileWidth, tileHeight, x0, y0, x1, y1, u0, u1, v0, v1, color, scissorArea, getBounds(x0, y0, x1, y1, pose, scissorArea));
/*    */   }
/*    */   
/*    */   public void buildVertices(VertexConsumer vertexConsumer) {
/* 19 */     int width = x1() - x0();
/* 20 */     int height = y1() - y0();
/*    */     int tileX;
/* 21 */     for (tileX = 0; tileX < width; tileX += tileWidth()) {
/*    */       int tileWidth;
/*    */       float u1;
/* 23 */       int remainingWidth = width - tileX;
/* 25 */       if (tileWidth() <= remainingWidth) {
/* 26 */         tileWidth = tileWidth();
/* 27 */         u1 = u1();
/*    */       } else {
/* 29 */         tileWidth = remainingWidth;
/* 30 */         u1 = Mth.lerp(remainingWidth / tileWidth(), u0(), u1());
/*    */       } 
/*    */       int tileY;
/* 32 */       for (tileY = 0; tileY < height; tileY += tileHeight()) {
/*    */         int tileHeight;
/*    */         float v1;
/* 34 */         int remainingHeight = height - tileY;
/* 36 */         if (tileHeight() <= remainingHeight) {
/* 37 */           tileHeight = tileHeight();
/* 38 */           v1 = v1();
/*    */         } else {
/* 40 */           tileHeight = remainingHeight;
/* 41 */           v1 = Mth.lerp(remainingHeight / tileHeight(), v0(), v1());
/*    */         } 
/* 43 */         int x0 = x0() + tileX;
/* 44 */         int x1 = x0() + tileX + tileWidth;
/* 45 */         int y0 = y0() + tileY;
/* 46 */         int y1 = y0() + tileY + tileHeight;
/* 47 */         vertexConsumer.addVertexWith2DPose((Matrix3x2fc)pose(), x0, y0).setUv(u0(), v0()).setColor(color());
/* 48 */         vertexConsumer.addVertexWith2DPose((Matrix3x2fc)pose(), x0, y1).setUv(u0(), v1).setColor(color());
/* 49 */         vertexConsumer.addVertexWith2DPose((Matrix3x2fc)pose(), x1, y1).setUv(u1, v1).setColor(color());
/* 50 */         vertexConsumer.addVertexWith2DPose((Matrix3x2fc)pose(), x1, y0).setUv(u1, v0()).setColor(color());
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private static ScreenRectangle getBounds(int x0, int y0, int x1, int y1, Matrix3x2f pose, ScreenRectangle scissorArea) {
/* 56 */     ScreenRectangle bounds = (new ScreenRectangle(x0, y0, x1 - x0, y1 - y0)).transformMaxBounds((Matrix3x2fc)pose);
/* 57 */     return (scissorArea != null) ? scissorArea.intersection(bounds) : bounds;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\gui\TiledBlitRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */