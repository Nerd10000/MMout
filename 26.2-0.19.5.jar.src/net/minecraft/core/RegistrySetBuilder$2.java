/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ class null extends RegistrySetBuilder.EmptyTagRegistryLookup<T> {
/*     */   null(HolderOwner<T> owner) {
/* 222 */     super(owner);
/*     */   }
/*     */   
/*     */   public ResourceKey<? extends Registry<? extends T>> key() {
/* 225 */     return key;
/*     */   }
/*     */   
/*     */   public Lifecycle registryLifecycle() {
/* 230 */     return lifecycle;
/*     */   }
/*     */   
/*     */   public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 235 */     return Optional.ofNullable((Holder.Reference<T>)entries.get(id));
/*     */   }
/*     */   
/*     */   public Stream<Holder.Reference<T>> listElements() {
/* 240 */     return entries.values().stream();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */