/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.loader;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import net.minecraft.IdentifierException;
/*    */ import net.minecraft.resources.FileToIdConverter;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.util.FileUtil;
/*    */ 
/*    */ public class TemplatePathFactory {
/*    */   private final Path sourceDir;
/*    */   
/*    */   public TemplatePathFactory(Path sourceDir, PackType packType) {
/* 16 */     this(sourceDir.resolve(packType.getDirectory()));
/*    */   }
/*    */   
/*    */   public TemplatePathFactory(Path sourceDir) {
/* 20 */     this.sourceDir = sourceDir;
/*    */   }
/*    */   
/*    */   public Path createAndValidatePathToStructure(Identifier id, FileToIdConverter converter) {
/* 24 */     return createAndValidatePathToStructure(converter.idToFile(id));
/*    */   }
/*    */   
/*    */   public Path createAndValidatePathToStructure(Identifier resourceLocation) {
/* 28 */     Path namespacePath = this.sourceDir.resolve(resourceLocation.getNamespace());
/* 29 */     List<String> decomposedPath = (List<String>)FileUtil.decomposePath(resourceLocation.getPath()).getOrThrow(msg -> new IdentifierException("Invalid file path '" + String.valueOf(resourceLocation) + "': " + msg));
/* 30 */     if (!decomposedPath.stream().allMatch(FileUtil::isPathPartPortable))
/* 31 */       throw new IdentifierException("Resource path '" + String.valueOf(resourceLocation) + "' is not portable"); 
/* 33 */     return FileUtil.resolvePath(namespacePath, decomposedPath);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\loader\TemplatePathFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */