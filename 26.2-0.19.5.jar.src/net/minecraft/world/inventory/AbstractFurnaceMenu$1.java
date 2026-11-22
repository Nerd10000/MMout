/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.recipebook.ServerPlaceRecipe;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.SingleRecipeInput;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ class null implements ServerPlaceRecipe.CraftingMenuAccess<AbstractCookingRecipe> {
/*     */   public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
/* 179 */     AbstractFurnaceMenu.this.fillCraftSlotsStackedContents(stackedContents);
/*     */   }
/*     */   
/*     */   public void clearCraftingContent() {
/* 184 */     slotsToClear.forEach(s -> s.set(ItemStack.EMPTY));
/*     */   }
/*     */   
/*     */   public boolean recipeMatches(RecipeHolder<AbstractCookingRecipe> recipe) {
/* 189 */     return ((AbstractCookingRecipe)recipe.value()).matches(new SingleRecipeInput(AbstractFurnaceMenu.this.container.getItem(0)), (Level)level);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\AbstractFurnaceMenu$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */