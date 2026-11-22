/*     */ package net.minecraft.util.filefix.virtualfilesystem;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.WatchService;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.UserPrincipalLookupService;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSCreationException;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSSymlinkException;
/*     */ import org.apache.commons.io.file.PathUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class CopyOnWriteFileSystem extends FileSystem {
/*  31 */   private static final Set<String> FILE_ATTRIBUTE_VIEWS = Set.of("basic");
/*     */   
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final CopyOnWriteFileStore store;
/*     */   
/*     */   private final CopyOnWriteFSProvider provider;
/*     */   
/*     */   private final Path baseDirectory;
/*     */   
/*     */   private final PathMatcher skippedPaths;
/*     */   
/*     */   private final Path tmpDirectory;
/*     */   
/*     */   private final CopyOnWriteFSPath rootPath;
/*     */   
/*  42 */   private final AtomicInteger tmpFileIndex = new AtomicInteger();
/*     */   
/*     */   private DirectoryNode fileTree;
/*     */   
/*     */   private CopyOnWriteFileSystem(String name, Path baseDirectory, Path tmpDirectory, PathMatcher skippedPaths) throws IOException {
/*  47 */     this.baseDirectory = baseDirectory;
/*  48 */     this.tmpDirectory = tmpDirectory;
/*  49 */     this.skippedPaths = skippedPaths;
/*  50 */     this.provider = new CopyOnWriteFSProvider(this);
/*  51 */     this.store = new CopyOnWriteFileStore(name, this);
/*  52 */     this.rootPath = getPath("/", new String[0]);
/*  53 */     this.fileTree = buildFileTreeFrom(baseDirectory);
/*     */   }
/*     */   
/*     */   public static CopyOnWriteFileSystem create(String name, Path baseDirectory, Path tmpDirectory, PathMatcher skippedPaths) throws IOException {
/*  57 */     if (Files.exists(tmpDirectory, new java.nio.file.LinkOption[0]))
/*  58 */       throw new CowFSCreationException("Temporary directory already exists: " + String.valueOf(tmpDirectory)); 
/*  60 */     CopyOnWriteFileSystem fileSystem = new CopyOnWriteFileSystem(name, baseDirectory, tmpDirectory, skippedPaths);
/*  62 */     Files.createDirectory(tmpDirectory, (FileAttribute<?>[])new FileAttribute[0]);
/*  63 */     return fileSystem;
/*     */   }
/*     */   
/*     */   private DirectoryNode buildFileTreeFrom(final Path baseDirectory) throws IOException {
/*  67 */     final DirectoryNode fileTree = new DirectoryNode(this.rootPath);
/*  69 */     Files.walkFileTree(baseDirectory, new SimpleFileVisitor<Path>() {
/*     */           public FileVisitResult visitFile(Path realPath, BasicFileAttributes attrs) throws IOException {
/*  72 */             checkAttributes(realPath, attrs);
/*  73 */             if (CopyOnWriteFileSystem.this.skippedPaths.matches(realPath))
/*  74 */               return FileVisitResult.CONTINUE; 
/*  76 */             CopyOnWriteFSPath cowPath = toCowPath(realPath);
/*  77 */             DirectoryNode parentNode = fileTree.directoryByPath(Objects.<CopyOnWriteFSPath>requireNonNull(cowPath.getParent()));
/*  78 */             parentNode.addChild(new FileNode(cowPath, realPath, false));
/*  79 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */           
/*     */           public FileVisitResult preVisitDirectory(Path realPath, BasicFileAttributes attrs) throws IOException {
/*  84 */             checkAttributes(realPath, attrs);
/*  85 */             if (CopyOnWriteFileSystem.this.skippedPaths.matches(realPath))
/*  86 */               return FileVisitResult.SKIP_SUBTREE; 
/*  87 */             if (realPath.equals(baseDirectory))
/*  88 */               return FileVisitResult.CONTINUE; 
/*  90 */             CopyOnWriteFSPath cowPath = toCowPath(realPath);
/*  91 */             DirectoryNode parentNode = fileTree.directoryByPath(Objects.<CopyOnWriteFSPath>requireNonNull(cowPath.getParent()));
/*  92 */             parentNode.addChild(new DirectoryNode(cowPath));
/*  93 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */           
/*     */           private static void checkAttributes(Path realPath, BasicFileAttributes attrs) throws CowFSCreationException {
/*  97 */             if (!attrs.isRegularFile() && !attrs.isDirectory())
/*  98 */               throw new CowFSSymlinkException("Cannot build copy-on-write file system when symlink is present: " + String.valueOf(realPath)); 
/* 100 */             if (!Files.isWritable(realPath))
/* 102 */               throw new CowFSCreationException("Cannot build copy-on-write file system, missing write access for file: " + String.valueOf(realPath)); 
/*     */           }
/*     */           
/*     */           private CopyOnWriteFSPath toCowPath(Path realPath) {
/* 107 */             return fileTree.path().resolve(baseDirectory.relativize(realPath).toString());
/*     */           }
/*     */         });
/* 110 */     return fileTree;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected void resetFileTreeToBaseFolderContent() throws IOException {
/* 115 */     this.fileTree = buildFileTreeFrom(this.baseDirectory);
/*     */   }
/*     */   
/*     */   public CopyOnWriteFSProvider provider() {
/* 120 */     return this.provider;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 125 */     if (Files.exists(this.tmpDirectory, new java.nio.file.LinkOption[0]))
/* 126 */       PathUtils.deleteDirectory(this.tmpDirectory); 
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 132 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly() {
/* 137 */     return false;
/*     */   }
/*     */   
/*     */   public String getSeparator() {
/* 142 */     return backingFileSystem().getSeparator();
/*     */   }
/*     */   
/*     */   public Iterable<Path> getRootDirectories() {
/* 147 */     return List.of(rootPath());
/*     */   }
/*     */   
/*     */   public Iterable<FileStore> getFileStores() {
/* 152 */     return List.of(this.store);
/*     */   }
/*     */   
/*     */   public Set<String> supportedFileAttributeViews() {
/* 157 */     return FILE_ATTRIBUTE_VIEWS;
/*     */   }
/*     */   
/*     */   public CopyOnWriteFSPath getPath(String first, String... more) {
/* 162 */     return CopyOnWriteFSPath.of(this, first, more);
/*     */   }
/*     */   
/*     */   public PathMatcher getPathMatcher(String syntaxAndPattern) {
/* 167 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public UserPrincipalLookupService getUserPrincipalLookupService() {
/* 172 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public WatchService newWatchService() {
/* 177 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public CopyOnWriteFileStore store() {
/* 181 */     return this.store;
/*     */   }
/*     */   
/*     */   public CopyOnWriteFSPath rootPath() {
/* 185 */     return this.rootPath;
/*     */   }
/*     */   
/*     */   DirectoryNode fileTree() {
/* 190 */     return this.fileTree;
/*     */   }
/*     */   
/*     */   public Path baseDirectory() {
/* 194 */     return this.baseDirectory;
/*     */   }
/*     */   
/*     */   public Path tmpDirectory() {
/* 198 */     return this.tmpDirectory;
/*     */   }
/*     */   
/*     */   Path createTemporaryFilePath() {
/* 203 */     return this.tmpDirectory.resolve("tmp_" + this.tmpFileIndex.incrementAndGet());
/*     */   }
/*     */   
/*     */   public FileSystem backingFileSystem() {
/* 207 */     return this.tmpDirectory.getFileSystem();
/*     */   }
/*     */   
/*     */   public static final class Moves extends Record {
/*     */     private final List<Path> directories;
/*     */     
/*     */     private final List<FileMove> copiedFiles;
/*     */     
/*     */     private final List<FileMove> preexistingFiles;
/*     */     
/*     */     public Moves(List<Path> directories, List<FileMove> copiedFiles, List<FileMove> preexistingFiles) {
/* 210 */       this.directories = directories;
/* 210 */       this.copiedFiles = copiedFiles;
/* 210 */       this.preexistingFiles = preexistingFiles;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #210	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #210	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #210	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<Path> directories() {
/* 210 */       return this.directories;
/*     */     }
/*     */     
/*     */     public List<FileMove> copiedFiles() {
/* 210 */       return this.copiedFiles;
/*     */     }
/*     */     
/*     */     public List<FileMove> preexistingFiles() {
/* 210 */       return this.preexistingFiles;
/*     */     }
/*     */   }
/*     */   
/*     */   public Moves collectMoveOperations(Path outPath) {
/* 217 */     Moves result = new Moves(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
/* 218 */     collectMoveOperations(outPath, this.fileTree, result);
/* 219 */     return result;
/*     */   }
/*     */   
/*     */   private void collectMoveOperations(Path outPath, DirectoryNode folder, Moves result) {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: invokevirtual children : ()Ljava/util/Collection;
/*     */     //   4: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   9: astore #4
/*     */     //   11: aload #4
/*     */     //   13: invokeinterface hasNext : ()Z
/*     */     //   18: ifeq -> 202
/*     */     //   21: aload #4
/*     */     //   23: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   28: checkcast net/minecraft/util/filefix/virtualfilesystem/Node
/*     */     //   31: astore #5
/*     */     //   33: aload_1
/*     */     //   34: aload #5
/*     */     //   36: invokevirtual name : ()Ljava/lang/String;
/*     */     //   39: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   42: checkcast java/lang/String
/*     */     //   45: invokeinterface resolve : (Ljava/lang/String;)Ljava/nio/file/Path;
/*     */     //   50: astore #6
/*     */     //   52: aload #5
/*     */     //   54: dup
/*     */     //   55: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   58: pop
/*     */     //   59: astore #7
/*     */     //   61: iconst_0
/*     */     //   62: istore #8
/*     */     //   64: aload #7
/*     */     //   66: iload #8
/*     */     //   68: <illegal opcode> typeSwitch : (Lnet/minecraft/util/filefix/virtualfilesystem/Node;I)I
/*     */     //   73: lookupswitch default -> 100, 0 -> 110, 1 -> 171
/*     */     //   100: new java/lang/MatchException
/*     */     //   103: dup
/*     */     //   104: aconst_null
/*     */     //   105: aconst_null
/*     */     //   106: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   109: athrow
/*     */     //   110: aload #7
/*     */     //   112: checkcast net/minecraft/util/filefix/virtualfilesystem/FileNode
/*     */     //   115: astore #9
/*     */     //   117: new net/minecraft/util/filefix/virtualfilesystem/FileMove
/*     */     //   120: dup
/*     */     //   121: aload #9
/*     */     //   123: invokevirtual storagePath : ()Ljava/nio/file/Path;
/*     */     //   126: aload #6
/*     */     //   128: invokespecial <init> : (Ljava/nio/file/Path;Ljava/nio/file/Path;)V
/*     */     //   131: astore #10
/*     */     //   133: aload #9
/*     */     //   135: getfield isCopy : Z
/*     */     //   138: ifeq -> 156
/*     */     //   141: aload_3
/*     */     //   142: getfield copiedFiles : Ljava/util/List;
/*     */     //   145: aload #10
/*     */     //   147: invokeinterface add : (Ljava/lang/Object;)Z
/*     */     //   152: pop
/*     */     //   153: goto -> 168
/*     */     //   156: aload_3
/*     */     //   157: getfield preexistingFiles : Ljava/util/List;
/*     */     //   160: aload #10
/*     */     //   162: invokeinterface add : (Ljava/lang/Object;)Z
/*     */     //   167: pop
/*     */     //   168: goto -> 199
/*     */     //   171: aload #7
/*     */     //   173: checkcast net/minecraft/util/filefix/virtualfilesystem/DirectoryNode
/*     */     //   176: astore #10
/*     */     //   178: aload_3
/*     */     //   179: getfield directories : Ljava/util/List;
/*     */     //   182: aload #6
/*     */     //   184: invokeinterface add : (Ljava/lang/Object;)Z
/*     */     //   189: pop
/*     */     //   190: aload_0
/*     */     //   191: aload #6
/*     */     //   193: aload #10
/*     */     //   195: aload_3
/*     */     //   196: invokevirtual collectMoveOperations : (Ljava/nio/file/Path;Lnet/minecraft/util/filefix/virtualfilesystem/DirectoryNode;Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;)V
/*     */     //   199: goto -> 11
/*     */     //   202: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #223	-> 0
/*     */     //   #224	-> 33
/*     */     //   #225	-> 52
/*     */     //   #226	-> 110
/*     */     //   #227	-> 117
/*     */     //   #228	-> 133
/*     */     //   #229	-> 141
/*     */     //   #231	-> 156
/*     */     //   #233	-> 168
/*     */     //   #234	-> 171
/*     */     //   #235	-> 178
/*     */     //   #236	-> 190
/*     */     //   #239	-> 199
/*     */     //   #240	-> 202
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   133	35	10	move	Lnet/minecraft/util/filefix/virtualfilesystem/FileMove;
/*     */     //   117	54	9	fileNode	Lnet/minecraft/util/filefix/virtualfilesystem/FileNode;
/*     */     //   178	21	10	directoryNode	Lnet/minecraft/util/filefix/virtualfilesystem/DirectoryNode;
/*     */     //   52	147	6	target	Ljava/nio/file/Path;
/*     */     //   33	166	5	childNode	Lnet/minecraft/util/filefix/virtualfilesystem/Node;
/*     */     //   0	203	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem;
/*     */     //   0	203	1	outPath	Ljava/nio/file/Path;
/*     */     //   0	203	2	folder	Lnet/minecraft/util/filefix/virtualfilesystem/DirectoryNode;
/*     */     //   0	203	3	result	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;
/*     */   }
/*     */   
/*     */   public static void createDirectories(List<Path> directories) throws IOException {
/* 243 */     for (Path directory : directories)
/* 244 */       Files.createDirectory(directory, (FileAttribute<?>[])new FileAttribute[0]); 
/*     */   }
/*     */   
/*     */   public static void hardLinkFiles(List<FileMove> moves) throws IOException {
/* 249 */     for (FileMove move : moves) {
/* 250 */       if (Files.exists(move.to(), new java.nio.file.LinkOption[0]))
/*     */         continue; 
/* 254 */       if (Files.isRegularFile(move.from(), new java.nio.file.LinkOption[0])) {
/* 255 */         Files.createLink(move.to(), move.from());
/*     */         continue;
/*     */       } 
/* 257 */       throw new IllegalStateException("Not a regular file: " + String.valueOf(move.from()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void moveFiles(List<FileMove> moves) throws IOException {
/* 263 */     for (FileMove move : moves)
/* 264 */       Files.move(move.from(), move.to(), new CopyOption[0]); 
/*     */   }
/*     */   
/*     */   public static void moveFilesWithRetry(List<FileMove> moves, CopyOption... options) throws IOException {
/* 269 */     for (FileMove move : moves) {
/* 270 */       if (!Files.exists(move.from(), new java.nio.file.LinkOption[0]) && Files.exists(move.to(), new java.nio.file.LinkOption[0]))
/*     */         continue; 
/* 274 */       if (Files.isRegularFile(move.from(), new java.nio.file.LinkOption[0])) {
/* 275 */         Files.move(move.from(), move.to(), options);
/*     */         continue;
/*     */       } 
/* 277 */       throw new IOException("Not a regular file: " + String.valueOf(move.from()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static List<FileMove> tryRevertMoves(List<FileMove> moves, CopyOption... options) {
/* 283 */     List<FileMove> failedMoves = new ArrayList<>();
/* 284 */     for (FileMove move : moves) {
/* 285 */       if (!Files.exists(move.to(), new java.nio.file.LinkOption[0]) && Files.exists(move.from(), new java.nio.file.LinkOption[0]))
/*     */         continue; 
/* 289 */       if (Files.isRegularFile(move.to(), new java.nio.file.LinkOption[0])) {
/* 291 */         boolean success = Util.safeMoveFile(move.to(), move.from(), options);
/* 292 */         if (success) {
/* 293 */           LOGGER.info("Reverted move from {} to {}", move.from(), move.to());
/*     */           continue;
/*     */         } 
/* 295 */         LOGGER.error("Failed to revert move from {} to {}", move.from(), move.to());
/* 296 */         failedMoves.add(move);
/*     */         continue;
/*     */       } 
/* 299 */       LOGGER.error("Skipping reverting move from {} to {} as it's not a file", move.from(), move.to());
/* 300 */       failedMoves.add(move);
/*     */     } 
/* 303 */     if (failedMoves.isEmpty()) {
/* 304 */       LOGGER.info("Successfully reverted back to previous world state");
/*     */     } else {
/* 306 */       LOGGER.error("Completed reverting with errors");
/*     */     } 
/* 308 */     return failedMoves;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\virtualfilesystem\CopyOnWriteFileSystem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */