/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.ProjectionType;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.BufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.state.WindowRenderState;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.CubeMapTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fStack;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public class CubeMap implements AutoCloseable {
/*     */   private static final int SIDES = 6;
/*     */   
/*     */   private static final float PROJECTION_Z_NEAR = 0.05F;
/*     */   
/*     */   private static final float PROJECTION_Z_FAR = 10.0F;
/*     */   
/*     */   private static final float PROJECTION_FOV = 85.0F;
/*     */   
/*     */   private final GpuBuffer vertexBuffer;
/*     */   
/*     */   private final Projection projection;
/*     */   
/*     */   private final ProjectionMatrixBuffer projectionMatrixUbo;
/*     */   
/*     */   private final Identifier location;
/*     */   
/*     */   public CubeMap(Identifier base) {
/*  43 */     this.location = base;
/*  44 */     this.projection = new Projection();
/*  45 */     this.projectionMatrixUbo = new ProjectionMatrixBuffer("cubemap");
/*  46 */     this.vertexBuffer = initializeVertices();
/*     */   }
/*     */   
/*     */   public void render(float rotXInDegrees, float rotYInDegrees) {
/*  50 */     Minecraft minecraft = Minecraft.getInstance();
/*  51 */     WindowRenderState windowState = (minecraft.gameRenderer.gameRenderState()).windowRenderState;
/*  52 */     this.projection.setupPerspective(0.05F, 10.0F, 85.0F, windowState.width, windowState.height);
/*  53 */     RenderSystem.setProjectionMatrix(this.projectionMatrixUbo.getBuffer(this.projection), ProjectionType.PERSPECTIVE);
/*  55 */     RenderPipeline renderPipeline = RenderPipelines.PANORAMA;
/*  56 */     RenderTarget mainRenderTarget = (Minecraft.getInstance()).gameRenderer.mainRenderTarget();
/*  57 */     GpuTextureView colorTexture = mainRenderTarget.getColorTextureView();
/*  58 */     GpuTextureView depthTexture = mainRenderTarget.getDepthTextureView();
/*  59 */     RenderSystem.AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
/*  60 */     GpuBuffer indexBuffer = indices.getBuffer(36);
/*  62 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/*  63 */     modelViewStack.pushMatrix();
/*  64 */     modelViewStack.rotationX(3.1415927F);
/*  65 */     modelViewStack.rotateX(rotXInDegrees * 0.017453292F);
/*  66 */     modelViewStack.rotateY(rotYInDegrees * 0.017453292F);
/*  67 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(new Matrix4f((Matrix4fc)modelViewStack));
/*  68 */     modelViewStack.popMatrix();
/*  70 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Cubemap", colorTexture, Optional.empty(), depthTexture, OptionalDouble.empty());
/*     */     try {
/*  71 */       renderPass.setPipeline(renderPipeline);
/*  72 */       RenderSystem.bindDefaultUniforms(renderPass);
/*  73 */       renderPass.setVertexBuffer(0, this.vertexBuffer.slice());
/*  74 */       renderPass.setIndexBuffer(indexBuffer, indices.type());
/*  76 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/*  77 */       AbstractTexture texture = minecraft.getTextureManager().getTexture(this.location);
/*  78 */       renderPass.bindTexture("Sampler0", texture.getTextureView(), texture.getSampler());
/*  79 */       renderPass.drawIndexed(36, 1, 0, 0, 0);
/*  80 */       if (renderPass != null)
/*  80 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static GpuBuffer initializeVertices() {
/*  84 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * 4 * 6);
/*     */     try {
/*  85 */       BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);
/*  86 */       bufferBuilder.addVertex(-1.0F, -1.0F, 1.0F);
/*  87 */       bufferBuilder.addVertex(-1.0F, 1.0F, 1.0F);
/*  88 */       bufferBuilder.addVertex(1.0F, 1.0F, 1.0F);
/*  89 */       bufferBuilder.addVertex(1.0F, -1.0F, 1.0F);
/*  91 */       bufferBuilder.addVertex(1.0F, -1.0F, 1.0F);
/*  92 */       bufferBuilder.addVertex(1.0F, 1.0F, 1.0F);
/*  93 */       bufferBuilder.addVertex(1.0F, 1.0F, -1.0F);
/*  94 */       bufferBuilder.addVertex(1.0F, -1.0F, -1.0F);
/*  96 */       bufferBuilder.addVertex(1.0F, -1.0F, -1.0F);
/*  97 */       bufferBuilder.addVertex(1.0F, 1.0F, -1.0F);
/*  98 */       bufferBuilder.addVertex(-1.0F, 1.0F, -1.0F);
/*  99 */       bufferBuilder.addVertex(-1.0F, -1.0F, -1.0F);
/* 101 */       bufferBuilder.addVertex(-1.0F, -1.0F, -1.0F);
/* 102 */       bufferBuilder.addVertex(-1.0F, 1.0F, -1.0F);
/* 103 */       bufferBuilder.addVertex(-1.0F, 1.0F, 1.0F);
/* 104 */       bufferBuilder.addVertex(-1.0F, -1.0F, 1.0F);
/* 106 */       bufferBuilder.addVertex(-1.0F, -1.0F, -1.0F);
/* 107 */       bufferBuilder.addVertex(-1.0F, -1.0F, 1.0F);
/* 108 */       bufferBuilder.addVertex(1.0F, -1.0F, 1.0F);
/* 109 */       bufferBuilder.addVertex(1.0F, -1.0F, -1.0F);
/* 111 */       bufferBuilder.addVertex(-1.0F, 1.0F, 1.0F);
/* 112 */       bufferBuilder.addVertex(-1.0F, 1.0F, -1.0F);
/* 113 */       bufferBuilder.addVertex(1.0F, 1.0F, -1.0F);
/* 114 */       bufferBuilder.addVertex(1.0F, 1.0F, 1.0F);
/* 116 */       MeshData meshData = bufferBuilder.buildOrThrow();
/*     */       try {
/* 117 */         GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "Cube map vertex buffer", 32, meshData.vertexBuffer());
/* 118 */         if (meshData != null)
/* 118 */           meshData.close(); 
/* 119 */         if (byteBufferBuilder != null)
/* 119 */           byteBufferBuilder.close(); 
/*     */         return gpuBuffer;
/*     */       } catch (Throwable throwable) {
/*     */         if (meshData != null)
/*     */           try {
/*     */             meshData.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } catch (Throwable throwable) {
/*     */       if (byteBufferBuilder != null)
/*     */         try {
/*     */           byteBufferBuilder.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void registerTextures(TextureManager textureManager) {
/* 123 */     textureManager.register(this.location, (AbstractTexture)new CubeMapTexture(this.location));
/*     */   }
/*     */   
/*     */   public void close() {
/* 128 */     this.vertexBuffer.close();
/* 129 */     this.projectionMatrixUbo.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\CubeMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */