/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.ListBuilder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public class NullOps implements DynamicOps<Unit> {
/*  25 */   public static final NullOps INSTANCE = new NullOps();
/*     */   
/*  27 */   private static final MapLike<Unit> EMPTY_MAP = new MapLike<Unit>() {
/*     */       public Unit get(Unit key) {
/*  30 */         return null;
/*     */       }
/*     */       
/*     */       public Unit get(String key) {
/*  35 */         return null;
/*     */       }
/*     */       
/*     */       public Stream<Pair<Unit, Unit>> entries() {
/*  40 */         return Stream.empty();
/*     */       }
/*     */     };
/*     */   
/*     */   public <U> U convertTo(DynamicOps<U> outOps, Unit input) {
/*  49 */     return (U)outOps.empty();
/*     */   }
/*     */   
/*     */   public Unit empty() {
/*  54 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit emptyMap() {
/*  59 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit emptyList() {
/*  64 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createNumeric(Number value) {
/*  69 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createByte(byte value) {
/*  74 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createShort(short value) {
/*  79 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createInt(int value) {
/*  84 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createLong(long value) {
/*  89 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createFloat(float value) {
/*  94 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createDouble(double value) {
/*  99 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createBoolean(boolean value) {
/* 104 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createString(String value) {
/* 109 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public DataResult<Number> getNumberValue(Unit input) {
/* 114 */     return DataResult.success(Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public DataResult<Boolean> getBooleanValue(Unit input) {
/* 119 */     return DataResult.success(Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public DataResult<String> getStringValue(Unit input) {
/* 124 */     return DataResult.success("");
/*     */   }
/*     */   
/*     */   public DataResult<Unit> mergeToList(Unit input, Unit value) {
/* 129 */     return DataResult.success(Unit.INSTANCE);
/*     */   }
/*     */   
/*     */   public DataResult<Unit> mergeToList(Unit input, List<Unit> values) {
/* 134 */     return DataResult.success(Unit.INSTANCE);
/*     */   }
/*     */   
/*     */   public DataResult<Unit> mergeToMap(Unit input, Unit key, Unit value) {
/* 139 */     return DataResult.success(Unit.INSTANCE);
/*     */   }
/*     */   
/*     */   public DataResult<Unit> mergeToMap(Unit input, Map<Unit, Unit> values) {
/* 144 */     return DataResult.success(Unit.INSTANCE);
/*     */   }
/*     */   
/*     */   public DataResult<Unit> mergeToMap(Unit input, MapLike<Unit> values) {
/* 149 */     return DataResult.success(Unit.INSTANCE);
/*     */   }
/*     */   
/*     */   public DataResult<Stream<Pair<Unit, Unit>>> getMapValues(Unit input) {
/* 154 */     return DataResult.success(Stream.empty());
/*     */   }
/*     */   
/*     */   public DataResult<Consumer<BiConsumer<Unit, Unit>>> getMapEntries(Unit input) {
/* 159 */     return DataResult.success(consumer -> {
/*     */         
/*     */         });
/*     */   }
/*     */   
/*     */   public DataResult<MapLike<Unit>> getMap(Unit input) {
/* 164 */     return DataResult.success(EMPTY_MAP);
/*     */   }
/*     */   
/*     */   public DataResult<Stream<Unit>> getStream(Unit input) {
/* 169 */     return DataResult.success(Stream.empty());
/*     */   }
/*     */   
/*     */   public DataResult<Consumer<Consumer<Unit>>> getList(Unit input) {
/* 174 */     return DataResult.success(consumer -> {
/*     */         
/*     */         });
/*     */   }
/*     */   
/*     */   public DataResult<ByteBuffer> getByteBuffer(Unit input) {
/* 179 */     return DataResult.success(ByteBuffer.wrap(new byte[0]));
/*     */   }
/*     */   
/*     */   public DataResult<IntStream> getIntStream(Unit input) {
/* 184 */     return DataResult.success(IntStream.empty());
/*     */   }
/*     */   
/*     */   public DataResult<LongStream> getLongStream(Unit input) {
/* 189 */     return DataResult.success(LongStream.empty());
/*     */   }
/*     */   
/*     */   public Unit createMap(Stream<Pair<Unit, Unit>> map) {
/* 194 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createMap(Map<Unit, Unit> map) {
/* 199 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createList(Stream<Unit> input) {
/* 204 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createByteList(ByteBuffer input) {
/* 209 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createIntList(IntStream input) {
/* 214 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit createLongList(LongStream input) {
/* 219 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public Unit remove(Unit input, String key) {
/* 224 */     return input;
/*     */   }
/*     */   
/*     */   public RecordBuilder<Unit> mapBuilder() {
/* 229 */     return (RecordBuilder<Unit>)new NullMapBuilder(this);
/*     */   }
/*     */   
/*     */   public ListBuilder<Unit> listBuilder() {
/* 234 */     return new NullListBuilder(this);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 239 */     return "Null";
/*     */   }
/*     */   
/*     */   private static final class NullMapBuilder extends RecordBuilder.AbstractUniversalBuilder<Unit, Unit> {
/*     */     public NullMapBuilder(DynamicOps<Unit> ops) {
/* 244 */       super(ops);
/*     */     }
/*     */     
/*     */     protected Unit initBuilder() {
/* 249 */       return Unit.INSTANCE;
/*     */     }
/*     */     
/*     */     protected Unit append(Unit key, Unit value, Unit builder) {
/* 254 */       return builder;
/*     */     }
/*     */     
/*     */     protected DataResult<Unit> build(Unit builder, Unit prefix) {
/* 259 */       return DataResult.success(prefix);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NullListBuilder extends AbstractListBuilder<Unit, Unit> {
/*     */     public NullListBuilder(DynamicOps<Unit> ops) {
/* 265 */       super(ops);
/*     */     }
/*     */     
/*     */     protected Unit initBuilder() {
/* 270 */       return Unit.INSTANCE;
/*     */     }
/*     */     
/*     */     protected Unit append(Unit builder, Unit value) {
/* 275 */       return builder;
/*     */     }
/*     */     
/*     */     protected DataResult<Unit> build(Unit builder, Unit prefix) {
/* 280 */       return DataResult.success(builder);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\NullOps.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */