/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ResourceProvider {
/*    */   public static final ResourceProvider EMPTY = location -> Optional.empty();
/*    */   
/*    */   default Resource getResourceOrThrow(Identifier location) throws FileNotFoundException {
/* 21 */     return getResource(location).<Throwable>orElseThrow(() -> new FileNotFoundException(location.toString()));
/*    */   }
/*    */   
/*    */   default InputStream open(Identifier location) throws IOException {
/* 25 */     return getResourceOrThrow(location).open();
/*    */   }
/*    */   
/*    */   default BufferedReader openAsReader(Identifier location) throws IOException {
/* 29 */     return getResourceOrThrow(location).openAsReader();
/*    */   }
/*    */   
/*    */   static ResourceProvider fromMap(Map<Identifier, Resource> map) {
/* 33 */     return location -> Optional.ofNullable((Resource)map.get(location));
/*    */   }
/*    */   
/*    */   Optional<Resource> getResource(Identifier paramIdentifier);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\ResourceProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */