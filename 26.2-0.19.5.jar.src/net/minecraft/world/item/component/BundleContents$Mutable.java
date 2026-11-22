/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import org.apache.commons.lang3.math.Fraction;
/*     */ 
/*     */ public class Mutable {
/*     */   private final List<ItemStack> items;
/*     */   
/*     */   private Fraction weight;
/*     */   
/*     */   private int selectedItem;
/*     */   
/*     */   public Mutable(BundleContents contents) {
/* 154 */     DataResult<Fraction> currentWeight = contents.weight.get();
/* 155 */     if (currentWeight.isError()) {
/* 157 */       this.items = new ArrayList<>();
/* 158 */       this.weight = Fraction.ZERO;
/* 159 */       this.selectedItem = -1;
/*     */     } else {
/* 161 */       this.items = new ArrayList<>(contents.items.size());
/* 162 */       for (ItemStackTemplate item : contents.items)
/* 163 */         this.items.add(item.create()); 
/* 165 */       this.weight = (Fraction)currentWeight.getOrThrow();
/* 166 */       this.selectedItem = contents.selectedItem;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Mutable clearItems() {
/* 171 */     this.items.clear();
/* 172 */     this.weight = Fraction.ZERO;
/* 173 */     this.selectedItem = -1;
/* 174 */     return this;
/*     */   }
/*     */   
/*     */   private int findStackIndex(ItemStack itemsToAdd) {
/* 178 */     if (!itemsToAdd.isStackable())
/* 179 */       return -1; 
/* 181 */     for (int i = 0; i < this.items.size(); i++) {
/* 182 */       if (ItemStack.isSameItemSameComponents(this.items.get(i), itemsToAdd))
/* 183 */         return i; 
/*     */     } 
/* 186 */     return -1;
/*     */   }
/*     */   
/*     */   private int getMaxAmountToAdd(Fraction itemWeight) {
/* 190 */     Fraction remainingWeight = Fraction.ONE.subtract(this.weight);
/* 191 */     return Math.max(remainingWeight.divideBy(itemWeight).intValue(), 0);
/*     */   }
/*     */   
/*     */   public int tryInsert(ItemStack itemsToAdd) {
/* 196 */     if (!BundleContents.canItemBeInBundle(itemsToAdd))
/* 197 */       return 0; 
/* 200 */     DataResult<Fraction> maybeItemWeight = BundleContents.getWeight((ItemInstance)itemsToAdd);
/* 201 */     if (maybeItemWeight.isError())
/* 202 */       return 0; 
/* 204 */     Fraction itemWeight = (Fraction)maybeItemWeight.getOrThrow();
/* 206 */     int amountToAdd = Math.min(itemsToAdd.getCount(), getMaxAmountToAdd(itemWeight));
/* 207 */     if (amountToAdd == 0)
/* 208 */       return 0; 
/* 211 */     this.weight = this.weight.add(itemWeight.multiplyBy(Fraction.getFraction(amountToAdd, 1)));
/* 213 */     int stackIndex = findStackIndex(itemsToAdd);
/* 214 */     if (stackIndex != -1) {
/* 215 */       ItemStack removedStack = this.items.remove(stackIndex);
/* 216 */       ItemStack mergedStack = removedStack.copyWithCount(removedStack.getCount() + amountToAdd);
/* 217 */       itemsToAdd.shrink(amountToAdd);
/* 219 */       this.items.add(0, mergedStack);
/*     */     } else {
/* 221 */       this.items.add(0, itemsToAdd.split(amountToAdd));
/*     */     } 
/* 224 */     return amountToAdd;
/*     */   }
/*     */   
/*     */   public int tryTransfer(Slot slot, Player player) {
/* 228 */     ItemStack other = slot.getItem();
/* 230 */     DataResult<Fraction> itemWeight = BundleContents.getWeight((ItemInstance)other);
/* 231 */     if (itemWeight.isError())
/* 232 */       return 0; 
/* 235 */     int maxAmount = getMaxAmountToAdd((Fraction)itemWeight.getOrThrow());
/* 236 */     return BundleContents.canItemBeInBundle(other) ? tryInsert(slot.safeTake(other.getCount(), maxAmount, player)) : 0;
/*     */   }
/*     */   
/*     */   public void toggleSelectedItem(int selectedItem) {
/* 240 */     this.selectedItem = (this.selectedItem == selectedItem || indexIsOutsideAllowedBounds(selectedItem)) ? -1 : selectedItem;
/*     */   }
/*     */   
/*     */   private boolean indexIsOutsideAllowedBounds(int selectedItem) {
/* 244 */     return (selectedItem < 0 || selectedItem >= this.items.size());
/*     */   }
/*     */   
/*     */   public ItemStack removeOne() {
/* 248 */     if (this.items.isEmpty())
/* 249 */       return null; 
/* 251 */     int removeIndex = indexIsOutsideAllowedBounds(this.selectedItem) ? 0 : this.selectedItem;
/* 252 */     ItemStack stack = ((ItemStack)this.items.remove(removeIndex)).copy();
/* 254 */     this.weight = this.weight.subtract(((Fraction)BundleContents.getWeight((ItemInstance)stack).getOrThrow()).multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
/* 255 */     toggleSelectedItem(-1);
/* 256 */     return stack;
/*     */   }
/*     */   
/*     */   public Fraction weight() {
/* 260 */     return this.weight;
/*     */   }
/*     */   
/*     */   public BundleContents toImmutable() {
/* 264 */     ImmutableList.Builder<ItemStackTemplate> builder = ImmutableList.builder();
/* 265 */     for (ItemStack item : this.items)
/* 266 */       builder.add(ItemStackTemplate.fromNonEmptyStack(item)); 
/* 268 */     return new BundleContents((List<ItemStackTemplate>)builder.build(), this.selectedItem);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\BundleContents$Mutable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */