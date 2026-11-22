/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ 
/*     */ class null implements ClientboundBossEventPacket.Operation {
/*     */   public ClientboundBossEventPacket.OperationType getType() {
/* 191 */     return ClientboundBossEventPacket.OperationType.REMOVE;
/*     */   }
/*     */   
/*     */   public void dispatch(UUID id, ClientboundBossEventPacket.Handler handler) {
/* 196 */     handler.remove(id);
/*     */   }
/*     */   
/*     */   public void write(RegistryFriendlyByteBuf output) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */