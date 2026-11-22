/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ShapedRecipe extends NormalCraftingRecipe {
/*    */   public static final MapCodec<ShapedRecipe> MAP_CODEC;
/*    */   
/*    */   public static final StreamCodec<RegistryFriendlyByteBuf, ShapedRecipe> STREAM_CODEC;
/*    */   
/*    */   static {
/* 20 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Recipe.CommonInfo.MAP_CODEC.forGetter(()), (App)CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(()), (App)ShapedRecipePattern.MAP_CODEC.forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(())).apply((Applicative)i, ShapedRecipe::new));
/* 27 */     STREAM_CODEC = StreamCodec.composite(Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo, CraftingRecipe.CraftingBookInfo.STREAM_CODEC, o -> o.bookInfo, ShapedRecipePattern.STREAM_CODEC, o -> o.pattern, ItemStackTemplate.STREAM_CODEC, o -> o.result, ShapedRecipe::new);
/*    */   }
/*    */   
/* 35 */   public static final RecipeSerializer<ShapedRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   private final ShapedRecipePattern pattern;
/*    */   
/*    */   private final ItemStackTemplate result;
/*    */   
/*    */   public ShapedRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ShapedRecipePattern pattern, ItemStackTemplate result) {
/* 41 */     super(commonInfo, bookInfo);
/* 42 */     this.pattern = pattern;
/* 43 */     this.result = result;
/*    */   }
/*    */   
/*    */   public RecipeSerializer<ShapedRecipe> getSerializer() {
/* 48 */     return SERIALIZER;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public List<Optional<Ingredient>> getIngredients() {
/* 53 */     return this.pattern.ingredients();
/*    */   }
/*    */   
/*    */   protected PlacementInfo createPlacementInfo() {
/* 58 */     return PlacementInfo.createFromOptionals(this.pattern.ingredients());
/*    */   }
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 63 */     return this.pattern.matches(input);
/*    */   }
/*    */   
/*    */   public ItemStack assemble(CraftingInput input) {
/* 68 */     return this.result.create();
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 72 */     return this.pattern.width();
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 76 */     return this.pattern.height();
/*    */   }
/*    */   
/*    */   public List<RecipeDisplay> display() {
/* 81 */     return (List)List.of(new ShapedCraftingRecipeDisplay(this.pattern
/* 82 */           .width(), this.pattern
/* 83 */           .height(), this.pattern
/* 84 */           .ingredients().stream().map(e -> (SlotDisplay)e.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList(), (SlotDisplay)new SlotDisplay.ItemStackSlotDisplay(this.result), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\ShapedRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */