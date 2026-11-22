/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileAlreadyExistsException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.SharedConstants;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ 
/*     */ public class FileUtil {
/*  20 */   private static final Pattern COPY_COUNTER_PATTERN = Pattern.compile("(<name>.*) \\((<count>\\d*)\\)", 66);
/*     */   
/*     */   private static final int MAX_FILE_NAME = 255;
/*     */   
/*  23 */   private static final Pattern RESERVED_WINDOWS_FILENAMES = Pattern.compile(".*\\.|(?:COM|CLOCK\\$|CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\..*)?", 2);
/*     */   
/*  25 */   private static final Pattern STRICT_PATH_SEGMENT_CHECK = Pattern.compile("[-._a-z0-9]+");
/*     */   
/*     */   public static String sanitizeName(String baseName) {
/*  28 */     for (char replacer : SharedConstants.ILLEGAL_FILE_CHARACTERS)
/*  29 */       baseName = baseName.replace(replacer, '_'); 
/*  32 */     return baseName.replaceAll("[./\"]", "_");
/*     */   }
/*     */   
/*     */   public static String findAvailableName(Path baseDir, String baseName, String suffix) throws IOException {
/*  36 */     baseName = sanitizeName(baseName);
/*  38 */     if (!isPathPartPortable(baseName))
/*  39 */       baseName = "_" + baseName + "_"; 
/*  42 */     Matcher matcher = COPY_COUNTER_PATTERN.matcher(baseName);
/*  43 */     int count = 0;
/*  44 */     if (matcher.matches()) {
/*  45 */       baseName = matcher.group("name");
/*  46 */       count = Integer.parseInt(matcher.group("count"));
/*     */     } 
/*  48 */     if (baseName.length() > 255 - suffix.length())
/*  49 */       baseName = baseName.substring(0, 255 - suffix.length()); 
/*     */     while (true) {
/*  55 */       String nameToTest = baseName;
/*  56 */       if (count != 0) {
/*  57 */         String countSuffix = " (" + count + ")";
/*  58 */         int length = 255 - countSuffix.length();
/*  59 */         if (nameToTest.length() > length)
/*  60 */           nameToTest = nameToTest.substring(0, length); 
/*  62 */         nameToTest = nameToTest + nameToTest;
/*     */       } 
/*  65 */       nameToTest = nameToTest + nameToTest;
/*  67 */       Path fullPath = baseDir.resolve(nameToTest);
/*     */       try {
/*  69 */         Path created = Files.createDirectory(fullPath, (FileAttribute<?>[])new FileAttribute[0]);
/*  70 */         Files.deleteIfExists(created);
/*  71 */         return baseDir.relativize(created).toString();
/*  72 */       } catch (FileAlreadyExistsException e) {
/*  73 */         count++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isPathPortable(Path path) {
/*  79 */     for (Path part : path) {
/*  80 */       if (!isPathPartPortable(part.toString()))
/*  81 */         return false; 
/*     */     } 
/*  85 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isPathPartPortable(String name) {
/*  89 */     return !RESERVED_WINDOWS_FILENAMES.matcher(name).matches();
/*     */   }
/*     */   
/*     */   public static String getFullResourcePath(String filename) {
/*  93 */     return FilenameUtils.getFullPath(filename).replace(File.separator, "/");
/*     */   }
/*     */   
/*     */   public static String normalizeResourcePath(String filename) {
/*  97 */     return FilenameUtils.normalize(filename).replace(File.separator, "/");
/*     */   }
/*     */   
/*     */   public static DataResult<List<String>> decomposePath(String path) {
/* 105 */     int segmentEnd = path.indexOf('/');
/* 106 */     if (segmentEnd == -1) {
/* 107 */       switch (path) {
/*     */         case "":
/*     */         case ".":
/*     */         case "..":
/*     */         
/*     */       } 
/* 107 */       return 
/*     */         
/* 110 */         !containsAllowedCharactersOnly(path) ? 
/* 111 */         DataResult.error(() -> "Invalid path '" + path + "'") : 
/*     */         
/* 113 */         DataResult.success(List.of(path));
/*     */     } 
/* 118 */     List<String> result = new ArrayList<>();
/* 120 */     int segmentStart = 0;
/* 121 */     boolean lastSegment = false;
/*     */     while (true) {
/* 123 */       String segment = path.substring(segmentStart, segmentEnd);
/* 124 */       switch (segment) {
/*     */         case "":
/*     */         case ".":
/*     */         case "..":
/* 126 */           return DataResult.error(() -> "Invalid segment '" + segment + "' in path '" + path + "'");
/*     */       } 
/* 129 */       if (!containsAllowedCharactersOnly(segment))
/* 130 */         return DataResult.error(() -> "Invalid segment '" + segment + "' in path '" + path + "'"); 
/* 132 */       result.add(segment);
/* 136 */       if (lastSegment)
/* 137 */         return DataResult.success(result); 
/* 139 */       segmentStart = segmentEnd + 1;
/* 140 */       segmentEnd = path.indexOf('/', segmentStart);
/* 141 */       if (segmentEnd == -1) {
/* 142 */         segmentEnd = path.length();
/* 143 */         lastSegment = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Path resolvePath(Path root, List<String> segments) {
/* 150 */     int size = segments.size();
/* 151 */     switch (size) {
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */     } 
/* 155 */     String[] rest = new String[size - 1];
/* 156 */     for (int i = 1; i < size; i++)
/* 157 */       rest[i - 1] = segments.get(i); 
/*     */   }
/*     */   
/*     */   private static boolean containsAllowedCharactersOnly(String segment) {
/* 165 */     return STRICT_PATH_SEGMENT_CHECK.matcher(segment).matches();
/*     */   }
/*     */   
/*     */   public static boolean isValidPathSegment(String segment) {
/* 169 */     return (!segment.equals("..") && !segment.equals(".") && containsAllowedCharactersOnly(segment));
/*     */   }
/*     */   
/*     */   public static void validatePath(String... path) {
/* 173 */     if (path.length == 0)
/* 174 */       throw new IllegalArgumentException("Path must have at least one element"); 
/* 176 */     for (String segment : path) {
/* 177 */       if (!isValidPathSegment(segment))
/* 178 */         throw new IllegalArgumentException("Illegal segment " + segment + " in path " + Arrays.toString(path)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void createDirectoriesSafe(Path dir) throws IOException {
/* 185 */     Files.createDirectories(Files.exists(dir, new java.nio.file.LinkOption[0]) ? dir.toRealPath(new java.nio.file.LinkOption[0]) : dir, (FileAttribute<?>[])new FileAttribute[0]);
/*     */   }
/*     */   
/*     */   public static boolean isEmptyPath(Path path) {
/* 189 */     return (path.getNameCount() == 1 && path.getFileName().toString().isEmpty());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\FileUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */