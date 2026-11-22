/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.feature.submit.BatchableSubmit;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ 
/*    */ public class CustomFeatureRenderer extends RenderTypeFeatureRenderer<CustomFeatureRenderer.Submit> {
/* 12 */   public static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create("Custom");
/*    */   
/*    */   protected void buildGroup(FeatureFrameContext context, List<Submit> submits) {
/* 16 */     for (Submit submit : submits) {
/* 17 */       VertexConsumer builder = getVertexBuilder(submit.renderType());
/* 18 */       submit.customGeometryRenderer().render(submit.pose(), builder);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static final class Submit extends Record implements BatchableSubmit {
/*    */     private final PoseStack.Pose pose;
/*    */     
/*    */     private final RenderType renderType;
/*    */     
/*    */     private final SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer;
/*    */     
/*    */     public Submit(PoseStack.Pose pose, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer) {
/* 22 */       this.pose = pose;
/* 22 */       this.renderType = renderType;
/* 22 */       this.customGeometryRenderer = customGeometryRenderer;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/CustomFeatureRenderer$Submit;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/CustomFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/CustomFeatureRenderer$Submit;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/CustomFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/CustomFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/CustomFeatureRenderer$Submit;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public PoseStack.Pose pose() {
/* 22 */       return this.pose;
/*    */     }
/*    */     
/*    */     public RenderType renderType() {
/* 22 */       return this.renderType;
/*    */     }
/*    */     
/*    */     public SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer() {
/* 22 */       return this.customGeometryRenderer;
/*    */     }
/*    */     
/*    */     public Object batchKey() {
/* 29 */       return this.renderType;
/*    */     }
/*    */     
/*    */     public FeatureRendererType<Submit> featureType() {
/* 34 */       return CustomFeatureRenderer.TYPE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\CustomFeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */