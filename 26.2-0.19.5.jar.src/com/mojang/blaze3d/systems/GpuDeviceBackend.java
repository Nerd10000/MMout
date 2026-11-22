package com.mojang.blaze3d.systems;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.CompiledRenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.ShaderSource;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Supplier;

public interface GpuDeviceBackend {
  GpuSurfaceBackend createSurface(long paramLong);
  
  CommandEncoderBackend createCommandEncoder();
  
  GpuSampler createSampler(AddressMode paramAddressMode1, AddressMode paramAddressMode2, FilterMode paramFilterMode1, FilterMode paramFilterMode2, int paramInt, OptionalDouble paramOptionalDouble);
  
  GpuTexture createTexture(Supplier<String> paramSupplier, int paramInt1, GpuFormat paramGpuFormat, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  GpuTexture createTexture(String paramString, int paramInt1, GpuFormat paramGpuFormat, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  GpuTextureView createTextureView(GpuTexture paramGpuTexture);
  
  GpuTextureView createTextureView(GpuTexture paramGpuTexture, int paramInt1, int paramInt2);
  
  GpuBuffer createBuffer(Supplier<String> paramSupplier, int paramInt, long paramLong);
  
  GpuBuffer createBuffer(Supplier<String> paramSupplier, int paramInt, ByteBuffer paramByteBuffer);
  
  List<String> getLastDebugMessages();
  
  boolean isDebuggingEnabled();
  
  CompiledRenderPipeline precompilePipeline(RenderPipeline paramRenderPipeline, ShaderSource paramShaderSource);
  
  void clearPipelineCache();
  
  void close();
  
  GpuQueryPool createTimestampQueryPool(int paramInt);
  
  long getTimestampNow();
  
  DeviceInfo getDeviceInfo();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\GpuDeviceBackend.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */