/*     */ package net.minecraft.server.packs.linkfs;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.ProviderMismatchException;
/*     */ import java.nio.file.ReadOnlyFileSystemException;
/*     */ import java.nio.file.WatchEvent;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.nio.file.WatchService;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.util.DummyFileAttributes;
/*     */ 
/*     */ class LinkFSPath implements Path {
/*  30 */   private static final Comparator<LinkFSPath> PATH_COMPARATOR = Comparator.comparing(LinkFSPath::pathToString);
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final LinkFileSystem fileSystem;
/*     */   
/*     */   private final LinkFSPath parent;
/*     */   
/*     */   private List<String> pathToRoot;
/*     */   
/*     */   private String pathString;
/*     */   
/*     */   private final PathContents pathContents;
/*     */   
/*     */   public LinkFSPath(LinkFileSystem fileSystem, String name, LinkFSPath parent, PathContents pathContents) {
/*  41 */     this.fileSystem = fileSystem;
/*  42 */     this.name = name;
/*  43 */     this.parent = parent;
/*  44 */     this.pathContents = pathContents;
/*     */   }
/*     */   
/*     */   private LinkFSPath createRelativePath(LinkFSPath parent, String name) {
/*  48 */     return new LinkFSPath(this.fileSystem, name, parent, PathContents.RELATIVE);
/*     */   }
/*     */   
/*     */   public LinkFileSystem getFileSystem() {
/*  53 */     return this.fileSystem;
/*     */   }
/*     */   
/*     */   public boolean isAbsolute() {
/*  58 */     return (this.pathContents != PathContents.RELATIVE);
/*     */   }
/*     */   
/*     */   public File toFile() {
/*  63 */     PathContents pathContents = this.pathContents;
/*  63 */     if (pathContents instanceof PathContents.FileContents) {
/*  63 */       PathContents.FileContents file = (PathContents.FileContents)pathContents;
/*  64 */       return file.contents().toFile();
/*     */     } 
/*  66 */     throw new UnsupportedOperationException("Path " + pathToString() + " does not represent file");
/*     */   }
/*     */   
/*     */   public LinkFSPath getRoot() {
/*  71 */     if (isAbsolute())
/*  72 */       return this.fileSystem.rootPath(); 
/*  74 */     return null;
/*     */   }
/*     */   
/*     */   public LinkFSPath getFileName() {
/*  79 */     return createRelativePath(null, this.name);
/*     */   }
/*     */   
/*     */   public LinkFSPath getParent() {
/*  84 */     return this.parent;
/*     */   }
/*     */   
/*     */   public int getNameCount() {
/*  89 */     return pathToRoot().size();
/*     */   }
/*     */   
/*     */   private List<String> pathToRoot() {
/*  94 */     if (this.name.isEmpty())
/*  95 */       return List.of(); 
/*  98 */     if (this.pathToRoot == null) {
/*  99 */       ImmutableList.Builder<String> result = ImmutableList.builder();
/* 100 */       if (this.parent != null)
/* 101 */         result.addAll(this.parent.pathToRoot()); 
/* 103 */       result.add(this.name);
/* 104 */       this.pathToRoot = (List<String>)result.build();
/*     */     } 
/* 106 */     return this.pathToRoot;
/*     */   }
/*     */   
/*     */   public LinkFSPath getName(int index) {
/* 111 */     List<String> names = pathToRoot();
/* 112 */     if (index < 0 || index >= names.size())
/* 113 */       throw new IllegalArgumentException("Invalid index: " + index); 
/* 115 */     return createRelativePath(null, names.get(index));
/*     */   }
/*     */   
/*     */   public LinkFSPath subpath(int beginIndex, int endIndex) {
/* 120 */     List<String> names = pathToRoot();
/* 122 */     if (beginIndex < 0 || endIndex > names.size() || beginIndex >= endIndex)
/* 123 */       throw new IllegalArgumentException(); 
/* 126 */     LinkFSPath current = null;
/* 127 */     for (int i = beginIndex; i < endIndex; i++)
/* 128 */       current = createRelativePath(current, names.get(i)); 
/* 130 */     return current;
/*     */   }
/*     */   
/*     */   public boolean startsWith(Path other) {
/* 135 */     if (other.isAbsolute() != isAbsolute())
/* 136 */       return false; 
/* 138 */     if (other instanceof LinkFSPath) {
/* 138 */       LinkFSPath otherLink = (LinkFSPath)other;
/* 139 */       if (otherLink.fileSystem != this.fileSystem)
/* 140 */         return false; 
/* 142 */       List<String> thisNames = pathToRoot();
/* 143 */       List<String> otherNames = otherLink.pathToRoot();
/* 145 */       int otherSize = otherNames.size();
/* 146 */       if (otherSize > thisNames.size())
/* 147 */         return false; 
/* 149 */       for (int i = 0; i < otherSize; i++) {
/* 150 */         if (!((String)otherNames.get(i)).equals(thisNames.get(i)))
/* 151 */           return false; 
/*     */       } 
/* 154 */       return true;
/*     */     } 
/* 156 */     return false;
/*     */   }
/*     */   
/*     */   public boolean endsWith(Path other) {
/* 161 */     if (other.isAbsolute() && !isAbsolute())
/* 162 */       return false; 
/* 164 */     if (other instanceof LinkFSPath) {
/* 164 */       LinkFSPath otherLink = (LinkFSPath)other;
/* 165 */       if (otherLink.fileSystem != this.fileSystem)
/* 166 */         return false; 
/* 168 */       List<String> thisNames = pathToRoot();
/* 169 */       List<String> otherNames = otherLink.pathToRoot();
/* 171 */       int otherSize = otherNames.size();
/* 172 */       int delta = thisNames.size() - otherSize;
/* 173 */       if (delta < 0)
/* 174 */         return false; 
/* 177 */       for (int i = otherSize - 1; i >= 0; i--) {
/* 178 */         if (!((String)otherNames.get(i)).equals(thisNames.get(delta + i)))
/* 179 */           return false; 
/*     */       } 
/* 182 */       return true;
/*     */     } 
/* 184 */     return false;
/*     */   }
/*     */   
/*     */   public LinkFSPath normalize() {
/* 190 */     return this;
/*     */   }
/*     */   
/*     */   public LinkFSPath resolve(Path other) {
/* 195 */     LinkFSPath otherLink = toLinkPath(other);
/* 196 */     if (other.isAbsolute())
/* 197 */       return otherLink; 
/* 199 */     return resolve(otherLink.pathToRoot());
/*     */   }
/*     */   
/*     */   private LinkFSPath resolve(List<String> names) {
/* 203 */     LinkFSPath current = this;
/* 204 */     for (String name : names)
/* 205 */       current = current.resolveName(name); 
/* 208 */     return current;
/*     */   }
/*     */   
/*     */   LinkFSPath resolveName(String name) {
/* 213 */     if (isRelativeOrMissing(this.pathContents))
/* 214 */       return new LinkFSPath(this.fileSystem, name, this, this.pathContents); 
/* 215 */     PathContents pathContents = this.pathContents;
/* 215 */     if (pathContents instanceof PathContents.DirectoryContents) {
/* 215 */       PathContents.DirectoryContents directory = (PathContents.DirectoryContents)pathContents;
/* 216 */       LinkFSPath child = directory.children().get(name);
/* 217 */       return (child != null) ? child : new LinkFSPath(this.fileSystem, name, this, PathContents.MISSING);
/*     */     } 
/* 218 */     if (this.pathContents instanceof PathContents.FileContents)
/* 219 */       return new LinkFSPath(this.fileSystem, name, this, PathContents.MISSING); 
/* 222 */     throw new AssertionError("All content types should be already handled");
/*     */   }
/*     */   
/*     */   private static boolean isRelativeOrMissing(PathContents contents) {
/* 226 */     return (contents == PathContents.MISSING || contents == PathContents.RELATIVE);
/*     */   }
/*     */   
/*     */   public LinkFSPath relativize(Path other) {
/* 231 */     LinkFSPath otherLink = toLinkPath(other);
/* 232 */     if (isAbsolute() != otherLink.isAbsolute())
/* 233 */       throw new IllegalArgumentException("absolute mismatch"); 
/* 235 */     List<String> thisNames = pathToRoot();
/* 236 */     List<String> otherNames = otherLink.pathToRoot();
/* 239 */     if (thisNames.size() >= otherNames.size())
/* 240 */       throw new IllegalArgumentException(); 
/* 243 */     for (int i = 0; i < thisNames.size(); i++) {
/* 244 */       if (!((String)thisNames.get(i)).equals(otherNames.get(i)))
/* 245 */         throw new IllegalArgumentException(); 
/*     */     } 
/* 249 */     return otherLink.subpath(thisNames.size(), otherNames.size());
/*     */   }
/*     */   
/*     */   public URI toUri() {
/*     */     try {
/* 255 */       return new URI("x-mc-link", this.fileSystem.store().name(), pathToString(), null);
/* 256 */     } catch (URISyntaxException e) {
/* 257 */       throw new AssertionError("Failed to create URI", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public LinkFSPath toAbsolutePath() {
/* 263 */     if (isAbsolute())
/* 264 */       return this; 
/* 267 */     return this.fileSystem.rootPath().resolve(this);
/*     */   }
/*     */   
/*     */   public LinkFSPath toRealPath(LinkOption... options) {
/* 272 */     return toAbsolutePath();
/*     */   }
/*     */   
/*     */   public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) {
/* 277 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public int compareTo(Path other) {
/* 282 */     LinkFSPath otherPath = toLinkPath(other);
/* 283 */     return PATH_COMPARATOR.compare(this, otherPath);
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/* 288 */     if (other == this)
/* 289 */       return true; 
/* 291 */     if (other instanceof LinkFSPath) {
/* 291 */       LinkFSPath that = (LinkFSPath)other;
/* 292 */       if (this.fileSystem != that.fileSystem)
/* 293 */         return false; 
/* 295 */       boolean hasRealContents = hasRealContents();
/* 296 */       if (hasRealContents != that.hasRealContents())
/* 297 */         return false; 
/* 299 */       if (hasRealContents)
/* 301 */         return (this.pathContents == that.pathContents); 
/* 303 */       return (Objects.equals(this.parent, that.parent) && Objects.equals(this.name, that.name));
/*     */     } 
/* 305 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasRealContents() {
/* 309 */     return !isRelativeOrMissing(this.pathContents);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 314 */     return hasRealContents() ? this.pathContents.hashCode() : this.name.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 319 */     return pathToString();
/*     */   }
/*     */   
/*     */   private String pathToString() {
/* 323 */     if (this.pathString == null) {
/* 324 */       StringBuilder builder = new StringBuilder();
/* 325 */       if (isAbsolute())
/* 326 */         builder.append("/"); 
/* 328 */       Joiner.on("/").appendTo(builder, pathToRoot());
/* 329 */       this.pathString = builder.toString();
/*     */     } 
/* 331 */     return this.pathString;
/*     */   }
/*     */   
/*     */   private LinkFSPath toLinkPath(Path path) {
/* 335 */     if (path == null)
/* 336 */       throw new NullPointerException(); 
/* 338 */     if (path instanceof LinkFSPath) {
/* 338 */       LinkFSPath p = (LinkFSPath)path;
/* 338 */       if (p.fileSystem == this.fileSystem)
/* 339 */         return p; 
/*     */     } 
/* 341 */     throw new ProviderMismatchException();
/*     */   }
/*     */   
/*     */   public boolean exists() {
/* 345 */     return hasRealContents();
/*     */   }
/*     */   
/*     */   public Path getTargetPath() {
/* 349 */     PathContents pathContents = this.pathContents;
/* 349 */     PathContents.FileContents file = (PathContents.FileContents)pathContents;
/* 349 */     return (pathContents instanceof PathContents.FileContents) ? file.contents() : null;
/*     */   }
/*     */   
/*     */   public PathContents.DirectoryContents getDirectoryContents() {
/* 353 */     PathContents pathContents = this.pathContents;
/* 353 */     PathContents.DirectoryContents dir = (PathContents.DirectoryContents)pathContents;
/* 353 */     return (pathContents instanceof PathContents.DirectoryContents) ? dir : null;
/*     */   }
/*     */   
/*     */   public BasicFileAttributeView getBasicAttributeView() {
/* 357 */     return new BasicFileAttributeView() {
/*     */         public String name() {
/* 360 */           return "basic";
/*     */         }
/*     */         
/*     */         public BasicFileAttributes readAttributes() throws IOException {
/* 365 */           return LinkFSPath.this.getBasicAttributes();
/*     */         }
/*     */         
/*     */         public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) {
/* 370 */           throw new ReadOnlyFileSystemException();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public BasicFileAttributes getBasicAttributes() throws IOException {
/* 376 */     if (this.pathContents instanceof PathContents.DirectoryContents)
/* 377 */       return DummyFileAttributes.DIRECTORY; 
/* 379 */     if (this.pathContents instanceof PathContents.FileContents)
/* 380 */       return DummyFileAttributes.FILE; 
/* 382 */     throw new NoSuchFileException(pathToString());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\linkfs\LinkFSPath.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */