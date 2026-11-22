/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ class null implements RegistryOps.RegistryInfoLookup {
/*    */   public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
/* 42 */     return PlaceholderLookupProvider.this.context.lookup(registryKey)
/* 43 */       .map(RegistryOps.RegistryInfo::fromRegistryLookup)
/* 44 */       .or(() -> Optional.of(new RegistryOps.RegistryInfo(PlaceholderLookupProvider.this.lookup.castAsOwner(), PlaceholderLookupProvider.this.lookup.castAsLookup(), Lifecycle.experimental())));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\PlaceholderLookupProvider$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */