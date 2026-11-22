/*    */ package net.minecraft.util.filefix.access;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Stream;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface FileRelation {
/* 16 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 18 */   public static final FileRelation ORIGIN = List::of;
/*    */   
/*    */   public static final FileRelation REGION;
/*    */   
/*    */   public static final FileRelation DATA;
/*    */   
/*    */   public static final FileRelation PLAYER_DATA;
/*    */   
/*    */   static {
/* 19 */     REGION = (basePath -> List.of(basePath.resolve("region")));
/* 20 */     DATA = (basePath -> List.of(basePath.resolve("data")));
/* 21 */     PLAYER_DATA = (basePath -> List.of(basePath.resolve("players/data")));
/*    */   }
/*    */   
/* 22 */   public static final FileRelation DIMENSIONS = FileRelation::discoverDimensions;
/*    */   
/* 23 */   public static final FileRelation DIMENSIONS_DATA = DIMENSIONS.resolve(DATA);
/*    */   
/*    */   public static final FileRelation GENERATED_NAMESPACES;
/*    */   
/*    */   static {
/* 24 */     GENERATED_NAMESPACES = ORIGIN.resolve(basePath -> directoriesInPath(basePath.resolve("generated")));
/*    */   }
/*    */   
/* 25 */   public static final FileRelation OLD_OVERWORLD = ORIGIN;
/*    */   
/*    */   public static final FileRelation OLD_NETHER;
/*    */   
/*    */   public static final FileRelation OLD_END;
/*    */   
/*    */   static {
/* 26 */     OLD_NETHER = ORIGIN.resolve(basePath -> List.of(basePath.resolve("DIM-1")));
/* 27 */     OLD_END = ORIGIN.resolve(basePath -> List.of(basePath.resolve("DIM1")));
/*    */   }
/*    */   
/*    */   static FileRelation forDataFileInDimension(String dimension, String fileName) {
/* 32 */     return basePath -> List.of(basePath.resolve("dimensions/minecraft/" + dimension + "/data/" + fileName));
/*    */   }
/*    */   
/*    */   default FileRelation forFile(String fileName) {
/* 36 */     return resolve(basePath -> List.of(basePath.resolve(fileName)));
/*    */   }
/*    */   
/*    */   default FileRelation resolve(FileRelation other) {
/* 40 */     return basePath -> getPaths(basePath).stream().flatMap(()).toList();
/*    */   }
/*    */   
/*    */   FileRelation join(FileRelation... relations) {
/* 44 */     return basePath -> {
/*    */         Set<Path> paths = new HashSet<>();
/*    */         for (FileRelation relation : relations)
/*    */           paths.addAll(relation.getPaths(basePath)); 
/*    */         return List.copyOf(paths);
/*    */       };
/*    */   }
/*    */   
/*    */   private static List<Path> discoverDimensions(Path basePath) {
/* 54 */     Path dimensionsRoot = basePath.resolve("dimensions");
/* 55 */     if (!Files.exists(dimensionsRoot, new java.nio.file.LinkOption[0]))
/* 56 */       return getDefaultDimensions(basePath); 
/*    */     try {
/* 59 */       Stream<Path> namespacePaths = Files.list(dimensionsRoot);
/*    */       try {
/* 63 */         List<Path> discoveredDimensions = namespacePaths.filter(x$0 -> Files.isDirectory(x$0, new java.nio.file.LinkOption[0])).flatMap(path -> directoriesInPath(path).stream()).toList();
/* 65 */         List<Path> list1 = discoveredDimensions.isEmpty() ? getDefaultDimensions(basePath) : discoveredDimensions;
/* 66 */         if (namespacePaths != null)
/* 66 */           namespacePaths.close(); 
/*    */         return list1;
/*    */       } catch (Throwable throwable) {
/*    */         if (namespacePaths != null)
/*    */           try {
/*    */             namespacePaths.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           }  
/*    */         throw throwable;
/*    */       } 
/* 66 */     } catch (IOException e) {
/* 67 */       LOGGER.warn("Failed to discover dimensions, assuming default: {}", e.toString());
/* 68 */       return getDefaultDimensions(basePath);
/*    */     } 
/*    */   }
/*    */   
/*    */   static List<Path> directoriesInPath(Path path) {
/*    */     try {
/* 73 */       Stream<Path> dimensionPaths = Files.list(path);
/*    */       try {
/* 74 */         List<Path> list = dimensionPaths.filter(x$0 -> Files.isDirectory(x$0, new java.nio.file.LinkOption[0])).toList();
/* 75 */         if (dimensionPaths != null)
/* 75 */           dimensionPaths.close(); 
/*    */         return list;
/*    */       } catch (Throwable throwable) {
/*    */         if (dimensionPaths != null)
/*    */           try {
/*    */             dimensionPaths.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           }  
/*    */         throw throwable;
/*    */       } 
/* 75 */     } catch (IOException e) {
/* 76 */       return List.of();
/*    */     } 
/*    */   }
/*    */   
/*    */   private static List<Path> getDefaultDimensions(Path basePath) {
/* 81 */     return List.of(basePath
/* 82 */         .resolve("dimensions/minecraft/overworld"), basePath
/* 83 */         .resolve("dimensions/minecraft/the_nether"), basePath
/* 84 */         .resolve("dimensions/minecraft/the_end"));
/*    */   }
/*    */   
/*    */   List<Path> getPaths(Path paramPath);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\access\FileRelation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */