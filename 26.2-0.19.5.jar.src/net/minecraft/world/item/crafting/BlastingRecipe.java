/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class BlastingRecipe extends AbstractCookingRecipe {
/* 11 */   public static final MapCodec<BlastingRecipe> MAP_CODEC = cookingMapCodec(BlastingRecipe::new, 100);
/*    */   
/* 13 */   public static final StreamCodec<RegistryFriendlyByteBuf, BlastingRecipe> STREAM_CODEC = cookingStreamCodec(BlastingRecipe::new);
/*    */   
/* 15 */   public static final RecipeSerializer<BlastingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   public BlastingRecipe(Recipe.CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime) {
/* 18 */     super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
/*    */   }
/*    */   
/*    */   protected Item furnaceIcon() {
/* 23 */     return Items.BLAST_FURNACE;
/*    */   }
/*    */   
/*    */   public RecipeSerializer<BlastingRecipe> getSerializer() {
/* 28 */     return SERIALIZER;
/*    */   }
/*    */   
/*    */   public RecipeType<BlastingRecipe> getType() {
/* 33 */     return RecipeType.BLASTING;
/*    */   }
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 38 */     switch (category()) {
/*    */       default:
/* 38 */         throw new MatchException(null, null);
/*    */       case BLOCKS:
/*    */       
/*    */       case FOOD:
/*    */       case MISC:
/*    */         break;
/*    */     } 
/* 38 */     return 
/*    */       
/* 40 */       RecipeBookCategories.BLAST_FURNACE_MISC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\BlastingRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */