/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ public enum Orientation {
/* 102 */   HORIZONTAL, VERTICAL;
/*     */   
/*     */   private void setSpacing(GridLayout gridLayout, int spacing) {
/* 105 */     switch (ordinal()) {
/*     */       case 0:
/* 106 */         gridLayout.columnSpacing(spacing);
/*     */         break;
/*     */       case 1:
/* 107 */         gridLayout.rowSpacing(spacing);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(GridLayout gridLayout, T child, int index, LayoutSettings cellSettings) {
/* 112 */     switch (ordinal()) {
/*     */       default:
/* 112 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */         break;
/*     */     } 
/* 112 */     return 
/*     */       
/* 114 */       gridLayout.addChild(child, index, 0, cellSettings);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\layouts\LinearLayout$Orientation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */