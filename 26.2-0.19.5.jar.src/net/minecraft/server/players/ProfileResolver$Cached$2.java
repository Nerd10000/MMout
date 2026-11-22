/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.Optional;
/*    */ 
/*    */ class null extends CacheLoader<String, Optional<GameProfile>> {
/*    */   public Optional<GameProfile> load(String name) {
/* 53 */     return nameToIdCache
/* 54 */       .get(name)
/* 55 */       .flatMap(nameAndId -> (Optional)ProfileResolver.Cached.this.profileCacheById.getUnchecked(nameAndId.id()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\ProfileResolver$Cached$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */