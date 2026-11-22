/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.events.ContainerEventHandler;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public abstract class AbstractSelectionList<E extends AbstractSelectionList.Entry<E>> extends AbstractContainerWidget {
/*  35 */   private static final Identifier MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/menu_list_background.png");
/*     */   
/*  36 */   private static final Identifier INWORLD_MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");
/*     */   
/*     */   private static final int SEPARATOR_HEIGHT = 2;
/*     */   
/*     */   protected final Minecraft minecraft;
/*     */   
/*     */   protected final int defaultEntryHeight;
/*     */   
/*  41 */   private final List<E> children = new TrackedList();
/*     */   
/*     */   protected boolean centerListVertically = true;
/*     */   
/*     */   private E selected;
/*     */   
/*     */   private E hovered;
/*     */   
/*     */   public AbstractSelectionList(Minecraft minecraft, int width, int height, int y, int defaultEntryHeight) {
/*  47 */     super(0, y, width, height, CommonComponents.EMPTY, AbstractScrollArea.defaultSettings(defaultEntryHeight / 2));
/*  48 */     this.minecraft = minecraft;
/*  49 */     this.defaultEntryHeight = defaultEntryHeight;
/*     */   }
/*     */   
/*     */   public E getSelected() {
/*  53 */     return this.selected;
/*     */   }
/*     */   
/*     */   public void setSelected(E selected) {
/*  57 */     this.selected = selected;
/*  58 */     if (selected != null) {
/*  59 */       boolean topClipped = (selected.getContentY() < getY());
/*  60 */       boolean bottomClipped = (selected.getContentBottom() > getBottom());
/*  61 */       if (this.minecraft.getLastInputType().isKeyboard() || topClipped || bottomClipped)
/*  62 */         scrollToEntry(selected); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public E getFocused() {
/*  70 */     return (E)super.getFocused();
/*     */   }
/*     */   
/*     */   public final List<E> children() {
/*  75 */     return Collections.unmodifiableList(this.children);
/*     */   }
/*     */   
/*     */   protected void sort(Comparator<E> comparator) {
/*  79 */     this.children.sort(comparator);
/*  80 */     repositionEntries();
/*     */   }
/*     */   
/*     */   protected void swap(int firstIndex, int secondIndex) {
/*  84 */     Collections.swap(this.children, firstIndex, secondIndex);
/*  85 */     repositionEntries();
/*  86 */     scrollToEntry(this.children.get(secondIndex));
/*     */   }
/*     */   
/*     */   protected void clearEntries() {
/*  90 */     this.children.clear();
/*  92 */     this.selected = null;
/*     */   }
/*     */   
/*     */   protected void clearEntriesExcept(E exception) {
/*  96 */     this.children.removeIf(entry -> (entry != exception));
/*  97 */     if (this.selected != exception)
/*  98 */       setSelected((E)null); 
/*     */   }
/*     */   
/*     */   public void replaceEntries(Collection<E> newChildren) {
/* 103 */     clearEntries();
/* 104 */     for (Entry entry : newChildren)
/* 105 */       addEntry((E)entry); 
/*     */   }
/*     */   
/*     */   private int getFirstEntryY() {
/* 110 */     return getY() + 2;
/*     */   }
/*     */   
/*     */   public int getNextY() {
/* 114 */     int y = getFirstEntryY() - (int)scrollAmount();
/* 115 */     for (Entry entry : this.children)
/* 116 */       y += entry.getHeight(); 
/* 118 */     return y;
/*     */   }
/*     */   
/*     */   protected int addEntry(E entry) {
/* 122 */     return addEntry(entry, this.defaultEntryHeight);
/*     */   }
/*     */   
/*     */   protected int addEntry(E entry, int height) {
/* 126 */     entry.setX(getRowLeft());
/* 127 */     entry.setWidth(getRowWidth());
/* 128 */     entry.setY(getNextY());
/* 129 */     entry.setHeight(height);
/* 130 */     this.children.add(entry);
/* 131 */     return this.children.size() - 1;
/*     */   }
/*     */   
/*     */   protected void addEntryToTop(E entry) {
/* 135 */     addEntryToTop(entry, this.defaultEntryHeight);
/*     */   }
/*     */   
/*     */   protected void addEntryToTop(E entry, int height) {
/* 139 */     double scrollFromBottom = maxScrollAmount() - scrollAmount();
/* 140 */     entry.setHeight(height);
/* 141 */     this.children.addFirst(entry);
/* 142 */     repositionEntries();
/* 143 */     setScrollAmount(maxScrollAmount() - scrollFromBottom);
/*     */   }
/*     */   
/*     */   private void repositionEntries() {
/* 147 */     int y = getFirstEntryY() - (int)scrollAmount();
/* 148 */     for (Entry entry : this.children) {
/* 149 */       entry.setY(y);
/* 150 */       y += entry.getHeight();
/* 151 */       entry.setX(getRowLeft());
/* 152 */       entry.setWidth(getRowWidth());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void removeEntryFromTop(E entry) {
/* 157 */     double scrollFromBottom = maxScrollAmount() - scrollAmount();
/* 158 */     removeEntry(entry);
/* 159 */     setScrollAmount(maxScrollAmount() - scrollFromBottom);
/*     */   }
/*     */   
/*     */   protected int getItemCount() {
/* 163 */     return children().size();
/*     */   }
/*     */   
/*     */   protected boolean entriesCanBeSelected() {
/* 167 */     return true;
/*     */   }
/*     */   
/*     */   protected final E getEntryAtPosition(double posX, double posY) {
/* 171 */     for (Entry entry : this.children) {
/* 172 */       if (entry.isMouseOver(posX, posY))
/* 173 */         return (E)entry; 
/*     */     } 
/* 176 */     return null;
/*     */   }
/*     */   
/*     */   public void updateSize(int width, HeaderAndFooterLayout layout) {
/* 181 */     updateSizeAndPosition(width, layout.getContentHeight(), layout.getHeaderHeight());
/*     */   }
/*     */   
/*     */   public void updateSizeAndPosition(int width, int height, int y) {
/* 185 */     updateSizeAndPosition(width, height, 0, y);
/*     */   }
/*     */   
/*     */   public void updateSizeAndPosition(int width, int height, int x, int y) {
/* 189 */     setSize(width, height);
/* 190 */     setPosition(x, y);
/* 191 */     repositionEntries();
/* 192 */     if (getSelected() != null)
/* 193 */       scrollToEntry(getSelected()); 
/* 195 */     refreshScrollAmount();
/*     */   }
/*     */   
/*     */   protected int contentHeight() {
/* 200 */     int totalHeight = 0;
/* 201 */     for (Entry entry : this.children)
/* 202 */       totalHeight += entry.getHeight(); 
/* 204 */     return totalHeight + 4;
/*     */   }
/*     */   
/*     */   public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 209 */     this.hovered = isMouseOver(mouseX, mouseY) ? getEntryAtPosition(mouseX, mouseY) : null;
/* 211 */     extractListBackground(graphics);
/* 213 */     enableScissor(graphics);
/* 214 */     extractListItems(graphics, mouseX, mouseY, a);
/* 215 */     graphics.disableScissor();
/* 217 */     extractListSeparators(graphics);
/* 219 */     extractScrollbar(graphics, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   protected void extractListSeparators(GuiGraphicsExtractor graphics) {
/* 223 */     Identifier headerSeparator = (this.minecraft.level == null) ? Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR;
/* 224 */     Identifier footerSeparator = (this.minecraft.level == null) ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
/* 225 */     graphics.blit(RenderPipelines.GUI_TEXTURED, headerSeparator, getX(), getY() - 2, 0.0F, 0.0F, getWidth(), 2, 32, 2);
/* 226 */     graphics.blit(RenderPipelines.GUI_TEXTURED, footerSeparator, getX(), getBottom(), 0.0F, 0.0F, getWidth(), 2, 32, 2);
/*     */   }
/*     */   
/*     */   protected void extractListBackground(GuiGraphicsExtractor graphics) {
/* 230 */     Identifier menuListBackground = (this.minecraft.level == null) ? MENU_LIST_BACKGROUND : INWORLD_MENU_LIST_BACKGROUND;
/* 231 */     graphics.blit(RenderPipelines.GUI_TEXTURED, menuListBackground, getX(), getY(), getRight(), (getBottom() + (int)scrollAmount()), getWidth(), getHeight(), 32, 32);
/*     */   }
/*     */   
/*     */   protected void enableScissor(GuiGraphicsExtractor graphics) {
/* 235 */     graphics.enableScissor(Mth.clamp(getX(), 0, graphics.guiWidth()), Mth.clamp(getY(), 0, graphics.guiHeight()), Mth.clamp(getRight(), 0, graphics.guiWidth()), Mth.clamp(getBottom(), 0, graphics.guiHeight()));
/*     */   }
/*     */   
/*     */   protected void scrollToEntry(E entry) {
/* 239 */     int topDelta = entry.getY() - getY() - 2;
/* 240 */     if (topDelta < 0)
/* 241 */       scroll(topDelta); 
/* 243 */     int bottomDelta = getBottom() - entry.getY() - entry.getHeight() - 2;
/* 244 */     if (bottomDelta < 0)
/* 245 */       scroll(-bottomDelta); 
/*     */   }
/*     */   
/*     */   protected void centerScrollOn(E entry) {
/* 250 */     int y = 0;
/* 251 */     for (Entry entry1 : this.children) {
/* 252 */       if (entry1 == entry) {
/* 253 */         y += entry1.getHeight() / 2;
/*     */         break;
/*     */       } 
/* 256 */       y += entry1.getHeight();
/*     */     } 
/* 258 */     setScrollAmount(y - this.height / 2.0D);
/*     */   }
/*     */   
/*     */   private void scroll(int amount) {
/* 262 */     setScrollAmount(scrollAmount() + amount);
/*     */   }
/*     */   
/*     */   public void setScrollAmount(double scrollAmount) {
/* 267 */     super.setScrollAmount(scrollAmount);
/* 268 */     repositionEntries();
/*     */   }
/*     */   
/*     */   protected int scrollBarX() {
/* 273 */     return getRowRight() + scrollbarWidth() + 2;
/*     */   }
/*     */   
/*     */   public Optional<GuiEventListener> getChildAt(double x, double y) {
/* 278 */     return Optional.ofNullable((GuiEventListener)getEntryAtPosition(x, y));
/*     */   }
/*     */   
/*     */   public void setFocused(GuiEventListener focused) {
/* 283 */     E oldFocus = getFocused();
/* 285 */     if (oldFocus != focused && oldFocus instanceof ContainerEventHandler) {
/* 285 */       ContainerEventHandler oldFocusContainer = (ContainerEventHandler)oldFocus;
/* 286 */       oldFocusContainer.setFocused(null);
/*     */     } 
/* 289 */     super.setFocused(focused);
/* 291 */     int index = this.children.indexOf(focused);
/* 292 */     if (index >= 0) {
/* 293 */       Entry entry = (Entry)this.children.get(index);
/* 294 */       setSelected((E)entry);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected E nextEntry(ScreenDirection dir) {
/* 299 */     return nextEntry(dir, entry -> true);
/*     */   }
/*     */   
/*     */   protected E nextEntry(ScreenDirection dir, Predicate<E> canSelect) {
/* 303 */     return nextEntry(dir, canSelect, getSelected());
/*     */   }
/*     */   
/*     */   protected E nextEntry(ScreenDirection dir, Predicate<E> canSelect, E startEntry) {
/* 307 */     switch (dir) {
/*     */       default:
/* 307 */         throw new MatchException(null, null);
/*     */       case RIGHT:
/*     */       case LEFT:
/*     */       
/*     */       case UP:
/*     */       
/*     */       case DOWN:
/*     */         break;
/*     */     } 
/* 307 */     int delta = 
/*     */       
/* 310 */       1;
/* 312 */     if (!children().isEmpty() && delta != 0) {
/*     */       int index;
/* 314 */       if (startEntry == null) {
/* 315 */         index = (delta > 0) ? 0 : (children().size() - 1);
/*     */       } else {
/* 317 */         index = children().indexOf(startEntry) + delta;
/*     */       } 
/*     */       int i;
/* 320 */       for (i = index; i >= 0 && i < this.children.size(); i += delta) {
/* 321 */         Entry entry = (Entry)children().get(i);
/* 322 */         if (canSelect.test((E)entry))
/* 323 */           return (E)entry; 
/*     */       } 
/*     */     } 
/* 327 */     return null;
/*     */   }
/*     */   
/*     */   protected void extractListItems(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 331 */     for (Entry entry : this.children) {
/* 332 */       if (entry.getY() + entry.getHeight() >= getY() && entry.getY() <= getBottom())
/* 333 */         extractItem(graphics, mouseX, mouseY, a, (E)entry); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void extractItem(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, E entry) {
/* 339 */     if (entriesCanBeSelected() && getSelected() == entry) {
/* 340 */       int outlineColor = isFocused() ? -1 : -8355712;
/* 341 */       extractSelection(graphics, entry, outlineColor);
/*     */     } 
/* 344 */     entry.extractContent(graphics, mouseX, mouseY, Objects.equals(this.hovered, entry), a);
/*     */   }
/*     */   
/*     */   protected void extractSelection(GuiGraphicsExtractor graphics, E entry, int outlineColor) {
/* 348 */     int outlineX0 = entry.getX();
/* 349 */     int outlineY0 = entry.getY();
/* 350 */     int outlineX1 = outlineX0 + entry.getWidth();
/* 351 */     int outlineY1 = outlineY0 + entry.getHeight();
/* 352 */     graphics.fill(outlineX0, outlineY0, outlineX1, outlineY1, outlineColor);
/* 353 */     graphics.fill(outlineX0 + 1, outlineY0 + 1, outlineX1 - 1, outlineY1 - 1, -16777216);
/*     */   }
/*     */   
/*     */   public int getRowLeft() {
/* 357 */     return getX() + this.width / 2 - getRowWidth() / 2;
/*     */   }
/*     */   
/*     */   public int getRowRight() {
/* 361 */     return getRowLeft() + getRowWidth();
/*     */   }
/*     */   
/*     */   public int getRowTop(int row) {
/* 365 */     return ((Entry)this.children.get(row)).getY();
/*     */   }
/*     */   
/*     */   public int getRowBottom(int row) {
/* 369 */     Entry entry = (Entry)this.children.get(row);
/* 370 */     return entry.getY() + entry.getHeight();
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/* 374 */     return 220;
/*     */   }
/*     */   
/*     */   public NarratableEntry.NarrationPriority narrationPriority() {
/* 379 */     if (isFocused())
/* 380 */       return NarratableEntry.NarrationPriority.FOCUSED; 
/* 382 */     if (this.hovered != null)
/* 383 */       return NarratableEntry.NarrationPriority.HOVERED; 
/* 385 */     return NarratableEntry.NarrationPriority.NONE;
/*     */   }
/*     */   
/*     */   protected void removeEntries(List<E> entries) {
/* 389 */     entries.forEach(this::removeEntry);
/*     */   }
/*     */   
/*     */   protected void removeEntry(E entry) {
/* 393 */     boolean removed = this.children.remove(entry);
/* 394 */     if (removed) {
/* 395 */       repositionEntries();
/* 396 */       if (entry == getSelected())
/* 397 */         setSelected((E)null); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected E getHovered() {
/* 403 */     return this.hovered;
/*     */   }
/*     */   
/*     */   private void bindEntryToSelf(Entry<E> entry) {
/* 407 */     entry.list = this;
/*     */   }
/*     */   
/*     */   protected void narrateListElementPosition(NarrationElementOutput output, E element) {
/* 411 */     List<E> children = children();
/* 412 */     if (children.size() > 1) {
/* 413 */       int index = children.indexOf(element);
/* 414 */       if (index != -1)
/* 415 */         output.add(NarratedElementType.POSITION, (Component)Component.translatable("narrator.position.list", new Object[] { Integer.valueOf(index + 1), Integer.valueOf(children.size()) })); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static abstract class Entry<E extends Entry<E>> implements LayoutElement, GuiEventListener {
/*     */     public static final int CONTENT_PADDING = 2;
/*     */     
/* 422 */     private int x = 0;
/*     */     
/* 423 */     private int y = 0;
/*     */     
/* 424 */     private int width = 0;
/*     */     
/*     */     private int height;
/*     */     
/*     */     @Deprecated
/*     */     private AbstractSelectionList<E> list;
/*     */     
/*     */     public void setFocused(boolean focused) {}
/*     */     
/*     */     public boolean isFocused() {
/* 437 */       return (this.list.getFocused() == this);
/*     */     }
/*     */     
/*     */     public abstract void extractContent(GuiGraphicsExtractor param1GuiGraphicsExtractor, int param1Int1, int param1Int2, boolean param1Boolean, float param1Float);
/*     */     
/*     */     public boolean isMouseOver(double mx, double my) {
/* 444 */       return getRectangle().containsPoint((int)mx, (int)my);
/*     */     }
/*     */     
/*     */     public void setX(int x) {
/* 449 */       this.x = x;
/*     */     }
/*     */     
/*     */     public void setY(int y) {
/* 454 */       this.y = y;
/*     */     }
/*     */     
/*     */     public void setWidth(int width) {
/* 458 */       this.width = width;
/*     */     }
/*     */     
/*     */     public void setHeight(int height) {
/* 462 */       this.height = height;
/*     */     }
/*     */     
/*     */     public int getContentX() {
/* 466 */       return getX() + 2;
/*     */     }
/*     */     
/*     */     public int getContentY() {
/* 470 */       return getY() + 2;
/*     */     }
/*     */     
/*     */     public int getContentHeight() {
/* 474 */       return getHeight() - 4;
/*     */     }
/*     */     
/*     */     public int getContentYMiddle() {
/* 478 */       return getContentY() + getContentHeight() / 2;
/*     */     }
/*     */     
/*     */     public int getContentBottom() {
/* 482 */       return getContentY() + getContentHeight();
/*     */     }
/*     */     
/*     */     public int getContentWidth() {
/* 486 */       return getWidth() - 4;
/*     */     }
/*     */     
/*     */     public int getContentXMiddle() {
/* 490 */       return getContentX() + getContentWidth() / 2;
/*     */     }
/*     */     
/*     */     public int getContentRight() {
/* 494 */       return getContentX() + getContentWidth();
/*     */     }
/*     */     
/*     */     public int getX() {
/* 499 */       return this.x;
/*     */     }
/*     */     
/*     */     public int getY() {
/* 504 */       return this.y;
/*     */     }
/*     */     
/*     */     public int getWidth() {
/* 509 */       return this.width;
/*     */     }
/*     */     
/*     */     public int getHeight() {
/* 514 */       return this.height;
/*     */     }
/*     */     
/*     */     public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {}
/*     */     
/*     */     public ScreenRectangle getRectangle() {
/* 523 */       return super.getRectangle();
/*     */     }
/*     */   }
/*     */   
/*     */   private class TrackedList extends AbstractList<E> {
/* 528 */     private final List<E> delegate = Lists.newArrayList();
/*     */     
/*     */     public E get(int index) {
/* 532 */       return this.delegate.get(index);
/*     */     }
/*     */     
/*     */     public int size() {
/* 537 */       return this.delegate.size();
/*     */     }
/*     */     
/*     */     public E set(int index, E element) {
/* 542 */       AbstractSelectionList.Entry entry = (AbstractSelectionList.Entry)this.delegate.set(index, element);
/* 543 */       AbstractSelectionList.this.bindEntryToSelf((AbstractSelectionList.Entry)element);
/* 544 */       return (E)entry;
/*     */     }
/*     */     
/*     */     public void add(int index, E element) {
/* 549 */       this.delegate.add(index, element);
/* 550 */       AbstractSelectionList.this.bindEntryToSelf((AbstractSelectionList.Entry)element);
/*     */     }
/*     */     
/*     */     public E remove(int index) {
/* 555 */       return this.delegate.remove(index);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\AbstractSelectionList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */