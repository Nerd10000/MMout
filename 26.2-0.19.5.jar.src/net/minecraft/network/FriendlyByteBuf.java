/*      */ package net.minecraft.network;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.gson.Gson;
/*      */ import com.google.gson.JsonElement;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import com.mojang.serialization.JsonOps;
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.ByteBufInputStream;
/*      */ import io.netty.buffer.ByteBufOutputStream;
/*      */ import io.netty.handler.codec.DecoderException;
/*      */ import io.netty.handler.codec.EncoderException;
/*      */ import io.netty.util.ByteProcessor;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*      */ import it.unimi.dsi.fastutil.ints.IntList;
/*      */ import java.io.DataInput;
/*      */ import java.io.DataOutput;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.GatheringByteChannel;
/*      */ import java.nio.channels.ScatteringByteChannel;
/*      */ import java.nio.charset.Charset;
/*      */ import java.security.PublicKey;
/*      */ import java.time.Instant;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.UUID;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.ToIntFunction;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.GlobalPos;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.EndTag;
/*      */ import net.minecraft.nbt.NbtAccounter;
/*      */ import net.minecraft.nbt.NbtIo;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.codec.StreamDecoder;
/*      */ import net.minecraft.network.codec.StreamEncoder;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.util.Crypt;
/*      */ import net.minecraft.util.CryptException;
/*      */ import net.minecraft.util.LenientJsonParser;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import org.joml.Quaternionf;
/*      */ import org.joml.Quaternionfc;
/*      */ import org.joml.Vector3f;
/*      */ import org.joml.Vector3fc;
/*      */ 
/*      */ public class FriendlyByteBuf extends ByteBuf {
/*      */   private final ByteBuf source;
/*      */   
/*      */   public static final short MAX_STRING_LENGTH = 32767;
/*      */   
/*      */   public static final int MAX_COMPONENT_STRING_LENGTH = 262144;
/*      */   
/*      */   private static final int PUBLIC_KEY_SIZE = 256;
/*      */   
/*      */   private static final int MAX_PUBLIC_KEY_HEADER_SIZE = 256;
/*      */   
/*      */   private static final int MAX_PUBLIC_KEY_LENGTH = 512;
/*      */   
/*   84 */   private static final Gson GSON = new Gson();
/*      */   
/*      */   public FriendlyByteBuf(ByteBuf source) {
/*   87 */     this.source = source;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public <T> T readWithCodecTrusted(DynamicOps<Tag> ops, Codec<T> codec) {
/*  100 */     return readWithCodec(ops, codec, NbtAccounter.unlimitedHeap());
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public <T> T readWithCodec(DynamicOps<Tag> ops, Codec<T> codec, NbtAccounter accounter) {
/*  105 */     Tag tag = readNbt(accounter);
/*  106 */     return (T)codec.parse(ops, tag).getOrThrow(msg -> new DecoderException("Failed to decode: " + msg + " " + String.valueOf(tag)));
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public <T> FriendlyByteBuf writeWithCodec(DynamicOps<Tag> ops, Codec<T> codec, T value) {
/*  115 */     Tag tag = (Tag)codec.encodeStart(ops, value).getOrThrow(msg -> new EncoderException("Failed to encode: " + msg + " " + String.valueOf(value)));
/*  116 */     writeNbt(tag);
/*  117 */     return this;
/*      */   }
/*      */   
/*      */   public <T> T readLenientJsonWithCodec(Codec<T> codec) {
/*  121 */     JsonElement json = LenientJsonParser.parse(readUtf());
/*  122 */     DataResult<T> result = codec.parse((DynamicOps)JsonOps.INSTANCE, json);
/*  123 */     return (T)result.getOrThrow(error -> new DecoderException("Failed to decode JSON: " + error));
/*      */   }
/*      */   
/*      */   public <T> void writeJsonWithCodec(Codec<T> codec, T value) {
/*  127 */     DataResult<JsonElement> result = codec.encodeStart((DynamicOps)JsonOps.INSTANCE, value);
/*  128 */     writeUtf(GSON.toJson((JsonElement)result.getOrThrow(error -> new EncoderException("Failed to encode: " + error + " " + String.valueOf(value)))));
/*      */   }
/*      */   
/*      */   public static <T> IntFunction<T> limitValue(IntFunction<T> original, int limit) {
/*  132 */     return value -> {
/*      */         if (value > limit)
/*      */           throw new DecoderException("Value " + value + " is larger than limit " + limit); 
/*      */         return original.apply(value);
/*      */       };
/*      */   }
/*      */   
/*      */   public <T, C extends Collection<T>> C readCollection(IntFunction<C> ctor, StreamDecoder<? super FriendlyByteBuf, T> elementDecoder) {
/*  141 */     int count = readVarInt();
/*  142 */     Collection<Object> collection = (Collection)ctor.apply(count);
/*  143 */     for (int i = 0; i < count; i++)
/*  144 */       collection.add(elementDecoder.decode(this)); 
/*  146 */     return (C)collection;
/*      */   }
/*      */   
/*      */   public <T> void writeCollection(Collection<T> collection, StreamEncoder<? super FriendlyByteBuf, T> encoder) {
/*  150 */     writeVarInt(collection.size());
/*  151 */     for (T element : collection)
/*  152 */       encoder.encode(this, element); 
/*      */   }
/*      */   
/*      */   public <T> List<T> readList(StreamDecoder<? super FriendlyByteBuf, T> elementDecoder) {
/*  157 */     return readCollection(Lists::newArrayListWithCapacity, elementDecoder);
/*      */   }
/*      */   
/*      */   public IntList readIntIdList() {
/*  161 */     int count = readVarInt();
/*  162 */     IntArrayList intArrayList = new IntArrayList();
/*  163 */     for (int i = 0; i < count; i++)
/*  164 */       intArrayList.add(readVarInt()); 
/*  166 */     return (IntList)intArrayList;
/*      */   }
/*      */   
/*      */   public void writeIntIdList(IntList ids) {
/*  170 */     writeVarInt(ids.size());
/*  171 */     ids.forEach(this::writeVarInt);
/*      */   }
/*      */   
/*      */   public <K, V, M extends Map<K, V>> M readMap(IntFunction<M> ctor, StreamDecoder<? super FriendlyByteBuf, K> keyDecoder, StreamDecoder<? super FriendlyByteBuf, V> valueDecoder) {
/*  175 */     int count = readVarInt();
/*  176 */     Map<K, V> map = (Map)ctor.apply(count);
/*  177 */     for (int i = 0; i < count; i++) {
/*  178 */       K key = (K)keyDecoder.decode(this);
/*  179 */       V value = (V)valueDecoder.decode(this);
/*  180 */       map.put(key, value);
/*      */     } 
/*  182 */     return (M)map;
/*      */   }
/*      */   
/*      */   public <K, V> Map<K, V> readMap(StreamDecoder<? super FriendlyByteBuf, K> keyDecoder, StreamDecoder<? super FriendlyByteBuf, V> valueDecoder) {
/*  186 */     return readMap(Maps::newHashMapWithExpectedSize, keyDecoder, valueDecoder);
/*      */   }
/*      */   
/*      */   public <K, V> void writeMap(Map<K, V> map, StreamEncoder<? super FriendlyByteBuf, K> keyEncoder, StreamEncoder<? super FriendlyByteBuf, V> valueEncoder) {
/*  190 */     writeVarInt(map.size());
/*  191 */     map.forEach((k, v) -> {
/*      */           keyEncoder.encode(this, k);
/*      */           valueEncoder.encode(this, v);
/*      */         });
/*      */   }
/*      */   
/*      */   public void readWithCount(Consumer<FriendlyByteBuf> reader) {
/*  198 */     int count = readVarInt();
/*  199 */     for (int i = 0; i < count; i++)
/*  200 */       reader.accept(this); 
/*      */   }
/*      */   
/*      */   public <E extends Enum<E>> void writeEnumSet(EnumSet<E> set, Class<E> clazz) {
/*  205 */     Enum[] arrayOfEnum = (Enum[])clazz.getEnumConstants();
/*  206 */     BitSet mask = new BitSet(arrayOfEnum.length);
/*  207 */     for (int i = 0; i < arrayOfEnum.length; i++)
/*  208 */       mask.set(i, set.contains(arrayOfEnum[i])); 
/*  210 */     writeFixedBitSet(mask, arrayOfEnum.length);
/*      */   }
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> readEnumSet(Class<E> clazz) {
/*  214 */     Enum[] arrayOfEnum = (Enum[])clazz.getEnumConstants();
/*  215 */     BitSet mask = readFixedBitSet(arrayOfEnum.length);
/*  216 */     EnumSet<E> result = EnumSet.noneOf(clazz);
/*  217 */     for (int i = 0; i < arrayOfEnum.length; i++) {
/*  218 */       if (mask.get(i))
/*  219 */         result.add((E)arrayOfEnum[i]); 
/*      */     } 
/*  222 */     return result;
/*      */   }
/*      */   
/*      */   public <T> void writeOptional(Optional<T> value, StreamEncoder<? super FriendlyByteBuf, T> valueWriter) {
/*  226 */     if (value.isPresent()) {
/*  227 */       writeBoolean(true);
/*  228 */       valueWriter.encode(this, value.get());
/*      */     } else {
/*  230 */       writeBoolean(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   public <T> Optional<T> readOptional(StreamDecoder<? super FriendlyByteBuf, T> valueReader) {
/*  235 */     if (readBoolean())
/*  236 */       return Optional.of((T)valueReader.decode(this)); 
/*  238 */     return Optional.empty();
/*      */   }
/*      */   
/*      */   public <L, R> void writeEither(Either<L, R> value, StreamEncoder<? super FriendlyByteBuf, L> leftWriter, StreamEncoder<? super FriendlyByteBuf, R> rightWriter) {
/*  242 */     value.ifLeft(left -> {
/*      */           writeBoolean(true);
/*      */           leftWriter.encode(this, left);
/*  245 */         }).ifRight(right -> {
/*      */           writeBoolean(false);
/*      */           rightWriter.encode(this, right);
/*      */         });
/*      */   }
/*      */   
/*      */   public <L, R> Either<L, R> readEither(StreamDecoder<? super FriendlyByteBuf, L> leftReader, StreamDecoder<? super FriendlyByteBuf, R> rightReader) {
/*  252 */     if (readBoolean())
/*  253 */       return Either.left(leftReader.decode(this)); 
/*  255 */     return Either.right(rightReader.decode(this));
/*      */   }
/*      */   
/*      */   public <T> T readNullable(StreamDecoder<? super FriendlyByteBuf, T> valueDecoder) {
/*  259 */     return readNullable(this, valueDecoder);
/*      */   }
/*      */   
/*      */   public static <T, B extends ByteBuf> T readNullable(B input, StreamDecoder<? super B, T> valueDecoder) {
/*  263 */     if (input.readBoolean())
/*  264 */       return (T)valueDecoder.decode(input); 
/*  266 */     return null;
/*      */   }
/*      */   
/*      */   public <T> void writeNullable(T value, StreamEncoder<? super FriendlyByteBuf, T> valueEncoder) {
/*  270 */     writeNullable(this, value, valueEncoder);
/*      */   }
/*      */   
/*      */   public static <T, B extends ByteBuf> void writeNullable(B output, T value, StreamEncoder<? super B, T> valueEncoder) {
/*  274 */     if (value != null) {
/*  275 */       output.writeBoolean(true);
/*  276 */       valueEncoder.encode(output, value);
/*      */     } else {
/*  278 */       output.writeBoolean(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   public byte[] readByteArray() {
/*  283 */     return readByteArray(this);
/*      */   }
/*      */   
/*      */   public static byte[] readByteArray(ByteBuf input) {
/*  287 */     return readByteArray(input, input.readableBytes());
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeByteArray(byte[] bytes) {
/*  291 */     writeByteArray(this, bytes);
/*  292 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeByteArray(ByteBuf output, byte[] bytes) {
/*  296 */     VarInt.write(output, bytes.length);
/*  297 */     output.writeBytes(bytes);
/*      */   }
/*      */   
/*      */   public byte[] readByteArray(int maxSize) {
/*  301 */     return readByteArray(this, maxSize);
/*      */   }
/*      */   
/*      */   public static byte[] readByteArray(ByteBuf input, int maxSize) {
/*  305 */     int size = VarInt.read(input);
/*  306 */     if (size > maxSize)
/*  307 */       throw new DecoderException("ByteArray with size " + size + " is bigger than allowed " + maxSize); 
/*  309 */     byte[] bytes = new byte[size];
/*  310 */     input.readBytes(bytes);
/*  312 */     return bytes;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeVarIntArray(int[] ints) {
/*  316 */     writeVarInt(ints.length);
/*  318 */     for (int i : ints)
/*  319 */       writeVarInt(i); 
/*  322 */     return this;
/*      */   }
/*      */   
/*      */   public int[] readVarIntArray() {
/*  326 */     return readVarIntArray(readableBytes());
/*      */   }
/*      */   
/*      */   public int[] readVarIntArray(int maxSize) {
/*  330 */     int size = readVarInt();
/*  331 */     if (size > maxSize)
/*  332 */       throw new DecoderException("VarIntArray with size " + size + " is bigger than allowed " + maxSize); 
/*  334 */     int[] ints = new int[size];
/*  336 */     for (int i = 0; i < ints.length; i++)
/*  337 */       ints[i] = readVarInt(); 
/*  340 */     return ints;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeLongArray(long[] longs) {
/*  344 */     writeLongArray(this, longs);
/*  345 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeLongArray(ByteBuf output, long[] longs) {
/*  349 */     VarInt.write(output, longs.length);
/*  350 */     writeFixedSizeLongArray(output, longs);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeFixedSizeLongArray(long[] longs) {
/*  354 */     writeFixedSizeLongArray(this, longs);
/*  355 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeFixedSizeLongArray(ByteBuf output, long[] longs) {
/*  359 */     for (long l : longs)
/*  360 */       output.writeLong(l); 
/*      */   }
/*      */   
/*      */   public long[] readLongArray() {
/*  365 */     return readLongArray(this);
/*      */   }
/*      */   
/*      */   public long[] readFixedSizeLongArray(long[] output) {
/*  369 */     return readFixedSizeLongArray(this, output);
/*      */   }
/*      */   
/*      */   public static long[] readLongArray(ByteBuf input) {
/*  373 */     int size = VarInt.read(input);
/*  374 */     int maxSize = input.readableBytes() / 8;
/*  375 */     if (size > maxSize)
/*  376 */       throw new DecoderException("LongArray with size " + size + " is bigger than allowed " + maxSize); 
/*  378 */     return readFixedSizeLongArray(input, new long[size]);
/*      */   }
/*      */   
/*      */   public static long[] readFixedSizeLongArray(ByteBuf input, long[] output) {
/*  382 */     for (int i = 0; i < output.length; i++)
/*  383 */       output[i] = input.readLong(); 
/*  385 */     return output;
/*      */   }
/*      */   
/*      */   public BlockPos readBlockPos() {
/*  393 */     return readBlockPos(this);
/*      */   }
/*      */   
/*      */   public static BlockPos readBlockPos(ByteBuf input) {
/*  397 */     return BlockPos.of(input.readLong());
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeBlockPos(BlockPos pos) {
/*  401 */     writeBlockPos(this, pos);
/*  402 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeBlockPos(ByteBuf output, BlockPos pos) {
/*  406 */     output.writeLong(pos.asLong());
/*      */   }
/*      */   
/*      */   public ChunkPos readChunkPos() {
/*  410 */     return ChunkPos.unpack(readLong());
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeChunkPos(ChunkPos pos) {
/*  414 */     writeLong(pos.pack());
/*  415 */     return this;
/*      */   }
/*      */   
/*      */   public static ChunkPos readChunkPos(ByteBuf input) {
/*  419 */     return ChunkPos.unpack(input.readLong());
/*      */   }
/*      */   
/*      */   public static void writeChunkPos(ByteBuf output, ChunkPos chunkPos) {
/*  423 */     output.writeLong(chunkPos.pack());
/*      */   }
/*      */   
/*      */   public GlobalPos readGlobalPos() {
/*  427 */     ResourceKey<Level> dimension = readResourceKey(Registries.DIMENSION);
/*  428 */     BlockPos pos = readBlockPos();
/*  429 */     return GlobalPos.of(dimension, pos);
/*      */   }
/*      */   
/*      */   public void writeGlobalPos(GlobalPos globalPos) {
/*  433 */     writeResourceKey(globalPos.dimension());
/*  434 */     writeBlockPos(globalPos.pos());
/*      */   }
/*      */   
/*      */   public Vector3f readVector3f() {
/*  438 */     return readVector3f(this);
/*      */   }
/*      */   
/*      */   public static Vector3f readVector3f(ByteBuf input) {
/*  442 */     return new Vector3f(input.readFloat(), input.readFloat(), input.readFloat());
/*      */   }
/*      */   
/*      */   public void writeVector3f(Vector3f v) {
/*  446 */     writeVector3f(this, (Vector3fc)v);
/*      */   }
/*      */   
/*      */   public static void writeVector3f(ByteBuf output, Vector3fc v) {
/*  450 */     output.writeFloat(v.x());
/*  451 */     output.writeFloat(v.y());
/*  452 */     output.writeFloat(v.z());
/*      */   }
/*      */   
/*      */   public Quaternionf readQuaternion() {
/*  456 */     return readQuaternion(this);
/*      */   }
/*      */   
/*      */   public static Quaternionf readQuaternion(ByteBuf input) {
/*  460 */     return new Quaternionf(input.readFloat(), input.readFloat(), input.readFloat(), input.readFloat());
/*      */   }
/*      */   
/*      */   public void writeQuaternion(Quaternionf q) {
/*  464 */     writeQuaternion(this, (Quaternionfc)q);
/*      */   }
/*      */   
/*      */   public static void writeQuaternion(ByteBuf output, Quaternionfc value) {
/*  468 */     output.writeFloat(value.x());
/*  469 */     output.writeFloat(value.y());
/*  470 */     output.writeFloat(value.z());
/*  471 */     output.writeFloat(value.w());
/*      */   }
/*      */   
/*      */   public <T extends Enum<T>> T readEnum(Class<T> clazz) {
/*  475 */     return (T)((Enum[])clazz.getEnumConstants())[readVarInt()];
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeEnum(Enum<?> value) {
/*  479 */     return writeVarInt(value.ordinal());
/*      */   }
/*      */   
/*      */   public <T> T readById(IntFunction<T> converter) {
/*  483 */     int id = readVarInt();
/*  484 */     return converter.apply(id);
/*      */   }
/*      */   
/*      */   public <T> FriendlyByteBuf writeById(ToIntFunction<T> converter, T value) {
/*  488 */     int id = converter.applyAsInt(value);
/*  489 */     return writeVarInt(id);
/*      */   }
/*      */   
/*      */   public int readVarInt() {
/*  493 */     return VarInt.read(this.source);
/*      */   }
/*      */   
/*      */   public long readVarLong() {
/*  497 */     return VarLong.read(this.source);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeUUID(UUID uuid) {
/*  501 */     writeUUID(this, uuid);
/*  502 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeUUID(ByteBuf output, UUID uuid) {
/*  506 */     output.writeLong(uuid.getMostSignificantBits());
/*  507 */     output.writeLong(uuid.getLeastSignificantBits());
/*      */   }
/*      */   
/*      */   public UUID readUUID() {
/*  511 */     return readUUID(this);
/*      */   }
/*      */   
/*      */   public static UUID readUUID(ByteBuf input) {
/*  515 */     return new UUID(input.readLong(), input.readLong());
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeVarInt(int value) {
/*  519 */     VarInt.write(this.source, value);
/*  520 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeVarLong(long value) {
/*  524 */     VarLong.write(this.source, value);
/*  525 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeNbt(Tag tag) {
/*  529 */     writeNbt(this, tag);
/*  530 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeNbt(ByteBuf output, Tag tag) {
/*      */     EndTag endTag;
/*  534 */     if (tag == null)
/*  535 */       endTag = EndTag.INSTANCE; 
/*      */     try {
/*  539 */       NbtIo.writeAnyTag((Tag)endTag, (DataOutput)new ByteBufOutputStream(output));
/*  540 */     } catch (IOException e) {
/*  541 */       throw new EncoderException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public CompoundTag readNbt() {
/*  546 */     return readNbt(this);
/*      */   }
/*      */   
/*      */   public static CompoundTag readNbt(ByteBuf input) {
/*  550 */     Tag result = readNbt(input, NbtAccounter.defaultQuota());
/*  551 */     if (result == null || result instanceof CompoundTag)
/*  552 */       return (CompoundTag)result; 
/*  554 */     throw new DecoderException("Not a compound tag: " + String.valueOf(result));
/*      */   }
/*      */   
/*      */   public static Tag readNbt(ByteBuf input, NbtAccounter accounter) {
/*      */     try {
/*  559 */       Tag tag = NbtIo.readAnyTag((DataInput)new ByteBufInputStream(input), accounter);
/*  560 */       if (tag.getId() == 0)
/*  561 */         return null; 
/*  563 */       return tag;
/*  564 */     } catch (IOException e) {
/*  565 */       throw new EncoderException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public Tag readNbt(NbtAccounter accounter) {
/*  570 */     return readNbt(this, accounter);
/*      */   }
/*      */   
/*      */   public String readUtf() {
/*  574 */     return readUtf(32767);
/*      */   }
/*      */   
/*      */   public String readUtf(int maxLength) {
/*  578 */     return Utf8String.read(this.source, maxLength);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeUtf(String value) {
/*  582 */     return writeUtf(value, 32767);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeUtf(String value, int maxLength) {
/*  586 */     Utf8String.write(this.source, value, maxLength);
/*  587 */     return this;
/*      */   }
/*      */   
/*      */   public Identifier readIdentifier() {
/*  591 */     return Identifier.parse(readUtf(32767));
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeIdentifier(Identifier identifier) {
/*  595 */     writeUtf(identifier.toString());
/*  596 */     return this;
/*      */   }
/*      */   
/*      */   public <T> ResourceKey<T> readResourceKey(ResourceKey<? extends Registry<T>> registry) {
/*  600 */     Identifier id = readIdentifier();
/*  601 */     return ResourceKey.create(registry, id);
/*      */   }
/*      */   
/*      */   public void writeResourceKey(ResourceKey<?> key) {
/*  605 */     writeIdentifier(key.identifier());
/*      */   }
/*      */   
/*      */   public <T> ResourceKey<? extends Registry<T>> readRegistryKey() {
/*  609 */     Identifier id = readIdentifier();
/*  610 */     return ResourceKey.createRegistryKey(id);
/*      */   }
/*      */   
/*      */   public Instant readInstant() {
/*  614 */     return Instant.ofEpochMilli(readLong());
/*      */   }
/*      */   
/*      */   public void writeInstant(Instant value) {
/*  618 */     writeLong(value.toEpochMilli());
/*      */   }
/*      */   
/*      */   public PublicKey readPublicKey() {
/*      */     try {
/*  623 */       return Crypt.byteToPublicKey(readByteArray(512));
/*  624 */     } catch (CryptException e) {
/*  625 */       throw new DecoderException("Malformed public key bytes", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writePublicKey(PublicKey publicKey) {
/*  630 */     writeByteArray(publicKey.getEncoded());
/*  631 */     return this;
/*      */   }
/*      */   
/*      */   public BlockHitResult readBlockHitResult() {
/*  635 */     BlockPos pos = readBlockPos();
/*  636 */     Direction face = readEnum(Direction.class);
/*  637 */     float clickX = readFloat();
/*  638 */     float clickY = readFloat();
/*  639 */     float clickZ = readFloat();
/*  640 */     boolean inside = readBoolean();
/*  641 */     boolean worldBorder = readBoolean();
/*  643 */     return new BlockHitResult(new Vec3(pos
/*  644 */           .getX() + clickX, pos
/*  645 */           .getY() + clickY, pos
/*  646 */           .getZ() + clickZ), face, pos, inside, worldBorder);
/*      */   }
/*      */   
/*      */   public void writeBlockHitResult(BlockHitResult blockHit) {
/*  655 */     BlockPos blockPos = blockHit.getBlockPos();
/*  656 */     writeBlockPos(blockPos);
/*  657 */     writeEnum((Enum<?>)blockHit.getDirection());
/*  658 */     Vec3 location = blockHit.getLocation();
/*  659 */     writeFloat((float)(location.x - blockPos.getX()));
/*  660 */     writeFloat((float)(location.y - blockPos.getY()));
/*  661 */     writeFloat((float)(location.z - blockPos.getZ()));
/*  662 */     writeBoolean(blockHit.isInside());
/*  663 */     writeBoolean(blockHit.isWorldBorderHit());
/*      */   }
/*      */   
/*      */   public BitSet readBitSet() {
/*  667 */     return BitSet.valueOf(readLongArray());
/*      */   }
/*      */   
/*      */   public void writeBitSet(BitSet bitSet) {
/*  671 */     writeLongArray(bitSet.toLongArray());
/*      */   }
/*      */   
/*      */   public BitSet readFixedBitSet(int size) {
/*  675 */     byte[] bytes = new byte[Mth.positiveCeilDiv(size, 8)];
/*  676 */     readBytes(bytes);
/*  677 */     return BitSet.valueOf(bytes);
/*      */   }
/*      */   
/*      */   public void writeFixedBitSet(BitSet bitSet, int size) {
/*  681 */     if (bitSet.length() > size)
/*  682 */       throw new EncoderException("BitSet is larger than expected size (" + bitSet.length() + ">" + size + ")"); 
/*  684 */     byte[] bytes = bitSet.toByteArray();
/*  685 */     writeBytes(Arrays.copyOf(bytes, Mth.positiveCeilDiv(size, 8)));
/*      */   }
/*      */   
/*      */   public static int readContainerId(ByteBuf input) {
/*  690 */     return VarInt.read(input);
/*      */   }
/*      */   
/*      */   public int readContainerId() {
/*  694 */     return readContainerId(this.source);
/*      */   }
/*      */   
/*      */   public static void writeContainerId(ByteBuf output, int id) {
/*  698 */     VarInt.write(output, id);
/*      */   }
/*      */   
/*      */   public void writeContainerId(int id) {
/*  702 */     writeContainerId(this.source, id);
/*      */   }
/*      */   
/*      */   public boolean isContiguous() {
/*  709 */     return this.source.isContiguous();
/*      */   }
/*      */   
/*      */   public int maxFastWritableBytes() {
/*  714 */     return this.source.maxFastWritableBytes();
/*      */   }
/*      */   
/*      */   public int capacity() {
/*  719 */     return this.source.capacity();
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf capacity(int newCapacity) {
/*  724 */     this.source.capacity(newCapacity);
/*  725 */     return this;
/*      */   }
/*      */   
/*      */   public int maxCapacity() {
/*  730 */     return this.source.maxCapacity();
/*      */   }
/*      */   
/*      */   public ByteBufAllocator alloc() {
/*  735 */     return this.source.alloc();
/*      */   }
/*      */   
/*      */   public ByteOrder order() {
/*  740 */     return this.source.order();
/*      */   }
/*      */   
/*      */   public ByteBuf order(ByteOrder endianness) {
/*  745 */     return this.source.order(endianness);
/*      */   }
/*      */   
/*      */   public ByteBuf unwrap() {
/*  750 */     return this.source;
/*      */   }
/*      */   
/*      */   public boolean isDirect() {
/*  755 */     return this.source.isDirect();
/*      */   }
/*      */   
/*      */   public boolean isReadOnly() {
/*  760 */     return this.source.isReadOnly();
/*      */   }
/*      */   
/*      */   public ByteBuf asReadOnly() {
/*  765 */     return this.source.asReadOnly();
/*      */   }
/*      */   
/*      */   public int readerIndex() {
/*  770 */     return this.source.readerIndex();
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf readerIndex(int readerIndex) {
/*  775 */     this.source.readerIndex(readerIndex);
/*  776 */     return this;
/*      */   }
/*      */   
/*      */   public int writerIndex() {
/*  781 */     return this.source.writerIndex();
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writerIndex(int writerIndex) {
/*  786 */     this.source.writerIndex(writerIndex);
/*  787 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setIndex(int readerIndex, int writerIndex) {
/*  792 */     this.source.setIndex(readerIndex, writerIndex);
/*  793 */     return this;
/*      */   }
/*      */   
/*      */   public int readableBytes() {
/*  798 */     return this.source.readableBytes();
/*      */   }
/*      */   
/*      */   public int writableBytes() {
/*  803 */     return this.source.writableBytes();
/*      */   }
/*      */   
/*      */   public int maxWritableBytes() {
/*  808 */     return this.source.maxWritableBytes();
/*      */   }
/*      */   
/*      */   public boolean isReadable() {
/*  813 */     return this.source.isReadable();
/*      */   }
/*      */   
/*      */   public boolean isReadable(int size) {
/*  818 */     return this.source.isReadable(size);
/*      */   }
/*      */   
/*      */   public boolean isWritable() {
/*  823 */     return this.source.isWritable();
/*      */   }
/*      */   
/*      */   public boolean isWritable(int size) {
/*  828 */     return this.source.isWritable(size);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf clear() {
/*  833 */     this.source.clear();
/*  834 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf markReaderIndex() {
/*  839 */     this.source.markReaderIndex();
/*  840 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf resetReaderIndex() {
/*  845 */     this.source.resetReaderIndex();
/*  846 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf markWriterIndex() {
/*  851 */     this.source.markWriterIndex();
/*  852 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf resetWriterIndex() {
/*  857 */     this.source.resetWriterIndex();
/*  858 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf discardReadBytes() {
/*  863 */     this.source.discardReadBytes();
/*  864 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf discardSomeReadBytes() {
/*  869 */     this.source.discardSomeReadBytes();
/*  870 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf ensureWritable(int minWritableBytes) {
/*  875 */     this.source.ensureWritable(minWritableBytes);
/*  876 */     return this;
/*      */   }
/*      */   
/*      */   public int ensureWritable(int minWritableBytes, boolean force) {
/*  881 */     return this.source.ensureWritable(minWritableBytes, force);
/*      */   }
/*      */   
/*      */   public boolean getBoolean(int index) {
/*  886 */     return this.source.getBoolean(index);
/*      */   }
/*      */   
/*      */   public byte getByte(int index) {
/*  891 */     return this.source.getByte(index);
/*      */   }
/*      */   
/*      */   public short getUnsignedByte(int index) {
/*  896 */     return this.source.getUnsignedByte(index);
/*      */   }
/*      */   
/*      */   public short getShort(int index) {
/*  901 */     return this.source.getShort(index);
/*      */   }
/*      */   
/*      */   public short getShortLE(int index) {
/*  906 */     return this.source.getShortLE(index);
/*      */   }
/*      */   
/*      */   public int getUnsignedShort(int index) {
/*  911 */     return this.source.getUnsignedShort(index);
/*      */   }
/*      */   
/*      */   public int getUnsignedShortLE(int index) {
/*  916 */     return this.source.getUnsignedShortLE(index);
/*      */   }
/*      */   
/*      */   public int getMedium(int index) {
/*  921 */     return this.source.getMedium(index);
/*      */   }
/*      */   
/*      */   public int getMediumLE(int index) {
/*  926 */     return this.source.getMediumLE(index);
/*      */   }
/*      */   
/*      */   public int getUnsignedMedium(int index) {
/*  931 */     return this.source.getUnsignedMedium(index);
/*      */   }
/*      */   
/*      */   public int getUnsignedMediumLE(int index) {
/*  936 */     return this.source.getUnsignedMediumLE(index);
/*      */   }
/*      */   
/*      */   public int getInt(int index) {
/*  941 */     return this.source.getInt(index);
/*      */   }
/*      */   
/*      */   public int getIntLE(int index) {
/*  946 */     return this.source.getIntLE(index);
/*      */   }
/*      */   
/*      */   public long getUnsignedInt(int index) {
/*  951 */     return this.source.getUnsignedInt(index);
/*      */   }
/*      */   
/*      */   public long getUnsignedIntLE(int index) {
/*  956 */     return this.source.getUnsignedIntLE(index);
/*      */   }
/*      */   
/*      */   public long getLong(int index) {
/*  961 */     return this.source.getLong(index);
/*      */   }
/*      */   
/*      */   public long getLongLE(int index) {
/*  966 */     return this.source.getLongLE(index);
/*      */   }
/*      */   
/*      */   public char getChar(int index) {
/*  971 */     return this.source.getChar(index);
/*      */   }
/*      */   
/*      */   public float getFloat(int index) {
/*  976 */     return this.source.getFloat(index);
/*      */   }
/*      */   
/*      */   public double getDouble(int index) {
/*  981 */     return this.source.getDouble(index);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, ByteBuf dst) {
/*  986 */     this.source.getBytes(index, dst);
/*  987 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, ByteBuf dst, int length) {
/*  992 */     this.source.getBytes(index, dst, length);
/*  993 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/*  998 */     this.source.getBytes(index, dst, dstIndex, length);
/*  999 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, byte[] dst) {
/* 1004 */     this.source.getBytes(index, dst);
/* 1005 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/* 1010 */     this.source.getBytes(index, dst, dstIndex, length);
/* 1011 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, ByteBuffer dst) {
/* 1016 */     this.source.getBytes(index, dst);
/* 1017 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/* 1022 */     this.source.getBytes(index, out, length);
/* 1023 */     return this;
/*      */   }
/*      */   
/*      */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/* 1028 */     return this.source.getBytes(index, out, length);
/*      */   }
/*      */   
/*      */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/* 1033 */     return this.source.getBytes(index, out, position, length);
/*      */   }
/*      */   
/*      */   public CharSequence getCharSequence(int index, int length, Charset charset) {
/* 1038 */     return this.source.getCharSequence(index, length, charset);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setBoolean(int index, boolean value) {
/* 1043 */     this.source.setBoolean(index, value);
/* 1044 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setByte(int index, int value) {
/* 1049 */     this.source.setByte(index, value);
/* 1050 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setShort(int index, int value) {
/* 1055 */     this.source.setShort(index, value);
/* 1056 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setShortLE(int index, int value) {
/* 1061 */     this.source.setShortLE(index, value);
/* 1062 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setMedium(int index, int value) {
/* 1067 */     this.source.setMedium(index, value);
/* 1068 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setMediumLE(int index, int value) {
/* 1073 */     this.source.setMediumLE(index, value);
/* 1074 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setInt(int index, int value) {
/* 1079 */     this.source.setInt(index, value);
/* 1080 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setIntLE(int index, int value) {
/* 1085 */     this.source.setIntLE(index, value);
/* 1086 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setLong(int index, long value) {
/* 1091 */     this.source.setLong(index, value);
/* 1092 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setLongLE(int index, long value) {
/* 1097 */     this.source.setLongLE(index, value);
/* 1098 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setChar(int index, int value) {
/* 1103 */     this.source.setChar(index, value);
/* 1104 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setFloat(int index, float value) {
/* 1109 */     this.source.setFloat(index, value);
/* 1110 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setDouble(int index, double value) {
/* 1115 */     this.source.setDouble(index, value);
/* 1116 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, ByteBuf src) {
/* 1121 */     this.source.setBytes(index, src);
/* 1122 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, ByteBuf src, int length) {
/* 1127 */     this.source.setBytes(index, src, length);
/* 1128 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/* 1133 */     this.source.setBytes(index, src, srcIndex, length);
/* 1134 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, byte[] src) {
/* 1139 */     this.source.setBytes(index, src);
/* 1140 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 1145 */     this.source.setBytes(index, src, srcIndex, length);
/* 1146 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, ByteBuffer src) {
/* 1151 */     this.source.setBytes(index, src);
/* 1152 */     return this;
/*      */   }
/*      */   
/*      */   public int setBytes(int index, InputStream in, int length) throws IOException {
/* 1157 */     return this.source.setBytes(index, in, length);
/*      */   }
/*      */   
/*      */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/* 1162 */     return this.source.setBytes(index, in, length);
/*      */   }
/*      */   
/*      */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/* 1167 */     return this.source.setBytes(index, in, position, length);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf setZero(int index, int length) {
/* 1172 */     this.source.setZero(index, length);
/* 1173 */     return this;
/*      */   }
/*      */   
/*      */   public int setCharSequence(int index, CharSequence sequence, Charset charset) {
/* 1178 */     return this.source.setCharSequence(index, sequence, charset);
/*      */   }
/*      */   
/*      */   public boolean readBoolean() {
/* 1183 */     return this.source.readBoolean();
/*      */   }
/*      */   
/*      */   public byte readByte() {
/* 1188 */     return this.source.readByte();
/*      */   }
/*      */   
/*      */   public short readUnsignedByte() {
/* 1193 */     return this.source.readUnsignedByte();
/*      */   }
/*      */   
/*      */   public short readShort() {
/* 1198 */     return this.source.readShort();
/*      */   }
/*      */   
/*      */   public short readShortLE() {
/* 1203 */     return this.source.readShortLE();
/*      */   }
/*      */   
/*      */   public int readUnsignedShort() {
/* 1208 */     return this.source.readUnsignedShort();
/*      */   }
/*      */   
/*      */   public int readUnsignedShortLE() {
/* 1213 */     return this.source.readUnsignedShortLE();
/*      */   }
/*      */   
/*      */   public int readMedium() {
/* 1218 */     return this.source.readMedium();
/*      */   }
/*      */   
/*      */   public int readMediumLE() {
/* 1223 */     return this.source.readMediumLE();
/*      */   }
/*      */   
/*      */   public int readUnsignedMedium() {
/* 1228 */     return this.source.readUnsignedMedium();
/*      */   }
/*      */   
/*      */   public int readUnsignedMediumLE() {
/* 1233 */     return this.source.readUnsignedMediumLE();
/*      */   }
/*      */   
/*      */   public int readInt() {
/* 1238 */     return this.source.readInt();
/*      */   }
/*      */   
/*      */   public int readIntLE() {
/* 1243 */     return this.source.readIntLE();
/*      */   }
/*      */   
/*      */   public long readUnsignedInt() {
/* 1248 */     return this.source.readUnsignedInt();
/*      */   }
/*      */   
/*      */   public long readUnsignedIntLE() {
/* 1253 */     return this.source.readUnsignedIntLE();
/*      */   }
/*      */   
/*      */   public long readLong() {
/* 1258 */     return this.source.readLong();
/*      */   }
/*      */   
/*      */   public long readLongLE() {
/* 1263 */     return this.source.readLongLE();
/*      */   }
/*      */   
/*      */   public char readChar() {
/* 1268 */     return this.source.readChar();
/*      */   }
/*      */   
/*      */   public float readFloat() {
/* 1273 */     return this.source.readFloat();
/*      */   }
/*      */   
/*      */   public double readDouble() {
/* 1278 */     return this.source.readDouble();
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(int length) {
/* 1283 */     return this.source.readBytes(length);
/*      */   }
/*      */   
/*      */   public ByteBuf readSlice(int length) {
/* 1288 */     return this.source.readSlice(length);
/*      */   }
/*      */   
/*      */   public ByteBuf readRetainedSlice(int length) {
/* 1293 */     return this.source.readRetainedSlice(length);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf readBytes(ByteBuf dst) {
/* 1298 */     this.source.readBytes(dst);
/* 1299 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf readBytes(ByteBuf dst, int length) {
/* 1304 */     this.source.readBytes(dst, length);
/* 1305 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
/* 1310 */     this.source.readBytes(dst, dstIndex, length);
/* 1311 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf readBytes(byte[] dst) {
/* 1316 */     this.source.readBytes(dst);
/* 1317 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf readBytes(byte[] dst, int dstIndex, int length) {
/* 1322 */     this.source.readBytes(dst, dstIndex, length);
/* 1323 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf readBytes(ByteBuffer dst) {
/* 1328 */     this.source.readBytes(dst);
/* 1329 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf readBytes(OutputStream out, int length) throws IOException {
/* 1334 */     this.source.readBytes(out, length);
/* 1335 */     return this;
/*      */   }
/*      */   
/*      */   public int readBytes(GatheringByteChannel out, int length) throws IOException {
/* 1340 */     return this.source.readBytes(out, length);
/*      */   }
/*      */   
/*      */   public CharSequence readCharSequence(int length, Charset charset) {
/* 1345 */     return this.source.readCharSequence(length, charset);
/*      */   }
/*      */   
/*      */   public String readString(int length, Charset charset) {
/* 1350 */     return this.source.readString(length, charset);
/*      */   }
/*      */   
/*      */   public int readBytes(FileChannel out, long position, int length) throws IOException {
/* 1355 */     return this.source.readBytes(out, position, length);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf skipBytes(int length) {
/* 1360 */     this.source.skipBytes(length);
/* 1361 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeBoolean(boolean value) {
/* 1366 */     this.source.writeBoolean(value);
/* 1367 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeByte(int value) {
/* 1372 */     this.source.writeByte(value);
/* 1373 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeShort(int value) {
/* 1378 */     this.source.writeShort(value);
/* 1379 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeShortLE(int value) {
/* 1384 */     this.source.writeShortLE(value);
/* 1385 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeMedium(int value) {
/* 1390 */     this.source.writeMedium(value);
/* 1391 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeMediumLE(int value) {
/* 1396 */     this.source.writeMediumLE(value);
/* 1397 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeInt(int value) {
/* 1402 */     this.source.writeInt(value);
/* 1403 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeIntLE(int value) {
/* 1408 */     this.source.writeIntLE(value);
/* 1409 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeLong(long value) {
/* 1414 */     this.source.writeLong(value);
/* 1415 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeLongLE(long value) {
/* 1420 */     this.source.writeLongLE(value);
/* 1421 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeChar(int value) {
/* 1426 */     this.source.writeChar(value);
/* 1427 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeFloat(float value) {
/* 1432 */     this.source.writeFloat(value);
/* 1433 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeDouble(double value) {
/* 1438 */     this.source.writeDouble(value);
/* 1439 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeBytes(ByteBuf src) {
/* 1444 */     this.source.writeBytes(src);
/* 1445 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeBytes(ByteBuf src, int length) {
/* 1450 */     this.source.writeBytes(src, length);
/* 1451 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
/* 1456 */     this.source.writeBytes(src, srcIndex, length);
/* 1457 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeBytes(byte[] src) {
/* 1462 */     this.source.writeBytes(src);
/* 1463 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeBytes(byte[] src, int srcIndex, int length) {
/* 1468 */     this.source.writeBytes(src, srcIndex, length);
/* 1469 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeBytes(ByteBuffer src) {
/* 1474 */     this.source.writeBytes(src);
/* 1475 */     return this;
/*      */   }
/*      */   
/*      */   public int writeBytes(InputStream in, int length) throws IOException {
/* 1480 */     return this.source.writeBytes(in, length);
/*      */   }
/*      */   
/*      */   public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
/* 1485 */     return this.source.writeBytes(in, length);
/*      */   }
/*      */   
/*      */   public int writeBytes(FileChannel in, long position, int length) throws IOException {
/* 1490 */     return this.source.writeBytes(in, position, length);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeZero(int length) {
/* 1495 */     this.source.writeZero(length);
/* 1496 */     return this;
/*      */   }
/*      */   
/*      */   public int writeCharSequence(CharSequence sequence, Charset charset) {
/* 1501 */     return this.source.writeCharSequence(sequence, charset);
/*      */   }
/*      */   
/*      */   public int indexOf(int fromIndex, int toIndex, byte value) {
/* 1506 */     return this.source.indexOf(fromIndex, toIndex, value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(byte value) {
/* 1511 */     return this.source.bytesBefore(value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(int length, byte value) {
/* 1516 */     return this.source.bytesBefore(length, value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(int index, int length, byte value) {
/* 1521 */     return this.source.bytesBefore(index, length, value);
/*      */   }
/*      */   
/*      */   public int forEachByte(ByteProcessor processor) {
/* 1526 */     return this.source.forEachByte(processor);
/*      */   }
/*      */   
/*      */   public int forEachByte(int index, int length, ByteProcessor processor) {
/* 1531 */     return this.source.forEachByte(index, length, processor);
/*      */   }
/*      */   
/*      */   public int forEachByteDesc(ByteProcessor processor) {
/* 1536 */     return this.source.forEachByteDesc(processor);
/*      */   }
/*      */   
/*      */   public int forEachByteDesc(int index, int length, ByteProcessor processor) {
/* 1541 */     return this.source.forEachByteDesc(index, length, processor);
/*      */   }
/*      */   
/*      */   public ByteBuf copy() {
/* 1546 */     return this.source.copy();
/*      */   }
/*      */   
/*      */   public ByteBuf copy(int index, int length) {
/* 1551 */     return this.source.copy(index, length);
/*      */   }
/*      */   
/*      */   public ByteBuf slice() {
/* 1556 */     return this.source.slice();
/*      */   }
/*      */   
/*      */   public ByteBuf retainedSlice() {
/* 1561 */     return this.source.retainedSlice();
/*      */   }
/*      */   
/*      */   public ByteBuf slice(int index, int length) {
/* 1566 */     return this.source.slice(index, length);
/*      */   }
/*      */   
/*      */   public ByteBuf retainedSlice(int index, int length) {
/* 1571 */     return this.source.retainedSlice(index, length);
/*      */   }
/*      */   
/*      */   public ByteBuf duplicate() {
/* 1576 */     return this.source.duplicate();
/*      */   }
/*      */   
/*      */   public ByteBuf retainedDuplicate() {
/* 1581 */     return this.source.retainedDuplicate();
/*      */   }
/*      */   
/*      */   public int nioBufferCount() {
/* 1586 */     return this.source.nioBufferCount();
/*      */   }
/*      */   
/*      */   public ByteBuffer nioBuffer() {
/* 1591 */     return this.source.nioBuffer();
/*      */   }
/*      */   
/*      */   public ByteBuffer nioBuffer(int index, int length) {
/* 1596 */     return this.source.nioBuffer(index, length);
/*      */   }
/*      */   
/*      */   public ByteBuffer internalNioBuffer(int index, int length) {
/* 1601 */     return this.source.internalNioBuffer(index, length);
/*      */   }
/*      */   
/*      */   public ByteBuffer[] nioBuffers() {
/* 1606 */     return this.source.nioBuffers();
/*      */   }
/*      */   
/*      */   public ByteBuffer[] nioBuffers(int index, int length) {
/* 1611 */     return this.source.nioBuffers(index, length);
/*      */   }
/*      */   
/*      */   public boolean hasArray() {
/* 1616 */     return this.source.hasArray();
/*      */   }
/*      */   
/*      */   public byte[] array() {
/* 1621 */     return this.source.array();
/*      */   }
/*      */   
/*      */   public int arrayOffset() {
/* 1626 */     return this.source.arrayOffset();
/*      */   }
/*      */   
/*      */   public boolean hasMemoryAddress() {
/* 1631 */     return this.source.hasMemoryAddress();
/*      */   }
/*      */   
/*      */   public long memoryAddress() {
/* 1636 */     return this.source.memoryAddress();
/*      */   }
/*      */   
/*      */   public String toString(Charset charset) {
/* 1641 */     return this.source.toString(charset);
/*      */   }
/*      */   
/*      */   public String toString(int index, int length, Charset charset) {
/* 1646 */     return this.source.toString(index, length, charset);
/*      */   }
/*      */   
/*      */   public int hashCode() {
/* 1651 */     return this.source.hashCode();
/*      */   }
/*      */   
/*      */   public boolean equals(Object obj) {
/* 1656 */     return this.source.equals(obj);
/*      */   }
/*      */   
/*      */   public int compareTo(ByteBuf buffer) {
/* 1661 */     return this.source.compareTo(buffer);
/*      */   }
/*      */   
/*      */   public String toString() {
/* 1666 */     return this.source.toString();
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf retain(int increment) {
/* 1671 */     this.source.retain(increment);
/* 1672 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf retain() {
/* 1677 */     this.source.retain();
/* 1678 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf touch() {
/* 1683 */     this.source.touch();
/* 1684 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf touch(Object hint) {
/* 1689 */     this.source.touch(hint);
/* 1690 */     return this;
/*      */   }
/*      */   
/*      */   public int refCnt() {
/* 1695 */     return this.source.refCnt();
/*      */   }
/*      */   
/*      */   public boolean release() {
/* 1700 */     return this.source.release();
/*      */   }
/*      */   
/*      */   public boolean release(int decrement) {
/* 1705 */     return this.source.release(decrement);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\FriendlyByteBuf.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */