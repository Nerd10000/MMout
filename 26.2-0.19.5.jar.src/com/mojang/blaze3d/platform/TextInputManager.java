/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import org.lwjgl.glfw.GLFW;
/*    */ 
/*    */ public class TextInputManager {
/*    */   private final Window window;
/*    */   
/*    */   private boolean textInputEnabled;
/*    */   
/*    */   private boolean imeRequested;
/*    */   
/*    */   private volatile boolean imeStatusChanged = true;
/*    */   
/*    */   private boolean cachedIMEStatus;
/*    */   
/*    */   public TextInputManager(Window window) {
/* 21 */     this.window = window;
/*    */   }
/*    */   
/*    */   public void setTextInputArea(int x0, int y0, int x1, int y1) {
/* 25 */     int guiScale = this.window.getGuiScale();
/* 27 */     GLFW.glfwSetPreeditCursorRectangle(this.window.handle(), x0 * guiScale, y0 * guiScale, (x1 - x0) * guiScale, (y1 - y0) * guiScale);
/*    */   }
/*    */   
/*    */   public void notifyIMEChanged() {
/* 34 */     this.imeStatusChanged = true;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 38 */     if (this.textInputEnabled) {
/* 39 */       tickDuringTextInput();
/*    */     } else {
/* 41 */       tickOutsideTextInput();
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean getIMEStatus() {
/* 46 */     if (this.imeStatusChanged) {
/* 47 */       this.imeStatusChanged = false;
/* 48 */       this.cachedIMEStatus = (GLFW.glfwGetInputMode(this.window.handle(), 208903) == 1);
/*    */     } 
/* 50 */     return this.cachedIMEStatus;
/*    */   }
/*    */   
/*    */   private void tickOutsideTextInput() {
/* 58 */     if (this.window.isFocused() && getIMEStatus())
/* 59 */       setIMEInputMode(false); 
/*    */   }
/*    */   
/*    */   private void tickDuringTextInput() {
/* 65 */     this.imeRequested = getIMEStatus();
/*    */   }
/*    */   
/*    */   public void startTextInput() {
/* 70 */     this.textInputEnabled = true;
/* 72 */     if (this.imeRequested)
/* 73 */       setIMEInputMode(true); 
/*    */   }
/*    */   
/*    */   public void stopTextInput() {
/* 78 */     this.textInputEnabled = false;
/*    */   }
/*    */   
/*    */   public void onTextInputFocusChange(boolean focused) {
/* 82 */     if (focused) {
/* 83 */       startTextInput();
/*    */     } else {
/* 85 */       stopTextInput();
/*    */     } 
/*    */   }
/*    */   
/*    */   private void setIMEInputMode(boolean value) {
/* 91 */     GLFW.glfwSetInputMode(this.window.handle(), 208903, GLX.glfwBool(value));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\TextInputManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */