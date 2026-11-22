/*     */ package net.minecraft.client.renderer.block.dispatch.multipart;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ 
/*     */ final class Key extends Record {
/*     */   private final MultiPartModel.Unbaked model;
/*     */   
/*     */   private final IntList selectors;
/*     */   
/*     */   Key(MultiPartModel.Unbaked model, IntList selectors) {
/* 132 */     this.model = model;
/* 132 */     this.selectors = selectors;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #132	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #132	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #132	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Unbaked$1Key;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public MultiPartModel.Unbaked model() {
/* 132 */     return this.model;
/*     */   }
/*     */   
/*     */   public IntList selectors() {
/* 132 */     return this.selectors;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\multipart\MultiPartModel$Unbaked$1Key.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */