/*     */ package net.minecraft.client.gui.screens.reporting;
/*     */ 
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class DiscardReportWarningScreen extends WarningScreen {
/* 206 */   private static final Component TITLE = (Component)Component.translatable("gui.abuseReport.discard.title").withStyle(ChatFormatting.BOLD);
/*     */   
/* 207 */   private static final Component MESSAGE = (Component)Component.translatable("gui.abuseReport.discard.content");
/*     */   
/* 208 */   private static final Component RETURN = (Component)Component.translatable("gui.abuseReport.discard.return");
/*     */   
/* 209 */   private static final Component DRAFT = (Component)Component.translatable("gui.abuseReport.discard.draft");
/*     */   
/* 210 */   private static final Component DISCARD = (Component)Component.translatable("gui.abuseReport.discard.discard");
/*     */   
/*     */   protected DiscardReportWarningScreen() {
/* 213 */     super(TITLE, MESSAGE, MESSAGE);
/*     */   }
/*     */   
/*     */   protected Layout addFooterButtons() {
/* 218 */     LinearLayout footer = LinearLayout.vertical().spacing(8);
/* 219 */     footer.defaultCellSetting().alignHorizontallyCenter();
/* 220 */     LinearLayout firstFooterRow = (LinearLayout)footer.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 221 */     firstFooterRow.addChild(
/* 222 */         (LayoutElement)Button.builder(RETURN, button -> onClose())
/* 223 */         .build());
/* 225 */     firstFooterRow.addChild(
/* 226 */         (LayoutElement)Button.builder(DRAFT, button -> {
/*     */             AbstractReportScreen.this.saveDraft();
/*     */             this.minecraft.gui.setScreen(AbstractReportScreen.this.lastScreen);
/* 230 */           }).build());
/* 232 */     footer.addChild(
/* 233 */         (LayoutElement)Button.builder(DISCARD, button -> {
/*     */             AbstractReportScreen.this.clearDraft();
/*     */             this.minecraft.gui.setScreen(AbstractReportScreen.this.lastScreen);
/* 237 */           }).build());
/* 239 */     return (Layout)footer;
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 244 */     this.minecraft.gui.setScreen(AbstractReportScreen.this);
/*     */   }
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/* 249 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\reporting\AbstractReportScreen$DiscardReportWarningScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */