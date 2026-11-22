/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class SmithingRecipeInput extends Record implements RecipeInput {
/*    */   private final ItemStack template;
/*    */   
/*    */   private final ItemStack base;
/*    */   
/*    */   private final ItemStack addition;
/*    */   
/*    */   public SmithingRecipeInput(ItemStack template, ItemStack base, ItemStack addition) {
/*  6 */     this.template = template;
/*  6 */     this.base = base;
/*  6 */     this.addition = addition;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/SmithingRecipeInput;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SmithingRecipeInput;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/SmithingRecipeInput;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SmithingRecipeInput;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/SmithingRecipeInput;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/SmithingRecipeInput;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ItemStack template() {
/*  6 */     return this.template;
/*    */   }
/*    */   
/*    */   public ItemStack base() {
/*  6 */     return this.base;
/*    */   }
/*    */   
/*    */   public ItemStack addition() {
/*  6 */     return this.addition;
/*    */   }
/*    */   
/*    */   public ItemStack getItem(int index) {
/*  9 */     switch (index) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */     } 
/* 13 */     throw new IllegalArgumentException("Recipe does not contain slot " + index);
/*    */   }
/*    */   
/*    */   public int size() {
/* 19 */     return 3;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 24 */     return (this.template.isEmpty() && this.base.isEmpty() && this.addition.isEmpty());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\SmithingRecipeInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */