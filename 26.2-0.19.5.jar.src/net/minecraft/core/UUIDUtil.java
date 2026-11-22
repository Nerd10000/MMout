/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.util.UndashedUuid;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public final class UUIDUtil {
/*     */   public static final Codec<UUID> CODEC;
/*     */   
/*     */   static {
/*  25 */     CODEC = Codec.INT_STREAM.comapFlatMap(list -> Util.fixedSize(list, 4).map(UUIDUtil::uuidFromIntArray), uuid -> Arrays.stream(uuidToIntArray(uuid)));
/*     */   }
/*     */   
/*  30 */   public static final Codec<Set<UUID>> CODEC_SET = Codec.list(CODEC).xmap(Sets::newHashSet, Lists::newArrayList);
/*     */   
/*  31 */   public static final Codec<Set<UUID>> CODEC_LINKED_SET = Codec.list(CODEC).xmap(Sets::newLinkedHashSet, Lists::newArrayList);
/*     */   
/*     */   public static final Codec<UUID> STRING_CODEC;
/*     */   
/*     */   public static final Codec<UUID> AUTHLIB_CODEC;
/*     */   
/*     */   static {
/*  33 */     STRING_CODEC = Codec.STRING.comapFlatMap(s -> {
/*     */           try {
/*     */             return DataResult.success(UUID.fromString(s), Lifecycle.stable());
/*  36 */           } catch (IllegalArgumentException e) {
/*     */             return DataResult.error(());
/*     */           } 
/*     */         }UUID::toString);
/*  44 */     AUTHLIB_CODEC = Codec.withAlternative(Codec.STRING
/*  45 */         .comapFlatMap(s -> {
/*     */             try {
/*     */               return DataResult.success(UndashedUuid.fromStringLenient(s), Lifecycle.stable());
/*  49 */             } catch (IllegalArgumentException e) {
/*     */               return DataResult.error(());
/*     */             } 
/*     */           }UndashedUuid::toString), CODEC);
/*     */   }
/*     */   
/*  59 */   public static final Codec<UUID> LENIENT_CODEC = Codec.withAlternative(CODEC, STRING_CODEC);
/*     */   
/*  61 */   public static final StreamCodec<ByteBuf, UUID> STREAM_CODEC = new StreamCodec<ByteBuf, UUID>() {
/*     */       public UUID decode(ByteBuf input) {
/*  64 */         return FriendlyByteBuf.readUUID(input);
/*     */       }
/*     */       
/*     */       public void encode(ByteBuf output, UUID value) {
/*  69 */         FriendlyByteBuf.writeUUID(output, value);
/*     */       }
/*     */     };
/*     */   
/*     */   public static final int UUID_BYTES = 16;
/*     */   
/*     */   private static final String UUID_PREFIX_OFFLINE_PLAYER = "OfflinePlayer:";
/*     */   
/*     */   public static UUID uuidFromIntArray(int[] intArray) {
/*  81 */     return new UUID(intArray[0] << 32L | intArray[1] & 0xFFFFFFFFL, intArray[2] << 32L | intArray[3] & 0xFFFFFFFFL);
/*     */   }
/*     */   
/*     */   public static int[] uuidToIntArray(UUID uuid) {
/*  88 */     long mostSignificantBits = uuid.getMostSignificantBits();
/*  89 */     long leastSignificantBits = uuid.getLeastSignificantBits();
/*  90 */     return leastMostToIntArray(mostSignificantBits, leastSignificantBits);
/*     */   }
/*     */   
/*     */   private static int[] leastMostToIntArray(long mostSignificantBits, long leastSignificantBits) {
/*  94 */     return new int[] { (int)(mostSignificantBits >> 32L), (int)mostSignificantBits, (int)(leastSignificantBits >> 32L), (int)leastSignificantBits };
/*     */   }
/*     */   
/*     */   public static byte[] uuidToByteArray(UUID uuid) {
/* 103 */     byte[] bytes = new byte[16];
/* 104 */     ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN)
/* 105 */       .putLong(uuid.getMostSignificantBits())
/* 106 */       .putLong(uuid.getLeastSignificantBits());
/* 108 */     return bytes;
/*     */   }
/*     */   
/*     */   public static UUID readUUID(Dynamic<?> input) {
/* 112 */     int[] intArray = input.asIntStream().toArray();
/* 113 */     if (intArray.length != 4)
/* 114 */       throw new IllegalArgumentException("Could not read UUID. Expected int-array of length 4, got " + intArray.length + "."); 
/* 116 */     return uuidFromIntArray(intArray);
/*     */   }
/*     */   
/*     */   public static UUID createOfflinePlayerUUID(String playerName) {
/* 120 */     return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8));
/*     */   }
/*     */   
/*     */   public static GameProfile createOfflineProfile(String playerName) {
/* 124 */     UUID id = createOfflinePlayerUUID(playerName);
/* 125 */     return new GameProfile(id, playerName);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\UUIDUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */