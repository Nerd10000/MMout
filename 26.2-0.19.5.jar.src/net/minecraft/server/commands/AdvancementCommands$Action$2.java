/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ 
/*     */ enum null {
/*     */   protected boolean perform(ServerPlayer player, AdvancementHolder advancement) {
/* 266 */     AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
/* 267 */     if (!progress.hasProgress())
/* 268 */       return false; 
/* 270 */     for (String criterion : progress.getCompletedCriteria())
/* 271 */       player.getAdvancements().revoke(advancement, criterion); 
/* 273 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean performCriterion(ServerPlayer player, AdvancementHolder advancement, String criterion) {
/* 278 */     return player.getAdvancements().revoke(advancement, criterion);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\AdvancementCommands$Action$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */