/*     */ package net.minecraft.commands;
/*     */ 
/*     */ public enum CommandSelection {
/* 210 */   ALL(true, true),
/* 211 */   DEDICATED(false, true),
/* 212 */   INTEGRATED(true, false);
/*     */   
/*     */   private final boolean includeIntegrated;
/*     */   
/*     */   private final boolean includeDedicated;
/*     */   
/*     */   CommandSelection(boolean includeIntegrated, boolean includeDedicated) {
/* 219 */     this.includeIntegrated = includeIntegrated;
/* 220 */     this.includeDedicated = includeDedicated;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\Commands$CommandSelection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */