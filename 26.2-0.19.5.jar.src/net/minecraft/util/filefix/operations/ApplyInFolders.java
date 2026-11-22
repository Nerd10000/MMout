/*    */ package net.minecraft.util.filefix.operations;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.filefix.access.FileRelation;
/*    */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*    */ 
/*    */ public final class ApplyInFolders extends Record implements FileFixOperation {
/*    */   private final FileRelation folders;
/*    */   
/*    */   private final List<FileFixOperation> fileFixOperations;
/*    */   
/*    */   public ApplyInFolders(FileRelation folders, List<FileFixOperation> fileFixOperations) {
/* 10 */     this.folders = folders;
/* 10 */     this.fileFixOperations = fileFixOperations;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/operations/ApplyInFolders;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/operations/ApplyInFolders;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/operations/ApplyInFolders;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/operations/ApplyInFolders;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/operations/ApplyInFolders;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/filefix/operations/ApplyInFolders;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public FileRelation folders() {
/* 10 */     return this.folders;
/*    */   }
/*    */   
/*    */   public List<FileFixOperation> fileFixOperations() {
/* 10 */     return this.fileFixOperations;
/*    */   }
/*    */   
/*    */   public void fix(Path baseDirectory, UpgradeProgress upgradeProgress) throws IOException {
/* 13 */     for (Path path : this.folders.getPaths(baseDirectory)) {
/* 14 */       for (FileFixOperation operation : this.fileFixOperations)
/* 15 */         operation.fix(path, upgradeProgress); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\operations\ApplyInFolders.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */