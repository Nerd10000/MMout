/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundEntityTagQueryPacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundEntityTagQueryPacket> STREAM_CODEC = Packet.codec(ServerboundEntityTagQueryPacket::write, ServerboundEntityTagQueryPacket::new);
/*    */   
/*    */   private final int transactionId;
/*    */   
/*    */   private final int entityId;
/*    */   
/*    */   public ServerboundEntityTagQueryPacket(int transactionId, int entityId) {
/* 15 */     this.transactionId = transactionId;
/* 16 */     this.entityId = entityId;
/*    */   }
/*    */   
/*    */   private ServerboundEntityTagQueryPacket(FriendlyByteBuf input) {
/* 20 */     this.transactionId = input.readVarInt();
/* 21 */     this.entityId = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 25 */     output.writeVarInt(this.transactionId);
/* 26 */     output.writeVarInt(this.entityId);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundEntityTagQueryPacket> type() {
/* 31 */     return GamePacketTypes.SERVERBOUND_ENTITY_TAG_QUERY;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 36 */     listener.handleEntityTagQuery(this);
/*    */   }
/*    */   
/*    */   public int getTransactionId() {
/* 40 */     return this.transactionId;
/*    */   }
/*    */   
/*    */   public int getEntityId() {
/* 44 */     return this.entityId;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundEntityTagQueryPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */