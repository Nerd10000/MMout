/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public interface DataComponentType<T> {
/*  19 */   public static final Codec<DataComponentType<?>> CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec());
/*     */   
/*  20 */   public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentType<?>> STREAM_CODEC = StreamCodec.recursive(c -> ByteBufCodecs.registry(Registries.DATA_COMPONENT_TYPE));
/*     */   
/*     */   public static final Codec<DataComponentType<?>> PERSISTENT_CODEC;
/*     */   
/*     */   static {
/*  22 */     PERSISTENT_CODEC = CODEC.validate(type -> type.isTransient() ? DataResult.error(()) : DataResult.success(type));
/*     */   }
/*     */   
/*  25 */   public static final Codec<Map<DataComponentType<?>, Object>> VALUE_MAP_CODEC = Codec.dispatchedMap(PERSISTENT_CODEC, DataComponentType::codecOrThrow);
/*     */   
/*     */   static <T> Builder<T> builder() {
/*  28 */     return new Builder<>();
/*     */   }
/*     */   
/*     */   default Codec<T> codecOrThrow() {
/*  34 */     Codec<T> codec = codec();
/*  35 */     if (codec == null)
/*  36 */       throw new IllegalStateException(String.valueOf(this) + " is not a persistent component"); 
/*  38 */     return codec;
/*     */   }
/*     */   
/*     */   default boolean isTransient() {
/*  42 */     return (codec() == null);
/*     */   }
/*     */   
/*     */   Codec<T> codec();
/*     */   
/*     */   boolean ignoreSwapAnimation();
/*     */   
/*     */   StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();
/*     */   
/*     */   public static class Builder<T> {
/*     */     private Codec<T> codec;
/*     */     
/*     */     private StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */     
/*     */     private boolean cacheEncoding;
/*     */     
/*     */     private boolean ignoreSwapAnimation;
/*     */     
/*     */     public Builder<T> persistent(Codec<T> codec) {
/*  56 */       this.codec = codec;
/*  57 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> networkSynchronized(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
/*  64 */       this.streamCodec = streamCodec;
/*  65 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> cacheEncoding() {
/*  69 */       this.cacheEncoding = true;
/*  70 */       return this;
/*     */     }
/*     */     
/*     */     public DataComponentType<T> build() {
/*  74 */       StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec = Objects.<StreamCodec<? super RegistryFriendlyByteBuf, T>>requireNonNullElseGet(this.streamCodec, () -> ByteBufCodecs.fromCodecWithRegistries(Objects.<Codec>requireNonNull(this.codec, "Missing Codec for component")));
/*  78 */       Codec<T> cachingCodec = (this.cacheEncoding && this.codec != null) ? DataComponents.ENCODER_CACHE.wrap(this.codec) : this.codec;
/*  79 */       return new SimpleType<>(cachingCodec, streamCodec, this.ignoreSwapAnimation);
/*     */     }
/*     */     
/*     */     public Builder<T> ignoreSwapAnimation() {
/*  83 */       this.ignoreSwapAnimation = true;
/*  84 */       return this;
/*     */     }
/*     */     
/*     */     private static class SimpleType<T> implements DataComponentType<T> {
/*     */       private final Codec<T> codec;
/*     */       
/*     */       private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */       
/*     */       private final boolean ignoreSwapAnimation;
/*     */       
/*     */       private SimpleType(Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean ignoreSwapAnimation) {
/*  94 */         this.codec = codec;
/*  95 */         this.streamCodec = streamCodec;
/*  96 */         this.ignoreSwapAnimation = ignoreSwapAnimation;
/*     */       }
/*     */       
/*     */       public boolean ignoreSwapAnimation() {
/* 101 */         return this.ignoreSwapAnimation;
/*     */       }
/*     */       
/*     */       public Codec<T> codec() {
/* 106 */         return this.codec;
/*     */       }
/*     */       
/*     */       public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
/* 111 */         return this.streamCodec;
/*     */       }
/*     */       
/*     */       public String toString() {
/* 116 */         return Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SimpleType<T> implements DataComponentType<T> {
/*     */     private final Codec<T> codec;
/*     */     
/*     */     private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */     
/*     */     private final boolean ignoreSwapAnimation;
/*     */     
/*     */     private SimpleType(Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean ignoreSwapAnimation) {
/*     */       this.codec = codec;
/*     */       this.streamCodec = streamCodec;
/*     */       this.ignoreSwapAnimation = ignoreSwapAnimation;
/*     */     }
/*     */     
/*     */     public boolean ignoreSwapAnimation() {
/*     */       return this.ignoreSwapAnimation;
/*     */     }
/*     */     
/*     */     public Codec<T> codec() {
/*     */       return this.codec;
/*     */     }
/*     */     
/*     */     public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
/*     */       return this.streamCodec;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 116 */       return Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */