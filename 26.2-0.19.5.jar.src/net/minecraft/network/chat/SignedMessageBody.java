/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.google.common.primitives.Ints;
/*    */ import com.google.common.primitives.Longs;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.security.SignatureException;
/*    */ import java.time.Instant;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.SignatureUpdater;
/*    */ 
/*    */ public final class SignedMessageBody extends Record {
/*    */   private final String content;
/*    */   
/*    */   private final Instant timeStamp;
/*    */   
/*    */   private final long salt;
/*    */   
/*    */   private final LastSeenMessages lastSeen;
/*    */   
/*    */   public static final MapCodec<SignedMessageBody> MAP_CODEC;
/*    */   
/*    */   public SignedMessageBody(String content, Instant timeStamp, long salt, LastSeenMessages lastSeen) {
/* 18 */     this.content = content;
/* 18 */     this.timeStamp = timeStamp;
/* 18 */     this.salt = salt;
/* 18 */     this.lastSeen = lastSeen;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/SignedMessageBody;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageBody;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/SignedMessageBody;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageBody;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/SignedMessageBody;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/SignedMessageBody;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String content() {
/* 18 */     return this.content;
/*    */   }
/*    */   
/*    */   public Instant timeStamp() {
/* 18 */     return this.timeStamp;
/*    */   }
/*    */   
/*    */   public long salt() {
/* 18 */     return this.salt;
/*    */   }
/*    */   
/*    */   public LastSeenMessages lastSeen() {
/* 18 */     return this.lastSeen;
/*    */   }
/*    */   
/*    */   static {
/* 19 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("content").forGetter(SignedMessageBody::content), (App)ExtraCodecs.INSTANT_ISO8601.fieldOf("time_stamp").forGetter(SignedMessageBody::timeStamp), (App)Codec.LONG.fieldOf("salt").forGetter(SignedMessageBody::salt), (App)LastSeenMessages.CODEC.optionalFieldOf("last_seen", LastSeenMessages.EMPTY).forGetter(SignedMessageBody::lastSeen)).apply((Applicative)i, SignedMessageBody::new));
/*    */   }
/*    */   
/*    */   public static SignedMessageBody unsigned(String content) {
/* 27 */     return new SignedMessageBody(content, Instant.now(), 0L, LastSeenMessages.EMPTY);
/*    */   }
/*    */   
/*    */   public void updateSignature(SignatureUpdater.Output output) throws SignatureException {
/* 31 */     output.update(Longs.toByteArray(this.salt));
/* 32 */     output.update(Longs.toByteArray(this.timeStamp.getEpochSecond()));
/* 33 */     byte[] contentBytes = this.content.getBytes(StandardCharsets.UTF_8);
/* 34 */     output.update(Ints.toByteArray(contentBytes.length));
/* 35 */     output.update(contentBytes);
/* 36 */     this.lastSeen.updateSignature(output);
/*    */   }
/*    */   
/*    */   public Packed pack(MessageSignatureCache cache) {
/* 40 */     return new Packed(this.content, this.timeStamp, this.salt, this.lastSeen.pack(cache));
/*    */   }
/*    */   
/*    */   public static final class Packed extends Record {
/*    */     private final String content;
/*    */     
/*    */     private final Instant timeStamp;
/*    */     
/*    */     private final long salt;
/*    */     
/*    */     private final LastSeenMessages.Packed lastSeen;
/*    */     
/*    */     public Packed(String content, Instant timeStamp, long salt, LastSeenMessages.Packed lastSeen) {
/* 43 */       this.content = content;
/* 43 */       this.timeStamp = timeStamp;
/* 43 */       this.salt = salt;
/* 43 */       this.lastSeen = lastSeen;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/SignedMessageBody$Packed;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageBody$Packed;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/SignedMessageBody$Packed;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageBody$Packed;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/SignedMessageBody$Packed;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/SignedMessageBody$Packed;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public String content() {
/* 43 */       return this.content;
/*    */     }
/*    */     
/*    */     public Instant timeStamp() {
/* 43 */       return this.timeStamp;
/*    */     }
/*    */     
/*    */     public long salt() {
/* 43 */       return this.salt;
/*    */     }
/*    */     
/*    */     public LastSeenMessages.Packed lastSeen() {
/* 43 */       return this.lastSeen;
/*    */     }
/*    */     
/*    */     public Packed(FriendlyByteBuf input) {
/* 45 */       this(input.readUtf(256), input.readInstant(), input.readLong(), new LastSeenMessages.Packed(input));
/*    */     }
/*    */     
/*    */     public void write(FriendlyByteBuf output) {
/* 49 */       output.writeUtf(this.content, 256);
/* 50 */       output.writeInstant(this.timeStamp);
/* 51 */       output.writeLong(this.salt);
/* 52 */       this.lastSeen.write(output);
/*    */     }
/*    */     
/*    */     public Optional<SignedMessageBody> unpack(MessageSignatureCache cache) {
/* 56 */       return this.lastSeen.unpack(cache).map(lastSeen -> new SignedMessageBody(this.content, this.timeStamp, this.salt, lastSeen));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\SignedMessageBody.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */