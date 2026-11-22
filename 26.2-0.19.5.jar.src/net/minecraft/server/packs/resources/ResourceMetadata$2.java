/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ 
/*    */ class null implements ResourceMetadata {
/*    */   public <T> Optional<T> getSection(MetadataSectionType<T> serializer) {
/* 37 */     String name = serializer.name();
/* 38 */     JsonElement rawSection = metadata.get(name);
/* 39 */     if (rawSection != null) {
/* 40 */       T section = (T)serializer.codec().parse((DynamicOps)JsonOps.INSTANCE, rawSection).getOrThrow(com.google.gson.JsonParseException::new);
/* 41 */       return Optional.of(section);
/*    */     } 
/* 43 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\ResourceMetadata$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */