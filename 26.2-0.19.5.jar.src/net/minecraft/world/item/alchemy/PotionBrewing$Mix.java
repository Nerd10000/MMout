/*     */ package net.minecraft.world.item.alchemy;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ 
/*     */ final class Mix<T> extends Record {
/*     */   private final Holder<T> from;
/*     */   
/*     */   private final Ingredient ingredient;
/*     */   
/*     */   private final Holder<T> to;
/*     */   
/*     */   private Mix(Holder<T> from, Ingredient ingredient, Holder<T> to) {
/* 282 */     this.from = from;
/* 282 */     this.ingredient = ingredient;
/* 282 */     this.to = to;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #282	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #282	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #282	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/alchemy/PotionBrewing$Mix<TT;>;
/*     */   }
/*     */   
/*     */   public Holder<T> from() {
/* 282 */     return this.from;
/*     */   }
/*     */   
/*     */   public Ingredient ingredient() {
/* 282 */     return this.ingredient;
/*     */   }
/*     */   
/*     */   public Holder<T> to() {
/* 282 */     return this.to;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\alchemy\PotionBrewing$Mix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */