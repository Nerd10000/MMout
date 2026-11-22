/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class SmokingRecipe extends AbstractCookingRecipe {
/* 11 */   public static final MapCodec<SmokingRecipe> MAP_CODEC = cookingMapCodec(SmokingRecipe::new, 100);
/*    */   
/* 13 */   public static final StreamCodec<RegistryFriendlyByteBuf, SmokingRecipe> STREAM_CODEC = cookingStreamCodec(SmokingRecipe::new);
/*    */   
/* 15 */   public static final RecipeSerializer<SmokingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   public SmokingRecipe(Recipe.CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime) {
/* 18 */     super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
/*    */   }
/*    */   
/*    */   protected Item furnaceIcon() {
/* 23 */     return Items.SMOKER;
/*    */   }
/*    */   
/*    */   public RecipeType<SmokingRecipe> getType() {
/* 28 */     return RecipeType.SMOKING;
/*    */   }
/*    */   
/*    */   public RecipeSerializer<SmokingRecipe> getSerializer() {
/* 33 */     return SERIALIZER;
/*    */   }
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 38 */     return RecipeBookCategories.SMOKER_FOOD;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\SmokingRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */