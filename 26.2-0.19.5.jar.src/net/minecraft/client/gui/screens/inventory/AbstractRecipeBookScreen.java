/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.navigation.ScreenPosition;
/*     */ import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
/*     */ import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.ContainerInput;
/*     */ import net.minecraft.world.inventory.RecipeBookMenu;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ 
/*     */ public abstract class AbstractRecipeBookScreen<T extends RecipeBookMenu> extends AbstractContainerScreen<T> implements RecipeUpdateListener {
/*     */   private final RecipeBookComponent<?> recipeBookComponent;
/*     */   
/*     */   private boolean widthTooNarrow;
/*     */   
/*     */   public AbstractRecipeBookScreen(T menu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component title) {
/*  23 */     super(menu, inventory, title);
/*  24 */     this.recipeBookComponent = recipeBookComponent;
/*     */   }
/*     */   
/*     */   protected void init() {
/*  29 */     super.init();
/*  30 */     this.widthTooNarrow = (this.width < 379);
/*  31 */     this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow);
/*  32 */     this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
/*  34 */     initButton();
/*     */   }
/*     */   
/*     */   private void initButton() {
/*  40 */     ScreenPosition buttonPos = getRecipeBookButtonPosition();
/*  41 */     addRenderableWidget((GuiEventListener)new ImageButton(buttonPos.x(), buttonPos.y(), 20, 18, RecipeBookComponent.RECIPE_BUTTON_SPRITES, button -> {
/*     */             this.recipeBookComponent.toggleVisibility();
/*     */             this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
/*     */             ScreenPosition updatedButtonPos = getRecipeBookButtonPosition();
/*     */             button.setPosition(updatedButtonPos.x(), updatedButtonPos.y());
/*     */             onRecipeBookButtonClick();
/*     */           }));
/*  48 */     addWidget((GuiEventListener)this.recipeBookComponent);
/*     */   }
/*     */   
/*     */   protected void onRecipeBookButtonClick() {}
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  56 */     if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
/*  57 */       extractBackground(graphics, mouseX, mouseY, a);
/*     */     } else {
/*  59 */       extractContents(graphics, mouseX, mouseY, a);
/*     */     } 
/*  61 */     graphics.nextStratum();
/*  62 */     this.recipeBookComponent.extractRenderState(graphics, mouseX, mouseY, a);
/*  64 */     graphics.nextStratum();
/*  65 */     extractCarriedItem(graphics, mouseX, mouseY);
/*  67 */     extractTooltip(graphics, mouseX, mouseY);
/*  68 */     this.recipeBookComponent.extractTooltip(graphics, mouseX, mouseY, this.hoveredSlot);
/*     */   }
/*     */   
/*     */   protected void extractSlots(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/*  73 */     super.extractSlots(graphics, mouseX, mouseY);
/*  74 */     this.recipeBookComponent.extractGhostRecipe(graphics, isBiggerResultSlot());
/*     */   }
/*     */   
/*     */   protected boolean isBiggerResultSlot() {
/*  78 */     return true;
/*     */   }
/*     */   
/*     */   public boolean charTyped(CharacterEvent event) {
/*  83 */     if (this.recipeBookComponent.charTyped(event))
/*  84 */       return true; 
/*  86 */     return super.charTyped(event);
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  91 */     if (this.recipeBookComponent.keyPressed(event))
/*  92 */       return true; 
/*  94 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*  99 */     if (this.recipeBookComponent.mouseClicked(event, doubleClick)) {
/* 100 */       setFocused((GuiEventListener)this.recipeBookComponent);
/* 101 */       return true;
/*     */     } 
/* 104 */     if (this.widthTooNarrow && this.recipeBookComponent.isVisible())
/* 105 */       return true; 
/* 108 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 113 */     if (this.recipeBookComponent.mouseDragged(event, dx, dy))
/* 114 */       return true; 
/* 116 */     return super.mouseDragged(event, dx, dy);
/*     */   }
/*     */   
/*     */   protected boolean isHovering(int left, int top, int w, int h, double xm, double ym) {
/* 121 */     return ((!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(left, top, w, h, xm, ym));
/*     */   }
/*     */   
/*     */   protected boolean hasClickedOutside(double mx, double my, int xo, int yo) {
/* 126 */     boolean clickedOutside = (mx < xo || my < yo || mx >= (xo + this.imageWidth) || my >= (yo + this.imageHeight));
/* 127 */     return (this.recipeBookComponent.hasClickedOutside(mx, my, this.leftPos, this.topPos, this.imageWidth, this.imageHeight) && clickedOutside);
/*     */   }
/*     */   
/*     */   protected void slotClicked(Slot slot, int slotId, int buttonNum, ContainerInput containerInput) {
/* 132 */     super.slotClicked(slot, slotId, buttonNum, containerInput);
/* 134 */     this.recipeBookComponent.slotClicked(slot);
/*     */   }
/*     */   
/*     */   public void containerTick() {
/* 139 */     super.containerTick();
/* 141 */     this.recipeBookComponent.tick();
/*     */   }
/*     */   
/*     */   public void recipesUpdated() {
/* 146 */     this.recipeBookComponent.recipesUpdated();
/*     */   }
/*     */   
/*     */   public void fillGhostRecipe(RecipeDisplay display) {
/* 151 */     this.recipeBookComponent.fillGhostRecipe(display);
/*     */   }
/*     */   
/*     */   protected abstract ScreenPosition getRecipeBookButtonPosition();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\AbstractRecipeBookScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */