/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.GpuFence;
/*     */ import com.mojang.blaze3d.pipeline.BlendFunction;
/*     */ import com.mojang.blaze3d.pipeline.ColorTargetState;
/*     */ import com.mojang.blaze3d.pipeline.DepthStencilState;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.systems.CommandEncoderBackend;
/*     */ import com.mojang.blaze3d.systems.GpuQueryPool;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderPassBackend;
/*     */ import com.mojang.blaze3d.systems.RenderPassDescriptor;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.systems.ScissorState;
/*     */ import com.mojang.blaze3d.systems.TransientMemory;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.function.BiConsumer;
/*     */ import org.joml.Vector4fc;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.opengl.ARBBaseInstance;
/*     */ import org.lwjgl.opengl.ARBDrawIndirect;
/*     */ import org.lwjgl.opengl.ARBMultiDrawIndirect;
/*     */ import org.lwjgl.opengl.GL33C;
/*     */ import org.lwjgl.system.CustomBuffer;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ class GlCommandEncoder implements CommandEncoderBackend, AutoCloseable {
/*  49 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int MAX_SUBMITS_IN_FLIGHT = 2;
/*     */   
/*     */   private static final long NO_FENCE = 0L;
/*     */   
/*     */   private final GlDevice device;
/*     */   
/*     */   private final GlTransientMemory transientMemory;
/*     */   
/*  57 */   private final long[] fences = new long[2];
/*     */   
/*  58 */   private long currentSubmitIndex = 2L;
/*     */   
/*     */   private final int readFbo;
/*     */   
/*     */   private final int drawFbo;
/*     */   
/*     */   private RenderPipeline lastPipeline;
/*     */   
/*     */   private GlProgram lastProgram;
/*     */   
/*     */   private VertexArrayCache.VertexArray lastVertexArray;
/*     */   
/*  67 */   private final List<FrameBufferAttachment> renderPassColorTextures = new ArrayList<>();
/*     */   
/*     */   protected GlCommandEncoder(GlDevice device) {
/*  70 */     this.device = device;
/*  71 */     this.transientMemory = device.getDeviceInfo().features().persistentMapping() ? new GlTransientMemory.PersistentMapping(device, this) : new GlTransientMemory.Fallback(device, this);
/*  72 */     this.readFbo = device.directStateAccess().createFrameBufferObject();
/*  73 */     this.drawFbo = device.directStateAccess().createFrameBufferObject();
/*     */   }
/*     */   
/*     */   public void close() {
/*  78 */     this.transientMemory.close();
/*     */   }
/*     */   
/*     */   public long currentSubmitIndex() {
/*  82 */     return this.currentSubmitIndex;
/*     */   }
/*     */   
/*     */   public int currentSubmitSlot() {
/*  86 */     return (int)(this.currentSubmitIndex % 2L);
/*     */   }
/*     */   
/*     */   public void submit() {
/*  91 */     this.fences[currentSubmitSlot()] = GL33C.glFenceSync(37143, 0);
/*  92 */     this.currentSubmitIndex++;
/*  93 */     if (!awaitSubmit(this.currentSubmitIndex - 2L, Long.MAX_VALUE))
/*  94 */       throw new IllegalStateException("Failed to wait for frame completion"); 
/*  96 */     this.transientMemory.rotate();
/*     */   }
/*     */   
/*     */   public boolean awaitSubmit(long index, long timeoutNS) {
/* 100 */     if (this.currentSubmitIndex > index + 2L)
/* 101 */       return true; 
/* 103 */     if (index == this.currentSubmitIndex) {
/* 104 */       if (timeoutNS == 0L)
/* 106 */         return false; 
/* 108 */       throw new IllegalStateException("Cannot wait on a fence for the current submit");
/*     */     } 
/* 110 */     int submitSlot = (int)(index % 2L);
/* 111 */     long fence = this.fences[submitSlot];
/* 112 */     if (fence == 0L)
/* 114 */       return true; 
/* 116 */     int result = GlStateManager._glClientWaitSync(fence, 1, timeoutNS);
/* 117 */     if (result == 37147)
/* 119 */       return false; 
/* 121 */     if (result == 37149)
/* 123 */       throw new IllegalStateException("Failed to complete GPU fence: " + GlStateManager._getError()); 
/* 128 */     GL33C.glDeleteSync(this.fences[submitSlot]);
/* 129 */     this.fences[submitSlot] = 0L;
/* 131 */     return true;
/*     */   }
/*     */   
/*     */   public TransientMemory transientMemory() {
/* 136 */     return this.transientMemory;
/*     */   }
/*     */   
/*     */   public RenderPassBackend createRenderPass(RenderPassDescriptor descriptor) {
/* 141 */     this.device.debugLabels().pushDebugGroup(descriptor.label());
/* 142 */     List<RenderPassDescriptor.Attachment<Optional<Vector4fc>>> colorAttachments = descriptor.colorAttachments();
/* 143 */     this.renderPassColorTextures.clear();
/* 144 */     for (RenderPassDescriptor.Attachment<Optional<Vector4fc>> colorAttachment : colorAttachments)
/* 145 */       this.renderPassColorTextures.add((colorAttachment != null) ? (GlTextureView)colorAttachment.textureView() : null); 
/* 147 */     RenderPassDescriptor.Attachment<OptionalDouble> depthAttachment = descriptor.depthAttachment();
/* 148 */     int fbo = this.device.frameBufferCache().getFbo(this.device.directStateAccess(), this.renderPassColorTextures, (depthAttachment == null) ? null : (GlTextureView)depthAttachment.textureView());
/* 149 */     GlStateManager._glBindFramebuffer(36160, fbo);
/* 150 */     assert descriptor.renderArea != null;
/* 151 */     GlStateManager._enableScissorTest();
/* 152 */     GlStateManager._scissorBox(descriptor.renderArea.x(), descriptor.renderArea.y(), descriptor.renderArea.width(), descriptor.renderArea.height());
/* 153 */     for (int i = 0; i < colorAttachments.size(); i++) {
/* 154 */       RenderPassDescriptor.Attachment<Optional<Vector4fc>> attachment = colorAttachments.get(i);
/* 155 */       if (attachment != null) {
/* 158 */         Optional<Vector4fc> clearValue = (Optional<Vector4fc>)attachment.clearValue();
/* 159 */         if (clearValue.isPresent()) {
/* 160 */           GlStateManager._colorMask(i, 15);
/* 161 */           GlStateManager._clearBuffer(i, clearValue.get());
/*     */         } 
/*     */       } 
/*     */     } 
/* 164 */     if (depthAttachment != null) {
/* 165 */       OptionalDouble clearValue = (OptionalDouble)depthAttachment.clearValue();
/* 166 */       if (clearValue.isPresent()) {
/* 167 */         GlStateManager._depthMask(true);
/* 168 */         GlStateManager._clearBuffer(clearValue.getAsDouble());
/*     */       } 
/*     */     } 
/* 172 */     int width = 0;
/* 173 */     int height = 0;
/* 174 */     if (!colorAttachments.isEmpty()) {
/* 175 */       for (RenderPassDescriptor.Attachment<Optional<Vector4fc>> colorAttachment : colorAttachments) {
/* 176 */         if (colorAttachment == null)
/*     */           continue; 
/* 179 */         GpuTextureView colorTexture = colorAttachment.textureView();
/* 180 */         width = colorTexture.getWidth(0);
/* 181 */         height = colorTexture.getHeight(0);
/*     */       } 
/* 183 */     } else if (depthAttachment != null) {
/* 184 */       width = depthAttachment.textureView().getWidth(0);
/* 185 */       height = depthAttachment.textureView().getHeight(0);
/*     */     } 
/* 187 */     GlStateManager._viewport(0, 0, width, height);
/* 188 */     this.lastPipeline = null;
/* 189 */     ScissorState scissorState = new ScissorState();
/* 190 */     scissorState.enable(descriptor.renderArea.x(), descriptor.renderArea.y(), descriptor.renderArea.width(), descriptor.renderArea.height());
/* 191 */     return new GlRenderPass(this, this.device, (depthAttachment != null), this.renderPassColorTextures.size(), scissorState);
/*     */   }
/*     */   
/*     */   public void clearColorTexture(GpuTexture colorTexture, Vector4fc clearColor) {
/* 196 */     this.device.directStateAccess().bindFrameBufferTextures(this.drawFbo, ((GlTexture)colorTexture).id, 0, 0, 36160);
/* 197 */     GL33C.glClearColor(clearColor.x(), clearColor.y(), clearColor.z(), clearColor.w());
/* 198 */     GlStateManager._disableScissorTest();
/* 199 */     GlStateManager._colorMask(15);
/* 200 */     GlStateManager._clear(16384);
/* 201 */     GlStateManager._glFramebufferTexture2D(36160, 36064, 3553, 0, 0);
/* 202 */     GlStateManager._glBindFramebuffer(36160, 0);
/*     */   }
/*     */   
/*     */   public void clearColorAndDepthTextures(GpuTexture colorTexture, Vector4fc clearColor, GpuTexture depthTexture, double clearDepth) {
/* 207 */     int fbo = this.device.frameBufferCache().getFbo(this.device.directStateAccess(), Collections.singletonList((GlTexture)colorTexture), (GlTexture)depthTexture);
/* 208 */     GlStateManager._glBindFramebuffer(36160, fbo);
/* 209 */     GlStateManager._disableScissorTest();
/* 210 */     GL33C.glClearDepth(clearDepth);
/* 211 */     GL33C.glClearColor(clearColor.x(), clearColor.y(), clearColor.z(), clearColor.w());
/* 212 */     GlStateManager._depthMask(true);
/* 213 */     GlStateManager._colorMask(15);
/* 214 */     GlStateManager._clear(16640);
/* 215 */     GlStateManager._glBindFramebuffer(36160, 0);
/*     */   }
/*     */   
/*     */   public void clearColorAndDepthTextures(GpuTexture colorTexture, Vector4fc clearColor, GpuTexture depthTexture, double clearDepth, int regionX, int regionY, int regionWidth, int regionHeight) {
/* 220 */     int fbo = this.device.frameBufferCache().getFbo(this.device.directStateAccess(), Collections.singletonList((GlTexture)colorTexture), (GlTexture)depthTexture);
/* 221 */     GlStateManager._glBindFramebuffer(36160, fbo);
/* 222 */     GlStateManager._scissorBox(regionX, regionY, regionWidth, regionHeight);
/* 223 */     GlStateManager._enableScissorTest();
/* 224 */     GL33C.glClearDepth(clearDepth);
/* 225 */     GL33C.glClearColor(clearColor.x(), clearColor.y(), clearColor.z(), clearColor.w());
/* 226 */     GlStateManager._depthMask(true);
/* 227 */     GlStateManager._colorMask(15);
/* 228 */     GlStateManager._clear(16640);
/* 229 */     GlStateManager._glBindFramebuffer(36160, 0);
/*     */   }
/*     */   
/*     */   public void clearDepthTexture(GpuTexture depthTexture, double clearDepth) {
/* 234 */     this.device.directStateAccess().bindFrameBufferTextures(this.drawFbo, 0, ((GlTexture)depthTexture).id, 0, 36160);
/* 235 */     GL33C.glDrawBuffer(0);
/* 236 */     GL33C.glClearDepth(clearDepth);
/* 237 */     GlStateManager._depthMask(true);
/* 238 */     GlStateManager._disableScissorTest();
/* 239 */     GlStateManager._clear(256);
/* 240 */     GL33C.glDrawBuffer(36064);
/* 241 */     GlStateManager._glFramebufferTexture2D(36160, 36096, 3553, 0, 0);
/* 242 */     GlStateManager._glBindFramebuffer(36160, 0);
/*     */   }
/*     */   
/*     */   public void writeToBuffer(GpuBufferSlice slice, ByteBuffer data) {
/* 247 */     GlBuffer buffer = (GlBuffer)slice.buffer();
/* 248 */     buffer.checkCanBeUsed();
/* 249 */     this.device.directStateAccess().bufferSubData(buffer.handle(), slice.offset(), data, buffer.usage());
/*     */   }
/*     */   
/*     */   public void copyToBuffer(GpuBufferSlice source, GpuBufferSlice target) {
/* 254 */     GlBuffer sourceBuffer = (GlBuffer)source.buffer();
/* 255 */     GlBuffer targetBuffer = (GlBuffer)target.buffer();
/* 256 */     sourceBuffer.checkCanBeUsed();
/* 257 */     targetBuffer.checkCanBeUsed();
/* 259 */     this.device.directStateAccess().copyBufferSubData(sourceBuffer.handle(), targetBuffer.handle(), source.offset(), target.offset(), source.length());
/*     */   }
/*     */   
/*     */   public void writeToTexture(GpuTexture destination, ByteBuffer source, int mipLevel, int depthOrLayer, int destX, int destY, int width, int height) {
/*     */     int target;
/* 265 */     if ((destination.usage() & 0x10) != 0) {
/* 266 */       target = GlConst.CUBEMAP_TARGETS[depthOrLayer % 6];
/* 267 */       GL33C.glBindTexture(34067, ((GlTexture)destination).id);
/*     */     } else {
/* 269 */       target = 3553;
/* 270 */       GlStateManager._bindTexture(((GlTexture)destination).id);
/*     */     } 
/* 272 */     GlStateManager._pixelStore(3314, width);
/* 273 */     GlStateManager._pixelStore(3316, 0);
/* 274 */     GlStateManager._pixelStore(3315, 0);
/* 275 */     GlStateManager._pixelStore(3317, destination.getFormat().componentCount());
/* 276 */     GlStateManager._texSubImage2D(target, mipLevel, destX, destY, width, height, GlConst.toGlExternalId(destination.getFormat()), 5121, source);
/*     */   }
/*     */   
/*     */   public void copyBufferToTexture(GpuBufferSlice source, int sourceX, int sourceY, int sourceWidth, int sourceHeight, GpuTexture destination, int destinationX, int destinationY, int copyWidth, int copyHeight, int mipLevel, int arrayLayer) {
/*     */     int target;
/* 283 */     if ((destination.usage() & 0x10) != 0) {
/* 284 */       target = GlConst.CUBEMAP_TARGETS[arrayLayer % 6];
/* 285 */       GL33C.glBindTexture(34067, ((GlTexture)destination).id);
/*     */     } else {
/* 287 */       target = 3553;
/* 288 */       GlStateManager._bindTexture(((GlTexture)destination).id);
/*     */     } 
/* 291 */     int texelSize = destination.getFormat().blockSize();
/* 292 */     long skipTexels = sourceX + sourceY * sourceWidth;
/* 293 */     long skipBytes = skipTexels * texelSize;
/* 295 */     GlBuffer sourceGlBuffer = (GlBuffer)source.buffer();
/* 296 */     GlStateManager._glBindBuffer(35052, sourceGlBuffer.handle());
/* 298 */     GlStateManager._pixelStore(3314, sourceWidth);
/* 299 */     GlStateManager._pixelStore(32878, sourceHeight);
/* 300 */     GlStateManager._pixelStore(3316, 0);
/* 301 */     GlStateManager._pixelStore(3315, 0);
/* 302 */     GlStateManager._pixelStore(3317, destination.getFormat().byteAlignment());
/* 303 */     GlStateManager._texSubImage2D(target, mipLevel, destinationX, destinationY, copyWidth, copyHeight, GlConst.toGlExternalId(destination.getFormat()), GlConst.toGlType(destination.getFormat()), source.offset() + skipBytes);
/* 304 */     GlStateManager._glBindBuffer(35052, 0);
/*     */   }
/*     */   
/*     */   public void copyTextureToBuffer(GpuTexture source, GpuBuffer destination, long offset, Runnable callback, int mipLevel) {
/* 309 */     copyTextureToBuffer(source, destination, offset, callback, mipLevel, 0, 0, source.getWidth(mipLevel), source.getHeight(mipLevel));
/*     */   }
/*     */   
/*     */   public void copyTextureToBuffer(GpuTexture source, GpuBuffer destination, long offset, Runnable callback, int mipLevel, int x, int y, int width, int height) {
/* 314 */     ((GlBuffer)destination).checkCanBeUsed();
/* 316 */     GlStateManager.clearGlErrors();
/* 317 */     this.device.directStateAccess().bindFrameBufferTextures(this.readFbo, ((GlTexture)source).glId(), 0, mipLevel, 36008);
/* 318 */     GlStateManager._glBindBuffer(35051, ((GlBuffer)destination).handle());
/* 319 */     GlStateManager._pixelStore(3330, width);
/* 320 */     GlStateManager._readPixels(x, y, width, height, GlConst.toGlExternalId(source.getFormat()), GlConst.toGlType(source.getFormat()), offset);
/* 321 */     RenderSystem.queueFencedTask(callback);
/* 322 */     GlStateManager._glFramebufferTexture2D(36008, 36064, 3553, 0, mipLevel);
/* 323 */     GlStateManager._glBindFramebuffer(36008, 0);
/* 324 */     GlStateManager._glBindBuffer(35051, 0);
/* 326 */     int error = GlStateManager._getError();
/* 327 */     if (error != 0)
/* 328 */       throw new IllegalStateException("Couldn't perform copyTobuffer for texture " + source.getLabel() + ": GL error " + error); 
/*     */   }
/*     */   
/*     */   public void copyTextureToTexture(GpuTexture source, GpuTexture destination, int mipLevel, int destX, int destY, int sourceX, int sourceY, int width, int height) {
/* 335 */     GlStateManager.clearGlErrors();
/* 336 */     GlStateManager._disableScissorTest();
/* 337 */     boolean isDepth = source.getFormat().hasDepthAspect();
/* 338 */     int sourceId = ((GlTexture)source).glId();
/* 339 */     int destId = ((GlTexture)destination).glId();
/* 340 */     this.device.directStateAccess().bindFrameBufferTextures(this.readFbo, isDepth ? 0 : sourceId, isDepth ? sourceId : 0, 0, 0);
/* 341 */     this.device.directStateAccess().bindFrameBufferTextures(this.drawFbo, isDepth ? 0 : destId, isDepth ? destId : 0, 0, 0);
/* 342 */     this.device.directStateAccess().blitFrameBuffers(this.readFbo, this.drawFbo, sourceX, sourceY, width, height, destX, destY, width, height, isDepth ? 256 : 16384, 9728);
/* 344 */     int error = GlStateManager._getError();
/* 345 */     if (error != 0)
/* 346 */       throw new IllegalStateException("Couldn't perform copyToTexture for texture " + source.getLabel() + " to " + destination.getLabel() + ": GL error " + error); 
/*     */   }
/*     */   
/*     */   public void presentTexture(GpuTextureView textureView, int swapchainWidth, int swapchainHeight) {
/* 351 */     int destY = Math.max(0, swapchainHeight - textureView.getHeight(0));
/* 352 */     int copyWidth = Math.min(swapchainWidth, textureView.getWidth(0));
/* 353 */     int copyHeight = Math.min(swapchainHeight, textureView.getHeight(0));
/* 354 */     GlStateManager._disableScissorTest();
/* 355 */     GlStateManager._viewport(0, 0, textureView.getWidth(0), textureView.getHeight(0));
/* 356 */     GlStateManager._depthMask(true);
/* 357 */     GlStateManager._colorMask(15);
/* 358 */     this.device.directStateAccess().bindFrameBufferTextures(this.drawFbo, ((GlTexture)textureView.texture()).glId(), 0, 0, 0);
/* 359 */     this.device.directStateAccess().blitFrameBuffers(this.drawFbo, 0, 0, 0, copyWidth, copyHeight, 0, destY, copyWidth, copyHeight + destY, 16384, 9728);
/*     */   }
/*     */   
/*     */   public GpuFence createFence() {
/* 364 */     return new GlFence(this);
/*     */   }
/*     */   
/*     */   protected <T> void executeDrawMultiple(GlRenderPass renderPass, Collection<RenderPass.Draw<T>> draws, GpuBuffer defaultIndexBuffer, IndexType defaultIndexType, Collection<String> dynamicUniforms, T uniformArgument) {
/* 368 */     if (!trySetup(renderPass, dynamicUniforms))
/*     */       return; 
/* 372 */     if (defaultIndexType == null)
/* 373 */       defaultIndexType = IndexType.SHORT; 
/* 376 */     for (RenderPass.Draw<T> draw : draws) {
/* 377 */       IndexType indexType = (draw.indexType() == null) ? defaultIndexType : draw.indexType();
/* 378 */       renderPass.setIndexBuffer((draw.indexBuffer() == null) ? defaultIndexBuffer : draw.indexBuffer(), indexType);
/* 379 */       renderPass.setVertexBuffer(draw.slot(), draw.vertexBuffer().slice());
/* 380 */       if (GlRenderPass.VALIDATION) {
/* 381 */         if (renderPass.indexBuffer == null)
/* 382 */           throw new IllegalStateException("Missing index buffer"); 
/* 384 */         ((GlBuffer)renderPass.indexBuffer).checkCanBeUsed();
/* 385 */         if (renderPass.indexBuffer.isClosed())
/* 386 */           throw new IllegalStateException("Index buffer has been closed!"); 
/* 388 */         if (draw.slot() < 0 || draw.slot() >= 16)
/* 389 */           throw new IllegalStateException("Vertex buffer slot must be between 0 and 16"); 
/* 391 */         if (renderPass.vertexBuffers[draw.slot()] != null)
/* 392 */           ((GlBuffer)renderPass.vertexBuffers[draw.slot()].buffer()).checkCanBeUsed(); 
/* 394 */         if (renderPass.vertexBuffers[draw.slot()] == null)
/* 395 */           throw new IllegalStateException("Missing vertex buffer at slot " + draw.slot()); 
/* 397 */         if (renderPass.vertexBuffers[draw.slot()].buffer().isClosed())
/* 398 */           throw new IllegalStateException("Vertex buffer at slot " + draw.slot() + " has been closed!"); 
/*     */       } 
/* 401 */       BiConsumer<T, RenderPass.UniformUploader> uniformUploaderConsumer = draw.uniformUploaderConsumer();
/* 402 */       if (uniformUploaderConsumer != null)
/* 403 */         uniformUploaderConsumer.accept(uniformArgument, (name, buffer) -> {
/*     */               ((GlBuffer)buffer.buffer()).checkCanBeUsed();
/*     */               Uniform patt1$temp = renderPass.pipeline.program().getUniform(name);
/*     */               if (patt1$temp instanceof Uniform.Ubo) {
/*     */                 Uniform.Ubo $b$0 = (Uniform.Ubo)patt1$temp;
/*     */                 try {
/*     */                   int patt2$temp = $b$0.blockBinding();
/*     */                   int tmp0$ = patt2$temp;
/*     */                   if (true) {
/*     */                     int blockBinding = patt2$temp;
/*     */                     GL33C.glBindBufferRange(35345, blockBinding, ((GlBuffer)buffer.buffer()).handle(), buffer.offset(), buffer.length());
/*     */                   } 
/* 405 */                 } catch (Throwable throwable) {
/*     */                   throw new MatchException(throwable.toString(), throwable);
/*     */                 } 
/*     */               } 
/*     */             }); 
/* 411 */       if (renderPass.vertexBufferDirty) {
/* 412 */         this.lastVertexArray = this.device.vertexArrayCache().bindVertexArray(renderPass.pipeline.info().getVertexFormatBindings(), renderPass.vertexBuffers, null);
/* 413 */         renderPass.vertexBufferDirty = false;
/*     */       } 
/* 415 */       drawFromBuffers(renderPass, draw.baseVertex(), draw.firstIndex(), draw.indexCount(), indexType, renderPass.pipeline, 1, 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void validateDraw(GlRenderPass renderPass, IndexType indexType) {
/* 420 */     if (GlRenderPass.VALIDATION) {
/* 421 */       if (indexType != null) {
/* 422 */         if (renderPass.indexBuffer == null)
/* 423 */           throw new IllegalStateException("Missing index buffer"); 
/* 425 */         ((GlBuffer)renderPass.indexBuffer).checkCanBeUsed();
/* 426 */         if (renderPass.indexBuffer.isClosed())
/* 427 */           throw new IllegalStateException("Index buffer has been closed!"); 
/* 429 */         if ((renderPass.indexBuffer.usage() & 0x40) == 0)
/* 430 */           throw new IllegalStateException("Index buffer must have GpuBuffer.USAGE_INDEX!"); 
/*     */       } 
/* 433 */       GlRenderPipeline pipeline = renderPass.pipeline;
/* 434 */       for (int i = 0; i < 16; i++) {
/* 435 */         if (renderPass.vertexBuffers[i] == null && pipeline != null && pipeline.info().getVertexFormatBindings()[i] != null)
/* 436 */           throw new IllegalStateException("Vertex format contains elements but vertex buffer at slot " + i + " is null"); 
/* 438 */         if (renderPass.vertexBuffers[i] != null)
/* 439 */           ((GlBuffer)renderPass.vertexBuffers[i].buffer()).checkCanBeUsed(); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void executeDraw(GlRenderPass renderPass, int baseVertex, int firstIndex, int drawCount, IndexType indexType, int instanceCount, int firstInstance) {
/* 446 */     if (!trySetup(renderPass, Collections.emptyList()))
/*     */       return; 
/* 450 */     validateDraw(renderPass, indexType);
/* 452 */     this.lastVertexArray = this.device.vertexArrayCache().bindVertexArray(renderPass.pipeline.info().getVertexFormatBindings(), renderPass.vertexBuffers, renderPass.vertexBufferDirty ? null : this.lastVertexArray);
/* 453 */     renderPass.vertexBufferDirty = false;
/* 454 */     drawFromBuffers(renderPass, baseVertex, firstIndex, drawCount, indexType, renderPass.pipeline, instanceCount, firstInstance);
/*     */   }
/*     */   
/*     */   public void executeDraws(GlRenderPass renderPass, IndexType indexType, PointerBuffer firstIndexOffsets, IntBuffer indexCounts, IntBuffer vertexOffsets, int drawCount) {
/* 458 */     if (!trySetup(renderPass, Collections.emptyList()))
/*     */       return; 
/* 462 */     validateDraw(renderPass, indexType);
/* 464 */     this.lastVertexArray = this.device.vertexArrayCache().bindVertexArray(renderPass.pipeline.info().getVertexFormatBindings(), renderPass.vertexBuffers, renderPass.vertexBufferDirty ? null : this.lastVertexArray);
/* 465 */     renderPass.vertexBufferDirty = false;
/* 466 */     if (indexType == null) {
/* 467 */       GL33C.nglMultiDrawArrays(GlConst.toGl(renderPass.pipeline.info().getPrimitiveTopology()), MemoryUtil.memAddress(vertexOffsets), MemoryUtil.memAddress(indexCounts), drawCount);
/*     */     } else {
/* 469 */       GlStateManager._glBindBuffer(34963, ((GlBuffer)renderPass.indexBuffer).handle());
/* 470 */       assert firstIndexOffsets != null;
/* 471 */       GL33C.nglMultiDrawElementsBaseVertex(GlConst.toGl(renderPass.pipeline.info().getPrimitiveTopology()), MemoryUtil.memAddress(indexCounts), GlConst.toGl(indexType), MemoryUtil.memAddress((CustomBuffer)firstIndexOffsets), drawCount, MemoryUtil.memAddress(vertexOffsets));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void executeDrawIndirect(GlRenderPass renderPass, IndexType indexType, GlBuffer commands, long offset, int drawCount) {
/* 476 */     if (!trySetup(renderPass, Collections.emptyList()))
/*     */       return; 
/* 480 */     validateDraw(renderPass, indexType);
/* 482 */     this.lastVertexArray = this.device.vertexArrayCache().bindVertexArray(renderPass.pipeline.info().getVertexFormatBindings(), renderPass.vertexBuffers, renderPass.vertexBufferDirty ? null : this.lastVertexArray);
/* 483 */     renderPass.vertexBufferDirty = false;
/* 484 */     GlStateManager._glBindBuffer(36671, commands.handle());
/* 485 */     if (indexType == null) {
/* 486 */       if (drawCount > 1) {
/* 487 */         ARBMultiDrawIndirect.glMultiDrawArraysIndirect(GlConst.toGl(renderPass.pipeline.info().getPrimitiveTopology()), offset, drawCount, 0);
/*     */       } else {
/* 489 */         ARBDrawIndirect.glDrawArraysIndirect(GlConst.toGl(renderPass.pipeline.info().getPrimitiveTopology()), offset);
/*     */       } 
/*     */     } else {
/* 492 */       GlStateManager._glBindBuffer(34963, ((GlBuffer)renderPass.indexBuffer).handle());
/* 493 */       if (drawCount > 1) {
/* 494 */         ARBMultiDrawIndirect.glMultiDrawElementsIndirect(GlConst.toGl(renderPass.pipeline.info().getPrimitiveTopology()), GlConst.toGl(indexType), offset, drawCount, 0);
/*     */       } else {
/* 496 */         ARBDrawIndirect.glDrawElementsIndirect(GlConst.toGl(renderPass.pipeline.info().getPrimitiveTopology()), GlConst.toGl(indexType), offset);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawFromBuffers(GlRenderPass renderPass, int baseVertex, int firstIndex, int drawCount, IndexType indexType, GlRenderPipeline pipeline, int instanceCount, int firstInstance) {
/* 502 */     if (indexType != null) {
/* 503 */       GlStateManager._glBindBuffer(34963, ((GlBuffer)renderPass.indexBuffer).handle());
/* 504 */       if (firstInstance > 0) {
/* 505 */         ARBBaseInstance.glDrawElementsInstancedBaseVertexBaseInstance(GlConst.toGl(pipeline.info().getPrimitiveTopology()), drawCount, GlConst.toGl(indexType), firstIndex * indexType.bytes, instanceCount, baseVertex, firstInstance);
/*     */       } else {
/* 507 */         GL33C.glDrawElementsInstancedBaseVertex(GlConst.toGl(pipeline.info().getPrimitiveTopology()), drawCount, GlConst.toGl(indexType), firstIndex * indexType.bytes, instanceCount, baseVertex);
/*     */       } 
/* 510 */     } else if (firstInstance > 0) {
/* 511 */       ARBBaseInstance.glDrawArraysInstancedBaseInstance(GlConst.toGl(pipeline.info().getPrimitiveTopology()), baseVertex, drawCount, instanceCount, firstInstance);
/*     */     } else {
/* 513 */       GL33C.glDrawArraysInstanced(GlConst.toGl(pipeline.info().getPrimitiveTopology()), baseVertex, drawCount, instanceCount);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean trySetup(GlRenderPass renderPass, Collection<String> dynamicUniforms) {
/*     */     // Byte code:
/*     */     //   0: getstatic com/mojang/blaze3d/opengl/GlRenderPass.VALIDATION : Z
/*     */     //   3: ifeq -> 602
/*     */     //   6: aload_1
/*     */     //   7: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   10: ifnonnull -> 24
/*     */     //   13: new java/lang/IllegalStateException
/*     */     //   16: dup
/*     */     //   17: ldc_w 'Can't draw without a render pipeline'
/*     */     //   20: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   23: athrow
/*     */     //   24: aload_1
/*     */     //   25: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   28: invokevirtual program : ()Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   31: getstatic com/mojang/blaze3d/opengl/GlProgram.INVALID_PROGRAM : Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   34: if_acmpne -> 48
/*     */     //   37: new java/lang/IllegalStateException
/*     */     //   40: dup
/*     */     //   41: ldc_w 'Pipeline contains invalid shader program'
/*     */     //   44: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   47: athrow
/*     */     //   48: aload_1
/*     */     //   49: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   52: invokevirtual info : ()Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   55: invokevirtual getBindGroupLayouts : ()Ljava/util/List;
/*     */     //   58: invokestatic flattenUniforms : (Ljava/util/List;)Ljava/util/List;
/*     */     //   61: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   66: astore_3
/*     */     //   67: aload_3
/*     */     //   68: invokeinterface hasNext : ()Z
/*     */     //   73: ifeq -> 347
/*     */     //   76: aload_3
/*     */     //   77: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   82: checkcast com/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription
/*     */     //   85: astore #4
/*     */     //   87: aload_1
/*     */     //   88: getfield uniforms : Ljava/util/HashMap;
/*     */     //   91: aload #4
/*     */     //   93: invokevirtual name : ()Ljava/lang/String;
/*     */     //   96: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   99: checkcast com/mojang/blaze3d/buffers/GpuBufferSlice
/*     */     //   102: astore #5
/*     */     //   104: aload_2
/*     */     //   105: aload #4
/*     */     //   107: invokevirtual name : ()Ljava/lang/String;
/*     */     //   110: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   115: ifeq -> 121
/*     */     //   118: goto -> 67
/*     */     //   121: aload #5
/*     */     //   123: ifnonnull -> 152
/*     */     //   126: new java/lang/IllegalStateException
/*     */     //   129: dup
/*     */     //   130: aload #4
/*     */     //   132: invokevirtual name : ()Ljava/lang/String;
/*     */     //   135: aload #4
/*     */     //   137: invokevirtual type : ()Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   140: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   143: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   148: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   151: athrow
/*     */     //   152: aload #5
/*     */     //   154: invokevirtual buffer : ()Lcom/mojang/blaze3d/buffers/GpuBuffer;
/*     */     //   157: checkcast com/mojang/blaze3d/opengl/GlBuffer
/*     */     //   160: invokevirtual checkCanBeUsed : ()V
/*     */     //   163: aload #4
/*     */     //   165: invokevirtual type : ()Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   168: getstatic com/mojang/blaze3d/shaders/UniformType.UNIFORM_BUFFER : Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   171: if_acmpne -> 236
/*     */     //   174: aload #5
/*     */     //   176: invokevirtual buffer : ()Lcom/mojang/blaze3d/buffers/GpuBuffer;
/*     */     //   179: invokevirtual isClosed : ()Z
/*     */     //   182: ifeq -> 203
/*     */     //   185: new java/lang/IllegalStateException
/*     */     //   188: dup
/*     */     //   189: aload #4
/*     */     //   191: invokevirtual name : ()Ljava/lang/String;
/*     */     //   194: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   199: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   202: athrow
/*     */     //   203: aload #5
/*     */     //   205: invokevirtual buffer : ()Lcom/mojang/blaze3d/buffers/GpuBuffer;
/*     */     //   208: invokevirtual usage : ()I
/*     */     //   211: sipush #128
/*     */     //   214: iand
/*     */     //   215: ifne -> 236
/*     */     //   218: new java/lang/IllegalStateException
/*     */     //   221: dup
/*     */     //   222: aload #4
/*     */     //   224: invokevirtual name : ()Ljava/lang/String;
/*     */     //   227: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   232: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   235: athrow
/*     */     //   236: aload #4
/*     */     //   238: invokevirtual type : ()Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   241: getstatic com/mojang/blaze3d/shaders/UniformType.TEXEL_BUFFER : Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   244: if_acmpne -> 344
/*     */     //   247: aload #5
/*     */     //   249: invokevirtual offset : ()J
/*     */     //   252: lconst_0
/*     */     //   253: lcmp
/*     */     //   254: ifne -> 274
/*     */     //   257: aload #5
/*     */     //   259: invokevirtual length : ()J
/*     */     //   262: aload #5
/*     */     //   264: invokevirtual buffer : ()Lcom/mojang/blaze3d/buffers/GpuBuffer;
/*     */     //   267: invokevirtual size : ()J
/*     */     //   270: lcmp
/*     */     //   271: ifeq -> 285
/*     */     //   274: new java/lang/IllegalStateException
/*     */     //   277: dup
/*     */     //   278: ldc_w 'Uniform texel buffers do not support a slice of a buffer, must be entire buffer'
/*     */     //   281: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   284: athrow
/*     */     //   285: aload #5
/*     */     //   287: invokevirtual buffer : ()Lcom/mojang/blaze3d/buffers/GpuBuffer;
/*     */     //   290: invokevirtual usage : ()I
/*     */     //   293: sipush #256
/*     */     //   296: iand
/*     */     //   297: ifne -> 318
/*     */     //   300: new java/lang/IllegalStateException
/*     */     //   303: dup
/*     */     //   304: aload #4
/*     */     //   306: invokevirtual name : ()Ljava/lang/String;
/*     */     //   309: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   314: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   317: athrow
/*     */     //   318: aload #4
/*     */     //   320: invokevirtual gpuFormat : ()Lcom/mojang/blaze3d/GpuFormat;
/*     */     //   323: ifnonnull -> 344
/*     */     //   326: new java/lang/IllegalStateException
/*     */     //   329: dup
/*     */     //   330: aload #4
/*     */     //   332: invokevirtual name : ()Ljava/lang/String;
/*     */     //   335: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   340: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   343: athrow
/*     */     //   344: goto -> 67
/*     */     //   347: aload_1
/*     */     //   348: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   351: invokevirtual program : ()Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   354: invokevirtual getUniforms : ()Ljava/util/Map;
/*     */     //   357: invokeinterface entrySet : ()Ljava/util/Set;
/*     */     //   362: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   367: astore_3
/*     */     //   368: aload_3
/*     */     //   369: invokeinterface hasNext : ()Z
/*     */     //   374: ifeq -> 558
/*     */     //   377: aload_3
/*     */     //   378: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   383: checkcast java/util/Map$Entry
/*     */     //   386: astore #4
/*     */     //   388: aload #4
/*     */     //   390: invokeinterface getValue : ()Ljava/lang/Object;
/*     */     //   395: instanceof com/mojang/blaze3d/opengl/Uniform$Sampler
/*     */     //   398: ifeq -> 555
/*     */     //   401: aload #4
/*     */     //   403: invokeinterface getKey : ()Ljava/lang/Object;
/*     */     //   408: checkcast java/lang/String
/*     */     //   411: astore #5
/*     */     //   413: aload_1
/*     */     //   414: getfield samplers : Ljava/util/HashMap;
/*     */     //   417: aload #5
/*     */     //   419: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   422: checkcast com/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler
/*     */     //   425: astore #6
/*     */     //   427: aload #6
/*     */     //   429: ifnonnull -> 447
/*     */     //   432: new java/lang/IllegalStateException
/*     */     //   435: dup
/*     */     //   436: aload #5
/*     */     //   438: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   443: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   446: athrow
/*     */     //   447: aload #6
/*     */     //   449: invokevirtual view : ()Lcom/mojang/blaze3d/opengl/GlTextureView;
/*     */     //   452: astore #7
/*     */     //   454: aload #7
/*     */     //   456: invokevirtual isClosed : ()Z
/*     */     //   459: ifeq -> 485
/*     */     //   462: new java/lang/IllegalStateException
/*     */     //   465: dup
/*     */     //   466: aload #5
/*     */     //   468: aload #7
/*     */     //   470: invokevirtual texture : ()Lcom/mojang/blaze3d/opengl/GlTexture;
/*     */     //   473: invokevirtual getLabel : ()Ljava/lang/String;
/*     */     //   476: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   481: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   484: athrow
/*     */     //   485: aload #7
/*     */     //   487: invokevirtual texture : ()Lcom/mojang/blaze3d/opengl/GlTexture;
/*     */     //   490: invokevirtual usage : ()I
/*     */     //   493: iconst_4
/*     */     //   494: iand
/*     */     //   495: ifne -> 521
/*     */     //   498: new java/lang/IllegalStateException
/*     */     //   501: dup
/*     */     //   502: aload #5
/*     */     //   504: aload #7
/*     */     //   506: invokevirtual texture : ()Lcom/mojang/blaze3d/opengl/GlTexture;
/*     */     //   509: invokevirtual getLabel : ()Ljava/lang/String;
/*     */     //   512: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   517: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   520: athrow
/*     */     //   521: aload #6
/*     */     //   523: invokevirtual sampler : ()Lcom/mojang/blaze3d/opengl/GlSampler;
/*     */     //   526: invokevirtual isClosed : ()Z
/*     */     //   529: ifeq -> 555
/*     */     //   532: new java/lang/IllegalStateException
/*     */     //   535: dup
/*     */     //   536: aload #5
/*     */     //   538: aload #7
/*     */     //   540: invokevirtual texture : ()Lcom/mojang/blaze3d/opengl/GlTexture;
/*     */     //   543: invokevirtual getLabel : ()Ljava/lang/String;
/*     */     //   546: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   551: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   554: athrow
/*     */     //   555: goto -> 368
/*     */     //   558: aload_1
/*     */     //   559: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   562: invokevirtual info : ()Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   565: invokevirtual wantsDepthTexture : ()Z
/*     */     //   568: ifeq -> 624
/*     */     //   571: aload_1
/*     */     //   572: invokevirtual hasDepthTexture : ()Z
/*     */     //   575: ifne -> 624
/*     */     //   578: getstatic com/mojang/blaze3d/opengl/GlCommandEncoder.LOGGER : Lorg/slf4j/Logger;
/*     */     //   581: ldc_w 'Render pipeline {} wants a depth texture but none was provided - this is probably a bug'
/*     */     //   584: aload_1
/*     */     //   585: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   588: invokevirtual info : ()Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   591: invokevirtual getLocation : ()Lnet/minecraft/resources/Identifier;
/*     */     //   594: invokeinterface warn : (Ljava/lang/String;Ljava/lang/Object;)V
/*     */     //   599: goto -> 624
/*     */     //   602: aload_1
/*     */     //   603: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   606: ifnull -> 622
/*     */     //   609: aload_1
/*     */     //   610: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   613: invokevirtual program : ()Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   616: getstatic com/mojang/blaze3d/opengl/GlProgram.INVALID_PROGRAM : Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   619: if_acmpne -> 624
/*     */     //   622: iconst_0
/*     */     //   623: ireturn
/*     */     //   624: aload_1
/*     */     //   625: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   628: invokevirtual info : ()Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   631: astore_3
/*     */     //   632: aload_1
/*     */     //   633: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   636: invokevirtual program : ()Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   639: astore #4
/*     */     //   641: aload_0
/*     */     //   642: aload_3
/*     */     //   643: invokevirtual applyPipelineState : (Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V
/*     */     //   646: aload_0
/*     */     //   647: getfield lastProgram : Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   650: aload #4
/*     */     //   652: if_acmpeq -> 659
/*     */     //   655: iconst_1
/*     */     //   656: goto -> 660
/*     */     //   659: iconst_0
/*     */     //   660: istore #5
/*     */     //   662: iload #5
/*     */     //   664: ifeq -> 681
/*     */     //   667: aload #4
/*     */     //   669: invokevirtual getProgramId : ()I
/*     */     //   672: invokestatic _glUseProgram : (I)V
/*     */     //   675: aload_0
/*     */     //   676: aload #4
/*     */     //   678: putfield lastProgram : Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   681: aload #4
/*     */     //   683: invokevirtual getUniforms : ()Ljava/util/Map;
/*     */     //   686: invokeinterface entrySet : ()Ljava/util/Set;
/*     */     //   691: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   696: astore #6
/*     */     //   698: aload #6
/*     */     //   700: invokeinterface hasNext : ()Z
/*     */     //   705: ifeq -> 1273
/*     */     //   708: aload #6
/*     */     //   710: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   715: checkcast java/util/Map$Entry
/*     */     //   718: astore #7
/*     */     //   720: aload #7
/*     */     //   722: invokeinterface getKey : ()Ljava/lang/Object;
/*     */     //   727: checkcast java/lang/String
/*     */     //   730: astore #8
/*     */     //   732: aload_1
/*     */     //   733: getfield dirtyUniforms : Ljava/util/Set;
/*     */     //   736: aload #8
/*     */     //   738: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   743: istore #9
/*     */     //   745: aload #7
/*     */     //   747: invokeinterface getValue : ()Ljava/lang/Object;
/*     */     //   752: checkcast com/mojang/blaze3d/opengl/Uniform
/*     */     //   755: dup
/*     */     //   756: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   759: pop
/*     */     //   760: astore #10
/*     */     //   762: iconst_0
/*     */     //   763: istore #11
/*     */     //   765: aload #10
/*     */     //   767: iload #11
/*     */     //   769: <illegal opcode> typeSwitch : (Lcom/mojang/blaze3d/opengl/Uniform;I)I
/*     */     //   774: tableswitch default -> 800, 0 -> 810, 1 -> 899, 2 -> 1061
/*     */     //   800: new java/lang/MatchException
/*     */     //   803: dup
/*     */     //   804: aconst_null
/*     */     //   805: aconst_null
/*     */     //   806: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   809: athrow
/*     */     //   810: aload #10
/*     */     //   812: checkcast com/mojang/blaze3d/opengl/Uniform$Ubo
/*     */     //   815: astore #12
/*     */     //   817: aload #12
/*     */     //   819: invokevirtual blockBinding : ()I
/*     */     //   822: istore #14
/*     */     //   824: iload #14
/*     */     //   826: istore #15
/*     */     //   828: iconst_1
/*     */     //   829: ifeq -> 839
/*     */     //   832: iload #14
/*     */     //   834: istore #13
/*     */     //   836: goto -> 845
/*     */     //   839: iconst_1
/*     */     //   840: istore #11
/*     */     //   842: goto -> 765
/*     */     //   845: iload #9
/*     */     //   847: ifne -> 853
/*     */     //   850: goto -> 698
/*     */     //   853: aload_1
/*     */     //   854: getfield uniforms : Ljava/util/HashMap;
/*     */     //   857: aload #8
/*     */     //   859: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   862: checkcast com/mojang/blaze3d/buffers/GpuBufferSlice
/*     */     //   865: astore #14
/*     */     //   867: ldc_w 35345
/*     */     //   870: iload #13
/*     */     //   872: aload #14
/*     */     //   874: invokevirtual buffer : ()Lcom/mojang/blaze3d/buffers/GpuBuffer;
/*     */     //   877: checkcast com/mojang/blaze3d/opengl/GlBuffer
/*     */     //   880: invokevirtual handle : ()I
/*     */     //   883: aload #14
/*     */     //   885: invokevirtual offset : ()J
/*     */     //   888: aload #14
/*     */     //   890: invokevirtual length : ()J
/*     */     //   893: invokestatic glBindBufferRange : (IIIJJ)V
/*     */     //   896: goto -> 1270
/*     */     //   899: aload #10
/*     */     //   901: checkcast com/mojang/blaze3d/opengl/Uniform$Utb
/*     */     //   904: astore #14
/*     */     //   906: aload #14
/*     */     //   908: invokevirtual location : ()I
/*     */     //   911: istore #19
/*     */     //   913: iload #19
/*     */     //   915: istore #20
/*     */     //   917: iconst_1
/*     */     //   918: ifeq -> 977
/*     */     //   921: iload #19
/*     */     //   923: istore #15
/*     */     //   925: aload #14
/*     */     //   927: invokevirtual samplerIndex : ()I
/*     */     //   930: istore #19
/*     */     //   932: iload #19
/*     */     //   934: istore #20
/*     */     //   936: iconst_1
/*     */     //   937: ifeq -> 977
/*     */     //   940: iload #19
/*     */     //   942: istore #16
/*     */     //   944: aload #14
/*     */     //   946: invokevirtual format : ()Lcom/mojang/blaze3d/GpuFormat;
/*     */     //   949: astore #19
/*     */     //   951: aload #19
/*     */     //   953: astore #17
/*     */     //   955: aload #14
/*     */     //   957: invokevirtual texture : ()I
/*     */     //   960: istore #19
/*     */     //   962: iload #19
/*     */     //   964: istore #20
/*     */     //   966: iconst_1
/*     */     //   967: ifeq -> 977
/*     */     //   970: iload #19
/*     */     //   972: istore #18
/*     */     //   974: goto -> 983
/*     */     //   977: iconst_2
/*     */     //   978: istore #11
/*     */     //   980: goto -> 765
/*     */     //   983: iload #5
/*     */     //   985: ifne -> 993
/*     */     //   988: iload #9
/*     */     //   990: ifeq -> 1000
/*     */     //   993: iload #15
/*     */     //   995: iload #16
/*     */     //   997: invokestatic _glUniform1i : (II)V
/*     */     //   1000: ldc_w 33984
/*     */     //   1003: iload #16
/*     */     //   1005: iadd
/*     */     //   1006: invokestatic _activeTexture : (I)V
/*     */     //   1009: ldc_w 35882
/*     */     //   1012: iload #18
/*     */     //   1014: invokestatic glBindTexture : (II)V
/*     */     //   1017: iload #9
/*     */     //   1019: ifeq -> 1270
/*     */     //   1022: aload_1
/*     */     //   1023: getfield uniforms : Ljava/util/HashMap;
/*     */     //   1026: aload #8
/*     */     //   1028: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   1031: checkcast com/mojang/blaze3d/buffers/GpuBufferSlice
/*     */     //   1034: astore #19
/*     */     //   1036: ldc_w 35882
/*     */     //   1039: aload #17
/*     */     //   1041: invokestatic toGlInternalId : (Lcom/mojang/blaze3d/GpuFormat;)I
/*     */     //   1044: aload #19
/*     */     //   1046: invokevirtual buffer : ()Lcom/mojang/blaze3d/buffers/GpuBuffer;
/*     */     //   1049: checkcast com/mojang/blaze3d/opengl/GlBuffer
/*     */     //   1052: invokevirtual handle : ()I
/*     */     //   1055: invokestatic glTexBuffer : (III)V
/*     */     //   1058: goto -> 1270
/*     */     //   1061: aload #10
/*     */     //   1063: checkcast com/mojang/blaze3d/opengl/Uniform$Sampler
/*     */     //   1066: astore #19
/*     */     //   1068: aload #19
/*     */     //   1070: invokevirtual location : ()I
/*     */     //   1073: istore #22
/*     */     //   1075: iload #22
/*     */     //   1077: istore #23
/*     */     //   1079: iconst_1
/*     */     //   1080: ifeq -> 1109
/*     */     //   1083: iload #22
/*     */     //   1085: istore #20
/*     */     //   1087: aload #19
/*     */     //   1089: invokevirtual samplerIndex : ()I
/*     */     //   1092: istore #22
/*     */     //   1094: iload #22
/*     */     //   1096: istore #23
/*     */     //   1098: iconst_1
/*     */     //   1099: ifeq -> 1109
/*     */     //   1102: iload #22
/*     */     //   1104: istore #21
/*     */     //   1106: goto -> 1115
/*     */     //   1109: iconst_3
/*     */     //   1110: istore #11
/*     */     //   1112: goto -> 765
/*     */     //   1115: aload_1
/*     */     //   1116: getfield samplers : Ljava/util/HashMap;
/*     */     //   1119: aload #8
/*     */     //   1121: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   1124: checkcast com/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler
/*     */     //   1127: astore #22
/*     */     //   1129: aload #22
/*     */     //   1131: ifnonnull -> 1137
/*     */     //   1134: goto -> 698
/*     */     //   1137: aload #22
/*     */     //   1139: invokevirtual view : ()Lcom/mojang/blaze3d/opengl/GlTextureView;
/*     */     //   1142: astore #23
/*     */     //   1144: iload #5
/*     */     //   1146: ifne -> 1154
/*     */     //   1149: iload #9
/*     */     //   1151: ifeq -> 1161
/*     */     //   1154: iload #20
/*     */     //   1156: iload #21
/*     */     //   1158: invokestatic _glUniform1i : (II)V
/*     */     //   1161: ldc_w 33984
/*     */     //   1164: iload #21
/*     */     //   1166: iadd
/*     */     //   1167: invokestatic _activeTexture : (I)V
/*     */     //   1170: aload #23
/*     */     //   1172: invokevirtual texture : ()Lcom/mojang/blaze3d/opengl/GlTexture;
/*     */     //   1175: astore #24
/*     */     //   1177: aload #24
/*     */     //   1179: invokevirtual usage : ()I
/*     */     //   1182: bipush #16
/*     */     //   1184: iand
/*     */     //   1185: ifeq -> 1207
/*     */     //   1188: ldc_w 34067
/*     */     //   1191: istore #25
/*     */     //   1193: ldc_w 34067
/*     */     //   1196: aload #24
/*     */     //   1198: getfield id : I
/*     */     //   1201: invokestatic glBindTexture : (II)V
/*     */     //   1204: goto -> 1220
/*     */     //   1207: sipush #3553
/*     */     //   1210: istore #25
/*     */     //   1212: aload #24
/*     */     //   1214: getfield id : I
/*     */     //   1217: invokestatic _bindTexture : (I)V
/*     */     //   1220: iload #21
/*     */     //   1222: aload #22
/*     */     //   1224: invokevirtual sampler : ()Lcom/mojang/blaze3d/opengl/GlSampler;
/*     */     //   1227: invokevirtual getId : ()I
/*     */     //   1230: invokestatic glBindSampler : (II)V
/*     */     //   1233: iload #25
/*     */     //   1235: ldc_w 33084
/*     */     //   1238: aload #23
/*     */     //   1240: invokevirtual baseMipLevel : ()I
/*     */     //   1243: invokestatic _texParameter : (III)V
/*     */     //   1246: iload #25
/*     */     //   1248: ldc_w 33085
/*     */     //   1251: aload #23
/*     */     //   1253: invokevirtual baseMipLevel : ()I
/*     */     //   1256: aload #23
/*     */     //   1258: invokevirtual mipLevels : ()I
/*     */     //   1261: iadd
/*     */     //   1262: iconst_1
/*     */     //   1263: isub
/*     */     //   1264: invokestatic _texParameter : (III)V
/*     */     //   1267: goto -> 1270
/*     */     //   1270: goto -> 698
/*     */     //   1273: aload_1
/*     */     //   1274: getfield dirtyUniforms : Ljava/util/Set;
/*     */     //   1277: invokeinterface clear : ()V
/*     */     //   1282: aload_1
/*     */     //   1283: getfield colorAttachmentCount : I
/*     */     //   1286: newarray int
/*     */     //   1288: astore #6
/*     */     //   1290: iconst_0
/*     */     //   1291: istore #7
/*     */     //   1293: iload #7
/*     */     //   1295: aload_1
/*     */     //   1296: getfield colorAttachmentCount : I
/*     */     //   1299: if_icmpge -> 1319
/*     */     //   1302: aload #6
/*     */     //   1304: iload #7
/*     */     //   1306: ldc_w 36064
/*     */     //   1309: iload #7
/*     */     //   1311: iadd
/*     */     //   1312: iastore
/*     */     //   1313: iinc #7, 1
/*     */     //   1316: goto -> 1293
/*     */     //   1319: aload #6
/*     */     //   1321: invokestatic glDrawBuffers : ([I)V
/*     */     //   1324: aload_1
/*     */     //   1325: invokevirtual isScissorEnabled : ()Z
/*     */     //   1328: ifeq -> 1356
/*     */     //   1331: invokestatic _enableScissorTest : ()V
/*     */     //   1334: aload_1
/*     */     //   1335: invokevirtual getScissorX : ()I
/*     */     //   1338: aload_1
/*     */     //   1339: invokevirtual getScissorY : ()I
/*     */     //   1342: aload_1
/*     */     //   1343: invokevirtual getScissorWidth : ()I
/*     */     //   1346: aload_1
/*     */     //   1347: invokevirtual getScissorHeight : ()I
/*     */     //   1350: invokestatic _scissorBox : (IIII)V
/*     */     //   1353: goto -> 1359
/*     */     //   1356: invokestatic _disableScissorTest : ()V
/*     */     //   1359: iconst_1
/*     */     //   1360: ireturn
/*     */     //   1361: astore #7
/*     */     //   1363: new java/lang/MatchException
/*     */     //   1366: dup
/*     */     //   1367: aload #7
/*     */     //   1369: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   1372: aload #7
/*     */     //   1374: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   1377: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #519	-> 0
/*     */     //   #521	-> 6
/*     */     //   #522	-> 13
/*     */     //   #525	-> 24
/*     */     //   #526	-> 37
/*     */     //   #529	-> 48
/*     */     //   #530	-> 87
/*     */     //   #531	-> 104
/*     */     //   #533	-> 118
/*     */     //   #535	-> 121
/*     */     //   #536	-> 126
/*     */     //   #538	-> 152
/*     */     //   #540	-> 163
/*     */     //   #541	-> 174
/*     */     //   #542	-> 185
/*     */     //   #544	-> 203
/*     */     //   #545	-> 218
/*     */     //   #548	-> 236
/*     */     //   #549	-> 247
/*     */     //   #550	-> 274
/*     */     //   #552	-> 285
/*     */     //   #553	-> 300
/*     */     //   #555	-> 318
/*     */     //   #556	-> 326
/*     */     //   #559	-> 344
/*     */     //   #561	-> 347
/*     */     //   #562	-> 388
/*     */     //   #563	-> 401
/*     */     //   #564	-> 413
/*     */     //   #565	-> 427
/*     */     //   #566	-> 432
/*     */     //   #568	-> 447
/*     */     //   #569	-> 454
/*     */     //   #570	-> 462
/*     */     //   #571	-> 485
/*     */     //   #572	-> 498
/*     */     //   #574	-> 521
/*     */     //   #575	-> 532
/*     */     //   #579	-> 555
/*     */     //   #581	-> 558
/*     */     //   #582	-> 578
/*     */     //   #584	-> 602
/*     */     //   #585	-> 622
/*     */     //   #591	-> 624
/*     */     //   #592	-> 632
/*     */     //   #594	-> 641
/*     */     //   #596	-> 646
/*     */     //   #597	-> 662
/*     */     //   #598	-> 667
/*     */     //   #599	-> 675
/*     */     //   #602	-> 681
/*     */     //   #603	-> 720
/*     */     //   #604	-> 732
/*     */     //   #606	-> 745
/*     */     //   #607	-> 810
/*     */     //   #608	-> 845
/*     */     //   #609	-> 850
/*     */     //   #611	-> 853
/*     */     //   #612	-> 867
/*     */     //   #613	-> 896
/*     */     //   #614	-> 899
/*     */     //   #615	-> 913
/*     */     //   #614	-> 925
/*     */     //   #615	-> 932
/*     */     //   #614	-> 944
/*     */     //   #615	-> 951
/*     */     //   #614	-> 955
/*     */     //   #615	-> 962
/*     */     //   #614	-> 977
/*     */     //   #617	-> 983
/*     */     //   #618	-> 993
/*     */     //   #620	-> 1000
/*     */     //   #621	-> 1009
/*     */     //   #622	-> 1017
/*     */     //   #623	-> 1022
/*     */     //   #624	-> 1036
/*     */     //   #625	-> 1058
/*     */     //   #627	-> 1061
/*     */     //   #628	-> 1115
/*     */     //   #629	-> 1129
/*     */     //   #630	-> 1134
/*     */     //   #633	-> 1137
/*     */     //   #634	-> 1144
/*     */     //   #635	-> 1154
/*     */     //   #637	-> 1161
/*     */     //   #638	-> 1170
/*     */     //   #640	-> 1177
/*     */     //   #641	-> 1188
/*     */     //   #642	-> 1193
/*     */     //   #644	-> 1207
/*     */     //   #645	-> 1212
/*     */     //   #647	-> 1220
/*     */     //   #648	-> 1233
/*     */     //   #649	-> 1246
/*     */     //   #650	-> 1267
/*     */     //   #652	-> 1270
/*     */     //   #653	-> 1273
/*     */     //   #655	-> 1282
/*     */     //   #656	-> 1290
/*     */     //   #657	-> 1302
/*     */     //   #656	-> 1313
/*     */     //   #659	-> 1319
/*     */     //   #661	-> 1324
/*     */     //   #662	-> 1331
/*     */     //   #663	-> 1334
/*     */     //   #665	-> 1356
/*     */     //   #667	-> 1359
/*     */     //   #627	-> 1361
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   104	240	5	value	Lcom/mojang/blaze3d/buffers/GpuBufferSlice;
/*     */     //   87	257	4	uniform	Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;
/*     */     //   454	101	7	textureView	Lcom/mojang/blaze3d/opengl/GlTextureView;
/*     */     //   413	142	5	name	Ljava/lang/String;
/*     */     //   427	128	6	viewAndSampler	Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;
/*     */     //   388	167	4	entry	Ljava/util/Map$Entry;
/*     */     //   836	3	13	blockBinding	I
/*     */     //   867	29	14	bufferView	Lcom/mojang/blaze3d/buffers/GpuBufferSlice;
/*     */     //   845	54	13	blockBinding	I
/*     */     //   925	52	15	location	I
/*     */     //   944	33	16	samplerIndex	I
/*     */     //   955	22	17	format	Lcom/mojang/blaze3d/GpuFormat;
/*     */     //   974	3	18	texture	I
/*     */     //   1036	22	19	bufferView	Lcom/mojang/blaze3d/buffers/GpuBufferSlice;
/*     */     //   983	78	15	location	I
/*     */     //   983	78	16	samplerIndex	I
/*     */     //   983	78	17	format	Lcom/mojang/blaze3d/GpuFormat;
/*     */     //   983	78	18	texture	I
/*     */     //   1087	22	20	location	I
/*     */     //   1106	3	21	samplerIndex	I
/*     */     //   1193	14	25	target	I
/*     */     //   1129	138	22	viewAndSampler	Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;
/*     */     //   1144	123	23	textureView	Lcom/mojang/blaze3d/opengl/GlTextureView;
/*     */     //   1177	90	24	texture	Lcom/mojang/blaze3d/opengl/GlTexture;
/*     */     //   1212	55	25	target	I
/*     */     //   1115	155	20	location	I
/*     */     //   1115	155	21	samplerIndex	I
/*     */     //   732	538	8	name	Ljava/lang/String;
/*     */     //   745	525	9	isDirty	Z
/*     */     //   720	550	7	entry	Ljava/util/Map$Entry;
/*     */     //   1293	26	7	i	I
/*     */     //   632	729	3	pipeline	Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   641	720	4	glProgram	Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   662	699	5	differentProgram	Z
/*     */     //   1290	71	6	drawBuffers	[I
/*     */     //   0	1378	0	this	Lcom/mojang/blaze3d/opengl/GlCommandEncoder;
/*     */     //   0	1378	1	renderPass	Lcom/mojang/blaze3d/opengl/GlRenderPass;
/*     */     //   0	1378	2	dynamicUniforms	Ljava/util/Collection;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   388	167	4	entry	Ljava/util/Map$Entry<Ljava/lang/String;Lcom/mojang/blaze3d/opengl/Uniform;>;
/*     */     //   720	550	7	entry	Ljava/util/Map$Entry<Ljava/lang/String;Lcom/mojang/blaze3d/opengl/Uniform;>;
/*     */     //   0	1378	2	dynamicUniforms	Ljava/util/Collection<Ljava/lang/String;>;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   819	822	1361	java/lang/Throwable
/*     */     //   908	911	1361	java/lang/Throwable
/*     */     //   927	930	1361	java/lang/Throwable
/*     */     //   946	949	1361	java/lang/Throwable
/*     */     //   957	960	1361	java/lang/Throwable
/*     */     //   1070	1073	1361	java/lang/Throwable
/*     */     //   1089	1092	1361	java/lang/Throwable
/*     */   }
/*     */   
/*     */   private void applyPipelineState(RenderPipeline pipeline) {
/* 672 */     if (this.lastPipeline == pipeline)
/*     */       return; 
/* 675 */     this.lastPipeline = pipeline;
/* 677 */     DepthStencilState depthStencilState = pipeline.getDepthStencilState();
/* 678 */     if (depthStencilState != null) {
/* 679 */       GlStateManager._enableDepthTest();
/* 680 */       GlStateManager._depthFunc(GlConst.toGl(depthStencilState.depthTest()));
/* 681 */       GlStateManager._depthMask(depthStencilState.writeDepth());
/* 682 */       if (depthStencilState.depthBiasConstant() != 0.0F || depthStencilState.depthBiasScaleFactor() != 0.0F) {
/* 683 */         GlStateManager._polygonOffset(depthStencilState.depthBiasScaleFactor(), depthStencilState.depthBiasConstant());
/* 684 */         GlStateManager._enablePolygonOffset();
/*     */       } else {
/* 686 */         GlStateManager._disablePolygonOffset();
/*     */       } 
/*     */     } else {
/* 689 */       GlStateManager._disableDepthTest();
/* 690 */       GlStateManager._depthMask(false);
/* 691 */       GlStateManager._disablePolygonOffset();
/*     */     } 
/* 694 */     if (pipeline.isCull()) {
/* 695 */       GlStateManager._enableCull();
/*     */     } else {
/* 697 */       GlStateManager._disableCull();
/*     */     } 
/* 700 */     ColorTargetState[] colorTargetStates = pipeline.getColorTargetStates();
/*     */     int i;
/* 702 */     for (i = 0; i < colorTargetStates.length; i++) {
/* 703 */       ColorTargetState state = colorTargetStates[i];
/* 704 */       if (state != null)
/* 707 */         if (state.blendFunction().isPresent()) {
/* 708 */           GlStateManager._enableBlend(i);
/* 709 */           BlendFunction blendFunction = state.blendFunction().get();
/* 710 */           GlStateManager._blendFuncSeparate(GlConst.toGl(blendFunction.color().sourceFactor()), GlConst.toGl(blendFunction.color().destFactor()), GlConst.toGl(blendFunction.alpha().sourceFactor()), GlConst.toGl(blendFunction.alpha().destFactor()));
/* 711 */           GlStateManager._blendEquationSeparate(GlConst.toGl(blendFunction.color().op()), GlConst.toGl(blendFunction.alpha().op()));
/*     */         } else {
/* 713 */           GlStateManager._disableBlend(i);
/*     */         }  
/*     */     } 
/* 717 */     GlStateManager._polygonMode(1032, GlConst.toGl(pipeline.getPolygonMode()));
/* 719 */     for (i = 0; i < colorTargetStates.length; i++) {
/* 720 */       ColorTargetState state = colorTargetStates[i];
/* 721 */       if (state != null)
/* 722 */         GlStateManager._colorMask(i, state.writeMask()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void submitRenderPass() {
/* 729 */     GlStateManager._glBindFramebuffer(36160, 0);
/* 730 */     this.device.debugLabels().popDebugGroup();
/*     */   }
/*     */   
/*     */   public void writeTimestamp(GpuQueryPool pool, int index) {
/* 735 */     ((GlQueryPool)pool).writeTimestamp(index);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlCommandEncoder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */