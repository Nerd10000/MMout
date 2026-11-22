/*    */ package net.minecraft.data.recipes.packs;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.data.recipes.RecipeOutput;
/*    */ import net.minecraft.data.recipes.RecipeProvider;
/*    */ 
/*    */ public class Runner extends RecipeProvider.Runner {
/*    */   public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
/* 76 */     super(packOutput, registries);
/*    */   }
/*    */   
/*    */   protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
/* 81 */     return new VanillaRecipeProvider(registries, output);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 86 */     return "Vanilla Recipes";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\packs\VanillaRecipeProvider$Runner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */