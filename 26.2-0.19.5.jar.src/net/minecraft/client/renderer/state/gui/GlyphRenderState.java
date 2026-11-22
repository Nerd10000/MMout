/*    */ package net.minecraft.client.renderer.state.gui;
/*    */ 
/*    */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.FilterMode;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.gui.font.TextRenderable;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.gui.render.TextureSetup;
/*    */ import org.joml.Matrix3x2fc;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public final class GlyphRenderState extends Record implements GuiElementRenderState {
/*    */   private final Matrix3x2fc pose;
/*    */   
/*    */   private final TextRenderable renderable;
/*    */   
/*    */   private final ScreenRectangle scissorArea;
/*    */   
/*    */   public GlyphRenderState(Matrix3x2fc pose, TextRenderable renderable, ScreenRectangle scissorArea) {
/* 15 */     this.pose = pose;
/* 15 */     this.renderable = renderable;
/* 15 */     this.scissorArea = scissorArea;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/gui/GlyphRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/GlyphRenderState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/gui/GlyphRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/GlyphRenderState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/gui/GlyphRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/state/gui/GlyphRenderState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Matrix3x2fc pose() {
/* 15 */     return this.pose;
/*    */   }
/*    */   
/*    */   public TextRenderable renderable() {
/* 15 */     return this.renderable;
/*    */   }
/*    */   
/*    */   public ScreenRectangle scissorArea() {
/* 15 */     return this.scissorArea;
/*    */   }
/*    */   
/*    */   public void buildVertices(VertexConsumer vertexConsumer) {
/* 22 */     this.renderable.render((Matrix4fc)(new Matrix4f()).mul(this.pose), vertexConsumer, 15728880, true);
/*    */   }
/*    */   
/*    */   public RenderPipeline pipeline() {
/* 27 */     return this.renderable.guiPipeline();
/*    */   }
/*    */   
/*    */   public TextureSetup textureSetup() {
/* 32 */     return TextureSetup.singleTextureWithLightmap(this.renderable.textureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
/*    */   }
/*    */   
/*    */   public ScreenRectangle bounds() {
/* 38 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\gui\GlyphRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */