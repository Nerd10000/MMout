/*      */ package net.minecraft.data.recipes.packs;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.advancements.CriterionTriggerInstance;
/*      */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*      */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*      */ import net.minecraft.advancements.triggers.InventoryChangeTrigger;
/*      */ import net.minecraft.advancements.triggers.PlayerTrigger;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.data.PackOutput;
/*      */ import net.minecraft.data.recipes.CustomCraftingRecipeBuilder;
/*      */ import net.minecraft.data.recipes.RecipeCategory;
/*      */ import net.minecraft.data.recipes.RecipeOutput;
/*      */ import net.minecraft.data.recipes.RecipeProvider;
/*      */ import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
/*      */ import net.minecraft.data.recipes.SpecialRecipeBuilder;
/*      */ import net.minecraft.data.recipes.TransmuteRecipeBuilder;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.flag.FeatureFlags;
/*      */ import net.minecraft.world.item.DyeColor;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStackTemplate;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.component.FireworkExplosion;
/*      */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*      */ import net.minecraft.world.item.crafting.BookCloningRecipe;
/*      */ import net.minecraft.world.item.crafting.CookingBookCategory;
/*      */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*      */ import net.minecraft.world.item.crafting.DecoratedPotRecipe;
/*      */ import net.minecraft.world.item.crafting.FireworkRocketRecipe;
/*      */ import net.minecraft.world.item.crafting.FireworkStarFadeRecipe;
/*      */ import net.minecraft.world.item.crafting.FireworkStarRecipe;
/*      */ import net.minecraft.world.item.crafting.ImbueRecipe;
/*      */ import net.minecraft.world.item.crafting.Ingredient;
/*      */ import net.minecraft.world.item.crafting.MapExtendingRecipe;
/*      */ import net.minecraft.world.item.crafting.Recipe;
/*      */ import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
/*      */ import net.minecraft.world.item.crafting.TransmuteRecipe;
/*      */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*      */ import net.minecraft.world.item.equipment.trim.TrimPatterns;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.ColorCollection;
/*      */ import net.minecraft.world.level.block.SuspiciousEffectHolder;
/*      */ import net.minecraft.world.level.block.WeatheringCopper;
/*      */ import net.minecraft.world.level.block.WeatheringCopperCollection;
/*      */ 
/*      */ public class VanillaRecipeProvider extends RecipeProvider {
/*   65 */   private static final ImmutableList<ItemLike> COAL_SMELTABLES = ImmutableList.of(Items.COAL_ORE, Items.DEEPSLATE_COAL_ORE);
/*      */   
/*   66 */   private static final ImmutableList<ItemLike> IRON_SMELTABLES = ImmutableList.of(Items.IRON_ORE, Items.DEEPSLATE_IRON_ORE, Items.RAW_IRON);
/*      */   
/*   67 */   private static final ImmutableList<ItemLike> COPPER_SMELTABLES = ImmutableList.of(Items.COPPER_ORE, Items.DEEPSLATE_COPPER_ORE, Items.RAW_COPPER);
/*      */   
/*   68 */   private static final ImmutableList<ItemLike> GOLD_SMELTABLES = ImmutableList.of(Items.GOLD_ORE, Items.DEEPSLATE_GOLD_ORE, Items.NETHER_GOLD_ORE, Items.RAW_GOLD);
/*      */   
/*   69 */   private static final ImmutableList<ItemLike> DIAMOND_SMELTABLES = ImmutableList.of(Items.DIAMOND_ORE, Items.DEEPSLATE_DIAMOND_ORE);
/*      */   
/*   70 */   private static final ImmutableList<ItemLike> LAPIS_SMELTABLES = ImmutableList.of(Items.LAPIS_ORE, Items.DEEPSLATE_LAPIS_ORE);
/*      */   
/*   71 */   private static final ImmutableList<ItemLike> REDSTONE_SMELTABLES = ImmutableList.of(Items.REDSTONE_ORE, Items.DEEPSLATE_REDSTONE_ORE);
/*      */   
/*   72 */   private static final ImmutableList<ItemLike> EMERALD_SMELTABLES = ImmutableList.of(Items.EMERALD_ORE, Items.DEEPSLATE_EMERALD_ORE);
/*      */   
/*      */   public static class Runner extends RecipeProvider.Runner {
/*      */     public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
/*   76 */       super(packOutput, registries);
/*      */     }
/*      */     
/*      */     protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
/*   81 */       return new VanillaRecipeProvider(registries, output);
/*      */     }
/*      */     
/*      */     public String getName() {
/*   86 */       return "Vanilla Recipes";
/*      */     }
/*      */   }
/*      */   
/*      */   private VanillaRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
/*   91 */     super(registries, output);
/*      */   }
/*      */   
/*      */   protected void buildRecipes() {
/*   96 */     this.output.includeRootAdvancement();
/*   98 */     generateForEnabledBlockFamilies(FeatureFlagSet.of(FeatureFlags.VANILLA));
/*  100 */     planksFromLog((ItemLike)Blocks.ACACIA_PLANKS, ItemTags.ACACIA_LOGS, 4);
/*  101 */     planksFromLogs((ItemLike)Blocks.BIRCH_PLANKS, ItemTags.BIRCH_LOGS, 4);
/*  102 */     planksFromLogs((ItemLike)Blocks.CRIMSON_PLANKS, ItemTags.CRIMSON_STEMS, 4);
/*  103 */     planksFromLog((ItemLike)Blocks.DARK_OAK_PLANKS, ItemTags.DARK_OAK_LOGS, 4);
/*  104 */     planksFromLog((ItemLike)Blocks.PALE_OAK_PLANKS, ItemTags.PALE_OAK_LOGS, 4);
/*  105 */     planksFromLogs((ItemLike)Blocks.JUNGLE_PLANKS, ItemTags.JUNGLE_LOGS, 4);
/*  106 */     planksFromLogs((ItemLike)Blocks.OAK_PLANKS, ItemTags.OAK_LOGS, 4);
/*  107 */     planksFromLogs((ItemLike)Blocks.SPRUCE_PLANKS, ItemTags.SPRUCE_LOGS, 4);
/*  108 */     planksFromLogs((ItemLike)Blocks.WARPED_PLANKS, ItemTags.WARPED_STEMS, 4);
/*  109 */     planksFromLogs((ItemLike)Blocks.MANGROVE_PLANKS, ItemTags.MANGROVE_LOGS, 4);
/*  111 */     woodFromLogs((ItemLike)Blocks.ACACIA_WOOD, (ItemLike)Blocks.ACACIA_LOG);
/*  112 */     woodFromLogs((ItemLike)Blocks.BIRCH_WOOD, (ItemLike)Blocks.BIRCH_LOG);
/*  113 */     woodFromLogs((ItemLike)Blocks.DARK_OAK_WOOD, (ItemLike)Blocks.DARK_OAK_LOG);
/*  114 */     woodFromLogs((ItemLike)Blocks.PALE_OAK_WOOD, (ItemLike)Blocks.PALE_OAK_LOG);
/*  115 */     woodFromLogs((ItemLike)Blocks.JUNGLE_WOOD, (ItemLike)Blocks.JUNGLE_LOG);
/*  116 */     woodFromLogs((ItemLike)Blocks.OAK_WOOD, (ItemLike)Blocks.OAK_LOG);
/*  117 */     woodFromLogs((ItemLike)Blocks.SPRUCE_WOOD, (ItemLike)Blocks.SPRUCE_LOG);
/*  118 */     woodFromLogs((ItemLike)Blocks.CRIMSON_HYPHAE, (ItemLike)Blocks.CRIMSON_STEM);
/*  119 */     woodFromLogs((ItemLike)Blocks.WARPED_HYPHAE, (ItemLike)Blocks.WARPED_STEM);
/*  120 */     woodFromLogs((ItemLike)Blocks.MANGROVE_WOOD, (ItemLike)Blocks.MANGROVE_LOG);
/*  122 */     woodFromLogs((ItemLike)Blocks.STRIPPED_ACACIA_WOOD, (ItemLike)Blocks.STRIPPED_ACACIA_LOG);
/*  123 */     woodFromLogs((ItemLike)Blocks.STRIPPED_BIRCH_WOOD, (ItemLike)Blocks.STRIPPED_BIRCH_LOG);
/*  124 */     woodFromLogs((ItemLike)Blocks.STRIPPED_DARK_OAK_WOOD, (ItemLike)Blocks.STRIPPED_DARK_OAK_LOG);
/*  125 */     woodFromLogs((ItemLike)Blocks.STRIPPED_PALE_OAK_WOOD, (ItemLike)Blocks.STRIPPED_PALE_OAK_LOG);
/*  126 */     woodFromLogs((ItemLike)Blocks.STRIPPED_JUNGLE_WOOD, (ItemLike)Blocks.STRIPPED_JUNGLE_LOG);
/*  127 */     woodFromLogs((ItemLike)Blocks.STRIPPED_OAK_WOOD, (ItemLike)Blocks.STRIPPED_OAK_LOG);
/*  128 */     woodFromLogs((ItemLike)Blocks.STRIPPED_SPRUCE_WOOD, (ItemLike)Blocks.STRIPPED_SPRUCE_LOG);
/*  129 */     woodFromLogs((ItemLike)Blocks.STRIPPED_CRIMSON_HYPHAE, (ItemLike)Blocks.STRIPPED_CRIMSON_STEM);
/*  130 */     woodFromLogs((ItemLike)Blocks.STRIPPED_WARPED_HYPHAE, (ItemLike)Blocks.STRIPPED_WARPED_STEM);
/*  131 */     woodFromLogs((ItemLike)Blocks.STRIPPED_MANGROVE_WOOD, (ItemLike)Blocks.STRIPPED_MANGROVE_LOG);
/*  133 */     woodenBoat((ItemLike)Items.ACACIA_BOAT, (ItemLike)Blocks.ACACIA_PLANKS);
/*  134 */     woodenBoat((ItemLike)Items.BIRCH_BOAT, (ItemLike)Blocks.BIRCH_PLANKS);
/*  135 */     woodenBoat((ItemLike)Items.DARK_OAK_BOAT, (ItemLike)Blocks.DARK_OAK_PLANKS);
/*  136 */     woodenBoat((ItemLike)Items.PALE_OAK_BOAT, (ItemLike)Blocks.PALE_OAK_PLANKS);
/*  137 */     woodenBoat((ItemLike)Items.JUNGLE_BOAT, (ItemLike)Blocks.JUNGLE_PLANKS);
/*  138 */     woodenBoat((ItemLike)Items.OAK_BOAT, (ItemLike)Blocks.OAK_PLANKS);
/*  139 */     woodenBoat((ItemLike)Items.SPRUCE_BOAT, (ItemLike)Blocks.SPRUCE_PLANKS);
/*  140 */     woodenBoat((ItemLike)Items.MANGROVE_BOAT, (ItemLike)Blocks.MANGROVE_PLANKS);
/*  142 */     shelf((ItemLike)Blocks.ACACIA_SHELF, (ItemLike)Items.STRIPPED_ACACIA_LOG);
/*  143 */     shelf((ItemLike)Blocks.BAMBOO_SHELF, (ItemLike)Items.STRIPPED_BAMBOO_BLOCK);
/*  144 */     shelf((ItemLike)Blocks.BIRCH_SHELF, (ItemLike)Items.STRIPPED_BIRCH_LOG);
/*  145 */     shelf((ItemLike)Blocks.CHERRY_SHELF, (ItemLike)Items.STRIPPED_CHERRY_LOG);
/*  146 */     shelf((ItemLike)Blocks.CRIMSON_SHELF, (ItemLike)Items.STRIPPED_CRIMSON_STEM);
/*  147 */     shelf((ItemLike)Blocks.DARK_OAK_SHELF, (ItemLike)Items.STRIPPED_DARK_OAK_LOG);
/*  148 */     shelf((ItemLike)Blocks.JUNGLE_SHELF, (ItemLike)Items.STRIPPED_JUNGLE_LOG);
/*  149 */     shelf((ItemLike)Blocks.MANGROVE_SHELF, (ItemLike)Items.STRIPPED_MANGROVE_LOG);
/*  150 */     shelf((ItemLike)Blocks.OAK_SHELF, (ItemLike)Items.STRIPPED_OAK_LOG);
/*  151 */     shelf((ItemLike)Blocks.PALE_OAK_SHELF, (ItemLike)Items.STRIPPED_PALE_OAK_LOG);
/*  152 */     shelf((ItemLike)Blocks.SPRUCE_SHELF, (ItemLike)Items.STRIPPED_SPRUCE_LOG);
/*  153 */     shelf((ItemLike)Blocks.WARPED_SHELF, (ItemLike)Items.STRIPPED_WARPED_STEM);
/*  155 */     List<Item> dyes = Items.DYE.asList();
/*  157 */     colorItemWithDye(dyes, Items.WOOL.asList(), "wool", RecipeCategory.BUILDING_BLOCKS);
/*  158 */     colorItemWithDye(dyes, Items.BED.asList(), "bed_dye", RecipeCategory.DECORATIONS);
/*  159 */     colorItemWithDye(dyes, Items.CARPET.asList(), "carpet_dye", RecipeCategory.DECORATIONS);
/*  160 */     colorItemWithDye(dyes, Items.HARNESS.asList(), "harness_dye", RecipeCategory.COMBAT);
/*  162 */     VanillaRecipeProvider vanillaRecipeProvider = this;
/*  162 */     ColorCollection.zipApply(Blocks.CARPET, Blocks.WOOL, (x$0, x$1) -> rec$.carpet((ItemLike)x$0, (ItemLike)x$1));
/*  163 */     vanillaRecipeProvider = this;
/*  163 */     ColorCollection.zipApply(Items.BED, Blocks.WOOL, (x$0, x$1) -> rec$.bedFromPlanksAndWool((ItemLike)x$0, (ItemLike)x$1));
/*  164 */     vanillaRecipeProvider = this;
/*  164 */     ColorCollection.zipApply(Items.BANNER, Blocks.WOOL, (x$0, x$1) -> rec$.banner((ItemLike)x$0, (ItemLike)x$1));
/*  166 */     carpet((ItemLike)Blocks.MOSS_CARPET, (ItemLike)Blocks.MOSS_BLOCK);
/*  167 */     carpet((ItemLike)Blocks.PALE_MOSS_CARPET, (ItemLike)Blocks.PALE_MOSS_BLOCK);
/*  169 */     vanillaRecipeProvider = this;
/*  169 */     ColorCollection.zipApply(Items.HARNESS, Blocks.WOOL, (x$0, x$1) -> rec$.harness((ItemLike)x$0, (ItemLike)x$1));
/*  171 */     vanillaRecipeProvider = this;
/*  171 */     ColorCollection.zipApply(Blocks.STAINED_GLASS, Items.DYE, (x$0, x$1) -> rec$.stainedGlassFromGlassAndDye((ItemLike)x$0, (ItemLike)x$1));
/*  172 */     vanillaRecipeProvider = this;
/*  172 */     ColorCollection.zipApply(Blocks.STAINED_GLASS_PANE, Blocks.STAINED_GLASS, (x$0, x$1) -> rec$.stainedGlassPaneFromStainedGlass((ItemLike)x$0, (ItemLike)x$1));
/*  173 */     vanillaRecipeProvider = this;
/*  173 */     ColorCollection.zipApply(Blocks.STAINED_GLASS_PANE, Items.DYE, (x$0, x$1) -> rec$.stainedGlassPaneFromGlassPaneAndDye((ItemLike)x$0, (ItemLike)x$1));
/*  175 */     vanillaRecipeProvider = this;
/*  175 */     ColorCollection.zipApply(Blocks.DYED_TERRACOTTA, Items.DYE, (x$0, x$1) -> rec$.coloredTerracottaFromTerracottaAndDye((ItemLike)x$0, (ItemLike)x$1));
/*  177 */     vanillaRecipeProvider = this;
/*  177 */     ColorCollection.zipApply(Blocks.CONCRETE_POWDER, Items.DYE, (x$0, x$1) -> rec$.concretePowder((ItemLike)x$0, (ItemLike)x$1));
/*  179 */     dryGhast((ItemLike)Blocks.DRIED_GHAST);
/*  181 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Items.CANDLE)
/*  182 */       .define(Character.valueOf('S'), (ItemLike)Items.STRING)
/*  183 */       .define(Character.valueOf('H'), (ItemLike)Items.HONEYCOMB)
/*  184 */       .pattern("S")
/*  185 */       .pattern("H")
/*  186 */       .unlockedBy("has_string", has((ItemLike)Items.STRING))
/*  187 */       .unlockedBy("has_honeycomb", has((ItemLike)Items.HONEYCOMB))
/*  188 */       .save(this.output);
/*  190 */     vanillaRecipeProvider = this;
/*  190 */     ColorCollection.zipApply(Blocks.DYED_CANDLE, Items.DYE, (x$0, x$1) -> rec$.candle((ItemLike)x$0, (ItemLike)x$1));
/*  192 */     shapeless(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.PACKED_MUD, 1)
/*  193 */       .requires((ItemLike)Blocks.MUD)
/*  194 */       .requires((ItemLike)Items.WHEAT)
/*  195 */       .unlockedBy("has_mud", has((ItemLike)Blocks.MUD))
/*  196 */       .save(this.output);
/*  198 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.MUD_BRICKS, 4)
/*  199 */       .define(Character.valueOf('#'), (ItemLike)Blocks.PACKED_MUD)
/*  200 */       .pattern("##")
/*  201 */       .pattern("##")
/*  202 */       .unlockedBy("has_packed_mud", has((ItemLike)Blocks.PACKED_MUD))
/*  203 */       .save(this.output);
/*  205 */     shapeless(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.MUDDY_MANGROVE_ROOTS, 1)
/*  206 */       .requires((ItemLike)Blocks.MUD)
/*  207 */       .requires((ItemLike)Items.MANGROVE_ROOTS)
/*  208 */       .unlockedBy("has_mangrove_roots", has((ItemLike)Blocks.MANGROVE_ROOTS))
/*  209 */       .save(this.output);
/*  211 */     shaped(RecipeCategory.TRANSPORTATION, (ItemLike)Blocks.ACTIVATOR_RAIL, 6)
/*  212 */       .define(Character.valueOf('#'), (ItemLike)Blocks.REDSTONE_TORCH)
/*  213 */       .define(Character.valueOf('S'), (ItemLike)Items.STICK)
/*  214 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/*  215 */       .pattern("XSX")
/*  216 */       .pattern("X#X")
/*  217 */       .pattern("XSX")
/*  218 */       .unlockedBy("has_rail", has((ItemLike)Blocks.RAIL))
/*  219 */       .save(this.output);
/*  221 */     shapeless(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.ANDESITE, 2)
/*  222 */       .requires((ItemLike)Blocks.DIORITE)
/*  223 */       .requires((ItemLike)Blocks.COBBLESTONE)
/*  224 */       .unlockedBy("has_stone", has((ItemLike)Blocks.DIORITE))
/*  225 */       .save(this.output);
/*  227 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.ANVIL)
/*  228 */       .define(Character.valueOf('I'), (ItemLike)Blocks.IRON_BLOCK)
/*  229 */       .define(Character.valueOf('i'), (ItemLike)Items.IRON_INGOT)
/*  230 */       .pattern("III")
/*  231 */       .pattern(" i ")
/*  232 */       .pattern("iii")
/*  233 */       .unlockedBy("has_iron_block", has((ItemLike)Blocks.IRON_BLOCK))
/*  234 */       .save(this.output);
/*  236 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Items.ARMOR_STAND)
/*  237 */       .define(Character.valueOf('/'), (ItemLike)Items.STICK)
/*  238 */       .define(Character.valueOf('_'), (ItemLike)Blocks.SMOOTH_STONE_SLAB)
/*  239 */       .pattern("///")
/*  240 */       .pattern(" / ")
/*  241 */       .pattern("/_/")
/*  242 */       .unlockedBy("has_stone_slab", has((ItemLike)Blocks.SMOOTH_STONE_SLAB))
/*  243 */       .save(this.output);
/*  245 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.ARROW, 4)
/*  246 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  247 */       .define(Character.valueOf('X'), (ItemLike)Items.FLINT)
/*  248 */       .define(Character.valueOf('Y'), (ItemLike)Items.FEATHER)
/*  249 */       .pattern("X")
/*  250 */       .pattern("#")
/*  251 */       .pattern("Y")
/*  252 */       .unlockedBy("has_feather", has((ItemLike)Items.FEATHER))
/*  253 */       .unlockedBy("has_flint", has((ItemLike)Items.FLINT))
/*  254 */       .save(this.output);
/*  256 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.BARREL, 1)
/*  257 */       .define(Character.valueOf('P'), ItemTags.PLANKS)
/*  258 */       .define(Character.valueOf('S'), ItemTags.WOODEN_SLABS)
/*  259 */       .pattern("PSP")
/*  260 */       .pattern("P P")
/*  261 */       .pattern("PSP")
/*  262 */       .unlockedBy("has_planks", has(ItemTags.PLANKS))
/*  263 */       .unlockedBy("has_wood_slab", has(ItemTags.WOODEN_SLABS))
/*  264 */       .save(this.output);
/*  266 */     shaped(RecipeCategory.MISC, (ItemLike)Blocks.BEACON)
/*  267 */       .define(Character.valueOf('S'), (ItemLike)Items.NETHER_STAR)
/*  268 */       .define(Character.valueOf('G'), (ItemLike)Blocks.GLASS)
/*  269 */       .define(Character.valueOf('O'), (ItemLike)Blocks.OBSIDIAN)
/*  270 */       .pattern("GGG")
/*  271 */       .pattern("GSG")
/*  272 */       .pattern("OOO")
/*  273 */       .unlockedBy("has_nether_star", has((ItemLike)Items.NETHER_STAR))
/*  274 */       .save(this.output);
/*  276 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.BEEHIVE)
/*  277 */       .define(Character.valueOf('P'), ItemTags.PLANKS)
/*  278 */       .define(Character.valueOf('H'), (ItemLike)Items.HONEYCOMB)
/*  279 */       .pattern("PPP")
/*  280 */       .pattern("HHH")
/*  281 */       .pattern("PPP")
/*  282 */       .unlockedBy("has_honeycomb", has((ItemLike)Items.HONEYCOMB))
/*  283 */       .save(this.output);
/*  285 */     shapeless(RecipeCategory.FOOD, (ItemLike)Items.BEETROOT_SOUP)
/*  286 */       .requires((ItemLike)Items.BOWL)
/*  287 */       .requires((ItemLike)Items.BEETROOT, 6)
/*  288 */       .unlockedBy("has_beetroot", has((ItemLike)Items.BEETROOT))
/*  289 */       .save(this.output);
/*  291 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.black())
/*  292 */       .requires((ItemLike)Items.INK_SAC)
/*  293 */       .group("black_dye")
/*  294 */       .unlockedBy("has_ink_sac", has((ItemLike)Items.INK_SAC))
/*  295 */       .save(this.output);
/*  297 */     oneToOneConversionRecipe((ItemLike)Items.DYE.black(), (ItemLike)Blocks.WITHER_ROSE, "black_dye");
/*  299 */     shapeless(RecipeCategory.BREWING, (ItemLike)Items.BLAZE_POWDER, 2)
/*  300 */       .requires((ItemLike)Items.BLAZE_ROD)
/*  301 */       .unlockedBy("has_blaze_rod", has((ItemLike)Items.BLAZE_ROD))
/*  302 */       .save(this.output);
/*  304 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.blue())
/*  305 */       .requires((ItemLike)Items.LAPIS_LAZULI)
/*  306 */       .group("blue_dye")
/*  307 */       .unlockedBy("has_lapis_lazuli", has((ItemLike)Items.LAPIS_LAZULI))
/*  308 */       .save(this.output);
/*  310 */     oneToOneConversionRecipe((ItemLike)Items.DYE.blue(), (ItemLike)Blocks.CORNFLOWER, "blue_dye");
/*  312 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.BLUE_ICE, (ItemLike)Blocks.PACKED_ICE);
/*  314 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.BONE_MEAL, 3)
/*  315 */       .requires((ItemLike)Items.BONE)
/*  316 */       .group("bonemeal")
/*  317 */       .unlockedBy("has_bone", has((ItemLike)Items.BONE))
/*  318 */       .save(this.output);
/*  320 */     nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory.MISC, (ItemLike)Items.BONE_MEAL, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.BONE_BLOCK, "bone_meal_from_bone_block", "bonemeal");
/*  322 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.BOOK)
/*  323 */       .requires((ItemLike)Items.PAPER, 3)
/*  324 */       .requires((ItemLike)Items.LEATHER)
/*  325 */       .unlockedBy("has_paper", has((ItemLike)Items.PAPER))
/*  326 */       .save(this.output);
/*  328 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.BOOKSHELF)
/*  329 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/*  330 */       .define(Character.valueOf('X'), (ItemLike)Items.BOOK)
/*  331 */       .pattern("###")
/*  332 */       .pattern("XXX")
/*  333 */       .pattern("###")
/*  334 */       .unlockedBy("has_book", has((ItemLike)Items.BOOK))
/*  335 */       .save(this.output);
/*  337 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.BOW)
/*  338 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  339 */       .define(Character.valueOf('X'), (ItemLike)Items.STRING)
/*  340 */       .pattern(" #X")
/*  341 */       .pattern("# X")
/*  342 */       .pattern(" #X")
/*  343 */       .unlockedBy("has_string", has((ItemLike)Items.STRING))
/*  344 */       .save(this.output);
/*  346 */     shaped(RecipeCategory.MISC, (ItemLike)Items.BOWL, 4)
/*  347 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/*  348 */       .pattern("# #")
/*  349 */       .pattern(" # ")
/*  350 */       .unlockedBy("has_brown_mushroom", has((ItemLike)Blocks.BROWN_MUSHROOM))
/*  351 */       .unlockedBy("has_red_mushroom", has((ItemLike)Blocks.RED_MUSHROOM))
/*  352 */       .unlockedBy("has_mushroom_stew", has((ItemLike)Items.MUSHROOM_STEW))
/*  353 */       .save(this.output);
/*  355 */     shaped(RecipeCategory.FOOD, (ItemLike)Items.BREAD)
/*  356 */       .define(Character.valueOf('#'), (ItemLike)Items.WHEAT)
/*  357 */       .pattern("###")
/*  358 */       .unlockedBy("has_wheat", has((ItemLike)Items.WHEAT))
/*  359 */       .save(this.output);
/*  361 */     shaped(RecipeCategory.BREWING, (ItemLike)Blocks.BREWING_STAND)
/*  362 */       .define(Character.valueOf('B'), (ItemLike)Items.BLAZE_ROD)
/*  363 */       .define(Character.valueOf('#'), ItemTags.STONE_CRAFTING_MATERIALS)
/*  364 */       .pattern(" B ")
/*  365 */       .pattern("###")
/*  366 */       .unlockedBy("has_blaze_rod", has((ItemLike)Items.BLAZE_ROD))
/*  367 */       .save(this.output);
/*  369 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.BRICKS)
/*  370 */       .define(Character.valueOf('#'), (ItemLike)Items.BRICK)
/*  371 */       .pattern("##")
/*  372 */       .pattern("##")
/*  373 */       .unlockedBy("has_brick", has((ItemLike)Items.BRICK))
/*  374 */       .save(this.output);
/*  376 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.brown())
/*  377 */       .requires((ItemLike)Items.COCOA_BEANS)
/*  378 */       .group("brown_dye")
/*  379 */       .unlockedBy("has_cocoa_beans", has((ItemLike)Items.COCOA_BEANS))
/*  380 */       .save(this.output);
/*  382 */     shaped(RecipeCategory.MISC, (ItemLike)Items.BUCKET)
/*  383 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/*  384 */       .pattern("# #")
/*  385 */       .pattern(" # ")
/*  386 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/*  387 */       .save(this.output);
/*  389 */     shaped(RecipeCategory.FOOD, (ItemLike)Blocks.CAKE)
/*  390 */       .define(Character.valueOf('A'), (ItemLike)Items.MILK_BUCKET)
/*  391 */       .define(Character.valueOf('B'), (ItemLike)Items.SUGAR)
/*  392 */       .define(Character.valueOf('C'), (ItemLike)Items.WHEAT)
/*  393 */       .define(Character.valueOf('E'), ItemTags.EGGS)
/*  394 */       .pattern("AAA")
/*  395 */       .pattern("BEB")
/*  396 */       .pattern("CCC")
/*  397 */       .unlockedBy("has_egg", has(ItemTags.EGGS))
/*  398 */       .save(this.output);
/*  400 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.CAMPFIRE)
/*  401 */       .define(Character.valueOf('L'), ItemTags.LOGS)
/*  402 */       .define(Character.valueOf('S'), (ItemLike)Items.STICK)
/*  403 */       .define(Character.valueOf('C'), ItemTags.COALS)
/*  404 */       .pattern(" S ")
/*  405 */       .pattern("SCS")
/*  406 */       .pattern("LLL")
/*  407 */       .unlockedBy("has_stick", has((ItemLike)Items.STICK))
/*  408 */       .unlockedBy("has_coal", has(ItemTags.COALS))
/*  409 */       .save(this.output);
/*  411 */     shaped(RecipeCategory.TRANSPORTATION, (ItemLike)Items.CARROT_ON_A_STICK)
/*  412 */       .define(Character.valueOf('#'), (ItemLike)Items.FISHING_ROD)
/*  413 */       .define(Character.valueOf('X'), (ItemLike)Items.CARROT)
/*  414 */       .pattern("# ")
/*  415 */       .pattern(" X")
/*  416 */       .unlockedBy("has_carrot", has((ItemLike)Items.CARROT))
/*  417 */       .save(this.output);
/*  419 */     shaped(RecipeCategory.TRANSPORTATION, (ItemLike)Items.WARPED_FUNGUS_ON_A_STICK)
/*  420 */       .define(Character.valueOf('#'), (ItemLike)Items.FISHING_ROD)
/*  421 */       .define(Character.valueOf('X'), (ItemLike)Items.WARPED_FUNGUS)
/*  422 */       .pattern("# ")
/*  423 */       .pattern(" X")
/*  424 */       .unlockedBy("has_warped_fungus", has((ItemLike)Items.WARPED_FUNGUS))
/*  425 */       .save(this.output);
/*  427 */     shaped(RecipeCategory.BREWING, (ItemLike)Blocks.CAULDRON)
/*  428 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/*  429 */       .pattern("# #")
/*  430 */       .pattern("# #")
/*  431 */       .pattern("###")
/*  432 */       .unlockedBy("has_water_bucket", has((ItemLike)Items.WATER_BUCKET))
/*  433 */       .save(this.output);
/*  435 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.COMPOSTER)
/*  436 */       .define(Character.valueOf('#'), ItemTags.WOODEN_SLABS)
/*  437 */       .pattern("# #")
/*  438 */       .pattern("# #")
/*  439 */       .pattern("###")
/*  440 */       .unlockedBy("has_wood_slab", has(ItemTags.WOODEN_SLABS))
/*  441 */       .save(this.output);
/*  443 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.CHEST)
/*  444 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/*  445 */       .pattern("###")
/*  446 */       .pattern("# #")
/*  447 */       .pattern("###")
/*  448 */       .unlockedBy("has_lots_of_items", CriteriaTriggers.INVENTORY_CHANGED.createCriterion((CriterionTriggerInstance)new InventoryChangeTrigger.TriggerInstance(Optional.empty(), new InventoryChangeTrigger.TriggerInstance.Slots(MinMaxBounds.Ints.atLeast(10), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY), List.of())))
/*  449 */       .save(this.output);
/*  451 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.COPPER_CHEST.weathering().unaffected())
/*  452 */       .define(Character.valueOf('#'), (ItemLike)Items.COPPER_INGOT)
/*  453 */       .define(Character.valueOf('X'), (ItemLike)Items.CHEST)
/*  454 */       .pattern("###")
/*  455 */       .pattern("#X#")
/*  456 */       .pattern("###")
/*  457 */       .unlockedBy("has_copper_chest", has((ItemLike)Items.COPPER_CHEST.weathering().unaffected()))
/*  458 */       .save(this.output);
/*  460 */     shapeless(RecipeCategory.TRANSPORTATION, (ItemLike)Items.CHEST_MINECART)
/*  461 */       .requires((ItemLike)Blocks.CHEST)
/*  462 */       .requires((ItemLike)Items.MINECART)
/*  463 */       .unlockedBy("has_minecart", has((ItemLike)Items.MINECART))
/*  464 */       .save(this.output);
/*  466 */     chestBoat((ItemLike)Items.ACACIA_CHEST_BOAT, (ItemLike)Items.ACACIA_BOAT);
/*  467 */     chestBoat((ItemLike)Items.BIRCH_CHEST_BOAT, (ItemLike)Items.BIRCH_BOAT);
/*  468 */     chestBoat((ItemLike)Items.DARK_OAK_CHEST_BOAT, (ItemLike)Items.DARK_OAK_BOAT);
/*  469 */     chestBoat((ItemLike)Items.PALE_OAK_CHEST_BOAT, (ItemLike)Items.PALE_OAK_BOAT);
/*  470 */     chestBoat((ItemLike)Items.JUNGLE_CHEST_BOAT, (ItemLike)Items.JUNGLE_BOAT);
/*  471 */     chestBoat((ItemLike)Items.OAK_CHEST_BOAT, (ItemLike)Items.OAK_BOAT);
/*  472 */     chestBoat((ItemLike)Items.SPRUCE_CHEST_BOAT, (ItemLike)Items.SPRUCE_BOAT);
/*  473 */     chestBoat((ItemLike)Items.MANGROVE_CHEST_BOAT, (ItemLike)Items.MANGROVE_BOAT);
/*  475 */     chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.CHISELED_QUARTZ_BLOCK, Ingredient.of((ItemLike)Blocks.QUARTZ_SLAB))
/*  476 */       .unlockedBy("has_chiseled_quartz_block", has((ItemLike)Blocks.CHISELED_QUARTZ_BLOCK))
/*  477 */       .unlockedBy("has_quartz_block", has((ItemLike)Blocks.QUARTZ_BLOCK))
/*  478 */       .unlockedBy("has_quartz_pillar", has((ItemLike)Blocks.QUARTZ_PILLAR))
/*  479 */       .save(this.output);
/*  481 */     chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.CHISELED_STONE_BRICKS, Ingredient.of((ItemLike)Blocks.STONE_BRICK_SLAB))
/*  482 */       .unlockedBy("has_tag", has(ItemTags.STONE_BRICKS))
/*  483 */       .save(this.output);
/*  485 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.CLAY, (ItemLike)Items.CLAY_BALL);
/*  487 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.CLOCK)
/*  488 */       .define(Character.valueOf('#'), (ItemLike)Items.GOLD_INGOT)
/*  489 */       .define(Character.valueOf('X'), (ItemLike)Items.REDSTONE)
/*  490 */       .pattern(" # ")
/*  491 */       .pattern("#X#")
/*  492 */       .pattern(" # ")
/*  493 */       .unlockedBy("has_redstone", has((ItemLike)Items.REDSTONE))
/*  494 */       .save(this.output);
/*  496 */     nineBlockStorageRecipes(RecipeCategory.MISC, (ItemLike)Items.COAL, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.COAL_BLOCK);
/*  498 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.COARSE_DIRT, 4)
/*  499 */       .define(Character.valueOf('D'), (ItemLike)Blocks.DIRT)
/*  500 */       .define(Character.valueOf('G'), (ItemLike)Blocks.GRAVEL)
/*  501 */       .pattern("DG")
/*  502 */       .pattern("GD")
/*  503 */       .unlockedBy("has_gravel", has((ItemLike)Blocks.GRAVEL))
/*  504 */       .save(this.output);
/*  506 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.COMPARATOR)
/*  507 */       .define(Character.valueOf('#'), (ItemLike)Blocks.REDSTONE_TORCH)
/*  508 */       .define(Character.valueOf('X'), (ItemLike)Items.QUARTZ)
/*  509 */       .define(Character.valueOf('I'), (ItemLike)Blocks.STONE)
/*  510 */       .pattern(" # ")
/*  511 */       .pattern("#X#")
/*  512 */       .pattern("III")
/*  513 */       .unlockedBy("has_quartz", has((ItemLike)Items.QUARTZ))
/*  514 */       .save(this.output);
/*  516 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.COMPASS)
/*  517 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/*  518 */       .define(Character.valueOf('X'), (ItemLike)Items.REDSTONE)
/*  519 */       .pattern(" # ")
/*  520 */       .pattern("#X#")
/*  521 */       .pattern(" # ")
/*  522 */       .unlockedBy("has_redstone", has((ItemLike)Items.REDSTONE))
/*  523 */       .save(this.output);
/*  525 */     shaped(RecipeCategory.FOOD, (ItemLike)Items.COOKIE, 8)
/*  526 */       .define(Character.valueOf('#'), (ItemLike)Items.WHEAT)
/*  527 */       .define(Character.valueOf('X'), (ItemLike)Items.COCOA_BEANS)
/*  528 */       .pattern("#X#")
/*  529 */       .unlockedBy("has_cocoa", has((ItemLike)Items.COCOA_BEANS))
/*  530 */       .save(this.output);
/*  532 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.CRAFTING_TABLE)
/*  533 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/*  534 */       .pattern("##")
/*  535 */       .pattern("##")
/*  536 */       .unlockedBy("unlock_right_away", PlayerTrigger.TriggerInstance.tick())
/*  537 */       .showNotification(false)
/*  538 */       .save(this.output);
/*  540 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.CROSSBOW)
/*  541 */       .define(Character.valueOf('~'), (ItemLike)Items.STRING)
/*  542 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  543 */       .define(Character.valueOf('&'), (ItemLike)Items.IRON_INGOT)
/*  544 */       .define(Character.valueOf('$'), (ItemLike)Blocks.TRIPWIRE_HOOK)
/*  545 */       .pattern("#&#")
/*  546 */       .pattern("~$~")
/*  547 */       .pattern(" # ")
/*  548 */       .unlockedBy("has_string", has((ItemLike)Items.STRING))
/*  549 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/*  550 */       .unlockedBy("has_tripwire_hook", has((ItemLike)Blocks.TRIPWIRE_HOOK))
/*  551 */       .save(this.output);
/*  553 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.LOOM)
/*  554 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/*  555 */       .define(Character.valueOf('@'), (ItemLike)Items.STRING)
/*  556 */       .pattern("@@")
/*  557 */       .pattern("##")
/*  558 */       .unlockedBy("has_string", has((ItemLike)Items.STRING))
/*  559 */       .save(this.output);
/*  561 */     chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.CHISELED_RED_SANDSTONE, Ingredient.of((ItemLike)Blocks.RED_SANDSTONE_SLAB))
/*  562 */       .unlockedBy("has_red_sandstone", has((ItemLike)Blocks.RED_SANDSTONE))
/*  563 */       .unlockedBy("has_chiseled_red_sandstone", has((ItemLike)Blocks.CHISELED_RED_SANDSTONE))
/*  564 */       .unlockedBy("has_cut_red_sandstone", has((ItemLike)Blocks.CUT_RED_SANDSTONE))
/*  565 */       .save(this.output);
/*  567 */     chiseled(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.CHISELED_SANDSTONE, (ItemLike)Blocks.SANDSTONE_SLAB);
/*  569 */     nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory.MISC, (ItemLike)Items.COPPER_INGOT, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.COPPER_BLOCK.weathering().unaffected(), getSimpleRecipeName((ItemLike)Items.COPPER_INGOT), getItemName((ItemLike)Items.COPPER_INGOT));
/*  571 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.COPPER_INGOT, 9)
/*  572 */       .requires((ItemLike)Blocks.COPPER_BLOCK.waxed().unaffected())
/*  573 */       .group(getItemName((ItemLike)Items.COPPER_INGOT))
/*  574 */       .unlockedBy(getHasName((ItemLike)Blocks.COPPER_BLOCK.waxed().unaffected()), has((ItemLike)Blocks.COPPER_BLOCK.waxed().unaffected()))
/*  575 */       .save(this.output, getConversionRecipeName((ItemLike)Items.COPPER_INGOT, (ItemLike)Blocks.COPPER_BLOCK.waxed().unaffected()));
/*  577 */     waxRecipes(FeatureFlagSet.of(FeatureFlags.VANILLA));
/*  579 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.cyan(), 2)
/*  580 */       .requires((ItemLike)Items.DYE.blue())
/*  581 */       .requires((ItemLike)Items.DYE.green())
/*  582 */       .group("cyan_dye")
/*  583 */       .unlockedBy("has_green_dye", has((ItemLike)Items.DYE.green()))
/*  584 */       .unlockedBy("has_blue_dye", has((ItemLike)Items.DYE.blue()))
/*  585 */       .save(this.output);
/*  587 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.DARK_PRISMARINE)
/*  588 */       .define(Character.valueOf('S'), (ItemLike)Items.PRISMARINE_SHARD)
/*  589 */       .define(Character.valueOf('I'), (ItemLike)Items.DYE.black())
/*  590 */       .pattern("SSS")
/*  591 */       .pattern("SIS")
/*  592 */       .pattern("SSS")
/*  593 */       .unlockedBy("has_prismarine_shard", has((ItemLike)Items.PRISMARINE_SHARD))
/*  594 */       .save(this.output);
/*  596 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.DAYLIGHT_DETECTOR)
/*  597 */       .define(Character.valueOf('Q'), (ItemLike)Items.QUARTZ)
/*  598 */       .define(Character.valueOf('G'), (ItemLike)Blocks.GLASS)
/*  599 */       .define(Character.valueOf('W'), ItemTags.WOODEN_SLABS)
/*  600 */       .pattern("GGG")
/*  601 */       .pattern("QQQ")
/*  602 */       .pattern("WWW")
/*  603 */       .unlockedBy("has_quartz", has((ItemLike)Items.QUARTZ))
/*  604 */       .save(this.output);
/*  606 */     shaped(RecipeCategory.TRANSPORTATION, (ItemLike)Blocks.DETECTOR_RAIL, 6)
/*  607 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/*  608 */       .define(Character.valueOf('#'), (ItemLike)Blocks.STONE_PRESSURE_PLATE)
/*  609 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/*  610 */       .pattern("X X")
/*  611 */       .pattern("X#X")
/*  612 */       .pattern("XRX")
/*  613 */       .unlockedBy("has_rail", has((ItemLike)Blocks.RAIL))
/*  614 */       .save(this.output);
/*  616 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.DIAMOND_AXE)
/*  617 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  618 */       .define(Character.valueOf('X'), ItemTags.DIAMOND_TOOL_MATERIALS)
/*  619 */       .pattern("XX")
/*  620 */       .pattern("X#")
/*  621 */       .pattern(" #")
/*  622 */       .unlockedBy("has_diamond", has(ItemTags.DIAMOND_TOOL_MATERIALS))
/*  623 */       .save(this.output);
/*  625 */     nineBlockStorageRecipes(RecipeCategory.MISC, (ItemLike)Items.DIAMOND, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.DIAMOND_BLOCK);
/*  627 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.DIAMOND_BOOTS)
/*  628 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/*  629 */       .pattern("X X")
/*  630 */       .pattern("X X")
/*  631 */       .unlockedBy("has_diamond", has((ItemLike)Items.DIAMOND))
/*  632 */       .save(this.output);
/*  634 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.DIAMOND_CHESTPLATE)
/*  635 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/*  636 */       .pattern("X X")
/*  637 */       .pattern("XXX")
/*  638 */       .pattern("XXX")
/*  639 */       .unlockedBy("has_diamond", has((ItemLike)Items.DIAMOND))
/*  640 */       .save(this.output);
/*  642 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.DIAMOND_HELMET)
/*  643 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/*  644 */       .pattern("XXX")
/*  645 */       .pattern("X X")
/*  646 */       .unlockedBy("has_diamond", has((ItemLike)Items.DIAMOND))
/*  647 */       .save(this.output);
/*  649 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.DIAMOND_HOE)
/*  650 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  651 */       .define(Character.valueOf('X'), ItemTags.DIAMOND_TOOL_MATERIALS)
/*  652 */       .pattern("XX")
/*  653 */       .pattern(" #")
/*  654 */       .pattern(" #")
/*  655 */       .unlockedBy("has_diamond", has(ItemTags.DIAMOND_TOOL_MATERIALS))
/*  656 */       .save(this.output);
/*  658 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.DIAMOND_LEGGINGS)
/*  659 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/*  660 */       .pattern("XXX")
/*  661 */       .pattern("X X")
/*  662 */       .pattern("X X")
/*  663 */       .unlockedBy("has_diamond", has((ItemLike)Items.DIAMOND))
/*  664 */       .save(this.output);
/*  666 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.DIAMOND_PICKAXE)
/*  667 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  668 */       .define(Character.valueOf('X'), ItemTags.DIAMOND_TOOL_MATERIALS)
/*  669 */       .pattern("XXX")
/*  670 */       .pattern(" # ")
/*  671 */       .pattern(" # ")
/*  672 */       .unlockedBy("has_diamond", has(ItemTags.DIAMOND_TOOL_MATERIALS))
/*  673 */       .save(this.output);
/*  675 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.DIAMOND_SHOVEL)
/*  676 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  677 */       .define(Character.valueOf('X'), ItemTags.DIAMOND_TOOL_MATERIALS)
/*  678 */       .pattern("X")
/*  679 */       .pattern("#")
/*  680 */       .pattern("#")
/*  681 */       .unlockedBy("has_diamond", has(ItemTags.DIAMOND_TOOL_MATERIALS))
/*  682 */       .save(this.output);
/*  684 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.DIAMOND_SWORD)
/*  685 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  686 */       .define(Character.valueOf('X'), ItemTags.DIAMOND_TOOL_MATERIALS)
/*  687 */       .pattern("X")
/*  688 */       .pattern("X")
/*  689 */       .pattern("#")
/*  690 */       .unlockedBy("has_diamond", has(ItemTags.DIAMOND_TOOL_MATERIALS))
/*  691 */       .save(this.output);
/*  693 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.DIAMOND_SPEAR)
/*  694 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  695 */       .define(Character.valueOf('X'), ItemTags.DIAMOND_TOOL_MATERIALS)
/*  696 */       .pattern("  X")
/*  697 */       .pattern(" # ")
/*  698 */       .pattern("#  ")
/*  699 */       .unlockedBy("has_diamond", has(ItemTags.DIAMOND_TOOL_MATERIALS))
/*  700 */       .save(this.output);
/*  702 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.DIORITE, 2)
/*  703 */       .define(Character.valueOf('Q'), (ItemLike)Items.QUARTZ)
/*  704 */       .define(Character.valueOf('C'), (ItemLike)Blocks.COBBLESTONE)
/*  705 */       .pattern("CQ")
/*  706 */       .pattern("QC")
/*  707 */       .unlockedBy("has_quartz", has((ItemLike)Items.QUARTZ))
/*  708 */       .save(this.output);
/*  710 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.DISPENSER)
/*  711 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/*  712 */       .define(Character.valueOf('#'), (ItemLike)Blocks.COBBLESTONE)
/*  713 */       .define(Character.valueOf('X'), (ItemLike)Items.BOW)
/*  714 */       .pattern("###")
/*  715 */       .pattern("#X#")
/*  716 */       .pattern("#R#")
/*  717 */       .unlockedBy("has_bow", has((ItemLike)Items.BOW))
/*  718 */       .save(this.output);
/*  720 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.DRIPSTONE_BLOCK, (ItemLike)Items.POINTED_DRIPSTONE);
/*  721 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.SULFUR, 1)
/*  722 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SULFUR_SPIKE)
/*  723 */       .pattern("##")
/*  724 */       .pattern("##")
/*  725 */       .unlockedBy(getHasName((ItemLike)Blocks.SULFUR_SPIKE), has((ItemLike)Blocks.SULFUR_SPIKE))
/*  726 */       .save(this.output, "sulfur_from_sulfur_spikes");
/*  728 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.DROPPER)
/*  729 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/*  730 */       .define(Character.valueOf('#'), (ItemLike)Blocks.COBBLESTONE)
/*  731 */       .pattern("###")
/*  732 */       .pattern("# #")
/*  733 */       .pattern("#R#")
/*  734 */       .unlockedBy("has_redstone", has((ItemLike)Items.REDSTONE))
/*  735 */       .save(this.output);
/*  737 */     nineBlockStorageRecipes(RecipeCategory.MISC, (ItemLike)Items.EMERALD, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.EMERALD_BLOCK);
/*  739 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.ENCHANTING_TABLE)
/*  740 */       .define(Character.valueOf('B'), (ItemLike)Items.BOOK)
/*  741 */       .define(Character.valueOf('#'), (ItemLike)Blocks.OBSIDIAN)
/*  742 */       .define(Character.valueOf('D'), (ItemLike)Items.DIAMOND)
/*  743 */       .pattern(" B ")
/*  744 */       .pattern("D#D")
/*  745 */       .pattern("###")
/*  746 */       .unlockedBy("has_obsidian", has((ItemLike)Blocks.OBSIDIAN))
/*  747 */       .save(this.output);
/*  749 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.ENDER_CHEST)
/*  750 */       .define(Character.valueOf('#'), (ItemLike)Blocks.OBSIDIAN)
/*  751 */       .define(Character.valueOf('E'), (ItemLike)Items.ENDER_EYE)
/*  752 */       .pattern("###")
/*  753 */       .pattern("#E#")
/*  754 */       .pattern("###")
/*  755 */       .unlockedBy("has_ender_eye", has((ItemLike)Items.ENDER_EYE))
/*  756 */       .save(this.output);
/*  758 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.ENDER_EYE)
/*  759 */       .requires((ItemLike)Items.ENDER_PEARL)
/*  760 */       .requires((ItemLike)Items.BLAZE_POWDER)
/*  761 */       .unlockedBy("has_blaze_powder", has((ItemLike)Items.BLAZE_POWDER))
/*  762 */       .save(this.output);
/*  764 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Items.END_CRYSTAL)
/*  765 */       .define(Character.valueOf('T'), (ItemLike)Items.GHAST_TEAR)
/*  766 */       .define(Character.valueOf('E'), (ItemLike)Items.ENDER_EYE)
/*  767 */       .define(Character.valueOf('G'), (ItemLike)Blocks.GLASS)
/*  768 */       .pattern("GGG")
/*  769 */       .pattern("GEG")
/*  770 */       .pattern("GTG")
/*  771 */       .unlockedBy("has_ender_eye", has((ItemLike)Items.ENDER_EYE))
/*  772 */       .save(this.output);
/*  774 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.END_ROD, 4)
/*  775 */       .define(Character.valueOf('#'), (ItemLike)Items.POPPED_CHORUS_FRUIT)
/*  776 */       .define(Character.valueOf('/'), (ItemLike)Items.BLAZE_ROD)
/*  777 */       .pattern("/")
/*  778 */       .pattern("#")
/*  779 */       .unlockedBy("has_chorus_fruit_popped", has((ItemLike)Items.POPPED_CHORUS_FRUIT))
/*  780 */       .save(this.output);
/*  782 */     shapeless(RecipeCategory.BREWING, (ItemLike)Items.FERMENTED_SPIDER_EYE)
/*  783 */       .requires((ItemLike)Items.SPIDER_EYE)
/*  784 */       .requires((ItemLike)Blocks.BROWN_MUSHROOM)
/*  785 */       .requires((ItemLike)Items.SUGAR)
/*  786 */       .unlockedBy("has_spider_eye", has((ItemLike)Items.SPIDER_EYE))
/*  787 */       .save(this.output);
/*  789 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.FIRE_CHARGE, 3)
/*  790 */       .requires((ItemLike)Items.GUNPOWDER)
/*  791 */       .requires((ItemLike)Items.BLAZE_POWDER)
/*  792 */       .requires(Ingredient.of(new ItemLike[] { (ItemLike)Items.COAL, (ItemLike)Items.CHARCOAL })).unlockedBy("has_blaze_powder", has((ItemLike)Items.BLAZE_POWDER))
/*  794 */       .save(this.output);
/*  796 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.FIREWORK_ROCKET, 3)
/*  797 */       .requires((ItemLike)Items.GUNPOWDER)
/*  798 */       .requires((ItemLike)Items.PAPER)
/*  799 */       .unlockedBy("has_gunpowder", has((ItemLike)Items.GUNPOWDER))
/*  800 */       .save(this.output, "firework_rocket_simple");
/*  802 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.FISHING_ROD)
/*  803 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  804 */       .define(Character.valueOf('X'), (ItemLike)Items.STRING)
/*  805 */       .pattern("  #")
/*  806 */       .pattern(" #X")
/*  807 */       .pattern("# X")
/*  808 */       .unlockedBy("has_string", has((ItemLike)Items.STRING))
/*  809 */       .save(this.output);
/*  811 */     shapeless(RecipeCategory.TOOLS, (ItemLike)Items.FLINT_AND_STEEL)
/*  812 */       .requires((ItemLike)Items.IRON_INGOT)
/*  813 */       .requires((ItemLike)Items.FLINT)
/*  814 */       .unlockedBy("has_flint", has((ItemLike)Items.FLINT))
/*  815 */       .unlockedBy("has_obsidian", has((ItemLike)Blocks.OBSIDIAN))
/*  816 */       .save(this.output);
/*  818 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.FLOWER_POT)
/*  819 */       .define(Character.valueOf('#'), (ItemLike)Items.BRICK)
/*  820 */       .pattern("# #")
/*  821 */       .pattern(" # ")
/*  822 */       .unlockedBy("has_brick", has((ItemLike)Items.BRICK))
/*  823 */       .save(this.output);
/*  825 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.FURNACE)
/*  826 */       .define(Character.valueOf('#'), ItemTags.STONE_CRAFTING_MATERIALS)
/*  827 */       .pattern("###")
/*  828 */       .pattern("# #")
/*  829 */       .pattern("###")
/*  830 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_CRAFTING_MATERIALS))
/*  831 */       .save(this.output);
/*  833 */     shapeless(RecipeCategory.TRANSPORTATION, (ItemLike)Items.FURNACE_MINECART)
/*  834 */       .requires((ItemLike)Blocks.FURNACE)
/*  835 */       .requires((ItemLike)Items.MINECART)
/*  836 */       .unlockedBy("has_minecart", has((ItemLike)Items.MINECART))
/*  837 */       .save(this.output);
/*  839 */     shaped(RecipeCategory.BREWING, (ItemLike)Items.GLASS_BOTTLE, 3)
/*  840 */       .define(Character.valueOf('#'), (ItemLike)Blocks.GLASS)
/*  841 */       .pattern("# #")
/*  842 */       .pattern(" # ")
/*  843 */       .unlockedBy("has_glass", has((ItemLike)Blocks.GLASS))
/*  844 */       .save(this.output);
/*  846 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.GLASS_PANE, 16)
/*  847 */       .define(Character.valueOf('#'), (ItemLike)Blocks.GLASS)
/*  848 */       .pattern("###")
/*  849 */       .pattern("###")
/*  850 */       .unlockedBy("has_glass", has((ItemLike)Blocks.GLASS))
/*  851 */       .save(this.output);
/*  853 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.GLOWSTONE, (ItemLike)Items.GLOWSTONE_DUST);
/*  855 */     shapeless(RecipeCategory.DECORATIONS, (ItemLike)Items.GLOW_ITEM_FRAME)
/*  856 */       .requires((ItemLike)Items.ITEM_FRAME)
/*  857 */       .requires((ItemLike)Items.GLOW_INK_SAC)
/*  858 */       .unlockedBy("has_item_frame", has((ItemLike)Items.ITEM_FRAME))
/*  859 */       .unlockedBy("has_glow_ink_sac", has((ItemLike)Items.GLOW_INK_SAC))
/*  860 */       .save(this.output);
/*  862 */     shaped(RecipeCategory.FOOD, (ItemLike)Items.GOLDEN_APPLE)
/*  863 */       .define(Character.valueOf('#'), (ItemLike)Items.GOLD_INGOT)
/*  864 */       .define(Character.valueOf('X'), (ItemLike)Items.APPLE)
/*  865 */       .pattern("###")
/*  866 */       .pattern("#X#")
/*  867 */       .pattern("###")
/*  868 */       .unlockedBy("has_gold_ingot", has((ItemLike)Items.GOLD_INGOT))
/*  869 */       .save(this.output);
/*  871 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.GOLDEN_AXE)
/*  872 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  873 */       .define(Character.valueOf('X'), ItemTags.GOLD_TOOL_MATERIALS)
/*  874 */       .pattern("XX")
/*  875 */       .pattern("X#")
/*  876 */       .pattern(" #")
/*  877 */       .unlockedBy("has_gold_ingot", has(ItemTags.GOLD_TOOL_MATERIALS))
/*  878 */       .save(this.output);
/*  880 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.GOLDEN_BOOTS)
/*  881 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/*  882 */       .pattern("X X")
/*  883 */       .pattern("X X")
/*  884 */       .unlockedBy("has_gold_ingot", has((ItemLike)Items.GOLD_INGOT))
/*  885 */       .save(this.output);
/*  887 */     shaped(RecipeCategory.BREWING, (ItemLike)Items.GOLDEN_CARROT)
/*  888 */       .define(Character.valueOf('#'), (ItemLike)Items.GOLD_NUGGET)
/*  889 */       .define(Character.valueOf('X'), (ItemLike)Items.CARROT)
/*  890 */       .pattern("###")
/*  891 */       .pattern("#X#")
/*  892 */       .pattern("###")
/*  893 */       .unlockedBy("has_gold_nugget", has((ItemLike)Items.GOLD_NUGGET))
/*  894 */       .save(this.output);
/*  896 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.GOLDEN_CHESTPLATE)
/*  897 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/*  898 */       .pattern("X X")
/*  899 */       .pattern("XXX")
/*  900 */       .pattern("XXX")
/*  901 */       .unlockedBy("has_gold_ingot", has((ItemLike)Items.GOLD_INGOT))
/*  902 */       .save(this.output);
/*  904 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.GOLDEN_HELMET)
/*  905 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/*  906 */       .pattern("XXX")
/*  907 */       .pattern("X X")
/*  908 */       .unlockedBy("has_gold_ingot", has((ItemLike)Items.GOLD_INGOT))
/*  909 */       .save(this.output);
/*  911 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.GOLDEN_HOE)
/*  912 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  913 */       .define(Character.valueOf('X'), ItemTags.GOLD_TOOL_MATERIALS)
/*  914 */       .pattern("XX")
/*  915 */       .pattern(" #")
/*  916 */       .pattern(" #")
/*  917 */       .unlockedBy("has_gold_ingot", has(ItemTags.GOLD_TOOL_MATERIALS))
/*  918 */       .save(this.output);
/*  920 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.GOLDEN_LEGGINGS)
/*  921 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/*  922 */       .pattern("XXX")
/*  923 */       .pattern("X X")
/*  924 */       .pattern("X X")
/*  925 */       .unlockedBy("has_gold_ingot", has((ItemLike)Items.GOLD_INGOT))
/*  926 */       .save(this.output);
/*  928 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.GOLDEN_PICKAXE)
/*  929 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  930 */       .define(Character.valueOf('X'), ItemTags.GOLD_TOOL_MATERIALS)
/*  931 */       .pattern("XXX")
/*  932 */       .pattern(" # ")
/*  933 */       .pattern(" # ")
/*  934 */       .unlockedBy("has_gold_ingot", has(ItemTags.GOLD_TOOL_MATERIALS))
/*  935 */       .save(this.output);
/*  937 */     shaped(RecipeCategory.TRANSPORTATION, (ItemLike)Blocks.POWERED_RAIL, 6)
/*  938 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/*  939 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  940 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/*  941 */       .pattern("X X")
/*  942 */       .pattern("X#X")
/*  943 */       .pattern("XRX")
/*  944 */       .unlockedBy("has_rail", has((ItemLike)Blocks.RAIL))
/*  945 */       .save(this.output);
/*  947 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.GOLDEN_SHOVEL)
/*  948 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  949 */       .define(Character.valueOf('X'), ItemTags.GOLD_TOOL_MATERIALS)
/*  950 */       .pattern("X")
/*  951 */       .pattern("#")
/*  952 */       .pattern("#")
/*  953 */       .unlockedBy("has_gold_ingot", has(ItemTags.GOLD_TOOL_MATERIALS))
/*  954 */       .save(this.output);
/*  956 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.GOLDEN_SWORD)
/*  957 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  958 */       .define(Character.valueOf('X'), ItemTags.GOLD_TOOL_MATERIALS)
/*  959 */       .pattern("X")
/*  960 */       .pattern("X")
/*  961 */       .pattern("#")
/*  962 */       .unlockedBy("has_gold_ingot", has(ItemTags.GOLD_TOOL_MATERIALS))
/*  963 */       .save(this.output);
/*  965 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.GOLDEN_SPEAR)
/*  966 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  967 */       .define(Character.valueOf('X'), ItemTags.GOLD_TOOL_MATERIALS)
/*  968 */       .pattern("  X")
/*  969 */       .pattern(" # ")
/*  970 */       .pattern("#  ")
/*  971 */       .unlockedBy("has_gold_ingot", has(ItemTags.GOLD_TOOL_MATERIALS))
/*  972 */       .save(this.output);
/*  974 */     nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory.MISC, (ItemLike)Items.GOLD_INGOT, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.GOLD_BLOCK, "gold_ingot_from_gold_block", "gold_ingot");
/*  975 */     nineBlockStorageRecipesWithCustomPacking(RecipeCategory.MISC, (ItemLike)Items.GOLD_NUGGET, RecipeCategory.MISC, (ItemLike)Items.GOLD_INGOT, "gold_ingot_from_nuggets", "gold_ingot");
/*  977 */     shapeless(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.GRANITE)
/*  978 */       .requires((ItemLike)Blocks.DIORITE)
/*  979 */       .requires((ItemLike)Items.QUARTZ)
/*  980 */       .unlockedBy("has_quartz", has((ItemLike)Items.QUARTZ))
/*  981 */       .save(this.output);
/*  983 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.gray(), 2)
/*  984 */       .requires((ItemLike)Items.DYE.black())
/*  985 */       .requires((ItemLike)Items.DYE.white())
/*  986 */       .group("gray_dye")
/*  987 */       .unlockedBy("has_white_dye", has((ItemLike)Items.DYE.white()))
/*  988 */       .unlockedBy("has_black_dye", has((ItemLike)Items.DYE.black()))
/*  989 */       .save(this.output);
/*  991 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.HAY_BLOCK, (ItemLike)Items.WHEAT);
/*  993 */     pressurePlate((ItemLike)Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, (ItemLike)Items.IRON_INGOT);
/*  995 */     shapeless(RecipeCategory.FOOD, (ItemLike)Items.HONEY_BOTTLE, 4)
/*  996 */       .requires((ItemLike)Items.HONEY_BLOCK)
/*  997 */       .requires((ItemLike)Items.GLASS_BOTTLE, 4)
/*  998 */       .unlockedBy("has_honey_block", has((ItemLike)Blocks.HONEY_BLOCK))
/*  999 */       .save(this.output);
/* 1001 */     twoByTwoPacker(RecipeCategory.REDSTONE, (ItemLike)Blocks.HONEY_BLOCK, (ItemLike)Items.HONEY_BOTTLE);
/* 1003 */     twoByTwoPacker(RecipeCategory.DECORATIONS, (ItemLike)Blocks.HONEYCOMB_BLOCK, (ItemLike)Items.HONEYCOMB);
/* 1005 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.HOPPER)
/* 1006 */       .define(Character.valueOf('C'), (ItemLike)Blocks.CHEST)
/* 1007 */       .define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT)
/* 1008 */       .pattern("I I")
/* 1009 */       .pattern("ICI")
/* 1010 */       .pattern(" I ")
/* 1011 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/* 1012 */       .save(this.output);
/* 1014 */     shapeless(RecipeCategory.TRANSPORTATION, (ItemLike)Items.HOPPER_MINECART)
/* 1015 */       .requires((ItemLike)Blocks.HOPPER)
/* 1016 */       .requires((ItemLike)Items.MINECART)
/* 1017 */       .unlockedBy("has_minecart", has((ItemLike)Items.MINECART))
/* 1018 */       .save(this.output);
/* 1020 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.IRON_AXE)
/* 1021 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1022 */       .define(Character.valueOf('X'), ItemTags.IRON_TOOL_MATERIALS)
/* 1023 */       .pattern("XX")
/* 1024 */       .pattern("X#")
/* 1025 */       .pattern(" #")
/* 1026 */       .unlockedBy("has_iron_ingot", has(ItemTags.IRON_TOOL_MATERIALS))
/* 1027 */       .save(this.output);
/* 1029 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.IRON_BARS, 16)
/* 1030 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/* 1031 */       .pattern("###")
/* 1032 */       .pattern("###")
/* 1033 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/* 1034 */       .save(this.output);
/* 1036 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.COPPER_BARS.weathering().unaffected(), 16)
/* 1037 */       .define(Character.valueOf('#'), (ItemLike)Items.COPPER_INGOT)
/* 1038 */       .pattern("###")
/* 1039 */       .pattern("###")
/* 1040 */       .unlockedBy("has_copper_ingot", has((ItemLike)Items.COPPER_INGOT))
/* 1041 */       .save(this.output);
/* 1043 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.IRON_BOOTS)
/* 1044 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1045 */       .pattern("X X")
/* 1046 */       .pattern("X X")
/* 1047 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/* 1048 */       .save(this.output);
/* 1050 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.IRON_CHESTPLATE)
/* 1051 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1052 */       .pattern("X X")
/* 1053 */       .pattern("XXX")
/* 1054 */       .pattern("XXX")
/* 1055 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/* 1056 */       .save(this.output);
/* 1058 */     doorBuilder((ItemLike)Blocks.IRON_DOOR, Ingredient.of((ItemLike)Items.IRON_INGOT))
/* 1059 */       .unlockedBy(getHasName((ItemLike)Items.IRON_INGOT), has((ItemLike)Items.IRON_INGOT))
/* 1060 */       .save(this.output);
/* 1062 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.IRON_HELMET)
/* 1063 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1064 */       .pattern("XXX")
/* 1065 */       .pattern("X X")
/* 1066 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/* 1067 */       .save(this.output);
/* 1069 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.IRON_HOE)
/* 1070 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1071 */       .define(Character.valueOf('X'), ItemTags.IRON_TOOL_MATERIALS)
/* 1072 */       .pattern("XX")
/* 1073 */       .pattern(" #")
/* 1074 */       .pattern(" #")
/* 1075 */       .unlockedBy("has_iron_ingot", has(ItemTags.IRON_TOOL_MATERIALS))
/* 1076 */       .save(this.output);
/* 1078 */     nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory.MISC, (ItemLike)Items.IRON_INGOT, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.IRON_BLOCK, "iron_ingot_from_iron_block", "iron_ingot");
/* 1079 */     nineBlockStorageRecipesWithCustomPacking(RecipeCategory.MISC, (ItemLike)Items.IRON_NUGGET, RecipeCategory.MISC, (ItemLike)Items.IRON_INGOT, "iron_ingot_from_nuggets", "iron_ingot");
/* 1081 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.IRON_LEGGINGS)
/* 1082 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1083 */       .pattern("XXX")
/* 1084 */       .pattern("X X")
/* 1085 */       .pattern("X X")
/* 1086 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/* 1087 */       .save(this.output);
/* 1089 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.IRON_PICKAXE)
/* 1090 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1091 */       .define(Character.valueOf('X'), ItemTags.IRON_TOOL_MATERIALS)
/* 1092 */       .pattern("XXX")
/* 1093 */       .pattern(" # ")
/* 1094 */       .pattern(" # ")
/* 1095 */       .unlockedBy("has_iron_ingot", has(ItemTags.IRON_TOOL_MATERIALS))
/* 1096 */       .save(this.output);
/* 1098 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.IRON_SHOVEL)
/* 1099 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1100 */       .define(Character.valueOf('X'), ItemTags.IRON_TOOL_MATERIALS)
/* 1101 */       .pattern("X")
/* 1102 */       .pattern("#")
/* 1103 */       .pattern("#")
/* 1104 */       .unlockedBy("has_iron_ingot", has(ItemTags.IRON_TOOL_MATERIALS))
/* 1105 */       .save(this.output);
/* 1107 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.IRON_SWORD)
/* 1108 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1109 */       .define(Character.valueOf('X'), ItemTags.IRON_TOOL_MATERIALS)
/* 1110 */       .pattern("X")
/* 1111 */       .pattern("X")
/* 1112 */       .pattern("#")
/* 1113 */       .unlockedBy("has_iron_ingot", has(ItemTags.IRON_TOOL_MATERIALS))
/* 1114 */       .save(this.output);
/* 1116 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.IRON_SPEAR)
/* 1117 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1118 */       .define(Character.valueOf('X'), ItemTags.IRON_TOOL_MATERIALS)
/* 1119 */       .pattern("  X")
/* 1120 */       .pattern(" # ")
/* 1121 */       .pattern("#  ")
/* 1122 */       .unlockedBy("has_iron_ingot", has(ItemTags.IRON_TOOL_MATERIALS))
/* 1123 */       .save(this.output);
/* 1125 */     twoByTwoPacker(RecipeCategory.REDSTONE, (ItemLike)Blocks.IRON_TRAPDOOR, (ItemLike)Items.IRON_INGOT);
/* 1127 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Items.ITEM_FRAME)
/* 1128 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1129 */       .define(Character.valueOf('X'), (ItemLike)Items.LEATHER)
/* 1130 */       .pattern("###")
/* 1131 */       .pattern("#X#")
/* 1132 */       .pattern("###")
/* 1133 */       .unlockedBy("has_leather", has((ItemLike)Items.LEATHER))
/* 1134 */       .save(this.output);
/* 1136 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.JUKEBOX)
/* 1137 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 1138 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/* 1139 */       .pattern("###")
/* 1140 */       .pattern("#X#")
/* 1141 */       .pattern("###")
/* 1142 */       .unlockedBy("has_diamond", has((ItemLike)Items.DIAMOND))
/* 1143 */       .save(this.output);
/* 1145 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.LADDER, 3)
/* 1146 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1147 */       .pattern("# #")
/* 1148 */       .pattern("###")
/* 1149 */       .pattern("# #")
/* 1150 */       .unlockedBy("has_stick", has((ItemLike)Items.STICK))
/* 1151 */       .save(this.output);
/* 1153 */     nineBlockStorageRecipes(RecipeCategory.MISC, (ItemLike)Items.LAPIS_LAZULI, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.LAPIS_BLOCK);
/* 1155 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.LEAD, 2)
/* 1156 */       .define(Character.valueOf('~'), (ItemLike)Items.STRING)
/* 1157 */       .pattern("~~ ")
/* 1158 */       .pattern("~~ ")
/* 1159 */       .pattern("  ~")
/* 1160 */       .unlockedBy("has_string", has((ItemLike)Items.STRING))
/* 1161 */       .save(this.output);
/* 1163 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.NAME_TAG)
/* 1164 */       .define(Character.valueOf('X'), ItemTags.METAL_NUGGETS)
/* 1165 */       .define(Character.valueOf('#'), (ItemLike)Items.PAPER)
/* 1166 */       .pattern(" X")
/* 1167 */       .pattern("# ")
/* 1168 */       .unlockedBy("has_metal_nugget", has(ItemTags.METAL_NUGGETS))
/* 1169 */       .unlockedBy("has_paper", has((ItemLike)Items.PAPER))
/* 1170 */       .unlockedBy("has_name_tag", has((ItemLike)Items.NAME_TAG))
/* 1171 */       .save(this.output);
/* 1173 */     twoByTwoPacker(RecipeCategory.MISC, (ItemLike)Items.LEATHER, (ItemLike)Items.RABBIT_HIDE);
/* 1175 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.LEATHER_BOOTS)
/* 1176 */       .define(Character.valueOf('X'), (ItemLike)Items.LEATHER)
/* 1177 */       .pattern("X X")
/* 1178 */       .pattern("X X")
/* 1179 */       .unlockedBy("has_leather", has((ItemLike)Items.LEATHER))
/* 1180 */       .save(this.output);
/* 1182 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.LEATHER_CHESTPLATE)
/* 1183 */       .define(Character.valueOf('X'), (ItemLike)Items.LEATHER)
/* 1184 */       .pattern("X X")
/* 1185 */       .pattern("XXX")
/* 1186 */       .pattern("XXX")
/* 1187 */       .unlockedBy("has_leather", has((ItemLike)Items.LEATHER))
/* 1188 */       .save(this.output);
/* 1190 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.LEATHER_HELMET)
/* 1191 */       .define(Character.valueOf('X'), (ItemLike)Items.LEATHER)
/* 1192 */       .pattern("XXX")
/* 1193 */       .pattern("X X")
/* 1194 */       .unlockedBy("has_leather", has((ItemLike)Items.LEATHER))
/* 1195 */       .save(this.output);
/* 1197 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.LEATHER_LEGGINGS)
/* 1198 */       .define(Character.valueOf('X'), (ItemLike)Items.LEATHER)
/* 1199 */       .pattern("XXX")
/* 1200 */       .pattern("X X")
/* 1201 */       .pattern("X X")
/* 1202 */       .unlockedBy("has_leather", has((ItemLike)Items.LEATHER))
/* 1203 */       .save(this.output);
/* 1205 */     shaped(RecipeCategory.MISC, (ItemLike)Items.LEATHER_HORSE_ARMOR)
/* 1206 */       .define(Character.valueOf('X'), (ItemLike)Items.LEATHER)
/* 1207 */       .pattern("X X")
/* 1208 */       .pattern("XXX")
/* 1209 */       .pattern("X X")
/* 1210 */       .unlockedBy("has_leather", has((ItemLike)Items.LEATHER))
/* 1211 */       .save(this.output);
/* 1213 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.SADDLE)
/* 1214 */       .define(Character.valueOf('X'), (ItemLike)Items.LEATHER)
/* 1215 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/* 1216 */       .pattern(" X ")
/* 1217 */       .pattern("X#X")
/* 1218 */       .unlockedBy("has_leather", has((ItemLike)Items.LEATHER))
/* 1219 */       .save(this.output);
/* 1221 */     nineBlockStorageRecipesWithCustomPacking(RecipeCategory.MISC, (ItemLike)Items.COPPER_NUGGET, RecipeCategory.MISC, (ItemLike)Items.COPPER_INGOT, "copper_ingot_from_nuggets", "copper_ingot");
/* 1223 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.COPPER_AXE)
/* 1224 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1225 */       .define(Character.valueOf('X'), ItemTags.COPPER_TOOL_MATERIALS)
/* 1226 */       .pattern("XX")
/* 1227 */       .pattern("X#")
/* 1228 */       .pattern(" #")
/* 1229 */       .unlockedBy("has_copper_ingot", has(ItemTags.COPPER_TOOL_MATERIALS))
/* 1230 */       .save(this.output);
/* 1232 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.COPPER_HOE)
/* 1233 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1234 */       .define(Character.valueOf('X'), ItemTags.COPPER_TOOL_MATERIALS)
/* 1235 */       .pattern("XX")
/* 1236 */       .pattern(" #")
/* 1237 */       .pattern(" #")
/* 1238 */       .unlockedBy("has_copper_ingot", has(ItemTags.COPPER_TOOL_MATERIALS))
/* 1239 */       .save(this.output);
/* 1241 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.COPPER_PICKAXE)
/* 1242 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1243 */       .define(Character.valueOf('X'), ItemTags.COPPER_TOOL_MATERIALS)
/* 1244 */       .pattern("XXX")
/* 1245 */       .pattern(" # ")
/* 1246 */       .pattern(" # ")
/* 1247 */       .unlockedBy("has_copper_ingot", has(ItemTags.COPPER_TOOL_MATERIALS))
/* 1248 */       .save(this.output);
/* 1250 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.COPPER_SHOVEL)
/* 1251 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1252 */       .define(Character.valueOf('X'), ItemTags.COPPER_TOOL_MATERIALS)
/* 1253 */       .pattern("X")
/* 1254 */       .pattern("#")
/* 1255 */       .pattern("#")
/* 1256 */       .unlockedBy("has_copper_ingot", has(ItemTags.COPPER_TOOL_MATERIALS))
/* 1257 */       .save(this.output);
/* 1259 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.COPPER_SWORD)
/* 1260 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1261 */       .define(Character.valueOf('X'), ItemTags.COPPER_TOOL_MATERIALS)
/* 1262 */       .pattern("X")
/* 1263 */       .pattern("X")
/* 1264 */       .pattern("#")
/* 1265 */       .unlockedBy("has_copper_ingot", has(ItemTags.COPPER_TOOL_MATERIALS))
/* 1266 */       .save(this.output);
/* 1268 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.COPPER_SPEAR)
/* 1269 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1270 */       .define(Character.valueOf('X'), ItemTags.COPPER_TOOL_MATERIALS)
/* 1271 */       .pattern("  X")
/* 1272 */       .pattern(" # ")
/* 1273 */       .pattern("#  ")
/* 1274 */       .unlockedBy("has_copper_ingot", has(ItemTags.COPPER_TOOL_MATERIALS))
/* 1275 */       .save(this.output);
/* 1277 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.COPPER_BOOTS)
/* 1278 */       .define(Character.valueOf('X'), (ItemLike)Items.COPPER_INGOT)
/* 1279 */       .pattern("X X")
/* 1280 */       .pattern("X X")
/* 1281 */       .unlockedBy("has_copper_ingot", has((ItemLike)Items.COPPER_INGOT))
/* 1282 */       .save(this.output);
/* 1284 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.COPPER_CHESTPLATE)
/* 1285 */       .define(Character.valueOf('X'), (ItemLike)Items.COPPER_INGOT)
/* 1286 */       .pattern("X X")
/* 1287 */       .pattern("XXX")
/* 1288 */       .pattern("XXX")
/* 1289 */       .unlockedBy("has_copper_ingot", has((ItemLike)Items.COPPER_INGOT))
/* 1290 */       .save(this.output);
/* 1292 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.COPPER_HELMET)
/* 1293 */       .define(Character.valueOf('X'), (ItemLike)Items.COPPER_INGOT)
/* 1294 */       .pattern("XXX")
/* 1295 */       .pattern("X X")
/* 1296 */       .unlockedBy("has_copper_ingot", has((ItemLike)Items.COPPER_INGOT))
/* 1297 */       .save(this.output);
/* 1299 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.COPPER_LEGGINGS)
/* 1300 */       .define(Character.valueOf('X'), (ItemLike)Items.COPPER_INGOT)
/* 1301 */       .pattern("XXX")
/* 1302 */       .pattern("X X")
/* 1303 */       .pattern("X X")
/* 1304 */       .unlockedBy("has_copper_ingot", has((ItemLike)Items.COPPER_INGOT))
/* 1305 */       .save(this.output);
/* 1307 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.LECTERN)
/* 1308 */       .define(Character.valueOf('S'), ItemTags.WOODEN_SLABS)
/* 1309 */       .define(Character.valueOf('B'), (ItemLike)Blocks.BOOKSHELF)
/* 1310 */       .pattern("SSS")
/* 1311 */       .pattern(" B ")
/* 1312 */       .pattern(" S ")
/* 1313 */       .unlockedBy("has_book", has((ItemLike)Items.BOOK))
/* 1314 */       .save(this.output);
/* 1316 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.LEVER)
/* 1317 */       .define(Character.valueOf('#'), (ItemLike)Blocks.COBBLESTONE)
/* 1318 */       .define(Character.valueOf('X'), (ItemLike)Items.STICK)
/* 1319 */       .pattern("X")
/* 1320 */       .pattern("#")
/* 1321 */       .unlockedBy("has_cobblestone", has((ItemLike)Blocks.COBBLESTONE))
/* 1322 */       .save(this.output);
/* 1324 */     oneToOneConversionRecipe((ItemLike)Items.DYE.lightBlue(), (ItemLike)Blocks.BLUE_ORCHID, "light_blue_dye");
/* 1326 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.lightBlue(), 2)
/* 1327 */       .requires((ItemLike)Items.DYE.blue())
/* 1328 */       .requires((ItemLike)Items.DYE.white())
/* 1329 */       .group("light_blue_dye")
/* 1330 */       .unlockedBy("has_blue_dye", has((ItemLike)Items.DYE.blue()))
/* 1331 */       .unlockedBy("has_white_dye", has((ItemLike)Items.DYE.white()))
/* 1332 */       .save(this.output, "light_blue_dye_from_blue_white_dye");
/* 1334 */     oneToOneConversionRecipe((ItemLike)Items.DYE.lightGray(), (ItemLike)Blocks.AZURE_BLUET, "light_gray_dye");
/* 1336 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.lightGray(), 2)
/* 1337 */       .requires((ItemLike)Items.DYE.gray())
/* 1338 */       .requires((ItemLike)Items.DYE.white())
/* 1339 */       .group("light_gray_dye")
/* 1340 */       .unlockedBy("has_gray_dye", has((ItemLike)Items.DYE.gray()))
/* 1341 */       .unlockedBy("has_white_dye", has((ItemLike)Items.DYE.white()))
/* 1342 */       .save(this.output, "light_gray_dye_from_gray_white_dye");
/* 1344 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.lightGray(), 3)
/* 1345 */       .requires((ItemLike)Items.DYE.black())
/* 1346 */       .requires((ItemLike)Items.DYE.white(), 2)
/* 1347 */       .group("light_gray_dye")
/* 1348 */       .unlockedBy("has_white_dye", has((ItemLike)Items.DYE.white()))
/* 1349 */       .unlockedBy("has_black_dye", has((ItemLike)Items.DYE.black()))
/* 1350 */       .save(this.output, "light_gray_dye_from_black_white_dye");
/* 1352 */     oneToOneConversionRecipe((ItemLike)Items.DYE.lightGray(), (ItemLike)Blocks.OXEYE_DAISY, "light_gray_dye");
/* 1354 */     oneToOneConversionRecipe((ItemLike)Items.DYE.lightGray(), (ItemLike)Blocks.WHITE_TULIP, "light_gray_dye");
/* 1356 */     pressurePlate((ItemLike)Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, (ItemLike)Items.GOLD_INGOT);
/* 1358 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.LIGHTNING_ROD.weathering().unaffected())
/* 1359 */       .define(Character.valueOf('#'), (ItemLike)Items.COPPER_INGOT)
/* 1360 */       .pattern("#")
/* 1361 */       .pattern("#")
/* 1362 */       .pattern("#")
/* 1363 */       .unlockedBy("has_copper_ingot", has((ItemLike)Items.COPPER_INGOT))
/* 1364 */       .save(this.output);
/* 1366 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.lime(), 2)
/* 1367 */       .requires((ItemLike)Items.DYE.green())
/* 1368 */       .requires((ItemLike)Items.DYE.white())
/* 1369 */       .unlockedBy("has_green_dye", has((ItemLike)Items.DYE.green()))
/* 1370 */       .unlockedBy("has_white_dye", has((ItemLike)Items.DYE.white()))
/* 1371 */       .save(this.output);
/* 1373 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.JACK_O_LANTERN)
/* 1374 */       .define(Character.valueOf('A'), (ItemLike)Blocks.CARVED_PUMPKIN)
/* 1375 */       .define(Character.valueOf('B'), (ItemLike)Blocks.TORCH)
/* 1376 */       .pattern("A")
/* 1377 */       .pattern("B")
/* 1378 */       .unlockedBy("has_carved_pumpkin", has((ItemLike)Blocks.CARVED_PUMPKIN))
/* 1379 */       .save(this.output);
/* 1381 */     oneToOneConversionRecipe((ItemLike)Items.DYE.magenta(), (ItemLike)Blocks.ALLIUM, "magenta_dye");
/* 1383 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.magenta(), 4)
/* 1384 */       .requires((ItemLike)Items.DYE.blue())
/* 1385 */       .requires((ItemLike)Items.DYE.red(), 2)
/* 1386 */       .requires((ItemLike)Items.DYE.white())
/* 1387 */       .group("magenta_dye")
/* 1388 */       .unlockedBy("has_blue_dye", has((ItemLike)Items.DYE.blue()))
/* 1389 */       .unlockedBy("has_rose_red", has((ItemLike)Items.DYE.red()))
/* 1390 */       .unlockedBy("has_white_dye", has((ItemLike)Items.DYE.white()))
/* 1391 */       .save(this.output, "magenta_dye_from_blue_red_white_dye");
/* 1393 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.magenta(), 3)
/* 1394 */       .requires((ItemLike)Items.DYE.blue())
/* 1395 */       .requires((ItemLike)Items.DYE.red())
/* 1396 */       .requires((ItemLike)Items.DYE.pink())
/* 1397 */       .group("magenta_dye")
/* 1398 */       .unlockedBy("has_pink_dye", has((ItemLike)Items.DYE.pink()))
/* 1399 */       .unlockedBy("has_blue_dye", has((ItemLike)Items.DYE.blue()))
/* 1400 */       .unlockedBy("has_red_dye", has((ItemLike)Items.DYE.red()))
/* 1401 */       .save(this.output, "magenta_dye_from_blue_red_pink");
/* 1403 */     oneToOneConversionRecipe((ItemLike)Items.DYE.magenta(), (ItemLike)Blocks.LILAC, "magenta_dye", 2);
/* 1405 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.magenta(), 2)
/* 1406 */       .requires((ItemLike)Items.DYE.purple())
/* 1407 */       .requires((ItemLike)Items.DYE.pink())
/* 1408 */       .group("magenta_dye")
/* 1409 */       .unlockedBy("has_pink_dye", has((ItemLike)Items.DYE.pink()))
/* 1410 */       .unlockedBy("has_purple_dye", has((ItemLike)Items.DYE.purple()))
/* 1411 */       .save(this.output, "magenta_dye_from_purple_and_pink");
/* 1413 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.MAGMA_BLOCK, (ItemLike)Items.MAGMA_CREAM);
/* 1415 */     shapeless(RecipeCategory.BREWING, (ItemLike)Items.MAGMA_CREAM)
/* 1416 */       .requires((ItemLike)Items.BLAZE_POWDER)
/* 1417 */       .requires((ItemLike)Items.SLIME_BALL)
/* 1418 */       .unlockedBy("has_blaze_powder", has((ItemLike)Items.BLAZE_POWDER))
/* 1419 */       .save(this.output);
/* 1421 */     shaped(RecipeCategory.MISC, (ItemLike)Items.MAP)
/* 1422 */       .define(Character.valueOf('#'), (ItemLike)Items.PAPER)
/* 1423 */       .define(Character.valueOf('X'), (ItemLike)Items.COMPASS)
/* 1424 */       .pattern("###")
/* 1425 */       .pattern("#X#")
/* 1426 */       .pattern("###")
/* 1427 */       .unlockedBy("has_compass", has((ItemLike)Items.COMPASS))
/* 1428 */       .save(this.output);
/* 1430 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.MELON, (ItemLike)Items.MELON_SLICE, "has_melon");
/* 1432 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.MELON_SEEDS)
/* 1433 */       .requires((ItemLike)Items.MELON_SLICE)
/* 1434 */       .unlockedBy("has_melon", has((ItemLike)Items.MELON_SLICE))
/* 1435 */       .save(this.output);
/* 1437 */     shaped(RecipeCategory.TRANSPORTATION, (ItemLike)Items.MINECART)
/* 1438 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/* 1439 */       .pattern("# #")
/* 1440 */       .pattern("###")
/* 1441 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/* 1442 */       .save(this.output);
/* 1444 */     shapeless(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.MOSSY_COBBLESTONE)
/* 1445 */       .requires((ItemLike)Blocks.COBBLESTONE)
/* 1446 */       .requires((ItemLike)Blocks.VINE)
/* 1447 */       .group("mossy_cobblestone")
/* 1448 */       .unlockedBy("has_vine", has((ItemLike)Blocks.VINE))
/* 1449 */       .save(this.output, getConversionRecipeName((ItemLike)Blocks.MOSSY_COBBLESTONE, (ItemLike)Blocks.VINE));
/* 1451 */     shapeless(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.MOSSY_STONE_BRICKS)
/* 1452 */       .requires((ItemLike)Blocks.STONE_BRICKS)
/* 1453 */       .requires((ItemLike)Blocks.VINE)
/* 1454 */       .group("mossy_stone_bricks")
/* 1455 */       .unlockedBy("has_vine", has((ItemLike)Blocks.VINE))
/* 1456 */       .save(this.output, getConversionRecipeName((ItemLike)Blocks.MOSSY_STONE_BRICKS, (ItemLike)Blocks.VINE));
/* 1458 */     shapeless(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.MOSSY_COBBLESTONE)
/* 1459 */       .requires((ItemLike)Blocks.COBBLESTONE)
/* 1460 */       .requires((ItemLike)Blocks.MOSS_BLOCK)
/* 1461 */       .group("mossy_cobblestone")
/* 1462 */       .unlockedBy("has_moss_block", has((ItemLike)Blocks.MOSS_BLOCK))
/* 1463 */       .save(this.output, getConversionRecipeName((ItemLike)Blocks.MOSSY_COBBLESTONE, (ItemLike)Blocks.MOSS_BLOCK));
/* 1465 */     shapeless(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.MOSSY_STONE_BRICKS)
/* 1466 */       .requires((ItemLike)Blocks.STONE_BRICKS)
/* 1467 */       .requires((ItemLike)Blocks.MOSS_BLOCK)
/* 1468 */       .group("mossy_stone_bricks")
/* 1469 */       .unlockedBy("has_moss_block", has((ItemLike)Blocks.MOSS_BLOCK))
/* 1470 */       .save(this.output, getConversionRecipeName((ItemLike)Blocks.MOSSY_STONE_BRICKS, (ItemLike)Blocks.MOSS_BLOCK));
/* 1472 */     shapeless(RecipeCategory.FOOD, (ItemLike)Items.MUSHROOM_STEW)
/* 1473 */       .requires((ItemLike)Blocks.BROWN_MUSHROOM)
/* 1474 */       .requires((ItemLike)Blocks.RED_MUSHROOM)
/* 1475 */       .requires((ItemLike)Items.BOWL)
/* 1476 */       .unlockedBy("has_mushroom_stew", has((ItemLike)Items.MUSHROOM_STEW))
/* 1477 */       .unlockedBy("has_bowl", has((ItemLike)Items.BOWL))
/* 1478 */       .unlockedBy("has_brown_mushroom", has((ItemLike)Blocks.BROWN_MUSHROOM))
/* 1479 */       .unlockedBy("has_red_mushroom", has((ItemLike)Blocks.RED_MUSHROOM))
/* 1480 */       .save(this.output);
/* 1482 */     BuiltInRegistries.ITEM.stream().forEach(item -> {
/*      */           SuspiciousEffectHolder effectHolder = SuspiciousEffectHolder.tryGet((ItemLike)item);
/*      */           if (effectHolder != null)
/*      */             suspiciousStew(item, effectHolder); 
/*      */         });
/* 1489 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.NETHER_BRICKS, (ItemLike)Items.NETHER_BRICK);
/* 1490 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.RESIN_BRICKS, (ItemLike)Items.RESIN_BRICK);
/* 1491 */     nineBlockStorageRecipes(RecipeCategory.MISC, (ItemLike)Items.RESIN_CLUMP, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.RESIN_BLOCK);
/* 1493 */     shaped(RecipeCategory.MISC, (ItemLike)Blocks.CREAKING_HEART)
/* 1494 */       .define(Character.valueOf('R'), (ItemLike)Items.RESIN_BLOCK)
/* 1495 */       .define(Character.valueOf('L'), (ItemLike)Blocks.PALE_OAK_LOG)
/* 1496 */       .pattern(" L ")
/* 1497 */       .pattern(" R ")
/* 1498 */       .pattern(" L ")
/* 1499 */       .unlockedBy("has_resin_block", has((ItemLike)Items.RESIN_BLOCK))
/* 1500 */       .save(this.output);
/* 1502 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.NETHER_WART_BLOCK, (ItemLike)Items.NETHER_WART);
/* 1504 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.NOTE_BLOCK)
/* 1505 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 1506 */       .define(Character.valueOf('X'), (ItemLike)Items.REDSTONE)
/* 1507 */       .pattern("###")
/* 1508 */       .pattern("#X#")
/* 1509 */       .pattern("###")
/* 1510 */       .unlockedBy("has_redstone", has((ItemLike)Items.REDSTONE))
/* 1511 */       .save(this.output);
/* 1513 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.OBSERVER)
/* 1514 */       .define(Character.valueOf('Q'), (ItemLike)Items.QUARTZ)
/* 1515 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/* 1516 */       .define(Character.valueOf('#'), (ItemLike)Blocks.COBBLESTONE)
/* 1517 */       .pattern("###")
/* 1518 */       .pattern("RRQ")
/* 1519 */       .pattern("###")
/* 1520 */       .unlockedBy("has_quartz", has((ItemLike)Items.QUARTZ))
/* 1521 */       .save(this.output);
/* 1523 */     oneToOneConversionRecipe((ItemLike)Items.DYE.orange(), (ItemLike)Blocks.ORANGE_TULIP, "orange_dye");
/* 1525 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.orange(), 2)
/* 1526 */       .requires((ItemLike)Items.DYE.red())
/* 1527 */       .requires((ItemLike)Items.DYE.yellow())
/* 1528 */       .group("orange_dye")
/* 1529 */       .unlockedBy("has_red_dye", has((ItemLike)Items.DYE.red()))
/* 1530 */       .unlockedBy("has_yellow_dye", has((ItemLike)Items.DYE.yellow()))
/* 1531 */       .save(this.output, "orange_dye_from_red_yellow");
/* 1533 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Items.PAINTING)
/* 1534 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1535 */       .define(Character.valueOf('X'), ItemTags.WOOL)
/* 1536 */       .pattern("###")
/* 1537 */       .pattern("#X#")
/* 1538 */       .pattern("###")
/* 1539 */       .unlockedBy("has_wool", has(ItemTags.WOOL))
/* 1540 */       .save(this.output);
/* 1542 */     shaped(RecipeCategory.MISC, (ItemLike)Items.PAPER, 3)
/* 1543 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SUGAR_CANE)
/* 1544 */       .pattern("###")
/* 1545 */       .unlockedBy("has_reeds", has((ItemLike)Blocks.SUGAR_CANE))
/* 1546 */       .save(this.output);
/* 1548 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.QUARTZ_PILLAR, 2)
/* 1549 */       .define(Character.valueOf('#'), (ItemLike)Blocks.QUARTZ_BLOCK)
/* 1550 */       .pattern("#")
/* 1551 */       .pattern("#")
/* 1552 */       .unlockedBy("has_chiseled_quartz_block", has((ItemLike)Blocks.CHISELED_QUARTZ_BLOCK))
/* 1553 */       .unlockedBy("has_quartz_block", has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 1554 */       .unlockedBy("has_quartz_pillar", has((ItemLike)Blocks.QUARTZ_PILLAR))
/* 1555 */       .save(this.output);
/* 1557 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.PACKED_ICE, (ItemLike)Blocks.ICE);
/* 1559 */     oneToOneConversionRecipe((ItemLike)Items.DYE.pink(), (ItemLike)Blocks.PEONY, "pink_dye", 2);
/* 1560 */     oneToOneConversionRecipe((ItemLike)Items.DYE.pink(), (ItemLike)Blocks.PINK_TULIP, "pink_dye");
/* 1561 */     oneToOneConversionRecipe((ItemLike)Items.DYE.pink(), (ItemLike)Blocks.CACTUS_FLOWER, "pink_dye");
/* 1563 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.pink(), 2)
/* 1564 */       .requires((ItemLike)Items.DYE.red())
/* 1565 */       .requires((ItemLike)Items.DYE.white())
/* 1566 */       .group("pink_dye")
/* 1567 */       .unlockedBy("has_white_dye", has((ItemLike)Items.DYE.white()))
/* 1568 */       .unlockedBy("has_red_dye", has((ItemLike)Items.DYE.red()))
/* 1569 */       .save(this.output, "pink_dye_from_red_white_dye");
/* 1571 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.PISTON)
/* 1572 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/* 1573 */       .define(Character.valueOf('#'), (ItemLike)Blocks.COBBLESTONE)
/* 1574 */       .define(Character.valueOf('T'), ItemTags.PLANKS)
/* 1575 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1576 */       .pattern("TTT")
/* 1577 */       .pattern("#X#")
/* 1578 */       .pattern("#R#")
/* 1579 */       .unlockedBy("has_redstone", has((ItemLike)Items.REDSTONE))
/* 1580 */       .save(this.output);
/* 1582 */     polished(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.POLISHED_BASALT, (ItemLike)Blocks.BASALT);
/* 1584 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.PRISMARINE, (ItemLike)Items.PRISMARINE_SHARD);
/* 1586 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.PRISMARINE_BRICKS, (ItemLike)Items.PRISMARINE_SHARD);
/* 1588 */     shapeless(RecipeCategory.FOOD, (ItemLike)Items.PUMPKIN_PIE)
/* 1589 */       .requires((ItemLike)Blocks.PUMPKIN)
/* 1590 */       .requires((ItemLike)Items.SUGAR)
/* 1591 */       .requires(ItemTags.EGGS)
/* 1592 */       .unlockedBy("has_carved_pumpkin", has((ItemLike)Blocks.CARVED_PUMPKIN))
/* 1593 */       .unlockedBy("has_pumpkin", has((ItemLike)Blocks.PUMPKIN))
/* 1594 */       .save(this.output);
/* 1596 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.PUMPKIN_SEEDS, 4)
/* 1597 */       .requires((ItemLike)Blocks.PUMPKIN)
/* 1598 */       .unlockedBy("has_pumpkin", has((ItemLike)Blocks.PUMPKIN))
/* 1599 */       .save(this.output);
/* 1601 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.purple(), 2)
/* 1602 */       .requires((ItemLike)Items.DYE.blue())
/* 1603 */       .requires((ItemLike)Items.DYE.red())
/* 1604 */       .unlockedBy("has_blue_dye", has((ItemLike)Items.DYE.blue()))
/* 1605 */       .unlockedBy("has_red_dye", has((ItemLike)Items.DYE.red()))
/* 1606 */       .save(this.output);
/* 1608 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.SHULKER_BOX)
/* 1609 */       .define(Character.valueOf('#'), (ItemLike)Blocks.CHEST)
/* 1610 */       .define(Character.valueOf('-'), (ItemLike)Items.SHULKER_SHELL)
/* 1611 */       .pattern("-")
/* 1612 */       .pattern("#")
/* 1613 */       .pattern("-")
/* 1614 */       .unlockedBy("has_shulker_shell", has((ItemLike)Items.SHULKER_SHELL))
/* 1615 */       .save(this.output);
/* 1617 */     vanillaRecipeProvider = this;
/* 1617 */     ColorCollection.zipApply(Items.DYE, Items.DYED_SHULKER_BOX, (x$0, x$1) -> rec$.dyedShulkerBoxRecipe(x$0, x$1));
/* 1619 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.PURPUR_BLOCK, 4)
/* 1620 */       .define(Character.valueOf('F'), (ItemLike)Items.POPPED_CHORUS_FRUIT)
/* 1621 */       .pattern("FF")
/* 1622 */       .pattern("FF")
/* 1623 */       .unlockedBy("has_chorus_fruit_popped", has((ItemLike)Items.POPPED_CHORUS_FRUIT))
/* 1624 */       .save(this.output);
/* 1626 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.PURPUR_PILLAR)
/* 1627 */       .define(Character.valueOf('#'), (ItemLike)Blocks.PURPUR_SLAB)
/* 1628 */       .pattern("#")
/* 1629 */       .pattern("#")
/* 1630 */       .unlockedBy("has_purpur_block", has((ItemLike)Blocks.PURPUR_BLOCK))
/* 1631 */       .save(this.output);
/* 1633 */     slabBuilder(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.PURPUR_SLAB, Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PURPUR_BLOCK, (ItemLike)Blocks.PURPUR_PILLAR })).unlockedBy("has_purpur_block", has((ItemLike)Blocks.PURPUR_BLOCK))
/* 1635 */       .save(this.output);
/* 1637 */     stairBuilder((ItemLike)Blocks.PURPUR_STAIRS, Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PURPUR_BLOCK, (ItemLike)Blocks.PURPUR_PILLAR })).unlockedBy("has_purpur_block", has((ItemLike)Blocks.PURPUR_BLOCK))
/* 1639 */       .save(this.output);
/* 1641 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.QUARTZ_BLOCK, (ItemLike)Items.QUARTZ);
/* 1643 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.QUARTZ_BRICKS, 4)
/* 1644 */       .define(Character.valueOf('#'), (ItemLike)Blocks.QUARTZ_BLOCK)
/* 1645 */       .pattern("##")
/* 1646 */       .pattern("##")
/* 1647 */       .unlockedBy("has_quartz_block", has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 1648 */       .save(this.output);
/* 1650 */     slabBuilder(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.QUARTZ_SLAB, Ingredient.of(new ItemLike[] { (ItemLike)Blocks.CHISELED_QUARTZ_BLOCK, (ItemLike)Blocks.QUARTZ_BLOCK, (ItemLike)Blocks.QUARTZ_PILLAR })).unlockedBy("has_chiseled_quartz_block", has((ItemLike)Blocks.CHISELED_QUARTZ_BLOCK))
/* 1652 */       .unlockedBy("has_quartz_block", has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 1653 */       .unlockedBy("has_quartz_pillar", has((ItemLike)Blocks.QUARTZ_PILLAR))
/* 1654 */       .save(this.output);
/* 1656 */     stairBuilder((ItemLike)Blocks.QUARTZ_STAIRS, Ingredient.of(new ItemLike[] { (ItemLike)Blocks.CHISELED_QUARTZ_BLOCK, (ItemLike)Blocks.QUARTZ_BLOCK, (ItemLike)Blocks.QUARTZ_PILLAR })).unlockedBy("has_chiseled_quartz_block", has((ItemLike)Blocks.CHISELED_QUARTZ_BLOCK))
/* 1658 */       .unlockedBy("has_quartz_block", has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 1659 */       .unlockedBy("has_quartz_pillar", has((ItemLike)Blocks.QUARTZ_PILLAR))
/* 1660 */       .save(this.output);
/* 1662 */     shapeless(RecipeCategory.FOOD, (ItemLike)Items.RABBIT_STEW)
/* 1663 */       .requires((ItemLike)Items.BAKED_POTATO)
/* 1664 */       .requires((ItemLike)Items.COOKED_RABBIT)
/* 1665 */       .requires((ItemLike)Items.BOWL)
/* 1666 */       .requires((ItemLike)Items.CARROT)
/* 1667 */       .requires((ItemLike)Blocks.BROWN_MUSHROOM)
/* 1668 */       .group("rabbit_stew")
/* 1669 */       .unlockedBy("has_cooked_rabbit", has((ItemLike)Items.COOKED_RABBIT))
/* 1670 */       .save(this.output, getConversionRecipeName((ItemLike)Items.RABBIT_STEW, (ItemLike)Items.BROWN_MUSHROOM));
/* 1672 */     shapeless(RecipeCategory.FOOD, (ItemLike)Items.RABBIT_STEW)
/* 1673 */       .requires((ItemLike)Items.BAKED_POTATO)
/* 1674 */       .requires((ItemLike)Items.COOKED_RABBIT)
/* 1675 */       .requires((ItemLike)Items.BOWL)
/* 1676 */       .requires((ItemLike)Items.CARROT)
/* 1677 */       .requires((ItemLike)Blocks.RED_MUSHROOM)
/* 1678 */       .group("rabbit_stew")
/* 1679 */       .unlockedBy("has_cooked_rabbit", has((ItemLike)Items.COOKED_RABBIT))
/* 1680 */       .save(this.output, getConversionRecipeName((ItemLike)Items.RABBIT_STEW, (ItemLike)Items.RED_MUSHROOM));
/* 1682 */     shaped(RecipeCategory.TRANSPORTATION, (ItemLike)Blocks.RAIL, 16)
/* 1683 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1684 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1685 */       .pattern("X X")
/* 1686 */       .pattern("X#X")
/* 1687 */       .pattern("X X")
/* 1688 */       .unlockedBy("has_minecart", has((ItemLike)Items.MINECART))
/* 1689 */       .save(this.output);
/* 1691 */     nineBlockStorageRecipes(RecipeCategory.REDSTONE, (ItemLike)Items.REDSTONE, RecipeCategory.REDSTONE, (ItemLike)Items.REDSTONE_BLOCK);
/* 1693 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.REDSTONE_LAMP)
/* 1694 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/* 1695 */       .define(Character.valueOf('G'), (ItemLike)Blocks.GLOWSTONE)
/* 1696 */       .pattern(" R ")
/* 1697 */       .pattern("RGR")
/* 1698 */       .pattern(" R ")
/* 1699 */       .unlockedBy("has_glowstone", has((ItemLike)Blocks.GLOWSTONE))
/* 1700 */       .save(this.output);
/* 1702 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.REDSTONE_TORCH)
/* 1703 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1704 */       .define(Character.valueOf('X'), (ItemLike)Items.REDSTONE)
/* 1705 */       .pattern("X")
/* 1706 */       .pattern("#")
/* 1707 */       .unlockedBy("has_redstone", has((ItemLike)Items.REDSTONE))
/* 1708 */       .save(this.output);
/* 1710 */     oneToOneConversionRecipe((ItemLike)Items.DYE.red(), (ItemLike)Items.BEETROOT, "red_dye");
/* 1711 */     oneToOneConversionRecipe((ItemLike)Items.DYE.red(), (ItemLike)Blocks.POPPY, "red_dye");
/* 1712 */     oneToOneConversionRecipe((ItemLike)Items.DYE.red(), (ItemLike)Blocks.ROSE_BUSH, "red_dye", 2);
/* 1714 */     oneToOneConversionRecipe((ItemLike)Items.DYE.orange(), (ItemLike)Blocks.OPEN_EYEBLOSSOM, "orange_dye");
/* 1715 */     oneToOneConversionRecipe((ItemLike)Items.DYE.gray(), (ItemLike)Blocks.CLOSED_EYEBLOSSOM, "gray_dye");
/* 1717 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.red())
/* 1718 */       .requires((ItemLike)Blocks.RED_TULIP)
/* 1719 */       .group("red_dye")
/* 1720 */       .unlockedBy("has_red_flower", has((ItemLike)Blocks.RED_TULIP))
/* 1721 */       .save(this.output, "red_dye_from_tulip");
/* 1723 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.RED_NETHER_BRICKS)
/* 1724 */       .define(Character.valueOf('W'), (ItemLike)Items.NETHER_WART)
/* 1725 */       .define(Character.valueOf('N'), (ItemLike)Items.NETHER_BRICK)
/* 1726 */       .pattern("NW")
/* 1727 */       .pattern("WN")
/* 1728 */       .unlockedBy("has_nether_wart", has((ItemLike)Items.NETHER_WART))
/* 1729 */       .save(this.output);
/* 1731 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.RED_SANDSTONE)
/* 1732 */       .define(Character.valueOf('#'), (ItemLike)Blocks.RED_SAND)
/* 1733 */       .pattern("##")
/* 1734 */       .pattern("##")
/* 1735 */       .unlockedBy("has_sand", has((ItemLike)Blocks.RED_SAND))
/* 1736 */       .save(this.output);
/* 1738 */     slabBuilder(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.RED_SANDSTONE_SLAB, Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_SANDSTONE, (ItemLike)Blocks.CHISELED_RED_SANDSTONE })).unlockedBy("has_red_sandstone", has((ItemLike)Blocks.RED_SANDSTONE))
/* 1740 */       .unlockedBy("has_chiseled_red_sandstone", has((ItemLike)Blocks.CHISELED_RED_SANDSTONE))
/* 1741 */       .save(this.output);
/* 1743 */     stairBuilder((ItemLike)Blocks.RED_SANDSTONE_STAIRS, Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_SANDSTONE, (ItemLike)Blocks.CHISELED_RED_SANDSTONE, (ItemLike)Blocks.CUT_RED_SANDSTONE })).unlockedBy("has_red_sandstone", has((ItemLike)Blocks.RED_SANDSTONE))
/* 1745 */       .unlockedBy("has_chiseled_red_sandstone", has((ItemLike)Blocks.CHISELED_RED_SANDSTONE))
/* 1746 */       .unlockedBy("has_cut_red_sandstone", has((ItemLike)Blocks.CUT_RED_SANDSTONE))
/* 1747 */       .save(this.output);
/* 1749 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.REPEATER)
/* 1750 */       .define(Character.valueOf('#'), (ItemLike)Blocks.REDSTONE_TORCH)
/* 1751 */       .define(Character.valueOf('X'), (ItemLike)Items.REDSTONE)
/* 1752 */       .define(Character.valueOf('I'), (ItemLike)Blocks.STONE)
/* 1753 */       .pattern("#X#")
/* 1754 */       .pattern("III")
/* 1755 */       .unlockedBy("has_redstone_torch", has((ItemLike)Blocks.REDSTONE_TORCH))
/* 1756 */       .save(this.output);
/* 1758 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.SANDSTONE, (ItemLike)Blocks.SAND);
/* 1760 */     slabBuilder(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.SANDSTONE_SLAB, Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SANDSTONE, (ItemLike)Blocks.CHISELED_SANDSTONE })).unlockedBy("has_sandstone", has((ItemLike)Blocks.SANDSTONE))
/* 1762 */       .unlockedBy("has_chiseled_sandstone", has((ItemLike)Blocks.CHISELED_SANDSTONE))
/* 1763 */       .save(this.output);
/* 1765 */     stairBuilder((ItemLike)Blocks.SANDSTONE_STAIRS, Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SANDSTONE, (ItemLike)Blocks.CHISELED_SANDSTONE, (ItemLike)Blocks.CUT_SANDSTONE })).unlockedBy("has_sandstone", has((ItemLike)Blocks.SANDSTONE))
/* 1767 */       .unlockedBy("has_chiseled_sandstone", has((ItemLike)Blocks.CHISELED_SANDSTONE))
/* 1768 */       .unlockedBy("has_cut_sandstone", has((ItemLike)Blocks.CUT_SANDSTONE))
/* 1769 */       .save(this.output);
/* 1771 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.SEA_LANTERN)
/* 1772 */       .define(Character.valueOf('S'), (ItemLike)Items.PRISMARINE_SHARD)
/* 1773 */       .define(Character.valueOf('C'), (ItemLike)Items.PRISMARINE_CRYSTALS)
/* 1774 */       .pattern("SCS")
/* 1775 */       .pattern("CCC")
/* 1776 */       .pattern("SCS")
/* 1777 */       .unlockedBy("has_prismarine_crystals", has((ItemLike)Items.PRISMARINE_CRYSTALS))
/* 1778 */       .save(this.output);
/* 1780 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.SHEARS)
/* 1781 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/* 1782 */       .pattern(" #")
/* 1783 */       .pattern("# ")
/* 1784 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/* 1785 */       .save(this.output);
/* 1787 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.SHIELD)
/* 1788 */       .define(Character.valueOf('W'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 1789 */       .define(Character.valueOf('o'), (ItemLike)Items.IRON_INGOT)
/* 1790 */       .pattern("WoW")
/* 1791 */       .pattern("WWW")
/* 1792 */       .pattern(" W ")
/* 1793 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/* 1794 */       .save(this.output);
/* 1796 */     nineBlockStorageRecipes(RecipeCategory.MISC, (ItemLike)Items.SLIME_BALL, RecipeCategory.REDSTONE, (ItemLike)Items.SLIME_BLOCK);
/* 1798 */     cut(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.CUT_RED_SANDSTONE, (ItemLike)Blocks.RED_SANDSTONE);
/* 1799 */     cut(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.CUT_SANDSTONE, (ItemLike)Blocks.SANDSTONE);
/* 1801 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.SNOW_BLOCK, (ItemLike)Items.SNOWBALL);
/* 1803 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.SNOW, 6)
/* 1804 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SNOW_BLOCK)
/* 1805 */       .pattern("###")
/* 1806 */       .unlockedBy("has_snowball", has((ItemLike)Items.SNOWBALL))
/* 1807 */       .save(this.output);
/* 1809 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.SOUL_CAMPFIRE)
/* 1810 */       .define(Character.valueOf('L'), ItemTags.LOGS)
/* 1811 */       .define(Character.valueOf('S'), (ItemLike)Items.STICK)
/* 1812 */       .define(Character.valueOf('#'), ItemTags.SOUL_FIRE_BASE_BLOCKS)
/* 1813 */       .pattern(" S ")
/* 1814 */       .pattern("S#S")
/* 1815 */       .pattern("LLL")
/* 1816 */       .unlockedBy("has_soul_sand", has(ItemTags.SOUL_FIRE_BASE_BLOCKS))
/* 1817 */       .save(this.output);
/* 1819 */     shaped(RecipeCategory.BREWING, (ItemLike)Items.GLISTERING_MELON_SLICE)
/* 1820 */       .define(Character.valueOf('#'), (ItemLike)Items.GOLD_NUGGET)
/* 1821 */       .define(Character.valueOf('X'), (ItemLike)Items.MELON_SLICE)
/* 1822 */       .pattern("###")
/* 1823 */       .pattern("#X#")
/* 1824 */       .pattern("###")
/* 1825 */       .unlockedBy("has_melon", has((ItemLike)Items.MELON_SLICE))
/* 1826 */       .save(this.output);
/* 1828 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.SPECTRAL_ARROW, 2)
/* 1829 */       .define(Character.valueOf('#'), (ItemLike)Items.GLOWSTONE_DUST)
/* 1830 */       .define(Character.valueOf('X'), (ItemLike)Items.ARROW)
/* 1831 */       .pattern(" # ")
/* 1832 */       .pattern("#X#")
/* 1833 */       .pattern(" # ")
/* 1834 */       .unlockedBy("has_glowstone_dust", has((ItemLike)Items.GLOWSTONE_DUST))
/* 1835 */       .save(this.output);
/* 1837 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.SPYGLASS)
/* 1838 */       .define(Character.valueOf('#'), (ItemLike)Items.AMETHYST_SHARD)
/* 1839 */       .define(Character.valueOf('X'), (ItemLike)Items.COPPER_INGOT)
/* 1840 */       .pattern(" # ")
/* 1841 */       .pattern(" X ")
/* 1842 */       .pattern(" X ")
/* 1843 */       .unlockedBy("has_amethyst_shard", has((ItemLike)Items.AMETHYST_SHARD))
/* 1844 */       .save(this.output);
/* 1846 */     shaped(RecipeCategory.MISC, (ItemLike)Items.STICK, 4)
/* 1847 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 1848 */       .pattern("#")
/* 1849 */       .pattern("#")
/* 1850 */       .group("sticks")
/* 1851 */       .unlockedBy("has_planks", has(ItemTags.PLANKS))
/* 1852 */       .save(this.output);
/* 1854 */     shaped(RecipeCategory.MISC, (ItemLike)Items.STICK, 1)
/* 1855 */       .define(Character.valueOf('#'), (ItemLike)Blocks.BAMBOO)
/* 1856 */       .pattern("#")
/* 1857 */       .pattern("#")
/* 1858 */       .group("sticks")
/* 1859 */       .unlockedBy("has_bamboo", has((ItemLike)Blocks.BAMBOO))
/* 1860 */       .save(this.output, "stick_from_bamboo_item");
/* 1862 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.STICKY_PISTON)
/* 1863 */       .define(Character.valueOf('P'), (ItemLike)Blocks.PISTON)
/* 1864 */       .define(Character.valueOf('S'), (ItemLike)Items.SLIME_BALL)
/* 1865 */       .pattern("S")
/* 1866 */       .pattern("P")
/* 1867 */       .unlockedBy("has_slime_ball", has((ItemLike)Items.SLIME_BALL))
/* 1868 */       .save(this.output);
/* 1870 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.STONE_AXE)
/* 1871 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1872 */       .define(Character.valueOf('X'), ItemTags.STONE_TOOL_MATERIALS)
/* 1873 */       .pattern("XX")
/* 1874 */       .pattern("X#")
/* 1875 */       .pattern(" #")
/* 1876 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
/* 1877 */       .save(this.output);
/* 1879 */     slabBuilder(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.STONE_BRICK_SLAB, Ingredient.of((ItemLike)Blocks.STONE_BRICKS))
/* 1880 */       .unlockedBy("has_stone_bricks", has(ItemTags.STONE_BRICKS))
/* 1881 */       .save(this.output);
/* 1883 */     stairBuilder((ItemLike)Blocks.STONE_BRICK_STAIRS, Ingredient.of((ItemLike)Blocks.STONE_BRICKS))
/* 1884 */       .unlockedBy("has_stone_bricks", has(ItemTags.STONE_BRICKS))
/* 1885 */       .save(this.output);
/* 1887 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.STONE_HOE)
/* 1888 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1889 */       .define(Character.valueOf('X'), ItemTags.STONE_TOOL_MATERIALS)
/* 1890 */       .pattern("XX")
/* 1891 */       .pattern(" #")
/* 1892 */       .pattern(" #")
/* 1893 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
/* 1894 */       .save(this.output);
/* 1896 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.STONE_PICKAXE)
/* 1897 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1898 */       .define(Character.valueOf('X'), ItemTags.STONE_TOOL_MATERIALS)
/* 1899 */       .pattern("XXX")
/* 1900 */       .pattern(" # ")
/* 1901 */       .pattern(" # ")
/* 1902 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
/* 1903 */       .save(this.output);
/* 1905 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.STONE_SHOVEL)
/* 1906 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1907 */       .define(Character.valueOf('X'), ItemTags.STONE_TOOL_MATERIALS)
/* 1908 */       .pattern("X")
/* 1909 */       .pattern("#")
/* 1910 */       .pattern("#")
/* 1911 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
/* 1912 */       .save(this.output);
/* 1914 */     slab(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.SMOOTH_STONE_SLAB, (ItemLike)Blocks.SMOOTH_STONE);
/* 1916 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.STONE_SWORD)
/* 1917 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1918 */       .define(Character.valueOf('X'), ItemTags.STONE_TOOL_MATERIALS)
/* 1919 */       .pattern("X")
/* 1920 */       .pattern("X")
/* 1921 */       .pattern("#")
/* 1922 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
/* 1923 */       .save(this.output);
/* 1925 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.STONE_SPEAR)
/* 1926 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1927 */       .define(Character.valueOf('X'), ItemTags.STONE_TOOL_MATERIALS)
/* 1928 */       .pattern("  X")
/* 1929 */       .pattern(" # ")
/* 1930 */       .pattern("#  ")
/* 1931 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
/* 1932 */       .save(this.output);
/* 1934 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.WOOL.white())
/* 1935 */       .define(Character.valueOf('#'), (ItemLike)Items.STRING)
/* 1936 */       .pattern("##")
/* 1937 */       .pattern("##")
/* 1938 */       .unlockedBy("has_string", has((ItemLike)Items.STRING))
/* 1939 */       .save(this.output, getConversionRecipeName((ItemLike)Blocks.WOOL.white(), (ItemLike)Items.STRING));
/* 1941 */     oneToOneConversionRecipe((ItemLike)Items.SUGAR, (ItemLike)Blocks.SUGAR_CANE, "sugar");
/* 1943 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.SUGAR, 3)
/* 1944 */       .requires((ItemLike)Items.HONEY_BOTTLE)
/* 1945 */       .group("sugar")
/* 1946 */       .unlockedBy("has_honey_bottle", has((ItemLike)Items.HONEY_BOTTLE))
/* 1947 */       .save(this.output, getConversionRecipeName((ItemLike)Items.SUGAR, (ItemLike)Items.HONEY_BOTTLE));
/* 1949 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.TARGET)
/* 1950 */       .define(Character.valueOf('H'), (ItemLike)Items.HAY_BLOCK)
/* 1951 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/* 1952 */       .pattern(" R ")
/* 1953 */       .pattern("RHR")
/* 1954 */       .pattern(" R ")
/* 1955 */       .unlockedBy("has_redstone", has((ItemLike)Items.REDSTONE))
/* 1956 */       .unlockedBy("has_hay_block", has((ItemLike)Blocks.HAY_BLOCK))
/* 1957 */       .save(this.output);
/* 1959 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.TNT)
/* 1960 */       .define(Character.valueOf('#'), Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SAND, (ItemLike)Blocks.RED_SAND })).define(Character.valueOf('X'), (ItemLike)Items.GUNPOWDER)
/* 1962 */       .pattern("X#X")
/* 1963 */       .pattern("#X#")
/* 1964 */       .pattern("X#X")
/* 1965 */       .unlockedBy("has_gunpowder", has((ItemLike)Items.GUNPOWDER))
/* 1966 */       .save(this.output);
/* 1968 */     shapeless(RecipeCategory.TRANSPORTATION, (ItemLike)Items.TNT_MINECART)
/* 1969 */       .requires((ItemLike)Blocks.TNT)
/* 1970 */       .requires((ItemLike)Items.MINECART)
/* 1971 */       .unlockedBy("has_minecart", has((ItemLike)Items.MINECART))
/* 1972 */       .save(this.output);
/* 1974 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.TORCH, 4)
/* 1975 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1976 */       .define(Character.valueOf('X'), Ingredient.of(new ItemLike[] { (ItemLike)Items.COAL, (ItemLike)Items.CHARCOAL })).pattern("X")
/* 1978 */       .pattern("#")
/* 1979 */       .unlockedBy("has_stone_pickaxe", has((ItemLike)Items.STONE_PICKAXE))
/* 1980 */       .save(this.output);
/* 1982 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.SOUL_TORCH, 4)
/* 1983 */       .define(Character.valueOf('X'), Ingredient.of(new ItemLike[] { (ItemLike)Items.COAL, (ItemLike)Items.CHARCOAL })).define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1985 */       .define(Character.valueOf('S'), ItemTags.SOUL_FIRE_BASE_BLOCKS)
/* 1986 */       .pattern("X")
/* 1987 */       .pattern("#")
/* 1988 */       .pattern("S")
/* 1989 */       .unlockedBy("has_soul_sand", has(ItemTags.SOUL_FIRE_BASE_BLOCKS))
/* 1990 */       .save(this.output);
/* 1992 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.COPPER_TORCH, 4)
/* 1993 */       .define(Character.valueOf('X'), Ingredient.of(new ItemLike[] { (ItemLike)Items.COAL, (ItemLike)Items.CHARCOAL })).define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1995 */       .define(Character.valueOf('C'), (ItemLike)Items.COPPER_NUGGET)
/* 1996 */       .pattern("C")
/* 1997 */       .pattern("X")
/* 1998 */       .pattern("#")
/* 1999 */       .unlockedBy("has_copper_nugget", has((ItemLike)Items.COPPER_NUGGET))
/* 2000 */       .save(this.output);
/* 2002 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.LANTERN)
/* 2003 */       .define(Character.valueOf('#'), (ItemLike)Items.TORCH)
/* 2004 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_NUGGET)
/* 2005 */       .pattern("XXX")
/* 2006 */       .pattern("X#X")
/* 2007 */       .pattern("XXX")
/* 2008 */       .unlockedBy("has_iron_nugget", has((ItemLike)Items.IRON_NUGGET))
/* 2009 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/* 2010 */       .save(this.output);
/* 2012 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.SOUL_LANTERN)
/* 2013 */       .define(Character.valueOf('#'), (ItemLike)Items.SOUL_TORCH)
/* 2014 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_NUGGET)
/* 2015 */       .pattern("XXX")
/* 2016 */       .pattern("X#X")
/* 2017 */       .pattern("XXX")
/* 2018 */       .unlockedBy("has_soul_torch", has((ItemLike)Items.SOUL_TORCH))
/* 2019 */       .save(this.output);
/* 2021 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.COPPER_LANTERN.weathering().unaffected())
/* 2022 */       .define(Character.valueOf('#'), (ItemLike)Items.COPPER_TORCH)
/* 2023 */       .define(Character.valueOf('X'), (ItemLike)Items.COPPER_NUGGET)
/* 2024 */       .pattern("XXX")
/* 2025 */       .pattern("X#X")
/* 2026 */       .pattern("XXX")
/* 2027 */       .unlockedBy("has_copper_torch", has((ItemLike)Items.COPPER_TORCH))
/* 2028 */       .save(this.output);
/* 2030 */     shapeless(RecipeCategory.REDSTONE, (ItemLike)Blocks.TRAPPED_CHEST)
/* 2031 */       .requires((ItemLike)Blocks.CHEST)
/* 2032 */       .requires((ItemLike)Blocks.TRIPWIRE_HOOK)
/* 2033 */       .unlockedBy("has_tripwire_hook", has((ItemLike)Blocks.TRIPWIRE_HOOK))
/* 2034 */       .save(this.output);
/* 2036 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.TRIPWIRE_HOOK, 2)
/* 2037 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 2038 */       .define(Character.valueOf('S'), (ItemLike)Items.STICK)
/* 2039 */       .define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT)
/* 2040 */       .pattern("I")
/* 2041 */       .pattern("S")
/* 2042 */       .pattern("#")
/* 2043 */       .unlockedBy("has_string", has((ItemLike)Items.STRING))
/* 2044 */       .save(this.output);
/* 2046 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.TURTLE_HELMET)
/* 2047 */       .define(Character.valueOf('X'), (ItemLike)Items.TURTLE_SCUTE)
/* 2048 */       .pattern("XXX")
/* 2049 */       .pattern("X X")
/* 2050 */       .unlockedBy("has_turtle_scute", has((ItemLike)Items.TURTLE_SCUTE))
/* 2051 */       .save(this.output);
/* 2053 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.WOLF_ARMOR)
/* 2054 */       .define(Character.valueOf('X'), (ItemLike)Items.ARMADILLO_SCUTE)
/* 2055 */       .pattern("X  ")
/* 2056 */       .pattern("XXX")
/* 2057 */       .pattern("X X")
/* 2058 */       .unlockedBy("has_armadillo_scute", has((ItemLike)Items.ARMADILLO_SCUTE))
/* 2059 */       .save(this.output);
/* 2061 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.WHEAT, 9)
/* 2062 */       .requires((ItemLike)Blocks.HAY_BLOCK)
/* 2063 */       .unlockedBy("has_hay_block", has((ItemLike)Blocks.HAY_BLOCK))
/* 2064 */       .save(this.output);
/* 2066 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.DYE.white())
/* 2067 */       .requires((ItemLike)Items.BONE_MEAL)
/* 2068 */       .group("white_dye")
/* 2069 */       .unlockedBy("has_bone_meal", has((ItemLike)Items.BONE_MEAL))
/* 2070 */       .save(this.output);
/* 2072 */     oneToOneConversionRecipe((ItemLike)Items.DYE.white(), (ItemLike)Blocks.LILY_OF_THE_VALLEY, "white_dye");
/* 2074 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.WOODEN_AXE)
/* 2075 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2076 */       .define(Character.valueOf('X'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 2077 */       .pattern("XX")
/* 2078 */       .pattern("X#")
/* 2079 */       .pattern(" #")
/* 2080 */       .unlockedBy("has_stick", has((ItemLike)Items.STICK))
/* 2081 */       .save(this.output);
/* 2083 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.WOODEN_HOE)
/* 2084 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2085 */       .define(Character.valueOf('X'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 2086 */       .pattern("XX")
/* 2087 */       .pattern(" #")
/* 2088 */       .pattern(" #")
/* 2089 */       .unlockedBy("has_stick", has((ItemLike)Items.STICK))
/* 2090 */       .save(this.output);
/* 2092 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.WOODEN_PICKAXE)
/* 2093 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2094 */       .define(Character.valueOf('X'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 2095 */       .pattern("XXX")
/* 2096 */       .pattern(" # ")
/* 2097 */       .pattern(" # ")
/* 2098 */       .unlockedBy("has_stick", has((ItemLike)Items.STICK))
/* 2099 */       .save(this.output);
/* 2101 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.WOODEN_SHOVEL)
/* 2102 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2103 */       .define(Character.valueOf('X'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 2104 */       .pattern("X")
/* 2105 */       .pattern("#")
/* 2106 */       .pattern("#")
/* 2107 */       .unlockedBy("has_stick", has((ItemLike)Items.STICK))
/* 2108 */       .save(this.output);
/* 2110 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.WOODEN_SWORD)
/* 2111 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2112 */       .define(Character.valueOf('X'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 2113 */       .pattern("X")
/* 2114 */       .pattern("X")
/* 2115 */       .pattern("#")
/* 2116 */       .unlockedBy("has_stick", has((ItemLike)Items.STICK))
/* 2117 */       .save(this.output);
/* 2119 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.WOODEN_SPEAR)
/* 2120 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2121 */       .define(Character.valueOf('X'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 2122 */       .pattern("  X")
/* 2123 */       .pattern(" # ")
/* 2124 */       .pattern("#  ")
/* 2125 */       .unlockedBy("has_stick", has((ItemLike)Items.STICK))
/* 2126 */       .save(this.output);
/* 2128 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.WRITABLE_BOOK)
/* 2129 */       .requires((ItemLike)Items.BOOK)
/* 2130 */       .requires((ItemLike)Items.INK_SAC)
/* 2131 */       .requires((ItemLike)Items.FEATHER)
/* 2132 */       .unlockedBy("has_book", has((ItemLike)Items.BOOK))
/* 2133 */       .save(this.output);
/* 2135 */     oneToOneConversionRecipe((ItemLike)Items.DYE.yellow(), (ItemLike)Blocks.DANDELION, "yellow_dye");
/* 2136 */     oneToOneConversionRecipe((ItemLike)Items.DYE.yellow(), (ItemLike)Blocks.GOLDEN_DANDELION, "yellow_dye");
/* 2137 */     oneToOneConversionRecipe((ItemLike)Items.DYE.yellow(), (ItemLike)Blocks.SUNFLOWER, "yellow_dye", 2);
/* 2138 */     oneToOneConversionRecipe((ItemLike)Items.DYE.yellow(), (ItemLike)Blocks.WILDFLOWERS, "yellow_dye");
/* 2140 */     nineBlockStorageRecipes(RecipeCategory.FOOD, (ItemLike)Items.DRIED_KELP, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.DRIED_KELP_BLOCK);
/* 2142 */     shaped(RecipeCategory.MISC, (ItemLike)Blocks.CONDUIT)
/* 2143 */       .define(Character.valueOf('#'), (ItemLike)Items.NAUTILUS_SHELL)
/* 2144 */       .define(Character.valueOf('X'), (ItemLike)Items.HEART_OF_THE_SEA)
/* 2145 */       .pattern("###")
/* 2146 */       .pattern("#X#")
/* 2147 */       .pattern("###")
/* 2148 */       .unlockedBy("has_nautilus_core", has((ItemLike)Items.HEART_OF_THE_SEA))
/* 2149 */       .unlockedBy("has_nautilus_shell", has((ItemLike)Items.NAUTILUS_SHELL))
/* 2150 */       .save(this.output);
/* 2152 */     wall(RecipeCategory.DECORATIONS, (ItemLike)Blocks.RED_SANDSTONE_WALL, (ItemLike)Blocks.RED_SANDSTONE);
/* 2153 */     wall(RecipeCategory.DECORATIONS, (ItemLike)Blocks.STONE_BRICK_WALL, (ItemLike)Blocks.STONE_BRICKS);
/* 2154 */     wall(RecipeCategory.DECORATIONS, (ItemLike)Blocks.SANDSTONE_WALL, (ItemLike)Blocks.SANDSTONE);
/* 2156 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.FIELD_MASONED_BANNER_PATTERN)
/* 2157 */       .requires((ItemLike)Items.PAPER)
/* 2158 */       .requires((ItemLike)Blocks.BRICKS)
/* 2159 */       .unlockedBy("has_bricks", has((ItemLike)Blocks.BRICKS))
/* 2160 */       .save(this.output);
/* 2162 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.BORDURE_INDENTED_BANNER_PATTERN)
/* 2163 */       .requires((ItemLike)Items.PAPER)
/* 2164 */       .requires((ItemLike)Blocks.VINE)
/* 2165 */       .unlockedBy("has_vines", has((ItemLike)Blocks.VINE))
/* 2166 */       .save(this.output);
/* 2168 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.CREEPER_BANNER_PATTERN)
/* 2169 */       .requires((ItemLike)Items.PAPER)
/* 2170 */       .requires((ItemLike)Items.CREEPER_HEAD)
/* 2171 */       .unlockedBy("has_creeper_head", has((ItemLike)Items.CREEPER_HEAD))
/* 2172 */       .save(this.output);
/* 2174 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.SKULL_BANNER_PATTERN)
/* 2175 */       .requires((ItemLike)Items.PAPER)
/* 2176 */       .requires((ItemLike)Items.WITHER_SKELETON_SKULL)
/* 2177 */       .unlockedBy("has_wither_skeleton_skull", has((ItemLike)Items.WITHER_SKELETON_SKULL))
/* 2178 */       .save(this.output);
/* 2180 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.FLOWER_BANNER_PATTERN)
/* 2181 */       .requires((ItemLike)Items.PAPER)
/* 2182 */       .requires((ItemLike)Blocks.OXEYE_DAISY)
/* 2183 */       .unlockedBy("has_oxeye_daisy", has((ItemLike)Blocks.OXEYE_DAISY))
/* 2184 */       .save(this.output);
/* 2186 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.MOJANG_BANNER_PATTERN)
/* 2187 */       .requires((ItemLike)Items.PAPER)
/* 2188 */       .requires((ItemLike)Items.ENCHANTED_GOLDEN_APPLE)
/* 2189 */       .unlockedBy("has_enchanted_golden_apple", has((ItemLike)Items.ENCHANTED_GOLDEN_APPLE))
/* 2190 */       .save(this.output);
/* 2192 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.SCAFFOLDING, 6)
/* 2193 */       .define(Character.valueOf('~'), (ItemLike)Items.STRING)
/* 2194 */       .define(Character.valueOf('I'), (ItemLike)Blocks.BAMBOO)
/* 2195 */       .pattern("I~I")
/* 2196 */       .pattern("I I")
/* 2197 */       .pattern("I I")
/* 2198 */       .unlockedBy("has_bamboo", has((ItemLike)Blocks.BAMBOO))
/* 2199 */       .save(this.output);
/* 2201 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.GRINDSTONE)
/* 2202 */       .define(Character.valueOf('I'), (ItemLike)Items.STICK)
/* 2203 */       .define(Character.valueOf('-'), (ItemLike)Blocks.STONE_SLAB)
/* 2204 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 2205 */       .pattern("I-I")
/* 2206 */       .pattern("# #")
/* 2207 */       .unlockedBy("has_stone_slab", has((ItemLike)Blocks.STONE_SLAB))
/* 2208 */       .save(this.output);
/* 2210 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.BLAST_FURNACE)
/* 2211 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SMOOTH_STONE)
/* 2212 */       .define(Character.valueOf('X'), (ItemLike)Blocks.FURNACE)
/* 2213 */       .define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT)
/* 2214 */       .pattern("III")
/* 2215 */       .pattern("IXI")
/* 2216 */       .pattern("###")
/* 2217 */       .unlockedBy("has_smooth_stone", has((ItemLike)Blocks.SMOOTH_STONE))
/* 2218 */       .save(this.output);
/* 2220 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.SMOKER)
/* 2221 */       .define(Character.valueOf('#'), ItemTags.LOGS)
/* 2222 */       .define(Character.valueOf('X'), (ItemLike)Blocks.FURNACE)
/* 2223 */       .pattern(" # ")
/* 2224 */       .pattern("#X#")
/* 2225 */       .pattern(" # ")
/* 2226 */       .unlockedBy("has_furnace", has((ItemLike)Blocks.FURNACE))
/* 2227 */       .save(this.output);
/* 2229 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.CARTOGRAPHY_TABLE)
/* 2230 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 2231 */       .define(Character.valueOf('@'), (ItemLike)Items.PAPER)
/* 2232 */       .pattern("@@")
/* 2233 */       .pattern("##")
/* 2234 */       .pattern("##")
/* 2235 */       .unlockedBy("has_paper", has((ItemLike)Items.PAPER))
/* 2236 */       .save(this.output);
/* 2238 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.SMITHING_TABLE)
/* 2239 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 2240 */       .define(Character.valueOf('@'), (ItemLike)Items.IRON_INGOT)
/* 2241 */       .pattern("@@")
/* 2242 */       .pattern("##")
/* 2243 */       .pattern("##")
/* 2244 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/* 2245 */       .save(this.output);
/* 2247 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.FLETCHING_TABLE)
/* 2248 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 2249 */       .define(Character.valueOf('@'), (ItemLike)Items.FLINT)
/* 2250 */       .pattern("@@")
/* 2251 */       .pattern("##")
/* 2252 */       .pattern("##")
/* 2253 */       .unlockedBy("has_flint", has((ItemLike)Items.FLINT))
/* 2254 */       .save(this.output);
/* 2256 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.STONECUTTER)
/* 2257 */       .define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT)
/* 2258 */       .define(Character.valueOf('#'), (ItemLike)Blocks.STONE)
/* 2259 */       .pattern(" I ")
/* 2260 */       .pattern("###")
/* 2261 */       .unlockedBy("has_stone", has((ItemLike)Blocks.STONE))
/* 2262 */       .save(this.output);
/* 2264 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.LODESTONE)
/* 2265 */       .define(Character.valueOf('S'), (ItemLike)Items.CHISELED_STONE_BRICKS)
/* 2266 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/* 2267 */       .pattern("SSS")
/* 2268 */       .pattern("S#S")
/* 2269 */       .pattern("SSS")
/* 2270 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/* 2271 */       .unlockedBy("has_lodestone", has((ItemLike)Items.LODESTONE))
/* 2272 */       .save(this.output);
/* 2274 */     nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory.MISC, (ItemLike)Items.NETHERITE_INGOT, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.NETHERITE_BLOCK, "netherite_ingot_from_netherite_block", "netherite_ingot");
/* 2276 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.NETHERITE_INGOT)
/* 2277 */       .requires((ItemLike)Items.NETHERITE_SCRAP, 4)
/* 2278 */       .requires((ItemLike)Items.GOLD_INGOT, 4)
/* 2279 */       .group("netherite_ingot")
/* 2280 */       .unlockedBy("has_netherite_scrap", has((ItemLike)Items.NETHERITE_SCRAP))
/* 2281 */       .save(this.output);
/* 2283 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.RESPAWN_ANCHOR)
/* 2284 */       .define(Character.valueOf('O'), (ItemLike)Blocks.CRYING_OBSIDIAN)
/* 2285 */       .define(Character.valueOf('G'), (ItemLike)Blocks.GLOWSTONE)
/* 2286 */       .pattern("OOO")
/* 2287 */       .pattern("GGG")
/* 2288 */       .pattern("OOO")
/* 2289 */       .unlockedBy("has_obsidian", has((ItemLike)Blocks.CRYING_OBSIDIAN))
/* 2290 */       .save(this.output);
/* 2292 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.IRON_CHAIN)
/* 2293 */       .define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT)
/* 2294 */       .define(Character.valueOf('N'), (ItemLike)Items.IRON_NUGGET)
/* 2295 */       .pattern("N")
/* 2296 */       .pattern("I")
/* 2297 */       .pattern("N")
/* 2298 */       .unlockedBy("has_iron_nugget", has((ItemLike)Items.IRON_NUGGET))
/* 2299 */       .unlockedBy("has_iron_ingot", has((ItemLike)Items.IRON_INGOT))
/* 2300 */       .save(this.output);
/* 2302 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.COPPER_CHAIN.weathering().unaffected())
/* 2303 */       .define(Character.valueOf('I'), (ItemLike)Items.COPPER_INGOT)
/* 2304 */       .define(Character.valueOf('N'), (ItemLike)Items.COPPER_NUGGET)
/* 2305 */       .pattern("N")
/* 2306 */       .pattern("I")
/* 2307 */       .pattern("N")
/* 2308 */       .unlockedBy("has_copper_nugget", has((ItemLike)Items.COPPER_NUGGET))
/* 2309 */       .unlockedBy("has_copper_ingot", has((ItemLike)Items.COPPER_INGOT))
/* 2310 */       .save(this.output);
/* 2312 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Blocks.GOLDEN_DANDELION)
/* 2313 */       .define(Character.valueOf('I'), (ItemLike)Items.DANDELION)
/* 2314 */       .define(Character.valueOf('#'), (ItemLike)Items.GOLD_NUGGET)
/* 2315 */       .pattern("###")
/* 2316 */       .pattern("#I#")
/* 2317 */       .pattern("###")
/* 2318 */       .unlockedBy("has_gold_nugget", has((ItemLike)Items.GOLD_NUGGET))
/* 2319 */       .unlockedBy("has_dandelion", has((ItemLike)Items.DANDELION))
/* 2320 */       .save(this.output);
/* 2322 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.TINTED_GLASS, 2)
/* 2323 */       .define(Character.valueOf('G'), (ItemLike)Blocks.GLASS)
/* 2324 */       .define(Character.valueOf('S'), (ItemLike)Items.AMETHYST_SHARD)
/* 2325 */       .pattern(" S ")
/* 2326 */       .pattern("SGS")
/* 2327 */       .pattern(" S ")
/* 2328 */       .unlockedBy("has_amethyst_shard", has((ItemLike)Items.AMETHYST_SHARD))
/* 2329 */       .save(this.output);
/* 2331 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.AMETHYST_BLOCK, (ItemLike)Items.AMETHYST_SHARD);
/* 2333 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.RECOVERY_COMPASS)
/* 2334 */       .define(Character.valueOf('C'), (ItemLike)Items.COMPASS)
/* 2335 */       .define(Character.valueOf('S'), (ItemLike)Items.ECHO_SHARD)
/* 2336 */       .pattern("SSS")
/* 2337 */       .pattern("SCS")
/* 2338 */       .pattern("SSS")
/* 2339 */       .unlockedBy("has_echo_shard", has((ItemLike)Items.ECHO_SHARD))
/* 2340 */       .save(this.output);
/* 2342 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Items.CALIBRATED_SCULK_SENSOR)
/* 2343 */       .define(Character.valueOf('#'), (ItemLike)Items.AMETHYST_SHARD)
/* 2344 */       .define(Character.valueOf('X'), (ItemLike)Items.SCULK_SENSOR)
/* 2345 */       .pattern(" # ")
/* 2346 */       .pattern("#X#")
/* 2347 */       .unlockedBy("has_amethyst_shard", has((ItemLike)Items.AMETHYST_SHARD))
/* 2348 */       .save(this.output);
/* 2350 */     threeByThreePacker(RecipeCategory.MISC, (ItemLike)Items.MUSIC_DISC_5, (ItemLike)Items.DISC_FRAGMENT_5);
/* 2353 */     dyedItem(Items.LEATHER_HELMET, "dyed_armor");
/* 2354 */     dyedItem(Items.LEATHER_CHESTPLATE, "dyed_armor");
/* 2355 */     dyedItem(Items.LEATHER_LEGGINGS, "dyed_armor");
/* 2356 */     dyedItem(Items.LEATHER_BOOTS, "dyed_armor");
/* 2357 */     dyedItem(Items.LEATHER_HORSE_ARMOR, "dyed_armor");
/* 2358 */     dyedItem(Items.WOLF_ARMOR, "dyed_armor");
/* 2360 */     CustomCraftingRecipeBuilder.customCrafting(RecipeCategory.MISC, (commonInfo, bookInfo) -> new ImbueRecipe(commonInfo, bookInfo, Ingredient.of((ItemLike)Items.LINGERING_POTION), Ingredient.of((ItemLike)Items.ARROW), new ItemStackTemplate(Items.TIPPED_ARROW, 8)))
/* 2361 */       .unlockedBy("has_lingering_potion", has((ItemLike)Items.LINGERING_POTION))
/* 2362 */       .save(this.output, "tipped_arrow");
/* 2364 */     SpecialRecipeBuilder.special(() -> new BookCloningRecipe(Ingredient.of((ItemLike)Items.WRITTEN_BOOK), tag(ItemTags.BOOK_CLONING_TARGET), BookCloningRecipe.DEFAULT_BOOK_GENERATION_RANGES, new ItemStackTemplate(Items.WRITTEN_BOOK)))
/* 2365 */       .save(this.output, "book_cloning");
/* 2367 */     SpecialRecipeBuilder.special(() -> new FireworkRocketRecipe(Ingredient.of((ItemLike)Items.PAPER), Ingredient.of((ItemLike)Items.GUNPOWDER), Ingredient.of((ItemLike)Items.FIREWORK_STAR), new ItemStackTemplate(Items.FIREWORK_ROCKET, 3)))
/* 2368 */       .save(this.output, "firework_rocket");
/* 2370 */     SpecialRecipeBuilder.special(() -> new FireworkStarRecipe(Map.of(FireworkExplosion.Shape.LARGE_BALL, Ingredient.of((ItemLike)Items.FIRE_CHARGE), FireworkExplosion.Shape.BURST, Ingredient.of((ItemLike)Items.FEATHER), FireworkExplosion.Shape.STAR, Ingredient.of((ItemLike)Items.GOLD_NUGGET), FireworkExplosion.Shape.CREEPER, tag(ItemTags.SKULLS)), Ingredient.of((ItemLike)Items.DIAMOND), Ingredient.of((ItemLike)Items.GLOWSTONE_DUST), Ingredient.of((ItemLike)Items.GUNPOWDER), tag(ItemTags.DYES), new ItemStackTemplate(Items.FIREWORK_STAR)))
/*      */       
/* 2383 */       .save(this.output, "firework_star");
/* 2385 */     SpecialRecipeBuilder.special(() -> new FireworkStarFadeRecipe(Ingredient.of((ItemLike)Items.FIREWORK_STAR), tag(ItemTags.DYES), new ItemStackTemplate(Items.FIREWORK_STAR)))
/* 2386 */       .save(this.output, "firework_star_fade");
/* 2388 */     TransmuteRecipeBuilder.transmute(RecipeCategory.MISC, 
/*      */         
/* 2390 */         Ingredient.of((ItemLike)Items.FILLED_MAP), 
/* 2391 */         Ingredient.of((ItemLike)Items.MAP), new ItemStackTemplate(Items.FILLED_MAP))
/*      */       
/* 2394 */       .addMaterialCountToOutput()
/* 2395 */       .setMaterialCount(TransmuteRecipe.FULL_RANGE_MATERIAL_COUNT)
/* 2396 */       .group("map_cloning")
/* 2397 */       .unlockedBy("has_filled_map", has((ItemLike)Items.FILLED_MAP))
/* 2398 */       .save(this.output, "map_cloning");
/* 2400 */     SpecialRecipeBuilder.special(() -> new MapExtendingRecipe(Ingredient.of((ItemLike)Items.FILLED_MAP), Ingredient.of((ItemLike)Items.PAPER), new ItemStackTemplate(Items.FILLED_MAP)))
/* 2401 */       .save(this.output, "map_extending");
/* 2403 */     SpecialRecipeBuilder.special(net.minecraft.world.item.crafting.RepairItemRecipe::new)
/* 2404 */       .save(this.output, "repair_item");
/* 2406 */     SpecialRecipeBuilder.special(() -> new ShieldDecorationRecipe(tag(ItemTags.BANNERS), Ingredient.of((ItemLike)Items.SHIELD), new ItemStackTemplate(Items.SHIELD)))
/* 2407 */       .save(this.output, "shield_decoration");
/* 2410 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Items.POTATO), RecipeCategory.FOOD, CookingBookCategory.FOOD, (ItemLike)Items.BAKED_POTATO, 0.35F, 200)
/* 2411 */       .unlockedBy("has_potato", has((ItemLike)Items.POTATO))
/* 2412 */       .save(this.output);
/* 2414 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Items.CLAY_BALL), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.BRICK, 0.3F, 200)
/* 2415 */       .unlockedBy("has_clay_ball", has((ItemLike)Items.CLAY_BALL))
/* 2416 */       .save(this.output);
/* 2418 */     SimpleCookingRecipeBuilder.smelting(tag(ItemTags.LOGS_THAT_BURN), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.CHARCOAL, 0.15F, 200)
/* 2419 */       .unlockedBy("has_log", has(ItemTags.LOGS_THAT_BURN))
/* 2420 */       .save(this.output);
/* 2422 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Items.CHORUS_FRUIT), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.POPPED_CHORUS_FRUIT, 0.1F, 200)
/* 2423 */       .unlockedBy("has_chorus_fruit", has((ItemLike)Items.CHORUS_FRUIT))
/* 2424 */       .save(this.output);
/* 2426 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Items.BEEF), RecipeCategory.FOOD, CookingBookCategory.FOOD, (ItemLike)Items.COOKED_BEEF, 0.35F, 200)
/* 2427 */       .unlockedBy("has_beef", has((ItemLike)Items.BEEF))
/* 2428 */       .save(this.output);
/* 2430 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Items.CHICKEN), RecipeCategory.FOOD, CookingBookCategory.FOOD, (ItemLike)Items.COOKED_CHICKEN, 0.35F, 200)
/* 2431 */       .unlockedBy("has_chicken", has((ItemLike)Items.CHICKEN))
/* 2432 */       .save(this.output);
/* 2434 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Items.COD), RecipeCategory.FOOD, CookingBookCategory.FOOD, (ItemLike)Items.COOKED_COD, 0.35F, 200)
/* 2435 */       .unlockedBy("has_cod", has((ItemLike)Items.COD))
/* 2436 */       .save(this.output);
/* 2438 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Blocks.KELP), RecipeCategory.FOOD, CookingBookCategory.FOOD, (ItemLike)Items.DRIED_KELP, 0.1F, 200)
/* 2439 */       .unlockedBy("has_kelp", has((ItemLike)Blocks.KELP))
/* 2440 */       .save(this.output, getSmeltingRecipeName((ItemLike)Items.DRIED_KELP));
/* 2442 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Items.SALMON), RecipeCategory.FOOD, CookingBookCategory.FOOD, (ItemLike)Items.COOKED_SALMON, 0.35F, 200)
/* 2443 */       .unlockedBy("has_salmon", has((ItemLike)Items.SALMON))
/* 2444 */       .save(this.output);
/* 2446 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Items.MUTTON), RecipeCategory.FOOD, CookingBookCategory.FOOD, (ItemLike)Items.COOKED_MUTTON, 0.35F, 200)
/* 2447 */       .unlockedBy("has_mutton", has((ItemLike)Items.MUTTON))
/* 2448 */       .save(this.output);
/* 2450 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Items.PORKCHOP), RecipeCategory.FOOD, CookingBookCategory.FOOD, (ItemLike)Items.COOKED_PORKCHOP, 0.35F, 200)
/* 2451 */       .unlockedBy("has_porkchop", has((ItemLike)Items.PORKCHOP))
/* 2452 */       .save(this.output);
/* 2454 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Items.RABBIT), RecipeCategory.FOOD, CookingBookCategory.FOOD, (ItemLike)Items.COOKED_RABBIT, 0.35F, 200)
/* 2455 */       .unlockedBy("has_rabbit", has((ItemLike)Items.RABBIT))
/* 2456 */       .save(this.output);
/* 2458 */     oreSmelting((List)COAL_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.COAL, 0.1F, 200, "coal");
/* 2459 */     oreSmelting((List)IRON_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.IRON_INGOT, 0.7F, 200, "iron_ingot");
/* 2460 */     oreSmelting((List)COPPER_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.COPPER_INGOT, 0.7F, 200, "copper_ingot");
/* 2461 */     oreSmelting((List)GOLD_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.GOLD_INGOT, 1.0F, 200, "gold_ingot");
/* 2462 */     oreSmelting((List)DIAMOND_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.DIAMOND, 1.0F, 200, "diamond");
/* 2463 */     oreSmelting((List)LAPIS_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.LAPIS_LAZULI, 0.2F, 200, "lapis_lazuli");
/* 2464 */     oreSmelting((List)REDSTONE_SMELTABLES, RecipeCategory.REDSTONE, CookingBookCategory.BLOCKS, (ItemLike)Items.REDSTONE, 0.7F, 200, "redstone");
/* 2465 */     oreSmelting((List)EMERALD_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.EMERALD, 1.0F, 200, "emerald");
/* 2467 */     nineBlockStorageRecipes(RecipeCategory.MISC, (ItemLike)Items.RAW_IRON, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.RAW_IRON_BLOCK);
/* 2468 */     nineBlockStorageRecipes(RecipeCategory.MISC, (ItemLike)Items.RAW_COPPER, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.RAW_COPPER_BLOCK);
/* 2469 */     nineBlockStorageRecipes(RecipeCategory.MISC, (ItemLike)Items.RAW_GOLD, RecipeCategory.BUILDING_BLOCKS, (ItemLike)Items.RAW_GOLD_BLOCK);
/* 2471 */     SimpleCookingRecipeBuilder.smelting(tag(ItemTags.SMELTS_TO_GLASS), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, (ItemLike)Items.GLASS, 0.1F, 200)
/* 2472 */       .unlockedBy("has_smelts_to_glass", has(ItemTags.SMELTS_TO_GLASS))
/* 2473 */       .save(this.output);
/* 2475 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Blocks.SEA_PICKLE), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.DYE.lime(), 0.1F, 200)
/* 2476 */       .unlockedBy("has_sea_pickle", has((ItemLike)Blocks.SEA_PICKLE))
/* 2477 */       .save(this.output, getSmeltingRecipeName((ItemLike)Items.DYE.lime()));
/* 2479 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Items.CACTUS), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.DYE.green(), 1.0F, 200)
/* 2480 */       .unlockedBy("has_cactus", has((ItemLike)Blocks.CACTUS))
/* 2481 */       .save(this.output);
/* 2483 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { 
/* 2483 */             (ItemLike)Items.GOLDEN_PICKAXE, (ItemLike)Items.GOLDEN_SHOVEL, (ItemLike)Items.GOLDEN_AXE, (ItemLike)Items.GOLDEN_HOE, (ItemLike)Items.GOLDEN_SWORD, (ItemLike)Items.GOLDEN_SPEAR, (ItemLike)Items.GOLDEN_HELMET, (ItemLike)Items.GOLDEN_CHESTPLATE, (ItemLike)Items.GOLDEN_LEGGINGS, (ItemLike)Items.GOLDEN_BOOTS, 
/* 2483 */             (ItemLike)Items.GOLDEN_HORSE_ARMOR, (ItemLike)Items.GOLDEN_NAUTILUS_ARMOR }, ), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.GOLD_NUGGET, 0.1F, 200)
/* 2484 */       .unlockedBy("has_golden_pickaxe", has((ItemLike)Items.GOLDEN_PICKAXE))
/* 2485 */       .unlockedBy("has_golden_shovel", has((ItemLike)Items.GOLDEN_SHOVEL))
/* 2486 */       .unlockedBy("has_golden_axe", has((ItemLike)Items.GOLDEN_AXE))
/* 2487 */       .unlockedBy("has_golden_hoe", has((ItemLike)Items.GOLDEN_HOE))
/* 2488 */       .unlockedBy("has_golden_sword", has((ItemLike)Items.GOLDEN_SWORD))
/* 2489 */       .unlockedBy("has_golden_spear", has((ItemLike)Items.GOLDEN_SPEAR))
/* 2490 */       .unlockedBy("has_golden_helmet", has((ItemLike)Items.GOLDEN_HELMET))
/* 2491 */       .unlockedBy("has_golden_chestplate", has((ItemLike)Items.GOLDEN_CHESTPLATE))
/* 2492 */       .unlockedBy("has_golden_leggings", has((ItemLike)Items.GOLDEN_LEGGINGS))
/* 2493 */       .unlockedBy("has_golden_boots", has((ItemLike)Items.GOLDEN_BOOTS))
/* 2494 */       .unlockedBy("has_golden_horse_armor", has((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 2495 */       .unlockedBy("has_golden_nautilus_armor", has((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR))
/* 2496 */       .save(this.output, getSmeltingRecipeName((ItemLike)Items.GOLD_NUGGET));
/* 2498 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { 
/* 2498 */             (ItemLike)Items.COPPER_PICKAXE, (ItemLike)Items.COPPER_SHOVEL, (ItemLike)Items.COPPER_AXE, (ItemLike)Items.COPPER_HOE, (ItemLike)Items.COPPER_SWORD, (ItemLike)Items.COPPER_SPEAR, (ItemLike)Items.COPPER_HELMET, (ItemLike)Items.COPPER_CHESTPLATE, (ItemLike)Items.COPPER_LEGGINGS, (ItemLike)Items.COPPER_BOOTS, 
/* 2498 */             (ItemLike)Items.COPPER_HORSE_ARMOR, (ItemLike)Items.COPPER_NAUTILUS_ARMOR }, ), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.COPPER_NUGGET, 0.1F, 200)
/* 2499 */       .unlockedBy("has_copper_pickaxe", has((ItemLike)Items.COPPER_PICKAXE))
/* 2500 */       .unlockedBy("has_copper_shovel", has((ItemLike)Items.COPPER_SHOVEL))
/* 2501 */       .unlockedBy("has_copper_axe", has((ItemLike)Items.COPPER_AXE))
/* 2502 */       .unlockedBy("has_copper_hoe", has((ItemLike)Items.COPPER_HOE))
/* 2503 */       .unlockedBy("has_copper_sword", has((ItemLike)Items.COPPER_SWORD))
/* 2504 */       .unlockedBy("has_copper_spear", has((ItemLike)Items.COPPER_SPEAR))
/* 2505 */       .unlockedBy("has_copper_helmet", has((ItemLike)Items.COPPER_HELMET))
/* 2506 */       .unlockedBy("has_copper_chestplate", has((ItemLike)Items.COPPER_CHESTPLATE))
/* 2507 */       .unlockedBy("has_copper_leggings", has((ItemLike)Items.COPPER_LEGGINGS))
/* 2508 */       .unlockedBy("has_copper_boots", has((ItemLike)Items.COPPER_BOOTS))
/* 2509 */       .unlockedBy("has_copper_horse_armor", has((ItemLike)Items.COPPER_HORSE_ARMOR))
/* 2510 */       .unlockedBy("has_copper_nautilus_armor", has((ItemLike)Items.COPPER_NAUTILUS_ARMOR))
/* 2511 */       .save(this.output, getSmeltingRecipeName((ItemLike)Items.COPPER_NUGGET));
/* 2513 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { 
/* 2513 */             (ItemLike)Items.IRON_PICKAXE, (ItemLike)Items.IRON_SHOVEL, (ItemLike)Items.IRON_AXE, (ItemLike)Items.IRON_HOE, (ItemLike)Items.IRON_SWORD, (ItemLike)Items.IRON_SPEAR, (ItemLike)Items.IRON_HELMET, (ItemLike)Items.IRON_CHESTPLATE, (ItemLike)Items.IRON_LEGGINGS, (ItemLike)Items.IRON_BOOTS, 
/* 2513 */             (ItemLike)Items.IRON_HORSE_ARMOR, (ItemLike)Items.CHAINMAIL_HELMET, (ItemLike)Items.CHAINMAIL_CHESTPLATE, (ItemLike)Items.CHAINMAIL_LEGGINGS, (ItemLike)Items.CHAINMAIL_BOOTS, (ItemLike)Items.IRON_NAUTILUS_ARMOR }, ), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.IRON_NUGGET, 0.1F, 200)
/* 2514 */       .unlockedBy("has_iron_pickaxe", has((ItemLike)Items.IRON_PICKAXE))
/* 2515 */       .unlockedBy("has_iron_shovel", has((ItemLike)Items.IRON_SHOVEL))
/* 2516 */       .unlockedBy("has_iron_axe", has((ItemLike)Items.IRON_AXE))
/* 2517 */       .unlockedBy("has_iron_hoe", has((ItemLike)Items.IRON_HOE))
/* 2518 */       .unlockedBy("has_iron_sword", has((ItemLike)Items.IRON_SWORD))
/* 2519 */       .unlockedBy("has_iron_spear", has((ItemLike)Items.IRON_SPEAR))
/* 2520 */       .unlockedBy("has_iron_helmet", has((ItemLike)Items.IRON_HELMET))
/* 2521 */       .unlockedBy("has_iron_chestplate", has((ItemLike)Items.IRON_CHESTPLATE))
/* 2522 */       .unlockedBy("has_iron_leggings", has((ItemLike)Items.IRON_LEGGINGS))
/* 2523 */       .unlockedBy("has_iron_boots", has((ItemLike)Items.IRON_BOOTS))
/* 2524 */       .unlockedBy("has_iron_horse_armor", has((ItemLike)Items.IRON_HORSE_ARMOR))
/* 2525 */       .unlockedBy("has_chainmail_helmet", has((ItemLike)Items.CHAINMAIL_HELMET))
/* 2526 */       .unlockedBy("has_chainmail_chestplate", has((ItemLike)Items.CHAINMAIL_CHESTPLATE))
/* 2527 */       .unlockedBy("has_chainmail_leggings", has((ItemLike)Items.CHAINMAIL_LEGGINGS))
/* 2528 */       .unlockedBy("has_chainmail_boots", has((ItemLike)Items.CHAINMAIL_BOOTS))
/* 2529 */       .unlockedBy("has_iron_nautilus_armor", has((ItemLike)Items.IRON_NAUTILUS_ARMOR))
/* 2530 */       .save(this.output, getSmeltingRecipeName((ItemLike)Items.IRON_NUGGET));
/* 2532 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Blocks.CLAY), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, (ItemLike)Items.TERRACOTTA, 0.35F, 200)
/* 2533 */       .unlockedBy("has_clay_block", has((ItemLike)Blocks.CLAY))
/* 2534 */       .save(this.output);
/* 2536 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Blocks.NETHERRACK), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.NETHER_BRICK, 0.1F, 200)
/* 2537 */       .unlockedBy("has_netherrack", has((ItemLike)Blocks.NETHERRACK))
/* 2538 */       .save(this.output);
/* 2540 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Items.RESIN_CLUMP), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.RESIN_BRICK, 0.1F, 200)
/* 2541 */       .unlockedBy("has_resin_clump", has((ItemLike)Blocks.RESIN_CLUMP))
/* 2542 */       .save(this.output);
/* 2544 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Blocks.NETHER_QUARTZ_ORE), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.QUARTZ, 0.2F, 200)
/* 2545 */       .unlockedBy("has_nether_quartz_ore", has((ItemLike)Blocks.NETHER_QUARTZ_ORE))
/* 2546 */       .save(this.output);
/* 2548 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Blocks.WET_SPONGE), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, (ItemLike)Items.SPONGE, 0.15F, 200)
/* 2549 */       .unlockedBy("has_wet_sponge", has((ItemLike)Blocks.WET_SPONGE))
/* 2550 */       .save(this.output);
/* 2552 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Blocks.STONE), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, (ItemLike)Items.SMOOTH_STONE, 0.1F, 200)
/* 2553 */       .unlockedBy("has_stone", has((ItemLike)Blocks.STONE))
/* 2554 */       .save(this.output);
/* 2556 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Blocks.SANDSTONE), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, (ItemLike)Items.SMOOTH_SANDSTONE, 0.1F, 200)
/* 2557 */       .unlockedBy("has_sandstone", has((ItemLike)Blocks.SANDSTONE))
/* 2558 */       .save(this.output);
/* 2560 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Blocks.RED_SANDSTONE), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, (ItemLike)Items.SMOOTH_RED_SANDSTONE, 0.1F, 200)
/* 2561 */       .unlockedBy("has_red_sandstone", has((ItemLike)Blocks.RED_SANDSTONE))
/* 2562 */       .save(this.output);
/* 2564 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Blocks.QUARTZ_BLOCK), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, (ItemLike)Items.SMOOTH_QUARTZ, 0.1F, 200)
/* 2565 */       .unlockedBy("has_quartz_block", has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 2566 */       .save(this.output);
/* 2568 */     DyeColor.VALUES.forEach(dyeColor -> SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Blocks.DYED_TERRACOTTA.pick(dyeColor)), RecipeCategory.DECORATIONS, CookingBookCategory.BLOCKS, (ItemLike)Items.GLAZED_TERRACOTTA.pick(dyeColor), 0.1F, 200).unlockedBy("has_" + dyeColor.getName() + "_terracotta", has((ItemLike)Blocks.DYED_TERRACOTTA.pick(dyeColor))).save(this.output));
/* 2574 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Blocks.ANCIENT_DEBRIS), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.NETHERITE_SCRAP, 2.0F, 200)
/* 2575 */       .unlockedBy("has_ancient_debris", has((ItemLike)Blocks.ANCIENT_DEBRIS))
/* 2576 */       .save(this.output);
/* 2578 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((ItemLike)Blocks.BASALT), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, (ItemLike)Items.SMOOTH_BASALT, 0.1F, 200)
/* 2579 */       .unlockedBy("has_basalt", has((ItemLike)Blocks.BASALT))
/* 2580 */       .save(this.output);
/* 2582 */     SimpleCookingRecipeBuilder.smelting(tag(ItemTags.LEAVES), RecipeCategory.MISC, CookingBookCategory.BLOCKS, (ItemLike)Items.LEAF_LITTER, 0.1F, 200)
/* 2583 */       .unlockedBy("has_leaves", has(ItemTags.LEAVES))
/* 2584 */       .save(this.output);
/* 2587 */     oreBlasting((List)COAL_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.COAL, 0.1F, 100, "coal");
/* 2588 */     oreBlasting((List)IRON_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.IRON_INGOT, 0.7F, 100, "iron_ingot");
/* 2589 */     oreBlasting((List)COPPER_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.COPPER_INGOT, 0.7F, 100, "copper_ingot");
/* 2590 */     oreBlasting((List)GOLD_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.GOLD_INGOT, 1.0F, 100, "gold_ingot");
/* 2591 */     oreBlasting((List)DIAMOND_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.DIAMOND, 1.0F, 100, "diamond");
/* 2592 */     oreBlasting((List)LAPIS_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.LAPIS_LAZULI, 0.2F, 100, "lapis_lazuli");
/* 2593 */     oreBlasting((List)REDSTONE_SMELTABLES, RecipeCategory.REDSTONE, CookingBookCategory.BLOCKS, (ItemLike)Items.REDSTONE, 0.7F, 100, "redstone");
/* 2594 */     oreBlasting((List)EMERALD_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.EMERALD, 1.0F, 100, "emerald");
/* 2596 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of((ItemLike)Blocks.NETHER_QUARTZ_ORE), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.QUARTZ, 0.2F, 100)
/* 2597 */       .unlockedBy("has_nether_quartz_ore", has((ItemLike)Blocks.NETHER_QUARTZ_ORE))
/* 2598 */       .save(this.output, getBlastingRecipeName((ItemLike)Items.QUARTZ));
/* 2600 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { 
/* 2600 */             (ItemLike)Items.GOLDEN_PICKAXE, (ItemLike)Items.GOLDEN_SHOVEL, (ItemLike)Items.GOLDEN_AXE, (ItemLike)Items.GOLDEN_HOE, (ItemLike)Items.GOLDEN_SWORD, (ItemLike)Items.GOLDEN_SPEAR, (ItemLike)Items.GOLDEN_HELMET, (ItemLike)Items.GOLDEN_CHESTPLATE, (ItemLike)Items.GOLDEN_LEGGINGS, (ItemLike)Items.GOLDEN_BOOTS, 
/* 2600 */             (ItemLike)Items.GOLDEN_HORSE_ARMOR, (ItemLike)Items.GOLDEN_NAUTILUS_ARMOR }, ), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.GOLD_NUGGET, 0.1F, 100)
/* 2601 */       .unlockedBy("has_golden_pickaxe", has((ItemLike)Items.GOLDEN_PICKAXE))
/* 2602 */       .unlockedBy("has_golden_shovel", has((ItemLike)Items.GOLDEN_SHOVEL))
/* 2603 */       .unlockedBy("has_golden_axe", has((ItemLike)Items.GOLDEN_AXE))
/* 2604 */       .unlockedBy("has_golden_hoe", has((ItemLike)Items.GOLDEN_HOE))
/* 2605 */       .unlockedBy("has_golden_sword", has((ItemLike)Items.GOLDEN_SWORD))
/* 2606 */       .unlockedBy("has_golden_spear", has((ItemLike)Items.GOLDEN_SPEAR))
/* 2607 */       .unlockedBy("has_golden_helmet", has((ItemLike)Items.GOLDEN_HELMET))
/* 2608 */       .unlockedBy("has_golden_chestplate", has((ItemLike)Items.GOLDEN_CHESTPLATE))
/* 2609 */       .unlockedBy("has_golden_leggings", has((ItemLike)Items.GOLDEN_LEGGINGS))
/* 2610 */       .unlockedBy("has_golden_boots", has((ItemLike)Items.GOLDEN_BOOTS))
/* 2611 */       .unlockedBy("has_golden_horse_armor", has((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 2612 */       .unlockedBy("has_golden_nautilus_armor", has((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR))
/* 2613 */       .save(this.output, getBlastingRecipeName((ItemLike)Items.GOLD_NUGGET));
/* 2615 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { 
/* 2615 */             (ItemLike)Items.COPPER_PICKAXE, (ItemLike)Items.COPPER_SHOVEL, (ItemLike)Items.COPPER_AXE, (ItemLike)Items.COPPER_HOE, (ItemLike)Items.COPPER_SWORD, (ItemLike)Items.COPPER_SPEAR, (ItemLike)Items.COPPER_HELMET, (ItemLike)Items.COPPER_CHESTPLATE, (ItemLike)Items.COPPER_LEGGINGS, (ItemLike)Items.COPPER_BOOTS, 
/* 2615 */             (ItemLike)Items.COPPER_HORSE_ARMOR, (ItemLike)Items.COPPER_NAUTILUS_ARMOR }, ), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.COPPER_NUGGET, 0.1F, 100)
/* 2616 */       .unlockedBy("has_copper_pickaxe", has((ItemLike)Items.COPPER_PICKAXE))
/* 2617 */       .unlockedBy("has_copper_shovel", has((ItemLike)Items.COPPER_SHOVEL))
/* 2618 */       .unlockedBy("has_copper_axe", has((ItemLike)Items.COPPER_AXE))
/* 2619 */       .unlockedBy("has_copper_hoe", has((ItemLike)Items.COPPER_HOE))
/* 2620 */       .unlockedBy("has_copper_sword", has((ItemLike)Items.COPPER_SWORD))
/* 2621 */       .unlockedBy("has_copper_spear", has((ItemLike)Items.COPPER_SPEAR))
/* 2622 */       .unlockedBy("has_copper_helmet", has((ItemLike)Items.COPPER_HELMET))
/* 2623 */       .unlockedBy("has_copper_chestplate", has((ItemLike)Items.COPPER_CHESTPLATE))
/* 2624 */       .unlockedBy("has_copper_leggings", has((ItemLike)Items.COPPER_LEGGINGS))
/* 2625 */       .unlockedBy("has_copper_boots", has((ItemLike)Items.COPPER_BOOTS))
/* 2626 */       .unlockedBy("has_copper_horse_armor", has((ItemLike)Items.COPPER_HORSE_ARMOR))
/* 2627 */       .unlockedBy("has_copper_nautilus_armor", has((ItemLike)Items.COPPER_NAUTILUS_ARMOR))
/* 2628 */       .save(this.output, getBlastingRecipeName((ItemLike)Items.COPPER_NUGGET));
/* 2630 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { 
/* 2630 */             (ItemLike)Items.IRON_PICKAXE, (ItemLike)Items.IRON_SHOVEL, (ItemLike)Items.IRON_AXE, (ItemLike)Items.IRON_HOE, (ItemLike)Items.IRON_SWORD, (ItemLike)Items.IRON_SPEAR, (ItemLike)Items.IRON_HELMET, (ItemLike)Items.IRON_CHESTPLATE, (ItemLike)Items.IRON_LEGGINGS, (ItemLike)Items.IRON_BOOTS, 
/* 2630 */             (ItemLike)Items.IRON_HORSE_ARMOR, (ItemLike)Items.IRON_NAUTILUS_ARMOR, (ItemLike)Items.CHAINMAIL_HELMET, (ItemLike)Items.CHAINMAIL_CHESTPLATE, (ItemLike)Items.CHAINMAIL_LEGGINGS, (ItemLike)Items.CHAINMAIL_BOOTS }, ), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.IRON_NUGGET, 0.1F, 100)
/* 2631 */       .unlockedBy("has_iron_pickaxe", has((ItemLike)Items.IRON_PICKAXE))
/* 2632 */       .unlockedBy("has_iron_shovel", has((ItemLike)Items.IRON_SHOVEL))
/* 2633 */       .unlockedBy("has_iron_axe", has((ItemLike)Items.IRON_AXE))
/* 2634 */       .unlockedBy("has_iron_hoe", has((ItemLike)Items.IRON_HOE))
/* 2635 */       .unlockedBy("has_iron_sword", has((ItemLike)Items.IRON_SWORD))
/* 2636 */       .unlockedBy("has_iron_spear", has((ItemLike)Items.IRON_SPEAR))
/* 2637 */       .unlockedBy("has_iron_helmet", has((ItemLike)Items.IRON_HELMET))
/* 2638 */       .unlockedBy("has_iron_chestplate", has((ItemLike)Items.IRON_CHESTPLATE))
/* 2639 */       .unlockedBy("has_iron_leggings", has((ItemLike)Items.IRON_LEGGINGS))
/* 2640 */       .unlockedBy("has_iron_boots", has((ItemLike)Items.IRON_BOOTS))
/* 2641 */       .unlockedBy("has_iron_horse_armor", has((ItemLike)Items.IRON_HORSE_ARMOR))
/* 2642 */       .unlockedBy("has_chainmail_helmet", has((ItemLike)Items.CHAINMAIL_HELMET))
/* 2643 */       .unlockedBy("has_chainmail_chestplate", has((ItemLike)Items.CHAINMAIL_CHESTPLATE))
/* 2644 */       .unlockedBy("has_chainmail_leggings", has((ItemLike)Items.CHAINMAIL_LEGGINGS))
/* 2645 */       .unlockedBy("has_chainmail_boots", has((ItemLike)Items.CHAINMAIL_BOOTS))
/* 2646 */       .unlockedBy("has_iron_nautilus_armor", has((ItemLike)Items.IRON_NAUTILUS_ARMOR))
/* 2647 */       .save(this.output, getBlastingRecipeName((ItemLike)Items.IRON_NUGGET));
/* 2649 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of((ItemLike)Blocks.ANCIENT_DEBRIS), RecipeCategory.MISC, CookingBookCategory.MISC, (ItemLike)Items.NETHERITE_SCRAP, 2.0F, 100)
/* 2650 */       .unlockedBy("has_ancient_debris", has((ItemLike)Blocks.ANCIENT_DEBRIS))
/* 2651 */       .save(this.output, getBlastingRecipeName((ItemLike)Items.NETHERITE_SCRAP));
/* 2653 */     cookRecipes("smoking", net.minecraft.world.item.crafting.SmokingRecipe::new, 100);
/* 2654 */     cookRecipes("campfire_cooking", net.minecraft.world.item.crafting.CampfireCookingRecipe::new, 600);
/* 2657 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.POLISHED_BASALT, (ItemLike)Blocks.BASALT);
/* 2659 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.SMOOTH_STONE_SLAB, (ItemLike)Blocks.SMOOTH_STONE, 2);
/* 2661 */     WeatheringCopperCollection.zipApply(Blocks.CUT_COPPER, Blocks.COPPER_BLOCK, (cutBlock, material) -> stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, (ItemLike)cutBlock, (ItemLike)material, 4));
/* 2662 */     WeatheringCopperCollection.zipApply(Blocks.CUT_COPPER_STAIRS, Blocks.COPPER_BLOCK, (cutStairs, material) -> stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, (ItemLike)cutStairs, (ItemLike)material, 4));
/* 2663 */     WeatheringCopperCollection.zipApply(Blocks.CUT_COPPER_SLAB, Blocks.COPPER_BLOCK, (cutSlab, material) -> stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, (ItemLike)cutSlab, (ItemLike)material, 8));
/* 2665 */     smithingTrims().forEach(trim -> trimSmithing(trim.template(), trim.patternId(), trim.recipeId()));
/* 2667 */     netheriteSmithing(Items.DIAMOND_CHESTPLATE, RecipeCategory.COMBAT, Items.NETHERITE_CHESTPLATE);
/* 2668 */     netheriteSmithing(Items.DIAMOND_LEGGINGS, RecipeCategory.COMBAT, Items.NETHERITE_LEGGINGS);
/* 2669 */     netheriteSmithing(Items.DIAMOND_HELMET, RecipeCategory.COMBAT, Items.NETHERITE_HELMET);
/* 2670 */     netheriteSmithing(Items.DIAMOND_BOOTS, RecipeCategory.COMBAT, Items.NETHERITE_BOOTS);
/* 2671 */     netheriteSmithing(Items.DIAMOND_NAUTILUS_ARMOR, RecipeCategory.COMBAT, Items.NETHERITE_NAUTILUS_ARMOR);
/* 2672 */     netheriteSmithing(Items.DIAMOND_HORSE_ARMOR, RecipeCategory.COMBAT, Items.NETHERITE_HORSE_ARMOR);
/* 2673 */     netheriteSmithing(Items.DIAMOND_SWORD, RecipeCategory.COMBAT, Items.NETHERITE_SWORD);
/* 2674 */     netheriteSmithing(Items.DIAMOND_SPEAR, RecipeCategory.COMBAT, Items.NETHERITE_SPEAR);
/* 2675 */     netheriteSmithing(Items.DIAMOND_AXE, RecipeCategory.TOOLS, Items.NETHERITE_AXE);
/* 2676 */     netheriteSmithing(Items.DIAMOND_PICKAXE, RecipeCategory.TOOLS, Items.NETHERITE_PICKAXE);
/* 2677 */     netheriteSmithing(Items.DIAMOND_HOE, RecipeCategory.TOOLS, Items.NETHERITE_HOE);
/* 2678 */     netheriteSmithing(Items.DIAMOND_SHOVEL, RecipeCategory.TOOLS, Items.NETHERITE_SHOVEL);
/* 2680 */     copySmithingTemplate((ItemLike)Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, (ItemLike)Items.NETHERRACK);
/* 2681 */     copySmithingTemplate((ItemLike)Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.COBBLESTONE);
/* 2682 */     copySmithingTemplate((ItemLike)Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.SANDSTONE);
/* 2683 */     copySmithingTemplate((ItemLike)Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.COBBLESTONE);
/* 2684 */     copySmithingTemplate((ItemLike)Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.MOSSY_COBBLESTONE);
/* 2685 */     copySmithingTemplate((ItemLike)Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.COBBLED_DEEPSLATE);
/* 2686 */     copySmithingTemplate((ItemLike)Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.END_STONE);
/* 2687 */     copySmithingTemplate((ItemLike)Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.COBBLESTONE);
/* 2688 */     copySmithingTemplate((ItemLike)Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.PRISMARINE);
/* 2689 */     copySmithingTemplate((ItemLike)Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.BLACKSTONE);
/* 2690 */     copySmithingTemplate((ItemLike)Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.NETHERRACK);
/* 2691 */     copySmithingTemplate((ItemLike)Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.PURPUR_BLOCK);
/* 2692 */     copySmithingTemplate((ItemLike)Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.COBBLED_DEEPSLATE);
/* 2693 */     copySmithingTemplate((ItemLike)Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.TERRACOTTA);
/* 2694 */     copySmithingTemplate((ItemLike)Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.TERRACOTTA);
/* 2695 */     copySmithingTemplate((ItemLike)Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.TERRACOTTA);
/* 2696 */     copySmithingTemplate((ItemLike)Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.TERRACOTTA);
/* 2697 */     copySmithingTemplate((ItemLike)Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, (ItemLike)Items.BREEZE_ROD);
/* 2698 */     copySmithingTemplate((ItemLike)Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, Ingredient.of(new ItemLike[] { (ItemLike)Items.COPPER_BLOCK.weathering().unaffected(), (ItemLike)Items.COPPER_BLOCK.waxed().unaffected() }));
/* 2700 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.BAMBOO_BLOCK, (ItemLike)Items.BAMBOO);
/* 2702 */     planksFromLogs((ItemLike)Blocks.BAMBOO_PLANKS, ItemTags.BAMBOO_BLOCKS, 2);
/* 2704 */     mosaicBuilder(RecipeCategory.DECORATIONS, (ItemLike)Blocks.BAMBOO_MOSAIC, (ItemLike)Blocks.BAMBOO_SLAB);
/* 2705 */     woodenBoat((ItemLike)Items.BAMBOO_RAFT, (ItemLike)Blocks.BAMBOO_PLANKS);
/* 2706 */     chestBoat((ItemLike)Items.BAMBOO_CHEST_RAFT, (ItemLike)Items.BAMBOO_RAFT);
/* 2708 */     shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.CHISELED_BOOKSHELF)
/* 2709 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 2710 */       .define(Character.valueOf('X'), ItemTags.WOODEN_SLABS)
/* 2711 */       .pattern("###")
/* 2712 */       .pattern("XXX")
/* 2713 */       .pattern("###")
/* 2714 */       .unlockedBy("has_book", has((ItemLike)Items.BOOK))
/* 2715 */       .save(this.output);
/* 2717 */     oneToOneConversionRecipe((ItemLike)Items.DYE.orange(), (ItemLike)Blocks.TORCHFLOWER, "orange_dye");
/* 2718 */     oneToOneConversionRecipe((ItemLike)Items.DYE.cyan(), (ItemLike)Blocks.PITCHER_PLANT, "cyan_dye", 2);
/* 2720 */     planksFromLog((ItemLike)Blocks.CHERRY_PLANKS, ItemTags.CHERRY_LOGS, 4);
/* 2721 */     woodFromLogs((ItemLike)Blocks.CHERRY_WOOD, (ItemLike)Blocks.CHERRY_LOG);
/* 2722 */     woodFromLogs((ItemLike)Blocks.STRIPPED_CHERRY_WOOD, (ItemLike)Blocks.STRIPPED_CHERRY_LOG);
/* 2723 */     woodenBoat((ItemLike)Items.CHERRY_BOAT, (ItemLike)Blocks.CHERRY_PLANKS);
/* 2724 */     chestBoat((ItemLike)Items.CHERRY_CHEST_BOAT, (ItemLike)Items.CHERRY_BOAT);
/* 2725 */     oneToOneConversionRecipe((ItemLike)Items.DYE.pink(), (ItemLike)Items.PINK_PETALS, "pink_dye", 1);
/* 2727 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.BRUSH)
/* 2728 */       .define(Character.valueOf('X'), (ItemLike)Items.FEATHER)
/* 2729 */       .define(Character.valueOf('#'), (ItemLike)Items.COPPER_INGOT)
/* 2730 */       .define(Character.valueOf('I'), (ItemLike)Items.STICK)
/* 2731 */       .pattern("X")
/* 2732 */       .pattern("#")
/* 2733 */       .pattern("I")
/* 2734 */       .unlockedBy("has_copper_ingot", has((ItemLike)Items.COPPER_INGOT))
/* 2735 */       .save(this.output);
/* 2737 */     shaped(RecipeCategory.DECORATIONS, (ItemLike)Items.DECORATED_POT)
/* 2738 */       .define(Character.valueOf('#'), (ItemLike)Items.BRICK)
/* 2739 */       .pattern(" # ")
/* 2740 */       .pattern("# #")
/* 2741 */       .pattern(" # ")
/* 2742 */       .unlockedBy("has_brick", has(ItemTags.DECORATED_POT_INGREDIENTS))
/* 2743 */       .save(this.output, "decorated_pot_simple");
/* 2745 */     SpecialRecipeBuilder.special(() -> new DecoratedPotRecipe(tag(ItemTags.DECORATED_POT_INGREDIENTS), new ItemStackTemplate(Items.DECORATED_POT)))
/* 2746 */       .save(this.output, "decorated_pot");
/* 2749 */     shaped(RecipeCategory.REDSTONE, (ItemLike)Blocks.CRAFTER)
/* 2750 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/* 2751 */       .define(Character.valueOf('C'), (ItemLike)Items.CRAFTING_TABLE)
/* 2752 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/* 2753 */       .define(Character.valueOf('D'), (ItemLike)Items.DROPPER)
/* 2754 */       .pattern("###")
/* 2755 */       .pattern("#C#")
/* 2756 */       .pattern("RDR")
/* 2757 */       .unlockedBy("has_dropper", has((ItemLike)Items.DROPPER))
/* 2758 */       .save(this.output);
/* 2760 */     WeatheringCopper.WeatherState.forEach(state -> {
/*      */           stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.CHISELED_COPPER.weathering().pick(state), (ItemLike)Blocks.COPPER_BLOCK.weathering().pick(state), 4);
/*      */           stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.CHISELED_COPPER.waxed().pick(state), (ItemLike)Blocks.COPPER_BLOCK.waxed().pick(state), 4);
/*      */           stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.CHISELED_COPPER.waxed().pick(state), (ItemLike)Blocks.CUT_COPPER.waxed().pick(state), 1);
/*      */         });
/* 2766 */     vanillaRecipeProvider = this;
/* 2766 */     WeatheringCopperCollection.zipApply(Blocks.COPPER_GRATE, Blocks.COPPER_BLOCK, (x$0, x$1) -> rec$.grate(x$0, x$1));
/* 2768 */     vanillaRecipeProvider = this;
/* 2768 */     WeatheringCopperCollection.zipApply(Blocks.COPPER_BULB, Blocks.COPPER_BLOCK, (x$0, x$1) -> rec$.copperBulb(x$0, x$1));
/* 2770 */     WeatheringCopper.WeatherState.forEach(state -> waxedChiseled((Block)Blocks.CHISELED_COPPER.waxed().pick(state), (Block)Blocks.CUT_COPPER_SLAB.waxed().pick(state)));
/* 2773 */     WeatheringCopperCollection.zipApply(Blocks.COPPER_GRATE, Blocks.COPPER_BLOCK, (grate, block) -> stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, (ItemLike)grate, (ItemLike)block, 4));
/* 2777 */     shapeless(RecipeCategory.MISC, (ItemLike)Items.WIND_CHARGE, 4)
/* 2778 */       .requires((ItemLike)Items.BREEZE_ROD)
/* 2779 */       .unlockedBy("has_breeze_rod", has((ItemLike)Items.BREEZE_ROD))
/* 2780 */       .save(this.output);
/* 2782 */     shaped(RecipeCategory.COMBAT, (ItemLike)Items.MACE, 1)
/* 2783 */       .define(Character.valueOf('I'), (ItemLike)Items.BREEZE_ROD)
/* 2784 */       .define(Character.valueOf('#'), (ItemLike)Blocks.HEAVY_CORE)
/* 2785 */       .pattern(" # ")
/* 2786 */       .pattern(" I ")
/* 2787 */       .unlockedBy("has_breeze_rod", has((ItemLike)Items.BREEZE_ROD))
/* 2788 */       .unlockedBy("has_heavy_core", has((ItemLike)Blocks.HEAVY_CORE))
/* 2789 */       .save(this.output);
/* 2791 */     doorBuilder((ItemLike)Blocks.COPPER_DOOR.weathering().unaffected(), Ingredient.of((ItemLike)Items.COPPER_INGOT))
/* 2792 */       .unlockedBy(getHasName((ItemLike)Items.COPPER_INGOT), has((ItemLike)Items.COPPER_INGOT))
/* 2793 */       .save(this.output);
/* 2795 */     twoByTwoPacker(RecipeCategory.REDSTONE, (ItemLike)Blocks.COPPER_TRAPDOOR.weathering().unaffected(), (ItemLike)Items.COPPER_INGOT);
/* 2797 */     shaped(RecipeCategory.TOOLS, (ItemLike)Items.BUNDLE)
/* 2798 */       .define(Character.valueOf('-'), (ItemLike)Items.STRING)
/* 2799 */       .define(Character.valueOf('#'), (ItemLike)Items.LEATHER)
/* 2800 */       .pattern("-")
/* 2801 */       .pattern("#")
/* 2802 */       .unlockedBy("has_string", has((ItemLike)Items.STRING))
/* 2803 */       .save(this.output);
/* 2805 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, (ItemLike)Blocks.POTENT_SULFUR, (ItemLike)Items.SULFUR);
/* 2807 */     vanillaRecipeProvider = this;
/* 2807 */     ColorCollection.zipApply(Items.DYE, Items.DYED_BUNDLE, (x$0, x$1) -> rec$.dyedBundleRecipe(x$0, x$1));
/*      */   }
/*      */   
/*      */   public static final class TrimTemplate extends Record {
/*      */     private final Item template;
/*      */     
/*      */     private final ResourceKey<TrimPattern> patternId;
/*      */     
/*      */     private final ResourceKey<Recipe<?>> recipeId;
/*      */     
/*      */     public TrimTemplate(Item template, ResourceKey<TrimPattern> patternId, ResourceKey<Recipe<?>> recipeId) {
/* 2810 */       this.template = template;
/* 2810 */       this.patternId = patternId;
/* 2810 */       this.recipeId = recipeId;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2810	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2810	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2810	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public Item template() {
/* 2810 */       return this.template;
/*      */     }
/*      */     
/*      */     public ResourceKey<TrimPattern> patternId() {
/* 2810 */       return this.patternId;
/*      */     }
/*      */     
/*      */     public ResourceKey<Recipe<?>> recipeId() {
/* 2810 */       return this.recipeId;
/*      */     }
/*      */   }
/*      */   
/*      */   public static Stream<TrimTemplate> smithingTrims() {
/* 2813 */     return Stream.<Pair>of(new Pair[] { 
/* 2814 */           Pair.of(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.BOLT), 
/* 2815 */           Pair.of(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.COAST), 
/* 2816 */           Pair.of(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.DUNE), 
/* 2817 */           Pair.of(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.EYE), 
/* 2818 */           Pair.of(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.FLOW), 
/* 2819 */           Pair.of(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.HOST), 
/* 2820 */           Pair.of(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.RAISER), 
/* 2821 */           Pair.of(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.RIB), 
/* 2822 */           Pair.of(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.SENTRY), 
/* 2823 */           Pair.of(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.SHAPER), 
/* 2824 */           Pair.of(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.SILENCE), 
/* 2825 */           Pair.of(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.SNOUT), 
/* 2826 */           Pair.of(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.SPIRE), 
/* 2827 */           Pair.of(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.TIDE), 
/* 2828 */           Pair.of(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.VEX), 
/* 2829 */           Pair.of(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.WARD), 
/* 2830 */           Pair.of(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.WAYFINDER), 
/* 2831 */           Pair.of(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.WILD) }).map(itemAndPattern -> {
/*      */           Item item = (Item)itemAndPattern.getFirst();
/*      */           ResourceKey<TrimPattern> patternId = (ResourceKey<TrimPattern>)itemAndPattern.getSecond();
/*      */           ResourceKey<Recipe<?>> recipeId = ResourceKey.create(Registries.RECIPE, Identifier.withDefaultNamespace(getItemName((ItemLike)item) + "_smithing_trim"));
/*      */           return new TrimTemplate(item, patternId, recipeId);
/*      */         });
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\packs\VanillaRecipeProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */