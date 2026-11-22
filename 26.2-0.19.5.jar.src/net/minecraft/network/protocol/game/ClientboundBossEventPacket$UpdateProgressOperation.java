/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ 
/*     */ final class UpdateProgressOperation extends Record implements ClientboundBossEventPacket.Operation {
/*     */   private final float progress;
/*     */   
/*     */   private UpdateProgressOperation(float progress) {
/* 204 */     this.progress = progress;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateProgressOperation;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #204	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateProgressOperation;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateProgressOperation;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #204	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateProgressOperation;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateProgressOperation;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #204	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateProgressOperation;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float progress() {
/* 204 */     return this.progress;
/*     */   }
/*     */   
/*     */   private UpdateProgressOperation(RegistryFriendlyByteBuf input) {
/* 206 */     this(input.readFloat());
/*     */   }
/*     */   
/*     */   public ClientboundBossEventPacket.OperationType getType() {
/* 211 */     return ClientboundBossEventPacket.OperationType.UPDATE_PROGRESS;
/*     */   }
/*     */   
/*     */   public void dispatch(UUID id, ClientboundBossEventPacket.Handler handler) {
/* 216 */     handler.updateProgress(id, this.progress);
/*     */   }
/*     */   
/*     */   public void write(RegistryFriendlyByteBuf output) {
/* 221 */     output.writeFloat(this.progress);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$UpdateProgressOperation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */