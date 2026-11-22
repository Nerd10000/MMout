/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.SignatureState;
/*     */ import com.mojang.authlib.minecraft.MinecraftProfileTexture;
/*     */ import com.mojang.authlib.minecraft.MinecraftProfileTextures;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Duration;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.renderer.texture.SkinTextureDownloader;
/*     */ import net.minecraft.core.ClientAsset;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.Services;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.PlayerModelType;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SkinManager {
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Services services;
/*     */   
/*     */   private final SkinTextureDownloader skinTextureDownloader;
/*     */   
/*     */   private final LoadingCache<CacheKey, CompletableFuture<Optional<PlayerSkin>>> skinCache;
/*     */   
/*     */   private final TextureCache skinTextures;
/*     */   
/*     */   private final TextureCache capeTextures;
/*     */   
/*     */   private final TextureCache elytraTextures;
/*     */   
/*     */   public SkinManager(Path skinsDirectory, final Services services, SkinTextureDownloader skinTextureDownloader, final Executor mainThreadExecutor) {
/*  45 */     this.services = services;
/*  46 */     this.skinTextureDownloader = skinTextureDownloader;
/*  47 */     this.skinTextures = new TextureCache(skinsDirectory, MinecraftProfileTexture.Type.SKIN);
/*  48 */     this.capeTextures = new TextureCache(skinsDirectory, MinecraftProfileTexture.Type.CAPE);
/*  49 */     this.elytraTextures = new TextureCache(skinsDirectory, MinecraftProfileTexture.Type.ELYTRA);
/*  50 */     this
/*     */       
/*  52 */       .skinCache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofSeconds(15L)).build(new CacheLoader<CacheKey, CompletableFuture<Optional<PlayerSkin>>>() {
/*     */           public CompletableFuture<Optional<PlayerSkin>> load(SkinManager.CacheKey key) {
/*  55 */             return CompletableFuture.supplyAsync(() -> {
/*     */                   Property packedTextures = key.packedTextures();
/*     */                   if (packedTextures == null)
/*     */                     return MinecraftProfileTextures.EMPTY; 
/*     */                   MinecraftProfileTextures textures = services.sessionService().unpackTextures(packedTextures);
/*     */                   if (textures.signatureState() == SignatureState.INVALID)
/*     */                     SkinManager.LOGGER.warn("Profile contained invalid signature for textures property (profile id: {})", key.profileId()); 
/*     */                   return textures;
/*  65 */                 }Util.backgroundExecutor().forName("unpackSkinTextures"))
/*  66 */               .thenComposeAsync(textures -> SkinManager.this.registerTextures(key.profileId(), textures), mainThreadExecutor)
/*  67 */               .handle((playerSkin, throwable) -> {
/*     */                   if (throwable != null)
/*     */                     SkinManager.LOGGER.warn("Failed to load texture for profile {}", key.profileId, throwable); 
/*     */                   return Optional.ofNullable(playerSkin);
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public Supplier<PlayerSkin> createLookup(GameProfile profile, boolean requireSecure) {
/*  80 */     CompletableFuture<Optional<PlayerSkin>> future = get(profile);
/*  81 */     PlayerSkin defaultSkin = DefaultPlayerSkin.get(profile);
/*  82 */     if (SharedConstants.DEBUG_DEFAULT_SKIN_OVERRIDE)
/*  83 */       return () -> defaultSkin; 
/*  86 */     Optional<PlayerSkin> currentValue = future.getNow(null);
/*  87 */     if (currentValue != null) {
/*  91 */       PlayerSkin playerSkin = currentValue.filter(skin -> (!requireSecure || skin.secure())).orElse(defaultSkin);
/*  92 */       return () -> playerSkin;
/*     */     } 
/*  95 */     return () -> (PlayerSkin)((Optional<PlayerSkin>)future.getNow(Optional.empty())).filter(()).orElse(defaultSkin);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Optional<PlayerSkin>> get(GameProfile profile) {
/* 101 */     if (SharedConstants.DEBUG_DEFAULT_SKIN_OVERRIDE) {
/* 102 */       PlayerSkin defaultSkin = DefaultPlayerSkin.get(profile);
/* 103 */       return CompletableFuture.completedFuture(Optional.of(defaultSkin));
/*     */     } 
/* 106 */     Property packedTextures = this.services.sessionService().getPackedTextures(profile);
/* 107 */     return (CompletableFuture<Optional<PlayerSkin>>)this.skinCache.getUnchecked(new CacheKey(profile.id(), packedTextures));
/*     */   }
/*     */   
/*     */   private CompletableFuture<PlayerSkin> registerTextures(UUID profileId, MinecraftProfileTextures textures) {
/*     */     CompletableFuture<ClientAsset.Texture> skinTexture;
/*     */     PlayerModelType model;
/* 111 */     MinecraftProfileTexture skinInfo = textures.skin();
/* 114 */     if (skinInfo != null) {
/* 115 */       skinTexture = this.skinTextures.getOrLoad(skinInfo);
/* 116 */       model = PlayerModelType.byLegacyServicesName(skinInfo.getMetadata("model"));
/*     */     } else {
/* 118 */       PlayerSkin defaultSkin = DefaultPlayerSkin.get(profileId);
/* 119 */       skinTexture = CompletableFuture.completedFuture(defaultSkin.body());
/* 120 */       model = defaultSkin.model();
/*     */     } 
/* 123 */     MinecraftProfileTexture capeInfo = textures.cape();
/* 124 */     CompletableFuture<ClientAsset.Texture> capeTexture = (capeInfo != null) ? this.capeTextures.getOrLoad(capeInfo) : CompletableFuture.<ClientAsset.Texture>completedFuture(null);
/* 126 */     MinecraftProfileTexture elytraInfo = textures.elytra();
/* 127 */     CompletableFuture<ClientAsset.Texture> elytraTexture = (elytraInfo != null) ? this.elytraTextures.getOrLoad(elytraInfo) : CompletableFuture.<ClientAsset.Texture>completedFuture(null);
/* 129 */     return CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] { skinTexture, capeTexture, elytraTexture }).thenApply(unused -> new PlayerSkin(skinTexture.join(), capeTexture.join(), elytraTexture.join(), model, (textures.signatureState() == SignatureState.SIGNED)));
/*     */   }
/*     */   
/*     */   private class TextureCache {
/*     */     private final Path root;
/*     */     
/*     */     private final MinecraftProfileTexture.Type type;
/*     */     
/* 138 */     private final Map<String, CompletableFuture<ClientAsset.Texture>> textures = (Map<String, CompletableFuture<ClientAsset.Texture>>)new Object2ObjectOpenHashMap();
/*     */     
/*     */     private TextureCache(Path root, MinecraftProfileTexture.Type type) {
/* 141 */       this.root = root;
/* 142 */       this.type = type;
/*     */     }
/*     */     
/*     */     public CompletableFuture<ClientAsset.Texture> getOrLoad(MinecraftProfileTexture texture) {
/* 146 */       String hash = texture.getHash();
/* 147 */       CompletableFuture<ClientAsset.Texture> future = this.textures.get(hash);
/* 148 */       if (future == null) {
/* 149 */         future = registerTexture(texture);
/* 150 */         this.textures.put(hash, future);
/*     */       } 
/* 152 */       return future;
/*     */     }
/*     */     
/*     */     private CompletableFuture<ClientAsset.Texture> registerTexture(MinecraftProfileTexture textureInfo) {
/* 156 */       String hash = Hashing.sha1().hashUnencodedChars(textureInfo.getHash()).toString();
/* 157 */       Identifier textureId = getTextureLocation(hash);
/* 158 */       Path file = this.root.resolve((hash.length() > 2) ? hash.substring(0, 2) : "xx").resolve(hash);
/* 159 */       return SkinManager.this.skinTextureDownloader
/* 160 */         .downloadAndRegisterSkin(textureId, file, textureInfo.getUrl(), (this.type == MinecraftProfileTexture.Type.SKIN));
/*     */     }
/*     */     
/*     */     private Identifier getTextureLocation(String textureHash) {
/*     */       // Byte code:
/*     */       //   0: getstatic net/minecraft/client/resources/SkinManager$2.$SwitchMap$com$mojang$authlib$minecraft$MinecraftProfileTexture$Type : [I
/*     */       //   3: aload_0
/*     */       //   4: getfield type : Lcom/mojang/authlib/minecraft/MinecraftProfileTexture$Type;
/*     */       //   7: invokevirtual ordinal : ()I
/*     */       //   10: iaload
/*     */       //   11: tableswitch default -> 36, 1 -> 46, 2 -> 51, 3 -> 56
/*     */       //   36: new java/lang/MatchException
/*     */       //   39: dup
/*     */       //   40: aconst_null
/*     */       //   41: aconst_null
/*     */       //   42: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   45: athrow
/*     */       //   46: ldc 'skins'
/*     */       //   48: goto -> 58
/*     */       //   51: ldc 'capes'
/*     */       //   53: goto -> 58
/*     */       //   56: ldc 'elytra'
/*     */       //   58: astore_2
/*     */       //   59: aload_2
/*     */       //   60: aload_1
/*     */       //   61: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */       //   66: invokestatic withDefaultNamespace : (Ljava/lang/String;)Lnet/minecraft/resources/Identifier;
/*     */       //   69: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #164	-> 0
/*     */       //   #165	-> 46
/*     */       //   #166	-> 51
/*     */       //   #167	-> 56
/*     */       //   #164	-> 58
/*     */       //   #169	-> 59
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	70	0	this	Lnet/minecraft/client/resources/SkinManager$TextureCache;
/*     */       //   0	70	1	textureHash	Ljava/lang/String;
/*     */       //   59	11	2	root	Ljava/lang/String;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class CacheKey extends Record {
/*     */     private final UUID profileId;
/*     */     
/*     */     private final Property packedTextures;
/*     */     
/*     */     private CacheKey(UUID profileId, Property packedTextures) {
/* 174 */       this.profileId = profileId;
/* 174 */       this.packedTextures = packedTextures;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/SkinManager$CacheKey;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #174	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/SkinManager$CacheKey;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/SkinManager$CacheKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #174	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/SkinManager$CacheKey;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/SkinManager$CacheKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #174	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/SkinManager$CacheKey;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public UUID profileId() {
/* 174 */       return this.profileId;
/*     */     }
/*     */     
/*     */     public Property packedTextures() {
/* 174 */       return this.packedTextures;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\SkinManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */