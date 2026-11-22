/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ServerboundPickItemFromEntityPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int id;
/*    */   
/*    */   private final boolean includeData;
/*    */   
/*    */   public ServerboundPickItemFromEntityPacket(int id, boolean includeData) {
/*  9 */     this.id = id;
/*  9 */     this.includeData = includeData;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundPickItemFromEntityPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPickItemFromEntityPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundPickItemFromEntityPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPickItemFromEntityPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundPickItemFromEntityPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundPickItemFromEntityPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int id() {
/*  9 */     return this.id;
/*    */   }
/*    */   
/*    */   public boolean includeData() {
/*  9 */     return this.includeData;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<ByteBuf, ServerboundPickItemFromEntityPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ServerboundPickItemFromEntityPacket::id, ByteBufCodecs.BOOL, ServerboundPickItemFromEntityPacket::includeData, ServerboundPickItemFromEntityPacket::new);
/*    */   
/*    */   public PacketType<ServerboundPickItemFromEntityPacket> type() {
/* 18 */     return GamePacketTypes.SERVERBOUND_PICK_ITEM_FROM_ENTITY;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 23 */     listener.handlePickItemFromEntity(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundPickItemFromEntityPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */