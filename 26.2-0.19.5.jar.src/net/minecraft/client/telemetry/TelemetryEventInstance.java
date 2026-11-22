/*    */ package net.minecraft.client.telemetry;
/*    */ 
/*    */ import com.mojang.authlib.minecraft.TelemetryEvent;
/*    */ import com.mojang.authlib.minecraft.TelemetrySession;
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public final class TelemetryEventInstance extends Record {
/*    */   private final TelemetryEventType type;
/*    */   
/*    */   private final TelemetryPropertyMap properties;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/telemetry/TelemetryEventInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/telemetry/TelemetryEventInstance;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/telemetry/TelemetryEventInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/telemetry/TelemetryEventInstance;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/telemetry/TelemetryEventInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/telemetry/TelemetryEventInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public TelemetryEventType type() {
/*  7 */     return this.type;
/*    */   }
/*    */   
/*    */   public TelemetryPropertyMap properties() {
/*  7 */     return this.properties;
/*    */   }
/*    */   
/*  8 */   public static final Codec<TelemetryEventInstance> CODEC = TelemetryEventType.CODEC.dispatchStable(TelemetryEventInstance::type, TelemetryEventType::codec);
/*    */   
/*    */   public TelemetryEventInstance(TelemetryEventType type, TelemetryPropertyMap properties) {
/* 11 */     properties.propertySet().forEach(property -> {
/*    */           if (!type.contains(property))
/*    */             throw new IllegalArgumentException("Property '" + property.id() + "' not expected for event: '" + type.id() + "'"); 
/*    */         });
/*    */     this.type = type;
/*    */     this.properties = properties;
/*    */   }
/*    */   
/*    */   public TelemetryEvent export(TelemetrySession session) {
/* 19 */     return this.type.export(session, this.properties);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\telemetry\TelemetryEventInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */