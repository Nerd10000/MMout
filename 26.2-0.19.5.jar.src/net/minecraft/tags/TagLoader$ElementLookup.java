/*     */ package net.minecraft.tags;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public interface ElementLookup<T> {
/*     */   static <T> ElementLookup<? extends Holder<T>> fromFrozenRegistry(Registry<T> registry) {
/* 193 */     return (id, required) -> registry.get(id);
/*     */   }
/*     */   
/*     */   static <T> ElementLookup<Holder<T>> fromWritableRegistry(WritableRegistry<T> registry) {
/* 197 */     return fromGetters(registry.key(), registry.createRegistrationLookup(), (HolderGetter<T>)registry);
/*     */   }
/*     */   
/*     */   static <T> ElementLookup<Holder<T>> fromGetters(ResourceKey<? extends Registry<T>> registryKey, HolderGetter<T> writable, HolderGetter<T> immutable) {
/* 202 */     return (id, required) -> (required ? writable : immutable).get(ResourceKey.create(registryKey, id));
/*     */   }
/*     */   
/*     */   Optional<? extends T> get(Identifier paramIdentifier, boolean paramBoolean);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\TagLoader$ElementLookup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */