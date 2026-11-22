/*     */ package net.minecraft.world.level.validation;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.stream.Stream;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PathAllowList implements PathMatcher {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String COMMENT_PREFIX = "#";
/*     */   
/*     */   private final List<ConfigEntry> entries;
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface EntryType {
/*  23 */     public static final EntryType FILESYSTEM = FileSystem::getPathMatcher;
/*     */     
/*     */     public static final EntryType PREFIX = (fileSystem, pattern) -> ();
/*     */     
/*     */     PathMatcher compile(FileSystem param1FileSystem, String param1String);
/*     */   }
/*     */   
/*     */   public static final class ConfigEntry extends Record {
/*     */     private final PathAllowList.EntryType type;
/*     */     
/*     */     private final String pattern;
/*     */     
/*     */     public ConfigEntry(PathAllowList.EntryType type, String pattern) {
/*  30 */       this.type = type;
/*  30 */       this.pattern = pattern;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #30	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #30	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #30	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public PathAllowList.EntryType type() {
/*  30 */       return this.type;
/*     */     }
/*     */     
/*     */     public String pattern() {
/*  30 */       return this.pattern;
/*     */     }
/*     */     
/*     */     public PathMatcher compile(FileSystem fileSystem) {
/*  32 */       return type().compile(fileSystem, this.pattern);
/*     */     }
/*     */     
/*     */     public static Optional<ConfigEntry> parse(String definition) {
/*  36 */       if (definition.isBlank() || definition.startsWith("#"))
/*  37 */         return Optional.empty(); 
/*  39 */       if (!definition.startsWith("["))
/*  40 */         return Optional.of(new ConfigEntry(PathAllowList.EntryType.PREFIX, definition)); 
/*  43 */       int split = definition.indexOf(']', 1);
/*  44 */       if (split == -1)
/*  45 */         throw new IllegalArgumentException("Unterminated type in line '" + definition + "'"); 
/*  48 */       String type = definition.substring(1, split);
/*  49 */       String contents = definition.substring(split + 1);
/*  50 */       switch (type) {
/*     */         case "glob":
/*     */         case "regex":
/*     */         
/*     */         case "prefix":
/*     */         
/*     */       } 
/*  53 */       throw new IllegalArgumentException("Unsupported definition type in line '" + definition + "'");
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     static ConfigEntry glob(String pattern) {
/*  60 */       return new ConfigEntry(PathAllowList.EntryType.FILESYSTEM, "glob:" + pattern);
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     static ConfigEntry regex(String pattern) {
/*  66 */       return new ConfigEntry(PathAllowList.EntryType.FILESYSTEM, "regex:" + pattern);
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     static ConfigEntry prefix(String pattern) {
/*  72 */       return new ConfigEntry(PathAllowList.EntryType.PREFIX, pattern);
/*     */     }
/*     */   }
/*     */   
/*  77 */   private final Map<String, PathMatcher> compiledPaths = new ConcurrentHashMap<>();
/*     */   
/*     */   public PathAllowList(List<ConfigEntry> entries) {
/*  80 */     this.entries = entries;
/*     */   }
/*     */   
/*     */   public PathMatcher getForFileSystem(FileSystem fileSystem) {
/*  84 */     return this.compiledPaths.computeIfAbsent(fileSystem.provider().getScheme(), scheme -> {
/*     */           List<PathMatcher> compiledMatchers;
/*     */           try {
/*     */             compiledMatchers = this.entries.stream().map(()).toList();
/*  90 */           } catch (Exception e) {
/*     */             LOGGER.error("Failed to compile file pattern list", e);
/*     */             return ();
/*     */           } 
/*     */           switch (compiledMatchers.size()) {
/*     */             case 0:
/*     */             
/*     */             case 1:
/*     */             
/*     */           } 
/*     */           return ();
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean matches(Path path) {
/* 112 */     return getForFileSystem(path.getFileSystem()).matches(path);
/*     */   }
/*     */   
/*     */   public static PathAllowList readPlain(BufferedReader reader) {
/* 116 */     return new PathAllowList(reader.lines().<ConfigEntry>flatMap(line -> ConfigEntry.parse(line).stream()).toList());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\validation\PathAllowList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */