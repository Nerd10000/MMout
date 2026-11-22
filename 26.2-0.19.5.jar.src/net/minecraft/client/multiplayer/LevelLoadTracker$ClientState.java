/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ interface ClientState {
/*     */   default ClientState tick() {
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   default ClientState loadingPacketsReceived() {
/* 115 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\LevelLoadTracker$ClientState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */