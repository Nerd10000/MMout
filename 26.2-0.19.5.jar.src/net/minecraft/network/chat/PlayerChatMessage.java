/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.security.SignatureException;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.SignatureUpdater;
/*     */ import net.minecraft.util.SignatureValidator;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public final class PlayerChatMessage extends Record {
/*     */   private final SignedMessageLink link;
/*     */   
/*     */   private final MessageSignature signature;
/*     */   
/*     */   private final SignedMessageBody signedBody;
/*     */   
/*     */   private final Component unsignedContent;
/*     */   
/*     */   private final FilterMask filterMask;
/*     */   
/*     */   public static final MapCodec<PlayerChatMessage> MAP_CODEC;
/*     */   
/*     */   public PlayerChatMessage(SignedMessageLink link, MessageSignature signature, SignedMessageBody signedBody, Component unsignedContent, FilterMask filterMask) {
/*  20 */     this.link = link;
/*  20 */     this.signature = signature;
/*  20 */     this.signedBody = signedBody;
/*  20 */     this.unsignedContent = unsignedContent;
/*  20 */     this.filterMask = filterMask;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/PlayerChatMessage;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/PlayerChatMessage;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/PlayerChatMessage;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/PlayerChatMessage;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/PlayerChatMessage;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/chat/PlayerChatMessage;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public SignedMessageLink link() {
/*  20 */     return this.link;
/*     */   }
/*     */   
/*     */   public MessageSignature signature() {
/*  20 */     return this.signature;
/*     */   }
/*     */   
/*     */   public SignedMessageBody signedBody() {
/*  20 */     return this.signedBody;
/*     */   }
/*     */   
/*     */   public Component unsignedContent() {
/*  20 */     return this.unsignedContent;
/*     */   }
/*     */   
/*     */   public FilterMask filterMask() {
/*  20 */     return this.filterMask;
/*     */   }
/*     */   
/*     */   static {
/*  21 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SignedMessageLink.CODEC.fieldOf("link").forGetter(PlayerChatMessage::link), (App)MessageSignature.CODEC.optionalFieldOf("signature").forGetter(()), (App)SignedMessageBody.MAP_CODEC.forGetter(PlayerChatMessage::signedBody), (App)ComponentSerialization.CODEC.optionalFieldOf("unsigned_content").forGetter(()), (App)FilterMask.CODEC.optionalFieldOf("filter_mask", FilterMask.PASS_THROUGH).forGetter(PlayerChatMessage::filterMask)).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*  29 */   private static final UUID SYSTEM_SENDER = Util.NIL_UUID;
/*     */   
/*  30 */   public static final Duration MESSAGE_EXPIRES_AFTER_SERVER = Duration.ofMinutes(5L);
/*     */   
/*  31 */   public static final Duration MESSAGE_EXPIRES_AFTER_CLIENT = MESSAGE_EXPIRES_AFTER_SERVER.plus(Duration.ofMinutes(2L));
/*     */   
/*     */   public static PlayerChatMessage system(String content) {
/*  34 */     return unsigned(SYSTEM_SENDER, content);
/*     */   }
/*     */   
/*     */   public static PlayerChatMessage unsigned(UUID sender, String content) {
/*  38 */     SignedMessageBody body = SignedMessageBody.unsigned(content);
/*  39 */     SignedMessageLink link = SignedMessageLink.unsigned(sender);
/*  40 */     return new PlayerChatMessage(link, null, body, null, FilterMask.PASS_THROUGH);
/*     */   }
/*     */   
/*     */   public PlayerChatMessage withUnsignedContent(Component content) {
/*  44 */     Component unsignedContent = !content.equals(Component.literal(signedContent())) ? content : null;
/*  45 */     return new PlayerChatMessage(this.link, this.signature, this.signedBody, unsignedContent, this.filterMask);
/*     */   }
/*     */   
/*     */   public PlayerChatMessage removeUnsignedContent() {
/*  49 */     if (this.unsignedContent != null)
/*  50 */       return new PlayerChatMessage(this.link, this.signature, this.signedBody, null, this.filterMask); 
/*  52 */     return this;
/*     */   }
/*     */   
/*     */   public PlayerChatMessage filter(FilterMask filterMask) {
/*  56 */     if (this.filterMask.equals(filterMask))
/*  57 */       return this; 
/*  59 */     return new PlayerChatMessage(this.link, this.signature, this.signedBody, this.unsignedContent, filterMask);
/*     */   }
/*     */   
/*     */   public PlayerChatMessage filter(boolean filtered) {
/*  63 */     return filter(filtered ? this.filterMask : FilterMask.PASS_THROUGH);
/*     */   }
/*     */   
/*     */   public PlayerChatMessage removeSignature() {
/*  67 */     SignedMessageBody body = SignedMessageBody.unsigned(signedContent());
/*  68 */     SignedMessageLink link = SignedMessageLink.unsigned(sender());
/*  69 */     return new PlayerChatMessage(link, null, body, this.unsignedContent, this.filterMask);
/*     */   }
/*     */   
/*     */   public static void updateSignature(SignatureUpdater.Output output, SignedMessageLink link, SignedMessageBody body) throws SignatureException {
/*  73 */     output.update(Ints.toByteArray(1));
/*  74 */     link.updateSignature(output);
/*  75 */     body.updateSignature(output);
/*     */   }
/*     */   
/*     */   public boolean verify(SignatureValidator signatureValidator) {
/*  79 */     return (this.signature != null && this.signature.verify(signatureValidator, output -> updateSignature(output, this.link, this.signedBody)));
/*     */   }
/*     */   
/*     */   public String signedContent() {
/*  83 */     return this.signedBody.content();
/*     */   }
/*     */   
/*     */   public Component decoratedContent() {
/*  87 */     return Objects.<Component>requireNonNullElseGet(this.unsignedContent, () -> Component.literal(signedContent()));
/*     */   }
/*     */   
/*     */   public Instant timeStamp() {
/*  91 */     return this.signedBody.timeStamp();
/*     */   }
/*     */   
/*     */   public long salt() {
/*  95 */     return this.signedBody.salt();
/*     */   }
/*     */   
/*     */   public boolean hasExpiredServer(Instant now) {
/*  99 */     return now.isAfter(timeStamp().plus(MESSAGE_EXPIRES_AFTER_SERVER));
/*     */   }
/*     */   
/*     */   public boolean hasExpiredClient(Instant now) {
/* 103 */     return now.isAfter(timeStamp().plus(MESSAGE_EXPIRES_AFTER_CLIENT));
/*     */   }
/*     */   
/*     */   public UUID sender() {
/* 107 */     return this.link.sender();
/*     */   }
/*     */   
/*     */   public boolean isSystem() {
/* 111 */     return sender().equals(SYSTEM_SENDER);
/*     */   }
/*     */   
/*     */   public boolean hasSignature() {
/* 115 */     return (this.signature != null);
/*     */   }
/*     */   
/*     */   public boolean hasSignatureFrom(UUID profileId) {
/* 119 */     return (hasSignature() && this.link.sender().equals(profileId));
/*     */   }
/*     */   
/*     */   public boolean isFullyFiltered() {
/* 123 */     return this.filterMask.isFullyFiltered();
/*     */   }
/*     */   
/*     */   public static String describeSigned(PlayerChatMessage message) {
/* 127 */     return "'" + message.signedBody.content() + "' @ " + String.valueOf(message.signedBody.timeStamp()) + "\n - From: " + 
/* 128 */       String.valueOf(message.link.sender()) + "/" + String.valueOf(message.link.sessionId()) + ", message #" + message.link.index() + "\n - Salt: " + message.signedBody
/* 129 */       .salt() + "\n - Signature: " + 
/* 130 */       MessageSignature.describe(message.signature) + "\n - Last Seen: [\n" + (String)message.signedBody
/*     */       
/* 132 */       .lastSeen().entries().stream()
/* 133 */       .map(signature -> "     " + MessageSignature.describe(signature) + "\n")
/* 134 */       .collect(Collectors.joining()) + " ]\n";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\PlayerChatMessage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */