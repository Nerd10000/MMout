/*    */ package net.minecraft.util.filefix.operations;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.util.filefix.FileFixUtil;
/*    */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*    */ 
/*    */ public final class Move extends Record implements FileFixOperation {
/*    */   private final String from;
/*    */   
/*    */   private final String to;
/*    */   
/*    */   public Move(String from, String to) {
/*  9 */     this.from = from;
/*  9 */     this.to = to;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/operations/Move;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/operations/Move;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/operations/Move;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/operations/Move;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/operations/Move;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/filefix/operations/Move;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String from() {
/*  9 */     return this.from;
/*    */   }
/*    */   
/*    */   public String to() {
/*  9 */     return this.to;
/*    */   }
/*    */   
/*    */   public Move relative(String sourceFolder, String targetFolder) {
/* 11 */     return new Move(sourceFolder + "/" + sourceFolder, targetFolder + "/" + targetFolder);
/*    */   }
/*    */   
/*    */   public void fix(Path baseDirectory, UpgradeProgress upgradeProgress) throws IOException {
/* 16 */     FileFixUtil.moveFile(baseDirectory, this.from, this.to);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\operations\Move.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */