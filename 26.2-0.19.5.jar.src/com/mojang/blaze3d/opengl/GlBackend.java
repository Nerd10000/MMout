/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.GLFWErrorCapture;
/*    */ import com.mojang.blaze3d.platform.MacosUtil;
/*    */ import com.mojang.blaze3d.shaders.GpuDebugOptions;
/*    */ import com.mojang.blaze3d.shaders.ShaderSource;
/*    */ import com.mojang.blaze3d.systems.BackendCreationException;
/*    */ import com.mojang.blaze3d.systems.GpuBackend;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ import java.util.Locale;
/*    */ import org.lwjgl.glfw.GLFW;
/*    */ 
/*    */ public class GlBackend implements GpuBackend {
/*    */   private static final int VERSION_MAJOR = 3;
/*    */   
/*    */   private static final int VERSION_MINOR = 3;
/*    */   
/*    */   public String getName() {
/* 21 */     return "OpenGL";
/*    */   }
/*    */   
/*    */   public void setWindowHints() {
/* 26 */     GLFW.glfwWindowHint(139265, 196609);
/* 27 */     GLFW.glfwWindowHint(139275, 221185);
/* 28 */     GLFW.glfwWindowHint(139266, 3);
/* 29 */     GLFW.glfwWindowHint(139267, 3);
/* 30 */     GLFW.glfwWindowHint(139272, 204801);
/* 31 */     GLFW.glfwWindowHint(139270, 1);
/*    */   }
/*    */   
/*    */   public void handleWindowCreationErrors(GLFWErrorCapture.Error error) throws BackendCreationException {
/* 36 */     if (error != null) {
/* 37 */       if (error.error() == 65542)
/* 38 */         throw new BackendCreationException("Driver does not support OpenGL", BackendCreationException.Reason.OPENGL_MISSING); 
/* 39 */       if (error.error() == 65543)
/* 40 */         throw new BackendCreationException("Driver does not support OpenGL 3.3", BackendCreationException.Reason.OPENGL_MISSING); 
/* 42 */       throw new BackendCreationException(String.format(Locale.ROOT, "GLFW_ERROR: 0x%X", new Object[] { Integer.valueOf(error.error()) }), BackendCreationException.Reason.OPENGL_MISSING);
/*    */     } 
/* 45 */     throw new BackendCreationException("Failed to create window with OpenGL context", BackendCreationException.Reason.OPENGL_MISSING);
/*    */   }
/*    */   
/*    */   public GpuDevice createDevice(long window, ShaderSource defaultShaderSource, GpuDebugOptions debugOptions, Runnable criticalShaderLoader) {
/* 51 */     if (MacosUtil.IS_MACOS)
/* 52 */       MacosUtil.setWindowColorSpaceForOpenGLBecauseGLFWDoesnt(window); 
/* 54 */     return new GpuDevice(new GlDevice(window, defaultShaderSource, debugOptions), criticalShaderLoader);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlBackend.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */