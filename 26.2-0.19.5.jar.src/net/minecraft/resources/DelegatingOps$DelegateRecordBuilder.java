/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ public class DelegateRecordBuilder implements RecordBuilder<T> {
/*     */   private final RecordBuilder<T> original;
/*     */   
/*     */   protected DelegateRecordBuilder(RecordBuilder<T> original) {
/* 303 */     this.original = original;
/*     */   }
/*     */   
/*     */   public DynamicOps<T> ops() {
/* 308 */     return DelegatingOps.this;
/*     */   }
/*     */   
/*     */   public RecordBuilder<T> add(T key, T value) {
/* 313 */     this.original.add(key, value);
/* 314 */     return this;
/*     */   }
/*     */   
/*     */   public RecordBuilder<T> add(T key, DataResult<T> value) {
/* 319 */     this.original.add(key, value);
/* 320 */     return this;
/*     */   }
/*     */   
/*     */   public RecordBuilder<T> add(DataResult<T> key, DataResult<T> value) {
/* 325 */     this.original.add(key, value);
/* 326 */     return this;
/*     */   }
/*     */   
/*     */   public RecordBuilder<T> add(String key, T value) {
/* 331 */     this.original.add(key, value);
/* 332 */     return this;
/*     */   }
/*     */   
/*     */   public RecordBuilder<T> add(String key, DataResult<T> value) {
/* 337 */     this.original.add(key, value);
/* 338 */     return this;
/*     */   }
/*     */   
/*     */   public <E> RecordBuilder<T> add(String key, E value, Encoder<E> encoder) {
/* 344 */     return this.original.add(key, encoder.encodeStart(ops(), value));
/*     */   }
/*     */   
/*     */   public RecordBuilder<T> withErrorsFrom(DataResult<?> result) {
/* 349 */     this.original.withErrorsFrom(result);
/* 350 */     return this;
/*     */   }
/*     */   
/*     */   public RecordBuilder<T> setLifecycle(Lifecycle lifecycle) {
/* 355 */     this.original.setLifecycle(lifecycle);
/* 356 */     return this;
/*     */   }
/*     */   
/*     */   public RecordBuilder<T> mapError(UnaryOperator<String> onError) {
/* 361 */     this.original.mapError(onError);
/* 362 */     return this;
/*     */   }
/*     */   
/*     */   public DataResult<T> build(T prefix) {
/* 367 */     return this.original.build(prefix);
/*     */   }
/*     */   
/*     */   public DataResult<T> build(DataResult<T> prefix) {
/* 372 */     return this.original.build(prefix);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\DelegatingOps$DelegateRecordBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */