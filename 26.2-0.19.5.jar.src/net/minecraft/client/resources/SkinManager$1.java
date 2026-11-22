/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.mojang.authlib.SignatureState;
/*    */ import com.mojang.authlib.minecraft.MinecraftProfileTextures;
/*    */ import com.mojang.authlib.properties.Property;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.server.Services;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.player.PlayerSkin;
/*    */ 
/*    */ class null extends CacheLoader<SkinManager.CacheKey, CompletableFuture<Optional<PlayerSkin>>> {
/*    */   public CompletableFuture<Optional<PlayerSkin>> load(SkinManager.CacheKey key) {
/* 55 */     return CompletableFuture.supplyAsync(() -> {
/*    */           Property packedTextures = key.packedTextures();
/*    */           if (packedTextures == null)
/*    */             return MinecraftProfileTextures.EMPTY; 
/*    */           MinecraftProfileTextures textures = services.sessionService().unpackTextures(packedTextures);
/*    */           if (textures.signatureState() == SignatureState.INVALID)
/*    */             SkinManager.LOGGER.warn("Profile contained invalid signature for textures property (profile id: {})", key.profileId()); 
/*    */           return textures;
/* 65 */         }Util.backgroundExecutor().forName("unpackSkinTextures"))
/* 66 */       .thenComposeAsync(textures -> SkinManager.this.registerTextures(key.profileId(), textures), mainThreadExecutor)
/* 67 */       .handle((playerSkin, throwable) -> {
/*    */           if (throwable != null)
/*    */             SkinManager.LOGGER.warn("Failed to load texture for profile {}", key.profileId, throwable); 
/*    */           return Optional.ofNullable(playerSkin);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\SkinManager$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */