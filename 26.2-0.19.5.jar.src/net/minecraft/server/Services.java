/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.authlib.GameProfileRepository;
/*    */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*    */ import com.mojang.authlib.yggdrasil.ServicesKeySet;
/*    */ import com.mojang.authlib.yggdrasil.ServicesKeyType;
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*    */ import java.io.File;
/*    */ import net.minecraft.server.players.CachedUserNameToIdResolver;
/*    */ import net.minecraft.server.players.ProfileResolver;
/*    */ import net.minecraft.server.players.UserNameToIdResolver;
/*    */ import net.minecraft.util.SignatureValidator;
/*    */ 
/*    */ public final class Services extends Record {
/*    */   private final MinecraftSessionService sessionService;
/*    */   
/*    */   private final ServicesKeySet servicesKeySet;
/*    */   
/*    */   private final GameProfileRepository profileRepository;
/*    */   
/*    */   private final UserNameToIdResolver nameToIdCache;
/*    */   
/*    */   private final ProfileResolver profileResolver;
/*    */   
/*    */   private static final String USERID_CACHE_FILE = "usercache.json";
/*    */   
/*    */   public Services(MinecraftSessionService sessionService, ServicesKeySet servicesKeySet, GameProfileRepository profileRepository, UserNameToIdResolver nameToIdCache, ProfileResolver profileResolver) {
/* 16 */     this.sessionService = sessionService;
/* 16 */     this.servicesKeySet = servicesKeySet;
/* 16 */     this.profileRepository = profileRepository;
/* 16 */     this.nameToIdCache = nameToIdCache;
/* 16 */     this.profileResolver = profileResolver;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/Services;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/Services;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/Services;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/Services;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/Services;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/Services;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MinecraftSessionService sessionService() {
/* 16 */     return this.sessionService;
/*    */   }
/*    */   
/*    */   public ServicesKeySet servicesKeySet() {
/* 16 */     return this.servicesKeySet;
/*    */   }
/*    */   
/*    */   public GameProfileRepository profileRepository() {
/* 16 */     return this.profileRepository;
/*    */   }
/*    */   
/*    */   public UserNameToIdResolver nameToIdCache() {
/* 16 */     return this.nameToIdCache;
/*    */   }
/*    */   
/*    */   public ProfileResolver profileResolver() {
/* 16 */     return this.profileResolver;
/*    */   }
/*    */   
/*    */   public static Services create(YggdrasilAuthenticationService serviceAccess, File nameCacheDir) {
/* 26 */     MinecraftSessionService sessionService = serviceAccess.createMinecraftSessionService();
/* 27 */     GameProfileRepository profileRepository = serviceAccess.createProfileRepository();
/* 28 */     CachedUserNameToIdResolver cachedUserNameToIdResolver = new CachedUserNameToIdResolver(profileRepository, new File(nameCacheDir, "usercache.json"));
/* 29 */     ProfileResolver.Cached cached = new ProfileResolver.Cached(sessionService, (UserNameToIdResolver)cachedUserNameToIdResolver);
/* 30 */     return new Services(sessionService, serviceAccess.getServicesKeySet(), profileRepository, (UserNameToIdResolver)cachedUserNameToIdResolver, (ProfileResolver)cached);
/*    */   }
/*    */   
/*    */   public SignatureValidator profileKeySignatureValidator() {
/* 34 */     return SignatureValidator.from(this.servicesKeySet, ServicesKeyType.PROFILE_KEY);
/*    */   }
/*    */   
/*    */   public boolean canValidateProfileKeys() {
/* 38 */     return !this.servicesKeySet.keys(ServicesKeyType.PROFILE_KEY).isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\Services.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */