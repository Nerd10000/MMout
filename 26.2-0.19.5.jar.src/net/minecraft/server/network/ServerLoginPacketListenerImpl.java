/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*     */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.math.BigInteger;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.security.PrivateKey;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.SecretKey;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.PacketSendListener;
/*     */ import net.minecraft.network.TickablePacketListener;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.configuration.ConfigurationProtocols;
/*     */ import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundHelloPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginCompressionPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginFinishedPacket;
/*     */ import net.minecraft.network.protocol.login.ServerLoginPacketListener;
/*     */ import net.minecraft.network.protocol.login.ServerboundCustomQueryAnswerPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundHelloPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundKeyPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundLoginAcknowledgedPacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.notifications.ServerActivityMonitor;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.util.Crypt;
/*     */ import net.minecraft.util.CryptException;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerLoginPacketListenerImpl implements ServerLoginPacketListener, TickablePacketListener {
/*  53 */   private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
/*     */   
/*  54 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_TICKS_BEFORE_LOGIN = 600;
/*     */   
/*     */   private final byte[] challenge;
/*     */   
/*     */   private final MinecraftServer server;
/*     */   
/*     */   private final Connection connection;
/*     */   
/*     */   private final ServerActivityMonitor serverActivityMonitor;
/*     */   
/*  61 */   private volatile State state = State.HELLO;
/*     */   
/*     */   private int tick;
/*     */   
/*     */   private String requestedUsername;
/*     */   
/*     */   private GameProfile authenticatedProfile;
/*     */   
/*  65 */   private final String serverId = "";
/*     */   
/*     */   private final boolean transferred;
/*     */   
/*     */   public ServerLoginPacketListenerImpl(MinecraftServer minecraftserver, Connection connection, boolean transferred) {
/*  69 */     this.server = minecraftserver;
/*  70 */     this.connection = connection;
/*  71 */     this.serverActivityMonitor = this.server.getServerActivityMonitor();
/*  72 */     this.challenge = Ints.toByteArray(RandomSource.create().nextInt());
/*  73 */     this.transferred = transferred;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  80 */     if (this.state == State.VERIFYING)
/*  81 */       verifyLoginAndFinishConnectionSetup(Objects.<GameProfile>requireNonNull(this.authenticatedProfile)); 
/*  84 */     if (this.state == State.WAITING_FOR_DUPE_DISCONNECT && 
/*  85 */       !isPlayerAlreadyInWorld(Objects.<GameProfile>requireNonNull(this.authenticatedProfile)))
/*  86 */       finishLoginAndWaitForClient(this.authenticatedProfile); 
/*  90 */     if (this.tick++ == 600)
/*  91 */       disconnect((Component)Component.translatable("multiplayer.disconnect.slow_login")); 
/*     */   }
/*     */   
/*     */   public boolean isAcceptingMessages() {
/*  97 */     return this.connection.isConnected();
/*     */   }
/*     */   
/*     */   public void disconnect(Component component) {
/*     */     try {
/* 102 */       LOGGER.info("Disconnecting {}: {}", getUserName(), component.getString());
/* 103 */       this.connection.send((Packet)new ClientboundLoginDisconnectPacket(component));
/* 104 */       this.connection.disconnect(component);
/* 105 */     } catch (Exception e) {
/* 106 */       LOGGER.error("Error whilst disconnecting player", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isPlayerAlreadyInWorld(GameProfile gameProfile) {
/* 111 */     return (this.server.getPlayerList().getPlayer(gameProfile.id()) != null);
/*     */   }
/*     */   
/*     */   public void onDisconnect(DisconnectionDetails details) {
/* 116 */     LOGGER.info("{} lost connection: {}", getUserName(), details.reason().getString());
/*     */   }
/*     */   
/*     */   public String getUserName() {
/* 120 */     String loggableAddress = this.connection.getLoggableAddress(this.server.logIPs());
/* 121 */     if (this.requestedUsername != null)
/* 122 */       return this.requestedUsername + " (" + this.requestedUsername + ")"; 
/* 124 */     return loggableAddress;
/*     */   }
/*     */   
/*     */   public void handleHello(ServerboundHelloPacket packet) {
/* 129 */     Validate.validState((this.state == State.HELLO), "Unexpected hello packet", new Object[0]);
/* 130 */     Validate.validState(StringUtil.isValidPlayerName(packet.name()), "Invalid characters in username", new Object[0]);
/* 132 */     this.requestedUsername = packet.name();
/* 134 */     GameProfile singleplayerProfile = this.server.getSingleplayerProfile();
/* 135 */     if (singleplayerProfile != null && this.requestedUsername.equalsIgnoreCase(singleplayerProfile.name())) {
/* 136 */       startClientVerification(singleplayerProfile);
/*     */       return;
/*     */     } 
/* 140 */     if (this.server.usesAuthentication() && !this.connection.isMemoryConnection()) {
/* 141 */       this.state = State.KEY;
/* 142 */       this.connection.send((Packet)new ClientboundHelloPacket("", this.server.getKeyPair().getPublic().getEncoded(), this.challenge, true));
/*     */     } else {
/* 144 */       startClientVerification(UUIDUtil.createOfflineProfile(this.requestedUsername));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startClientVerification(GameProfile profile) {
/* 149 */     this.authenticatedProfile = profile;
/* 150 */     this.state = State.VERIFYING;
/*     */   }
/*     */   
/*     */   private void verifyLoginAndFinishConnectionSetup(GameProfile profile) {
/* 155 */     PlayerList playerList = this.server.getPlayerList();
/* 156 */     Component error = playerList.canPlayerLogin(this.connection.getRemoteAddress(), new NameAndId(profile));
/* 157 */     if (error != null) {
/* 158 */       disconnect(error);
/* 159 */     } else if (this.connection.getIntendedProfileId() != null && !profile.id().equals(this.connection.getIntendedProfileId())) {
/* 160 */       disconnect(CommonComponents.CONNECT_FAILED);
/*     */     } else {
/* 162 */       if (this.server.getCompressionThreshold() >= 0 && !this.connection.isMemoryConnection())
/* 163 */         this.connection.send((Packet)new ClientboundLoginCompressionPacket(this.server.getCompressionThreshold()), PacketSendListener.thenRun(() -> this.connection.setupCompression(this.server.getCompressionThreshold(), true))); 
/* 167 */       boolean waitForDisconnection = playerList.disconnectAllPlayersWithProfile(profile.id());
/* 169 */       if (waitForDisconnection) {
/* 170 */         this.state = State.WAITING_FOR_DUPE_DISCONNECT;
/*     */       } else {
/* 172 */         finishLoginAndWaitForClient(profile);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void finishLoginAndWaitForClient(GameProfile gameProfile) {
/* 178 */     this.state = State.PROTOCOL_SWITCHING;
/* 180 */     this.connection.send((Packet)new ClientboundLoginFinishedPacket(gameProfile, this.server.getConnection().getSessionId()));
/*     */   }
/*     */   
/*     */   public void handleKey(ServerboundKeyPacket packet) {
/*     */     final String digest;
/* 187 */     Validate.validState((this.state == State.KEY), "Unexpected key packet", new Object[0]);
/*     */     try {
/* 191 */       PrivateKey serverPrivateKey = this.server.getKeyPair().getPrivate();
/* 193 */       if (!packet.isChallengeValid(this.challenge, serverPrivateKey))
/* 194 */         throw new IllegalStateException("Protocol error"); 
/* 197 */       SecretKey secretKey = packet.getSecretKey(serverPrivateKey);
/* 198 */       digest = (new BigInteger(Crypt.digestData("", this.server.getKeyPair().getPublic(), secretKey))).toString(16);
/* 200 */       this.state = State.AUTHENTICATING;
/* 201 */       Cipher decryptCipher = Crypt.getCipher(2, secretKey);
/* 202 */       Cipher encryptCipher = Crypt.getCipher(1, secretKey);
/* 203 */       this.connection.setEncryptionKey(decryptCipher, encryptCipher);
/* 204 */     } catch (CryptException e) {
/* 205 */       throw new IllegalStateException("Protocol error", e);
/*     */     } 
/* 208 */     Thread thread = new Thread("User Authenticator #" + UNIQUE_THREAD_ID.incrementAndGet()) {
/*     */         public void run() {
/* 211 */           String name = Objects.<String>requireNonNull(ServerLoginPacketListenerImpl.this.requestedUsername, "Player name not initialized");
/*     */           try {
/* 213 */             ProfileResult result = ServerLoginPacketListenerImpl.this.server.services().sessionService().hasJoinedServer(name, digest, getAddress());
/* 215 */             if (result != null) {
/* 216 */               GameProfile profile = result.profile();
/* 217 */               ServerLoginPacketListenerImpl.LOGGER.info("UUID of player {} is {}", profile.name(), profile.id());
/* 218 */               ServerLoginPacketListenerImpl.this.serverActivityMonitor.reportLoginActivity();
/* 219 */               ServerLoginPacketListenerImpl.this.startClientVerification(profile);
/* 220 */             } else if (ServerLoginPacketListenerImpl.this.server.isSingleplayer()) {
/* 221 */               ServerLoginPacketListenerImpl.LOGGER.warn("Failed to verify username but will let them in anyway!");
/* 222 */               ServerLoginPacketListenerImpl.this.startClientVerification(UUIDUtil.createOfflineProfile(name));
/*     */             } else {
/* 224 */               ServerLoginPacketListenerImpl.this.disconnect((Component)Component.translatable("multiplayer.disconnect.unverified_username"));
/* 225 */               ServerLoginPacketListenerImpl.LOGGER.error("Username '{}' tried to join with an invalid session", name);
/*     */             } 
/* 227 */           } catch (AuthenticationUnavailableException ignored) {
/* 228 */             if (ServerLoginPacketListenerImpl.this.server.isSingleplayer()) {
/* 229 */               ServerLoginPacketListenerImpl.LOGGER.warn("Authentication servers are down but will let them in anyway!");
/* 230 */               ServerLoginPacketListenerImpl.this.startClientVerification(UUIDUtil.createOfflineProfile(name));
/*     */             } else {
/* 232 */               ServerLoginPacketListenerImpl.this.disconnect((Component)Component.translatable("multiplayer.disconnect.authservers_down"));
/* 233 */               ServerLoginPacketListenerImpl.LOGGER.error("Couldn't verify username because servers are unavailable");
/*     */             } 
/*     */           } 
/*     */         }
/*     */         
/*     */         private InetAddress getAddress() {
/* 239 */           SocketAddress remoteAddress = ServerLoginPacketListenerImpl.this.connection.getRemoteAddress();
/* 240 */           InetSocketAddress inetSocketAddress = (InetSocketAddress)remoteAddress;
/* 240 */           return (ServerLoginPacketListenerImpl.this.server.getPreventProxyConnections() && remoteAddress instanceof InetSocketAddress) ? inetSocketAddress.getAddress() : null;
/*     */         }
/*     */       };
/* 243 */     thread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LOGGER));
/* 244 */     thread.start();
/*     */   }
/*     */   
/*     */   public void handleCustomQueryPacket(ServerboundCustomQueryAnswerPacket packet) {
/* 250 */     disconnect(ServerCommonPacketListenerImpl.DISCONNECT_UNEXPECTED_QUERY);
/*     */   }
/*     */   
/*     */   public void handleLoginAcknowledgement(ServerboundLoginAcknowledgedPacket packet) {
/* 256 */     Validate.validState((this.state == State.PROTOCOL_SWITCHING), "Unexpected login acknowledgement packet", new Object[0]);
/* 257 */     this.connection.setupOutboundProtocol(ConfigurationProtocols.CLIENTBOUND);
/* 258 */     CommonListenerCookie cookie = CommonListenerCookie.createInitial(Objects.<GameProfile>requireNonNull(this.authenticatedProfile), this.transferred);
/* 259 */     ServerConfigurationPacketListenerImpl configPacketListener = new ServerConfigurationPacketListenerImpl(this.server, this.connection, cookie);
/* 260 */     this.connection.setupInboundProtocol(ConfigurationProtocols.SERVERBOUND, (PacketListener)configPacketListener);
/* 261 */     configPacketListener.startConfiguration();
/* 262 */     this.state = State.ACCEPTED;
/*     */   }
/*     */   
/*     */   public void fillListenerSpecificCrashDetails(CrashReport report, CrashReportCategory connectionDetails) {
/* 267 */     connectionDetails.setDetail("Login phase", () -> this.state.toString());
/*     */   }
/*     */   
/*     */   public void handleCookieResponse(ServerboundCookieResponsePacket packet) {
/* 272 */     disconnect(ServerCommonPacketListenerImpl.DISCONNECT_UNEXPECTED_QUERY);
/*     */   }
/*     */   
/*     */   private enum State {
/* 276 */     HELLO, KEY, AUTHENTICATING, NEGOTIATING, VERIFYING, WAITING_FOR_DUPE_DISCONNECT, PROTOCOL_SWITCHING, ACCEPTED;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerLoginPacketListenerImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */