/*     */ package com.mojang.blaze3d.systems;
/*     */ 
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ 
/*     */ public class GpuSurface implements AutoCloseable {
/*     */   private final GpuSurfaceBackend backend;
/*     */   
/*     */   private boolean hasImageAcquired = false;
/*     */   
/*     */   private boolean hasBlittedTexture = false;
/*     */   
/*  13 */   private Optional<Configuration> currentConfiguration = Optional.empty();
/*     */   
/*     */   public GpuSurface(GpuSurfaceBackend backend) {
/*  16 */     this.backend = backend;
/*     */   }
/*     */   
/*     */   public void close() {
/*  21 */     if (this.hasImageAcquired)
/*  22 */       throw new IllegalStateException("Cannot close a surface while it is acquired"); 
/*  24 */     this.backend.close();
/*     */   }
/*     */   
/*     */   public void configure(Configuration config) throws SurfaceException {
/*  28 */     if (this.hasImageAcquired)
/*  29 */       throw new IllegalStateException("Cannot configure a surface while it is acquired"); 
/*  31 */     if (!supportedPresentModes().contains(config.presentMode()))
/*  32 */       throw new SurfaceException("Surface does not support present mode " + String.valueOf(config.presentMode()) + " (supported: " + String.valueOf(supportedPresentModes()) + ")"); 
/*  34 */     this.backend.configure(config);
/*  35 */     this.currentConfiguration = Optional.of(config);
/*     */   }
/*     */   
/*     */   public Optional<Configuration> currentConfiguration() {
/*  39 */     return this.currentConfiguration;
/*     */   }
/*     */   
/*     */   public Collection<PresentMode> supportedPresentModes() {
/*  43 */     return this.backend.supportedPresentModes();
/*     */   }
/*     */   
/*     */   public boolean isSuboptimal() {
/*  47 */     return this.backend.isSuboptimal();
/*     */   }
/*     */   
/*     */   public boolean isAcquired() {
/*  51 */     return this.hasImageAcquired;
/*     */   }
/*     */   
/*     */   public void acquireNextTexture() throws SurfaceException {
/*  55 */     if (this.hasImageAcquired)
/*  56 */       throw new IllegalStateException("Cannot acquire a surface while it is already acquired"); 
/*  58 */     if (this.currentConfiguration.isEmpty())
/*  59 */       throw new IllegalStateException("Cannot acquire an unconfigured surface"); 
/*  61 */     this.backend.acquireNextTexture();
/*  62 */     this.hasImageAcquired = true;
/*  63 */     this.hasBlittedTexture = false;
/*     */   }
/*     */   
/*     */   public void blitFromTexture(CommandEncoder commandEncoder, GpuTextureView textureView) {
/*  67 */     if (commandEncoder.isInRenderPass())
/*  68 */       throw new IllegalStateException("Close the existing render pass before presenting with a command encoder"); 
/*  70 */     if (!textureView.texture().getFormat().hasColorAspect())
/*  71 */       throw new IllegalStateException("Cannot present a non-color texture!"); 
/*  73 */     if ((textureView.texture().usage() & 0x2) == 0)
/*  74 */       throw new IllegalStateException("Color texture must have USAGE_COPY_SRC to presented to the screen"); 
/*  76 */     if (textureView.texture().getDepthOrLayers() > 1)
/*  77 */       throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported for presentation"); 
/*  79 */     if (!this.hasImageAcquired)
/*  80 */       throw new IllegalStateException("Cannot present to an unacquired surface"); 
/*  82 */     if (this.hasBlittedTexture)
/*  83 */       throw new IllegalStateException("Already blitted to this frame!"); 
/*  87 */     this.backend.blitFromTexture(commandEncoder.backend(), textureView);
/*  88 */     this.hasBlittedTexture = true;
/*     */   }
/*     */   
/*     */   public void present() {
/*  92 */     if (!this.hasImageAcquired)
/*  93 */       throw new IllegalStateException("Cannot present to a surface if it isn't acquired"); 
/*  95 */     if (!this.hasBlittedTexture)
/*  96 */       throw new IllegalStateException("Must blit to surface before presenting!"); 
/*  98 */     this.backend.present();
/*  99 */     this.hasImageAcquired = false;
/*     */   }
/*     */   
/*     */   public static final class Configuration extends Record {
/*     */     private final int width;
/*     */     
/*     */     private final int height;
/*     */     
/*     */     private final GpuSurface.PresentMode presentMode;
/*     */     
/*     */     public Configuration(int width, int height, GpuSurface.PresentMode presentMode) {
/* 102 */       this.width = width;
/* 102 */       this.height = height;
/* 102 */       this.presentMode = presentMode;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/GpuSurface$Configuration;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #102	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/GpuSurface$Configuration;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/GpuSurface$Configuration;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #102	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/GpuSurface$Configuration;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/GpuSurface$Configuration;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #102	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/systems/GpuSurface$Configuration;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int width() {
/* 102 */       return this.width;
/*     */     }
/*     */     
/*     */     public int height() {
/* 102 */       return this.height;
/*     */     }
/*     */     
/*     */     public GpuSurface.PresentMode presentMode() {
/* 102 */       return this.presentMode;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum PresentMode {
/* 109 */     IMMEDIATE, MAILBOX, FIFO, FIFO_RELAXED;
/*     */     
/* 131 */     private static final PresentMode[] PRESENT_MODES_VSYNC = new PresentMode[] { FIFO_RELAXED, FIFO };
/*     */     
/* 136 */     private static final PresentMode[] PRESENT_MODES_NO_VSYNC = new PresentMode[] { IMMEDIATE, MAILBOX, FIFO };
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     public static PresentMode getSupportedVsyncMode(Collection<PresentMode> supportedModes, boolean vsync) {
/* 143 */       PresentMode[] preferred = vsync ? PRESENT_MODES_VSYNC : PRESENT_MODES_NO_VSYNC;
/* 144 */       for (PresentMode mode : preferred) {
/* 145 */         if (supportedModes.contains(mode))
/* 146 */           return mode; 
/*     */       } 
/* 150 */       throw new IllegalStateException("No supported presentation mode was found");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\GpuSurface.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */