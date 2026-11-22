/*     */ package net.minecraft.client.gui.screens.packs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardWatchEventKinds;
/*     */ import java.nio.file.WatchEvent;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.nio.file.WatchService;
/*     */ import java.util.List;
/*     */ 
/*     */ class Watcher implements AutoCloseable {
/*     */   private final WatchService watcher;
/*     */   
/*     */   private final Path packPath;
/*     */   
/*     */   public Watcher(Path packPath) throws IOException {
/* 336 */     this.packPath = packPath;
/* 337 */     this.watcher = packPath.getFileSystem().newWatchService();
/*     */     try {
/* 340 */       watchDir(packPath);
/* 343 */       DirectoryStream<Path> paths = Files.newDirectoryStream(packPath);
/*     */       try {
/* 344 */         for (Path path : paths) {
/* 345 */           if (Files.isDirectory(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS }))
/* 346 */             watchDir(path); 
/*     */         } 
/* 349 */         if (paths != null)
/* 349 */           paths.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (paths != null)
/*     */           try {
/*     */             paths.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 350 */     } catch (Exception e) {
/* 351 */       this.watcher.close();
/* 352 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Watcher create(Path packDir) {
/*     */     try {
/* 358 */       return new Watcher(packDir);
/* 359 */     } catch (IOException e) {
/* 360 */       PackSelectionScreen.LOGGER.warn("Failed to initialize pack directory {} monitoring", packDir, e);
/* 361 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void watchDir(Path packPath) throws IOException {
/* 366 */     packPath.register(this.watcher, (WatchEvent.Kind<?>[])new WatchEvent.Kind[] { StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY });
/*     */   }
/*     */   
/*     */   public boolean pollForChanges() throws IOException {
/* 370 */     boolean hasChanges = false;
/*     */     WatchKey key;
/* 374 */     while ((key = this.watcher.poll()) != null) {
/* 375 */       List<WatchEvent<?>> watchEvents = key.pollEvents();
/* 376 */       for (WatchEvent<?> watchEvent : watchEvents) {
/* 377 */         hasChanges = true;
/* 379 */         Path newPath = this.packPath.resolve((Path)watchEvent.context());
/* 380 */         if (key.watchable() == this.packPath && watchEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE && Files.isDirectory(newPath, new LinkOption[] { LinkOption.NOFOLLOW_LINKS }))
/* 381 */           watchDir(newPath); 
/*     */       } 
/* 385 */       key.reset();
/*     */     } 
/* 388 */     return hasChanges;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 393 */     this.watcher.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\packs\PackSelectionScreen$Watcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */