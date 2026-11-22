/*    */ package net.minecraft.client.renderer.rendertype;
/*    */ 
/*    */ import com.mojang.blaze3d.IndexType;
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*    */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*    */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*    */ import com.mojang.blaze3d.systems.RenderPass;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.systems.ScissorState;
/*    */ import com.mojang.blaze3d.textures.GpuSampler;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalDouble;
/*    */ import net.minecraft.client.renderer.StagedVertexBuffer;
/*    */ 
/*    */ public final class PreparedRenderType extends Record {
/*    */   private final RenderPipeline pipeline;
/*    */   
/*    */   private final OutputTarget outputTarget;
/*    */   
/*    */   private final GpuBufferSlice dynamicTransforms;
/*    */   
/*    */   private final ScissorState scissorState;
/*    */   
/*    */   private final List<Texture> textures;
/*    */   
/*    */   public PreparedRenderType(RenderPipeline pipeline, OutputTarget outputTarget, GpuBufferSlice dynamicTransforms, ScissorState scissorState, List<Texture> textures) {
/* 19 */     this.pipeline = pipeline;
/* 19 */     this.outputTarget = outputTarget;
/* 19 */     this.dynamicTransforms = dynamicTransforms;
/* 19 */     this.scissorState = scissorState;
/* 19 */     this.textures = textures;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/rendertype/PreparedRenderType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/PreparedRenderType;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/rendertype/PreparedRenderType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/PreparedRenderType;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/rendertype/PreparedRenderType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/rendertype/PreparedRenderType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public RenderPipeline pipeline() {
/* 19 */     return this.pipeline;
/*    */   }
/*    */   
/*    */   public OutputTarget outputTarget() {
/* 19 */     return this.outputTarget;
/*    */   }
/*    */   
/*    */   public GpuBufferSlice dynamicTransforms() {
/* 19 */     return this.dynamicTransforms;
/*    */   }
/*    */   
/*    */   public ScissorState scissorState() {
/* 19 */     return this.scissorState;
/*    */   }
/*    */   
/*    */   public List<Texture> textures() {
/* 19 */     return this.textures;
/*    */   }
/*    */   
/*    */   public void drawFromBuffer(StagedVertexBuffer.ExecuteInfo info) {
/* 28 */     drawFromBuffer(info.vertexBuffer(), info.indexBuffer(), info.indexType(), info.baseVertex(), info.firstIndex(), info.indexCount());
/*    */   }
/*    */   
/*    */   public void drawFromBuffer(GpuBuffer vertexBuffer, GpuBuffer indexBuffer, IndexType indexType, int baseVertex, int firstIndex, int indexCount) {
/* 32 */     RenderTarget renderTarget = this.outputTarget.getRenderTarget();
/* 33 */     GpuTextureView colorTexture = (RenderSystem.outputColorTextureOverride != null) ? RenderSystem.outputColorTextureOverride : renderTarget.getColorTextureView();
/* 34 */     GpuTextureView depthTexture = renderTarget.useDepth ? ((RenderSystem.outputDepthTextureOverride != null) ? RenderSystem.outputDepthTextureOverride : renderTarget.getDepthTextureView()) : null;
/* 35 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Immediate draw with " + String.valueOf(this.pipeline), colorTexture, Optional.empty(), depthTexture, OptionalDouble.empty());
/*    */     try {
/* 36 */       renderPass.setPipeline(this.pipeline);
/* 37 */       if (this.scissorState.enabled())
/* 38 */         renderPass.enableScissor(this.scissorState.x(), this.scissorState.y(), this.scissorState.width(), this.scissorState.height()); 
/* 40 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 41 */       renderPass.setUniform("DynamicTransforms", this.dynamicTransforms);
/* 42 */       renderPass.setVertexBuffer(0, vertexBuffer.slice());
/* 44 */       for (Texture texture : this.textures)
/* 45 */         renderPass.bindTexture(texture.name, texture.textureView, texture.sampler); 
/* 48 */       renderPass.setIndexBuffer(indexBuffer, indexType);
/* 49 */       renderPass.drawIndexed(indexCount, 1, firstIndex, baseVertex, 0);
/* 50 */       if (renderPass != null)
/* 50 */         renderPass.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (renderPass != null)
/*    */         try {
/*    */           renderPass.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static final class Texture extends Record {
/*    */     private final String name;
/*    */     
/*    */     private final GpuTextureView textureView;
/*    */     
/*    */     private final GpuSampler sampler;
/*    */     
/*    */     public Texture(String name, GpuTextureView textureView, GpuSampler sampler) {
/* 53 */       this.name = name;
/* 53 */       this.textureView = textureView;
/* 53 */       this.sampler = sampler;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/rendertype/PreparedRenderType$Texture;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/PreparedRenderType$Texture;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/rendertype/PreparedRenderType$Texture;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/PreparedRenderType$Texture;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/rendertype/PreparedRenderType$Texture;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/rendertype/PreparedRenderType$Texture;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public String name() {
/* 53 */       return this.name;
/*    */     }
/*    */     
/*    */     public GpuTextureView textureView() {
/* 53 */       return this.textureView;
/*    */     }
/*    */     
/*    */     public GpuSampler sampler() {
/* 53 */       return this.sampler;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\rendertype\PreparedRenderType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */