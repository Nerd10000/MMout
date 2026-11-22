/*     */ package net.minecraft.commands;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderOwner;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ 
/*     */ class null implements CommandBuildContext {
/*     */   public FeatureFlagSet enabledFeatures() {
/* 513 */     return FeatureFlags.REGISTRY.allFlags();
/*     */   }
/*     */   
/*     */   public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/* 518 */     return registries.listRegistryKeys();
/*     */   }
/*     */   
/*     */   public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
/* 523 */     return registries.lookup(key).map(this::createLookup);
/*     */   }
/*     */   
/*     */   private <T> HolderLookup.RegistryLookup.Delegate<T> createLookup(final HolderLookup.RegistryLookup<T> original) {
/* 528 */     return new HolderLookup.RegistryLookup.Delegate<T>(this) {
/*     */         public HolderLookup.RegistryLookup<T> parent() {
/* 531 */           return original;
/*     */         }
/*     */         
/*     */         public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 536 */           return Optional.of(getOrThrow(id));
/*     */         }
/*     */         
/*     */         public HolderSet.Named<T> getOrThrow(TagKey<T> id) {
/* 541 */           Optional<HolderSet.Named<T>> tag = parent().get(id);
/* 542 */           return tag.orElseGet(() -> HolderSet.emptyNamed((HolderOwner)parent(), id));
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\Commands$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */