/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ public final class FileToIdConverter extends Record {
/*    */   private final String prefix;
/*    */   
/*    */   private final String extension;
/*    */   
/*    */   public FileToIdConverter(String prefix, String extension) {
/* 11 */     this.prefix = prefix;
/* 11 */     this.extension = extension;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/resources/FileToIdConverter;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/resources/FileToIdConverter;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/FileToIdConverter;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/resources/FileToIdConverter;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/resources/FileToIdConverter;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/resources/FileToIdConverter;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String prefix() {
/* 11 */     return this.prefix;
/*    */   }
/*    */   
/*    */   public String extension() {
/* 11 */     return this.extension;
/*    */   }
/*    */   
/*    */   public static FileToIdConverter json(String prefix) {
/* 13 */     return new FileToIdConverter(prefix, ".json");
/*    */   }
/*    */   
/*    */   public static FileToIdConverter registry(ResourceKey<? extends Registry<?>> registry) {
/* 17 */     return json(Registries.elementsDirPath(registry));
/*    */   }
/*    */   
/*    */   public Identifier idToFile(Identifier id) {
/* 21 */     return id.withPath(this.prefix + "/" + this.prefix + id.getPath());
/*    */   }
/*    */   
/*    */   public Identifier fileToId(Identifier file) {
/* 25 */     String path = file.getPath();
/* 26 */     return file.withPath(path.substring(this.prefix.length() + 1, path.length() - this.extension.length()));
/*    */   }
/*    */   
/*    */   public boolean extensionMatches(Identifier id) {
/* 30 */     return id.getPath().endsWith(this.extension);
/*    */   }
/*    */   
/*    */   public Map<Identifier, Resource> listMatchingResources(ResourceManager manager) {
/* 34 */     return manager.listResources(this.prefix, this::extensionMatches);
/*    */   }
/*    */   
/*    */   public Map<Identifier, List<Resource>> listMatchingResourceStacks(ResourceManager manager) {
/* 38 */     return manager.listResourceStacks(this.prefix, this::extensionMatches);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\FileToIdConverter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */