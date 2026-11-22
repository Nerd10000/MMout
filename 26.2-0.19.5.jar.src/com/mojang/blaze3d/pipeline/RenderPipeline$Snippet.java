/*     */ package com.mojang.blaze3d.pipeline;
/*     */ 
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.platform.PolygonMode;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.renderer.ShaderDefines;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public final class Snippet extends Record {
/*     */   private final Optional<Identifier> vertexShader;
/*     */   
/*     */   private final Optional<Identifier> fragmentShader;
/*     */   
/*     */   private final Optional<ShaderDefines> shaderDefines;
/*     */   
/*     */   private final Optional<List<BindGroupLayout>> bindGroupLayouts;
/*     */   
/*     */   private final ColorTargetState[] colorTargetStates;
/*     */   
/*     */   private final int activeColorTargetStateCount;
/*     */   
/*     */   private final Optional<DepthStencilState> depthStencilState;
/*     */   
/*     */   private final Optional<PolygonMode> polygonMode;
/*     */   
/*     */   private final Optional<Boolean> cull;
/*     */   
/*     */   private final VertexFormat[] vertexFormatPerBuffer;
/*     */   
/*     */   private final Optional<PrimitiveTopology> vertexFormatMode;
/*     */   
/*     */   public Snippet(Optional<Identifier> vertexShader, Optional<Identifier> fragmentShader, Optional<ShaderDefines> shaderDefines, Optional<List<BindGroupLayout>> bindGroupLayouts, ColorTargetState[] colorTargetStates, int activeColorTargetStateCount, Optional<DepthStencilState> depthStencilState, Optional<PolygonMode> polygonMode, Optional<Boolean> cull, VertexFormat[] vertexFormatPerBuffer, Optional<PrimitiveTopology> vertexFormatMode) {
/* 411 */     this.vertexShader = vertexShader;
/* 411 */     this.fragmentShader = fragmentShader;
/* 411 */     this.shaderDefines = shaderDefines;
/* 411 */     this.bindGroupLayouts = bindGroupLayouts;
/* 411 */     this.colorTargetStates = colorTargetStates;
/* 411 */     this.activeColorTargetStateCount = activeColorTargetStateCount;
/* 411 */     this.depthStencilState = depthStencilState;
/* 411 */     this.polygonMode = polygonMode;
/* 411 */     this.cull = cull;
/* 411 */     this.vertexFormatPerBuffer = vertexFormatPerBuffer;
/* 411 */     this.vertexFormatMode = vertexFormatMode;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #411	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #411	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #411	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<Identifier> vertexShader() {
/* 411 */     return this.vertexShader;
/*     */   }
/*     */   
/*     */   public Optional<Identifier> fragmentShader() {
/* 411 */     return this.fragmentShader;
/*     */   }
/*     */   
/*     */   public Optional<ShaderDefines> shaderDefines() {
/* 411 */     return this.shaderDefines;
/*     */   }
/*     */   
/*     */   public Optional<List<BindGroupLayout>> bindGroupLayouts() {
/* 411 */     return this.bindGroupLayouts;
/*     */   }
/*     */   
/*     */   public ColorTargetState[] colorTargetStates() {
/* 411 */     return this.colorTargetStates;
/*     */   }
/*     */   
/*     */   public int activeColorTargetStateCount() {
/* 411 */     return this.activeColorTargetStateCount;
/*     */   }
/*     */   
/*     */   public Optional<DepthStencilState> depthStencilState() {
/* 411 */     return this.depthStencilState;
/*     */   }
/*     */   
/*     */   public Optional<PolygonMode> polygonMode() {
/* 411 */     return this.polygonMode;
/*     */   }
/*     */   
/*     */   public Optional<Boolean> cull() {
/* 411 */     return this.cull;
/*     */   }
/*     */   
/*     */   public VertexFormat[] vertexFormatPerBuffer() {
/* 411 */     return this.vertexFormatPerBuffer;
/*     */   }
/*     */   
/*     */   public Optional<PrimitiveTopology> vertexFormatMode() {
/* 411 */     return this.vertexFormatMode;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\pipeline\RenderPipeline$Snippet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */