/*     */ package com.mojang.blaze3d.pipeline;
/*     */ 
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.platform.PolygonMode;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.renderer.ShaderDefines;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class RenderPipeline {
/*     */   private final Identifier location;
/*     */   
/*     */   private final Identifier vertexShader;
/*     */   
/*     */   private final Identifier fragmentShader;
/*     */   
/*     */   private final ShaderDefines shaderDefines;
/*     */   
/*     */   private final List<BindGroupLayout> bindGroupLayouts;
/*     */   
/*     */   private final DepthStencilState depthStencilState;
/*     */   
/*     */   private final PolygonMode polygonMode;
/*     */   
/*     */   private final boolean cull;
/*     */   
/*     */   private final ColorTargetState[] colorTargetStates;
/*     */   
/*  29 */   private final VertexFormat[] vertexFormatPerBuffer = new VertexFormat[16];
/*     */   
/*     */   private final PrimitiveTopology primitiveTopology;
/*     */   
/*     */   private final int sortKey;
/*     */   
/*     */   private static int sortKeySeed;
/*     */   
/*     */   protected RenderPipeline(Identifier location, Identifier vertexShader, Identifier fragmentShader, ShaderDefines shaderDefines, List<BindGroupLayout> bindGroupLayouts, ColorTargetState[] colorTargetStates, DepthStencilState depthStencilState, PolygonMode polygonMode, boolean cull, VertexFormat[] vertexFormatPerBuffer, PrimitiveTopology primitiveTopology, int sortKey) {
/*  49 */     this.location = location;
/*  50 */     this.vertexShader = vertexShader;
/*  51 */     this.fragmentShader = fragmentShader;
/*  52 */     this.shaderDefines = shaderDefines;
/*  53 */     this.bindGroupLayouts = bindGroupLayouts;
/*  54 */     this.depthStencilState = depthStencilState;
/*  55 */     this.polygonMode = polygonMode;
/*  56 */     this.cull = cull;
/*  57 */     this.colorTargetStates = colorTargetStates;
/*  58 */     this.primitiveTopology = primitiveTopology;
/*  59 */     this.sortKey = sortKey;
/*  60 */     System.arraycopy(vertexFormatPerBuffer, 0, this.vertexFormatPerBuffer, 0, this.vertexFormatPerBuffer.length);
/*     */   }
/*     */   
/*     */   public int getSortKey() {
/*  64 */     return SharedConstants.DEBUG_SHUFFLE_UI_RENDERING_ORDER ? (hashCode() * (sortKeySeed + 1)) : this.sortKey;
/*     */   }
/*     */   
/*     */   public static void updateSortKeySeed() {
/*  68 */     sortKeySeed = Math.round(100000.0F * (float)Math.random());
/*     */   }
/*     */   
/*     */   public String toString() {
/*  73 */     return this.location.toString();
/*     */   }
/*     */   
/*     */   public PolygonMode getPolygonMode() {
/*  77 */     return this.polygonMode;
/*     */   }
/*     */   
/*     */   public boolean isCull() {
/*  81 */     return this.cull;
/*     */   }
/*     */   
/*     */   public ColorTargetState[] getColorTargetStates() {
/*  85 */     return this.colorTargetStates;
/*     */   }
/*     */   
/*     */   public ColorTargetState getColorTargetState() {
/*  89 */     return this.colorTargetStates[0];
/*     */   }
/*     */   
/*     */   public DepthStencilState getDepthStencilState() {
/*  93 */     return this.depthStencilState;
/*     */   }
/*     */   
/*     */   public Identifier getLocation() {
/*  97 */     return this.location;
/*     */   }
/*     */   
/*     */   public VertexFormat[] getVertexFormatBindings() {
/* 101 */     return this.vertexFormatPerBuffer;
/*     */   }
/*     */   
/*     */   public VertexFormat getVertexFormatBinding(int bindingIndex) {
/* 105 */     return this.vertexFormatPerBuffer[bindingIndex];
/*     */   }
/*     */   
/*     */   public PrimitiveTopology getPrimitiveTopology() {
/* 109 */     return this.primitiveTopology;
/*     */   }
/*     */   
/*     */   public Identifier getVertexShader() {
/* 113 */     return this.vertexShader;
/*     */   }
/*     */   
/*     */   public Identifier getFragmentShader() {
/* 117 */     return this.fragmentShader;
/*     */   }
/*     */   
/*     */   public ShaderDefines getShaderDefines() {
/* 121 */     return this.shaderDefines;
/*     */   }
/*     */   
/*     */   public List<BindGroupLayout> getBindGroupLayouts() {
/* 125 */     return this.bindGroupLayouts;
/*     */   }
/*     */   
/*     */   public boolean wantsDepthTexture() {
/* 129 */     return (this.depthStencilState != null);
/*     */   }
/*     */   
/*     */   public static Builder builder(Snippet... snippets) {
/* 137 */     Builder builder = new Builder();
/* 139 */     for (Snippet snippet : snippets)
/* 140 */       builder.withSnippet(snippet); 
/* 142 */     return builder;
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private static int nextPipelineSortKey;
/*     */     
/* 147 */     private Optional<Identifier> location = Optional.empty();
/*     */     
/* 148 */     private Optional<Identifier> fragmentShader = Optional.empty();
/*     */     
/* 149 */     private Optional<Identifier> vertexShader = Optional.empty();
/*     */     
/* 150 */     private Optional<ShaderDefines.Builder> definesBuilder = Optional.empty();
/*     */     
/* 151 */     private Optional<List<BindGroupLayout>> bindGroupLayouts = Optional.empty();
/*     */     
/* 152 */     private Optional<DepthStencilState> depthStencilState = Optional.empty();
/*     */     
/* 153 */     private Optional<PolygonMode> polygonMode = Optional.empty();
/*     */     
/* 154 */     private Optional<Boolean> cull = Optional.empty();
/*     */     
/* 155 */     private final ColorTargetState[] colorTargetStates = new ColorTargetState[8];
/*     */     
/*     */     private int activeColorTargetStateCount;
/*     */     
/* 157 */     private final VertexFormat[] vertexFormatPerBuffer = new VertexFormat[16];
/*     */     
/* 158 */     private Optional<PrimitiveTopology> primitiveTopology = Optional.empty();
/*     */     
/*     */     public Builder withLocation(String location) {
/* 164 */       this.location = Optional.of(Identifier.withDefaultNamespace(location));
/* 165 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withLocation(Identifier location) {
/* 169 */       this.location = Optional.of(location);
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withFragmentShader(String fragmentShader) {
/* 174 */       this.fragmentShader = Optional.of(Identifier.withDefaultNamespace(fragmentShader));
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withFragmentShader(Identifier fragmentShader) {
/* 179 */       this.fragmentShader = Optional.of(fragmentShader);
/* 180 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withVertexShader(String vertexShader) {
/* 184 */       this.vertexShader = Optional.of(Identifier.withDefaultNamespace(vertexShader));
/* 185 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withVertexShader(Identifier vertexShader) {
/* 189 */       this.vertexShader = Optional.of(vertexShader);
/* 190 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withShaderDefine(String key) {
/* 194 */       if (this.definesBuilder.isEmpty())
/* 195 */         this.definesBuilder = Optional.of(ShaderDefines.builder()); 
/* 197 */       ((ShaderDefines.Builder)this.definesBuilder.get()).define(key);
/* 198 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withShaderDefine(String key, int value) {
/* 202 */       if (this.definesBuilder.isEmpty())
/* 203 */         this.definesBuilder = Optional.of(ShaderDefines.builder()); 
/* 205 */       ((ShaderDefines.Builder)this.definesBuilder.get()).define(key, value);
/* 206 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withShaderDefine(String key, float value) {
/* 210 */       if (this.definesBuilder.isEmpty())
/* 211 */         this.definesBuilder = Optional.of(ShaderDefines.builder()); 
/* 213 */       ((ShaderDefines.Builder)this.definesBuilder.get()).define(key, value);
/* 214 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withBindGroupLayout(BindGroupLayout bindGroupLayout) {
/* 218 */       if (this.bindGroupLayouts.isEmpty())
/* 219 */         this.bindGroupLayouts = Optional.of(new ArrayList<>()); 
/* 221 */       ((List<BindGroupLayout>)this.bindGroupLayouts.get()).add(bindGroupLayout);
/* 222 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withPolygonMode(PolygonMode polygonMode) {
/* 226 */       this.polygonMode = Optional.of(polygonMode);
/* 227 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withCull(boolean cull) {
/* 231 */       this.cull = Optional.of(Boolean.valueOf(cull));
/* 232 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withColorTargetState(int index, ColorTargetState colorTargetState) {
/* 236 */       this.colorTargetStates[index] = colorTargetState;
/* 237 */       this.activeColorTargetStateCount = Math.max(this.activeColorTargetStateCount, index + 1);
/* 238 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withUnusedColorTargetState(int index) {
/* 242 */       this.colorTargetStates[index] = null;
/* 243 */       this.activeColorTargetStateCount = Math.max(this.activeColorTargetStateCount, index + 1);
/* 244 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withColorTargetState(ColorTargetState colorTargetState) {
/* 248 */       return withColorTargetState(0, colorTargetState);
/*     */     }
/*     */     
/*     */     public Builder withDepthStencilState(DepthStencilState depthStencilState) {
/* 252 */       this.depthStencilState = Optional.of(depthStencilState);
/* 253 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withDepthStencilState(Optional<DepthStencilState> depthStencilState) {
/* 257 */       this.depthStencilState = depthStencilState;
/* 258 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withVertexBinding(int bindingIndex, VertexFormat vertexFormat) {
/* 262 */       this.vertexFormatPerBuffer[bindingIndex] = vertexFormat;
/* 263 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withPrimitiveTopology(PrimitiveTopology primitiveTopology) {
/* 267 */       this.primitiveTopology = Optional.of(primitiveTopology);
/* 268 */       return this;
/*     */     }
/*     */     
/*     */     private void withSnippet(RenderPipeline.Snippet snippet) {
/* 272 */       if (snippet.vertexShader.isPresent())
/* 273 */         this.vertexShader = snippet.vertexShader; 
/* 275 */       if (snippet.fragmentShader.isPresent())
/* 276 */         this.fragmentShader = snippet.fragmentShader; 
/* 278 */       if (snippet.shaderDefines.isPresent()) {
/* 279 */         if (this.definesBuilder.isEmpty())
/* 280 */           this.definesBuilder = Optional.of(ShaderDefines.builder()); 
/* 282 */         ShaderDefines snippetDefines = snippet.shaderDefines.get();
/* 283 */         for (Map.Entry<String, String> snippetValue : (Iterable<Map.Entry<String, String>>)snippetDefines.values().entrySet())
/* 284 */           ((ShaderDefines.Builder)this.definesBuilder.get()).define(snippetValue.getKey(), snippetValue.getValue()); 
/* 286 */         for (String flag : snippetDefines.flags())
/* 287 */           ((ShaderDefines.Builder)this.definesBuilder.get()).define(flag); 
/*     */       } 
/* 290 */       snippet.bindGroupLayouts.ifPresent(snippetLayouts -> {
/*     */             if (this.bindGroupLayouts.isPresent()) {
/*     */               ((List)this.bindGroupLayouts.get()).addAll(snippetLayouts);
/*     */             } else {
/*     */               this.bindGroupLayouts = Optional.of(new ArrayList<>(snippetLayouts));
/*     */             } 
/*     */           });
/* 299 */       if (snippet.depthStencilState.isPresent())
/* 300 */         this.depthStencilState = snippet.depthStencilState; 
/* 303 */       if (snippet.cull.isPresent())
/* 304 */         this.cull = snippet.cull; 
/*     */       int i;
/* 307 */       for (i = 0; i < snippet.activeColorTargetStateCount; i++) {
/* 308 */         if (this.colorTargetStates[i] == null && snippet.colorTargetStates[i] != null)
/* 309 */           this.colorTargetStates[i] = snippet.colorTargetStates[i]; 
/*     */       } 
/* 312 */       this.activeColorTargetStateCount = Math.max(this.activeColorTargetStateCount, snippet.activeColorTargetStateCount);
/* 314 */       for (i = 0; i < snippet.vertexFormatPerBuffer.length; i++) {
/* 315 */         VertexFormat vertexFormat = snippet.vertexFormatPerBuffer[i];
/* 316 */         if (vertexFormat != null)
/* 317 */           this.vertexFormatPerBuffer[i] = vertexFormat; 
/*     */       } 
/* 321 */       if (snippet.vertexFormatMode.isPresent())
/* 322 */         this.primitiveTopology = snippet.vertexFormatMode; 
/* 325 */       if (snippet.polygonMode.isPresent())
/* 326 */         this.polygonMode = snippet.polygonMode; 
/*     */     }
/*     */     
/*     */     public RenderPipeline.Snippet buildSnippet() {
/* 331 */       return new RenderPipeline.Snippet(this.vertexShader, this.fragmentShader, this.definesBuilder
/*     */           
/* 334 */           .map(ShaderDefines.Builder::build), this.bindGroupLayouts
/* 335 */           .map(Collections::unmodifiableList), this.colorTargetStates, this.activeColorTargetStateCount, this.depthStencilState, this.polygonMode, this.cull, this.vertexFormatPerBuffer, this.primitiveTopology);
/*     */     }
/*     */     
/*     */     public RenderPipeline build() {
/*     */       ColorTargetState[] activeColorTargetStates;
/* 347 */       if (this.location.isEmpty())
/* 348 */         throw new IllegalStateException("Missing location"); 
/* 350 */       if (this.vertexShader.isEmpty())
/* 351 */         throw new IllegalStateException("Missing vertex shader"); 
/* 353 */       if (this.fragmentShader.isEmpty())
/* 354 */         throw new IllegalStateException("Missing fragment shader"); 
/* 356 */       if (this.primitiveTopology.isEmpty())
/* 357 */         throw new IllegalStateException("Missing primitive topology"); 
/* 361 */       if (this.activeColorTargetStateCount == 0) {
/* 362 */         activeColorTargetStates = new ColorTargetState[] { ColorTargetState.DEFAULT };
/*     */       } else {
/* 364 */         activeColorTargetStates = Arrays.<ColorTargetState>copyOf(this.colorTargetStates, this.activeColorTargetStateCount);
/* 366 */         Optional<BlendFunction> lastBlend = Optional.empty();
/* 367 */         for (ColorTargetState activeColorTargetState : activeColorTargetStates) {
/* 368 */           if (activeColorTargetState != null) {
/* 372 */             Optional<BlendFunction> currentBlend = activeColorTargetState.blendFunction();
/* 373 */             if (currentBlend.isPresent())
/* 374 */               if (lastBlend.isEmpty()) {
/* 375 */                 lastBlend = currentBlend;
/* 376 */               } else if (!currentBlend.equals(lastBlend)) {
/* 377 */                 throw new IllegalStateException("Blend functions must currently be the same for all color targets");
/*     */               }  
/*     */           } 
/*     */         } 
/*     */       } 
/* 383 */       int boundVertexAttribCount = 0;
/* 384 */       for (VertexFormat bindings : this.vertexFormatPerBuffer) {
/* 385 */         if (bindings != null)
/* 389 */           boundVertexAttribCount += bindings.getElements().size(); 
/*     */       } 
/* 391 */       if (boundVertexAttribCount > 16)
/* 392 */         throw new IllegalStateException("Binding more than 16 vertex attributes is not supported"); 
/* 395 */       return new RenderPipeline(this.location
/* 396 */           .get(), this.vertexShader
/* 397 */           .get(), this.fragmentShader
/* 398 */           .get(), ((ShaderDefines.Builder)this.definesBuilder
/* 399 */           .orElse(ShaderDefines.builder())).build(), 
/* 400 */           List.copyOf(this.bindGroupLayouts.orElse(new ArrayList<>())), activeColorTargetStates, this.depthStencilState
/*     */           
/* 402 */           .orElse(null), this.polygonMode
/* 403 */           .orElse(PolygonMode.FILL), ((Boolean)this.cull
/* 404 */           .orElse(Boolean.valueOf(true))).booleanValue(), this.vertexFormatPerBuffer, this.primitiveTopology
/*     */           
/* 406 */           .get(), nextPipelineSortKey++);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Snippet extends Record {
/*     */     private final Optional<Identifier> vertexShader;
/*     */     
/*     */     private final Optional<Identifier> fragmentShader;
/*     */     
/*     */     private final Optional<ShaderDefines> shaderDefines;
/*     */     
/*     */     private final Optional<List<BindGroupLayout>> bindGroupLayouts;
/*     */     
/*     */     private final ColorTargetState[] colorTargetStates;
/*     */     
/*     */     private final int activeColorTargetStateCount;
/*     */     
/*     */     private final Optional<DepthStencilState> depthStencilState;
/*     */     
/*     */     private final Optional<PolygonMode> polygonMode;
/*     */     
/*     */     private final Optional<Boolean> cull;
/*     */     
/*     */     private final VertexFormat[] vertexFormatPerBuffer;
/*     */     
/*     */     private final Optional<PrimitiveTopology> vertexFormatMode;
/*     */     
/*     */     public Snippet(Optional<Identifier> vertexShader, Optional<Identifier> fragmentShader, Optional<ShaderDefines> shaderDefines, Optional<List<BindGroupLayout>> bindGroupLayouts, ColorTargetState[] colorTargetStates, int activeColorTargetStateCount, Optional<DepthStencilState> depthStencilState, Optional<PolygonMode> polygonMode, Optional<Boolean> cull, VertexFormat[] vertexFormatPerBuffer, Optional<PrimitiveTopology> vertexFormatMode) {
/* 411 */       this.vertexShader = vertexShader;
/* 411 */       this.fragmentShader = fragmentShader;
/* 411 */       this.shaderDefines = shaderDefines;
/* 411 */       this.bindGroupLayouts = bindGroupLayouts;
/* 411 */       this.colorTargetStates = colorTargetStates;
/* 411 */       this.activeColorTargetStateCount = activeColorTargetStateCount;
/* 411 */       this.depthStencilState = depthStencilState;
/* 411 */       this.polygonMode = polygonMode;
/* 411 */       this.cull = cull;
/* 411 */       this.vertexFormatPerBuffer = vertexFormatPerBuffer;
/* 411 */       this.vertexFormatMode = vertexFormatMode;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #411	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #411	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #411	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Optional<Identifier> vertexShader() {
/* 411 */       return this.vertexShader;
/*     */     }
/*     */     
/*     */     public Optional<Identifier> fragmentShader() {
/* 411 */       return this.fragmentShader;
/*     */     }
/*     */     
/*     */     public Optional<ShaderDefines> shaderDefines() {
/* 411 */       return this.shaderDefines;
/*     */     }
/*     */     
/*     */     public Optional<List<BindGroupLayout>> bindGroupLayouts() {
/* 411 */       return this.bindGroupLayouts;
/*     */     }
/*     */     
/*     */     public ColorTargetState[] colorTargetStates() {
/* 411 */       return this.colorTargetStates;
/*     */     }
/*     */     
/*     */     public int activeColorTargetStateCount() {
/* 411 */       return this.activeColorTargetStateCount;
/*     */     }
/*     */     
/*     */     public Optional<DepthStencilState> depthStencilState() {
/* 411 */       return this.depthStencilState;
/*     */     }
/*     */     
/*     */     public Optional<PolygonMode> polygonMode() {
/* 411 */       return this.polygonMode;
/*     */     }
/*     */     
/*     */     public Optional<Boolean> cull() {
/* 411 */       return this.cull;
/*     */     }
/*     */     
/*     */     public VertexFormat[] vertexFormatPerBuffer() {
/* 411 */       return this.vertexFormatPerBuffer;
/*     */     }
/*     */     
/*     */     public Optional<PrimitiveTopology> vertexFormatMode() {
/* 411 */       return this.vertexFormatMode;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\pipeline\RenderPipeline.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */