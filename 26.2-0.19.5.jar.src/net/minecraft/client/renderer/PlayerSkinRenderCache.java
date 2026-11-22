/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.time.Duration;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.font.GlyphRenderTypes;
/*     */ import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*     */ import net.minecraft.client.resources.SkinManager;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ 
/*     */ public class PlayerSkinRenderCache {
/*  25 */   public static final RenderType DEFAULT_PLAYER_SKIN_RENDER_TYPE = playerSkinRenderType(DefaultPlayerSkin.getDefaultSkin());
/*     */   
/*  26 */   public static final Duration CACHE_DURATION = Duration.ofMinutes(5L);
/*     */   
/*  29 */   private final LoadingCache<ResolvableProfile, CompletableFuture<Optional<RenderInfo>>> renderInfoCache = CacheBuilder.newBuilder()
/*  30 */     .expireAfterAccess(CACHE_DURATION)
/*  31 */     .build(new CacheLoader<ResolvableProfile, CompletableFuture<Optional<RenderInfo>>>() {
/*     */         public CompletableFuture<Optional<PlayerSkinRenderCache.RenderInfo>> load(ResolvableProfile profile) {
/*  34 */           return profile
/*  35 */             .resolveProfile(PlayerSkinRenderCache.this.profileResolver)
/*  36 */             .thenCompose(resolvedProfile -> PlayerSkinRenderCache.this.skinManager.get(resolvedProfile).thenApply(()));
/*     */         }
/*     */       });
/*     */   
/*  45 */   private final LoadingCache<ResolvableProfile, RenderInfo> defaultSkinCache = CacheBuilder.newBuilder()
/*  46 */     .expireAfterAccess(CACHE_DURATION)
/*  47 */     .build(new CacheLoader<ResolvableProfile, RenderInfo>() {
/*     */         public PlayerSkinRenderCache.RenderInfo load(ResolvableProfile profile) {
/*  50 */           GameProfile temporaryProfile = profile.partialProfile();
/*  51 */           return new PlayerSkinRenderCache.RenderInfo(temporaryProfile, DefaultPlayerSkin.get(temporaryProfile), profile.skinPatch());
/*     */         }
/*     */       });
/*     */   
/*     */   private final TextureManager textureManager;
/*     */   
/*     */   private final SkinManager skinManager;
/*     */   
/*     */   private final ProfileResolver profileResolver;
/*     */   
/*     */   public PlayerSkinRenderCache(TextureManager textureManager, SkinManager skinManager, ProfileResolver profileResolver) {
/*  60 */     this.textureManager = textureManager;
/*  61 */     this.skinManager = skinManager;
/*  62 */     this.profileResolver = profileResolver;
/*     */   }
/*     */   
/*     */   public RenderInfo getOrDefault(ResolvableProfile profile) {
/*  67 */     RenderInfo result = ((Optional<RenderInfo>)lookup(profile).getNow(Optional.empty())).orElse(null);
/*  68 */     if (result != null)
/*  69 */       return result; 
/*  72 */     return (RenderInfo)this.defaultSkinCache.getUnchecked(profile);
/*     */   }
/*     */   
/*     */   public Supplier<RenderInfo> createLookup(ResolvableProfile profile) {
/*  76 */     RenderInfo defaultForProfile = (RenderInfo)this.defaultSkinCache.getUnchecked(profile);
/*  77 */     CompletableFuture<Optional<RenderInfo>> future = (CompletableFuture<Optional<RenderInfo>>)this.renderInfoCache.getUnchecked(profile);
/*  78 */     Optional<RenderInfo> currentValue = future.getNow(null);
/*  79 */     if (currentValue != null) {
/*  80 */       RenderInfo finalValue = currentValue.orElse(defaultForProfile);
/*  81 */       return () -> finalValue;
/*     */     } 
/*  84 */     return () -> (RenderInfo)((Optional<RenderInfo>)future.getNow(Optional.empty())).orElse(defaultForProfile);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Optional<RenderInfo>> lookup(ResolvableProfile profile) {
/*  89 */     return (CompletableFuture<Optional<RenderInfo>>)this.renderInfoCache.getUnchecked(profile);
/*     */   }
/*     */   
/*     */   private static RenderType playerSkinRenderType(PlayerSkin playerSkin) {
/*  93 */     return SkullBlockRenderer.getPlayerSkinRenderType(playerSkin.body().texturePath());
/*     */   }
/*     */   
/*     */   public final class RenderInfo {
/*     */     private final GameProfile gameProfile;
/*     */     
/*     */     private final PlayerSkin playerSkin;
/*     */     
/*     */     private RenderType itemRenderType;
/*     */     
/*     */     private GpuTextureView textureView;
/*     */     
/*     */     private GlyphRenderTypes glyphRenderTypes;
/*     */     
/*     */     public RenderInfo(GameProfile gameProfile, PlayerSkin playerSkin, PlayerSkin.Patch patch) {
/* 105 */       this.gameProfile = gameProfile;
/* 106 */       this.playerSkin = playerSkin.with(patch);
/*     */     }
/*     */     
/*     */     public GameProfile gameProfile() {
/* 110 */       return this.gameProfile;
/*     */     }
/*     */     
/*     */     public PlayerSkin playerSkin() {
/* 114 */       return this.playerSkin;
/*     */     }
/*     */     
/*     */     public RenderType renderType() {
/* 118 */       if (this.itemRenderType == null)
/* 119 */         this.itemRenderType = PlayerSkinRenderCache.playerSkinRenderType(this.playerSkin); 
/* 121 */       return this.itemRenderType;
/*     */     }
/*     */     
/*     */     public GpuTextureView textureView() {
/* 125 */       if (this.textureView == null)
/* 126 */         this.textureView = PlayerSkinRenderCache.this.textureManager.getTexture(this.playerSkin.body().texturePath()).getTextureView(); 
/* 128 */       return this.textureView;
/*     */     }
/*     */     
/*     */     public GlyphRenderTypes glyphRenderTypes() {
/* 132 */       if (this.glyphRenderTypes == null)
/* 133 */         this.glyphRenderTypes = GlyphRenderTypes.createForColorTexture(this.playerSkin.body().texturePath()); 
/* 135 */       return this.glyphRenderTypes;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 140 */       if (this != o) {
/* 140 */         if (o instanceof RenderInfo) {
/* 140 */           RenderInfo that = (RenderInfo)o;
/* 140 */           if (this.gameProfile.equals(that.gameProfile) && this.playerSkin.equals(that.playerSkin));
/*     */         } 
/* 140 */         return false;
/*     */       } 
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 145 */       int result = 1;
/* 146 */       result = 31 * result + this.gameProfile.hashCode();
/* 147 */       result = 31 * result + this.playerSkin.hashCode();
/* 148 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PlayerSkinRenderCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */