/*     */ package net.minecraft.commands;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderOwner;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ class null implements HolderLookup.RegistryLookup.Delegate<T> {
/*     */   public HolderLookup.RegistryLookup<T> parent() {
/* 531 */     return original;
/*     */   }
/*     */   
/*     */   public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 536 */     return Optional.of(getOrThrow(id));
/*     */   }
/*     */   
/*     */   public HolderSet.Named<T> getOrThrow(TagKey<T> id) {
/* 541 */     Optional<HolderSet.Named<T>> tag = parent().get(id);
/* 542 */     return tag.orElseGet(() -> HolderSet.emptyNamed((HolderOwner)parent(), id));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\Commands$2$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */