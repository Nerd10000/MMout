/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ class null extends RegistrySetBuilder.EmptyTagLookup<T> {
/*     */   null(HolderOwner<T> owner) {
/* 117 */     super(owner);
/*     */   }
/*     */   
/*     */   public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 120 */     return original.get(id);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */