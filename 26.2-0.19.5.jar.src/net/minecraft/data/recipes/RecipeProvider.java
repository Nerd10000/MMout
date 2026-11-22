/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.CriterionTriggerInstance;
/*     */ import net.minecraft.advancements.predicates.ItemPredicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.advancements.triggers.BredAnimalsTrigger;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.advancements.triggers.Criterion;
/*     */ import net.minecraft.advancements.triggers.EnterBlockTrigger;
/*     */ import net.minecraft.advancements.triggers.ImpossibleTrigger;
/*     */ import net.minecraft.advancements.triggers.InventoryChangeTrigger;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.component.DataComponentPatch;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.BlockFamilies;
/*     */ import net.minecraft.data.BlockFamily;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.BannerDuplicateRecipe;
/*     */ import net.minecraft.world.item.crafting.CookingBookCategory;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.DyeRecipe;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.SuspiciousEffectHolder;
/*     */ 
/*     */ public abstract class RecipeProvider {
/*     */   protected final HolderLookup.Provider registries;
/*     */   
/*     */   private final HolderGetter<Item> items;
/*     */   
/*     */   protected final RecipeOutput output;
/*     */   
/*     */   private static final Map<BlockFamily.Variant, FamilyCraftingRecipeProvider> SHAPE_BUILDERS;
/*     */   
/*     */   private static final Map<BlockFamily.Variant, FamilyStonecutterRecipeProvider> STONECUTTER_RECIPE_BUILDERS;
/*     */   
/*     */   protected RecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
/*  75 */     this.registries = registries;
/*  76 */     this.items = (HolderGetter<Item>)registries.lookupOrThrow(Registries.ITEM);
/*  77 */     this.output = output;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface FamilyCraftingRecipeProvider {
/*     */     RecipeBuilder create(RecipeProvider param1RecipeProvider, ItemLike param1ItemLike1, ItemLike param1ItemLike2);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface FamilyStonecutterRecipeProvider {
/*     */     void create(RecipeProvider param1RecipeProvider, ItemLike param1ItemLike1, ItemLike param1ItemLike2);
/*     */   }
/*     */   
/*     */   protected static abstract class Runner implements DataProvider {
/*     */     private final PackOutput packOutput;
/*     */     
/*     */     private final CompletableFuture<HolderLookup.Provider> registries;
/*     */     
/*     */     protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
/*  85 */       this.packOutput = packOutput;
/*  86 */       this.registries = registries;
/*     */     }
/*     */     
/*     */     public final CompletableFuture<?> run(final CachedOutput cache) {
/*  91 */       return this.registries.thenCompose(registries -> {
/*     */             final PackOutput.PathProvider recipePathProvider = this.packOutput.createRegistryElementsPathProvider(Registries.RECIPE);
/*     */             final PackOutput.PathProvider advancementPathProvider = this.packOutput.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
/*     */             final Set<ResourceKey<Recipe<?>>> allRecipes = Sets.newHashSet();
/*     */             final List<CompletableFuture<?>> tasks = new ArrayList<>();
/*     */             RecipeOutput recipeOutput = new RecipeOutput(this) {
/*     */                 public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, AdvancementHolder advancementHolder) {
/* 101 */                   if (!allRecipes.add(id))
/* 102 */                     throw new IllegalStateException("Duplicate recipe " + String.valueOf(id.identifier())); 
/* 104 */                   saveRecipe(id, recipe);
/* 105 */                   if (advancementHolder != null)
/* 106 */                     saveAdvancement(advancementHolder); 
/*     */                 }
/*     */                 
/*     */                 public Advancement.Builder advancement() {
/* 112 */                   return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
/*     */                 }
/*     */                 
/*     */                 public void includeRootAdvancement() {
/* 117 */                   AdvancementHolder root = Advancement.Builder.recipeAdvancement().addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion((CriterionTriggerInstance)new ImpossibleTrigger.TriggerInstance())).build(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
/* 118 */                   saveAdvancement(root);
/*     */                 }
/*     */                 
/*     */                 private void saveRecipe(ResourceKey<Recipe<?>> id, Recipe<?> recipe) {
/* 122 */                   tasks.add(DataProvider.saveStable(cache, registries, Recipe.CODEC, recipe, recipePathProvider.json(id.identifier())));
/*     */                 }
/*     */                 
/*     */                 private void saveAdvancement(AdvancementHolder advancementHolder) {
/* 126 */                   tasks.add(DataProvider.saveStable(cache, registries, Advancement.CODEC, advancementHolder.value(), advancementPathProvider.json(advancementHolder.id())));
/*     */                 }
/*     */               };
/*     */             createRecipeProvider(registries, recipeOutput).buildRecipes();
/*     */             return CompletableFuture.allOf((CompletableFuture<?>[])tasks.toArray(()));
/*     */           });
/*     */     }
/*     */     
/*     */     protected abstract RecipeProvider createRecipeProvider(HolderLookup.Provider param1Provider, RecipeOutput param1RecipeOutput);
/*     */   }
/*     */   
/*     */   class null implements RecipeOutput {
/*     */     null(RecipeProvider.Runner this$0) {}
/*     */     
/*     */     public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, AdvancementHolder advancementHolder) {
/*     */       if (!allRecipes.add(id))
/*     */         throw new IllegalStateException("Duplicate recipe " + String.valueOf(id.identifier())); 
/*     */       saveRecipe(id, recipe);
/*     */       if (advancementHolder != null)
/*     */         saveAdvancement(advancementHolder); 
/*     */     }
/*     */     
/*     */     public Advancement.Builder advancement() {
/*     */       return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
/*     */     }
/*     */     
/*     */     public void includeRootAdvancement() {
/*     */       AdvancementHolder root = Advancement.Builder.recipeAdvancement().addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion((CriterionTriggerInstance)new ImpossibleTrigger.TriggerInstance())).build(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
/*     */       saveAdvancement(root);
/*     */     }
/*     */     
/*     */     private void saveRecipe(ResourceKey<Recipe<?>> id, Recipe<?> recipe) {
/*     */       tasks.add(DataProvider.saveStable(cache, registries, Recipe.CODEC, recipe, recipePathProvider.json(id.identifier())));
/*     */     }
/*     */     
/*     */     private void saveAdvancement(AdvancementHolder advancementHolder) {
/* 126 */       tasks.add(DataProvider.saveStable(cache, registries, Advancement.CODEC, advancementHolder.value(), advancementPathProvider.json(advancementHolder.id())));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void generateForEnabledBlockFamilies(FeatureFlagSet flagSet) {
/* 141 */     BlockFamilies.getAllFamilies()
/* 142 */       .forEach(family -> generateRecipes(family, flagSet));
/*     */   }
/*     */   
/*     */   protected void oneToOneConversionRecipe(ItemLike product, ItemLike resource, String group) {
/* 146 */     oneToOneConversionRecipe(product, resource, group, 1);
/*     */   }
/*     */   
/*     */   protected void oneToOneConversionRecipe(ItemLike product, ItemLike resource, String group, int productCount) {
/* 150 */     shapeless(RecipeCategory.MISC, product, productCount)
/* 151 */       .requires(resource)
/* 152 */       .group(group)
/* 153 */       .unlockedBy(getHasName(resource), has(resource))
/* 154 */       .save(this.output, getConversionRecipeName(product, resource));
/*     */   }
/*     */   
/*     */   protected void oreSmelting(List<ItemLike> smeltables, RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, float experience, int cookingTime, String group) {
/* 158 */     oreCooking(net.minecraft.world.item.crafting.SmeltingRecipe::new, smeltables, craftingCategory, cookingCategory, result, experience, cookingTime, group, "_from_smelting");
/*     */   }
/*     */   
/*     */   protected void oreBlasting(List<ItemLike> smeltables, RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, float experience, int cookingTime, String group) {
/* 162 */     oreCooking(net.minecraft.world.item.crafting.BlastingRecipe::new, smeltables, craftingCategory, cookingCategory, result, experience, cookingTime, group, "_from_blasting");
/*     */   }
/*     */   
/*     */   private <T extends AbstractCookingRecipe> void oreCooking(AbstractCookingRecipe.Factory<T> factory, List<ItemLike> smeltables, RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, float experience, int cookingTime, String group, String fromDesc) {
/* 166 */     for (ItemLike item : smeltables)
/* 167 */       SimpleCookingRecipeBuilder.<T>generic(Ingredient.of(item), craftingCategory, cookingCategory, result, experience, cookingTime, factory)
/* 168 */         .group(group)
/* 169 */         .unlockedBy(getHasName(item), has(item))
/* 170 */         .save(this.output, getItemName(result) + getItemName(result) + "_" + fromDesc); 
/*     */   }
/*     */   
/*     */   protected void netheriteSmithing(Item base, RecipeCategory category, Item result) {
/* 175 */     SmithingTransformRecipeBuilder.smithing(Ingredient.of((ItemLike)Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of((ItemLike)base), tag(ItemTags.NETHERITE_TOOL_MATERIALS), category, result)
/* 176 */       .unlocks("has_netherite_ingot", has(ItemTags.NETHERITE_TOOL_MATERIALS))
/* 177 */       .save(this.output, getItemName((ItemLike)result) + "_smithing");
/*     */   }
/*     */   
/*     */   protected void trimSmithing(Item trimTemplate, ResourceKey<TrimPattern> patternId, ResourceKey<Recipe<?>> id) {
/* 181 */     Holder.Reference<TrimPattern> pattern = this.registries.lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(patternId);
/* 182 */     SmithingTrimRecipeBuilder.smithingTrim(Ingredient.of((ItemLike)trimTemplate), tag(ItemTags.TRIMMABLE_ARMOR), tag(ItemTags.TRIM_MATERIALS), (Holder<TrimPattern>)pattern, RecipeCategory.MISC)
/* 183 */       .unlocks("has_smithing_trim_template", has((ItemLike)trimTemplate))
/* 184 */       .save(this.output, id);
/*     */   }
/*     */   
/*     */   protected void twoByTwoPacker(RecipeCategory category, ItemLike result, ItemLike ingredient) {
/* 188 */     shaped(category, result, 1)
/* 189 */       .define(Character.valueOf('#'), ingredient)
/* 190 */       .pattern("##")
/* 191 */       .pattern("##")
/* 192 */       .unlockedBy(getHasName(ingredient), has(ingredient))
/* 193 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void threeByThreePacker(RecipeCategory category, ItemLike result, ItemLike ingredient, String unlockedBy) {
/* 197 */     shapeless(category, result)
/* 198 */       .requires(ingredient, 9)
/* 199 */       .unlockedBy(unlockedBy, has(ingredient))
/* 200 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void threeByThreePacker(RecipeCategory category, ItemLike result, ItemLike ingredient) {
/* 204 */     threeByThreePacker(category, result, ingredient, getHasName(ingredient));
/*     */   }
/*     */   
/*     */   protected void planksFromLog(ItemLike result, TagKey<Item> logs, int count) {
/* 209 */     shapeless(RecipeCategory.BUILDING_BLOCKS, result, count)
/* 210 */       .requires(logs)
/* 211 */       .group("planks")
/* 212 */       .unlockedBy("has_log", has(logs))
/* 213 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void planksFromLogs(ItemLike result, TagKey<Item> logs, int count) {
/* 217 */     shapeless(RecipeCategory.BUILDING_BLOCKS, result, count)
/* 218 */       .requires(logs)
/* 219 */       .group("planks")
/* 220 */       .unlockedBy("has_logs", has(logs))
/* 221 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void woodFromLogs(ItemLike result, ItemLike log) {
/* 225 */     shaped(RecipeCategory.BUILDING_BLOCKS, result, 3)
/* 226 */       .define(Character.valueOf('#'), log)
/* 227 */       .pattern("##")
/* 228 */       .pattern("##")
/* 229 */       .group("bark")
/* 230 */       .unlockedBy("has_log", has(log))
/* 231 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void woodenBoat(ItemLike result, ItemLike planks) {
/* 235 */     shaped(RecipeCategory.TRANSPORTATION, result)
/* 236 */       .define(Character.valueOf('#'), planks)
/* 237 */       .pattern("# #")
/* 238 */       .pattern("###")
/* 239 */       .group("boat")
/* 240 */       .unlockedBy("in_water", insideOf(Blocks.WATER))
/* 241 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void chestBoat(ItemLike chestBoat, ItemLike boat) {
/* 245 */     shapeless(RecipeCategory.TRANSPORTATION, chestBoat)
/* 246 */       .requires((ItemLike)Blocks.CHEST)
/* 247 */       .requires(boat)
/* 248 */       .group("chest_boat")
/* 249 */       .unlockedBy("has_boat", has(ItemTags.BOATS))
/* 250 */       .save(this.output);
/*     */   }
/*     */   
/*     */   private RecipeBuilder buttonBuilder(ItemLike result, Ingredient base) {
/* 254 */     return shapeless(RecipeCategory.REDSTONE, result)
/* 255 */       .requires(base);
/*     */   }
/*     */   
/*     */   protected RecipeBuilder doorBuilder(ItemLike result, Ingredient base) {
/* 259 */     return shaped(RecipeCategory.REDSTONE, result, 3)
/* 260 */       .define(Character.valueOf('#'), base)
/* 261 */       .pattern("##")
/* 262 */       .pattern("##")
/* 263 */       .pattern("##");
/*     */   }
/*     */   
/*     */   private RecipeBuilder fenceBuilder(ItemLike result, Ingredient base) {
/* 267 */     int count = (result == Blocks.NETHER_BRICK_FENCE) ? 6 : 3;
/* 268 */     Item base2 = (result == Blocks.NETHER_BRICK_FENCE) ? Items.NETHER_BRICK : Items.STICK;
/* 269 */     return shaped(RecipeCategory.DECORATIONS, result, count)
/* 270 */       .define(Character.valueOf('W'), base)
/* 271 */       .define(Character.valueOf('#'), (ItemLike)base2)
/* 272 */       .pattern("W#W")
/* 273 */       .pattern("W#W");
/*     */   }
/*     */   
/*     */   private RecipeBuilder fenceGateBuilder(ItemLike result, Ingredient planks) {
/* 277 */     return shaped(RecipeCategory.REDSTONE, result)
/* 278 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 279 */       .define(Character.valueOf('W'), planks)
/* 280 */       .pattern("#W#")
/* 281 */       .pattern("#W#");
/*     */   }
/*     */   
/*     */   protected void pressurePlate(ItemLike result, ItemLike base) {
/* 285 */     pressurePlateBuilder(RecipeCategory.REDSTONE, result, Ingredient.of(base))
/* 286 */       .unlockedBy(getHasName(base), has(base))
/* 287 */       .save(this.output);
/*     */   }
/*     */   
/*     */   private RecipeBuilder pressurePlateBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 291 */     return shaped(category, result)
/* 292 */       .define(Character.valueOf('#'), base)
/* 293 */       .pattern("##");
/*     */   }
/*     */   
/*     */   protected void slab(RecipeCategory category, ItemLike result, ItemLike base) {
/* 297 */     slabBuilder(category, result, Ingredient.of(base))
/* 298 */       .unlockedBy(getHasName(base), has(base))
/* 299 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void shelf(ItemLike result, ItemLike strippedLogs) {
/* 303 */     shaped(RecipeCategory.DECORATIONS, result, 6)
/* 304 */       .define(Character.valueOf('#'), strippedLogs)
/* 305 */       .pattern("###")
/* 306 */       .pattern("   ")
/* 307 */       .pattern("###")
/* 308 */       .group("shelf")
/* 309 */       .unlockedBy(getHasName(strippedLogs), has(strippedLogs))
/* 310 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected RecipeBuilder slabBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 314 */     return shaped(category, result, 6)
/* 315 */       .define(Character.valueOf('#'), base)
/* 316 */       .pattern("###");
/*     */   }
/*     */   
/*     */   protected RecipeBuilder stairBuilder(ItemLike result, Ingredient base) {
/* 320 */     return shaped(RecipeCategory.BUILDING_BLOCKS, result, 4)
/* 321 */       .define(Character.valueOf('#'), base)
/* 322 */       .pattern("#  ")
/* 323 */       .pattern("## ")
/* 324 */       .pattern("###");
/*     */   }
/*     */   
/*     */   protected RecipeBuilder trapdoorBuilder(ItemLike result, Ingredient base) {
/* 328 */     return shaped(RecipeCategory.REDSTONE, result, 2)
/* 329 */       .define(Character.valueOf('#'), base)
/* 330 */       .pattern("###")
/* 331 */       .pattern("###");
/*     */   }
/*     */   
/*     */   private RecipeBuilder signBuilder(ItemLike result, Ingredient planks) {
/* 335 */     return shaped(RecipeCategory.DECORATIONS, result, 3)
/* 336 */       .group("sign")
/* 337 */       .define(Character.valueOf('#'), planks)
/* 338 */       .define(Character.valueOf('X'), (ItemLike)Items.STICK)
/* 339 */       .pattern("###")
/* 340 */       .pattern("###")
/* 341 */       .pattern(" X ");
/*     */   }
/*     */   
/*     */   protected RecipeBuilder hangingSignBuilder(ItemLike result, Ingredient ingredient) {
/* 345 */     return shaped(RecipeCategory.DECORATIONS, result, 6)
/* 346 */       .group("hanging_sign")
/* 347 */       .define(Character.valueOf('#'), ingredient)
/* 348 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_CHAIN)
/* 349 */       .pattern("X X")
/* 350 */       .pattern("###")
/* 351 */       .pattern("###");
/*     */   }
/*     */   
/*     */   protected void colorItemWithDye(List<Item> dyes, List<Item> items, String groupName, RecipeCategory category) {
/* 355 */     colorWithDye(dyes, items, null, groupName, category);
/*     */   }
/*     */   
/*     */   protected void colorWithDye(List<Item> dyes, List<Item> dyedItems, Item uncoloredItem, String groupName, RecipeCategory category) {
/* 359 */     for (int dyeIndex = 0; dyeIndex < dyes.size(); dyeIndex++) {
/* 360 */       Item dye = dyes.get(dyeIndex);
/* 361 */       Item dyedItem = dyedItems.get(dyeIndex);
/* 363 */       Stream<Item> sourceItems = dyedItems.stream().filter(b -> !b.equals(dyedItem));
/* 364 */       if (uncoloredItem != null)
/* 365 */         sourceItems = Stream.concat(sourceItems, Stream.of(uncoloredItem)); 
/* 368 */       shapeless(category, (ItemLike)dyedItem)
/* 369 */         .requires((ItemLike)dye)
/* 370 */         .requires(Ingredient.of(sourceItems))
/* 371 */         .group(groupName)
/* 372 */         .unlockedBy("has_needed_dye", has((ItemLike)dye))
/* 373 */         .save(this.output, "dye_" + getItemName((ItemLike)dyedItem));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void carpet(ItemLike result, ItemLike sourceItem) {
/* 378 */     shaped(RecipeCategory.DECORATIONS, result, 3)
/* 379 */       .define(Character.valueOf('#'), sourceItem)
/* 380 */       .pattern("##")
/* 381 */       .group("carpet")
/* 382 */       .unlockedBy(getHasName(sourceItem), has(sourceItem))
/* 383 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void bedFromPlanksAndWool(ItemLike result, ItemLike wool) {
/* 387 */     shaped(RecipeCategory.DECORATIONS, result)
/* 388 */       .define(Character.valueOf('#'), wool)
/* 389 */       .define(Character.valueOf('X'), ItemTags.PLANKS)
/* 390 */       .pattern("###")
/* 391 */       .pattern("XXX")
/* 392 */       .group("bed")
/* 393 */       .unlockedBy(getHasName(wool), has(wool))
/* 394 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void banner(ItemLike result, ItemLike wool) {
/* 398 */     shaped(RecipeCategory.DECORATIONS, result)
/* 399 */       .define(Character.valueOf('#'), wool)
/* 400 */       .define(Character.valueOf('|'), (ItemLike)Items.STICK)
/* 401 */       .pattern("###")
/* 402 */       .pattern("###")
/* 403 */       .pattern(" | ")
/* 404 */       .group("banner")
/* 405 */       .unlockedBy(getHasName(wool), has(wool))
/* 406 */       .save(this.output);
/* 408 */     SpecialRecipeBuilder.special(() -> new BannerDuplicateRecipe(Ingredient.of(result), new ItemStackTemplate(result.asItem())))
/* 409 */       .save(this.output, getItemName(result) + "_duplicate");
/*     */   }
/*     */   
/*     */   protected void stainedGlassFromGlassAndDye(ItemLike result, ItemLike dye) {
/* 413 */     shaped(RecipeCategory.BUILDING_BLOCKS, result, 8)
/* 414 */       .define(Character.valueOf('#'), (ItemLike)Blocks.GLASS)
/* 415 */       .define(Character.valueOf('X'), dye)
/* 416 */       .pattern("###")
/* 417 */       .pattern("#X#")
/* 418 */       .pattern("###")
/* 419 */       .group("stained_glass")
/* 420 */       .unlockedBy("has_glass", has((ItemLike)Blocks.GLASS))
/* 421 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void dryGhast(ItemLike result) {
/* 425 */     shaped(RecipeCategory.BUILDING_BLOCKS, result, 1)
/* 426 */       .define(Character.valueOf('#'), (ItemLike)Items.GHAST_TEAR)
/* 427 */       .define(Character.valueOf('X'), (ItemLike)Items.SOUL_SAND)
/* 428 */       .pattern("###")
/* 429 */       .pattern("#X#")
/* 430 */       .pattern("###")
/* 431 */       .group("dry_ghast")
/* 432 */       .unlockedBy(getHasName((ItemLike)Items.GHAST_TEAR), has((ItemLike)Items.GHAST_TEAR))
/* 433 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void harness(ItemLike result, ItemLike wool) {
/* 437 */     shaped(RecipeCategory.COMBAT, result)
/* 438 */       .define(Character.valueOf('#'), wool)
/* 439 */       .define(Character.valueOf('G'), (ItemLike)Items.GLASS)
/* 440 */       .define(Character.valueOf('L'), (ItemLike)Items.LEATHER)
/* 441 */       .pattern("LLL")
/* 442 */       .pattern("G#G")
/* 443 */       .group("harness")
/* 444 */       .unlockedBy("has_dried_ghast", has((ItemLike)Blocks.DRIED_GHAST))
/* 445 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void stainedGlassPaneFromStainedGlass(ItemLike result, ItemLike stainedGlass) {
/* 449 */     shaped(RecipeCategory.DECORATIONS, result, 16)
/* 450 */       .define(Character.valueOf('#'), stainedGlass)
/* 451 */       .pattern("###")
/* 452 */       .pattern("###")
/* 453 */       .group("stained_glass_pane")
/* 454 */       .unlockedBy("has_glass", has(stainedGlass))
/* 455 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void stainedGlassPaneFromGlassPaneAndDye(ItemLike result, ItemLike dye) {
/* 459 */     shaped(RecipeCategory.DECORATIONS, result, 8)
/* 460 */       .define(Character.valueOf('#'), (ItemLike)Blocks.GLASS_PANE)
/* 461 */       .define(Character.valueOf('$'), dye)
/* 462 */       .pattern("###")
/* 463 */       .pattern("#$#")
/* 464 */       .pattern("###")
/* 465 */       .group("stained_glass_pane")
/* 466 */       .unlockedBy("has_glass_pane", has((ItemLike)Blocks.GLASS_PANE))
/* 467 */       .unlockedBy(getHasName(dye), has(dye))
/* 468 */       .save(this.output, getConversionRecipeName(result, (ItemLike)Blocks.GLASS_PANE));
/*     */   }
/*     */   
/*     */   protected void coloredTerracottaFromTerracottaAndDye(ItemLike result, ItemLike dye) {
/* 472 */     shaped(RecipeCategory.BUILDING_BLOCKS, result, 8)
/* 473 */       .define(Character.valueOf('#'), (ItemLike)Blocks.TERRACOTTA)
/* 474 */       .define(Character.valueOf('X'), dye)
/* 475 */       .pattern("###")
/* 476 */       .pattern("#X#")
/* 477 */       .pattern("###")
/* 478 */       .group("stained_terracotta")
/* 479 */       .unlockedBy("has_terracotta", has((ItemLike)Blocks.TERRACOTTA))
/* 480 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void concretePowder(ItemLike result, ItemLike dye) {
/* 484 */     shapeless(RecipeCategory.BUILDING_BLOCKS, result, 8)
/* 485 */       .requires(dye)
/* 486 */       .requires((ItemLike)Blocks.SAND, 4)
/* 487 */       .requires((ItemLike)Blocks.GRAVEL, 4)
/* 488 */       .group("concrete_powder")
/* 489 */       .unlockedBy("has_sand", has((ItemLike)Blocks.SAND))
/* 490 */       .unlockedBy("has_gravel", has((ItemLike)Blocks.GRAVEL))
/* 491 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void candle(ItemLike result, ItemLike dye) {
/* 495 */     shapeless(RecipeCategory.DECORATIONS, result)
/* 496 */       .requires((ItemLike)Blocks.CANDLE)
/* 497 */       .requires(dye)
/* 498 */       .group("dyed_candle")
/* 499 */       .unlockedBy(getHasName(dye), has(dye))
/* 500 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void wall(RecipeCategory category, ItemLike result, ItemLike base) {
/* 504 */     wallBuilder(category, result, Ingredient.of(base))
/* 505 */       .unlockedBy(getHasName(base), has(base))
/* 506 */       .save(this.output);
/*     */   }
/*     */   
/*     */   private RecipeBuilder wallBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 510 */     return shaped(category, result, 6)
/* 511 */       .define(Character.valueOf('#'), base)
/* 512 */       .pattern("###")
/* 513 */       .pattern("###");
/*     */   }
/*     */   
/*     */   private RecipeBuilder bricksBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 517 */     return shaped(category, result, 4)
/* 518 */       .define(Character.valueOf('#'), base)
/* 519 */       .pattern("##")
/* 520 */       .pattern("##");
/*     */   }
/*     */   
/*     */   private RecipeBuilder tilesBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 524 */     return shaped(category, result, 4)
/* 525 */       .define(Character.valueOf('#'), base)
/* 526 */       .pattern("##")
/* 527 */       .pattern("##");
/*     */   }
/*     */   
/*     */   private RecipeBuilder pillarBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 531 */     return shaped(category, result, 2)
/* 532 */       .define(Character.valueOf('#'), base)
/* 533 */       .pattern("#")
/* 534 */       .pattern("#");
/*     */   }
/*     */   
/*     */   protected void polished(RecipeCategory category, ItemLike result, ItemLike base) {
/* 538 */     polishedBuilder(category, result, Ingredient.of(base))
/* 539 */       .unlockedBy(getHasName(base), has(base))
/* 540 */       .save(this.output);
/*     */   }
/*     */   
/*     */   private RecipeBuilder polishedBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 544 */     return shaped(category, result, 4)
/* 545 */       .define(Character.valueOf('S'), base)
/* 546 */       .pattern("SS")
/* 547 */       .pattern("SS");
/*     */   }
/*     */   
/*     */   protected void cut(RecipeCategory category, ItemLike result, ItemLike base) {
/* 551 */     cutBuilder(category, result, Ingredient.of(base))
/* 552 */       .unlockedBy(getHasName(base), has(base))
/* 553 */       .save(this.output);
/*     */   }
/*     */   
/*     */   private ShapedRecipeBuilder cutBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 557 */     return shaped(category, result, 4)
/* 558 */       .define(Character.valueOf('#'), base)
/* 559 */       .pattern("##")
/* 560 */       .pattern("##");
/*     */   }
/*     */   
/*     */   protected void chiseled(RecipeCategory category, ItemLike result, ItemLike base) {
/* 564 */     chiseledBuilder(category, result, Ingredient.of(base))
/* 565 */       .unlockedBy(getHasName(base), has(base))
/* 566 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void mosaicBuilder(RecipeCategory category, ItemLike result, ItemLike base) {
/* 570 */     shaped(category, result)
/* 571 */       .define(Character.valueOf('#'), base)
/* 572 */       .pattern("#")
/* 573 */       .pattern("#")
/* 574 */       .unlockedBy(getHasName(base), has(base))
/* 575 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected ShapedRecipeBuilder chiseledBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 579 */     return shaped(category, result)
/* 580 */       .define(Character.valueOf('#'), base)
/* 581 */       .pattern("#")
/* 582 */       .pattern("#");
/*     */   }
/*     */   
/*     */   protected void stonecutterResultFromBase(RecipeCategory category, ItemLike result, ItemLike base) {
/* 586 */     stonecutterResultFromBase(category, result, base, 1);
/*     */   }
/*     */   
/*     */   protected void stonecutterResultFromBase(RecipeCategory category, ItemLike result, ItemLike base, int count) {
/* 590 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(base), category, result, count)
/* 591 */       .unlockedBy(getHasName(base), has(base))
/* 592 */       .save(this.output, getConversionRecipeName(result, base) + "_stonecutting");
/*     */   }
/*     */   
/*     */   private void smeltingResultFromBase(ItemLike result, ItemLike base) {
/* 596 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(base), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, result, 0.1F, 200)
/* 597 */       .unlockedBy(getHasName(base), has(base))
/* 598 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void nineBlockStorageRecipes(RecipeCategory unpackedFormCategory, ItemLike unpackedForm, RecipeCategory packedFormCategory, ItemLike packedForm) {
/* 602 */     nineBlockStorageRecipes(unpackedFormCategory, unpackedForm, packedFormCategory, packedForm, getSimpleRecipeName(packedForm), null, getSimpleRecipeName(unpackedForm), null);
/*     */   }
/*     */   
/*     */   protected void nineBlockStorageRecipesWithCustomPacking(RecipeCategory unpackedFormCategory, ItemLike unpackedForm, RecipeCategory packedFormCategory, ItemLike packedForm, String packingRecipeId, String packingRecipeGroup) {
/* 606 */     nineBlockStorageRecipes(unpackedFormCategory, unpackedForm, packedFormCategory, packedForm, packingRecipeId, packingRecipeGroup, getSimpleRecipeName(unpackedForm), null);
/*     */   }
/*     */   
/*     */   protected void nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory unpackedFormCategory, ItemLike unpackedForm, RecipeCategory packedFormCategory, ItemLike packedForm, String unpackingRecipeId, String unpackingRecipeGroup) {
/* 610 */     nineBlockStorageRecipes(unpackedFormCategory, unpackedForm, packedFormCategory, packedForm, getSimpleRecipeName(packedForm), null, unpackingRecipeId, unpackingRecipeGroup);
/*     */   }
/*     */   
/*     */   private void nineBlockStorageRecipes(RecipeCategory unpackedFormCategory, ItemLike unpackedForm, RecipeCategory packedFormCategory, ItemLike packedForm, String packingRecipeId, String packingRecipeGroup, String unpackingRecipeId, String unpackingRecipeGroup) {
/* 614 */     shapeless(unpackedFormCategory, unpackedForm, 9)
/* 615 */       .requires(packedForm)
/* 616 */       .group(unpackingRecipeGroup)
/* 617 */       .unlockedBy(getHasName(packedForm), has(packedForm))
/* 618 */       .save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.parse(unpackingRecipeId)));
/* 620 */     shaped(packedFormCategory, packedForm)
/* 621 */       .define(Character.valueOf('#'), unpackedForm)
/* 622 */       .pattern("###")
/* 623 */       .pattern("###")
/* 624 */       .pattern("###")
/* 625 */       .group(packingRecipeGroup)
/* 626 */       .unlockedBy(getHasName(unpackedForm), has(unpackedForm))
/* 627 */       .save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.parse(packingRecipeId)));
/*     */   }
/*     */   
/*     */   protected void copySmithingTemplate(ItemLike smithingTemplate, ItemLike baseMaterial) {
/* 631 */     shaped(RecipeCategory.MISC, smithingTemplate, 2)
/* 632 */       .define(Character.valueOf('#'), (ItemLike)Items.DIAMOND)
/* 633 */       .define(Character.valueOf('C'), baseMaterial)
/* 634 */       .define(Character.valueOf('S'), smithingTemplate)
/* 635 */       .pattern("#S#")
/* 636 */       .pattern("#C#")
/* 637 */       .pattern("###")
/* 638 */       .unlockedBy(getHasName(smithingTemplate), has(smithingTemplate))
/* 639 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void copySmithingTemplate(ItemLike smithingTemplate, Ingredient baseMaterials) {
/* 643 */     shaped(RecipeCategory.MISC, smithingTemplate, 2)
/* 644 */       .define(Character.valueOf('#'), (ItemLike)Items.DIAMOND)
/* 645 */       .define(Character.valueOf('C'), baseMaterials)
/* 646 */       .define(Character.valueOf('S'), smithingTemplate)
/* 647 */       .pattern("#S#")
/* 648 */       .pattern("#C#")
/* 649 */       .pattern("###")
/* 650 */       .unlockedBy(getHasName(smithingTemplate), has(smithingTemplate))
/* 651 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected <T extends AbstractCookingRecipe> void cookRecipes(String source, AbstractCookingRecipe.Factory<T> factory, int cookingTime) {
/* 655 */     simpleCookingRecipe(source, factory, cookingTime, (ItemLike)Items.BEEF, (ItemLike)Items.COOKED_BEEF, 0.35F);
/* 656 */     simpleCookingRecipe(source, factory, cookingTime, (ItemLike)Items.CHICKEN, (ItemLike)Items.COOKED_CHICKEN, 0.35F);
/* 657 */     simpleCookingRecipe(source, factory, cookingTime, (ItemLike)Items.COD, (ItemLike)Items.COOKED_COD, 0.35F);
/* 658 */     simpleCookingRecipe(source, factory, cookingTime, (ItemLike)Items.KELP, (ItemLike)Items.DRIED_KELP, 0.1F);
/* 659 */     simpleCookingRecipe(source, factory, cookingTime, (ItemLike)Items.SALMON, (ItemLike)Items.COOKED_SALMON, 0.35F);
/* 660 */     simpleCookingRecipe(source, factory, cookingTime, (ItemLike)Items.MUTTON, (ItemLike)Items.COOKED_MUTTON, 0.35F);
/* 661 */     simpleCookingRecipe(source, factory, cookingTime, (ItemLike)Items.PORKCHOP, (ItemLike)Items.COOKED_PORKCHOP, 0.35F);
/* 662 */     simpleCookingRecipe(source, factory, cookingTime, (ItemLike)Items.POTATO, (ItemLike)Items.BAKED_POTATO, 0.35F);
/* 663 */     simpleCookingRecipe(source, factory, cookingTime, (ItemLike)Items.RABBIT, (ItemLike)Items.COOKED_RABBIT, 0.35F);
/*     */   }
/*     */   
/*     */   private <T extends AbstractCookingRecipe> void simpleCookingRecipe(String source, AbstractCookingRecipe.Factory<T> factory, int cookingTime, ItemLike base, ItemLike result, float experience) {
/* 667 */     SimpleCookingRecipeBuilder.<T>generic(Ingredient.of(base), RecipeCategory.FOOD, CookingBookCategory.FOOD, result, experience, cookingTime, factory)
/* 668 */       .unlockedBy(getHasName(base), has(base))
/* 669 */       .save(this.output, getItemName(result) + "_from_" + getItemName(result));
/*     */   }
/*     */   
/*     */   protected void waxRecipes(FeatureFlagSet flagSet) {
/* 673 */     ((BiMap)HoneycombItem.WAXABLES.get()).forEach((block, waxedBlock) -> {
/*     */           if (!waxedBlock.requiredFeatures().isSubsetOf(flagSet))
/*     */             return; 
/*     */           Pair<RecipeCategory, String> pair = (Pair<RecipeCategory, String>)HoneycombItem.WAXED_RECIPES.getOrDefault(waxedBlock, Pair.of(RecipeCategory.BUILDING_BLOCKS, getItemName((ItemLike)waxedBlock)));
/*     */           RecipeCategory recipeCategory = (RecipeCategory)pair.getFirst();
/*     */           String group = (String)pair.getSecond();
/*     */           shapeless(recipeCategory, (ItemLike)waxedBlock).requires((ItemLike)block).requires((ItemLike)Items.HONEYCOMB).group(group).unlockedBy(getHasName((ItemLike)block), has((ItemLike)block)).save(this.output, getConversionRecipeName((ItemLike)waxedBlock, (ItemLike)Items.HONEYCOMB));
/*     */         });
/*     */   }
/*     */   
/*     */   protected void grate(Block grateBlock, Block material) {
/* 694 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)grateBlock, 4)
/* 695 */       .define(Character.valueOf('M'), (ItemLike)material)
/* 696 */       .pattern(" M ")
/* 697 */       .pattern("M M")
/* 698 */       .pattern(" M ")
/* 699 */       .group(getItemName((ItemLike)grateBlock))
/* 700 */       .unlockedBy(getHasName((ItemLike)material), has((ItemLike)material))
/* 701 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void copperBulb(Block copperBulb, Block copperMaterial) {
/* 705 */     shaped(RecipeCategory.REDSTONE, (ItemLike)copperBulb, 4)
/* 706 */       .define(Character.valueOf('C'), (ItemLike)copperMaterial)
/* 707 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/* 708 */       .define(Character.valueOf('B'), (ItemLike)Items.BLAZE_ROD)
/* 709 */       .pattern(" C ")
/* 710 */       .pattern("CBC")
/* 711 */       .pattern(" R ")
/* 712 */       .unlockedBy(getHasName((ItemLike)copperMaterial), has((ItemLike)copperMaterial))
/* 713 */       .group(getItemName((ItemLike)copperBulb))
/* 714 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void waxedChiseled(Block result, Block material) {
/* 718 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)result)
/* 719 */       .define(Character.valueOf('M'), (ItemLike)material)
/* 720 */       .pattern(" M ")
/* 721 */       .pattern(" M ")
/* 722 */       .group(getItemName((ItemLike)result))
/* 723 */       .unlockedBy(getHasName((ItemLike)material), has((ItemLike)material))
/* 724 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void suspiciousStew(Item item, SuspiciousEffectHolder effectHolder) {
/* 730 */     ItemStackTemplate stew = new ItemStackTemplate(Items.SUSPICIOUS_STEW, DataComponentPatch.builder().set(DataComponents.SUSPICIOUS_STEW_EFFECTS, effectHolder.getSuspiciousEffects()).build());
/* 732 */     shapeless(RecipeCategory.FOOD, stew)
/* 733 */       .requires((ItemLike)Items.BOWL)
/* 734 */       .requires((ItemLike)Items.BROWN_MUSHROOM)
/* 735 */       .requires((ItemLike)Items.RED_MUSHROOM)
/* 736 */       .requires((ItemLike)item)
/* 737 */       .group("suspicious_stew")
/* 738 */       .unlockedBy(getHasName((ItemLike)item), has((ItemLike)item))
/* 739 */       .save(this.output, getItemName((ItemLike)stew.item().value()) + "_from_" + getItemName((ItemLike)stew.item().value()));
/*     */   }
/*     */   
/*     */   protected void dyedItem(Item target, String group) {
/* 743 */     CustomCraftingRecipeBuilder.customCrafting(RecipeCategory.MISC, (commonInfo, bookInfo) -> new DyeRecipe(commonInfo, bookInfo, Ingredient.of((ItemLike)target), tag(ItemTags.DYES), new ItemStackTemplate(target)))
/* 744 */       .unlockedBy(getHasName((ItemLike)target), has((ItemLike)target))
/* 745 */       .group(group)
/* 746 */       .save(this.output, getItemName((ItemLike)target) + "_dyed");
/*     */   }
/*     */   
/*     */   protected void dyedShulkerBoxRecipe(Item dye, Item dyedResult) {
/* 750 */     TransmuteRecipeBuilder.transmute(RecipeCategory.DECORATIONS, 
/*     */         
/* 752 */         tag(ItemTags.SHULKER_BOXES), 
/* 753 */         Ingredient.of((ItemLike)dye), dyedResult)
/*     */       
/* 756 */       .group("shulker_box_dye")
/* 757 */       .unlockedBy("has_shulker_box", has(ItemTags.SHULKER_BOXES))
/* 758 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void dyedBundleRecipe(Item dye, Item dyedResult) {
/* 762 */     TransmuteRecipeBuilder.transmute(RecipeCategory.TOOLS, 
/*     */         
/* 764 */         tag(ItemTags.BUNDLES), 
/* 765 */         Ingredient.of((ItemLike)dye), dyedResult)
/*     */       
/* 768 */       .group("bundle_dye")
/* 769 */       .unlockedBy(getHasName((ItemLike)dye), has((ItemLike)dye))
/* 770 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void generateRecipes(BlockFamily family, FeatureFlagSet flagSet) {
/* 774 */     family.getVariants().forEach((variant, result) -> {
/*     */           if (!result.requiredFeatures().isSubsetOf(flagSet))
/*     */             return; 
/*     */           if (family.shouldGenerateCraftingRecipe())
/*     */             generateCraftingRecipe(family, variant, result, (ItemLike)getBaseBlockForCrafting(family, variant)); 
/*     */           if (family.shouldGenerateSmeltingRecipe())
/*     */             generateSmeltingRecipe(variant, result, (ItemLike)getBaseBlockForCrafting(family, variant)); 
/*     */           if (family.shouldGenerateStonecutterRecipe())
/*     */             generateStonecutterRecipe(family, variant, family.getBaseBlock()); 
/*     */         });
/*     */   }
/*     */   
/*     */   private void generateCraftingRecipe(BlockFamily family, BlockFamily.Variant variant, Block result, ItemLike base) {
/* 791 */     FamilyCraftingRecipeProvider recipeFunction = SHAPE_BUILDERS.get(variant);
/* 792 */     if (recipeFunction != null) {
/* 793 */       RecipeBuilder builder = recipeFunction.create(this, (ItemLike)result, base);
/* 794 */       family.getRecipeGroupPrefix().ifPresent(prefix -> builder.group(variant.getPrefixedRecipeGroup(prefix)));
/* 795 */       builder.unlockedBy(getCraftingCriterionName(family, variant, base), has(base));
/* 796 */       builder.save(this.output);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void generateSmeltingRecipe(BlockFamily.Variant variant, Block result, ItemLike base) {
/* 801 */     if (variant == BlockFamily.Variant.CRACKED)
/* 802 */       smeltingResultFromBase((ItemLike)result, base); 
/* 804 */     if (variant == BlockFamily.Variant.COBBLED)
/* 805 */       smeltingResultFromBase(base, (ItemLike)result); 
/*     */   }
/*     */   
/*     */   private void generateStonecutterRecipe(BlockFamily family, BlockFamily.Variant variant, Block base) {
/* 810 */     FamilyStonecutterRecipeProvider recipeFunction = STONECUTTER_RECIPE_BUILDERS.get(variant);
/* 811 */     if (recipeFunction != null)
/* 812 */       recipeFunction.create(this, (ItemLike)family.get(variant), (ItemLike)base); 
/* 816 */     if (variant == BlockFamily.Variant.POLISHED || variant == BlockFamily.Variant.CUT || variant == BlockFamily.Variant.BRICKS || variant == BlockFamily.Variant.TILES || variant == BlockFamily.Variant.PILLAR || variant == BlockFamily.Variant.COBBLED) {
/* 822 */       BlockFamily childVariantFamily = BlockFamilies.getFamily(family.get(variant));
/* 823 */       if (childVariantFamily != null)
/* 824 */         childVariantFamily.getVariants().forEach((childVariant, r) -> generateStonecutterRecipe(childVariantFamily, childVariant, base)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Block getBaseBlockForCrafting(BlockFamily family, BlockFamily.Variant variant) {
/* 830 */     BlockFamily.Variant baseVariant = variant.getBaseVariantForCrafting();
/* 831 */     if (baseVariant != null) {
/* 832 */       if (!family.getVariants().containsKey(baseVariant))
/* 833 */         throw new IllegalStateException(baseVariant.name() + " is not defined for the family."); 
/* 835 */       return family.get(baseVariant);
/*     */     } 
/* 837 */     return family.getBaseBlock();
/*     */   }
/*     */   
/*     */   private String getCraftingCriterionName(BlockFamily family, BlockFamily.Variant variant, ItemLike base) {
/* 842 */     if (base != family.getBaseBlock())
/* 843 */       return "has_" + variant.getBaseVariantForCrafting().getRecipeGroup(); 
/* 845 */     return family.getRecipeUnlockedBy().orElseGet(() -> getHasName(base));
/*     */   }
/*     */   
/*     */   static {
/* 879 */     SHAPE_BUILDERS = (Map<BlockFamily.Variant, FamilyCraftingRecipeProvider>)ImmutableMap.builder().put(BlockFamily.Variant.BUTTON, (context, result, base) -> context.buttonBuilder(result, Ingredient.of(base))).put(BlockFamily.Variant.CHISELED, (context, result, base) -> context.chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, result, Ingredient.of(base))).put(BlockFamily.Variant.CUT, (context, result, base) -> context.cutBuilder(RecipeCategory.BUILDING_BLOCKS, result, Ingredient.of(base))).put(BlockFamily.Variant.DOOR, (context, result, base) -> context.doorBuilder(result, Ingredient.of(base))).put(BlockFamily.Variant.CUSTOM_FENCE, (context, result, base) -> context.fenceBuilder(result, Ingredient.of(base))).put(BlockFamily.Variant.FENCE, (context, result, base) -> context.fenceBuilder(result, Ingredient.of(base))).put(BlockFamily.Variant.CUSTOM_FENCE_GATE, (context, result, base) -> context.fenceGateBuilder(result, Ingredient.of(base))).put(BlockFamily.Variant.FENCE_GATE, (context, result, base) -> context.fenceGateBuilder(result, Ingredient.of(base))).put(BlockFamily.Variant.SIGN, (context, result, base) -> context.signBuilder(result, Ingredient.of(base))).put(BlockFamily.Variant.CUSTOM_HANGING_SIGN, (context, result, base) -> context.hangingSignBuilder(result, Ingredient.of(base))).put(BlockFamily.Variant.HANGING_SIGN, (context, result, base) -> context.hangingSignBuilder(result, Ingredient.of(base))).put(BlockFamily.Variant.SLAB, (context, result, base) -> context.slabBuilder(RecipeCategory.BUILDING_BLOCKS, result, Ingredient.of(base))).put(BlockFamily.Variant.STAIRS, (context, result, base) -> context.stairBuilder(result, Ingredient.of(base))).put(BlockFamily.Variant.PRESSURE_PLATE, (context, result, base) -> context.pressurePlateBuilder(RecipeCategory.REDSTONE, result, Ingredient.of(base))).put(BlockFamily.Variant.POLISHED, (context, result, base) -> context.polishedBuilder(RecipeCategory.BUILDING_BLOCKS, result, Ingredient.of(base))).put(BlockFamily.Variant.TRAPDOOR, (context, result, base) -> context.trapdoorBuilder(result, Ingredient.of(base))).put(BlockFamily.Variant.WALL, (context, result, base) -> context.wallBuilder(RecipeCategory.DECORATIONS, result, Ingredient.of(base))).put(BlockFamily.Variant.BRICKS, (context, result, base) -> context.bricksBuilder(RecipeCategory.BUILDING_BLOCKS, result, Ingredient.of(base))).put(BlockFamily.Variant.TILES, (context, result, base) -> context.tilesBuilder(RecipeCategory.BUILDING_BLOCKS, result, Ingredient.of(base))).put(BlockFamily.Variant.PILLAR, (context, result, base) -> context.pillarBuilder(RecipeCategory.BUILDING_BLOCKS, result, Ingredient.of(base))).build();
/* 892 */     STONECUTTER_RECIPE_BUILDERS = (Map<BlockFamily.Variant, FamilyStonecutterRecipeProvider>)ImmutableMap.builder().put(BlockFamily.Variant.SLAB, (context, result, base) -> context.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, result, base, 2)).put(BlockFamily.Variant.STAIRS, (context, result, base) -> context.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, result, base, 1)).put(BlockFamily.Variant.BRICKS, (context, result, base) -> context.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, result, base, 1)).put(BlockFamily.Variant.WALL, (context, result, base) -> context.stonecutterResultFromBase(RecipeCategory.DECORATIONS, result, base, 1)).put(BlockFamily.Variant.CHISELED, (context, result, base) -> context.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, result, base, 1)).put(BlockFamily.Variant.POLISHED, (context, result, base) -> context.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, result, base, 1)).put(BlockFamily.Variant.CUT, (context, result, base) -> context.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, result, base, 1)).put(BlockFamily.Variant.TILES, (context, result, base) -> context.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, result, base, 1)).put(BlockFamily.Variant.PILLAR, (context, result, base) -> context.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, result, base, 1)).put(BlockFamily.Variant.COBBLED, (context, result, base) -> context.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, result, base, 1)).build();
/*     */   }
/*     */   
/*     */   private static Criterion<EnterBlockTrigger.TriggerInstance> insideOf(Block block) {
/* 896 */     return CriteriaTriggers.ENTER_BLOCK.createCriterion((CriterionTriggerInstance)new EnterBlockTrigger.TriggerInstance(Optional.empty(), Optional.of(block.builtInRegistryHolder()), Optional.empty()));
/*     */   }
/*     */   
/*     */   protected Criterion<BredAnimalsTrigger.TriggerInstance> bredAnimal() {
/* 900 */     return CriteriaTriggers.BRED_ANIMALS.createCriterion((CriterionTriggerInstance)new BredAnimalsTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
/*     */   }
/*     */   
/*     */   private Criterion<InventoryChangeTrigger.TriggerInstance> has(MinMaxBounds.Ints count, ItemLike item) {
/* 904 */     return inventoryTrigger(new ItemPredicate.Builder[] { ItemPredicate.Builder.item().of(this.items, new ItemLike[] { item }).withCount(count) });
/*     */   }
/*     */   
/*     */   protected Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike item) {
/* 908 */     return inventoryTrigger(new ItemPredicate.Builder[] { ItemPredicate.Builder.item().of(this.items, new ItemLike[] { item }) });
/*     */   }
/*     */   
/*     */   protected Criterion<InventoryChangeTrigger.TriggerInstance> has(TagKey<Item> tag) {
/* 912 */     return inventoryTrigger(new ItemPredicate.Builder[] { ItemPredicate.Builder.item().of(this.items, tag) });
/*     */   }
/*     */   
/*     */   private static Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate.Builder... predicates) {
/* 916 */     return inventoryTrigger((ItemPredicate[])Arrays.<ItemPredicate.Builder>stream(predicates).map(ItemPredicate.Builder::build).toArray(x$0 -> new ItemPredicate[x$0]));
/*     */   }
/*     */   
/*     */   private static Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate... predicates) {
/* 920 */     return CriteriaTriggers.INVENTORY_CHANGED.createCriterion((CriterionTriggerInstance)new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(predicates)));
/*     */   }
/*     */   
/*     */   protected static String getHasName(ItemLike baseBlock) {
/* 924 */     return "has_" + getItemName(baseBlock);
/*     */   }
/*     */   
/*     */   protected static String getItemName(ItemLike itemLike) {
/* 928 */     return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath();
/*     */   }
/*     */   
/*     */   protected static String getSimpleRecipeName(ItemLike itemLike) {
/* 932 */     return getItemName(itemLike);
/*     */   }
/*     */   
/*     */   protected static String getConversionRecipeName(ItemLike product, ItemLike material) {
/* 936 */     return getItemName(product) + "_from_" + getItemName(product);
/*     */   }
/*     */   
/*     */   protected static String getSmeltingRecipeName(ItemLike product) {
/* 940 */     return getItemName(product) + "_from_smelting";
/*     */   }
/*     */   
/*     */   protected static String getBlastingRecipeName(ItemLike product) {
/* 944 */     return getItemName(product) + "_from_blasting";
/*     */   }
/*     */   
/*     */   protected Ingredient tag(TagKey<Item> id) {
/* 948 */     return Ingredient.of((HolderSet)this.items.getOrThrow(id));
/*     */   }
/*     */   
/*     */   protected ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike item) {
/* 952 */     return ShapedRecipeBuilder.shaped(this.items, category, item);
/*     */   }
/*     */   
/*     */   protected ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike item, int count) {
/* 956 */     return ShapedRecipeBuilder.shaped(this.items, category, item, count);
/*     */   }
/*     */   
/*     */   protected ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemStackTemplate result) {
/* 960 */     return ShapelessRecipeBuilder.shapeless(this.items, category, result);
/*     */   }
/*     */   
/*     */   protected ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike item) {
/* 964 */     return ShapelessRecipeBuilder.shapeless(this.items, category, item);
/*     */   }
/*     */   
/*     */   protected ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike item, int count) {
/* 968 */     return ShapelessRecipeBuilder.shapeless(this.items, category, item, count);
/*     */   }
/*     */   
/*     */   protected abstract void buildRecipes();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\RecipeProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */