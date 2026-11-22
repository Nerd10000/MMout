/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.mojang.authlib.ProfileLookupCallback;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ 
/*     */ class null implements ProfileLookupCallback {
/*     */   public void onProfileLookupSucceeded(String profileName, UUID profileId) {
/* 257 */     NameAndId profile = new NameAndId(profileId, profileName);
/* 258 */     server.services().nameToIdCache().add(profile);
/* 259 */     profiles.add(profile);
/*     */   }
/*     */   
/*     */   public void onProfileLookupFailed(String profileName, Exception exception) {
/* 264 */     OldUsersConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", profileName, exception);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\OldUsersConverter$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */