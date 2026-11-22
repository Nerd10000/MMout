/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hasher;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.ListBuilder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public class HashOps implements DynamicOps<HashCode> {
/*     */   private static final byte TAG_EMPTY = 1;
/*     */   
/*     */   private static final byte TAG_MAP_START = 2;
/*     */   
/*     */   private static final byte TAG_MAP_END = 3;
/*     */   
/*     */   private static final byte TAG_LIST_START = 4;
/*     */   
/*     */   private static final byte TAG_LIST_END = 5;
/*     */   
/*     */   private static final byte TAG_BYTE = 6;
/*     */   
/*     */   private static final byte TAG_SHORT = 7;
/*     */   
/*     */   private static final byte TAG_INT = 8;
/*     */   
/*     */   private static final byte TAG_LONG = 9;
/*     */   
/*     */   private static final byte TAG_FLOAT = 10;
/*     */   
/*     */   private static final byte TAG_DOUBLE = 11;
/*     */   
/*     */   private static final byte TAG_STRING = 12;
/*     */   
/*     */   private static final byte TAG_BOOLEAN = 13;
/*     */   
/*     */   private static final byte TAG_BYTE_ARRAY_START = 14;
/*     */   
/*     */   private static final byte TAG_BYTE_ARRAY_END = 15;
/*     */   
/*     */   private static final byte TAG_INT_ARRAY_START = 16;
/*     */   
/*     */   private static final byte TAG_INT_ARRAY_END = 17;
/*     */   
/*     */   private static final byte TAG_LONG_ARRAY_START = 18;
/*     */   
/*     */   private static final byte TAG_LONG_ARRAY_END = 19;
/*     */   
/*  56 */   private static final byte[] EMPTY_PAYLOAD = new byte[] { 1 };
/*     */   
/*  57 */   private static final byte[] FALSE_PAYLOAD = new byte[] { 13, 0 };
/*     */   
/*  58 */   private static final byte[] TRUE_PAYLOAD = new byte[] { 13, 1 };
/*     */   
/*  60 */   public static final byte[] EMPTY_MAP_PAYLOAD = new byte[] { 2, 3 };
/*     */   
/*  61 */   public static final byte[] EMPTY_LIST_PAYLOAD = new byte[] { 4, 5 };
/*     */   
/*  63 */   private static final DataResult<Object> UNSUPPORTED_OPERATION_ERROR = DataResult.error(() -> "Unsupported operation");
/*     */   
/*  65 */   private static final Comparator<HashCode> HASH_COMPARATOR = Comparator.comparingLong(HashCode::padToLong);
/*     */   
/*  67 */   private static final Comparator<Map.Entry<HashCode, HashCode>> MAP_ENTRY_ORDER = Map.Entry.<HashCode, HashCode>comparingByKey(HASH_COMPARATOR)
/*  68 */     .thenComparing((Comparator)Map.Entry.comparingByValue(HASH_COMPARATOR));
/*     */   
/*  70 */   private static final Comparator<Pair<HashCode, HashCode>> MAPLIKE_ENTRY_ORDER = Comparator.<Pair<HashCode, HashCode>, HashCode>comparing(Pair::getFirst, HASH_COMPARATOR)
/*  71 */     .thenComparing(Pair::getSecond, HASH_COMPARATOR);
/*     */   
/*  73 */   public static final HashOps CRC32C_INSTANCE = new HashOps(Hashing.crc32c());
/*     */   
/*     */   private final HashFunction hashFunction;
/*     */   
/*     */   private final HashCode empty;
/*     */   
/*     */   private final HashCode emptyMap;
/*     */   
/*     */   private final HashCode emptyList;
/*     */   
/*     */   private final HashCode trueHash;
/*     */   
/*     */   private final HashCode falseHash;
/*     */   
/*     */   public HashOps(HashFunction hashFunction) {
/*  84 */     this.hashFunction = hashFunction;
/*  86 */     this.empty = hashFunction.hashBytes(EMPTY_PAYLOAD);
/*  87 */     this.emptyMap = hashFunction.hashBytes(EMPTY_MAP_PAYLOAD);
/*  88 */     this.emptyList = hashFunction.hashBytes(EMPTY_LIST_PAYLOAD);
/*  90 */     this.falseHash = hashFunction.hashBytes(FALSE_PAYLOAD);
/*  91 */     this.trueHash = hashFunction.hashBytes(TRUE_PAYLOAD);
/*     */   }
/*     */   
/*     */   public HashCode empty() {
/*  96 */     return this.empty;
/*     */   }
/*     */   
/*     */   public HashCode emptyMap() {
/* 101 */     return this.emptyMap;
/*     */   }
/*     */   
/*     */   public HashCode emptyList() {
/* 106 */     return this.emptyList;
/*     */   }
/*     */   
/*     */   public HashCode createNumeric(Number value) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_2
/*     */     //   7: iconst_0
/*     */     //   8: istore_3
/*     */     //   9: aload_2
/*     */     //   10: iload_3
/*     */     //   11: <illegal opcode> typeSwitch : (Ljava/lang/Number;I)I
/*     */     //   16: tableswitch default -> 164, 0 -> 56, 1 -> 74, 2 -> 92, 3 -> 110, 4 -> 128, 5 -> 146
/*     */     //   56: aload_2
/*     */     //   57: checkcast java/lang/Byte
/*     */     //   60: astore #4
/*     */     //   62: aload_0
/*     */     //   63: aload #4
/*     */     //   65: invokevirtual byteValue : ()B
/*     */     //   68: invokevirtual createByte : (B)Lcom/google/common/hash/HashCode;
/*     */     //   71: goto -> 172
/*     */     //   74: aload_2
/*     */     //   75: checkcast java/lang/Short
/*     */     //   78: astore #5
/*     */     //   80: aload_0
/*     */     //   81: aload #5
/*     */     //   83: invokevirtual shortValue : ()S
/*     */     //   86: invokevirtual createShort : (S)Lcom/google/common/hash/HashCode;
/*     */     //   89: goto -> 172
/*     */     //   92: aload_2
/*     */     //   93: checkcast java/lang/Integer
/*     */     //   96: astore #6
/*     */     //   98: aload_0
/*     */     //   99: aload #6
/*     */     //   101: invokevirtual intValue : ()I
/*     */     //   104: invokevirtual createInt : (I)Lcom/google/common/hash/HashCode;
/*     */     //   107: goto -> 172
/*     */     //   110: aload_2
/*     */     //   111: checkcast java/lang/Long
/*     */     //   114: astore #7
/*     */     //   116: aload_0
/*     */     //   117: aload #7
/*     */     //   119: invokevirtual longValue : ()J
/*     */     //   122: invokevirtual createLong : (J)Lcom/google/common/hash/HashCode;
/*     */     //   125: goto -> 172
/*     */     //   128: aload_2
/*     */     //   129: checkcast java/lang/Double
/*     */     //   132: astore #8
/*     */     //   134: aload_0
/*     */     //   135: aload #8
/*     */     //   137: invokevirtual doubleValue : ()D
/*     */     //   140: invokevirtual createDouble : (D)Lcom/google/common/hash/HashCode;
/*     */     //   143: goto -> 172
/*     */     //   146: aload_2
/*     */     //   147: checkcast java/lang/Float
/*     */     //   150: astore #9
/*     */     //   152: aload_0
/*     */     //   153: aload #9
/*     */     //   155: invokevirtual floatValue : ()F
/*     */     //   158: invokevirtual createFloat : (F)Lcom/google/common/hash/HashCode;
/*     */     //   161: goto -> 172
/*     */     //   164: aload_0
/*     */     //   165: aload_1
/*     */     //   166: invokevirtual doubleValue : ()D
/*     */     //   169: invokevirtual createDouble : (D)Lcom/google/common/hash/HashCode;
/*     */     //   172: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #111	-> 0
/*     */     //   #112	-> 56
/*     */     //   #113	-> 74
/*     */     //   #114	-> 92
/*     */     //   #115	-> 110
/*     */     //   #117	-> 128
/*     */     //   #118	-> 146
/*     */     //   #119	-> 164
/*     */     //   #111	-> 172
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   62	12	4	v	Ljava/lang/Byte;
/*     */     //   80	12	5	v	Ljava/lang/Short;
/*     */     //   98	12	6	v	Ljava/lang/Integer;
/*     */     //   116	12	7	v	Ljava/lang/Long;
/*     */     //   134	12	8	v	Ljava/lang/Double;
/*     */     //   152	12	9	v	Ljava/lang/Float;
/*     */     //   0	173	0	this	Lnet/minecraft/util/HashOps;
/*     */     //   0	173	1	value	Ljava/lang/Number;
/*     */   }
/*     */   
/*     */   public HashCode createByte(byte value) {
/* 125 */     return this.hashFunction.newHasher(2).putByte((byte)6).putByte(value).hash();
/*     */   }
/*     */   
/*     */   public HashCode createShort(short value) {
/* 130 */     return this.hashFunction.newHasher(3).putByte((byte)7).putShort(value).hash();
/*     */   }
/*     */   
/*     */   public HashCode createInt(int value) {
/* 135 */     return this.hashFunction.newHasher(5).putByte((byte)8).putInt(value).hash();
/*     */   }
/*     */   
/*     */   public HashCode createLong(long value) {
/* 140 */     return this.hashFunction.newHasher(9).putByte((byte)9).putLong(value).hash();
/*     */   }
/*     */   
/*     */   public HashCode createFloat(float value) {
/* 145 */     return this.hashFunction.newHasher(5).putByte((byte)10).putFloat(value).hash();
/*     */   }
/*     */   
/*     */   public HashCode createDouble(double value) {
/* 150 */     return this.hashFunction.newHasher(9).putByte((byte)11).putDouble(value).hash();
/*     */   }
/*     */   
/*     */   public HashCode createString(String value) {
/* 155 */     return this.hashFunction.newHasher().putByte((byte)12).putInt(value.length()).putUnencodedChars(value).hash();
/*     */   }
/*     */   
/*     */   public HashCode createBoolean(boolean value) {
/* 160 */     return value ? this.trueHash : this.falseHash;
/*     */   }
/*     */   
/*     */   private static Hasher hashMap(Hasher hasher, Map<HashCode, HashCode> map) {
/* 164 */     hasher.putByte((byte)2);
/* 165 */     map.entrySet().stream().sorted(MAP_ENTRY_ORDER).forEach(e -> hasher.putBytes(((HashCode)e.getKey()).asBytes()).putBytes(((HashCode)e.getValue()).asBytes()));
/* 166 */     hasher.putByte((byte)3);
/* 167 */     return hasher;
/*     */   }
/*     */   
/*     */   private static Hasher hashMap(Hasher hasher, Stream<Pair<HashCode, HashCode>> map) {
/* 171 */     hasher.putByte((byte)2);
/* 172 */     map.sorted(MAPLIKE_ENTRY_ORDER).forEach(e -> hasher.putBytes(((HashCode)e.getFirst()).asBytes()).putBytes(((HashCode)e.getSecond()).asBytes()));
/* 173 */     hasher.putByte((byte)3);
/* 174 */     return hasher;
/*     */   }
/*     */   
/*     */   public HashCode createMap(Stream<Pair<HashCode, HashCode>> map) {
/* 179 */     return hashMap(this.hashFunction.newHasher(), map).hash();
/*     */   }
/*     */   
/*     */   public HashCode createMap(Map<HashCode, HashCode> map) {
/* 184 */     return hashMap(this.hashFunction.newHasher(), map).hash();
/*     */   }
/*     */   
/*     */   public HashCode createList(Stream<HashCode> input) {
/* 189 */     Hasher hasher = this.hashFunction.newHasher();
/* 190 */     hasher.putByte((byte)4);
/* 191 */     input.forEach(value -> hasher.putBytes(value.asBytes()));
/* 192 */     hasher.putByte((byte)5);
/* 193 */     return hasher.hash();
/*     */   }
/*     */   
/*     */   public HashCode createByteList(ByteBuffer input) {
/* 198 */     Hasher hasher = this.hashFunction.newHasher();
/* 199 */     hasher.putByte((byte)14);
/* 200 */     hasher.putBytes(input);
/* 201 */     hasher.putByte((byte)15);
/* 202 */     return hasher.hash();
/*     */   }
/*     */   
/*     */   public HashCode createIntList(IntStream input) {
/* 207 */     Hasher hasher = this.hashFunction.newHasher();
/* 208 */     hasher.putByte((byte)16);
/* 209 */     Objects.requireNonNull(hasher);
/* 209 */     input.forEach(hasher::putInt);
/* 210 */     hasher.putByte((byte)17);
/* 211 */     return hasher.hash();
/*     */   }
/*     */   
/*     */   public HashCode createLongList(LongStream input) {
/* 216 */     Hasher hasher = this.hashFunction.newHasher();
/* 217 */     hasher.putByte((byte)18);
/* 218 */     Objects.requireNonNull(hasher);
/* 218 */     input.forEach(hasher::putLong);
/* 219 */     hasher.putByte((byte)19);
/* 220 */     return hasher.hash();
/*     */   }
/*     */   
/*     */   public HashCode remove(HashCode input, String key) {
/* 226 */     return input;
/*     */   }
/*     */   
/*     */   public RecordBuilder<HashCode> mapBuilder() {
/* 231 */     return (RecordBuilder<HashCode>)new MapHashBuilder();
/*     */   }
/*     */   
/*     */   private final class MapHashBuilder extends RecordBuilder.AbstractUniversalBuilder<HashCode, List<Pair<HashCode, HashCode>>> {
/*     */     public MapHashBuilder() {
/* 236 */       super(HashOps.this);
/*     */     }
/*     */     
/*     */     protected List<Pair<HashCode, HashCode>> initBuilder() {
/* 241 */       return new ArrayList<>();
/*     */     }
/*     */     
/*     */     protected List<Pair<HashCode, HashCode>> append(HashCode key, HashCode value, List<Pair<HashCode, HashCode>> builder) {
/* 246 */       builder.add(Pair.of(key, value));
/* 247 */       return builder;
/*     */     }
/*     */     
/*     */     protected DataResult<HashCode> build(List<Pair<HashCode, HashCode>> builder, HashCode prefix) {
/* 252 */       assert HashOps.this.isEmpty(prefix);
/* 253 */       return DataResult.success(HashOps.hashMap(HashOps.this.hashFunction.newHasher(), builder.stream()).hash());
/*     */     }
/*     */   }
/*     */   
/*     */   public ListBuilder<HashCode> listBuilder() {
/* 259 */     return new ListHashBuilder();
/*     */   }
/*     */   
/*     */   private class ListHashBuilder extends AbstractListBuilder<HashCode, Hasher> {
/*     */     public ListHashBuilder() {
/* 264 */       super(HashOps.this);
/*     */     }
/*     */     
/*     */     protected Hasher initBuilder() {
/* 269 */       return HashOps.this.hashFunction.newHasher().putByte((byte)4);
/*     */     }
/*     */     
/*     */     protected Hasher append(Hasher hasher, HashCode value) {
/* 274 */       return hasher.putBytes(value.asBytes());
/*     */     }
/*     */     
/*     */     protected DataResult<HashCode> build(Hasher hasher, HashCode prefix) {
/* 279 */       assert prefix.equals(HashOps.this.empty);
/* 280 */       hasher.putByte((byte)5);
/* 281 */       return DataResult.success(hasher.hash());
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 287 */     return "Hash " + String.valueOf(this.hashFunction);
/*     */   }
/*     */   
/*     */   public <U> U convertTo(DynamicOps<U> outOps, HashCode input) {
/* 294 */     throw new UnsupportedOperationException("Can't convert from this type");
/*     */   }
/*     */   
/*     */   public Number getNumberValue(HashCode input, Number defaultValue) {
/* 299 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public HashCode set(HashCode input, String key, HashCode value) {
/* 304 */     return input;
/*     */   }
/*     */   
/*     */   public HashCode update(HashCode input, String key, Function<HashCode, HashCode> function) {
/* 309 */     return input;
/*     */   }
/*     */   
/*     */   public HashCode updateGeneric(HashCode input, HashCode key, Function<HashCode, HashCode> function) {
/* 314 */     return input;
/*     */   }
/*     */   
/*     */   private static <T> DataResult<T> unsupported() {
/* 319 */     return (DataResult)UNSUPPORTED_OPERATION_ERROR;
/*     */   }
/*     */   
/*     */   public DataResult<HashCode> get(HashCode input, String key) {
/* 324 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<HashCode> getGeneric(HashCode input, HashCode key) {
/* 329 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<Number> getNumberValue(HashCode input) {
/* 334 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<Boolean> getBooleanValue(HashCode input) {
/* 339 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<String> getStringValue(HashCode input) {
/* 344 */     return unsupported();
/*     */   }
/*     */   
/*     */   private boolean isEmpty(HashCode value) {
/* 348 */     return value.equals(this.empty);
/*     */   }
/*     */   
/*     */   public DataResult<HashCode> mergeToList(HashCode prefix, HashCode value) {
/* 353 */     if (isEmpty(prefix))
/* 354 */       return DataResult.success(createList(Stream.of(value))); 
/* 356 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<HashCode> mergeToList(HashCode prefix, List<HashCode> values) {
/* 361 */     if (isEmpty(prefix))
/* 362 */       return DataResult.success(createList(values.stream())); 
/* 365 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<HashCode> mergeToMap(HashCode prefix, HashCode key, HashCode value) {
/* 370 */     if (isEmpty(prefix))
/* 371 */       return DataResult.success(createMap(Map.of(key, value))); 
/* 373 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<HashCode> mergeToMap(HashCode prefix, Map<HashCode, HashCode> values) {
/* 378 */     if (isEmpty(prefix))
/* 379 */       return DataResult.success(createMap(values)); 
/* 381 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<HashCode> mergeToMap(HashCode prefix, MapLike<HashCode> values) {
/* 386 */     if (isEmpty(prefix))
/* 387 */       return DataResult.success(createMap(values.entries())); 
/* 389 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<Stream<Pair<HashCode, HashCode>>> getMapValues(HashCode input) {
/* 394 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<Consumer<BiConsumer<HashCode, HashCode>>> getMapEntries(HashCode input) {
/* 399 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<Stream<HashCode>> getStream(HashCode input) {
/* 404 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<Consumer<Consumer<HashCode>>> getList(HashCode input) {
/* 409 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<MapLike<HashCode>> getMap(HashCode input) {
/* 414 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<ByteBuffer> getByteBuffer(HashCode input) {
/* 419 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<IntStream> getIntStream(HashCode input) {
/* 424 */     return unsupported();
/*     */   }
/*     */   
/*     */   public DataResult<LongStream> getLongStream(HashCode input) {
/* 429 */     return unsupported();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\HashOps.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */