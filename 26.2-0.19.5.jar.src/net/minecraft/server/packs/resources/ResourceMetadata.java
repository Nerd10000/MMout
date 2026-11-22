/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public interface ResourceMetadata {
/* 22 */   public static final ResourceMetadata EMPTY = new ResourceMetadata() {
/*    */       public <T> Optional<T> getSection(MetadataSectionType<T> serializer) {
/* 25 */         return Optional.empty();
/*    */       }
/*    */     };
/*    */   
/*    */   public static final IoSupplier<ResourceMetadata> EMPTY_SUPPLIER = () -> EMPTY;
/*    */   
/*    */   static ResourceMetadata fromJsonStream(InputStream inputStream) throws IOException {
/* 31 */     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
/*    */     try {
/* 32 */       final JsonObject metadata = GsonHelper.parse(reader);
/* 34 */       ResourceMetadata resourceMetadata = new ResourceMetadata() {
/*    */           public <T> Optional<T> getSection(MetadataSectionType<T> serializer) {
/* 37 */             String name = serializer.name();
/* 38 */             JsonElement rawSection = metadata.get(name);
/* 39 */             if (rawSection != null) {
/* 40 */               T section = (T)serializer.codec().parse((DynamicOps)JsonOps.INSTANCE, rawSection).getOrThrow(com.google.gson.JsonParseException::new);
/* 41 */               return Optional.of(section);
/*    */             } 
/* 43 */             return Optional.empty();
/*    */           }
/*    */         };
/* 47 */       reader.close();
/*    */       return resourceMetadata;
/*    */     } catch (Throwable throwable) {
/*    */       try {
/*    */         reader.close();
/*    */       } catch (Throwable throwable1) {
/*    */         throwable.addSuppressed(throwable1);
/*    */       } 
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */   
/*    */   default <T> Optional<MetadataSectionType.WithValue<T>> getTypedSection(MetadataSectionType<T> type) {
/* 53 */     Objects.requireNonNull(type);
/* 53 */     return getSection(type).map(type::withValue);
/*    */   }
/*    */   
/*    */   static <T> ResourceMetadata of(MetadataSectionType<T> k, T v) {
/* 57 */     return new MapBased(Map.of(k, v));
/*    */   }
/*    */   
/*    */   static <T1, T2> ResourceMetadata of(MetadataSectionType<T1> k1, T1 v1, MetadataSectionType<T2> k2, T2 v2) {
/* 61 */     return new MapBased(Map.of(k1, v1, k2, v2));
/*    */   }
/*    */   
/*    */   default List<MetadataSectionType.WithValue<?>> getTypedSections(Collection<MetadataSectionType<?>> types) {
/* 65 */     return (List<MetadataSectionType.WithValue<?>>)types.stream().map(this::getTypedSection).flatMap(Optional::stream).collect(Collectors.toUnmodifiableList());
/*    */   }
/*    */   
/*    */   <T> Optional<T> getSection(MetadataSectionType<T> paramMetadataSectionType);
/*    */   
/*    */   public static class MapBased implements ResourceMetadata {
/*    */     private final Map<MetadataSectionType<?>, ?> values;
/*    */     
/*    */     private MapBased(Map<MetadataSectionType<?>, ?> values) {
/* 72 */       this.values = values;
/*    */     }
/*    */     
/*    */     public <T> Optional<T> getSection(MetadataSectionType<T> serializer) {
/* 78 */       return Optional.ofNullable((T)this.values.get(serializer));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\ResourceMetadata.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */