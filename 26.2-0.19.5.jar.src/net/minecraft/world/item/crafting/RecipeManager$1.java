/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ class null implements RecipeManager.CachedCheck<I, T> {
/*     */   private ResourceKey<Recipe<?>> lastRecipe;
/*     */   
/*     */   public Optional<RecipeHolder<T>> getRecipeFor(I input, ServerLevel level) {
/* 219 */     RecipeManager recipeManager = level.recipeAccess();
/* 220 */     Optional<RecipeHolder<T>> result = (Optional)recipeManager.getRecipeFor(type, (RecipeInput)input, (Level)level, this.lastRecipe);
/* 221 */     if (result.isPresent()) {
/* 222 */       RecipeHolder<T> unpackedResult = result.get();
/* 223 */       this.lastRecipe = unpackedResult.id();
/* 224 */       return Optional.of(unpackedResult);
/*     */     } 
/* 226 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\RecipeManager$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */