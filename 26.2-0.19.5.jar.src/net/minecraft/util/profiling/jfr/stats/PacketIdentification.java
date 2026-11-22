/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ import jdk.jfr.consumer.RecordedEvent;
/*    */ 
/*    */ public final class PacketIdentification extends Record {
/*    */   private final String direction;
/*    */   
/*    */   private final String protocolId;
/*    */   
/*    */   private final String packetId;
/*    */   
/*    */   public PacketIdentification(String direction, String protocolId, String packetId) {
/*  6 */     this.direction = direction;
/*  6 */     this.protocolId = protocolId;
/*  6 */     this.packetId = packetId;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/PacketIdentification;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/PacketIdentification;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/PacketIdentification;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/PacketIdentification;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/PacketIdentification;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/PacketIdentification;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String direction() {
/*  6 */     return this.direction;
/*    */   }
/*    */   
/*    */   public String protocolId() {
/*  6 */     return this.protocolId;
/*    */   }
/*    */   
/*    */   public String packetId() {
/*  6 */     return this.packetId;
/*    */   }
/*    */   
/*    */   public static PacketIdentification from(RecordedEvent event) {
/*  8 */     return new PacketIdentification(event
/*  9 */         .getString("packetDirection"), event
/* 10 */         .getString("protocolId"), event
/* 11 */         .getString("packetId"));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\stats\PacketIdentification.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */