/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ShapelessRecipe extends NormalCraftingRecipe {
/*    */   public static final MapCodec<ShapelessRecipe> MAP_CODEC;
/*    */   
/*    */   public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> STREAM_CODEC;
/*    */   
/*    */   static {
/* 19 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Recipe.CommonInfo.MAP_CODEC.forGetter(()), (App)CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(()), (App)Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(())).apply((Applicative)i, ShapelessRecipe::new));
/* 26 */     STREAM_CODEC = StreamCodec.composite(Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo, CraftingRecipe.CraftingBookInfo.STREAM_CODEC, o -> o.bookInfo, ItemStackTemplate.STREAM_CODEC, o -> o.result, Ingredient.CONTENTS_STREAM_CODEC
/*    */         
/* 30 */         .apply(ByteBufCodecs.list()), o -> o.ingredients, ShapelessRecipe::new);
/*    */   }
/*    */   
/* 34 */   public static final RecipeSerializer<ShapelessRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   private final ItemStackTemplate result;
/*    */   
/*    */   private final List<Ingredient> ingredients;
/*    */   
/*    */   public ShapelessRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ItemStackTemplate result, List<Ingredient> ingredients) {
/* 40 */     super(commonInfo, bookInfo);
/* 41 */     this.result = result;
/* 42 */     this.ingredients = ingredients;
/*    */   }
/*    */   
/*    */   public RecipeSerializer<ShapelessRecipe> getSerializer() {
/* 47 */     return SERIALIZER;
/*    */   }
/*    */   
/*    */   protected PlacementInfo createPlacementInfo() {
/* 52 */     return PlacementInfo.create(this.ingredients);
/*    */   }
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 57 */     if (input.ingredientCount() != this.ingredients.size())
/* 58 */       return false; 
/* 60 */     if (input.size() == 1 && this.ingredients.size() == 1)
/* 61 */       return ((Ingredient)this.ingredients.getFirst()).test(input.getItem(0)); 
/* 63 */     return input.stackedContents().canCraft(this, null);
/*    */   }
/*    */   
/*    */   public ItemStack assemble(CraftingInput input) {
/* 68 */     return this.result.create();
/*    */   }
/*    */   
/*    */   public List<RecipeDisplay> display() {
/* 73 */     return (List)List.of(new ShapelessCraftingRecipeDisplay(this.ingredients
/* 74 */           .stream().map(Ingredient::display).toList(), (SlotDisplay)new SlotDisplay.ItemStackSlotDisplay(this.result), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\ShapelessRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */