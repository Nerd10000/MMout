/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.PotDecorations;
/*    */ 
/*    */ public class DecoratedPotRecipe extends CustomRecipe {
/*    */   public static final MapCodec<DecoratedPotRecipe> MAP_CODEC;
/*    */   
/*    */   public static final StreamCodec<RegistryFriendlyByteBuf, DecoratedPotRecipe> STREAM_CODEC;
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Ingredient.CODEC.fieldOf("back").forGetter(()), (App)Ingredient.CODEC.fieldOf("left").forGetter(()), (App)Ingredient.CODEC.fieldOf("right").forGetter(()), (App)Ingredient.CODEC.fieldOf("front").forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(())).apply((Applicative)i, DecoratedPotRecipe::new));
/* 23 */     STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, o -> o.backPattern, Ingredient.CONTENTS_STREAM_CODEC, o -> o.leftPattern, Ingredient.CONTENTS_STREAM_CODEC, o -> o.rightPattern, Ingredient.CONTENTS_STREAM_CODEC, o -> o.frontPattern, ItemStackTemplate.STREAM_CODEC, o -> o.result, DecoratedPotRecipe::new);
/*    */   }
/*    */   
/* 32 */   public static final RecipeSerializer<DecoratedPotRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   private final Ingredient backPattern;
/*    */   
/*    */   private final Ingredient leftPattern;
/*    */   
/*    */   private final Ingredient rightPattern;
/*    */   
/*    */   private final Ingredient frontPattern;
/*    */   
/*    */   private final ItemStackTemplate result;
/*    */   
/*    */   public DecoratedPotRecipe(Ingredient wallPattern, ItemStackTemplate result) {
/* 42 */     this(wallPattern, wallPattern, wallPattern, wallPattern, result);
/*    */   }
/*    */   
/*    */   public DecoratedPotRecipe(Ingredient backPattern, Ingredient leftPattern, Ingredient rightPattern, Ingredient frontPattern, ItemStackTemplate result) {
/* 46 */     this.backPattern = backPattern;
/* 47 */     this.leftPattern = leftPattern;
/* 48 */     this.rightPattern = rightPattern;
/* 49 */     this.frontPattern = frontPattern;
/* 50 */     this.result = result;
/*    */   }
/*    */   
/*    */   private static ItemStack back(CraftingInput input) {
/* 54 */     return input.getItem(1, 0);
/*    */   }
/*    */   
/*    */   private static ItemStack left(CraftingInput input) {
/* 58 */     return input.getItem(0, 1);
/*    */   }
/*    */   
/*    */   private static ItemStack right(CraftingInput input) {
/* 62 */     return input.getItem(2, 1);
/*    */   }
/*    */   
/*    */   private static ItemStack front(CraftingInput input) {
/* 66 */     return input.getItem(1, 2);
/*    */   }
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 72 */     if (input.width() != 3 || input.height() != 3 || input.ingredientCount() != 4)
/* 73 */       return false; 
/* 77 */     return (this.backPattern.test(back(input)) && this.leftPattern
/* 78 */       .test(left(input)) && this.rightPattern
/* 79 */       .test(right(input)) && this.frontPattern
/* 80 */       .test(front(input)));
/*    */   }
/*    */   
/*    */   public ItemStack assemble(CraftingInput input) {
/* 89 */     PotDecorations decorations = new PotDecorations(back(input).getItem(), left(input).getItem(), right(input).getItem(), front(input).getItem());
/* 93 */     DataComponentPatch components = DataComponentPatch.builder().set(DataComponents.POT_DECORATIONS, decorations).build();
/* 94 */     return this.result.apply(components);
/*    */   }
/*    */   
/*    */   public RecipeSerializer<DecoratedPotRecipe> getSerializer() {
/* 99 */     return SERIALIZER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\DecoratedPotRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */