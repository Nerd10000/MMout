/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ 
/*     */ final class UpdateNameOperation extends Record implements ClientboundBossEventPacket.Operation {
/*     */   private final Component name;
/*     */   
/*     */   private UpdateNameOperation(Component name) {
/* 225 */     this.name = name;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateNameOperation;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #225	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateNameOperation;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateNameOperation;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #225	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateNameOperation;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateNameOperation;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #225	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateNameOperation;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Component name() {
/* 225 */     return this.name;
/*     */   }
/*     */   
/*     */   private UpdateNameOperation(RegistryFriendlyByteBuf input) {
/* 227 */     this((Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input));
/*     */   }
/*     */   
/*     */   public ClientboundBossEventPacket.OperationType getType() {
/* 232 */     return ClientboundBossEventPacket.OperationType.UPDATE_NAME;
/*     */   }
/*     */   
/*     */   public void dispatch(UUID id, ClientboundBossEventPacket.Handler handler) {
/* 237 */     handler.updateName(id, this.name);
/*     */   }
/*     */   
/*     */   public void write(RegistryFriendlyByteBuf output) {
/* 242 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.name);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$UpdateNameOperation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */