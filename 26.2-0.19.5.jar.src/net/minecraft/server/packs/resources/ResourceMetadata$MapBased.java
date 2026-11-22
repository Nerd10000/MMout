/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ 
/*    */ public class MapBased implements ResourceMetadata {
/*    */   private final Map<MetadataSectionType<?>, ?> values;
/*    */   
/*    */   private MapBased(Map<MetadataSectionType<?>, ?> values) {
/* 72 */     this.values = values;
/*    */   }
/*    */   
/*    */   public <T> Optional<T> getSection(MetadataSectionType<T> serializer) {
/* 78 */     return Optional.ofNullable((T)this.values.get(serializer));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\ResourceMetadata$MapBased.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */