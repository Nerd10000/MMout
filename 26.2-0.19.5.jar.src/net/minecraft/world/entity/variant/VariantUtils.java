/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class VariantUtils {
/*    */   public static final String TAG_VARIANT = "variant";
/*    */   
/*    */   public static <T> Holder<T> getDefaultOrAny(RegistryAccess registryAccess, ResourceKey<T> id) {
/* 23 */     Registry<T> registry = registryAccess.lookupOrThrow(id.registryKey());
/* 24 */     Objects.requireNonNull(registry);
/* 24 */     return registry.get(id).or(registry::getAny).orElseThrow();
/*    */   }
/*    */   
/*    */   public static <T> Holder<T> getAny(RegistryAccess registryAccess, ResourceKey<? extends Registry<T>> registryId) {
/* 28 */     return registryAccess.lookupOrThrow(registryId).getAny().orElseThrow();
/*    */   }
/*    */   
/*    */   public static <T> void writeVariant(ValueOutput output, Holder<T> holder) {
/* 33 */     holder.unwrapKey().ifPresent(k -> output.store("variant", Identifier.CODEC, k.identifier()));
/*    */   }
/*    */   
/*    */   public static <T> Optional<Holder<T>> readVariant(ValueInput input, ResourceKey<? extends Registry<T>> registryId) {
/* 40 */     Objects.requireNonNull(input.lookup());
/* 40 */     return input.read("variant", Identifier.CODEC).map(id -> ResourceKey.create(registryId, id)).flatMap(input.lookup()::get);
/*    */   }
/*    */   
/*    */   public static <T extends PriorityProvider<SpawnContext, ?>> Optional<Holder.Reference<T>> selectVariantToSpawn(SpawnContext context, ResourceKey<Registry<T>> variantRegistry) {
/* 44 */     ServerLevelAccessor level = context.level();
/* 45 */     Stream<Holder.Reference<T>> entries = level.registryAccess().lookupOrThrow(variantRegistry).listElements();
/* 46 */     return PriorityProvider.pick(entries, Holder::value, level.getRandom(), context);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\variant\VariantUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */