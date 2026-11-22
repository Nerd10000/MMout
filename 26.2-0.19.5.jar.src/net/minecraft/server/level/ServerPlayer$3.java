/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ class null implements CommandSource {
/*     */   public boolean acceptsSuccess() {
/* 376 */     return ((Boolean)ServerPlayer.this.level().getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean acceptsFailure() {
/* 381 */     return true;
/*     */   }
/*     */   
/*     */   public boolean shouldInformAdmins() {
/* 386 */     return true;
/*     */   }
/*     */   
/*     */   public void sendSystemMessage(Component message) {
/* 391 */     ServerPlayer.this.sendSystemMessage(message);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerPlayer$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */