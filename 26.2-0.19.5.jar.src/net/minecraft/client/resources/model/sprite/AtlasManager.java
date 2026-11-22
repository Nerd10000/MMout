/*     */ package net.minecraft.client.resources.model.sprite;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.SpriteLoader;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.metadata.gui.GuiMetadataSection;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*     */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class AtlasManager implements AutoCloseable, PreparableReloadListener, SpriteGetter {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  31 */   private static final List<AtlasConfig> KNOWN_ATLASES = List.of(new AtlasConfig[] { 
/*  31 */         new AtlasConfig(Sheets.ARMOR_TRIMS_SHEET, AtlasIds.ARMOR_TRIMS, false), new AtlasConfig(Sheets.BANNER_SHEET, AtlasIds.BANNER_PATTERNS, false), new AtlasConfig(TextureAtlas.LOCATION_BLOCKS, AtlasIds.BLOCKS, true), new AtlasConfig(TextureAtlas.LOCATION_ITEMS, AtlasIds.ITEMS, false), new AtlasConfig(Sheets.CHEST_SHEET, AtlasIds.CHESTS, false), new AtlasConfig(Sheets.DECORATED_POT_SHEET, AtlasIds.DECORATED_POT, false), new AtlasConfig(Sheets.GUI_SHEET, AtlasIds.GUI, false, 
/*     */           
/*  38 */           Set.of(GuiMetadataSection.TYPE)), new AtlasConfig(Sheets.MAP_DECORATIONS_SHEET, AtlasIds.MAP_DECORATIONS, false), new AtlasConfig(Sheets.PAINTINGS_SHEET, AtlasIds.PAINTINGS, false), new AtlasConfig(TextureAtlas.LOCATION_PARTICLES, AtlasIds.PARTICLES, false), 
/*  38 */         new AtlasConfig(Sheets.SHIELD_SHEET, AtlasIds.SHIELD_PATTERNS, false), new AtlasConfig(Sheets.SHULKER_SHEET, AtlasIds.SHULKER_BOXES, false), new AtlasConfig(Sheets.CELESTIAL_SHEET, AtlasIds.CELESTIALS, false) });
/*     */   
/*  47 */   public static final PreparableReloadListener.StateKey<PendingStitchResults> PENDING_STITCH = new PreparableReloadListener.StateKey();
/*     */   
/*  49 */   private final Map<Identifier, AtlasEntry> atlasByTexture = new HashMap<>();
/*     */   
/*  50 */   private final Map<Identifier, AtlasEntry> atlasById = new HashMap<>();
/*     */   
/*  51 */   private Map<SpriteId, TextureAtlasSprite> spriteLookup = Map.of();
/*     */   
/*     */   private int maxMipmapLevels;
/*     */   
/*     */   public AtlasManager(TextureManager textureManager, int maxMipmapLevels) {
/*  55 */     for (AtlasConfig info : KNOWN_ATLASES) {
/*  56 */       TextureAtlas atlasTexture = new TextureAtlas(info.textureId);
/*  57 */       textureManager.register(info.textureId, (AbstractTexture)atlasTexture);
/*  58 */       AtlasEntry atlasEntry = new AtlasEntry(atlasTexture, info);
/*  59 */       this.atlasByTexture.put(info.textureId, atlasEntry);
/*  60 */       this.atlasById.put(info.definitionLocation, atlasEntry);
/*     */     } 
/*  62 */     this.maxMipmapLevels = maxMipmapLevels;
/*     */   }
/*     */   
/*     */   public TextureAtlas getAtlasOrThrow(Identifier atlasId) {
/*  66 */     AtlasEntry atlasEntry = this.atlasById.get(atlasId);
/*  67 */     if (atlasEntry == null)
/*  68 */       throw new IllegalArgumentException("Invalid atlas id: " + String.valueOf(atlasId)); 
/*  70 */     return atlasEntry.atlas();
/*     */   }
/*     */   
/*     */   public void forEach(BiConsumer<Identifier, TextureAtlas> output) {
/*  74 */     this.atlasById.forEach((atlasId, entry) -> output.accept(atlasId, entry.atlas));
/*     */   }
/*     */   
/*     */   public void updateMaxMipLevel(int maxMipmapLevels) {
/*  78 */     this.maxMipmapLevels = maxMipmapLevels;
/*     */   }
/*     */   
/*     */   public void close() {
/*  83 */     this.spriteLookup = Map.of();
/*  84 */     this.atlasById.values().forEach(AtlasEntry::close);
/*  85 */     this.atlasById.clear();
/*  86 */     this.atlasByTexture.clear();
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite get(SpriteId sprite) {
/*  91 */     TextureAtlasSprite result = this.spriteLookup.get(sprite);
/*  92 */     if (result != null)
/*  93 */       return result; 
/*  96 */     Identifier atlasTextureId = sprite.atlasLocation();
/*  97 */     AtlasEntry atlasEntry = this.atlasByTexture.get(atlasTextureId);
/*  98 */     if (atlasEntry == null)
/*  99 */       throw new IllegalArgumentException("Invalid atlas texture id: " + String.valueOf(atlasTextureId)); 
/* 101 */     return atlasEntry.atlas().missingSprite();
/*     */   }
/*     */   
/*     */   public void prepareSharedState(PreparableReloadListener.SharedState currentReload) {
/* 106 */     int atlasCount = this.atlasById.size();
/* 107 */     List<PendingStitch> pendingStitches = new ArrayList<>(atlasCount);
/* 108 */     Map<Identifier, CompletableFuture<SpriteLoader.Preparations>> pendingStitchById = new HashMap<>(atlasCount);
/* 109 */     List<CompletableFuture<?>> readyForUploads = new ArrayList<>(atlasCount);
/* 111 */     this.atlasById.forEach((atlasId, atlasEntry) -> {
/*     */           CompletableFuture<SpriteLoader.Preparations> stitchingDone = new CompletableFuture<>();
/*     */           pendingStitchById.put(atlasId, stitchingDone);
/*     */           pendingStitches.add(new PendingStitch(atlasEntry, stitchingDone));
/*     */           readyForUploads.add(stitchingDone.thenCompose(SpriteLoader.Preparations::readyForUpload));
/*     */         });
/* 119 */     CompletableFuture<?> allReadyForUploads = CompletableFuture.allOf((CompletableFuture<?>[])readyForUploads.toArray(x$0 -> new CompletableFuture[x$0]));
/* 121 */     currentReload.set(PENDING_STITCH, new PendingStitchResults(pendingStitches, pendingStitchById, allReadyForUploads));
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
/* 126 */     PendingStitchResults pendingStitches = (PendingStitchResults)currentReload.get(PENDING_STITCH);
/* 127 */     ResourceManager resourceManager = currentReload.resourceManager();
/* 129 */     pendingStitches.pendingStitches.forEach(pending -> pending.entry.scheduleLoad(resourceManager, taskExecutor, this.maxMipmapLevels).whenComplete(()));
/* 139 */     Objects.requireNonNull(preparationBarrier);
/* 139 */     return pendingStitches.allReadyToUpload.thenCompose(preparationBarrier::wait)
/* 140 */       .thenAcceptAsync(unused -> updateSpriteMaps(pendingStitches), reloadExecutor);
/*     */   }
/*     */   
/*     */   private void updateSpriteMaps(PendingStitchResults pendingStitches) {
/* 144 */     this.spriteLookup = pendingStitches.joinAndUpload();
/* 145 */     Map<Identifier, TextureAtlasSprite> globalSpriteLookup = new HashMap<>();
/* 146 */     this.spriteLookup.forEach((id, sprite) -> {
/*     */           if (!id.texture().equals(MissingTextureAtlasSprite.getLocation())) {
/*     */             TextureAtlasSprite previous = globalSpriteLookup.putIfAbsent(id.texture(), sprite);
/*     */             if (previous != null)
/*     */               LOGGER.warn("Duplicate sprite {} from atlas {}, already defined in atlas {}. This will be rejected in a future version", new Object[] { id.texture(), id.atlasLocation(), previous.atlasLocation() }); 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static final class PendingStitch extends Record {
/*     */     private final AtlasManager.AtlasEntry entry;
/*     */     
/*     */     private final CompletableFuture<SpriteLoader.Preparations> preparations;
/*     */     
/*     */     private PendingStitch(AtlasManager.AtlasEntry entry, CompletableFuture<SpriteLoader.Preparations> preparations) {
/* 160 */       this.entry = entry;
/* 160 */       this.preparations = preparations;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$PendingStitch;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$PendingStitch;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$PendingStitch;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$PendingStitch;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$PendingStitch;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$PendingStitch;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public AtlasManager.AtlasEntry entry() {
/* 160 */       return this.entry;
/*     */     }
/*     */     
/*     */     public CompletableFuture<SpriteLoader.Preparations> preparations() {
/* 160 */       return this.preparations;
/*     */     }
/*     */     
/*     */     public void joinAndUpload(Map<SpriteId, TextureAtlasSprite> result) {
/* 165 */       SpriteLoader.Preparations preparations = this.preparations.join();
/* 166 */       this.entry.atlas.upload(preparations);
/* 167 */       preparations.regions().forEach((spriteId, spriteContents) -> result.put(new SpriteId(this.entry.config.textureId, spriteId), spriteContents));
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class AtlasEntry extends Record implements AutoCloseable {
/*     */     private final TextureAtlas atlas;
/*     */     
/*     */     private final AtlasManager.AtlasConfig config;
/*     */     
/*     */     private AtlasEntry(TextureAtlas atlas, AtlasManager.AtlasConfig config) {
/* 171 */       this.atlas = atlas;
/* 171 */       this.config = config;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #171	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #171	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #171	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public TextureAtlas atlas() {
/* 171 */       return this.atlas;
/*     */     }
/*     */     
/*     */     public AtlasManager.AtlasConfig config() {
/* 171 */       return this.config;
/*     */     }
/*     */     
/*     */     public void close() {
/* 177 */       this.atlas.close();
/*     */     }
/*     */     
/*     */     private CompletableFuture<SpriteLoader.Preparations> scheduleLoad(ResourceManager resourceManager, Executor executor, int maxMipmapLevels) {
/* 181 */       return SpriteLoader.create(this.atlas)
/* 182 */         .loadAndStitch(resourceManager, this.config.definitionLocation, this.config.createMipmaps ? maxMipmapLevels : 0, executor, this.config.additionalMetadata);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class AtlasConfig extends Record {
/*     */     private final Identifier textureId;
/*     */     
/*     */     private final Identifier definitionLocation;
/*     */     
/*     */     private final boolean createMipmaps;
/*     */     
/*     */     private final Set<MetadataSectionType<?>> additionalMetadata;
/*     */     
/*     */     public Set<MetadataSectionType<?>> additionalMetadata() {
/* 186 */       return this.additionalMetadata;
/*     */     }
/*     */     
/*     */     public boolean createMipmaps() {
/* 186 */       return this.createMipmaps;
/*     */     }
/*     */     
/*     */     public Identifier definitionLocation() {
/* 186 */       return this.definitionLocation;
/*     */     }
/*     */     
/*     */     public Identifier textureId() {
/* 186 */       return this.textureId;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasConfig;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #186	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasConfig;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasConfig;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #186	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasConfig;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasConfig;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #186	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasConfig;
/*     */     }
/*     */     
/*     */     public AtlasConfig(Identifier textureId, Identifier definitionLocation, boolean createMipmaps, Set<MetadataSectionType<?>> additionalMetadata) {
/* 186 */       this.textureId = textureId;
/* 186 */       this.definitionLocation = definitionLocation;
/* 186 */       this.createMipmaps = createMipmaps;
/* 186 */       this.additionalMetadata = additionalMetadata;
/*     */     }
/*     */     
/*     */     public AtlasConfig(Identifier textureId, Identifier definitionLocation, boolean createMipmaps) {
/* 193 */       this(textureId, definitionLocation, createMipmaps, Set.of());
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PendingStitchResults {
/*     */     private final List<AtlasManager.PendingStitch> pendingStitches;
/*     */     
/*     */     private final Map<Identifier, CompletableFuture<SpriteLoader.Preparations>> stitchFuturesById;
/*     */     
/*     */     private final CompletableFuture<?> allReadyToUpload;
/*     */     
/*     */     private PendingStitchResults(List<AtlasManager.PendingStitch> pendingStitches, Map<Identifier, CompletableFuture<SpriteLoader.Preparations>> stitchFuturesById, CompletableFuture<?> allReadyToUpload) {
/* 203 */       this.pendingStitches = pendingStitches;
/* 204 */       this.stitchFuturesById = stitchFuturesById;
/* 205 */       this.allReadyToUpload = allReadyToUpload;
/*     */     }
/*     */     
/*     */     public Map<SpriteId, TextureAtlasSprite> joinAndUpload() {
/* 209 */       Map<SpriteId, TextureAtlasSprite> result = new HashMap<>();
/* 210 */       this.pendingStitches.forEach(pendingStitch -> pendingStitch.joinAndUpload(result));
/* 211 */       return result;
/*     */     }
/*     */     
/*     */     public CompletableFuture<SpriteLoader.Preparations> get(Identifier atlasId) {
/* 215 */       return Objects.<CompletableFuture<SpriteLoader.Preparations>>requireNonNull(this.stitchFuturesById.get(atlasId));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\sprite\AtlasManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */