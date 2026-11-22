/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.loader;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.resources.FileToIdConverter;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ResourceManagerTemplateSource extends TemplateSource {
/* 17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private ResourceManager resourceManager;
/*    */   
/*    */   private final FileToIdConverter fileToIdConverter;
/*    */   
/*    */   public ResourceManagerTemplateSource(DataFixer fixerUpper, HolderGetter<Block> blockLookup, ResourceManager resourceManager, FileToIdConverter fileToIdConverter) {
/* 23 */     super(fixerUpper, blockLookup);
/* 24 */     this.resourceManager = resourceManager;
/* 25 */     this.fileToIdConverter = fileToIdConverter;
/*    */   }
/*    */   
/*    */   public void setResourceManager(ResourceManager resourceManager) {
/* 29 */     this.resourceManager = resourceManager;
/*    */   }
/*    */   
/*    */   public Optional<StructureTemplate> load(Identifier id) {
/* 34 */     Identifier identifier = this.fileToIdConverter.idToFile(id);
/* 35 */     return load(() -> this.resourceManager.open(identifier), false, e -> LOGGER.error("Couldn't load structure {}", id, e));
/*    */   }
/*    */   
/*    */   public Stream<Identifier> list() {
/* 40 */     Objects.requireNonNull(this.fileToIdConverter);
/* 40 */     return this.fileToIdConverter.listMatchingResources(this.resourceManager).keySet().stream().map(this.fileToIdConverter::fileToId);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\loader\ResourceManagerTemplateSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */