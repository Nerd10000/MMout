/*    */ package net.minecraft.client.player;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*    */ import net.minecraft.client.multiplayer.PlayerInfo;
/*    */ import net.minecraft.server.players.ProfileResolver;
/*    */ 
/*    */ public class LocalPlayerResolver implements ProfileResolver {
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   private final ProfileResolver parentResolver;
/*    */   
/*    */   public LocalPlayerResolver(Minecraft minecraft, ProfileResolver parentResolver) {
/* 19 */     this.minecraft = minecraft;
/* 20 */     this.parentResolver = parentResolver;
/*    */   }
/*    */   
/*    */   public Optional<GameProfile> fetchByName(String name) {
/* 25 */     ClientPacketListener connection = this.minecraft.getConnection();
/* 26 */     if (connection != null) {
/* 28 */       PlayerInfo playerInfo = connection.getPlayerInfoIgnoreCase(name);
/* 29 */       if (playerInfo != null)
/* 30 */         return Optional.of(playerInfo.getProfile()); 
/*    */     } 
/* 34 */     return this.parentResolver.fetchByName(name);
/*    */   }
/*    */   
/*    */   public Optional<GameProfile> fetchById(UUID id) {
/* 39 */     ClientPacketListener connection = this.minecraft.getConnection();
/* 40 */     if (connection != null) {
/* 41 */       PlayerInfo playerInfo = connection.getPlayerInfo(id);
/* 42 */       if (playerInfo != null)
/* 43 */         return Optional.of(playerInfo.getProfile()); 
/*    */     } 
/* 47 */     return this.parentResolver.fetchById(id);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\player\LocalPlayerResolver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */