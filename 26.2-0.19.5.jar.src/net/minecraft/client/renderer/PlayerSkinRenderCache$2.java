/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ 
/*    */ class null extends CacheLoader<ResolvableProfile, PlayerSkinRenderCache.RenderInfo> {
/*    */   public PlayerSkinRenderCache.RenderInfo load(ResolvableProfile profile) {
/* 50 */     GameProfile temporaryProfile = profile.partialProfile();
/* 51 */     return new PlayerSkinRenderCache.RenderInfo(PlayerSkinRenderCache.this, temporaryProfile, DefaultPlayerSkin.get(temporaryProfile), profile.skinPatch());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PlayerSkinRenderCache$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */