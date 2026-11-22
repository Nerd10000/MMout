/*     */ package net.minecraft.server.rcon.thread;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.PortUnreachableException;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.rcon.NetworkDataOutputStream;
/*     */ import net.minecraft.server.rcon.PktUtils;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class QueryThreadGs4 extends GenericThread {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String GAME_TYPE = "SMP";
/*     */   
/*     */   private static final String GAME_ID = "MINECRAFT";
/*     */   
/*     */   private static final long CHALLENGE_CHECK_INTERVAL = 30000L;
/*     */   
/*     */   private static final long RESPONSE_CACHE_TIME = 5000L;
/*     */   
/*     */   private long lastChallengeCheck;
/*     */   
/*     */   private final int port;
/*     */   
/*     */   private final int serverPort;
/*     */   
/*     */   private final int maxPlayers;
/*     */   
/*     */   private final String serverName;
/*     */   
/*     */   private final String worldName;
/*     */   
/*     */   private DatagramSocket socket;
/*     */   
/*  39 */   private final byte[] buffer = new byte[1460];
/*     */   
/*     */   private String hostIp;
/*     */   
/*     */   private String serverIp;
/*     */   
/*     */   private final Map<SocketAddress, RequestChallenge> validChallenges;
/*     */   
/*     */   private final NetworkDataOutputStream rulesResponse;
/*     */   
/*     */   private long lastRulesResponse;
/*     */   
/*     */   private final ServerInterface serverInterface;
/*     */   
/*     */   private QueryThreadGs4(ServerInterface serverInterface, int port) {
/*  48 */     super("Query Listener");
/*  49 */     this.serverInterface = serverInterface;
/*  51 */     this.port = port;
/*  52 */     this.serverIp = serverInterface.getServerIp();
/*  53 */     this.serverPort = serverInterface.getServerPort();
/*  54 */     this.serverName = serverInterface.getServerName();
/*  55 */     this.maxPlayers = serverInterface.getMaxPlayers();
/*  56 */     this.worldName = serverInterface.getLevelIdName();
/*  59 */     this.lastRulesResponse = 0L;
/*  61 */     this.hostIp = "0.0.0.0";
/*  64 */     if (this.serverIp.isEmpty() || this.hostIp.equals(this.serverIp)) {
/*  66 */       this.serverIp = "0.0.0.0";
/*     */       try {
/*  68 */         InetAddress addr = InetAddress.getLocalHost();
/*  69 */         this.hostIp = addr.getHostAddress();
/*  70 */       } catch (UnknownHostException e) {
/*  71 */         LOGGER.warn("Unable to determine local host IP, please set server-ip in server.properties", e);
/*     */       } 
/*     */     } else {
/*  74 */       this.hostIp = this.serverIp;
/*     */     } 
/*  78 */     this.rulesResponse = new NetworkDataOutputStream(1460);
/*  79 */     this.validChallenges = Maps.newHashMap();
/*     */   }
/*     */   
/*     */   public static QueryThreadGs4 create(ServerInterface serverInterface) {
/*  83 */     int port = (serverInterface.getProperties()).queryPort;
/*  84 */     if (0 >= port || 65535 < port) {
/*  85 */       LOGGER.warn("Invalid query port {} found in server.properties (queries disabled)", Integer.valueOf(port));
/*  86 */       return null;
/*     */     } 
/*  89 */     QueryThreadGs4 result = new QueryThreadGs4(serverInterface, port);
/*  90 */     if (!result.start())
/*  91 */       return null; 
/*  93 */     return result;
/*     */   }
/*     */   
/*     */   private void sendTo(byte[] data, DatagramPacket src) throws IOException {
/*  97 */     this.socket.send(new DatagramPacket(data, data.length, src.getSocketAddress()));
/*     */   }
/*     */   
/*     */   private boolean processPacket(DatagramPacket packet) throws IOException {
/*     */     NetworkDataOutputStream dos;
/* 101 */     byte[] buf = packet.getData();
/* 102 */     int len = packet.getLength();
/* 103 */     SocketAddress socketAddress = packet.getSocketAddress();
/* 104 */     LOGGER.debug("Packet len {} [{}]", Integer.valueOf(len), socketAddress);
/* 105 */     if (3 > len || -2 != buf[0] || -3 != buf[1]) {
/* 107 */       LOGGER.debug("Invalid packet [{}]", socketAddress);
/* 108 */       return false;
/*     */     } 
/* 112 */     LOGGER.debug("Packet '{}' [{}]", PktUtils.toHexString(buf[2]), socketAddress);
/* 113 */     switch (buf[2]) {
/*     */       case 9:
/* 115 */         sendChallenge(packet);
/* 116 */         LOGGER.debug("Challenge [{}]", socketAddress);
/* 117 */         return true;
/*     */       case 0:
/* 120 */         if (!validChallenge(packet).booleanValue()) {
/* 121 */           LOGGER.debug("Invalid challenge [{}]", socketAddress);
/* 122 */           return false;
/*     */         } 
/* 125 */         if (15 == len) {
/* 127 */           sendTo(buildRuleResponse(packet), packet);
/* 128 */           LOGGER.debug("Rules [{}]", socketAddress);
/*     */           break;
/*     */         } 
/* 131 */         dos = new NetworkDataOutputStream(1460);
/* 132 */         dos.write(0);
/* 133 */         dos.writeBytes(getIdentBytes(packet.getSocketAddress()));
/* 134 */         dos.writeString(this.serverName);
/* 135 */         dos.writeString("SMP");
/* 136 */         dos.writeString(this.worldName);
/* 137 */         dos.writeString(Integer.toString(this.serverInterface.getPlayerCount()));
/* 138 */         dos.writeString(Integer.toString(this.maxPlayers));
/* 139 */         dos.writeShort((short)this.serverPort);
/* 140 */         dos.writeString(this.hostIp);
/* 142 */         sendTo(dos.toByteArray(), packet);
/* 143 */         LOGGER.debug("Status [{}]", socketAddress);
/*     */         break;
/*     */     } 
/* 148 */     return true;
/*     */   }
/*     */   
/*     */   private byte[] buildRuleResponse(DatagramPacket packet) throws IOException {
/* 152 */     long now = Util.getMillis();
/* 153 */     if (now < this.lastRulesResponse + 5000L) {
/* 155 */       byte[] data = this.rulesResponse.toByteArray();
/* 156 */       byte[] ident = getIdentBytes(packet.getSocketAddress());
/* 157 */       data[1] = ident[0];
/* 158 */       data[2] = ident[1];
/* 159 */       data[3] = ident[2];
/* 160 */       data[4] = ident[3];
/* 162 */       return data;
/*     */     } 
/* 165 */     this.lastRulesResponse = now;
/* 167 */     this.rulesResponse.reset();
/* 168 */     this.rulesResponse.write(0);
/* 169 */     this.rulesResponse.writeBytes(getIdentBytes(packet.getSocketAddress()));
/* 170 */     this.rulesResponse.writeString("splitnum");
/* 171 */     this.rulesResponse.write(128);
/* 172 */     this.rulesResponse.write(0);
/* 175 */     this.rulesResponse.writeString("hostname");
/* 176 */     this.rulesResponse.writeString(this.serverName);
/* 177 */     this.rulesResponse.writeString("gametype");
/* 178 */     this.rulesResponse.writeString("SMP");
/* 179 */     this.rulesResponse.writeString("game_id");
/* 180 */     this.rulesResponse.writeString("MINECRAFT");
/* 181 */     this.rulesResponse.writeString("version");
/* 182 */     this.rulesResponse.writeString(this.serverInterface.getServerVersion());
/* 183 */     this.rulesResponse.writeString("plugins");
/* 184 */     this.rulesResponse.writeString(this.serverInterface.getPluginNames());
/* 185 */     this.rulesResponse.writeString("map");
/* 186 */     this.rulesResponse.writeString(this.worldName);
/* 187 */     this.rulesResponse.writeString("numplayers");
/* 188 */     this.rulesResponse.writeString("" + this.serverInterface.getPlayerCount());
/* 189 */     this.rulesResponse.writeString("maxplayers");
/* 190 */     this.rulesResponse.writeString("" + this.maxPlayers);
/* 191 */     this.rulesResponse.writeString("hostport");
/* 192 */     this.rulesResponse.writeString("" + this.serverPort);
/* 193 */     this.rulesResponse.writeString("hostip");
/* 194 */     this.rulesResponse.writeString(this.hostIp);
/* 195 */     this.rulesResponse.write(0);
/* 196 */     this.rulesResponse.write(1);
/* 200 */     this.rulesResponse.writeString("player_");
/* 201 */     this.rulesResponse.write(0);
/* 203 */     String[] players = this.serverInterface.getPlayerNames();
/* 204 */     for (String player : players)
/* 205 */       this.rulesResponse.writeString(player); 
/* 207 */     this.rulesResponse.write(0);
/* 209 */     return this.rulesResponse.toByteArray();
/*     */   }
/*     */   
/*     */   private byte[] getIdentBytes(SocketAddress src) {
/* 213 */     return ((RequestChallenge)this.validChallenges.get(src)).getIdentBytes();
/*     */   }
/*     */   
/*     */   private Boolean validChallenge(DatagramPacket src) {
/* 217 */     SocketAddress sockAddr = src.getSocketAddress();
/* 218 */     if (!this.validChallenges.containsKey(sockAddr))
/* 220 */       return Boolean.valueOf(false); 
/* 223 */     byte[] data = src.getData();
/* 224 */     return Boolean.valueOf((((RequestChallenge)this.validChallenges.get(sockAddr)).getChallenge() == PktUtils.intFromNetworkByteArray(data, 7, src.getLength())));
/*     */   }
/*     */   
/*     */   private void sendChallenge(DatagramPacket src) throws IOException {
/* 228 */     RequestChallenge challenge = new RequestChallenge(src);
/* 229 */     this.validChallenges.put(src.getSocketAddress(), challenge);
/* 231 */     sendTo(challenge.getChallengeBytes(), src);
/*     */   }
/*     */   
/*     */   private void pruneChallenges() {
/* 235 */     if (!this.running)
/*     */       return; 
/* 239 */     long now = Util.getMillis();
/* 240 */     if (now < this.lastChallengeCheck + 30000L)
/*     */       return; 
/* 243 */     this.lastChallengeCheck = now;
/* 245 */     this.validChallenges.values().removeIf(challenge -> challenge.before(now).booleanValue());
/*     */   }
/*     */   
/*     */   public void run() {
/* 250 */     LOGGER.info("Query running on {}:{}", this.serverIp, Integer.valueOf(this.port));
/* 251 */     this.lastChallengeCheck = Util.getMillis();
/* 252 */     DatagramPacket request = new DatagramPacket(this.buffer, this.buffer.length);
/*     */     try {
/* 255 */       while (this.running) {
/*     */         try {
/* 257 */           this.socket.receive(request);
/* 260 */           pruneChallenges();
/* 263 */           processPacket(request);
/* 264 */         } catch (SocketTimeoutException ignored) {
/* 266 */           pruneChallenges();
/* 267 */         } catch (PortUnreachableException portUnreachableException) {
/*     */         
/* 269 */         } catch (IOException e) {
/* 271 */           recoverSocketError(e);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 275 */       LOGGER.debug("closeSocket: {}:{}", this.serverIp, Integer.valueOf(this.port));
/* 276 */       this.socket.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean start() {
/* 282 */     if (this.running)
/* 283 */       return true; 
/* 286 */     if (!initSocket())
/* 287 */       return false; 
/* 290 */     return super.start();
/*     */   }
/*     */   
/*     */   private void recoverSocketError(Exception e) {
/* 294 */     if (!this.running)
/*     */       return; 
/* 299 */     LOGGER.warn("Unexpected exception", e);
/* 302 */     if (!initSocket()) {
/* 303 */       LOGGER.error("Failed to recover from exception, shutting down!");
/* 304 */       this.running = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean initSocket() {
/*     */     try {
/* 310 */       this.socket = new DatagramSocket(this.port, InetAddress.getByName(this.serverIp));
/* 311 */       this.socket.setSoTimeout(500);
/* 312 */       return true;
/* 313 */     } catch (Exception e) {
/* 314 */       LOGGER.warn("Unable to initialise query system on {}:{}", new Object[] { this.serverIp, Integer.valueOf(this.port), e });
/* 316 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class RequestChallenge {
/* 327 */     private final long time = (new Date()).getTime();
/*     */     
/*     */     private final int challenge;
/*     */     
/*     */     private final byte[] identBytes;
/*     */     
/*     */     private final byte[] challengeBytes;
/*     */     
/*     */     private final String ident;
/*     */     
/*     */     public RequestChallenge(DatagramPacket src) {
/* 328 */       byte[] buf = src.getData();
/* 329 */       this.identBytes = new byte[4];
/* 330 */       this.identBytes[0] = buf[3];
/* 331 */       this.identBytes[1] = buf[4];
/* 332 */       this.identBytes[2] = buf[5];
/* 333 */       this.identBytes[3] = buf[6];
/* 334 */       this.ident = new String(this.identBytes, StandardCharsets.UTF_8);
/* 335 */       this.challenge = RandomSource.createThreadLocalInstance().nextInt(16777216);
/* 336 */       this.challengeBytes = String.format(Locale.ROOT, "\t%s%d\000", new Object[] { this.ident, Integer.valueOf(this.challenge) }).getBytes(StandardCharsets.UTF_8);
/*     */     }
/*     */     
/*     */     public Boolean before(long time) {
/* 340 */       return Boolean.valueOf((this.time < time));
/*     */     }
/*     */     
/*     */     public int getChallenge() {
/* 344 */       return this.challenge;
/*     */     }
/*     */     
/*     */     public byte[] getChallengeBytes() {
/* 348 */       return this.challengeBytes;
/*     */     }
/*     */     
/*     */     public byte[] getIdentBytes() {
/* 352 */       return this.identBytes;
/*     */     }
/*     */     
/*     */     public String getIdent() {
/* 357 */       return this.ident;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\rcon\thread\QueryThreadGs4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */