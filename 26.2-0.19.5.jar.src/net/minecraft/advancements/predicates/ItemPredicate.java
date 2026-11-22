/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public final class ItemPredicate extends Record implements Predicate<ItemInstance> {
/*    */   private final Optional<HolderSet<Item>> items;
/*    */   
/*    */   private final MinMaxBounds.Ints count;
/*    */   
/*    */   private final DataComponentMatchers components;
/*    */   
/*    */   public static final Codec<ItemPredicate> CODEC;
/*    */   
/*    */   public ItemPredicate(Optional<HolderSet<Item>> items, MinMaxBounds.Ints count, DataComponentMatchers components) {
/* 17 */     this.items = items;
/* 17 */     this.count = count;
/* 17 */     this.components = components;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/ItemPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/ItemPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/ItemPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/ItemPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/ItemPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/ItemPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<HolderSet<Item>> items() {
/* 17 */     return this.items;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints count() {
/* 17 */     return this.count;
/*    */   }
/*    */   
/*    */   public DataComponentMatchers components() {
/* 17 */     return this.components;
/*    */   }
/*    */   
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.ITEM).optionalFieldOf("items").forGetter(ItemPredicate::items), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("count", MinMaxBounds.Ints.ANY).forGetter(ItemPredicate::count), (App)DataComponentMatchers.CODEC.forGetter(ItemPredicate::components)).apply((Applicative)i, ItemPredicate::new));
/*    */   }
/*    */   
/*    */   public boolean test(ItemInstance itemStack) {
/* 30 */     if (this.items.isPresent() && !itemStack.is(this.items.get()))
/* 31 */       return false; 
/* 33 */     if (!this.count.matches(itemStack.count()))
/* 34 */       return false; 
/* 37 */     if (!this.components.test((DataComponentGetter)itemStack))
/* 38 */       return false; 
/* 41 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 45 */     private Optional<HolderSet<Item>> items = Optional.empty();
/*    */     
/* 46 */     private MinMaxBounds.Ints count = MinMaxBounds.Ints.ANY;
/*    */     
/* 47 */     private DataComponentMatchers components = DataComponentMatchers.ANY;
/*    */     
/*    */     public static Builder item() {
/* 50 */       return new Builder();
/*    */     }
/*    */     
/*    */     public Builder of(HolderGetter<Item> lookup, ItemLike... items) {
/* 55 */       this.items = Optional.of(HolderSet.direct(i -> i.asItem().builtInRegistryHolder(), (Object[])items));
/* 56 */       return this;
/*    */     }
/*    */     
/*    */     public Builder of(HolderGetter<Item> lookup, TagKey<Item> tag) {
/* 60 */       this.items = Optional.of(lookup.getOrThrow(tag));
/* 61 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withCount(MinMaxBounds.Ints count) {
/* 65 */       this.count = count;
/* 66 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withComponents(DataComponentMatchers components) {
/* 70 */       this.components = components;
/* 71 */       return this;
/*    */     }
/*    */     
/*    */     public ItemPredicate build() {
/* 75 */       return new ItemPredicate(this.items, this.count, this.components);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\ItemPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */