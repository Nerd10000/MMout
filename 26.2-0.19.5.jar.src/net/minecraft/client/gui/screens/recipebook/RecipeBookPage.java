/*     */ package net.minecraft.client.gui.screens.recipebook;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.ClientRecipeBook;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplayContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class RecipeBookPage {
/*     */   public static final int ITEMS_PER_PAGE = 20;
/*     */   
/*  29 */   private static final WidgetSprites PAGE_FORWARD_SPRITES = new WidgetSprites(
/*  30 */       Identifier.withDefaultNamespace("recipe_book/page_forward"), 
/*  31 */       Identifier.withDefaultNamespace("recipe_book/page_forward_highlighted"));
/*     */   
/*  33 */   private static final WidgetSprites PAGE_BACKWARD_SPRITES = new WidgetSprites(
/*  34 */       Identifier.withDefaultNamespace("recipe_book/page_backward"), 
/*  35 */       Identifier.withDefaultNamespace("recipe_book/page_backward_highlighted"));
/*     */   
/*  37 */   private static final Component NEXT_PAGE_TEXT = (Component)Component.translatable("gui.recipebook.next_page");
/*     */   
/*  38 */   private static final Component PREVIOUS_PAGE_TEXT = (Component)Component.translatable("gui.recipebook.previous_page");
/*     */   
/*     */   private static final int TURN_PAGE_SPRITE_WIDTH = 12;
/*     */   
/*     */   private static final int TURN_PAGE_SPRITE_HEIGHT = 17;
/*     */   
/*  42 */   private final List<RecipeButton> buttons = Lists.newArrayListWithCapacity(20);
/*     */   
/*     */   private RecipeButton hoveredButton;
/*     */   
/*     */   private final OverlayRecipeComponent overlay;
/*     */   
/*     */   private Minecraft minecraft;
/*     */   
/*     */   private final RecipeBookComponent<?> parent;
/*     */   
/*  51 */   private List<RecipeCollection> recipeCollections = (List<RecipeCollection>)ImmutableList.of();
/*     */   
/*     */   private ImageButton forwardButton;
/*     */   
/*     */   private ImageButton backButton;
/*     */   
/*     */   private int totalPages;
/*     */   
/*     */   private int currentPage;
/*     */   
/*     */   private ClientRecipeBook recipeBook;
/*     */   
/*     */   private RecipeDisplayId lastClickedRecipe;
/*     */   
/*     */   private RecipeCollection lastClickedRecipeCollection;
/*     */   
/*     */   private boolean isFiltering;
/*     */   
/*     */   public RecipeBookPage(RecipeBookComponent<?> parent, SlotSelectTime slotSelectTime, boolean isFurnaceMenu) {
/*  64 */     this.parent = parent;
/*  65 */     this.overlay = new OverlayRecipeComponent(slotSelectTime, isFurnaceMenu);
/*  67 */     for (int i = 0; i < 20; i++)
/*  68 */       this.buttons.add(new RecipeButton(slotSelectTime)); 
/*     */   }
/*     */   
/*     */   public void init(Minecraft minecraft, int xo, int yo) {
/*  73 */     this.minecraft = minecraft;
/*  74 */     this.recipeBook = minecraft.player.getRecipeBook();
/*  76 */     for (int i = 0; i < this.buttons.size(); i++)
/*  77 */       ((RecipeButton)this.buttons.get(i)).setPosition(xo + 11 + 25 * i % 5, yo + 31 + 25 * i / 5); 
/*  83 */     this.forwardButton = new ImageButton(xo + 93, yo + 137, 12, 17, PAGE_FORWARD_SPRITES, button -> updateArrowButtons(), NEXT_PAGE_TEXT);
/*  84 */     this.forwardButton.setTooltip(Tooltip.create(NEXT_PAGE_TEXT));
/*  85 */     this.backButton = new ImageButton(xo + 38, yo + 137, 12, 17, PAGE_BACKWARD_SPRITES, button -> updateArrowButtons(), PREVIOUS_PAGE_TEXT);
/*  86 */     this.backButton.setTooltip(Tooltip.create(PREVIOUS_PAGE_TEXT));
/*     */   }
/*     */   
/*     */   public void updateCollections(List<RecipeCollection> recipeCollections, boolean resetPage, boolean isFiltering) {
/*  90 */     this.recipeCollections = recipeCollections;
/*  91 */     this.isFiltering = isFiltering;
/*  92 */     this.totalPages = (int)Math.ceil(recipeCollections.size() / 20.0D);
/*  94 */     if (this.totalPages <= this.currentPage || resetPage)
/*  95 */       this.currentPage = 0; 
/*  98 */     updateButtonsForPage();
/*     */   }
/*     */   
/*     */   private void updateButtonsForPage() {
/* 102 */     int startOffset = 20 * this.currentPage;
/* 103 */     ContextMap context = SlotDisplayContext.fromLevel((Level)this.minecraft.level);
/* 104 */     for (int i = 0; i < this.buttons.size(); i++) {
/* 105 */       RecipeButton button = this.buttons.get(i);
/* 107 */       if (startOffset + i < this.recipeCollections.size()) {
/* 108 */         RecipeCollection recipeCollection = this.recipeCollections.get(startOffset + i);
/* 110 */         button.init(recipeCollection, this.isFiltering, this, context);
/* 112 */         button.visible = true;
/*     */       } else {
/* 114 */         button.visible = false;
/*     */       } 
/*     */     } 
/* 118 */     updateArrowButtons();
/*     */   }
/*     */   
/*     */   private void updateArrowButtons() {
/* 122 */     if (this.forwardButton != null)
/* 123 */       this.forwardButton.visible = (this.totalPages > 1 && this.currentPage < this.totalPages - 1); 
/* 125 */     if (this.backButton != null)
/* 126 */       this.backButton.visible = (this.totalPages > 1 && this.currentPage > 0); 
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int xo, int yo, int mouseX, int mouseY, float a) {
/* 131 */     if (this.totalPages > 1) {
/* 132 */       MutableComponent mutableComponent = Component.translatable("gui.recipebook.page", new Object[] { Integer.valueOf(this.currentPage + 1), Integer.valueOf(this.totalPages) });
/* 133 */       int pWidth = this.minecraft.font.width((FormattedText)mutableComponent);
/* 134 */       graphics.text(this.minecraft.font, (Component)mutableComponent, xo - pWidth / 2 + 73, yo + 141, -1);
/*     */     } 
/* 137 */     this.hoveredButton = null;
/* 138 */     for (RecipeButton recipeBookButton : this.buttons) {
/* 139 */       recipeBookButton.extractRenderState(graphics, mouseX, mouseY, a);
/* 140 */       if (recipeBookButton.visible && recipeBookButton.isHoveredOrFocused())
/* 141 */         this.hoveredButton = recipeBookButton; 
/*     */     } 
/* 145 */     if (this.forwardButton != null)
/* 146 */       this.forwardButton.extractRenderState(graphics, mouseX, mouseY, a); 
/* 148 */     if (this.backButton != null)
/* 149 */       this.backButton.extractRenderState(graphics, mouseX, mouseY, a); 
/* 152 */     graphics.nextStratum();
/* 153 */     this.overlay.extractRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   public void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/* 157 */     if (this.minecraft.gui.screen() != null && this.hoveredButton != null && !this.overlay.isVisible()) {
/* 158 */       ItemStack displayStack = this.hoveredButton.getDisplayStack();
/* 159 */       Identifier tooltipStyle = (Identifier)displayStack.get(DataComponents.TOOLTIP_STYLE);
/* 160 */       graphics.setComponentTooltipForNextFrame(this.minecraft.font, this.hoveredButton.getTooltipText(displayStack), mouseX, mouseY, tooltipStyle);
/*     */     } 
/*     */   }
/*     */   
/*     */   public RecipeDisplayId getLastClickedRecipe() {
/* 165 */     return this.lastClickedRecipe;
/*     */   }
/*     */   
/*     */   public RecipeCollection getLastClickedRecipeCollection() {
/* 169 */     return this.lastClickedRecipeCollection;
/*     */   }
/*     */   
/*     */   public void setInvisible() {
/* 173 */     this.overlay.setVisible(false);
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, int xo, int yo, int imageWidth, int imageHeight, boolean doubleClick) {
/* 177 */     this.lastClickedRecipe = null;
/* 178 */     this.lastClickedRecipeCollection = null;
/* 180 */     if (this.overlay.isVisible()) {
/* 181 */       if (this.overlay.mouseClicked(event, doubleClick)) {
/* 182 */         this.lastClickedRecipe = this.overlay.getLastRecipeClicked();
/* 183 */         this.lastClickedRecipeCollection = this.overlay.getRecipeCollection();
/*     */       } else {
/* 185 */         this.overlay.setVisible(false);
/*     */       } 
/* 188 */       return true;
/*     */     } 
/* 191 */     if (this.forwardButton.mouseClicked(event, doubleClick)) {
/* 192 */       this.currentPage++;
/* 193 */       updateButtonsForPage();
/* 194 */       return true;
/*     */     } 
/* 195 */     if (this.backButton.mouseClicked(event, doubleClick)) {
/* 196 */       this.currentPage--;
/* 197 */       updateButtonsForPage();
/* 198 */       return true;
/*     */     } 
/* 201 */     ContextMap context = SlotDisplayContext.fromLevel((Level)this.minecraft.level);
/* 202 */     for (RecipeButton button : this.buttons) {
/* 203 */       if (button.mouseClicked(event, doubleClick)) {
/* 204 */         if (event.button() == 0) {
/* 205 */           this.lastClickedRecipe = button.getCurrentRecipe();
/* 206 */           this.lastClickedRecipeCollection = button.getCollection();
/* 207 */         } else if (event.button() == 1 && 
/* 208 */           !this.overlay.isVisible() && !button.isOnlyOption()) {
/* 209 */           this.overlay.init(button.getCollection(), context, this.isFiltering, button.getX(), button.getY(), xo + imageWidth / 2, yo + 13 + imageHeight / 2, button.getWidth());
/*     */         } 
/* 212 */         return true;
/*     */       } 
/*     */     } 
/* 216 */     return false;
/*     */   }
/*     */   
/*     */   public void recipeShown(RecipeDisplayId recipe) {
/* 220 */     this.parent.recipeShown(recipe);
/*     */   }
/*     */   
/*     */   public ClientRecipeBook getRecipeBook() {
/* 224 */     return this.recipeBook;
/*     */   }
/*     */   
/*     */   protected void listButtons(Consumer<AbstractWidget> buttonConsumer) {
/* 228 */     this.buttons.forEach(buttonConsumer);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\recipebook\RecipeBookPage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */