/*    */ package net.minecraft.world.item.trading;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponentExactPredicate;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public final class ItemCost extends Record {
/*    */   private final Holder<Item> item;
/*    */   
/*    */   private final int count;
/*    */   
/*    */   private final DataComponentExactPredicate components;
/*    */   
/*    */   private final ItemStack itemStack;
/*    */   
/*    */   public static final Codec<ItemCost> CODEC;
/*    */   
/*    */   public ItemCost(Holder<Item> item, int count, DataComponentExactPredicate components, ItemStack itemStack) {
/* 21 */     this.item = item;
/* 21 */     this.count = count;
/* 21 */     this.components = components;
/* 21 */     this.itemStack = itemStack;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/trading/ItemCost;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/trading/ItemCost;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/trading/ItemCost;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/trading/ItemCost;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/trading/ItemCost;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/trading/ItemCost;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<Item> item() {
/* 21 */     return this.item;
/*    */   }
/*    */   
/*    */   public int count() {
/* 21 */     return this.count;
/*    */   }
/*    */   
/*    */   public DataComponentExactPredicate components() {
/* 21 */     return this.components;
/*    */   }
/*    */   
/*    */   public ItemStack itemStack() {
/* 21 */     return this.itemStack;
/*    */   }
/*    */   
/*    */   static {
/* 23 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Item.CODEC.fieldOf("id").forGetter(ItemCost::item), (App)ExtraCodecs.optionalAlwaysPresentFieldOf(ExtraCodecs.POSITIVE_INT, "count", Integer.valueOf(1)).forGetter(ItemCost::count), (App)DataComponentExactPredicate.CODEC.optionalFieldOf("components", DataComponentExactPredicate.EMPTY).forGetter(ItemCost::components)).apply((Applicative)i, ItemCost::new));
/*    */   }
/*    */   
/* 29 */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemCost> STREAM_CODEC = StreamCodec.composite(Item.STREAM_CODEC, ItemCost::item, ByteBufCodecs.VAR_INT, ItemCost::count, DataComponentExactPredicate.STREAM_CODEC, ItemCost::components, ItemCost::new);
/*    */   
/* 35 */   public static final StreamCodec<RegistryFriendlyByteBuf, Optional<ItemCost>> OPTIONAL_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs::optional);
/*    */   
/*    */   public ItemCost(ItemLike item) {
/* 38 */     this(item, 1);
/*    */   }
/*    */   
/*    */   public ItemCost(ItemLike item, int count) {
/* 42 */     this((Holder<Item>)item.asItem().builtInRegistryHolder(), count, DataComponentExactPredicate.EMPTY);
/*    */   }
/*    */   
/*    */   public ItemCost(Holder<Item> item, int count, DataComponentExactPredicate components) {
/* 46 */     this(item, count, components, createStack(item, count, components));
/*    */   }
/*    */   
/*    */   public ItemCost withComponents(UnaryOperator<DataComponentExactPredicate.Builder> components) {
/* 50 */     return new ItemCost(this.item, this.count, ((DataComponentExactPredicate.Builder)components.apply(DataComponentExactPredicate.builder())).build());
/*    */   }
/*    */   
/*    */   private static ItemStack createStack(Holder<Item> item, int count, DataComponentExactPredicate components) {
/* 54 */     return new ItemStack(item, count, components.asPatch());
/*    */   }
/*    */   
/*    */   public boolean test(ItemStack itemStack) {
/* 58 */     return (itemStack.is(this.item) && this.components.test((DataComponentGetter)itemStack));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\trading\ItemCost.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */