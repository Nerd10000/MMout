/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.mojang.util.UndashedUuid;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class User {
/*    */   private final String name;
/*    */   
/*    */   private final UUID uuid;
/*    */   
/*    */   private final String accessToken;
/*    */   
/*    */   private final Optional<String> xuid;
/*    */   
/*    */   private final Optional<String> clientId;
/*    */   
/*    */   public User(String name, UUID uuid, String accessToken, Optional<String> xuid, Optional<String> clientId) {
/* 16 */     this.name = name;
/* 17 */     this.uuid = uuid;
/* 18 */     this.accessToken = accessToken;
/* 19 */     this.xuid = xuid;
/* 20 */     this.clientId = clientId;
/*    */   }
/*    */   
/*    */   public String getSessionId() {
/* 24 */     return "token:" + this.accessToken + ":" + UndashedUuid.toString(this.uuid);
/*    */   }
/*    */   
/*    */   public UUID getProfileId() {
/* 28 */     return this.uuid;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 32 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getAccessToken() {
/* 36 */     return this.accessToken;
/*    */   }
/*    */   
/*    */   public Optional<String> getClientId() {
/* 40 */     return this.clientId;
/*    */   }
/*    */   
/*    */   public Optional<String> getXuid() {
/* 44 */     return this.xuid;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\User.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */