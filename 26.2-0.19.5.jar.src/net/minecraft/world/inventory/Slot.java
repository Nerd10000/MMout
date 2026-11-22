/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class Slot {
/*     */   private final int slot;
/*     */   
/*     */   public final Container container;
/*     */   
/*     */   public int index;
/*     */   
/*     */   public final int x;
/*     */   
/*     */   public final int y;
/*     */   
/*     */   public Slot(Container container, int slot, int x, int y) {
/*  20 */     this.container = container;
/*  21 */     this.slot = slot;
/*  22 */     this.x = x;
/*  23 */     this.y = y;
/*     */   }
/*     */   
/*     */   public void onQuickCraft(ItemStack picked, ItemStack original) {
/*  27 */     int count = original.getCount() - picked.getCount();
/*  28 */     if (count > 0)
/*  29 */       onQuickCraft(original, count); 
/*     */   }
/*     */   
/*     */   protected void onQuickCraft(ItemStack picked, int count) {}
/*     */   
/*     */   protected void onSwapCraft(int count) {}
/*     */   
/*     */   protected void checkTakeAchievements(ItemStack carried) {}
/*     */   
/*     */   public void onTake(Player player, ItemStack carried) {
/*  44 */     setChanged();
/*     */   }
/*     */   
/*     */   public boolean mayPlace(ItemStack itemStack) {
/*  48 */     return true;
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/*  52 */     return this.container.getItem(this.slot);
/*     */   }
/*     */   
/*     */   public boolean hasItem() {
/*  56 */     return !getItem().isEmpty();
/*     */   }
/*     */   
/*     */   public void setByPlayer(ItemStack itemStack) {
/*  62 */     setByPlayer(itemStack, getItem());
/*     */   }
/*     */   
/*     */   public void setByPlayer(ItemStack itemStack, ItemStack previous) {
/*  65 */     set(itemStack);
/*     */   }
/*     */   
/*     */   public void set(ItemStack itemStack) {
/*  74 */     this.container.setItem(this.slot, itemStack);
/*  75 */     setChanged();
/*     */   }
/*     */   
/*     */   public void setChanged() {
/*  79 */     this.container.setChanged();
/*     */   }
/*     */   
/*     */   public int getMaxStackSize() {
/*  83 */     return this.container.getMaxStackSize();
/*     */   }
/*     */   
/*     */   public int getMaxStackSize(ItemStack itemStack) {
/*  87 */     return Math.min(getMaxStackSize(), itemStack.getMaxStackSize());
/*     */   }
/*     */   
/*     */   public Identifier getNoItemIcon() {
/*  91 */     return null;
/*     */   }
/*     */   
/*     */   public ItemStack remove(int amount) {
/*  95 */     return this.container.removeItem(this.slot, amount);
/*     */   }
/*     */   
/*     */   public boolean mayPickup(Player player) {
/*  99 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 103 */     return true;
/*     */   }
/*     */   
/*     */   public Optional<ItemStack> tryRemove(int amount, int maxAmount, Player player) {
/* 108 */     if (!mayPickup(player))
/* 109 */       return Optional.empty(); 
/* 113 */     if (!allowModification(player) && maxAmount < getItem().getCount())
/* 114 */       return Optional.empty(); 
/* 117 */     amount = Math.min(amount, maxAmount);
/* 118 */     ItemStack result = remove(amount);
/* 119 */     if (result.isEmpty())
/* 120 */       return Optional.empty(); 
/* 122 */     if (getItem().isEmpty())
/* 123 */       setByPlayer(ItemStack.EMPTY, result); 
/* 125 */     return Optional.of(result);
/*     */   }
/*     */   
/*     */   public ItemStack safeTake(int amount, int maxAmount, Player player) {
/* 134 */     Optional<ItemStack> result = tryRemove(amount, maxAmount, player);
/* 135 */     result.ifPresent(item -> onTake(player, item));
/* 136 */     return result.orElse(ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   public ItemStack safeClone(Player player) {
/* 141 */     ItemStack item = getItem();
/* 142 */     return item.copyWithCount(item.getMaxStackSize());
/*     */   }
/*     */   
/*     */   public ItemStack safeInsert(ItemStack stack) {
/* 149 */     return safeInsert(stack, stack.getCount());
/*     */   }
/*     */   
/*     */   public ItemStack safeInsert(ItemStack inputStack, int inputAmount) {
/* 156 */     if (inputStack.isEmpty() || !mayPlace(inputStack))
/* 157 */       return inputStack; 
/* 160 */     ItemStack slotStack = getItem();
/* 161 */     int transferableItemCount = Math.min(Math.min(inputAmount, inputStack.getCount()), getMaxStackSize(inputStack) - slotStack.getCount());
/* 163 */     if (transferableItemCount <= 0)
/* 164 */       return inputStack; 
/* 165 */     if (slotStack.isEmpty()) {
/* 166 */       setByPlayer(inputStack.split(transferableItemCount));
/* 167 */     } else if (ItemStack.isSameItemSameComponents(slotStack, inputStack)) {
/* 168 */       inputStack.shrink(transferableItemCount);
/* 169 */       slotStack.grow(transferableItemCount);
/* 171 */       setByPlayer(slotStack);
/*     */     } 
/* 173 */     return inputStack;
/*     */   }
/*     */   
/*     */   public boolean allowModification(Player player) {
/* 178 */     return (mayPickup(player) && mayPlace(getItem()));
/*     */   }
/*     */   
/*     */   public int getContainerSlot() {
/* 182 */     return this.slot;
/*     */   }
/*     */   
/*     */   public boolean isHighlightable() {
/* 186 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isFake() {
/* 190 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\Slot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */