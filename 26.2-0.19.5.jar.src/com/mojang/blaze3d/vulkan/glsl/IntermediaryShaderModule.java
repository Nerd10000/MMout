/*     */ package com.mojang.blaze3d.vulkan.glsl;
/*     */ 
/*     */ import com.mojang.blaze3d.vulkan.VulkanBindGroupLayout;
/*     */ import com.mojang.blaze3d.vulkan.VulkanDevice;
/*     */ import com.mojang.blaze3d.vulkan.VulkanUtils;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.lwjgl.util.spvc.Spvc;
/*     */ import org.lwjgl.util.spvc.SpvcReflectedResource;
/*     */ import org.lwjgl.vulkan.VK12;
/*     */ import org.lwjgl.vulkan.VkShaderModuleCreateInfo;
/*     */ 
/*     */ public final class IntermediaryShaderModule extends Record implements AutoCloseable {
/*     */   private final String name;
/*     */   
/*     */   private final ByteBuffer spirv;
/*     */   
/*     */   private final List<SpvUniformBuffer> uniformBuffers;
/*     */   
/*     */   private final List<SpvSampler> samplers;
/*     */   
/*     */   private final List<SpvVariable> outputs;
/*     */   
/*     */   private final List<SpvVariable> inputs;
/*     */   
/*     */   public IntermediaryShaderModule(String name, ByteBuffer spirv, List<SpvUniformBuffer> uniformBuffers, List<SpvSampler> samplers, List<SpvVariable> outputs, List<SpvVariable> inputs) {
/*  24 */     this.name = name;
/*  24 */     this.spirv = spirv;
/*  24 */     this.uniformBuffers = uniformBuffers;
/*  24 */     this.samplers = samplers;
/*  24 */     this.outputs = outputs;
/*  24 */     this.inputs = inputs;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/glsl/IntermediaryShaderModule;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #24	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/glsl/IntermediaryShaderModule;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/glsl/IntermediaryShaderModule;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #24	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/glsl/IntermediaryShaderModule;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/glsl/IntermediaryShaderModule;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #24	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/glsl/IntermediaryShaderModule;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String name() {
/*  24 */     return this.name;
/*     */   }
/*     */   
/*     */   public ByteBuffer spirv() {
/*  24 */     return this.spirv;
/*     */   }
/*     */   
/*     */   public List<SpvUniformBuffer> uniformBuffers() {
/*  24 */     return this.uniformBuffers;
/*     */   }
/*     */   
/*     */   public List<SpvSampler> samplers() {
/*  24 */     return this.samplers;
/*     */   }
/*     */   
/*     */   public List<SpvVariable> outputs() {
/*  24 */     return this.outputs;
/*     */   }
/*     */   
/*     */   public List<SpvVariable> inputs() {
/*  24 */     return this.inputs;
/*     */   }
/*     */   
/*  25 */   public static final IntermediaryShaderModule INVALID = new IntermediaryShaderModule("invalid", null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
/*     */   
/*     */   public static IntermediaryShaderModule createFromSpirv(String filename, ByteBuffer spirv) throws ShaderCompileException {
/*  28 */     List<SpvUniformBuffer> uniformBuffers = new ArrayList<>();
/*  29 */     List<SpvSampler> samplers = new ArrayList<>();
/*  30 */     List<SpvVariable> outputs = new ArrayList<>();
/*  31 */     List<SpvVariable> inputs = new ArrayList<>();
/*  33 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/*  34 */       PointerBuffer pointer = stack.callocPointer(1);
/*  35 */       IntBuffer intReturnBuffer = stack.callocInt(1);
/*  36 */       throwIfError(Spvc.spvc_context_create(pointer), "Couldn't create spvc context");
/*  37 */       long context = pointer.get(0);
/*     */       try {
/*  40 */         throwIfError(Spvc.spvc_context_parse_spirv(context, spirv.asIntBuffer(), (spirv.remaining() / 4), pointer), "Couldn't parse spirv");
/*  41 */         long ir = pointer.get(0);
/*  43 */         throwIfError(Spvc.spvc_context_create_compiler(context, 0, ir, 1, pointer), "Couldn't create compiler");
/*  44 */         long compiler = pointer.get(0);
/*  46 */         throwIfError(Spvc.spvc_compiler_create_shader_resources(compiler, pointer), "Couldn't create resource list");
/*  47 */         long spvcResources = pointer.get(0);
/*  49 */         PointerBuffer countPointer = stack.callocPointer(1);
/*  50 */         throwIfError(Spvc.spvc_resources_get_resource_list_for_type(spvcResources, 1, pointer, countPointer), "Couldn't list uniform buffers");
/*  51 */         long spvcList = pointer.get(0);
/*  52 */         long spvcCount = countPointer.get(0);
/*  53 */         SpvcReflectedResource.Buffer resources = SpvcReflectedResource.create(spvcList, (int)spvcCount);
/*     */         int j;
/*  54 */         for (j = 0; j < spvcCount; j++) {
/*  55 */           SpvcReflectedResource resource = (SpvcReflectedResource)resources.get(j);
/*  56 */           String name = resource.nameString();
/*  57 */           int bindingOffset = getDecorationOffset(compiler, resource, 33, intReturnBuffer);
/*  58 */           uniformBuffers.add(new SpvUniformBuffer(name, bindingOffset));
/*     */         } 
/*  61 */         throwIfError(Spvc.spvc_resources_get_resource_list_for_type(spvcResources, 7, pointer, countPointer), "Couldn't list sampled images");
/*  62 */         spvcList = pointer.get(0);
/*  63 */         spvcCount = countPointer.get(0);
/*  64 */         resources = SpvcReflectedResource.create(spvcList, (int)spvcCount);
/*  65 */         for (j = 0; j < spvcCount; j++) {
/*  66 */           SpvcReflectedResource resource = (SpvcReflectedResource)resources.get(j);
/*  67 */           String name = resource.nameString();
/*  68 */           int bindingOffset = getDecorationOffset(compiler, resource, 33, intReturnBuffer);
/*  69 */           long typeHandle = Spvc.spvc_compiler_get_type_handle(compiler, resource.type_id());
/*  70 */           int dimension = Spvc.spvc_type_get_image_dimension(typeHandle);
/*  71 */           samplers.add(new SpvSampler(name, bindingOffset, dimension));
/*     */         } 
/*  74 */         throwIfError(Spvc.spvc_resources_get_resource_list_for_type(spvcResources, 4, pointer, countPointer), "Couldn't list output variables");
/*  75 */         spvcList = pointer.get(0);
/*  76 */         spvcCount = countPointer.get(0);
/*  77 */         resources = SpvcReflectedResource.create(spvcList, (int)spvcCount);
/*  78 */         for (j = 0; j < spvcCount; j++) {
/*  79 */           SpvcReflectedResource resource = (SpvcReflectedResource)resources.get(j);
/*  80 */           String name = resource.nameString();
/*  81 */           int bindingOffset = getDecorationOffset(compiler, resource, 30, intReturnBuffer);
/*  82 */           outputs.add(new SpvVariable(name, bindingOffset));
/*     */         } 
/*  85 */         throwIfError(Spvc.spvc_resources_get_resource_list_for_type(spvcResources, 3, pointer, countPointer), "Couldn't list input variables");
/*  86 */         spvcList = pointer.get(0);
/*  87 */         spvcCount = countPointer.get(0);
/*  88 */         resources = SpvcReflectedResource.create(spvcList, (int)spvcCount);
/*  89 */         for (j = 0; j < spvcCount; j++) {
/*  90 */           SpvcReflectedResource resource = (SpvcReflectedResource)resources.get(j);
/*  91 */           String name = resource.nameString();
/*  92 */           int bindingOffset = getDecorationOffset(compiler, resource, 30, intReturnBuffer);
/*  93 */           inputs.add(new SpvVariable(name, bindingOffset));
/*     */         } 
/*     */       } finally {
/*  96 */         Spvc.spvc_context_destroy(context);
/*     */       } 
/*  98 */       if (stack != null)
/*  98 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 100 */     IntBuffer spvAsIntBuffer = spirv.asIntBuffer();
/* 101 */     for (int i = 0; i < outputs.size(); i++)
/* 102 */       spvAsIntBuffer.put(((SpvVariable)outputs.get(i)).locationOffset(), i); 
/* 105 */     return new IntermediaryShaderModule(filename, spirv, uniformBuffers, samplers, outputs, inputs);
/*     */   }
/*     */   
/*     */   public void close() {
/* 110 */     MemoryUtil.memFree(this.spirv);
/*     */   }
/*     */   
/*     */   public void rebind(List<String> inputVariables, List<VulkanBindGroupLayout.Entry> entries) throws ShaderCompileException {
/* 114 */     if (this.spirv == null)
/* 115 */       throw new IllegalStateException("Attempt to use invalid shader"); 
/* 117 */     IntBuffer spvAsIntBuffer = this.spirv.asIntBuffer();
/* 118 */     Set<String> remainingInputs = new HashSet<>();
/* 119 */     Set<String> remainingSamplers = new HashSet<>();
/* 120 */     Set<String> remainingUniformBuffers = new HashSet<>();
/* 122 */     for (SpvVariable input : this.inputs)
/* 123 */       remainingInputs.add(input.name()); 
/* 125 */     for (SpvUniformBuffer uniformBuffer : this.uniformBuffers)
/* 126 */       remainingUniformBuffers.add(uniformBuffer.name()); 
/* 128 */     for (SpvSampler sampler : this.samplers)
/* 129 */       remainingSamplers.add(sampler.name()); 
/* 132 */     String previousName = null;
/* 133 */     int attribLocation = 0;
/*     */     int i;
/* 134 */     for (i = 0; i < inputVariables.size(); i++) {
/* 135 */       String variableName = inputVariables.get(i);
/* 136 */       SpvVariable inputVariable = getInputVariable(variableName);
/* 138 */       if (inputVariable != null) {
/* 140 */         if (!variableName.equals(previousName)) {
/* 141 */           spvAsIntBuffer.put(inputVariable.locationOffset(), attribLocation);
/* 142 */           remainingInputs.remove(variableName);
/*     */         } 
/* 144 */         attribLocation++;
/* 145 */         previousName = variableName;
/*     */       } 
/*     */     } 
/* 149 */     for (i = 0; i < entries.size(); i++) {
/*     */       SpvUniformBuffer ubo;
/*     */       SpvSampler sampler;
/* 150 */       VulkanBindGroupLayout.Entry entry = entries.get(i);
/* 151 */       switch (entry.type()) {
/*     */         case UNIFORM_BUFFER:
/* 153 */           ubo = getUniformBuffer(entry.name());
/* 154 */           if (ubo != null) {
/* 155 */             spvAsIntBuffer.put(ubo.bindingOffset(), i);
/* 156 */             remainingUniformBuffers.remove(entry.name());
/*     */           } 
/*     */           break;
/*     */         case SAMPLED_IMAGE:
/* 160 */           sampler = getSampler(entry.name());
/* 161 */           if (sampler != null) {
/* 162 */             if (sampler.dimensions() != 1 && sampler.dimensions() != 3)
/* 163 */               throw new ShaderCompileException("Unsupported texture dimensions '" + SpvcUtil.imageDimensionToString(sampler.dimensions()) + "' for sampler " + entry.name()); 
/* 165 */             spvAsIntBuffer.put(sampler.bindingOffset(), i);
/* 166 */             remainingSamplers.remove(entry.name());
/*     */           } 
/*     */           break;
/*     */         case TEXEL_BUFFER:
/* 170 */           sampler = getSampler(entry.name());
/* 171 */           if (sampler != null) {
/* 172 */             if (sampler.dimensions() != 5)
/* 173 */               throw new ShaderCompileException("Unsupported texel buffer dimensions '" + SpvcUtil.imageDimensionToString(sampler.dimensions()) + "' for sampler " + entry.name()); 
/* 175 */             spvAsIntBuffer.put(sampler.bindingOffset(), i);
/* 176 */             remainingSamplers.remove(entry.name());
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/* 182 */     if (!remainingInputs.isEmpty())
/* 183 */       throw new ShaderCompileException("Shader expects input variables which are not being provided: " + String.valueOf(remainingInputs)); 
/* 185 */     if (!remainingUniformBuffers.isEmpty())
/* 186 */       throw new ShaderCompileException("Shader expects uniform buffers which are not being provided: " + String.valueOf(remainingUniformBuffers)); 
/* 188 */     if (!remainingSamplers.isEmpty())
/* 189 */       throw new ShaderCompileException("Shader expects samplers which are not being provided: " + String.valueOf(remainingSamplers)); 
/*     */   }
/*     */   
/*     */   public long createVulkanShaderModule(VulkanDevice device) {
/* 194 */     if (this.spirv == null)
/* 195 */       throw new IllegalStateException("Attempt to use invalid shader"); 
/* 197 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 201 */       VkShaderModuleCreateInfo info = VkShaderModuleCreateInfo.calloc(stack).sType$Default().pCode(this.spirv);
/* 202 */       LongBuffer pointer = stack.callocLong(1);
/* 203 */       VulkanUtils.crashIfFailure(device, VK12.vkCreateShaderModule(device.vkDevice(), info, null, pointer), "Can't compile " + this.name);
/* 204 */       device.instance().debug().setObjectName(device.vkDevice(), 15, pointer.get(0), () -> this.name);
/* 205 */       long l = pointer.get(0);
/* 206 */       if (stack != null)
/* 206 */         stack.close(); 
/*     */       return l;
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
/*     */   private SpvUniformBuffer getUniformBuffer(String name) {
/* 210 */     for (SpvUniformBuffer ubo : this.uniformBuffers) {
/* 211 */       if (ubo.name().equals(name))
/* 212 */         return ubo; 
/*     */     } 
/* 215 */     return null;
/*     */   }
/*     */   
/*     */   private SpvSampler getSampler(String name) {
/* 219 */     for (SpvSampler sampler : this.samplers) {
/* 220 */       if (sampler.name().equals(name))
/* 221 */         return sampler; 
/*     */     } 
/* 224 */     return null;
/*     */   }
/*     */   
/*     */   private SpvVariable getInputVariable(String name) {
/* 228 */     for (SpvVariable variable : this.inputs) {
/* 229 */       if (variable.name().equals(name))
/* 230 */         return variable; 
/*     */     } 
/* 233 */     return null;
/*     */   }
/*     */   
/*     */   private static void throwIfError(int result, String message) throws ShaderCompileException {
/* 237 */     if (result != 0) {
/* 238 */       switch (result) {
/*     */         case -1:
/*     */         
/*     */         case -2:
/*     */         
/*     */         case -3:
/*     */         
/*     */         case -4:
/*     */         
/*     */       } 
/* 244 */       String name = Integer.toString(result);
/* 245 */       throw new ShaderCompileException(message + " (" + message + ")");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int getDecorationOffset(long compiler, SpvcReflectedResource resource, int decoration, IntBuffer returnBuffer) throws ShaderCompileException {
/* 250 */     if (!Spvc.spvc_compiler_get_binary_offset_for_decoration(compiler, resource.id(), decoration, returnBuffer))
/* 251 */       throw new ShaderCompileException("Couldn't find byte offset for location decoration of " + resource.nameString()); 
/* 253 */     return returnBuffer.get(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\glsl\IntermediaryShaderModule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */