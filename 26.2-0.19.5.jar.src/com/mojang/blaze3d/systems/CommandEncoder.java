/*     */ package com.mojang.blaze3d.systems;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.GpuFence;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.function.Supplier;
/*     */ import org.joml.Vector4fc;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class CommandEncoder {
/*  22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final GpuDeviceBackend device;
/*     */   
/*     */   private final CommandEncoderBackend backend;
/*     */   
/*     */   private boolean isInRenderPass;
/*     */   
/*     */   private final TracyGpuProfiler profiler;
/*     */   
/*     */   public CommandEncoder(TracyGpuProfiler profiler, GpuDeviceBackend device, CommandEncoderBackend backend) {
/*  30 */     this.profiler = profiler;
/*  31 */     this.device = device;
/*  32 */     this.backend = backend;
/*     */   }
/*     */   
/*     */   protected CommandEncoderBackend backend() {
/*  36 */     return this.backend;
/*     */   }
/*     */   
/*     */   public void submit() {
/*  40 */     this.backend.submit();
/*  41 */     if (this.profiler != null)
/*  42 */       this.profiler.endFrame(); 
/*     */   }
/*     */   
/*     */   public TransientMemory transientMemory() {
/*  47 */     return this.backend.transientMemory();
/*     */   }
/*     */   
/*     */   protected boolean isInRenderPass() {
/*  51 */     return this.isInRenderPass;
/*     */   }
/*     */   
/*     */   public RenderPass createRenderPass(Supplier<String> label, GpuTextureView colorTexture, Optional<Vector4fc> clearColor) {
/*  55 */     return createRenderPass(label, colorTexture, clearColor, null, OptionalDouble.empty());
/*     */   }
/*     */   
/*     */   public RenderPass createRenderPass(Supplier<String> label, GpuTextureView colorTexture, Optional<Vector4fc> clearColor, GpuTextureView depthTexture, OptionalDouble clearDepth) {
/*  59 */     return createRenderPass(label, colorTexture, clearColor, depthTexture, clearDepth, new RenderPass.RenderArea(0, 0, colorTexture.getWidth(0), colorTexture.getHeight(0)));
/*     */   }
/*     */   
/*     */   public RenderPass createRenderPass(Supplier<String> label, GpuTextureView colorTexture, Optional<Vector4fc> clearColor, GpuTextureView depthTexture, OptionalDouble clearDepth, RenderPass.RenderArea renderArea) {
/*  64 */     RenderPassDescriptor descriptor = RenderPassDescriptor.create(label).withColorAttachment(colorTexture, clearColor);
/*  66 */     if (depthTexture != null)
/*  67 */       descriptor.withDepthAttachment(depthTexture, clearDepth); 
/*  70 */     descriptor.withRenderArea(renderArea);
/*  72 */     return createRenderPass(descriptor);
/*     */   }
/*     */   
/*     */   public RenderPass createRenderPass(RenderPassDescriptor descriptor) {
/*     */     int attachmentWidth, attachmentHeight;
/*  76 */     if (this.isInRenderPass)
/*  77 */       throw new IllegalStateException("Close the existing render pass before creating a new one!"); 
/*  80 */     int maxColorAttachments = RenderSystem.getDevice().getDeviceInfo().limits().maxColorAttachments();
/*  81 */     int colorAttachmentCount = descriptor.colorAttachments.size();
/*  82 */     if (colorAttachmentCount > maxColorAttachments)
/*  83 */       throw new IllegalStateException("Render pass created with " + colorAttachmentCount + " color attachments but device only supports " + maxColorAttachments); 
/*  86 */     int totalAttachments = colorAttachmentCount + ((descriptor.depthAttachment != null) ? 1 : 0);
/*  87 */     if (totalAttachments == 0)
/*  88 */       throw new IllegalArgumentException("At least one attachment (depth or color) must be specified"); 
/*  93 */     if (colorAttachmentCount != 0) {
/*  94 */       RenderPassDescriptor.Attachment<Optional<Vector4fc>> firstAttachment = descriptor.colorAttachments.getFirst();
/*  95 */       assert firstAttachment != null;
/*  96 */       attachmentWidth = firstAttachment.textureView().getWidth(0);
/*  97 */       attachmentHeight = firstAttachment.textureView().getHeight(0);
/*     */     } else {
/*  99 */       attachmentWidth = descriptor.depthAttachment.textureView().getWidth(0);
/* 100 */       attachmentHeight = descriptor.depthAttachment.textureView().getHeight(0);
/*     */     } 
/* 103 */     if (descriptor.renderArea == null)
/* 104 */       throw new IllegalArgumentException("RenderPassDescriptor.renderArea must be provided"); 
/* 106 */     if (descriptor.renderArea.x() < 0 || descriptor.renderArea.y() < 0 || descriptor.renderArea.x() + descriptor.renderArea.width() > attachmentWidth || descriptor.renderArea.y() + descriptor.renderArea.height() > attachmentHeight)
/* 107 */       throw new IllegalArgumentException("RenderPass render area " + String.valueOf(descriptor.renderArea) + " is out of bounds for texture of " + attachmentWidth + "x" + attachmentHeight); 
/* 110 */     for (int i = 0; i < colorAttachmentCount; i++) {
/* 111 */       RenderPassDescriptor.Attachment<Optional<Vector4fc>> colorAttachment = descriptor.colorAttachments.get(i);
/* 112 */       if (colorAttachment != null) {
/* 115 */         GpuTextureView colorTexture = colorAttachment.textureView();
/* 116 */         if (colorTexture.isClosed())
/* 117 */           throw new IllegalStateException("Color texture " + i + " is closed"); 
/* 119 */         if ((colorTexture.texture().usage() & 0x8) == 0)
/* 120 */           throw new IllegalStateException("Color texture " + i + " must have USAGE_RENDER_ATTACHMENT"); 
/* 122 */         if (colorTexture.texture().getDepthOrLayers() > 1)
/* 123 */           throw new UnsupportedOperationException("Color texture " + i + ": Textures with multiple depths or layers are not yet supported as an attachment"); 
/* 125 */         if (colorTexture.getWidth(0) != attachmentWidth || colorTexture.getHeight(0) != attachmentHeight)
/* 126 */           throw new IllegalArgumentException("Color texture " + i + ": size does not match expected attachment size. Is " + colorTexture.getWidth(0) + "x" + colorTexture.getHeight(0) + " expected " + attachmentWidth + "x" + attachmentHeight); 
/*     */       } 
/*     */     } 
/* 129 */     if (descriptor.depthAttachment != null) {
/* 130 */       GpuTextureView depthTexture = descriptor.depthAttachment.textureView();
/* 131 */       if (depthTexture.isClosed())
/* 132 */         throw new IllegalStateException("Depth texture is closed"); 
/* 134 */       if ((depthTexture.texture().usage() & 0x8) == 0)
/* 135 */         throw new IllegalStateException("Depth texture must have USAGE_RENDER_ATTACHMENT"); 
/* 137 */       if (depthTexture.texture().getDepthOrLayers() > 1)
/* 138 */         throw new UnsupportedOperationException("Depth texture: Textures with multiple depths or layers are not yet supported as an attachment"); 
/* 140 */       if (depthTexture.getWidth(0) != attachmentWidth || depthTexture.getHeight(0) != attachmentHeight)
/* 141 */         throw new IllegalArgumentException("Depth texture: size does not match expected attachment size. Is " + depthTexture.getWidth(0) + "x" + depthTexture.getHeight(0) + " expected " + attachmentWidth + "x" + attachmentHeight); 
/*     */     } 
/* 145 */     this.isInRenderPass = true;
/* 146 */     if (this.profiler != null)
/* 147 */       this.profiler.pushZone(this, descriptor.label().get()); 
/* 150 */     return new RenderPass(this.backend.createRenderPass(descriptor), this.device, descriptor.colorAttachments, this::submitRenderPass, descriptor.renderArea);
/*     */   }
/*     */   
/*     */   protected void submitRenderPass() {
/* 154 */     if (!this.isInRenderPass)
/* 155 */       throw new IllegalStateException("Can't submit a render pass if one isn't open"); 
/* 157 */     this.isInRenderPass = false;
/* 158 */     this.backend.submitRenderPass();
/* 159 */     if (this.profiler != null)
/* 160 */       this.profiler.popZone(this); 
/*     */   }
/*     */   
/*     */   public void clearColorTexture(GpuTexture colorTexture, Vector4fc clearColor) {
/* 165 */     if (this.isInRenderPass)
/* 166 */       throw new IllegalStateException("Close the existing render pass before creating a new one!"); 
/* 169 */     verifyColorTexture(colorTexture);
/* 171 */     this.backend.clearColorTexture(colorTexture, clearColor);
/*     */   }
/*     */   
/*     */   public void clearColorAndDepthTextures(GpuTexture colorTexture, Vector4fc clearColor, GpuTexture depthTexture, double clearDepth) {
/* 175 */     if (this.isInRenderPass)
/* 176 */       throw new IllegalStateException("Close the existing render pass before creating a new one!"); 
/* 179 */     verifyColorTexture(colorTexture);
/* 180 */     verifyDepthTexture(depthTexture);
/* 182 */     this.backend.clearColorAndDepthTextures(colorTexture, clearColor, depthTexture, clearDepth);
/*     */   }
/*     */   
/*     */   public void clearColorAndDepthTextures(GpuTexture colorTexture, Vector4fc clearColor, GpuTexture depthTexture, double clearDepth, int regionX, int regionY, int regionWidth, int regionHeight) {
/* 186 */     if (this.isInRenderPass)
/* 187 */       throw new IllegalStateException("Close the existing render pass before creating a new one!"); 
/* 190 */     verifyColorTexture(colorTexture);
/* 191 */     verifyDepthTexture(depthTexture);
/* 193 */     verifyRegion(colorTexture, regionX, regionY, regionWidth, regionHeight);
/* 195 */     this.backend.clearColorAndDepthTextures(colorTexture, clearColor, depthTexture, clearDepth, regionX, regionY, regionWidth, regionHeight);
/*     */   }
/*     */   
/*     */   public void clearDepthTexture(GpuTexture depthTexture, double clearDepth) {
/* 199 */     if (this.isInRenderPass)
/* 200 */       throw new IllegalStateException("Close the existing render pass before creating a new one!"); 
/* 203 */     verifyDepthTexture(depthTexture);
/* 205 */     this.backend.clearDepthTexture(depthTexture, clearDepth);
/*     */   }
/*     */   
/*     */   public void writeToBuffer(GpuBufferSlice destination, ByteBuffer data) {
/* 217 */     if (this.isInRenderPass)
/* 218 */       throw new IllegalStateException("Close the existing render pass before performing additional commands"); 
/* 221 */     GpuBuffer buffer = destination.buffer();
/* 222 */     if (buffer.isClosed())
/* 223 */       throw new IllegalStateException("Buffer already closed"); 
/* 225 */     if ((buffer.usage() & 0x8) == 0)
/* 226 */       throw new IllegalStateException("Buffer needs USAGE_COPY_DST to be a destination for a copy"); 
/* 229 */     int length = data.remaining();
/* 230 */     if (length > destination.length())
/* 231 */       throw new IllegalArgumentException("Cannot write more data than the slice allows (attempting to write " + length + " bytes into a slice of length " + destination.length() + ")"); 
/* 233 */     if (destination.length() + destination.offset() > buffer.size())
/* 234 */       throw new IllegalArgumentException("Cannot write more data than this buffer can hold (attempting to write " + length + " bytes at offset " + destination.offset() + " to " + buffer.size() + " size buffer)"); 
/* 237 */     this.backend.writeToBuffer(destination, data);
/*     */   }
/*     */   
/*     */   public void copyToBuffer(GpuBufferSlice source, GpuBufferSlice target) {
/* 249 */     if (this.isInRenderPass)
/* 250 */       throw new IllegalStateException("Close the existing render pass before performing additional commands"); 
/* 253 */     GpuBuffer sourceBuffer = source.buffer();
/* 254 */     if (sourceBuffer.isClosed())
/* 255 */       throw new IllegalStateException("Source buffer already closed"); 
/* 256 */     if ((sourceBuffer.usage() & 0x10) == 0)
/* 257 */       throw new IllegalStateException("Source buffer needs USAGE_COPY_SRC to be a source for a copy"); 
/* 260 */     GpuBuffer targetBuffer = target.buffer();
/* 261 */     if (targetBuffer.isClosed())
/* 262 */       throw new IllegalStateException("Target buffer already closed"); 
/* 263 */     if ((targetBuffer.usage() & 0x8) == 0)
/* 264 */       throw new IllegalStateException("Target buffer needs USAGE_COPY_DST to be a destination for a copy"); 
/* 267 */     if (source.length() != target.length())
/* 268 */       throw new IllegalArgumentException("Cannot copy from slice of size " + source.length() + " to slice of size " + target.length() + ", they must be equal"); 
/* 271 */     if (source.offset() + source.length() > sourceBuffer.size())
/* 272 */       throw new IllegalArgumentException("Cannot copy more data than the source buffer holds (attempting to copy " + source.length() + " bytes at offset " + source.offset() + " from " + sourceBuffer.size() + " size buffer)"); 
/* 275 */     if (target.offset() + target.length() > targetBuffer.size())
/* 276 */       throw new IllegalArgumentException("Cannot copy more data than the target buffer can hold (attempting to copy " + target.length() + " bytes at offset " + target.offset() + " to " + targetBuffer.size() + " size buffer)"); 
/* 279 */     this.backend.copyToBuffer(source, target);
/*     */   }
/*     */   
/*     */   public void writeToTexture(GpuTexture destination, NativeImage source) {
/* 289 */     int width = destination.getWidth(0);
/* 290 */     int height = destination.getHeight(0);
/* 291 */     if (source.getWidth() != width || source.getHeight() != height)
/* 292 */       throw new IllegalArgumentException("Cannot replace texture of size " + width + "x" + height + " with image of size " + source.getWidth() + "x" + source.getHeight()); 
/* 295 */     writeToTexture(destination, source, 0, 0, 0, 0);
/*     */   }
/*     */   
/*     */   public void writeToTexture(GpuTexture destination, NativeImage source, int mipLevel, int depthOrLayer, int destX, int destY) {
/* 299 */     if (this.isInRenderPass)
/* 300 */       throw new IllegalStateException("Close the existing render pass before performing additional commands"); 
/* 303 */     if (destination.getFormat().componentType() != GpuFormat.ComponentType.UNORM_8)
/* 304 */       throw new IllegalArgumentException("Destination texture for NativeImage writes must have component type of UNORM_8"); 
/* 306 */     if (destination.getFormat().componentCount() != source.format().components())
/* 307 */       throw new IllegalArgumentException("Destination(" + String.valueOf(destination.getFormat()) + ") texture for NativeImage(" + String.valueOf(source.format()) + ") write must have channel count matching source"); 
/* 309 */     if (mipLevel < 0 || mipLevel >= destination.getMipLevels())
/* 310 */       throw new IllegalArgumentException("Invalid mipLevel " + mipLevel + ", must be >= 0 and < " + destination.getMipLevels()); 
/* 312 */     if (destX + source.getWidth() > destination.getWidth(mipLevel) || destY + source.getHeight() > destination.getHeight(mipLevel))
/* 313 */       throw new IllegalArgumentException("Dest texture (" + source.getWidth() + "x" + source.getHeight() + ") is not large enough to write a rectangle of " + source.getWidth() + "x" + source.getHeight() + " at " + destX + "x" + destY + " (at mip level " + mipLevel + ")"); 
/* 315 */     if (destination.isClosed())
/* 316 */       throw new IllegalStateException("Destination texture is closed"); 
/* 318 */     if ((destination.usage() & 0x1) == 0)
/* 319 */       throw new IllegalStateException("Color texture must have USAGE_COPY_DST to be a destination for a write"); 
/* 321 */     if (depthOrLayer >= destination.getDepthOrLayers())
/* 322 */       throw new UnsupportedOperationException("Depth or layer is out of range, must be >= 0 and < " + destination.getDepthOrLayers()); 
/* 325 */     writeToTexture(destination, source.getPixelBytes(), mipLevel, depthOrLayer, destX, destY, source.getWidth(), source.getHeight());
/*     */   }
/*     */   
/*     */   public void writeToTexture(GpuTexture destination, ByteBuffer source, int mipLevel, int depthOrLayer, int destX, int destY, int width, int height) {
/* 344 */     if (this.isInRenderPass)
/* 345 */       throw new IllegalStateException("Close the existing render pass before performing additional commands"); 
/* 348 */     if (mipLevel < 0 || mipLevel >= destination.getMipLevels())
/* 349 */       throw new IllegalArgumentException("Invalid mipLevel, must be >= 0 and < " + destination.getMipLevels()); 
/* 351 */     if (width * height * destination.getFormat().blockSize() > source.remaining())
/* 352 */       throw new IllegalArgumentException("Copy would overrun the source buffer (remaining length of " + source.remaining() + ", but copy is " + width + "x" + height + " of format " + String.valueOf(destination.getFormat()) + ")"); 
/* 354 */     if (destX + width > destination.getWidth(mipLevel) || destY + height > destination.getHeight(mipLevel))
/* 355 */       throw new IllegalArgumentException("Dest texture (" + destination.getWidth(mipLevel) + "x" + destination.getHeight(mipLevel) + ") is not large enough to write a rectangle of " + width + "x" + height + " at " + destX + "x" + destY); 
/* 357 */     if (destination.isClosed())
/* 358 */       throw new IllegalStateException("Destination texture is closed"); 
/* 360 */     if ((destination.usage() & 0x1) == 0)
/* 361 */       throw new IllegalStateException("Color texture must have USAGE_COPY_DST to be a destination for a write"); 
/* 363 */     if (depthOrLayer >= destination.getDepthOrLayers())
/* 364 */       throw new UnsupportedOperationException("Depth or layer is out of range, must be >= 0 and < " + destination.getDepthOrLayers()); 
/* 367 */     this.backend.writeToTexture(destination, source, mipLevel, depthOrLayer, destX, destY, width, height);
/*     */   }
/*     */   
/*     */   public void copyBufferToTexture(GpuBufferSlice source, int sourceX, int sourceY, int sourceWidth, int sourceHeight, GpuTexture destination, int destinationX, int destinationY, int copyWidth, int copyHeight, int mipLevel, int arrayLayer) {
/* 371 */     if (this.isInRenderPass)
/* 372 */       throw new IllegalStateException("Close the existing render pass before performing additional commands"); 
/* 375 */     if (mipLevel < 0 || mipLevel >= destination.getMipLevels())
/* 376 */       throw new IllegalArgumentException("Invalid mipLevel, must be >= 0 and < " + destination.getMipLevels()); 
/* 378 */     if (sourceX + copyWidth > sourceWidth || sourceY + copyHeight > sourceHeight)
/* 379 */       throw new IllegalArgumentException("Copy source (" + sourceWidth + "x" + sourceHeight + ") is not large enough to read a rectangle of " + copyWidth + "x" + copyHeight + " from " + sourceX + "x" + sourceY); 
/* 383 */     if (sourceWidth * copyHeight * destination.getFormat().blockSize() > source.length())
/* 384 */       throw new IllegalArgumentException("Copy would overrun the source buffer (remaining length of " + source.length() + ", but copy is " + copyWidth + "x" + copyHeight + " of format " + String.valueOf(destination.getFormat()) + ")"); 
/* 386 */     if (destinationX + copyWidth > destination.getWidth(mipLevel) || destinationY + copyHeight > destination.getHeight(mipLevel))
/* 387 */       throw new IllegalArgumentException("Dest texture (" + destination.getWidth(mipLevel) + "x" + destination.getHeight(mipLevel) + ") is not large enough to write a rectangle of " + copyWidth + "x" + copyHeight + " at " + destinationX + "x" + destinationY); 
/* 389 */     if (source.buffer().isClosed())
/* 390 */       throw new IllegalStateException("Source buffer is closed"); 
/* 392 */     if ((source.buffer().usage() & 0x10) == 0)
/* 393 */       throw new IllegalStateException("Source buffer must have USAGE_COPY_SRC to be a source for a read"); 
/* 395 */     if (destination.isClosed())
/* 396 */       throw new IllegalStateException("Destination texture is closed"); 
/* 398 */     if ((destination.usage() & 0x1) == 0)
/* 399 */       throw new IllegalStateException("Color texture must have USAGE_COPY_DST to be a destination for a write"); 
/* 401 */     if (arrayLayer >= destination.getDepthOrLayers())
/* 402 */       throw new UnsupportedOperationException("Depth or layer is out of range, must be >= 0 and < " + destination.getDepthOrLayers()); 
/* 405 */     this.backend.copyBufferToTexture(source, sourceX, sourceY, sourceWidth, sourceHeight, destination, destinationX, destinationY, copyWidth, copyHeight, mipLevel, arrayLayer);
/*     */   }
/*     */   
/*     */   public void copyTextureToBuffer(GpuTexture source, GpuBuffer destination, long offset, Runnable callback, int mipLevel) {
/* 421 */     if (this.isInRenderPass)
/* 422 */       throw new IllegalStateException("Close the existing render pass before performing additional commands"); 
/* 425 */     this.backend.copyTextureToBuffer(source, destination, offset, callback, mipLevel);
/*     */   }
/*     */   
/*     */   public void copyTextureToBuffer(GpuTexture source, GpuBuffer destination, long offset, Runnable callback, int mipLevel, int x, int y, int width, int height) {
/* 445 */     if (this.isInRenderPass)
/* 446 */       throw new IllegalStateException("Close the existing render pass before performing additional commands"); 
/* 449 */     if (mipLevel < 0 || mipLevel >= source.getMipLevels())
/* 450 */       throw new IllegalArgumentException("Invalid mipLevel " + mipLevel + ", must be >= 0 and < " + source.getMipLevels()); 
/* 452 */     if (width * height * source.getFormat().blockSize() + offset > destination.size())
/* 453 */       throw new IllegalArgumentException("Buffer of size " + destination.size() + " is not large enough to hold " + width + "x" + height + " pixels (" + source.getFormat().blockSize() + " bytes each) starting from offset " + offset); 
/* 455 */     if ((source.usage() & 0x2) == 0)
/* 456 */       throw new IllegalArgumentException("Texture needs USAGE_COPY_SRC to be a source for a copy"); 
/* 458 */     if ((destination.usage() & 0x8) == 0)
/* 459 */       throw new IllegalArgumentException("Buffer needs USAGE_COPY_DST to be a destination for a copy"); 
/* 461 */     if (x + width > source.getWidth(mipLevel) || y + height > source.getHeight(mipLevel))
/* 462 */       throw new IllegalArgumentException("Copy source texture (" + source.getWidth(mipLevel) + "x" + source.getHeight(mipLevel) + ") is not large enough to read a rectangle of " + width + "x" + height + " from " + x + "," + y); 
/* 464 */     if (source.isClosed())
/* 465 */       throw new IllegalStateException("Source texture is closed"); 
/* 467 */     if (destination.isClosed())
/* 468 */       throw new IllegalStateException("Destination buffer is closed"); 
/* 470 */     if (source.getDepthOrLayers() > 1)
/* 471 */       throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported for copying"); 
/* 474 */     this.backend.copyTextureToBuffer(source, destination, offset, callback, mipLevel, x, y, width, height);
/*     */   }
/*     */   
/*     */   public void copyTextureToTexture(GpuTexture source, GpuTexture destination, int mipLevel, int destX, int destY, int sourceX, int sourceY, int width, int height) {
/* 489 */     if (this.isInRenderPass)
/* 490 */       throw new IllegalStateException("Close the existing render pass before performing additional commands"); 
/* 493 */     if (mipLevel < 0 || mipLevel >= source.getMipLevels() || mipLevel >= destination.getMipLevels())
/* 494 */       throw new IllegalArgumentException("Invalid mipLevel " + mipLevel + ", must be >= 0 and < " + source.getMipLevels() + " and < " + destination.getMipLevels()); 
/* 496 */     if (destX + width > destination.getWidth(mipLevel) || destY + height > destination.getHeight(mipLevel))
/* 497 */       throw new IllegalArgumentException("Dest texture (" + destination.getWidth(mipLevel) + "x" + destination.getHeight(mipLevel) + ") is not large enough to write a rectangle of " + width + "x" + height + " at " + destX + "x" + destY); 
/* 499 */     if (sourceX + width > source.getWidth(mipLevel) || sourceY + height > source.getHeight(mipLevel))
/* 500 */       throw new IllegalArgumentException("Source texture (" + source.getWidth(mipLevel) + "x" + source.getHeight(mipLevel) + ") is not large enough to read a rectangle of " + width + "x" + height + " at " + sourceX + "x" + sourceY); 
/* 502 */     if (source.isClosed())
/* 503 */       throw new IllegalStateException("Source texture is closed"); 
/* 505 */     if (destination.isClosed())
/* 506 */       throw new IllegalStateException("Destination texture is closed"); 
/* 508 */     if ((source.usage() & 0x2) == 0)
/* 509 */       throw new IllegalArgumentException("Texture needs USAGE_COPY_SRC to be a source for a copy"); 
/* 511 */     if ((destination.usage() & 0x1) == 0)
/* 512 */       throw new IllegalArgumentException("Texture needs USAGE_COPY_DST to be a destination for a copy"); 
/* 514 */     if (source.getDepthOrLayers() > 1)
/* 515 */       throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported for copying"); 
/* 517 */     if (destination.getDepthOrLayers() > 1)
/* 518 */       throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported for copying"); 
/* 521 */     this.backend.copyTextureToTexture(source, destination, mipLevel, destX, destY, sourceX, sourceY, width, height);
/*     */   }
/*     */   
/*     */   public GpuFence createFence() {
/* 529 */     if (this.isInRenderPass)
/* 530 */       throw new IllegalStateException("Close the existing render pass before performing additional commands"); 
/* 533 */     return this.backend.createFence();
/*     */   }
/*     */   
/*     */   public void writeTimestamp(GpuQueryPool pool, int index) {
/* 537 */     if (index < 0 || index > pool.size())
/* 538 */       throw new IllegalStateException("Index " + index + " is out of range for query pool of size " + pool.size()); 
/* 540 */     this.backend.writeTimestamp(pool, index);
/*     */   }
/*     */   
/*     */   private void verifyColorTexture(GpuTexture colorTexture) {
/* 544 */     if (!colorTexture.getFormat().hasColorAspect())
/* 545 */       throw new IllegalStateException("Trying to clear a non-color texture as color"); 
/* 547 */     if (colorTexture.isClosed())
/* 548 */       throw new IllegalStateException("Color texture is closed"); 
/* 550 */     if ((colorTexture.usage() & 0x8) == 0)
/* 551 */       throw new IllegalStateException("Color texture must have USAGE_RENDER_ATTACHMENT"); 
/* 553 */     if ((colorTexture.usage() & 0x1) == 0)
/* 554 */       throw new IllegalStateException("Color texture must have USAGE_COPY_DST"); 
/* 556 */     if (colorTexture.getDepthOrLayers() > 1)
/* 557 */       throw new UnsupportedOperationException("Clearing a texture with multiple layers or depths is not yet supported"); 
/*     */   }
/*     */   
/*     */   private void verifyDepthTexture(GpuTexture depthTexture) {
/* 562 */     if (!depthTexture.getFormat().hasDepthAspect())
/* 563 */       throw new IllegalStateException("Trying to clear a non-depth texture as depth"); 
/* 565 */     if (depthTexture.isClosed())
/* 566 */       throw new IllegalStateException("Depth texture is closed"); 
/* 568 */     if ((depthTexture.usage() & 0x8) == 0)
/* 569 */       throw new IllegalStateException("Depth texture must have USAGE_RENDER_ATTACHMENT"); 
/* 571 */     if ((depthTexture.usage() & 0x1) == 0)
/* 572 */       throw new IllegalStateException("Depth texture must have USAGE_COPY_DST"); 
/* 574 */     if (depthTexture.getDepthOrLayers() > 1)
/* 575 */       throw new UnsupportedOperationException("Clearing a texture with multiple layers or depths is not yet supported"); 
/*     */   }
/*     */   
/*     */   private void verifyRegion(GpuTexture colorTexture, int regionX, int regionY, int regionWidth, int regionHeight) {
/* 580 */     if (regionX < 0 || regionX >= colorTexture.getWidth(0))
/* 581 */       throw new IllegalArgumentException("regionX should not be outside of the texture"); 
/* 583 */     if (regionY < 0 || regionY >= colorTexture.getHeight(0))
/* 584 */       throw new IllegalArgumentException("regionY should not be outside of the texture"); 
/* 586 */     if (regionWidth <= 0)
/* 587 */       throw new IllegalArgumentException("regionWidth should be greater than 0"); 
/* 589 */     if (regionX + regionWidth > colorTexture.getWidth(0))
/* 590 */       throw new IllegalArgumentException("regionWidth + regionX should be less than the texture width"); 
/* 592 */     if (regionHeight <= 0)
/* 593 */       throw new IllegalArgumentException("regionHeight should be greater than 0"); 
/* 595 */     if (regionY + regionHeight > colorTexture.getHeight(0))
/* 596 */       throw new IllegalArgumentException("regionWidth + regionX should be less than the texture height"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\CommandEncoder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */