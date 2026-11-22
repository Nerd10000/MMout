/*     */ package net.minecraft.client.resources.model.sprite;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.client.renderer.texture.SpriteLoader;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ 
/*     */ final class AtlasEntry extends Record implements AutoCloseable {
/*     */   private final TextureAtlas atlas;
/*     */   
/*     */   private final AtlasManager.AtlasConfig config;
/*     */   
/*     */   private AtlasEntry(TextureAtlas atlas, AtlasManager.AtlasConfig config) {
/* 171 */     this.atlas = atlas;
/* 171 */     this.config = config;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasEntry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #171	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasEntry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasEntry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #171	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasEntry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasEntry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #171	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$AtlasEntry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public TextureAtlas atlas() {
/* 171 */     return this.atlas;
/*     */   }
/*     */   
/*     */   public AtlasManager.AtlasConfig config() {
/* 171 */     return this.config;
/*     */   }
/*     */   
/*     */   public void close() {
/* 177 */     this.atlas.close();
/*     */   }
/*     */   
/*     */   private CompletableFuture<SpriteLoader.Preparations> scheduleLoad(ResourceManager resourceManager, Executor executor, int maxMipmapLevels) {
/* 181 */     return SpriteLoader.create(this.atlas)
/* 182 */       .loadAndStitch(resourceManager, this.config.definitionLocation, this.config.createMipmaps ? maxMipmapLevels : 0, executor, this.config.additionalMetadata);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\sprite\AtlasManager$AtlasEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */