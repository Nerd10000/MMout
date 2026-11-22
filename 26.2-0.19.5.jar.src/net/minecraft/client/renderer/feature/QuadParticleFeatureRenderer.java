/*     */ package net.minecraft.client.renderer.feature;
/*     */ 
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.particle.SingleQuadParticle;
/*     */ import net.minecraft.client.renderer.StagedVertexBuffer;
/*     */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*     */ import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ 
/*     */ public class QuadParticleFeatureRenderer implements FeatureRenderer<QuadParticleFeatureRenderer.Submit> {
/*  30 */   public static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create("Particle");
/*     */   
/*  32 */   private final List<PreparedGroup> groups = new ArrayList<>();
/*     */   
/*     */   private GpuBufferSlice dynamicTransforms;
/*     */   
/*     */   public void prepareGroup(FeatureFrameContext context, List<Submit> submits, boolean strictlyOrdered) {
/*  37 */     if (submits.isEmpty())
/*     */       return; 
/*  41 */     StagedVertexBuffer stagedVertexBuffer = context.stagedVertexBuffer();
/*  42 */     Map<SingleQuadParticle.Layer, StagedVertexBuffer.Draw> drawByLayer = new IdentityHashMap<>();
/*  43 */     for (Submit submit : submits) {
/*  44 */       QuadParticleRenderState particles = submit.particles();
/*  45 */       if (particles.isEmpty())
/*     */         continue; 
/*  49 */       for (SingleQuadParticle.Layer layer : particles.layers()) {
/*  50 */         if (layer.translucent() != submit.translucent())
/*     */           continue; 
/*  53 */         StagedVertexBuffer.Draw draw = drawByLayer.computeIfAbsent(layer, paramLayer -> stagedVertexBuffer.appendDraw(DefaultVertexFormat.PARTICLE, PrimitiveTopology.QUADS, null));
/*  56 */         particles.buildLayer(layer, stagedVertexBuffer.getVertexBuilder(draw));
/*     */       } 
/*     */     } 
/*  61 */     boolean translucent = ((Submit)submits.getFirst()).translucent();
/*  62 */     this.groups.add(new PreparedGroup(drawByLayer, translucent));
/*     */   }
/*     */   
/*     */   public void finishPrepare(FeatureFrameContext context) {
/*  67 */     this.dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy());
/*     */   }
/*     */   
/*     */   public void executeGroup(FeatureFrameContext context, int groupIndex, List<Submit> submits, boolean strictlyOrdered) {
/*  72 */     PreparedGroup group = this.groups.get(groupIndex);
/*  74 */     GpuDevice device = RenderSystem.getDevice();
/*  75 */     Minecraft minecraft = Minecraft.getInstance();
/*  76 */     RenderTarget mainTarget = minecraft.gameRenderer.mainRenderTarget();
/*  77 */     RenderTarget particleTarget = minecraft.levelRenderer.particlesTarget();
/*  79 */     boolean useParticleTarget = (particleTarget != null && group.translucent);
/*  81 */     GpuTextureView colorTextureView = useParticleTarget ? particleTarget.getColorTextureView() : mainTarget.getColorTextureView();
/*  82 */     GpuTextureView depthTextureView = useParticleTarget ? particleTarget.getDepthTextureView() : mainTarget.getDepthTextureView();
/*  83 */     RenderPass renderPass = device.createCommandEncoder().createRenderPass(() -> "Particles - " + (group.translucent ? "Translucent" : "Solid"), colorTextureView, Optional.empty(), depthTextureView, OptionalDouble.empty());
/*     */     try {
/*  84 */       RenderSystem.bindDefaultUniforms(renderPass);
/*  85 */       renderPass.setUniform("DynamicTransforms", Objects.<GpuBufferSlice>requireNonNull(this.dynamicTransforms));
/*  86 */       renderPass.bindTexture("Sampler2", context.lightmap(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
/*  88 */       drawLayers(context.stagedVertexBuffer(), group.layers, renderPass, context.textureManager());
/*  89 */       if (renderPass != null)
/*  89 */         renderPass.close(); 
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
/*     */   private static void drawLayers(StagedVertexBuffer stagedBuffer, Map<SingleQuadParticle.Layer, StagedVertexBuffer.Draw> layers, RenderPass renderPass, TextureManager textureManager) {
/*  93 */     for (Map.Entry<SingleQuadParticle.Layer, StagedVertexBuffer.Draw> entry : layers.entrySet()) {
/*  94 */       StagedVertexBuffer.ExecuteInfo executeInfo = stagedBuffer.getExecuteInfo(entry.getValue());
/*  95 */       if (executeInfo == null)
/*     */         continue; 
/*  99 */       renderPass.setPipeline(((SingleQuadParticle.Layer)entry.getKey()).pipeline());
/* 101 */       renderPass.setVertexBuffer(0, executeInfo.vertexBuffer().slice());
/* 102 */       renderPass.setIndexBuffer(executeInfo.indexBuffer(), executeInfo.indexType());
/* 104 */       AbstractTexture texture = textureManager.getTexture(((SingleQuadParticle.Layer)entry.getKey()).textureAtlasLocation());
/* 105 */       renderPass.bindTexture("Sampler0", texture.getTextureView(), texture.getSampler());
/* 107 */       renderPass.drawIndexed(executeInfo.indexCount(), 1, executeInfo.firstIndex(), executeInfo.baseVertex(), 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void finishExecute(FeatureFrameContext context) {
/* 113 */     this.groups.clear();
/* 114 */     this.dynamicTransforms = null;
/*     */   }
/*     */   
/*     */   private static final class PreparedGroup extends Record {
/*     */     private final Map<SingleQuadParticle.Layer, StagedVertexBuffer.Draw> layers;
/*     */     
/*     */     private final boolean translucent;
/*     */     
/*     */     private PreparedGroup(Map<SingleQuadParticle.Layer, StagedVertexBuffer.Draw> layers, boolean translucent) {
/* 117 */       this.layers = layers;
/* 117 */       this.translucent = translucent;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$PreparedGroup;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$PreparedGroup;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$PreparedGroup;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$PreparedGroup;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$PreparedGroup;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$PreparedGroup;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Map<SingleQuadParticle.Layer, StagedVertexBuffer.Draw> layers() {
/* 117 */       return this.layers;
/*     */     }
/*     */     
/*     */     public boolean translucent() {
/* 117 */       return this.translucent;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Submit extends Record implements SubmitNode {
/*     */     private final QuadParticleRenderState particles;
/*     */     
/*     */     private final boolean translucent;
/*     */     
/*     */     public Submit(QuadParticleRenderState particles, boolean translucent) {
/* 123 */       this.particles = particles;
/* 123 */       this.translucent = translucent;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$Submit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #123	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$Submit;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$Submit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #123	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$Submit;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #123	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$Submit;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public QuadParticleRenderState particles() {
/* 123 */       return this.particles;
/*     */     }
/*     */     
/*     */     public boolean translucent() {
/* 123 */       return this.translucent;
/*     */     }
/*     */     
/*     */     public FeatureRendererType<Submit> featureType() {
/* 129 */       return QuadParticleFeatureRenderer.TYPE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\QuadParticleFeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */