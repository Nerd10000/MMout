/*     */ package net.minecraft.client.gui.components.tabs;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.TabButton;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class MenuTabButton extends TabButton {
/*  75 */   private static final WidgetSprites SPRITES = new WidgetSprites(
/*  76 */       Identifier.withDefaultNamespace("widget/tab_selected"), 
/*  77 */       Identifier.withDefaultNamespace("widget/tab"), 
/*  78 */       Identifier.withDefaultNamespace("widget/tab_selected_highlighted"), 
/*  79 */       Identifier.withDefaultNamespace("widget/tab_highlighted"));
/*     */   
/*     */   private static final int SELECTED_OFFSET = 3;
/*     */   
/*     */   private static final int TEXT_MARGIN = 1;
/*     */   
/*     */   private static final int UNDERLINE_HEIGHT = 1;
/*     */   
/*     */   private static final int UNDERLINE_MARGIN_X = 4;
/*     */   
/*     */   private static final int UNDERLINE_MARGIN_BOTTOM = 2;
/*     */   
/*     */   public MenuTabButton(TabManager tabManager, Tab tab, int width, int height) {
/*  90 */     super(tabManager, tab, width, height);
/*     */   }
/*     */   
/*     */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  95 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITES.get(isSelected(), isHoveredOrFocused()), getX(), getY(), this.width, this.height);
/*  97 */     Font font = (Minecraft.getInstance()).font;
/*  98 */     int underlineColor = this.active ? -1 : -6250336;
/* 100 */     if (isSelected()) {
/* 101 */       renderMenuBackground(graphics, getX() + 2, getY() + 2, getRight() - 2, getBottom());
/* 102 */       renderFocusUnderline(graphics, font, underlineColor);
/*     */     } 
/* 104 */     renderLabel(graphics.textRendererForWidget((AbstractWidget)this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
/* 106 */     handleCursor(graphics);
/*     */   }
/*     */   
/*     */   protected void renderMenuBackground(GuiGraphicsExtractor graphics, int x0, int y0, int x1, int y1) {
/* 110 */     Screen.extractMenuBackgroundTexture(graphics, Screen.MENU_BACKGROUND, x0, y0, 0.0F, 0.0F, x1 - x0, y1 - y0);
/*     */   }
/*     */   
/*     */   private void renderLabel(ActiveTextCollector output) {
/* 114 */     int left = getX() + 1;
/* 115 */     int top = getY() + (isSelected() ? 0 : 3);
/* 116 */     int right = getX() + getWidth() - 1;
/* 117 */     int bottom = getY() + getHeight();
/* 118 */     output.acceptScrollingWithDefaultCenter(getMessage(), left, right, top, bottom);
/*     */   }
/*     */   
/*     */   private void renderFocusUnderline(GuiGraphicsExtractor graphics, Font font, int color) {
/* 122 */     int width = Math.min(font.width((FormattedText)getMessage()), getWidth() - 4);
/* 123 */     int left = getX() + (getWidth() - width) / 2;
/* 124 */     int top = getY() + getHeight() - 2;
/* 125 */     graphics.fill(left, top, left + width, top + 1, color);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\tabs\MenuTabBar$MenuTabButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */