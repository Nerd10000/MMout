/*     */ package com.mojang.realmsclient.dto;
/*     */ 
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public enum WorldType {
/* 333 */   NORMAL("normal"),
/* 334 */   MINIGAME("minigame"),
/* 335 */   ADVENTUREMAP("adventureMap"),
/* 336 */   EXPERIENCE("experience"),
/* 337 */   INSPIRATION("inspiration"),
/* 338 */   UNKNOWN("unknown");
/*     */   
/*     */   private static final String TRANSLATION_PREFIX = "mco.backup.entry.worldType.";
/*     */   
/*     */   private final Component displayName;
/*     */   
/*     */   WorldType(String translationKey) {
/* 343 */     this.displayName = (Component)Component.translatable("mco.backup.entry.worldType." + translationKey);
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/* 347 */     return this.displayName;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsServer$WorldType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */