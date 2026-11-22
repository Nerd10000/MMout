/*     */ package net.minecraft.client.resources.model.sprite;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.client.renderer.texture.SpriteLoader;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ final class PendingStitch extends Record {
/*     */   private final AtlasManager.AtlasEntry entry;
/*     */   
/*     */   private final CompletableFuture<SpriteLoader.Preparations> preparations;
/*     */   
/*     */   private PendingStitch(AtlasManager.AtlasEntry entry, CompletableFuture<SpriteLoader.Preparations> preparations) {
/* 160 */     this.entry = entry;
/* 160 */     this.preparations = preparations;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$PendingStitch;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #160	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$PendingStitch;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$PendingStitch;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #160	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$PendingStitch;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/AtlasManager$PendingStitch;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #160	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/AtlasManager$PendingStitch;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public AtlasManager.AtlasEntry entry() {
/* 160 */     return this.entry;
/*     */   }
/*     */   
/*     */   public CompletableFuture<SpriteLoader.Preparations> preparations() {
/* 160 */     return this.preparations;
/*     */   }
/*     */   
/*     */   public void joinAndUpload(Map<SpriteId, TextureAtlasSprite> result) {
/* 165 */     SpriteLoader.Preparations preparations = this.preparations.join();
/* 166 */     this.entry.atlas.upload(preparations);
/* 167 */     preparations.regions().forEach((spriteId, spriteContents) -> result.put(new SpriteId(this.entry.config.textureId, spriteId), spriteContents));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\sprite\AtlasManager$PendingStitch.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */