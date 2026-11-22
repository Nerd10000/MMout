/*     */ package net.minecraft.client.renderer.feature;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*     */ import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.gizmos.TextGizmo;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ public class GizmoFeatureRenderer extends RenderTypeFeatureRenderer<GizmoFeatureRenderer.Submit> {
/*  25 */   public static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create("Gizmo");
/*     */   
/*  27 */   private final PoseStack poseStack = new PoseStack();
/*     */   
/*     */   protected void buildGroup(FeatureFrameContext context, List<Submit> submits) {
/*  31 */     Matrix4f modelViewMatrix = RenderSystem.getModelViewMatrixCopy();
/*  32 */     for (Submit submit : submits) {
/*  33 */       CameraRenderState camera = submit.camera;
/*  34 */       buildQuads(submit.group.quads(), camera);
/*  35 */       buildTriangleFans(submit.group.triangleFans(), camera);
/*  36 */       buildLines(submit.group.lines(), camera, (Matrix4fc)modelViewMatrix, submit.group.opaque());
/*  37 */       buildTexts(submit.group.texts(), camera, context.font());
/*  38 */       buildPoints(submit.group.points(), camera);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void buildTexts(List<DrawableGizmoPrimitives.Text> texts, CameraRenderState camera, Font font) {
/*  43 */     if (texts.isEmpty() || !camera.initialized)
/*     */       return; 
/*  46 */     double camX = camera.pos.x();
/*  47 */     double camY = camera.pos.y();
/*  48 */     double camZ = camera.pos.z();
/*  50 */     for (DrawableGizmoPrimitives.Text text : texts) {
/*     */       float fontX;
/*  51 */       TextGizmo.Style style = text.style();
/*  52 */       this.poseStack.pushPose();
/*  53 */       this.poseStack.translate((float)(text.pos().x() - camX), (float)(text.pos().y() - camY), (float)(text.pos().z() - camZ));
/*  54 */       this.poseStack.mulPose((Quaternionfc)camera.orientation);
/*  55 */       this.poseStack.scale(style.scale() / 16.0F, -style.scale() / 16.0F, style.scale() / 16.0F);
/*  58 */       if (style.adjustLeft().isEmpty()) {
/*  60 */         fontX = -font.width(text.text()) / 2.0F;
/*     */       } else {
/*  62 */         fontX = (float)-style.adjustLeft().getAsDouble() / style.scale();
/*     */       } 
/*  65 */       final Matrix4f pose = this.poseStack.last().pose();
/*  66 */       Font.PreparedText preparedText = font.prepareText(text.text(), fontX, 0.0F, style.color(), false, 0);
/*  67 */       preparedText.visit(new Font.GlyphVisitor() {
/*     */             public void acceptRenderable(TextRenderable renderable) {
/*  70 */               VertexConsumer buffer = GizmoFeatureRenderer.this.getVertexBuilder(renderable.renderType(Font.DisplayMode.NORMAL));
/*  71 */               renderable.render((Matrix4fc)pose, buffer, 15728880, false);
/*     */             }
/*     */           });
/*  75 */       this.poseStack.popPose();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void buildLines(List<DrawableGizmoPrimitives.Line> lines, CameraRenderState camera, Matrix4fc modelViewMatrix, boolean opaque) {
/*  80 */     if (lines.isEmpty())
/*     */       return; 
/*  84 */     VertexConsumer builder = getVertexBuilder(opaque ? RenderTypes.lines() : RenderTypes.linesTranslucent());
/*  85 */     PoseStack.Pose pose = this.poseStack.last();
/*  86 */     Vector4f start = new Vector4f();
/*  87 */     Vector4f end = new Vector4f();
/*  88 */     Vector4f startViewSpace = new Vector4f();
/*  89 */     Vector4f endViewSpace = new Vector4f();
/*  90 */     Vector4f intersectionInWorld = new Vector4f();
/*  91 */     double camX = camera.pos.x();
/*  92 */     double camY = camera.pos.y();
/*  93 */     double camZ = camera.pos.z();
/*  95 */     for (DrawableGizmoPrimitives.Line line : lines) {
/*  96 */       start.set(line.start().x() - camX, line.start().y() - camY, line.start().z() - camZ, 1.0D);
/*  97 */       end.set(line.end().x() - camX, line.end().y() - camY, line.end().z() - camZ, 1.0D);
/*  98 */       start.mul(modelViewMatrix, startViewSpace);
/*  99 */       end.mul(modelViewMatrix, endViewSpace);
/* 101 */       boolean startIsBehindCamera = (startViewSpace.z > -0.05F);
/* 102 */       boolean endIsBehindCamera = (endViewSpace.z > -0.05F);
/* 104 */       if (startIsBehindCamera && endIsBehindCamera)
/*     */         continue; 
/* 109 */       if (startIsBehindCamera || endIsBehindCamera) {
/* 113 */         float denom = endViewSpace.z - startViewSpace.z;
/* 114 */         if (Math.abs(denom) < 1.0E-9F)
/*     */           continue; 
/* 119 */         float intersection = Mth.clamp((-0.05F - startViewSpace.z) / denom, 0.0F, 1.0F);
/* 120 */         start.lerp((Vector4fc)end, intersection, intersectionInWorld);
/* 121 */         if (startIsBehindCamera) {
/* 122 */           start.set((Vector4fc)intersectionInWorld);
/*     */         } else {
/* 124 */           end.set((Vector4fc)intersectionInWorld);
/*     */         } 
/*     */       } 
/* 128 */       builder.addVertex(pose, start.x, start.y, start.z).setNormal(pose, end.x - start.x, end.y - start.y, end.z - start.z).setColor(line.color()).setLineWidth(line.width());
/* 129 */       builder.addVertex(pose, end.x, end.y, end.z).setNormal(pose, end.x - start.x, end.y - start.y, end.z - start.z).setColor(line.color()).setLineWidth(line.width());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void buildTriangleFans(List<DrawableGizmoPrimitives.TriangleFan> triangleFans, CameraRenderState camera) {
/* 134 */     if (triangleFans.isEmpty())
/*     */       return; 
/* 137 */     PoseStack.Pose pose = this.poseStack.last();
/* 138 */     double camX = camera.pos.x();
/* 139 */     double camY = camera.pos.y();
/* 140 */     double camZ = camera.pos.z();
/* 141 */     for (DrawableGizmoPrimitives.TriangleFan triangleFan : triangleFans) {
/* 142 */       VertexConsumer builder = getVertexBuilder(RenderTypes.debugTriangleFan());
/* 143 */       for (Vec3 point : triangleFan.points())
/* 144 */         builder.addVertex(pose, (float)(point.x() - camX), (float)(point.y() - camY), (float)(point.z() - camZ)).setColor(triangleFan.color()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void buildQuads(List<DrawableGizmoPrimitives.Quad> quads, CameraRenderState camera) {
/* 150 */     if (quads.isEmpty())
/*     */       return; 
/* 153 */     VertexConsumer builder = getVertexBuilder(RenderTypes.debugFilledBox());
/* 154 */     PoseStack.Pose pose = this.poseStack.last();
/* 155 */     double camX = camera.pos.x();
/* 156 */     double camY = camera.pos.y();
/* 157 */     double camZ = camera.pos.z();
/* 158 */     for (DrawableGizmoPrimitives.Quad quad : quads) {
/* 159 */       builder.addVertex(pose, (float)(quad.a().x() - camX), (float)(quad.a().y() - camY), (float)(quad.a().z() - camZ)).setColor(quad.color());
/* 160 */       builder.addVertex(pose, (float)(quad.b().x() - camX), (float)(quad.b().y() - camY), (float)(quad.b().z() - camZ)).setColor(quad.color());
/* 161 */       builder.addVertex(pose, (float)(quad.c().x() - camX), (float)(quad.c().y() - camY), (float)(quad.c().z() - camZ)).setColor(quad.color());
/* 162 */       builder.addVertex(pose, (float)(quad.d().x() - camX), (float)(quad.d().y() - camY), (float)(quad.d().z() - camZ)).setColor(quad.color());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void buildPoints(List<DrawableGizmoPrimitives.Point> points, CameraRenderState camera) {
/* 167 */     if (points.isEmpty())
/*     */       return; 
/* 170 */     VertexConsumer builder = getVertexBuilder(RenderTypes.debugPoint());
/* 171 */     PoseStack.Pose pose = this.poseStack.last();
/* 172 */     double camX = camera.pos.x();
/* 173 */     double camY = camera.pos.y();
/* 174 */     double camZ = camera.pos.z();
/* 175 */     for (DrawableGizmoPrimitives.Point point : points) {
/* 176 */       Vec3 pos = point.pos();
/* 177 */       builder.addVertex(pose, (float)(pos.x() - camX), (float)(pos.y() - camY), (float)(pos.z() - camZ)).setColor(point.color()).setLineWidth(point.size());
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final class Submit extends Record implements SubmitNode {
/*     */     private final DrawableGizmoPrimitives.Group group;
/*     */     
/*     */     private final CameraRenderState camera;
/*     */     
/*     */     public Submit(DrawableGizmoPrimitives.Group group, CameraRenderState camera) {
/* 181 */       this.group = group;
/* 181 */       this.camera = camera;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/GizmoFeatureRenderer$Submit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #181	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/GizmoFeatureRenderer$Submit;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/GizmoFeatureRenderer$Submit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #181	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/GizmoFeatureRenderer$Submit;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/GizmoFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #181	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/GizmoFeatureRenderer$Submit;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public DrawableGizmoPrimitives.Group group() {
/* 181 */       return this.group;
/*     */     }
/*     */     
/*     */     public CameraRenderState camera() {
/* 181 */       return this.camera;
/*     */     }
/*     */     
/*     */     public FeatureRendererType<Submit> featureType() {
/* 187 */       return GizmoFeatureRenderer.TYPE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\GizmoFeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */