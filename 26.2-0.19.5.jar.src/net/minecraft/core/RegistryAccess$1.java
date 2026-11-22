/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ class null implements RegistryAccess.Frozen {
/*    */   public <T> Optional<Registry<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
/* 83 */     Registry<Registry<T>> registry = registries;
/* 84 */     return (Optional)registry.getOptional(registryKey);
/*    */   }
/*    */   
/*    */   public Stream<RegistryAccess.RegistryEntry<?>> registries() {
/* 89 */     return registries.entrySet().stream().map(RegistryAccess.RegistryEntry::fromMapEntry);
/*    */   }
/*    */   
/*    */   public RegistryAccess.Frozen freeze() {
/* 94 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistryAccess$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */