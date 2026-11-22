/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.loader;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.InputStream;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.resources.FileToIdConverter;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.server.packs.PathPackResources;
/*    */ import net.minecraft.server.packs.resources.IoSupplier;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DirectoryTemplateSource extends TemplateSource {
/* 23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Path sourceDir;
/*    */   
/*    */   private final FileToIdConverter fileToIdConverter;
/*    */   
/*    */   private final boolean loadAsText;
/*    */   
/*    */   public DirectoryTemplateSource(DataFixer fixerUpper, HolderGetter<Block> blockLookup, Path sourceDir, PackType packType, FileToIdConverter fileToIdConverter, boolean loadAsText) {
/* 30 */     this(fixerUpper, blockLookup, sourceDir.resolve(packType.getDirectory()), fileToIdConverter, loadAsText);
/*    */   }
/*    */   
/*    */   public DirectoryTemplateSource(DataFixer fixerUpper, HolderGetter<Block> blockLookup, Path sourceDir, FileToIdConverter fileToIdConverter, boolean loadAsText) {
/* 34 */     super(fixerUpper, blockLookup);
/* 35 */     this.sourceDir = sourceDir;
/* 36 */     this.fileToIdConverter = fileToIdConverter;
/* 37 */     this.loadAsText = loadAsText;
/*    */   }
/*    */   
/*    */   public Optional<StructureTemplate> load(Identifier id) {
/* 42 */     if (!Files.isDirectory(this.sourceDir, new java.nio.file.LinkOption[0]))
/* 43 */       return Optional.empty(); 
/* 46 */     IoSupplier<InputStream> resource = PathPackResources.getResource(this.sourceDir, this.fileToIdConverter.idToFile(id));
/* 47 */     if (resource == null)
/* 48 */       return Optional.empty(); 
/* 51 */     return load(resource, this.loadAsText, e -> LOGGER.error("Couldn't load structure from {}:{}", new Object[] { this.sourceDir, id, e }));
/*    */   }
/*    */   
/*    */   public Stream<Identifier> list() {
/* 56 */     if (!Files.isDirectory(this.sourceDir, new java.nio.file.LinkOption[0]))
/* 57 */       return Stream.empty(); 
/* 60 */     Stream.Builder<Identifier> resultBuilder = Stream.builder();
/* 62 */     Set<String> namespaces = PathPackResources.getNamespaces(this.sourceDir);
/* 63 */     for (String namespace : namespaces) {
/* 64 */       PathPackResources.listResources(this.sourceDir, namespace, this.fileToIdConverter.prefix(), (id, paramIoSupplier) -> {
/*    */             if (this.fileToIdConverter.extensionMatches(id))
/*    */               resultBuilder.accept(this.fileToIdConverter.fileToId(id)); 
/*    */           });
/*    */     } 
/* 71 */     return resultBuilder.build();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\loader\DirectoryTemplateSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */