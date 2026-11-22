/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundResourcePackPushPacket extends Record implements Packet<ClientCommonPacketListener> {
/*    */   private final UUID id;
/*    */   
/*    */   private final String url;
/*    */   
/*    */   private final String hash;
/*    */   
/*    */   private final boolean required;
/*    */   
/*    */   private final Optional<Component> prompt;
/*    */   
/*    */   public static final int MAX_HASH_LENGTH = 40;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPushPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPushPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPushPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPushPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPushPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPushPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public UUID id() {
/* 16 */     return this.id;
/*    */   }
/*    */   
/*    */   public String url() {
/* 16 */     return this.url;
/*    */   }
/*    */   
/*    */   public String hash() {
/* 16 */     return this.hash;
/*    */   }
/*    */   
/*    */   public boolean required() {
/* 16 */     return this.required;
/*    */   }
/*    */   
/*    */   public Optional<Component> prompt() {
/* 16 */     return this.prompt;
/*    */   }
/*    */   
/* 25 */   public static final StreamCodec<ByteBuf, ClientboundResourcePackPushPacket> STREAM_CODEC = StreamCodec.composite(UUIDUtil.STREAM_CODEC, ClientboundResourcePackPushPacket::id, ByteBufCodecs.STRING_UTF8, ClientboundResourcePackPushPacket::url, 
/*    */       
/* 28 */       ByteBufCodecs.stringUtf8(40), ClientboundResourcePackPushPacket::hash, ByteBufCodecs.BOOL, ClientboundResourcePackPushPacket::required, ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC
/*    */       
/* 30 */       .apply(ByteBufCodecs::optional), ClientboundResourcePackPushPacket::prompt, ClientboundResourcePackPushPacket::new);
/*    */   
/*    */   public ClientboundResourcePackPushPacket(UUID id, String url, String hash, boolean required, Optional<Component> prompt) {
/* 35 */     if (hash.length() > 40)
/* 36 */       throw new IllegalArgumentException("Hash is too long (max 40, was " + hash.length() + ")"); 
/*    */     this.id = id;
/*    */     this.url = url;
/*    */     this.hash = hash;
/*    */     this.required = required;
/*    */     this.prompt = prompt;
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundResourcePackPushPacket> type() {
/* 42 */     return CommonPacketTypes.CLIENTBOUND_RESOURCE_PACK_PUSH;
/*    */   }
/*    */   
/*    */   public void handle(ClientCommonPacketListener listener) {
/* 47 */     listener.handleResourcePackPush(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\ClientboundResourcePackPushPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */