/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ class null extends Player {
/*     */   null(GameTestHelper this$0, Level level, GameProfile gameProfile) {
/* 374 */     super(level, gameProfile);
/*     */   }
/*     */   
/*     */   public GameType gameMode() {
/* 377 */     return gameType;
/*     */   }
/*     */   
/*     */   public boolean isClientAuthoritative() {
/* 382 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestHelper$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */