/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ class UniversalLookup extends RegistrySetBuilder.EmptyTagLookup<Object> {
/*  99 */   private final Map<ResourceKey<Object>, Holder.Reference<Object>> holders = new HashMap<>();
/*     */   
/*     */   public UniversalLookup(HolderOwner<Object> owner) {
/* 102 */     super(owner);
/*     */   }
/*     */   
/*     */   public Optional<Holder.Reference<Object>> get(ResourceKey<Object> id) {
/* 107 */     return Optional.of(getOrCreate(id));
/*     */   }
/*     */   
/*     */   private <T> Holder.Reference<T> getOrCreate(ResourceKey<T> id) {
/* 112 */     return (Holder.Reference<T>)this.holders.computeIfAbsent(id, k -> Holder.Reference.createStandAlone(this.owner, k));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$UniversalLookup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */