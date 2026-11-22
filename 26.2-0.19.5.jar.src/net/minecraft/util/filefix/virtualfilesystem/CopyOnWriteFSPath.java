/*     */ package net.minecraft.util.filefix.virtualfilesystem;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.WatchEvent;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.nio.file.WatchService;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSIllegalArgumentException;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSNoSuchFileException;
/*     */ 
/*     */ public class CopyOnWriteFSPath implements Path {
/*     */   private final Path path;
/*     */   
/*     */   private final CopyOnWriteFileSystem fs;
/*     */   
/*     */   private final boolean isAbsolute;
/*     */   
/*     */   private CopyOnWriteFSPath(Path path, CopyOnWriteFileSystem fs, boolean isAbsolute) {
/*  22 */     this.path = path;
/*  23 */     this.fs = fs;
/*  24 */     this.isAbsolute = isAbsolute;
/*     */   }
/*     */   
/*     */   protected static CopyOnWriteFSPath of(CopyOnWriteFileSystem fs, String first, String... more) {
/*  28 */     boolean isAbsolute = false;
/*  29 */     while (first.startsWith("/")) {
/*  30 */       isAbsolute = true;
/*  31 */       first = first.substring(1);
/*     */     } 
/*  33 */     return new CopyOnWriteFSPath(fs.backingFileSystem().getPath(first, more), fs, isAbsolute);
/*     */   }
/*     */   
/*     */   public CopyOnWriteFileSystem getFileSystem() {
/*  38 */     return this.fs;
/*     */   }
/*     */   
/*     */   public boolean isAbsolute() {
/*  43 */     return this.isAbsolute;
/*     */   }
/*     */   
/*     */   public Path getRoot() {
/*  48 */     if (isAbsolute())
/*  49 */       return this.fs.rootPath(); 
/*  51 */     return null;
/*     */   }
/*     */   
/*     */   public CopyOnWriteFSPath getFileName() {
/*  56 */     if (isRoot())
/*  57 */       return null; 
/*  59 */     return new CopyOnWriteFSPath(this.path.getFileName(), this.fs, false);
/*     */   }
/*     */   
/*     */   public CopyOnWriteFSPath getParent() {
/*  64 */     if (isRoot())
/*  65 */       return null; 
/*  67 */     Path parent = this.path.getParent();
/*  68 */     if (parent == null) {
/*  69 */       if (isAbsolute())
/*  70 */         return this.fs.rootPath(); 
/*  72 */       return null;
/*     */     } 
/*  74 */     return new CopyOnWriteFSPath(parent, this.fs, isAbsolute());
/*     */   }
/*     */   
/*     */   public int getNameCount() {
/*  79 */     return (this.path.toString().isEmpty() && this.isAbsolute) ? 0 : this.path.getNameCount();
/*     */   }
/*     */   
/*     */   public Path getName(int index) {
/*  84 */     if (isRoot())
/*  85 */       throw new IllegalArgumentException(); 
/*  87 */     return new CopyOnWriteFSPath(this.path.getName(index), this.fs, false);
/*     */   }
/*     */   
/*     */   public Path subpath(int beginIndex, int endIndex) {
/*  92 */     return new CopyOnWriteFSPath(this.path.subpath(beginIndex, endIndex), this.fs, false);
/*     */   }
/*     */   
/*     */   public boolean startsWith(Path other) {
/*  97 */     CopyOnWriteFSPath otherCow = asCow(other);
/*  98 */     if (isAbsolute() != otherCow.isAbsolute())
/*  99 */       return false; 
/* 101 */     return this.path.startsWith(otherCow.path);
/*     */   }
/*     */   
/*     */   public boolean endsWith(Path other) {
/* 106 */     CopyOnWriteFSPath otherCow = asCow(other);
/* 107 */     if (otherCow.isAbsolute()) {
/* 108 */       if (isAbsolute())
/* 109 */         return equals(otherCow); 
/* 111 */       return false;
/*     */     } 
/* 113 */     return this.path.endsWith(otherCow.path);
/*     */   }
/*     */   
/*     */   public CopyOnWriteFSPath normalize() {
/* 118 */     Path normalize = this.path.normalize();
/* 119 */     if (isAbsolute() && (normalize.startsWith(".") || normalize.startsWith("..")))
/* 120 */       return this.fs.rootPath(); 
/* 122 */     return new CopyOnWriteFSPath(normalize, this.fs, isAbsolute());
/*     */   }
/*     */   
/*     */   public CopyOnWriteFSPath resolve(Path other) {
/* 127 */     CopyOnWriteFSPath otherCow = asCow(other);
/* 128 */     if (other.isAbsolute())
/* 129 */       return otherCow; 
/* 131 */     Path result = this.path.resolve(otherCow.path);
/* 132 */     return new CopyOnWriteFSPath(result, this.fs, isAbsolute());
/*     */   }
/*     */   
/*     */   public CopyOnWriteFSPath resolve(String other) {
/* 138 */     return resolve(getFileSystem().getPath(other, new String[0]));
/*     */   }
/*     */   
/*     */   public CopyOnWriteFSPath resolve(String first, String... more) {
/* 144 */     CopyOnWriteFSPath result = resolve(first);
/* 145 */     for (String s : more)
/* 146 */       result = result.resolve(s); 
/* 148 */     return result;
/*     */   }
/*     */   
/*     */   public CopyOnWriteFSPath relativize(Path other) {
/* 153 */     CopyOnWriteFSPath otherCow = asCow(other);
/* 154 */     if (isAbsolute() != otherCow.isAbsolute())
/* 155 */       throw new IllegalArgumentException("'other' is different type of Path"); 
/* 157 */     Path result = this.path.relativize(otherCow.path);
/* 158 */     return new CopyOnWriteFSPath(result, this.fs, false);
/*     */   }
/*     */   
/*     */   public URI toUri() {
/* 163 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public CopyOnWriteFSPath toAbsolutePath() {
/* 168 */     if (isAbsolute())
/* 169 */       return this; 
/* 171 */     return new CopyOnWriteFSPath(this.path, this.fs, true);
/*     */   }
/*     */   
/*     */   public CopyOnWriteFSPath toRealPath(LinkOption... options) throws CowFSNoSuchFileException {
/* 176 */     return this.fs.provider().getRealPath(this);
/*     */   }
/*     */   
/*     */   public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
/* 181 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public int compareTo(Path other) {
/* 186 */     CopyOnWriteFSPath otherCow = asCow(other);
/* 187 */     return toString().compareTo(otherCow.toString());
/*     */   }
/*     */   
/*     */   public String toString() {
/* 192 */     return isAbsolute() ? ("/" + String.valueOf(this.path)) : this.path.toString();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 197 */     if (o == null || getClass() != o.getClass())
/* 198 */       return false; 
/* 200 */     CopyOnWriteFSPath paths = (CopyOnWriteFSPath)o;
/* 201 */     return (this.isAbsolute == paths.isAbsolute && Objects.equals(this.path, paths.path) && Objects.equals(this.fs, paths.fs));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 206 */     return Objects.hash(new Object[] { this.path, this.fs, Boolean.valueOf(this.isAbsolute) });
/*     */   }
/*     */   
/*     */   protected boolean isRoot() {
/* 210 */     return (getNameCount() == 0);
/*     */   }
/*     */   
/*     */   protected static CopyOnWriteFSPath asCow(Path other) {
/* 214 */     if (other instanceof CopyOnWriteFSPath) {
/* 214 */       CopyOnWriteFSPath copyOnWriteFSPath = (CopyOnWriteFSPath)other;
/* 215 */       return copyOnWriteFSPath;
/*     */     } 
/* 217 */     throw new CowFSIllegalArgumentException("Other path is of mismatching file system");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\virtualfilesystem\CopyOnWriteFSPath.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */