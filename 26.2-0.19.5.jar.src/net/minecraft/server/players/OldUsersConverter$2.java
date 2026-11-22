/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.mojang.authlib.ProfileLookupCallback;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ 
/*     */ class null implements ProfileLookupCallback {
/*     */   public void onProfileLookupSucceeded(String profileName, UUID profileId) {
/* 166 */     NameAndId profile = new NameAndId(profileId, profileName);
/* 167 */     server.services().nameToIdCache().add(profile);
/* 168 */     opsList.add(new ServerOpListEntry(profile, server.operatorUserPermissions(), false));
/*     */   }
/*     */   
/*     */   public void onProfileLookupFailed(String profileName, Exception exception) {
/* 173 */     OldUsersConverter.LOGGER.warn("Could not lookup oplist entry for {}", profileName, exception);
/* 174 */     if (!(exception instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException))
/* 175 */       throw new OldUsersConverter.ConversionError("Could not request user " + profileName + " from backend systems", exception); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\OldUsersConverter$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */