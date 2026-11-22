/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.ListBuilder;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ public class DelegateListBuilder implements ListBuilder<T> {
/*     */   private final ListBuilder<T> original;
/*     */   
/*     */   protected DelegateListBuilder(ListBuilder<T> original) {
/* 233 */     this.original = original;
/*     */   }
/*     */   
/*     */   public DynamicOps<T> ops() {
/* 238 */     return DelegatingOps.this;
/*     */   }
/*     */   
/*     */   public DataResult<T> build(T prefix) {
/* 243 */     return this.original.build(prefix);
/*     */   }
/*     */   
/*     */   public ListBuilder<T> add(T value) {
/* 248 */     this.original.add(value);
/* 249 */     return this;
/*     */   }
/*     */   
/*     */   public ListBuilder<T> add(DataResult<T> value) {
/* 254 */     this.original.add(value);
/* 255 */     return this;
/*     */   }
/*     */   
/*     */   public <E> ListBuilder<T> add(E value, Encoder<E> encoder) {
/* 261 */     this.original.add(encoder.encodeStart(ops(), value));
/* 262 */     return this;
/*     */   }
/*     */   
/*     */   public <E> ListBuilder<T> addAll(Iterable<E> values, Encoder<E> encoder) {
/* 268 */     values.forEach(v -> this.original.add(encoder.encode(v, ops(), ops().empty())));
/* 269 */     return this;
/*     */   }
/*     */   
/*     */   public ListBuilder<T> withErrorsFrom(DataResult<?> result) {
/* 274 */     this.original.withErrorsFrom(result);
/* 275 */     return this;
/*     */   }
/*     */   
/*     */   public ListBuilder<T> mapError(UnaryOperator<String> onError) {
/* 280 */     this.original.mapError(onError);
/* 281 */     return this;
/*     */   }
/*     */   
/*     */   public DataResult<T> build(DataResult<T> prefix) {
/* 286 */     return this.original.build(prefix);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\DelegatingOps$DelegateListBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */