package com.mojang.blaze3d.systems;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.GpuFence;
import com.mojang.blaze3d.textures.GpuTexture;
import java.nio.ByteBuffer;
import org.joml.Vector4fc;

public interface CommandEncoderBackend {
  void submit();
  
  TransientMemory transientMemory();
  
  RenderPassBackend createRenderPass(RenderPassDescriptor paramRenderPassDescriptor);
  
  void submitRenderPass();
  
  void clearColorTexture(GpuTexture paramGpuTexture, Vector4fc paramVector4fc);
  
  void clearColorAndDepthTextures(GpuTexture paramGpuTexture1, Vector4fc paramVector4fc, GpuTexture paramGpuTexture2, double paramDouble);
  
  void clearColorAndDepthTextures(GpuTexture paramGpuTexture1, Vector4fc paramVector4fc, GpuTexture paramGpuTexture2, double paramDouble, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  void clearDepthTexture(GpuTexture paramGpuTexture, double paramDouble);
  
  void writeToBuffer(GpuBufferSlice paramGpuBufferSlice, ByteBuffer paramByteBuffer);
  
  void copyToBuffer(GpuBufferSlice paramGpuBufferSlice1, GpuBufferSlice paramGpuBufferSlice2);
  
  void writeToTexture(GpuTexture paramGpuTexture, ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  void copyBufferToTexture(GpuBufferSlice paramGpuBufferSlice, int paramInt1, int paramInt2, int paramInt3, int paramInt4, GpuTexture paramGpuTexture, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10);
  
  void copyTextureToBuffer(GpuTexture paramGpuTexture, GpuBuffer paramGpuBuffer, long paramLong, Runnable paramRunnable, int paramInt);
  
  void copyTextureToBuffer(GpuTexture paramGpuTexture, GpuBuffer paramGpuBuffer, long paramLong, Runnable paramRunnable, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  void copyTextureToTexture(GpuTexture paramGpuTexture1, GpuTexture paramGpuTexture2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);
  
  GpuFence createFence();
  
  void writeTimestamp(GpuQueryPool paramGpuQueryPool, int paramInt);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\CommandEncoderBackend.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */