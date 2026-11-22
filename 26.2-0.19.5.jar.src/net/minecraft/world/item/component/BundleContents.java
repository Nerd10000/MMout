/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*     */ import org.apache.commons.lang3.math.Fraction;
/*     */ 
/*     */ public final class BundleContents implements TooltipComponent {
/*  28 */   public static final BundleContents EMPTY = new BundleContents(List.of());
/*     */   
/*     */   public static final Codec<BundleContents> CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, BundleContents> STREAM_CODEC;
/*     */   
/*     */   static {
/*  30 */     CODEC = ItemStackTemplate.CODEC.listOf().xmap(BundleContents::new, contents -> contents.items);
/*  31 */     STREAM_CODEC = ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list()).map(BundleContents::new, contents -> contents.items);
/*     */   }
/*     */   
/*  33 */   private static final Fraction BUNDLE_IN_BUNDLE_WEIGHT = Fraction.getFraction(1, 16);
/*     */   
/*     */   private static final int NO_STACK_INDEX = -1;
/*     */   
/*     */   public static final int NO_SELECTED_ITEM_INDEX = -1;
/*     */   
/*  36 */   public static final DataResult<Fraction> BEEHIVE_WEIGHT = DataResult.success(Fraction.ONE);
/*     */   
/*     */   private final List<ItemStackTemplate> items;
/*     */   
/*     */   private final int selectedItem;
/*     */   
/*     */   private final Supplier<DataResult<Fraction>> weight;
/*     */   
/*     */   private BundleContents(List<ItemStackTemplate> items, int selectedItem) {
/*  45 */     this.items = items;
/*  46 */     this.selectedItem = selectedItem;
/*  48 */     this.weight = (Supplier<DataResult<Fraction>>)Suppliers.memoize(() -> computeContentWeight((List)this.items));
/*     */   }
/*     */   
/*     */   public BundleContents(List<ItemStackTemplate> items) {
/*  52 */     this(items, -1);
/*     */   }
/*     */   
/*     */   private static DataResult<Fraction> computeContentWeight(List<? extends ItemInstance> items) {
/*     */     try {
/*  57 */       Fraction weight = Fraction.ZERO;
/*  58 */       for (ItemInstance stack : items) {
/*  59 */         DataResult<Fraction> itemWeight = getWeight(stack);
/*  60 */         if (itemWeight.isError())
/*  61 */           return itemWeight; 
/*  63 */         weight = weight.add(((Fraction)itemWeight.getOrThrow()).multiplyBy(Fraction.getFraction(stack.count(), 1)));
/*     */       } 
/*  65 */       return DataResult.success(weight);
/*  66 */     } catch (ArithmeticException exception) {
/*  67 */       return DataResult.error(() -> "Excessive total bundle weight");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static DataResult<Fraction> getWeight(ItemInstance item) {
/*  73 */     BundleContents bundle = (BundleContents)item.get(DataComponents.BUNDLE_CONTENTS);
/*  74 */     if (bundle != null)
/*  75 */       return bundle.weight().map(nestedWeight -> nestedWeight.add(BUNDLE_IN_BUNDLE_WEIGHT)); 
/*  77 */     List<BeehiveBlockEntity.Occupant> bees = ((Bees)item.getOrDefault(DataComponents.BEES, Bees.EMPTY)).bees();
/*  78 */     if (!bees.isEmpty())
/*  79 */       return BEEHIVE_WEIGHT; 
/*  81 */     return DataResult.success(Fraction.getFraction(1, item.getMaxStackSize()));
/*     */   }
/*     */   
/*     */   public static boolean canItemBeInBundle(ItemStack itemToAdd) {
/*  85 */     return (!itemToAdd.isEmpty() && itemToAdd.getItem().canFitInsideContainerItems());
/*     */   }
/*     */   
/*     */   public int getNumberOfItemsToShow() {
/*  89 */     int numberOfItemStacks = size();
/*  90 */     int availableItemsToShow = (numberOfItemStacks > 12) ? 11 : 12;
/*  91 */     int itemsOnNonFullRow = numberOfItemStacks % 4;
/*  92 */     int emptySpaceOnNonFullRow = (itemsOnNonFullRow == 0) ? 0 : (4 - itemsOnNonFullRow);
/*  93 */     return Math.min(numberOfItemStacks, availableItemsToShow - emptySpaceOnNonFullRow);
/*     */   }
/*     */   
/*     */   public Stream<ItemStack> itemCopyStream() {
/*  97 */     return this.items.stream().map(ItemStackTemplate::create);
/*     */   }
/*     */   
/*     */   public List<ItemStackTemplate> items() {
/* 101 */     return this.items;
/*     */   }
/*     */   
/*     */   public int size() {
/* 105 */     return this.items.size();
/*     */   }
/*     */   
/*     */   public DataResult<Fraction> weight() {
/* 109 */     return this.weight.get();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 113 */     return this.items.isEmpty();
/*     */   }
/*     */   
/*     */   public int getSelectedItemIndex() {
/* 117 */     return this.selectedItem;
/*     */   }
/*     */   
/*     */   public ItemStackTemplate getSelectedItem() {
/* 121 */     if (this.selectedItem == -1)
/* 122 */       return null; 
/* 124 */     return this.items.get(this.selectedItem);
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 129 */     if (this == obj)
/* 130 */       return true; 
/* 132 */     if (obj instanceof BundleContents) {
/* 132 */       BundleContents contents = (BundleContents)obj;
/* 133 */       return this.items.equals(contents.items);
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 140 */     return this.items.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 145 */     return "BundleContents" + String.valueOf(this.items);
/*     */   }
/*     */   
/*     */   public static class Mutable {
/*     */     private final List<ItemStack> items;
/*     */     
/*     */     private Fraction weight;
/*     */     
/*     */     private int selectedItem;
/*     */     
/*     */     public Mutable(BundleContents contents) {
/* 154 */       DataResult<Fraction> currentWeight = contents.weight.get();
/* 155 */       if (currentWeight.isError()) {
/* 157 */         this.items = new ArrayList<>();
/* 158 */         this.weight = Fraction.ZERO;
/* 159 */         this.selectedItem = -1;
/*     */       } else {
/* 161 */         this.items = new ArrayList<>(contents.items.size());
/* 162 */         for (ItemStackTemplate item : contents.items)
/* 163 */           this.items.add(item.create()); 
/* 165 */         this.weight = (Fraction)currentWeight.getOrThrow();
/* 166 */         this.selectedItem = contents.selectedItem;
/*     */       } 
/*     */     }
/*     */     
/*     */     public Mutable clearItems() {
/* 171 */       this.items.clear();
/* 172 */       this.weight = Fraction.ZERO;
/* 173 */       this.selectedItem = -1;
/* 174 */       return this;
/*     */     }
/*     */     
/*     */     private int findStackIndex(ItemStack itemsToAdd) {
/* 178 */       if (!itemsToAdd.isStackable())
/* 179 */         return -1; 
/* 181 */       for (int i = 0; i < this.items.size(); i++) {
/* 182 */         if (ItemStack.isSameItemSameComponents(this.items.get(i), itemsToAdd))
/* 183 */           return i; 
/*     */       } 
/* 186 */       return -1;
/*     */     }
/*     */     
/*     */     private int getMaxAmountToAdd(Fraction itemWeight) {
/* 190 */       Fraction remainingWeight = Fraction.ONE.subtract(this.weight);
/* 191 */       return Math.max(remainingWeight.divideBy(itemWeight).intValue(), 0);
/*     */     }
/*     */     
/*     */     public int tryInsert(ItemStack itemsToAdd) {
/* 196 */       if (!BundleContents.canItemBeInBundle(itemsToAdd))
/* 197 */         return 0; 
/* 200 */       DataResult<Fraction> maybeItemWeight = BundleContents.getWeight((ItemInstance)itemsToAdd);
/* 201 */       if (maybeItemWeight.isError())
/* 202 */         return 0; 
/* 204 */       Fraction itemWeight = (Fraction)maybeItemWeight.getOrThrow();
/* 206 */       int amountToAdd = Math.min(itemsToAdd.getCount(), getMaxAmountToAdd(itemWeight));
/* 207 */       if (amountToAdd == 0)
/* 208 */         return 0; 
/* 211 */       this.weight = this.weight.add(itemWeight.multiplyBy(Fraction.getFraction(amountToAdd, 1)));
/* 213 */       int stackIndex = findStackIndex(itemsToAdd);
/* 214 */       if (stackIndex != -1) {
/* 215 */         ItemStack removedStack = this.items.remove(stackIndex);
/* 216 */         ItemStack mergedStack = removedStack.copyWithCount(removedStack.getCount() + amountToAdd);
/* 217 */         itemsToAdd.shrink(amountToAdd);
/* 219 */         this.items.add(0, mergedStack);
/*     */       } else {
/* 221 */         this.items.add(0, itemsToAdd.split(amountToAdd));
/*     */       } 
/* 224 */       return amountToAdd;
/*     */     }
/*     */     
/*     */     public int tryTransfer(Slot slot, Player player) {
/* 228 */       ItemStack other = slot.getItem();
/* 230 */       DataResult<Fraction> itemWeight = BundleContents.getWeight((ItemInstance)other);
/* 231 */       if (itemWeight.isError())
/* 232 */         return 0; 
/* 235 */       int maxAmount = getMaxAmountToAdd((Fraction)itemWeight.getOrThrow());
/* 236 */       return BundleContents.canItemBeInBundle(other) ? tryInsert(slot.safeTake(other.getCount(), maxAmount, player)) : 0;
/*     */     }
/*     */     
/*     */     public void toggleSelectedItem(int selectedItem) {
/* 240 */       this.selectedItem = (this.selectedItem == selectedItem || indexIsOutsideAllowedBounds(selectedItem)) ? -1 : selectedItem;
/*     */     }
/*     */     
/*     */     private boolean indexIsOutsideAllowedBounds(int selectedItem) {
/* 244 */       return (selectedItem < 0 || selectedItem >= this.items.size());
/*     */     }
/*     */     
/*     */     public ItemStack removeOne() {
/* 248 */       if (this.items.isEmpty())
/* 249 */         return null; 
/* 251 */       int removeIndex = indexIsOutsideAllowedBounds(this.selectedItem) ? 0 : this.selectedItem;
/* 252 */       ItemStack stack = ((ItemStack)this.items.remove(removeIndex)).copy();
/* 254 */       this.weight = this.weight.subtract(((Fraction)BundleContents.getWeight((ItemInstance)stack).getOrThrow()).multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
/* 255 */       toggleSelectedItem(-1);
/* 256 */       return stack;
/*     */     }
/*     */     
/*     */     public Fraction weight() {
/* 260 */       return this.weight;
/*     */     }
/*     */     
/*     */     public BundleContents toImmutable() {
/* 264 */       ImmutableList.Builder<ItemStackTemplate> builder = ImmutableList.builder();
/* 265 */       for (ItemStack item : this.items)
/* 266 */         builder.add(ItemStackTemplate.fromNonEmptyStack(item)); 
/* 268 */       return new BundleContents((List<ItemStackTemplate>)builder.build(), this.selectedItem);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\BundleContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */