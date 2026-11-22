/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.PackLocationInfo;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackSelectionConfig;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.VanillaPackResources;
/*     */ import net.minecraft.server.packs.VanillaPackResourcesBuilder;
/*     */ import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
/*     */ import net.minecraft.server.packs.repository.BuiltInPackSource;
/*     */ import net.minecraft.server.packs.repository.KnownPack;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.repository.PackSource;
/*     */ import net.minecraft.server.packs.resources.ResourceMetadata;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ 
/*     */ public class ClientPackSource extends BuiltInPackSource {
/*  30 */   private static final PackMetadataSection VERSION_METADATA_SECTION = new PackMetadataSection(
/*  31 */       (Component)Component.translatable("resourcePack.vanilla.description"), 
/*  32 */       SharedConstants.getCurrentVersion().packVersion(PackType.CLIENT_RESOURCES).minorRange());
/*     */   
/*  35 */   private static final ResourceMetadata BUILT_IN_METADATA = ResourceMetadata.of(PackMetadataSection.CLIENT_TYPE, VERSION_METADATA_SECTION);
/*     */   
/*     */   public static final String HIGH_CONTRAST_PACK = "high_contrast";
/*     */   
/*  39 */   private static final Map<String, Component> SPECIAL_PACK_NAMES = (Map)Map.of("programmer_art", 
/*  40 */       Component.translatable("resourcePack.programmer_art.name"), "high_contrast", 
/*  41 */       Component.translatable("resourcePack.high_contrast.name"));
/*     */   
/*  44 */   private static final PackLocationInfo VANILLA_PACK_INFO = new PackLocationInfo("vanilla", 
/*     */       
/*  46 */       (Component)Component.translatable("resourcePack.vanilla.name"), PackSource.BUILT_IN, 
/*     */       
/*  48 */       Optional.of(CORE_PACK_INFO));
/*     */   
/*  51 */   private static final PackSelectionConfig VANILLA_SELECTION_CONFIG = new PackSelectionConfig(true, Pack.Position.BOTTOM, false);
/*     */   
/*  57 */   private static final PackSelectionConfig BUILT_IN_SELECTION_CONFIG = new PackSelectionConfig(false, Pack.Position.TOP, false);
/*     */   
/*  63 */   private static final Identifier PACKS_DIR = Identifier.withDefaultNamespace("resourcepacks");
/*     */   
/*     */   private final Path externalAssetDir;
/*     */   
/*     */   public ClientPackSource(Path externalAssetSource, DirectoryValidator validator) {
/*  68 */     super(PackType.CLIENT_RESOURCES, createVanillaPackSource(externalAssetSource), PACKS_DIR, validator);
/*  69 */     this.externalAssetDir = findExplodedAssetPacks(externalAssetSource);
/*     */   }
/*     */   
/*     */   private static PackLocationInfo createBuiltInPackLocation(String id, Component title) {
/*  73 */     return new PackLocationInfo(id, title, PackSource.BUILT_IN, 
/*     */         
/*  77 */         Optional.of(KnownPack.vanilla(id)));
/*     */   }
/*     */   
/*     */   private Path findExplodedAssetPacks(Path externalAssetSource) {
/*  82 */     if (SharedConstants.IS_RUNNING_IN_IDE && externalAssetSource.getFileSystem() == FileSystems.getDefault()) {
/*  83 */       Path devAssetDir = externalAssetSource.getParent().resolve("resourcepacks");
/*  84 */       if (Files.isDirectory(devAssetDir, new java.nio.file.LinkOption[0]))
/*  85 */         return devAssetDir; 
/*     */     } 
/*  88 */     return null;
/*     */   }
/*     */   
/*     */   private static VanillaPackResources createVanillaPackSource(Path externalAssetRoot) {
/*  92 */     return (new VanillaPackResourcesBuilder())
/*  93 */       .setMetadata(BUILT_IN_METADATA)
/*  94 */       .exposeNamespace(new String[] { "minecraft", "realms" }).applyDevelopmentConfig()
/*  96 */       .pushJarResources()
/*  97 */       .pushAssetPath(PackType.CLIENT_RESOURCES, externalAssetRoot)
/*  98 */       .build(VANILLA_PACK_INFO);
/*     */   }
/*     */   
/*     */   protected Component getPackTitle(String id) {
/* 103 */     Component title = SPECIAL_PACK_NAMES.get(id);
/* 104 */     return (title != null) ? title : (Component)Component.literal(id);
/*     */   }
/*     */   
/*     */   protected Pack createVanillaPack(PackResources resources) {
/* 109 */     return Pack.readMetaAndCreate(VANILLA_PACK_INFO, fixedResources(resources), PackType.CLIENT_RESOURCES, VANILLA_SELECTION_CONFIG);
/*     */   }
/*     */   
/*     */   protected Pack createBuiltinPack(String id, Pack.ResourcesSupplier resources, Component name) {
/* 114 */     return Pack.readMetaAndCreate(createBuiltInPackLocation(id, name), resources, PackType.CLIENT_RESOURCES, BUILT_IN_SELECTION_CONFIG);
/*     */   }
/*     */   
/*     */   protected void populatePackList(BiConsumer<String, Function<String, Pack>> discoveredPacks) {
/* 119 */     super.populatePackList(discoveredPacks);
/* 121 */     if (this.externalAssetDir != null)
/* 122 */       discoverPacksInPath(this.externalAssetDir, discoveredPacks); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\ClientPackSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */