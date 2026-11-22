/*    */ package net.minecraft.client.telemetry;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface TelemetryEventSender {
/*    */   public static final TelemetryEventSender DISABLED = (type, buildFunction) -> {
/*    */     
/*    */     };
/*    */   
/*    */   default TelemetryEventSender decorate(Consumer<TelemetryPropertyMap.Builder> decorator) {
/* 10 */     return (type, buildFunction) -> send(type, ());
/*    */   }
/*    */   
/*    */   void send(TelemetryEventType paramTelemetryEventType, Consumer<TelemetryPropertyMap.Builder> paramConsumer);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\telemetry\TelemetryEventSender.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */