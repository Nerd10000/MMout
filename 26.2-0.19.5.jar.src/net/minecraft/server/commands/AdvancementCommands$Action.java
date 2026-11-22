/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ 
/*     */ enum Action {
/* 245 */   GRANT("grant") {
/*     */     protected boolean perform(ServerPlayer player, AdvancementHolder advancement) {
/* 248 */       AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
/* 249 */       if (progress.isDone())
/* 250 */         return false; 
/* 252 */       for (String criterion : progress.getRemainingCriteria())
/* 253 */         player.getAdvancements().award(advancement, criterion); 
/* 255 */       return true;
/*     */     }
/*     */     
/*     */     protected boolean performCriterion(ServerPlayer player, AdvancementHolder advancement, String criterion) {
/* 260 */       return player.getAdvancements().award(advancement, criterion);
/*     */     }
/*     */   },
/* 263 */   REVOKE("revoke") {
/*     */     protected boolean perform(ServerPlayer player, AdvancementHolder advancement) {
/* 266 */       AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
/* 267 */       if (!progress.hasProgress())
/* 268 */         return false; 
/* 270 */       for (String criterion : progress.getCompletedCriteria())
/* 271 */         player.getAdvancements().revoke(advancement, criterion); 
/* 273 */       return true;
/*     */     }
/*     */     
/*     */     protected boolean performCriterion(ServerPlayer player, AdvancementHolder advancement, String criterion) {
/* 278 */       return player.getAdvancements().revoke(advancement, criterion);
/*     */     }
/*     */   };
/*     */   
/*     */   private final String key;
/*     */   
/*     */   Action(String key) {
/* 286 */     this.key = "commands.advancement." + key;
/*     */   }
/*     */   
/*     */   public int perform(ServerPlayer player, Iterable<AdvancementHolder> advancements, boolean showAdvancements) {
/* 290 */     int count = 0;
/* 291 */     if (!showAdvancements)
/* 293 */       player.getAdvancements().flushDirty(player, true); 
/* 295 */     for (AdvancementHolder advancement : advancements) {
/* 296 */       if (perform(player, advancement))
/* 297 */         count++; 
/*     */     } 
/* 300 */     if (!showAdvancements)
/* 301 */       player.getAdvancements().flushDirty(player, false); 
/* 303 */     return count;
/*     */   }
/*     */   
/*     */   protected String getKey() {
/* 311 */     return this.key;
/*     */   }
/*     */   
/*     */   protected abstract boolean perform(ServerPlayer paramServerPlayer, AdvancementHolder paramAdvancementHolder);
/*     */   
/*     */   protected abstract boolean performCriterion(ServerPlayer paramServerPlayer, AdvancementHolder paramAdvancementHolder, String paramString);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\AdvancementCommands$Action.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */