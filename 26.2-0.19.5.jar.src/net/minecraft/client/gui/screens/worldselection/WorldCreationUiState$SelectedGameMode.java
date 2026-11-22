/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public enum SelectedGameMode {
/* 314 */   SURVIVAL("survival", GameType.SURVIVAL),
/* 315 */   HARDCORE("hardcore", GameType.SURVIVAL),
/* 316 */   CREATIVE("creative", GameType.CREATIVE),
/* 318 */   DEBUG("spectator", GameType.SPECTATOR);
/*     */   
/*     */   public final GameType gameType;
/*     */   
/*     */   public final Component displayName;
/*     */   
/*     */   private final Component info;
/*     */   
/*     */   SelectedGameMode(String name, GameType gameType) {
/* 326 */     this.gameType = gameType;
/* 327 */     this.displayName = (Component)Component.translatable("selectWorld.gameMode." + name);
/* 328 */     this.info = (Component)Component.translatable("selectWorld.gameMode." + name + ".info");
/*     */   }
/*     */   
/*     */   public Component getInfo() {
/* 332 */     return this.info;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldCreationUiState$SelectedGameMode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */