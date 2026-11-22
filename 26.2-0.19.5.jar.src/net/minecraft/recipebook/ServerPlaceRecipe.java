/*     */ package net.minecraft.recipebook;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.inventory.RecipeBookMenu;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ 
/*     */ public class ServerPlaceRecipe<R extends Recipe<?>> {
/*     */   private static final int ITEM_NOT_FOUND = -1;
/*     */   
/*     */   private final Inventory inventory;
/*     */   
/*     */   private final CraftingMenuAccess<R> menu;
/*     */   
/*     */   private final boolean useMaxItems;
/*     */   
/*     */   private final int gridWidth;
/*     */   
/*     */   private final int gridHeight;
/*     */   
/*     */   private final List<Slot> inputGridSlots;
/*     */   
/*     */   private final List<Slot> slotsToClear;
/*     */   
/*     */   public static <I extends net.minecraft.world.item.crafting.RecipeInput, R extends Recipe<I>> RecipeBookMenu.PostPlaceAction placeRecipe(CraftingMenuAccess<R> menu, int gridWidth, int gridHeight, List<Slot> inputGridSlots, List<Slot> slotsToClear, Inventory inventory, RecipeHolder<R> recipe, boolean useMaxItems, boolean allowDroppingItemsToClear) {
/*  42 */     ServerPlaceRecipe<R> placer = (ServerPlaceRecipe)new ServerPlaceRecipe<>(menu, inventory, useMaxItems, gridWidth, gridHeight, inputGridSlots, slotsToClear);
/*  45 */     if (!allowDroppingItemsToClear && !placer.testClearGrid())
/*  46 */       return RecipeBookMenu.PostPlaceAction.NOTHING; 
/*  49 */     StackedItemContents availableItems = new StackedItemContents();
/*  50 */     inventory.fillStackedContents(availableItems);
/*  51 */     menu.fillCraftSlotsStackedContents(availableItems);
/*  53 */     return placer.tryPlaceRecipe(recipe, availableItems);
/*     */   }
/*     */   
/*     */   private ServerPlaceRecipe(CraftingMenuAccess<R> menu, Inventory inventory, boolean useMaxItems, int gridWidth, int gridHeight, List<Slot> inputGridSlots, List<Slot> slotsToClear) {
/*  57 */     this.menu = menu;
/*  58 */     this.inventory = inventory;
/*  59 */     this.useMaxItems = useMaxItems;
/*  60 */     this.gridWidth = gridWidth;
/*  61 */     this.gridHeight = gridHeight;
/*  62 */     this.inputGridSlots = inputGridSlots;
/*  63 */     this.slotsToClear = slotsToClear;
/*     */   }
/*     */   
/*     */   private RecipeBookMenu.PostPlaceAction tryPlaceRecipe(RecipeHolder<R> recipe, StackedItemContents availableItems) {
/*  67 */     if (availableItems.canCraft(recipe.value(), null)) {
/*  68 */       placeRecipe(recipe, availableItems);
/*  69 */       this.inventory.setChanged();
/*  70 */       return RecipeBookMenu.PostPlaceAction.NOTHING;
/*     */     } 
/*  72 */     clearGrid();
/*  73 */     this.inventory.setChanged();
/*  74 */     return RecipeBookMenu.PostPlaceAction.PLACE_GHOST_RECIPE;
/*     */   }
/*     */   
/*     */   private void clearGrid() {
/*  79 */     for (Slot slot : this.slotsToClear) {
/*  80 */       ItemStack itemStackCopy = slot.getItem().copy();
/*  81 */       this.inventory.placeItemBackInInventory(itemStackCopy, false);
/*  82 */       slot.set(itemStackCopy);
/*     */     } 
/*  84 */     this.menu.clearCraftingContent();
/*     */   }
/*     */   
/*     */   private void placeRecipe(RecipeHolder<R> recipe, StackedItemContents availableItems) {
/*  88 */     boolean recipeMatchesPlaced = this.menu.recipeMatches(recipe);
/*  89 */     int biggestCraftableStack = availableItems.getBiggestCraftableStack(recipe.value(), null);
/*  92 */     if (recipeMatchesPlaced)
/*  93 */       for (Slot inputSlot : this.inputGridSlots) {
/*  94 */         ItemStack itemStack = inputSlot.getItem();
/*  95 */         if (!itemStack.isEmpty() && Math.min(biggestCraftableStack, itemStack.getMaxStackSize()) < itemStack.getCount() + 1)
/*     */           return; 
/*     */       }  
/* 101 */     int amountToCraft = calculateAmountToCraft(biggestCraftableStack, recipeMatchesPlaced);
/* 102 */     List<Holder<Item>> itemsUsedPerIngredient = new ArrayList<>();
/* 103 */     Objects.requireNonNull(itemsUsedPerIngredient);
/* 103 */     if (!availableItems.canCraft(recipe.value(), amountToCraft, itemsUsedPerIngredient::add))
/*     */       return; 
/* 107 */     int adjustedAmountToCraft = clampToMaxStackSize(amountToCraft, itemsUsedPerIngredient);
/* 109 */     itemsUsedPerIngredient.clear();
/* 110 */     Objects.requireNonNull(itemsUsedPerIngredient);
/* 110 */     if (adjustedAmountToCraft != amountToCraft && !availableItems.canCraft(recipe.value(), adjustedAmountToCraft, itemsUsedPerIngredient::add))
/*     */       return; 
/* 115 */     clearGrid();
/* 116 */     PlaceRecipeHelper.placeRecipe(this.gridWidth, this.gridHeight, recipe.value(), (Iterable<?>)recipe.value().placementInfo().slotsToIngredientIndex(), (ingredientIndex, gridIndex, gridXPos, gridYPos) -> {
/*     */           if (ingredientIndex.intValue() == -1)
/*     */             return; 
/*     */           Slot targetGridSlot = this.inputGridSlots.get(gridIndex);
/*     */           Holder<Item> itemUsed = itemsUsedPerIngredient.get(ingredientIndex.intValue());
/*     */           int remainingCount = adjustedAmountToCraft;
/*     */           while (remainingCount > 0) {
/*     */             remainingCount = moveItemToGrid(targetGridSlot, itemUsed, remainingCount);
/*     */             if (remainingCount == -1)
/*     */               return; 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static int clampToMaxStackSize(int value, List<Holder<Item>> items) {
/* 135 */     for (Holder<Item> item : items)
/* 136 */       value = Math.min(value, ((Integer)item.components().getOrDefault(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1))).intValue()); 
/* 138 */     return value;
/*     */   }
/*     */   
/*     */   private int calculateAmountToCraft(int biggestCraftableStack, boolean recipeMatchesPlaced) {
/* 142 */     if (this.useMaxItems)
/* 143 */       return biggestCraftableStack; 
/* 146 */     if (recipeMatchesPlaced) {
/* 147 */       int smallestStackSize = Integer.MAX_VALUE;
/* 149 */       for (Slot inputSlot : this.inputGridSlots) {
/* 150 */         ItemStack itemStack = inputSlot.getItem();
/* 151 */         if (!itemStack.isEmpty() && smallestStackSize > itemStack.getCount())
/* 152 */           smallestStackSize = itemStack.getCount(); 
/*     */       } 
/* 157 */       if (smallestStackSize != Integer.MAX_VALUE)
/* 158 */         smallestStackSize++; 
/* 160 */       return smallestStackSize;
/*     */     } 
/* 163 */     return 1;
/*     */   }
/*     */   
/*     */   private int moveItemToGrid(Slot targetSlot, Holder<Item> itemInInventory, int count) {
/* 167 */     ItemStack takenStack, itemInTargetSlot = targetSlot.getItem();
/* 168 */     int inventorySlotId = this.inventory.findSlotMatchingCraftingIngredient(itemInInventory, itemInTargetSlot);
/* 169 */     if (inventorySlotId == -1)
/* 170 */       return -1; 
/* 172 */     ItemStack inventoryItem = this.inventory.getItem(inventorySlotId);
/* 175 */     if (count < inventoryItem.getCount()) {
/* 176 */       takenStack = this.inventory.removeItem(inventorySlotId, count);
/*     */     } else {
/* 178 */       takenStack = this.inventory.removeItemNoUpdate(inventorySlotId);
/*     */     } 
/* 181 */     int takenCount = takenStack.getCount();
/* 182 */     if (itemInTargetSlot.isEmpty()) {
/* 183 */       targetSlot.set(takenStack);
/*     */     } else {
/* 187 */       itemInTargetSlot.grow(takenCount);
/*     */     } 
/* 190 */     return count - takenCount;
/*     */   }
/*     */   
/*     */   private boolean testClearGrid() {
/* 194 */     List<ItemStack> freeSlots = Lists.newArrayList();
/* 195 */     int freeSlotsInInventory = getAmountOfFreeSlotsInInventory();
/* 197 */     for (Slot inputSlot : this.inputGridSlots) {
/* 198 */       ItemStack itemStack = inputSlot.getItem().copy();
/* 199 */       if (itemStack.isEmpty())
/*     */         continue; 
/* 203 */       int slotId = this.inventory.getSlotWithRemainingSpace(itemStack);
/* 204 */       if (slotId == -1 && freeSlots.size() <= freeSlotsInInventory) {
/* 205 */         for (ItemStack itemStackInList : freeSlots) {
/* 206 */           if (ItemStack.isSameItem(itemStackInList, itemStack) && itemStackInList.getCount() != itemStackInList.getMaxStackSize() && itemStackInList.getCount() + itemStack.getCount() <= itemStackInList.getMaxStackSize()) {
/* 207 */             itemStackInList.grow(itemStack.getCount());
/* 208 */             itemStack.setCount(0);
/*     */             break;
/*     */           } 
/*     */         } 
/* 213 */         if (!itemStack.isEmpty()) {
/* 214 */           if (freeSlots.size() < freeSlotsInInventory) {
/* 215 */             freeSlots.add(itemStack);
/*     */             continue;
/*     */           } 
/* 217 */           return false;
/*     */         } 
/*     */         continue;
/*     */       } 
/* 224 */       if (slotId == -1)
/* 225 */         return false; 
/*     */     } 
/* 229 */     return true;
/*     */   }
/*     */   
/*     */   private int getAmountOfFreeSlotsInInventory() {
/* 233 */     int freeSlots = 0;
/* 234 */     for (ItemStack item : this.inventory.getNonEquipmentItems()) {
/* 235 */       if (item.isEmpty())
/* 236 */         freeSlots++; 
/*     */     } 
/* 239 */     return freeSlots;
/*     */   }
/*     */   
/*     */   public static interface CraftingMenuAccess<T extends Recipe<?>> {
/*     */     void fillCraftSlotsStackedContents(StackedItemContents param1StackedItemContents);
/*     */     
/*     */     void clearCraftingContent();
/*     */     
/*     */     boolean recipeMatches(RecipeHolder<T> param1RecipeHolder);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\recipebook\ServerPlaceRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */