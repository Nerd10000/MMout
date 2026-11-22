/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ 
/*    */ public interface CraftingRecipe extends Recipe<CraftingInput> {
/*    */   default RecipeType<CraftingRecipe> getType() {
/* 14 */     return RecipeType.CRAFTING;
/*    */   }
/*    */   
/*    */   RecipeSerializer<? extends CraftingRecipe> getSerializer();
/*    */   
/*    */   CraftingBookCategory category();
/*    */   
/*    */   default NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
/* 24 */     return defaultCraftingReminder(input);
/*    */   }
/*    */   
/*    */   static NonNullList<ItemStack> defaultCraftingReminder(CraftingInput input) {
/* 28 */     NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);
/* 30 */     for (int slot = 0; slot < result.size(); slot++) {
/* 31 */       Item item = input.getItem(slot).getItem();
/* 32 */       ItemStackTemplate remainder = item.getCraftingRemainder();
/* 33 */       result.set(slot, (remainder != null) ? remainder.create() : ItemStack.EMPTY);
/*    */     } 
/* 36 */     return result;
/*    */   }
/*    */   
/*    */   default RecipeBookCategory recipeBookCategory() {
/* 41 */     switch (category()) {
/*    */       default:
/* 41 */         throw new MatchException(null, null);
/*    */       case BUILDING:
/*    */       
/*    */       case EQUIPMENT:
/*    */       
/*    */       case REDSTONE:
/*    */       
/*    */       case MISC:
/*    */         break;
/*    */     } 
/* 41 */     return 
/*    */       
/* 45 */       RecipeBookCategories.CRAFTING_MISC;
/*    */   }
/*    */   
/*    */   public static final class CraftingBookInfo extends Record implements Recipe.BookInfo<CraftingBookCategory> {
/*    */     private final CraftingBookCategory category;
/*    */     
/*    */     private final String group;
/*    */     
/*    */     public CraftingBookInfo(CraftingBookCategory category, String group) {
/* 49 */       this.category = category;
/* 49 */       this.group = group;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/CraftingRecipe$CraftingBookInfo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #49	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/CraftingRecipe$CraftingBookInfo;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/CraftingRecipe$CraftingBookInfo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #49	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/CraftingRecipe$CraftingBookInfo;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/CraftingRecipe$CraftingBookInfo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #49	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/CraftingRecipe$CraftingBookInfo;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public CraftingBookCategory category() {
/* 49 */       return this.category;
/*    */     }
/*    */     
/*    */     public String group() {
/* 49 */       return this.group;
/*    */     }
/*    */     
/* 53 */     public static final MapCodec<CraftingBookInfo> MAP_CODEC = Recipe.BookInfo.mapCodec(CraftingBookCategory.CODEC, CraftingBookCategory.MISC, CraftingBookInfo::new);
/*    */     
/* 55 */     public static final StreamCodec<RegistryFriendlyByteBuf, CraftingBookInfo> STREAM_CODEC = Recipe.BookInfo.streamCodec((StreamCodec)CraftingBookCategory.STREAM_CODEC, CraftingBookInfo::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\CraftingRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */