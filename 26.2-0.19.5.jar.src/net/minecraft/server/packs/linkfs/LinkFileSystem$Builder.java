/*     */ package net.minecraft.server.packs.linkfs;
/*     */ 
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Builder {
/* 167 */   private final LinkFileSystem.DirectoryEntry root = new LinkFileSystem.DirectoryEntry();
/*     */   
/*     */   public Builder put(List<String> path, String name, Path target) {
/* 170 */     LinkFileSystem.DirectoryEntry currentEntry = this.root;
/* 171 */     for (String segment : path)
/* 172 */       currentEntry = currentEntry.children.computeIfAbsent(segment, n -> new LinkFileSystem.DirectoryEntry()); 
/* 174 */     currentEntry.files.put(name, target);
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public Builder put(List<String> path, Path target) {
/* 179 */     if (path.isEmpty())
/* 180 */       throw new IllegalArgumentException("Path can't be empty"); 
/* 182 */     int lastIndex = path.size() - 1;
/* 183 */     return put(path.subList(0, lastIndex), path.get(lastIndex), target);
/*     */   }
/*     */   
/*     */   public FileSystem build(String name) {
/* 187 */     return new LinkFileSystem(name, this.root);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\linkfs\LinkFileSystem$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */