/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.ToIntFunction;
/*    */ 
/*    */ public class StringRepresentableCodec<S extends StringRepresentable> implements Codec<S> {
/*    */   private final Codec<S> codec;
/*    */   
/*    */   public StringRepresentableCodec(S[] valueArray, Function<String, S> nameResolver, ToIntFunction<S> idResolver) {
/* 28 */     this.codec = ExtraCodecs.orCompressed(
/* 29 */         Codec.stringResolver(StringRepresentable::getSerializedName, nameResolver), 
/* 30 */         ExtraCodecs.idResolverCodec(idResolver, i -> (i >= 0 && i < valueArray.length) ? valueArray[i] : null, -1));
/*    */   }
/*    */   
/*    */   public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T input) {
/* 36 */     return this.codec.decode(ops, input);
/*    */   }
/*    */   
/*    */   public <T> DataResult<T> encode(S input, DynamicOps<T> ops, T prefix) {
/* 41 */     return this.codec.encode(input, ops, prefix);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\StringRepresentable$StringRepresentableCodec.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */