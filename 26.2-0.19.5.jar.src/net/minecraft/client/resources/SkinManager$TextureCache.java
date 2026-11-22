/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.mojang.authlib.minecraft.MinecraftProfileTexture;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.core.ClientAsset;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class TextureCache {
/*     */   private final Path root;
/*     */   
/*     */   private final MinecraftProfileTexture.Type type;
/*     */   
/* 138 */   private final Map<String, CompletableFuture<ClientAsset.Texture>> textures = (Map<String, CompletableFuture<ClientAsset.Texture>>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   private TextureCache(Path root, MinecraftProfileTexture.Type type) {
/* 141 */     this.root = root;
/* 142 */     this.type = type;
/*     */   }
/*     */   
/*     */   public CompletableFuture<ClientAsset.Texture> getOrLoad(MinecraftProfileTexture texture) {
/* 146 */     String hash = texture.getHash();
/* 147 */     CompletableFuture<ClientAsset.Texture> future = this.textures.get(hash);
/* 148 */     if (future == null) {
/* 149 */       future = registerTexture(texture);
/* 150 */       this.textures.put(hash, future);
/*     */     } 
/* 152 */     return future;
/*     */   }
/*     */   
/*     */   private CompletableFuture<ClientAsset.Texture> registerTexture(MinecraftProfileTexture textureInfo) {
/* 156 */     String hash = Hashing.sha1().hashUnencodedChars(textureInfo.getHash()).toString();
/* 157 */     Identifier textureId = getTextureLocation(hash);
/* 158 */     Path file = this.root.resolve((hash.length() > 2) ? hash.substring(0, 2) : "xx").resolve(hash);
/* 159 */     return SkinManager.this.skinTextureDownloader
/* 160 */       .downloadAndRegisterSkin(textureId, file, textureInfo.getUrl(), (this.type == MinecraftProfileTexture.Type.SKIN));
/*     */   }
/*     */   
/*     */   private Identifier getTextureLocation(String textureHash) {
/* 164 */     switch (SkinManager.null.$SwitchMap$com$mojang$authlib$minecraft$MinecraftProfileTexture$Type[this.type.ordinal()]) {
/*     */       default:
/* 164 */         throw new MatchException(null, null);
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */         break;
/*     */     } 
/* 164 */     String root = 
/*     */       
/* 167 */       "elytra";
/* 169 */     return Identifier.withDefaultNamespace(root + "/" + root);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\SkinManager$TextureCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */