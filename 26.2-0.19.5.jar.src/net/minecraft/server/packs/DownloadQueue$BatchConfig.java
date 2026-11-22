/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.google.common.hash.HashFunction;
/*    */ import java.net.Proxy;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.HttpUtil;
/*    */ 
/*    */ public final class BatchConfig extends Record {
/*    */   private final HashFunction hashFunction;
/*    */   
/*    */   private final int maxSize;
/*    */   
/*    */   private final Map<String, String> headers;
/*    */   
/*    */   private final Proxy proxy;
/*    */   
/*    */   private final HttpUtil.DownloadProgressListener listener;
/*    */   
/*    */   public BatchConfig(HashFunction hashFunction, int maxSize, Map<String, String> headers, Proxy proxy, HttpUtil.DownloadProgressListener listener) {
/* 81 */     this.hashFunction = hashFunction;
/* 81 */     this.maxSize = maxSize;
/* 81 */     this.headers = headers;
/* 81 */     this.proxy = proxy;
/* 81 */     this.listener = listener;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #81	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #81	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #81	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HashFunction hashFunction() {
/* 81 */     return this.hashFunction;
/*    */   }
/*    */   
/*    */   public int maxSize() {
/* 81 */     return this.maxSize;
/*    */   }
/*    */   
/*    */   public Map<String, String> headers() {
/* 81 */     return this.headers;
/*    */   }
/*    */   
/*    */   public Proxy proxy() {
/* 81 */     return this.proxy;
/*    */   }
/*    */   
/*    */   public HttpUtil.DownloadProgressListener listener() {
/* 81 */     return this.listener;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\DownloadQueue$BatchConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */