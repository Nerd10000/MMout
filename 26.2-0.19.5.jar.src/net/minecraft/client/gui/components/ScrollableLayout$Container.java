/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ 
/*     */ class Container extends AbstractContainerWidget {
/*     */   private final Minecraft minecraft;
/*     */   
/* 122 */   private final List<AbstractWidget> children = new ArrayList<>();
/*     */   
/*     */   public Container(Minecraft minecraft, int width, int height, AbstractScrollArea.ScrollbarSettings scrollbarSettings) {
/* 125 */     super(0, 0, width, height, CommonComponents.EMPTY, scrollbarSettings);
/* 126 */     this.minecraft = minecraft;
/* 127 */     refreshChildren();
/*     */   }
/*     */   
/*     */   public void refreshChildren() {
/* 131 */     this.children.clear();
/* 132 */     Objects.requireNonNull(this.children);
/* 132 */     ScrollableLayout.this.content.visitWidgets(this.children::add);
/*     */   }
/*     */   
/*     */   protected int contentHeight() {
/* 137 */     return ScrollableLayout.this.content.getHeight();
/*     */   }
/*     */   
/*     */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 142 */     graphics.enableScissor(getX(), getY(), getX() + this.width, getY() + this.height);
/* 144 */     for (AbstractWidget child : this.children)
/* 145 */       child.extractRenderState(graphics, mouseX, mouseY, a); 
/* 148 */     graphics.disableScissor();
/* 150 */     extractScrollbar(graphics, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   protected void updateWidgetNarration(NarrationElementOutput output) {}
/*     */   
/*     */   public ScreenRectangle getBorderForArrowNavigation(ScreenDirection opposite) {
/* 160 */     GuiEventListener focused = getFocused();
/* 161 */     return (focused != null) ? focused.getBorderForArrowNavigation(opposite) : (new ScreenRectangle(getX(), getY(), this.width, contentHeight())).getBorder(opposite);
/*     */   }
/*     */   
/*     */   public void setFocused(GuiEventListener focused) {
/* 166 */     super.setFocused(focused);
/* 167 */     if (focused == null || !this.minecraft.getLastInputType().isKeyboard())
/*     */       return; 
/* 171 */     ScreenRectangle area = getRectangle();
/* 172 */     ScreenRectangle focusedRect = focused.getRectangle();
/* 173 */     int topDelta = focusedRect.top() - area.top();
/* 174 */     int bottomDelta = focusedRect.bottom() - area.bottom();
/* 175 */     double scrollRate = scrollRate();
/* 177 */     if (topDelta < 0) {
/* 178 */       setScrollAmount(scrollAmount() + topDelta - scrollRate);
/* 179 */     } else if (bottomDelta > 0) {
/* 180 */       setScrollAmount(scrollAmount() + bottomDelta + scrollRate);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/* 186 */     super.setX(x);
/* 187 */     ScrollableLayout.this.content.setX(x + ((ScrollableLayout.this.reserveStrategy == ScrollableLayout.ReserveStrategy.BOTH) ? scrollbarReserve() : 0));
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/* 192 */     super.setY(y);
/* 193 */     ScrollableLayout.this.content.setY(y - (int)scrollAmount());
/*     */   }
/*     */   
/*     */   private int scrollbarReserve() {
/* 197 */     return ScrollableLayout.this.scrollbarSpacing + scrollbarWidth();
/*     */   }
/*     */   
/*     */   public void setScrollAmount(double scrollAmount) {
/* 202 */     super.setScrollAmount(scrollAmount);
/* 203 */     ScrollableLayout.this.content.setY(getRectangle().top() - (int)scrollAmount());
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 208 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   public Collection<? extends NarratableEntry> getNarratables() {
/* 213 */     return (Collection)this.children;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ScrollableLayout$Container.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */