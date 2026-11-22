/*     */ package net.minecraft.client.gui.components.tabs;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
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
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class MenuTabBar extends TabNavigationBar {
/*     */   private static final int HEIGHT = 24;
/*     */   
/*     */   private static final int MAX_WIDTH = 400;
/*     */   
/*     */   private static final int MARGIN = 14;
/*     */   
/*     */   public MenuTabBar(int x, int y, int width, int height, TabManager tabManager, ImmutableList<TabButton> tabButtons, ImmutableList<Tab> tabs) {
/*  22 */     super(x, y, width, height, tabManager, tabButtons, tabs);
/*     */   }
/*     */   
/*     */   public static class Builder extends TabNavigationBar.Builder {
/*     */     private Builder(TabManager tabManager, int width) {
/*  27 */       super(tabManager, 0, 0, width, 24);
/*     */     }
/*     */     
/*     */     public Builder addTab(Tab tab) {
/*  31 */       addTab(new MenuTabBar.MenuTabButton(this.tabManager, tab, 0, this.height), tab);
/*  32 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addTabs(Tab... tabs) {
/*  36 */       for (Tab tab : tabs)
/*  37 */         addTab(tab); 
/*  39 */       return this;
/*     */     }
/*     */     
/*     */     public MenuTabBar build() {
/*  44 */       return new MenuTabBar(this.x, this.y, this.width, this.height, this.tabManager, ImmutableList.copyOf(this.tabButtons), ImmutableList.copyOf(this.tabs));
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder builder(TabManager tabManager, int width) {
/*  49 */     return new Builder(tabManager, width);
/*     */   }
/*     */   
/*     */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  54 */     graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.HEADER_SEPARATOR, 0, this.layout.getY() + this.layout.getHeight() - 2, 0.0F, 0.0F, ((TabButton)this.tabButtons.getFirst()).getX(), 2, 32, 2);
/*  55 */     int afterLastTab = ((TabButton)this.tabButtons.get(this.tabButtons.size() - 1)).getRight();
/*  56 */     graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.HEADER_SEPARATOR, afterLastTab, this.layout.getY() + this.layout.getHeight() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);
/*  57 */     super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   public void arrangeElements(int width) {
/*  63 */     this.width = width;
/*  64 */     int tabsWidth = Math.min(400, width) - 28;
/*  65 */     int tabWidth = Mth.roundToward(tabsWidth / this.tabs.size(), 2);
/*  66 */     for (UnmodifiableIterator<TabButton> unmodifiableIterator = this.tabButtons.iterator(); unmodifiableIterator.hasNext(); ) {
/*  66 */       TabButton button = unmodifiableIterator.next();
/*  67 */       button.setWidth(tabWidth);
/*     */     } 
/*  69 */     this.layout.arrangeElements();
/*  70 */     this.layout.setX(Mth.roundToward((width - tabsWidth) / 2, 2));
/*  71 */     this.layout.setY(0);
/*     */   }
/*     */   
/*     */   public static class MenuTabButton extends TabButton {
/*  75 */     private static final WidgetSprites SPRITES = new WidgetSprites(
/*  76 */         Identifier.withDefaultNamespace("widget/tab_selected"), 
/*  77 */         Identifier.withDefaultNamespace("widget/tab"), 
/*  78 */         Identifier.withDefaultNamespace("widget/tab_selected_highlighted"), 
/*  79 */         Identifier.withDefaultNamespace("widget/tab_highlighted"));
/*     */     
/*     */     private static final int SELECTED_OFFSET = 3;
/*     */     
/*     */     private static final int TEXT_MARGIN = 1;
/*     */     
/*     */     private static final int UNDERLINE_HEIGHT = 1;
/*     */     
/*     */     private static final int UNDERLINE_MARGIN_X = 4;
/*     */     
/*     */     private static final int UNDERLINE_MARGIN_BOTTOM = 2;
/*     */     
/*     */     public MenuTabButton(TabManager tabManager, Tab tab, int width, int height) {
/*  90 */       super(tabManager, tab, width, height);
/*     */     }
/*     */     
/*     */     protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  95 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITES.get(isSelected(), isHoveredOrFocused()), getX(), getY(), this.width, this.height);
/*  97 */       Font font = (Minecraft.getInstance()).font;
/*  98 */       int underlineColor = this.active ? -1 : -6250336;
/* 100 */       if (isSelected()) {
/* 101 */         renderMenuBackground(graphics, getX() + 2, getY() + 2, getRight() - 2, getBottom());
/* 102 */         renderFocusUnderline(graphics, font, underlineColor);
/*     */       } 
/* 104 */       renderLabel(graphics.textRendererForWidget((AbstractWidget)this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
/* 106 */       handleCursor(graphics);
/*     */     }
/*     */     
/*     */     protected void renderMenuBackground(GuiGraphicsExtractor graphics, int x0, int y0, int x1, int y1) {
/* 110 */       Screen.extractMenuBackgroundTexture(graphics, Screen.MENU_BACKGROUND, x0, y0, 0.0F, 0.0F, x1 - x0, y1 - y0);
/*     */     }
/*     */     
/*     */     private void renderLabel(ActiveTextCollector output) {
/* 114 */       int left = getX() + 1;
/* 115 */       int top = getY() + (isSelected() ? 0 : 3);
/* 116 */       int right = getX() + getWidth() - 1;
/* 117 */       int bottom = getY() + getHeight();
/* 118 */       output.acceptScrollingWithDefaultCenter(getMessage(), left, right, top, bottom);
/*     */     }
/*     */     
/*     */     private void renderFocusUnderline(GuiGraphicsExtractor graphics, Font font, int color) {
/* 122 */       int width = Math.min(font.width((FormattedText)getMessage()), getWidth() - 4);
/* 123 */       int left = getX() + (getWidth() - width) / 2;
/* 124 */       int top = getY() + getHeight() - 2;
/* 125 */       graphics.fill(left, top, left + width, top + 1, color);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\tabs\MenuTabBar.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */