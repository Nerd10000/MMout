/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class NumberProviders {
/*    */   private static final Codec<NumberProvider> TYPED_CODEC;
/*    */   
/*    */   public static final Codec<NumberProvider> CODEC;
/*    */   
/*    */   static {
/* 10 */     TYPED_CODEC = BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE.byNameCodec().dispatch(NumberProvider::codec, c -> c);
/* 12 */     CODEC = Codec.lazyInitialized(() -> {
/*    */           Codec<NumberProvider> typedCodecWithFallback = Codec.withAlternative(TYPED_CODEC, UniformGenerator.MAP_CODEC.codec());
/*    */           return Codec.either(ConstantValue.INLINE_CODEC, typedCodecWithFallback).xmap(Either::unwrap, ());
/*    */         });
/*    */   }
/*    */   
/*    */   public static MapCodec<? extends NumberProvider> bootstrap(Registry<MapCodec<? extends NumberProvider>> registry) {
/* 22 */     Registry.register(registry, "constant", ConstantValue.MAP_CODEC);
/* 23 */     Registry.register(registry, "uniform", UniformGenerator.MAP_CODEC);
/* 24 */     Registry.register(registry, "binomial", BinomialDistributionGenerator.MAP_CODEC);
/* 25 */     Registry.register(registry, "score", ScoreboardValue.MAP_CODEC);
/* 26 */     Registry.register(registry, "storage", StorageValue.MAP_CODEC);
/* 27 */     Registry.register(registry, "sum", Sum.MAP_CODEC);
/* 28 */     Registry.register(registry, "enchantment_level", EnchantmentLevelProvider.MAP_CODEC);
/* 29 */     return (MapCodec<? extends NumberProvider>)Registry.register(registry, "environment_attribute", EnvironmentAttributeValue.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\number\NumberProviders.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */