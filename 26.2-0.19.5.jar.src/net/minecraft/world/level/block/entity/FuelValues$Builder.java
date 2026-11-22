/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntSortedMap;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class Builder {
/*     */   private final HolderLookup<Item> items;
/*     */   
/*     */   private final FeatureFlagSet enabledFeatures;
/*     */   
/* 123 */   private final Object2IntSortedMap<Item> values = (Object2IntSortedMap<Item>)new Object2IntLinkedOpenHashMap();
/*     */   
/*     */   public Builder(HolderLookup.Provider registries, FeatureFlagSet enabledFeatures) {
/* 126 */     this.items = (HolderLookup<Item>)registries.lookupOrThrow(Registries.ITEM);
/* 127 */     this.enabledFeatures = enabledFeatures;
/*     */   }
/*     */   
/*     */   public FuelValues build() {
/* 131 */     return new FuelValues(this.values);
/*     */   }
/*     */   
/*     */   public Builder remove(TagKey<Item> tag) {
/* 135 */     this.values.keySet().removeIf(item -> item.builtInRegistryHolder().is(tag));
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public Builder add(TagKey<Item> tag, int time) {
/* 140 */     this.items.get(tag).ifPresent(items -> {
/*     */           for (Holder<Item> item : (Iterable<Holder<Item>>)items)
/*     */             putInternal(time, (Item)item.value()); 
/*     */         });
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public Builder add(ItemLike itemLike, int time) {
/* 149 */     Item item = itemLike.asItem();
/* 150 */     putInternal(time, item);
/* 151 */     return this;
/*     */   }
/*     */   
/*     */   private void putInternal(int time, Item item) {
/* 155 */     if (item.isEnabled(this.enabledFeatures))
/* 156 */       this.values.put(item, time); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\FuelValues$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */