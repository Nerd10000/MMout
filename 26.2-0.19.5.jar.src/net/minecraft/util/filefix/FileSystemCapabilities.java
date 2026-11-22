/*    */ package net.minecraft.util.filefix;
/*    */ 
/*    */ import java.nio.file.CopyOption;
/*    */ import java.nio.file.LinkOption;
/*    */ import java.nio.file.StandardCopyOption;
/*    */ 
/*    */ public final class FileSystemCapabilities extends Record {
/*    */   private final boolean atomicMove;
/*    */   
/*    */   private final boolean hardLinks;
/*    */   
/*    */   public FileSystemCapabilities(boolean atomicMove, boolean hardLinks) {
/*  7 */     this.atomicMove = atomicMove;
/*  7 */     this.hardLinks = hardLinks;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/FileSystemCapabilities;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/FileSystemCapabilities;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/FileSystemCapabilities;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/FileSystemCapabilities;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/FileSystemCapabilities;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/filefix/FileSystemCapabilities;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean atomicMove() {
/*  7 */     return this.atomicMove;
/*    */   }
/*    */   
/*    */   public boolean hardLinks() {
/*  7 */     return this.hardLinks;
/*    */   }
/*    */   
/*    */   public CopyOption[] getMoveOptions() {
/*    */     CopyOption[] moveOptions;
/* 13 */     if (this.atomicMove) {
/* 14 */       moveOptions = new CopyOption[] { StandardCopyOption.ATOMIC_MOVE, LinkOption.NOFOLLOW_LINKS };
/*    */     } else {
/* 16 */       moveOptions = new CopyOption[] { LinkOption.NOFOLLOW_LINKS };
/*    */     } 
/* 18 */     return moveOptions;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\FileSystemCapabilities.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */