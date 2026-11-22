/*    */ package net.minecraft.data;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import com.google.common.hash.HashCode;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class ProviderCache extends Record {
/*    */   private final String version;
/*    */   
/*    */   private final ImmutableMap<Path, HashCode> data;
/*    */   
/*    */   private ProviderCache(String version, ImmutableMap<Path, HashCode> data) {
/* 38 */     this.version = version;
/* 38 */     this.data = data;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/HashCache$ProviderCache;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/HashCache$ProviderCache;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/HashCache$ProviderCache;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/HashCache$ProviderCache;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/HashCache$ProviderCache;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/HashCache$ProviderCache;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String version() {
/* 38 */     return this.version;
/*    */   }
/*    */   
/*    */   public ImmutableMap<Path, HashCode> data() {
/* 38 */     return this.data;
/*    */   }
/*    */   
/*    */   public HashCode get(Path path) {
/* 40 */     return (HashCode)this.data.get(path);
/*    */   }
/*    */   
/*    */   public int count() {
/* 44 */     return this.data.size();
/*    */   }
/*    */   
/*    */   public static ProviderCache load(Path rootDir, Path cacheFile) throws IOException {
/* 48 */     BufferedReader reader = Files.newBufferedReader(cacheFile, StandardCharsets.UTF_8);
/*    */     try {
/* 49 */       String header = reader.readLine();
/* 50 */       if (!header.startsWith("// "))
/* 51 */         throw new IllegalStateException("Missing cache file header"); 
/* 53 */       String[] headerFields = header.substring("// ".length()).split("\t", 2);
/* 54 */       String savedVersionId = headerFields[0];
/* 55 */       ImmutableMap.Builder<Path, HashCode> result = ImmutableMap.builder();
/* 56 */       reader.lines().forEach(s -> {
/*    */             int i = s.indexOf(' ');
/*    */             result.put(rootDir.resolve(s.substring(i + 1)), HashCode.fromString(s.substring(0, i)));
/*    */           });
/* 60 */       ProviderCache providerCache = new ProviderCache(savedVersionId, result.build());
/* 61 */       if (reader != null)
/* 61 */         reader.close(); 
/*    */       return providerCache;
/*    */     } catch (Throwable throwable) {
/*    */       if (reader != null)
/*    */         try {
/*    */           reader.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void save(Path rootDir, Path cacheFile, String extraHeaderInfo) {
/*    */     try {
/* 65 */       BufferedWriter output = Files.newBufferedWriter(cacheFile, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]);
/*    */       try {
/* 66 */         output.write("// ");
/* 67 */         output.write(this.version);
/* 68 */         output.write(9);
/* 69 */         output.write(extraHeaderInfo);
/* 70 */         output.newLine();
/* 71 */         for (UnmodifiableIterator<Map.Entry<Path, HashCode>> unmodifiableIterator = this.data.entrySet().iterator(); unmodifiableIterator.hasNext(); ) {
/* 71 */           Map.Entry<Path, HashCode> e = unmodifiableIterator.next();
/* 72 */           output.write(((HashCode)e.getValue()).toString());
/* 73 */           output.write(32);
/* 74 */           output.write(rootDir.relativize(e.getKey()).toString());
/* 75 */           output.newLine();
/*    */         } 
/* 77 */         if (output != null)
/* 77 */           output.close(); 
/*    */       } catch (Throwable throwable) {
/*    */         if (output != null)
/*    */           try {
/*    */             output.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           }  
/*    */         throw throwable;
/*    */       } 
/* 77 */     } catch (IOException e) {
/* 78 */       HashCache.LOGGER.warn("Unable write cachefile {}: {}", cacheFile, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\HashCache$ProviderCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */