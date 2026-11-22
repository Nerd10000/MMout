/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ public enum Orientation {
/* 112 */   HORIZONTAL, VERTICAL;
/*     */   
/*     */   private int getPrimaryLength(LayoutElement widget) {
/* 115 */     switch (ordinal()) {
/*     */       default:
/* 115 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */         break;
/*     */     } 
/* 115 */     return 
/*     */       
/* 117 */       widget.getHeight();
/*     */   }
/*     */   
/*     */   private int getPrimaryLength(EqualSpacingLayout.ChildContainer childContainer) {
/* 122 */     switch (ordinal()) {
/*     */       default:
/* 122 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */         break;
/*     */     } 
/* 122 */     return 
/*     */       
/* 124 */       childContainer.getHeight();
/*     */   }
/*     */   
/*     */   private int getSecondaryLength(LayoutElement widget) {
/* 129 */     switch (ordinal()) {
/*     */       default:
/* 129 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */         break;
/*     */     } 
/* 129 */     return 
/*     */       
/* 131 */       widget.getWidth();
/*     */   }
/*     */   
/*     */   private int getSecondaryLength(EqualSpacingLayout.ChildContainer childContainer) {
/* 136 */     switch (ordinal()) {
/*     */       default:
/* 136 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */         break;
/*     */     } 
/* 136 */     return 
/*     */       
/* 138 */       childContainer.getWidth();
/*     */   }
/*     */   
/*     */   private void setPrimaryPosition(EqualSpacingLayout.ChildContainer childContainer, int position) {
/* 143 */     switch (ordinal()) {
/*     */       case 0:
/* 144 */         childContainer.setX(position, childContainer.getWidth());
/*     */         break;
/*     */       case 1:
/* 145 */         childContainer.setY(position, childContainer.getHeight());
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setSecondaryPosition(EqualSpacingLayout.ChildContainer childContainer, int position, int availableSpace) {
/* 150 */     switch (ordinal()) {
/*     */       case 0:
/* 151 */         childContainer.setY(position, availableSpace);
/*     */         break;
/*     */       case 1:
/* 152 */         childContainer.setX(position, availableSpace);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getPrimaryPosition(LayoutElement widget) {
/* 157 */     switch (ordinal()) {
/*     */       default:
/* 157 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */         break;
/*     */     } 
/* 157 */     return 
/*     */       
/* 159 */       widget.getY();
/*     */   }
/*     */   
/*     */   private int getSecondaryPosition(LayoutElement widget) {
/* 164 */     switch (ordinal()) {
/*     */       default:
/* 164 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */         break;
/*     */     } 
/* 164 */     return 
/*     */       
/* 166 */       widget.getX();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\layouts\EqualSpacingLayout$Orientation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */