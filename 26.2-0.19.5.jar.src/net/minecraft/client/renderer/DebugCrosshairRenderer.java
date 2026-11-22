/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.PrimitiveTopology;
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*    */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*    */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*    */ import com.mojang.blaze3d.systems.RenderPass;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import com.mojang.blaze3d.vertex.BufferBuilder;
/*    */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*    */ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
/*    */ import com.mojang.blaze3d.vertex.MeshData;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalDouble;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fStack;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class DebugCrosshairRenderer implements AutoCloseable {
/*    */   private static final float CROSSHAIR_SCALE = 0.01F;
/*    */   
/*    */   private static final int CROSSHAIR_INDEX_COUNT = 36;
/*    */   
/*    */   private final GpuBuffer crosshairBuffer;
/*    */   
/* 30 */   private final RenderSystem.AutoStorageIndexBuffer crosshairIndicies = RenderSystem.getSequentialBuffer(PrimitiveTopology.LINES);
/*    */   
/*    */   public DebugCrosshairRenderer() {
/* 33 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH.getVertexSize() * 12 * 2);
/*    */     try {
/* 34 */       BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH);
/* 35 */       bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(-16777216).setNormal(1.0F, 0.0F, 0.0F).setLineWidth(4.0F);
/* 36 */       bufferBuilder.addVertex(1.0F, 0.0F, 0.0F).setColor(-16777216).setNormal(1.0F, 0.0F, 0.0F).setLineWidth(4.0F);
/* 37 */       bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(-16777216).setNormal(0.0F, 1.0F, 0.0F).setLineWidth(4.0F);
/* 38 */       bufferBuilder.addVertex(0.0F, 1.0F, 0.0F).setColor(-16777216).setNormal(0.0F, 1.0F, 0.0F).setLineWidth(4.0F);
/* 39 */       bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(-16777216).setNormal(0.0F, 0.0F, 1.0F).setLineWidth(4.0F);
/* 40 */       bufferBuilder.addVertex(0.0F, 0.0F, 1.0F).setColor(-16777216).setNormal(0.0F, 0.0F, 1.0F).setLineWidth(4.0F);
/* 42 */       bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(-65536).setNormal(1.0F, 0.0F, 0.0F).setLineWidth(2.0F);
/* 43 */       bufferBuilder.addVertex(1.0F, 0.0F, 0.0F).setColor(-65536).setNormal(1.0F, 0.0F, 0.0F).setLineWidth(2.0F);
/* 44 */       bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(-16711936).setNormal(0.0F, 1.0F, 0.0F).setLineWidth(2.0F);
/* 45 */       bufferBuilder.addVertex(0.0F, 1.0F, 0.0F).setColor(-16711936).setNormal(0.0F, 1.0F, 0.0F).setLineWidth(2.0F);
/* 46 */       bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(-8421377).setNormal(0.0F, 0.0F, 1.0F).setLineWidth(2.0F);
/* 47 */       bufferBuilder.addVertex(0.0F, 0.0F, 1.0F).setColor(-8421377).setNormal(0.0F, 0.0F, 1.0F).setLineWidth(2.0F);
/* 48 */       MeshData meshData = bufferBuilder.buildOrThrow();
/*    */       try {
/* 49 */         this.crosshairBuffer = RenderSystem.getDevice().createBuffer(() -> "Crosshair vertex buffer", 32, meshData.vertexBuffer());
/* 50 */         if (meshData != null)
/* 50 */           meshData.close(); 
/*    */       } catch (Throwable throwable) {
/*    */         if (meshData != null)
/*    */           try {
/*    */             meshData.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           }  
/*    */         throw throwable;
/*    */       } 
/* 51 */       if (byteBufferBuilder != null)
/* 51 */         byteBufferBuilder.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (byteBufferBuilder != null)
/*    */         try {
/*    */           byteBufferBuilder.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void close() {
/* 56 */     this.crosshairBuffer.close();
/*    */   }
/*    */   
/*    */   public void render(CameraRenderState cameraState, int guiScale) {
/* 60 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 61 */     modelViewStack.pushMatrix();
/* 62 */     modelViewStack.translate(0.0F, 0.0F, -1.0F);
/* 63 */     modelViewStack.rotateX(cameraState.xRot * 0.017453292F);
/* 64 */     modelViewStack.rotateY(cameraState.yRot * 0.017453292F);
/* 65 */     float crosshairScale = 0.01F * guiScale;
/* 66 */     modelViewStack.scale(-crosshairScale, crosshairScale, -crosshairScale);
/* 68 */     RenderPipeline renderPipelineOutline = RenderPipelines.LINES;
/* 69 */     RenderPipeline renderPipelineFill = RenderPipelines.LINES_DEPTH_BIAS;
/* 71 */     RenderTarget mainRenderTarget = (Minecraft.getInstance()).gameRenderer.mainRenderTarget();
/* 72 */     GpuTextureView colorTexture = mainRenderTarget.getColorTextureView();
/* 73 */     GpuTextureView depthTexture = mainRenderTarget.getDepthTextureView();
/* 74 */     GpuBuffer indexBuffer = this.crosshairIndicies.getBuffer(36);
/* 75 */     GpuBufferSlice dynamicTransform = RenderSystem.getDynamicUniforms().writeTransform(new Matrix4f((Matrix4fc)modelViewStack));
/* 77 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "3d crosshair", colorTexture, Optional.empty(), depthTexture, OptionalDouble.empty());
/*    */     try {
/* 78 */       renderPass.setPipeline(renderPipelineOutline);
/* 79 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 80 */       renderPass.setVertexBuffer(0, this.crosshairBuffer.slice());
/* 81 */       renderPass.setIndexBuffer(indexBuffer, this.crosshairIndicies.type());
/* 83 */       renderPass.setUniform("DynamicTransforms", dynamicTransform);
/* 84 */       renderPass.drawIndexed(18, 1, 0, 0, 0);
/* 86 */       renderPass.setPipeline(renderPipelineFill);
/* 87 */       renderPass.drawIndexed(18, 1, 18, 0, 0);
/* 88 */       if (renderPass != null)
/* 88 */         renderPass.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (renderPass != null)
/*    */         try {
/*    */           renderPass.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/* 89 */     modelViewStack.popMatrix();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\DebugCrosshairRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */