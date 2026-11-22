/*     */ package net.minecraft.world;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.inventory.StackedContentsCompatible;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class SimpleContainer implements Container, StackedContentsCompatible {
/*     */   private final int size;
/*     */   
/*     */   private final NonNullList<ItemStack> items;
/*     */   
/*     */   public SimpleContainer(int size) {
/*  21 */     this.size = size;
/*  22 */     this.items = NonNullList.withSize(size, ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   public SimpleContainer(ItemStack... itemstacks) {
/*  26 */     this.size = itemstacks.length;
/*  27 */     this.items = NonNullList.of(ItemStack.EMPTY, (Object[])itemstacks);
/*     */   }
/*     */   
/*     */   public ItemStack getItem(int slot) {
/*  32 */     if (slot < 0 || slot >= this.items.size())
/*  33 */       return ItemStack.EMPTY; 
/*  35 */     return (ItemStack)this.items.get(slot);
/*     */   }
/*     */   
/*     */   public List<ItemStack> removeAllItems() {
/*  39 */     List<ItemStack> itemsRemoved = (List<ItemStack>)this.items.stream().filter(item -> !item.isEmpty()).collect(Collectors.toList());
/*  40 */     clearContent();
/*  41 */     return itemsRemoved;
/*     */   }
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  46 */     ItemStack result = ContainerHelper.removeItem((List<ItemStack>)this.items, slot, count);
/*  47 */     if (!result.isEmpty())
/*  48 */       setChanged(); 
/*  50 */     return result;
/*     */   }
/*     */   
/*     */   public ItemStack removeItemType(Item itemType, int count) {
/*  56 */     ItemStack removed = new ItemStack((ItemLike)itemType, 0);
/*  58 */     for (int slot = this.size - 1; slot >= 0; slot--) {
/*  59 */       ItemStack current = getItem(slot);
/*  60 */       if (current.getItem().equals(itemType)) {
/*  61 */         int stillNeeded = count - removed.getCount();
/*  62 */         ItemStack removedFromThisSlot = current.split(stillNeeded);
/*  63 */         removed.grow(removedFromThisSlot.getCount());
/*  64 */         if (removed.getCount() == count)
/*     */           break; 
/*     */       } 
/*     */     } 
/*  69 */     if (!removed.isEmpty())
/*  70 */       setChanged(); 
/*  72 */     return removed;
/*     */   }
/*     */   
/*     */   public ItemStack addItem(ItemStack itemStack) {
/*  78 */     if (itemStack.isEmpty())
/*  79 */       return ItemStack.EMPTY; 
/*  82 */     ItemStack remainingItems = itemStack.copy();
/*  84 */     moveItemToOccupiedSlotsWithSameType(remainingItems);
/*  85 */     if (remainingItems.isEmpty())
/*  86 */       return ItemStack.EMPTY; 
/*  89 */     moveItemToEmptySlots(remainingItems);
/*  90 */     if (remainingItems.isEmpty())
/*  91 */       return ItemStack.EMPTY; 
/*  94 */     return remainingItems;
/*     */   }
/*     */   
/*     */   public boolean canAddItem(ItemStack itemStack) {
/*  98 */     boolean hasSpace = false;
/*  99 */     for (ItemStack targetStack : this.items) {
/* 100 */       if (targetStack.isEmpty() || (ItemStack.isSameItemSameComponents(targetStack, itemStack) && targetStack.getCount() < targetStack.getMaxStackSize())) {
/* 101 */         hasSpace = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 105 */     return hasSpace;
/*     */   }
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/* 110 */     ItemStack itemStack = (ItemStack)this.items.get(slot);
/* 111 */     if (itemStack.isEmpty())
/* 112 */       return ItemStack.EMPTY; 
/* 114 */     this.items.set(slot, ItemStack.EMPTY);
/* 115 */     return itemStack;
/*     */   }
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/* 120 */     this.items.set(slot, itemStack);
/* 121 */     itemStack.limitSize(getMaxStackSize(itemStack));
/* 122 */     setChanged();
/*     */   }
/*     */   
/*     */   public void setChanged() {}
/*     */   
/*     */   public int getContainerSize() {
/* 131 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 136 */     for (ItemStack itemStack : this.items) {
/* 137 */       if (!itemStack.isEmpty())
/* 138 */         return false; 
/*     */     } 
/* 141 */     return true;
/*     */   }
/*     */   
/*     */   public boolean stillValid(Player player) {
/* 146 */     return true;
/*     */   }
/*     */   
/*     */   public void clearContent() {
/* 151 */     this.items.clear();
/* 152 */     setChanged();
/*     */   }
/*     */   
/*     */   public void fillStackedContents(StackedItemContents contents) {
/* 157 */     for (ItemStack itemStack : this.items)
/* 158 */       contents.accountStack(itemStack); 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 164 */     return this.items.stream()
/* 165 */       .filter(item -> !item.isEmpty())
/* 166 */       .toList()
/* 167 */       .toString();
/*     */   }
/*     */   
/*     */   private void moveItemToEmptySlots(ItemStack sourceStack) {
/* 171 */     for (int slot = 0; slot < this.size; slot++) {
/* 172 */       ItemStack targetStack = getItem(slot);
/* 173 */       if (targetStack.isEmpty()) {
/* 174 */         setItem(slot, sourceStack.copyAndClear());
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveItemToOccupiedSlotsWithSameType(ItemStack sourceStack) {
/* 181 */     for (int slot = 0; slot < this.size; slot++) {
/* 182 */       ItemStack targetStack = getItem(slot);
/* 183 */       if (ItemStack.isSameItemSameComponents(targetStack, sourceStack)) {
/* 184 */         moveItemsBetweenStacks(sourceStack, targetStack);
/* 185 */         if (sourceStack.isEmpty())
/*     */           return; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveItemsBetweenStacks(ItemStack sourceStack, ItemStack targetStack) {
/* 194 */     int maxCount = getMaxStackSize(targetStack);
/* 195 */     int diff = Math.min(sourceStack.getCount(), maxCount - targetStack.getCount());
/* 196 */     if (diff > 0) {
/* 197 */       targetStack.grow(diff);
/* 198 */       sourceStack.shrink(diff);
/* 199 */       setChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fromItemList(ValueInput.TypedInputList<ItemStack> items) {
/* 204 */     clearContent();
/* 206 */     for (ItemStack stack : items)
/* 207 */       addItem(stack); 
/*     */   }
/*     */   
/*     */   public void storeAsItemList(ValueOutput.TypedOutputList<ItemStack> output) {
/* 212 */     for (int i = 0; i < getContainerSize(); i++) {
/* 213 */       ItemStack itemStack = getItem(i);
/* 214 */       if (!itemStack.isEmpty())
/* 215 */         output.add(itemStack); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public NonNullList<ItemStack> getItems() {
/* 221 */     return this.items;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\SimpleContainer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */