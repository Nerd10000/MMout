/*     */ package net.minecraft.client.model.object.book;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class State extends Record {
/*     */   private final float openness;
/*     */   
/*     */   private final float pageFlip1;
/*     */   
/*     */   private final float pageFlip2;
/*     */   
/*     */   public State(float openness, float pageFlip1, float pageFlip2) {
/*  96 */     this.openness = openness;
/*  96 */     this.pageFlip1 = pageFlip1;
/*  96 */     this.pageFlip2 = pageFlip2;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/object/book/BookModel$State;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #96	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/model/object/book/BookModel$State;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/object/book/BookModel$State;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #96	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/model/object/book/BookModel$State;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/object/book/BookModel$State;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #96	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/model/object/book/BookModel$State;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float openness() {
/*  96 */     return this.openness;
/*     */   }
/*     */   
/*     */   public float pageFlip1() {
/*  96 */     return this.pageFlip1;
/*     */   }
/*     */   
/*     */   public float pageFlip2() {
/*  96 */     return this.pageFlip2;
/*     */   }
/*     */   
/*     */   public static State forAnimation(float progress, float pageFlip1, float pageFlip2, float openness) {
/* 102 */     return new State((
/* 103 */         Mth.sin((progress * 0.02F)) * 0.1F + 1.25F) * openness, pageFlip1, pageFlip2);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\object\book\BookModel$State.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */