/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.screens.LoadingDotsText;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class LoadingHeader extends WorldSelectionList.Entry {
/* 662 */   private static final Component LOADING_LABEL = (Component)Component.translatable("selectWorld.loading_list");
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   public LoadingHeader(Minecraft minecraft) {
/* 667 */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 672 */     int labelX = ((this.minecraft.gui.screen()).width - this.minecraft.font.width((FormattedText)LOADING_LABEL)) / 2;
/* 673 */     Objects.requireNonNull(this.minecraft.font);
/* 673 */     int labelY = getContentY() + (getContentHeight() - 9) / 2;
/* 674 */     graphics.text(this.minecraft.font, LOADING_LABEL, labelX, labelY, -1);
/* 676 */     String dots = LoadingDotsText.get(Util.getMillis());
/* 677 */     int dotsX = ((this.minecraft.gui.screen()).width - this.minecraft.font.width(dots)) / 2;
/* 678 */     Objects.requireNonNull(this.minecraft.font);
/* 678 */     int dotsY = labelY + 9;
/* 679 */     graphics.text(this.minecraft.font, dots, dotsX, dotsY, -8355712);
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 684 */     return LOADING_LABEL;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldSelectionList$LoadingHeader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */