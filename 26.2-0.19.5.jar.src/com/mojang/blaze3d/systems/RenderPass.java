/*     */ package com.mojang.blaze3d.systems;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.ColorTargetState;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import org.joml.Vector4fc;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.vulkan.VkDrawIndexedIndirectCommand;
/*     */ import org.lwjgl.vulkan.VkDrawIndirectCommand;
/*     */ 
/*     */ public class RenderPass implements AutoCloseable {
/*     */   public static final int MAX_VERTEX_BUFFERS = 16;
/*     */   
/*     */   private final RenderPassBackend backend;
/*     */   
/*     */   private final GpuDeviceBackend device;
/*     */   
/*     */   private final DeviceFeatures deviceFeatures;
/*     */   
/*     */   private final DeviceLimits deviceLimits;
/*     */   
/*     */   private final Runnable onFinish;
/*     */   
/*     */   private final RenderArea renderArea;
/*     */   
/*     */   private boolean isClosed;
/*     */   
/*     */   private int pushedDebugGroups;
/*     */   
/*     */   private final List<RenderPassDescriptor.Attachment<Optional<Vector4fc>>> colorAttachments;
/*     */   
/*     */   public RenderPass(RenderPassBackend backend, GpuDeviceBackend device, List<RenderPassDescriptor.Attachment<Optional<Vector4fc>>> colorAttachments, Runnable onFinish, RenderArea renderArea) {
/*  39 */     this.backend = backend;
/*  40 */     this.device = device;
/*  41 */     this.deviceFeatures = device.getDeviceInfo().features();
/*  42 */     this.deviceLimits = device.getDeviceInfo().limits();
/*  43 */     this.colorAttachments = colorAttachments;
/*  44 */     this.onFinish = onFinish;
/*  45 */     this.renderArea = renderArea;
/*     */   }
/*     */   
/*     */   public void pushDebugGroup(Supplier<String> label) {
/*  49 */     if (this.isClosed)
/*  50 */       throw new IllegalStateException("Can't use a closed render pass"); 
/*  52 */     this.pushedDebugGroups++;
/*  53 */     this.backend.pushDebugGroup(label);
/*     */   }
/*     */   
/*     */   public void popDebugGroup() {
/*  57 */     if (this.isClosed)
/*  58 */       throw new IllegalStateException("Can't use a closed render pass"); 
/*  60 */     if (this.pushedDebugGroups == 0)
/*  61 */       throw new IllegalStateException("Can't pop more debug groups than was pushed!"); 
/*  63 */     this.pushedDebugGroups--;
/*  64 */     this.backend.popDebugGroup();
/*     */   }
/*     */   
/*     */   public void writeTimestamp(GpuQueryPool pool, int index) {
/*  68 */     if (index < 0 || index > pool.size())
/*  69 */       throw new IllegalStateException("Index " + index + " is out of range for query pool of size " + pool.size()); 
/*  71 */     this.backend.writeTimestamp(pool, index);
/*     */   }
/*     */   
/*     */   public void setPipeline(RenderPipeline pipeline) {
/*  75 */     ColorTargetState[] colorTargetStates = pipeline.getColorTargetStates();
/*  76 */     if (colorTargetStates.length != this.colorAttachments.size())
/*  77 */       throw new IllegalStateException("Render pass color attachment count must match pipeline color target state count."); 
/*  79 */     for (int i = 0; i < this.colorAttachments.size(); i++) {
/*  80 */       RenderPassDescriptor.Attachment<Optional<Vector4fc>> attachment = this.colorAttachments.get(i);
/*  81 */       if (attachment != null) {
/*  84 */         ColorTargetState colorTargetState = colorTargetStates[i];
/*  85 */         if (colorTargetState == null || colorTargetState.format() != attachment.textureView().texture().getFormat())
/*  86 */           throw new IllegalStateException("Render pass color attachment " + i + " format doesn't match pipeline format."); 
/*     */       } 
/*     */     } 
/*  89 */     this.backend.setPipeline(pipeline);
/*     */   }
/*     */   
/*     */   public void bindTexture(String name, GpuTextureView textureView, GpuSampler sampler) {
/*  93 */     this.backend.bindTexture(name, textureView, sampler);
/*     */   }
/*     */   
/*     */   public void setUniform(String name, GpuBuffer value) {
/*  97 */     this.backend.setUniform(name, value);
/*     */   }
/*     */   
/*     */   public void setUniform(String name, GpuBufferSlice value) {
/* 101 */     int alignment = this.device.getDeviceInfo().limits().minUniformOffsetAlignment();
/* 102 */     if (value.offset() % alignment > 0L)
/* 103 */       throw new IllegalArgumentException("Uniform buffer offset must be aligned to " + alignment); 
/* 105 */     this.backend.setUniform(name, value);
/*     */   }
/*     */   
/*     */   public void enableScissor(int x, int y, int width, int height) {
/* 109 */     if (width <= 0 || height <= 0)
/* 110 */       throw new IllegalArgumentException("Scissor size must be >0, was " + width + "x" + height); 
/* 112 */     if (x < this.renderArea.x() || y < this.renderArea.y() || x + width > this.renderArea.x() + this.renderArea.width() || y + height > this.renderArea.height())
/* 113 */       throw new IllegalArgumentException("Scissor at " + x + ", " + y + " with size " + width + "x" + height + " is out of bounds for render area " + String.valueOf(this.renderArea)); 
/* 115 */     this.backend.enableScissor(x, y, width, height);
/*     */   }
/*     */   
/*     */   public void disableScissor() {
/* 119 */     this.backend.disableScissor();
/*     */   }
/*     */   
/*     */   public void setVertexBuffer(int slot, GpuBufferSlice vertexBuffer) {
/* 123 */     if (slot < 0 || slot >= 16)
/* 124 */       throw new IllegalArgumentException("Vertex buffer slot is out of range: " + slot); 
/* 126 */     if (vertexBuffer != null && vertexBuffer.buffer().isClosed())
/* 127 */       throw new IllegalStateException("Vertex buffer at slot " + slot + " has been closed!"); 
/* 129 */     if (vertexBuffer != null && (vertexBuffer.buffer().usage() & 0x20) == 0)
/* 130 */       throw new IllegalStateException("Vertex buffer at slot " + slot + " doesn't have GpuBuffer.USAGE_VERTEX flag!"); 
/* 132 */     this.backend.setVertexBuffer(slot, vertexBuffer);
/*     */   }
/*     */   
/*     */   public void setIndexBuffer(GpuBuffer indexBuffer, IndexType indexType) {
/* 136 */     this.backend.setIndexBuffer(indexBuffer, indexType);
/*     */   }
/*     */   
/*     */   public void drawIndexed(int indexCount, int instanceCount, int firstIndex, int vertexOffset, int firstInstance) {
/* 149 */     if (this.isClosed)
/* 150 */       throw new IllegalStateException("Can't use a closed render pass"); 
/* 152 */     if (firstInstance != 0 && !this.deviceFeatures.nonZeroFirstInstance())
/* 153 */       throw new UnsupportedOperationException("firstInstance must be zero on when device does not support nonZeroFirstInstance"); 
/* 155 */     this.backend.drawIndexed(indexCount, instanceCount, firstIndex, vertexOffset, firstInstance);
/*     */   }
/*     */   
/*     */   public void multiDrawIndexed(IntBuffer drawParameters, int instanceCount, int firstInstance, int drawCount) {
/* 159 */     if (this.isClosed)
/* 160 */       throw new IllegalStateException("Can't use a closed render pass"); 
/* 162 */     if (!this.deviceFeatures.multiDrawDirectInterleaved())
/* 163 */       throw new UnsupportedOperationException("device does not support multiDrawDirectInterleaved"); 
/* 165 */     if (firstInstance != 0 && !this.deviceFeatures.nonZeroFirstInstance())
/* 166 */       throw new UnsupportedOperationException("firstInstance must be zero on when device does not support nonZeroFirstInstance"); 
/* 168 */     if (drawCount > this.deviceLimits.maxMultiDrawDirectInterleavedDrawCount())
/* 169 */       throw new IllegalArgumentException("May not exceed maxMultiDrawDirectInterleavedDrawCount draws in a single multiDrawDirectInterleaved call"); 
/* 171 */     if (drawParameters.remaining() < drawCount * 3)
/* 172 */       throw new IllegalArgumentException("Not enough elements in drawParameters for drawCount draws"); 
/* 174 */     this.backend.multiDrawIndexed(drawParameters, instanceCount, firstInstance, drawCount);
/*     */   }
/*     */   
/*     */   public void multiDrawIndexed(PointerBuffer firstIndexOffsets, IntBuffer indexCounts, IntBuffer vertexOffsets, int drawCount) {
/* 178 */     if (this.isClosed)
/* 179 */       throw new IllegalStateException("Can't use a closed render pass"); 
/* 181 */     if (!this.deviceFeatures.multiDrawDirectSeparate())
/* 182 */       throw new UnsupportedOperationException("device does not support multiDrawDirectSeparate"); 
/* 184 */     if (firstIndexOffsets.remaining() < drawCount)
/* 185 */       throw new IllegalArgumentException("firstIndexOffsets does not contain enough elements for drawCount draws"); 
/* 187 */     if (indexCounts.remaining() < drawCount)
/* 188 */       throw new IllegalArgumentException("indexCounts does not contain enough elements for drawCount draws"); 
/* 190 */     if (vertexOffsets.remaining() < drawCount)
/* 191 */       throw new IllegalArgumentException("vertexOffsets does not contain enough elements for drawCount draws"); 
/* 193 */     this.backend.multiDrawIndexed(firstIndexOffsets, indexCounts, vertexOffsets, drawCount);
/*     */   }
/*     */   
/*     */   public void drawIndexedIndirect(GpuBufferSlice commands, int drawCount) {
/* 197 */     if (this.isClosed)
/* 198 */       throw new IllegalStateException("Can't use a closed render pass"); 
/* 200 */     if (!this.deviceFeatures.drawIndirect())
/* 201 */       throw new UnsupportedOperationException("device does not support drawIndirect"); 
/* 203 */     if (drawCount > 1 && !this.deviceFeatures.multiDrawIndirect())
/* 204 */       throw new UnsupportedOperationException("drawCount must be one when device does not support multiDrawIndirect"); 
/* 206 */     if ((commands.buffer().usage() & 0x200) == 0)
/* 207 */       throw new IllegalArgumentException("Indirect commands buffer must have GpuBuffer.USAGE_INDIRECT_PARAMETERS flag"); 
/* 209 */     if (commands.length() < drawCount * VkDrawIndexedIndirectCommand.SIZEOF)
/* 210 */       throw new IllegalArgumentException("Commands buffer is not large enough to hold requested draw count at the given offset"); 
/* 212 */     if (commands.offset() % 4L != 0L)
/* 213 */       throw new IllegalArgumentException("Commands offset must be multiple of 4"); 
/* 215 */     this.backend.drawIndexedIndirect(commands, drawCount);
/*     */   }
/*     */   
/*     */   public <T> void drawMultipleIndexed(Collection<Draw<T>> draws, GpuBuffer defaultIndexBuffer, IndexType defaultIndexType, Collection<String> dynamicUniforms, T uniformArgument) {
/* 219 */     if (this.isClosed)
/* 220 */       throw new IllegalStateException("Can't use a closed render pass"); 
/* 222 */     this.backend.drawMultipleIndexed(draws, defaultIndexBuffer, defaultIndexType, dynamicUniforms, uniformArgument);
/*     */   }
/*     */   
/*     */   public void draw(int vertexCount, int instanceCount, int firstVertex, int firstInstance) {
/* 232 */     if (this.isClosed)
/* 233 */       throw new IllegalStateException("Can't use a closed render pass"); 
/* 235 */     if (firstInstance != 0 && !this.deviceFeatures.nonZeroFirstInstance())
/* 236 */       throw new UnsupportedOperationException("firstInstance must be zero on when device does not support nonZeroFirstInstance"); 
/* 238 */     this.backend.draw(vertexCount, instanceCount, firstVertex, firstInstance);
/*     */   }
/*     */   
/*     */   public void multiDraw(IntBuffer drawParameters, int instanceCount, int firstInstance, int drawCount) {
/* 242 */     if (this.isClosed)
/* 243 */       throw new IllegalStateException("Can't use a closed render pass"); 
/* 245 */     if (!this.deviceFeatures.multiDrawDirectInterleaved())
/* 246 */       throw new UnsupportedOperationException("device does not support multiDrawDirectInterleaved"); 
/* 248 */     if (firstInstance != 0 && !this.deviceFeatures.nonZeroFirstInstance())
/* 249 */       throw new UnsupportedOperationException("firstInstance must be zero on when device does not support nonZeroFirstInstance"); 
/* 251 */     if (drawCount > this.deviceLimits.maxMultiDrawDirectInterleavedDrawCount())
/* 252 */       throw new IllegalArgumentException("May not exceed maxMultiDrawDirectInterleavedDrawCount draws in a single multiDrawDirectInterleaved call"); 
/* 254 */     if (drawParameters.remaining() < drawCount * 2)
/* 255 */       throw new IllegalArgumentException("Not enough elements in drawParameters for drawCount draws"); 
/* 257 */     this.backend.multiDraw(drawParameters, instanceCount, firstInstance, drawCount);
/*     */   }
/*     */   
/*     */   public void multiDraw(IntBuffer firstVertices, IntBuffer vertexCounts, int drawCount) {
/* 261 */     if (this.isClosed)
/* 262 */       throw new IllegalStateException("Can't use a closed render pass"); 
/* 264 */     if (!this.deviceFeatures.multiDrawDirectSeparate())
/* 265 */       throw new UnsupportedOperationException("device does not support multiDrawDirectSeparate"); 
/* 267 */     if (firstVertices.remaining() < drawCount)
/* 268 */       throw new IllegalArgumentException("firstVertices does not contain enough elements for drawCount draws"); 
/* 270 */     if (vertexCounts.remaining() < drawCount)
/* 271 */       throw new IllegalArgumentException("vertexCounts does not contain enough elements for drawCount draws"); 
/* 273 */     this.backend.multiDraw(firstVertices, vertexCounts, drawCount);
/*     */   }
/*     */   
/*     */   public void drawIndirect(GpuBufferSlice commands, int drawCount) {
/* 277 */     if (this.isClosed)
/* 278 */       throw new IllegalStateException("Can't use a closed render pass"); 
/* 280 */     if (!this.deviceFeatures.drawIndirect())
/* 281 */       throw new UnsupportedOperationException("device does not support drawIndirect"); 
/* 283 */     if (drawCount > 1 && !this.deviceFeatures.multiDrawIndirect())
/* 284 */       throw new UnsupportedOperationException("drawCount must be one when device does not support multiDrawIndirect"); 
/* 286 */     if ((commands.buffer().usage() & 0x200) == 0)
/* 287 */       throw new IllegalArgumentException("Indirect commands buffer must have GpuBuffer.USAGE_INDIRECT_PARAMETERS flag"); 
/* 289 */     if (commands.length() < drawCount * VkDrawIndirectCommand.SIZEOF)
/* 290 */       throw new IllegalArgumentException("Commands buffer is not large enough to hold requested draw count at the given offset"); 
/* 292 */     if (commands.offset() % 4L != 0L)
/* 293 */       throw new IllegalArgumentException("Commands offset must be multiple of 4"); 
/* 295 */     this.backend.drawIndirect(commands, drawCount);
/*     */   }
/*     */   
/*     */   public void close() {
/* 300 */     if (!this.isClosed) {
/* 301 */       this.isClosed = true;
/* 302 */       if (this.pushedDebugGroups > 0)
/* 303 */         throw new IllegalStateException("Render pass had debug groups left open!"); 
/* 305 */       this.onFinish.run();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface UniformUploader {
/*     */     void upload(String param1String, GpuBufferSlice param1GpuBufferSlice);
/*     */   }
/*     */   
/*     */   public static final class Draw<T> extends Record {
/*     */     private final int slot;
/*     */     
/*     */     private final GpuBuffer vertexBuffer;
/*     */     
/*     */     private final GpuBuffer indexBuffer;
/*     */     
/*     */     private final IndexType indexType;
/*     */     
/*     */     private final int firstIndex;
/*     */     
/*     */     private final int indexCount;
/*     */     
/*     */     private final int baseVertex;
/*     */     
/*     */     private final BiConsumer<T, RenderPass.UniformUploader> uniformUploaderConsumer;
/*     */     
/*     */     public BiConsumer<T, RenderPass.UniformUploader> uniformUploaderConsumer() {
/* 313 */       return this.uniformUploaderConsumer;
/*     */     }
/*     */     
/*     */     public int baseVertex() {
/* 313 */       return this.baseVertex;
/*     */     }
/*     */     
/*     */     public int indexCount() {
/* 313 */       return this.indexCount;
/*     */     }
/*     */     
/*     */     public int firstIndex() {
/* 313 */       return this.firstIndex;
/*     */     }
/*     */     
/*     */     public IndexType indexType() {
/* 313 */       return this.indexType;
/*     */     }
/*     */     
/*     */     public GpuBuffer indexBuffer() {
/* 313 */       return this.indexBuffer;
/*     */     }
/*     */     
/*     */     public GpuBuffer vertexBuffer() {
/* 313 */       return this.vertexBuffer;
/*     */     }
/*     */     
/*     */     public int slot() {
/* 313 */       return this.slot;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/RenderPass$Draw;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #313	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/RenderPass$Draw;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #313	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw<TT;>;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/RenderPass$Draw;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #313	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw<TT;>;
/*     */     }
/*     */     
/*     */     public Draw(int slot, GpuBuffer vertexBuffer, GpuBuffer indexBuffer, IndexType indexType, int firstIndex, int indexCount, int baseVertex, BiConsumer<T, RenderPass.UniformUploader> uniformUploaderConsumer) {
/* 313 */       this.slot = slot;
/* 313 */       this.vertexBuffer = vertexBuffer;
/* 313 */       this.indexBuffer = indexBuffer;
/* 313 */       this.indexType = indexType;
/* 313 */       this.firstIndex = firstIndex;
/* 313 */       this.indexCount = indexCount;
/* 313 */       this.baseVertex = baseVertex;
/* 313 */       this.uniformUploaderConsumer = uniformUploaderConsumer;
/*     */     }
/*     */     
/*     */     public Draw(int slot, GpuBuffer vertexBuffer, GpuBuffer indexBuffer, IndexType indexType, int firstIndex, int indexCount, int baseVertex) {
/* 315 */       this(slot, vertexBuffer, indexBuffer, indexType, firstIndex, indexCount, baseVertex, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class RenderArea extends Record {
/*     */     private final int x;
/*     */     
/*     */     private final int y;
/*     */     
/*     */     private final int width;
/*     */     
/*     */     private final int height;
/*     */     
/*     */     public RenderArea(int x, int y, int width, int height) {
/* 319 */       this.x = x;
/* 319 */       this.y = y;
/* 319 */       this.width = width;
/* 319 */       this.height = height;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/RenderPass$RenderArea;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #319	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$RenderArea;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/RenderPass$RenderArea;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #319	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$RenderArea;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/RenderPass$RenderArea;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #319	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderPass$RenderArea;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int x() {
/* 319 */       return this.x;
/*     */     }
/*     */     
/*     */     public int y() {
/* 319 */       return this.y;
/*     */     }
/*     */     
/*     */     public int width() {
/* 319 */       return this.width;
/*     */     }
/*     */     
/*     */     public int height() {
/* 319 */       return this.height;
/*     */     }
/*     */     
/*     */     public boolean fillsTexture(GpuTextureView texture) {
/* 321 */       return (this.x == 0 && this.y == 0 && this.width == texture.getWidth(0) && this.height == texture.getHeight(0));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\RenderPass.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */