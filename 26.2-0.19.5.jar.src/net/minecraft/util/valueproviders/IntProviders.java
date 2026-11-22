/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class IntProviders {
/*    */   private static final Codec<Either<Integer, IntProvider>> CONSTANT_OR_DISPATCH_CODEC;
/*    */   
/*    */   public static final Codec<IntProvider> CODEC;
/*    */   
/*    */   static {
/* 11 */     CONSTANT_OR_DISPATCH_CODEC = Codec.either((Codec)Codec.INT, BuiltInRegistries.INT_PROVIDER_TYPE
/*    */         
/* 13 */         .byNameCodec().dispatch(IntProvider::codec, t -> t));
/* 16 */     CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(either -> (IntProvider)either.map(ConstantInt::of, ()), f -> {
/*    */           ConstantInt constantInt = (ConstantInt)f;
/*    */           return (f instanceof ConstantInt) ? Either.left(Integer.valueOf(constantInt.value())) : Either.right(f);
/*    */         });
/*    */   }
/*    */   
/*    */   public static Codec<IntProvider> codec(int minValue, int maxValue) {
/* 22 */     return validateCodec(minValue, maxValue, CODEC);
/*    */   }
/*    */   
/*    */   public static <T extends IntProvider> Codec<T> validateCodec(int minValue, int maxValue, Codec<T> codec) {
/* 26 */     return codec.validate(value -> validate(minValue, maxValue, value));
/*    */   }
/*    */   
/*    */   private static <T extends IntProvider> DataResult<T> validate(int minValue, int maxValue, T value) {
/* 30 */     if (value.minInclusive() < minValue)
/* 31 */       return DataResult.error(() -> "Value provider too low: " + minValue + " [" + value.minInclusive() + "-" + value.maxInclusive() + "]"); 
/* 33 */     if (value.maxInclusive() > maxValue)
/* 34 */       return DataResult.error(() -> "Value provider too high: " + maxValue + " [" + value.minInclusive() + "-" + value.maxInclusive() + "]"); 
/* 36 */     return DataResult.success(value);
/*    */   }
/*    */   
/* 39 */   public static final Codec<IntProvider> NON_NEGATIVE_CODEC = codec(0, 2147483647);
/*    */   
/* 40 */   public static final Codec<IntProvider> POSITIVE_CODEC = codec(1, 2147483647);
/*    */   
/*    */   public static MapCodec<? extends IntProvider> bootstrap(Registry<MapCodec<? extends IntProvider>> registry) {
/* 43 */     Registry.register(registry, "constant", ConstantInt.MAP_CODEC);
/* 44 */     Registry.register(registry, "uniform", UniformInt.MAP_CODEC);
/* 45 */     Registry.register(registry, "biased_to_bottom", BiasedToBottomInt.MAP_CODEC);
/* 46 */     Registry.register(registry, "clamped", ClampedInt.MAP_CODEC);
/* 47 */     Registry.register(registry, "weighted_list", WeightedListInt.MAP_CODEC);
/* 48 */     Registry.register(registry, "clamped_normal", ClampedNormalInt.MAP_CODEC);
/* 49 */     return (MapCodec<? extends IntProvider>)Registry.register(registry, "trapezoid", TrapezoidInt.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\IntProviders.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */