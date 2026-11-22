/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ 
/*     */ enum null {
/*     */   protected boolean perform(ServerPlayer player, AdvancementHolder advancement) {
/* 248 */     AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
/* 249 */     if (progress.isDone())
/* 250 */       return false; 
/* 252 */     for (String criterion : progress.getRemainingCriteria())
/* 253 */       player.getAdvancements().award(advancement, criterion); 
/* 255 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean performCriterion(ServerPlayer player, AdvancementHolder advancement, String criterion) {
/* 260 */     return player.getAdvancements().award(advancement, criterion);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\AdvancementCommands$Action$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */