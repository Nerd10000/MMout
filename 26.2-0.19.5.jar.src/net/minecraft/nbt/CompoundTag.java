/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class CompoundTag implements Tag {
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final Codec<CompoundTag> CODEC;
/*     */   
/*     */   private static final int SELF_SIZE_IN_BYTES = 48;
/*     */   
/*     */   private static final int MAP_ENTRY_SIZE_IN_BYTES = 32;
/*     */   
/*     */   static {
/*  30 */     CODEC = Codec.PASSTHROUGH.comapFlatMap(t -> {
/*     */           Tag tag = (Tag)t.convert(NbtOps.INSTANCE).getValue();
/*     */           if (tag instanceof CompoundTag) {
/*     */             CompoundTag compoundTag = (CompoundTag)tag;
/*     */             return DataResult.success((compoundTag == t.getValue()) ? compoundTag.copy() : compoundTag);
/*     */           } 
/*     */           return DataResult.error(());
/*     */         }t -> new Dynamic(NbtOps.INSTANCE, t.copy()));
/*     */   }
/*     */   
/*  65 */   public static final TagType<CompoundTag> TYPE = new TagType.VariableSize<CompoundTag>() {
/*     */       public CompoundTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  68 */         accounter.pushDepth();
/*     */         try {
/*  70 */           return loadCompound(input, accounter);
/*     */         } finally {
/*  72 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */       
/*     */       private static CompoundTag loadCompound(DataInput input, NbtAccounter accounter) throws IOException {
/*  77 */         accounter.accountBytes(48L);
/*  79 */         Map<String, Tag> values = Maps.newHashMap();
/*     */         byte tagType;
/*  81 */         while ((tagType = input.readByte()) != 0) {
/*  82 */           String key = readString(input, accounter);
/*  83 */           Tag tag = CompoundTag.readNamedTagData(TagTypes.getType(tagType), key, input, accounter);
/*  84 */           if (values.put(key, tag) == null)
/*  85 */             accounter.accountBytes(36L); 
/*     */         } 
/*  88 */         return new CompoundTag(values);
/*     */       }
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  93 */         accounter.pushDepth();
/*     */         try {
/*  95 */           return parseCompound(input, output, accounter);
/*     */         } finally {
/*  97 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */       
/*     */       private static StreamTagVisitor.ValueResult parseCompound(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 102 */         accounter.accountBytes(48L);
/*     */         byte tagTypeId;
/* 107 */         while ((tagTypeId = input.readByte()) != 0) {
/* 108 */           TagType<?> tagType = TagTypes.getType(tagTypeId);
/* 110 */           switch (output.visitEntry(tagType)) {
/*     */             case HALT:
/* 112 */               return StreamTagVisitor.ValueResult.HALT;
/*     */             case BREAK:
/* 115 */               StringTag.skipString(input);
/* 116 */               tagType.skip(input, accounter);
/*     */               break;
/*     */             case null:
/* 120 */               StringTag.skipString(input);
/* 121 */               tagType.skip(input, accounter);
/*     */               continue;
/*     */           } 
/* 126 */           String key = readString(input, accounter);
/* 127 */           switch (output.visitEntry(tagType, key)) {
/*     */             case HALT:
/* 129 */               return StreamTagVisitor.ValueResult.HALT;
/*     */             case BREAK:
/* 132 */               tagType.skip(input, accounter);
/*     */               break;
/*     */             case null:
/* 136 */               tagType.skip(input, accounter);
/*     */               continue;
/*     */           } 
/* 141 */           accounter.accountBytes(36L);
/* 142 */           switch (tagType.parse(input, output, accounter)) {
/*     */             case HALT:
/* 144 */               return StreamTagVisitor.ValueResult.HALT;
/*     */           } 
/*     */         } 
/* 151 */         if (tagTypeId != 0)
/* 152 */           while ((tagTypeId = input.readByte()) != 0) {
/* 153 */             StringTag.skipString(input);
/* 154 */             TagTypes.getType(tagTypeId).skip(input, accounter);
/*     */           }  
/* 158 */         return output.visitContainerEnd();
/*     */       }
/*     */       
/*     */       private static String readString(DataInput input, NbtAccounter accounter) throws IOException {
/* 162 */         String key = input.readUTF();
/* 163 */         accounter.accountBytes(28L);
/* 164 */         accounter.accountBytes(2L, key.length());
/* 165 */         return key;
/*     */       }
/*     */       
/*     */       public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 170 */         accounter.pushDepth();
/*     */         try {
/*     */           byte tagTypeId;
/* 173 */           while ((tagTypeId = input.readByte()) != 0) {
/* 174 */             StringTag.skipString(input);
/* 175 */             TagTypes.getType(tagTypeId).skip(input, accounter);
/*     */           } 
/*     */         } finally {
/* 178 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */       
/*     */       public String getName() {
/* 184 */         return "COMPOUND";
/*     */       }
/*     */       
/*     */       public String getPrettyName() {
/* 189 */         return "TAG_Compound";
/*     */       }
/*     */     };
/*     */   
/*     */   private final Map<String, Tag> tags;
/*     */   
/*     */   CompoundTag(Map<String, Tag> tags) {
/* 197 */     this.tags = tags;
/*     */   }
/*     */   
/*     */   public CompoundTag() {
/* 201 */     this(new HashMap<>());
/*     */   }
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/* 206 */     for (String key : this.tags.keySet()) {
/* 207 */       Tag tag = this.tags.get(key);
/* 208 */       writeNamedTag(key, tag, output);
/*     */     } 
/* 210 */     output.writeByte(0);
/*     */   }
/*     */   
/*     */   public int sizeInBytes() {
/* 215 */     int size = 48;
/* 216 */     for (Map.Entry<String, Tag> entry : this.tags.entrySet()) {
/* 217 */       size += 28 + 2 * ((String)entry.getKey()).length();
/* 218 */       size += 36;
/* 219 */       size += ((Tag)entry.getValue()).sizeInBytes();
/*     */     } 
/* 221 */     return size;
/*     */   }
/*     */   
/*     */   public Set<String> keySet() {
/* 225 */     return this.tags.keySet();
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<String, Tag>> entrySet() {
/* 229 */     return this.tags.entrySet();
/*     */   }
/*     */   
/*     */   public Collection<Tag> values() {
/* 233 */     return this.tags.values();
/*     */   }
/*     */   
/*     */   public void forEach(BiConsumer<String, Tag> consumer) {
/* 237 */     this.tags.forEach(consumer);
/*     */   }
/*     */   
/*     */   public byte getId() {
/* 242 */     return 10;
/*     */   }
/*     */   
/*     */   public TagType<CompoundTag> getType() {
/* 247 */     return TYPE;
/*     */   }
/*     */   
/*     */   public int size() {
/* 251 */     return this.tags.size();
/*     */   }
/*     */   
/*     */   public Tag put(String name, Tag tag) {
/* 255 */     return this.tags.put(name, tag);
/*     */   }
/*     */   
/*     */   public void putByte(String name, byte value) {
/* 259 */     this.tags.put(name, ByteTag.valueOf(value));
/*     */   }
/*     */   
/*     */   public void putShort(String name, short value) {
/* 263 */     this.tags.put(name, ShortTag.valueOf(value));
/*     */   }
/*     */   
/*     */   public void putInt(String name, int value) {
/* 267 */     this.tags.put(name, IntTag.valueOf(value));
/*     */   }
/*     */   
/*     */   public void putLong(String name, long value) {
/* 271 */     this.tags.put(name, LongTag.valueOf(value));
/*     */   }
/*     */   
/*     */   public void putFloat(String name, float value) {
/* 275 */     this.tags.put(name, FloatTag.valueOf(value));
/*     */   }
/*     */   
/*     */   public void putDouble(String name, double value) {
/* 279 */     this.tags.put(name, DoubleTag.valueOf(value));
/*     */   }
/*     */   
/*     */   public void putString(String name, String value) {
/* 283 */     this.tags.put(name, StringTag.valueOf(value));
/*     */   }
/*     */   
/*     */   public void putByteArray(String name, byte[] value) {
/* 287 */     this.tags.put(name, new ByteArrayTag(value));
/*     */   }
/*     */   
/*     */   public void putIntArray(String name, int[] value) {
/* 291 */     this.tags.put(name, new IntArrayTag(value));
/*     */   }
/*     */   
/*     */   public void putLongArray(String name, long[] value) {
/* 295 */     this.tags.put(name, new LongArrayTag(value));
/*     */   }
/*     */   
/*     */   public void putBoolean(String name, boolean value) {
/* 299 */     this.tags.put(name, ByteTag.valueOf(value));
/*     */   }
/*     */   
/*     */   public Tag get(String name) {
/* 303 */     return this.tags.get(name);
/*     */   }
/*     */   
/*     */   public boolean contains(String name) {
/* 307 */     return this.tags.containsKey(name);
/*     */   }
/*     */   
/*     */   private Optional<Tag> getOptional(String name) {
/* 311 */     return Optional.ofNullable(this.tags.get(name));
/*     */   }
/*     */   
/*     */   public Optional<Byte> getByte(String name) {
/* 316 */     return getOptional(name).flatMap(Tag::asByte);
/*     */   }
/*     */   
/*     */   public byte getByteOr(String name, byte defaultValue) {
/* 321 */     NumericTag numericTag = (NumericTag)this.tags.get(name);
/* 321 */     if (numericTag instanceof NumericTag) {
/* 321 */       NumericTag tag = numericTag;
/* 322 */       return tag.byteValue();
/*     */     } 
/* 324 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<Short> getShort(String name) {
/* 329 */     return getOptional(name).flatMap(Tag::asShort);
/*     */   }
/*     */   
/*     */   public short getShortOr(String name, short defaultValue) {
/* 333 */     NumericTag numericTag = (NumericTag)this.tags.get(name);
/* 333 */     if (numericTag instanceof NumericTag) {
/* 333 */       NumericTag tag = numericTag;
/* 334 */       return tag.shortValue();
/*     */     } 
/* 336 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<Integer> getInt(String name) {
/* 341 */     return getOptional(name).flatMap(Tag::asInt);
/*     */   }
/*     */   
/*     */   public int getIntOr(String name, int defaultValue) {
/* 345 */     NumericTag numericTag = (NumericTag)this.tags.get(name);
/* 345 */     if (numericTag instanceof NumericTag) {
/* 345 */       NumericTag tag = numericTag;
/* 346 */       return tag.intValue();
/*     */     } 
/* 348 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<Long> getLong(String name) {
/* 353 */     return getOptional(name).flatMap(Tag::asLong);
/*     */   }
/*     */   
/*     */   public long getLongOr(String name, long defaultValue) {
/* 357 */     NumericTag numericTag = (NumericTag)this.tags.get(name);
/* 357 */     if (numericTag instanceof NumericTag) {
/* 357 */       NumericTag tag = numericTag;
/* 358 */       return tag.longValue();
/*     */     } 
/* 360 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<Float> getFloat(String name) {
/* 365 */     return getOptional(name).flatMap(Tag::asFloat);
/*     */   }
/*     */   
/*     */   public float getFloatOr(String name, float defaultValue) {
/* 369 */     NumericTag numericTag = (NumericTag)this.tags.get(name);
/* 369 */     if (numericTag instanceof NumericTag) {
/* 369 */       NumericTag tag = numericTag;
/* 370 */       return tag.floatValue();
/*     */     } 
/* 372 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<Double> getDouble(String name) {
/* 377 */     return getOptional(name).flatMap(Tag::asDouble);
/*     */   }
/*     */   
/*     */   public double getDoubleOr(String name, double defaultValue) {
/* 381 */     NumericTag numericTag = (NumericTag)this.tags.get(name);
/* 381 */     if (numericTag instanceof NumericTag) {
/* 381 */       NumericTag tag = numericTag;
/* 382 */       return tag.doubleValue();
/*     */     } 
/* 384 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<String> getString(String name) {
/* 389 */     return getOptional(name).flatMap(Tag::asString);
/*     */   }
/*     */   
/*     */   public String getStringOr(String name, String defaultValue) {
/* 393 */     StringTag stringTag = (StringTag)this.tags.get(name);
/* 393 */     if (stringTag instanceof StringTag) {
/* 393 */       StringTag stringTag1 = stringTag;
/*     */       try {
/* 393 */         String str1 = stringTag1.value(), value = str1;
/* 394 */         return value;
/*     */       } catch (Throwable throwable) {
/*     */         throw new MatchException(throwable.toString(), throwable);
/*     */       } 
/*     */     } 
/* 396 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<byte[]> getByteArray(String name) {
/* 400 */     ByteArrayTag byteArrayTag = (ByteArrayTag)this.tags.get(name);
/* 400 */     if (byteArrayTag instanceof ByteArrayTag) {
/* 400 */       ByteArrayTag tag = byteArrayTag;
/* 401 */       return (Optional)Optional.of(tag.getAsByteArray());
/*     */     } 
/* 403 */     return (Optional)Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<int[]> getIntArray(String name) {
/* 407 */     IntArrayTag intArrayTag = (IntArrayTag)this.tags.get(name);
/* 407 */     if (intArrayTag instanceof IntArrayTag) {
/* 407 */       IntArrayTag tag = intArrayTag;
/* 408 */       return (Optional)Optional.of(tag.getAsIntArray());
/*     */     } 
/* 410 */     return (Optional)Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<long[]> getLongArray(String name) {
/* 414 */     LongArrayTag longArrayTag = (LongArrayTag)this.tags.get(name);
/* 414 */     if (longArrayTag instanceof LongArrayTag) {
/* 414 */       LongArrayTag tag = longArrayTag;
/* 415 */       return (Optional)Optional.of(tag.getAsLongArray());
/*     */     } 
/* 417 */     return (Optional)Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<CompoundTag> getCompound(String name) {
/* 421 */     CompoundTag compoundTag = (CompoundTag)this.tags.get(name);
/* 421 */     if (compoundTag instanceof CompoundTag) {
/* 421 */       CompoundTag tag = compoundTag;
/* 422 */       return Optional.of(tag);
/*     */     } 
/* 424 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public CompoundTag getCompoundOrEmpty(String name) {
/* 428 */     return getCompound(name).orElseGet(CompoundTag::new);
/*     */   }
/*     */   
/*     */   public Optional<ListTag> getList(String name) {
/* 432 */     ListTag listTag = (ListTag)this.tags.get(name);
/* 432 */     if (listTag instanceof ListTag) {
/* 432 */       ListTag tag = listTag;
/* 433 */       return Optional.of(tag);
/*     */     } 
/* 435 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public ListTag getListOrEmpty(String name) {
/* 439 */     return getList(name).orElseGet(ListTag::new);
/*     */   }
/*     */   
/*     */   public Optional<Boolean> getBoolean(String name) {
/* 444 */     return getOptional(name).flatMap(Tag::asBoolean);
/*     */   }
/*     */   
/*     */   public boolean getBooleanOr(String string, boolean defaultValue) {
/* 448 */     return (getByteOr(string, defaultValue ? 1 : 0) != 0);
/*     */   }
/*     */   
/*     */   public Tag remove(String name) {
/* 452 */     return this.tags.remove(name);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 457 */     StringTagVisitor visitor = new StringTagVisitor();
/* 458 */     visitor.visitCompound(this);
/* 459 */     return visitor.build();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 463 */     return this.tags.isEmpty();
/*     */   }
/*     */   
/*     */   CompoundTag shallowCopy() {
/* 468 */     return new CompoundTag(new HashMap<>(this.tags));
/*     */   }
/*     */   
/*     */   public CompoundTag copy() {
/* 474 */     HashMap<String, Tag> newTags = new HashMap<>();
/* 475 */     this.tags.forEach((key, tag) -> newTags.put(key, tag.copy()));
/* 476 */     return new CompoundTag(newTags);
/*     */   }
/*     */   
/*     */   public Optional<CompoundTag> asCompound() {
/* 481 */     return Optional.of(this);
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 486 */     if (this == obj)
/* 487 */       return true; 
/* 490 */     if (obj instanceof CompoundTag) {
/* 490 */       CompoundTag compoundTag = (CompoundTag)obj;
/* 490 */       if (Objects.equals(this.tags, compoundTag.tags));
/*     */     } 
/* 490 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 495 */     return this.tags.hashCode();
/*     */   }
/*     */   
/*     */   private static void writeNamedTag(String name, Tag tag, DataOutput output) throws IOException {
/* 499 */     output.writeByte(tag.getId());
/* 500 */     if (tag.getId() == 0)
/*     */       return; 
/* 504 */     output.writeUTF(name);
/* 506 */     tag.write(output);
/*     */   }
/*     */   
/*     */   private static Tag readNamedTagData(TagType<?> type, String name, DataInput input, NbtAccounter accounter) {
/*     */     try {
/* 511 */       return (Tag)type.load(input, accounter);
/* 512 */     } catch (IOException e) {
/* 513 */       CrashReport report = CrashReport.forThrowable(e, "Loading NBT data");
/* 514 */       CrashReportCategory category = report.addCategory("NBT Tag");
/* 515 */       category.setDetail("Tag name", name);
/* 516 */       category.setDetail("Tag type", type.getName());
/* 517 */       throw new ReportedNbtException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompoundTag merge(CompoundTag other) {
/* 533 */     for (String tagName : other.tags.keySet()) {
/* 534 */       Tag otherTag = other.tags.get(tagName);
/* 537 */       if (otherTag instanceof CompoundTag) {
/* 537 */         CompoundTag otherCompound = (CompoundTag)otherTag, compoundTag1 = (CompoundTag)this.tags.get(tagName);
/* 537 */         if (compoundTag1 instanceof CompoundTag) {
/* 537 */           CompoundTag selfCompound = compoundTag1;
/* 538 */           selfCompound.merge(otherCompound);
/*     */           continue;
/*     */         } 
/*     */       } 
/* 540 */       put(tagName, otherTag.copy());
/*     */     } 
/* 543 */     return this;
/*     */   }
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/* 548 */     visitor.visitCompound(this);
/*     */   }
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 553 */     for (Map.Entry<String, Tag> entry : this.tags.entrySet()) {
/* 554 */       Tag value = entry.getValue();
/* 555 */       TagType<?> type = value.getType();
/* 556 */       StreamTagVisitor.EntryResult entryParseResult = visitor.visitEntry(type);
/* 557 */       switch (entryParseResult) {
/*     */         case HALT:
/* 559 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case BREAK:
/* 562 */           return visitor.visitContainerEnd();
/*     */         case null:
/*     */           continue;
/*     */       } 
/* 569 */       entryParseResult = visitor.visitEntry(type, entry.getKey());
/* 570 */       switch (entryParseResult) {
/*     */         case HALT:
/* 572 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case BREAK:
/* 575 */           return visitor.visitContainerEnd();
/*     */         case null:
/*     */           continue;
/*     */       } 
/* 582 */       StreamTagVisitor.ValueResult valueResult = value.accept(visitor);
/* 583 */       switch (valueResult) {
/*     */         case HALT:
/* 585 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case BREAK:
/* 588 */           return visitor.visitContainerEnd();
/*     */       } 
/*     */     } 
/* 592 */     return visitor.visitContainerEnd();
/*     */   }
/*     */   
/*     */   public <T> void store(String name, Codec<T> codec, T value) {
/* 596 */     store(name, codec, NbtOps.INSTANCE, value);
/*     */   }
/*     */   
/*     */   public <T> void storeNullable(String name, Codec<T> codec, T value) {
/* 600 */     if (value != null)
/* 601 */       store(name, codec, value); 
/*     */   }
/*     */   
/*     */   public <T> void store(String name, Codec<T> codec, DynamicOps<Tag> ops, T value) {
/* 606 */     put(name, (Tag)codec.encodeStart(ops, value).getOrThrow());
/*     */   }
/*     */   
/*     */   public <T> void storeNullable(String name, Codec<T> codec, DynamicOps<Tag> ops, T value) {
/* 610 */     if (value != null)
/* 611 */       store(name, codec, ops, value); 
/*     */   }
/*     */   
/*     */   public <T> void store(MapCodec<T> codec, T value) {
/* 616 */     store(codec, NbtOps.INSTANCE, value);
/*     */   }
/*     */   
/*     */   public <T> void store(MapCodec<T> codec, DynamicOps<Tag> ops, T value) {
/* 620 */     merge((CompoundTag)codec.encoder().encodeStart(ops, value).getOrThrow());
/*     */   }
/*     */   
/*     */   public <T> Optional<T> read(String name, Codec<T> codec) {
/* 624 */     return read(name, codec, NbtOps.INSTANCE);
/*     */   }
/*     */   
/*     */   public <T> Optional<T> read(String name, Codec<T> codec, DynamicOps<Tag> ops) {
/* 628 */     Tag tag = get(name);
/* 629 */     if (tag == null)
/* 630 */       return Optional.empty(); 
/* 632 */     return codec.parse(ops, tag)
/* 633 */       .resultOrPartial(error -> LOGGER.error("Failed to read field ({}={}): {}", new Object[] { name, tag, error }));
/*     */   }
/*     */   
/*     */   public <T> Optional<T> read(MapCodec<T> codec) {
/* 637 */     return read(codec, NbtOps.INSTANCE);
/*     */   }
/*     */   
/*     */   public <T> Optional<T> read(MapCodec<T> codec, DynamicOps<Tag> ops) {
/* 641 */     return codec.decode(ops, (MapLike)ops.getMap(this).getOrThrow())
/* 642 */       .resultOrPartial(error -> LOGGER.error("Failed to read value ({}): {}", this, error));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\CompoundTag.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */