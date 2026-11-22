/*    */ package com.mojang.blaze3d.platform.cursor;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Window;
/*    */ import org.lwjgl.glfw.GLFW;
/*    */ 
/*    */ public class CursorType {
/*  8 */   public static final CursorType DEFAULT = new CursorType("default", 0L);
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final long handle;
/*    */   
/*    */   private CursorType(String name, long handle) {
/* 14 */     this.name = name;
/* 15 */     this.handle = handle;
/*    */   }
/*    */   
/*    */   public void select(Window window) {
/* 19 */     GLFW.glfwSetCursor(window.handle(), this.handle);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 24 */     return this.name;
/*    */   }
/*    */   
/*    */   public static CursorType createStandardCursor(int shape, String name, CursorType fallback) {
/* 31 */     long handle = GLFW.glfwCreateStandardCursor(shape);
/* 32 */     if (handle == 0L)
/* 34 */       return fallback; 
/* 37 */     return new CursorType(name, handle);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\cursor\CursorType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */