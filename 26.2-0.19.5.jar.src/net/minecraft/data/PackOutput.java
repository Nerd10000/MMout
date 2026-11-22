/*    */ package net.minecraft.data;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class PackOutput {
/*    */   private final Path outputFolder;
/*    */   
/*    */   public PackOutput(Path outputFolder) {
/* 14 */     this.outputFolder = outputFolder;
/*    */   }
/*    */   
/*    */   public Path getOutputFolder() {
/* 18 */     return this.outputFolder;
/*    */   }
/*    */   
/*    */   public Path getOutputFolder(Target target) {
/* 22 */     return getOutputFolder().resolve(target.directory);
/*    */   }
/*    */   
/*    */   public enum Target {
/* 26 */     DATA_PACK("data"),
/* 27 */     RESOURCE_PACK("assets"),
/* 28 */     REPORTS("reports");
/*    */     
/*    */     private final String directory;
/*    */     
/*    */     Target(String directory) {
/* 34 */       this.directory = directory;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class PathProvider {
/*    */     private final Path root;
/*    */     
/*    */     private final String kind;
/*    */     
/*    */     private PathProvider(PackOutput output, PackOutput.Target target, String kind) {
/* 43 */       this.root = output.getOutputFolder(target);
/* 44 */       this.kind = kind;
/*    */     }
/*    */     
/*    */     public Path file(Identifier element, String extension) {
/* 48 */       return element.withPath(path -> this.kind + "/" + this.kind + "." + path).resolveAgainst(this.root);
/*    */     }
/*    */     
/*    */     public Path json(Identifier element) {
/* 52 */       return element.withPath(path -> this.kind + "/" + this.kind + ".json").resolveAgainst(this.root);
/*    */     }
/*    */     
/*    */     public Path json(ResourceKey<?> element) {
/* 56 */       return json(element.identifier());
/*    */     }
/*    */   }
/*    */   
/*    */   public PathProvider createPathProvider(Target target, String kind) {
/* 61 */     return new PathProvider(this, target, kind);
/*    */   }
/*    */   
/*    */   public PathProvider createRegistryElementsPathProvider(ResourceKey<? extends Registry<?>> registryKey) {
/* 65 */     return createPathProvider(Target.DATA_PACK, Registries.elementsDirPath(registryKey));
/*    */   }
/*    */   
/*    */   public PathProvider createRegistryTagsPathProvider(ResourceKey<? extends Registry<?>> registryKey) {
/* 69 */     return createPathProvider(Target.DATA_PACK, Registries.tagsDirPath(registryKey));
/*    */   }
/*    */   
/*    */   public PathProvider createRegistryComponentPathProvider(ResourceKey<? extends Registry<?>> registryKey) {
/* 74 */     return createPathProvider(Target.REPORTS, Registries.componentsDirPath(registryKey));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\PackOutput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */