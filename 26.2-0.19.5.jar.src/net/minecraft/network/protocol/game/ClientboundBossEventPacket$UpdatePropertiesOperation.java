/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ 
/*     */ class UpdatePropertiesOperation implements ClientboundBossEventPacket.Operation {
/*     */   private final boolean darkenScreen;
/*     */   
/*     */   private final boolean playMusic;
/*     */   
/*     */   private final boolean createWorldFog;
/*     */   
/*     */   private UpdatePropertiesOperation(boolean darkenScreen, boolean playMusic, boolean createWorldFog) {
/* 283 */     this.darkenScreen = darkenScreen;
/* 284 */     this.playMusic = playMusic;
/* 285 */     this.createWorldFog = createWorldFog;
/*     */   }
/*     */   
/*     */   private UpdatePropertiesOperation(RegistryFriendlyByteBuf input) {
/* 289 */     int flags = input.readUnsignedByte();
/* 290 */     this.darkenScreen = ((flags & 0x1) > 0);
/* 291 */     this.playMusic = ((flags & 0x2) > 0);
/* 292 */     this.createWorldFog = ((flags & 0x4) > 0);
/*     */   }
/*     */   
/*     */   public ClientboundBossEventPacket.OperationType getType() {
/* 297 */     return ClientboundBossEventPacket.OperationType.UPDATE_PROPERTIES;
/*     */   }
/*     */   
/*     */   public void dispatch(UUID id, ClientboundBossEventPacket.Handler handler) {
/* 302 */     handler.updateProperties(id, this.darkenScreen, this.playMusic, this.createWorldFog);
/*     */   }
/*     */   
/*     */   public void write(RegistryFriendlyByteBuf output) {
/* 307 */     output.writeByte(ClientboundBossEventPacket.encodeProperties(this.darkenScreen, this.playMusic, this.createWorldFog));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$UpdatePropertiesOperation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */