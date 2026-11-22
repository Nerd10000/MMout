/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.CommandEncoderBackend;
/*    */ import com.mojang.blaze3d.systems.GpuSurface;
/*    */ import com.mojang.blaze3d.systems.GpuSurfaceBackend;
/*    */ import com.mojang.blaze3d.systems.SurfaceException;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import java.util.Collection;
/*    */ import java.util.EnumSet;
/*    */ import java.util.Set;
/*    */ import org.lwjgl.glfw.GLFW;
/*    */ 
/*    */ public class GlSurface implements GpuSurfaceBackend {
/* 17 */   private static final Set<GpuSurface.PresentMode> SUPPORTED_PRESENT_MODES = EnumSet.of(GpuSurface.PresentMode.FIFO, GpuSurface.PresentMode.IMMEDIATE);
/*    */   
/*    */   private final long windowHandle;
/*    */   
/*    */   private int swapchainWidth;
/*    */   
/*    */   private int swapchainHeight;
/*    */   
/*    */   public GlSurface(long windowHandle) {
/* 24 */     this.windowHandle = windowHandle;
/*    */   }
/*    */   
/*    */   public void configure(GpuSurface.Configuration config) throws SurfaceException {
/* 29 */     GLFW.glfwSwapInterval((config.presentMode() == GpuSurface.PresentMode.FIFO) ? 1 : 0);
/* 30 */     this.swapchainWidth = config.width();
/* 31 */     this.swapchainHeight = config.height();
/*    */   }
/*    */   
/*    */   public boolean isSuboptimal() {
/* 36 */     return false;
/*    */   }
/*    */   
/*    */   public void acquireNextTexture() {}
/*    */   
/*    */   public void blitFromTexture(CommandEncoderBackend commandEncoder, GpuTextureView textureView) {
/* 46 */     ((GlCommandEncoder)commandEncoder).presentTexture(textureView, this.swapchainWidth, this.swapchainHeight);
/*    */   }
/*    */   
/*    */   public void present() {
/* 51 */     GLFW.glfwSwapBuffers(this.windowHandle);
/*    */   }
/*    */   
/*    */   public void close() {}
/*    */   
/*    */   public Collection<GpuSurface.PresentMode> supportedPresentModes() {
/* 61 */     return SUPPORTED_PRESENT_MODES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlSurface.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */