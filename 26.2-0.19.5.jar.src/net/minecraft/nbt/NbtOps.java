/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import it.unimi.dsi.fastutil.bytes.ByteArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class NbtOps implements DynamicOps<Tag> {
/*  28 */   public static final NbtOps INSTANCE = new NbtOps();
/*     */   
/*     */   public Tag empty() {
/*  35 */     return EndTag.INSTANCE;
/*     */   }
/*     */   
/*     */   public Tag emptyList() {
/*  40 */     return new ListTag();
/*     */   }
/*     */   
/*     */   public Tag emptyMap() {
/*  45 */     return new CompoundTag();
/*     */   }
/*     */   
/*     */   public <U> U convertTo(DynamicOps<U> outOps, Tag input) {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_3
/*     */     //   7: iconst_0
/*     */     //   8: istore #4
/*     */     //   10: aload_3
/*     */     //   11: iload #4
/*     */     //   13: <illegal opcode> typeSwitch : (Lnet/minecraft/nbt/Tag;I)I
/*     */     //   18: tableswitch default -> 84, 0 -> 94, 1 -> 109, 2 -> 154, 3 -> 199, 4 -> 244, 5 -> 289, 6 -> 335, 7 -> 381, 8 -> 404, 9 -> 432, 10 -> 448, 11 -> 464, 12 -> 487
/*     */     //   84: new java/lang/MatchException
/*     */     //   87: dup
/*     */     //   88: aconst_null
/*     */     //   89: aconst_null
/*     */     //   90: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   93: athrow
/*     */     //   94: aload_3
/*     */     //   95: checkcast net/minecraft/nbt/EndTag
/*     */     //   98: astore #5
/*     */     //   100: aload_1
/*     */     //   101: invokeinterface empty : ()Ljava/lang/Object;
/*     */     //   106: goto -> 510
/*     */     //   109: aload_3
/*     */     //   110: checkcast net/minecraft/nbt/ByteTag
/*     */     //   113: astore #6
/*     */     //   115: aload #6
/*     */     //   117: invokevirtual value : ()B
/*     */     //   120: istore #8
/*     */     //   122: iload #8
/*     */     //   124: istore #9
/*     */     //   126: iconst_1
/*     */     //   127: ifeq -> 137
/*     */     //   130: iload #8
/*     */     //   132: istore #7
/*     */     //   134: goto -> 143
/*     */     //   137: iconst_2
/*     */     //   138: istore #4
/*     */     //   140: goto -> 10
/*     */     //   143: aload_1
/*     */     //   144: iload #7
/*     */     //   146: invokeinterface createByte : (B)Ljava/lang/Object;
/*     */     //   151: goto -> 510
/*     */     //   154: aload_3
/*     */     //   155: checkcast net/minecraft/nbt/ShortTag
/*     */     //   158: astore #8
/*     */     //   160: aload #8
/*     */     //   162: invokevirtual value : ()S
/*     */     //   165: istore #10
/*     */     //   167: iload #10
/*     */     //   169: istore #11
/*     */     //   171: iconst_1
/*     */     //   172: ifeq -> 182
/*     */     //   175: iload #10
/*     */     //   177: istore #9
/*     */     //   179: goto -> 188
/*     */     //   182: iconst_3
/*     */     //   183: istore #4
/*     */     //   185: goto -> 10
/*     */     //   188: aload_1
/*     */     //   189: iload #9
/*     */     //   191: invokeinterface createShort : (S)Ljava/lang/Object;
/*     */     //   196: goto -> 510
/*     */     //   199: aload_3
/*     */     //   200: checkcast net/minecraft/nbt/IntTag
/*     */     //   203: astore #10
/*     */     //   205: aload #10
/*     */     //   207: invokevirtual value : ()I
/*     */     //   210: istore #12
/*     */     //   212: iload #12
/*     */     //   214: istore #13
/*     */     //   216: iconst_1
/*     */     //   217: ifeq -> 227
/*     */     //   220: iload #12
/*     */     //   222: istore #11
/*     */     //   224: goto -> 233
/*     */     //   227: iconst_4
/*     */     //   228: istore #4
/*     */     //   230: goto -> 10
/*     */     //   233: aload_1
/*     */     //   234: iload #11
/*     */     //   236: invokeinterface createInt : (I)Ljava/lang/Object;
/*     */     //   241: goto -> 510
/*     */     //   244: aload_3
/*     */     //   245: checkcast net/minecraft/nbt/LongTag
/*     */     //   248: astore #12
/*     */     //   250: aload #12
/*     */     //   252: invokevirtual value : ()J
/*     */     //   255: lstore #15
/*     */     //   257: lload #15
/*     */     //   259: lstore #17
/*     */     //   261: iconst_1
/*     */     //   262: ifeq -> 272
/*     */     //   265: lload #15
/*     */     //   267: lstore #13
/*     */     //   269: goto -> 278
/*     */     //   272: iconst_5
/*     */     //   273: istore #4
/*     */     //   275: goto -> 10
/*     */     //   278: aload_1
/*     */     //   279: lload #13
/*     */     //   281: invokeinterface createLong : (J)Ljava/lang/Object;
/*     */     //   286: goto -> 510
/*     */     //   289: aload_3
/*     */     //   290: checkcast net/minecraft/nbt/FloatTag
/*     */     //   293: astore #15
/*     */     //   295: aload #15
/*     */     //   297: invokevirtual value : ()F
/*     */     //   300: fstore #17
/*     */     //   302: fload #17
/*     */     //   304: fstore #18
/*     */     //   306: iconst_1
/*     */     //   307: ifeq -> 317
/*     */     //   310: fload #17
/*     */     //   312: fstore #16
/*     */     //   314: goto -> 324
/*     */     //   317: bipush #6
/*     */     //   319: istore #4
/*     */     //   321: goto -> 10
/*     */     //   324: aload_1
/*     */     //   325: fload #16
/*     */     //   327: invokeinterface createFloat : (F)Ljava/lang/Object;
/*     */     //   332: goto -> 510
/*     */     //   335: aload_3
/*     */     //   336: checkcast net/minecraft/nbt/DoubleTag
/*     */     //   339: astore #17
/*     */     //   341: aload #17
/*     */     //   343: invokevirtual value : ()D
/*     */     //   346: dstore #20
/*     */     //   348: dload #20
/*     */     //   350: dstore #22
/*     */     //   352: iconst_1
/*     */     //   353: ifeq -> 363
/*     */     //   356: dload #20
/*     */     //   358: dstore #18
/*     */     //   360: goto -> 370
/*     */     //   363: bipush #7
/*     */     //   365: istore #4
/*     */     //   367: goto -> 10
/*     */     //   370: aload_1
/*     */     //   371: dload #18
/*     */     //   373: invokeinterface createDouble : (D)Ljava/lang/Object;
/*     */     //   378: goto -> 510
/*     */     //   381: aload_3
/*     */     //   382: checkcast net/minecraft/nbt/ByteArrayTag
/*     */     //   385: astore #20
/*     */     //   387: aload_1
/*     */     //   388: aload #20
/*     */     //   390: invokevirtual getAsByteArray : ()[B
/*     */     //   393: invokestatic wrap : ([B)Ljava/nio/ByteBuffer;
/*     */     //   396: invokeinterface createByteList : (Ljava/nio/ByteBuffer;)Ljava/lang/Object;
/*     */     //   401: goto -> 510
/*     */     //   404: aload_3
/*     */     //   405: checkcast net/minecraft/nbt/StringTag
/*     */     //   408: astore #21
/*     */     //   410: aload #21
/*     */     //   412: invokevirtual value : ()Ljava/lang/String;
/*     */     //   415: astore #23
/*     */     //   417: aload #23
/*     */     //   419: astore #22
/*     */     //   421: aload_1
/*     */     //   422: aload #22
/*     */     //   424: invokeinterface createString : (Ljava/lang/String;)Ljava/lang/Object;
/*     */     //   429: goto -> 510
/*     */     //   432: aload_3
/*     */     //   433: checkcast net/minecraft/nbt/ListTag
/*     */     //   436: astore #23
/*     */     //   438: aload_0
/*     */     //   439: aload_1
/*     */     //   440: aload #23
/*     */     //   442: invokevirtual convertList : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   445: goto -> 510
/*     */     //   448: aload_3
/*     */     //   449: checkcast net/minecraft/nbt/CompoundTag
/*     */     //   452: astore #24
/*     */     //   454: aload_0
/*     */     //   455: aload_1
/*     */     //   456: aload #24
/*     */     //   458: invokevirtual convertMap : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   461: goto -> 510
/*     */     //   464: aload_3
/*     */     //   465: checkcast net/minecraft/nbt/IntArrayTag
/*     */     //   468: astore #25
/*     */     //   470: aload_1
/*     */     //   471: aload #25
/*     */     //   473: invokevirtual getAsIntArray : ()[I
/*     */     //   476: invokestatic stream : ([I)Ljava/util/stream/IntStream;
/*     */     //   479: invokeinterface createIntList : (Ljava/util/stream/IntStream;)Ljava/lang/Object;
/*     */     //   484: goto -> 510
/*     */     //   487: aload_3
/*     */     //   488: checkcast net/minecraft/nbt/LongArrayTag
/*     */     //   491: astore #26
/*     */     //   493: aload_1
/*     */     //   494: aload #26
/*     */     //   496: invokevirtual getAsLongArray : ()[J
/*     */     //   499: invokestatic stream : ([J)Ljava/util/stream/LongStream;
/*     */     //   502: invokeinterface createLongList : (Ljava/util/stream/LongStream;)Ljava/lang/Object;
/*     */     //   507: goto -> 510
/*     */     //   510: areturn
/*     */     //   511: astore_3
/*     */     //   512: new java/lang/MatchException
/*     */     //   515: dup
/*     */     //   516: aload_3
/*     */     //   517: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   520: aload_3
/*     */     //   521: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   524: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #50	-> 0
/*     */     //   #51	-> 94
/*     */     //   #52	-> 109
/*     */     //   #53	-> 154
/*     */     //   #54	-> 199
/*     */     //   #55	-> 244
/*     */     //   #56	-> 289
/*     */     //   #57	-> 335
/*     */     //   #58	-> 381
/*     */     //   #59	-> 404
/*     */     //   #60	-> 432
/*     */     //   #61	-> 448
/*     */     //   #62	-> 464
/*     */     //   #63	-> 487
/*     */     //   #50	-> 510
/*     */     //   #63	-> 511
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   100	9	5	ignored	Lnet/minecraft/nbt/EndTag;
/*     */     //   134	3	7	value	B
/*     */     //   143	11	7	value	B
/*     */     //   179	3	9	value	S
/*     */     //   188	11	9	value	S
/*     */     //   224	3	11	value	I
/*     */     //   233	11	11	value	I
/*     */     //   269	3	13	value	J
/*     */     //   278	11	13	value	J
/*     */     //   314	3	16	value	F
/*     */     //   324	11	16	value	F
/*     */     //   360	3	18	value	D
/*     */     //   370	11	18	value	D
/*     */     //   387	17	20	byteArrayTag	Lnet/minecraft/nbt/ByteArrayTag;
/*     */     //   421	11	22	value	Ljava/lang/String;
/*     */     //   438	10	23	listTag	Lnet/minecraft/nbt/ListTag;
/*     */     //   454	10	24	compoundTag	Lnet/minecraft/nbt/CompoundTag;
/*     */     //   470	17	25	intArrayTag	Lnet/minecraft/nbt/IntArrayTag;
/*     */     //   493	17	26	longArrayTag	Lnet/minecraft/nbt/LongArrayTag;
/*     */     //   0	525	0	this	Lnet/minecraft/nbt/NbtOps;
/*     */     //   0	525	1	outOps	Lcom/mojang/serialization/DynamicOps;
/*     */     //   0	525	2	input	Lnet/minecraft/nbt/Tag;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	525	1	outOps	Lcom/mojang/serialization/DynamicOps<TU;>;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   117	120	511	java/lang/Throwable
/*     */     //   162	165	511	java/lang/Throwable
/*     */     //   207	210	511	java/lang/Throwable
/*     */     //   252	255	511	java/lang/Throwable
/*     */     //   297	300	511	java/lang/Throwable
/*     */     //   343	346	511	java/lang/Throwable
/*     */     //   412	415	511	java/lang/Throwable
/*     */   }
/*     */   
/*     */   public DataResult<Number> getNumberValue(Tag input) {
/*  69 */     return input.asNumber()
/*  70 */       .<DataResult<Number>>map(DataResult::success)
/*  71 */       .orElseGet(() -> DataResult.error(()));
/*     */   }
/*     */   
/*     */   public Tag createNumeric(Number i) {
/*  76 */     return DoubleTag.valueOf(i.doubleValue());
/*     */   }
/*     */   
/*     */   public Tag createByte(byte value) {
/*  81 */     return ByteTag.valueOf(value);
/*     */   }
/*     */   
/*     */   public Tag createShort(short value) {
/*  86 */     return ShortTag.valueOf(value);
/*     */   }
/*     */   
/*     */   public Tag createInt(int value) {
/*  91 */     return IntTag.valueOf(value);
/*     */   }
/*     */   
/*     */   public Tag createLong(long value) {
/*  96 */     return LongTag.valueOf(value);
/*     */   }
/*     */   
/*     */   public Tag createFloat(float value) {
/* 101 */     return FloatTag.valueOf(value);
/*     */   }
/*     */   
/*     */   public Tag createDouble(double value) {
/* 106 */     return DoubleTag.valueOf(value);
/*     */   }
/*     */   
/*     */   public DataResult<Boolean> getBooleanValue(Tag input) {
/* 111 */     return getNumberValue(input).map(value -> Boolean.valueOf((value.doubleValue() != 0.0D)));
/*     */   }
/*     */   
/*     */   public Tag createBoolean(boolean value) {
/* 116 */     return ByteTag.valueOf(value);
/*     */   }
/*     */   
/*     */   public DataResult<String> getStringValue(Tag input) {
/* 121 */     if (input instanceof StringTag) {
/* 121 */       StringTag stringTag = (StringTag)input;
/*     */       try {
/* 121 */         String str1 = stringTag.value(), value = str1;
/* 122 */         return DataResult.success(value);
/*     */       } catch (Throwable throwable) {
/*     */         throw new MatchException(throwable.toString(), throwable);
/*     */       } 
/*     */     } 
/* 124 */     return DataResult.error(() -> "Not a string");
/*     */   }
/*     */   
/*     */   public Tag createString(String value) {
/* 129 */     return StringTag.valueOf(value);
/*     */   }
/*     */   
/*     */   public DataResult<Tag> mergeToList(Tag list, Tag value) {
/* 134 */     return createCollector(list)
/* 135 */       .<DataResult<Tag>>map(collector -> DataResult.success(collector.accept(value).result()))
/* 136 */       .orElseGet(() -> DataResult.error((), list));
/*     */   }
/*     */   
/*     */   public DataResult<Tag> mergeToList(Tag list, List<Tag> values) {
/* 141 */     return createCollector(list)
/* 142 */       .<DataResult<Tag>>map(collector -> DataResult.success(collector.acceptAll(values).result()))
/* 143 */       .orElseGet(() -> DataResult.error((), list));
/*     */   }
/*     */   
/*     */   public DataResult<Tag> mergeToMap(Tag map, Tag key, Tag value) {
/* 148 */     if (!(map instanceof CompoundTag) && !(map instanceof EndTag))
/* 149 */       return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(map), map); 
/* 151 */     if (key instanceof StringTag) {
/* 151 */       StringTag stringTag = (StringTag)key;
/*     */       try {
/* 151 */         String str1 = stringTag.value(), stringKey = str1;
/* 155 */         CompoundTag tag = (CompoundTag)map, output = (map instanceof CompoundTag) ? tag.shallowCopy() : new CompoundTag();
/* 156 */         output.put(stringKey, value);
/* 157 */         return DataResult.success(output);
/*     */       } catch (Throwable throwable) {
/*     */         throw new MatchException(throwable.toString(), throwable);
/*     */       } 
/*     */     } 
/*     */     return DataResult.error(() -> "key is not a string: " + String.valueOf(key), map);
/*     */   }
/*     */   
/*     */   public DataResult<Tag> mergeToMap(Tag map, MapLike<Tag> values) {
/* 162 */     if (!(map instanceof CompoundTag) && !(map instanceof EndTag))
/* 163 */       return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(map), map); 
/* 166 */     Iterator<Pair<Tag, Tag>> valuesIterator = values.entries().iterator();
/* 167 */     if (!valuesIterator.hasNext()) {
/* 168 */       if (map == empty())
/* 169 */         return DataResult.success(emptyMap()); 
/* 171 */       return DataResult.success(map);
/*     */     } 
/* 174 */     CompoundTag tag = (CompoundTag)map, output = (map instanceof CompoundTag) ? tag.shallowCopy() : new CompoundTag();
/* 175 */     List<Tag> missed = new ArrayList<>();
/* 177 */     valuesIterator.forEachRemaining(entry -> {
/*     */           Tag key = (Tag)entry.getFirst();
/*     */           if (key instanceof StringTag) {
/*     */             StringTag $b$0 = (StringTag)key;
/*     */             try {
/*     */               String patt1$temp = $b$0.value();
/*     */               String stringKey = patt1$temp;
/*     */               output.put(stringKey, (Tag)entry.getSecond());
/* 184 */             } catch (Throwable throwable) {
/*     */               throw new MatchException(throwable.toString(), throwable);
/*     */             } 
/*     */           } else {
/*     */             missed.add(key);
/*     */             return;
/*     */           } 
/*     */         });
/* 187 */     if (!missed.isEmpty())
/* 188 */       return DataResult.error(() -> "some keys are not strings: " + String.valueOf(missed), output); 
/* 191 */     return DataResult.success(output);
/*     */   }
/*     */   
/*     */   public DataResult<Tag> mergeToMap(Tag map, Map<Tag, Tag> values) {
/* 196 */     if (!(map instanceof CompoundTag) && !(map instanceof EndTag))
/* 197 */       return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(map), map); 
/* 200 */     if (values.isEmpty()) {
/* 201 */       if (map == empty())
/* 202 */         return DataResult.success(emptyMap()); 
/* 204 */       return DataResult.success(map);
/*     */     } 
/* 207 */     CompoundTag tag = (CompoundTag)map, output = (map instanceof CompoundTag) ? tag.shallowCopy() : new CompoundTag();
/* 209 */     List<Tag> missed = new ArrayList<>();
/* 210 */     Iterator<Map.Entry<Tag, Tag>> iterator = values.entrySet().iterator();
/*     */     while (true) {
/* 210 */       if (iterator.hasNext()) {
/* 210 */         Map.Entry<Tag, Tag> entry = iterator.next();
/* 211 */         Tag key = entry.getKey();
/* 212 */         if (key instanceof StringTag) {
/* 212 */           StringTag stringTag = (StringTag)key;
/*     */           try {
/* 212 */             String str1 = stringTag.value(), stringKey = str1;
/* 213 */             output.put(stringKey, entry.getValue());
/*     */           } catch (Throwable throwable) {
/*     */             throw new MatchException(throwable.toString(), throwable);
/*     */           } 
/*     */           continue;
/*     */         } 
/* 215 */         missed.add(key);
/*     */         continue;
/*     */       } 
/* 219 */       if (!missed.isEmpty())
/* 220 */         return DataResult.error(() -> "some keys are not strings: " + String.valueOf(missed), output); 
/* 223 */       return DataResult.success(output);
/*     */     } 
/*     */   }
/*     */   
/*     */   public DataResult<Stream<Pair<Tag, Tag>>> getMapValues(Tag input) {
/* 228 */     if (input instanceof CompoundTag) {
/* 228 */       CompoundTag tag = (CompoundTag)input;
/* 229 */       return DataResult.success(tag.entrySet().stream().map(entry -> Pair.of(createString((String)entry.getKey()), entry.getValue())));
/*     */     } 
/* 231 */     return DataResult.error(() -> "Not a map: " + String.valueOf(input));
/*     */   }
/*     */   
/*     */   public DataResult<Consumer<BiConsumer<Tag, Tag>>> getMapEntries(Tag input) {
/* 236 */     if (input instanceof CompoundTag) {
/* 236 */       CompoundTag tag = (CompoundTag)input;
/* 237 */       return DataResult.success(c -> {
/*     */             for (Map.Entry<String, Tag> entry : tag.entrySet())
/*     */               c.accept(createString(entry.getKey()), entry.getValue()); 
/*     */           });
/*     */     } 
/* 243 */     return DataResult.error(() -> "Not a map: " + String.valueOf(input));
/*     */   }
/*     */   
/*     */   public DataResult<MapLike<Tag>> getMap(Tag input) {
/* 248 */     if (input instanceof CompoundTag) {
/* 248 */       final CompoundTag tag = (CompoundTag)input;
/* 249 */       return DataResult.success(new MapLike<Tag>() {
/*     */             public Tag get(Tag key) {
/* 252 */               if (key instanceof StringTag) {
/* 252 */                 StringTag stringTag = (StringTag)key;
/*     */                 try {
/* 252 */                   String str1 = stringTag.value(), stringKey = str1;
/* 253 */                   return tag.get(stringKey);
/*     */                 } catch (Throwable throwable) {
/*     */                   throw new MatchException(throwable.toString(), throwable);
/*     */                 } 
/*     */               } 
/* 255 */               throw new UnsupportedOperationException("Cannot get map entry with non-string key: " + String.valueOf(key));
/*     */             }
/*     */             
/*     */             public Tag get(String key) {
/* 260 */               return tag.get(key);
/*     */             }
/*     */             
/*     */             public Stream<Pair<Tag, Tag>> entries() {
/* 265 */               return tag.entrySet().stream().map(entry -> Pair.of(NbtOps.this.createString((String)entry.getKey()), entry.getValue()));
/*     */             }
/*     */             
/*     */             public String toString() {
/* 270 */               return "MapLike[" + String.valueOf(tag) + "]";
/*     */             }
/*     */           });
/*     */     } 
/* 274 */     return DataResult.error(() -> "Not a map: " + String.valueOf(input));
/*     */   }
/*     */   
/*     */   public Tag createMap(Stream<Pair<Tag, Tag>> map) {
/* 279 */     CompoundTag tag = new CompoundTag();
/* 280 */     map.forEach(entry -> {
/*     */           Tag key = (Tag)entry.getFirst();
/*     */           Tag value = (Tag)entry.getSecond();
/*     */           if (key instanceof StringTag) {
/*     */             StringTag $b$0 = (StringTag)key;
/*     */             try {
/*     */               String patt1$temp = $b$0.value();
/*     */               String stringKey = patt1$temp;
/*     */               tag.put(stringKey, value);
/* 283 */             } catch (Throwable throwable) {
/*     */               throw new MatchException(throwable.toString(), throwable);
/*     */             } 
/*     */           } else {
/*     */             throw new UnsupportedOperationException("Cannot create map with non-string key: " + String.valueOf(key));
/*     */           } 
/*     */         });
/* 289 */     return tag;
/*     */   }
/*     */   
/*     */   public DataResult<Stream<Tag>> getStream(Tag input) {
/* 294 */     if (input instanceof CollectionTag) {
/* 294 */       CollectionTag collection = (CollectionTag)input;
/* 295 */       return DataResult.success(collection.stream());
/*     */     } 
/* 297 */     return DataResult.error(() -> "Not a list");
/*     */   }
/*     */   
/*     */   public DataResult<Consumer<Consumer<Tag>>> getList(Tag input) {
/* 302 */     if (input instanceof CollectionTag) {
/* 302 */       CollectionTag collection = (CollectionTag)input;
/* 303 */       Objects.requireNonNull(collection);
/* 303 */       return DataResult.success(collection::forEach);
/*     */     } 
/* 305 */     return DataResult.error(() -> "Not a list: " + String.valueOf(input));
/*     */   }
/*     */   
/*     */   public DataResult<ByteBuffer> getByteBuffer(Tag input) {
/* 310 */     if (input instanceof ByteArrayTag) {
/* 310 */       ByteArrayTag array = (ByteArrayTag)input;
/* 311 */       return DataResult.success(ByteBuffer.wrap(array.getAsByteArray()));
/*     */     } 
/* 313 */     return super.getByteBuffer(input);
/*     */   }
/*     */   
/*     */   public Tag createByteList(ByteBuffer input) {
/* 319 */     ByteBuffer wholeBuffer = input.duplicate().clear();
/* 321 */     byte[] bytes = new byte[input.capacity()];
/* 322 */     wholeBuffer.get(0, bytes, 0, bytes.length);
/* 323 */     return new ByteArrayTag(bytes);
/*     */   }
/*     */   
/*     */   public DataResult<IntStream> getIntStream(Tag input) {
/* 328 */     if (input instanceof IntArrayTag) {
/* 328 */       IntArrayTag array = (IntArrayTag)input;
/* 329 */       return DataResult.success(Arrays.stream(array.getAsIntArray()));
/*     */     } 
/* 331 */     return super.getIntStream(input);
/*     */   }
/*     */   
/*     */   public Tag createIntList(IntStream input) {
/* 336 */     return new IntArrayTag(input.toArray());
/*     */   }
/*     */   
/*     */   public DataResult<LongStream> getLongStream(Tag input) {
/* 341 */     if (input instanceof LongArrayTag) {
/* 341 */       LongArrayTag array = (LongArrayTag)input;
/* 342 */       return DataResult.success(Arrays.stream(array.getAsLongArray()));
/*     */     } 
/* 344 */     return super.getLongStream(input);
/*     */   }
/*     */   
/*     */   public Tag createLongList(LongStream input) {
/* 349 */     return new LongArrayTag(input.toArray());
/*     */   }
/*     */   
/*     */   public Tag createList(Stream<Tag> input) {
/* 354 */     return new ListTag(input.collect(Util.toMutableList()));
/*     */   }
/*     */   
/*     */   public Tag remove(Tag input, String key) {
/* 359 */     if (input instanceof CompoundTag) {
/* 359 */       CompoundTag tag = (CompoundTag)input;
/* 360 */       CompoundTag result = tag.shallowCopy();
/* 361 */       result.remove(key);
/* 362 */       return result;
/*     */     } 
/* 364 */     return input;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 369 */     return "NBT";
/*     */   }
/*     */   
/*     */   public RecordBuilder<Tag> mapBuilder() {
/* 374 */     return (RecordBuilder<Tag>)new NbtRecordBuilder(this);
/*     */   }
/*     */   
/*     */   private class NbtRecordBuilder extends RecordBuilder.AbstractStringBuilder<Tag, CompoundTag> {
/*     */     protected NbtRecordBuilder(NbtOps this$0) {
/* 379 */       super(this$0);
/*     */     }
/*     */     
/*     */     protected CompoundTag initBuilder() {
/* 384 */       return new CompoundTag();
/*     */     }
/*     */     
/*     */     protected CompoundTag append(String key, Tag value, CompoundTag builder) {
/* 389 */       builder.put(key, value);
/* 390 */       return builder;
/*     */     }
/*     */     
/*     */     protected DataResult<Tag> build(CompoundTag builder, Tag prefix) {
/* 395 */       if (prefix == null || prefix == EndTag.INSTANCE)
/* 396 */         return DataResult.success(builder); 
/* 398 */       if (prefix instanceof CompoundTag) {
/* 398 */         CompoundTag compound = (CompoundTag)prefix;
/* 399 */         CompoundTag result = compound.shallowCopy();
/* 400 */         for (Map.Entry<String, Tag> entry : builder.entrySet())
/* 401 */           result.put(entry.getKey(), entry.getValue()); 
/* 403 */         return DataResult.success(result);
/*     */       } 
/* 405 */       return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(prefix), prefix);
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface ListCollector {
/*     */     ListCollector accept(Tag param1Tag);
/*     */     
/*     */     default ListCollector acceptAll(Iterable<Tag> tags) {
/* 413 */       ListCollector collector = this;
/* 414 */       for (Tag tag : tags)
/* 415 */         collector = collector.accept(tag); 
/* 417 */       return collector;
/*     */     }
/*     */     
/*     */     Tag result();
/*     */   }
/*     */   
/*     */   private static Optional<ListCollector> createCollector(Tag tag) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: instanceof net/minecraft/nbt/EndTag
/*     */     //   4: ifeq -> 18
/*     */     //   7: new net/minecraft/nbt/NbtOps$GenericListCollector
/*     */     //   10: dup
/*     */     //   11: invokespecial <init> : ()V
/*     */     //   14: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   17: areturn
/*     */     //   18: aload_0
/*     */     //   19: instanceof net/minecraft/nbt/CollectionTag
/*     */     //   22: ifeq -> 197
/*     */     //   25: aload_0
/*     */     //   26: checkcast net/minecraft/nbt/CollectionTag
/*     */     //   29: astore_1
/*     */     //   30: aload_1
/*     */     //   31: invokeinterface isEmpty : ()Z
/*     */     //   36: ifeq -> 50
/*     */     //   39: new net/minecraft/nbt/NbtOps$GenericListCollector
/*     */     //   42: dup
/*     */     //   43: invokespecial <init> : ()V
/*     */     //   46: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   49: areturn
/*     */     //   50: aload_1
/*     */     //   51: dup
/*     */     //   52: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   55: pop
/*     */     //   56: astore_2
/*     */     //   57: iconst_0
/*     */     //   58: istore_3
/*     */     //   59: aload_2
/*     */     //   60: iload_3
/*     */     //   61: <illegal opcode> typeSwitch : (Lnet/minecraft/nbt/CollectionTag;I)I
/*     */     //   66: tableswitch default -> 96, 0 -> 106, 1 -> 127, 2 -> 151, 3 -> 175
/*     */     //   96: new java/lang/MatchException
/*     */     //   99: dup
/*     */     //   100: aconst_null
/*     */     //   101: aconst_null
/*     */     //   102: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   105: athrow
/*     */     //   106: aload_2
/*     */     //   107: checkcast net/minecraft/nbt/ListTag
/*     */     //   110: astore #4
/*     */     //   112: new net/minecraft/nbt/NbtOps$GenericListCollector
/*     */     //   115: dup
/*     */     //   116: aload #4
/*     */     //   118: invokespecial <init> : (Lnet/minecraft/nbt/ListTag;)V
/*     */     //   121: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   124: goto -> 196
/*     */     //   127: aload_2
/*     */     //   128: checkcast net/minecraft/nbt/ByteArrayTag
/*     */     //   131: astore #5
/*     */     //   133: new net/minecraft/nbt/NbtOps$ByteListCollector
/*     */     //   136: dup
/*     */     //   137: aload #5
/*     */     //   139: invokevirtual getAsByteArray : ()[B
/*     */     //   142: invokespecial <init> : ([B)V
/*     */     //   145: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   148: goto -> 196
/*     */     //   151: aload_2
/*     */     //   152: checkcast net/minecraft/nbt/IntArrayTag
/*     */     //   155: astore #6
/*     */     //   157: new net/minecraft/nbt/NbtOps$IntListCollector
/*     */     //   160: dup
/*     */     //   161: aload #6
/*     */     //   163: invokevirtual getAsIntArray : ()[I
/*     */     //   166: invokespecial <init> : ([I)V
/*     */     //   169: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   172: goto -> 196
/*     */     //   175: aload_2
/*     */     //   176: checkcast net/minecraft/nbt/LongArrayTag
/*     */     //   179: astore #7
/*     */     //   181: new net/minecraft/nbt/NbtOps$LongListCollector
/*     */     //   184: dup
/*     */     //   185: aload #7
/*     */     //   187: invokevirtual getAsLongArray : ()[J
/*     */     //   190: invokespecial <init> : ([J)V
/*     */     //   193: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   196: areturn
/*     */     //   197: invokestatic empty : ()Ljava/util/Optional;
/*     */     //   200: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #424	-> 0
/*     */     //   #425	-> 7
/*     */     //   #427	-> 18
/*     */     //   #428	-> 30
/*     */     //   #429	-> 39
/*     */     //   #431	-> 50
/*     */     //   #432	-> 106
/*     */     //   #436	-> 127
/*     */     //   #437	-> 151
/*     */     //   #438	-> 175
/*     */     //   #431	-> 196
/*     */     //   #441	-> 197
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   112	15	4	list	Lnet/minecraft/nbt/ListTag;
/*     */     //   133	18	5	array	Lnet/minecraft/nbt/ByteArrayTag;
/*     */     //   157	18	6	array	Lnet/minecraft/nbt/IntArrayTag;
/*     */     //   181	15	7	array	Lnet/minecraft/nbt/LongArrayTag;
/*     */     //   30	167	1	collection	Lnet/minecraft/nbt/CollectionTag;
/*     */     //   0	201	0	tag	Lnet/minecraft/nbt/Tag;
/*     */   }
/*     */   
/*     */   private static class GenericListCollector implements ListCollector {
/* 445 */     private final ListTag result = new ListTag();
/*     */     
/*     */     private GenericListCollector(ListTag initial) {
/* 451 */       this.result.addAll(initial);
/*     */     }
/*     */     
/*     */     public GenericListCollector(IntArrayList initials) {
/* 455 */       initials.forEach(v -> this.result.add(IntTag.valueOf(v)));
/*     */     }
/*     */     
/*     */     public GenericListCollector(ByteArrayList initials) {
/* 459 */       initials.forEach(v -> this.result.add(ByteTag.valueOf(v)));
/*     */     }
/*     */     
/*     */     public GenericListCollector(LongArrayList initials) {
/* 463 */       initials.forEach(v -> this.result.add(LongTag.valueOf(v)));
/*     */     }
/*     */     
/*     */     public NbtOps.ListCollector accept(Tag tag) {
/* 468 */       this.result.add(tag);
/* 469 */       return this;
/*     */     }
/*     */     
/*     */     public Tag result() {
/* 474 */       return this.result;
/*     */     }
/*     */     
/*     */     private GenericListCollector() {}
/*     */   }
/*     */   
/*     */   private static class IntListCollector implements ListCollector {
/* 479 */     private final IntArrayList values = new IntArrayList();
/*     */     
/*     */     public IntListCollector(int[] initialValues) {
/* 482 */       this.values.addElements(0, initialValues);
/*     */     }
/*     */     
/*     */     public NbtOps.ListCollector accept(Tag tag) {
/* 487 */       if (tag instanceof IntTag) {
/* 487 */         IntTag intTag = (IntTag)tag;
/* 488 */         this.values.add(intTag.intValue());
/* 489 */         return this;
/*     */       } 
/* 491 */       return (new NbtOps.GenericListCollector(this.values)).accept(tag);
/*     */     }
/*     */     
/*     */     public Tag result() {
/* 496 */       return new IntArrayTag(this.values.toIntArray());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ByteListCollector implements ListCollector {
/* 501 */     private final ByteArrayList values = new ByteArrayList();
/*     */     
/*     */     public ByteListCollector(byte[] initialValues) {
/* 504 */       this.values.addElements(0, initialValues);
/*     */     }
/*     */     
/*     */     public NbtOps.ListCollector accept(Tag tag) {
/* 509 */       if (tag instanceof ByteTag) {
/* 509 */         ByteTag byteTag = (ByteTag)tag;
/* 510 */         this.values.add(byteTag.byteValue());
/* 511 */         return this;
/*     */       } 
/* 513 */       return (new NbtOps.GenericListCollector(this.values)).accept(tag);
/*     */     }
/*     */     
/*     */     public Tag result() {
/* 518 */       return new ByteArrayTag(this.values.toByteArray());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LongListCollector implements ListCollector {
/* 523 */     private final LongArrayList values = new LongArrayList();
/*     */     
/*     */     public LongListCollector(long[] initialValues) {
/* 526 */       this.values.addElements(0, initialValues);
/*     */     }
/*     */     
/*     */     public NbtOps.ListCollector accept(Tag tag) {
/* 531 */       if (tag instanceof LongTag) {
/* 531 */         LongTag longTag = (LongTag)tag;
/* 532 */         this.values.add(longTag.longValue());
/* 533 */         return this;
/*     */       } 
/* 535 */       return (new NbtOps.GenericListCollector(this.values)).accept(tag);
/*     */     }
/*     */     
/*     */     public Tag result() {
/* 540 */       return new LongArrayTag(this.values.toLongArray());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\NbtOps.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */