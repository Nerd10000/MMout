/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class SingleItemRecipe implements Recipe<SingleRecipeInput> {
/*    */   protected final Recipe.CommonInfo commonInfo;
/*    */   
/*    */   private final Ingredient input;
/*    */   
/*    */   private final ItemStackTemplate result;
/*    */   
/*    */   private PlacementInfo placementInfo;
/*    */   
/*    */   public SingleItemRecipe(Recipe.CommonInfo commonInfo, Ingredient input, ItemStackTemplate result) {
/* 21 */     this.commonInfo = commonInfo;
/* 22 */     this.input = input;
/* 23 */     this.result = result;
/*    */   }
/*    */   
/*    */   public boolean matches(SingleRecipeInput input, Level level) {
/* 34 */     return this.input.test(input.item());
/*    */   }
/*    */   
/*    */   public boolean showNotification() {
/* 39 */     return this.commonInfo.showNotification();
/*    */   }
/*    */   
/*    */   public Ingredient input() {
/* 43 */     return this.input;
/*    */   }
/*    */   
/*    */   protected ItemStackTemplate result() {
/* 47 */     return this.result;
/*    */   }
/*    */   
/*    */   public PlacementInfo placementInfo() {
/* 52 */     if (this.placementInfo == null)
/* 53 */       this.placementInfo = PlacementInfo.create(this.input); 
/* 55 */     return this.placementInfo;
/*    */   }
/*    */   
/*    */   public ItemStack assemble(SingleRecipeInput input) {
/* 60 */     return this.result.create();
/*    */   }
/*    */   
/*    */   public static <T extends SingleItemRecipe> MapCodec<T> simpleMapCodec(Factory<T> factory) {
/* 64 */     return RecordCodecBuilder.mapCodec(i -> {
/*    */           Objects.requireNonNull(factory);
/*    */           return i.group((App)Recipe.CommonInfo.MAP_CODEC.forGetter(()), (App)Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleItemRecipe::input), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(SingleItemRecipe::result)).apply((Applicative)i, factory::create);
/*    */         });
/*    */   }
/*    */   
/*    */   public static <T extends SingleItemRecipe> StreamCodec<RegistryFriendlyByteBuf, T> simpleStreamCodec(Factory<T> factory) {
/* 76 */     Objects.requireNonNull(factory);
/* 76 */     return StreamCodec.composite(Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo, Ingredient.CONTENTS_STREAM_CODEC, SingleItemRecipe::input, ItemStackTemplate.STREAM_CODEC, SingleItemRecipe::result, factory::create);
/*    */   }
/*    */   
/*    */   public abstract RecipeSerializer<? extends SingleItemRecipe> getSerializer();
/*    */   
/*    */   public abstract RecipeType<? extends SingleItemRecipe> getType();
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Factory<T extends SingleItemRecipe> {
/*    */     T create(Recipe.CommonInfo param1CommonInfo, Ingredient param1Ingredient, ItemStackTemplate param1ItemStackTemplate);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\SingleItemRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */