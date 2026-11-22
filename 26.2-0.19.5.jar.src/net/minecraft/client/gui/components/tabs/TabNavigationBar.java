/*     */ package net.minecraft.client.gui.components.tabs;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import net.minecraft.client.gui.ComponentPath;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractContainerWidget;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Renderable;
/*     */ import net.minecraft.client.gui.components.TabButton;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.ContainerEventHandler;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class TabNavigationBar extends AbstractContainerWidget implements NarratableEntry, Renderable {
/*     */   private static final int NO_TAB = -1;
/*     */   
/*  34 */   private static final Component USAGE_NARRATION = (Component)Component.translatable("narration.tab_navigation.usage");
/*     */   
/*     */   protected final FrameLayout layout;
/*     */   
/*     */   private final TabManager tabManager;
/*     */   
/*     */   protected final ImmutableList<Tab> tabs;
/*     */   
/*     */   protected final ImmutableList<TabButton> tabButtons;
/*     */   
/*     */   protected TabNavigationBar(int x, int y, int width, int height, TabManager tabManager, ImmutableList<TabButton> tabButtons, ImmutableList<Tab> tabs) {
/*  43 */     super(x, y, width, height, CommonComponents.EMPTY);
/*  44 */     this.tabManager = tabManager;
/*  45 */     this.tabButtons = tabButtons;
/*  46 */     this.tabs = ImmutableList.copyOf((Collection)tabs);
/*  48 */     this.layout = new FrameLayout();
/*  49 */     this.layout.setPosition(x, y);
/*  51 */     LinearLayout linearLayout = (LinearLayout)this.layout.addChild((LayoutElement)LinearLayout.horizontal());
/*  52 */     for (UnmodifiableIterator<TabButton> unmodifiableIterator = tabButtons.iterator(); unmodifiableIterator.hasNext(); ) {
/*  52 */       TabButton tabButton = unmodifiableIterator.next();
/*  53 */       linearLayout.addChild((LayoutElement)tabButton);
/*     */     } 
/*  55 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   protected int contentHeight() {
/*  60 */     return this.height;
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     protected final int x;
/*     */     
/*     */     protected final int y;
/*     */     
/*     */     protected final int width;
/*     */     
/*     */     protected final int height;
/*     */     
/*     */     protected final TabManager tabManager;
/*     */     
/*  69 */     protected final List<TabButton> tabButtons = new ArrayList<>();
/*     */     
/*  70 */     protected final List<Tab> tabs = new ArrayList<>();
/*     */     
/*     */     protected Builder(TabManager tabManager, int x, int y, int width, int height) {
/*  73 */       this.tabManager = tabManager;
/*  74 */       this.x = x;
/*  75 */       this.y = y;
/*  76 */       this.width = width;
/*  77 */       this.height = height;
/*     */     }
/*     */     
/*     */     public Builder addTab(TabButton button, Tab tab) {
/*  81 */       this.tabButtons.add(button);
/*  82 */       this.tabs.add(tab);
/*  83 */       return this;
/*     */     }
/*     */     
/*     */     public TabNavigationBar build() {
/*  87 */       return new TabNavigationBar(this.x, this.y, this.width, this.height, this.tabManager, ImmutableList.copyOf(this.tabButtons), ImmutableList.copyOf(this.tabs));
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder builder(TabManager tabManager, int x, int y, int width, int height) {
/*  92 */     return new Builder(tabManager, x, y, width, height);
/*     */   }
/*     */   
/*     */   public void arrangeElements(int width) {
/*  96 */     this.layout.setPosition(getX(), getY());
/*  97 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   public boolean isMouseOver(double mouseX, double mouseY) {
/* 102 */     AtomicBoolean mouseOver = new AtomicBoolean();
/* 103 */     this.layout.visitChildren(child -> {
/*     */           if (child.getRectangle().containsPoint((int)mouseX, (int)mouseY))
/*     */             mouseOver.set(true); 
/*     */         });
/* 108 */     return mouseOver.get();
/*     */   }
/*     */   
/*     */   public void setFocused(boolean focused) {
/* 113 */     super.setFocused(focused);
/* 114 */     if (getFocused() != null)
/* 115 */       setFocused((GuiEventListener)null); 
/*     */   }
/*     */   
/*     */   public void setFocused(GuiEventListener focused) {
/* 121 */     super.setFocused(focused);
/* 122 */     if (focused instanceof TabButton) {
/* 122 */       TabButton button = (TabButton)focused;
/* 122 */       if (button.isActive())
/* 123 */         this.tabManager.setCurrentTab(button.tab(), true); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 129 */     if (!isFocused()) {
/* 130 */       TabButton button = currentTabButton();
/* 131 */       if (button != null)
/* 132 */         return ComponentPath.path((ContainerEventHandler)this, ComponentPath.leaf((GuiEventListener)button)); 
/*     */     } 
/* 135 */     if (navigationEvent instanceof FocusNavigationEvent.TabNavigation)
/* 136 */       return null; 
/* 138 */     return super.nextFocusPath(navigationEvent);
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 143 */     return (List)this.tabButtons;
/*     */   }
/*     */   
/*     */   public List<Tab> getTabs() {
/* 147 */     return (List<Tab>)this.tabs;
/*     */   }
/*     */   
/*     */   public NarratableEntry.NarrationPriority narrationPriority() {
/* 152 */     return this.tabButtons.stream()
/* 153 */       .map(AbstractWidget::narrationPriority)
/* 154 */       .max(Comparator.naturalOrder())
/* 155 */       .orElse(NarratableEntry.NarrationPriority.NONE);
/*     */   }
/*     */   
/*     */   protected void updateWidgetNarration(NarrationElementOutput output) {
/* 161 */     Optional<TabButton> selected = this.tabButtons.stream().filter(AbstractWidget::isHovered).findFirst().or(() -> Optional.ofNullable(currentTabButton()));
/* 162 */     selected.ifPresent(button -> {
/*     */           narrateListElementPosition(output.nest(), button);
/*     */           button.updateNarration(output);
/*     */         });
/* 166 */     if (isFocused())
/* 167 */       output.add(NarratedElementType.USAGE, USAGE_NARRATION); 
/*     */   }
/*     */   
/*     */   protected void narrateListElementPosition(NarrationElementOutput output, TabButton widget) {
/* 172 */     if (this.tabs.size() > 1) {
/* 173 */       int index = this.tabButtons.indexOf(widget);
/* 174 */       if (index != -1)
/* 175 */         output.add(NarratedElementType.POSITION, (Component)Component.translatable("narrator.position.tab", new Object[] { Integer.valueOf(index + 1), Integer.valueOf(this.tabs.size()) })); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 182 */     for (UnmodifiableIterator<TabButton> unmodifiableIterator = this.tabButtons.iterator(); unmodifiableIterator.hasNext(); ) {
/* 182 */       TabButton value = unmodifiableIterator.next();
/* 183 */       value.extractRenderState(graphics, mouseX, mouseY, a);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ScreenRectangle getRectangle() {
/* 189 */     return this.layout.getRectangle();
/*     */   }
/*     */   
/*     */   public void selectTab(int index, boolean playSound) {
/* 194 */     if (isFocused()) {
/* 195 */       setFocused((GuiEventListener)this.tabButtons.get(index));
/* 196 */     } else if (((TabButton)this.tabButtons.get(index)).isActive()) {
/* 197 */       this.tabManager.setCurrentTab((Tab)this.tabs.get(index), playSound);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setTabActiveState(int index, boolean active) {
/* 202 */     if (index >= 0 && index < this.tabButtons.size())
/* 203 */       ((TabButton)this.tabButtons.get(index)).active = active; 
/*     */   }
/*     */   
/*     */   public void setTabTooltip(int index, Tooltip hint) {
/* 208 */     if (index >= 0 && index < this.tabButtons.size())
/* 209 */       ((TabButton)this.tabButtons.get(index)).setTooltip(hint); 
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 215 */     if (event.hasControlDownWithQuirk()) {
/* 216 */       int tabIndex = getNextTabIndex(event);
/* 217 */       if (tabIndex != -1) {
/* 218 */         selectTab(Mth.clamp(tabIndex, 0, this.tabs.size() - 1), true);
/* 219 */         return true;
/*     */       } 
/*     */     } 
/* 222 */     return false;
/*     */   }
/*     */   
/*     */   private int getNextTabIndex(KeyEvent event) {
/* 226 */     return getNextTabIndex(currentTabIndex(), event);
/*     */   }
/*     */   
/*     */   private int getNextTabIndex(int currentTab, KeyEvent event) {
/* 230 */     int digit = event.getDigit();
/* 231 */     if (digit != -1)
/* 233 */       return Math.floorMod(digit - 1, 10); 
/* 234 */     if (event.isCycleFocus() && 
/* 235 */       currentTab != -1) {
/* 236 */       int nextTabIndex = event.hasShiftDown() ? (currentTab - 1) : (currentTab + 1);
/* 237 */       int index = Math.floorMod(nextTabIndex, this.tabs.size());
/* 238 */       if (((TabButton)this.tabButtons.get(index)).active)
/* 239 */         return index; 
/* 241 */       return getNextTabIndex(index, event);
/*     */     } 
/* 245 */     return -1;
/*     */   }
/*     */   
/*     */   private int currentTabIndex() {
/* 249 */     Tab currentTab = this.tabManager.getCurrentTab();
/* 250 */     int index = this.tabs.indexOf(currentTab);
/* 251 */     return (index != -1) ? index : -1;
/*     */   }
/*     */   
/*     */   private TabButton currentTabButton() {
/* 255 */     int index = currentTabIndex();
/* 256 */     return (index != -1) ? (TabButton)this.tabButtons.get(index) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\tabs\TabNavigationBar.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */