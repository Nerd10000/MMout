/*     */ package net.minecraft.util.filefix.virtualfilesystem;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class Moves extends Record {
/*     */   private final List<Path> directories;
/*     */   
/*     */   private final List<FileMove> copiedFiles;
/*     */   
/*     */   private final List<FileMove> preexistingFiles;
/*     */   
/*     */   public Moves(List<Path> directories, List<FileMove> copiedFiles, List<FileMove> preexistingFiles) {
/* 210 */     this.directories = directories;
/* 210 */     this.copiedFiles = copiedFiles;
/* 210 */     this.preexistingFiles = preexistingFiles;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #210	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #210	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #210	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem$Moves;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public List<Path> directories() {
/* 210 */     return this.directories;
/*     */   }
/*     */   
/*     */   public List<FileMove> copiedFiles() {
/* 210 */     return this.copiedFiles;
/*     */   }
/*     */   
/*     */   public List<FileMove> preexistingFiles() {
/* 210 */     return this.preexistingFiles;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\virtualfilesystem\CopyOnWriteFileSystem$Moves.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */