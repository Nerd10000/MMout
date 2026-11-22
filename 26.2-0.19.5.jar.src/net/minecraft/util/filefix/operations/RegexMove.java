/*    */ package net.minecraft.util.filefix.operations;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.filefix.FileFixUtil;
/*    */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*    */ 
/*    */ public final class RegexMove extends Record implements FileFixOperation {
/*    */   private final Pattern fromPattern;
/*    */   
/*    */   private final String toReplacement;
/*    */   
/*    */   public RegexMove(Pattern fromPattern, String toReplacement) {
/* 13 */     this.fromPattern = fromPattern;
/* 13 */     this.toReplacement = toReplacement;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/operations/RegexMove;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/operations/RegexMove;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/operations/RegexMove;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/operations/RegexMove;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/operations/RegexMove;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/filefix/operations/RegexMove;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Pattern fromPattern() {
/* 13 */     return this.fromPattern;
/*    */   }
/*    */   
/*    */   public String toReplacement() {
/* 13 */     return this.toReplacement;
/*    */   }
/*    */   
/*    */   public RegexMove(String fromPattern, String toPattern) {
/* 15 */     this(Pattern.compile(fromPattern), toPattern);
/*    */   }
/*    */   
/*    */   public void fix(Path baseDirectory, UpgradeProgress upgradeProgress) throws IOException {
/* 20 */     if (!Files.exists(baseDirectory, new java.nio.file.LinkOption[0]) || !Files.isDirectory(baseDirectory, new java.nio.file.LinkOption[0]))
/*    */       return; 
/* 23 */     Stream<Path> files = Files.list(baseDirectory);
/*    */     try {
/* 24 */       for (Path file : files.toList()) {
/* 25 */         String fileName = file.getFileName().toString();
/* 26 */         Matcher matcher = this.fromPattern.matcher(fileName);
/* 27 */         if (matcher.matches()) {
/* 28 */           String newName = matcher.replaceAll(this.toReplacement);
/* 29 */           FileFixUtil.moveFile(baseDirectory, fileName, newName);
/*    */         } 
/*    */       } 
/* 32 */       if (files != null)
/* 32 */         files.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (files != null)
/*    */         try {
/*    */           files.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\operations\RegexMove.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */