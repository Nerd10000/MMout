/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.ListBuilder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public abstract class DelegatingOps<T> implements DynamicOps<T> {
/*     */   protected final DynamicOps<T> delegate;
/*     */   
/*     */   protected DelegatingOps(DynamicOps<T> delegate) {
/*  27 */     this.delegate = delegate;
/*     */   }
/*     */   
/*     */   public T empty() {
/*  32 */     return (T)this.delegate.empty();
/*     */   }
/*     */   
/*     */   public T emptyMap() {
/*  37 */     return (T)this.delegate.emptyMap();
/*     */   }
/*     */   
/*     */   public T emptyList() {
/*  42 */     return (T)this.delegate.emptyList();
/*     */   }
/*     */   
/*     */   public <U> U convertTo(DynamicOps<U> outOps, T input) {
/*  48 */     if (Objects.equals(outOps, this.delegate))
/*  49 */       return (U)input; 
/*  52 */     return (U)this.delegate.convertTo(outOps, input);
/*     */   }
/*     */   
/*     */   public DataResult<Number> getNumberValue(T input) {
/*  57 */     return this.delegate.getNumberValue(input);
/*     */   }
/*     */   
/*     */   public T createNumeric(Number i) {
/*  62 */     return (T)this.delegate.createNumeric(i);
/*     */   }
/*     */   
/*     */   public T createByte(byte value) {
/*  67 */     return (T)this.delegate.createByte(value);
/*     */   }
/*     */   
/*     */   public T createShort(short value) {
/*  72 */     return (T)this.delegate.createShort(value);
/*     */   }
/*     */   
/*     */   public T createInt(int value) {
/*  77 */     return (T)this.delegate.createInt(value);
/*     */   }
/*     */   
/*     */   public T createLong(long value) {
/*  82 */     return (T)this.delegate.createLong(value);
/*     */   }
/*     */   
/*     */   public T createFloat(float value) {
/*  87 */     return (T)this.delegate.createFloat(value);
/*     */   }
/*     */   
/*     */   public T createDouble(double value) {
/*  92 */     return (T)this.delegate.createDouble(value);
/*     */   }
/*     */   
/*     */   public DataResult<Boolean> getBooleanValue(T input) {
/*  97 */     return this.delegate.getBooleanValue(input);
/*     */   }
/*     */   
/*     */   public T createBoolean(boolean value) {
/* 102 */     return (T)this.delegate.createBoolean(value);
/*     */   }
/*     */   
/*     */   public DataResult<String> getStringValue(T input) {
/* 107 */     return this.delegate.getStringValue(input);
/*     */   }
/*     */   
/*     */   public T createString(String value) {
/* 112 */     return (T)this.delegate.createString(value);
/*     */   }
/*     */   
/*     */   public DataResult<T> mergeToList(T list, T value) {
/* 117 */     return this.delegate.mergeToList(list, value);
/*     */   }
/*     */   
/*     */   public DataResult<T> mergeToList(T list, List<T> values) {
/* 122 */     return this.delegate.mergeToList(list, values);
/*     */   }
/*     */   
/*     */   public DataResult<T> mergeToMap(T map, T key, T value) {
/* 127 */     return this.delegate.mergeToMap(map, key, value);
/*     */   }
/*     */   
/*     */   public DataResult<T> mergeToMap(T map, MapLike<T> values) {
/* 132 */     return this.delegate.mergeToMap(map, values);
/*     */   }
/*     */   
/*     */   public DataResult<T> mergeToMap(T map, Map<T, T> values) {
/* 137 */     return this.delegate.mergeToMap(map, values);
/*     */   }
/*     */   
/*     */   public DataResult<T> mergeToPrimitive(T prefix, T value) {
/* 142 */     return this.delegate.mergeToPrimitive(prefix, value);
/*     */   }
/*     */   
/*     */   public DataResult<Stream<Pair<T, T>>> getMapValues(T input) {
/* 147 */     return this.delegate.getMapValues(input);
/*     */   }
/*     */   
/*     */   public DataResult<Consumer<BiConsumer<T, T>>> getMapEntries(T input) {
/* 152 */     return this.delegate.getMapEntries(input);
/*     */   }
/*     */   
/*     */   public T createMap(Map<T, T> map) {
/* 157 */     return (T)this.delegate.createMap(map);
/*     */   }
/*     */   
/*     */   public T createMap(Stream<Pair<T, T>> map) {
/* 162 */     return (T)this.delegate.createMap(map);
/*     */   }
/*     */   
/*     */   public DataResult<MapLike<T>> getMap(T input) {
/* 167 */     return this.delegate.getMap(input);
/*     */   }
/*     */   
/*     */   public DataResult<Stream<T>> getStream(T input) {
/* 172 */     return this.delegate.getStream(input);
/*     */   }
/*     */   
/*     */   public DataResult<Consumer<Consumer<T>>> getList(T input) {
/* 177 */     return this.delegate.getList(input);
/*     */   }
/*     */   
/*     */   public T createList(Stream<T> input) {
/* 182 */     return (T)this.delegate.createList(input);
/*     */   }
/*     */   
/*     */   public DataResult<ByteBuffer> getByteBuffer(T input) {
/* 187 */     return this.delegate.getByteBuffer(input);
/*     */   }
/*     */   
/*     */   public T createByteList(ByteBuffer input) {
/* 192 */     return (T)this.delegate.createByteList(input);
/*     */   }
/*     */   
/*     */   public DataResult<IntStream> getIntStream(T input) {
/* 197 */     return this.delegate.getIntStream(input);
/*     */   }
/*     */   
/*     */   public T createIntList(IntStream input) {
/* 202 */     return (T)this.delegate.createIntList(input);
/*     */   }
/*     */   
/*     */   public DataResult<LongStream> getLongStream(T input) {
/* 207 */     return this.delegate.getLongStream(input);
/*     */   }
/*     */   
/*     */   public T createLongList(LongStream input) {
/* 212 */     return (T)this.delegate.createLongList(input);
/*     */   }
/*     */   
/*     */   public T remove(T input, String key) {
/* 217 */     return (T)this.delegate.remove(input, key);
/*     */   }
/*     */   
/*     */   public boolean compressMaps() {
/* 222 */     return this.delegate.compressMaps();
/*     */   }
/*     */   
/*     */   protected class DelegateListBuilder implements ListBuilder<T> {
/*     */     private final ListBuilder<T> original;
/*     */     
/*     */     protected DelegateListBuilder(ListBuilder<T> original) {
/* 233 */       this.original = original;
/*     */     }
/*     */     
/*     */     public DynamicOps<T> ops() {
/* 238 */       return DelegatingOps.this;
/*     */     }
/*     */     
/*     */     public DataResult<T> build(T prefix) {
/* 243 */       return this.original.build(prefix);
/*     */     }
/*     */     
/*     */     public ListBuilder<T> add(T value) {
/* 248 */       this.original.add(value);
/* 249 */       return this;
/*     */     }
/*     */     
/*     */     public ListBuilder<T> add(DataResult<T> value) {
/* 254 */       this.original.add(value);
/* 255 */       return this;
/*     */     }
/*     */     
/*     */     public <E> ListBuilder<T> add(E value, Encoder<E> encoder) {
/* 261 */       this.original.add(encoder.encodeStart(ops(), value));
/* 262 */       return this;
/*     */     }
/*     */     
/*     */     public <E> ListBuilder<T> addAll(Iterable<E> values, Encoder<E> encoder) {
/* 268 */       values.forEach(v -> this.original.add(encoder.encode(v, ops(), ops().empty())));
/* 269 */       return this;
/*     */     }
/*     */     
/*     */     public ListBuilder<T> withErrorsFrom(DataResult<?> result) {
/* 274 */       this.original.withErrorsFrom(result);
/* 275 */       return this;
/*     */     }
/*     */     
/*     */     public ListBuilder<T> mapError(UnaryOperator<String> onError) {
/* 280 */       this.original.mapError(onError);
/* 281 */       return this;
/*     */     }
/*     */     
/*     */     public DataResult<T> build(DataResult<T> prefix) {
/* 286 */       return this.original.build(prefix);
/*     */     }
/*     */   }
/*     */   
/*     */   public ListBuilder<T> listBuilder() {
/* 292 */     return new DelegateListBuilder(this.delegate.listBuilder());
/*     */   }
/*     */   
/*     */   protected class DelegateRecordBuilder implements RecordBuilder<T> {
/*     */     private final RecordBuilder<T> original;
/*     */     
/*     */     protected DelegateRecordBuilder(RecordBuilder<T> original) {
/* 303 */       this.original = original;
/*     */     }
/*     */     
/*     */     public DynamicOps<T> ops() {
/* 308 */       return DelegatingOps.this;
/*     */     }
/*     */     
/*     */     public RecordBuilder<T> add(T key, T value) {
/* 313 */       this.original.add(key, value);
/* 314 */       return this;
/*     */     }
/*     */     
/*     */     public RecordBuilder<T> add(T key, DataResult<T> value) {
/* 319 */       this.original.add(key, value);
/* 320 */       return this;
/*     */     }
/*     */     
/*     */     public RecordBuilder<T> add(DataResult<T> key, DataResult<T> value) {
/* 325 */       this.original.add(key, value);
/* 326 */       return this;
/*     */     }
/*     */     
/*     */     public RecordBuilder<T> add(String key, T value) {
/* 331 */       this.original.add(key, value);
/* 332 */       return this;
/*     */     }
/*     */     
/*     */     public RecordBuilder<T> add(String key, DataResult<T> value) {
/* 337 */       this.original.add(key, value);
/* 338 */       return this;
/*     */     }
/*     */     
/*     */     public <E> RecordBuilder<T> add(String key, E value, Encoder<E> encoder) {
/* 344 */       return this.original.add(key, encoder.encodeStart(ops(), value));
/*     */     }
/*     */     
/*     */     public RecordBuilder<T> withErrorsFrom(DataResult<?> result) {
/* 349 */       this.original.withErrorsFrom(result);
/* 350 */       return this;
/*     */     }
/*     */     
/*     */     public RecordBuilder<T> setLifecycle(Lifecycle lifecycle) {
/* 355 */       this.original.setLifecycle(lifecycle);
/* 356 */       return this;
/*     */     }
/*     */     
/*     */     public RecordBuilder<T> mapError(UnaryOperator<String> onError) {
/* 361 */       this.original.mapError(onError);
/* 362 */       return this;
/*     */     }
/*     */     
/*     */     public DataResult<T> build(T prefix) {
/* 367 */       return this.original.build(prefix);
/*     */     }
/*     */     
/*     */     public DataResult<T> build(DataResult<T> prefix) {
/* 372 */       return this.original.build(prefix);
/*     */     }
/*     */   }
/*     */   
/*     */   public RecordBuilder<T> mapBuilder() {
/* 378 */     return new DelegateRecordBuilder(this.delegate.mapBuilder());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\DelegatingOps.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */