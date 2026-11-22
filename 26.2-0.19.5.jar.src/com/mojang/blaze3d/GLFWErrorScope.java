/*    */ package com.mojang.blaze3d;
/*    */ 
/*    */ import org.lwjgl.glfw.GLFW;
/*    */ import org.lwjgl.glfw.GLFWErrorCallback;
/*    */ import org.lwjgl.glfw.GLFWErrorCallbackI;
/*    */ 
/*    */ public class GLFWErrorScope implements AutoCloseable {
/*    */   private final GLFWErrorCallback previousCallback;
/*    */   
/*    */   private final GLFWErrorCallback expectedCallback;
/*    */   
/*    */   public GLFWErrorScope(GLFWErrorCallbackI callback) {
/* 13 */     this.expectedCallback = GLFWErrorCallback.create(callback);
/* 14 */     this.previousCallback = GLFW.glfwSetErrorCallback((GLFWErrorCallbackI)this.expectedCallback);
/*    */   }
/*    */   
/*    */   public void close() {
/* 19 */     GLFWErrorCallback currentCallback = GLFW.glfwSetErrorCallback((GLFWErrorCallbackI)this.previousCallback);
/* 20 */     if (currentCallback == null || currentCallback.address() != this.expectedCallback.address())
/* 21 */       throw new IllegalStateException("GLFW error callback has unexpectedly changed during this scope!"); 
/* 23 */     currentCallback.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\GLFWErrorScope.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */