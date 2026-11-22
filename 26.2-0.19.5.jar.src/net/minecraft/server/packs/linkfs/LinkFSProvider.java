/*     */ package net.minecraft.server.packs.linkfs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.AccessDeniedException;
/*     */ import java.nio.file.AccessMode;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.DirectoryIteratorException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.NotDirectoryException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.ProviderMismatchException;
/*     */ import java.nio.file.ReadOnlyFileSystemException;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ class LinkFSProvider extends FileSystemProvider {
/*     */   public static final String SCHEME = "x-mc-link";
/*     */   
/*     */   public String getScheme() {
/*  40 */     return "x-mc-link";
/*     */   }
/*     */   
/*     */   public FileSystem newFileSystem(URI uri, Map<String, ?> env) {
/*  45 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public FileSystem getFileSystem(URI uri) {
/*  50 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Path getPath(URI uri) {
/*  55 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
/*  60 */     if (options.contains(StandardOpenOption.CREATE_NEW) || options
/*  61 */       .contains(StandardOpenOption.CREATE) || options
/*  62 */       .contains(StandardOpenOption.APPEND) || options
/*  63 */       .contains(StandardOpenOption.WRITE))
/*  65 */       throw new UnsupportedOperationException(); 
/*  67 */     Path targetPath = toLinkPath(path).toAbsolutePath().getTargetPath();
/*  68 */     if (targetPath == null)
/*  69 */       throw new NoSuchFileException(path.toString()); 
/*  71 */     return Files.newByteChannel(targetPath, options, attrs);
/*     */   }
/*     */   
/*     */   public DirectoryStream<Path> newDirectoryStream(Path dir, final DirectoryStream.Filter<? super Path> filter) throws IOException {
/*  76 */     final PathContents.DirectoryContents directoryContents = toLinkPath(dir).toAbsolutePath().getDirectoryContents();
/*  77 */     if (directoryContents == null)
/*  78 */       throw new NotDirectoryException(dir.toString()); 
/*  81 */     return new DirectoryStream<Path>(this) {
/*     */         public Iterator<Path> iterator() {
/*  84 */           return directoryContents.children().values()
/*  85 */             .stream()
/*  86 */             .filter(path -> {
/*     */                 try {
/*     */                   return filter.accept(path);
/*  89 */                 } catch (IOException e) {
/*     */                   throw new DirectoryIteratorException(e);
/*     */                 } 
/*  93 */               }).map(path -> path)
/*  94 */             .iterator();
/*     */         }
/*     */         
/*     */         public void close() {}
/*     */       };
/*     */   }
/*     */   
/*     */   public void createDirectory(Path dir, FileAttribute<?>... attrs) {
/* 105 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */   
/*     */   public void delete(Path path) {
/* 110 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */   
/*     */   public void copy(Path source, Path target, CopyOption... options) {
/* 115 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */   
/*     */   public void move(Path source, Path target, CopyOption... options) {
/* 120 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */   
/*     */   public boolean isSameFile(Path path, Path path2) {
/* 125 */     return (path instanceof LinkFSPath && path2 instanceof LinkFSPath && path.equals(path2));
/*     */   }
/*     */   
/*     */   public boolean isHidden(Path path) {
/* 130 */     return false;
/*     */   }
/*     */   
/*     */   public FileStore getFileStore(Path path) {
/* 135 */     return toLinkPath(path).getFileSystem().store();
/*     */   }
/*     */   
/*     */   public void checkAccess(Path path, AccessMode... modes) throws IOException {
/* 140 */     if (modes.length == 0 && 
/* 141 */       !toLinkPath(path).exists())
/* 142 */       throw new NoSuchFileException(path.toString()); 
/* 146 */     for (AccessMode mode : modes) {
/* 147 */       switch (mode) {
/*     */         case READ:
/* 149 */           if (!toLinkPath(path).exists())
/* 150 */             throw new NoSuchFileException(path.toString()); 
/*     */           break;
/*     */         case EXECUTE:
/*     */         case WRITE:
/* 153 */           throw new AccessDeniedException(mode.toString());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public <V extends java.nio.file.attribute.FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
/* 161 */     LinkFSPath linkPath = toLinkPath(path);
/* 162 */     if (type == BasicFileAttributeView.class)
/* 163 */       return (V)linkPath.getBasicAttributeView(); 
/* 165 */     return null;
/*     */   }
/*     */   
/*     */   public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
/* 171 */     LinkFSPath linkPath = toLinkPath(path).toAbsolutePath();
/* 172 */     if (type == BasicFileAttributes.class)
/* 173 */       return (A)linkPath.getBasicAttributes(); 
/* 175 */     throw new UnsupportedOperationException("Attributes of type " + type.getName() + " not supported");
/*     */   }
/*     */   
/*     */   public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) {
/* 181 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setAttribute(Path path, String attribute, Object value, LinkOption... options) {
/* 186 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */   
/*     */   private static LinkFSPath toLinkPath(Path path) {
/* 190 */     if (path == null)
/* 191 */       throw new NullPointerException(); 
/* 193 */     if (path instanceof LinkFSPath) {
/* 193 */       LinkFSPath p = (LinkFSPath)path;
/* 194 */       return p;
/*     */     } 
/* 196 */     throw new ProviderMismatchException();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\linkfs\LinkFSProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */