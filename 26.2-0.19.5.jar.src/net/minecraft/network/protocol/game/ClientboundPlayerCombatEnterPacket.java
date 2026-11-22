/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundPlayerCombatEnterPacket implements Packet<ClientGamePacketListener> {
/*  9 */   public static final ClientboundPlayerCombatEnterPacket INSTANCE = new ClientboundPlayerCombatEnterPacket();
/*    */   
/* 10 */   public static final StreamCodec<ByteBuf, ClientboundPlayerCombatEnterPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */   
/*    */   public PacketType<ClientboundPlayerCombatEnterPacket> type() {
/* 17 */     return GamePacketTypes.CLIENTBOUND_PLAYER_COMBAT_ENTER;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 22 */     listener.handlePlayerCombatEnter(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundPlayerCombatEnterPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */