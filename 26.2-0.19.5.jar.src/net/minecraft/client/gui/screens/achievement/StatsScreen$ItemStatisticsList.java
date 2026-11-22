/*     */ package net.minecraft.client.gui.screens.achievement;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.ItemDisplayWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.stats.Stat;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ class ItemStatisticsList extends ContainerObjectSelectionList<StatsScreen.ItemStatisticsList.Entry> {
/*     */   private static final int SLOT_BG_SIZE = 18;
/*     */   
/*     */   private static final int SLOT_STAT_HEIGHT = 22;
/*     */   
/*     */   private static final int SLOT_BG_Y = 1;
/*     */   
/*     */   private static final int SORT_NONE = 0;
/*     */   
/*     */   private static final int SORT_DOWN = -1;
/*     */   
/*     */   private static final int SORT_UP = 1;
/*     */   
/*     */   protected final List<StatType<Block>> blockColumns;
/*     */   
/*     */   protected final List<StatType<Item>> itemColumns;
/*     */   
/* 272 */   protected final Comparator<ItemRow> itemStatSorter = new ItemRowComparator();
/*     */   
/*     */   protected StatType<?> sortColumn;
/*     */   
/*     */   protected int sortOrder;
/*     */   
/*     */   public ItemStatisticsList(Minecraft minecraft) {
/* 278 */     super(minecraft, paramStatsScreen.width, paramStatsScreen.layout.getContentHeight(), 33, 22);
/* 280 */     this.blockColumns = Lists.newArrayList();
/* 281 */     this.blockColumns.add(Stats.BLOCK_MINED);
/* 282 */     this.itemColumns = Lists.newArrayList((Object[])new StatType[] { Stats.ITEM_BROKEN, Stats.ITEM_CRAFTED, Stats.ITEM_USED, Stats.ITEM_PICKED_UP, Stats.ITEM_DROPPED });
/* 284 */     Set<Item> items = Sets.newIdentityHashSet();
/* 285 */     for (Item item : BuiltInRegistries.ITEM) {
/* 286 */       boolean addToList = false;
/* 288 */       for (StatType<Item> type : this.itemColumns) {
/* 289 */         if (type.contains(item) && paramStatsScreen.stats.getValue(type.get(item)) > 0)
/* 290 */           addToList = true; 
/*     */       } 
/* 294 */       if (addToList)
/* 295 */         items.add(item); 
/*     */     } 
/* 299 */     for (Block block : BuiltInRegistries.BLOCK) {
/* 300 */       boolean addToList = false;
/* 302 */       for (StatType<Block> type : this.blockColumns) {
/* 303 */         if (type.contains(block) && paramStatsScreen.stats.getValue(type.get(block)) > 0)
/* 304 */           addToList = true; 
/*     */       } 
/* 308 */       if (addToList)
/* 309 */         items.add(block.asItem()); 
/*     */     } 
/* 313 */     items.remove(Items.AIR);
/* 314 */     if (!items.isEmpty()) {
/* 315 */       addEntry((AbstractSelectionList.Entry)new HeaderEntry());
/* 316 */       for (Item item : items)
/* 317 */         addEntry((AbstractSelectionList.Entry)new ItemRow(item)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void extractListBackground(GuiGraphicsExtractor graphics) {}
/*     */   
/*     */   private int getColumnX(int col) {
/* 327 */     return 75 + 40 * col;
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/* 332 */     return 280;
/*     */   }
/*     */   
/*     */   private StatType<?> getColumn(int i) {
/* 336 */     return (i < this.blockColumns.size()) ? this.blockColumns.get(i) : this.itemColumns.get(i - this.blockColumns.size());
/*     */   }
/*     */   
/*     */   private int getColumnIndex(StatType<?> column) {
/* 340 */     int i = this.blockColumns.indexOf(column);
/* 341 */     if (i >= 0)
/* 342 */       return i; 
/* 344 */     int j = this.itemColumns.indexOf(column);
/* 345 */     if (j >= 0)
/* 346 */       return j + this.blockColumns.size(); 
/* 348 */     return -1;
/*     */   }
/*     */   
/*     */   protected void sortByColumn(StatType<?> column) {
/* 352 */     if (column != this.sortColumn) {
/* 353 */       this.sortColumn = column;
/* 354 */       this.sortOrder = -1;
/* 355 */     } else if (this.sortOrder == -1) {
/* 356 */       this.sortOrder = 1;
/*     */     } else {
/* 358 */       this.sortColumn = null;
/* 359 */       this.sortOrder = 0;
/*     */     } 
/* 362 */     sortItems(this.itemStatSorter);
/*     */   }
/*     */   
/*     */   protected void sortItems(Comparator<ItemRow> comparator) {
/* 366 */     List<ItemRow> itemRows = getItemRows();
/* 367 */     itemRows.sort(comparator);
/* 368 */     clearEntriesExcept((AbstractSelectionList.Entry)children().getFirst());
/* 369 */     for (ItemRow newChild : itemRows)
/* 370 */       addEntry((AbstractSelectionList.Entry)newChild); 
/*     */   }
/*     */   
/*     */   private List<ItemRow> getItemRows() {
/* 375 */     List<ItemRow> itemRows = new ArrayList<>();
/* 376 */     children().forEach(entry -> {
/*     */           if (entry instanceof ItemRow) {
/*     */             ItemRow itemRow = (ItemRow)entry;
/*     */             itemRows.add(itemRow);
/*     */           } 
/*     */         });
/* 381 */     return itemRows;
/*     */   }
/*     */   
/*     */   private class ItemRowComparator implements Comparator<ItemRow> {
/*     */     public int compare(StatsScreen.ItemStatisticsList.ItemRow one, StatsScreen.ItemStatisticsList.ItemRow two) {
/*     */       int key1, key2;
/* 388 */       Item item1 = one.getItem();
/* 389 */       Item item2 = two.getItem();
/* 393 */       if (StatsScreen.ItemStatisticsList.this.sortColumn == null) {
/* 394 */         key1 = 0;
/* 395 */         key2 = 0;
/* 396 */       } else if (StatsScreen.ItemStatisticsList.this.blockColumns.contains(StatsScreen.ItemStatisticsList.this.sortColumn)) {
/* 397 */         StatType<Block> type = (StatType)StatsScreen.ItemStatisticsList.this.sortColumn;
/* 398 */         BlockItem blockItem = (BlockItem)item1;
/* 398 */         key1 = (item1 instanceof BlockItem) ? this.this$1.this$0.stats.getValue(type, blockItem.getBlock()) : -1;
/* 399 */         blockItem = (BlockItem)item2;
/* 399 */         key2 = (item2 instanceof BlockItem) ? this.this$1.this$0.stats.getValue(type, blockItem.getBlock()) : -1;
/*     */       } else {
/* 401 */         StatType<Item> type = (StatType)StatsScreen.ItemStatisticsList.this.sortColumn;
/* 402 */         key1 = this.this$1.this$0.stats.getValue(type, item1);
/* 403 */         key2 = this.this$1.this$0.stats.getValue(type, item2);
/*     */       } 
/* 406 */       if (key1 == key2)
/* 407 */         return StatsScreen.ItemStatisticsList.this.sortOrder * Integer.compare(Item.getId(item1), Item.getId(item2)); 
/* 410 */       return StatsScreen.ItemStatisticsList.this.sortOrder * Integer.compare(key1, key2);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void extractListSeparators(GuiGraphicsExtractor graphics) {}
/*     */   
/*     */   private static abstract class Entry extends ContainerObjectSelectionList.Entry<Entry> {}
/*     */   
/*     */   private class ItemRow extends Entry {
/*     */     private final Item item;
/*     */     
/*     */     private final ItemRowWidget itemRowWidget;
/*     */     
/*     */     private ItemRow(Item item) {
/* 426 */       this.item = item;
/* 427 */       this.itemRowWidget = new ItemRowWidget(item.getDefaultInstance());
/*     */     }
/*     */     
/*     */     protected Item getItem() {
/* 431 */       return this.item;
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 436 */       this.itemRowWidget.setPosition(getContentX(), getContentY());
/* 437 */       this.itemRowWidget.extractRenderState(graphics, mouseX, mouseY, a);
/* 439 */       StatsScreen.ItemStatisticsList itemStatsList = StatsScreen.ItemStatisticsList.this;
/* 440 */       int index = itemStatsList.children().indexOf(this);
/*     */       int col;
/* 441 */       for (col = 0; col < itemStatsList.blockColumns.size(); col++) {
/*     */         Stat<Block> stat;
/* 443 */         Item item = this.item;
/* 443 */         if (item instanceof BlockItem) {
/* 443 */           BlockItem blockItem = (BlockItem)item;
/* 444 */           stat = ((StatType)itemStatsList.blockColumns.get(col)).get(blockItem.getBlock());
/*     */         } else {
/* 446 */           stat = null;
/*     */         } 
/* 448 */         Objects.requireNonNull(StatsScreen.access$400(this.this$1.this$0));
/* 448 */         extractStat(graphics, stat, getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(col), getContentYMiddle() - 9 / 2, (index % 2 == 0));
/*     */       } 
/* 450 */       for (col = 0; col < itemStatsList.itemColumns.size(); 
/* 451 */         extractStat(graphics, ((StatType)itemStatsList.itemColumns.get(col)).get(this.item), getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(col + itemStatsList.blockColumns.size()), 
/* 452 */           getContentYMiddle() - 9 / 2, (index % 2 == 0)), col++)
/* 452 */         Objects.requireNonNull(StatsScreen.access$500(this.this$1.this$0)); 
/*     */     }
/*     */     
/*     */     protected void extractStat(GuiGraphicsExtractor graphics, Stat<?> stat, int x, int y, boolean shaded) {
/* 457 */       Component msg = (stat == null) ? StatsScreen.NO_VALUE_DISPLAY : (Component)Component.literal(stat.format(this.this$1.this$0.stats.getValue(stat)));
/* 458 */       graphics.text(StatsScreen.access$600(this.this$1.this$0), msg, x - StatsScreen.access$700(this.this$1.this$0).width((FormattedText)msg), y, shaded ? -1 : -4539718);
/*     */     }
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 463 */       return (List)List.of(this.itemRowWidget);
/*     */     }
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 468 */       return (List)List.of(this.itemRowWidget);
/*     */     }
/*     */     
/*     */     private class ItemRowWidget extends ItemDisplayWidget {
/*     */       private ItemRowWidget(ItemStack itemStack) {
/* 473 */         super(StatsScreen.ItemStatisticsList.this.minecraft, 1, 1, 18, 18, itemStack.getHoverName(), itemStack, false, true);
/*     */       }
/*     */       
/*     */       protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 478 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, StatsScreen.SLOT_SPRITE, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX(), StatsScreen.ItemStatisticsList.ItemRow.this.getContentY(), 18, 18);
/* 479 */         super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/*     */       }
/*     */       
/*     */       protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
/* 484 */         super.extractTooltip(graphics, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX() + 18, StatsScreen.ItemStatisticsList.ItemRow.this.getContentY() + 18);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class HeaderEntry extends Entry {
/* 490 */     private static final Identifier BLOCK_MINED_SPRITE = Identifier.withDefaultNamespace("statistics/block_mined");
/*     */     
/* 491 */     private static final Identifier ITEM_BROKEN_SPRITE = Identifier.withDefaultNamespace("statistics/item_broken");
/*     */     
/* 492 */     private static final Identifier ITEM_CRAFTED_SPRITE = Identifier.withDefaultNamespace("statistics/item_crafted");
/*     */     
/* 493 */     private static final Identifier ITEM_USED_SPRITE = Identifier.withDefaultNamespace("statistics/item_used");
/*     */     
/* 494 */     private static final Identifier ITEM_PICKED_UP_SPRITE = Identifier.withDefaultNamespace("statistics/item_picked_up");
/*     */     
/* 495 */     private static final Identifier ITEM_DROPPED_SPRITE = Identifier.withDefaultNamespace("statistics/item_dropped");
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
/* 504 */     private final List<AbstractWidget> children = new ArrayList<>();
/*     */     
/*     */     private HeaderEntry() {
/* 507 */       this.blockMined = new StatSortButton(this, 0, BLOCK_MINED_SPRITE);
/* 508 */       this.itemBroken = new StatSortButton(this, 1, ITEM_BROKEN_SPRITE);
/* 509 */       this.itemCrafted = new StatSortButton(this, 2, ITEM_CRAFTED_SPRITE);
/* 510 */       this.itemUsed = new StatSortButton(this, 3, ITEM_USED_SPRITE);
/* 511 */       this.itemPickedUp = new StatSortButton(this, 4, ITEM_PICKED_UP_SPRITE);
/* 512 */       this.itemDropped = new StatSortButton(this, 5, ITEM_DROPPED_SPRITE);
/* 513 */       this.children.addAll((Collection)List.of(this.blockMined, this.itemBroken, this.itemCrafted, this.itemUsed, this.itemPickedUp, this.itemDropped));
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 518 */       this.blockMined.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(0) - 18, getContentY() + 1);
/* 519 */       this.blockMined.extractRenderState(graphics, mouseX, mouseY, a);
/* 520 */       this.itemBroken.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(1) - 18, getContentY() + 1);
/* 521 */       this.itemBroken.extractRenderState(graphics, mouseX, mouseY, a);
/* 522 */       this.itemCrafted.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(2) - 18, getContentY() + 1);
/* 523 */       this.itemCrafted.extractRenderState(graphics, mouseX, mouseY, a);
/* 524 */       this.itemUsed.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(3) - 18, getContentY() + 1);
/* 525 */       this.itemUsed.extractRenderState(graphics, mouseX, mouseY, a);
/* 526 */       this.itemPickedUp.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(4) - 18, getContentY() + 1);
/* 527 */       this.itemPickedUp.extractRenderState(graphics, mouseX, mouseY, a);
/* 528 */       this.itemDropped.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(5) - 18, getContentY() + 1);
/* 529 */       this.itemDropped.extractRenderState(graphics, mouseX, mouseY, a);
/* 531 */       if (StatsScreen.ItemStatisticsList.this.sortColumn != null) {
/* 532 */         int offset = StatsScreen.ItemStatisticsList.this.getColumnX(StatsScreen.ItemStatisticsList.this.getColumnIndex(StatsScreen.ItemStatisticsList.this.sortColumn)) - 36;
/* 534 */         Identifier sprite = (StatsScreen.ItemStatisticsList.this.sortOrder == 1) ? StatsScreen.SORT_UP_SPRITE : StatsScreen.SORT_DOWN_SPRITE;
/* 535 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getContentX() + offset, getContentY() + 1, 18, 18);
/*     */       } 
/*     */     }
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 541 */       return (List)this.children;
/*     */     }
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 546 */       return (List)this.children;
/*     */     }
/*     */     
/*     */     private class StatSortButton extends ImageButton {
/*     */       private final Identifier sprite;
/*     */       
/*     */       private StatSortButton(StatsScreen.ItemStatisticsList.HeaderEntry this$0, int column, Identifier sprite) {
/* 553 */         super(18, 18, new WidgetSprites(StatsScreen.HEADER_SPRITE, StatsScreen.SLOT_SPRITE), button -> this$2.this$1.sortByColumn(this$2.this$1.getColumn(column)), this$0.this$1
/* 554 */             .getColumn(column).getDisplayName());
/* 555 */         this.sprite = sprite;
/* 556 */         setTooltip(Tooltip.create(getMessage()));
/*     */       }
/*     */       
/*     */       public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 561 */         Identifier background = this.sprites.get(isActive(), isHoveredOrFocused());
/* 562 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, getX(), getY(), this.width, this.height);
/* 563 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX(), getY(), this.width, this.height);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\achievement\StatsScreen$ItemStatisticsList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */