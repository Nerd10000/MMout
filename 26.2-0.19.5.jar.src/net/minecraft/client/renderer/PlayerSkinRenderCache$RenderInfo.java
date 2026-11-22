/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import net.minecraft.client.gui.font.GlyphRenderTypes;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ 
/*     */ public final class RenderInfo {
/*     */   private final GameProfile gameProfile;
/*     */   
/*     */   private final PlayerSkin playerSkin;
/*     */   
/*     */   private RenderType itemRenderType;
/*     */   
/*     */   private GpuTextureView textureView;
/*     */   
/*     */   private GlyphRenderTypes glyphRenderTypes;
/*     */   
/*     */   public RenderInfo(GameProfile gameProfile, PlayerSkin playerSkin, PlayerSkin.Patch patch) {
/* 105 */     this.gameProfile = gameProfile;
/* 106 */     this.playerSkin = playerSkin.with(patch);
/*     */   }
/*     */   
/*     */   public GameProfile gameProfile() {
/* 110 */     return this.gameProfile;
/*     */   }
/*     */   
/*     */   public PlayerSkin playerSkin() {
/* 114 */     return this.playerSkin;
/*     */   }
/*     */   
/*     */   public RenderType renderType() {
/* 118 */     if (this.itemRenderType == null)
/* 119 */       this.itemRenderType = PlayerSkinRenderCache.playerSkinRenderType(this.playerSkin); 
/* 121 */     return this.itemRenderType;
/*     */   }
/*     */   
/*     */   public GpuTextureView textureView() {
/* 125 */     if (this.textureView == null)
/* 126 */       this.textureView = PlayerSkinRenderCache.this.textureManager.getTexture(this.playerSkin.body().texturePath()).getTextureView(); 
/* 128 */     return this.textureView;
/*     */   }
/*     */   
/*     */   public GlyphRenderTypes glyphRenderTypes() {
/* 132 */     if (this.glyphRenderTypes == null)
/* 133 */       this.glyphRenderTypes = GlyphRenderTypes.createForColorTexture(this.playerSkin.body().texturePath()); 
/* 135 */     return this.glyphRenderTypes;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 140 */     if (this != o) {
/* 140 */       if (o instanceof RenderInfo) {
/* 140 */         RenderInfo that = (RenderInfo)o;
/* 140 */         if (this.gameProfile.equals(that.gameProfile) && this.playerSkin.equals(that.playerSkin));
/*     */       } 
/* 140 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 145 */     int result = 1;
/* 146 */     result = 31 * result + this.gameProfile.hashCode();
/* 147 */     result = 31 * result + this.playerSkin.hashCode();
/* 148 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PlayerSkinRenderCache$RenderInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */