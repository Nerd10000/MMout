/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.structures.NbtToSnbt;
/*     */ import net.minecraft.gametest.framework.StructureUtils;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.loader.DirectoryTemplateSource;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.loader.ResourceManagerTemplateSource;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.loader.TemplatePathFactory;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.loader.TemplateSource;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class StructureTemplateManager {
/*  40 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String STRUCTURE_FILE_EXTENSION = ".nbt";
/*     */   
/*     */   private static final String STRUCTURE_TEXT_FILE_EXTENSION = ".snbt";
/*     */   
/*     */   public static final String STRUCTURE_DIRECTORY_NAME = "structure";
/*     */   
/*  46 */   public static final FileToIdConverter WORLD_STRUCTURE_LISTER = new FileToIdConverter("structure", ".nbt");
/*     */   
/*  47 */   private static final FileToIdConverter WORLD_TEXT_STRUCTURE_LISTER = new FileToIdConverter("structure", ".snbt");
/*     */   
/*  49 */   private static final FileToIdConverter RESOURCE_STRUCTURE_LISTER = new FileToIdConverter("structure", ".nbt");
/*     */   
/*  50 */   public static final FileToIdConverter RESOURCE_TEXT_STRUCTURE_LISTER = new FileToIdConverter("structure", ".snbt");
/*     */   
/*  52 */   private final Map<Identifier, Optional<StructureTemplate>> structureRepository = new ConcurrentHashMap<>();
/*     */   
/*     */   private final ResourceManagerTemplateSource resourceManagerSource;
/*     */   
/*     */   private final List<TemplateSource> sources;
/*     */   
/*     */   private final TemplatePathFactory worldTemplates;
/*     */   
/*     */   private final TemplatePathFactory testTemplates;
/*     */   
/*     */   public StructureTemplateManager(ResourceManager resourceManager, LevelStorageSource.LevelStorageAccess storage, DataFixer fixerUpper, HolderGetter<Block> blockLookup) {
/*  60 */     this.resourceManagerSource = new ResourceManagerTemplateSource(fixerUpper, blockLookup, resourceManager, RESOURCE_STRUCTURE_LISTER);
/*  62 */     Path generatedDir = storage.getLevelPath(LevelResource.GENERATED_DIR).normalize();
/*  63 */     this.worldTemplates = new TemplatePathFactory(generatedDir);
/*  65 */     if (StructureUtils.testStructuresTargetDir != null) {
/*  66 */       this.testTemplates = new TemplatePathFactory(StructureUtils.testStructuresTargetDir, PackType.SERVER_DATA);
/*     */     } else {
/*  68 */       this.testTemplates = null;
/*     */     } 
/*  71 */     ImmutableList.Builder<TemplateSource> sources = ImmutableList.builder();
/*  72 */     sources.add(new DirectoryTemplateSource(fixerUpper, blockLookup, generatedDir, WORLD_STRUCTURE_LISTER, false));
/*  73 */     if (StructureUtils.testStructuresSourceDir != null)
/*  74 */       sources.add(new DirectoryTemplateSource(fixerUpper, blockLookup, StructureUtils.testStructuresSourceDir, PackType.SERVER_DATA, RESOURCE_TEXT_STRUCTURE_LISTER, true)); 
/*  76 */     sources.add(this.resourceManagerSource);
/*  77 */     this.sources = (List<TemplateSource>)sources.build();
/*     */   }
/*     */   
/*     */   public StructureTemplate getOrCreate(Identifier id) {
/*  81 */     Optional<StructureTemplate> cachedTemplate = get(id);
/*  83 */     if (cachedTemplate.isPresent())
/*  84 */       return cachedTemplate.get(); 
/*  87 */     StructureTemplate template = new StructureTemplate();
/*  88 */     this.structureRepository.put(id, Optional.of(template));
/*  89 */     return template;
/*     */   }
/*     */   
/*     */   public Optional<StructureTemplate> get(Identifier id) {
/*  93 */     return this.structureRepository.computeIfAbsent(id, this::tryLoad);
/*     */   }
/*     */   
/*     */   public Stream<Identifier> listTemplates() {
/*  97 */     return this.sources.stream().flatMap(TemplateSource::list).distinct();
/*     */   }
/*     */   
/*     */   private Optional<StructureTemplate> tryLoad(Identifier id) {
/* 101 */     for (TemplateSource source : this.sources) {
/*     */       try {
/* 103 */         Optional<StructureTemplate> loaded = source.load(id);
/* 104 */         if (loaded.isPresent())
/* 105 */           return loaded; 
/* 107 */       } catch (Exception exception) {}
/*     */     } 
/* 110 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public void onResourceManagerReload(ResourceManager resourceManager) {
/* 114 */     this.resourceManagerSource.setResourceManager(resourceManager);
/* 115 */     this.structureRepository.clear();
/*     */   }
/*     */   
/*     */   public boolean save(Identifier id) {
/*     */     Path file;
/*     */     boolean saveAsText;
/* 119 */     Optional<StructureTemplate> structureTemplate = this.structureRepository.get(id);
/* 120 */     if (structureTemplate.isEmpty())
/* 121 */       return false; 
/* 126 */     if (SharedConstants.DEBUG_SAVE_STRUCTURES_AS_SNBT) {
/* 127 */       file = this.worldTemplates.createAndValidatePathToStructure(id, WORLD_TEXT_STRUCTURE_LISTER);
/* 128 */       saveAsText = true;
/*     */     } else {
/* 130 */       file = this.worldTemplates.createAndValidatePathToStructure(id, WORLD_STRUCTURE_LISTER);
/* 131 */       saveAsText = false;
/*     */     } 
/*     */     try {
/* 135 */       return save(file, structureTemplate.get(), saveAsText);
/* 136 */     } catch (Exception e) {
/* 137 */       LOGGER.warn("Failed to save structure file {} to {}", new Object[] { id, file, e });
/* 138 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean save(Path file, StructureTemplate structureTemplate, boolean asText) throws IOException {
/* 143 */     Path parent = file.getParent();
/* 144 */     if (parent == null)
/* 145 */       return false; 
/* 148 */     FileUtil.createDirectoriesSafe(parent);
/* 150 */     CompoundTag tag = structureTemplate.save(new CompoundTag());
/* 151 */     if (asText) {
/* 152 */       NbtToSnbt.writeSnbt(CachedOutput.NO_CACHE, file, NbtUtils.structureToSnbt(tag));
/*     */     } else {
/* 154 */       OutputStream output = new FileOutputStream(file.toFile());
/*     */       try {
/* 155 */         NbtIo.writeCompressed(tag, output);
/* 156 */         output.close();
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           output.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/*     */     } 
/* 158 */     return true;
/*     */   }
/*     */   
/*     */   public TemplatePathFactory worldTemplates() {
/* 162 */     return this.worldTemplates;
/*     */   }
/*     */   
/*     */   public TemplatePathFactory testTemplates() {
/* 166 */     return this.testTemplates;
/*     */   }
/*     */   
/*     */   public void remove(Identifier id) {
/* 170 */     this.structureRepository.remove(id);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureTemplateManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */