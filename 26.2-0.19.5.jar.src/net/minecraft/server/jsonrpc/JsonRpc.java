/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import com.google.common.net.HostAndPort;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import io.netty.handler.ssl.SslContext;
/*    */ import net.minecraft.server.dedicated.DedicatedServerProperties;
/*    */ import net.minecraft.server.dedicated.DedicatedServerSettings;
/*    */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*    */ import net.minecraft.server.jsonrpc.security.AuthenticationHandler;
/*    */ import net.minecraft.server.jsonrpc.security.JsonRpcSslContextProvider;
/*    */ import net.minecraft.server.jsonrpc.security.SecurityConfig;
/*    */ import net.minecraft.server.notifications.NotificationManager;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class JsonRpc {
/* 17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static ManagementServer create(DedicatedServerSettings settings, NotificationManager notificationManager) {
/* 20 */     DedicatedServerProperties properties = settings.getProperties();
/* 21 */     if (properties.managementServerEnabled) {
/* 22 */       String managementServerSecret = properties.managementServerSecret;
/* 23 */       if (!SecurityConfig.isValid(managementServerSecret))
/* 24 */         throw new IllegalStateException("Invalid management server secret, must be 40 alphanumeric characters"); 
/* 27 */       String managementHost = properties.managementServerHost;
/* 28 */       int managementPort = properties.managementServerPort;
/* 29 */       HostAndPort hostAndPort = HostAndPort.fromParts(managementHost, managementPort);
/* 30 */       SecurityConfig securityConfig = new SecurityConfig(managementServerSecret);
/* 31 */       String allowedOrigins = properties.managementServerAllowedOrigins;
/* 32 */       AuthenticationHandler authenticationHandler = new AuthenticationHandler(securityConfig, allowedOrigins);
/* 33 */       LOGGER.info("Starting json RPC server on {}", hostAndPort);
/* 34 */       ManagementServer jsonRpcServer = new ManagementServer(hostAndPort, authenticationHandler);
/* 35 */       MinecraftApi minecraftApi = MinecraftApi.of(notificationManager);
/* 36 */       minecraftApi.notificationManager().registerService(new JsonRpcNotificationService(minecraftApi, jsonRpcServer));
/* 38 */       if (properties.managementServerTlsEnabled) {
/* 39 */         SslContext sslContext = createSslContext(properties);
/* 40 */         jsonRpcServer.startWithTls(minecraftApi, sslContext);
/*    */       } else {
/* 42 */         jsonRpcServer.startWithoutTls(minecraftApi);
/*    */       } 
/* 44 */       jsonRpcServer.scheduleHeartbeat(notificationManager, ((Integer)properties.statusHeartbeatInterval.get()).intValue());
/* 45 */       return jsonRpcServer;
/*    */     } 
/* 47 */     return null;
/*    */   }
/*    */   
/*    */   private static SslContext createSslContext(DedicatedServerProperties properties) {
/*    */     try {
/* 52 */       return JsonRpcSslContextProvider.createFrom(properties.managementServerTlsKeystore, properties.managementServerTlsKeystorePassword);
/* 55 */     } catch (Exception e) {
/* 56 */       JsonRpcSslContextProvider.printInstructions();
/* 57 */       throw new IllegalStateException("Failed to configure TLS for the server management protocol", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\JsonRpc.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */