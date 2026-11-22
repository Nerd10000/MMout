/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class LeakedResourceWarningInputStream extends FilterInputStream {
/*     */   private final Supplier<String> message;
/*     */   
/*     */   private boolean closed;
/*     */   
/*     */   public LeakedResourceWarningInputStream(InputStream wrapped, Identifier location, String name) {
/* 105 */     super(wrapped);
/* 106 */     Exception exception = new Exception("Stacktrace");
/* 107 */     this.message = (() -> {
/*     */         StringWriter data = new StringWriter();
/*     */         exception.printStackTrace(new PrintWriter(data));
/*     */         return "Leaked resource: '" + String.valueOf(location) + "' loaded from pack: '" + name + "'\n" + String.valueOf(data);
/*     */       });
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 116 */     super.close();
/* 117 */     this.closed = true;
/*     */   }
/*     */   
/*     */   protected void finalize() throws Throwable {
/* 122 */     if (!this.closed)
/* 123 */       FallbackResourceManager.LOGGER.warn("{}", this.message.get()); 
/* 126 */     super.finalize();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\FallbackResourceManager$LeakedResourceWarningInputStream.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */