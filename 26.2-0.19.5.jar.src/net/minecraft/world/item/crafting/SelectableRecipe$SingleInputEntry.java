/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class SingleInputEntry<T extends Recipe<?>> extends Record {
/*    */   private final Ingredient input;
/*    */   
/*    */   private final SelectableRecipe<T> recipe;
/*    */   
/*    */   public SingleInputEntry(Ingredient input, SelectableRecipe<T> recipe) {
/* 21 */     this.input = input;
/* 21 */     this.recipe = recipe;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry<TT;>;
/*    */   }
/*    */   
/*    */   public Ingredient input() {
/* 21 */     return this.input;
/*    */   }
/*    */   
/*    */   public SelectableRecipe<T> recipe() {
/* 21 */     return this.recipe;
/*    */   }
/*    */   
/*    */   public static <T extends Recipe<?>> StreamCodec<RegistryFriendlyByteBuf, SingleInputEntry<T>> noRecipeCodec() {
/* 23 */     return StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, SingleInputEntry::input, 
/*    */         
/* 25 */         SelectableRecipe.noRecipeCodec(), SingleInputEntry::recipe, SingleInputEntry::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\SelectableRecipe$SingleInputEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */