/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.network.Connection;
/*    */ import net.minecraft.network.DisconnectionDetails;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.handshake.ClientIntent;
/*    */ import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
/*    */ import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener;
/*    */ import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
/*    */ import net.minecraft.network.protocol.login.LoginProtocols;
/*    */ import net.minecraft.network.protocol.status.ServerStatus;
/*    */ import net.minecraft.network.protocol.status.StatusProtocols;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class ServerHandshakePacketListenerImpl implements ServerHandshakePacketListener {
/* 16 */   private static final Component IGNORE_STATUS_REASON = (Component)Component.translatable("disconnect.ignoring_status_request");
/*    */   
/*    */   private final MinecraftServer server;
/*    */   
/*    */   private final Connection connection;
/*    */   
/*    */   public ServerHandshakePacketListenerImpl(MinecraftServer server, Connection connection) {
/* 22 */     this.server = server;
/* 23 */     this.connection = connection;
/*    */   }
/*    */   
/*    */   public void handleIntention(ClientIntentionPacket packet) {
/*    */     ServerStatus status;
/* 28 */     switch (packet.intention()) {
/*    */       case LOGIN:
/* 29 */         beginLogin(packet, false);
/*    */         return;
/*    */       case STATUS:
/* 31 */         status = this.server.getStatus();
/* 32 */         this.connection.setupOutboundProtocol(StatusProtocols.CLIENTBOUND);
/* 33 */         if (this.server.repliesToStatus() && status != null) {
/* 34 */           this.connection.setupInboundProtocol(StatusProtocols.SERVERBOUND, (PacketListener)new ServerStatusPacketListenerImpl(status, this.connection));
/*    */         } else {
/* 36 */           this.connection.disconnect(IGNORE_STATUS_REASON);
/*    */         } 
/*    */         return;
/*    */       case TRANSFER:
/* 40 */         if (!this.server.acceptsTransfers()) {
/* 41 */           this.connection.setupOutboundProtocol(LoginProtocols.CLIENTBOUND);
/* 42 */           MutableComponent mutableComponent = Component.translatable("multiplayer.disconnect.transfers_disabled");
/* 43 */           this.connection.send((Packet)new ClientboundLoginDisconnectPacket((Component)mutableComponent));
/* 44 */           this.connection.disconnect((Component)mutableComponent);
/*    */         } else {
/* 46 */           beginLogin(packet, true);
/*    */         } 
/*    */         return;
/*    */     } 
/* 49 */     throw new UnsupportedOperationException("Invalid intention " + String.valueOf(packet.intention()));
/*    */   }
/*    */   
/*    */   private void beginLogin(ClientIntentionPacket packet, boolean transfer) {
/* 54 */     this.connection.setupOutboundProtocol(LoginProtocols.CLIENTBOUND);
/* 55 */     if (packet.protocolVersion() != SharedConstants.getCurrentVersion().protocolVersion()) {
/*    */       MutableComponent mutableComponent;
/* 60 */       if (packet.protocolVersion() < 754) {
/* 61 */         mutableComponent = Component.translatable("multiplayer.disconnect.outdated_client", new Object[] { SharedConstants.getCurrentVersion().name() });
/*    */       } else {
/* 63 */         mutableComponent = Component.translatable("multiplayer.disconnect.incompatible", new Object[] { SharedConstants.getCurrentVersion().name() });
/*    */       } 
/* 65 */       this.connection.send((Packet)new ClientboundLoginDisconnectPacket((Component)mutableComponent));
/* 66 */       this.connection.disconnect((Component)mutableComponent);
/*    */     } else {
/* 68 */       this.connection.setupInboundProtocol(LoginProtocols.SERVERBOUND, (PacketListener)new ServerLoginPacketListenerImpl(this.server, this.connection, transfer));
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onDisconnect(DisconnectionDetails details) {}
/*    */   
/*    */   public boolean isAcceptingMessages() {
/* 78 */     return this.connection.isConnected();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerHandshakePacketListenerImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */