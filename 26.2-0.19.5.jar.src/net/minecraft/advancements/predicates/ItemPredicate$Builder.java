/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public class Builder {
/* 45 */   private Optional<HolderSet<Item>> items = Optional.empty();
/*    */   
/* 46 */   private MinMaxBounds.Ints count = MinMaxBounds.Ints.ANY;
/*    */   
/* 47 */   private DataComponentMatchers components = DataComponentMatchers.ANY;
/*    */   
/*    */   public static Builder item() {
/* 50 */     return new Builder();
/*    */   }
/*    */   
/*    */   public Builder of(HolderGetter<Item> lookup, ItemLike... items) {
/* 55 */     this.items = Optional.of(HolderSet.direct(i -> i.asItem().builtInRegistryHolder(), (Object[])items));
/* 56 */     return this;
/*    */   }
/*    */   
/*    */   public Builder of(HolderGetter<Item> lookup, TagKey<Item> tag) {
/* 60 */     this.items = Optional.of(lookup.getOrThrow(tag));
/* 61 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withCount(MinMaxBounds.Ints count) {
/* 65 */     this.count = count;
/* 66 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withComponents(DataComponentMatchers components) {
/* 70 */     this.components = components;
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   public ItemPredicate build() {
/* 75 */     return new ItemPredicate(this.items, this.count, this.components);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\ItemPredicate$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */