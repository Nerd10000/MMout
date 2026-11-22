/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.systems.GpuQueryPool;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderPassBackend;
/*     */ import com.mojang.blaze3d.systems.ScissorState;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ 
/*     */ class GlRenderPass implements RenderPassBackend {
/*  27 */   public static final boolean VALIDATION = SharedConstants.IS_RUNNING_IN_IDE;
/*     */   
/*     */   private final GlCommandEncoder encoder;
/*     */   
/*     */   private final GlDevice device;
/*     */   
/*     */   private final boolean hasDepthTexture;
/*     */   
/*     */   private final ScissorState defaultScissorState;
/*     */   
/*     */   protected GlRenderPipeline pipeline;
/*     */   
/*  36 */   protected final GpuBufferSlice[] vertexBuffers = new GpuBufferSlice[16];
/*     */   
/*     */   protected boolean vertexBufferDirty = true;
/*     */   
/*     */   protected GpuBuffer indexBuffer;
/*     */   
/*  41 */   protected IndexType indexType = IndexType.INT;
/*     */   
/*  43 */   private final ScissorState scissorState = new ScissorState();
/*     */   
/*  47 */   protected final HashMap<String, GpuBufferSlice> uniforms = new HashMap<>();
/*     */   
/*  48 */   protected final HashMap<String, TextureViewAndSampler> samplers = new HashMap<>();
/*     */   
/*  49 */   protected final Set<String> dirtyUniforms = new HashSet<>();
/*     */   
/*     */   protected final int colorAttachmentCount;
/*     */   
/*     */   public GlRenderPass(GlCommandEncoder encoder, GlDevice device, boolean hasDepthTexture, int colorAttachmentCount, ScissorState defaultScissorState) {
/*  54 */     this.encoder = encoder;
/*  55 */     this.device = device;
/*  56 */     this.hasDepthTexture = hasDepthTexture;
/*  57 */     this.colorAttachmentCount = colorAttachmentCount;
/*  59 */     this.defaultScissorState = defaultScissorState;
/*  60 */     this.scissorState.setFrom(defaultScissorState);
/*     */   }
/*     */   
/*     */   public boolean hasDepthTexture() {
/*  64 */     return this.hasDepthTexture;
/*     */   }
/*     */   
/*     */   public void pushDebugGroup(Supplier<String> label) {
/*  69 */     this.device.debugLabels().pushDebugGroup(label);
/*     */   }
/*     */   
/*     */   public void popDebugGroup() {
/*  74 */     this.device.debugLabels().popDebugGroup();
/*     */   }
/*     */   
/*     */   public void setPipeline(RenderPipeline pipeline) {
/*  79 */     if (this.pipeline == null || this.pipeline.info() != pipeline) {
/*  80 */       this.dirtyUniforms.addAll(this.uniforms.keySet());
/*  81 */       this.dirtyUniforms.addAll(this.samplers.keySet());
/*     */     } 
/*  83 */     this.pipeline = this.device.getOrCompilePipeline(pipeline);
/*     */   }
/*     */   
/*     */   public void bindTexture(String name, GpuTextureView textureView, GpuSampler sampler) {
/*  88 */     if (sampler == null) {
/*  89 */       this.samplers.remove(name);
/*     */     } else {
/*  91 */       this.samplers.put(name, new TextureViewAndSampler((GlTextureView)textureView, (GlSampler)sampler));
/*     */     } 
/*  93 */     this.dirtyUniforms.add(name);
/*     */   }
/*     */   
/*     */   public void setUniform(String name, GpuBuffer value) {
/*  98 */     this.uniforms.put(name, value.slice());
/*  99 */     this.dirtyUniforms.add(name);
/*     */   }
/*     */   
/*     */   public void setUniform(String name, GpuBufferSlice value) {
/* 104 */     this.uniforms.put(name, value);
/* 105 */     this.dirtyUniforms.add(name);
/*     */   }
/*     */   
/*     */   public void enableScissor(int x, int y, int width, int height) {
/* 110 */     this.scissorState.enable(x, y, width, height);
/*     */   }
/*     */   
/*     */   public void disableScissor() {
/* 115 */     this.scissorState.setFrom(this.defaultScissorState);
/*     */   }
/*     */   
/*     */   public boolean isScissorEnabled() {
/* 119 */     return this.scissorState.enabled();
/*     */   }
/*     */   
/*     */   public int getScissorX() {
/* 123 */     return this.scissorState.x();
/*     */   }
/*     */   
/*     */   public int getScissorY() {
/* 127 */     return this.scissorState.y();
/*     */   }
/*     */   
/*     */   public int getScissorWidth() {
/* 131 */     return this.scissorState.width();
/*     */   }
/*     */   
/*     */   public int getScissorHeight() {
/* 135 */     return this.scissorState.height();
/*     */   }
/*     */   
/*     */   public void setVertexBuffer(int slot, GpuBufferSlice vertexBuffer) {
/* 140 */     GpuBuffer inputBuffer = (vertexBuffer != null) ? vertexBuffer.buffer() : null;
/* 141 */     GpuBuffer existingBuffer = (this.vertexBuffers[slot] != null) ? this.vertexBuffers[slot].buffer() : null;
/* 142 */     long inputOffset = (vertexBuffer != null) ? vertexBuffer.offset() : 0L;
/* 143 */     long exitingOffset = (this.vertexBuffers[slot] != null) ? this.vertexBuffers[slot].offset() : 0L;
/* 144 */     this.vertexBufferDirty |= (inputBuffer != existingBuffer || inputOffset != exitingOffset) ? 1 : 0;
/* 145 */     this.vertexBuffers[slot] = vertexBuffer;
/*     */   }
/*     */   
/*     */   public void setIndexBuffer(GpuBuffer indexBuffer, IndexType indexType) {
/* 150 */     this.indexBuffer = indexBuffer;
/* 151 */     this.indexType = indexType;
/*     */   }
/*     */   
/*     */   public void drawIndexed(int indexCount, int instanceCount, int firstIndex, int vertexOffset, int firstInstance) {
/* 156 */     this.encoder.executeDraw(this, vertexOffset, firstIndex, indexCount, this.indexType, instanceCount, firstInstance);
/*     */   }
/*     */   
/*     */   public void multiDrawIndexed(IntBuffer drawParameters, int instanceCount, int firstInstance, int drawCount) {
/* 161 */     throw new UnsupportedOperationException("OpenGL does not support the multiDrawDirectInterleaved device feature");
/*     */   }
/*     */   
/*     */   public void multiDrawIndexed(PointerBuffer firstIndexOffsets, IntBuffer indexCounts, IntBuffer vertexOffsets, int drawCount) {
/* 166 */     this.encoder.executeDraws(this, this.indexType, firstIndexOffsets, indexCounts, vertexOffsets, drawCount);
/*     */   }
/*     */   
/*     */   public void drawIndexedIndirect(GpuBufferSlice commands, int drawCount) {
/* 171 */     this.encoder.executeDrawIndirect(this, this.indexType, (GlBuffer)commands.buffer(), commands.offset(), drawCount);
/*     */   }
/*     */   
/*     */   public <T> void drawMultipleIndexed(Collection<RenderPass.Draw<T>> draws, GpuBuffer defaultIndexBuffer, IndexType defaultIndexType, Collection<String> dynamicUniforms, T uniformArgument) {
/* 176 */     this.encoder.executeDrawMultiple(this, draws, defaultIndexBuffer, defaultIndexType, dynamicUniforms, uniformArgument);
/*     */   }
/*     */   
/*     */   public void draw(int vertexCount, int instanceCount, int firstVertex, int firstInstance) {
/* 181 */     this.encoder.executeDraw(this, firstVertex, 0, vertexCount, null, instanceCount, firstInstance);
/*     */   }
/*     */   
/*     */   public void multiDraw(IntBuffer drawParameters, int instanceCount, int firstInstance, int drawCount) {
/* 186 */     throw new UnsupportedOperationException("OpenGL does not support the multiDrawDirectInterleaved device feature");
/*     */   }
/*     */   
/*     */   public void multiDraw(IntBuffer firstVertices, IntBuffer vertexCounts, int drawCount) {
/* 191 */     this.encoder.executeDraws(this, null, null, vertexCounts, firstVertices, drawCount);
/*     */   }
/*     */   
/*     */   public void drawIndirect(GpuBufferSlice commands, int drawCount) {
/* 196 */     this.encoder.executeDrawIndirect(this, null, (GlBuffer)commands.buffer(), commands.offset(), drawCount);
/*     */   }
/*     */   
/*     */   public void writeTimestamp(GpuQueryPool pool, int index) {
/* 201 */     ((GlQueryPool)pool).writeTimestamp(index);
/*     */   }
/*     */   
/*     */   protected static final class TextureViewAndSampler extends Record {
/*     */     private final GlTextureView view;
/*     */     
/*     */     private final GlSampler sampler;
/*     */     
/*     */     protected TextureViewAndSampler(GlTextureView view, GlSampler sampler) {
/* 204 */       this.view = view;
/* 204 */       this.sampler = sampler;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #204	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #204	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #204	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public GlTextureView view() {
/* 204 */       return this.view;
/*     */     }
/*     */     
/*     */     public GlSampler sampler() {
/* 204 */       return this.sampler;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlRenderPass.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */