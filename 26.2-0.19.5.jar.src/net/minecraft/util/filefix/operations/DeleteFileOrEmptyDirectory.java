/*    */ package net.minecraft.util.filefix.operations;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.util.filefix.FileFixUtil;
/*    */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*    */ 
/*    */ public final class DeleteFileOrEmptyDirectory extends Record implements FileFixOperation {
/*    */   private final String target;
/*    */   
/*    */   public DeleteFileOrEmptyDirectory(String target) {
/*  9 */     this.target = target;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/operations/DeleteFileOrEmptyDirectory;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/operations/DeleteFileOrEmptyDirectory;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/operations/DeleteFileOrEmptyDirectory;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/operations/DeleteFileOrEmptyDirectory;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/operations/DeleteFileOrEmptyDirectory;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/filefix/operations/DeleteFileOrEmptyDirectory;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String target() {
/*  9 */     return this.target;
/*    */   }
/*    */   
/*    */   public void fix(Path baseDirectory, UpgradeProgress upgradeProgress) throws IOException {
/* 12 */     FileFixUtil.deleteFileOrEmptyDirectory(baseDirectory, this.target);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\operations\DeleteFileOrEmptyDirectory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */