/*     */ package net.minecraft.client.gui.screens.debug;
/*     */ 
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ 
/*     */ class null implements NarratableEntry {
/*     */   public NarratableEntry.NarrationPriority narrationPriority() {
/* 208 */     return NarratableEntry.NarrationPriority.HOVERED;
/*     */   }
/*     */   
/*     */   public void updateNarration(NarrationElementOutput output) {
/* 213 */     output.add(NarratedElementType.TITLE, DebugOptionsScreen.CategoryEntry.this.category);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\debug\DebugOptionsScreen$CategoryEntry$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */