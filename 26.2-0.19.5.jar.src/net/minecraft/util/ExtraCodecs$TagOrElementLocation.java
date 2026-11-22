/*     */ package net.minecraft.util;
/*     */ 
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public final class TagOrElementLocation extends Record {
/*     */   private final Identifier id;
/*     */   
/*     */   private final boolean tag;
/*     */   
/*     */   public TagOrElementLocation(Identifier id, boolean tag) {
/* 598 */     this.id = id;
/* 598 */     this.tag = tag;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ExtraCodecs$TagOrElementLocation;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #598	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/ExtraCodecs$TagOrElementLocation;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/ExtraCodecs$TagOrElementLocation;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #598	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/ExtraCodecs$TagOrElementLocation;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier id() {
/* 598 */     return this.id;
/*     */   }
/*     */   
/*     */   public boolean tag() {
/* 598 */     return this.tag;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 601 */     return decoratedId();
/*     */   }
/*     */   
/*     */   private String decoratedId() {
/* 605 */     return this.tag ? ("#" + String.valueOf(this.id)) : this.id.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ExtraCodecs$TagOrElementLocation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */