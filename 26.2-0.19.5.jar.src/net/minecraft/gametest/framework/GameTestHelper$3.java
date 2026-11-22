/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ClientInformation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ class null extends ServerPlayer {
/*     */   null(GameTestHelper this$0, MinecraftServer server, ServerLevel level, GameProfile gameProfile, ClientInformation clientInformation) {
/* 408 */     super(server, level, gameProfile, clientInformation);
/*     */   }
/*     */   
/*     */   public GameType gameMode() {
/* 411 */     return GameType.CREATIVE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestHelper$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */