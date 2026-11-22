/*    */ package net.minecraft.client.telemetry;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.client.telemetry.events.PerformanceMetricsEvent;
/*    */ import net.minecraft.client.telemetry.events.WorldLoadEvent;
/*    */ import net.minecraft.client.telemetry.events.WorldLoadTimesEvent;
/*    */ import net.minecraft.client.telemetry.events.WorldUnloadEvent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class WorldSessionTelemetryManager {
/* 17 */   private final UUID worldSessionId = UUID.randomUUID();
/*    */   
/*    */   private final TelemetryEventSender eventSender;
/*    */   
/*    */   private final WorldLoadEvent worldLoadEvent;
/*    */   
/*    */   private final WorldUnloadEvent worldUnloadEvent;
/*    */   
/*    */   private final PerformanceMetricsEvent performanceMetricsEvent;
/*    */   
/*    */   private final WorldLoadTimesEvent worldLoadTimesEvent;
/*    */   
/*    */   public WorldSessionTelemetryManager(TelemetryEventSender eventSender, boolean newWorld, Duration worldLoadDuration, String minigameName, UUID sessionId) {
/* 26 */     this.worldUnloadEvent = new WorldUnloadEvent();
/* 27 */     this.worldLoadEvent = new WorldLoadEvent(minigameName);
/* 28 */     this.performanceMetricsEvent = new PerformanceMetricsEvent();
/* 29 */     this.worldLoadTimesEvent = new WorldLoadTimesEvent(newWorld, worldLoadDuration);
/* 31 */     this.eventSender = eventSender.decorate(properties -> {
/*    */           this.worldLoadEvent.addProperties(properties);
/*    */           properties.put(TelemetryProperty.WORLD_SESSION_ID, this.worldSessionId);
/*    */           properties.put(TelemetryProperty.SERVER_SESSION_ID, sessionId);
/*    */         });
/*    */   }
/*    */   
/*    */   public void tick() {
/* 39 */     this.performanceMetricsEvent.tick(this.eventSender);
/*    */   }
/*    */   
/*    */   public void onPlayerInfoReceived(GameType type, boolean hardcore) {
/* 43 */     this.worldLoadEvent.setGameMode(type, hardcore);
/* 44 */     this.worldUnloadEvent.onPlayerInfoReceived();
/* 45 */     worldSessionStart();
/*    */   }
/*    */   
/*    */   public void onServerBrandReceived(String serverBrand) {
/* 49 */     this.worldLoadEvent.setServerBrand(serverBrand);
/* 50 */     worldSessionStart();
/*    */   }
/*    */   
/*    */   public void setTime(long gameTime) {
/* 54 */     this.worldUnloadEvent.setTime(gameTime);
/*    */   }
/*    */   
/*    */   public void worldSessionStart() {
/* 58 */     if (this.worldLoadEvent.send(this.eventSender, false)) {
/* 59 */       this.worldLoadTimesEvent.send(this.eventSender);
/* 60 */       this.performanceMetricsEvent.start();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onDisconnect() {
/* 66 */     this.worldLoadEvent.send(this.eventSender, true);
/* 67 */     this.performanceMetricsEvent.stop();
/* 68 */     if (this.worldLoadEvent.wasSent())
/* 69 */       this.worldUnloadEvent.send(this.eventSender); 
/*    */   }
/*    */   
/*    */   public void onAdvancementDone(Level level, AdvancementHolder holder) {
/* 74 */     Identifier advancementId = holder.id();
/* 75 */     if (holder.value().sendsTelemetryEvent() && "minecraft".equals(advancementId.getNamespace())) {
/* 76 */       long gameTime = level.getGameTime();
/* 77 */       this.eventSender.send(TelemetryEventType.ADVANCEMENT_MADE, properties -> {
/*    */             properties.put(TelemetryProperty.ADVANCEMENT_ID, advancementId.toString());
/*    */             properties.put(TelemetryProperty.ADVANCEMENT_GAME_TIME, Long.valueOf(gameTime));
/*    */           });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\telemetry\WorldSessionTelemetryManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */