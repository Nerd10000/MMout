/*     */ package net.minecraft.server.packs.linkfs;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.nio.file.WatchService;
/*     */ import java.nio.file.attribute.UserPrincipalLookupService;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public class LinkFileSystem extends FileSystem {
/*  37 */   private static final Set<String> VIEWS = Set.of("basic");
/*     */   
/*     */   public static final String PATH_SEPARATOR = "/";
/*     */   
/*  39 */   private static final Splitter PATH_SPLITTER = Splitter.on('/');
/*     */   
/*     */   private final FileStore store;
/*     */   
/*  42 */   private final FileSystemProvider provider = new LinkFSProvider();
/*     */   
/*     */   private final LinkFSPath root;
/*     */   
/*     */   private LinkFileSystem(String name, DirectoryEntry rootEntry) {
/*  46 */     this.store = new LinkFSFileStore(name);
/*  47 */     this.root = buildPath(rootEntry, this, "", null);
/*     */   }
/*     */   
/*     */   private static LinkFSPath buildPath(DirectoryEntry entry, LinkFileSystem fileSystem, String selfName, LinkFSPath parent) {
/*  51 */     Object2ObjectOpenHashMap<String, LinkFSPath> children = new Object2ObjectOpenHashMap();
/*  52 */     LinkFSPath result = new LinkFSPath(fileSystem, selfName, parent, new PathContents.DirectoryContents((Map<String, LinkFSPath>)children));
/*  53 */     entry.files.forEach((name, linkTarget) -> children.put(name, new LinkFSPath(fileSystem, name, result, new PathContents.FileContents(linkTarget))));
/*  56 */     entry.children.forEach((name, childEntry) -> children.put(name, buildPath(childEntry, fileSystem, name, result)));
/*  59 */     children.trim();
/*  60 */     return result;
/*     */   }
/*     */   
/*     */   public FileSystemProvider provider() {
/*  65 */     return this.provider;
/*     */   }
/*     */   
/*     */   public void close() {}
/*     */   
/*     */   public boolean isOpen() {
/*  74 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly() {
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   public String getSeparator() {
/*  84 */     return "/";
/*     */   }
/*     */   
/*     */   public Iterable<Path> getRootDirectories() {
/*  89 */     return List.of(this.root);
/*     */   }
/*     */   
/*     */   public Iterable<FileStore> getFileStores() {
/*  94 */     return List.of(this.store);
/*     */   }
/*     */   
/*     */   public Set<String> supportedFileAttributeViews() {
/*  99 */     return VIEWS;
/*     */   }
/*     */   
/*     */   public Path getPath(String first, String... more) {
/* 104 */     Stream<String> path = Stream.of(first);
/* 105 */     if (more.length > 0)
/* 106 */       path = Stream.concat(path, Stream.of(more)); 
/* 108 */     String joinedPath = path.collect(Collectors.joining("/"));
/* 109 */     if (joinedPath.equals("/"))
/* 110 */       return this.root; 
/* 113 */     if (joinedPath.startsWith("/")) {
/* 114 */       LinkFSPath linkFSPath = this.root;
/* 115 */       for (String segment : PATH_SPLITTER.split(joinedPath.substring(1))) {
/* 116 */         if (segment.isEmpty())
/* 117 */           throw new IllegalArgumentException("Empty paths not allowed"); 
/* 119 */         linkFSPath = linkFSPath.resolveName(segment);
/*     */       } 
/* 121 */       return linkFSPath;
/*     */     } 
/* 123 */     LinkFSPath result = null;
/* 124 */     for (String segment : PATH_SPLITTER.split(joinedPath)) {
/* 125 */       if (segment.isEmpty())
/* 126 */         throw new IllegalArgumentException("Empty paths not allowed"); 
/* 128 */       result = new LinkFSPath(this, segment, result, PathContents.RELATIVE);
/*     */     } 
/* 130 */     if (result == null)
/* 131 */       throw new IllegalArgumentException("Empty paths not allowed"); 
/* 133 */     return result;
/*     */   }
/*     */   
/*     */   public PathMatcher getPathMatcher(String syntaxAndPattern) {
/* 139 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public UserPrincipalLookupService getUserPrincipalLookupService() {
/* 144 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public WatchService newWatchService() {
/* 149 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public FileStore store() {
/* 153 */     return this.store;
/*     */   }
/*     */   
/*     */   public LinkFSPath rootPath() {
/* 157 */     return this.root;
/*     */   }
/*     */   
/*     */   private static final class DirectoryEntry extends Record {
/*     */     private final Map<String, DirectoryEntry> children;
/*     */     
/*     */     private final Map<String, Path> files;
/*     */     
/*     */     public Map<String, Path> files() {
/* 160 */       return this.files;
/*     */     }
/*     */     
/*     */     public Map<String, DirectoryEntry> children() {
/* 160 */       return this.children;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;
/*     */     }
/*     */     
/*     */     private DirectoryEntry(Map<String, DirectoryEntry> children, Map<String, Path> files) {
/* 160 */       this.children = children;
/* 160 */       this.files = files;
/*     */     }
/*     */     
/*     */     public DirectoryEntry() {
/* 162 */       this(new HashMap<>(), new HashMap<>());
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final LinkFileSystem.DirectoryEntry root;
/*     */     
/*     */     public Builder() {
/* 167 */       this.root = new LinkFileSystem.DirectoryEntry();
/*     */     }
/*     */     
/*     */     public Builder put(List<String> path, String name, Path target) {
/* 170 */       LinkFileSystem.DirectoryEntry currentEntry = this.root;
/* 171 */       for (String segment : path)
/* 172 */         currentEntry = currentEntry.children.computeIfAbsent(segment, n -> new LinkFileSystem.DirectoryEntry()); 
/* 174 */       currentEntry.files.put(name, target);
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Builder put(List<String> path, Path target) {
/* 179 */       if (path.isEmpty())
/* 180 */         throw new IllegalArgumentException("Path can't be empty"); 
/* 182 */       int lastIndex = path.size() - 1;
/* 183 */       return put(path.subList(0, lastIndex), path.get(lastIndex), target);
/*     */     }
/*     */     
/*     */     public FileSystem build(String name) {
/* 187 */       return new LinkFileSystem(name, this.root);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/* 192 */     return new Builder();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\linkfs\LinkFileSystem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */