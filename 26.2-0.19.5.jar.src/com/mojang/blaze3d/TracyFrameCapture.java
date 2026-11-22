/*     */ package com.mojang.blaze3d;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ 
/*     */ public class TracyFrameCapture implements AutoCloseable {
/*     */   private static final int MAX_WIDTH = 320;
/*     */   
/*     */   private static final int MAX_HEIGHT = 180;
/*     */   
/*     */   private static final long BYTES_PER_PIXEL = 4L;
/*     */   
/*     */   private int targetWidth;
/*     */   
/*     */   private int targetHeight;
/*     */   
/*     */   private int width;
/*     */   
/*     */   private int height;
/*     */   
/*     */   private GpuTexture frameBuffer;
/*     */   
/*     */   private GpuTextureView frameBufferView;
/*     */   
/*     */   private GpuBuffer pixelbuffer;
/*     */   
/*     */   private int lastCaptureDelay;
/*     */   
/*     */   private boolean capturedThisFrame;
/*     */   
/*  32 */   private Status status = Status.WAITING_FOR_CAPTURE;
/*     */   
/*     */   public TracyFrameCapture() {
/*  35 */     this.width = 320;
/*  36 */     this.height = 180;
/*  37 */     GpuDevice device = RenderSystem.getDevice();
/*  38 */     this.frameBuffer = device.createTexture("Tracy Frame Capture", 10, GpuFormat.RGBA8_UNORM, this.width, this.height, 1, 1);
/*  39 */     this.frameBufferView = device.createTextureView(this.frameBuffer);
/*  40 */     this.pixelbuffer = device.createBuffer(() -> "Tracy Frame Capture buffer", 9, (this.width * this.height) * 4L);
/*     */   }
/*     */   
/*     */   private void resize(int width, int height) {
/*  44 */     float aspectRatio = width / height;
/*  46 */     if (width > 320) {
/*  47 */       width = 320;
/*  48 */       height = (int)(320.0F / aspectRatio);
/*     */     } 
/*  50 */     if (height > 180) {
/*  51 */       width = (int)(180.0F * aspectRatio);
/*  52 */       height = 180;
/*     */     } 
/*  56 */     width = width / 4 * 4;
/*  57 */     height = height / 4 * 4;
/*  59 */     if (this.width != width || this.height != height) {
/*  60 */       this.width = width;
/*  61 */       this.height = height;
/*  63 */       GpuDevice device = RenderSystem.getDevice();
/*  64 */       this.frameBuffer.close();
/*  65 */       this.frameBuffer = device.createTexture("Tracy Frame Capture", 10, GpuFormat.RGBA8_UNORM, width, height, 1, 1);
/*  66 */       this.frameBufferView.close();
/*  67 */       this.frameBufferView = device.createTextureView(this.frameBuffer);
/*  68 */       this.pixelbuffer.close();
/*  69 */       this.pixelbuffer = device.createBuffer(() -> "Tracy Frame Capture buffer", 9, (width * height) * 4L);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void capture(RenderTarget captureTarget) {
/*  76 */     if (this.status != Status.WAITING_FOR_CAPTURE || this.capturedThisFrame || captureTarget.getColorTexture() == null)
/*     */       return; 
/*  79 */     this.capturedThisFrame = true;
/*  81 */     if (captureTarget.width != this.targetWidth || captureTarget.height != this.targetHeight) {
/*  82 */       this.targetWidth = captureTarget.width;
/*  83 */       this.targetHeight = captureTarget.height;
/*  84 */       resize(this.targetWidth, this.targetHeight);
/*     */     } 
/*  87 */     this.status = Status.WAITING_FOR_COPY;
/*  88 */     CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
/*  89 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Tracy blit", this.frameBufferView, Optional.empty());
/*     */     try {
/*  90 */       RenderSystem.bindDefaultUniforms(renderPass);
/*  91 */       renderPass.setPipeline(RenderPipelines.TRACY_BLIT);
/*  92 */       renderPass.bindTexture("InSampler", captureTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
/*  93 */       renderPass.draw(3, 1, 0, 0);
/*  94 */       if (renderPass != null)
/*  94 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*  96 */     commandEncoder.copyTextureToBuffer(this.frameBuffer, this.pixelbuffer, 0L, () -> this.status = Status.WAITING_FOR_UPLOAD, 0);
/*  98 */     this.lastCaptureDelay = 0;
/*     */   }
/*     */   
/*     */   public void upload() {
/* 104 */     if (this.status != Status.WAITING_FOR_UPLOAD)
/*     */       return; 
/* 107 */     this.status = Status.WAITING_FOR_CAPTURE;
/* 109 */     GpuBufferSlice.MappedView view = this.pixelbuffer.map(true, false);
/*     */     try {
/* 110 */       TracyClient.frameImage(view.data(), this.width, this.height, this.lastCaptureDelay, true);
/* 111 */       if (view != null)
/* 111 */         view.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (view != null)
/*     */         try {
/*     */           view.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void endFrame() {
/* 115 */     this.lastCaptureDelay++;
/* 116 */     this.capturedThisFrame = false;
/* 117 */     TracyClient.markFrame();
/*     */   }
/*     */   
/*     */   public void close() {
/* 122 */     this.frameBuffer.close();
/* 123 */     this.frameBufferView.close();
/* 124 */     this.pixelbuffer.close();
/*     */   }
/*     */   
/*     */   private enum Status {
/* 128 */     WAITING_FOR_CAPTURE, WAITING_FOR_COPY, WAITING_FOR_UPLOAD;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\TracyFrameCapture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */