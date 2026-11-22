/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.network.ServerboundPacketListener;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ 
/*     */ final class DeferredPacket extends Record {
/*     */   private final Packet<? extends ServerboundPacketListener> packet;
/*     */   
/*     */   private final BooleanSupplier sendCondition;
/*     */   
/*     */   private final long expirationTime;
/*     */   
/*     */   private DeferredPacket(Packet<? extends ServerboundPacketListener> packet, BooleanSupplier sendCondition, long expirationTime) {
/* 429 */     this.packet = packet;
/* 429 */     this.sendCondition = sendCondition;
/* 429 */     this.expirationTime = expirationTime;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$DeferredPacket;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #429	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$DeferredPacket;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$DeferredPacket;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #429	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$DeferredPacket;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$DeferredPacket;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #429	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$DeferredPacket;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Packet<? extends ServerboundPacketListener> packet() {
/* 429 */     return this.packet;
/*     */   }
/*     */   
/*     */   public BooleanSupplier sendCondition() {
/* 429 */     return this.sendCondition;
/*     */   }
/*     */   
/*     */   public long expirationTime() {
/* 429 */     return this.expirationTime;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientCommonPacketListenerImpl$DeferredPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */