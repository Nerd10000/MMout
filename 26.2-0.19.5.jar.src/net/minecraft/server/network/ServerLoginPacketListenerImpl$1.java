/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*     */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class null extends Thread {
/*     */   null(String name) {
/* 208 */     super(name);
/*     */   }
/*     */   
/*     */   public void run() {
/* 211 */     String name = Objects.<String>requireNonNull(ServerLoginPacketListenerImpl.this.requestedUsername, "Player name not initialized");
/*     */     try {
/* 213 */       ProfileResult result = ServerLoginPacketListenerImpl.this.server.services().sessionService().hasJoinedServer(name, digest, getAddress());
/* 215 */       if (result != null) {
/* 216 */         GameProfile profile = result.profile();
/* 217 */         ServerLoginPacketListenerImpl.LOGGER.info("UUID of player {} is {}", profile.name(), profile.id());
/* 218 */         ServerLoginPacketListenerImpl.this.serverActivityMonitor.reportLoginActivity();
/* 219 */         ServerLoginPacketListenerImpl.this.startClientVerification(profile);
/* 220 */       } else if (ServerLoginPacketListenerImpl.this.server.isSingleplayer()) {
/* 221 */         ServerLoginPacketListenerImpl.LOGGER.warn("Failed to verify username but will let them in anyway!");
/* 222 */         ServerLoginPacketListenerImpl.this.startClientVerification(UUIDUtil.createOfflineProfile(name));
/*     */       } else {
/* 224 */         ServerLoginPacketListenerImpl.this.disconnect((Component)Component.translatable("multiplayer.disconnect.unverified_username"));
/* 225 */         ServerLoginPacketListenerImpl.LOGGER.error("Username '{}' tried to join with an invalid session", name);
/*     */       } 
/* 227 */     } catch (AuthenticationUnavailableException ignored) {
/* 228 */       if (ServerLoginPacketListenerImpl.this.server.isSingleplayer()) {
/* 229 */         ServerLoginPacketListenerImpl.LOGGER.warn("Authentication servers are down but will let them in anyway!");
/* 230 */         ServerLoginPacketListenerImpl.this.startClientVerification(UUIDUtil.createOfflineProfile(name));
/*     */       } else {
/* 232 */         ServerLoginPacketListenerImpl.this.disconnect((Component)Component.translatable("multiplayer.disconnect.authservers_down"));
/* 233 */         ServerLoginPacketListenerImpl.LOGGER.error("Couldn't verify username because servers are unavailable");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private InetAddress getAddress() {
/* 239 */     SocketAddress remoteAddress = ServerLoginPacketListenerImpl.this.connection.getRemoteAddress();
/* 240 */     InetSocketAddress inetSocketAddress = (InetSocketAddress)remoteAddress;
/* 240 */     return (ServerLoginPacketListenerImpl.this.server.getPreventProxyConnections() && remoteAddress instanceof InetSocketAddress) ? inetSocketAddress.getAddress() : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerLoginPacketListenerImpl$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */