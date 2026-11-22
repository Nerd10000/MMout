/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.feature.submit.BatchableSubmit;
/*    */ import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class ModelFeatureRenderer extends RenderTypeFeatureRenderer<ModelFeatureRenderer.Submit<?>> {
/* 16 */   public static final FeatureRendererType<Submit<?>> TYPE = FeatureRendererType.create("Entity Model");
/*    */   
/* 18 */   private final PoseStack poseStack = new PoseStack();
/*    */   
/*    */   protected void buildGroup(FeatureFrameContext context, List<Submit<?>> submits) {
/* 22 */     for (Submit<?> submit : submits)
/* 23 */       prepareModel(submit); 
/*    */   }
/*    */   
/*    */   private <S> void prepareModel(Submit<S> submit) {
/*    */     SheetedDecalTextureGenerator sheetedDecalTextureGenerator;
/*    */     VertexConsumer vertexConsumer1;
/* 28 */     this.poseStack.last().set(submit.pose());
/* 30 */     VertexConsumer buffer = getVertexBuilder(submit.renderType());
/* 31 */     if (submit.sheetedDecalPose() != null) {
/* 32 */       sheetedDecalTextureGenerator = new SheetedDecalTextureGenerator(buffer, submit.sheetedDecalPose(), 1.0F);
/* 33 */     } else if (submit.sprite() != null) {
/* 34 */       vertexConsumer1 = submit.sprite().wrap((VertexConsumer)sheetedDecalTextureGenerator);
/*    */     } 
/* 37 */     Model<? super S> model = submit.model();
/* 38 */     model.setupAnim(submit.state());
/* 39 */     model.renderToBuffer(this.poseStack, vertexConsumer1, submit.lightCoords(), submit.overlayCoords(), submit.tintedColor());
/*    */   }
/*    */   
/*    */   public static final class CrumblingOverlay extends Record {
/*    */     private final int progress;
/*    */     
/*    */     private final PoseStack.Pose cameraPose;
/*    */     
/*    */     public CrumblingOverlay(int progress, PoseStack.Pose cameraPose) {
/* 42 */       this.progress = progress;
/* 42 */       this.cameraPose = cameraPose;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #42	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #42	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #42	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public int progress() {
/* 42 */       return this.progress;
/*    */     }
/*    */     
/*    */     public PoseStack.Pose cameraPose() {
/* 42 */       return this.cameraPose;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Submit<S> extends Record implements BatchableSubmit, TranslucentSubmit {
/*    */     private final RenderType renderType;
/*    */     
/*    */     private final PoseStack.Pose pose;
/*    */     
/*    */     private final Model<? super S> model;
/*    */     
/*    */     private final S state;
/*    */     
/*    */     private final int lightCoords;
/*    */     
/*    */     private final int overlayCoords;
/*    */     
/*    */     private final int tintedColor;
/*    */     
/*    */     private final TextureAtlasSprite sprite;
/*    */     
/*    */     private final PoseStack.Pose sheetedDecalPose;
/*    */     
/*    */     public Submit(RenderType renderType, PoseStack.Pose pose, Model<? super S> model, S state, int lightCoords, int overlayCoords, int tintedColor, TextureAtlasSprite sprite, PoseStack.Pose sheetedDecalPose) {
/* 44 */       this.renderType = renderType;
/* 44 */       this.pose = pose;
/* 44 */       this.model = model;
/* 44 */       this.state = state;
/* 44 */       this.lightCoords = lightCoords;
/* 44 */       this.overlayCoords = overlayCoords;
/* 44 */       this.tintedColor = tintedColor;
/* 44 */       this.sprite = sprite;
/* 44 */       this.sheetedDecalPose = sheetedDecalPose;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit<TS;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit<TS;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit<TS;>;
/*    */     }
/*    */     
/*    */     public RenderType renderType() {
/* 44 */       return this.renderType;
/*    */     }
/*    */     
/*    */     public PoseStack.Pose pose() {
/* 44 */       return this.pose;
/*    */     }
/*    */     
/*    */     public Model<? super S> model() {
/* 44 */       return this.model;
/*    */     }
/*    */     
/*    */     public S state() {
/* 44 */       return this.state;
/*    */     }
/*    */     
/*    */     public int lightCoords() {
/* 44 */       return this.lightCoords;
/*    */     }
/*    */     
/*    */     public int overlayCoords() {
/* 44 */       return this.overlayCoords;
/*    */     }
/*    */     
/*    */     public int tintedColor() {
/* 44 */       return this.tintedColor;
/*    */     }
/*    */     
/*    */     public TextureAtlasSprite sprite() {
/* 44 */       return this.sprite;
/*    */     }
/*    */     
/*    */     public PoseStack.Pose sheetedDecalPose() {
/* 44 */       return this.sheetedDecalPose;
/*    */     }
/*    */     
/*    */     public Object batchKey() {
/* 57 */       return this.renderType;
/*    */     }
/*    */     
/*    */     public float distanceToCameraSq() {
/* 62 */       return TranslucentSubmit.computeDistanceToCameraSq((Matrix4fc)this.pose.pose());
/*    */     }
/*    */     
/*    */     public FeatureRendererType<Submit<S>> featureType() {
/* 68 */       return (FeatureRendererType)ModelFeatureRenderer.TYPE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\ModelFeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */