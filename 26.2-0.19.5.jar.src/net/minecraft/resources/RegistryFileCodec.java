/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderOwner;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public final class RegistryFileCodec<E> implements Codec<Holder<E>> {
/*    */   private final ResourceKey<? extends Registry<E>> registryKey;
/*    */   
/*    */   private final Codec<E> elementCodec;
/*    */   
/*    */   private final boolean allowInline;
/*    */   
/*    */   public static <E> RegistryFileCodec<E> create(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec) {
/* 24 */     return create(registryKey, elementCodec, true);
/*    */   }
/*    */   
/*    */   public static <E> RegistryFileCodec<E> create(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec, boolean allowInline) {
/* 28 */     return new RegistryFileCodec<>(registryKey, elementCodec, allowInline);
/*    */   }
/*    */   
/*    */   private RegistryFileCodec(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec, boolean allowInline) {
/* 32 */     this.registryKey = registryKey;
/* 33 */     this.elementCodec = elementCodec;
/* 34 */     this.allowInline = allowInline;
/*    */   }
/*    */   
/*    */   public <T> DataResult<T> encode(Holder<E> input, DynamicOps<T> ops, T prefix) {
/* 39 */     if (ops instanceof RegistryOps) {
/* 39 */       RegistryOps<?> registryOps = (RegistryOps)ops;
/* 40 */       Optional<HolderOwner<E>> maybeOwner = registryOps.owner(this.registryKey);
/* 41 */       if (maybeOwner.isPresent()) {
/* 42 */         if (!input.canSerializeIn(maybeOwner.get()))
/* 43 */           return DataResult.error(() -> "Element " + String.valueOf(input) + " is not valid in current registry set"); 
/* 45 */         return (DataResult<T>)input.unwrap().map(id -> Identifier.CODEC.encode(id.identifier(), ops, prefix), value -> this.elementCodec.encode(value, ops, prefix));
/*    */       } 
/*    */     } 
/* 52 */     return this.elementCodec.encode(input.value(), ops, prefix);
/*    */   }
/*    */   
/*    */   public <T> DataResult<Pair<Holder<E>, T>> decode(DynamicOps<T> ops, T input) {
/* 57 */     if (ops instanceof RegistryOps) {
/* 57 */       RegistryOps<?> registryOps = (RegistryOps)ops;
/* 58 */       Optional<HolderGetter<E>> maybeLookup = registryOps.getter(this.registryKey);
/* 59 */       if (maybeLookup.isEmpty())
/* 60 */         return DataResult.error(() -> "Registry does not exist: " + String.valueOf(this.registryKey)); 
/* 63 */       HolderGetter<E> lookup = maybeLookup.get();
/* 65 */       DataResult<Pair<Identifier, T>> decoded = Identifier.CODEC.decode(ops, input);
/* 66 */       if (decoded.result().isEmpty()) {
/* 67 */         if (!this.allowInline)
/* 68 */           return DataResult.error(() -> "Inline definitions not allowed here"); 
/* 70 */         return this.elementCodec.decode(ops, input).map(p -> p.mapFirst(Holder::direct));
/*    */       } 
/* 73 */       Pair<Identifier, T> pair = decoded.result().get();
/* 74 */       ResourceKey<E> elementKey = ResourceKey.create(this.registryKey, (Identifier)pair.getFirst());
/* 75 */       return ((DataResult)lookup.get(elementKey)
/* 76 */         .map(DataResult::success).orElseGet(() -> DataResult.error(())))
/* 77 */         .map(h -> Pair.of(h, pair.getSecond())).setLifecycle(Lifecycle.stable());
/*    */     } 
/* 79 */     return this.elementCodec.decode(ops, input).map(p -> p.mapFirst(Holder::direct));
/*    */   }
/*    */   
/*    */   public String toString() {
/* 84 */     return "RegistryFileCodec[" + String.valueOf(this.registryKey) + " " + String.valueOf(this.elementCodec) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\RegistryFileCodec.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */