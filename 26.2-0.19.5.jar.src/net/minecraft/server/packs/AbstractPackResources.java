/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.server.packs.resources.IoSupplier;
/*    */ import net.minecraft.server.packs.resources.ResourceMetadata;
/*    */ 
/*    */ public abstract class AbstractPackResources implements PackResources {
/*    */   private final PackLocationInfo location;
/*    */   
/*    */   private ResourceMetadata metadata;
/*    */   
/*    */   protected AbstractPackResources(PackLocationInfo location) {
/* 17 */     this.location = location;
/*    */   }
/*    */   
/*    */   public <T> T getMetadataSection(MetadataSectionType<T> metadataSerializer) throws IOException {
/* 22 */     if (this.metadata == null)
/* 23 */       this.metadata = loadMetadata(this); 
/* 26 */     return this.metadata.getSection(metadataSerializer).orElse(null);
/*    */   }
/*    */   
/*    */   public static ResourceMetadata loadMetadata(PackResources packResources) throws IOException {
/* 30 */     IoSupplier<InputStream> metadata = packResources.getRootResource(new String[] { "pack.mcmeta" });
/* 31 */     if (metadata == null)
/* 32 */       return ResourceMetadata.EMPTY; 
/* 34 */     InputStream resource = (InputStream)metadata.get();
/*    */     try {
/* 35 */       ResourceMetadata resourceMetadata = ResourceMetadata.fromJsonStream(resource);
/* 36 */       if (resource != null)
/* 36 */         resource.close(); 
/*    */       return resourceMetadata;
/*    */     } catch (Throwable throwable) {
/*    */       if (resource != null)
/*    */         try {
/*    */           resource.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */   
/*    */   public PackLocationInfo location() {
/* 41 */     return this.location;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\AbstractPackResources.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */