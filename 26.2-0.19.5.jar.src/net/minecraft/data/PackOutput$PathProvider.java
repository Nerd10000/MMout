/*    */ package net.minecraft.data;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class PathProvider {
/*    */   private final Path root;
/*    */   
/*    */   private final String kind;
/*    */   
/*    */   private PathProvider(PackOutput output, PackOutput.Target target, String kind) {
/* 43 */     this.root = output.getOutputFolder(target);
/* 44 */     this.kind = kind;
/*    */   }
/*    */   
/*    */   public Path file(Identifier element, String extension) {
/* 48 */     return element.withPath(path -> this.kind + "/" + this.kind + "." + path).resolveAgainst(this.root);
/*    */   }
/*    */   
/*    */   public Path json(Identifier element) {
/* 52 */     return element.withPath(path -> this.kind + "/" + this.kind + ".json").resolveAgainst(this.root);
/*    */   }
/*    */   
/*    */   public Path json(ResourceKey<?> element) {
/* 56 */     return json(element.identifier());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\PackOutput$PathProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */