/*     */ package net.minecraft.client.gui.screens.achievement;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.ItemDisplayWidget;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.components.tabs.LoadingTab;
/*     */ import net.minecraft.client.gui.components.tabs.MenuTabBar;
/*     */ import net.minecraft.client.gui.components.tabs.Tab;
/*     */ import net.minecraft.client.gui.components.tabs.TabManager;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.resources.language.I18n;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.stats.Stat;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.stats.StatsCounter;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ public class StatsScreen extends Screen {
/*  54 */   private static final Component TITLE = (Component)Component.translatable("gui.stats");
/*     */   
/*  56 */   private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
/*     */   
/*  57 */   private static final Identifier HEADER_SPRITE = Identifier.withDefaultNamespace("statistics/header");
/*     */   
/*  58 */   private static final Identifier SORT_UP_SPRITE = Identifier.withDefaultNamespace("statistics/sort_up");
/*     */   
/*  59 */   private static final Identifier SORT_DOWN_SPRITE = Identifier.withDefaultNamespace("statistics/sort_down");
/*     */   
/*  61 */   private static final Component PENDING_TEXT = (Component)Component.translatable("multiplayer.downloadingStats");
/*     */   
/*  62 */   private static final Component NO_VALUE_DISPLAY = (Component)Component.translatable("stats.none");
/*     */   
/*  63 */   private static final Component GENERAL_BUTTON = (Component)Component.translatable("stat.generalButton");
/*     */   
/*  64 */   private static final Component ITEMS_BUTTON = (Component)Component.translatable("stat.itemsButton");
/*     */   
/*  65 */   private static final Component MOBS_BUTTON = (Component)Component.translatable("stat.mobsButton");
/*     */   
/*     */   protected final Screen lastScreen;
/*     */   
/*     */   private static final int LIST_WIDTH = 280;
/*     */   
/*  71 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private final TabManager tabManager;
/*     */   
/*     */   private MenuTabBar tabNavigationBar;
/*     */   
/*     */   private final StatsCounter stats;
/*     */   
/*     */   private boolean isLoading;
/*     */   
/*     */   public StatsScreen(Screen lastScreen, StatsCounter stats) {
/*  82 */     super(TITLE);
/*     */     StatsScreen statsScreen = this;
/*     */     statsScreen = this;
/*     */     this.tabManager = new TabManager(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0), x$0 -> rec$.removeWidget((GuiEventListener)x$0));
/*     */     this.isLoading = true;
/*  84 */     this.lastScreen = lastScreen;
/*  85 */     this.stats = stats;
/*     */   }
/*     */   
/*     */   protected void init() {
/*  90 */     Component loadingTitle = PENDING_TEXT;
/*  91 */     this
/*     */       
/*  96 */       .tabNavigationBar = MenuTabBar.builder(this.tabManager, this.width).addTabs(new Tab[] { (Tab)new LoadingTab(getFont(), GENERAL_BUTTON, loadingTitle), (Tab)new LoadingTab(getFont(), ITEMS_BUTTON, loadingTitle), (Tab)new LoadingTab(getFont(), MOBS_BUTTON, loadingTitle) }).build();
/*  97 */     addRenderableWidget((GuiEventListener)this.tabNavigationBar);
/*  98 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(200).build());
/* 100 */     this.tabNavigationBar.setTabActiveState(0, true);
/* 101 */     this.tabNavigationBar.setTabActiveState(1, false);
/* 102 */     this.tabNavigationBar.setTabActiveState(2, false);
/* 103 */     this.layout.visitWidgets(button -> {
/*     */           button.setTabOrderGroup(1);
/*     */           addRenderableWidget((GuiEventListener)button);
/*     */         });
/* 107 */     this.tabNavigationBar.selectTab(0, false);
/* 108 */     repositionElements();
/* 109 */     this.minecraft.getConnection().send((Packet)new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS));
/*     */   }
/*     */   
/*     */   public void onStatsUpdated() {
/* 113 */     if (this.isLoading) {
/* 114 */       if (this.tabNavigationBar != null)
/* 115 */         removeWidget((GuiEventListener)this.tabNavigationBar); 
/* 117 */       this
/*     */         
/* 122 */         .tabNavigationBar = MenuTabBar.builder(this.tabManager, this.width).addTabs(new Tab[] { (Tab)new StatisticsTab(GENERAL_BUTTON, (AbstractSelectionList<?>)new GeneralStatisticsList(this.minecraft)), (Tab)new StatisticsTab(ITEMS_BUTTON, (AbstractSelectionList<?>)new ItemStatisticsList(this.minecraft)), (Tab)new StatisticsTab(MOBS_BUTTON, (AbstractSelectionList<?>)new MobsStatisticsList(this.minecraft)) }).build();
/* 123 */       setFocused((GuiEventListener)this.tabNavigationBar);
/* 124 */       addRenderableWidget((GuiEventListener)this.tabNavigationBar);
/* 125 */       setTabActiveStateAndTooltip(1);
/* 126 */       setTabActiveStateAndTooltip(2);
/* 127 */       this.tabNavigationBar.selectTab(0, false);
/* 128 */       repositionElements();
/* 129 */       this.isLoading = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setTabActiveStateAndTooltip(int index) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield tabNavigationBar : Lnet/minecraft/client/gui/components/tabs/MenuTabBar;
/*     */     //   4: ifnonnull -> 8
/*     */     //   7: return
/*     */     //   8: aload_0
/*     */     //   9: getfield tabNavigationBar : Lnet/minecraft/client/gui/components/tabs/MenuTabBar;
/*     */     //   12: invokevirtual getTabs : ()Ljava/util/List;
/*     */     //   15: iload_1
/*     */     //   16: invokeinterface get : (I)Ljava/lang/Object;
/*     */     //   21: astore #4
/*     */     //   23: aload #4
/*     */     //   25: instanceof net/minecraft/client/gui/screens/achievement/StatsScreen$StatisticsTab
/*     */     //   28: ifeq -> 56
/*     */     //   31: aload #4
/*     */     //   33: checkcast net/minecraft/client/gui/screens/achievement/StatsScreen$StatisticsTab
/*     */     //   36: astore_3
/*     */     //   37: aload_3
/*     */     //   38: getfield list : Lnet/minecraft/client/gui/components/AbstractSelectionList;
/*     */     //   41: invokevirtual children : ()Ljava/util/List;
/*     */     //   44: invokeinterface isEmpty : ()Z
/*     */     //   49: ifne -> 56
/*     */     //   52: iconst_1
/*     */     //   53: goto -> 57
/*     */     //   56: iconst_0
/*     */     //   57: istore_2
/*     */     //   58: aload_0
/*     */     //   59: getfield tabNavigationBar : Lnet/minecraft/client/gui/components/tabs/MenuTabBar;
/*     */     //   62: iload_1
/*     */     //   63: iload_2
/*     */     //   64: invokevirtual setTabActiveState : (IZ)V
/*     */     //   67: iload_2
/*     */     //   68: ifeq -> 83
/*     */     //   71: aload_0
/*     */     //   72: getfield tabNavigationBar : Lnet/minecraft/client/gui/components/tabs/MenuTabBar;
/*     */     //   75: iload_1
/*     */     //   76: aconst_null
/*     */     //   77: invokevirtual setTabTooltip : (ILnet/minecraft/client/gui/components/Tooltip;)V
/*     */     //   80: goto -> 99
/*     */     //   83: aload_0
/*     */     //   84: getfield tabNavigationBar : Lnet/minecraft/client/gui/components/tabs/MenuTabBar;
/*     */     //   87: iload_1
/*     */     //   88: ldc 'gui.stats.none_found'
/*     */     //   90: invokestatic translatable : (Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;
/*     */     //   93: invokestatic create : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/client/gui/components/Tooltip;
/*     */     //   96: invokevirtual setTabTooltip : (ILnet/minecraft/client/gui/components/Tooltip;)V
/*     */     //   99: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #134	-> 0
/*     */     //   #135	-> 7
/*     */     //   #137	-> 8
/*     */     //   #138	-> 58
/*     */     //   #139	-> 67
/*     */     //   #140	-> 71
/*     */     //   #142	-> 83
/*     */     //   #144	-> 99
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   37	19	3	statsTab	Lnet/minecraft/client/gui/screens/achievement/StatsScreen$StatisticsTab;
/*     */     //   0	100	0	this	Lnet/minecraft/client/gui/screens/achievement/StatsScreen;
/*     */     //   0	100	1	index	I
/*     */     //   58	42	2	active	Z
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 148 */     if (this.tabNavigationBar == null)
/*     */       return; 
/* 151 */     this.tabNavigationBar.arrangeElements(this.width);
/* 153 */     int tabAreaTop = this.tabNavigationBar.getRectangle().bottom();
/* 154 */     ScreenRectangle tabArea = new ScreenRectangle(0, tabAreaTop, this.width, this.height - this.layout.getFooterHeight() - tabAreaTop);
/* 155 */     this.tabNavigationBar.getTabs().forEach(tab -> tab.visitChildren(()));
/* 156 */     this.tabManager.setTabArea(tabArea);
/* 157 */     this.layout.setHeaderHeight(tabAreaTop);
/* 158 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 163 */     if (this.tabNavigationBar != null && this.tabNavigationBar.keyPressed(event))
/* 164 */       return true; 
/* 166 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int xm, int ym, float a) {
/* 171 */     super.extractRenderState(graphics, xm, ym, a);
/* 172 */     graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, 0, this.height - this.layout.getFooterHeight(), 0.0F, 0.0F, this.width, 2, 32, 2);
/*     */   }
/*     */   
/*     */   protected void extractMenuBackground(GuiGraphicsExtractor graphics) {
/* 177 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CreateWorldScreen.TAB_HEADER_BACKGROUND, 0, 0, 0.0F, 0.0F, this.width, this.layout.getHeaderHeight(), 16, 16);
/* 178 */     extractMenuBackground(graphics, 0, this.layout.getHeaderHeight(), this.width, this.height);
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 183 */     this.minecraft.gui.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   private class StatisticsTab extends GridLayoutTab {
/*     */     protected final AbstractSelectionList<?> list;
/*     */     
/*     */     public StatisticsTab(Component title, AbstractSelectionList<?> list) {
/* 190 */       super(title);
/* 191 */       this.layout.addChild((LayoutElement)list, 1, 1);
/* 192 */       this.list = list;
/*     */     }
/*     */     
/*     */     public void doLayout(ScreenRectangle screenRectangle) {
/* 197 */       this.list.updateSizeAndPosition(StatsScreen.this.width, StatsScreen.this.layout.getContentHeight(), StatsScreen.this.layout.getHeaderHeight());
/* 198 */       super.doLayout(screenRectangle);
/*     */     }
/*     */   }
/*     */   
/*     */   private class GeneralStatisticsList extends ObjectSelectionList<GeneralStatisticsList.Entry> {
/*     */     public GeneralStatisticsList(Minecraft minecraft) {
/* 204 */       super(minecraft, StatsScreen.this.width, StatsScreen.this.layout.getContentHeight(), 33, 14);
/* 206 */       ObjectArrayList<Stat<Identifier>> stats = new ObjectArrayList(Stats.CUSTOM.iterator());
/* 207 */       stats.sort(Comparator.comparing(k -> I18n.get(StatsScreen.getTranslationKey(k), new Object[0])));
/* 208 */       for (ObjectListIterator<Stat<Identifier>> objectListIterator = stats.iterator(); objectListIterator.hasNext(); ) {
/* 208 */         Stat<Identifier> stat = objectListIterator.next();
/* 209 */         addEntry((AbstractSelectionList.Entry)new Entry(stat));
/*     */       } 
/*     */     }
/*     */     
/*     */     public int getRowWidth() {
/* 215 */       return 280;
/*     */     }
/*     */     
/*     */     protected void extractListBackground(GuiGraphicsExtractor graphics) {}
/*     */     
/*     */     protected void extractListSeparators(GuiGraphicsExtractor graphics) {}
/*     */     
/*     */     private class Entry extends ObjectSelectionList.Entry<Entry> {
/*     */       private final Stat<Identifier> stat;
/*     */       
/*     */       private final Component statDisplay;
/*     */       
/*     */       private Entry(Stat<Identifier> stat) {
/* 231 */         this.stat = stat;
/* 232 */         this.statDisplay = (Component)Component.translatable(StatsScreen.getTranslationKey(stat));
/*     */       }
/*     */       
/*     */       private String getValueText() {
/* 236 */         return this.stat.format(StatsScreen.this.stats.getValue(this.stat));
/*     */       }
/*     */       
/*     */       public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 241 */         Objects.requireNonNull(StatsScreen.this.font);
/* 241 */         int y = getContentYMiddle() - 9 / 2;
/* 242 */         int index = StatsScreen.GeneralStatisticsList.this.children().indexOf(this);
/* 243 */         int color = (index % 2 == 0) ? -1 : -4539718;
/* 244 */         graphics.text(StatsScreen.this.font, this.statDisplay, getContentX() + 2, y, color);
/* 245 */         String msg = getValueText();
/* 246 */         graphics.text(StatsScreen.this.font, msg, getContentRight() - StatsScreen.this.font.width(msg) - 4, y, color);
/*     */       }
/*     */       
/*     */       public Component getNarration() {
/* 251 */         return (Component)Component.translatable("narrator.select", new Object[] { Component.empty().append(this.statDisplay).append(CommonComponents.SPACE).append(getValueText()) });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class Entry extends ObjectSelectionList.Entry<GeneralStatisticsList.Entry> {
/*     */     private final Stat<Identifier> stat;
/*     */     
/*     */     private final Component statDisplay;
/*     */     
/*     */     private Entry(Stat<Identifier> stat) {
/*     */       this.stat = stat;
/*     */       this.statDisplay = (Component)Component.translatable(StatsScreen.getTranslationKey(stat));
/*     */     }
/*     */     
/*     */     private String getValueText() {
/*     */       return this.stat.format(StatsScreen.this.stats.getValue(this.stat));
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       Objects.requireNonNull(StatsScreen.this.font);
/*     */       int y = getContentYMiddle() - 9 / 2;
/*     */       int index = this.this$1.children().indexOf(this);
/*     */       int color = (index % 2 == 0) ? -1 : -4539718;
/*     */       graphics.text(StatsScreen.this.font, this.statDisplay, getContentX() + 2, y, color);
/*     */       String msg = getValueText();
/*     */       graphics.text(StatsScreen.this.font, msg, getContentRight() - StatsScreen.this.font.width(msg) - 4, y, color);
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 251 */       return (Component)Component.translatable("narrator.select", new Object[] { Component.empty().append(this.statDisplay).append(CommonComponents.SPACE).append(getValueText()) });
/*     */     }
/*     */   }
/*     */   
/*     */   private static String getTranslationKey(Stat<Identifier> stat) {
/* 257 */     return "stat." + ((Identifier)stat.getValue()).toString().replace(':', '.');
/*     */   }
/*     */   
/*     */   private class ItemStatisticsList extends ContainerObjectSelectionList<ItemStatisticsList.Entry> {
/*     */     private static final int SLOT_BG_SIZE = 18;
/*     */     
/*     */     private static final int SLOT_STAT_HEIGHT = 22;
/*     */     
/*     */     private static final int SLOT_BG_Y = 1;
/*     */     
/*     */     private static final int SORT_NONE = 0;
/*     */     
/*     */     private static final int SORT_DOWN = -1;
/*     */     
/*     */     private static final int SORT_UP = 1;
/*     */     
/*     */     protected final List<StatType<Block>> blockColumns;
/*     */     
/*     */     protected final List<StatType<Item>> itemColumns;
/*     */     
/* 272 */     protected final Comparator<ItemRow> itemStatSorter = new ItemRowComparator();
/*     */     
/*     */     protected StatType<?> sortColumn;
/*     */     
/*     */     protected int sortOrder;
/*     */     
/*     */     public ItemStatisticsList(Minecraft minecraft) {
/* 278 */       super(minecraft, StatsScreen.this.width, StatsScreen.this.layout.getContentHeight(), 33, 22);
/* 280 */       this.blockColumns = Lists.newArrayList();
/* 281 */       this.blockColumns.add(Stats.BLOCK_MINED);
/* 282 */       this.itemColumns = Lists.newArrayList((Object[])new StatType[] { Stats.ITEM_BROKEN, Stats.ITEM_CRAFTED, Stats.ITEM_USED, Stats.ITEM_PICKED_UP, Stats.ITEM_DROPPED });
/* 284 */       Set<Item> items = Sets.newIdentityHashSet();
/* 285 */       for (Item item : BuiltInRegistries.ITEM) {
/* 286 */         boolean addToList = false;
/* 288 */         for (StatType<Item> type : this.itemColumns) {
/* 289 */           if (type.contains(item) && StatsScreen.this.stats.getValue(type.get(item)) > 0)
/* 290 */             addToList = true; 
/*     */         } 
/* 294 */         if (addToList)
/* 295 */           items.add(item); 
/*     */       } 
/* 299 */       for (Block block : BuiltInRegistries.BLOCK) {
/* 300 */         boolean addToList = false;
/* 302 */         for (StatType<Block> type : this.blockColumns) {
/* 303 */           if (type.contains(block) && StatsScreen.this.stats.getValue(type.get(block)) > 0)
/* 304 */             addToList = true; 
/*     */         } 
/* 308 */         if (addToList)
/* 309 */           items.add(block.asItem()); 
/*     */       } 
/* 313 */       items.remove(Items.AIR);
/* 314 */       if (!items.isEmpty()) {
/* 315 */         addEntry((AbstractSelectionList.Entry)new HeaderEntry());
/* 316 */         for (Item item : items)
/* 317 */           addEntry((AbstractSelectionList.Entry)new ItemRow(item)); 
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void extractListBackground(GuiGraphicsExtractor graphics) {}
/*     */     
/*     */     private int getColumnX(int col) {
/* 327 */       return 75 + 40 * col;
/*     */     }
/*     */     
/*     */     public int getRowWidth() {
/* 332 */       return 280;
/*     */     }
/*     */     
/*     */     private StatType<?> getColumn(int i) {
/* 336 */       return (i < this.blockColumns.size()) ? this.blockColumns.get(i) : this.itemColumns.get(i - this.blockColumns.size());
/*     */     }
/*     */     
/*     */     private int getColumnIndex(StatType<?> column) {
/* 340 */       int i = this.blockColumns.indexOf(column);
/* 341 */       if (i >= 0)
/* 342 */         return i; 
/* 344 */       int j = this.itemColumns.indexOf(column);
/* 345 */       if (j >= 0)
/* 346 */         return j + this.blockColumns.size(); 
/* 348 */       return -1;
/*     */     }
/*     */     
/*     */     protected void sortByColumn(StatType<?> column) {
/* 352 */       if (column != this.sortColumn) {
/* 353 */         this.sortColumn = column;
/* 354 */         this.sortOrder = -1;
/* 355 */       } else if (this.sortOrder == -1) {
/* 356 */         this.sortOrder = 1;
/*     */       } else {
/* 358 */         this.sortColumn = null;
/* 359 */         this.sortOrder = 0;
/*     */       } 
/* 362 */       sortItems(this.itemStatSorter);
/*     */     }
/*     */     
/*     */     protected void sortItems(Comparator<ItemRow> comparator) {
/* 366 */       List<ItemRow> itemRows = getItemRows();
/* 367 */       itemRows.sort(comparator);
/* 368 */       clearEntriesExcept((AbstractSelectionList.Entry)children().getFirst());
/* 369 */       for (ItemRow newChild : itemRows)
/* 370 */         addEntry((AbstractSelectionList.Entry)newChild); 
/*     */     }
/*     */     
/*     */     private List<ItemRow> getItemRows() {
/* 375 */       List<ItemRow> itemRows = new ArrayList<>();
/* 376 */       children().forEach(entry -> {
/*     */             if (entry instanceof ItemRow) {
/*     */               ItemRow itemRow = (ItemRow)entry;
/*     */               itemRows.add(itemRow);
/*     */             } 
/*     */           });
/* 381 */       return itemRows;
/*     */     }
/*     */     
/*     */     private class ItemRowComparator implements Comparator<ItemRow> {
/*     */       public int compare(StatsScreen.ItemStatisticsList.ItemRow one, StatsScreen.ItemStatisticsList.ItemRow two) {
/*     */         int key1, key2;
/* 388 */         Item item1 = one.getItem();
/* 389 */         Item item2 = two.getItem();
/* 393 */         if (StatsScreen.ItemStatisticsList.this.sortColumn == null) {
/* 394 */           key1 = 0;
/* 395 */           key2 = 0;
/* 396 */         } else if (StatsScreen.ItemStatisticsList.this.blockColumns.contains(StatsScreen.ItemStatisticsList.this.sortColumn)) {
/* 397 */           StatType<Block> type = (StatType)StatsScreen.ItemStatisticsList.this.sortColumn;
/* 398 */           BlockItem blockItem = (BlockItem)item1;
/* 398 */           key1 = (item1 instanceof BlockItem) ? StatsScreen.this.stats.getValue(type, blockItem.getBlock()) : -1;
/* 399 */           blockItem = (BlockItem)item2;
/* 399 */           key2 = (item2 instanceof BlockItem) ? StatsScreen.this.stats.getValue(type, blockItem.getBlock()) : -1;
/*     */         } else {
/* 401 */           StatType<Item> type = (StatType)StatsScreen.ItemStatisticsList.this.sortColumn;
/* 402 */           key1 = StatsScreen.this.stats.getValue(type, item1);
/* 403 */           key2 = StatsScreen.this.stats.getValue(type, item2);
/*     */         } 
/* 406 */         if (key1 == key2)
/* 407 */           return StatsScreen.ItemStatisticsList.this.sortOrder * Integer.compare(Item.getId(item1), Item.getId(item2)); 
/* 410 */         return StatsScreen.ItemStatisticsList.this.sortOrder * Integer.compare(key1, key2);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void extractListSeparators(GuiGraphicsExtractor graphics) {}
/*     */     
/*     */     private static abstract class Entry extends ContainerObjectSelectionList.Entry<Entry> {}
/*     */     
/*     */     private class ItemRow extends Entry {
/*     */       private final Item item;
/*     */       
/*     */       private final ItemRowWidget itemRowWidget;
/*     */       
/*     */       private ItemRow(Item item) {
/* 426 */         this.item = item;
/* 427 */         this.itemRowWidget = new ItemRowWidget(item.getDefaultInstance());
/*     */       }
/*     */       
/*     */       protected Item getItem() {
/* 431 */         return this.item;
/*     */       }
/*     */       
/*     */       public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 436 */         this.itemRowWidget.setPosition(getContentX(), getContentY());
/* 437 */         this.itemRowWidget.extractRenderState(graphics, mouseX, mouseY, a);
/* 439 */         StatsScreen.ItemStatisticsList itemStatsList = StatsScreen.ItemStatisticsList.this;
/* 440 */         int index = itemStatsList.children().indexOf(this);
/*     */         int col;
/* 441 */         for (col = 0; col < itemStatsList.blockColumns.size(); col++) {
/*     */           Stat<Block> stat;
/* 443 */           Item item = this.item;
/* 443 */           if (item instanceof BlockItem) {
/* 443 */             BlockItem blockItem = (BlockItem)item;
/* 444 */             stat = ((StatType)itemStatsList.blockColumns.get(col)).get(blockItem.getBlock());
/*     */           } else {
/* 446 */             stat = null;
/*     */           } 
/* 448 */           Objects.requireNonNull(StatsScreen.this.font);
/* 448 */           extractStat(graphics, stat, getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(col), getContentYMiddle() - 9 / 2, (index % 2 == 0));
/*     */         } 
/* 450 */         for (col = 0; col < itemStatsList.itemColumns.size(); 
/* 451 */           extractStat(graphics, ((StatType)itemStatsList.itemColumns.get(col)).get(this.item), getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(col + itemStatsList.blockColumns.size()), 
/* 452 */             getContentYMiddle() - 9 / 2, (index % 2 == 0)), col++)
/* 452 */           Objects.requireNonNull(StatsScreen.this.font); 
/*     */       }
/*     */       
/*     */       protected void extractStat(GuiGraphicsExtractor graphics, Stat<?> stat, int x, int y, boolean shaded) {
/* 457 */         Component msg = (stat == null) ? StatsScreen.NO_VALUE_DISPLAY : (Component)Component.literal(stat.format(StatsScreen.this.stats.getValue(stat)));
/* 458 */         graphics.text(StatsScreen.this.font, msg, x - StatsScreen.this.font.width((FormattedText)msg), y, shaded ? -1 : -4539718);
/*     */       }
/*     */       
/*     */       public List<? extends NarratableEntry> narratables() {
/* 463 */         return (List)List.of(this.itemRowWidget);
/*     */       }
/*     */       
/*     */       public List<? extends GuiEventListener> children() {
/* 468 */         return (List)List.of(this.itemRowWidget);
/*     */       }
/*     */       
/*     */       private class ItemRowWidget extends ItemDisplayWidget {
/*     */         private ItemRowWidget(ItemStack itemStack) {
/* 473 */           super(StatsScreen.ItemStatisticsList.this.minecraft, 1, 1, 18, 18, itemStack.getHoverName(), itemStack, false, true);
/*     */         }
/*     */         
/*     */         protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 478 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, StatsScreen.SLOT_SPRITE, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX(), StatsScreen.ItemStatisticsList.ItemRow.this.getContentY(), 18, 18);
/* 479 */           super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/*     */         }
/*     */         
/*     */         protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
/* 484 */           super.extractTooltip(graphics, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX() + 18, StatsScreen.ItemStatisticsList.ItemRow.this.getContentY() + 18);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private class HeaderEntry extends Entry {
/* 490 */       private static final Identifier BLOCK_MINED_SPRITE = Identifier.withDefaultNamespace("statistics/block_mined");
/*     */       
/* 491 */       private static final Identifier ITEM_BROKEN_SPRITE = Identifier.withDefaultNamespace("statistics/item_broken");
/*     */       
/* 492 */       private static final Identifier ITEM_CRAFTED_SPRITE = Identifier.withDefaultNamespace("statistics/item_crafted");
/*     */       
/* 493 */       private static final Identifier ITEM_USED_SPRITE = Identifier.withDefaultNamespace("statistics/item_used");
/*     */       
/* 494 */       private static final Identifier ITEM_PICKED_UP_SPRITE = Identifier.withDefaultNamespace("statistics/item_picked_up");
/*     */       
/* 495 */       private static final Identifier ITEM_DROPPED_SPRITE = Identifier.withDefaultNamespace("statistics/item_dropped");
/*     */       
/*     */       private final StatSortButton blockMined;
/*     */       
/*     */       private final StatSortButton itemBroken;
/*     */       
/*     */       private final StatSortButton itemCrafted;
/*     */       
/*     */       private final StatSortButton itemUsed;
/*     */       
/*     */       private final StatSortButton itemPickedUp;
/*     */       
/*     */       private final StatSortButton itemDropped;
/*     */       
/* 504 */       private final List<AbstractWidget> children = new ArrayList<>();
/*     */       
/*     */       private HeaderEntry() {
/* 507 */         this.blockMined = new StatSortButton(this, 0, BLOCK_MINED_SPRITE);
/* 508 */         this.itemBroken = new StatSortButton(this, 1, ITEM_BROKEN_SPRITE);
/* 509 */         this.itemCrafted = new StatSortButton(this, 2, ITEM_CRAFTED_SPRITE);
/* 510 */         this.itemUsed = new StatSortButton(this, 3, ITEM_USED_SPRITE);
/* 511 */         this.itemPickedUp = new StatSortButton(this, 4, ITEM_PICKED_UP_SPRITE);
/* 512 */         this.itemDropped = new StatSortButton(this, 5, ITEM_DROPPED_SPRITE);
/* 513 */         this.children.addAll((Collection)List.of(this.blockMined, this.itemBroken, this.itemCrafted, this.itemUsed, this.itemPickedUp, this.itemDropped));
/*     */       }
/*     */       
/*     */       public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 518 */         this.blockMined.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(0) - 18, getContentY() + 1);
/* 519 */         this.blockMined.extractRenderState(graphics, mouseX, mouseY, a);
/* 520 */         this.itemBroken.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(1) - 18, getContentY() + 1);
/* 521 */         this.itemBroken.extractRenderState(graphics, mouseX, mouseY, a);
/* 522 */         this.itemCrafted.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(2) - 18, getContentY() + 1);
/* 523 */         this.itemCrafted.extractRenderState(graphics, mouseX, mouseY, a);
/* 524 */         this.itemUsed.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(3) - 18, getContentY() + 1);
/* 525 */         this.itemUsed.extractRenderState(graphics, mouseX, mouseY, a);
/* 526 */         this.itemPickedUp.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(4) - 18, getContentY() + 1);
/* 527 */         this.itemPickedUp.extractRenderState(graphics, mouseX, mouseY, a);
/* 528 */         this.itemDropped.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(5) - 18, getContentY() + 1);
/* 529 */         this.itemDropped.extractRenderState(graphics, mouseX, mouseY, a);
/* 531 */         if (StatsScreen.ItemStatisticsList.this.sortColumn != null) {
/* 532 */           int offset = StatsScreen.ItemStatisticsList.this.getColumnX(StatsScreen.ItemStatisticsList.this.getColumnIndex(StatsScreen.ItemStatisticsList.this.sortColumn)) - 36;
/* 534 */           Identifier sprite = (StatsScreen.ItemStatisticsList.this.sortOrder == 1) ? StatsScreen.SORT_UP_SPRITE : StatsScreen.SORT_DOWN_SPRITE;
/* 535 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getContentX() + offset, getContentY() + 1, 18, 18);
/*     */         } 
/*     */       }
/*     */       
/*     */       public List<? extends GuiEventListener> children() {
/* 541 */         return (List)this.children;
/*     */       }
/*     */       
/*     */       public List<? extends NarratableEntry> narratables() {
/* 546 */         return (List)this.children;
/*     */       }
/*     */       
/*     */       private class StatSortButton extends ImageButton {
/*     */         private final Identifier sprite;
/*     */         
/*     */         private StatSortButton(StatsScreen.ItemStatisticsList.HeaderEntry this$0, int column, Identifier sprite) {
/* 553 */           super(18, 18, new WidgetSprites(StatsScreen.HEADER_SPRITE, StatsScreen.SLOT_SPRITE), button -> this$2.this$1.sortByColumn(this$2.this$1.getColumn(column)), this$0.this$1
/* 554 */               .getColumn(column).getDisplayName());
/* 555 */           this.sprite = sprite;
/* 556 */           setTooltip(Tooltip.create(getMessage()));
/*     */         }
/*     */         
/*     */         public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 561 */           Identifier background = this.sprites.get(isActive(), isHoveredOrFocused());
/* 562 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, getX(), getY(), this.width, this.height);
/* 563 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX(), getY(), this.width, this.height);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class ItemRowComparator implements Comparator<ItemStatisticsList.ItemRow> {
/*     */     public int compare(StatsScreen.ItemStatisticsList.ItemRow one, StatsScreen.ItemStatisticsList.ItemRow two) {
/*     */       int key1, key2;
/*     */       Item item1 = one.getItem();
/*     */       Item item2 = two.getItem();
/*     */       if (this.this$1.sortColumn == null) {
/*     */         key1 = 0;
/*     */         key2 = 0;
/*     */       } else if (this.this$1.blockColumns.contains(this.this$1.sortColumn)) {
/*     */         StatType<Block> type = (StatType)this.this$1.sortColumn;
/*     */         BlockItem blockItem = (BlockItem)item1;
/*     */         key1 = (item1 instanceof BlockItem) ? StatsScreen.this.stats.getValue(type, blockItem.getBlock()) : -1;
/*     */         blockItem = (BlockItem)item2;
/*     */         key2 = (item2 instanceof BlockItem) ? StatsScreen.this.stats.getValue(type, blockItem.getBlock()) : -1;
/*     */       } else {
/*     */         StatType<Item> type = (StatType)this.this$1.sortColumn;
/*     */         key1 = StatsScreen.this.stats.getValue(type, item1);
/*     */         key2 = StatsScreen.this.stats.getValue(type, item2);
/*     */       } 
/*     */       if (key1 == key2)
/*     */         return this.this$1.sortOrder * Integer.compare(Item.getId(item1), Item.getId(item2)); 
/*     */       return this.this$1.sortOrder * Integer.compare(key1, key2);
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class Entry extends ContainerObjectSelectionList.Entry<ItemStatisticsList.Entry> {}
/*     */   
/*     */   private class ItemRow extends ItemStatisticsList.Entry {
/*     */     private final Item item;
/*     */     
/*     */     private final ItemRowWidget itemRowWidget;
/*     */     
/*     */     private ItemRow(Item item) {
/*     */       this.item = item;
/*     */       this.itemRowWidget = new ItemRowWidget(item.getDefaultInstance());
/*     */     }
/*     */     
/*     */     protected Item getItem() {
/*     */       return this.item;
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       this.itemRowWidget.setPosition(getContentX(), getContentY());
/*     */       this.itemRowWidget.extractRenderState(graphics, mouseX, mouseY, a);
/*     */       StatsScreen.ItemStatisticsList itemStatsList = this.this$1;
/*     */       int index = itemStatsList.children().indexOf(this);
/*     */       int col;
/*     */       for (col = 0; col < itemStatsList.blockColumns.size(); col++) {
/*     */         Stat<Block> stat;
/*     */         Item item = this.item;
/*     */         if (item instanceof BlockItem) {
/*     */           BlockItem blockItem = (BlockItem)item;
/*     */           stat = ((StatType)itemStatsList.blockColumns.get(col)).get(blockItem.getBlock());
/*     */         } else {
/*     */           stat = null;
/*     */         } 
/*     */         Objects.requireNonNull(StatsScreen.this.font);
/*     */         extractStat(graphics, stat, getContentX() + this.this$1.getColumnX(col), getContentYMiddle() - 9 / 2, (index % 2 == 0));
/*     */       } 
/*     */       for (col = 0; col < itemStatsList.itemColumns.size(); extractStat(graphics, ((StatType)itemStatsList.itemColumns.get(col)).get(this.item), getContentX() + this.this$1.getColumnX(col + itemStatsList.blockColumns.size()), getContentYMiddle() - 9 / 2, (index % 2 == 0)), col++)
/*     */         Objects.requireNonNull(StatsScreen.this.font); 
/*     */     }
/*     */     
/*     */     protected void extractStat(GuiGraphicsExtractor graphics, Stat<?> stat, int x, int y, boolean shaded) {
/*     */       Component msg = (stat == null) ? StatsScreen.NO_VALUE_DISPLAY : (Component)Component.literal(stat.format(StatsScreen.this.stats.getValue(stat)));
/*     */       graphics.text(StatsScreen.this.font, msg, x - StatsScreen.this.font.width((FormattedText)msg), y, shaded ? -1 : -4539718);
/*     */     }
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/*     */       return (List)List.of(this.itemRowWidget);
/*     */     }
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/*     */       return (List)List.of(this.itemRowWidget);
/*     */     }
/*     */     
/*     */     private class ItemRowWidget extends ItemDisplayWidget {
/*     */       private ItemRowWidget(ItemStack itemStack) {
/*     */         super(this$0.this$1.minecraft, 1, 1, 18, 18, itemStack.getHoverName(), itemStack, false, true);
/*     */       }
/*     */       
/*     */       protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*     */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, StatsScreen.SLOT_SPRITE, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX(), StatsScreen.ItemStatisticsList.ItemRow.this.getContentY(), 18, 18);
/*     */         super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/*     */       }
/*     */       
/*     */       protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
/*     */         super.extractTooltip(graphics, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX() + 18, StatsScreen.ItemStatisticsList.ItemRow.this.getContentY() + 18);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class ItemRowWidget extends ItemDisplayWidget {
/*     */     private ItemRowWidget(ItemStack itemStack) {
/*     */       super(((StatsScreen.ItemStatisticsList.ItemRow)this$0).this$1.minecraft, 1, 1, 18, 18, itemStack.getHoverName(), itemStack, false, true);
/*     */     }
/*     */     
/*     */     protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*     */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, StatsScreen.SLOT_SPRITE, this.this$2.getContentX(), this.this$2.getContentY(), 18, 18);
/*     */       super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/*     */     }
/*     */     
/*     */     protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
/*     */       super.extractTooltip(graphics, this.this$2.getContentX() + 18, this.this$2.getContentY() + 18);
/*     */     }
/*     */   }
/*     */   
/*     */   private class HeaderEntry extends ItemStatisticsList.Entry {
/*     */     private static final Identifier BLOCK_MINED_SPRITE = Identifier.withDefaultNamespace("statistics/block_mined");
/*     */     
/*     */     private static final Identifier ITEM_BROKEN_SPRITE = Identifier.withDefaultNamespace("statistics/item_broken");
/*     */     
/*     */     private static final Identifier ITEM_CRAFTED_SPRITE = Identifier.withDefaultNamespace("statistics/item_crafted");
/*     */     
/*     */     private static final Identifier ITEM_USED_SPRITE = Identifier.withDefaultNamespace("statistics/item_used");
/*     */     
/*     */     private static final Identifier ITEM_PICKED_UP_SPRITE = Identifier.withDefaultNamespace("statistics/item_picked_up");
/*     */     
/*     */     private static final Identifier ITEM_DROPPED_SPRITE = Identifier.withDefaultNamespace("statistics/item_dropped");
/*     */     
/*     */     private final StatSortButton blockMined;
/*     */     
/*     */     private final StatSortButton itemBroken;
/*     */     
/*     */     private final StatSortButton itemCrafted;
/*     */     
/*     */     private final StatSortButton itemUsed;
/*     */     
/*     */     private final StatSortButton itemPickedUp;
/*     */     
/*     */     private final StatSortButton itemDropped;
/*     */     
/*     */     private final List<AbstractWidget> children = new ArrayList<>();
/*     */     
/*     */     private HeaderEntry() {
/*     */       this.blockMined = new StatSortButton(this, 0, BLOCK_MINED_SPRITE);
/*     */       this.itemBroken = new StatSortButton(this, 1, ITEM_BROKEN_SPRITE);
/*     */       this.itemCrafted = new StatSortButton(this, 2, ITEM_CRAFTED_SPRITE);
/*     */       this.itemUsed = new StatSortButton(this, 3, ITEM_USED_SPRITE);
/*     */       this.itemPickedUp = new StatSortButton(this, 4, ITEM_PICKED_UP_SPRITE);
/*     */       this.itemDropped = new StatSortButton(this, 5, ITEM_DROPPED_SPRITE);
/*     */       this.children.addAll((Collection)List.of(this.blockMined, this.itemBroken, this.itemCrafted, this.itemUsed, this.itemPickedUp, this.itemDropped));
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       this.blockMined.setPosition(getContentX() + this.this$1.getColumnX(0) - 18, getContentY() + 1);
/*     */       this.blockMined.extractRenderState(graphics, mouseX, mouseY, a);
/*     */       this.itemBroken.setPosition(getContentX() + this.this$1.getColumnX(1) - 18, getContentY() + 1);
/*     */       this.itemBroken.extractRenderState(graphics, mouseX, mouseY, a);
/*     */       this.itemCrafted.setPosition(getContentX() + this.this$1.getColumnX(2) - 18, getContentY() + 1);
/*     */       this.itemCrafted.extractRenderState(graphics, mouseX, mouseY, a);
/*     */       this.itemUsed.setPosition(getContentX() + this.this$1.getColumnX(3) - 18, getContentY() + 1);
/*     */       this.itemUsed.extractRenderState(graphics, mouseX, mouseY, a);
/*     */       this.itemPickedUp.setPosition(getContentX() + this.this$1.getColumnX(4) - 18, getContentY() + 1);
/*     */       this.itemPickedUp.extractRenderState(graphics, mouseX, mouseY, a);
/*     */       this.itemDropped.setPosition(getContentX() + this.this$1.getColumnX(5) - 18, getContentY() + 1);
/*     */       this.itemDropped.extractRenderState(graphics, mouseX, mouseY, a);
/*     */       if (this.this$1.sortColumn != null) {
/*     */         int offset = this.this$1.getColumnX(this.this$1.getColumnIndex(this.this$1.sortColumn)) - 36;
/*     */         Identifier sprite = (this.this$1.sortOrder == 1) ? StatsScreen.SORT_UP_SPRITE : StatsScreen.SORT_DOWN_SPRITE;
/*     */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getContentX() + offset, getContentY() + 1, 18, 18);
/*     */       } 
/*     */     }
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/*     */       return (List)this.children;
/*     */     }
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/*     */       return (List)this.children;
/*     */     }
/*     */     
/*     */     private class StatSortButton extends ImageButton {
/*     */       private final Identifier sprite;
/*     */       
/*     */       private StatSortButton(StatsScreen.ItemStatisticsList.HeaderEntry this$0, int column, Identifier sprite) {
/*     */         super(18, 18, new WidgetSprites(StatsScreen.HEADER_SPRITE, StatsScreen.SLOT_SPRITE), button -> this$2.this$1.sortByColumn(this$2.this$1.getColumn(column)), this$0.this$1.getColumn(column).getDisplayName());
/*     */         this.sprite = sprite;
/*     */         setTooltip(Tooltip.create(getMessage()));
/*     */       }
/*     */       
/*     */       public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*     */         Identifier background = this.sprites.get(isActive(), isHoveredOrFocused());
/*     */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, getX(), getY(), this.width, this.height);
/* 563 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX(), getY(), this.width, this.height);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class StatSortButton extends ImageButton {
/*     */     private final Identifier sprite;
/*     */     
/*     */     private StatSortButton(StatsScreen this$0, int column, Identifier sprite) {
/*     */       super(18, 18, new WidgetSprites(StatsScreen.HEADER_SPRITE, StatsScreen.SLOT_SPRITE), button -> this$2.this$1.sortByColumn(this$2.this$1.getColumn(column)), ((StatsScreen.ItemStatisticsList.HeaderEntry)this$0).this$1.getColumn(column).getDisplayName());
/*     */       this.sprite = sprite;
/*     */       setTooltip(Tooltip.create(getMessage()));
/*     */     }
/*     */     
/*     */     public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*     */       Identifier background = this.sprites.get(isActive(), isHoveredOrFocused());
/*     */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, getX(), getY(), this.width, this.height);
/* 563 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX(), getY(), this.width, this.height);
/*     */     }
/*     */   }
/*     */   
/*     */   private class MobsStatisticsList extends ObjectSelectionList<MobsStatisticsList.MobRow> {
/*     */     public MobsStatisticsList(Minecraft minecraft) {
/* 571 */       super(minecraft, StatsScreen.this.width, StatsScreen.this.layout.getContentHeight(), 33, 9 * 4);
/* 573 */       for (EntityType<?> type : (Iterable<EntityType<?>>)BuiltInRegistries.ENTITY_TYPE) {
/* 574 */         if (StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED.get(type)) > 0 || StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED_BY.get(type)) > 0)
/* 575 */           addEntry((AbstractSelectionList.Entry)new MobRow(type)); 
/*     */       } 
/*     */     }
/*     */     
/*     */     public int getRowWidth() {
/* 582 */       return 280;
/*     */     }
/*     */     
/*     */     protected void extractListBackground(GuiGraphicsExtractor graphics) {}
/*     */     
/*     */     protected void extractListSeparators(GuiGraphicsExtractor graphics) {}
/*     */     
/*     */     private class MobRow extends ObjectSelectionList.Entry<MobRow> {
/*     */       private final Component mobName;
/*     */       
/*     */       private final Component kills;
/*     */       
/*     */       private final Component killedBy;
/*     */       
/*     */       private final boolean hasKills;
/*     */       
/*     */       private final boolean wasKilledBy;
/*     */       
/*     */       public MobRow(EntityType<?> type) {
/* 602 */         this.mobName = type.getDescription();
/* 604 */         int kills = StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED.get(type));
/* 605 */         if (kills == 0) {
/* 606 */           this.kills = (Component)Component.translatable("stat_type.minecraft.killed.none", new Object[] { this.mobName });
/* 607 */           this.hasKills = false;
/*     */         } else {
/* 609 */           this.kills = (Component)Component.translatable("stat_type.minecraft.killed", new Object[] { Integer.valueOf(kills), this.mobName });
/* 610 */           this.hasKills = true;
/*     */         } 
/* 613 */         int killedBy = StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED_BY.get(type));
/* 614 */         if (killedBy == 0) {
/* 615 */           this.killedBy = (Component)Component.translatable("stat_type.minecraft.killed_by.none", new Object[] { this.mobName });
/* 616 */           this.wasKilledBy = false;
/*     */         } else {
/* 618 */           this.killedBy = (Component)Component.translatable("stat_type.minecraft.killed_by", new Object[] { this.mobName, Integer.valueOf(killedBy) });
/* 619 */           this.wasKilledBy = true;
/*     */         } 
/*     */       }
/*     */       
/*     */       public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 625 */         graphics.text(StatsScreen.this.font, this.mobName, getContentX() + 2, getContentY() + 1, -1);
/* 626 */         Objects.requireNonNull(StatsScreen.this.font);
/* 626 */         graphics.text(StatsScreen.this.font, this.kills, getContentX() + 2 + 10, getContentY() + 1 + 9, this.hasKills ? -4539718 : -8355712);
/* 627 */         Objects.requireNonNull(StatsScreen.this.font);
/* 627 */         graphics.text(StatsScreen.this.font, this.killedBy, getContentX() + 2 + 10, getContentY() + 1 + 9 * 2, this.wasKilledBy ? -4539718 : -8355712);
/*     */       }
/*     */       
/*     */       public Component getNarration() {
/* 632 */         return (Component)Component.translatable("narrator.select", new Object[] { CommonComponents.joinForNarration(new Component[] { this.kills, this.killedBy }) });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class MobRow extends ObjectSelectionList.Entry<MobsStatisticsList.MobRow> {
/*     */     private final Component mobName;
/*     */     
/*     */     private final Component kills;
/*     */     
/*     */     private final Component killedBy;
/*     */     
/*     */     private final boolean hasKills;
/*     */     
/*     */     private final boolean wasKilledBy;
/*     */     
/*     */     public MobRow(EntityType<?> type) {
/*     */       this.mobName = type.getDescription();
/*     */       int kills = StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED.get(type));
/*     */       if (kills == 0) {
/*     */         this.kills = (Component)Component.translatable("stat_type.minecraft.killed.none", new Object[] { this.mobName });
/*     */         this.hasKills = false;
/*     */       } else {
/*     */         this.kills = (Component)Component.translatable("stat_type.minecraft.killed", new Object[] { Integer.valueOf(kills), this.mobName });
/*     */         this.hasKills = true;
/*     */       } 
/*     */       int killedBy = StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED_BY.get(type));
/*     */       if (killedBy == 0) {
/*     */         this.killedBy = (Component)Component.translatable("stat_type.minecraft.killed_by.none", new Object[] { this.mobName });
/*     */         this.wasKilledBy = false;
/*     */       } else {
/*     */         this.killedBy = (Component)Component.translatable("stat_type.minecraft.killed_by", new Object[] { this.mobName, Integer.valueOf(killedBy) });
/*     */         this.wasKilledBy = true;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       graphics.text(StatsScreen.this.font, this.mobName, getContentX() + 2, getContentY() + 1, -1);
/*     */       Objects.requireNonNull(StatsScreen.this.font);
/*     */       graphics.text(StatsScreen.this.font, this.kills, getContentX() + 2 + 10, getContentY() + 1 + 9, this.hasKills ? -4539718 : -8355712);
/*     */       Objects.requireNonNull(StatsScreen.this.font);
/*     */       graphics.text(StatsScreen.this.font, this.killedBy, getContentX() + 2 + 10, getContentY() + 1 + 9 * 2, this.wasKilledBy ? -4539718 : -8355712);
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 632 */       return (Component)Component.translatable("narrator.select", new Object[] { CommonComponents.joinForNarration(new Component[] { this.kills, this.killedBy }) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\achievement\StatsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */