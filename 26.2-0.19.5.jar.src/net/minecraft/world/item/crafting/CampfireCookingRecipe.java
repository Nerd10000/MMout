/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class CampfireCookingRecipe extends AbstractCookingRecipe {
/* 11 */   public static final MapCodec<CampfireCookingRecipe> MAP_CODEC = cookingMapCodec(CampfireCookingRecipe::new, 100);
/*    */   
/* 13 */   public static final StreamCodec<RegistryFriendlyByteBuf, CampfireCookingRecipe> STREAM_CODEC = cookingStreamCodec(CampfireCookingRecipe::new);
/*    */   
/* 15 */   public static final RecipeSerializer<CampfireCookingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   public CampfireCookingRecipe(Recipe.CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime) {
/* 18 */     super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
/*    */   }
/*    */   
/*    */   protected Item furnaceIcon() {
/* 23 */     return Items.CAMPFIRE;
/*    */   }
/*    */   
/*    */   public RecipeSerializer<CampfireCookingRecipe> getSerializer() {
/* 28 */     return SERIALIZER;
/*    */   }
/*    */   
/*    */   public RecipeType<CampfireCookingRecipe> getType() {
/* 33 */     return RecipeType.CAMPFIRE_COOKING;
/*    */   }
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 38 */     return RecipeBookCategories.CAMPFIRE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\CampfireCookingRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */