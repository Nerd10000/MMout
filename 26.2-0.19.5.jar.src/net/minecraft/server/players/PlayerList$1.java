/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDelayPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.border.BorderChangeListener;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ 
/*     */ class null implements BorderChangeListener {
/*     */   public void onSetSize(WorldBorder border, double newSize) {
/* 263 */     PlayerList.this.broadcastAll((Packet<?>)new ClientboundSetBorderSizePacket(border), level.dimension());
/*     */   }
/*     */   
/*     */   public void onLerpSize(WorldBorder border, double fromSize, double targetSize, long ticks, long gameTime) {
/* 268 */     PlayerList.this.broadcastAll((Packet<?>)new ClientboundSetBorderLerpSizePacket(border), level.dimension());
/*     */   }
/*     */   
/*     */   public void onSetCenter(WorldBorder border, double x, double z) {
/* 273 */     PlayerList.this.broadcastAll((Packet<?>)new ClientboundSetBorderCenterPacket(border), level.dimension());
/*     */   }
/*     */   
/*     */   public void onSetWarningTime(WorldBorder border, int time) {
/* 278 */     PlayerList.this.broadcastAll((Packet<?>)new ClientboundSetBorderWarningDelayPacket(border), level.dimension());
/*     */   }
/*     */   
/*     */   public void onSetWarningBlocks(WorldBorder border, int blocks) {
/* 283 */     PlayerList.this.broadcastAll((Packet<?>)new ClientboundSetBorderWarningDistancePacket(border), level.dimension());
/*     */   }
/*     */   
/*     */   public void onSetDamagePerBlock(WorldBorder border, double damagePerBlock) {}
/*     */   
/*     */   public void onSetSafeZone(WorldBorder border, double safeZone) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\PlayerList$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */