/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.font.TextRenderable;
/*    */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class TextFeatureRenderer extends RenderTypeFeatureRenderer<TextFeatureRenderer.Submit> {
/* 15 */   public static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create("Text");
/*    */   
/*    */   protected void buildGroup(FeatureFrameContext context, List<Submit> submits) {
/* 19 */     Font font = context.font();
/* 20 */     GlyphRenderer glyphRenderer = new GlyphRenderer();
/* 21 */     for (Submit submit : submits) {
/* 22 */       glyphRenderer.pose.set(submit.pose());
/* 23 */       glyphRenderer.lightCoords = submit.lightCoords();
/* 24 */       glyphRenderer.displayMode = submit.displayMode();
/* 25 */       if (submit.outlineColor() == 0) {
/* 26 */         Font.PreparedText preparedText = font.prepareText(submit.string(), submit.x(), submit.y(), submit.color(), submit.dropShadow(), false, submit.backgroundColor());
/* 27 */         preparedText.visit(glyphRenderer);
/*    */         continue;
/*    */       } 
/* 29 */       Font.PreparedText outline = font.prepare8xTextOutline(submit.string(), submit.x(), submit.y(), submit.outlineColor());
/* 30 */       Font.PreparedText text = font.prepareText(submit.string(), submit.x(), submit.y(), submit.color(), false, false, 0);
/* 31 */       glyphRenderer.displayMode = Font.DisplayMode.NORMAL;
/* 32 */       outline.visit(glyphRenderer);
/* 33 */       glyphRenderer.displayMode = Font.DisplayMode.POLYGON_OFFSET;
/* 34 */       text.visit(glyphRenderer);
/*    */     } 
/*    */   }
/*    */   
/*    */   private class GlyphRenderer implements Font.GlyphVisitor {
/* 40 */     private final Matrix4f pose = new Matrix4f();
/*    */     
/* 41 */     private int lightCoords = 15728880;
/*    */     
/* 42 */     private Font.DisplayMode displayMode = Font.DisplayMode.NORMAL;
/*    */     
/*    */     public void acceptRenderable(TextRenderable renderable) {
/* 46 */       VertexConsumer builder = TextFeatureRenderer.this.getVertexBuilder(renderable.renderType(this.displayMode));
/* 47 */       renderable.render((Matrix4fc)this.pose, builder, this.lightCoords, false);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Submit extends Record implements SubmitNode {
/*    */     private final Matrix4fc pose;
/*    */     
/*    */     private final float x;
/*    */     
/*    */     private final float y;
/*    */     
/*    */     private final FormattedCharSequence string;
/*    */     
/*    */     private final boolean dropShadow;
/*    */     
/*    */     private final Font.DisplayMode displayMode;
/*    */     
/*    */     private final int lightCoords;
/*    */     
/*    */     private final int color;
/*    */     
/*    */     private final int backgroundColor;
/*    */     
/*    */     private final int outlineColor;
/*    */     
/*    */     public Submit(Matrix4fc pose, float x, float y, FormattedCharSequence string, boolean dropShadow, Font.DisplayMode displayMode, int lightCoords, int color, int backgroundColor, int outlineColor) {
/* 51 */       this.pose = pose;
/* 51 */       this.x = x;
/* 51 */       this.y = y;
/* 51 */       this.string = string;
/* 51 */       this.dropShadow = dropShadow;
/* 51 */       this.displayMode = displayMode;
/* 51 */       this.lightCoords = lightCoords;
/* 51 */       this.color = color;
/* 51 */       this.backgroundColor = backgroundColor;
/* 51 */       this.outlineColor = outlineColor;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/TextFeatureRenderer$Submit;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #51	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/TextFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/TextFeatureRenderer$Submit;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #51	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/TextFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/TextFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #51	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/TextFeatureRenderer$Submit;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Matrix4fc pose() {
/* 51 */       return this.pose;
/*    */     }
/*    */     
/*    */     public float x() {
/* 51 */       return this.x;
/*    */     }
/*    */     
/*    */     public float y() {
/* 51 */       return this.y;
/*    */     }
/*    */     
/*    */     public FormattedCharSequence string() {
/* 51 */       return this.string;
/*    */     }
/*    */     
/*    */     public boolean dropShadow() {
/* 51 */       return this.dropShadow;
/*    */     }
/*    */     
/*    */     public Font.DisplayMode displayMode() {
/* 51 */       return this.displayMode;
/*    */     }
/*    */     
/*    */     public int lightCoords() {
/* 51 */       return this.lightCoords;
/*    */     }
/*    */     
/*    */     public int color() {
/* 51 */       return this.color;
/*    */     }
/*    */     
/*    */     public int backgroundColor() {
/* 51 */       return this.backgroundColor;
/*    */     }
/*    */     
/*    */     public int outlineColor() {
/* 51 */       return this.outlineColor;
/*    */     }
/*    */     
/*    */     public FeatureRendererType<Submit> featureType() {
/* 65 */       return TextFeatureRenderer.TYPE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\TextFeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */