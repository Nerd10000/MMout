/*    */ package net.minecraft.world.item.trading;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponentExactPredicate;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public final class TradeCost extends Record implements Validatable {
/*    */   private final Holder<Item> item;
/*    */   
/*    */   private final NumberProvider count;
/*    */   
/*    */   private final DataComponentExactPredicate components;
/*    */   
/*    */   public static final Codec<TradeCost> CODEC;
/*    */   
/*    */   public TradeCost(Holder<Item> item, NumberProvider count, DataComponentExactPredicate components) {
/* 18 */     this.item = item;
/* 18 */     this.count = count;
/* 18 */     this.components = components;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/trading/TradeCost;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/trading/TradeCost;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/trading/TradeCost;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/trading/TradeCost;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/trading/TradeCost;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/trading/TradeCost;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<Item> item() {
/* 18 */     return this.item;
/*    */   }
/*    */   
/*    */   public NumberProvider count() {
/* 18 */     return this.count;
/*    */   }
/*    */   
/*    */   public DataComponentExactPredicate components() {
/* 18 */     return this.components;
/*    */   }
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Item.CODEC.fieldOf("id").forGetter(TradeCost::item), (App)NumberProviders.CODEC.optionalFieldOf("count", ConstantValue.exactly(1.0F)).forGetter(TradeCost::count), (App)DataComponentExactPredicate.CODEC.optionalFieldOf("components", DataComponentExactPredicate.EMPTY).forGetter(TradeCost::components)).apply((Applicative)i, TradeCost::new));
/*    */   }
/*    */   
/*    */   public TradeCost(ItemLike item, int count) {
/* 26 */     this((Holder<Item>)item.asItem().builtInRegistryHolder(), (NumberProvider)ConstantValue.exactly(count), DataComponentExactPredicate.EMPTY);
/*    */   }
/*    */   
/*    */   public TradeCost(ItemLike item, NumberProvider count) {
/* 30 */     this((Holder<Item>)item.asItem().builtInRegistryHolder(), count, DataComponentExactPredicate.EMPTY);
/*    */   }
/*    */   
/*    */   public ItemCost toItemCost(LootContext lootContext, int additionalCost) {
/* 34 */     int count = Mth.clamp(count().getInt(lootContext) + additionalCost, 0, ((Item)item().value()).getDefaultMaxStackSize());
/* 35 */     return new ItemCost(item(), count, components());
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 40 */     Validatable.validate(context, "count", (Validatable)this.count);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\trading\TradeCost.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */