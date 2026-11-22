/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public interface Type<T extends DataComponentPredicate> {
/* 40 */   public static final Codec<Type<?>> CODEC = Codec.either(BuiltInRegistries.DATA_COMPONENT_PREDICATE_TYPE
/* 41 */       .byNameCodec(), BuiltInRegistries.DATA_COMPONENT_TYPE
/* 42 */       .byNameCodec())
/* 43 */     .xmap(Type::copyOrCreateType, Type::unpackType);
/*    */   
/* 48 */   public static final StreamCodec<RegistryFriendlyByteBuf, Type<?>> STREAM_CODEC = ByteBufCodecs.either(
/* 49 */       ByteBufCodecs.registry(Registries.DATA_COMPONENT_PREDICATE_TYPE), 
/* 50 */       ByteBufCodecs.registry(Registries.DATA_COMPONENT_TYPE))
/* 51 */     .map(Type::copyOrCreateType, Type::unpackType);
/*    */   
/*    */   private static <T extends Type<?>> Either<T, DataComponentType<?>> unpackType(T type) {
/* 57 */     DataComponentPredicate.AnyValueType anyCheck = (DataComponentPredicate.AnyValueType)type;
/* 57 */     return (type instanceof DataComponentPredicate.AnyValueType) ? Either.right(anyCheck.componentType()) : Either.left(type);
/*    */   }
/*    */   
/*    */   private static Type<?> copyOrCreateType(Either<Type<?>, DataComponentType<?>> concreteTypeOrComponent) {
/* 61 */     return (Type)concreteTypeOrComponent.map(concrete -> concrete, DataComponentPredicate.AnyValueType::create);
/*    */   }
/*    */   
/*    */   Codec<T> codec();
/*    */   
/*    */   MapCodec<DataComponentPredicate.Single<T>> wrappedCodec();
/*    */   
/*    */   StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate.Single<T>> singleStreamCodec();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\DataComponentPredicate$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */