/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class SmeltingRecipe extends AbstractCookingRecipe {
/* 11 */   public static final MapCodec<SmeltingRecipe> MAP_CODEC = cookingMapCodec(SmeltingRecipe::new, 200);
/*    */   
/* 13 */   public static final StreamCodec<RegistryFriendlyByteBuf, SmeltingRecipe> STREAM_CODEC = cookingStreamCodec(SmeltingRecipe::new);
/*    */   
/* 15 */   public static final RecipeSerializer<SmeltingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   public SmeltingRecipe(Recipe.CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime) {
/* 18 */     super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
/*    */   }
/*    */   
/*    */   protected Item furnaceIcon() {
/* 23 */     return Items.FURNACE;
/*    */   }
/*    */   
/*    */   public RecipeSerializer<SmeltingRecipe> getSerializer() {
/* 28 */     return SERIALIZER;
/*    */   }
/*    */   
/*    */   public RecipeType<SmeltingRecipe> getType() {
/* 33 */     return RecipeType.SMELTING;
/*    */   }
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 38 */     switch (category()) {
/*    */       default:
/* 38 */         throw new MatchException(null, null);
/*    */       case BLOCKS:
/*    */       
/*    */       case FOOD:
/*    */       
/*    */       case MISC:
/*    */         break;
/*    */     } 
/* 38 */     return 
/*    */       
/* 41 */       RecipeBookCategories.FURNACE_MISC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\SmeltingRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */