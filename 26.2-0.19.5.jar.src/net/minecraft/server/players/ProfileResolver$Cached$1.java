/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*    */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ 
/*    */ class null extends CacheLoader<UUID, Optional<GameProfile>> {
/*    */   null(ProfileResolver.Cached this$0) {}
/*    */   
/*    */   public Optional<GameProfile> load(UUID profileId) {
/* 42 */     ProfileResult result = sessionService.fetchProfile(profileId, true);
/* 43 */     return Optional.<ProfileResult>ofNullable(result).map(ProfileResult::profile);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\ProfileResolver$Cached$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */