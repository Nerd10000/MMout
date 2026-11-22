/*    */ package net.minecraft.data;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.hash.HashCode;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ 
/*    */ final class ProviderCacheBuilder extends Record {
/*    */   private final String version;
/*    */   
/*    */   private final ConcurrentMap<Path, HashCode> data;
/*    */   
/*    */   private ProviderCacheBuilder(String version, ConcurrentMap<Path, HashCode> data) {
/* 83 */     this.version = version;
/* 83 */     this.data = data;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/HashCache$ProviderCacheBuilder;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/HashCache$ProviderCacheBuilder;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/HashCache$ProviderCacheBuilder;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/HashCache$ProviderCacheBuilder;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/HashCache$ProviderCacheBuilder;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/HashCache$ProviderCacheBuilder;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String version() {
/* 83 */     return this.version;
/*    */   }
/*    */   
/*    */   public ConcurrentMap<Path, HashCode> data() {
/* 83 */     return this.data;
/*    */   }
/*    */   
/*    */   public ProviderCacheBuilder(String version) {
/* 85 */     this(version, new ConcurrentHashMap<>());
/*    */   }
/*    */   
/*    */   public void put(Path path, HashCode hash) {
/* 89 */     this.data.put(path, hash);
/*    */   }
/*    */   
/*    */   public HashCache.ProviderCache build() {
/* 93 */     return new HashCache.ProviderCache(this.version, ImmutableMap.copyOf(this.data));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\HashCache$ProviderCacheBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */