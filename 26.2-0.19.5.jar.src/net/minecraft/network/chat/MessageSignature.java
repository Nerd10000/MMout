/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.Base64;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.SignatureUpdater;
/*     */ import net.minecraft.util.SignatureValidator;
/*     */ 
/*     */ public final class MessageSignature extends Record {
/*     */   private final byte[] bytes;
/*     */   
/*     */   public byte[] bytes() {
/*  17 */     return this.bytes;
/*     */   }
/*     */   
/*  18 */   public static final Codec<MessageSignature> CODEC = ExtraCodecs.BASE64_STRING.xmap(MessageSignature::new, MessageSignature::bytes);
/*     */   
/*     */   public static final int BYTES = 256;
/*     */   
/*     */   public MessageSignature(byte[] bytes) {
/*  26 */     Preconditions.checkState((bytes.length == 256), "Invalid message signature size");
/*     */     this.bytes = bytes;
/*     */   }
/*     */   
/*     */   public static MessageSignature read(FriendlyByteBuf input) {
/*  30 */     byte[] bytes = new byte[256];
/*  31 */     input.readBytes(bytes);
/*  32 */     return new MessageSignature(bytes);
/*     */   }
/*     */   
/*     */   public static void write(FriendlyByteBuf output, MessageSignature signature) {
/*  36 */     output.writeBytes(signature.bytes);
/*     */   }
/*     */   
/*     */   public boolean verify(SignatureValidator signature, SignatureUpdater updater) {
/*  40 */     return signature.validate(updater, this.bytes);
/*     */   }
/*     */   
/*     */   public ByteBuffer asByteBuffer() {
/*  44 */     return ByteBuffer.wrap(this.bytes);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  49 */     if (this != o) {
/*  49 */       if (o instanceof MessageSignature) {
/*  49 */         MessageSignature that = (MessageSignature)o;
/*  49 */         if (Arrays.equals(this.bytes, that.bytes));
/*     */       } 
/*  49 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  54 */     return Arrays.hashCode(this.bytes);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  59 */     return Base64.getEncoder().encodeToString(this.bytes);
/*     */   }
/*     */   
/*     */   public static String describe(MessageSignature signature) {
/*  63 */     if (signature == null)
/*  64 */       return "<no signature>"; 
/*  66 */     return signature.toString();
/*     */   }
/*     */   
/*     */   public Packed pack(MessageSignatureCache cache) {
/*  70 */     int packedId = cache.pack(this);
/*  71 */     return (packedId != -1) ? new Packed(packedId) : new Packed(this);
/*     */   }
/*     */   
/*     */   public int checksum() {
/*  76 */     return Arrays.hashCode(this.bytes);
/*     */   }
/*     */   
/*     */   public static final class Packed extends Record {
/*     */     private final int id;
/*     */     
/*     */     private final MessageSignature fullSignature;
/*     */     
/*     */     public static final int FULL_SIGNATURE = -1;
/*     */     
/*     */     public Packed(int id, MessageSignature fullSignature) {
/*  79 */       this.id = id;
/*  79 */       this.fullSignature = fullSignature;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/MessageSignature$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/MessageSignature$Packed;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/MessageSignature$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/MessageSignature$Packed;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/MessageSignature$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/MessageSignature$Packed;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int id() {
/*  79 */       return this.id;
/*     */     }
/*     */     
/*     */     public MessageSignature fullSignature() {
/*  79 */       return this.fullSignature;
/*     */     }
/*     */     
/*     */     public Packed(MessageSignature signature) {
/*  83 */       this(-1, signature);
/*     */     }
/*     */     
/*     */     public Packed(int id) {
/*  87 */       this(id, null);
/*     */     }
/*     */     
/*     */     public static Packed read(FriendlyByteBuf input) {
/*  91 */       int id = input.readVarInt() - 1;
/*  92 */       if (id == -1)
/*  93 */         return new Packed(MessageSignature.read(input)); 
/*  95 */       return new Packed(id);
/*     */     }
/*     */     
/*     */     public static void write(FriendlyByteBuf output, Packed packed) {
/* 100 */       output.writeVarInt(packed.id() + 1);
/* 101 */       if (packed.fullSignature() != null)
/* 102 */         MessageSignature.write(output, packed.fullSignature()); 
/*     */     }
/*     */     
/*     */     public Optional<MessageSignature> unpack(MessageSignatureCache cache) {
/* 107 */       if (this.fullSignature != null)
/* 108 */         return Optional.of(this.fullSignature); 
/* 110 */       return Optional.ofNullable(cache.unpack(this.id));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\MessageSignature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */