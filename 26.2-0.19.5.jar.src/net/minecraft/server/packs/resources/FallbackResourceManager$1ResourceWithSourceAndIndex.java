/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ 
/*     */ final class ResourceWithSourceAndIndex extends Record {
/*     */   private final PackResources packResources;
/*     */   
/*     */   private final IoSupplier<InputStream> resource;
/*     */   
/*     */   private final int packIndex;
/*     */   
/*     */   ResourceWithSourceAndIndex(PackResources packResources, IoSupplier<InputStream> resource, int packIndex) {
/* 188 */     this.packResources = packResources;
/* 188 */     this.resource = resource;
/* 188 */     this.packIndex = packIndex;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$1ResourceWithSourceAndIndex;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #188	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$1ResourceWithSourceAndIndex;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$1ResourceWithSourceAndIndex;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #188	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$1ResourceWithSourceAndIndex;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$1ResourceWithSourceAndIndex;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #188	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$1ResourceWithSourceAndIndex;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public PackResources packResources() {
/* 188 */     return this.packResources;
/*     */   }
/*     */   
/*     */   public IoSupplier<InputStream> resource() {
/* 188 */     return this.resource;
/*     */   }
/*     */   
/*     */   public int packIndex() {
/* 188 */     return this.packIndex;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\FallbackResourceManager$1ResourceWithSourceAndIndex.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */