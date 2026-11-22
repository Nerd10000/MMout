/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.advancements.triggers.Criterion;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.ShapedRecipe;
/*     */ import net.minecraft.world.item.crafting.ShapedRecipePattern;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class ShapedRecipeBuilder implements RecipeBuilder {
/*     */   private final HolderGetter<Item> items;
/*     */   
/*     */   private final RecipeCategory category;
/*     */   
/*     */   private final ItemStackTemplate result;
/*     */   
/*  25 */   private final List<String> rows = Lists.newArrayList();
/*     */   
/*  26 */   private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
/*     */   
/*  27 */   private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
/*     */   
/*     */   private String group;
/*     */   
/*     */   private boolean showNotification = true;
/*     */   
/*     */   private ShapedRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemStackTemplate result) {
/*  32 */     this.items = items;
/*  33 */     this.category = category;
/*  34 */     this.result = result;
/*     */   }
/*     */   
/*     */   private ShapedRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemLike result, int count) {
/*  38 */     this(items, category, new ItemStackTemplate(result.asItem(), count));
/*     */   }
/*     */   
/*     */   public static ShapedRecipeBuilder shaped(HolderGetter<Item> items, RecipeCategory category, ItemLike item) {
/*  42 */     return shaped(items, category, item, 1);
/*     */   }
/*     */   
/*     */   public static ShapedRecipeBuilder shaped(HolderGetter<Item> items, RecipeCategory category, ItemLike item, int count) {
/*  46 */     return new ShapedRecipeBuilder(items, category, item, count);
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder define(Character symbol, TagKey<Item> tag) {
/*  50 */     return define(symbol, Ingredient.of((HolderSet)this.items.getOrThrow(tag)));
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder define(Character symbol, ItemLike item) {
/*  54 */     return define(symbol, Ingredient.of(item));
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder define(Character symbol, Ingredient ingredient) {
/*  58 */     if (this.key.containsKey(symbol))
/*  59 */       throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!"); 
/*  61 */     if (symbol.charValue() == ' ')
/*  62 */       throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined"); 
/*  64 */     this.key.put(symbol, ingredient);
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder pattern(String row) {
/*  69 */     if (!this.rows.isEmpty() && row.length() != ((String)this.rows.get(0)).length())
/*  70 */       throw new IllegalArgumentException("Pattern must be the same width on every line!"); 
/*  72 */     this.rows.add(row);
/*  73 */     return this;
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
/*  78 */     this.advancementBuilder.unlockedBy(name, criterion);
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder group(String group) {
/*  84 */     this.group = group;
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder showNotification(boolean showNotification) {
/*  89 */     this.showNotification = showNotification;
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public ResourceKey<Recipe<?>> defaultId() {
/*  95 */     return RecipeBuilder.getDefaultRecipeId((ItemInstance)this.result);
/*     */   }
/*     */   
/*     */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 100 */     ShapedRecipePattern pattern = ShapedRecipePattern.of(this.key, this.rows);
/* 103 */     ShapedRecipe recipe = new ShapedRecipe(RecipeBuilder.createCraftingCommonInfo(this.showNotification), RecipeBuilder.createCraftingBookInfo(this.category, this.group), pattern, this.result);
/* 107 */     output.accept(id, (Recipe<?>)recipe, this.advancementBuilder.build(output, id, this.category));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\ShapedRecipeBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */