/*    */ package net.minecraft.client.gui.render.pip;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.Iterator;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.state.gui.pip.GuiProfilerChartRenderState;
/*    */ import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.profiling.ResultField;
/*    */ 
/*    */ public class GuiProfilerChartRenderer extends PictureInPictureRenderer<GuiProfilerChartRenderState> {
/*    */   public Class<GuiProfilerChartRenderState> getRenderStateClass() {
/* 16 */     return GuiProfilerChartRenderState.class;
/*    */   }
/*    */   
/*    */   protected void renderToTexture(GuiProfilerChartRenderState chartState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
/* 21 */     double totalPercentage = 0.0D;
/* 22 */     poseStack.translate(0.0F, -5.0F, 0.0F);
/* 23 */     for (Iterator<ResultField> iterator = chartState.chartData().iterator(); iterator.hasNext(); ) {
/* 23 */       ResultField result = iterator.next();
/* 24 */       double slicePercentage = result.percentage;
/* 25 */       double currentPercentage = totalPercentage;
/* 26 */       totalPercentage += slicePercentage;
/* 28 */       int steps = Mth.floor(slicePercentage / 4.0D) + 1;
/* 30 */       int color = ARGB.opaque(result.getColor());
/* 31 */       int shadeColor = ARGB.multiply(color, -8355712);
/* 33 */       submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.debugTriangleFan(), (pose, buffer) -> {
/*    */             buffer.addVertex(pose, 0.0F, 0.0F, 0.0F).setColor(color);
/*    */             for (int j = steps; j >= 0; j--) {
/*    */               float dir = (float)((currentPercentage + slicePercentage * j / steps) * 6.2831854820251465D / 100.0D);
/*    */               float xx = Mth.sin(dir) * 105.0F;
/*    */               float yy = Mth.cos(dir) * 105.0F * 0.5F;
/*    */               buffer.addVertex(pose, xx, yy, 0.0F).setColor(color);
/*    */             } 
/*    */           });
/* 43 */       submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.debugQuads(), (pose, buffer) -> {
/*    */             for (int j = steps; j > 0; j--) {
/*    */               float dir0 = (float)((currentPercentage + slicePercentage * j / steps) * 6.2831854820251465D / 100.0D);
/*    */               float x0 = Mth.sin(dir0) * 105.0F;
/*    */               float y0 = Mth.cos(dir0) * 105.0F * 0.5F;
/*    */               float dir1 = (float)((currentPercentage + slicePercentage * (j - 1) / steps) * 6.2831854820251465D / 100.0D);
/*    */               float x1 = Mth.sin(dir1) * 105.0F;
/*    */               float y1 = Mth.cos(dir1) * 105.0F * 0.5F;
/*    */               if ((y0 + y1) / 2.0F >= 0.0F) {
/*    */                 buffer.addVertex(pose, x0, y0, 0.0F).setColor(shadeColor);
/*    */                 buffer.addVertex(pose, x0, y0 + 10.0F, 0.0F).setColor(shadeColor);
/*    */                 buffer.addVertex(pose, x1, y1 + 10.0F, 0.0F).setColor(shadeColor);
/*    */                 buffer.addVertex(pose, x1, y1, 0.0F).setColor(shadeColor);
/*    */               } 
/*    */             } 
/*    */           });
/*    */     } 
/*    */   }
/*    */   
/*    */   protected float getTranslateY(int height, int guiScale) {
/* 65 */     return height / 2.0F;
/*    */   }
/*    */   
/*    */   protected String getTextureLabel() {
/* 70 */     return "profiler chart";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\pip\GuiProfilerChartRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */