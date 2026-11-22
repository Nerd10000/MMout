/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.GpuFence;
/*     */ import com.mojang.blaze3d.systems.CommandEncoderBackend;
/*     */ import com.mojang.blaze3d.systems.GpuQueryPool;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderPassBackend;
/*     */ import com.mojang.blaze3d.systems.RenderPassDescriptor;
/*     */ import com.mojang.blaze3d.systems.TransientMemory;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vulkan.checkpoints.CheckpointExtension;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import org.joml.Vector4fc;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.vulkan.KHRDynamicRendering;
/*     */ import org.lwjgl.vulkan.KHRSynchronization2;
/*     */ import org.lwjgl.vulkan.VK12;
/*     */ import org.lwjgl.vulkan.VkBufferCopy;
/*     */ import org.lwjgl.vulkan.VkBufferImageCopy;
/*     */ import org.lwjgl.vulkan.VkClearAttachment;
/*     */ import org.lwjgl.vulkan.VkClearColorValue;
/*     */ import org.lwjgl.vulkan.VkClearDepthStencilValue;
/*     */ import org.lwjgl.vulkan.VkClearRect;
/*     */ import org.lwjgl.vulkan.VkClearValue;
/*     */ import org.lwjgl.vulkan.VkCommandBuffer;
/*     */ import org.lwjgl.vulkan.VkCommandBufferBeginInfo;
/*     */ import org.lwjgl.vulkan.VkDependencyInfo;
/*     */ import org.lwjgl.vulkan.VkImageCopy;
/*     */ import org.lwjgl.vulkan.VkImageSubresourceLayers;
/*     */ import org.lwjgl.vulkan.VkImageSubresourceRange;
/*     */ import org.lwjgl.vulkan.VkMemoryBarrier2;
/*     */ import org.lwjgl.vulkan.VkRect2D;
/*     */ import org.lwjgl.vulkan.VkRenderingAttachmentInfo;
/*     */ import org.lwjgl.vulkan.VkRenderingInfo;
/*     */ import org.lwjgl.vulkan.VkSemaphoreCreateInfo;
/*     */ import org.lwjgl.vulkan.VkSemaphoreTypeCreateInfo;
/*     */ import org.lwjgl.vulkan.VkSemaphoreWaitInfo;
/*     */ 
/*     */ public class VulkanCommandEncoder implements CommandEncoderBackend, Destroyable {
/*     */   public static final int MAX_SUBMITS_IN_FLIGHT = 2;
/*     */   
/*     */   private final VulkanDevice device;
/*     */   
/*     */   private final VulkanTransientMemory transientMemory;
/*     */   
/*     */   private final long submitSemaphore;
/*     */   
/*  59 */   private long currentSubmitIndex = 2L;
/*     */   
/*  60 */   private long completedSubmitIndex = 0L;
/*     */   
/*     */   private final CheckpointExtension.CheckpointStorage checkpointStorage;
/*     */   
/*     */   private VulkanQueue.Submission submissionBuilder;
/*     */   
/*  65 */   private final DestructionQueue<Destroyable> destroyQueue = new DestructionQueue<>(2, Destroyable::destroy);
/*     */   
/*  67 */   private final VulkanCommandPool[] commandPools = new VulkanCommandPool[2];
/*     */   
/*     */   private VkCommandBuffer currentCommandBuffer;
/*     */   
/*     */   private VulkanRenderPass currentRenderPass;
/*     */   
/*     */   public VulkanCommandEncoder(VulkanDevice device) {
/*  73 */     this.device = device;
/*  74 */     this.transientMemory = new VulkanTransientMemory(device, this);
/*  76 */     MemoryStack baseStack = MemoryStack.stackGet();
/*  77 */     MemoryStack stack = baseStack.push();
/*     */     try {
/*  78 */       VkSemaphoreTypeCreateInfo semaphoreTypeCreateInfo = VkSemaphoreTypeCreateInfo.calloc(stack).sType$Default();
/*  79 */       semaphoreTypeCreateInfo.semaphoreType(1);
/*  80 */       semaphoreTypeCreateInfo.initialValue(this.currentSubmitIndex - 1L);
/*  81 */       VkSemaphoreCreateInfo semaphoreCreateInfo = VkSemaphoreCreateInfo.calloc(stack).sType$Default();
/*  82 */       semaphoreCreateInfo.pNext(semaphoreTypeCreateInfo);
/*  83 */       LongBuffer semaphoreHandlePtr = stack.callocLong(1);
/*  84 */       VulkanUtils.crashIfFailure(device, VK12.vkCreateSemaphore(device.vkDevice(), semaphoreCreateInfo, null, semaphoreHandlePtr), "Failed to create submit VkSemaphore");
/*  85 */       this.submitSemaphore = semaphoreHandlePtr.get(0);
/*  86 */       if (stack != null)
/*  86 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*  87 */     for (int i = 0; i < 2; i++)
/*  88 */       this.commandPools[i] = new VulkanCommandPool(device, device.graphicsQueue()); 
/*  90 */     this.checkpointStorage = device.checkpointExtension().createStorage(device, device.graphicsQueue(), 2);
/*  92 */     this.submissionBuilder = device.graphicsQueue().beginSubmit();
/*  93 */     this.transientMemory.beginSubmit();
/*     */   }
/*     */   
/*     */   public void destroy() {
/*  98 */     this.transientMemory.endSubmit();
/*  99 */     this.submissionBuilder.close();
/* 100 */     this.device.graphicsQueue().waitIdle();
/* 101 */     this.destroyQueue.close();
/* 102 */     this.transientMemory.destroy();
/* 106 */     this.destroyQueue.close();
/* 107 */     for (int i = 0; i < 2; i++)
/* 108 */       this.commandPools[i].destroy(); 
/* 110 */     VK12.vkDestroySemaphore(this.device.vkDevice(), this.submitSemaphore, null);
/*     */   }
/*     */   
/*     */   public void queueForDestroy(Destroyable destroyable) {
/* 114 */     this.destroyQueue.add(destroyable);
/*     */   }
/*     */   
/*     */   private VulkanCommandPool currentCommandPool() {
/* 118 */     return this.commandPools[(int)(this.currentSubmitIndex % 2L)];
/*     */   }
/*     */   
/*     */   public VkCommandBuffer allocateAndBeginTransientCommandBuffer() {
/* 122 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 123 */       VkCommandBuffer commandBuffer = currentCommandPool().allocateBuffer();
/* 125 */       VkCommandBufferBeginInfo beginInfo = VkCommandBufferBeginInfo.calloc(stack).sType$Default();
/* 126 */       beginInfo.flags(1);
/* 127 */       VulkanUtils.crashIfFailure(this.device, VK12.vkBeginCommandBuffer(commandBuffer, beginInfo), "Failed to begin VkCommandBuffer");
/* 129 */       VkCommandBuffer vkCommandBuffer1 = commandBuffer;
/* 130 */       if (stack != null)
/* 130 */         stack.close(); 
/*     */       return vkCommandBuffer1;
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
/* 134 */     if (this.currentCommandBuffer != null)
/* 135 */       return this.currentCommandBuffer; 
/* 137 */     if (this.currentRenderPass != null)
/* 138 */       throw new IllegalStateException("Cannot start command buffer while inside RenderPass"); 
/* 140 */     this.currentCommandBuffer = allocateAndBeginTransientCommandBuffer();
/* 141 */     this.submissionBuilder.executeCommands(this.currentCommandBuffer);
/* 142 */     return this.currentCommandBuffer;
/*     */   }
/*     */   
/*     */   VkCommandBuffer textureInitCommandBuffer() {
/* 149 */     return commandBuffer();
/*     */   }
/*     */   
/*     */   private void endCommandBuffer() {
/* 153 */     if (this.currentCommandBuffer == null)
/*     */       return; 
/* 156 */     if (this.currentRenderPass != null)
/* 157 */       throw new IllegalStateException("Cannot end command buffer while inside RenderPass"); 
/* 159 */     VulkanUtils.crashIfFailure(this.device, VK12.vkEndCommandBuffer(this.currentCommandBuffer), "Failed to end VkCommandBuffer");
/* 160 */     this.currentCommandBuffer = null;
/*     */   }
/*     */   
/*     */   public void waitSemaphore(long vkSemaphore, long value, long stageMask) {
/* 164 */     if (this.currentRenderPass != null)
/* 165 */       throw new IllegalStateException("Cannot add semaphore operation while inside RenderPass"); 
/* 167 */     endCommandBuffer();
/* 168 */     this.submissionBuilder.waitSemaphore(vkSemaphore, value, stageMask);
/*     */   }
/*     */   
/*     */   public void execute(VkCommandBuffer commandBuffer) {
/* 172 */     if (this.currentRenderPass != null)
/* 173 */       throw new IllegalStateException("Cannot execute command buffer while inside RenderPass"); 
/* 175 */     endCommandBuffer();
/* 176 */     this.submissionBuilder.executeCommands(commandBuffer);
/*     */   }
/*     */   
/*     */   public void signalSemaphore(long vkSemaphore, long value, long stageMask) {
/* 180 */     if (this.currentRenderPass != null)
/* 181 */       throw new IllegalStateException("Cannot add semaphore operation while inside RenderPass"); 
/* 183 */     endCommandBuffer();
/* 184 */     this.submissionBuilder.signalSemaphore(vkSemaphore, value, stageMask);
/*     */   }
/*     */   
/*     */   private void memoryBarrier(MemoryStack stack) {
/* 188 */     memoryBarrier(commandBuffer(), stack);
/*     */   }
/*     */   
/*     */   public static void memoryBarrier(VkCommandBuffer commandBuffer, MemoryStack stack) {
/* 192 */     VkMemoryBarrier2.Buffer memoryBarrier = VkMemoryBarrier2.calloc(1, stack).sType$Default();
/* 193 */     memoryBarrier.srcStageMask(65536L);
/* 194 */     memoryBarrier.srcAccessMask(98304L);
/* 195 */     memoryBarrier.dstStageMask(65536L);
/* 196 */     memoryBarrier.dstAccessMask(98304L);
/* 197 */     VkDependencyInfo depInfo = VkDependencyInfo.calloc(stack).sType$Default();
/* 198 */     depInfo.pMemoryBarriers(memoryBarrier);
/* 199 */     KHRSynchronization2.vkCmdPipelineBarrier2KHR(commandBuffer, depInfo);
/*     */   }
/*     */   
/*     */   public void submit() {
/* 204 */     endCommandBuffer();
/* 205 */     this.transientMemory.endSubmit();
/* 206 */     signalSemaphore(this.submitSemaphore, this.currentSubmitIndex, 65536L);
/* 207 */     this.submissionBuilder.close();
/* 208 */     this.submissionBuilder = this.device.graphicsQueue().beginSubmit();
/* 210 */     this.currentSubmitIndex++;
/* 213 */     if (!awaitSubmitCompletion(this.currentSubmitIndex - 2L, 5000000000L)) {
/* 214 */       List<CheckpointExtension.QueueCheckpoints> checkpoints = this.device.checkpointExtension().retrieveCheckpoints(false);
/* 215 */       throw new IllegalStateException("5s timeout reached when waiting for VK semaphore: " + VulkanUtils.formatCheckpoints(checkpoints));
/*     */     } 
/* 218 */     currentCommandPool().reset();
/* 219 */     this.destroyQueue.rotate();
/* 220 */     this.checkpointStorage.rotate();
/* 221 */     this.transientMemory.beginSubmit();
/*     */   }
/*     */   
/*     */   public TransientMemory transientMemory() {
/* 226 */     return this.transientMemory;
/*     */   }
/*     */   
/*     */   public RenderPassBackend createRenderPass(RenderPassDescriptor descriptor) {
/* 231 */     List<RenderPassDescriptor.Attachment<Optional<Vector4fc>>> colorAttachments = descriptor.colorAttachments();
/* 232 */     VulkanGpuTextureView[] colorTextures = new VulkanGpuTextureView[colorAttachments.size()];
/* 233 */     for (int i = 0; i < colorAttachments.size(); i++) {
/* 234 */       RenderPassDescriptor.Attachment<Optional<Vector4fc>> attachment = colorAttachments.get(i);
/* 235 */       colorTextures[i] = (attachment != null) ? (VulkanGpuTextureView)attachment.textureView() : null;
/*     */     } 
/* 237 */     RenderPassDescriptor.Attachment<OptionalDouble> depthAttachment = descriptor.depthAttachment();
/* 239 */     this.device.instance().debug().beginDebugGroup(commandBuffer(), descriptor.label());
/* 240 */     this.checkpointStorage.recordCheckpoint(commandBuffer(), CheckpointExtension.CheckpointType.BEGIN_RENDER_PASS, descriptor.label());
/* 242 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 243 */       int width = 0;
/* 244 */       int height = 0;
/* 245 */       if (!colorAttachments.isEmpty()) {
/* 246 */         for (RenderPassDescriptor.Attachment<Optional<Vector4fc>> colorAttachment : colorAttachments) {
/* 247 */           if (colorAttachment == null)
/*     */             continue; 
/* 250 */           GpuTextureView colorTexture = colorAttachment.textureView();
/* 251 */           width = colorTexture.getWidth(0);
/* 252 */           height = colorTexture.getHeight(0);
/*     */         } 
/* 254 */       } else if (depthAttachment != null) {
/* 255 */         width = depthAttachment.textureView().getWidth(0);
/* 256 */         height = depthAttachment.textureView().getHeight(0);
/*     */       } 
/* 259 */       VkRect2D vkRenderArea = VkRect2D.calloc(stack);
/* 260 */       assert descriptor.renderArea != null;
/* 261 */       vkRenderArea.extent().set(descriptor.renderArea.width(), descriptor.renderArea.height());
/* 262 */       vkRenderArea.offset().set(descriptor.renderArea.x(), descriptor.renderArea.y());
/* 264 */       VkRenderingAttachmentInfo.Buffer colorAttachmentInfo = VkRenderingAttachmentInfo.calloc(colorAttachments.size(), stack);
/* 265 */       for (int j = 0; j < colorAttachments.size(); j++) {
/* 266 */         ((VkRenderingAttachmentInfo.Buffer)colorAttachmentInfo.position(j)).sType$Default();
/* 267 */         VulkanGpuTextureView colorTexture = colorTextures[j];
/* 268 */         if (colorTexture != null) {
/* 269 */           colorAttachmentInfo.imageView(colorTexture.vkImageView());
/* 270 */           colorAttachmentInfo.imageLayout(1);
/* 271 */           colorAttachmentInfo.storeOp(0);
/* 272 */           RenderPassDescriptor.Attachment<Optional<Vector4fc>> attachment = colorAttachments.get(j);
/* 273 */           Optional<Vector4fc> clearValue = (Optional<Vector4fc>)attachment.clearValue();
/* 274 */           if (clearValue.isPresent()) {
/* 275 */             Vector4fc color = clearValue.get();
/* 276 */             VkClearColorValue vkClearColor = VulkanUtils.putArgb(VkClearColorValue.calloc(stack), color);
/* 277 */             colorAttachmentInfo.loadOp(1);
/* 278 */             colorAttachmentInfo.clearValue(VkClearValue.calloc(stack).color(vkClearColor));
/*     */           } else {
/* 280 */             colorAttachmentInfo.loadOp(0);
/*     */           } 
/*     */         } else {
/* 283 */           colorAttachmentInfo.imageView(0L);
/* 284 */           colorAttachmentInfo.imageLayout(0);
/* 285 */           colorAttachmentInfo.storeOp(1);
/* 286 */           colorAttachmentInfo.loadOp(2);
/*     */         } 
/*     */       } 
/* 289 */       colorAttachmentInfo.position(0);
/* 291 */       VkRenderingInfo renderingInfo = VkRenderingInfo.calloc(stack).sType$Default();
/* 292 */       renderingInfo.renderArea(vkRenderArea);
/* 293 */       renderingInfo.layerCount(1);
/* 294 */       renderingInfo.viewMask(0);
/* 295 */       renderingInfo.pColorAttachments(colorAttachmentInfo);
/* 296 */       if (depthAttachment != null) {
/* 297 */         VkRenderingAttachmentInfo depthAttachmentInfo = VkRenderingAttachmentInfo.calloc(stack).sType$Default();
/* 298 */         VulkanGpuTextureView vulkanDepthAttachment = (VulkanGpuTextureView)depthAttachment.textureView();
/* 299 */         depthAttachmentInfo.imageView(vulkanDepthAttachment.vkImageView());
/* 300 */         depthAttachmentInfo.imageLayout(1);
/* 301 */         depthAttachmentInfo.storeOp(0);
/* 302 */         OptionalDouble clearValue = (OptionalDouble)depthAttachment.clearValue();
/* 303 */         if (clearValue.isPresent()) {
/* 304 */           double color = clearValue.getAsDouble();
/* 305 */           VkClearDepthStencilValue vkClearColor = VkClearDepthStencilValue.calloc(stack).depth((float)color);
/* 306 */           depthAttachmentInfo.loadOp(1);
/* 307 */           depthAttachmentInfo.clearValue(VkClearValue.calloc(stack).depthStencil(vkClearColor));
/*     */         } else {
/* 309 */           depthAttachmentInfo.loadOp(0);
/*     */         } 
/* 311 */         renderingInfo.pDepthAttachment(depthAttachmentInfo);
/*     */       } 
/* 314 */       KHRDynamicRendering.vkCmdBeginRenderingKHR(commandBuffer(), renderingInfo);
/* 316 */       this.currentRenderPass = new VulkanRenderPass(this.device, this, commandBuffer(), this.checkpointStorage, descriptor.renderArea, width, height, (depthAttachment != null), descriptor.label());
/* 317 */       if (stack != null)
/* 317 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 319 */     return this.currentRenderPass;
/*     */   }
/*     */   
/*     */   public void submitRenderPass() {
/* 324 */     if (this.currentRenderPass == null)
/* 325 */       throw new IllegalStateException("Cannot submit a renderpass if one hasn't been started!"); 
/* 327 */     KHRDynamicRendering.vkCmdEndRenderingKHR(commandBuffer());
/* 328 */     this.device.instance().debug().endDebugGroup(commandBuffer());
/* 329 */     this.checkpointStorage.recordCheckpoint(commandBuffer(), CheckpointExtension.CheckpointType.END_RENDER_PASS, this.currentRenderPass.getLabel());
/* 330 */     this.currentRenderPass = null;
/* 331 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 332 */       memoryBarrier(stack);
/* 333 */       if (stack != null)
/* 333 */         stack.close(); 
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
/*     */   private void clearColorTextureUnsynced(MemoryStack stack, GpuTexture colorTexture, Vector4fc clearColor) {
/* 337 */     VkClearColorValue vkClearColor = VulkanUtils.putArgb(VkClearColorValue.calloc(stack), clearColor);
/* 338 */     VkImageSubresourceRange subresourceRange = VkImageSubresourceRange.calloc(stack);
/* 339 */     subresourceRange.baseMipLevel(0);
/* 340 */     subresourceRange.levelCount(colorTexture.getMipLevels());
/* 341 */     subresourceRange.baseArrayLayer(0);
/* 342 */     subresourceRange.layerCount(1);
/* 343 */     subresourceRange.aspectMask(1);
/* 345 */     VK12.vkCmdClearColorImage(commandBuffer(), ((VulkanGpuTexture)colorTexture).vkImage(), 1, vkClearColor, subresourceRange);
/*     */   }
/*     */   
/*     */   public void clearDepthTextureUnsynced(MemoryStack stack, GpuTexture depthTexture, double clearDepth) {
/* 349 */     VkClearDepthStencilValue vkClearDepth = VkClearDepthStencilValue.calloc(stack).depth((float)clearDepth);
/* 351 */     VkImageSubresourceRange subresourceRange = VkImageSubresourceRange.calloc(stack);
/* 352 */     subresourceRange.baseMipLevel(0);
/* 353 */     subresourceRange.levelCount(depthTexture.getMipLevels());
/* 354 */     subresourceRange.baseArrayLayer(0);
/* 355 */     subresourceRange.layerCount(1);
/* 356 */     subresourceRange.aspectMask(2);
/* 358 */     VK12.vkCmdClearDepthStencilImage(commandBuffer(), ((VulkanGpuTexture)depthTexture).vkImage(), 1, vkClearDepth, subresourceRange);
/*     */   }
/*     */   
/*     */   public void clearColorTexture(GpuTexture colorTexture, Vector4fc clearColor) {
/* 363 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 364 */       clearColorTextureUnsynced(stack, colorTexture, clearColor);
/* 365 */       memoryBarrier(stack);
/* 366 */       if (stack != null)
/* 366 */         stack.close(); 
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
/*     */   public void clearColorAndDepthTextures(GpuTexture colorTexture, Vector4fc clearColor, GpuTexture depthTexture, double clearDepth) {
/* 371 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 372 */       clearColorTextureUnsynced(stack, colorTexture, clearColor);
/* 373 */       clearDepthTextureUnsynced(stack, depthTexture, clearDepth);
/* 374 */       memoryBarrier(stack);
/* 375 */       if (stack != null)
/* 375 */         stack.close(); 
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
/*     */   public void clearColorAndDepthTextures(GpuTexture colorTexture, Vector4fc clearColor, GpuTexture depthTexture, double clearDepth, int regionX, int regionY, int regionWidth, int regionHeight) {
/* 382 */     GpuTextureView colorTextureView = this.device.createTextureView(colorTexture);
/*     */     try {
/* 383 */       GpuTextureView depthTextureView = this.device.createTextureView(depthTexture);
/*     */       try {
/* 384 */         MemoryStack stack = MemoryStack.stackPush();
/*     */         try {
/* 386 */           createRenderPass(RenderPassDescriptor.create(() -> "ClearColorDepthTextures").withColorAttachment(colorTextureView).withDepthAttachment(depthTextureView).withRenderArea(new RenderPass.RenderArea(0, 0, colorTexture.getWidth(0), colorTexture.getHeight(0))));
/* 387 */           assert this.currentRenderPass != null;
/* 389 */           VkClearRect.Buffer rects = VkClearRect.calloc(1, stack);
/* 390 */           rects.baseArrayLayer(0);
/* 391 */           rects.layerCount(1);
/* 392 */           rects.rect().offset().set(regionX, regionY);
/* 393 */           rects.rect().extent().set(regionWidth, regionHeight);
/* 395 */           VkClearAttachment.Buffer attachments = VkClearAttachment.calloc(2, stack);
/* 396 */           VkClearValue colorClearValue = VkClearValue.calloc(stack);
/* 397 */           VulkanUtils.putArgb(colorClearValue.color(), clearColor);
/* 398 */           attachments.aspectMask(1);
/* 399 */           attachments.clearValue(colorClearValue);
/* 401 */           VkClearValue depthClearValue = VkClearValue.calloc(stack);
/* 402 */           VkClearDepthStencilValue clearValue = depthClearValue.depthStencil();
/* 403 */           clearValue.depth((float)clearDepth);
/* 404 */           attachments.position(1);
/* 405 */           attachments.aspectMask(2);
/* 406 */           attachments.clearValue(depthClearValue);
/* 408 */           attachments.position(0);
/* 409 */           VK12.vkCmdClearAttachments(commandBuffer(), attachments, rects);
/* 410 */           submitRenderPass();
/* 411 */           if (stack != null)
/* 411 */             stack.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (stack != null)
/*     */             try {
/*     */               stack.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 411 */         if (depthTextureView != null)
/* 411 */           depthTextureView.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (depthTextureView != null)
/*     */           try {
/*     */             depthTextureView.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 411 */       if (colorTextureView != null)
/* 411 */         colorTextureView.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (colorTextureView != null)
/*     */         try {
/*     */           colorTextureView.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clearDepthTexture(GpuTexture depthTexture, double clearDepth) {
/* 416 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 417 */       clearDepthTextureUnsynced(stack, depthTexture, clearDepth);
/* 418 */       memoryBarrier(stack);
/* 419 */       if (stack != null)
/* 419 */         stack.close(); 
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
/*     */   public void writeToBuffer(GpuBufferSlice destination, ByteBuffer data) {
/* 424 */     VulkanGpuBuffer destBuffer = (VulkanGpuBuffer)destination.buffer();
/* 425 */     GpuBufferSlice stagingBuffer = this.transientMemory.uploadStaging(data, 1L, 16);
/* 427 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 429 */       VkBufferCopy.Buffer regions = VkBufferCopy.calloc(1, stack).srcOffset(stagingBuffer.offset()).dstOffset(destination.offset()).size(data.remaining());
/* 431 */       VK12.vkCmdCopyBuffer(commandBuffer(), ((VulkanGpuBuffer)stagingBuffer.buffer()).vkBuffer(), destBuffer.vkBuffer(), regions);
/* 432 */       memoryBarrier(stack);
/* 433 */       if (stack != null)
/* 433 */         stack.close(); 
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
/*     */   public void copyToBuffer(GpuBufferSlice source, GpuBufferSlice target) {
/* 438 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 439 */       VkBufferCopy.Buffer copyInfo = VkBufferCopy.calloc(1, stack);
/* 440 */       copyInfo.srcOffset(source.offset());
/* 441 */       copyInfo.dstOffset(target.offset());
/* 442 */       copyInfo.size(source.length());
/* 443 */       VK12.vkCmdCopyBuffer(commandBuffer(), ((VulkanGpuBuffer)source.buffer()).vkBuffer(), ((VulkanGpuBuffer)target.buffer()).vkBuffer(), copyInfo);
/* 444 */       memoryBarrier(stack);
/* 445 */       if (stack != null)
/* 445 */         stack.close(); 
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
/*     */   public void writeToTexture(GpuTexture destination, ByteBuffer source, int mipLevel, int depthOrLayer, int destX, int destY, int width, int height) {
/* 450 */     GpuBufferSlice stagingBuffer = this.transientMemory.uploadStaging(source, 1L, 16);
/* 451 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 452 */       VkBufferImageCopy.Buffer region = VkBufferImageCopy.calloc(1, stack);
/* 453 */       region.bufferOffset(stagingBuffer.offset());
/* 454 */       region.bufferRowLength(width);
/* 455 */       region.bufferImageHeight(height);
/* 456 */       VkImageSubresourceLayers imageSubresource = region.imageSubresource();
/* 457 */       imageSubresource.aspectMask(1);
/* 458 */       imageSubresource.mipLevel(mipLevel);
/* 459 */       imageSubresource.baseArrayLayer(depthOrLayer);
/* 460 */       imageSubresource.layerCount(1);
/* 461 */       region.imageOffset().set(destX, destY, 0);
/* 462 */       region.imageExtent().set(width, height, 1);
/* 464 */       VK12.vkCmdCopyBufferToImage(commandBuffer(), ((VulkanGpuBuffer)stagingBuffer.buffer()).vkBuffer(), ((VulkanGpuTexture)destination).vkImage(), 1, region);
/* 465 */       memoryBarrier(stack);
/* 466 */       if (stack != null)
/* 466 */         stack.close(); 
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
/*     */   public void copyBufferToTexture(GpuBufferSlice source, int sourceX, int sourceY, int sourceWidth, int sourceHeight, GpuTexture destination, int destinationX, int destinationY, int copyWidth, int copyHeight, int mipLevel, int arrayLayer) {
/* 471 */     int texelSize = destination.getFormat().blockSize();
/* 472 */     long skipTexels = sourceX + sourceY * sourceWidth;
/* 473 */     long skipBytes = skipTexels * texelSize;
/* 475 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 477 */       VkBufferImageCopy.Buffer region = VkBufferImageCopy.calloc(1, stack);
/* 478 */       region.bufferOffset(source.offset() + skipBytes);
/* 479 */       region.bufferRowLength(sourceWidth);
/* 480 */       region.bufferImageHeight(sourceHeight);
/* 482 */       VkImageSubresourceLayers imageSubresource = region.imageSubresource();
/* 483 */       imageSubresource.aspectMask(1);
/* 484 */       imageSubresource.mipLevel(mipLevel);
/* 485 */       imageSubresource.baseArrayLayer(arrayLayer);
/* 486 */       imageSubresource.layerCount(1);
/* 488 */       region.imageOffset().set(destinationX, destinationY, 0);
/* 489 */       region.imageExtent().set(copyWidth, copyHeight, 1);
/* 491 */       VK12.vkCmdCopyBufferToImage(commandBuffer(), ((VulkanGpuBuffer)source.buffer()).vkBuffer(), ((VulkanGpuTexture)destination).vkImage(), 1, region);
/* 492 */       memoryBarrier(stack);
/* 493 */       if (stack != null)
/* 493 */         stack.close(); 
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
/*     */   public void copyTextureToBuffer(GpuTexture source, GpuBuffer destination, long offset, Runnable callback, int mipLevel) {
/* 498 */     copyTextureToBuffer(source, destination, offset, callback, mipLevel, 0, 0, source.getWidth(mipLevel), source.getHeight(mipLevel));
/*     */   }
/*     */   
/*     */   public void copyTextureToBuffer(GpuTexture source, GpuBuffer destination, long offset, Runnable callback, int mipLevel, int x, int y, int width, int height) {
/* 504 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 505 */       VkBufferImageCopy.Buffer copy = VkBufferImageCopy.calloc(1, stack);
/* 506 */       copy.bufferOffset(offset);
/* 507 */       VkImageSubresourceLayers subresource = copy.imageSubresource();
/* 508 */       subresource.aspectMask(VulkanConst.formatAspectMask(source.getFormat()));
/* 509 */       subresource.mipLevel(mipLevel);
/* 510 */       subresource.baseArrayLayer(0);
/* 511 */       subresource.layerCount(1);
/* 512 */       copy.imageOffset().set(x, y, 0);
/* 513 */       copy.imageExtent().set(width, height, 1);
/* 514 */       copy.bufferRowLength(width);
/* 515 */       copy.bufferImageHeight(height);
/* 517 */       VK12.vkCmdCopyImageToBuffer(commandBuffer(), ((VulkanGpuTexture)source).vkImage(), 1, ((VulkanGpuBuffer)destination).vkBuffer(), copy);
/* 518 */       memoryBarrier(stack);
/* 519 */       if (stack != null)
/* 519 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 520 */     Objects.requireNonNull(callback);
/* 520 */     queueForDestroy(callback::run);
/*     */   }
/*     */   
/*     */   public void copyTextureToTexture(GpuTexture source, GpuTexture destination, int mipLevel, int destX, int destY, int sourceX, int sourceY, int width, int height) {
/* 525 */     VulkanGpuTexture vulkanSrc = (VulkanGpuTexture)source;
/* 526 */     VulkanGpuTexture vulkanDst = (VulkanGpuTexture)destination;
/* 528 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 529 */       VkImageSubresourceLayers subresourceLayers = VkImageSubresourceLayers.calloc(stack);
/* 530 */       subresourceLayers.mipLevel(mipLevel);
/* 531 */       subresourceLayers.baseArrayLayer(0);
/* 532 */       subresourceLayers.layerCount(1);
/* 533 */       subresourceLayers.aspectMask(VulkanConst.formatAspectMask(source.getFormat()));
/* 534 */       VkImageCopy.Buffer regions = VkImageCopy.calloc(1, stack);
/* 535 */       regions.srcOffset().set(sourceX, sourceY, 0);
/* 536 */       regions.dstOffset().set(destX, destY, 0);
/* 537 */       regions.extent().set(width, height, 1);
/* 538 */       regions.srcSubresource(subresourceLayers);
/* 539 */       regions.dstSubresource(subresourceLayers);
/* 541 */       VK12.vkCmdCopyImage(commandBuffer(), vulkanSrc.vkImage(), 1, vulkanDst.vkImage(), 1, regions);
/* 542 */       memoryBarrier(stack);
/* 543 */       if (stack != null)
/* 543 */         stack.close(); 
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
/*     */   private boolean awaitSubmitCompletion(long submitIndex, long timeoutNS) {
/* 548 */     if (this.completedSubmitIndex >= submitIndex)
/* 549 */       return true; 
/* 551 */     if (submitIndex == this.currentSubmitIndex) {
/* 552 */       if (timeoutNS == 0L)
/* 554 */         return false; 
/* 556 */       throw new IllegalStateException("Cannot wait on a fence for the current submit");
/*     */     } 
/* 558 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 559 */       VkSemaphoreWaitInfo waitInfo = VkSemaphoreWaitInfo.calloc(stack).sType$Default();
/* 560 */       waitInfo.pSemaphores(stack.longs(this.submitSemaphore));
/* 561 */       waitInfo.pValues(stack.longs(submitIndex));
/* 562 */       waitInfo.semaphoreCount(1);
/* 563 */       int result = VK12.vkWaitSemaphores(this.device.vkDevice(), waitInfo, timeoutNS);
/* 564 */       VulkanUtils.crashIfFailure(this.device, result, "Failed to wait for semaphore");
/* 565 */       boolean completed = (result == 0);
/* 566 */       if (completed)
/* 567 */         this.completedSubmitIndex = submitIndex; 
/* 569 */       boolean bool1 = completed;
/* 570 */       if (stack != null)
/* 570 */         stack.close(); 
/*     */       return bool1;
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
/*     */   public GpuFence createFence() {
/* 575 */     return new GpuFence() {
/* 576 */         private final long submitIndex = VulkanCommandEncoder.this.currentSubmitIndex;
/*     */         
/*     */         private boolean completed = false;
/*     */         
/*     */         public boolean awaitCompletion(long timeoutMs) {
/* 581 */           if (!this.completed)
/* 582 */             this.completed = VulkanCommandEncoder.this.awaitSubmitCompletion(this.submitIndex, timeoutMs); 
/* 584 */           return this.completed;
/*     */         }
/*     */         
/*     */         public void close() {
/* 589 */           this.completed = true;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public void writeTimestamp(GpuQueryPool pool, int index) {
/* 596 */     long queryPool = ((VulkanQueryPool)pool).vkQueryPool();
/* 599 */     VK12.vkResetQueryPool(this.device.vkDevice(), queryPool, index, 1);
/* 600 */     KHRSynchronization2.vkCmdWriteTimestamp2KHR(commandBuffer(), 65536L, queryPool, index);
/*     */   }
/*     */   
/*     */   public long getTimestampNow() {
/* 606 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 607 */       VulkanQueryPool queryPool = (VulkanQueryPool)this.device.createTimestampQueryPool(1);
/*     */       try {
/* 609 */         VkCommandBuffer commandBuffer = allocateAndBeginTransientCommandBuffer();
/* 611 */         KHRSynchronization2.vkCmdWriteTimestamp2KHR(commandBuffer, 0L, queryPool.vkQueryPool(), 0);
/* 612 */         VulkanUtils.crashIfFailure(this.device, VK12.vkEndCommandBuffer(commandBuffer), "Failed to end VkCommandBuffer");
/* 614 */         VulkanQueue.Submission submit = this.device.graphicsQueue().beginSubmit();
/*     */         try {
/* 615 */           submit.executeCommands(commandBuffer);
/* 616 */           if (submit != null)
/* 616 */             submit.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (submit != null)
/*     */             try {
/*     */               submit.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 618 */         LongBuffer timestampPtr = stack.callocLong(1);
/* 619 */         VulkanUtils.crashIfFailure(this.device, VK12.vkGetQueryPoolResults(this.device.vkDevice(), queryPool.vkQueryPool(), 0, 1, timestampPtr, 0L, 3), "Cannot fetch current timestamp");
/* 620 */         long l = timestampPtr.get(0);
/* 621 */         if (queryPool != null)
/* 621 */           queryPool.close(); 
/* 621 */         if (stack != null)
/* 621 */           stack.close(); 
/*     */         return l;
/*     */       } catch (Throwable throwable) {
/*     */         if (queryPool != null)
/*     */           try {
/*     */             queryPool.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
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
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanCommandEncoder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */