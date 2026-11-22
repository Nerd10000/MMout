/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.IdentifierException;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ class null extends GlslPreprocessor {
/* 102 */   private final Set<Identifier> importedLocations = (Set<Identifier>)new ObjectArraySet();
/*     */   
/*     */   public String applyImport(boolean isRelative, String path) {
/*     */     Identifier location;
/*     */     try {
/* 108 */       if (isRelative) {
/* 109 */         location = parentLocation.withPath(parentPath -> FileUtil.normalizeResourcePath(parentPath + parentPath));
/*     */       } else {
/* 111 */         location = Identifier.parse(path).withPrefix("shaders/include/");
/*     */       } 
/* 113 */     } catch (IdentifierException e) {
/* 114 */       ShaderManager.LOGGER.error("Malformed GLSL import {}: {}", path, e.getMessage());
/* 115 */       return "#error " + e.getMessage();
/*     */     } 
/* 118 */     if (!this.importedLocations.add(location))
/* 119 */       return null; 
/*     */     try {
/* 122 */       Reader importResource = ((Resource)files.get(location)).openAsReader();
/*     */       try {
/* 123 */         String str = IOUtils.toString(importResource);
/* 124 */         if (importResource != null)
/* 124 */           importResource.close(); 
/*     */         return str;
/*     */       } catch (Throwable throwable) {
/*     */         if (importResource != null)
/*     */           try {
/*     */             importResource.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 124 */     } catch (IOException e) {
/* 125 */       ShaderManager.LOGGER.error("Could not open GLSL import {}: {}", location, e.getMessage());
/* 126 */       return "#error " + e.getMessage();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\ShaderManager$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */