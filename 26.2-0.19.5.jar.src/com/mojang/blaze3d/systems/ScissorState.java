/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ public class ScissorState {
/*    */   private boolean enabled;
/*    */   
/*    */   private int x;
/*    */   
/*    */   private int y;
/*    */   
/*    */   private int width;
/*    */   
/*    */   private int height;
/*    */   
/*    */   public ScissorState() {}
/*    */   
/*    */   public ScissorState(ScissorState state) {
/* 14 */     this.enabled = state.enabled;
/* 15 */     this.x = state.x;
/* 16 */     this.y = state.y;
/* 17 */     this.width = state.width;
/* 18 */     this.height = state.height;
/*    */   }
/*    */   
/*    */   public void enable(int x, int y, int width, int height) {
/* 22 */     this.enabled = true;
/* 23 */     this.x = x;
/* 24 */     this.y = y;
/* 25 */     this.width = width;
/* 26 */     this.height = height;
/*    */   }
/*    */   
/*    */   public void disable() {
/* 30 */     this.enabled = false;
/*    */   }
/*    */   
/*    */   public boolean enabled() {
/* 34 */     return this.enabled;
/*    */   }
/*    */   
/*    */   public int x() {
/* 38 */     return this.x;
/*    */   }
/*    */   
/*    */   public int y() {
/* 42 */     return this.y;
/*    */   }
/*    */   
/*    */   public int width() {
/* 46 */     return this.width;
/*    */   }
/*    */   
/*    */   public int height() {
/* 50 */     return this.height;
/*    */   }
/*    */   
/*    */   public void setFrom(ScissorState state) {
/* 54 */     this.enabled = state.enabled;
/* 55 */     this.x = state.x;
/* 56 */     this.y = state.y;
/* 57 */     this.width = state.width;
/* 58 */     this.height = state.height;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\ScissorState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */