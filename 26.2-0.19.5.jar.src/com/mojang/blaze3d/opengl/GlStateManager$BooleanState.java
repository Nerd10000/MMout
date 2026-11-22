/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import org.lwjgl.opengl.GL33C;
/*     */ 
/*     */ class BooleanState {
/*     */   private final int state;
/*     */   
/*     */   private boolean enabled;
/*     */   
/*     */   public BooleanState(int state) {
/* 615 */     this.state = state;
/*     */   }
/*     */   
/*     */   public void disable() {
/* 619 */     setEnabled(false);
/*     */   }
/*     */   
/*     */   public void enable() {
/* 623 */     setEnabled(true);
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/* 627 */     RenderSystem.assertOnRenderThread();
/* 628 */     if (enabled != this.enabled) {
/* 629 */       this.enabled = enabled;
/* 630 */       if (enabled) {
/* 631 */         GL33C.glEnable(this.state);
/*     */       } else {
/* 633 */         GL33C.glDisable(this.state);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlStateManager$BooleanState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */