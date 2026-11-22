/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.nbt.NbtAccounter;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ServerboundCustomClickActionPacket extends Record implements Packet<ServerCommonPacketListener> {
/*    */   private final Identifier id;
/*    */   
/*    */   private final Optional<Tag> payload;
/*    */   
/*    */   public ServerboundCustomClickActionPacket(Identifier id, Optional<Tag> payload) {
/* 14 */     this.id = id;
/* 14 */     this.payload = payload;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ServerboundCustomClickActionPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundCustomClickActionPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ServerboundCustomClickActionPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundCustomClickActionPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ServerboundCustomClickActionPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ServerboundCustomClickActionPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/* 14 */     return this.id;
/*    */   }
/*    */   
/*    */   public Optional<Tag> payload() {
/* 14 */     return this.payload;
/*    */   }
/*    */   
/* 16 */   private static final StreamCodec<ByteBuf, Optional<Tag>> UNTRUSTED_TAG_CODEC = ByteBufCodecs.optionalTagCodec(() -> new NbtAccounter(32768L, 16))
/* 17 */     .apply(ByteBufCodecs.lengthPrefixed(65536));
/*    */   
/* 19 */   public static final StreamCodec<ByteBuf, ServerboundCustomClickActionPacket> STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, ServerboundCustomClickActionPacket::id, UNTRUSTED_TAG_CODEC, ServerboundCustomClickActionPacket::payload, ServerboundCustomClickActionPacket::new);
/*    */   
/*    */   public PacketType<ServerboundCustomClickActionPacket> type() {
/* 27 */     return CommonPacketTypes.SERVERBOUND_CUSTOM_CLICK_ACTION;
/*    */   }
/*    */   
/*    */   public void handle(ServerCommonPacketListener listener) {
/* 32 */     listener.handleCustomClickAction(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\ServerboundCustomClickActionPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */