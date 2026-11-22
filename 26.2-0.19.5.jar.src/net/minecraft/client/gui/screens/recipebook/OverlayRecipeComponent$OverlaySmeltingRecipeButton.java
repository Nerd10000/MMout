/*     */ package net.minecraft.client.gui.screens.recipebook;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ 
/*     */ class OverlaySmeltingRecipeButton extends OverlayRecipeComponent.OverlayRecipeButton {
/* 174 */   private static final Identifier ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/furnace_overlay");
/*     */   
/* 175 */   private static final Identifier HIGHLIGHTED_ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/furnace_overlay_highlighted");
/*     */   
/* 176 */   private static final Identifier DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/furnace_overlay_disabled");
/*     */   
/* 177 */   private static final Identifier HIGHLIGHTED_DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/furnace_overlay_disabled_highlighted");
/*     */   
/*     */   public OverlaySmeltingRecipeButton(OverlayRecipeComponent paramOverlayRecipeComponent, int x, int y, RecipeDisplayId id, RecipeDisplay recipe, ContextMap context, boolean isCraftable) {
/* 180 */     super(paramOverlayRecipeComponent, x, y, id, isCraftable, calculateIngredientsPositions(recipe, context));
/*     */   }
/*     */   
/*     */   private static List<OverlayRecipeComponent.OverlayRecipeButton.Pos> calculateIngredientsPositions(RecipeDisplay recipe, ContextMap context) {
/* 184 */     if (recipe instanceof FurnaceRecipeDisplay) {
/* 184 */       FurnaceRecipeDisplay furnaceRecipe = (FurnaceRecipeDisplay)recipe;
/* 185 */       List<ItemStack> items = furnaceRecipe.ingredient().resolveForStacks(context);
/* 186 */       if (!items.isEmpty())
/* 187 */         return List.of(createGridPos(1, 1, items)); 
/*     */     } 
/* 190 */     return List.of();
/*     */   }
/*     */   
/*     */   protected Identifier getSprite(boolean isCraftable) {
/* 195 */     if (isCraftable)
/* 196 */       return isHoveredOrFocused() ? HIGHLIGHTED_ENABLED_SPRITE : ENABLED_SPRITE; 
/* 198 */     return isHoveredOrFocused() ? HIGHLIGHTED_DISABLED_SPRITE : DISABLED_SPRITE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\recipebook\OverlayRecipeComponent$OverlaySmeltingRecipeButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */