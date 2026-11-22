/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ class null implements ClientboundBossEventPacket.Handler {
/*     */   public void add(UUID id, Component name, float progress, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay, boolean darkenScreen, boolean playMusic, boolean createWorldFog) {
/* 115 */     BossHealthOverlay.this.events.put(id, new LerpingBossEvent(id, name, progress, color, overlay, darkenScreen, playMusic, createWorldFog));
/*     */   }
/*     */   
/*     */   public void remove(UUID id) {
/* 120 */     BossHealthOverlay.this.events.remove(id);
/*     */   }
/*     */   
/*     */   public void updateProgress(UUID id, float progress) {
/* 125 */     ((LerpingBossEvent)BossHealthOverlay.this.events.get(id)).setProgress(progress);
/*     */   }
/*     */   
/*     */   public void updateName(UUID id, Component name) {
/* 130 */     ((LerpingBossEvent)BossHealthOverlay.this.events.get(id)).setName(name);
/*     */   }
/*     */   
/*     */   public void updateStyle(UUID id, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay) {
/* 135 */     LerpingBossEvent event = BossHealthOverlay.this.events.get(id);
/* 136 */     event.setColor(color);
/* 137 */     event.setOverlay(overlay);
/*     */   }
/*     */   
/*     */   public void updateProperties(UUID id, boolean darkenScreen, boolean playMusic, boolean createWorldFog) {
/* 142 */     LerpingBossEvent event = BossHealthOverlay.this.events.get(id);
/* 143 */     event.setDarkenScreen(darkenScreen);
/* 144 */     event.setPlayBossMusic(playMusic);
/* 145 */     event.setCreateWorldFog(createWorldFog);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\BossHealthOverlay$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */