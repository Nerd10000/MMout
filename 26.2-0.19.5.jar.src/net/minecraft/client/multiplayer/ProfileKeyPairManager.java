/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import com.mojang.authlib.minecraft.UserApiService;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.client.User;
/*    */ import net.minecraft.world.entity.player.ProfileKeyPair;
/*    */ 
/*    */ public interface ProfileKeyPairManager {
/* 12 */   public static final ProfileKeyPairManager EMPTY_KEY_MANAGER = new ProfileKeyPairManager() {
/*    */       public CompletableFuture<Optional<ProfileKeyPair>> prepareKeyPair() {
/* 15 */         return CompletableFuture.completedFuture(Optional.empty());
/*    */       }
/*    */       
/*    */       public boolean shouldRefreshKeyPair() {
/* 20 */         return false;
/*    */       }
/*    */     };
/*    */   
/*    */   static ProfileKeyPairManager create(UserApiService userApiService, User user, Path gameDirectory) {
/* 25 */     return new AccountProfileKeyPairManager(userApiService, user.getProfileId(), gameDirectory);
/*    */   }
/*    */   
/*    */   CompletableFuture<Optional<ProfileKeyPair>> prepareKeyPair();
/*    */   
/*    */   boolean shouldRefreshKeyPair();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ProfileKeyPairManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */