/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.HolderSetCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.entity.player.StackedContents;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public final class Ingredient implements Predicate<ItemStack>, StackedContents.IngredientInfo<Holder<Item>> {
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, Ingredient> CONTENTS_STREAM_CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, Optional<Ingredient>> OPTIONAL_CONTENTS_STREAM_CODEC;
/*     */   
/*     */   static {
/*  28 */     CONTENTS_STREAM_CODEC = ByteBufCodecs.holderSet(Registries.ITEM).map(Ingredient::new, i -> i.values);
/*  31 */     OPTIONAL_CONTENTS_STREAM_CODEC = ByteBufCodecs.holderSet(Registries.ITEM).map(ingredient -> (ingredient.size() == 0) ? Optional.empty() : Optional.<Ingredient>of(new Ingredient(ingredient)), ingredient -> (HolderSet)ingredient.map(()).orElse(HolderSet.empty()));
/*     */   }
/*     */   
/*  36 */   public static final Codec<HolderSet<Item>> NON_AIR_HOLDER_SET_CODEC = HolderSetCodec.create(Registries.ITEM, Item.CODEC, false);
/*     */   
/*     */   public static final Codec<Ingredient> CODEC;
/*     */   
/*     */   private final HolderSet<Item> values;
/*     */   
/*     */   static {
/*  37 */     CODEC = ExtraCodecs.nonEmptyHolderSet(NON_AIR_HOLDER_SET_CODEC).xmap(Ingredient::new, i -> i.values);
/*     */   }
/*     */   
/*     */   private Ingredient(HolderSet<Item> values) {
/*  42 */     values.unwrap().ifRight(directValues -> {
/*     */           if (directValues.isEmpty())
/*     */             throw new UnsupportedOperationException("Ingredients can't be empty"); 
/*     */           if (directValues.contains(Items.AIR.builtInRegistryHolder()))
/*     */             throw new UnsupportedOperationException("Ingredient can't contain air"); 
/*     */         });
/*  51 */     this.values = values;
/*     */   }
/*     */   
/*     */   public static boolean testOptionalIngredient(Optional<Ingredient> ingredient, ItemStack stack) {
/*  55 */     Objects.requireNonNull(stack);
/*  55 */     return ((Boolean)ingredient.<Boolean>map(value -> Boolean.valueOf(value.test(stack))).orElseGet(stack::isEmpty)).booleanValue();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Stream<Holder<Item>> items() {
/*  61 */     return this.values.stream();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  65 */     return (this.values.size() == 0);
/*     */   }
/*     */   
/*     */   public boolean test(ItemStack input) {
/*  70 */     return input.is(this.values);
/*     */   }
/*     */   
/*     */   public boolean acceptsItem(Holder<Item> item) {
/*  75 */     return this.values.contains(item);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  80 */     if (o instanceof Ingredient) {
/*  80 */       Ingredient other = (Ingredient)o;
/*  81 */       return Objects.equals(this.values, other.values);
/*     */     } 
/*  83 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  88 */     return Objects.hashCode(this.values);
/*     */   }
/*     */   
/*     */   public static Ingredient of(ItemLike itemLike) {
/*  92 */     return new Ingredient((HolderSet<Item>)HolderSet.direct(new Holder[] { (Holder)itemLike.asItem().builtInRegistryHolder() }));
/*     */   }
/*     */   
/*     */   public static Ingredient of(ItemLike... items) {
/*  96 */     return of(Arrays.stream(items));
/*     */   }
/*     */   
/*     */   public static Ingredient of(Stream<? extends ItemLike> stream) {
/* 100 */     return new Ingredient((HolderSet<Item>)HolderSet.direct(stream.map(e -> e.asItem().builtInRegistryHolder()).toList()));
/*     */   }
/*     */   
/*     */   public static Ingredient of(HolderSet<Item> tag) {
/* 104 */     return new Ingredient(tag);
/*     */   }
/*     */   
/*     */   public SlotDisplay display() {
/* 108 */     return (SlotDisplay)this.values.unwrap().map(net.minecraft.world.item.crafting.display.SlotDisplay.TagSlotDisplay::new, l -> new SlotDisplay.Composite(l.stream().map(Ingredient::displayForSingleItem).toList()));
/*     */   }
/*     */   
/*     */   public static SlotDisplay optionalIngredientToDisplay(Optional<Ingredient> ingredient) {
/* 115 */     return (SlotDisplay)ingredient.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE);
/*     */   }
/*     */   
/*     */   private static SlotDisplay displayForSingleItem(Holder<Item> item) {
/* 119 */     SlotDisplay.ItemSlotDisplay itemSlotDisplay = new SlotDisplay.ItemSlotDisplay(item);
/* 121 */     ItemStackTemplate remainderStack = ((Item)item.value()).getCraftingRemainder();
/* 122 */     if (remainderStack != null) {
/* 123 */       SlotDisplay.ItemStackSlotDisplay itemStackSlotDisplay = new SlotDisplay.ItemStackSlotDisplay(remainderStack);
/* 124 */       return (SlotDisplay)new SlotDisplay.WithRemainder((SlotDisplay)itemSlotDisplay, (SlotDisplay)itemStackSlotDisplay);
/*     */     } 
/* 126 */     return (SlotDisplay)itemSlotDisplay;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\Ingredient.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */