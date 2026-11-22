/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ 
/*     */ class HeaderEntry extends CreateFlatWorldScreen.DetailsList.Entry {
/*     */   private final Font font;
/*     */   
/*     */   public HeaderEntry(Font font) {
/* 261 */     this.font = font;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 266 */     graphics.text(this.font, CreateFlatWorldScreen.DetailsList.LAYER_MATERIAL_TITLE, getContentX(), getContentY(), -1);
/* 267 */     graphics.text(this.font, CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE, getContentRight() - this.font.width((FormattedText)CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE), getContentY(), -1);
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 272 */     return (Component)CommonComponents.joinForNarration(new Component[] { CreateFlatWorldScreen.DetailsList.LAYER_MATERIAL_TITLE, CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\CreateFlatWorldScreen$DetailsList$HeaderEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */