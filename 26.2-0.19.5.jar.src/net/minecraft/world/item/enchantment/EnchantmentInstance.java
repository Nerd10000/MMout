/*   */ package net.minecraft.world.item.enchantment;
/*   */ 
/*   */ import net.minecraft.core.Holder;
/*   */ 
/*   */ public final class EnchantmentInstance extends Record {
/*   */   private final Holder<Enchantment> enchantment;
/*   */   
/*   */   private final int level;
/*   */   
/*   */   public EnchantmentInstance(Holder<Enchantment> enchantment, int level) {
/* 5 */     this.enchantment = enchantment;
/* 5 */     this.level = level;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/EnchantmentInstance;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/EnchantmentInstance;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/EnchantmentInstance;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/EnchantmentInstance;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/EnchantmentInstance;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/EnchantmentInstance;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public Holder<Enchantment> enchantment() {
/* 5 */     return this.enchantment;
/*   */   }
/*   */   
/*   */   public int level() {
/* 5 */     return this.level;
/*   */   }
/*   */   
/*   */   public int weight() {
/* 7 */     return ((Enchantment)enchantment().value()).getWeight();
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\EnchantmentInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */