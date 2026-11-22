/*     */ package net.minecraft.client.gui.screens.recipebook;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.ClientRecipeBook;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.Renderable;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.navigation.ScreenAxis;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.input.PreeditEvent;
/*     */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.resources.language.LanguageInfo;
/*     */ import net.minecraft.client.resources.language.LanguageManager;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.inventory.RecipeBookMenu;
/*     */ import net.minecraft.world.inventory.RecipeBookType;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
/*     */ import net.minecraft.world.item.crafting.RecipeBookCategory;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplayContext;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public abstract class RecipeBookComponent<T extends RecipeBookMenu> implements GuiEventListener, Renderable, NarratableEntry {
/*  58 */   public static final WidgetSprites RECIPE_BUTTON_SPRITES = new WidgetSprites(
/*  59 */       Identifier.withDefaultNamespace("recipe_book/button"), 
/*  60 */       Identifier.withDefaultNamespace("recipe_book/button_highlighted"));
/*     */   
/*  63 */   protected static final Identifier RECIPE_BOOK_LOCATION = Identifier.withDefaultNamespace("textures/gui/recipe_book.png");
/*     */   
/*     */   private static final int BACKGROUND_TEXTURE_WIDTH = 256;
/*     */   
/*     */   private static final int BACKGROUND_TEXTURE_HEIGHT = 256;
/*     */   
/*  67 */   private static final Component SEARCH_HINT = (Component)Component.translatable("gui.recipebook.search_hint").withStyle(EditBox.SEARCH_HINT_STYLE);
/*     */   
/*     */   public static final int IMAGE_WIDTH = 147;
/*     */   
/*     */   public static final int IMAGE_HEIGHT = 166;
/*     */   
/*     */   private static final int OFFSET_X_POSITION = 86;
/*     */   
/*     */   private static final int BORDER_WIDTH = 8;
/*     */   
/*  73 */   private static final Component ALL_RECIPES_TOOLTIP = (Component)Component.translatable("gui.recipebook.toggleRecipes.all");
/*     */   
/*     */   private static final int TICKS_TO_SWAP_SLOT = 30;
/*     */   
/*     */   private int xOffset;
/*     */   
/*     */   private int width;
/*     */   
/*     */   private int height;
/*     */   
/*     */   private float time;
/*     */   
/*     */   private RecipeDisplayId lastPlacedRecipe;
/*     */   
/*     */   private final GhostSlots ghostSlots;
/*     */   
/*  86 */   private final List<RecipeBookTabButton> tabButtons = Lists.newArrayList();
/*     */   
/*     */   private RecipeBookTabButton selectedTab;
/*     */   
/*     */   protected CycleButton<Boolean> filterButton;
/*     */   
/*     */   protected final T menu;
/*     */   
/*     */   protected Minecraft minecraft;
/*     */   
/*     */   private EditBox searchBox;
/*     */   
/*  94 */   private String lastSearch = "";
/*     */   
/*     */   private final List<TabInfo> tabInfos;
/*     */   
/*     */   private ClientRecipeBook book;
/*     */   
/*     */   private final RecipeBookPage recipeBookPage;
/*     */   
/*     */   private RecipeDisplayId lastRecipe;
/*     */   
/*     */   private RecipeCollection lastRecipeCollection;
/*     */   
/* 103 */   private final StackedItemContents stackedContents = new StackedItemContents();
/*     */   
/*     */   private int timesInventoryChanged;
/*     */   
/*     */   private boolean ignoreTextInput;
/*     */   
/*     */   private boolean visible;
/*     */   
/*     */   private boolean widthTooNarrow;
/*     */   
/*     */   private ScreenRectangle magnifierIconPlacement;
/*     */   
/*     */   public RecipeBookComponent(T menu, List<TabInfo> tabInfos) {
/* 115 */     this.menu = menu;
/* 116 */     this.tabInfos = tabInfos;
/* 117 */     SlotSelectTime slotSelectTime = () -> Mth.floor(this.time / 30.0F);
/* 119 */     this.ghostSlots = new GhostSlots(slotSelectTime);
/* 120 */     this.recipeBookPage = new RecipeBookPage(this, slotSelectTime, menu instanceof net.minecraft.world.inventory.AbstractFurnaceMenu);
/*     */   }
/*     */   
/*     */   public void init(int width, int height, Minecraft minecraft, boolean widthTooNarrow) {
/* 124 */     this.minecraft = minecraft;
/* 125 */     this.width = width;
/* 126 */     this.height = height;
/* 127 */     this.widthTooNarrow = widthTooNarrow;
/* 128 */     this.book = minecraft.player.getRecipeBook();
/* 130 */     this.timesInventoryChanged = minecraft.player.getInventory().getTimesChanged();
/* 132 */     this.visible = isVisibleAccordingToBookData();
/* 133 */     if (this.visible)
/* 134 */       initVisuals(); 
/*     */   }
/*     */   
/*     */   private void initVisuals() {
/* 139 */     boolean isFiltering = isFiltering();
/* 141 */     this.xOffset = this.widthTooNarrow ? 0 : 86;
/* 142 */     int xo = getXOrigin();
/* 143 */     int yo = getYOrigin();
/* 145 */     this.stackedContents.clear();
/* 146 */     this.minecraft.player.getInventory().fillStackedContents(this.stackedContents);
/* 147 */     this.menu.fillCraftSlotsStackedContents(this.stackedContents);
/* 149 */     String oldEdit = (this.searchBox != null) ? this.searchBox.getValue() : "";
/* 150 */     Objects.requireNonNull(this.minecraft.font);
/* 150 */     this.searchBox = new EditBox(this.minecraft.font, xo + 25, yo + 13, 81, 9 + 5, (Component)Component.translatable("itemGroup.search"));
/* 151 */     this.searchBox.setMaxLength(50);
/* 152 */     this.searchBox.setVisible(true);
/* 153 */     this.searchBox.setTextColor(-1);
/* 154 */     this.searchBox.setValue(oldEdit);
/* 155 */     this.searchBox.setHint(SEARCH_HINT);
/* 158 */     this.magnifierIconPlacement = ScreenRectangle.of(ScreenAxis.HORIZONTAL, xo + 8, this.searchBox
/*     */         
/* 161 */         .getY(), this.searchBox
/* 162 */         .getX() - getXOrigin(), this.searchBox
/* 163 */         .getHeight());
/* 166 */     this.recipeBookPage.init(this.minecraft, xo, yo);
/* 168 */     this
/*     */       
/* 172 */       .filterButton = CycleButton.booleanBuilder(getRecipeFilterName(), ALL_RECIPES_TOOLTIP, isFiltering).withTooltip(filtering -> filtering.booleanValue() ? Tooltip.create(getRecipeFilterName()) : Tooltip.create(ALL_RECIPES_TOOLTIP)).withSprite((cycleButton, filtering) -> getFilterButtonTextures().get(filtering.booleanValue(), cycleButton.isHoveredOrFocused())).displayState(CycleButton.DisplayState.HIDE).create(xo + 110, yo + 12, 26, 16, CommonComponents.EMPTY, (button, value) -> {
/*     */           toggleFiltering();
/*     */           sendUpdateSettings();
/*     */           updateCollections(false, value.booleanValue());
/*     */         });
/* 178 */     this.tabButtons.clear();
/* 180 */     for (TabInfo tabInfo : this.tabInfos)
/* 181 */       this.tabButtons.add(new RecipeBookTabButton(0, 0, tabInfo, this::onTabButtonPress)); 
/* 184 */     if (this.selectedTab != null)
/* 185 */       this.selectedTab = this.tabButtons.stream().filter(o -> o.getCategory().equals(this.selectedTab.getCategory())).findFirst().orElse(null); 
/* 187 */     if (this.selectedTab == null)
/* 188 */       this.selectedTab = this.tabButtons.get(0); 
/* 190 */     this.selectedTab.select();
/* 192 */     selectMatchingRecipes();
/* 193 */     updateTabs(isFiltering);
/* 194 */     updateCollections(false, isFiltering);
/*     */   }
/*     */   
/*     */   private int getYOrigin() {
/* 198 */     return (this.height - 166) / 2;
/*     */   }
/*     */   
/*     */   private int getXOrigin() {
/* 202 */     return (this.width - 147) / 2 - this.xOffset;
/*     */   }
/*     */   
/*     */   public int updateScreenPosition(int width, int imageWidth) {
/*     */     int leftPos;
/* 209 */     if (isVisible() && !this.widthTooNarrow) {
/* 210 */       leftPos = 177 + (width - imageWidth - 200) / 2;
/*     */     } else {
/* 212 */       leftPos = (width - imageWidth) / 2;
/*     */     } 
/* 215 */     return leftPos;
/*     */   }
/*     */   
/*     */   public void toggleVisibility() {
/* 219 */     setVisible(!isVisible());
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 223 */     return this.visible;
/*     */   }
/*     */   
/*     */   private boolean isVisibleAccordingToBookData() {
/* 227 */     return this.book.isOpen(this.menu.getRecipeBookType());
/*     */   }
/*     */   
/*     */   protected void setVisible(boolean visible) {
/* 231 */     if (visible)
/* 232 */       initVisuals(); 
/* 235 */     this.visible = visible;
/* 236 */     this.book.setOpen(this.menu.getRecipeBookType(), visible);
/* 237 */     if (!visible)
/* 238 */       this.recipeBookPage.setInvisible(); 
/* 240 */     sendUpdateSettings();
/*     */   }
/*     */   
/*     */   public void slotClicked(Slot slot) {
/* 246 */     if (slot != null && isCraftingSlot(slot)) {
/* 247 */       this.lastPlacedRecipe = null;
/* 248 */       this.ghostSlots.clear();
/* 250 */       if (isVisible())
/* 251 */         updateStackedContents(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void selectMatchingRecipes() {
/* 258 */     for (TabInfo tabInfo : this.tabInfos) {
/* 259 */       for (RecipeCollection recipeCollection : this.book.getCollection(tabInfo.category()))
/* 260 */         selectMatchingRecipes(recipeCollection, this.stackedContents); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateCollections(boolean resetPage, boolean isFiltering) {
/* 268 */     List<RecipeCollection> tabCollection = this.book.getCollection(this.selectedTab.getCategory());
/* 270 */     List<RecipeCollection> collection = Lists.newArrayList(tabCollection);
/* 273 */     collection.removeIf(c -> !c.hasAnySelected());
/* 276 */     String searchTarget = this.searchBox.getValue();
/* 277 */     if (!searchTarget.isEmpty()) {
/* 278 */       ClientPacketListener connection = this.minecraft.getConnection();
/* 279 */       if (connection != null) {
/* 280 */         ObjectLinkedOpenHashSet objectLinkedOpenHashSet = new ObjectLinkedOpenHashSet(connection.searchTrees().recipes().search(searchTarget.toLowerCase(Locale.ROOT)));
/* 281 */         collection.removeIf(e -> !set.contains(e));
/*     */       } 
/*     */     } 
/* 286 */     if (isFiltering)
/* 287 */       collection.removeIf(c -> !c.hasCraftable()); 
/* 290 */     this.recipeBookPage.updateCollections(collection, resetPage, isFiltering);
/*     */   }
/*     */   
/*     */   private void updateTabs(boolean isFiltering) {
/* 294 */     int xPosTab = (this.width - 147) / 2 - this.xOffset - 30;
/* 295 */     int yPosTab = (this.height - 166) / 2 + 3;
/* 296 */     int yOffset = 27;
/* 298 */     int index = 0;
/* 299 */     for (RecipeBookTabButton tabButton : this.tabButtons) {
/* 300 */       ExtendedRecipeBookCategory category = tabButton.getCategory();
/* 302 */       if (category instanceof SearchRecipeBookCategory) {
/* 303 */         tabButton.visible = true;
/* 304 */         tabButton.setPosition(xPosTab, yPosTab + 27 * index++);
/*     */         continue;
/*     */       } 
/* 305 */       if (tabButton.updateVisibility(this.book)) {
/* 306 */         tabButton.setPosition(xPosTab, yPosTab + 27 * index++);
/* 307 */         tabButton.startAnimation(this.book, isFiltering);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 314 */     boolean shouldBeVisible = isVisibleAccordingToBookData();
/* 315 */     if (isVisible() != shouldBeVisible)
/* 316 */       setVisible(shouldBeVisible); 
/* 319 */     if (!isVisible())
/*     */       return; 
/* 323 */     if (this.timesInventoryChanged != this.minecraft.player.getInventory().getTimesChanged()) {
/* 324 */       updateStackedContents();
/* 325 */       this.timesInventoryChanged = this.minecraft.player.getInventory().getTimesChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateStackedContents() {
/* 330 */     this.stackedContents.clear();
/* 331 */     this.minecraft.player.getInventory().fillStackedContents(this.stackedContents);
/* 332 */     this.menu.fillCraftSlotsStackedContents(this.stackedContents);
/* 334 */     selectMatchingRecipes();
/* 335 */     updateCollections(false, isFiltering());
/*     */   }
/*     */   
/*     */   private boolean isFiltering() {
/* 339 */     return this.book.isFiltering(this.menu.getRecipeBookType());
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 344 */     if (!isVisible())
/*     */       return; 
/* 348 */     if (!this.minecraft.hasControlDown())
/* 349 */       this.time += a; 
/* 352 */     int xo = getXOrigin();
/* 353 */     int yo = getYOrigin();
/* 354 */     graphics.blit(RenderPipelines.GUI_TEXTURED, RECIPE_BOOK_LOCATION, xo, yo, 1.0F, 1.0F, 147, 166, 256, 256);
/* 356 */     this.searchBox.extractRenderState(graphics, mouseX, mouseY, a);
/* 358 */     for (RecipeBookTabButton tabButton : this.tabButtons)
/* 359 */       tabButton.extractRenderState(graphics, mouseX, mouseY, a); 
/* 362 */     this.filterButton.extractRenderState(graphics, mouseX, mouseY, a);
/* 364 */     this.recipeBookPage.extractRenderState(graphics, xo, yo, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   public void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY, Slot hoveredSlot) {
/* 368 */     if (!isVisible())
/*     */       return; 
/* 372 */     this.recipeBookPage.extractTooltip(graphics, mouseX, mouseY);
/* 374 */     this.ghostSlots.extractTooltip(graphics, this.minecraft, mouseX, mouseY, hoveredSlot);
/*     */   }
/*     */   
/*     */   public void extractGhostRecipe(GuiGraphicsExtractor graphics, boolean isResultSlotBig) {
/* 380 */     this.ghostSlots.extractRenderState(graphics, this.minecraft, isResultSlotBig);
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 385 */     if (!isVisible() || this.minecraft.player.isSpectator())
/* 386 */       return false; 
/* 390 */     if (this.recipeBookPage.mouseClicked(event, getXOrigin(), getYOrigin(), 147, 166, doubleClick)) {
/* 391 */       RecipeDisplayId recipe = this.recipeBookPage.getLastClickedRecipe();
/* 392 */       RecipeCollection recipeCollection = this.recipeBookPage.getLastClickedRecipeCollection();
/* 394 */       if (recipe != null && recipeCollection != null) {
/* 395 */         if (!tryPlaceRecipe(recipeCollection, recipe, event.hasShiftDown()))
/* 396 */           return false; 
/* 399 */         this.lastRecipeCollection = recipeCollection;
/* 400 */         this.lastRecipe = recipe;
/* 402 */         if (!isOffsetNextToMainGUI())
/* 403 */           setVisible(false); 
/*     */       } 
/* 407 */       return true;
/*     */     } 
/* 410 */     if (this.searchBox != null) {
/* 411 */       boolean clickedMagnifierIcon = (this.magnifierIconPlacement != null && this.magnifierIconPlacement.containsPoint(Mth.floor(event.x()), Mth.floor(event.y())));
/* 412 */       if (clickedMagnifierIcon || this.searchBox.mouseClicked(event, doubleClick)) {
/* 413 */         this.searchBox.setFocused(true);
/* 414 */         return true;
/*     */       } 
/* 416 */       this.searchBox.setFocused(false);
/*     */     } 
/* 420 */     if (this.filterButton.mouseClicked(event, doubleClick))
/* 421 */       return true; 
/* 424 */     for (RecipeBookTabButton tabButton : this.tabButtons) {
/* 425 */       if (tabButton.mouseClicked(event, doubleClick))
/* 426 */         return true; 
/*     */     } 
/* 430 */     return false;
/*     */   }
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 435 */     if (this.searchBox != null && this.searchBox.isFocused())
/* 436 */       return this.searchBox.mouseDragged(event, dx, dy); 
/* 438 */     return false;
/*     */   }
/*     */   
/*     */   private boolean tryPlaceRecipe(RecipeCollection recipeCollection, RecipeDisplayId recipe, boolean useMaxItems) {
/* 442 */     if (!recipeCollection.isCraftable(recipe) && recipe.equals(this.lastPlacedRecipe))
/* 443 */       return false; 
/* 446 */     this.lastPlacedRecipe = recipe;
/* 447 */     this.ghostSlots.clear();
/* 448 */     this.minecraft.gameMode.handlePlaceRecipe(this.minecraft.player.containerMenu.containerId, recipe, useMaxItems);
/* 449 */     return true;
/*     */   }
/*     */   
/*     */   private void onTabButtonPress(Button button) {
/* 453 */     if (this.selectedTab != button && button instanceof RecipeBookTabButton) {
/* 453 */       RecipeBookTabButton recipeBookTabButton = (RecipeBookTabButton)button;
/* 454 */       replaceSelected(recipeBookTabButton);
/* 455 */       updateCollections(true, isFiltering());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void replaceSelected(RecipeBookTabButton tabButton) {
/* 460 */     if (this.selectedTab != null)
/* 461 */       this.selectedTab.unselect(); 
/* 463 */     tabButton.select();
/* 464 */     this.selectedTab = tabButton;
/*     */   }
/*     */   
/*     */   private void toggleFiltering() {
/* 468 */     RecipeBookType type = this.menu.getRecipeBookType();
/* 469 */     boolean newSetting = !this.book.isFiltering(type);
/* 470 */     this.book.setFiltering(type, newSetting);
/*     */   }
/*     */   
/*     */   public boolean hasClickedOutside(double mx, double my, int leftPos, int topPos, int imageWidth, int imageHeight) {
/* 474 */     if (!isVisible())
/* 475 */       return true; 
/* 478 */     boolean clickedOutside = (mx < leftPos || my < topPos || mx >= (leftPos + imageWidth) || my >= (topPos + imageHeight));
/* 479 */     boolean clickedOnRecipeBook = ((leftPos - 147) < mx && mx < leftPos && topPos < my && my < (topPos + imageHeight));
/* 481 */     return (clickedOutside && !clickedOnRecipeBook && !this.selectedTab.isHoveredOrFocused());
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 486 */     this.ignoreTextInput = false;
/* 487 */     if (!isVisible() || this.minecraft.player.isSpectator())
/* 488 */       return false; 
/* 491 */     if (event.isEscape() && !isOffsetNextToMainGUI()) {
/* 492 */       setVisible(false);
/* 493 */       return true;
/*     */     } 
/* 496 */     if (this.searchBox.keyPressed(event)) {
/* 497 */       checkSearchStringUpdate();
/* 498 */       return true;
/*     */     } 
/* 500 */     if (this.searchBox.isFocused() && this.searchBox.isVisible() && !event.isEscape())
/* 502 */       return true; 
/* 505 */     if (this.minecraft.options.keyChat.matches(event) && !this.searchBox.isFocused()) {
/* 506 */       this.ignoreTextInput = true;
/* 507 */       this.searchBox.setFocused(true);
/* 509 */       return true;
/*     */     } 
/* 512 */     if (event.isSelection() && this.lastRecipeCollection != null && this.lastRecipe != null) {
/* 513 */       AbstractWidget.playButtonClickSound(Minecraft.getInstance().getSoundManager());
/* 514 */       return tryPlaceRecipe(this.lastRecipeCollection, this.lastRecipe, event.hasShiftDown());
/*     */     } 
/* 517 */     return false;
/*     */   }
/*     */   
/*     */   public boolean keyReleased(KeyEvent event) {
/* 522 */     this.ignoreTextInput = false;
/* 523 */     return super.keyReleased(event);
/*     */   }
/*     */   
/*     */   public boolean charTyped(CharacterEvent event) {
/* 528 */     if (this.ignoreTextInput)
/* 529 */       return false; 
/* 531 */     if (!isVisible() || this.minecraft.player.isSpectator())
/* 532 */       return false; 
/* 535 */     if (this.searchBox.charTyped(event)) {
/* 536 */       checkSearchStringUpdate();
/* 537 */       return true;
/*     */     } 
/* 540 */     return super.charTyped(event);
/*     */   }
/*     */   
/*     */   public boolean preeditUpdated(PreeditEvent event) {
/* 545 */     if (this.ignoreTextInput)
/* 546 */       return false; 
/* 548 */     if (!isVisible() || this.minecraft.player.isSpectator())
/* 549 */       return false; 
/* 552 */     if (this.searchBox.preeditUpdated(event))
/* 553 */       return true; 
/* 556 */     return super.preeditUpdated(event);
/*     */   }
/*     */   
/*     */   public boolean isMouseOver(double mouseX, double mouseY) {
/* 561 */     return false;
/*     */   }
/*     */   
/*     */   public void setFocused(boolean focused) {}
/*     */   
/*     */   public boolean isFocused() {
/* 571 */     return false;
/*     */   }
/*     */   
/*     */   private void checkSearchStringUpdate() {
/* 575 */     String searchText = this.searchBox.getValue().toLowerCase(Locale.ROOT);
/* 576 */     pirateSpeechForThePeople(searchText);
/* 578 */     if (!searchText.equals(this.lastSearch)) {
/* 579 */       updateCollections(false, isFiltering());
/* 580 */       this.lastSearch = searchText;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void pirateSpeechForThePeople(String searchTarget) {
/* 585 */     if ("excitedze".equals(searchTarget)) {
/* 586 */       LanguageManager languageManager = this.minecraft.getLanguageManager();
/* 587 */       String arrrrCode = "en_pt";
/* 588 */       LanguageInfo language = languageManager.getLanguage("en_pt");
/* 589 */       if (language == null || languageManager.getSelected().equals("en_pt"))
/*     */         return; 
/* 592 */       languageManager.setSelected("en_pt");
/* 593 */       this.minecraft.options.languageCode = "en_pt";
/* 594 */       this.minecraft.reloadResourcePacks();
/* 595 */       this.minecraft.options.save();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isOffsetNextToMainGUI() {
/* 600 */     return (this.xOffset == 86);
/*     */   }
/*     */   
/*     */   public void recipesUpdated() {
/* 604 */     selectMatchingRecipes();
/* 605 */     updateTabs(isFiltering());
/* 607 */     if (isVisible())
/* 609 */       updateCollections(false, isFiltering()); 
/*     */   }
/*     */   
/*     */   public void recipeShown(RecipeDisplayId recipe) {
/* 614 */     this.minecraft.player.removeRecipeHighlight(recipe);
/*     */   }
/*     */   
/*     */   public void fillGhostRecipe(RecipeDisplay recipe) {
/* 618 */     this.ghostSlots.clear();
/* 619 */     ContextMap context = SlotDisplayContext.fromLevel((Level)Objects.requireNonNull(this.minecraft.level));
/* 620 */     fillGhostRecipe(this.ghostSlots, recipe, context);
/*     */   }
/*     */   
/*     */   protected void sendUpdateSettings() {
/* 626 */     if (this.minecraft.getConnection() != null) {
/* 627 */       RecipeBookType type = this.menu.getRecipeBookType();
/* 628 */       boolean open = this.book.getBookSettings().isOpen(type);
/* 629 */       boolean filtering = this.book.getBookSettings().isFiltering(type);
/* 630 */       this.minecraft.getConnection().send((Packet)new ServerboundRecipeBookChangeSettingsPacket(type, open, filtering));
/*     */     } 
/*     */   }
/*     */   
/*     */   public NarratableEntry.NarrationPriority narrationPriority() {
/* 638 */     return this.visible ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
/*     */   }
/*     */   
/*     */   public void updateNarration(NarrationElementOutput output) {
/* 643 */     List<NarratableEntry> narratableEntries = Lists.newArrayList();
/* 644 */     this.recipeBookPage.listButtons(e -> {
/*     */           if (e.isActive())
/*     */             narratableEntries.add(e); 
/*     */         });
/* 649 */     narratableEntries.add(this.searchBox);
/* 650 */     narratableEntries.add(this.filterButton);
/* 651 */     narratableEntries.addAll(this.tabButtons);
/* 653 */     Screen.NarratableSearchResult narratable = Screen.findNarratableWidget(narratableEntries, null);
/* 654 */     if (narratable != null)
/* 655 */       narratable.entry().updateNarration(output.nest()); 
/*     */   }
/*     */   
/*     */   protected abstract WidgetSprites getFilterButtonTextures();
/*     */   
/*     */   protected abstract boolean isCraftingSlot(Slot paramSlot);
/*     */   
/*     */   protected abstract void selectMatchingRecipes(RecipeCollection paramRecipeCollection, StackedItemContents paramStackedItemContents);
/*     */   
/*     */   protected abstract Component getRecipeFilterName();
/*     */   
/*     */   protected abstract void fillGhostRecipe(GhostSlots paramGhostSlots, RecipeDisplay paramRecipeDisplay, ContextMap paramContextMap);
/*     */   
/*     */   public static final class TabInfo extends Record {
/*     */     private final ItemStack primaryIcon;
/*     */     
/*     */     private final Optional<ItemStack> secondaryIcon;
/*     */     
/*     */     private final ExtendedRecipeBookCategory category;
/*     */     
/*     */     public TabInfo(ItemStack primaryIcon, Optional<ItemStack> secondaryIcon, ExtendedRecipeBookCategory category) {
/* 659 */       this.primaryIcon = primaryIcon;
/* 659 */       this.secondaryIcon = secondaryIcon;
/* 659 */       this.category = category;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #659	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #659	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #659	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ItemStack primaryIcon() {
/* 659 */       return this.primaryIcon;
/*     */     }
/*     */     
/*     */     public Optional<ItemStack> secondaryIcon() {
/* 659 */       return this.secondaryIcon;
/*     */     }
/*     */     
/*     */     public ExtendedRecipeBookCategory category() {
/* 659 */       return this.category;
/*     */     }
/*     */     
/*     */     public TabInfo(SearchRecipeBookCategory category) {
/* 666 */       this(new ItemStack((ItemLike)Items.COMPASS), Optional.empty(), category);
/*     */     }
/*     */     
/*     */     public TabInfo(Item icon, RecipeBookCategory category) {
/* 670 */       this(new ItemStack((ItemLike)icon), Optional.empty(), (ExtendedRecipeBookCategory)category);
/*     */     }
/*     */     
/*     */     public TabInfo(Item primaryIcon, Item secondaryIcon, RecipeBookCategory category) {
/* 674 */       this(new ItemStack((ItemLike)primaryIcon), Optional.of(new ItemStack((ItemLike)secondaryIcon)), (ExtendedRecipeBookCategory)category);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\recipebook\RecipeBookComponent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */