/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*    */ import net.minecraft.world.item.crafting.display.StonecutterRecipeDisplay;
/*    */ 
/*    */ public class StonecutterRecipe extends SingleItemRecipe {
/* 15 */   public static final MapCodec<StonecutterRecipe> MAP_CODEC = simpleMapCodec(StonecutterRecipe::new);
/*    */   
/* 17 */   public static final StreamCodec<RegistryFriendlyByteBuf, StonecutterRecipe> STREAM_CODEC = simpleStreamCodec(StonecutterRecipe::new);
/*    */   
/* 19 */   public static final RecipeSerializer<StonecutterRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   public StonecutterRecipe(Recipe.CommonInfo commonInfo, Ingredient ingredient, ItemStackTemplate result) {
/* 22 */     super(commonInfo, ingredient, result);
/*    */   }
/*    */   
/*    */   public RecipeType<StonecutterRecipe> getType() {
/* 27 */     return RecipeType.STONECUTTING;
/*    */   }
/*    */   
/*    */   public RecipeSerializer<StonecutterRecipe> getSerializer() {
/* 32 */     return SERIALIZER;
/*    */   }
/*    */   
/*    */   public String group() {
/* 37 */     return "";
/*    */   }
/*    */   
/*    */   public List<RecipeDisplay> display() {
/* 42 */     return (List)List.of(new StonecutterRecipeDisplay(
/* 43 */           input().display(), 
/* 44 */           resultDisplay(), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.STONECUTTER)));
/*    */   }
/*    */   
/*    */   public SlotDisplay resultDisplay() {
/* 50 */     return (SlotDisplay)new SlotDisplay.ItemStackSlotDisplay(result());
/*    */   }
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 55 */     return RecipeBookCategories.STONECUTTER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\StonecutterRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */