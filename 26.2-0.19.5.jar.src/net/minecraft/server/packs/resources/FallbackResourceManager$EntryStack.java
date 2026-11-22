/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ 
/*     */ final class EntryStack extends Record {
/*     */   private final Identifier fileLocation;
/*     */   
/*     */   private final Identifier metadataLocation;
/*     */   
/*     */   private final List<FallbackResourceManager.ResourceWithSource> fileSources;
/*     */   
/*     */   private final Map<PackResources, IoSupplier<InputStream>> metaSources;
/*     */   
/*     */   public Map<PackResources, IoSupplier<InputStream>> metaSources() {
/* 264 */     return this.metaSources;
/*     */   }
/*     */   
/*     */   public List<FallbackResourceManager.ResourceWithSource> fileSources() {
/* 264 */     return this.fileSources;
/*     */   }
/*     */   
/*     */   public Identifier metadataLocation() {
/* 264 */     return this.metadataLocation;
/*     */   }
/*     */   
/*     */   public Identifier fileLocation() {
/* 264 */     return this.fileLocation;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$EntryStack;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #264	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$EntryStack;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$EntryStack;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #264	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$EntryStack;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$EntryStack;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #264	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$EntryStack;
/*     */   }
/*     */   
/*     */   private EntryStack(Identifier fileLocation, Identifier metadataLocation, List<FallbackResourceManager.ResourceWithSource> fileSources, Map<PackResources, IoSupplier<InputStream>> metaSources) {
/* 264 */     this.fileLocation = fileLocation;
/* 264 */     this.metadataLocation = metadataLocation;
/* 264 */     this.fileSources = fileSources;
/* 264 */     this.metaSources = metaSources;
/*     */   }
/*     */   
/*     */   public EntryStack(Identifier fileLocation) {
/* 266 */     this(fileLocation, 
/*     */         
/* 268 */         FallbackResourceManager.getMetadataLocation(fileLocation), new ArrayList<>(), (Map<PackResources, IoSupplier<InputStream>>)new Object2ObjectArrayMap());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\FallbackResourceManager$EntryStack.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */