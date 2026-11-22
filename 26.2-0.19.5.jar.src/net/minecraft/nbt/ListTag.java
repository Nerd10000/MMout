/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public final class ListTag extends AbstractList<Tag> implements CollectionTag {
/*     */   private static final String WRAPPER_MARKER = "";
/*     */   
/*     */   private static final int SELF_SIZE_IN_BYTES = 36;
/*     */   
/*  30 */   public static final TagType<ListTag> TYPE = new TagType.VariableSize<ListTag>() {
/*     */       public ListTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  33 */         accounter.pushDepth();
/*     */         try {
/*  35 */           return loadList(input, accounter);
/*     */         } finally {
/*  37 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */       
/*     */       private static ListTag loadList(DataInput input, NbtAccounter accounter) throws IOException {
/*  42 */         accounter.accountBytes(36L);
/*  43 */         byte typeId = input.readByte();
/*  44 */         int count = readListCount(input);
/*  45 */         if (typeId == 0 && count > 0)
/*  46 */           throw new NbtFormatException("Missing type on ListTag"); 
/*  48 */         accounter.accountBytes(4L, count);
/*  49 */         TagType<?> type = TagTypes.getType(typeId);
/*  50 */         ListTag list = new ListTag(new ArrayList<>(count));
/*  51 */         for (int i = 0; i < count; i++)
/*  52 */           list.addAndUnwrap((Tag)type.load(input, accounter)); 
/*  54 */         return list;
/*     */       }
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  59 */         accounter.pushDepth();
/*     */         try {
/*  61 */           return parseList(input, output, accounter);
/*     */         } finally {
/*  63 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */       
/*     */       private static StreamTagVisitor.ValueResult parseList(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  68 */         accounter.accountBytes(36L);
/*  69 */         TagType<?> elementType = TagTypes.getType(input.readByte());
/*  70 */         int count = readListCount(input);
/*  71 */         switch (output.visitList(elementType, count)) {
/*     */           case HALT:
/*  73 */             return StreamTagVisitor.ValueResult.HALT;
/*     */           case BREAK:
/*  76 */             elementType.skip(input, count, accounter);
/*  77 */             return output.visitContainerEnd();
/*     */         } 
/*  81 */         accounter.accountBytes(4L, count);
/*     */         int i;
/*  84 */         for (i = 0; i < count; i++) {
/*  85 */           switch (output.visitElement(elementType, i)) {
/*     */             case HALT:
/*  87 */               return StreamTagVisitor.ValueResult.HALT;
/*     */             case BREAK:
/*  90 */               elementType.skip(input, accounter);
/*     */               break;
/*     */             case SKIP:
/*  94 */               elementType.skip(input, accounter);
/*     */               break;
/*     */             default:
/*  99 */               switch (elementType.parse(input, output, accounter)) {
/*     */                 case HALT:
/* 101 */                   return StreamTagVisitor.ValueResult.HALT;
/*     */                 case BREAK:
/*     */                   break;
/*     */               } 
/*     */               break;
/*     */           } 
/*     */         } 
/* 108 */         int amountToSkip = count - 1 - i;
/* 109 */         if (amountToSkip > 0)
/* 110 */           elementType.skip(input, amountToSkip, accounter); 
/* 112 */         return output.visitContainerEnd();
/*     */       }
/*     */       
/*     */       private static int readListCount(DataInput input) throws IOException {
/* 116 */         int count = input.readInt();
/* 117 */         if (count < 0)
/* 118 */           throw new NbtFormatException("ListTag length cannot be negative: " + count); 
/* 120 */         return count;
/*     */       }
/*     */       
/*     */       public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 125 */         accounter.pushDepth();
/*     */         try {
/* 127 */           TagType<?> type = TagTypes.getType(input.readByte());
/* 128 */           int count = input.readInt();
/* 129 */           type.skip(input, count, accounter);
/*     */         } finally {
/* 131 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */       
/*     */       public String getName() {
/* 137 */         return "LIST";
/*     */       }
/*     */       
/*     */       public String getPrettyName() {
/* 142 */         return "TAG_List";
/*     */       }
/*     */     };
/*     */   
/*     */   private final List<Tag> list;
/*     */   
/*     */   public ListTag() {
/* 149 */     this(new ArrayList<>());
/*     */   }
/*     */   
/*     */   ListTag(List<Tag> list) {
/* 154 */     this.list = list;
/*     */   }
/*     */   
/*     */   private static Tag tryUnwrap(CompoundTag tag) {
/* 158 */     if (tag.size() == 1) {
/* 159 */       Tag value = tag.get("");
/* 160 */       if (value != null)
/* 161 */         return value; 
/*     */     } 
/* 164 */     return tag;
/*     */   }
/*     */   
/*     */   private static boolean isWrapper(CompoundTag tag) {
/* 168 */     return (tag.size() == 1 && tag.contains(""));
/*     */   }
/*     */   
/*     */   private static Tag wrapIfNeeded(byte elementType, Tag tag) {
/* 173 */     if (elementType != 10)
/* 174 */       return tag; 
/* 176 */     if (tag instanceof CompoundTag) {
/* 176 */       CompoundTag compoundTag = (CompoundTag)tag;
/* 176 */       if (!isWrapper(compoundTag))
/* 177 */         return compoundTag; 
/*     */     } 
/* 179 */     return wrapElement(tag);
/*     */   }
/*     */   
/*     */   private static CompoundTag wrapElement(Tag tag) {
/* 184 */     return new CompoundTag(Map.of("", tag));
/*     */   }
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/* 189 */     byte elementType = identifyRawElementType();
/* 190 */     output.writeByte(elementType);
/* 191 */     output.writeInt(this.list.size());
/* 192 */     for (Tag element : this.list)
/* 193 */       wrapIfNeeded(elementType, element).write(output); 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   byte identifyRawElementType() {
/* 200 */     byte homogenousType = 0;
/* 201 */     for (Tag element : this.list) {
/* 202 */       byte elementType = element.getId();
/* 203 */       if (homogenousType == 0) {
/* 204 */         homogenousType = elementType;
/*     */         continue;
/*     */       } 
/* 205 */       if (homogenousType != elementType)
/* 207 */         return 10; 
/*     */     } 
/* 210 */     return homogenousType;
/*     */   }
/*     */   
/*     */   public void addAndUnwrap(Tag tag) {
/* 214 */     if (tag instanceof CompoundTag) {
/* 214 */       CompoundTag compound = (CompoundTag)tag;
/* 215 */       add(tryUnwrap(compound));
/*     */     } else {
/* 217 */       add(tag);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int sizeInBytes() {
/* 223 */     int size = 36;
/* 224 */     size += 4 * this.list.size();
/* 225 */     for (Tag child : this.list)
/* 226 */       size += child.sizeInBytes(); 
/* 228 */     return size;
/*     */   }
/*     */   
/*     */   public byte getId() {
/* 233 */     return 9;
/*     */   }
/*     */   
/*     */   public TagType<ListTag> getType() {
/* 238 */     return TYPE;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 243 */     StringTagVisitor visitor = new StringTagVisitor();
/* 244 */     visitor.visitList(this);
/* 245 */     return visitor.build();
/*     */   }
/*     */   
/*     */   public Tag remove(int index) {
/* 250 */     return this.list.remove(index);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 255 */     return this.list.isEmpty();
/*     */   }
/*     */   
/*     */   public Optional<CompoundTag> getCompound(int index) {
/* 259 */     Tag tag = getNullable(index);
/* 259 */     if (tag instanceof CompoundTag) {
/* 259 */       CompoundTag compoundTag = (CompoundTag)tag;
/* 260 */       return Optional.of(compoundTag);
/*     */     } 
/* 262 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public CompoundTag getCompoundOrEmpty(int index) {
/* 266 */     return getCompound(index).orElseGet(CompoundTag::new);
/*     */   }
/*     */   
/*     */   public Optional<ListTag> getList(int index) {
/* 270 */     Tag tag = getNullable(index);
/* 270 */     if (tag instanceof ListTag) {
/* 270 */       ListTag listTag = (ListTag)tag;
/* 271 */       return Optional.of(listTag);
/*     */     } 
/* 273 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public ListTag getListOrEmpty(int index) {
/* 277 */     return getList(index).orElseGet(ListTag::new);
/*     */   }
/*     */   
/*     */   public Optional<Short> getShort(int index) {
/* 282 */     return getOptional(index).flatMap(Tag::asShort);
/*     */   }
/*     */   
/*     */   public short getShortOr(int index, short defaultValue) {
/* 286 */     Tag tag = getNullable(index);
/* 286 */     if (tag instanceof NumericTag) {
/* 286 */       NumericTag numericTag = (NumericTag)tag;
/* 287 */       return numericTag.shortValue();
/*     */     } 
/* 289 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<Integer> getInt(int index) {
/* 294 */     return getOptional(index).flatMap(Tag::asInt);
/*     */   }
/*     */   
/*     */   public int getIntOr(int index, int defaultValue) {
/* 298 */     Tag tag = getNullable(index);
/* 298 */     if (tag instanceof NumericTag) {
/* 298 */       NumericTag numericTag = (NumericTag)tag;
/* 299 */       return numericTag.intValue();
/*     */     } 
/* 301 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<int[]> getIntArray(int index) {
/* 305 */     Tag tag = getNullable(index);
/* 305 */     if (tag instanceof IntArrayTag) {
/* 305 */       IntArrayTag intArrayTag = (IntArrayTag)tag;
/* 306 */       return (Optional)Optional.of(intArrayTag.getAsIntArray());
/*     */     } 
/* 308 */     return (Optional)Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<long[]> getLongArray(int index) {
/* 312 */     Tag tag = getNullable(index);
/* 312 */     if (tag instanceof LongArrayTag) {
/* 312 */       LongArrayTag longArrayTag = (LongArrayTag)tag;
/* 313 */       return (Optional)Optional.of(longArrayTag.getAsLongArray());
/*     */     } 
/* 315 */     return (Optional)Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<Double> getDouble(int index) {
/* 320 */     return getOptional(index).flatMap(Tag::asDouble);
/*     */   }
/*     */   
/*     */   public double getDoubleOr(int index, double defaultValue) {
/* 324 */     Tag tag = getNullable(index);
/* 324 */     if (tag instanceof NumericTag) {
/* 324 */       NumericTag numericTag = (NumericTag)tag;
/* 325 */       return numericTag.doubleValue();
/*     */     } 
/* 327 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<Float> getFloat(int index) {
/* 332 */     return getOptional(index).flatMap(Tag::asFloat);
/*     */   }
/*     */   
/*     */   public float getFloatOr(int index, float defaultValue) {
/* 336 */     Tag tag = getNullable(index);
/* 336 */     if (tag instanceof NumericTag) {
/* 336 */       NumericTag numericTag = (NumericTag)tag;
/* 337 */       return numericTag.floatValue();
/*     */     } 
/* 339 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<String> getString(int index) {
/* 344 */     return getOptional(index).flatMap(Tag::asString);
/*     */   }
/*     */   
/*     */   public String getStringOr(int index, String defaultValue) {
/* 348 */     Tag tag = getNullable(index);
/* 349 */     if (tag instanceof StringTag) {
/* 349 */       StringTag stringTag = (StringTag)tag;
/*     */       try {
/* 349 */         String str1 = stringTag.value(), value = str1;
/* 350 */         return value;
/*     */       } catch (Throwable throwable) {
/*     */         throw new MatchException(throwable.toString(), throwable);
/*     */       } 
/*     */     } 
/* 352 */     return defaultValue;
/*     */   }
/*     */   
/*     */   private Tag getNullable(int index) {
/* 356 */     return (index >= 0 && index < this.list.size()) ? this.list.get(index) : null;
/*     */   }
/*     */   
/*     */   private Optional<Tag> getOptional(int index) {
/* 360 */     return Optional.ofNullable(getNullable(index));
/*     */   }
/*     */   
/*     */   public int size() {
/* 365 */     return this.list.size();
/*     */   }
/*     */   
/*     */   public Tag get(int index) {
/* 370 */     return this.list.get(index);
/*     */   }
/*     */   
/*     */   public Tag set(int index, Tag tag) {
/* 375 */     return this.list.set(index, tag);
/*     */   }
/*     */   
/*     */   public void add(int index, Tag tag) {
/* 380 */     this.list.add(index, tag);
/*     */   }
/*     */   
/*     */   public boolean setTag(int index, Tag tag) {
/* 385 */     this.list.set(index, tag);
/* 386 */     return true;
/*     */   }
/*     */   
/*     */   public boolean addTag(int index, Tag tag) {
/* 391 */     this.list.add(index, tag);
/* 392 */     return true;
/*     */   }
/*     */   
/*     */   public ListTag copy() {
/* 397 */     List<Tag> copy = new ArrayList<>(this.list.size());
/* 398 */     for (Tag tag : this.list)
/* 399 */       copy.add(tag.copy()); 
/* 401 */     return new ListTag(copy);
/*     */   }
/*     */   
/*     */   public Optional<ListTag> asList() {
/* 406 */     return Optional.of(this);
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 411 */     if (this == obj)
/* 412 */       return true; 
/* 415 */     if (obj instanceof ListTag) {
/* 415 */       ListTag listTag = (ListTag)obj;
/* 415 */       if (Objects.equals(this.list, listTag.list));
/*     */     } 
/* 415 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 420 */     return this.list.hashCode();
/*     */   }
/*     */   
/*     */   public Stream<Tag> stream() {
/* 425 */     return super.stream();
/*     */   }
/*     */   
/*     */   public Stream<CompoundTag> compoundStream() {
/* 429 */     return stream().mapMulti((tag, output) -> {
/*     */           if (tag instanceof CompoundTag) {
/*     */             CompoundTag compound = (CompoundTag)tag;
/*     */             output.accept(compound);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/* 438 */     visitor.visitList(this);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 443 */     this.list.clear();
/*     */   }
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 448 */     byte elementType = identifyRawElementType();
/* 449 */     switch (visitor.visitList(TagTypes.getType(elementType), this.list.size())) {
/*     */       case HALT:
/* 451 */         return StreamTagVisitor.ValueResult.HALT;
/*     */       case BREAK:
/* 454 */         return visitor.visitContainerEnd();
/*     */     } 
/* 457 */     for (int i = 0; i < this.list.size(); i++) {
/* 458 */       Tag tag = wrapIfNeeded(elementType, this.list.get(i));
/* 459 */       switch (visitor.visitElement(tag.getType(), i)) {
/*     */         case HALT:
/* 461 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case SKIP:
/*     */           break;
/*     */         case BREAK:
/* 467 */           return visitor.visitContainerEnd();
/*     */         default:
/* 470 */           switch (tag.accept(visitor)) {
/*     */             case HALT:
/* 472 */               return StreamTagVisitor.ValueResult.HALT;
/*     */             case BREAK:
/* 475 */               return visitor.visitContainerEnd();
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/* 479 */     return visitor.visitContainerEnd();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\ListTag.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */