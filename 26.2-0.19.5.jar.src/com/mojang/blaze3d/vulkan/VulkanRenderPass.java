/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.BindGroupLayout;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.shaders.UniformType;
/*     */ import com.mojang.blaze3d.systems.GpuQueryPool;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderPassBackend;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vulkan.checkpoints.CheckpointExtension;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.lwjgl.vulkan.EXTMultiDraw;
/*     */ import org.lwjgl.vulkan.KHRPushDescriptor;
/*     */ import org.lwjgl.vulkan.KHRSynchronization2;
/*     */ import org.lwjgl.vulkan.VK12;
/*     */ import org.lwjgl.vulkan.VkBufferViewCreateInfo;
/*     */ import org.lwjgl.vulkan.VkCommandBuffer;
/*     */ import org.lwjgl.vulkan.VkDescriptorBufferInfo;
/*     */ import org.lwjgl.vulkan.VkDescriptorImageInfo;
/*     */ import org.lwjgl.vulkan.VkDrawIndexedIndirectCommand;
/*     */ import org.lwjgl.vulkan.VkDrawIndirectCommand;
/*     */ import org.lwjgl.vulkan.VkMultiDrawIndexedInfoEXT;
/*     */ import org.lwjgl.vulkan.VkMultiDrawInfoEXT;
/*     */ import org.lwjgl.vulkan.VkRect2D;
/*     */ import org.lwjgl.vulkan.VkViewport;
/*     */ import org.lwjgl.vulkan.VkWriteDescriptorSet;
/*     */ 
/*     */ public class VulkanRenderPass implements RenderPassBackend {
/*  45 */   public static final boolean VALIDATION = SharedConstants.IS_RUNNING_IN_IDE;
/*     */   
/*     */   private final VulkanDevice device;
/*     */   
/*     */   private final VulkanCommandEncoder encoder;
/*     */   
/*     */   private final CheckpointExtension.CheckpointStorage checkpointStorage;
/*     */   
/*     */   private final RenderPass.RenderArea renderArea;
/*     */   
/*     */   private final int outputWidth;
/*     */   
/*     */   private final int outputHeight;
/*     */   
/*     */   private final boolean hasDepth;
/*     */   
/*     */   private final Supplier<String> label;
/*     */   
/*  56 */   protected int pushedDebugGroups = 0;
/*     */   
/*     */   private final VkCommandBuffer commandBuffer;
/*     */   
/*     */   protected VulkanRenderPipeline pipeline;
/*     */   
/*     */   private boolean anyDescriptorDirty = false;
/*     */   
/*  63 */   protected final HashMap<String, GpuBufferSlice> uniforms = new HashMap<>();
/*     */   
/*  64 */   protected final HashMap<String, TextureViewAndSampler> textures = new HashMap<>();
/*     */   
/*     */   public VulkanRenderPass(VulkanDevice device, VulkanCommandEncoder encoder, VkCommandBuffer commandBuffer, CheckpointExtension.CheckpointStorage checkpointStorage, RenderPass.RenderArea renderArea, int outputWidth, int outputHeight, boolean hasDepth, Supplier<String> label) {
/*  67 */     this.device = device;
/*  68 */     this.encoder = encoder;
/*  69 */     this.commandBuffer = commandBuffer;
/*  70 */     this.checkpointStorage = checkpointStorage;
/*  71 */     this.renderArea = renderArea;
/*  72 */     this.outputWidth = outputWidth;
/*  73 */     this.outputHeight = outputHeight;
/*  74 */     this.hasDepth = hasDepth;
/*  75 */     this.label = label;
/*  77 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/*  79 */       VkViewport.Buffer viewport = VkViewport.calloc(1, stack);
/*  80 */       viewport.x(0.0F);
/*  81 */       viewport.y(0.0F);
/*  82 */       viewport.width(outputWidth);
/*  83 */       viewport.height(outputHeight);
/*  84 */       viewport.minDepth(0.0F);
/*  85 */       viewport.maxDepth(1.0F);
/*  87 */       VK12.vkCmdSetViewport(commandBuffer(), 0, viewport);
/*  88 */       setScissor(stack, commandBuffer(), renderArea.x(), renderArea.y(), renderArea.width(), renderArea.height());
/*  89 */       if (stack != null)
/*  89 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private VkCommandBuffer commandBuffer() {
/*  93 */     return this.commandBuffer;
/*     */   }
/*     */   
/*     */   public void pushDebugGroup(Supplier<String> label) {
/*  98 */     this.pushedDebugGroups++;
/*  99 */     this.device.instance().debug().beginDebugGroup(commandBuffer(), label);
/*     */   }
/*     */   
/*     */   public void popDebugGroup() {
/* 104 */     if (this.pushedDebugGroups == 0)
/* 105 */       throw new IllegalStateException("Can't pop more debug groups than was pushed!"); 
/* 107 */     this.pushedDebugGroups--;
/* 108 */     this.device.instance().debug().endDebugGroup(commandBuffer());
/*     */   }
/*     */   
/*     */   public void setPipeline(RenderPipeline pipeline) {
/* 113 */     this.pipeline = this.device.getOrCompilePipeline(pipeline);
/* 114 */     if (!this.pipeline.isValid())
/* 115 */       throw new IllegalStateException("Pipeline is not valid (may contain invalid shaders?)"); 
/* 117 */     this.anyDescriptorDirty = true;
/* 118 */     VK12.vkCmdBindPipeline(commandBuffer(), 0, this.hasDepth ? this.pipeline.withDepthPipeline() : this.pipeline.withoutDepthPipeline());
/*     */   }
/*     */   
/*     */   public void bindTexture(String name, GpuTextureView textureView, GpuSampler sampler) {
/* 123 */     if (textureView == null || sampler == null) {
/* 124 */       if (textureView != null || sampler != null)
/* 125 */         throw new IllegalArgumentException(); 
/* 127 */       this.textures.remove(name);
/*     */       return;
/*     */     } 
/* 130 */     this.textures.put(name, new TextureViewAndSampler((VulkanGpuTextureView)textureView, (VulkanGpuSampler)sampler));
/* 131 */     this.anyDescriptorDirty = true;
/*     */   }
/*     */   
/*     */   public void setUniform(String name, GpuBuffer value) {
/* 136 */     this.uniforms.put(name, value.slice());
/* 137 */     this.anyDescriptorDirty = true;
/*     */   }
/*     */   
/*     */   public void setUniform(String name, GpuBufferSlice value) {
/* 142 */     this.uniforms.put(name, value);
/* 143 */     this.anyDescriptorDirty = true;
/*     */   }
/*     */   
/*     */   public void enableScissor(int x, int y, int width, int height) {
/* 148 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 149 */       setScissor(stack, commandBuffer(), x, y, width, height);
/* 150 */       if (stack != null)
/* 150 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void setScissor(MemoryStack stack, VkCommandBuffer commandBuffer, int x, int y, int width, int height) {
/* 154 */     VkRect2D.Buffer scissor = VkRect2D.calloc(1, stack);
/* 155 */     scissor.offset().set(x, y);
/* 156 */     scissor.extent().set(width, height);
/* 157 */     VK12.vkCmdSetScissor(commandBuffer, 0, scissor);
/*     */   }
/*     */   
/*     */   public void disableScissor() {
/* 162 */     if (this.renderArea != null) {
/* 163 */       enableScissor(this.renderArea.x(), this.renderArea.y(), this.renderArea.width(), this.renderArea.height());
/*     */     } else {
/* 165 */       enableScissor(0, 0, this.outputWidth, this.outputHeight);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setVertexBuffer(int slot, GpuBufferSlice vertexBuffer) {
/* 171 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 172 */       long buffer = (vertexBuffer != null) ? ((VulkanGpuBuffer)vertexBuffer.buffer()).vkBuffer() : 0L;
/* 173 */       long offset = (vertexBuffer != null) ? vertexBuffer.offset() : 0L;
/* 174 */       VK12.vkCmdBindVertexBuffers(commandBuffer(), slot, stack.longs(buffer), stack.longs(offset));
/* 175 */       if (stack != null)
/* 175 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setIndexBuffer(GpuBuffer indexBuffer, IndexType indexType) {
/* 180 */     switch (indexType) {
/*     */       default:
/* 180 */         throw new MatchException(null, null);
/*     */       case SHORT:
/*     */       
/*     */       case INT:
/*     */         break;
/*     */     } 
/* 180 */     int type = 
/*     */       
/* 182 */       1;
/* 184 */     VK12.vkCmdBindIndexBuffer(commandBuffer(), ((VulkanGpuBuffer)indexBuffer).vkBuffer(), 0L, type);
/*     */   }
/*     */   
/*     */   public void drawIndexed(int indexCount, int instanceCount, int firstIndex, int vertexOffset, int firstInstance) {
/* 189 */     if (this.pipeline == null || !this.pipeline.isValid())
/* 190 */       throw new IllegalStateException("Pipeline is missing or not valid"); 
/* 192 */     pushDescriptors();
/* 193 */     VK12.vkCmdDrawIndexed(commandBuffer(), indexCount, instanceCount, firstIndex, vertexOffset, firstInstance);
/*     */   }
/*     */   
/*     */   public void multiDrawIndexed(IntBuffer drawParameters, int instanceCount, int firstInstance, int drawCount) {
/* 198 */     if (this.pipeline == null || !this.pipeline.isValid())
/* 199 */       throw new IllegalStateException("Pipeline is missing or not valid"); 
/* 201 */     pushDescriptors();
/* 202 */     EXTMultiDraw.nvkCmdDrawMultiIndexedEXT(commandBuffer(), drawCount, MemoryUtil.memAddress(drawParameters), instanceCount, firstInstance, VkMultiDrawIndexedInfoEXT.SIZEOF, 0L);
/*     */   }
/*     */   
/*     */   public void multiDrawIndexed(PointerBuffer firstIndexOffsets, IntBuffer indexCounts, IntBuffer vertexOffsets, int drawCount) {
/* 207 */     throw new UnsupportedOperationException("Vulkan does not support the multiDrawDirectSeparate device feature");
/*     */   }
/*     */   
/*     */   public void drawIndexedIndirect(GpuBufferSlice commands, int drawCount) {
/* 212 */     if (this.pipeline == null || !this.pipeline.isValid())
/* 213 */       throw new IllegalStateException("Pipeline is missing or not valid"); 
/* 215 */     pushDescriptors();
/* 216 */     VK12.vkCmdDrawIndexedIndirect(commandBuffer(), ((VulkanGpuBuffer)commands.buffer()).vkBuffer(), commands.offset(), drawCount, VkDrawIndexedIndirectCommand.SIZEOF);
/*     */   }
/*     */   
/*     */   public <T> void drawMultipleIndexed(Collection<RenderPass.Draw<T>> draws, GpuBuffer defaultIndexBuffer, IndexType defaultIndexType, Collection<String> dynamicUniforms, T uniformArgument) {
/* 221 */     for (RenderPass.Draw<T> draw : draws) {
/* 222 */       BiConsumer<T, RenderPass.UniformUploader> uniformUploaderConsumer = draw.uniformUploaderConsumer();
/* 223 */       if (uniformUploaderConsumer != null)
/* 224 */         uniformUploaderConsumer.accept(uniformArgument, this::setUniform); 
/* 227 */       assert draw.indexBuffer() != null || defaultIndexBuffer != null;
/* 228 */       assert draw.indexType() != null || defaultIndexType != null;
/* 229 */       setIndexBuffer((draw.indexBuffer() == null) ? defaultIndexBuffer : draw.indexBuffer(), (draw.indexType() == null) ? defaultIndexType : draw.indexType());
/* 230 */       setVertexBuffer(draw.slot(), draw.vertexBuffer().slice());
/* 231 */       drawIndexed(draw.indexCount(), 1, draw.firstIndex(), draw.baseVertex(), 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void draw(int vertexCount, int instanceCount, int firstVertex, int firstInstance) {
/* 237 */     if (this.pipeline == null || !this.pipeline.isValid())
/*     */       return; 
/* 240 */     pushDescriptors();
/* 241 */     VK12.vkCmdDraw(commandBuffer(), vertexCount, instanceCount, firstVertex, firstInstance);
/*     */   }
/*     */   
/*     */   public void multiDraw(IntBuffer drawParameters, int instanceCount, int firstInstance, int drawCount) {
/* 246 */     if (this.pipeline == null || !this.pipeline.isValid())
/* 247 */       throw new IllegalStateException("Pipeline is missing or not valid"); 
/* 249 */     pushDescriptors();
/* 250 */     EXTMultiDraw.nvkCmdDrawMultiEXT(commandBuffer(), drawCount, MemoryUtil.memAddress(drawParameters), instanceCount, firstInstance, VkMultiDrawInfoEXT.SIZEOF);
/*     */   }
/*     */   
/*     */   public void multiDraw(IntBuffer firstVertices, IntBuffer vertexCounts, int drawCount) {
/* 255 */     throw new UnsupportedOperationException("Vulkan does not support the multiDrawDirectSeparate device feature");
/*     */   }
/*     */   
/*     */   public void drawIndirect(GpuBufferSlice commands, int drawCount) {
/* 260 */     if (this.pipeline == null || !this.pipeline.isValid())
/* 261 */       throw new IllegalStateException("Pipeline is missing or not valid"); 
/* 263 */     pushDescriptors();
/* 264 */     VK12.vkCmdDrawIndirect(commandBuffer(), ((VulkanGpuBuffer)commands.buffer()).vkBuffer(), commands.offset(), drawCount, VkDrawIndirectCommand.SIZEOF);
/*     */   }
/*     */   
/*     */   private void pushDescriptors() {
/* 268 */     if (!this.anyDescriptorDirty)
/*     */       return; 
/* 272 */     if (VALIDATION)
/* 273 */       for (BindGroupLayout.UniformDescription uniform : BindGroupLayout.flattenUniforms(this.pipeline.info().getBindGroupLayouts())) {
/* 274 */         GpuBufferSlice value = this.uniforms.get(uniform.name());
/* 275 */         if (value == null)
/* 276 */           throw new IllegalStateException("Missing uniform " + uniform.name() + " (should be " + String.valueOf(uniform.type()) + ")"); 
/* 279 */         if (uniform.type() == UniformType.UNIFORM_BUFFER) {
/* 280 */           if (value.buffer().isClosed())
/* 281 */             throw new IllegalStateException("Uniform buffer " + uniform.name() + " is already closed"); 
/* 283 */           if ((value.buffer().usage() & 0x80) == 0)
/* 284 */             throw new IllegalStateException("Uniform buffer " + uniform.name() + " must have GpuBuffer.USAGE_UNIFORM"); 
/*     */         } 
/* 287 */         if (uniform.type() == UniformType.TEXEL_BUFFER) {
/* 288 */           if (value.offset() != 0L || value.length() != value.buffer().size())
/* 289 */             throw new IllegalStateException("Uniform texel buffers do not support a slice of a buffer, must be entire buffer"); 
/* 291 */           if ((value.buffer().usage() & 0x100) == 0)
/* 292 */             throw new IllegalStateException("Uniform texel buffer " + uniform.name() + " must have GpuBuffer.USAGE_UNIFORM_TEXEL_BUFFER"); 
/* 294 */           if (uniform.gpuFormat() == null)
/* 295 */             throw new IllegalStateException("Invalid uniform texel buffer " + uniform.name() + " (missing a texture format)"); 
/*     */         } 
/*     */       }  
/* 305 */     assert this.pipeline != null;
/* 306 */     VulkanBindGroupLayout layout = this.pipeline.layout();
/* 307 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 308 */       VkWriteDescriptorSet.Buffer writes = VkWriteDescriptorSet.calloc(layout.entries().size(), stack);
/* 309 */       for (int i = 0; i < layout.entries().size(); i++) {
/* 311 */         VulkanBindGroupLayout.Entry entry = layout.entries().get(i);
/* 312 */         VkWriteDescriptorSet set = ((VkWriteDescriptorSet)writes.get()).sType$Default();
/* 313 */         set.dstBinding(i);
/* 314 */         set.dstArrayElement(0);
/* 315 */         set.descriptorCount(1);
/* 316 */         if (entry.type() == VulkanBindGroupLayout.VulkanBindGroupEntryType.UNIFORM_BUFFER) {
/* 317 */           GpuBufferSlice buffer = this.uniforms.get(entry.name());
/* 318 */           if (buffer == null)
/* 319 */             throw new IllegalStateException("Missing uniform " + entry.name() + " (should be " + String.valueOf(entry.type()) + ")"); 
/* 322 */           VkDescriptorBufferInfo.Buffer bufferInfo = VkDescriptorBufferInfo.calloc(1, stack);
/* 323 */           bufferInfo.buffer(((VulkanGpuBuffer)buffer.buffer()).vkBuffer());
/* 324 */           bufferInfo.offset(buffer.offset());
/* 325 */           bufferInfo.range(buffer.length());
/* 327 */           set.descriptorType(6);
/* 328 */           set.pBufferInfo(bufferInfo);
/* 329 */         } else if (entry.type() == VulkanBindGroupLayout.VulkanBindGroupEntryType.SAMPLED_IMAGE) {
/* 330 */           TextureViewAndSampler value = this.textures.get(entry.name());
/* 331 */           if (value == null)
/* 332 */             throw new IllegalStateException("Missing sampler " + entry.name()); 
/* 334 */           VkDescriptorImageInfo.Buffer imageInfo = VkDescriptorImageInfo.calloc(1, stack);
/* 335 */           imageInfo.sampler(value.sampler.vkSampler());
/* 336 */           imageInfo.imageView(value.view.vkImageView());
/* 337 */           imageInfo.imageLayout(1);
/* 339 */           set.descriptorType(1);
/* 340 */           set.pImageInfo(imageInfo);
/* 341 */         } else if (entry.type() == VulkanBindGroupLayout.VulkanBindGroupEntryType.TEXEL_BUFFER) {
/* 342 */           GpuBufferSlice value = this.uniforms.get(entry.name());
/* 343 */           if (value == null)
/* 344 */             throw new IllegalStateException("Missing uniform " + entry.name() + " (should be " + String.valueOf(entry.type()) + ")"); 
/* 348 */           LongBuffer bufferViewPtr = stack.callocLong(1);
/* 349 */           MemoryStack memoryStack = stack.push();
/*     */           try {
/* 350 */             assert entry.texelBufferFormat() != null;
/* 351 */             VkBufferViewCreateInfo viewCreateInfo = VkBufferViewCreateInfo.calloc(stack).sType$Default();
/* 352 */             viewCreateInfo.buffer(((VulkanGpuBuffer)value.buffer()).vkBuffer());
/* 353 */             viewCreateInfo.offset(value.offset());
/* 354 */             viewCreateInfo.range(value.length());
/* 355 */             viewCreateInfo.format(VulkanConst.toVk(entry.texelBufferFormat()));
/* 357 */             VulkanUtils.crashIfFailure(this.device, VK12.vkCreateBufferView(this.device.vkDevice(), viewCreateInfo, null, bufferViewPtr), "Couldn't create buffer view for texel buffer");
/* 359 */             long bufferViewHandle = bufferViewPtr.get(0);
/* 360 */             this.encoder.queueForDestroy(() -> VK12.vkDestroyBufferView(this.device.vkDevice(), bufferViewHandle, null));
/* 363 */             if (memoryStack != null)
/* 363 */               memoryStack.close(); 
/*     */           } catch (Throwable throwable) {
/*     */             if (memoryStack != null)
/*     */               try {
/*     */                 memoryStack.close();
/*     */               } catch (Throwable throwable1) {
/*     */                 throwable.addSuppressed(throwable1);
/*     */               }  
/*     */             throw throwable;
/*     */           } 
/* 365 */           set.descriptorType(4);
/* 366 */           set.pTexelBufferView(bufferViewPtr);
/*     */         } 
/*     */       } 
/* 369 */       KHRPushDescriptor.vkCmdPushDescriptorSetKHR(commandBuffer(), 0, this.pipeline.pipelineLayout(), 0, (VkWriteDescriptorSet.Buffer)writes.flip());
/* 370 */       if (stack != null)
/* 370 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 371 */     this.anyDescriptorDirty = false;
/*     */   }
/*     */   
/*     */   public void writeTimestamp(GpuQueryPool pool, int index) {
/* 376 */     long queryPool = ((VulkanQueryPool)pool).vkQueryPool();
/* 379 */     VK12.vkResetQueryPool(this.device.vkDevice(), queryPool, index, 1);
/* 380 */     KHRSynchronization2.vkCmdWriteTimestamp2KHR(commandBuffer(), 65536L, queryPool, index);
/*     */   }
/*     */   
/*     */   public Supplier<String> getLabel() {
/* 384 */     return this.label;
/*     */   }
/*     */   
/*     */   protected static final class TextureViewAndSampler extends Record {
/*     */     private final VulkanGpuTextureView view;
/*     */     
/*     */     private final VulkanGpuSampler sampler;
/*     */     
/*     */     protected TextureViewAndSampler(VulkanGpuTextureView view, VulkanGpuSampler sampler) {
/* 387 */       this.view = view;
/* 387 */       this.sampler = sampler;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanRenderPass$TextureViewAndSampler;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #387	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanRenderPass$TextureViewAndSampler;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanRenderPass$TextureViewAndSampler;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #387	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanRenderPass$TextureViewAndSampler;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanRenderPass$TextureViewAndSampler;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #387	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanRenderPass$TextureViewAndSampler;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public VulkanGpuTextureView view() {
/* 387 */       return this.view;
/*     */     }
/*     */     
/*     */     public VulkanGpuSampler sampler() {
/* 387 */       return this.sampler;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanRenderPass.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */