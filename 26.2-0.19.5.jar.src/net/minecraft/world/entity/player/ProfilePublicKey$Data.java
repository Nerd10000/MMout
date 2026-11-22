/*     */ package net.minecraft.world.entity.player;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.security.PublicKey;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Arrays;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.Crypt;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.SignatureValidator;
/*     */ 
/*     */ public final class Data extends Record {
/*     */   private final Instant expiresAt;
/*     */   
/*     */   private final PublicKey key;
/*     */   
/*     */   private final byte[] keySignature;
/*     */   
/*     */   private static final int MAX_KEY_SIGNATURE_SIZE = 4096;
/*     */   
/*     */   public static final Codec<Data> CODEC;
/*     */   
/*     */   public Data(Instant expiresAt, PublicKey key, byte[] keySignature) {
/*  46 */     this.expiresAt = expiresAt;
/*  46 */     this.key = key;
/*  46 */     this.keySignature = keySignature;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/ProfilePublicKey$Data;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #46	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfilePublicKey$Data;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/ProfilePublicKey$Data;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #46	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfilePublicKey$Data;
/*     */   }
/*     */   
/*     */   public Instant expiresAt() {
/*  46 */     return this.expiresAt;
/*     */   }
/*     */   
/*     */   public PublicKey key() {
/*  46 */     return this.key;
/*     */   }
/*     */   
/*     */   public byte[] keySignature() {
/*  46 */     return this.keySignature;
/*     */   }
/*     */   
/*     */   static {
/*  50 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.INSTANT_ISO8601.fieldOf("expires_at").forGetter(Data::expiresAt), (App)Crypt.PUBLIC_KEY_CODEC.fieldOf("key").forGetter(Data::key), (App)ExtraCodecs.BASE64_STRING.fieldOf("signature_v2").forGetter(Data::keySignature)).apply((Applicative)i, Data::new));
/*     */   }
/*     */   
/*     */   public Data(FriendlyByteBuf input) {
/*  57 */     this(input
/*  58 */         .readInstant(), input
/*  59 */         .readPublicKey(), input
/*  60 */         .readByteArray(4096));
/*     */   }
/*     */   
/*     */   public void write(FriendlyByteBuf output) {
/*  65 */     output.writeInstant(this.expiresAt);
/*  66 */     output.writePublicKey(this.key);
/*  67 */     output.writeByteArray(this.keySignature);
/*     */   }
/*     */   
/*     */   private boolean validateSignature(SignatureValidator validator, UUID profileId) {
/*  71 */     return validator.validate(signedPayload(profileId), this.keySignature);
/*     */   }
/*     */   
/*     */   private byte[] signedPayload(UUID profileId) {
/*  75 */     byte[] keyBytes = this.key.getEncoded();
/*  76 */     byte[] signedPayload = new byte[24 + keyBytes.length];
/*  78 */     ByteBuffer buffer = ByteBuffer.wrap(signedPayload).order(ByteOrder.BIG_ENDIAN);
/*  79 */     buffer.putLong(profileId.getMostSignificantBits())
/*  80 */       .putLong(profileId.getLeastSignificantBits())
/*  81 */       .putLong(this.expiresAt.toEpochMilli())
/*  82 */       .put(keyBytes);
/*  84 */     return signedPayload;
/*     */   }
/*     */   
/*     */   public boolean hasExpired() {
/*  88 */     return this.expiresAt.isBefore(Instant.now());
/*     */   }
/*     */   
/*     */   public boolean hasExpired(Duration gracePeriod) {
/*  92 */     return this.expiresAt.plus(gracePeriod).isBefore(Instant.now());
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  97 */     if (o instanceof Data) {
/*  97 */       Data data = (Data)o;
/*  98 */       return (this.expiresAt.equals(data.expiresAt) && this.key.equals(data.key) && Arrays.equals(this.keySignature, data.keySignature));
/*     */     } 
/* 100 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\player\ProfilePublicKey$Data.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */