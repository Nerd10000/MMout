/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.nio.ByteBuffer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ class ImageDataHolder implements AutoCloseable {
/*     */   private final Identifier identifier;
/*     */   
/*     */   private final NativeImage image;
/*     */   
/* 189 */   private GpuBuffer gpuBuffer = null;
/*     */   
/*     */   private ImageDataHolder(Identifier identifier, NativeImage image) {
/* 192 */     this.identifier = identifier;
/* 193 */     this.image = image;
/*     */   }
/*     */   
/*     */   private GpuBuffer gpuData() {
/* 198 */     if (this.gpuBuffer == null) {
/* 199 */       ByteBuffer imageBytes = this.image.getPixelBytes();
/* 200 */       this.gpuBuffer = RenderSystem.getDevice().createBuffer(() -> String.valueOf(this.identifier) + " staging buffer", 22, imageBytes.remaining());
/* 201 */       GpuBufferSlice.MappedView mappedView = this.gpuBuffer.map(false, true);
/*     */       try {
/* 202 */         MemoryUtil.memCopy(imageBytes, mappedView.data());
/* 203 */         if (mappedView != null)
/* 203 */           mappedView.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (mappedView != null)
/*     */           try {
/*     */             mappedView.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } 
/* 205 */     return this.gpuBuffer;
/*     */   }
/*     */   
/*     */   public void close() {
/* 210 */     this.image.close();
/* 211 */     if (this.gpuBuffer != null)
/* 212 */       this.gpuBuffer.close(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\BitmapProvider$ImageDataHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */