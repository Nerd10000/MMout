/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.google.common.hash.HashCode;
/*    */ import java.net.URL;
/*    */ 
/*    */ public final class DownloadRequest extends Record {
/*    */   private final URL url;
/*    */   
/*    */   private final HashCode hash;
/*    */   
/*    */   public DownloadRequest(URL url, HashCode hash) {
/* 76 */     this.url = url;
/* 76 */     this.hash = hash;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #76	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #76	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #76	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public URL url() {
/* 76 */     return this.url;
/*    */   }
/*    */   
/*    */   public HashCode hash() {
/* 76 */     return this.hash;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\DownloadQueue$DownloadRequest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */