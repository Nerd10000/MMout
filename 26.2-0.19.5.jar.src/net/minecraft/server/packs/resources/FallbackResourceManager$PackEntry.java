/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ 
/*     */ final class PackEntry extends Record {
/*     */   private final String name;
/*     */   
/*     */   private final PackResources resources;
/*     */   
/*     */   private final Predicate<Identifier> filter;
/*     */   
/*     */   private PackEntry(String name, PackResources resources, Predicate<Identifier> filter) {
/* 339 */     this.name = name;
/* 339 */     this.resources = resources;
/* 339 */     this.filter = filter;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$PackEntry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #339	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$PackEntry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$PackEntry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #339	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$PackEntry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$PackEntry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #339	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$PackEntry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String name() {
/* 339 */     return this.name;
/*     */   }
/*     */   
/*     */   public PackResources resources() {
/* 339 */     return this.resources;
/*     */   }
/*     */   
/*     */   public Predicate<Identifier> filter() {
/* 339 */     return this.filter;
/*     */   }
/*     */   
/*     */   public void filterAll(Collection<Identifier> collection) {
/* 341 */     if (this.filter != null)
/* 342 */       collection.removeIf(this.filter); 
/*     */   }
/*     */   
/*     */   public boolean isFiltered(Identifier location) {
/* 347 */     return (this.filter != null && this.filter.test(location));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\FallbackResourceManager$PackEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */