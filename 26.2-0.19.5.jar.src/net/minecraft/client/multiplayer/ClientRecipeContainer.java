/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.RecipeAccess;
/*    */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*    */ import net.minecraft.world.item.crafting.SelectableRecipe;
/*    */ import net.minecraft.world.item.crafting.StonecutterRecipe;
/*    */ 
/*    */ public class ClientRecipeContainer implements RecipeAccess {
/*    */   private final Map<ResourceKey<RecipePropertySet>, RecipePropertySet> itemSets;
/*    */   
/*    */   private final SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes;
/*    */   
/*    */   public ClientRecipeContainer(Map<ResourceKey<RecipePropertySet>, RecipePropertySet> itemSets, SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes) {
/* 16 */     this.itemSets = itemSets;
/* 17 */     this.stonecutterRecipes = stonecutterRecipes;
/*    */   }
/*    */   
/*    */   public RecipePropertySet propertySet(ResourceKey<RecipePropertySet> id) {
/* 22 */     return this.itemSets.getOrDefault(id, RecipePropertySet.EMPTY);
/*    */   }
/*    */   
/*    */   public SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes() {
/* 27 */     return this.stonecutterRecipes;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientRecipeContainer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */