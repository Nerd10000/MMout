/*     */ package net.minecraft.advancements.predicates;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ class null implements StreamCodec<B, MinMaxBounds.Bounds<T>> {
/*     */   private static final int MIN_FLAG = 1;
/*     */   
/*     */   private static final int MAX_FLAG = 2;
/*     */   
/*     */   public MinMaxBounds.Bounds<T> decode(B input) {
/* 272 */     byte flags = input.readByte();
/* 273 */     Optional<T> min = ((flags & 0x1) != 0) ? Optional.<T>of((T)numberCodec.decode(input)) : Optional.<T>empty();
/* 274 */     Optional<T> max = ((flags & 0x2) != 0) ? Optional.<T>of((T)numberCodec.decode(input)) : Optional.<T>empty();
/* 275 */     return (MinMaxBounds.Bounds)new MinMaxBounds.Bounds<>((Optional)min, (Optional)max);
/*     */   }
/*     */   
/*     */   public void encode(B output, MinMaxBounds.Bounds<T> value) {
/* 280 */     Optional<T> min = value.min();
/* 281 */     Optional<T> max = value.max();
/* 282 */     output.writeByte((min.isPresent() ? 1 : 0) | (max.isPresent() ? 2 : 0));
/* 283 */     min.ifPresent(v -> numberCodec.encode(output, v));
/* 284 */     max.ifPresent(v -> numberCodec.encode(output, v));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\MinMaxBounds$Bounds$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */