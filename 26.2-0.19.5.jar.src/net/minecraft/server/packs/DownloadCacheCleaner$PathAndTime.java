/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.FileTime;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ final class PathAndTime extends Record {
/*    */   private final Path path;
/*    */   
/*    */   private final FileTime modifiedTime;
/*    */   
/*    */   private PathAndTime(Path path, FileTime modifiedTime) {
/* 27 */     this.path = path;
/* 27 */     this.modifiedTime = modifiedTime;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Path path() {
/* 27 */     return this.path;
/*    */   }
/*    */   
/*    */   public FileTime modifiedTime() {
/* 27 */     return this.modifiedTime;
/*    */   }
/*    */   
/* 28 */   public static final Comparator<PathAndTime> NEWEST_FIRST = Comparator.<PathAndTime, Comparable>comparing(PathAndTime::modifiedTime).reversed();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\DownloadCacheCleaner$PathAndTime.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */