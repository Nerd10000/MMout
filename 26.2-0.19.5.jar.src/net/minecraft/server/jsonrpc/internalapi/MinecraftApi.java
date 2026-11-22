/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.notifications.NotificationManager;
/*    */ 
/*    */ public class MinecraftApi {
/*    */   private final NotificationManager notificationManager;
/*    */   
/*    */   private final MinecraftAllowListService allowListService;
/*    */   
/*    */   private final MinecraftBanListService banListService;
/*    */   
/*    */   private final MinecraftPlayerListService minecraftPlayerListService;
/*    */   
/*    */   private final MinecraftGameRuleService gameRuleService;
/*    */   
/*    */   private final MinecraftOperatorListService minecraftOperatorListService;
/*    */   
/*    */   private final MinecraftServerSettingsService minecraftServerSettingsService;
/*    */   
/*    */   private final MinecraftServerStateService minecraftServerStateService;
/*    */   
/*    */   private final MinecraftExecutorService executorService;
/*    */   
/*    */   public MinecraftApi(NotificationManager notificationManager, MinecraftAllowListService allowListService, MinecraftBanListService banListService, MinecraftPlayerListService minecraftPlayerListService, MinecraftGameRuleService gameRuleService, MinecraftOperatorListService minecraftOperatorListService, MinecraftServerSettingsService minecraftServerSettingsService, MinecraftServerStateService minecraftServerStateService, MinecraftExecutorService executorService) {
/* 31 */     this.notificationManager = notificationManager;
/* 32 */     this.allowListService = allowListService;
/* 33 */     this.banListService = banListService;
/* 34 */     this.minecraftPlayerListService = minecraftPlayerListService;
/* 35 */     this.gameRuleService = gameRuleService;
/* 36 */     this.minecraftOperatorListService = minecraftOperatorListService;
/* 37 */     this.minecraftServerSettingsService = minecraftServerSettingsService;
/* 38 */     this.minecraftServerStateService = minecraftServerStateService;
/* 39 */     this.executorService = executorService;
/*    */   }
/*    */   
/*    */   public <V> CompletableFuture<V> submit(Supplier<V> supplier) {
/* 43 */     return this.executorService.submit(supplier);
/*    */   }
/*    */   
/*    */   public CompletableFuture<Void> submit(Runnable runnable) {
/* 47 */     return this.executorService.submit(runnable);
/*    */   }
/*    */   
/*    */   public MinecraftAllowListService allowListService() {
/* 51 */     return this.allowListService;
/*    */   }
/*    */   
/*    */   public MinecraftBanListService banListService() {
/* 55 */     return this.banListService;
/*    */   }
/*    */   
/*    */   public MinecraftPlayerListService playerListService() {
/* 59 */     return this.minecraftPlayerListService;
/*    */   }
/*    */   
/*    */   public MinecraftGameRuleService gameRuleService() {
/* 63 */     return this.gameRuleService;
/*    */   }
/*    */   
/*    */   public MinecraftOperatorListService operatorListService() {
/* 67 */     return this.minecraftOperatorListService;
/*    */   }
/*    */   
/*    */   public MinecraftServerSettingsService serverSettingsService() {
/* 71 */     return this.minecraftServerSettingsService;
/*    */   }
/*    */   
/*    */   public MinecraftServerStateService serverStateService() {
/* 75 */     return this.minecraftServerStateService;
/*    */   }
/*    */   
/*    */   public NotificationManager notificationManager() {
/* 79 */     return this.notificationManager;
/*    */   }
/*    */   
/*    */   public static MinecraftApi of(NotificationManager notificationManager) {
/* 83 */     JsonRpcLogger jsonrpcLogger = new JsonRpcLogger();
/* 84 */     MinecraftAllowListServiceImpl allowListService = new MinecraftAllowListServiceImpl(notificationManager, jsonrpcLogger);
/* 85 */     MinecraftBanListServiceImpl banListService = new MinecraftBanListServiceImpl(notificationManager, jsonrpcLogger);
/* 86 */     MinecraftPlayerListServiceImpl playerListService = new MinecraftPlayerListServiceImpl(notificationManager, jsonrpcLogger);
/* 87 */     MinecraftGameRuleServiceImpl gameRuleService = new MinecraftGameRuleServiceImpl(notificationManager, jsonrpcLogger);
/* 88 */     MinecraftOperatorListServiceImpl operatorListService = new MinecraftOperatorListServiceImpl(notificationManager, jsonrpcLogger);
/* 89 */     MinecraftServerSettingsServiceImpl serverSettingsService = new MinecraftServerSettingsServiceImpl(notificationManager, jsonrpcLogger);
/* 90 */     MinecraftServerStateServiceImpl serverStateService = new MinecraftServerStateServiceImpl(notificationManager, jsonrpcLogger);
/* 91 */     MinecraftExecutorService executorService = new MinecraftExecutorServiceImpl(notificationManager);
/* 92 */     return new MinecraftApi(notificationManager, allowListService, banListService, playerListService, gameRuleService, operatorListService, serverSettingsService, serverStateService, executorService);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftApi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */