/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderOwner;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ class UniversalLookup implements HolderGetter<Object>, HolderOwner<Object> {
/*     */   public Optional<Holder.Reference<Object>> get(ResourceKey<Object> id) {
/*  67 */     return Optional.of(getOrCreate(id));
/*     */   }
/*     */   
/*     */   public Holder.Reference<Object> getOrThrow(ResourceKey<Object> id) {
/*  72 */     return getOrCreate(id);
/*     */   }
/*     */   
/*     */   private Holder.Reference<Object> getOrCreate(ResourceKey<Object> id) {
/*  76 */     return PlaceholderLookupProvider.this.holders.computeIfAbsent(id, k -> Holder.Reference.createStandAlone(this, k));
/*     */   }
/*     */   
/*     */   public Optional<HolderSet.Named<Object>> get(TagKey<Object> id) {
/*  81 */     return Optional.of(getOrCreate(id));
/*     */   }
/*     */   
/*     */   public HolderSet.Named<Object> getOrThrow(TagKey<Object> id) {
/*  86 */     return getOrCreate(id);
/*     */   }
/*     */   
/*     */   private HolderSet.Named<Object> getOrCreate(TagKey<Object> id) {
/*  90 */     return PlaceholderLookupProvider.this.holderSets.computeIfAbsent(id, k -> HolderSet.emptyNamed(this, k));
/*     */   }
/*     */   
/*     */   public <T> HolderGetter<T> castAsLookup() {
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public <T> HolderOwner<T> castAsOwner() {
/* 100 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\PlaceholderLookupProvider$UniversalLookup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */