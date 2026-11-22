/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ class UpdateStyleOperation implements ClientboundBossEventPacket.Operation {
/*     */   private final BossEvent.BossBarColor color;
/*     */   
/*     */   private final BossEvent.BossBarOverlay overlay;
/*     */   
/*     */   private UpdateStyleOperation(BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay) {
/* 251 */     this.color = color;
/* 252 */     this.overlay = overlay;
/*     */   }
/*     */   
/*     */   private UpdateStyleOperation(RegistryFriendlyByteBuf input) {
/* 256 */     this.color = (BossEvent.BossBarColor)input.readEnum(BossEvent.BossBarColor.class);
/* 257 */     this.overlay = (BossEvent.BossBarOverlay)input.readEnum(BossEvent.BossBarOverlay.class);
/*     */   }
/*     */   
/*     */   public ClientboundBossEventPacket.OperationType getType() {
/* 262 */     return ClientboundBossEventPacket.OperationType.UPDATE_STYLE;
/*     */   }
/*     */   
/*     */   public void dispatch(UUID id, ClientboundBossEventPacket.Handler handler) {
/* 267 */     handler.updateStyle(id, this.color, this.overlay);
/*     */   }
/*     */   
/*     */   public void write(RegistryFriendlyByteBuf output) {
/* 272 */     output.writeEnum((Enum)this.color);
/* 273 */     output.writeEnum((Enum)this.overlay);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$UpdateStyleOperation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */