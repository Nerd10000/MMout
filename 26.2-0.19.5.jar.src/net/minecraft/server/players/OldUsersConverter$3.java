/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.mojang.authlib.ProfileLookupCallback;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ 
/*     */ class null implements ProfileLookupCallback {
/*     */   public void onProfileLookupSucceeded(String profileName, UUID profileId) {
/* 209 */     NameAndId profile = new NameAndId(profileId, profileName);
/* 210 */     server.services().nameToIdCache().add(profile);
/* 211 */     whitelist.add(new UserWhiteListEntry(profile));
/*     */   }
/*     */   
/*     */   public void onProfileLookupFailed(String profileName, Exception exception) {
/* 216 */     OldUsersConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", profileName, exception);
/* 217 */     if (!(exception instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException))
/* 218 */       throw new OldUsersConverter.ConversionError("Could not request user " + profileName + " from backend systems", exception); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\OldUsersConverter$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */