/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.jsonrpc.methods.GameRulesService;
/*    */ import net.minecraft.server.notifications.NotificationManager;
/*    */ import net.minecraft.world.level.gamerules.GameRule;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ public class MinecraftGameRuleServiceImpl implements MinecraftGameRuleService {
/*    */   private final NotificationManager notificationManager;
/*    */   
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftGameRuleServiceImpl(NotificationManager notificationManager, JsonRpcLogger jsonrpcLogger) {
/* 21 */     this.notificationManager = notificationManager;
/* 22 */     this.jsonrpcLogger = jsonrpcLogger;
/*    */   }
/*    */   
/*    */   private DedicatedServer server() {
/* 26 */     return Objects.<DedicatedServer>requireNonNull(this.notificationManager.server());
/*    */   }
/*    */   
/*    */   public <T> GameRulesService.GameRuleUpdate<T> updateGameRule(GameRulesService.GameRuleUpdate<T> update, ClientInfo clientInfo) {
/* 31 */     GameRule<T> gameRule = update.gameRule();
/* 32 */     DedicatedServer dedicatedServer = server();
/* 33 */     GameRules gameRules = dedicatedServer.getGameRules();
/* 34 */     T oldValue = (T)gameRules.get(gameRule);
/* 35 */     T newValue = (T)update.value();
/* 36 */     gameRules.set(gameRule, newValue, (MinecraftServer)dedicatedServer);
/* 37 */     this.jsonrpcLogger.log(clientInfo, "Game rule '{}' updated from '{}' to '{}'", new Object[] { gameRule.id(), gameRule.serialize(oldValue), gameRule.serialize(newValue) });
/* 38 */     return update;
/*    */   }
/*    */   
/*    */   public <T> GameRulesService.GameRuleUpdate<T> getTypedRule(GameRule<T> gameRule, T value) {
/* 43 */     return new GameRulesService.GameRuleUpdate(gameRule, value);
/*    */   }
/*    */   
/*    */   public Stream<GameRule<?>> getAvailableGameRules() {
/* 48 */     return server().getGameRules().availableRules();
/*    */   }
/*    */   
/*    */   public <T> T getRuleValue(GameRule<T> gameRule) {
/* 53 */     return (T)server().getGameRules().get(gameRule);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftGameRuleServiceImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */