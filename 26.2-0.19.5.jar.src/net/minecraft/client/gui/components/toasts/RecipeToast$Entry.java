/*    */ package net.minecraft.client.gui.components.toasts;
/*    */ 
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ final class Entry extends Record {
/*    */   private final ItemStack categoryItem;
/*    */   
/*    */   private final ItemStack unlockedItem;
/*    */   
/*    */   private Entry(ItemStack categoryItem, ItemStack unlockedItem) {
/* 89 */     this.categoryItem = categoryItem;
/* 89 */     this.unlockedItem = unlockedItem;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/toasts/RecipeToast$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #89	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/components/toasts/RecipeToast$Entry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/toasts/RecipeToast$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #89	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/components/toasts/RecipeToast$Entry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/toasts/RecipeToast$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #89	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/components/toasts/RecipeToast$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ItemStack categoryItem() {
/* 89 */     return this.categoryItem;
/*    */   }
/*    */   
/*    */   public ItemStack unlockedItem() {
/* 89 */     return this.unlockedItem;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\toasts\RecipeToast$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */