/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ class null implements StreamCodec<B, T> {
/*     */   private final Supplier<StreamCodec<B, T>> inner;
/*     */   
/*     */   null() {
/* 543 */     this.inner = (Supplier<StreamCodec<B, T>>)Suppliers.memoize(() -> (StreamCodec)factory.apply(this));
/*     */   }
/*     */   
/*     */   public T decode(B input) {
/* 547 */     return ((StreamCodec<B, T>)this.inner.get()).decode(input);
/*     */   }
/*     */   
/*     */   public void encode(B output, T value) {
/* 552 */     ((StreamCodec<B, T>)this.inner.get()).encode(output, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$19.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */