/*     */ package net.minecraft.client.gui.screens.multiplayer;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.LoadingDotsWidget;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ 
/*     */ public class LANHeader extends ServerSelectionList.Entry {
/* 159 */   private final Minecraft minecraft = Minecraft.getInstance();
/*     */   
/* 160 */   private final LoadingDotsWidget loadingDotsWidget = new LoadingDotsWidget(this.minecraft.font, ServerSelectionList.SCANNING_LABEL);
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 165 */     this.loadingDotsWidget.setPosition(getContentXMiddle() - this.minecraft.font.width((FormattedText)ServerSelectionList.SCANNING_LABEL) / 2, getContentY());
/* 166 */     this.loadingDotsWidget.extractRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 171 */     return ServerSelectionList.SCANNING_LABEL;
/*     */   }
/*     */   
/*     */   protected boolean matches(ServerSelectionList.Entry other) {
/* 176 */     return other instanceof LANHeader;
/*     */   }
/*     */   
/*     */   public void join() {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\multiplayer\ServerSelectionList$LANHeader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */