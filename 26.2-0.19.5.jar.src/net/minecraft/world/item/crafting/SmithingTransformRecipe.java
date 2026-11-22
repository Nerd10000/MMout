/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
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
/*    */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*    */ import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;
/*    */ 
/*    */ public class SmithingTransformRecipe extends SimpleSmithingRecipe {
/*    */   public static final MapCodec<SmithingTransformRecipe> MAP_CODEC;
/*    */   
/*    */   public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTransformRecipe> STREAM_CODEC;
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Recipe.CommonInfo.MAP_CODEC.forGetter(()), (App)Ingredient.CODEC.optionalFieldOf("template").forGetter(()), (App)Ingredient.CODEC.fieldOf("base").forGetter(()), (App)Ingredient.CODEC.optionalFieldOf("addition").forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(())).apply((Applicative)i, SmithingTransformRecipe::new));
/* 26 */     STREAM_CODEC = StreamCodec.composite(Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo, Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, o -> o.template, Ingredient.CONTENTS_STREAM_CODEC, o -> o.base, Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, o -> o.addition, ItemStackTemplate.STREAM_CODEC, o -> o.result, SmithingTransformRecipe::new);
/*    */   }
/*    */   
/* 35 */   public static final RecipeSerializer<SmithingTransformRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   private final Optional<Ingredient> template;
/*    */   
/*    */   private final Ingredient base;
/*    */   
/*    */   private final Optional<Ingredient> addition;
/*    */   
/*    */   private final ItemStackTemplate result;
/*    */   
/*    */   public SmithingTransformRecipe(Recipe.CommonInfo commonInfo, Optional<Ingredient> template, Ingredient base, Optional<Ingredient> addition, ItemStackTemplate result) {
/* 43 */     super(commonInfo);
/* 44 */     this.template = template;
/* 45 */     this.base = base;
/* 46 */     this.addition = addition;
/* 47 */     this.result = result;
/*    */   }
/*    */   
/*    */   public ItemStack assemble(SmithingRecipeInput input) {
/* 52 */     return TransmuteRecipe.createWithOriginalComponents(this.result, input.base());
/*    */   }
/*    */   
/*    */   public Optional<Ingredient> templateIngredient() {
/* 57 */     return this.template;
/*    */   }
/*    */   
/*    */   public Ingredient baseIngredient() {
/* 62 */     return this.base;
/*    */   }
/*    */   
/*    */   public Optional<Ingredient> additionIngredient() {
/* 67 */     return this.addition;
/*    */   }
/*    */   
/*    */   public RecipeSerializer<SmithingTransformRecipe> getSerializer() {
/* 72 */     return SERIALIZER;
/*    */   }
/*    */   
/*    */   protected PlacementInfo createPlacementInfo() {
/* 77 */     return PlacementInfo.createFromOptionals(List.of(this.template, Optional.of(this.base), this.addition));
/*    */   }
/*    */   
/*    */   public List<RecipeDisplay> display() {
/* 83 */     return (List)List.of(new SmithingRecipeDisplay(
/*    */           
/* 85 */           Ingredient.optionalIngredientToDisplay(this.template), this.base
/* 86 */           .display(), 
/* 87 */           Ingredient.optionalIngredientToDisplay(this.addition), (SlotDisplay)new SlotDisplay.ItemStackSlotDisplay(this.result), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.SMITHING_TABLE)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\SmithingTransformRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */