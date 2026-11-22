/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.pipeline.CompiledRenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
/*     */ import com.mojang.blaze3d.shaders.ShaderSource;
/*     */ import com.mojang.blaze3d.shaders.ShaderType;
/*     */ import com.mojang.blaze3d.systems.CommandEncoderBackend;
/*     */ import com.mojang.blaze3d.systems.DeviceFeatures;
/*     */ import com.mojang.blaze3d.systems.DeviceInfo;
/*     */ import com.mojang.blaze3d.systems.DeviceLimits;
/*     */ import com.mojang.blaze3d.systems.GpuDeviceBackend;
/*     */ import com.mojang.blaze3d.systems.GpuQueryPool;
/*     */ import com.mojang.blaze3d.systems.GpuSurfaceBackend;
/*     */ import com.mojang.blaze3d.systems.HintsAndWorkarounds;
/*     */ import com.mojang.blaze3d.textures.AddressMode;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vulkan.checkpoints.CheckpointExtension;
/*     */ import com.mojang.blaze3d.vulkan.glsl.GlslCompiler;
/*     */ import com.mojang.blaze3d.vulkan.glsl.IntermediaryShaderModule;
/*     */ import com.mojang.blaze3d.vulkan.glsl.ShaderCompileException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.IntIntPair;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.renderer.ShaderDefines;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.lwjgl.util.vma.Vma;
/*     */ import org.lwjgl.vulkan.VK12;
/*     */ import org.lwjgl.vulkan.VkDevice;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceLimits;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceVulkan11Properties;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VulkanDevice implements GpuDeviceBackend {
/*  51 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final ShaderSource defaultShaderSource;
/*     */   
/*  54 */   private final Map<RenderPipeline, VulkanRenderPipeline> pipelineCache = new IdentityHashMap<>();
/*     */   
/*  55 */   private final Map<ShaderCompilationKey, IntermediaryShaderModule> shaderCache = new HashMap<>();
/*     */   
/*     */   private final VulkanInstance instance;
/*     */   
/*     */   private final VkDevice vkDevice;
/*     */   
/*     */   private final long vma;
/*     */   
/*  59 */   private final GlslCompiler glslCompiler = new GlslCompiler();
/*     */   
/*     */   private final DeviceInfo deviceInfo;
/*     */   
/*     */   private final VulkanQueue graphicsQueue;
/*     */   
/*     */   private final VulkanQueue computeQueue;
/*     */   
/*     */   private final VulkanQueue transferQueue;
/*     */   
/*     */   private final boolean isIntegratedIntelMoltenVK;
/*     */   
/*     */   private final VulkanCommandEncoder commandEncoder;
/*     */   
/*     */   private final CheckpointExtension checkpointExtension;
/*     */   
/*     */   public VulkanDevice(ShaderSource defaultShaderSource, VulkanInstance instance, VulkanPhysicalDevice physicalDevice, Set<String> enabledDeviceExtensions, VkDevice vkDevice, long vma, CheckpointExtension checkpointExtension) {
/*  71 */     this.defaultShaderSource = defaultShaderSource;
/*  72 */     this.instance = instance;
/*  73 */     this.vkDevice = vkDevice;
/*  74 */     this.vma = vma;
/*  75 */     this.checkpointExtension = checkpointExtension;
/*  77 */     Set<String> extensionNames = new HashSet<>();
/*  78 */     for (String name : instance.getEnabledExtensions())
/*  79 */       extensionNames.add(name + " (I)"); 
/*  81 */     for (String name : enabledDeviceExtensions)
/*  82 */       extensionNames.add(name + " (D)"); 
/*  84 */     VkPhysicalDeviceLimits limits = physicalDevice.vkPhysicalDeviceProperties().limits();
/*  85 */     VkPhysicalDeviceVulkan11Properties vk11Properties = physicalDevice.vkPhysicalDeviceVulkan11Properties();
/*  86 */     this
/*     */       
/* 115 */       .deviceInfo = new DeviceInfo(physicalDevice.deviceName(), physicalDevice.vendorName(), physicalDevice.driverInfo(), true, "Vulkan", limits.timestampPeriod(), new DeviceLimits((int)limits.maxSamplerAnisotropy(), (int)limits.minUniformBufferOffsetAlignment(), limits.maxImageDimension2D(), (vk11Properties.maxMemoryAllocationSize() < 0L) ? Long.MAX_VALUE : vk11Properties.maxMemoryAllocationSize(), (physicalDevice.vkPhysicalDeviceMultiDrawPropertiesEXT().maxMultiDrawCount() < 0) ? Integer.MAX_VALUE : physicalDevice.vkPhysicalDeviceMultiDrawPropertiesEXT().maxMultiDrawCount(), limits.maxColorAttachments()), new DeviceFeatures(true, enabledDeviceExtensions.contains("VK_EXT_multi_draw"), false, true, true, true, true), Collections.unmodifiableSet(extensionNames), new HintsAndWorkarounds(false, false), physicalDevice.deviceType());
/* 118 */     IntIntPair graphicsQueueFamily = physicalDevice.graphicsQueueFamilyAndIndex();
/* 119 */     assert graphicsQueueFamily != null;
/* 120 */     IntIntPair computeQueueFamily = physicalDevice.computeQueueFamilyAndIndex();
/* 121 */     IntIntPair transferQueueFamily = physicalDevice.transferQueueFamilyAndIndex();
/* 123 */     this.graphicsQueue = new VulkanQueue(this, graphicsQueueFamily.leftInt(), graphicsQueueFamily.rightInt());
/* 124 */     if (computeQueueFamily != null) {
/* 125 */       this.computeQueue = new VulkanQueue(this, computeQueueFamily.leftInt(), computeQueueFamily.rightInt());
/*     */     } else {
/* 127 */       this.computeQueue = this.graphicsQueue;
/*     */     } 
/* 129 */     if (transferQueueFamily != null) {
/* 130 */       this.transferQueue = new VulkanQueue(this, transferQueueFamily.leftInt(), transferQueueFamily.rightInt());
/*     */     } else {
/* 132 */       this.transferQueue = this.computeQueue;
/*     */     } 
/* 138 */     this.isIntegratedIntelMoltenVK = (physicalDevice.vkPhysicalDeviceProperties().deviceType() == 1 && physicalDevice.vkPhysicalDeviceProperties().vendorID() == 32902 && physicalDevice.vkPhysicalDeviceDriverProperties().driverID() == 14);
/* 140 */     physicalDevice.close();
/* 142 */     this.commandEncoder = new VulkanCommandEncoder(this);
/*     */   }
/*     */   
/*     */   public void close() {
/* 147 */     this.checkpointExtension.close();
/* 148 */     this.commandEncoder.destroy();
/* 149 */     clearPipelineCache();
/* 150 */     Vma.vmaDestroyAllocator(this.vma);
/* 151 */     VK12.vkDestroyDevice(this.vkDevice, null);
/* 152 */     this.instance.close();
/* 153 */     this.glslCompiler.close();
/*     */   }
/*     */   
/*     */   public DeviceInfo getDeviceInfo() {
/* 158 */     return this.deviceInfo;
/*     */   }
/*     */   
/*     */   public VulkanInstance instance() {
/* 162 */     return this.instance;
/*     */   }
/*     */   
/*     */   public VkDevice vkDevice() {
/* 166 */     return this.vkDevice;
/*     */   }
/*     */   
/*     */   public VulkanQueue graphicsQueue() {
/* 170 */     return this.graphicsQueue;
/*     */   }
/*     */   
/*     */   public VulkanQueue computeQueue() {
/* 174 */     return this.computeQueue;
/*     */   }
/*     */   
/*     */   public VulkanQueue transferQueue() {
/* 178 */     return this.transferQueue;
/*     */   }
/*     */   
/*     */   public long vma() {
/* 182 */     return this.vma;
/*     */   }
/*     */   
/*     */   public GpuSurfaceBackend createSurface(long windowHandle) {
/* 187 */     return new VulkanGpuSurface(this, windowHandle);
/*     */   }
/*     */   
/*     */   public VulkanCommandEncoder createCommandEncoder() {
/* 192 */     return this.commandEncoder;
/*     */   }
/*     */   
/*     */   public GpuSampler createSampler(AddressMode addressModeU, AddressMode addressModeV, FilterMode minFilter, FilterMode magFilter, int maxAnisotropy, OptionalDouble maxLod) {
/* 197 */     return new VulkanGpuSampler(this, addressModeU, addressModeV, minFilter, magFilter, maxAnisotropy, maxLod);
/*     */   }
/*     */   
/*     */   public GpuTexture createTexture(Supplier<String> label, int usage, GpuFormat format, int width, int height, int depthOrLayers, int mipLevels) {
/* 202 */     return new VulkanGpuTexture(this, usage, (isDebuggingEnabled() && label != null) ? label.get() : "", format, width, height, depthOrLayers, mipLevels);
/*     */   }
/*     */   
/*     */   public GpuTexture createTexture(String label, int usage, GpuFormat format, int width, int height, int depthOrLayers, int mipLevels) {
/* 207 */     return new VulkanGpuTexture(this, usage, (isDebuggingEnabled() && label != null) ? label : "", format, width, height, depthOrLayers, mipLevels);
/*     */   }
/*     */   
/*     */   public GpuTextureView createTextureView(GpuTexture texture) {
/* 213 */     return createTextureView(texture, 0, texture.getMipLevels());
/*     */   }
/*     */   
/*     */   public GpuTextureView createTextureView(GpuTexture texture, int baseMipLevel, int mipLevels) {
/* 218 */     return new VulkanGpuTextureView(this, (VulkanGpuTexture)texture, baseMipLevel, mipLevels);
/*     */   }
/*     */   
/*     */   public VulkanGpuBuffer createBuffer(Supplier<String> label, int usage, long size) {
/* 223 */     return new VulkanGpuBuffer.Direct(this, label, usage, size, this.isIntegratedIntelMoltenVK);
/*     */   }
/*     */   
/*     */   public GpuBuffer createBuffer(Supplier<String> label, int usage, ByteBuffer data) {
/* 230 */     GpuBuffer buffer = createBuffer(label, usage | 0x8, data.remaining());
/* 231 */     createCommandEncoder().writeToBuffer(buffer.slice(), data);
/* 232 */     return buffer;
/*     */   }
/*     */   
/*     */   public List<String> getLastDebugMessages() {
/* 237 */     return List.of();
/*     */   }
/*     */   
/*     */   public boolean isDebuggingEnabled() {
/* 242 */     return this.instance.debug().enabled();
/*     */   }
/*     */   
/*     */   public CompiledRenderPipeline precompilePipeline(RenderPipeline pipeline, ShaderSource customShaderSource) {
/* 247 */     ShaderSource shaderSource = (customShaderSource == null) ? this.defaultShaderSource : customShaderSource;
/* 248 */     return this.pipelineCache.computeIfAbsent(pipeline, ignored -> compilePipeline(pipeline, shaderSource));
/*     */   }
/*     */   
/*     */   protected VulkanRenderPipeline getOrCompilePipeline(RenderPipeline pipeline) {
/* 252 */     return this.pipelineCache.computeIfAbsent(pipeline, ignored -> compilePipeline(pipeline, this.defaultShaderSource));
/*     */   }
/*     */   
/*     */   protected IntermediaryShaderModule getOrCompileShader(Identifier id, ShaderType type, ShaderDefines defines, ShaderSource shaderSource) {
/* 256 */     ShaderCompilationKey key = new ShaderCompilationKey(id, type, defines);
/* 257 */     return this.shaderCache.computeIfAbsent(key, ignored -> compileShader(key, shaderSource));
/*     */   }
/*     */   
/*     */   private IntermediaryShaderModule compileShader(ShaderCompilationKey key, ShaderSource shaderSource) {
/* 261 */     String source = shaderSource.get(key.id, key.type);
/* 262 */     if (source == null) {
/* 263 */       LOGGER.error("Couldn't find source for {} shader ({})", key.type, key.id);
/* 264 */       return IntermediaryShaderModule.INVALID;
/*     */     } 
/* 266 */     String sourceWithDefines = GlslPreprocessor.injectDefines(source, key.defines);
/*     */     try {
/* 268 */       return this.glslCompiler.createIntermediary(key.id.toDebugFileName(), sourceWithDefines, key.type);
/* 269 */     } catch (ShaderCompileException e) {
/* 270 */       LOGGER.error("Couldn't compile {} shader {}: {}", new Object[] { key.type, key.id, e.getMessage() });
/* 271 */       return IntermediaryShaderModule.INVALID;
/*     */     } 
/*     */   }
/*     */   
/*     */   private VulkanRenderPipeline compilePipeline(RenderPipeline pipeline, ShaderSource shaderSource) {
/* 276 */     IntermediaryShaderModule vertexShader = getOrCompileShader(pipeline.getVertexShader(), ShaderType.VERTEX, pipeline.getShaderDefines(), shaderSource);
/* 277 */     IntermediaryShaderModule fragmentShader = getOrCompileShader(pipeline.getFragmentShader(), ShaderType.FRAGMENT, pipeline.getShaderDefines(), shaderSource);
/* 278 */     if (vertexShader == IntermediaryShaderModule.INVALID) {
/* 279 */       LOGGER.error("Couldn't compile pipeline {}: vertex shader {} was invalid", pipeline.getLocation(), pipeline.getVertexShader());
/* 280 */       return new VulkanRenderPipeline(pipeline, this, 0L, 0L, 0L, VulkanBindGroupLayout.INVALID_LAYOUT, 0L, 0L);
/*     */     } 
/* 282 */     if (fragmentShader == IntermediaryShaderModule.INVALID) {
/* 283 */       LOGGER.error("Couldn't compile pipeline {}: fragment shader {} was invalid", pipeline.getLocation(), pipeline.getFragmentShader());
/* 284 */       return new VulkanRenderPipeline(pipeline, this, 0L, 0L, 0L, VulkanBindGroupLayout.INVALID_LAYOUT, 0L, 0L);
/*     */     } 
/*     */     try {
/* 287 */       GlslCompiler.CompiledModules modules = this.glslCompiler.compile(this, pipeline, vertexShader, fragmentShader);
/* 288 */       return VulkanRenderPipeline.compile(this, modules.layout(), pipeline, modules.vertex(), modules.fragment());
/* 289 */     } catch (ShaderCompileException e) {
/* 290 */       LOGGER.error("Couldn't compile pipeline {}: {}", pipeline.getLocation(), e.getMessage());
/* 291 */       return new VulkanRenderPipeline(pipeline, this, 0L, 0L, 0L, VulkanBindGroupLayout.INVALID_LAYOUT, 0L, 0L);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clearPipelineCache() {
/* 297 */     this.graphicsQueue.waitIdle();
/* 298 */     this.pipelineCache.values().forEach(VulkanRenderPipeline::destroy);
/* 299 */     this.pipelineCache.clear();
/* 300 */     this.shaderCache.values().forEach(IntermediaryShaderModule::close);
/* 301 */     this.shaderCache.clear();
/*     */   }
/*     */   
/*     */   public GpuQueryPool createTimestampQueryPool(int size) {
/* 306 */     return new VulkanQueryPool(this, size);
/*     */   }
/*     */   
/*     */   public long getTimestampNow() {
/* 311 */     return this.commandEncoder.getTimestampNow();
/*     */   }
/*     */   
/*     */   public CheckpointExtension checkpointExtension() {
/* 315 */     return this.checkpointExtension;
/*     */   }
/*     */   
/*     */   private static final class ShaderCompilationKey extends Record {
/*     */     private final Identifier id;
/*     */     
/*     */     private final ShaderType type;
/*     */     
/*     */     private final ShaderDefines defines;
/*     */     
/*     */     private ShaderCompilationKey(Identifier id, ShaderType type, ShaderDefines defines) {
/* 318 */       this.id = id;
/* 318 */       this.type = type;
/* 318 */       this.defines = defines;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanDevice$ShaderCompilationKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #318	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanDevice$ShaderCompilationKey;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanDevice$ShaderCompilationKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #318	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanDevice$ShaderCompilationKey;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Identifier id() {
/* 318 */       return this.id;
/*     */     }
/*     */     
/*     */     public ShaderType type() {
/* 318 */       return this.type;
/*     */     }
/*     */     
/*     */     public ShaderDefines defines() {
/* 318 */       return this.defines;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 321 */       String string = String.valueOf(this.id) + " (" + String.valueOf(this.id) + ")";
/* 322 */       if (!this.defines.isEmpty())
/* 323 */         return string + " with " + string; 
/* 325 */       return string;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanDevice.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */