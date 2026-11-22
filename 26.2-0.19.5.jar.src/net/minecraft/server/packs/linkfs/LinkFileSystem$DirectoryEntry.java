/*     */ package net.minecraft.server.packs.linkfs;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ final class DirectoryEntry extends Record {
/*     */   private final Map<String, DirectoryEntry> children;
/*     */   
/*     */   private final Map<String, Path> files;
/*     */   
/*     */   public Map<String, Path> files() {
/* 160 */     return this.files;
/*     */   }
/*     */   
/*     */   public Map<String, DirectoryEntry> children() {
/* 160 */     return this.children;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #160	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #160	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #160	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/packs/linkfs/LinkFileSystem$DirectoryEntry;
/*     */   }
/*     */   
/*     */   private DirectoryEntry(Map<String, DirectoryEntry> children, Map<String, Path> files) {
/* 160 */     this.children = children;
/* 160 */     this.files = files;
/*     */   }
/*     */   
/*     */   public DirectoryEntry() {
/* 162 */     this(new HashMap<>(), new HashMap<>());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\linkfs\LinkFileSystem$DirectoryEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */