/*    */ package net.minecraft.client.telemetry.events;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class Measurement extends Record {
/*    */   private final int millis;
/*    */   
/*    */   public static final Codec<Measurement> CODEC;
/*    */   
/*    */   public Measurement(int millis) {
/* 75 */     this.millis = millis;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/telemetry/events/GameLoadTimesEvent$Measurement;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #75	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/telemetry/events/GameLoadTimesEvent$Measurement;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/telemetry/events/GameLoadTimesEvent$Measurement;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #75	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/telemetry/events/GameLoadTimesEvent$Measurement;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/telemetry/events/GameLoadTimesEvent$Measurement;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #75	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/telemetry/events/GameLoadTimesEvent$Measurement;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int millis() {
/* 75 */     return this.millis;
/*    */   }
/*    */   
/*    */   static {
/* 76 */     CODEC = Codec.INT.xmap(Measurement::new, o -> Integer.valueOf(o.millis));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\telemetry\events\GameLoadTimesEvent$Measurement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */