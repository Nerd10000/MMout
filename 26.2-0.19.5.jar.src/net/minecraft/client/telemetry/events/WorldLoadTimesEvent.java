/*    */ package net.minecraft.client.telemetry.events;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import net.minecraft.client.telemetry.TelemetryEventSender;
/*    */ import net.minecraft.client.telemetry.TelemetryEventType;
/*    */ import net.minecraft.client.telemetry.TelemetryProperty;
/*    */ import net.minecraft.client.telemetry.TelemetryPropertyMap;
/*    */ 
/*    */ public class WorldLoadTimesEvent {
/*    */   private final boolean newWorld;
/*    */   
/*    */   private final Duration worldLoadDuration;
/*    */   
/*    */   public WorldLoadTimesEvent(boolean newWorld, Duration worldLoadDuration) {
/* 15 */     this.worldLoadDuration = worldLoadDuration;
/* 16 */     this.newWorld = newWorld;
/*    */   }
/*    */   
/*    */   public void send(TelemetryEventSender eventSender) {
/* 20 */     if (this.worldLoadDuration != null)
/* 21 */       eventSender.send(TelemetryEventType.WORLD_LOAD_TIMES, event -> {
/*    */             event.put(TelemetryProperty.WORLD_LOAD_TIME_MS, Integer.valueOf((int)this.worldLoadDuration.toMillis()));
/*    */             event.put(TelemetryProperty.NEW_WORLD, Boolean.valueOf(this.newWorld));
/*    */           }); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\telemetry\events\WorldLoadTimesEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */