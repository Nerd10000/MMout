/*     */ package net.minecraft.core.component.predicates;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ public interface DataComponentPredicate {
/*  22 */   public static final Codec<Map<Type<?>, DataComponentPredicate>> CODEC = Codec.dispatchedMap(Type.CODEC, Type::codec);
/*     */   
/*     */   static MapCodec<Single<?>> singleCodec(String name) {
/*  25 */     return Type.CODEC.dispatchMap(name, Single::type, Type::wrappedCodec);
/*     */   }
/*     */   
/*  28 */   public static final StreamCodec<RegistryFriendlyByteBuf, Single<?>> SINGLE_STREAM_CODEC = Type.STREAM_CODEC.dispatch(Single::type, Type::singleStreamCodec);
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, Map<Type<?>, DataComponentPredicate>> STREAM_CODEC;
/*     */   
/*     */   static {
/*  32 */     STREAM_CODEC = SINGLE_STREAM_CODEC.apply(ByteBufCodecs.list(64)).map(singles -> (Map)singles.stream().collect(Collectors.toMap(Single::type, Single::predicate)), map -> map.entrySet().stream().map(Single::fromEntry).toList());
/*     */   }
/*     */   
/*     */   boolean matches(DataComponentGetter paramDataComponentGetter);
/*     */   
/*     */   public static interface Type<T extends DataComponentPredicate> {
/*  40 */     public static final Codec<Type<?>> CODEC = Codec.either(BuiltInRegistries.DATA_COMPONENT_PREDICATE_TYPE
/*  41 */         .byNameCodec(), BuiltInRegistries.DATA_COMPONENT_TYPE
/*  42 */         .byNameCodec())
/*  43 */       .xmap(Type::copyOrCreateType, Type::unpackType);
/*     */     
/*  48 */     public static final StreamCodec<RegistryFriendlyByteBuf, Type<?>> STREAM_CODEC = ByteBufCodecs.either(
/*  49 */         ByteBufCodecs.registry(Registries.DATA_COMPONENT_PREDICATE_TYPE), 
/*  50 */         ByteBufCodecs.registry(Registries.DATA_COMPONENT_TYPE))
/*  51 */       .map(Type::copyOrCreateType, Type::unpackType);
/*     */     
/*     */     private static <T extends Type<?>> Either<T, DataComponentType<?>> unpackType(T type) {
/*  57 */       DataComponentPredicate.AnyValueType anyCheck = (DataComponentPredicate.AnyValueType)type;
/*  57 */       return (type instanceof DataComponentPredicate.AnyValueType) ? Either.right(anyCheck.componentType()) : Either.left(type);
/*     */     }
/*     */     
/*     */     private static Type<?> copyOrCreateType(Either<Type<?>, DataComponentType<?>> concreteTypeOrComponent) {
/*  61 */       return (Type)concreteTypeOrComponent.map(concrete -> concrete, DataComponentPredicate.AnyValueType::create);
/*     */     }
/*     */     
/*     */     Codec<T> codec();
/*     */     
/*     */     MapCodec<DataComponentPredicate.Single<T>> wrappedCodec();
/*     */     
/*     */     StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate.Single<T>> singleStreamCodec();
/*     */   }
/*     */   
/*     */   public static abstract class TypeBase<T extends DataComponentPredicate> implements Type<T> {
/*     */     private final Codec<T> codec;
/*     */     
/*     */     private final MapCodec<DataComponentPredicate.Single<T>> wrappedCodec;
/*     */     
/*     */     private final StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate.Single<T>> singleStreamCodec;
/*     */     
/*     */     public TypeBase(Codec<T> codec) {
/*  77 */       this.codec = codec;
/*  78 */       this.wrappedCodec = DataComponentPredicate.Single.wrapCodec(this, codec);
/*  79 */       this.singleStreamCodec = ByteBufCodecs.fromCodecWithRegistries(codec).map(v -> new DataComponentPredicate.Single<>(this, (T)v), DataComponentPredicate.Single::predicate);
/*     */     }
/*     */     
/*     */     public Codec<T> codec() {
/*  84 */       return this.codec;
/*     */     }
/*     */     
/*     */     public MapCodec<DataComponentPredicate.Single<T>> wrappedCodec() {
/*  89 */       return this.wrappedCodec;
/*     */     }
/*     */     
/*     */     public StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate.Single<T>> singleStreamCodec() {
/*  94 */       return this.singleStreamCodec;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ConcreteType<T extends DataComponentPredicate> extends TypeBase<T> {
/*     */     public ConcreteType(Codec<T> codec) {
/* 100 */       super(codec);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class AnyValueType extends TypeBase<AnyValue> {
/*     */     private final AnyValue predicate;
/*     */     
/*     */     public AnyValueType(AnyValue predicate) {
/* 108 */       super(MapCodec.unitCodec(predicate));
/* 109 */       this.predicate = predicate;
/*     */     }
/*     */     
/*     */     public AnyValue predicate() {
/* 113 */       return this.predicate;
/*     */     }
/*     */     
/*     */     public DataComponentType<?> componentType() {
/* 117 */       return this.predicate.type();
/*     */     }
/*     */     
/*     */     public static AnyValueType create(DataComponentType<?> componentType) {
/* 121 */       return new AnyValueType(new AnyValue(componentType));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Single<T extends DataComponentPredicate> extends Record {
/*     */     private final DataComponentPredicate.Type<T> type;
/*     */     
/*     */     private final T predicate;
/*     */     
/*     */     public Single(DataComponentPredicate.Type<T> type, T predicate) {
/* 125 */       this.type = type;
/* 125 */       this.predicate = predicate;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single<TT;>;
/*     */     }
/*     */     
/*     */     public DataComponentPredicate.Type<T> type() {
/* 125 */       return this.type;
/*     */     }
/*     */     
/*     */     public T predicate() {
/* 125 */       return this.predicate;
/*     */     }
/*     */     
/*     */     private static <T extends DataComponentPredicate> MapCodec<Single<T>> wrapCodec(DataComponentPredicate.Type<T> type, Codec<T> codec) {
/* 130 */       return RecordCodecBuilder.mapCodec(i -> i.group((App)codec.fieldOf("value").forGetter(Single::predicate)).apply((Applicative)i, ()));
/*     */     }
/*     */     
/*     */     private static <T extends DataComponentPredicate> Single<T> fromEntry(Map.Entry<DataComponentPredicate.Type<?>, T> e) {
/* 137 */       return new Single<>((DataComponentPredicate.Type<T>)e.getKey(), e.getValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\DataComponentPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */