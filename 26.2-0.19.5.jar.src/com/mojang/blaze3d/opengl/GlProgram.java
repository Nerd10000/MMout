/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.pipeline.BindGroupLayout;
/*     */ import com.mojang.blaze3d.shaders.UniformType;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.blaze3d.vertex.VertexFormatElement;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.renderer.ShaderManager;
/*     */ import org.lwjgl.opengl.GL33C;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GlProgram implements AutoCloseable {
/*  22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  25 */   public static final Set<String> BUILT_IN_UNIFORMS = Sets.newHashSet((Object[])new String[] { "Projection", "Lighting", "Fog", "Globals" });
/*     */   
/*  32 */   public static final GlProgram INVALID_PROGRAM = new GlProgram(-1, "invalid");
/*     */   
/*  34 */   private final Map<String, Uniform> uniformsByName = new HashMap<>();
/*     */   
/*     */   private final int programId;
/*     */   
/*     */   private final String debugLabel;
/*     */   
/*     */   private GlProgram(int programId, String debugLabel) {
/*  41 */     this.programId = programId;
/*  42 */     this.debugLabel = debugLabel;
/*     */   }
/*     */   
/*     */   public static GlProgram link(GlShaderModule vertexShader, GlShaderModule fragmentShader, VertexFormat[] vertexBindings, String debugLabel) throws ShaderManager.CompilationException {
/*  46 */     int programId = GlStateManager.glCreateProgram();
/*  47 */     if (programId <= 0)
/*  48 */       throw new ShaderManager.CompilationException("Could not create shader program (returned program ID " + programId + ")"); 
/*  51 */     int attributeLocation = 0;
/*  52 */     String previousName = null;
/*  53 */     for (VertexFormat vertexFormat : vertexBindings) {
/*  54 */       if (vertexFormat != null)
/*  58 */         for (VertexFormatElement attribute : vertexFormat.getElements()) {
/*  60 */           String attributeName = attribute.name();
/*  61 */           if (!attributeName.equals(previousName))
/*  62 */             GlStateManager._glBindAttribLocation(programId, attributeLocation, attributeName); 
/*  64 */           previousName = attributeName;
/*  65 */           attributeLocation++;
/*     */         }  
/*     */     } 
/*  69 */     GlStateManager.glAttachShader(programId, vertexShader.getShaderId());
/*  70 */     GlStateManager.glAttachShader(programId, fragmentShader.getShaderId());
/*  71 */     GlStateManager.glLinkProgram(programId);
/*  73 */     int linkStatus = GlStateManager.glGetProgrami(programId, 35714);
/*  74 */     String linkMessage = GlStateManager.glGetProgramInfoLog(programId, 32768);
/*  76 */     if (linkStatus == 0 || linkMessage.contains("Failed for unknown reason"))
/*  77 */       throw new ShaderManager.CompilationException("Error encountered when linking program containing VS " + String.valueOf(vertexShader.getId()) + " and FS " + String.valueOf(fragmentShader.getId()) + ". Log output: " + linkMessage); 
/*  78 */     if (!linkMessage.isEmpty())
/*  79 */       LOGGER.info("Info log when linking program containing VS {} and FS {}. Log output: {}", new Object[] { vertexShader.getId(), fragmentShader.getId(), linkMessage }); 
/*  82 */     return new GlProgram(programId, debugLabel);
/*     */   }
/*     */   
/*     */   public void setupBindGroupLayouts(List<BindGroupLayout> bindGroupLayouts) {
/*  86 */     BindGroupLayout.ensureCompatible(bindGroupLayouts);
/*  87 */     List<BindGroupLayout.UniformDescription> uniforms = BindGroupLayout.flattenUniforms(bindGroupLayouts);
/*  88 */     List<String> samplers = BindGroupLayout.flattenSamplers(bindGroupLayouts);
/*  89 */     int nextUboBinding = 0;
/*  90 */     int nextSamplerIndex = 0;
/*  92 */     for (BindGroupLayout.UniformDescription uniformDescription : uniforms) {
/*     */       int index, location, uboBinding, samplerIndex;
/*  93 */       String uniformName = uniformDescription.name();
/*  94 */       switch (uniformDescription.type()) {
/*     */         default:
/*  94 */           throw new MatchException(null, null);
/*     */         case UNIFORM_BUFFER:
/*  96 */           index = GL33C.glGetUniformBlockIndex(this.programId, uniformName);
/*  97 */           if (index == -1);
/* 100 */           uboBinding = nextUboBinding++;
/* 101 */           GL33C.glUniformBlockBinding(this.programId, index, uboBinding);
/*     */         case TEXEL_BUFFER:
/* 105 */           location = GlStateManager._glGetUniformLocation(this.programId, uniformName);
/* 106 */           if (location == -1)
/* 107 */             LOGGER.warn("{} shader program does not use utb {} defined in the pipeline. This might be a bug.", this.debugLabel, uniformName); 
/* 110 */           samplerIndex = nextSamplerIndex++;
/*     */       } 
/* 111 */       Uniform uniform = new Uniform.Utb(location, samplerIndex, Objects.<GpuFormat>requireNonNull(uniformDescription.gpuFormat()));
/* 114 */       if (uniform == null)
/*     */         continue; 
/* 117 */       this.uniformsByName.put(uniformName, uniform);
/*     */     } 
/* 120 */     for (String sampler : samplers) {
/* 121 */       int location = GlStateManager._glGetUniformLocation(this.programId, sampler);
/* 122 */       if (location == -1) {
/* 123 */         LOGGER.warn("{} shader program does not use sampler {} defined in the pipeline. This might be a bug.", this.debugLabel, sampler);
/*     */         continue;
/*     */       } 
/* 126 */       int samplerIndex = nextSamplerIndex++;
/* 127 */       this.uniformsByName.put(sampler, new Uniform.Sampler(location, samplerIndex));
/*     */     } 
/* 130 */     int totalDefinedBlocks = GlStateManager.glGetProgrami(this.programId, 35382);
/* 131 */     for (int i = 0; i < totalDefinedBlocks; i++) {
/* 132 */       String name = GL33C.glGetActiveUniformBlockName(this.programId, i);
/* 133 */       if (!this.uniformsByName.containsKey(name))
/* 134 */         if (!samplers.contains(name) && BUILT_IN_UNIFORMS.contains(name)) {
/* 135 */           int uboBinding = nextUboBinding++;
/* 136 */           GL33C.glUniformBlockBinding(this.programId, i, uboBinding);
/* 137 */           this.uniformsByName.put(name, new Uniform.Ubo(uboBinding));
/*     */         } else {
/* 139 */           LOGGER.warn("Found unknown and unsupported uniform {} in {}", name, this.debugLabel);
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 147 */     this.uniformsByName.values().forEach(Uniform::close);
/* 148 */     GlStateManager.glDeleteProgram(this.programId);
/*     */   }
/*     */   
/*     */   public Uniform getUniform(String name) {
/* 152 */     RenderSystem.assertOnRenderThread();
/* 153 */     return this.uniformsByName.get(name);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getProgramId() {
/* 158 */     return this.programId;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 163 */     return this.debugLabel;
/*     */   }
/*     */   
/*     */   public String getDebugLabel() {
/* 167 */     return this.debugLabel;
/*     */   }
/*     */   
/*     */   public Map<String, Uniform> getUniforms() {
/* 171 */     return this.uniformsByName;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlProgram.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */