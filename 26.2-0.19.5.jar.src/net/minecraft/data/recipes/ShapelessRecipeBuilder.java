/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.advancements.triggers.Criterion;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.ShapelessRecipe;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public class ShapelessRecipeBuilder implements RecipeBuilder {
/*    */   private final HolderGetter<Item> items;
/*    */   
/*    */   private final RecipeCategory category;
/*    */   
/*    */   private final ItemStackTemplate result;
/*    */   
/* 22 */   private final List<Ingredient> ingredients = new ArrayList<>();
/*    */   
/* 23 */   private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
/*    */   
/*    */   private String group;
/*    */   
/*    */   private ShapelessRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemStackTemplate result) {
/* 27 */     this.items = items;
/* 28 */     this.category = category;
/* 29 */     this.result = result;
/*    */   }
/*    */   
/*    */   public static ShapelessRecipeBuilder shapeless(HolderGetter<Item> items, RecipeCategory category, ItemStackTemplate result) {
/* 33 */     return new ShapelessRecipeBuilder(items, category, result);
/*    */   }
/*    */   
/*    */   public static ShapelessRecipeBuilder shapeless(HolderGetter<Item> items, RecipeCategory category, ItemLike item) {
/* 37 */     return shapeless(items, category, item, 1);
/*    */   }
/*    */   
/*    */   public static ShapelessRecipeBuilder shapeless(HolderGetter<Item> items, RecipeCategory category, ItemLike item, int count) {
/* 41 */     return new ShapelessRecipeBuilder(items, category, new ItemStackTemplate(item.asItem(), count));
/*    */   }
/*    */   
/*    */   public ShapelessRecipeBuilder requires(TagKey<Item> tag) {
/* 45 */     return requires(Ingredient.of((HolderSet)this.items.getOrThrow(tag)));
/*    */   }
/*    */   
/*    */   public ShapelessRecipeBuilder requires(ItemLike item) {
/* 49 */     return requires(item, 1);
/*    */   }
/*    */   
/*    */   public ShapelessRecipeBuilder requires(ItemLike item, int count) {
/* 53 */     for (int i = 0; i < count; i++)
/* 54 */       requires(Ingredient.of(item)); 
/* 56 */     return this;
/*    */   }
/*    */   
/*    */   public ShapelessRecipeBuilder requires(Ingredient ingredient) {
/* 60 */     return requires(ingredient, 1);
/*    */   }
/*    */   
/*    */   public ShapelessRecipeBuilder requires(Ingredient ingredient, int count) {
/* 64 */     for (int i = 0; i < count; i++)
/* 65 */       this.ingredients.add(ingredient); 
/* 67 */     return this;
/*    */   }
/*    */   
/*    */   public ShapelessRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
/* 72 */     this.advancementBuilder.unlockedBy(name, criterion);
/* 73 */     return this;
/*    */   }
/*    */   
/*    */   public ShapelessRecipeBuilder group(String group) {
/* 78 */     this.group = group;
/* 79 */     return this;
/*    */   }
/*    */   
/*    */   public ResourceKey<Recipe<?>> defaultId() {
/* 84 */     return RecipeBuilder.getDefaultRecipeId((ItemInstance)this.result);
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 91 */     ShapelessRecipe recipe = new ShapelessRecipe(RecipeBuilder.createCraftingCommonInfo(true), RecipeBuilder.createCraftingBookInfo(this.category, this.group), this.result, this.ingredients);
/* 95 */     output.accept(id, (Recipe<?>)recipe, this.advancementBuilder.build(output, id, this.category));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\ShapelessRecipeBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */