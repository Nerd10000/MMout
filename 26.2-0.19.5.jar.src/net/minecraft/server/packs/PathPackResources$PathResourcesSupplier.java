/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ 
/*     */ public class PathResourcesSupplier implements Pack.ResourcesSupplier {
/*     */   private final Path content;
/*     */   
/*     */   public PathResourcesSupplier(Path content) {
/* 185 */     this.content = content;
/*     */   }
/*     */   
/*     */   public PackResources openPrimary(PackLocationInfo location) {
/* 190 */     return new PathPackResources(location, this.content);
/*     */   }
/*     */   
/*     */   public PackResources openFull(PackLocationInfo location, Pack.Metadata metadata) {
/* 195 */     PackResources primary = openPrimary(location);
/* 197 */     List<String> overlays = metadata.overlays();
/* 198 */     if (overlays.isEmpty())
/* 199 */       return primary; 
/* 202 */     List<PackResources> overlayResources = new ArrayList<>(overlays.size());
/* 203 */     for (String overlay : overlays) {
/* 204 */       Path overlayRoot = this.content.resolve(overlay);
/* 205 */       overlayResources.add(new PathPackResources(location, overlayRoot));
/*     */     } 
/* 208 */     return new CompositePackResources(primary, overlayResources);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\PathPackResources$PathResourcesSupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */