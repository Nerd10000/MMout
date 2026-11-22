/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class FloatProviders {
/*    */   private static final Codec<Either<Float, FloatProvider>> CONSTANT_OR_DISPATCH_CODEC;
/*    */   
/*    */   public static final Codec<FloatProvider> CODEC;
/*    */   
/*    */   static {
/* 11 */     CONSTANT_OR_DISPATCH_CODEC = Codec.either((Codec)Codec.FLOAT, BuiltInRegistries.FLOAT_PROVIDER_TYPE
/*    */         
/* 13 */         .byNameCodec().dispatch(FloatProvider::codec, t -> t));
/* 15 */     CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(either -> (FloatProvider)either.map(ConstantFloat::of, ()), f -> {
/*    */           ConstantFloat constantFloat = (ConstantFloat)f;
/*    */           return (f instanceof ConstantFloat) ? Either.left(Float.valueOf(constantFloat.value())) : Either.right(f);
/*    */         });
/*    */   }
/*    */   
/*    */   public static Codec<FloatProvider> codec(float minValue, float maxValue) {
/* 21 */     return CODEC.validate(value -> (value.min() < minValue) ? DataResult.error(()) : ((value.max() > maxValue) ? DataResult.error(()) : DataResult.success(value)));
/*    */   }
/*    */   
/*    */   public static Codec<FloatProvider> codec(float minValue) {
/* 33 */     return CODEC.validate(value -> (value.min() < minValue) ? DataResult.error(()) : DataResult.success(value));
/*    */   }
/*    */   
/*    */   public static MapCodec<? extends FloatProvider> bootstrap(Registry<MapCodec<? extends FloatProvider>> registry) {
/* 42 */     Registry.register(registry, "constant", ConstantFloat.MAP_CODEC);
/* 43 */     Registry.register(registry, "uniform", UniformFloat.MAP_CODEC);
/* 44 */     Registry.register(registry, "clamped_normal", ClampedNormalFloat.MAP_CODEC);
/* 45 */     return (MapCodec<? extends FloatProvider>)Registry.register(registry, "trapezoid", TrapezoidFloat.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\FloatProviders.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */