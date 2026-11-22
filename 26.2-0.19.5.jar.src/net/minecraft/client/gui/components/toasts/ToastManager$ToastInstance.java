/*     */ package net.minecraft.client.gui.components.toasts;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ class ToastInstance<T extends Toast> {
/*     */   private static final long SLIDE_ANIMATION_DURATION_MS = 600L;
/*     */   
/*     */   private final T toast;
/*     */   
/*     */   private final int firstSlotIndex;
/*     */   
/*     */   private final int occupiedSlotCount;
/*     */   
/*     */   private long animationStartTime;
/*     */   
/*     */   private long becameFullyVisibleAt;
/*     */   
/*     */   private Toast.Visibility visibility;
/*     */   
/*     */   private long fullyVisibleFor;
/*     */   
/*     */   private float visiblePortion;
/*     */   
/*     */   protected boolean hasFinishedRendering;
/*     */   
/*     */   private ToastInstance(T toast, int firstSlotIndex, int occupiedSlotCount) {
/* 198 */     this.toast = toast;
/* 199 */     this.firstSlotIndex = firstSlotIndex;
/* 200 */     this.occupiedSlotCount = occupiedSlotCount;
/* 201 */     resetToast();
/*     */   }
/*     */   
/*     */   public T getToast() {
/* 205 */     return this.toast;
/*     */   }
/*     */   
/*     */   public void resetToast() {
/* 209 */     this.animationStartTime = -1L;
/* 210 */     this.becameFullyVisibleAt = -1L;
/* 211 */     this.visibility = Toast.Visibility.HIDE;
/* 212 */     this.fullyVisibleFor = 0L;
/* 213 */     this.visiblePortion = 0.0F;
/* 214 */     this.hasFinishedRendering = false;
/*     */   }
/*     */   
/*     */   public boolean hasFinishedRendering() {
/* 218 */     return this.hasFinishedRendering;
/*     */   }
/*     */   
/*     */   private void calculateVisiblePortion(long now) {
/* 222 */     float animationProgress = Mth.clamp((float)(now - this.animationStartTime) / 600.0F, 0.0F, 1.0F);
/* 223 */     animationProgress *= animationProgress;
/* 224 */     if (this.visibility == Toast.Visibility.HIDE) {
/* 225 */       this.visiblePortion = 1.0F - animationProgress;
/*     */     } else {
/* 227 */       this.visiblePortion = animationProgress;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void update() {
/* 232 */     long now = Util.getMillis();
/* 234 */     if (this.animationStartTime == -1L) {
/* 235 */       this.animationStartTime = now;
/* 236 */       this.visibility = Toast.Visibility.SHOW;
/*     */     } 
/* 239 */     if (this.visibility == Toast.Visibility.SHOW && now - this.animationStartTime <= 600L)
/* 240 */       this.becameFullyVisibleAt = now; 
/* 243 */     this.fullyVisibleFor = now - this.becameFullyVisibleAt;
/* 244 */     calculateVisiblePortion(now);
/* 246 */     this.toast.update(ToastManager.this, this.fullyVisibleFor);
/* 248 */     Toast.Visibility wantedVisibility = this.toast.getWantedVisibility();
/* 250 */     if (wantedVisibility != this.visibility) {
/* 252 */       this.animationStartTime = now - (int)((1.0F - this.visiblePortion) * 600.0F);
/* 253 */       this.visibility = wantedVisibility;
/*     */     } 
/* 255 */     boolean wasAlreadyFinishedRendering = this.hasFinishedRendering;
/* 256 */     this.hasFinishedRendering = (this.visibility == Toast.Visibility.HIDE && now - this.animationStartTime > 600L);
/* 257 */     if (this.hasFinishedRendering && !wasAlreadyFinishedRendering)
/* 258 */       this.toast.onFinishedRendering(); 
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int screenWidth) {
/* 263 */     if (this.hasFinishedRendering)
/*     */       return; 
/* 266 */     graphics.pose().pushMatrix();
/* 267 */     graphics.pose().translate(this.toast.xPos(screenWidth, this.visiblePortion), this.toast.yPos(this.firstSlotIndex));
/* 268 */     this.toast.extractRenderState(graphics, ToastManager.this.minecraft.font, this.fullyVisibleFor);
/* 269 */     graphics.pose().popMatrix();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\toasts\ToastManager$ToastInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */