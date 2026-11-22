/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import com.mojang.blaze3d.IndexType;
/*    */ import com.mojang.blaze3d.PrimitiveTopology;
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*    */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*    */ import com.mojang.blaze3d.systems.RenderPass;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.FilterMode;
/*    */ import com.mojang.blaze3d.textures.GpuSampler;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.EnumMap;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalDouble;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ 
/*    */ public final class ChunkSectionsToRender extends Record {
/*    */   private final GpuTextureView textureView;
/*    */   
/*    */   private final EnumMap<ChunkSectionLayer, Int2ObjectOpenHashMap<List<RenderPass.Draw<GpuBufferSlice[]>>>> drawGroupsPerLayer;
/*    */   
/*    */   private final int maxIndicesRequired;
/*    */   
/*    */   private final GpuBufferSlice[] chunkSectionInfos;
/*    */   
/*    */   public ChunkSectionsToRender(GpuTextureView textureView, EnumMap<ChunkSectionLayer, Int2ObjectOpenHashMap<List<RenderPass.Draw<GpuBufferSlice[]>>>> drawGroupsPerLayer, int maxIndicesRequired, GpuBufferSlice[] chunkSectionInfos) {
/* 23 */     this.textureView = textureView;
/* 23 */     this.drawGroupsPerLayer = drawGroupsPerLayer;
/* 23 */     this.maxIndicesRequired = maxIndicesRequired;
/* 23 */     this.chunkSectionInfos = chunkSectionInfos;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public GpuTextureView textureView() {
/* 23 */     return this.textureView;
/*    */   }
/*    */   
/*    */   public EnumMap<ChunkSectionLayer, Int2ObjectOpenHashMap<List<RenderPass.Draw<GpuBufferSlice[]>>>> drawGroupsPerLayer() {
/* 23 */     return this.drawGroupsPerLayer;
/*    */   }
/*    */   
/*    */   public int maxIndicesRequired() {
/* 23 */     return this.maxIndicesRequired;
/*    */   }
/*    */   
/*    */   public GpuBufferSlice[] chunkSectionInfos() {
/* 23 */     return this.chunkSectionInfos;
/*    */   }
/*    */   
/*    */   public void renderGroup(ChunkSectionLayerGroup group, GpuSampler sampler) {
/* 26 */     RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
/* 27 */     GpuBuffer defaultIndexBuffer = (this.maxIndicesRequired == 0) ? null : autoIndices.getBuffer(this.maxIndicesRequired);
/* 28 */     IndexType defaultIndexType = (this.maxIndicesRequired == 0) ? null : autoIndices.type();
/* 29 */     ChunkSectionLayer[] layers = group.layers();
/* 30 */     Minecraft minecraft = Minecraft.getInstance();
/* 31 */     boolean wireframe = (SharedConstants.DEBUG_HOTKEYS && minecraft.wireframe);
/* 33 */     RenderTarget renderTarget = group.outputTarget();
/* 34 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Section layers for " + group.label(), renderTarget.getColorTextureView(), Optional.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty());
/*    */     try {
/* 35 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 36 */       renderPass.bindTexture("Sampler0", this.textureView, sampler);
/* 37 */       renderPass.bindTexture("Sampler2", minecraft.gameRenderer.lightmap(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
/* 39 */       for (ChunkSectionLayer layer : layers) {
/* 40 */         renderPass.setPipeline(wireframe ? RenderPipelines.WIREFRAME : layer.pipeline());
/* 41 */         Int2ObjectOpenHashMap<List<RenderPass.Draw<GpuBufferSlice[]>>> drawGroup = this.drawGroupsPerLayer.get(layer);
/* 42 */         for (ObjectIterator<List<RenderPass.Draw<GpuBufferSlice[]>>> objectIterator = drawGroup.values().iterator(); objectIterator.hasNext(); ) {
/* 42 */           List<RenderPass.Draw<GpuBufferSlice[]>> draws = objectIterator.next();
/* 43 */           if (!draws.isEmpty()) {
/* 44 */             if (layer == ChunkSectionLayer.TRANSLUCENT)
/* 45 */               draws = draws.reversed(); 
/* 47 */             renderPass.drawMultipleIndexed(draws, defaultIndexBuffer, defaultIndexType, List.of("ChunkSection"), this.chunkSectionInfos);
/*    */           } 
/*    */         } 
/*    */       } 
/* 51 */       if (renderPass != null)
/* 51 */         renderPass.close(); 
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
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\ChunkSectionsToRender.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */