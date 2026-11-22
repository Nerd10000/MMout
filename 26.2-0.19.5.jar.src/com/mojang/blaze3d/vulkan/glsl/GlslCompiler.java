/*     */ package com.mojang.blaze3d.vulkan.glsl;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.BindGroupLayout;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
/*     */ import com.mojang.blaze3d.shaders.ShaderType;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.blaze3d.vertex.VertexFormatElement;
/*     */ import com.mojang.blaze3d.vulkan.VulkanBindGroupLayout;
/*     */ import com.mojang.blaze3d.vulkan.VulkanDevice;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.renderer.ShaderDefines;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.lwjgl.util.shaderc.Shaderc;
/*     */ 
/*     */ public class GlslCompiler implements AutoCloseable {
/*     */   private final long shaderCompiler;
/*     */   
/*     */   private final long shaderOptions;
/*     */   
/*     */   private final ShaderDefines globalDefines;
/*     */   
/*     */   public GlslCompiler() {
/*  28 */     this.shaderCompiler = Shaderc.shaderc_compiler_initialize();
/*  29 */     this.shaderOptions = Shaderc.shaderc_compile_options_initialize();
/*  30 */     Shaderc.shaderc_compile_options_set_target_env(this.shaderOptions, 0, 4202496);
/*  31 */     Shaderc.shaderc_compile_options_set_auto_bind_uniforms(this.shaderOptions, true);
/*  32 */     Shaderc.shaderc_compile_options_set_auto_map_locations(this.shaderOptions, true);
/*  33 */     Shaderc.shaderc_compile_options_set_generate_debug_info(this.shaderOptions);
/*  34 */     Shaderc.shaderc_compile_options_set_optimization_level(this.shaderOptions, 0);
/*  36 */     this
/*     */       
/*  39 */       .globalDefines = ShaderDefines.builder().define("gl_VertexID", "gl_VertexIndex").define("gl_InstanceID", "gl_InstanceIndex").build();
/*     */   }
/*     */   
/*     */   public IntermediaryShaderModule createIntermediary(String filename, String source, ShaderType type) throws ShaderCompileException {
/*  43 */     source = GlslPreprocessor.injectDefines(source, this.globalDefines);
/*  44 */     int shaderType = (type == ShaderType.FRAGMENT) ? 1 : 0;
/*  45 */     ByteBuffer sourceBuffer = MemoryUtil.memUTF8(source, false);
/*  46 */     ByteBuffer filenameBuffer = MemoryUtil.memUTF8(filename);
/*  47 */     ByteBuffer entrypointBuffer = MemoryUtil.memUTF8("main");
/*  48 */     long result = Shaderc.shaderc_compile_into_spv(this.shaderCompiler, sourceBuffer, shaderType, filenameBuffer, entrypointBuffer, this.shaderOptions);
/*     */     try {
/*  50 */       int status = Shaderc.shaderc_result_get_compilation_status(result);
/*  51 */       if (status != 0)
/*  52 */         throw new ShaderCompileException("Couldn't parse GLSL: " + Shaderc.shaderc_result_get_error_message(result)); 
/*  54 */       ByteBuffer spirv = Shaderc.shaderc_result_get_bytes(result);
/*  55 */       ByteBuffer copy = MemoryUtil.memCalloc(spirv.remaining());
/*  56 */       MemoryUtil.memCopy(spirv, copy);
/*  57 */       return IntermediaryShaderModule.createFromSpirv(filename, copy);
/*     */     } finally {
/*  59 */       Shaderc.shaderc_result_release(result);
/*  60 */       MemoryUtil.memFree(entrypointBuffer);
/*  61 */       MemoryUtil.memFree(filenameBuffer);
/*  62 */       MemoryUtil.memFree(sourceBuffer);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompiledModules compile(VulkanDevice device, RenderPipeline pipeline, IntermediaryShaderModule vertex, IntermediaryShaderModule fragment) throws ShaderCompileException {
/*  67 */     String pipelineName = pipeline.getLocation().toString();
/*  68 */     List<VulkanBindGroupLayout.Entry> entries = new ArrayList<>();
/*  70 */     addToBindGroup(entries, vertex, pipeline);
/*  71 */     addToBindGroup(entries, fragment, pipeline);
/*  73 */     List<String> vertexOutputNames = new ArrayList<>();
/*  74 */     for (SpvVariable output : vertex.outputs())
/*  75 */       vertexOutputNames.add(output.name()); 
/*  78 */     List<String> vertexInputNames = new ArrayList<>();
/*  79 */     for (VertexFormat vertexFormat : pipeline.getVertexFormatBindings()) {
/*  80 */       if (vertexFormat != null)
/*  84 */         for (VertexFormatElement attribute : vertexFormat.getElements())
/*  85 */           vertexInputNames.add(attribute.name());  
/*     */     } 
/*  93 */     vertex.rebind(vertexInputNames, entries);
/*  94 */     fragment.rebind(vertexOutputNames, entries);
/*  95 */     long vertexId = vertex.createVulkanShaderModule(device);
/*  96 */     long fragmentId = fragment.createVulkanShaderModule(device);
/*  97 */     VulkanBindGroupLayout layout = VulkanBindGroupLayout.create(device, entries, pipelineName);
/*  98 */     return new CompiledModules(vertexId, fragmentId, layout);
/*     */   }
/*     */   
/*     */   public void close() {
/* 103 */     Shaderc.shaderc_compile_options_release(this.shaderOptions);
/* 104 */     Shaderc.shaderc_compiler_release(this.shaderCompiler);
/*     */   }
/*     */   
/*     */   private static void addToBindGroup(List<VulkanBindGroupLayout.Entry> entries, IntermediaryShaderModule shader, RenderPipeline pipeline) throws ShaderCompileException {
/* 108 */     for (SpvUniformBuffer buffer : shader.uniformBuffers()) {
/* 109 */       String name = buffer.name();
/* 110 */       Optional<BindGroupLayout.UniformDescription> uniformDescription = BindGroupLayout.flattenUniforms(pipeline.getBindGroupLayouts()).stream().filter(d -> d.name().equals(name)).findFirst();
/* 111 */       if (uniformDescription.isEmpty())
/* 112 */         throw new ShaderCompileException("Unable to find shader defined uniform (" + name + ")"); 
/* 114 */       if (entries.stream().noneMatch(e -> (e.type() == VulkanBindGroupLayout.VulkanBindGroupEntryType.UNIFORM_BUFFER && e.name().equals(name))))
/* 115 */         entries.add(new VulkanBindGroupLayout.Entry(VulkanBindGroupLayout.VulkanBindGroupEntryType.UNIFORM_BUFFER, name, null)); 
/*     */     } 
/* 118 */     for (SpvSampler sampler : shader.samplers()) {
/* 119 */       String name = sampler.name();
/* 120 */       Optional<BindGroupLayout.UniformDescription> uniformDescription = BindGroupLayout.flattenUniforms(pipeline.getBindGroupLayouts()).stream().filter(d -> d.name().equals(name)).findFirst();
/* 121 */       if (uniformDescription.isPresent()) {
/* 122 */         if (sampler.dimensions() != 5)
/* 123 */           throw new ShaderCompileException("UTB (" + name + ") must have type of SpvDimBuffer"); 
/* 125 */         if (entries.stream().noneMatch(e -> (e.type() == VulkanBindGroupLayout.VulkanBindGroupEntryType.TEXEL_BUFFER && e.name().equals(name))))
/* 126 */           entries.add(new VulkanBindGroupLayout.Entry(VulkanBindGroupLayout.VulkanBindGroupEntryType.TEXEL_BUFFER, name, ((BindGroupLayout.UniformDescription)uniformDescription.get()).gpuFormat())); 
/*     */         continue;
/*     */       } 
/* 130 */       Objects.requireNonNull(name);
/* 130 */       if (BindGroupLayout.flattenSamplers(pipeline.getBindGroupLayouts()).stream().noneMatch(name::equals))
/* 131 */         throw new ShaderCompileException("Unable to find shader defined uniform (" + name + ")"); 
/* 133 */       if (sampler.dimensions() != 1 && sampler.dimensions() != 3)
/* 134 */         throw new ShaderCompileException("Sampled texture (" + name + ") must have type of SpvDim2D or SpvDimCube"); 
/* 136 */       if (entries.stream().noneMatch(e -> (e.type() == VulkanBindGroupLayout.VulkanBindGroupEntryType.SAMPLED_IMAGE && e.name().equals(name))))
/* 137 */         entries.add(new VulkanBindGroupLayout.Entry(VulkanBindGroupLayout.VulkanBindGroupEntryType.SAMPLED_IMAGE, name, null)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final class CompiledModules extends Record {
/*     */     private final long vertex;
/*     */     
/*     */     private final long fragment;
/*     */     
/*     */     private final VulkanBindGroupLayout layout;
/*     */     
/*     */     public CompiledModules(long vertex, long fragment, VulkanBindGroupLayout layout) {
/* 142 */       this.vertex = vertex;
/* 142 */       this.fragment = fragment;
/* 142 */       this.layout = layout;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/glsl/GlslCompiler$CompiledModules;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/glsl/GlslCompiler$CompiledModules;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/glsl/GlslCompiler$CompiledModules;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/glsl/GlslCompiler$CompiledModules;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/glsl/GlslCompiler$CompiledModules;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/glsl/GlslCompiler$CompiledModules;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public long vertex() {
/* 142 */       return this.vertex;
/*     */     }
/*     */     
/*     */     public long fragment() {
/* 142 */       return this.fragment;
/*     */     }
/*     */     
/*     */     public VulkanBindGroupLayout layout() {
/* 142 */       return this.layout;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\glsl\GlslCompiler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */