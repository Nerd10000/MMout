/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.recipebook.ServerPlaceRecipe;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*     */ import net.minecraft.world.item.crafting.SingleRecipeInput;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public abstract class AbstractFurnaceMenu extends RecipeBookMenu {
/*     */   public static final int INGREDIENT_SLOT = 0;
/*     */   
/*     */   public static final int FUEL_SLOT = 1;
/*     */   
/*     */   public static final int RESULT_SLOT = 2;
/*     */   
/*     */   public static final int SLOT_COUNT = 3;
/*     */   
/*     */   public static final int DATA_COUNT = 4;
/*     */   
/*     */   private static final int INV_SLOT_START = 3;
/*     */   
/*     */   private static final int INV_SLOT_END = 30;
/*     */   
/*     */   private static final int USE_ROW_SLOT_START = 30;
/*     */   
/*     */   private static final int USE_ROW_SLOT_END = 39;
/*     */   
/*     */   private final Container container;
/*     */   
/*     */   private final ContainerData data;
/*     */   
/*     */   protected final Level level;
/*     */   
/*     */   private final RecipePropertySet acceptedInputs;
/*     */   
/*     */   private final RecipeBookType recipeBookType;
/*     */   
/*     */   protected AbstractFurnaceMenu(MenuType<?> menuType, ResourceKey<RecipePropertySet> allowedInputs, RecipeBookType recipeBookType, int containerId, Inventory inventory) {
/*  46 */     this(menuType, allowedInputs, recipeBookType, containerId, inventory, (Container)new SimpleContainer(3), new SimpleContainerData(4));
/*     */   }
/*     */   
/*     */   protected AbstractFurnaceMenu(MenuType<?> menuType, ResourceKey<RecipePropertySet> allowedInputs, RecipeBookType recipeBookType, int containerId, Inventory inventory, Container container, ContainerData data) {
/*  50 */     super(menuType, containerId);
/*  51 */     this.recipeBookType = recipeBookType;
/*  52 */     checkContainerSize(container, 3);
/*  53 */     checkContainerDataCount(data, 4);
/*  54 */     this.container = container;
/*  55 */     this.data = data;
/*  56 */     this.level = inventory.player.level();
/*  58 */     this.acceptedInputs = this.level.recipeAccess().propertySet(allowedInputs);
/*  60 */     addSlot(new Slot(container, 0, 56, 17));
/*  61 */     addSlot(new FurnaceFuelSlot(this, container, 1, 56, 53));
/*  62 */     addSlot(new FurnaceResultSlot(inventory.player, container, 2, 116, 35));
/*  64 */     addStandardInventorySlots((Container)inventory, 8, 84);
/*  66 */     addDataSlots(data);
/*     */   }
/*     */   
/*     */   public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
/*  71 */     Container container = this.container;
/*  71 */     if (container instanceof StackedContentsCompatible) {
/*  71 */       StackedContentsCompatible stackedContentsCompatible = (StackedContentsCompatible)container;
/*  72 */       stackedContentsCompatible.fillStackedContents(stackedContents);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Slot getResultSlot() {
/*  77 */     return (Slot)this.slots.get(2);
/*     */   }
/*     */   
/*     */   public boolean stillValid(Player player) {
/*  82 */     return this.container.stillValid(player);
/*     */   }
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/*  87 */     ItemStack clicked = ItemStack.EMPTY;
/*  88 */     Slot slot = (Slot)this.slots.get(slotIndex);
/*  89 */     if (slot != null && slot.hasItem()) {
/*  90 */       ItemStack stack = slot.getItem();
/*  91 */       clicked = stack.copy();
/*  93 */       if (slotIndex == 2) {
/*  94 */         if (!moveItemStackTo(stack, 3, 39, true))
/*  95 */           return ItemStack.EMPTY; 
/*  97 */         slot.onQuickCraft(stack, clicked);
/*  98 */       } else if (slotIndex == 1 || slotIndex == 0) {
/*  99 */         if (!moveItemStackTo(stack, 3, 39, false))
/* 100 */           return ItemStack.EMPTY; 
/* 102 */       } else if (canSmelt(stack)) {
/* 103 */         if (!moveItemStackTo(stack, 0, 1, false))
/* 104 */           return ItemStack.EMPTY; 
/* 106 */       } else if (isFuel(stack)) {
/* 107 */         if (!moveItemStackTo(stack, 1, 2, false))
/* 108 */           return ItemStack.EMPTY; 
/* 110 */       } else if (slotIndex >= 3 && slotIndex < 30) {
/* 111 */         if (!moveItemStackTo(stack, 30, 39, false))
/* 112 */           return ItemStack.EMPTY; 
/* 114 */       } else if (slotIndex >= 30 && slotIndex < 39 && 
/* 115 */         !moveItemStackTo(stack, 3, 30, false)) {
/* 116 */         return ItemStack.EMPTY;
/*     */       } 
/* 119 */       if (stack.isEmpty()) {
/* 120 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 122 */         slot.setChanged();
/*     */       } 
/* 124 */       if (stack.getCount() == clicked.getCount())
/* 125 */         return ItemStack.EMPTY; 
/* 127 */       slot.onTake(player, stack);
/*     */     } 
/* 130 */     return clicked;
/*     */   }
/*     */   
/*     */   protected boolean canSmelt(ItemStack itemStack) {
/* 134 */     return this.acceptedInputs.test(itemStack);
/*     */   }
/*     */   
/*     */   protected boolean isFuel(ItemStack itemStack) {
/* 138 */     return this.level.fuelValues().isFuel(itemStack);
/*     */   }
/*     */   
/*     */   public float getBurnProgress() {
/* 142 */     int current = this.data.get(2);
/* 143 */     int total = this.data.get(3);
/* 144 */     if (total == 0 || current == 0)
/* 145 */       return 0.0F; 
/* 147 */     return Mth.clamp(current / total, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public float getLitProgress() {
/* 152 */     int litDuration = this.data.get(1);
/* 153 */     if (litDuration == 0)
/* 154 */       litDuration = 200; 
/* 156 */     return Mth.clamp(this.data.get(0) / litDuration, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean isLit() {
/* 160 */     return (this.data.get(0) > 0);
/*     */   }
/*     */   
/*     */   public RecipeBookType getRecipeBookType() {
/* 165 */     return this.recipeBookType;
/*     */   }
/*     */   
/*     */   public RecipeBookMenu.PostPlaceAction handlePlacement(boolean useMaxItems, boolean allowDroppingItemsToClear, RecipeHolder<?> recipe, final ServerLevel level, Inventory inventory) {
/* 173 */     final List<Slot> slotsToClear = List.of(getSlot(0), getSlot(2));
/* 174 */     RecipeHolder<AbstractCookingRecipe> typedRecipe = (RecipeHolder)recipe;
/* 175 */     return ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<AbstractCookingRecipe>() {
/*     */           public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
/* 179 */             AbstractFurnaceMenu.this.fillCraftSlotsStackedContents(stackedContents);
/*     */           }
/*     */           
/*     */           public void clearCraftingContent() {
/* 184 */             slotsToClear.forEach(s -> s.set(ItemStack.EMPTY));
/*     */           }
/*     */           
/*     */           public boolean recipeMatches(RecipeHolder<AbstractCookingRecipe> recipe) {
/* 189 */             return ((AbstractCookingRecipe)recipe.value()).matches(new SingleRecipeInput(AbstractFurnaceMenu.this.container.getItem(0)), (Level)level);
/*     */           }
/* 193 */         }1, 1, List.of(getSlot(0)), slotsToClear, inventory, typedRecipe, useMaxItems, allowDroppingItemsToClear);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\AbstractFurnaceMenu.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */