/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ class SimpleType<T> implements DataComponentType<T> {
/*     */   private final Codec<T> codec;
/*     */   
/*     */   private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */   
/*     */   private final boolean ignoreSwapAnimation;
/*     */   
/*     */   private SimpleType(Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean ignoreSwapAnimation) {
/*  94 */     this.codec = codec;
/*  95 */     this.streamCodec = streamCodec;
/*  96 */     this.ignoreSwapAnimation = ignoreSwapAnimation;
/*     */   }
/*     */   
/*     */   public boolean ignoreSwapAnimation() {
/* 101 */     return this.ignoreSwapAnimation;
/*     */   }
/*     */   
/*     */   public Codec<T> codec() {
/* 106 */     return this.codec;
/*     */   }
/*     */   
/*     */   public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
/* 111 */     return this.streamCodec;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 116 */     return Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentType$Builder$SimpleType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */