/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ 
/*     */ final class PendingReload extends Record {
/*     */   private final ReloadableTexture texture;
/*     */   
/*     */   private final CompletableFuture<TextureContents> newContents;
/*     */   
/*     */   private PendingReload(ReloadableTexture texture, CompletableFuture<TextureContents> newContents) {
/* 189 */     this.texture = texture;
/* 189 */     this.newContents = newContents;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/TextureManager$PendingReload;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #189	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/TextureManager$PendingReload;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/TextureManager$PendingReload;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #189	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/TextureManager$PendingReload;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/TextureManager$PendingReload;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #189	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/TextureManager$PendingReload;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ReloadableTexture texture() {
/* 189 */     return this.texture;
/*     */   }
/*     */   
/*     */   public CompletableFuture<TextureContents> newContents() {
/* 189 */     return this.newContents;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\TextureManager$PendingReload.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */