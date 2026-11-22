/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundClientCommandPacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundClientCommandPacket> STREAM_CODEC = Packet.codec(ServerboundClientCommandPacket::write, ServerboundClientCommandPacket::new);
/*    */   
/*    */   private final Action action;
/*    */   
/*    */   public ServerboundClientCommandPacket(Action action) {
/* 14 */     this.action = action;
/*    */   }
/*    */   
/*    */   private ServerboundClientCommandPacket(FriendlyByteBuf input) {
/* 18 */     this.action = (Action)input.readEnum(Action.class);
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeEnum(this.action);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundClientCommandPacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_CLIENT_COMMAND;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleClientCommand(this);
/*    */   }
/*    */   
/*    */   public Action getAction() {
/* 36 */     return this.action;
/*    */   }
/*    */   
/*    */   public enum Action {
/* 40 */     PERFORM_RESPAWN, REQUEST_STATS, REQUEST_GAMERULE_VALUES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundClientCommandPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */