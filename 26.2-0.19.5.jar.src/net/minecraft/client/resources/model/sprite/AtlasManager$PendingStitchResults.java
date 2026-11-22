/*     */ package net.minecraft.client.resources.model.sprite;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.client.renderer.texture.SpriteLoader;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class PendingStitchResults {
/*     */   private final List<AtlasManager.PendingStitch> pendingStitches;
/*     */   
/*     */   private final Map<Identifier, CompletableFuture<SpriteLoader.Preparations>> stitchFuturesById;
/*     */   
/*     */   private final CompletableFuture<?> allReadyToUpload;
/*     */   
/*     */   private PendingStitchResults(List<AtlasManager.PendingStitch> pendingStitches, Map<Identifier, CompletableFuture<SpriteLoader.Preparations>> stitchFuturesById, CompletableFuture<?> allReadyToUpload) {
/* 203 */     this.pendingStitches = pendingStitches;
/* 204 */     this.stitchFuturesById = stitchFuturesById;
/* 205 */     this.allReadyToUpload = allReadyToUpload;
/*     */   }
/*     */   
/*     */   public Map<SpriteId, TextureAtlasSprite> joinAndUpload() {
/* 209 */     Map<SpriteId, TextureAtlasSprite> result = new HashMap<>();
/* 210 */     this.pendingStitches.forEach(pendingStitch -> pendingStitch.joinAndUpload(result));
/* 211 */     return result;
/*     */   }
/*     */   
/*     */   public CompletableFuture<SpriteLoader.Preparations> get(Identifier atlasId) {
/* 215 */     return Objects.<CompletableFuture<SpriteLoader.Preparations>>requireNonNull(this.stitchFuturesById.get(atlasId));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\sprite\AtlasManager$PendingStitchResults.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */