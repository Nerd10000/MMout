/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.DirectoryNotEmptyException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DownloadCacheCleaner {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final class PathAndTime extends Record {
/*     */     private final Path path;
/*     */     
/*     */     private final FileTime modifiedTime;
/*     */     
/*     */     private PathAndTime(Path path, FileTime modifiedTime) {
/*  27 */       this.path = path;
/*  27 */       this.modifiedTime = modifiedTime;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Path path() {
/*  27 */       return this.path;
/*     */     }
/*     */     
/*     */     public FileTime modifiedTime() {
/*  27 */       return this.modifiedTime;
/*     */     }
/*     */     
/*  28 */     public static final Comparator<PathAndTime> NEWEST_FIRST = Comparator.<PathAndTime, Comparable>comparing(PathAndTime::modifiedTime).reversed();
/*     */   }
/*     */   
/*     */   private static final class PathAndPriority extends Record {
/*     */     private final Path path;
/*     */     
/*     */     private final int removalPriority;
/*     */     
/*     */     private PathAndPriority(Path path, int removalPriority) {
/*  31 */       this.path = path;
/*  31 */       this.removalPriority = removalPriority;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Path path() {
/*  31 */       return this.path;
/*     */     }
/*     */     
/*     */     public int removalPriority() {
/*  31 */       return this.removalPriority;
/*     */     }
/*     */     
/*  32 */     public static final Comparator<PathAndPriority> HIGHEST_PRIORITY_FIRST = Comparator.<PathAndPriority, Comparable>comparing(PathAndPriority::removalPriority).reversed();
/*     */   }
/*     */   
/*     */   public static void vacuumCacheDir(Path cacheDir, int maxFiles) {
/*     */     try {
/*  42 */       List<PathAndTime> filesAndDates = listFilesWithModificationTimes(cacheDir);
/*  43 */       int toRemove = filesAndDates.size() - maxFiles;
/*  44 */       if (toRemove <= 0)
/*     */         return; 
/*  48 */       filesAndDates.sort(PathAndTime.NEWEST_FIRST);
/*  49 */       List<PathAndPriority> filesWithDirOrder = prioritizeFilesInDirs(filesAndDates);
/*  51 */       Collections.reverse(filesWithDirOrder);
/*  52 */       filesWithDirOrder.sort(PathAndPriority.HIGHEST_PRIORITY_FIRST);
/*  54 */       Set<Path> emptyDirectoryCandidates = new HashSet<>();
/*  55 */       for (int i = 0; i < toRemove; i++) {
/*  56 */         PathAndPriority entry = filesWithDirOrder.get(i);
/*  57 */         Path pathToRemove = entry.path;
/*     */         try {
/*  59 */           Files.delete(pathToRemove);
/*  62 */           if (entry.removalPriority == 0)
/*  63 */             emptyDirectoryCandidates.add(pathToRemove.getParent()); 
/*  65 */         } catch (IOException e) {
/*  67 */           LOGGER.warn("Failed to delete cache file {}", pathToRemove, e);
/*     */         } 
/*     */       } 
/*  70 */       emptyDirectoryCandidates.remove(cacheDir);
/*  71 */       for (Path dir : emptyDirectoryCandidates) {
/*     */         try {
/*  73 */           Files.delete(dir);
/*  74 */         } catch (DirectoryNotEmptyException directoryNotEmptyException) {
/*     */         
/*  76 */         } catch (IOException e) {
/*  77 */           LOGGER.warn("Failed to delete empty(?) cache directory {}", dir, e);
/*     */         } 
/*     */       } 
/*  81 */     } catch (IOException|java.io.UncheckedIOException e) {
/*  82 */       LOGGER.error("Failed to vacuum cache dir {}", cacheDir, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<PathAndTime> listFilesWithModificationTimes(final Path cacheDir) throws IOException {
/*     */     try {
/*  88 */       final List<PathAndTime> unsortedFiles = new ArrayList<>();
/*  89 */       Files.walkFileTree(cacheDir, new SimpleFileVisitor<Path>() {
/*     */             public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
/*  93 */               if (attrs.isRegularFile() && !file.getParent().equals(cacheDir)) {
/*  96 */                 FileTime fileTime = attrs.lastModifiedTime();
/*  97 */                 unsortedFiles.add(new DownloadCacheCleaner.PathAndTime(file, fileTime));
/*     */               } 
/*  99 */               return FileVisitResult.CONTINUE;
/*     */             }
/*     */           });
/* 102 */       return unsortedFiles;
/* 103 */     } catch (NoSuchFileException e) {
/* 105 */       return List.of();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<PathAndPriority> prioritizeFilesInDirs(List<PathAndTime> filesAndDates) {
/* 110 */     List<PathAndPriority> result = new ArrayList<>();
/* 113 */     Object2IntOpenHashMap<Path> parentCounts = new Object2IntOpenHashMap();
/* 114 */     for (PathAndTime entry : filesAndDates) {
/* 115 */       int removalPriority = parentCounts.addTo(entry.path.getParent(), 1);
/* 116 */       result.add(new PathAndPriority(entry.path, removalPriority));
/*     */     } 
/* 119 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\DownloadCacheCleaner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */