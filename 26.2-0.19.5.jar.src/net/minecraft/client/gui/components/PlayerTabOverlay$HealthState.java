/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ class HealthState {
/*     */   private static final long DISPLAY_UPDATE_DELAY = 20L;
/*     */   
/*     */   private static final long DECREASE_BLINK_DURATION = 20L;
/*     */   
/*     */   private static final long INCREASE_BLINK_DURATION = 10L;
/*     */   
/*     */   private int lastValue;
/*     */   
/*     */   private int displayedValue;
/*     */   
/*     */   private long lastUpdateTick;
/*     */   
/*     */   private long blinkUntilTick;
/*     */   
/*     */   public HealthState(int value) {
/* 369 */     this.displayedValue = value;
/* 370 */     this.lastValue = value;
/*     */   }
/*     */   
/*     */   public void update(int value, long tick) {
/* 374 */     if (value != this.lastValue) {
/* 375 */       long blinkDuration = (value < this.lastValue) ? 20L : 10L;
/* 376 */       this.blinkUntilTick = tick + blinkDuration;
/* 377 */       this.lastValue = value;
/* 378 */       this.lastUpdateTick = tick;
/*     */     } 
/* 381 */     if (tick - this.lastUpdateTick > 20L)
/* 382 */       this.displayedValue = value; 
/*     */   }
/*     */   
/*     */   public int displayedValue() {
/* 387 */     return this.displayedValue;
/*     */   }
/*     */   
/*     */   public boolean isBlinking(long tick) {
/* 391 */     return (this.blinkUntilTick > tick && (this.blinkUntilTick - tick) % 6L >= 3L);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\PlayerTabOverlay$HealthState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */