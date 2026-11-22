/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public final class Preparations extends Record {
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final int mipLevel;
/*     */   
/*     */   private final TextureAtlasSprite missing;
/*     */   
/*     */   private final Map<Identifier, TextureAtlasSprite> regions;
/*     */   
/*     */   private final CompletableFuture<Void> readyForUpload;
/*     */   
/*     */   public Preparations(int width, int height, int mipLevel, TextureAtlasSprite missing, Map<Identifier, TextureAtlasSprite> regions, CompletableFuture<Void> readyForUpload) {
/* 127 */     this.width = width;
/* 127 */     this.height = height;
/* 127 */     this.mipLevel = mipLevel;
/* 127 */     this.missing = missing;
/* 127 */     this.regions = regions;
/* 127 */     this.readyForUpload = readyForUpload;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/SpriteLoader$Preparations;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/SpriteLoader$Preparations;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/SpriteLoader$Preparations;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/SpriteLoader$Preparations;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/SpriteLoader$Preparations;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/SpriteLoader$Preparations;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int width() {
/* 127 */     return this.width;
/*     */   }
/*     */   
/*     */   public int height() {
/* 127 */     return this.height;
/*     */   }
/*     */   
/*     */   public int mipLevel() {
/* 127 */     return this.mipLevel;
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite missing() {
/* 127 */     return this.missing;
/*     */   }
/*     */   
/*     */   public Map<Identifier, TextureAtlasSprite> regions() {
/* 127 */     return this.regions;
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> readyForUpload() {
/* 127 */     return this.readyForUpload;
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite getSprite(Identifier id) {
/* 129 */     return this.regions.get(id);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\SpriteLoader$Preparations.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */