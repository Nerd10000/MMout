/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ public class IngredientCollector implements Consumer<Recipe<?>> {
/*     */   private final ResourceKey<RecipePropertySet> key;
/*     */   
/*     */   private final RecipeManager.IngredientExtractor extractor;
/*     */   
/* 286 */   private final List<Ingredient> ingredients = new ArrayList<>();
/*     */   
/*     */   protected IngredientCollector(ResourceKey<RecipePropertySet> key, RecipeManager.IngredientExtractor extractor) {
/* 289 */     this.key = key;
/* 290 */     this.extractor = extractor;
/*     */   }
/*     */   
/*     */   public void accept(Recipe<?> recipe) {
/* 295 */     Objects.requireNonNull(this.ingredients);
/* 295 */     this.extractor.apply(recipe).ifPresent(this.ingredients::add);
/*     */   }
/*     */   
/*     */   public RecipePropertySet asPropertySet(FeatureFlagSet enabledFeatures) {
/* 299 */     return RecipePropertySet.create(RecipeManager.filterDisabled(enabledFeatures, this.ingredients));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\RecipeManager$IngredientCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */