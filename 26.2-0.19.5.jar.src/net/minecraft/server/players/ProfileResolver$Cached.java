/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.common.cache.CacheBuilder;
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.google.common.cache.LoadingCache;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*    */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*    */ import java.time.Duration;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.util.StringUtil;
/*    */ 
/*    */ public class Cached implements ProfileResolver {
/* 36 */   private final LoadingCache<UUID, Optional<GameProfile>> profileCacheById = CacheBuilder.newBuilder()
/* 37 */     .expireAfterAccess(Duration.ofMinutes(10L))
/* 38 */     .maximumSize(256L)
/* 39 */     .build(new CacheLoader<UUID, Optional<GameProfile>>(this) {
/*    */         public Optional<GameProfile> load(UUID profileId) {
/* 42 */           ProfileResult result = sessionService.fetchProfile(profileId, true);
/* 43 */           return Optional.<ProfileResult>ofNullable(result).map(ProfileResult::profile);
/*    */         }
/*    */       });
/*    */   
/* 47 */   private final LoadingCache<String, Optional<GameProfile>> profileCacheByName = CacheBuilder.newBuilder()
/* 48 */     .expireAfterAccess(Duration.ofMinutes(10L))
/* 49 */     .maximumSize(256L)
/* 50 */     .build(new CacheLoader<String, Optional<GameProfile>>() {
/*    */         public Optional<GameProfile> load(String name) {
/* 53 */           return nameToIdCache
/* 54 */             .get(name)
/* 55 */             .flatMap(nameAndId -> (Optional)ProfileResolver.Cached.this.profileCacheById.getUnchecked(nameAndId.id()));
/*    */         }
/*    */       });
/*    */   
/*    */   public Cached(final MinecraftSessionService sessionService, final UserNameToIdResolver nameToIdCache) {}
/*    */   
/*    */   public Optional<GameProfile> fetchByName(String name) {
/* 62 */     if (StringUtil.isValidPlayerName(name))
/* 63 */       return (Optional<GameProfile>)this.profileCacheByName.getUnchecked(name); 
/* 65 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   public Optional<GameProfile> fetchById(UUID id) {
/* 70 */     return (Optional<GameProfile>)this.profileCacheById.getUnchecked(id);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\ProfileResolver$Cached.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */