/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ final class Entry extends Record {
/*    */   private final NonNullList<ItemStack> key;
/*    */   
/*    */   private final int width;
/*    */   
/*    */   private final int height;
/*    */   
/*    */   private final RecipeHolder<CraftingRecipe> value;
/*    */   
/*    */   private Entry(NonNullList<ItemStack> key, int width, int height, RecipeHolder<CraftingRecipe> value) {
/* 72 */     this.key = key;
/* 72 */     this.width = width;
/* 72 */     this.height = height;
/* 72 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/RecipeCache$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #72	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeCache$Entry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/RecipeCache$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #72	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeCache$Entry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/RecipeCache$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #72	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/RecipeCache$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public NonNullList<ItemStack> key() {
/* 72 */     return this.key;
/*    */   }
/*    */   
/*    */   public int width() {
/* 72 */     return this.width;
/*    */   }
/*    */   
/*    */   public int height() {
/* 72 */     return this.height;
/*    */   }
/*    */   
/*    */   public RecipeHolder<CraftingRecipe> value() {
/* 72 */     return this.value;
/*    */   }
/*    */   
/*    */   public boolean matches(CraftingInput input) {
/* 74 */     if (this.width != input.width() || this.height != input.height())
/* 75 */       return false; 
/* 77 */     for (int i = 0; i < this.key.size(); i++) {
/* 78 */       if (!ItemStack.isSameItemSameComponents((ItemStack)this.key.get(i), input.getItem(i)))
/* 79 */         return false; 
/*    */     } 
/* 82 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\RecipeCache$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */