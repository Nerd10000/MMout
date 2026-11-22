/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import com.mojang.blaze3d.GLFWErrorScope;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import net.minecraft.util.StringDecomposer;
/*    */ import org.lwjgl.BufferUtils;
/*    */ import org.lwjgl.glfw.GLFW;
/*    */ import org.lwjgl.glfw.GLFWErrorCallbackI;
/*    */ import org.lwjgl.system.MemoryUtil;
/*    */ 
/*    */ public class ClipboardManager {
/*    */   public static final int FORMAT_UNAVAILABLE = 65545;
/*    */   
/* 16 */   private final ByteBuffer clipboardScratchBuffer = BufferUtils.createByteBuffer(8192);
/*    */   
/*    */   public String getClipboard(Window window, GLFWErrorCallbackI errorCallback) {
/* 19 */     GLFWErrorScope ignored = new GLFWErrorScope(errorCallback);
/*    */     try {
/* 20 */       String clipboard = GLFW.glfwGetClipboardString(window.handle());
/* 21 */       clipboard = (clipboard != null) ? StringDecomposer.filterBrokenSurrogates(clipboard) : "";
/* 22 */       String str1 = clipboard;
/* 23 */       ignored.close();
/*    */       return str1;
/*    */     } catch (Throwable throwable) {
/*    */       try {
/*    */         ignored.close();
/*    */       } catch (Throwable throwable1) {
/*    */         throwable.addSuppressed(throwable1);
/*    */       } 
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void pushClipboard(Window window, ByteBuffer buffer, byte[] data) {
/* 27 */     buffer.clear();
/* 28 */     buffer.put(data);
/* 29 */     buffer.put((byte)0);
/* 30 */     buffer.flip();
/* 31 */     GLFW.glfwSetClipboardString(window.handle(), buffer);
/*    */   }
/*    */   
/*    */   public void setClipboard(Window window, String clipboard) {
/* 35 */     byte[] encoded = clipboard.getBytes(StandardCharsets.UTF_8);
/* 37 */     int encodedLength = encoded.length + 1;
/* 38 */     if (encodedLength < this.clipboardScratchBuffer.capacity()) {
/* 39 */       pushClipboard(window, this.clipboardScratchBuffer, encoded);
/*    */     } else {
/* 41 */       ByteBuffer buffer = MemoryUtil.memAlloc(encodedLength);
/*    */       try {
/* 43 */         pushClipboard(window, buffer, encoded);
/*    */       } finally {
/* 45 */         MemoryUtil.memFree(buffer);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\ClipboardManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */