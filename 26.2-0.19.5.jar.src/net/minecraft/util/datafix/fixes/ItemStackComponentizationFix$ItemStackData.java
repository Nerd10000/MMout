/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ class ItemStackData {
/*     */   private final String item;
/*     */   
/*     */   private final int count;
/*     */   
/*     */   private Dynamic<?> components;
/*     */   
/*     */   private final Dynamic<?> remainder;
/*     */   
/*     */   private Dynamic<?> tag;
/*     */   
/*     */   private ItemStackData(String item, int count, Dynamic<?> remainder) {
/* 707 */     this.item = NamespacedSchema.ensureNamespaced(item);
/* 708 */     this.count = count;
/* 709 */     this.components = remainder.emptyMap();
/* 710 */     this.tag = remainder.get("tag").orElseEmptyMap();
/* 712 */     this.remainder = remainder.remove("tag");
/*     */   }
/*     */   
/*     */   public static Optional<ItemStackData> read(Dynamic<?> dynamic) {
/* 716 */     return dynamic.get("id").asString().apply2stable((item, count) -> new ItemStackData(item, count.intValue(), dynamic.remove("id").remove("Count")), dynamic
/*     */         
/* 718 */         .get("Count").asNumber())
/* 719 */       .result();
/*     */   }
/*     */   
/*     */   public OptionalDynamic<?> removeTag(String key) {
/* 723 */     OptionalDynamic<?> value = this.tag.get(key);
/* 724 */     this.tag = this.tag.remove(key);
/* 725 */     return value;
/*     */   }
/*     */   
/*     */   public void setComponent(String type, Dynamic<?> value) {
/* 729 */     this.components = this.components.set(type, value);
/*     */   }
/*     */   
/*     */   public void setComponent(String type, OptionalDynamic<?> optionalValue) {
/* 733 */     optionalValue.result().ifPresent(value -> this.components = this.components.set(type, value));
/*     */   }
/*     */   
/*     */   public Dynamic<?> moveTagInto(String fromKey, Dynamic<?> target, String toKey) {
/* 739 */     Optional<? extends Dynamic<?>> value = removeTag(fromKey).result();
/* 740 */     if (value.isPresent())
/* 741 */       return target.set(toKey, value.get()); 
/* 743 */     return target;
/*     */   }
/*     */   
/*     */   public void moveTagToComponent(String key, String type, Dynamic<?> defaultValue) {
/* 747 */     Optional<? extends Dynamic<?>> value = removeTag(key).result();
/* 748 */     if (value.isPresent() && !((Dynamic)value.get()).equals(defaultValue))
/* 749 */       setComponent(type, value.get()); 
/*     */   }
/*     */   
/*     */   public void moveTagToComponent(String key, String type) {
/* 754 */     removeTag(key).result().ifPresent(value -> setComponent(type, value));
/*     */   }
/*     */   
/*     */   public void fixSubTag(String key, boolean dontFixWhenFieldIsMissing, UnaryOperator<Dynamic<?>> function) {
/* 758 */     OptionalDynamic<?> value = this.tag.get(key);
/* 759 */     if (dontFixWhenFieldIsMissing && value.result().isEmpty())
/*     */       return; 
/* 762 */     Dynamic<?> map = value.orElseEmptyMap();
/* 763 */     map = function.apply(map);
/* 764 */     if (map.equals(map.emptyMap())) {
/* 765 */       this.tag = this.tag.remove(key);
/*     */     } else {
/* 767 */       this.tag = this.tag.set(key, map);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Dynamic<?> write() {
/* 774 */     Dynamic<?> result = this.tag.emptyMap().set("id", this.tag.createString(this.item)).set("count", this.tag.createInt(this.count));
/* 775 */     if (!this.tag.equals(this.tag.emptyMap()))
/* 776 */       this.components = this.components.set("minecraft:custom_data", this.tag); 
/* 778 */     if (!this.components.equals(this.tag.emptyMap()))
/* 779 */       result = result.set("components", this.components); 
/* 781 */     return mergeRemainder(result, this.remainder);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> mergeRemainder(Dynamic<T> itemStack, Dynamic<?> remainder) {
/* 785 */     DynamicOps<T> ops = itemStack.getOps();
/* 786 */     return ops.getMap(itemStack.getValue())
/* 787 */       .flatMap(itemStackMap -> ops.mergeToMap(remainder.convert(ops).getValue(), itemStackMap))
/* 788 */       .map(merged -> new Dynamic(ops, merged))
/* 789 */       .result().orElse(itemStack);
/*     */   }
/*     */   
/*     */   public boolean is(String id) {
/* 793 */     return this.item.equals(id);
/*     */   }
/*     */   
/*     */   public boolean is(Set<String> ids) {
/* 797 */     return ids.contains(this.item);
/*     */   }
/*     */   
/*     */   public boolean hasComponent(String id) {
/* 801 */     return this.components.get(id).result().isPresent();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ItemStackComponentizationFix$ItemStackData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */