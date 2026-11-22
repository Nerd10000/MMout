/*     */ package net.minecraft.client.resources.model.sprite;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*     */ 
/*     */ public final class AtlasConfig extends Record {
/*     */   private final Identifier textureId;
/*     */   
/*     */   private final Identifier definitionLocation;
/*     */   
/*     */   private final boolean createMipmaps;
/*     */   
/*     */   private final Set<MetadataSectionType<?>> additionalMetadata;
/*     */   
/*     */   public Set<MetadataSectionType<?>> additionalMetadata() {
/* 186 */     return this.additionalMetadata;
/*     */   }
/*     */   
/*     */   public boolean createMipmaps() {
/* 186 */     return this.createMipmaps;
/*     */   }
/*     */   
/*     */   public Identifier definitionLocation() {
/* 186 */     return this.definitionLocation;
/*     */   }
/*     */   
/*     */   public Identifier textureId() {
/* 186 */     return this.textureId;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasConfig;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #186	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasConfig;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasConfig;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #186	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasConfig;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasConfig;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #186	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasConfig;
/*     */   }
/*     */   
/*     */   public AtlasConfig(Identifier textureId, Identifier definitionLocation, boolean createMipmaps, Set<MetadataSectionType<?>> additionalMetadata) {
/* 186 */     this.textureId = textureId;
/* 186 */     this.definitionLocation = definitionLocation;
/* 186 */     this.createMipmaps = createMipmaps;
/* 186 */     this.additionalMetadata = additionalMetadata;
/*     */   }
/*     */   
/*     */   public AtlasConfig(Identifier textureId, Identifier definitionLocation, boolean createMipmaps) {
/* 193 */     this(textureId, definitionLocation, createMipmaps, Set.of());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\sprite\AtlasManager$AtlasConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */