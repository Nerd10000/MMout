/*     */ package net.minecraft.server.jsonrpc.security;
/*     */ 
/*     */ class SecurityCheckResult {
/*     */   private final boolean allowed;
/*     */   
/*     */   private final String reason;
/*     */   
/*     */   private final boolean tokenSentInSecWebsocketProtocol;
/*     */   
/*     */   private SecurityCheckResult(boolean allowed, String reason, boolean tokenSentInSecWebsocketProtocol) {
/* 175 */     this.allowed = allowed;
/* 176 */     this.reason = reason;
/* 177 */     this.tokenSentInSecWebsocketProtocol = tokenSentInSecWebsocketProtocol;
/*     */   }
/*     */   
/*     */   public static SecurityCheckResult allowed() {
/* 181 */     return new SecurityCheckResult(true, null, false);
/*     */   }
/*     */   
/*     */   public static SecurityCheckResult allowed(boolean tokenSentInSecWebsocketProtocol) {
/* 185 */     return new SecurityCheckResult(true, null, tokenSentInSecWebsocketProtocol);
/*     */   }
/*     */   
/*     */   public static SecurityCheckResult denied(String reason) {
/* 189 */     return new SecurityCheckResult(false, reason, false);
/*     */   }
/*     */   
/*     */   public boolean isAllowed() {
/* 193 */     return this.allowed;
/*     */   }
/*     */   
/*     */   public String getReason() {
/* 197 */     return this.reason;
/*     */   }
/*     */   
/*     */   public boolean isTokenSentInSecWebsocketProtocol() {
/* 201 */     return this.tokenSentInSecWebsocketProtocol;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\security\AuthenticationHandler$SecurityCheckResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */