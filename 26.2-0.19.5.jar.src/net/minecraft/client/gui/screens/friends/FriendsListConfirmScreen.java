/*    */ package net.minecraft.client.gui.screens.friends;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*    */ import java.net.URI;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*    */ import net.minecraft.client.gui.screens.PrivacyConfirmLinkScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class FriendsListConfirmScreen extends ConfirmScreen {
/* 16 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("friends/background_dark");
/*    */   
/* 17 */   private static final Component USAGE_FOCUSED = (Component)Component.translatable("narration.link.usage.focused");
/*    */   
/* 18 */   private static final Component USAGE_HOVERED = (Component)Component.translatable("narration.link.usage.hovered");
/*    */   
/*    */   private static final int PANEL_PADDING = 10;
/*    */   
/*    */   private static final int BG_BORDER_WIDTH = 8;
/*    */   
/*    */   public FriendsListConfirmScreen(BooleanConsumer callback, Component title, Component message, Component yesButton, Component noButton) {
/* 23 */     super(callback, title, message, yesButton, noButton);
/*    */   }
/*    */   
/*    */   protected LayoutElement addMessage() {
/* 28 */     LinearLayout content = LinearLayout.vertical();
/* 29 */     content.defaultCellSetting().alignHorizontallyCenter().alignVerticallyMiddle().padding(10, 5, 10, 5);
/* 34 */     FocusableTextWidget focusable = FocusableTextWidget.builder(this.message, this.font).maxWidth(this.width - 180).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.NEVER).build();
/* 35 */     focusable.setMaxRows(15);
/* 36 */     focusable.setComponentClickHandler(style -> {
/*    */           ClickEvent patt1$temp = style.getClickEvent();
/*    */           if (patt1$temp instanceof ClickEvent.OpenUrl) {
/*    */             ClickEvent.OpenUrl $b$0 = (ClickEvent.OpenUrl)patt1$temp;
/*    */             try {
/*    */               URI patt2$temp = $b$0.uri();
/*    */               URI uri = patt2$temp;
/*    */               PrivacyConfirmLinkScreen.confirmLinkNow((Screen)this, uri);
/* 37 */             } catch (Throwable throwable) {
/*    */               throw new MatchException(throwable.toString(), throwable);
/*    */             } 
/*    */           } 
/*    */         });
/* 41 */     focusable.setNarrateMessage(false);
/* 42 */     focusable.setUsageNarration(USAGE_FOCUSED, USAGE_HOVERED);
/* 43 */     content.addChild((LayoutElement)focusable);
/* 44 */     return (LayoutElement)content;
/*    */   }
/*    */   
/*    */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 49 */     super.extractBackground(graphics, mouseX, mouseY, a);
/* 50 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, this.layout
/*    */         
/* 52 */         .getX() - 10 - 8, this.layout
/* 53 */         .getY() - 10 - 8, this.layout
/* 54 */         .getWidth() + 20 + 16 + 1, this.layout
/* 55 */         .getHeight() + 20 + 16 + 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\friends\FriendsListConfirmScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */