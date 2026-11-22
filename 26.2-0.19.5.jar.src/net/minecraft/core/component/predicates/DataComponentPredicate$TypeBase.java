/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public abstract class TypeBase<T extends DataComponentPredicate> implements DataComponentPredicate.Type<T> {
/*    */   private final Codec<T> codec;
/*    */   
/*    */   private final MapCodec<DataComponentPredicate.Single<T>> wrappedCodec;
/*    */   
/*    */   private final StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate.Single<T>> singleStreamCodec;
/*    */   
/*    */   public TypeBase(Codec<T> codec) {
/* 77 */     this.codec = codec;
/* 78 */     this.wrappedCodec = DataComponentPredicate.Single.wrapCodec(this, codec);
/* 79 */     this.singleStreamCodec = ByteBufCodecs.fromCodecWithRegistries(codec).map(v -> new DataComponentPredicate.Single<>(this, (T)v), DataComponentPredicate.Single::predicate);
/*    */   }
/*    */   
/*    */   public Codec<T> codec() {
/* 84 */     return this.codec;
/*    */   }
/*    */   
/*    */   public MapCodec<DataComponentPredicate.Single<T>> wrappedCodec() {
/* 89 */     return this.wrappedCodec;
/*    */   }
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate.Single<T>> singleStreamCodec() {
/* 94 */     return this.singleStreamCodec;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\DataComponentPredicate$TypeBase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */