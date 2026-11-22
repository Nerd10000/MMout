/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ class null implements Codec.ResultFunction<A> {
/*     */   public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<A, T>> a) {
/* 243 */     MutableObject<String> message = new MutableObject();
/* 244 */     Objects.requireNonNull(message);
/* 244 */     Optional<Pair<A, T>> result = a.resultOrPartial(message::setValue);
/* 245 */     if (result.isPresent())
/* 246 */       return a; 
/* 248 */     return DataResult.error(() -> "(" + (String)message.get() + " -> using default)", Pair.of(value, input));
/*     */   }
/*     */   
/*     */   public <T> DataResult<T> coApply(DynamicOps<T> ops, A input, DataResult<T> t) {
/* 253 */     return t;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 258 */     return "OrElsePartial[" + String.valueOf(value) + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ExtraCodecs$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */