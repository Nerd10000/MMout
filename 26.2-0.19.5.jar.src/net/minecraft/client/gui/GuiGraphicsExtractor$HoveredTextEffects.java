/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ public enum HoveredTextEffects {
/* 860 */   NONE(false, false),
/* 861 */   TOOLTIP_ONLY(true, false),
/* 862 */   TOOLTIP_AND_CURSOR(true, true);
/*     */   
/*     */   public final boolean allowTooltip;
/*     */   
/*     */   public final boolean allowCursorChanges;
/*     */   
/*     */   HoveredTextEffects(boolean allowTooltip, boolean allowCursorChanges) {
/* 869 */     this.allowTooltip = allowTooltip;
/* 870 */     this.allowCursorChanges = allowCursorChanges;
/*     */   }
/*     */   
/*     */   public static HoveredTextEffects notClickable(boolean canTooltip) {
/* 874 */     return canTooltip ? TOOLTIP_ONLY : NONE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\GuiGraphicsExtractor$HoveredTextEffects.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */