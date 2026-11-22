/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
/*     */ import net.minecraft.client.multiplayer.LevelLoadTracker;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.multiplayer.TransferState;
/*     */ import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
/*     */ import net.minecraft.client.resources.server.ServerPackManager;
/*     */ import net.minecraft.network.ClientboundPacketListener;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ import net.minecraft.network.protocol.login.LoginProtocols;
/*     */ import net.minecraft.network.protocol.login.ServerboundHelloPacket;
/*     */ import net.minecraft.server.network.EventLoopGroupHolder;
/*     */ 
/*     */ class null extends Thread {
/*     */   null(String name) {
/*  85 */     super(name);
/*     */   }
/*     */   
/*     */   public void run() {
/*  88 */     InetSocketAddress address = null;
/*     */     try {
/*     */       Connection pendingConnection;
/*  90 */       if (ConnectScreen.this.aborted)
/*     */         return; 
/*  95 */       Optional<InetSocketAddress> resolvedAddress = ServerNameResolver.DEFAULT.resolveAddress(hostAndPort).map(ResolvedServerAddress::asInetSocketAddress);
/*  96 */       if (ConnectScreen.this.aborted)
/*     */         return; 
/*  99 */       if (resolvedAddress.isEmpty()) {
/* 100 */         minecraft.execute(() -> minecraft.gui.setScreen(new DisconnectedScreen(ConnectScreen.this.parent, ConnectScreen.this.connectFailedTitle, ConnectScreen.UNKNOWN_HOST_MESSAGE)));
/*     */         return;
/*     */       } 
/* 103 */       address = resolvedAddress.get();
/* 106 */       synchronized (ConnectScreen.this) {
/* 107 */         if (ConnectScreen.this.aborted)
/*     */           return; 
/* 110 */         pendingConnection = new Connection(PacketFlow.CLIENTBOUND);
/* 111 */         pendingConnection.setBandwidthLogger(minecraft.getDebugOverlay().getBandwidthLogger());
/* 113 */         ConnectScreen.this.channelFuture = Connection.connect(address, EventLoopGroupHolder.remote(minecraft.options.useNativeTransport()), pendingConnection);
/*     */       } 
/* 115 */       ConnectScreen.this.channelFuture.syncUninterruptibly();
/* 117 */       synchronized (ConnectScreen.this) {
/* 118 */         if (ConnectScreen.this.aborted) {
/* 119 */           pendingConnection.disconnect(ConnectScreen.ABORT_CONNECTION);
/*     */           return;
/*     */         } 
/* 122 */         ConnectScreen.this.connection = pendingConnection;
/* 123 */         minecraft.getDownloadedPackSource().configureForServerControl(pendingConnection, convertPackStatus(server.getResourcePackStatus()));
/*     */       } 
/* 130 */       Objects.requireNonNull(ConnectScreen.this);
/* 130 */       ConnectScreen.this.connection.initiateServerboundPlayConnection(address.getHostName(), address.getPort(), LoginProtocols.SERVERBOUND, LoginProtocols.CLIENTBOUND, (ClientboundPacketListener)new ClientHandshakePacketListenerImpl(ConnectScreen.this.connection, minecraft, server, ConnectScreen.this.parent, false, null, ConnectScreen.this::updateStatus, new LevelLoadTracker(), transferState), (transferState != null));
/* 133 */       ConnectScreen.this.connection.send((Packet)new ServerboundHelloPacket(minecraft.getUser().getName(), minecraft.getUser().getProfileId()));
/* 134 */     } catch (Exception exception) {
/*     */       Exception cause;
/* 135 */       if (ConnectScreen.this.aborted)
/*     */         return; 
/* 142 */       Throwable throwable = exception.getCause();
/* 142 */       if (throwable instanceof Exception) {
/* 142 */         Exception originalCause = (Exception)throwable;
/* 143 */         cause = originalCause;
/*     */       } else {
/* 145 */         cause = exception;
/*     */       } 
/* 148 */       ConnectScreen.LOGGER.error("Couldn't connect to server", exception);
/* 149 */       String message = (address == null) ? cause.getMessage() : cause.getMessage().replaceAll(address.getHostName() + ":" + address.getHostName(), "").replaceAll(address.toString(), "");
/* 150 */       minecraft.execute(() -> minecraft.gui.setScreen(new DisconnectedScreen(ConnectScreen.this.parent, ConnectScreen.this.connectFailedTitle, (Component)Component.translatable("disconnect.genericReason", new Object[] { message }))));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ServerPackManager.PackPromptStatus convertPackStatus(ServerData.ServerPackStatus resourcePackStatus) {
/* 155 */     switch (ConnectScreen.null.$SwitchMap$net$minecraft$client$multiplayer$ServerData$ServerPackStatus[resourcePackStatus.ordinal()]) {
/*     */       default:
/* 155 */         throw new MatchException(null, null);
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */         break;
/*     */     } 
/* 155 */     return 
/*     */       
/* 158 */       ServerPackManager.PackPromptStatus.PENDING;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\ConnectScreen$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */