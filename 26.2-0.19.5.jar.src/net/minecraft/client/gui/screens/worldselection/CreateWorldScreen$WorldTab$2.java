/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ 
/*     */ class null implements CycleButton.ValueListSupplier<WorldCreationUiState.WorldTypeEntry> {
/*     */   public List<WorldCreationUiState.WorldTypeEntry> getSelectedList() {
/* 251 */     return CycleButton.DEFAULT_ALT_LIST_SELECTOR.getAsBoolean() ? this.this$1.this$0.uiState.getAltPresetList() : this.this$1.this$0.uiState.getNormalPresetList();
/*     */   }
/*     */   
/*     */   public List<WorldCreationUiState.WorldTypeEntry> getDefaultList() {
/* 256 */     return this.this$1.this$0.uiState.getNormalPresetList();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\CreateWorldScreen$WorldTab$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */