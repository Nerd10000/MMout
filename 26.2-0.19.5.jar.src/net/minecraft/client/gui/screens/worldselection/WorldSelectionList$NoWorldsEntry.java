/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public final class NoWorldsEntry extends WorldSelectionList.Entry {
/*     */   private final StringWidget stringWidget;
/*     */   
/*     */   public NoWorldsEntry(Component component, Font font) {
/* 296 */     this.stringWidget = new StringWidget(component, font);
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 301 */     return this.stringWidget.getMessage();
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 306 */     this.stringWidget.setPosition(getContentXMiddle() - this.stringWidget.getWidth() / 2, getContentYMiddle() - this.stringWidget.getHeight() / 2);
/* 307 */     this.stringWidget.extractRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldSelectionList$NoWorldsEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */