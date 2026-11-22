/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class Builder<T> {
/*     */   private Codec<T> codec;
/*     */   
/*     */   private StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */   
/*     */   private boolean cacheEncoding;
/*     */   
/*     */   private boolean ignoreSwapAnimation;
/*     */   
/*     */   public Builder<T> persistent(Codec<T> codec) {
/*  56 */     this.codec = codec;
/*  57 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> networkSynchronized(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
/*  64 */     this.streamCodec = streamCodec;
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> cacheEncoding() {
/*  69 */     this.cacheEncoding = true;
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public DataComponentType<T> build() {
/*  74 */     StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec = Objects.<StreamCodec<? super RegistryFriendlyByteBuf, T>>requireNonNullElseGet(this.streamCodec, () -> ByteBufCodecs.fromCodecWithRegistries(Objects.<Codec>requireNonNull(this.codec, "Missing Codec for component")));
/*  78 */     Codec<T> cachingCodec = (this.cacheEncoding && this.codec != null) ? DataComponents.ENCODER_CACHE.wrap(this.codec) : this.codec;
/*  79 */     return new SimpleType<>(cachingCodec, streamCodec, this.ignoreSwapAnimation);
/*     */   }
/*     */   
/*     */   public Builder<T> ignoreSwapAnimation() {
/*  83 */     this.ignoreSwapAnimation = true;
/*  84 */     return this;
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
/*  94 */       this.codec = codec;
/*  95 */       this.streamCodec = streamCodec;
/*  96 */       this.ignoreSwapAnimation = ignoreSwapAnimation;
/*     */     }
/*     */     
/*     */     public boolean ignoreSwapAnimation() {
/* 101 */       return this.ignoreSwapAnimation;
/*     */     }
/*     */     
/*     */     public Codec<T> codec() {
/* 106 */       return this.codec;
/*     */     }
/*     */     
/*     */     public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
/* 111 */       return this.streamCodec;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 116 */       return Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentType$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */