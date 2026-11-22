/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import net.minecraft.world.entity.player.PlayerSkin;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ 
/*    */ class null extends CacheLoader<ResolvableProfile, CompletableFuture<Optional<PlayerSkinRenderCache.RenderInfo>>> {
/*    */   public CompletableFuture<Optional<PlayerSkinRenderCache.RenderInfo>> load(ResolvableProfile profile) {
/* 34 */     return profile
/* 35 */       .resolveProfile(PlayerSkinRenderCache.this.profileResolver)
/* 36 */       .thenCompose(resolvedProfile -> PlayerSkinRenderCache.this.skinManager.get(resolvedProfile).thenApply(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PlayerSkinRenderCache$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */