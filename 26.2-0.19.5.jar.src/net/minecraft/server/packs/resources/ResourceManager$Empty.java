/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ 
/*    */ public enum Empty implements ResourceManager {
/* 33 */   INSTANCE;
/*    */   
/*    */   public Set<String> getNamespaces() {
/* 37 */     return Set.of();
/*    */   }
/*    */   
/*    */   public Optional<Resource> getResource(Identifier location) {
/* 42 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   public List<Resource> getResourceStack(Identifier location) {
/* 47 */     return List.of();
/*    */   }
/*    */   
/*    */   public Map<Identifier, Resource> listResources(String directory, Predicate<Identifier> filter) {
/* 52 */     return Map.of();
/*    */   }
/*    */   
/*    */   public Map<Identifier, List<Resource>> listResourceStacks(String directory, Predicate<Identifier> filter) {
/* 57 */     return Map.of();
/*    */   }
/*    */   
/*    */   public Stream<PackResources> listPacks() {
/* 62 */     return Stream.of(new PackResources[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\ResourceManager$Empty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */