package com.mojang.blaze3d.systems;

import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.function.Supplier;
import org.lwjgl.PointerBuffer;

public interface RenderPassBackend {
  void pushDebugGroup(Supplier<String> paramSupplier);
  
  void popDebugGroup();
  
  void setPipeline(RenderPipeline paramRenderPipeline);
  
  void bindTexture(String paramString, GpuTextureView paramGpuTextureView, GpuSampler paramGpuSampler);
  
  void setUniform(String paramString, GpuBuffer paramGpuBuffer);
  
  void setUniform(String paramString, GpuBufferSlice paramGpuBufferSlice);
  
  void enableScissor(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  void disableScissor();
  
  void setVertexBuffer(int paramInt, GpuBufferSlice paramGpuBufferSlice);
  
  void setIndexBuffer(GpuBuffer paramGpuBuffer, IndexType paramIndexType);
  
  void drawIndexed(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  void multiDrawIndexed(IntBuffer paramIntBuffer, int paramInt1, int paramInt2, int paramInt3);
  
  void multiDrawIndexed(PointerBuffer paramPointerBuffer, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2, int paramInt);
  
  void drawIndexedIndirect(GpuBufferSlice paramGpuBufferSlice, int paramInt);
  
  <T> void drawMultipleIndexed(Collection<RenderPass.Draw<T>> paramCollection, GpuBuffer paramGpuBuffer, IndexType paramIndexType, Collection<String> paramCollection1, T paramT);
  
  void draw(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  void multiDraw(IntBuffer paramIntBuffer, int paramInt1, int paramInt2, int paramInt3);
  
  void multiDraw(IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2, int paramInt);
  
  void drawIndirect(GpuBufferSlice paramGpuBufferSlice, int paramInt);
  
  void writeTimestamp(GpuQueryPool paramGpuQueryPool, int paramInt);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\RenderPassBackend.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */