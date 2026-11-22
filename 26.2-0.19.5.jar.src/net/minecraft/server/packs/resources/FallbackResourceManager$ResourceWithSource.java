/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ 
/*     */ final class ResourceWithSource extends Record {
/*     */   private final PackResources source;
/*     */   
/*     */   private final IoSupplier<InputStream> resource;
/*     */   
/*     */   private ResourceWithSource(PackResources source, IoSupplier<InputStream> resource) {
/* 351 */     this.source = source;
/* 351 */     this.resource = resource;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$ResourceWithSource;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #351	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$ResourceWithSource;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$ResourceWithSource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #351	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$ResourceWithSource;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$ResourceWithSource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #351	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$ResourceWithSource;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public PackResources source() {
/* 351 */     return this.source;
/*     */   }
/*     */   
/*     */   public IoSupplier<InputStream> resource() {
/* 351 */     return this.resource;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\FallbackResourceManager$ResourceWithSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */