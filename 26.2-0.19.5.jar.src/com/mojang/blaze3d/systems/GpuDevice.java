/*     */ package com.mojang.blaze3d.systems;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.pipeline.CompiledRenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.shaders.ShaderSource;
/*     */ import com.mojang.blaze3d.textures.AddressMode;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class GpuDevice {
/*     */   private final GpuDeviceBackend backend;
/*     */   
/*     */   private final Runnable criticalShaderLoader;
/*     */   
/*     */   private final TracyGpuProfiler profiler;
/*     */   
/*     */   public GpuDevice(GpuDeviceBackend backend, Runnable criticalShaderLoader) {
/*  28 */     this.backend = backend;
/*  29 */     this.criticalShaderLoader = criticalShaderLoader;
/*  30 */     if (TracyClient.isAvailable()) {
/*  31 */       this.profiler = new TracyGpuProfiler(this);
/*     */     } else {
/*  33 */       this.profiler = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public GpuSurface createSurface(long windowHandle) {
/*  38 */     return new GpuSurface(this.backend.createSurface(windowHandle));
/*     */   }
/*     */   
/*     */   public CommandEncoder createCommandEncoder() {
/*  42 */     return new CommandEncoder(this.profiler, this.backend, this.backend.createCommandEncoder());
/*     */   }
/*     */   
/*     */   public GpuSampler createSampler(AddressMode addressModeU, AddressMode addressModeV, FilterMode minFilter, FilterMode magFilter, int maxAnisotropy, OptionalDouble maxLod) {
/*  53 */     int maxSupportedAnisotropy = getDeviceInfo().limits().maxAnisotropy();
/*  54 */     if (maxAnisotropy < 1 || maxAnisotropy > maxSupportedAnisotropy)
/*  55 */       throw new IllegalArgumentException("maxAnisotropy out of range; must be >= 1 and <= " + maxSupportedAnisotropy + ", but was " + maxAnisotropy); 
/*  57 */     return this.backend.createSampler(addressModeU, addressModeV, minFilter, magFilter, maxAnisotropy, maxLod);
/*     */   }
/*     */   
/*     */   public GpuTexture createTexture(Supplier<String> label, int usage, GpuFormat format, int width, int height, int depthOrLayers, int mipLevels) {
/*  61 */     verifyTextureCreationArgs(usage, width, height, depthOrLayers, mipLevels);
/*  62 */     return this.backend.createTexture(label, usage, format, width, height, depthOrLayers, mipLevels);
/*     */   }
/*     */   
/*     */   public GpuTexture createTexture(String label, int usage, GpuFormat format, int width, int height, int depthOrLayers, int mipLevels) {
/*  66 */     verifyTextureCreationArgs(usage, width, height, depthOrLayers, mipLevels);
/*  67 */     return this.backend.createTexture(label, usage, format, width, height, depthOrLayers, mipLevels);
/*     */   }
/*     */   
/*     */   private void verifyTextureCreationArgs(int usage, int width, int height, int depthOrLayers, int mipLevels) {
/*  71 */     if (mipLevels < 1)
/*  72 */       throw new IllegalArgumentException("mipLevels must be at least 1"); 
/*  75 */     int maxDimension = Math.max(width, height);
/*  76 */     int maxMipSupported = Mth.log2(maxDimension) + 1;
/*  77 */     if (mipLevels > maxMipSupported)
/*  78 */       throw new IllegalArgumentException("mipLevels must be at most " + maxMipSupported + " for a texture of width " + width + " and height " + height + " (asked for " + mipLevels + " mipLevels)"); 
/*  80 */     if (depthOrLayers < 1)
/*  81 */       throw new IllegalArgumentException("depthOrLayers must be at least 1"); 
/*  84 */     boolean isCubemap = ((usage & 0x10) != 0);
/*  85 */     if (isCubemap) {
/*  86 */       if (width != height)
/*  87 */         throw new IllegalArgumentException("Cubemap compatible textures must be square, but size is " + width + "x" + height); 
/*  89 */       if (depthOrLayers % 6 != 0)
/*  90 */         throw new IllegalArgumentException("Cubemap compatible textures must have a layer count with a multiple of 6, was " + depthOrLayers); 
/*  92 */       if (depthOrLayers > 6)
/*  93 */         throw new UnsupportedOperationException("Array textures are not yet supported"); 
/*  95 */     } else if (depthOrLayers > 1) {
/*  96 */       throw new UnsupportedOperationException("Array or 3D textures are not yet supported");
/*     */     } 
/*     */   }
/*     */   
/*     */   public GpuTextureView createTextureView(GpuTexture texture) {
/* 101 */     verifyTextureViewCreationArgs(texture, 0, texture.getMipLevels());
/* 102 */     return this.backend.createTextureView(texture, 0, texture.getMipLevels());
/*     */   }
/*     */   
/*     */   public GpuTextureView createTextureView(GpuTexture texture, int baseMipLevel, int mipLevels) {
/* 106 */     verifyTextureViewCreationArgs(texture, baseMipLevel, mipLevels);
/* 107 */     return this.backend.createTextureView(texture, baseMipLevel, mipLevels);
/*     */   }
/*     */   
/*     */   private void verifyTextureViewCreationArgs(GpuTexture texture, int baseMipLevel, int mipLevels) {
/* 111 */     if (texture.isClosed())
/* 112 */       throw new IllegalArgumentException("Can't create texture view with closed texture"); 
/* 115 */     if (baseMipLevel < 0 || baseMipLevel + mipLevels > texture.getMipLevels())
/* 116 */       throw new IllegalArgumentException("" + mipLevels + " mip levels starting from " + mipLevels + " would be out of range for texture with only " + baseMipLevel + " mip levels"); 
/*     */   }
/*     */   
/*     */   public GpuBuffer createBuffer(Supplier<String> label, int usage, long size) {
/* 121 */     if (size <= 0L)
/* 122 */       throw new IllegalArgumentException("Buffer size must be greater than zero"); 
/* 124 */     return this.backend.createBuffer(label, usage, size);
/*     */   }
/*     */   
/*     */   public GpuBuffer createBuffer(Supplier<String> label, int usage, ByteBuffer data) {
/* 128 */     if (!data.hasRemaining())
/* 129 */       throw new IllegalArgumentException("Buffer source must not be empty"); 
/* 131 */     return this.backend.createBuffer(label, usage, data);
/*     */   }
/*     */   
/*     */   public List<String> getLastDebugMessages() {
/* 135 */     return this.backend.getLastDebugMessages();
/*     */   }
/*     */   
/*     */   public boolean isDebuggingEnabled() {
/* 139 */     return this.backend.isDebuggingEnabled();
/*     */   }
/*     */   
/*     */   public CompiledRenderPipeline precompilePipeline(RenderPipeline pipeline) {
/* 143 */     return precompilePipeline(pipeline, null);
/*     */   }
/*     */   
/*     */   public CompiledRenderPipeline precompilePipeline(RenderPipeline pipeline, ShaderSource shaderSource) {
/* 147 */     return this.backend.precompilePipeline(pipeline, shaderSource);
/*     */   }
/*     */   
/*     */   public void clearPipelineCache() {
/* 151 */     this.backend.clearPipelineCache();
/*     */   }
/*     */   
/*     */   public void loadCriticalShaders() {
/* 155 */     this.criticalShaderLoader.run();
/*     */   }
/*     */   
/*     */   public void close() {
/* 159 */     this.backend.close();
/*     */   }
/*     */   
/*     */   public GpuQueryPool createTimestampQueryPool(int size) {
/* 163 */     return this.backend.createTimestampQueryPool(size);
/*     */   }
/*     */   
/*     */   protected long getTimestampNow() {
/* 168 */     return this.backend.getTimestampNow();
/*     */   }
/*     */   
/*     */   public DeviceInfo getDeviceInfo() {
/* 172 */     return this.backend.getDeviceInfo();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\GpuDevice.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */