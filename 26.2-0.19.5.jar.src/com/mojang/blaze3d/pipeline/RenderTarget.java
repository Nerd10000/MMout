/*     */ package com.mojang.blaze3d.pipeline;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ 
/*     */ public abstract class RenderTarget {
/*  17 */   private static int UNNAMED_RENDER_TARGETS = 0;
/*     */   
/*     */   public int width;
/*     */   
/*     */   public int height;
/*     */   
/*     */   protected final String label;
/*     */   
/*     */   public final boolean useDepth;
/*     */   
/*     */   protected final GpuFormat format;
/*     */   
/*     */   protected GpuTexture colorTexture;
/*     */   
/*     */   protected GpuTextureView colorTextureView;
/*     */   
/*     */   protected GpuTexture depthTexture;
/*     */   
/*     */   protected GpuTextureView depthTextureView;
/*     */   
/*     */   public RenderTarget(String label, boolean useDepth, GpuFormat format) {
/*  32 */     this.label = (label == null) ? ("FBO " + UNNAMED_RENDER_TARGETS++) : label;
/*  33 */     this.useDepth = useDepth;
/*  34 */     this.format = format;
/*     */   }
/*     */   
/*     */   public void resize(int width, int height) {
/*  38 */     RenderSystem.assertOnRenderThread();
/*  40 */     destroyBuffers();
/*  41 */     createBuffers(width, height);
/*     */   }
/*     */   
/*     */   public void destroyBuffers() {
/*  45 */     RenderSystem.assertOnRenderThread();
/*  47 */     if (this.depthTexture != null) {
/*  48 */       this.depthTexture.close();
/*  49 */       this.depthTexture = null;
/*     */     } 
/*  51 */     if (this.depthTextureView != null) {
/*  52 */       this.depthTextureView.close();
/*  53 */       this.depthTextureView = null;
/*     */     } 
/*  55 */     if (this.colorTexture != null) {
/*  56 */       this.colorTexture.close();
/*  57 */       this.colorTexture = null;
/*     */     } 
/*  59 */     if (this.colorTextureView != null) {
/*  60 */       this.colorTextureView.close();
/*  61 */       this.colorTextureView = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void copyDepthFrom(RenderTarget source) {
/*  66 */     RenderSystem.assertOnRenderThread();
/*  68 */     if (this.depthTexture == null)
/*  69 */       throw new IllegalStateException("Trying to copy depth texture to a RenderTarget without a depth texture"); 
/*  71 */     if (source.depthTexture == null)
/*  72 */       throw new IllegalStateException("Trying to copy depth texture from a RenderTarget without a depth texture"); 
/*  74 */     RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(source.depthTexture, this.depthTexture, 0, 0, 0, 0, 0, this.width, this.height);
/*     */   }
/*     */   
/*     */   public void createBuffers(int width, int height) {
/*  78 */     RenderSystem.assertOnRenderThread();
/*  80 */     GpuDevice device = RenderSystem.getDevice();
/*  81 */     int maxTextureSize = device.getDeviceInfo().limits().maxTextureSize();
/*  82 */     if (width <= 0 || width > maxTextureSize || height <= 0 || height > maxTextureSize)
/*  83 */       throw new IllegalArgumentException("Window " + width + "x" + height + " size out of bounds (max. size: " + maxTextureSize + ")"); 
/*  86 */     this.width = width;
/*  87 */     this.height = height;
/*  89 */     if (this.useDepth) {
/*  90 */       this.depthTexture = device.createTexture(() -> this.label + " / Depth", 15, GpuFormat.D32_FLOAT, width, height, 1, 1);
/*  91 */       this.depthTextureView = device.createTextureView(this.depthTexture);
/*     */     } 
/*  94 */     this.colorTexture = device.createTexture(() -> this.label + " / Color", 15, this.format, width, height, 1, 1);
/*  95 */     this.colorTextureView = device.createTextureView(this.colorTexture);
/*     */   }
/*     */   
/*     */   public void blitAndBlendToTexture(GpuTextureView output, GpuTextureView outputDepth) {
/*  99 */     RenderSystem.assertOnRenderThread();
/* 101 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Blit render target", output, Optional.empty(), outputDepth, OptionalDouble.empty());
/*     */     try {
/* 102 */       renderPass.setPipeline(RenderPipelines.ENTITY_OUTLINE_BLIT);
/* 103 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 104 */       renderPass.bindTexture("InSampler", this.colorTextureView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
/* 105 */       renderPass.draw(3, 1, 0, 0);
/* 106 */       if (renderPass != null)
/* 106 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public GpuTexture getColorTexture() {
/* 110 */     return this.colorTexture;
/*     */   }
/*     */   
/*     */   public GpuTextureView getColorTextureView() {
/* 114 */     return this.colorTextureView;
/*     */   }
/*     */   
/*     */   public GpuTexture getDepthTexture() {
/* 118 */     return this.depthTexture;
/*     */   }
/*     */   
/*     */   public GpuTextureView getDepthTextureView() {
/* 122 */     return this.depthTextureView;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\pipeline\RenderTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */