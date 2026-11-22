/*    */ package net.minecraft.client.gui.screens.friends;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.TabButton;
/*    */ import net.minecraft.client.gui.components.WidgetSprites;
/*    */ import net.minecraft.client.gui.components.tabs.Tab;
/*    */ import net.minecraft.client.gui.components.tabs.TabManager;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ class FriendsOverlayTabButton extends TabButton {
/*    */   private final WidgetSprites sprites;
/*    */   
/*    */   private static final int SELECTED_OFFSET = 1;
/*    */   
/*    */   private static final int TEXT_MARGIN = 1;
/*    */   
/*    */   private static final int UNDERLINE_HEIGHT = 1;
/*    */   
/*    */   private static final int UNDERLINE_MARGIN_X = 4;
/*    */   
/*    */   private static final int UNDERLINE_MARGIN_BOTTOM = 2;
/*    */   
/*    */   public FriendsOverlayTabButton(TabManager tabManager, Tab tab, int width, int height) {
/* 28 */     super(tabManager, tab, width, height);
/* 29 */     this
/*    */       
/* 33 */       .sprites = new WidgetSprites(Identifier.withDefaultNamespace("friends/button"), Identifier.withDefaultNamespace("friends/button_disabled"), Identifier.withDefaultNamespace("friends/button_highlighted"), Identifier.withDefaultNamespace("friends/button_highlighted"));
/*    */   }
/*    */   
/*    */   public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 39 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprites.get(isSelected(), isHoveredOrFocused()), getX(), getY(), this.width, this.height);
/* 41 */     Font font = (Minecraft.getInstance()).font;
/* 42 */     int underlineColor = this.active ? -1 : -6250336;
/* 44 */     if (isSelected())
/* 45 */       renderFocusUnderline(graphics, font, underlineColor); 
/* 47 */     renderLabel(graphics.textRendererForWidget((AbstractWidget)this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
/* 49 */     handleCursor(graphics);
/*    */   }
/*    */   
/*    */   private void renderLabel(ActiveTextCollector output) {
/* 53 */     int left = getX() + 1;
/* 54 */     int top = getY() + (isSelected() ? 0 : 1);
/* 55 */     int right = getX() + getWidth() - 1;
/* 56 */     int bottom = getY() + getHeight();
/* 57 */     output.acceptScrollingWithDefaultCenter(getMessage(), left, right, top, bottom);
/*    */   }
/*    */   
/*    */   private void renderFocusUnderline(GuiGraphicsExtractor graphics, Font font, int color) {
/* 61 */     int width = Math.min(font.width((FormattedText)getMessage()), getWidth() - 4);
/* 62 */     int left = getX() + (getWidth() - width) / 2;
/* 63 */     int top = getY() + getHeight() - 2;
/* 64 */     graphics.fill(left, top, left + width, top + 1, color);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\friends\FriendsOverlayTabButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */