/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class ImmutableRegistryAccess implements RegistryAccess {
/*    */   private final Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>> registries;
/*    */   
/*    */   public ImmutableRegistryAccess(List<? extends Registry<?>> registries) {
/* 55 */     this.registries = (Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>>)registries.stream().collect(Collectors.toUnmodifiableMap(Registry::key, v -> v));
/*    */   }
/*    */   
/*    */   public ImmutableRegistryAccess(Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>> registries) {
/* 59 */     this.registries = Map.copyOf(registries);
/*    */   }
/*    */   
/*    */   public ImmutableRegistryAccess(Stream<RegistryAccess.RegistryEntry<?>> entries) {
/* 63 */     this.registries = entries.collect(ImmutableMap.toImmutableMap(RegistryAccess.RegistryEntry::key, RegistryAccess.RegistryEntry::value));
/*    */   }
/*    */   
/*    */   public <E> Optional<Registry<E>> lookup(ResourceKey<? extends Registry<? extends E>> registryKey) {
/* 69 */     return Optional.<Registry>ofNullable(this.registries.get(registryKey)).map(r -> r);
/*    */   }
/*    */   
/*    */   public Stream<RegistryAccess.RegistryEntry<?>> registries() {
/* 74 */     return this.registries.entrySet().stream().map(RegistryAccess.RegistryEntry::fromMapEntry);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistryAccess$ImmutableRegistryAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */