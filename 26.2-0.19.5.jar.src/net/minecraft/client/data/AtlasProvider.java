/*     */ package net.minecraft.client.data;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.data.models.ItemModelGenerators;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.SpriteMapper;
/*     */ import net.minecraft.client.renderer.blockentity.BellRenderer;
/*     */ import net.minecraft.client.renderer.blockentity.ConduitRenderer;
/*     */ import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteSource;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteSources;
/*     */ import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
/*     */ import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
/*     */ import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
/*     */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPatterns;
/*     */ 
/*     */ public class AtlasProvider implements DataProvider {
/*  35 */   private static final Identifier TRIM_PALETTE_KEY = Identifier.withDefaultNamespace("trims/color_palettes/trim_palette");
/*     */   
/*     */   private static final Map<String, Identifier> TRIM_PALETTE_VALUES;
/*     */   
/*     */   static {
/*  38 */     TRIM_PALETTE_VALUES = extractAllMaterialAssets().collect(Collectors.toMap(MaterialAssetGroup.AssetInfo::suffix, asset -> Identifier.withDefaultNamespace("trims/color_palettes/" + asset.suffix())));
/*     */   }
/*     */   
/*  43 */   private static final List<ResourceKey<TrimPattern>> VANILLA_PATTERNS = List.of((ResourceKey<TrimPattern>[])new ResourceKey[] { 
/*  43 */         TrimPatterns.SENTRY, TrimPatterns.DUNE, TrimPatterns.COAST, TrimPatterns.WILD, TrimPatterns.WARD, TrimPatterns.EYE, TrimPatterns.VEX, TrimPatterns.TIDE, TrimPatterns.SNOUT, TrimPatterns.RIB, 
/*  43 */         TrimPatterns.SPIRE, TrimPatterns.WAYFINDER, TrimPatterns.SHAPER, TrimPatterns.SILENCE, TrimPatterns.RAISER, TrimPatterns.HOST, TrimPatterns.FLOW, TrimPatterns.BOLT });
/*     */   
/*  64 */   private static final List<EquipmentClientInfo.LayerType> HUMANOID_LAYERS = List.of(EquipmentClientInfo.LayerType.HUMANOID, EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS);
/*     */   
/*     */   private final PackOutput.PathProvider pathProvider;
/*     */   
/*     */   public AtlasProvider(PackOutput output) {
/*  72 */     this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "atlases");
/*     */   }
/*     */   
/*     */   private static List<Identifier> patternTextures() {
/*  76 */     List<Identifier> result = new ArrayList<>(VANILLA_PATTERNS.size() * HUMANOID_LAYERS.size());
/*  77 */     for (ResourceKey<TrimPattern> vanillaPattern : VANILLA_PATTERNS) {
/*  78 */       Identifier assetId = TrimPatterns.defaultAssetId(vanillaPattern);
/*  79 */       for (EquipmentClientInfo.LayerType humanoidLayer : HUMANOID_LAYERS)
/*  80 */         result.add(assetId.withPath(patternPath -> humanoidLayer.trimAssetPrefix() + "/" + humanoidLayer.trimAssetPrefix())); 
/*     */     } 
/*  83 */     return result;
/*     */   }
/*     */   
/*     */   private static SpriteSource forMaterial(SpriteId sprite) {
/*  88 */     return (SpriteSource)new SingleFile(sprite.texture());
/*     */   }
/*     */   
/*     */   private static SpriteSource forMapper(SpriteMapper mapper) {
/*  93 */     return (SpriteSource)new DirectoryLister(mapper.prefix(), mapper.prefix() + "/");
/*     */   }
/*     */   
/*     */   private static List<SpriteSource> simpleMapper(SpriteMapper mapper) {
/*  97 */     return List.of(
/*  98 */         forMapper(mapper));
/*     */   }
/*     */   
/*     */   private static List<SpriteSource> noPrefixMapper(String directory) {
/* 103 */     return (List)List.of(new DirectoryLister(directory, ""));
/*     */   }
/*     */   
/*     */   private static Stream<MaterialAssetGroup.AssetInfo> extractAllMaterialAssets() {
/* 109 */     return ItemModelGenerators.TRIM_MATERIAL_MODELS
/* 110 */       .stream()
/* 111 */       .map(ItemModelGenerators.TrimMaterialData::assets)
/* 112 */       .flatMap(asset -> Stream.concat(Stream.of(asset.base()), asset.overrides().values().stream()))
/* 113 */       .sorted(Comparator.comparing(MaterialAssetGroup.AssetInfo::suffix));
/*     */   }
/*     */   
/*     */   private static List<SpriteSource> armorTrims() {
/* 117 */     return (List)List.of(new PalettedPermutations(
/*     */           
/* 119 */           patternTextures(), TRIM_PALETTE_KEY, TRIM_PALETTE_VALUES));
/*     */   }
/*     */   
/*     */   private static List<SpriteSource> blocksList() {
/* 127 */     return List.of(
/* 128 */         forMapper(Sheets.BLOCKS_MAPPER), 
/* 129 */         forMapper(ConduitRenderer.MAPPER), 
/* 130 */         forMaterial(BellRenderer.BELL_TEXTURE), 
/* 131 */         forMaterial(EnchantTableRenderer.BOOK_TEXTURE));
/*     */   }
/*     */   
/*     */   private static List<SpriteSource> itemsList() {
/* 136 */     return (List)List.of(
/* 137 */         forMapper(Sheets.ITEMS_MAPPER), new PalettedPermutations(
/*     */           
/* 140 */           List.of(ItemModelGenerators.TRIM_PREFIX_HELMET, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, ItemModelGenerators.TRIM_PREFIX_BOOTS), TRIM_PALETTE_KEY, TRIM_PALETTE_VALUES));
/*     */   }
/*     */   
/*     */   private static List<SpriteSource> bannerPatterns() {
/* 153 */     return List.of(
/* 154 */         forMapper(Sheets.BANNER_MAPPER));
/*     */   }
/*     */   
/*     */   private static List<SpriteSource> shieldPatterns() {
/* 159 */     return List.of(
/* 160 */         forMapper(Sheets.SHIELD_MAPPER));
/*     */   }
/*     */   
/*     */   private static List<SpriteSource> guiSprites() {
/* 165 */     return (List)List.of(new DirectoryLister("gui/sprites", ""), new DirectoryLister("mob_effect", "mob_effect/"));
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> run(CachedOutput cache) {
/* 173 */     return CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] { 
/* 174 */           storeAtlas(cache, AtlasIds.ARMOR_TRIMS, armorTrims()), 
/* 175 */           storeAtlas(cache, AtlasIds.BANNER_PATTERNS, bannerPatterns()), 
/* 176 */           storeAtlas(cache, AtlasIds.BLOCKS, blocksList()), 
/* 177 */           storeAtlas(cache, AtlasIds.ITEMS, itemsList()), 
/* 178 */           storeAtlas(cache, AtlasIds.CHESTS, simpleMapper(Sheets.CHEST_MAPPER)), 
/* 179 */           storeAtlas(cache, AtlasIds.DECORATED_POT, simpleMapper(Sheets.DECORATED_POT_MAPPER)), 
/* 180 */           storeAtlas(cache, AtlasIds.GUI, guiSprites()), 
/* 181 */           storeAtlas(cache, AtlasIds.MAP_DECORATIONS, noPrefixMapper("map/decorations")), 
/* 182 */           storeAtlas(cache, AtlasIds.PAINTINGS, noPrefixMapper("painting")), 
/* 183 */           storeAtlas(cache, AtlasIds.PARTICLES, noPrefixMapper("particle")), 
/* 184 */           storeAtlas(cache, AtlasIds.SHIELD_PATTERNS, shieldPatterns()), 
/* 185 */           storeAtlas(cache, AtlasIds.SHULKER_BOXES, simpleMapper(Sheets.SHULKER_MAPPER)), 
/* 186 */           storeAtlas(cache, AtlasIds.CELESTIALS, noPrefixMapper("environment/celestial")) });
/*     */   }
/*     */   
/*     */   private CompletableFuture<?> storeAtlas(CachedOutput cache, Identifier atlasId, List<SpriteSource> contents) {
/* 191 */     return DataProvider.saveStable(cache, SpriteSources.FILE_CODEC, contents, this.pathProvider.json(atlasId));
/*     */   }
/*     */   
/*     */   public String getName() {
/* 196 */     return "Atlas Definitions";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\AtlasProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */