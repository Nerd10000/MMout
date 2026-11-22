/*    */ package com.mojang.blaze3d;
/*    */ 
/*    */ import org.lwjgl.glfw.GLFW;
/*    */ import org.lwjgl.system.MemoryUtil;
/*    */ 
/*    */ public class Blaze3D {
/*    */   public static void youJustLostTheGame() {
/*  9 */     MemoryUtil.memSet(0L, 0, 1L);
/*    */   }
/*    */   
/*    */   public static double getTime() {
/* 13 */     return GLFW.glfwGetTime();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\Blaze3D.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */