/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.world.entity.player.ProfileKeyPair;
/*    */ 
/*    */ class null implements ProfileKeyPairManager {
/*    */   public CompletableFuture<Optional<ProfileKeyPair>> prepareKeyPair() {
/* 15 */     return CompletableFuture.completedFuture(Optional.empty());
/*    */   }
/*    */   
/*    */   public boolean shouldRefreshKeyPair() {
/* 20 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ProfileKeyPairManager$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */