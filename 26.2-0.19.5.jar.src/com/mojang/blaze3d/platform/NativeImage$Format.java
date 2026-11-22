/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ public enum Format {
/* 505 */   RGBA(4, true, true, true, false, true, 0, 8, 16, 255, 24, true),
/* 511 */   RGB(3, true, true, true, false, false, 0, 8, 16, 255, 255, true),
/* 517 */   LUMINANCE_ALPHA(2, false, false, false, true, true, 255, 255, 255, 0, 8, true),
/* 523 */   LUMINANCE(1, false, false, false, true, false, 0, 0, 0, 0, 255, true);
/*     */   
/*     */   private final int components;
/*     */   
/*     */   private final boolean hasRed;
/*     */   
/*     */   private final boolean hasGreen;
/*     */   
/*     */   private final boolean hasBlue;
/*     */   
/*     */   private final boolean hasLuminance;
/*     */   
/*     */   private final boolean hasAlpha;
/*     */   
/*     */   private final int redOffset;
/*     */   
/*     */   private final int greenOffset;
/*     */   
/*     */   private final int blueOffset;
/*     */   
/*     */   private final int luminanceOffset;
/*     */   
/*     */   private final int alphaOffset;
/*     */   
/*     */   private final boolean supportedByStb;
/*     */   
/*     */   Format(int components, boolean hasRed, boolean hasGreen, boolean hasBlue, boolean hasLuminance, boolean hasAlpha, int redOffset, int greenOffset, int blueOffset, int luminanceOffset, int alphaOffset, boolean supportedByStb) {
/* 548 */     this.components = components;
/* 549 */     this.hasRed = hasRed;
/* 550 */     this.hasGreen = hasGreen;
/* 551 */     this.hasBlue = hasBlue;
/* 552 */     this.hasLuminance = hasLuminance;
/* 553 */     this.hasAlpha = hasAlpha;
/* 554 */     this.redOffset = redOffset;
/* 555 */     this.greenOffset = greenOffset;
/* 556 */     this.blueOffset = blueOffset;
/* 557 */     this.luminanceOffset = luminanceOffset;
/* 558 */     this.alphaOffset = alphaOffset;
/* 559 */     this.supportedByStb = supportedByStb;
/*     */   }
/*     */   
/*     */   public int components() {
/* 563 */     return this.components;
/*     */   }
/*     */   
/*     */   public boolean hasRed() {
/* 567 */     return this.hasRed;
/*     */   }
/*     */   
/*     */   public boolean hasGreen() {
/* 571 */     return this.hasGreen;
/*     */   }
/*     */   
/*     */   public boolean hasBlue() {
/* 575 */     return this.hasBlue;
/*     */   }
/*     */   
/*     */   public boolean hasLuminance() {
/* 579 */     return this.hasLuminance;
/*     */   }
/*     */   
/*     */   public boolean hasAlpha() {
/* 583 */     return this.hasAlpha;
/*     */   }
/*     */   
/*     */   public int redOffset() {
/* 587 */     return this.redOffset;
/*     */   }
/*     */   
/*     */   public int greenOffset() {
/* 591 */     return this.greenOffset;
/*     */   }
/*     */   
/*     */   public int blueOffset() {
/* 595 */     return this.blueOffset;
/*     */   }
/*     */   
/*     */   public int luminanceOffset() {
/* 599 */     return this.luminanceOffset;
/*     */   }
/*     */   
/*     */   public int alphaOffset() {
/* 603 */     return this.alphaOffset;
/*     */   }
/*     */   
/*     */   public boolean hasLuminanceOrRed() {
/* 607 */     return (this.hasLuminance || this.hasRed);
/*     */   }
/*     */   
/*     */   public boolean hasLuminanceOrGreen() {
/* 611 */     return (this.hasLuminance || this.hasGreen);
/*     */   }
/*     */   
/*     */   public boolean hasLuminanceOrBlue() {
/* 615 */     return (this.hasLuminance || this.hasBlue);
/*     */   }
/*     */   
/*     */   public boolean hasLuminanceOrAlpha() {
/* 619 */     return (this.hasLuminance || this.hasAlpha);
/*     */   }
/*     */   
/*     */   public int luminanceOrRedOffset() {
/* 623 */     return this.hasLuminance ? this.luminanceOffset : this.redOffset;
/*     */   }
/*     */   
/*     */   public int luminanceOrGreenOffset() {
/* 627 */     return this.hasLuminance ? this.luminanceOffset : this.greenOffset;
/*     */   }
/*     */   
/*     */   public int luminanceOrBlueOffset() {
/* 631 */     return this.hasLuminance ? this.luminanceOffset : this.blueOffset;
/*     */   }
/*     */   
/*     */   public int luminanceOrAlphaOffset() {
/* 635 */     return this.hasLuminance ? this.luminanceOffset : this.alphaOffset;
/*     */   }
/*     */   
/*     */   public boolean supportedByStb() {
/* 639 */     return this.supportedByStb;
/*     */   }
/*     */   
/*     */   private static Format getStbFormat(int i) {
/* 643 */     switch (i) {
/*     */       case 1:
/* 645 */         return LUMINANCE;
/*     */       case 2:
/* 647 */         return LUMINANCE_ALPHA;
/*     */       case 3:
/* 649 */         return RGB;
/*     */     } 
/* 652 */     return RGBA;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\NativeImage$Format.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */