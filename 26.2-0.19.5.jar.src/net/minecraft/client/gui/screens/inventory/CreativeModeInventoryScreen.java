/*      */ package net.minecraft.client.gui.screens.inventory;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.blaze3d.platform.InputConstants;
/*      */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.function.Predicate;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.client.HotbarManager;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*      */ import net.minecraft.client.gui.components.EditBox;
/*      */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*      */ import net.minecraft.client.input.CharacterEvent;
/*      */ import net.minecraft.client.input.KeyEvent;
/*      */ import net.minecraft.client.input.MouseButtonEvent;
/*      */ import net.minecraft.client.input.PreeditEvent;
/*      */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*      */ import net.minecraft.client.multiplayer.SessionSearchTrees;
/*      */ import net.minecraft.client.player.LocalPlayer;
/*      */ import net.minecraft.client.player.inventory.Hotbar;
/*      */ import net.minecraft.client.renderer.RenderPipelines;
/*      */ import net.minecraft.client.searchtree.SearchTree;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.NonNullList;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.SimpleContainer;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.player.Inventory;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*      */ import net.minecraft.world.inventory.ContainerInput;
/*      */ import net.minecraft.world.inventory.InventoryMenu;
/*      */ import net.minecraft.world.inventory.Slot;
/*      */ import net.minecraft.world.item.CreativeModeTab;
/*      */ import net.minecraft.world.item.CreativeModeTabs;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.TooltipFlag;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ 
/*      */ public class CreativeModeInventoryScreen extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
/*   61 */   private static final Identifier SCROLLER_SPRITE = Identifier.withDefaultNamespace("container/creative_inventory/scroller");
/*      */   
/*   62 */   private static final Identifier SCROLLER_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/creative_inventory/scroller_disabled");
/*      */   
/*   64 */   private static final Identifier[] UNSELECTED_TOP_TABS = new Identifier[] { Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_1"), 
/*   66 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_2"), 
/*   67 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_3"), 
/*   68 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_4"), 
/*   69 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_5"), 
/*   70 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_6"), 
/*   71 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_7") };
/*      */   
/*   73 */   private static final Identifier[] SELECTED_TOP_TABS = new Identifier[] { Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_1"), 
/*   75 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_2"), 
/*   76 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_3"), 
/*   77 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_4"), 
/*   78 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_5"), 
/*   79 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_6"), 
/*   80 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_7") };
/*      */   
/*   82 */   private static final Identifier[] UNSELECTED_BOTTOM_TABS = new Identifier[] { Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_1"), 
/*   84 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_2"), 
/*   85 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_3"), 
/*   86 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_4"), 
/*   87 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_5"), 
/*   88 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_6"), 
/*   89 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_unselected_7") };
/*      */   
/*   91 */   private static final Identifier[] SELECTED_BOTTOM_TABS = new Identifier[] { Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_1"), 
/*   93 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_2"), 
/*   94 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_3"), 
/*   95 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_4"), 
/*   96 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_5"), 
/*   97 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_6"), 
/*   98 */       Identifier.withDefaultNamespace("container/creative_inventory/tab_bottom_selected_7") };
/*      */   
/*      */   private static final int NUM_ROWS = 5;
/*      */   
/*      */   private static final int NUM_COLS = 9;
/*      */   
/*      */   private static final int TAB_WIDTH = 26;
/*      */   
/*      */   private static final int TAB_HEIGHT = 32;
/*      */   
/*      */   private static final int SCROLLER_WIDTH = 12;
/*      */   
/*      */   private static final int SCROLLER_HEIGHT = 15;
/*      */   
/*  107 */   private static final SimpleContainer CONTAINER = new SimpleContainer(45);
/*      */   
/*  108 */   private static final Component TRASH_SLOT_TOOLTIP = (Component)Component.translatable("inventory.binSlot");
/*      */   
/*  110 */   private static CreativeModeTab selectedTab = CreativeModeTabs.getDefaultTab();
/*      */   
/*      */   private float scrollOffs;
/*      */   
/*      */   private boolean scrolling;
/*      */   
/*      */   private EditBox searchBox;
/*      */   
/*      */   private List<Slot> originalSlots;
/*      */   
/*      */   private Slot destroyItemSlot;
/*      */   
/*      */   private CreativeInventoryListener listener;
/*      */   
/*      */   private boolean ignoreTextInput;
/*      */   
/*      */   private boolean hasClickedOutside;
/*      */   
/*  120 */   private final Set<TagKey<Item>> visibleTags = new HashSet<>();
/*      */   
/*      */   private final boolean displayOperatorCreativeTab;
/*      */   
/*      */   private final EffectsInInventory effects;
/*      */   
/*      */   public static class ItemPickerMenu extends AbstractContainerMenu {
/*  125 */     public final NonNullList<ItemStack> items = NonNullList.create();
/*      */     
/*      */     private final AbstractContainerMenu inventoryMenu;
/*      */     
/*      */     public ItemPickerMenu(Player player) {
/*  130 */       super(null, 0);
/*  131 */       this.inventoryMenu = (AbstractContainerMenu)player.inventoryMenu;
/*  133 */       Inventory inventory = player.getInventory();
/*  134 */       for (int y = 0; y < 5; y++) {
/*  135 */         for (int x = 0; x < 9; x++)
/*  136 */           addSlot(new CreativeModeInventoryScreen.CustomCreativeSlot((Container)CreativeModeInventoryScreen.CONTAINER, y * 9 + x, 9 + x * 18, 18 + y * 18)); 
/*      */       } 
/*  140 */       addInventoryHotbarSlots((Container)inventory, 9, 112);
/*  142 */       scrollTo(0.0F);
/*      */     }
/*      */     
/*      */     public boolean stillValid(Player player) {
/*  147 */       return true;
/*      */     }
/*      */     
/*      */     protected int calculateRowCount() {
/*  151 */       return Mth.positiveCeilDiv(this.items.size(), 9) - 5;
/*      */     }
/*      */     
/*      */     protected int getRowIndexForScroll(float scrollOffs) {
/*  155 */       return Math.max((int)((scrollOffs * calculateRowCount()) + 0.5D), 0);
/*      */     }
/*      */     
/*      */     protected float getScrollForRowIndex(int rowIndex) {
/*  159 */       return Mth.clamp(rowIndex / calculateRowCount(), 0.0F, 1.0F);
/*      */     }
/*      */     
/*      */     protected float subtractInputFromScroll(float scrollOffs, double input) {
/*  163 */       return Mth.clamp(scrollOffs - (float)(input / calculateRowCount()), 0.0F, 1.0F);
/*      */     }
/*      */     
/*      */     public void scrollTo(float scrollOffs) {
/*  167 */       int rowToScrollTo = getRowIndexForScroll(scrollOffs);
/*  168 */       for (int y = 0; y < 5; y++) {
/*  169 */         for (int x = 0; x < 9; x++) {
/*  170 */           int slot = x + (y + rowToScrollTo) * 9;
/*  171 */           if (slot >= 0 && slot < this.items.size()) {
/*  172 */             CreativeModeInventoryScreen.CONTAINER.setItem(x + y * 9, (ItemStack)this.items.get(slot));
/*      */           } else {
/*  174 */             CreativeModeInventoryScreen.CONTAINER.setItem(x + y * 9, ItemStack.EMPTY);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean canScroll() {
/*  181 */       return (this.items.size() > 45);
/*      */     }
/*      */     
/*      */     public ItemStack quickMoveStack(Player player, int slotIndex) {
/*  186 */       if (slotIndex >= this.slots.size() - 9 && slotIndex < this.slots.size()) {
/*  187 */         Slot slot = (Slot)this.slots.get(slotIndex);
/*  189 */         if (slot != null && slot.hasItem())
/*  190 */           slot.setByPlayer(ItemStack.EMPTY); 
/*      */       } 
/*  194 */       return ItemStack.EMPTY;
/*      */     }
/*      */     
/*      */     public boolean canTakeItemForPickAll(ItemStack carried, Slot target) {
/*  199 */       return (target.container != CreativeModeInventoryScreen.CONTAINER);
/*      */     }
/*      */     
/*      */     public boolean canDragTo(Slot slot) {
/*  204 */       return (slot.container != CreativeModeInventoryScreen.CONTAINER);
/*      */     }
/*      */     
/*      */     public ItemStack getCarried() {
/*  209 */       return this.inventoryMenu.getCarried();
/*      */     }
/*      */     
/*      */     public void setCarried(ItemStack carried) {
/*  214 */       this.inventoryMenu.setCarried(carried);
/*      */     }
/*      */   }
/*      */   
/*      */   public CreativeModeInventoryScreen(LocalPlayer player, FeatureFlagSet enabledFeatures, boolean displayOperatorCreativeTab) {
/*  219 */     super(new ItemPickerMenu((Player)player), player.getInventory(), CommonComponents.EMPTY, 195, 136);
/*  220 */     player.containerMenu = this.menu;
/*  221 */     this.displayOperatorCreativeTab = displayOperatorCreativeTab;
/*  222 */     tryRebuildTabContents(player.connection.searchTrees(), enabledFeatures, hasPermissions((Player)player), (HolderLookup.Provider)player.level().registryAccess());
/*  223 */     this.effects = new EffectsInInventory(this);
/*      */   }
/*      */   
/*      */   private boolean hasPermissions(Player player) {
/*  227 */     return (player.canUseGameMasterBlocks() && this.displayOperatorCreativeTab);
/*      */   }
/*      */   
/*      */   private void tryRefreshInvalidatedTabs(FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup.Provider holders) {
/*  231 */     ClientPacketListener connection = this.minecraft.getConnection();
/*  232 */     if (tryRebuildTabContents((connection != null) ? connection.searchTrees() : null, enabledFeatures, hasPermissions, holders))
/*  233 */       for (CreativeModeTab tab : CreativeModeTabs.allTabs()) {
/*  234 */         Collection<ItemStack> displayList = tab.getDisplayItems();
/*  235 */         if (tab == selectedTab) {
/*  236 */           if (tab.getType() == CreativeModeTab.Type.CATEGORY && displayList.isEmpty()) {
/*  237 */             selectTab(CreativeModeTabs.getDefaultTab());
/*      */             continue;
/*      */           } 
/*  239 */           refreshCurrentTabContents(displayList);
/*      */         } 
/*      */       }  
/*      */   }
/*      */   
/*      */   private boolean tryRebuildTabContents(SessionSearchTrees searchTrees, FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup.Provider holders) {
/*  247 */     if (!CreativeModeTabs.tryRebuildTabContents(enabledFeatures, hasPermissions, holders))
/*  248 */       return false; 
/*  251 */     if (searchTrees != null) {
/*  252 */       List<ItemStack> creativeSearchItems = List.copyOf(CreativeModeTabs.searchTab().getDisplayItems());
/*  253 */       searchTrees.updateCreativeTooltips(holders, creativeSearchItems);
/*  254 */       searchTrees.updateCreativeTags(creativeSearchItems);
/*      */     } 
/*  257 */     return true;
/*      */   }
/*      */   
/*      */   private void refreshCurrentTabContents(Collection<ItemStack> displayList) {
/*  261 */     int oldRowIndex = this.menu.getRowIndexForScroll(this.scrollOffs);
/*  262 */     this.menu.items.clear();
/*  263 */     if (selectedTab.getType() == CreativeModeTab.Type.SEARCH) {
/*  264 */       refreshSearchResults();
/*      */     } else {
/*  266 */       this.menu.items.addAll(displayList);
/*      */     } 
/*  271 */     this.scrollOffs = this.menu.getScrollForRowIndex(oldRowIndex);
/*  272 */     this.menu.scrollTo(this.scrollOffs);
/*      */   }
/*      */   
/*      */   public void containerTick() {
/*  277 */     super.containerTick();
/*  279 */     LocalPlayer player = this.minecraft.player;
/*  280 */     if (player != null) {
/*  281 */       tryRefreshInvalidatedTabs(player.connection.enabledFeatures(), hasPermissions((Player)player), (HolderLookup.Provider)player.level().registryAccess());
/*  282 */       if (!player.hasInfiniteMaterials())
/*  283 */         this.minecraft.gui.setScreen(new InventoryScreen((Player)player)); 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void slotClicked(Slot slot, int slotId, int buttonNum, ContainerInput containerInput) {
/*  290 */     if (isCreativeSlot(slot)) {
/*  291 */       this.searchBox.moveCursorToEnd(false);
/*  292 */       this.searchBox.setHighlightPos(0);
/*      */     } 
/*  295 */     boolean quickKey = (containerInput == ContainerInput.QUICK_MOVE);
/*  296 */     containerInput = (slotId == -999 && containerInput == ContainerInput.PICKUP) ? ContainerInput.THROW : containerInput;
/*  297 */     if (containerInput == ContainerInput.THROW && !this.minecraft.player.canDropItems())
/*      */       return; 
/*  300 */     onMouseClickAction(slot, containerInput);
/*  302 */     if (slot != null || selectedTab.getType() == CreativeModeTab.Type.INVENTORY || containerInput == ContainerInput.QUICK_CRAFT) {
/*  303 */       if (slot != null && !slot.mayPickup((Player)this.minecraft.player))
/*      */         return; 
/*  306 */       if (slot == this.destroyItemSlot && quickKey) {
/*  307 */         for (int i = 0; i < this.minecraft.player.inventoryMenu.getItems().size(); i++) {
/*  308 */           this.minecraft.player.inventoryMenu.getSlot(i).set(ItemStack.EMPTY);
/*  309 */           this.minecraft.gameMode.handleCreativeModeItemAdd(ItemStack.EMPTY, i);
/*      */         } 
/*  311 */       } else if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
/*  313 */         if (slot == this.destroyItemSlot) {
/*  314 */           this.menu.setCarried(ItemStack.EMPTY);
/*  315 */         } else if (containerInput == ContainerInput.THROW && slot != null && slot.hasItem()) {
/*  316 */           ItemStack toDrop = slot.remove((buttonNum == 0) ? 1 : slot.getItem().getMaxStackSize());
/*  317 */           ItemStack afterDrop = slot.getItem();
/*  318 */           this.minecraft.player.drop(toDrop, true);
/*  319 */           this.minecraft.gameMode.handleCreativeModeItemDrop(toDrop);
/*  321 */           this.minecraft.gameMode.handleCreativeModeItemAdd(afterDrop, ((SlotWrapper)slot).target.index);
/*  322 */         } else if (containerInput == ContainerInput.THROW && slotId == -999 && !this.menu.getCarried().isEmpty()) {
/*  323 */           this.minecraft.player.drop(this.menu.getCarried(), true);
/*  324 */           this.minecraft.gameMode.handleCreativeModeItemDrop(this.menu.getCarried());
/*  325 */           this.menu.setCarried(ItemStack.EMPTY);
/*      */         } else {
/*  327 */           this.minecraft.player.inventoryMenu.clicked((slot == null) ? slotId : ((SlotWrapper)slot).target.index, buttonNum, containerInput, (Player)this.minecraft.player);
/*  328 */           this.minecraft.player.inventoryMenu.broadcastChanges();
/*      */         } 
/*  331 */       } else if (containerInput != ContainerInput.QUICK_CRAFT && slot.container == CONTAINER) {
/*  333 */         ItemStack carried = this.menu.getCarried();
/*  334 */         ItemStack clicked = slot.getItem();
/*  336 */         if (containerInput == ContainerInput.SWAP) {
/*  337 */           if (!clicked.isEmpty()) {
/*  338 */             this.minecraft.player.getInventory().setItem(buttonNum, clicked.copyWithCount(clicked.getMaxStackSize()));
/*  339 */             this.minecraft.player.inventoryMenu.broadcastChanges();
/*      */           } 
/*      */           return;
/*      */         } 
/*  343 */         if (containerInput == ContainerInput.CLONE) {
/*  344 */           if (this.menu.getCarried().isEmpty() && slot.hasItem()) {
/*  345 */             ItemStack item = slot.getItem();
/*  346 */             this.menu.setCarried(item.copyWithCount(item.getMaxStackSize()));
/*      */           } 
/*      */           return;
/*      */         } 
/*  350 */         if (containerInput == ContainerInput.THROW) {
/*  351 */           if (!clicked.isEmpty()) {
/*  352 */             ItemStack toDrop = clicked.copyWithCount((buttonNum == 0) ? 1 : clicked.getMaxStackSize());
/*  353 */             this.minecraft.player.drop(toDrop, true);
/*  354 */             this.minecraft.gameMode.handleCreativeModeItemDrop(toDrop);
/*      */           } 
/*      */           return;
/*      */         } 
/*  360 */         if (!carried.isEmpty() && !clicked.isEmpty() && ItemStack.isSameItemSameComponents(carried, clicked)) {
/*  362 */           if (buttonNum == 0) {
/*  363 */             if (quickKey) {
/*  364 */               carried.setCount(carried.getMaxStackSize());
/*  366 */             } else if (carried.getCount() < carried.getMaxStackSize()) {
/*  367 */               carried.grow(1);
/*      */             } 
/*      */           } else {
/*  371 */             carried.shrink(1);
/*      */           } 
/*  373 */         } else if (clicked.isEmpty() || !carried.isEmpty()) {
/*  375 */           if (buttonNum == 0) {
/*  376 */             this.menu.setCarried(ItemStack.EMPTY);
/*  377 */           } else if (!this.menu.getCarried().isEmpty()) {
/*  378 */             this.menu.getCarried().shrink(1);
/*      */           } 
/*      */         } else {
/*  382 */           int count = quickKey ? clicked.getMaxStackSize() : clicked.getCount();
/*  383 */           this.menu.setCarried(clicked.copyWithCount(count));
/*      */         } 
/*  385 */       } else if (this.menu != null) {
/*  386 */         ItemStack oldItemStack = (slot == null) ? ItemStack.EMPTY : this.menu.getSlot(slot.index).getItem();
/*  387 */         this.menu.clicked((slot == null) ? slotId : slot.index, buttonNum, containerInput, (Player)this.minecraft.player);
/*  389 */         if (AbstractContainerMenu.getQuickcraftHeader(buttonNum) == 2) {
/*  390 */           for (int i = 0; i < 9; i++)
/*  391 */             this.minecraft.gameMode.handleCreativeModeItemAdd(this.menu.getSlot(45 + i).getItem(), 36 + i); 
/*  393 */         } else if (slot != null && Inventory.isHotbarSlot(slot.getContainerSlot()) && selectedTab.getType() != CreativeModeTab.Type.INVENTORY) {
/*  394 */           if (containerInput == ContainerInput.THROW && !oldItemStack.isEmpty() && !this.menu.getCarried().isEmpty()) {
/*  398 */             int numToDrop = (buttonNum == 0) ? 1 : oldItemStack.getCount();
/*  399 */             ItemStack toDrop = oldItemStack.copyWithCount(numToDrop);
/*  400 */             oldItemStack.shrink(numToDrop);
/*  401 */             this.minecraft.player.drop(toDrop, true);
/*  402 */             this.minecraft.gameMode.handleCreativeModeItemDrop(toDrop);
/*      */           } 
/*  405 */           this.minecraft.player.inventoryMenu.broadcastChanges();
/*      */         } 
/*      */       } 
/*  410 */     } else if (!this.menu.getCarried().isEmpty() && this.hasClickedOutside) {
/*  411 */       if (!this.minecraft.player.canDropItems())
/*      */         return; 
/*  414 */       if (buttonNum == 0) {
/*  415 */         this.minecraft.player.drop(this.menu.getCarried(), true);
/*  416 */         this.minecraft.gameMode.handleCreativeModeItemDrop(this.menu.getCarried());
/*  417 */         this.menu.setCarried(ItemStack.EMPTY);
/*      */       } 
/*  419 */       if (buttonNum == 1) {
/*  420 */         ItemStack removedItem = this.menu.getCarried().split(1);
/*  421 */         this.minecraft.player.drop(removedItem, true);
/*  422 */         this.minecraft.gameMode.handleCreativeModeItemDrop(removedItem);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isCreativeSlot(Slot slot) {
/*  429 */     return (slot != null && slot.container == CONTAINER);
/*      */   }
/*      */   
/*      */   protected void init() {
/*  434 */     if (this.minecraft.player.hasInfiniteMaterials()) {
/*  435 */       super.init();
/*  437 */       Objects.requireNonNull(this.font);
/*  437 */       this.searchBox = new EditBox(this.font, this.leftPos + 82, this.topPos + 6, 80, 9, (Component)Component.translatable("itemGroup.search"));
/*  438 */       this.searchBox.setMaxLength(50);
/*  439 */       this.searchBox.setBordered(false);
/*  440 */       this.searchBox.setVisible(false);
/*  441 */       this.searchBox.setTextColor(-1);
/*  442 */       this.searchBox.setInvertHighlightedTextColor(false);
/*  443 */       addWidget((GuiEventListener)this.searchBox);
/*  445 */       CreativeModeTab tab = selectedTab;
/*  446 */       selectedTab = CreativeModeTabs.getDefaultTab();
/*  447 */       selectTab(tab);
/*  449 */       this.minecraft.player.inventoryMenu.removeSlotListener(this.listener);
/*  450 */       this.listener = new CreativeInventoryListener(this.minecraft);
/*  451 */       this.minecraft.player.inventoryMenu.addSlotListener(this.listener);
/*  452 */       if (!selectedTab.shouldDisplay())
/*  453 */         selectTab(CreativeModeTabs.getDefaultTab()); 
/*      */     } else {
/*  456 */       this.minecraft.gui.setScreen(new InventoryScreen((Player)this.minecraft.player));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void resize(int width, int height) {
/*  462 */     int oldRowIndex = this.menu.getRowIndexForScroll(this.scrollOffs);
/*  464 */     String oldEdit = this.searchBox.getValue();
/*  465 */     init(width, height);
/*  466 */     this.searchBox.setValue(oldEdit);
/*  468 */     if (!this.searchBox.getValue().isEmpty())
/*  469 */       refreshSearchResults(); 
/*  472 */     this.scrollOffs = this.menu.getScrollForRowIndex(oldRowIndex);
/*  473 */     this.menu.scrollTo(this.scrollOffs);
/*      */   }
/*      */   
/*      */   public void removed() {
/*  478 */     super.removed();
/*  480 */     if (this.minecraft.player != null && this.minecraft.player.getInventory() != null)
/*  481 */       this.minecraft.player.inventoryMenu.removeSlotListener(this.listener); 
/*      */   }
/*      */   
/*      */   public boolean charTyped(CharacterEvent event) {
/*  487 */     if (this.ignoreTextInput)
/*  488 */       return false; 
/*  490 */     if (selectedTab.getType() != CreativeModeTab.Type.SEARCH)
/*  491 */       return false; 
/*  493 */     String oldContents = this.searchBox.getValue();
/*  494 */     if (this.searchBox.charTyped(event)) {
/*  495 */       if (!Objects.equals(oldContents, this.searchBox.getValue()))
/*  496 */         refreshSearchResults(); 
/*  498 */       return true;
/*      */     } 
/*  500 */     return false;
/*      */   }
/*      */   
/*      */   public boolean preeditUpdated(PreeditEvent event) {
/*  505 */     if (this.ignoreTextInput)
/*  506 */       return false; 
/*  508 */     if (selectedTab.getType() != CreativeModeTab.Type.SEARCH)
/*  509 */       return false; 
/*  512 */     return this.searchBox.preeditUpdated(event);
/*      */   }
/*      */   
/*      */   public boolean keyPressed(KeyEvent event) {
/*  517 */     this.ignoreTextInput = false;
/*  518 */     if (selectedTab.getType() != CreativeModeTab.Type.SEARCH) {
/*  519 */       if (this.minecraft.options.keyChat.matches(event)) {
/*  520 */         this.ignoreTextInput = true;
/*  521 */         selectTab(CreativeModeTabs.searchTab());
/*  522 */         return true;
/*      */       } 
/*  524 */       return super.keyPressed(event);
/*      */     } 
/*  527 */     boolean doQuickSwap = (!isCreativeSlot(this.hoveredSlot) || this.hoveredSlot.hasItem());
/*  528 */     boolean pressingNumber = InputConstants.getKey(event).getNumericKeyValue().isPresent();
/*  529 */     if (doQuickSwap && pressingNumber && checkHotbarKeyPressed(event)) {
/*  532 */       this.ignoreTextInput = true;
/*  533 */       return true;
/*      */     } 
/*  536 */     String oldContents = this.searchBox.getValue();
/*  537 */     if (this.searchBox.keyPressed(event)) {
/*  538 */       if (!Objects.equals(oldContents, this.searchBox.getValue()))
/*  539 */         refreshSearchResults(); 
/*  542 */       return true;
/*      */     } 
/*  544 */     if (this.searchBox.isFocused() && this.searchBox.isVisible() && !event.isEscape())
/*  546 */       return true; 
/*  548 */     return super.keyPressed(event);
/*      */   }
/*      */   
/*      */   public boolean keyReleased(KeyEvent event) {
/*  553 */     this.ignoreTextInput = false;
/*  554 */     return super.keyReleased(event);
/*      */   }
/*      */   
/*      */   private void refreshSearchResults() {
/*  558 */     this.menu.items.clear();
/*  559 */     this.visibleTags.clear();
/*  561 */     String searchTerm = this.searchBox.getValue();
/*  562 */     if (searchTerm.isEmpty()) {
/*  563 */       this.menu.items.addAll(selectedTab.getDisplayItems());
/*      */     } else {
/*  565 */       ClientPacketListener connection = this.minecraft.getConnection();
/*  566 */       if (connection != null) {
/*      */         SearchTree<ItemStack> tree;
/*  568 */         SessionSearchTrees searchTrees = connection.searchTrees();
/*  569 */         if (searchTerm.startsWith("#")) {
/*  570 */           searchTerm = searchTerm.substring(1);
/*  571 */           tree = searchTrees.creativeTagSearch();
/*  572 */           updateVisibleTags(searchTerm);
/*      */         } else {
/*  574 */           tree = searchTrees.creativeNameSearch();
/*      */         } 
/*  576 */         this.menu.items.addAll(tree.search(searchTerm.toLowerCase(Locale.ROOT)));
/*      */       } 
/*      */     } 
/*  580 */     this.scrollOffs = 0.0F;
/*  581 */     this.menu.scrollTo(0.0F);
/*      */   }
/*      */   
/*      */   private void updateVisibleTags(String searchTerm) {
/*      */     Predicate<Identifier> matcher;
/*  585 */     int colonIndex = searchTerm.indexOf(':');
/*  588 */     if (colonIndex == -1) {
/*  589 */       matcher = (id -> id.getPath().contains(searchTerm));
/*      */     } else {
/*  591 */       String nsMatcher = searchTerm.substring(0, colonIndex).trim();
/*  592 */       String pathMatcher = searchTerm.substring(colonIndex + 1).trim();
/*  593 */       matcher = (id -> (id.getNamespace().contains(nsMatcher) && id.getPath().contains(pathMatcher)));
/*      */     } 
/*  596 */     Objects.requireNonNull(this.visibleTags);
/*  596 */     BuiltInRegistries.ITEM.getTags().map(HolderSet.Named::key).filter(tag -> matcher.test(tag.location())).forEach(this.visibleTags::add);
/*      */   }
/*      */   
/*      */   protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
/*  601 */     if (selectedTab.showTitle())
/*  602 */       graphics.text(this.font, selectedTab.getDisplayName(), 8, 6, -12566464, false); 
/*      */   }
/*      */   
/*      */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*  608 */     if (event.button() == 0) {
/*  609 */       double xm = event.x() - this.leftPos;
/*  610 */       double ym = event.y() - this.topPos;
/*  612 */       for (CreativeModeTab tab : CreativeModeTabs.tabs()) {
/*  613 */         if (checkTabClicked(tab, xm, ym))
/*  614 */           return true; 
/*      */       } 
/*  618 */       if (selectedTab.getType() != CreativeModeTab.Type.INVENTORY && insideScrollbar(event.x(), event.y())) {
/*  619 */         this.scrolling = canScroll();
/*  620 */         return true;
/*      */       } 
/*      */     } 
/*  624 */     return super.mouseClicked(event, doubleClick);
/*      */   }
/*      */   
/*      */   public boolean mouseReleased(MouseButtonEvent event) {
/*  629 */     if (event.button() == 0) {
/*  630 */       double xm = event.x() - this.leftPos;
/*  631 */       double ym = event.y() - this.topPos;
/*  633 */       this.scrolling = false;
/*  635 */       for (CreativeModeTab tab : CreativeModeTabs.tabs()) {
/*  636 */         if (checkTabClicked(tab, xm, ym)) {
/*  637 */           selectTab(tab);
/*  638 */           return true;
/*      */         } 
/*      */       } 
/*      */     } 
/*  643 */     return super.mouseReleased(event);
/*      */   }
/*      */   
/*      */   private boolean canScroll() {
/*  647 */     return (selectedTab.canScroll() && this.menu.canScroll());
/*      */   }
/*      */   
/*      */   private void selectTab(CreativeModeTab tab) {
/*  651 */     CreativeModeTab oldTab = selectedTab;
/*  652 */     selectedTab = tab;
/*  654 */     this.quickCraftSlots.clear();
/*  655 */     this.menu.items.clear();
/*  656 */     if (selectedTab.getType() == CreativeModeTab.Type.HOTBAR) {
/*  657 */       HotbarManager manager = this.minecraft.getHotbarManager();
/*  658 */       for (int hotbarIndex = 0; hotbarIndex < 9; hotbarIndex++) {
/*  659 */         Hotbar hotbar = manager.get(hotbarIndex);
/*  660 */         if (hotbar.isEmpty()) {
/*  661 */           for (int i = 0; i < 9; i++) {
/*  662 */             if (i == hotbarIndex) {
/*  663 */               ItemStack placeholder = new ItemStack((ItemLike)Items.PAPER);
/*  664 */               placeholder.set(DataComponents.CREATIVE_SLOT_LOCK, Unit.INSTANCE);
/*  666 */               Component translatedKeyMessage = this.minecraft.options.keyHotbarSlots[hotbarIndex].getTranslatedKeyMessage();
/*  667 */               Component activatorKeyMessage = this.minecraft.options.keySaveHotbarActivator.getTranslatedKeyMessage();
/*  668 */               placeholder.set(DataComponents.ITEM_NAME, Component.translatable("inventory.hotbarInfo", new Object[] { activatorKeyMessage, translatedKeyMessage }));
/*  669 */               this.menu.items.add(placeholder);
/*      */             } else {
/*  671 */               this.menu.items.add(ItemStack.EMPTY);
/*      */             } 
/*      */           } 
/*      */         } else {
/*  675 */           this.menu.items.addAll(hotbar.load((HolderLookup.Provider)this.minecraft.level.registryAccess()));
/*      */         } 
/*      */       } 
/*  678 */     } else if (selectedTab.getType() == CreativeModeTab.Type.CATEGORY) {
/*  679 */       this.menu.items.addAll(selectedTab.getDisplayItems());
/*      */     } 
/*  682 */     if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
/*  683 */       InventoryMenu inventoryMenu = this.minecraft.player.inventoryMenu;
/*  685 */       if (this.originalSlots == null)
/*  686 */         this.originalSlots = (List<Slot>)ImmutableList.copyOf((Collection)this.menu.slots); 
/*  688 */       this.menu.slots.clear();
/*  689 */       for (int i = 0; i < ((AbstractContainerMenu)inventoryMenu).slots.size(); i++) {
/*      */         int x, y;
/*  692 */         if (i >= 5 && i < 9) {
/*  693 */           int pos = i - 5;
/*  694 */           int col = pos / 2;
/*  695 */           int row = pos % 2;
/*  697 */           x = 54 + col * 54;
/*  698 */           y = 6 + row * 27;
/*  699 */         } else if (i >= 0 && i < 5) {
/*  700 */           x = -2000;
/*  701 */           y = -2000;
/*  702 */         } else if (i == 45) {
/*  703 */           x = 35;
/*  704 */           y = 20;
/*      */         } else {
/*  706 */           int pos = i - 9;
/*  707 */           int col = pos % 9;
/*  708 */           int row = pos / 9;
/*  710 */           x = 9 + col * 18;
/*  712 */           if (i >= 36) {
/*  713 */             y = 112;
/*      */           } else {
/*  715 */             y = 54 + row * 18;
/*      */           } 
/*      */         } 
/*  719 */         Slot slot = new SlotWrapper((Slot)((AbstractContainerMenu)inventoryMenu).slots.get(i), i, x, y);
/*  720 */         this.menu.slots.add(slot);
/*      */       } 
/*  723 */       this.destroyItemSlot = new Slot((Container)CONTAINER, 0, 173, 112);
/*  724 */       this.menu.slots.add(this.destroyItemSlot);
/*  725 */     } else if (oldTab.getType() == CreativeModeTab.Type.INVENTORY) {
/*  726 */       this.menu.slots.clear();
/*  727 */       this.menu.slots.addAll(this.originalSlots);
/*  728 */       this.originalSlots = null;
/*      */     } 
/*  731 */     if (selectedTab.getType() == CreativeModeTab.Type.SEARCH) {
/*  732 */       this.searchBox.setVisible(true);
/*  733 */       this.searchBox.setCanLoseFocus(false);
/*  734 */       this.searchBox.setFocused(true);
/*  735 */       if (oldTab != tab)
/*  736 */         this.searchBox.setValue(""); 
/*  738 */       refreshSearchResults();
/*      */     } else {
/*  740 */       this.searchBox.setVisible(false);
/*  741 */       this.searchBox.setCanLoseFocus(true);
/*  742 */       this.searchBox.setFocused(false);
/*  743 */       this.searchBox.setValue("");
/*      */     } 
/*  746 */     this.scrollOffs = 0.0F;
/*  747 */     this.menu.scrollTo(0.0F);
/*      */   }
/*      */   
/*      */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/*  752 */     if (super.mouseScrolled(x, y, scrollX, scrollY))
/*  753 */       return true; 
/*  756 */     if (!canScroll())
/*  757 */       return false; 
/*  759 */     this.scrollOffs = this.menu.subtractInputFromScroll(this.scrollOffs, scrollY);
/*  760 */     this.menu.scrollTo(this.scrollOffs);
/*  761 */     return true;
/*      */   }
/*      */   
/*      */   protected boolean hasClickedOutside(double mx, double my, int xo, int yo) {
/*  766 */     boolean clickedOutside = (mx < xo || my < yo || mx >= (xo + this.imageWidth) || my >= (yo + this.imageHeight));
/*  767 */     this.hasClickedOutside = (clickedOutside && !checkTabClicked(selectedTab, mx, my));
/*  768 */     return this.hasClickedOutside;
/*      */   }
/*      */   
/*      */   protected boolean insideScrollbar(double xm, double ym) {
/*  772 */     int xo = this.leftPos;
/*  773 */     int yo = this.topPos;
/*  775 */     int xscr = xo + 175;
/*  776 */     int yscr = yo + 18;
/*  777 */     int xscr2 = xscr + 14;
/*  778 */     int yscr2 = yscr + 112;
/*  779 */     return (xm >= xscr && ym >= yscr && xm < xscr2 && ym < yscr2 && selectedTab.canScroll());
/*      */   }
/*      */   
/*      */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/*  785 */     if (this.scrolling) {
/*  786 */       int yscr = this.topPos + 18;
/*  787 */       int yscr2 = yscr + 112;
/*  789 */       this.scrollOffs = ((float)event.y() - yscr - 7.5F) / ((yscr2 - yscr) - 15.0F);
/*  790 */       this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
/*  791 */       this.menu.scrollTo(this.scrollOffs);
/*  793 */       return true;
/*      */     } 
/*  795 */     return super.mouseDragged(event, dx, dy);
/*      */   }
/*      */   
/*      */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  800 */     this.effects.extractRenderState(graphics, mouseX, mouseY);
/*  801 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/*  802 */     for (CreativeModeTab tab : CreativeModeTabs.tabs()) {
/*  803 */       if (checkTabHovering(graphics, tab, mouseX, mouseY))
/*      */         break; 
/*      */     } 
/*  808 */     if (this.destroyItemSlot != null && selectedTab.getType() == CreativeModeTab.Type.INVENTORY && isHovering(this.destroyItemSlot.x, this.destroyItemSlot.y, 16, 16, mouseX, mouseY))
/*  809 */       graphics.setTooltipForNextFrame(this.font, TRASH_SLOT_TOOLTIP, mouseX, mouseY); 
/*      */   }
/*      */   
/*      */   public boolean showsActiveEffects() {
/*  815 */     return this.effects.canSeeEffects();
/*      */   }
/*      */   
/*      */   public List<Component> getTooltipFromContainerItem(ItemStack itemStack) {
/*  820 */     boolean isCreativeSlot = (this.hoveredSlot != null && this.hoveredSlot instanceof CustomCreativeSlot);
/*  821 */     boolean isSingleCategoryTab = (selectedTab.getType() == CreativeModeTab.Type.CATEGORY);
/*  822 */     boolean isSearchTab = (selectedTab.getType() == CreativeModeTab.Type.SEARCH);
/*  824 */     TooltipFlag.Default originalTooltipStyle = this.minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;
/*  825 */     TooltipFlag.Default default_1 = isCreativeSlot ? originalTooltipStyle.asCreative() : originalTooltipStyle;
/*  826 */     List<Component> originalLines = itemStack.getTooltipLines(Item.TooltipContext.of((Level)this.minecraft.level), (Player)this.minecraft.player, (TooltipFlag)default_1);
/*  827 */     if (originalLines.isEmpty())
/*  828 */       return originalLines; 
/*  831 */     if (!isSingleCategoryTab || !isCreativeSlot) {
/*  832 */       List<Component> linesToDisplay = Lists.newArrayList(originalLines);
/*  834 */       if (isSearchTab && isCreativeSlot)
/*  835 */         this.visibleTags.forEach(tag -> {
/*      */               if (itemStack.is(tag))
/*      */                 linesToDisplay.add(1, Component.literal("#" + String.valueOf(tag.location())).withStyle(ChatFormatting.DARK_PURPLE)); 
/*      */             }); 
/*  842 */       int i = 1;
/*  843 */       for (CreativeModeTab tab : CreativeModeTabs.tabs()) {
/*  844 */         if (tab.getType() != CreativeModeTab.Type.SEARCH && tab.contains(itemStack))
/*  845 */           linesToDisplay.add(i++, tab.getDisplayName().copy().withStyle(ChatFormatting.BLUE)); 
/*      */       } 
/*  849 */       return linesToDisplay;
/*      */     } 
/*  852 */     return originalLines;
/*      */   }
/*      */   
/*      */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  857 */     super.extractBackground(graphics, mouseX, mouseY, a);
/*  858 */     for (CreativeModeTab tab : CreativeModeTabs.tabs()) {
/*  859 */       if (tab != selectedTab)
/*  860 */         extractTabButton(graphics, mouseX, mouseY, tab); 
/*      */     } 
/*  864 */     graphics.blit(RenderPipelines.GUI_TEXTURED, selectedTab.getBackgroundTexture(), this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*  866 */     if (insideScrollbar(mouseX, mouseY))
/*  867 */       if (canScroll()) {
/*  868 */         graphics.requestCursor(this.scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
/*      */       } else {
/*  870 */         graphics.requestCursor(CursorTypes.NOT_ALLOWED);
/*      */       }  
/*  874 */     this.searchBox.extractRenderState(graphics, mouseX, mouseY, a);
/*  876 */     int xscr = this.leftPos + 175;
/*  877 */     int yscr = this.topPos + 18;
/*  878 */     int yscr2 = yscr + 112;
/*  880 */     if (selectedTab.canScroll()) {
/*  881 */       Identifier sprite = canScroll() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
/*  882 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, xscr, yscr + (int)((yscr2 - yscr - 17) * this.scrollOffs), 12, 15);
/*      */     } 
/*  885 */     extractTabButton(graphics, mouseX, mouseY, selectedTab);
/*  887 */     if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY)
/*  888 */       InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, this.leftPos + 73, this.topPos + 6, this.leftPos + 105, this.topPos + 49, 20, 0.0625F, mouseX, mouseY, (LivingEntity)this.minecraft.player); 
/*      */   }
/*      */   
/*      */   private int getTabX(CreativeModeTab tab) {
/*  893 */     int pos = tab.column();
/*  894 */     int spacing = 27;
/*  895 */     int x = 27 * pos;
/*  897 */     if (tab.isAlignedRight())
/*  898 */       x = this.imageWidth - 27 * (7 - pos) + 1; 
/*  900 */     return x;
/*      */   }
/*      */   
/*      */   private int getTabY(CreativeModeTab tab) {
/*  904 */     int y = 0;
/*  905 */     if (tab.row() == CreativeModeTab.Row.TOP) {
/*  906 */       y -= 32;
/*      */     } else {
/*  908 */       y += this.imageHeight;
/*      */     } 
/*  910 */     return y;
/*      */   }
/*      */   
/*      */   protected boolean checkTabClicked(CreativeModeTab tab, double xm, double ym) {
/*  914 */     int x = getTabX(tab);
/*  915 */     int y = getTabY(tab);
/*  916 */     return (xm >= x && xm <= (x + 26) && ym >= y && ym <= (y + 32));
/*      */   }
/*      */   
/*      */   protected boolean checkTabHovering(GuiGraphicsExtractor graphics, CreativeModeTab tab, int xm, int ym) {
/*  920 */     int x = getTabX(tab);
/*  921 */     int y = getTabY(tab);
/*  922 */     if (isHovering(x + 3, y + 3, 21, 27, xm, ym)) {
/*  923 */       graphics.setTooltipForNextFrame(this.font, tab.getDisplayName(), xm, ym);
/*  924 */       return true;
/*      */     } 
/*  927 */     return false;
/*      */   }
/*      */   
/*      */   protected void extractTabButton(GuiGraphicsExtractor graphics, int mouseX, int mouseY, CreativeModeTab tab) {
/*      */     Identifier[] sprites;
/*  931 */     boolean selected = (tab == selectedTab);
/*  932 */     boolean isTop = (tab.row() == CreativeModeTab.Row.TOP);
/*  933 */     int pos = tab.column();
/*  934 */     int x = this.leftPos + getTabX(tab);
/*  935 */     int y = this.topPos - (isTop ? 28 : -(this.imageHeight - 4));
/*  938 */     if (isTop) {
/*  939 */       sprites = selected ? SELECTED_TOP_TABS : UNSELECTED_TOP_TABS;
/*      */     } else {
/*  941 */       sprites = selected ? SELECTED_BOTTOM_TABS : UNSELECTED_BOTTOM_TABS;
/*      */     } 
/*  944 */     if (!selected && mouseX > x && mouseY > y && mouseX < x + 26 && mouseY < y + 32)
/*  945 */       graphics.requestCursor(CursorTypes.POINTING_HAND); 
/*  948 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprites[Mth.clamp(pos, 0, sprites.length)], x, y, 26, 32);
/*  950 */     int iconX = x + 13 - 8;
/*  951 */     int iconY = y + 16 - 8 + (isTop ? 1 : -1);
/*  952 */     graphics.item(tab.getIconItem(), iconX, iconY);
/*      */   }
/*      */   
/*      */   public boolean isInventoryOpen() {
/*  956 */     return (selectedTab.getType() == CreativeModeTab.Type.INVENTORY);
/*      */   }
/*      */   
/*      */   private static class SlotWrapper extends Slot {
/*      */     private final Slot target;
/*      */     
/*      */     public SlotWrapper(Slot target, int index, int x, int y) {
/*  964 */       super(target.container, index, x, y);
/*  965 */       this.target = target;
/*      */     }
/*      */     
/*      */     public void onTake(Player player, ItemStack carried) {
/*  970 */       this.target.onTake(player, carried);
/*      */     }
/*      */     
/*      */     public boolean mayPlace(ItemStack itemStack) {
/*  975 */       return this.target.mayPlace(itemStack);
/*      */     }
/*      */     
/*      */     public ItemStack getItem() {
/*  980 */       return this.target.getItem();
/*      */     }
/*      */     
/*      */     public boolean hasItem() {
/*  985 */       return this.target.hasItem();
/*      */     }
/*      */     
/*      */     public void setByPlayer(ItemStack itemStack, ItemStack previous) {
/*  990 */       this.target.setByPlayer(itemStack, previous);
/*      */     }
/*      */     
/*      */     public void set(ItemStack itemStack) {
/*  995 */       this.target.set(itemStack);
/*      */     }
/*      */     
/*      */     public void setChanged() {
/* 1000 */       this.target.setChanged();
/*      */     }
/*      */     
/*      */     public int getMaxStackSize() {
/* 1005 */       return this.target.getMaxStackSize();
/*      */     }
/*      */     
/*      */     public int getMaxStackSize(ItemStack itemStack) {
/* 1010 */       return this.target.getMaxStackSize(itemStack);
/*      */     }
/*      */     
/*      */     public Identifier getNoItemIcon() {
/* 1015 */       return this.target.getNoItemIcon();
/*      */     }
/*      */     
/*      */     public ItemStack remove(int amount) {
/* 1020 */       return this.target.remove(amount);
/*      */     }
/*      */     
/*      */     public boolean isActive() {
/* 1025 */       return this.target.isActive();
/*      */     }
/*      */     
/*      */     public boolean mayPickup(Player player) {
/* 1030 */       return this.target.mayPickup(player);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class CustomCreativeSlot extends Slot {
/*      */     public CustomCreativeSlot(Container container, int slot, int x, int y) {
/* 1036 */       super(container, slot, x, y);
/*      */     }
/*      */     
/*      */     public boolean mayPickup(Player player) {
/* 1041 */       ItemStack item = getItem();
/* 1042 */       if (super.mayPickup(player) && !item.isEmpty())
/* 1043 */         return (item.isItemEnabled(player.level().enabledFeatures()) && 
/* 1044 */           !item.has(DataComponents.CREATIVE_SLOT_LOCK)); 
/* 1046 */       return item.isEmpty();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void handleHotbarLoadOrSave(Minecraft minecraft, int index, boolean isLoadPressed, boolean isSavePressed) {
/* 1051 */     LocalPlayer player = minecraft.player;
/* 1052 */     RegistryAccess registries = player.level().registryAccess();
/* 1053 */     HotbarManager manager = minecraft.getHotbarManager();
/* 1054 */     Hotbar hotbar = manager.get(index);
/* 1056 */     if (isLoadPressed) {
/* 1057 */       List<ItemStack> originalItems = hotbar.load((HolderLookup.Provider)registries);
/* 1058 */       for (int i = 0; i < Inventory.getSelectionSize(); i++) {
/* 1059 */         ItemStack itemStack = originalItems.get(i);
/* 1060 */         player.getInventory().setItem(i, itemStack);
/* 1061 */         minecraft.gameMode.handleCreativeModeItemAdd(itemStack, 36 + i);
/*      */       } 
/* 1063 */       player.inventoryMenu.broadcastChanges();
/* 1064 */     } else if (isSavePressed) {
/* 1065 */       hotbar.storeFrom(player.getInventory(), registries);
/* 1066 */       Component translatedKeyMessage = minecraft.options.keyHotbarSlots[index].getTranslatedKeyMessage();
/* 1067 */       Component activatorKeyMessage = minecraft.options.keyLoadHotbarActivator.getTranslatedKeyMessage();
/* 1068 */       MutableComponent mutableComponent = Component.translatable("inventory.hotbarSaved", new Object[] { activatorKeyMessage, translatedKeyMessage });
/* 1069 */       minecraft.gui.hud.setOverlayMessage((Component)mutableComponent, false);
/* 1070 */       minecraft.getNarrator().saySystemNow((Component)mutableComponent);
/* 1072 */       manager.save();
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\CreativeModeInventoryScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */