/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntArraySet;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.LongStream;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.BitStorage;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.SimpleBitStorage;
/*     */ import net.minecraft.util.ThreadingDetector;
/*     */ import net.minecraft.util.ZeroBitStorage;
/*     */ 
/*     */ public class PalettedContainer<T> implements PaletteResize<T>, PalettedContainerRO<T> {
/*     */   private static final int MIN_PALETTE_BITS = 0;
/*     */   
/*     */   private volatile Data<T> data;
/*     */   
/*     */   private final Strategy<T> strategy;
/*     */   
/*  34 */   private final ThreadingDetector threadingDetector = new ThreadingDetector("PalettedContainer");
/*     */   
/*     */   public void acquire() {
/*  37 */     this.threadingDetector.checkAndLock();
/*     */   }
/*     */   
/*     */   public void release() {
/*  41 */     this.threadingDetector.checkAndUnlock();
/*     */   }
/*     */   
/*     */   public static <T> Codec<PalettedContainer<T>> codecRW(Codec<T> elementCodec, Strategy<T> strategy, T defaultValue) {
/*  45 */     PalettedContainerRO.Unpacker<T, PalettedContainer<T>> unpacker = PalettedContainer::unpack;
/*  46 */     return codec(elementCodec, strategy, defaultValue, unpacker);
/*     */   }
/*     */   
/*     */   public static <T> Codec<PalettedContainerRO<T>> codecRO(Codec<T> elementCodec, Strategy<T> strategy, T defaultValue) {
/*  50 */     PalettedContainerRO.Unpacker<T, PalettedContainerRO<T>> unpacker = (s, data) -> unpack(s, data).map(());
/*  51 */     return codec(elementCodec, strategy, defaultValue, unpacker);
/*     */   }
/*     */   
/*     */   private static <T, C extends PalettedContainerRO<T>> Codec<C> codec(Codec<T> elementCodec, Strategy<T> strategy, T defaultValue, PalettedContainerRO.Unpacker<T, C> unpacker) {
/*  55 */     return RecordCodecBuilder.create(i -> i.group((App)elementCodec.mapResult(ExtraCodecs.orElsePartial(defaultValue)).listOf().fieldOf("palette").forGetter(PalettedContainerRO.PackedData::paletteEntries), (App)Codec.LONG_STREAM.lenientOptionalFieldOf("data").forGetter(PalettedContainerRO.PackedData::storage)).apply((Applicative)i, PackedData::new))
/*     */       
/*  58 */       .comapFlatMap(discData -> unpacker.read(strategy, discData), palettedContainer -> palettedContainer.pack(strategy));
/*     */   }
/*     */   
/*     */   private PalettedContainer(Strategy<T> strategy, Configuration dataConfiguration, BitStorage storage, Palette<T> palette) {
/*  65 */     this.strategy = strategy;
/*  66 */     this.data = new Data<>(dataConfiguration, storage, palette);
/*     */   }
/*     */   
/*     */   private PalettedContainer(PalettedContainer<T> source) {
/*  70 */     this.strategy = source.strategy;
/*  71 */     this.data = source.data.copy();
/*     */   }
/*     */   
/*     */   public PalettedContainer(T initialValue, Strategy<T> strategy) {
/*  75 */     this.strategy = strategy;
/*  76 */     this.data = createOrReuseData(null, 0);
/*  78 */     this.data.palette.idFor(initialValue, this);
/*     */   }
/*     */   
/*     */   private Data<T> createOrReuseData(Data<T> oldData, int targetBits) {
/*  82 */     Configuration dataConfiguration = this.strategy.getConfigurationForBitCount(targetBits);
/*  83 */     if (oldData != null && dataConfiguration.equals(oldData.configuration()))
/*  84 */       return oldData; 
/*  87 */     BitStorage storage = (dataConfiguration.bitsInMemory() == 0) ? (BitStorage)new ZeroBitStorage(this.strategy.entryCount()) : (BitStorage)new SimpleBitStorage(dataConfiguration.bitsInMemory(), this.strategy.entryCount());
/*  88 */     Palette<T> palette = dataConfiguration.createPalette(this.strategy, List.of());
/*  89 */     return new Data<>(dataConfiguration, storage, palette);
/*     */   }
/*     */   
/*     */   public int onResize(int bits, T lastAddedValue) {
/*  94 */     Data<T> oldData = this.data;
/*  95 */     Data<T> newData = createOrReuseData(oldData, bits);
/*  97 */     newData.copyFrom(oldData.palette, oldData.storage);
/*  99 */     this.data = newData;
/* 100 */     return newData.palette.idFor(lastAddedValue, PaletteResize.noResizeExpected());
/*     */   }
/*     */   
/*     */   public T getAndSet(int x, int y, int z, T value) {
/* 104 */     acquire();
/*     */     try {
/* 106 */       return getAndSet(this.strategy.getIndex(x, y, z), value);
/*     */     } finally {
/* 108 */       release();
/*     */     } 
/*     */   }
/*     */   
/*     */   public T getAndSetUnchecked(int x, int y, int z, T value) {
/* 115 */     return getAndSet(this.strategy.getIndex(x, y, z), value);
/*     */   }
/*     */   
/*     */   private T getAndSet(int index, T value) {
/* 119 */     int id = this.data.palette.idFor(value, this);
/* 120 */     int oldId = this.data.storage.getAndSet(index, id);
/* 121 */     return this.data.palette.valueFor(oldId);
/*     */   }
/*     */   
/*     */   public void set(int x, int y, int z, T value) {
/* 125 */     acquire();
/*     */     try {
/* 127 */       set(this.strategy.getIndex(x, y, z), value);
/*     */     } finally {
/* 129 */       release();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void set(int index, T value) {
/* 134 */     int id = this.data.palette.idFor(value, this);
/* 136 */     this.data.storage.set(index, id);
/*     */   }
/*     */   
/*     */   public T get(int x, int y, int z) {
/* 141 */     return get(this.strategy.getIndex(x, y, z));
/*     */   }
/*     */   
/*     */   protected T get(int index) {
/* 146 */     Data<T> data = this.data;
/* 147 */     return data.palette.valueFor(data.storage.get(index));
/*     */   }
/*     */   
/*     */   public void getAll(Consumer<T> consumer) {
/* 152 */     Palette<T> palette = this.data.palette();
/* 153 */     IntArraySet intArraySet = new IntArraySet();
/* 154 */     Objects.requireNonNull(intArraySet);
/* 154 */     this.data.storage.getAll(intArraySet::add);
/* 155 */     intArraySet.forEach(state -> consumer.accept(palette.valueFor(state)));
/*     */   }
/*     */   
/*     */   public void read(FriendlyByteBuf buffer) {
/* 159 */     acquire();
/*     */     try {
/* 161 */       int newBits = buffer.readByte();
/* 163 */       Data<T> newData = createOrReuseData(this.data, newBits);
/* 164 */       newData.palette.read(buffer, this.strategy.globalMap());
/* 165 */       buffer.readFixedSizeLongArray(newData.storage.getRaw());
/* 167 */       this.data = newData;
/*     */     } finally {
/* 169 */       release();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void write(FriendlyByteBuf buffer) {
/* 175 */     acquire();
/*     */     try {
/* 177 */       this.data.write(buffer, this.strategy.globalMap());
/*     */     } finally {
/* 179 */       release();
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static <T> DataResult<PalettedContainer<T>> unpack(Strategy<T> strategy, PalettedContainerRO.PackedData<T> discData) {
/*     */     SimpleBitStorage simpleBitStorage;
/*     */     Palette<T> palette;
/* 185 */     List<T> paletteEntries = discData.paletteEntries();
/* 186 */     int entryCount = strategy.entryCount();
/* 188 */     Configuration storedConfiguration = strategy.getConfigurationForPaletteSize(paletteEntries.size());
/* 189 */     int bitsOnDisc = storedConfiguration.bitsInStorage();
/* 191 */     if (discData.bitsPerEntry() != -1 && bitsOnDisc != discData.bitsPerEntry())
/* 192 */       return DataResult.error(() -> "Invalid bit count, calculated " + bitsOnDisc + ", but container declared " + discData.bitsPerEntry()); 
/* 197 */     if (storedConfiguration.bitsInMemory() == 0) {
/* 198 */       palette = storedConfiguration.createPalette(strategy, paletteEntries);
/* 199 */       ZeroBitStorage zeroBitStorage = new ZeroBitStorage(entryCount);
/*     */     } else {
/* 201 */       Optional<LongStream> dataOpt = discData.storage();
/* 202 */       if (dataOpt.isEmpty())
/* 203 */         return DataResult.error(() -> "Missing values for non-zero storage"); 
/* 205 */       long[] data = ((LongStream)dataOpt.get()).toArray();
/*     */       try {
/* 207 */         if (storedConfiguration.alwaysRepack() || storedConfiguration.bitsInMemory() != bitsOnDisc) {
/* 209 */           Palette<T> oldPalette = new HashMapPalette<>(bitsOnDisc, paletteEntries);
/* 210 */           SimpleBitStorage oldStorage = new SimpleBitStorage(bitsOnDisc, entryCount, data);
/* 212 */           Palette<T> newPalette = storedConfiguration.createPalette(strategy, paletteEntries);
/* 214 */           int[] newContents = reencodeContents((BitStorage)oldStorage, oldPalette, newPalette);
/* 216 */           palette = newPalette;
/* 217 */           simpleBitStorage = new SimpleBitStorage(storedConfiguration.bitsInMemory(), entryCount, newContents);
/*     */         } else {
/* 220 */           palette = storedConfiguration.createPalette(strategy, paletteEntries);
/* 221 */           simpleBitStorage = new SimpleBitStorage(storedConfiguration.bitsInMemory(), entryCount, data);
/*     */         } 
/* 223 */       } catch (net.minecraft.util.SimpleBitStorage.InitializationException exception) {
/* 224 */         return DataResult.error(() -> "Failed to read PalettedContainer: " + exception.getMessage());
/*     */       } 
/*     */     } 
/* 228 */     return DataResult.success(new PalettedContainer<>(strategy, storedConfiguration, (BitStorage)simpleBitStorage, palette));
/*     */   }
/*     */   
/*     */   public PalettedContainerRO.PackedData<T> pack(Strategy<T> strategy) {
/* 233 */     acquire();
/*     */     try {
/*     */       Optional<LongStream> values;
/* 236 */       BitStorage currentStorage = this.data.storage;
/* 237 */       Palette<T> currentPalette = this.data.palette;
/* 239 */       HashMapPalette<T> newPalette = new HashMapPalette<>(currentStorage.getBits());
/* 240 */       int entryCount = strategy.entryCount();
/* 241 */       int[] newContents = reencodeContents(currentStorage, currentPalette, newPalette);
/* 243 */       Configuration storedConfiguration = strategy.getConfigurationForPaletteSize(newPalette.getSize());
/* 246 */       int bitsOnDisc = storedConfiguration.bitsInStorage();
/* 247 */       if (bitsOnDisc != 0) {
/* 248 */         SimpleBitStorage storage = new SimpleBitStorage(bitsOnDisc, entryCount, newContents);
/* 249 */         values = Optional.of(Arrays.stream(storage.getRaw()));
/*     */       } else {
/* 251 */         values = Optional.empty();
/*     */       } 
/* 253 */       return new PalettedContainerRO.PackedData(newPalette.getEntries(), values, bitsOnDisc);
/*     */     } finally {
/* 255 */       release();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T> int[] reencodeContents(BitStorage storage, Palette<T> oldPalette, Palette<T> newPalette) {
/* 260 */     int[] buffer = new int[storage.getSize()];
/* 261 */     storage.unpack(buffer);
/* 263 */     PaletteResize<T> dummyResizer = PaletteResize.noResizeExpected();
/* 265 */     int lastReadId = -1;
/* 266 */     int lastWrittenId = -1;
/* 268 */     for (int index = 0; index < buffer.length; index++) {
/* 269 */       int id = buffer[index];
/* 270 */       if (id != lastReadId) {
/* 271 */         lastReadId = id;
/* 272 */         lastWrittenId = newPalette.idFor(oldPalette.valueFor(id), dummyResizer);
/*     */       } 
/* 274 */       buffer[index] = lastWrittenId;
/*     */     } 
/* 276 */     return buffer;
/*     */   }
/*     */   
/*     */   public int getSerializedSize() {
/* 281 */     return this.data.getSerializedSize(this.strategy.globalMap());
/*     */   }
/*     */   
/*     */   public int bitsPerEntry() {
/* 286 */     return this.data.storage().getBits();
/*     */   }
/*     */   
/*     */   public boolean maybeHas(Predicate<T> predicate) {
/* 291 */     return this.data.palette.maybeHas(predicate);
/*     */   }
/*     */   
/*     */   public void forEachInPalette(Consumer<T> consumer) {
/* 296 */     for (int i = 0; i < this.data.palette.getSize(); i++)
/* 297 */       consumer.accept(this.data.palette.valueFor(i)); 
/*     */   }
/*     */   
/*     */   public PalettedContainer<T> copy() {
/* 303 */     return new PalettedContainer(this);
/*     */   }
/*     */   
/*     */   public PalettedContainer<T> recreate() {
/* 308 */     return new PalettedContainer(this.data.palette.valueFor(0), this.strategy);
/*     */   }
/*     */   
/*     */   public void count(CountConsumer<T> output) {
/* 318 */     if (this.data.palette.getSize() == 1) {
/* 319 */       output.accept(this.data.palette.valueFor(0), this.data.storage.getSize());
/*     */       return;
/*     */     } 
/* 322 */     Int2IntOpenHashMap counts = new Int2IntOpenHashMap();
/* 323 */     this.data.storage.getAll(state -> counts.addTo(state, 1));
/* 324 */     counts.int2IntEntrySet().forEach(entry -> output.accept(this.data.palette.valueFor(entry.getIntKey()), entry.getIntValue()));
/*     */   }
/*     */   
/*     */   private static final class Data<T> extends Record {
/*     */     private final Configuration configuration;
/*     */     
/*     */     private final BitStorage storage;
/*     */     
/*     */     private final Palette<T> palette;
/*     */     
/*     */     private Data(Configuration configuration, BitStorage storage, Palette<T> palette) {
/* 327 */       this.configuration = configuration;
/* 327 */       this.storage = storage;
/* 327 */       this.palette = palette;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/PalettedContainer$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #327	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/PalettedContainer$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #327	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/PalettedContainer$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #327	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data<TT;>;
/*     */     }
/*     */     
/*     */     public Configuration configuration() {
/* 327 */       return this.configuration;
/*     */     }
/*     */     
/*     */     public BitStorage storage() {
/* 327 */       return this.storage;
/*     */     }
/*     */     
/*     */     public Palette<T> palette() {
/* 327 */       return this.palette;
/*     */     }
/*     */     
/*     */     public void copyFrom(Palette<T> oldPalette, BitStorage oldStorage) {
/* 334 */       PaletteResize<T> dummyResizer = PaletteResize.noResizeExpected();
/* 335 */       for (int i = 0; i < oldStorage.getSize(); i++) {
/* 336 */         T value = oldPalette.valueFor(oldStorage.get(i));
/* 337 */         this.storage.set(i, this.palette.idFor(value, dummyResizer));
/*     */       } 
/*     */     }
/*     */     
/*     */     public int getSerializedSize(IdMap<T> globalMap) {
/* 342 */       return 1 + this.palette.getSerializedSize(globalMap) + (this.storage.getRaw()).length * 8;
/*     */     }
/*     */     
/*     */     public void write(FriendlyByteBuf buffer, IdMap<T> globalMap) {
/* 346 */       buffer.writeByte(this.storage.getBits());
/* 347 */       this.palette.write(buffer, globalMap);
/* 348 */       buffer.writeFixedSizeLongArray(this.storage.getRaw());
/*     */     }
/*     */     
/*     */     public Data<T> copy() {
/* 352 */       return new Data(this.configuration, this.storage.copy(), this.palette.copy());
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface CountConsumer<T> {
/*     */     void accept(T param1T, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\PalettedContainer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */