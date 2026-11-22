/*    */ package net.minecraft.world.level.storage.loot.providers.nbt;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class NbtProviders {
/*    */   private static final Codec<NbtProvider> TYPED_CODEC;
/*    */   
/*    */   static {
/* 10 */     TYPED_CODEC = BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE.byNameCodec().dispatch(NbtProvider::codec, c -> c);
/*    */   }
/*    */   
/* 12 */   public static final Codec<NbtProvider> CODEC = Codec.lazyInitialized(() -> Codec.either(ContextNbtProvider.INLINE_CODEC, TYPED_CODEC).xmap(Either::unwrap, ()));
/*    */   
/*    */   public static MapCodec<? extends NbtProvider> bootstrap(Registry<MapCodec<? extends NbtProvider>> registry) {
/* 20 */     Registry.register(registry, "storage", StorageNbtProvider.MAP_CODEC);
/* 21 */     return (MapCodec<? extends NbtProvider>)Registry.register(registry, "context", ContextNbtProvider.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\nbt\NbtProviders.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */