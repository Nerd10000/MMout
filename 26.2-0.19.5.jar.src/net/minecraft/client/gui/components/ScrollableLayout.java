/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ 
/*     */ public class ScrollableLayout implements Layout {
/*     */   private static final int DEFAULT_SCROLLBAR_SPACING = 4;
/*     */   
/*     */   private final Layout content;
/*     */   
/*     */   private final Container container;
/*     */   
/*     */   private final ReserveStrategy reserveStrategy;
/*     */   
/*  26 */   private int scrollbarSpacing = 4;
/*     */   
/*     */   private int minWidth;
/*     */   
/*     */   private int minHeight;
/*     */   
/*     */   private int maxHeight;
/*     */   
/*     */   public ScrollableLayout(Minecraft minecraft, Layout content, int maxHeight) {
/*  32 */     this(minecraft, content, maxHeight, ReserveStrategy.BOTH);
/*     */   }
/*     */   
/*     */   public ScrollableLayout(Minecraft minecraft, Layout content, int maxHeight, ReserveStrategy reserveStrategy) {
/*  36 */     this.content = content;
/*  37 */     this.maxHeight = maxHeight;
/*  38 */     this.reserveStrategy = reserveStrategy;
/*  39 */     this.container = new Container(minecraft, 0, maxHeight, AbstractScrollArea.defaultSettings(10));
/*     */   }
/*     */   
/*     */   public void setScrollbarSpacing(int scrollbarSpacing) {
/*  43 */     this.scrollbarSpacing = scrollbarSpacing;
/*     */   }
/*     */   
/*     */   public void setMinWidth(int minWidth) {
/*  47 */     this.minWidth = minWidth;
/*  48 */     this.container.setWidth(Math.max(this.content.getWidth(), minWidth));
/*     */   }
/*     */   
/*     */   public void setMinHeight(int minHeight) {
/*  52 */     this.minHeight = minHeight;
/*  53 */     this.container.setHeight(Math.max(this.content.getHeight(), minHeight));
/*     */   }
/*     */   
/*     */   public void setMaxHeight(int maxHeight) {
/*  57 */     this.maxHeight = maxHeight;
/*  58 */     this.container.setHeight(Math.min(this.content.getHeight(), maxHeight));
/*  59 */     this.container.refreshScrollAmount();
/*     */   }
/*     */   
/*     */   public void arrangeElements() {
/*  64 */     this.content.arrangeElements();
/*  65 */     int contentWidth = this.content.getWidth();
/*  69 */     switch (this.reserveStrategy.ordinal()) {
/*     */       default:
/*  69 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */         break;
/*     */     } 
/*  69 */     int scrollbarReserve = 
/*     */       
/*  71 */       2 * this.container.scrollbarReserve();
/*  73 */     this.container.setWidth(Math.max(contentWidth, this.minWidth) + scrollbarReserve);
/*  74 */     this.container.setHeight(Math.clamp(this.container.getHeight(), this.minHeight, this.maxHeight));
/*  75 */     this.container.refreshChildren();
/*  76 */     this.container.refreshScrollAmount();
/*     */   }
/*     */   
/*     */   public void visitChildren(Consumer<LayoutElement> layoutElementVisitor) {
/*  81 */     layoutElementVisitor.accept(this.container);
/*     */   }
/*     */   
/*     */   public void removeChildren() {
/*  86 */     this.container.children().clear();
/*  87 */     this.content.removeChildren();
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/*  92 */     this.container.setX(x);
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/*  97 */     this.container.setY(y);
/*     */   }
/*     */   
/*     */   public int getX() {
/* 102 */     return this.container.getX();
/*     */   }
/*     */   
/*     */   public int getY() {
/* 107 */     return this.container.getY();
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 112 */     return this.container.getWidth();
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 117 */     return this.container.getHeight();
/*     */   }
/*     */   
/*     */   private class Container extends AbstractContainerWidget {
/*     */     private final Minecraft minecraft;
/*     */     
/* 122 */     private final List<AbstractWidget> children = new ArrayList<>();
/*     */     
/*     */     public Container(Minecraft minecraft, int width, int height, AbstractScrollArea.ScrollbarSettings scrollbarSettings) {
/* 125 */       super(0, 0, width, height, CommonComponents.EMPTY, scrollbarSettings);
/* 126 */       this.minecraft = minecraft;
/* 127 */       refreshChildren();
/*     */     }
/*     */     
/*     */     public void refreshChildren() {
/* 131 */       this.children.clear();
/* 132 */       Objects.requireNonNull(this.children);
/* 132 */       ScrollableLayout.this.content.visitWidgets(this.children::add);
/*     */     }
/*     */     
/*     */     protected int contentHeight() {
/* 137 */       return ScrollableLayout.this.content.getHeight();
/*     */     }
/*     */     
/*     */     protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 142 */       graphics.enableScissor(getX(), getY(), getX() + this.width, getY() + this.height);
/* 144 */       for (AbstractWidget child : this.children)
/* 145 */         child.extractRenderState(graphics, mouseX, mouseY, a); 
/* 148 */       graphics.disableScissor();
/* 150 */       extractScrollbar(graphics, mouseX, mouseY);
/*     */     }
/*     */     
/*     */     protected void updateWidgetNarration(NarrationElementOutput output) {}
/*     */     
/*     */     public ScreenRectangle getBorderForArrowNavigation(ScreenDirection opposite) {
/* 160 */       GuiEventListener focused = getFocused();
/* 161 */       return (focused != null) ? focused.getBorderForArrowNavigation(opposite) : (new ScreenRectangle(getX(), getY(), this.width, contentHeight())).getBorder(opposite);
/*     */     }
/*     */     
/*     */     public void setFocused(GuiEventListener focused) {
/* 166 */       super.setFocused(focused);
/* 167 */       if (focused == null || !this.minecraft.getLastInputType().isKeyboard())
/*     */         return; 
/* 171 */       ScreenRectangle area = getRectangle();
/* 172 */       ScreenRectangle focusedRect = focused.getRectangle();
/* 173 */       int topDelta = focusedRect.top() - area.top();
/* 174 */       int bottomDelta = focusedRect.bottom() - area.bottom();
/* 175 */       double scrollRate = scrollRate();
/* 177 */       if (topDelta < 0) {
/* 178 */         setScrollAmount(scrollAmount() + topDelta - scrollRate);
/* 179 */       } else if (bottomDelta > 0) {
/* 180 */         setScrollAmount(scrollAmount() + bottomDelta + scrollRate);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setX(int x) {
/* 186 */       super.setX(x);
/* 187 */       ScrollableLayout.this.content.setX(x + ((ScrollableLayout.this.reserveStrategy == ScrollableLayout.ReserveStrategy.BOTH) ? scrollbarReserve() : 0));
/*     */     }
/*     */     
/*     */     public void setY(int y) {
/* 192 */       super.setY(y);
/* 193 */       ScrollableLayout.this.content.setY(y - (int)scrollAmount());
/*     */     }
/*     */     
/*     */     private int scrollbarReserve() {
/* 197 */       return ScrollableLayout.this.scrollbarSpacing + scrollbarWidth();
/*     */     }
/*     */     
/*     */     public void setScrollAmount(double scrollAmount) {
/* 202 */       super.setScrollAmount(scrollAmount);
/* 203 */       ScrollableLayout.this.content.setY(getRectangle().top() - (int)scrollAmount());
/*     */     }
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 208 */       return (List)this.children;
/*     */     }
/*     */     
/*     */     public Collection<? extends NarratableEntry> getNarratables() {
/* 213 */       return (Collection)this.children;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum ReserveStrategy {
/* 218 */     RIGHT, BOTH;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ScrollableLayout.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */