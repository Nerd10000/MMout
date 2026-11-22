/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ public enum DisplayMode {
/* 776 */   BACKGROUND(false, false),
/* 777 */   FOREGROUND(true, false),
/* 778 */   FOREGROUND_RESTRICTED(true, true);
/*     */   
/*     */   public final boolean foreground;
/*     */   
/*     */   public final boolean showRestrictedPrompt;
/*     */   
/*     */   DisplayMode(boolean foreground, boolean showRestrictedPrompt) {
/* 785 */     this.foreground = foreground;
/* 786 */     this.showRestrictedPrompt = showRestrictedPrompt;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ChatComponent$DisplayMode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */