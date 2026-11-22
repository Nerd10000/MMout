/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ 
/*     */ class null extends ServerTextFilter.PlayerContext {
/*     */   null(GameProfile profile) {
/* 118 */     super(profile);
/*     */   }
/*     */   
/*     */   public void join() {
/* 121 */     LegacyTextFilter.this.processJoinOrLeave(this.profile, LegacyTextFilter.this.joinEndpoint, LegacyTextFilter.this.joinEncoder, this.streamExecutor);
/*     */   }
/*     */   
/*     */   public void leave() {
/* 126 */     LegacyTextFilter.this.processJoinOrLeave(this.profile, LegacyTextFilter.this.leaveEndpoint, LegacyTextFilter.this.leaveEncoder, this.streamExecutor);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\LegacyTextFilter$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */