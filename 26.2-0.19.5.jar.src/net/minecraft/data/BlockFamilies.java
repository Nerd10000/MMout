/*     */ package net.minecraft.data;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.WeatheringCopperCollection;
/*     */ 
/*     */ public class BlockFamilies {
/*  14 */   private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();
/*     */   
/*     */   private static final String RECIPE_GROUP_PREFIX_WOODEN = "wooden";
/*     */   
/*     */   private static final String RECIPE_UNLOCKED_BY_HAS_PLANKS = "has_planks";
/*     */   
/*  19 */   public static final BlockFamily ACACIA_PLANKS = familyBuilder(Blocks.ACACIA_PLANKS)
/*  20 */     .log(Blocks.ACACIA_LOG)
/*  21 */     .strippedLog(Blocks.STRIPPED_ACACIA_LOG)
/*  22 */     .button(Blocks.ACACIA_BUTTON)
/*  23 */     .fence(Blocks.ACACIA_FENCE)
/*  24 */     .fenceGate(Blocks.ACACIA_FENCE_GATE)
/*  25 */     .hangingSign(Blocks.ACACIA_HANGING_SIGN, Blocks.ACACIA_WALL_HANGING_SIGN)
/*  26 */     .pressurePlate(Blocks.ACACIA_PRESSURE_PLATE)
/*  27 */     .sign(Blocks.ACACIA_SIGN, Blocks.ACACIA_WALL_SIGN)
/*  28 */     .slab(Blocks.ACACIA_SLAB)
/*  29 */     .stairs(Blocks.ACACIA_STAIRS)
/*  30 */     .door(Blocks.ACACIA_DOOR)
/*  31 */     .trapdoor(Blocks.ACACIA_TRAPDOOR)
/*  32 */     .recipeGroupPrefix("wooden")
/*  33 */     .recipeUnlockedBy("has_planks")
/*  34 */     .getFamily();
/*     */   
/*  36 */   public static final BlockFamily CHERRY_PLANKS = familyBuilder(Blocks.CHERRY_PLANKS)
/*  37 */     .log(Blocks.CHERRY_LOG)
/*  38 */     .strippedLog(Blocks.STRIPPED_CHERRY_LOG)
/*  39 */     .button(Blocks.CHERRY_BUTTON)
/*  40 */     .fence(Blocks.CHERRY_FENCE)
/*  41 */     .fenceGate(Blocks.CHERRY_FENCE_GATE)
/*  42 */     .hangingSign(Blocks.CHERRY_HANGING_SIGN, Blocks.CHERRY_WALL_HANGING_SIGN)
/*  43 */     .pressurePlate(Blocks.CHERRY_PRESSURE_PLATE)
/*  44 */     .sign(Blocks.CHERRY_SIGN, Blocks.CHERRY_WALL_SIGN)
/*  45 */     .slab(Blocks.CHERRY_SLAB)
/*  46 */     .stairs(Blocks.CHERRY_STAIRS)
/*  47 */     .door(Blocks.CHERRY_DOOR)
/*  48 */     .trapdoor(Blocks.CHERRY_TRAPDOOR)
/*  49 */     .recipeGroupPrefix("wooden")
/*  50 */     .recipeUnlockedBy("has_planks")
/*  51 */     .getFamily();
/*     */   
/*  53 */   public static final BlockFamily BIRCH_PLANKS = familyBuilder(Blocks.BIRCH_PLANKS)
/*  54 */     .log(Blocks.BIRCH_LOG)
/*  55 */     .strippedLog(Blocks.STRIPPED_BIRCH_LOG)
/*  56 */     .button(Blocks.BIRCH_BUTTON)
/*  57 */     .fence(Blocks.BIRCH_FENCE)
/*  58 */     .fenceGate(Blocks.BIRCH_FENCE_GATE)
/*  59 */     .hangingSign(Blocks.BIRCH_HANGING_SIGN, Blocks.BIRCH_WALL_HANGING_SIGN)
/*  60 */     .pressurePlate(Blocks.BIRCH_PRESSURE_PLATE)
/*  61 */     .sign(Blocks.BIRCH_SIGN, Blocks.BIRCH_WALL_SIGN)
/*  62 */     .slab(Blocks.BIRCH_SLAB)
/*  63 */     .stairs(Blocks.BIRCH_STAIRS)
/*  64 */     .door(Blocks.BIRCH_DOOR)
/*  65 */     .trapdoor(Blocks.BIRCH_TRAPDOOR)
/*  66 */     .recipeGroupPrefix("wooden")
/*  67 */     .recipeUnlockedBy("has_planks")
/*  68 */     .getFamily();
/*     */   
/*  70 */   public static final BlockFamily CRIMSON_PLANKS = familyBuilder(Blocks.CRIMSON_PLANKS)
/*  71 */     .log(Blocks.CRIMSON_STEM)
/*  72 */     .strippedLog(Blocks.STRIPPED_CRIMSON_STEM)
/*  73 */     .button(Blocks.CRIMSON_BUTTON)
/*  74 */     .fence(Blocks.CRIMSON_FENCE)
/*  75 */     .fenceGate(Blocks.CRIMSON_FENCE_GATE)
/*  76 */     .hangingSign(Blocks.CRIMSON_HANGING_SIGN, Blocks.CRIMSON_WALL_HANGING_SIGN)
/*  77 */     .pressurePlate(Blocks.CRIMSON_PRESSURE_PLATE)
/*  78 */     .sign(Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN)
/*  79 */     .slab(Blocks.CRIMSON_SLAB)
/*  80 */     .stairs(Blocks.CRIMSON_STAIRS)
/*  81 */     .door(Blocks.CRIMSON_DOOR)
/*  82 */     .trapdoor(Blocks.CRIMSON_TRAPDOOR)
/*  83 */     .recipeGroupPrefix("wooden")
/*  84 */     .recipeUnlockedBy("has_planks")
/*  85 */     .getFamily();
/*     */   
/*  87 */   public static final BlockFamily JUNGLE_PLANKS = familyBuilder(Blocks.JUNGLE_PLANKS)
/*  88 */     .log(Blocks.JUNGLE_LOG)
/*  89 */     .strippedLog(Blocks.STRIPPED_JUNGLE_LOG)
/*  90 */     .button(Blocks.JUNGLE_BUTTON)
/*  91 */     .fence(Blocks.JUNGLE_FENCE)
/*  92 */     .fenceGate(Blocks.JUNGLE_FENCE_GATE)
/*  93 */     .hangingSign(Blocks.JUNGLE_HANGING_SIGN, Blocks.JUNGLE_WALL_HANGING_SIGN)
/*  94 */     .pressurePlate(Blocks.JUNGLE_PRESSURE_PLATE)
/*  95 */     .sign(Blocks.JUNGLE_SIGN, Blocks.JUNGLE_WALL_SIGN)
/*  96 */     .slab(Blocks.JUNGLE_SLAB)
/*  97 */     .stairs(Blocks.JUNGLE_STAIRS)
/*  98 */     .door(Blocks.JUNGLE_DOOR)
/*  99 */     .trapdoor(Blocks.JUNGLE_TRAPDOOR)
/* 100 */     .recipeGroupPrefix("wooden")
/* 101 */     .recipeUnlockedBy("has_planks")
/* 102 */     .getFamily();
/*     */   
/* 104 */   public static final BlockFamily OAK_PLANKS = familyBuilder(Blocks.OAK_PLANKS)
/* 105 */     .log(Blocks.OAK_LOG)
/* 106 */     .strippedLog(Blocks.STRIPPED_OAK_LOG)
/* 107 */     .button(Blocks.OAK_BUTTON)
/* 108 */     .fence(Blocks.OAK_FENCE)
/* 109 */     .fenceGate(Blocks.OAK_FENCE_GATE)
/* 110 */     .hangingSign(Blocks.OAK_HANGING_SIGN, Blocks.OAK_WALL_HANGING_SIGN)
/* 111 */     .pressurePlate(Blocks.OAK_PRESSURE_PLATE)
/* 112 */     .sign(Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN)
/* 113 */     .slab(Blocks.OAK_SLAB)
/* 114 */     .stairs(Blocks.OAK_STAIRS)
/* 115 */     .door(Blocks.OAK_DOOR)
/* 116 */     .trapdoor(Blocks.OAK_TRAPDOOR)
/* 117 */     .recipeGroupPrefix("wooden")
/* 118 */     .recipeUnlockedBy("has_planks")
/* 119 */     .getFamily();
/*     */   
/* 121 */   public static final BlockFamily DARK_OAK_PLANKS = familyBuilder(Blocks.DARK_OAK_PLANKS)
/* 122 */     .log(Blocks.DARK_OAK_LOG)
/* 123 */     .strippedLog(Blocks.STRIPPED_DARK_OAK_LOG)
/* 124 */     .button(Blocks.DARK_OAK_BUTTON)
/* 125 */     .fence(Blocks.DARK_OAK_FENCE)
/* 126 */     .fenceGate(Blocks.DARK_OAK_FENCE_GATE)
/* 127 */     .hangingSign(Blocks.DARK_OAK_HANGING_SIGN, Blocks.DARK_OAK_WALL_HANGING_SIGN)
/* 128 */     .pressurePlate(Blocks.DARK_OAK_PRESSURE_PLATE)
/* 129 */     .sign(Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_WALL_SIGN)
/* 130 */     .slab(Blocks.DARK_OAK_SLAB)
/* 131 */     .stairs(Blocks.DARK_OAK_STAIRS)
/* 132 */     .door(Blocks.DARK_OAK_DOOR)
/* 133 */     .trapdoor(Blocks.DARK_OAK_TRAPDOOR)
/* 134 */     .recipeGroupPrefix("wooden")
/* 135 */     .recipeUnlockedBy("has_planks")
/* 136 */     .getFamily();
/*     */   
/* 138 */   public static final BlockFamily PALE_OAK_PLANKS = familyBuilder(Blocks.PALE_OAK_PLANKS)
/* 139 */     .log(Blocks.PALE_OAK_LOG)
/* 140 */     .strippedLog(Blocks.STRIPPED_PALE_OAK_LOG)
/* 141 */     .button(Blocks.PALE_OAK_BUTTON)
/* 142 */     .fence(Blocks.PALE_OAK_FENCE)
/* 143 */     .fenceGate(Blocks.PALE_OAK_FENCE_GATE)
/* 144 */     .hangingSign(Blocks.PALE_OAK_HANGING_SIGN, Blocks.PALE_OAK_WALL_HANGING_SIGN)
/* 145 */     .pressurePlate(Blocks.PALE_OAK_PRESSURE_PLATE)
/* 146 */     .sign(Blocks.PALE_OAK_SIGN, Blocks.PALE_OAK_WALL_SIGN)
/* 147 */     .slab(Blocks.PALE_OAK_SLAB)
/* 148 */     .stairs(Blocks.PALE_OAK_STAIRS)
/* 149 */     .door(Blocks.PALE_OAK_DOOR)
/* 150 */     .trapdoor(Blocks.PALE_OAK_TRAPDOOR)
/* 151 */     .recipeGroupPrefix("wooden")
/* 152 */     .recipeUnlockedBy("has_planks")
/* 153 */     .getFamily();
/*     */   
/* 155 */   public static final BlockFamily SPRUCE_PLANKS = familyBuilder(Blocks.SPRUCE_PLANKS)
/* 156 */     .log(Blocks.SPRUCE_LOG)
/* 157 */     .strippedLog(Blocks.STRIPPED_SPRUCE_LOG)
/* 158 */     .button(Blocks.SPRUCE_BUTTON)
/* 159 */     .fence(Blocks.SPRUCE_FENCE)
/* 160 */     .fenceGate(Blocks.SPRUCE_FENCE_GATE)
/* 161 */     .hangingSign(Blocks.SPRUCE_HANGING_SIGN, Blocks.SPRUCE_WALL_HANGING_SIGN)
/* 162 */     .pressurePlate(Blocks.SPRUCE_PRESSURE_PLATE)
/* 163 */     .sign(Blocks.SPRUCE_SIGN, Blocks.SPRUCE_WALL_SIGN)
/* 164 */     .slab(Blocks.SPRUCE_SLAB)
/* 165 */     .stairs(Blocks.SPRUCE_STAIRS)
/* 166 */     .door(Blocks.SPRUCE_DOOR)
/* 167 */     .trapdoor(Blocks.SPRUCE_TRAPDOOR)
/* 168 */     .recipeGroupPrefix("wooden")
/* 169 */     .recipeUnlockedBy("has_planks")
/* 170 */     .getFamily();
/*     */   
/* 172 */   public static final BlockFamily WARPED_PLANKS = familyBuilder(Blocks.WARPED_PLANKS)
/* 173 */     .log(Blocks.WARPED_STEM)
/* 174 */     .strippedLog(Blocks.STRIPPED_WARPED_STEM)
/* 175 */     .button(Blocks.WARPED_BUTTON)
/* 176 */     .fence(Blocks.WARPED_FENCE)
/* 177 */     .fenceGate(Blocks.WARPED_FENCE_GATE)
/* 178 */     .hangingSign(Blocks.WARPED_HANGING_SIGN, Blocks.WARPED_WALL_HANGING_SIGN)
/* 179 */     .pressurePlate(Blocks.WARPED_PRESSURE_PLATE)
/* 180 */     .sign(Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN)
/* 181 */     .slab(Blocks.WARPED_SLAB)
/* 182 */     .stairs(Blocks.WARPED_STAIRS)
/* 183 */     .door(Blocks.WARPED_DOOR)
/* 184 */     .trapdoor(Blocks.WARPED_TRAPDOOR)
/* 185 */     .recipeGroupPrefix("wooden")
/* 186 */     .recipeUnlockedBy("has_planks")
/* 187 */     .getFamily();
/*     */   
/* 189 */   public static final BlockFamily MANGROVE_PLANKS = familyBuilder(Blocks.MANGROVE_PLANKS)
/* 190 */     .log(Blocks.MANGROVE_LOG)
/* 191 */     .strippedLog(Blocks.STRIPPED_MANGROVE_LOG)
/* 192 */     .button(Blocks.MANGROVE_BUTTON)
/* 193 */     .slab(Blocks.MANGROVE_SLAB)
/* 194 */     .stairs(Blocks.MANGROVE_STAIRS)
/* 195 */     .fence(Blocks.MANGROVE_FENCE)
/* 196 */     .fenceGate(Blocks.MANGROVE_FENCE_GATE)
/* 197 */     .hangingSign(Blocks.MANGROVE_HANGING_SIGN, Blocks.MANGROVE_WALL_HANGING_SIGN)
/* 198 */     .pressurePlate(Blocks.MANGROVE_PRESSURE_PLATE)
/* 199 */     .sign(Blocks.MANGROVE_SIGN, Blocks.MANGROVE_WALL_SIGN)
/* 200 */     .door(Blocks.MANGROVE_DOOR)
/* 201 */     .trapdoor(Blocks.MANGROVE_TRAPDOOR)
/* 202 */     .recipeGroupPrefix("wooden")
/* 203 */     .recipeUnlockedBy("has_planks")
/* 204 */     .getFamily();
/*     */   
/* 206 */   public static final BlockFamily BAMBOO_PLANKS = familyBuilder(Blocks.BAMBOO_PLANKS)
/* 207 */     .log(Blocks.BAMBOO_BLOCK)
/* 208 */     .strippedLog(Blocks.STRIPPED_BAMBOO_BLOCK)
/* 209 */     .button(Blocks.BAMBOO_BUTTON)
/* 210 */     .slab(Blocks.BAMBOO_SLAB)
/* 211 */     .stairs(Blocks.BAMBOO_STAIRS)
/* 212 */     .customFence(Blocks.BAMBOO_FENCE)
/* 213 */     .customFenceGate(Blocks.BAMBOO_FENCE_GATE)
/* 214 */     .customHangingSign(Blocks.BAMBOO_HANGING_SIGN, Blocks.BAMBOO_WALL_HANGING_SIGN)
/* 215 */     .pressurePlate(Blocks.BAMBOO_PRESSURE_PLATE)
/* 216 */     .sign(Blocks.BAMBOO_SIGN, Blocks.BAMBOO_WALL_SIGN)
/* 217 */     .door(Blocks.BAMBOO_DOOR)
/* 218 */     .trapdoor(Blocks.BAMBOO_TRAPDOOR)
/* 219 */     .mosaic(Blocks.BAMBOO_MOSAIC)
/* 220 */     .recipeGroupPrefix("wooden")
/* 221 */     .recipeUnlockedBy("has_planks")
/* 222 */     .getFamily();
/*     */   
/* 224 */   public static final BlockFamily BAMBOO_MOSAIC = familyBuilder(Blocks.BAMBOO_MOSAIC)
/* 225 */     .slab(Blocks.BAMBOO_MOSAIC_SLAB)
/* 226 */     .stairs(Blocks.BAMBOO_MOSAIC_STAIRS)
/* 227 */     .getFamily();
/*     */   
/* 229 */   public static final BlockFamily MUD_BRICKS = familyBuilder(Blocks.MUD_BRICKS)
/* 230 */     .wall(Blocks.MUD_BRICK_WALL)
/* 231 */     .stairs(Blocks.MUD_BRICK_STAIRS)
/* 232 */     .slab(Blocks.MUD_BRICK_SLAB)
/* 233 */     .generateStonecutterRecipe()
/* 234 */     .getFamily();
/*     */   
/* 236 */   public static final BlockFamily ANDESITE = familyBuilder(Blocks.ANDESITE)
/* 237 */     .wall(Blocks.ANDESITE_WALL)
/* 238 */     .stairs(Blocks.ANDESITE_STAIRS)
/* 239 */     .slab(Blocks.ANDESITE_SLAB)
/* 240 */     .polished(Blocks.POLISHED_ANDESITE)
/* 241 */     .generateStonecutterRecipe()
/* 242 */     .getFamily();
/*     */   
/* 244 */   public static final BlockFamily POLISHED_ANDESITE = familyBuilder(Blocks.POLISHED_ANDESITE)
/* 245 */     .stairs(Blocks.POLISHED_ANDESITE_STAIRS)
/* 246 */     .slab(Blocks.POLISHED_ANDESITE_SLAB)
/* 247 */     .generateStonecutterRecipe()
/* 248 */     .getFamily();
/*     */   
/* 250 */   public static final BlockFamily BLACKSTONE = familyBuilder(Blocks.BLACKSTONE)
/* 251 */     .wall(Blocks.BLACKSTONE_WALL)
/* 252 */     .stairs(Blocks.BLACKSTONE_STAIRS)
/* 253 */     .slab(Blocks.BLACKSTONE_SLAB)
/* 254 */     .polished(Blocks.POLISHED_BLACKSTONE)
/* 255 */     .generateStonecutterRecipe()
/* 256 */     .getFamily();
/*     */   
/* 258 */   public static final BlockFamily POLISHED_BLACKSTONE = familyBuilder(Blocks.POLISHED_BLACKSTONE)
/* 259 */     .wall(Blocks.POLISHED_BLACKSTONE_WALL)
/* 260 */     .pressurePlate(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE)
/* 261 */     .button(Blocks.POLISHED_BLACKSTONE_BUTTON)
/* 262 */     .stairs(Blocks.POLISHED_BLACKSTONE_STAIRS)
/* 263 */     .slab(Blocks.POLISHED_BLACKSTONE_SLAB)
/* 264 */     .bricks(Blocks.POLISHED_BLACKSTONE_BRICKS)
/* 265 */     .chiseled(Blocks.CHISELED_POLISHED_BLACKSTONE)
/* 266 */     .generateStonecutterRecipe()
/* 267 */     .getFamily();
/*     */   
/* 269 */   public static final BlockFamily POLISHED_BLACKSTONE_BRICKS = familyBuilder(Blocks.POLISHED_BLACKSTONE_BRICKS)
/* 270 */     .wall(Blocks.POLISHED_BLACKSTONE_BRICK_WALL)
/* 271 */     .stairs(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS)
/* 272 */     .slab(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB)
/* 273 */     .cracked(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)
/* 274 */     .generateStonecutterRecipe()
/* 275 */     .getFamily();
/*     */   
/* 277 */   public static final BlockFamily BRICKS = familyBuilder(Blocks.BRICKS)
/* 278 */     .wall(Blocks.BRICK_WALL)
/* 279 */     .stairs(Blocks.BRICK_STAIRS)
/* 280 */     .slab(Blocks.BRICK_SLAB)
/* 281 */     .generateStonecutterRecipe()
/* 282 */     .getFamily();
/*     */   
/* 284 */   public static final BlockFamily END_STONE = familyBuilder(Blocks.END_STONE)
/* 285 */     .bricks(Blocks.END_STONE_BRICKS)
/* 286 */     .generateStonecutterRecipe()
/* 287 */     .getFamily();
/*     */   
/* 289 */   public static final BlockFamily END_STONE_BRICKS = familyBuilder(Blocks.END_STONE_BRICKS)
/* 290 */     .wall(Blocks.END_STONE_BRICK_WALL)
/* 291 */     .stairs(Blocks.END_STONE_BRICK_STAIRS)
/* 292 */     .slab(Blocks.END_STONE_BRICK_SLAB)
/* 293 */     .generateStonecutterRecipe()
/* 294 */     .getFamily();
/*     */   
/* 296 */   public static final BlockFamily MOSSY_STONE_BRICKS = familyBuilder(Blocks.MOSSY_STONE_BRICKS)
/* 297 */     .wall(Blocks.MOSSY_STONE_BRICK_WALL)
/* 298 */     .stairs(Blocks.MOSSY_STONE_BRICK_STAIRS)
/* 299 */     .slab(Blocks.MOSSY_STONE_BRICK_SLAB)
/* 300 */     .generateStonecutterRecipe()
/* 301 */     .getFamily();
/*     */   
/*     */   public static final WeatheringCopperCollection<BlockFamily> CUT_COPPER;
/*     */   
/*     */   public static final WeatheringCopperCollection<BlockFamily> COPPER_BLOCK;
/*     */   
/*     */   static {
/* 303 */     CUT_COPPER = WeatheringCopperCollection.createFamily((prefix, state) -> familyBuilder((Block)Blocks.CUT_COPPER.waxed().pick(state)).slab((Block)Blocks.CUT_COPPER_SLAB.waxed().pick(state)).stairs((Block)Blocks.CUT_COPPER_STAIRS.waxed().pick(state)).recipeGroupPrefix(prefix + "cut_copper").dontGenerateModel().generateStonecutterRecipe().getFamily(), (paramString, state) -> familyBuilder((Block)Blocks.CUT_COPPER.weathering().pick(state)).slab((Block)Blocks.CUT_COPPER_SLAB.weathering().pick(state)).stairs((Block)Blocks.CUT_COPPER_STAIRS.weathering().pick(state)).chiseled((Block)Blocks.CHISELED_COPPER.weathering().pick(state)).dontGenerateModel().generateStonecutterRecipe().getFamily());
/* 320 */     COPPER_BLOCK = WeatheringCopperCollection.createFamily((prefix, state) -> familyBuilder((Block)Blocks.COPPER_BLOCK.waxed().pick(state)).cut((Block)Blocks.CUT_COPPER.waxed().pick(state)).recipeGroupPrefix(prefix + "cut_copper").dontGenerateModel().getFamily(), (paramString, state) -> familyBuilder((Block)Blocks.COPPER_BLOCK.weathering().pick(state)).cut((Block)Blocks.CUT_COPPER.weathering().pick(state)).dontGenerateModel().getFamily());
/*     */   }
/*     */   
/* 332 */   public static final BlockFamily COBBLESTONE = familyBuilder(Blocks.COBBLESTONE)
/* 333 */     .wall(Blocks.COBBLESTONE_WALL)
/* 334 */     .stairs(Blocks.COBBLESTONE_STAIRS)
/* 335 */     .slab(Blocks.COBBLESTONE_SLAB)
/* 336 */     .generateStonecutterRecipe()
/* 337 */     .getFamily();
/*     */   
/* 339 */   public static final BlockFamily MOSSY_COBBLESTONE = familyBuilder(Blocks.MOSSY_COBBLESTONE)
/* 340 */     .wall(Blocks.MOSSY_COBBLESTONE_WALL)
/* 341 */     .stairs(Blocks.MOSSY_COBBLESTONE_STAIRS)
/* 342 */     .slab(Blocks.MOSSY_COBBLESTONE_SLAB)
/* 343 */     .generateStonecutterRecipe()
/* 344 */     .getFamily();
/*     */   
/* 346 */   public static final BlockFamily DIORITE = familyBuilder(Blocks.DIORITE)
/* 347 */     .wall(Blocks.DIORITE_WALL)
/* 348 */     .stairs(Blocks.DIORITE_STAIRS)
/* 349 */     .slab(Blocks.DIORITE_SLAB)
/* 350 */     .polished(Blocks.POLISHED_DIORITE)
/* 351 */     .generateStonecutterRecipe()
/* 352 */     .getFamily();
/*     */   
/* 354 */   public static final BlockFamily POLISHED_DIORITE = familyBuilder(Blocks.POLISHED_DIORITE)
/* 355 */     .stairs(Blocks.POLISHED_DIORITE_STAIRS)
/* 356 */     .slab(Blocks.POLISHED_DIORITE_SLAB)
/* 357 */     .generateStonecutterRecipe()
/* 358 */     .getFamily();
/*     */   
/* 360 */   public static final BlockFamily GRANITE = familyBuilder(Blocks.GRANITE)
/* 361 */     .wall(Blocks.GRANITE_WALL)
/* 362 */     .stairs(Blocks.GRANITE_STAIRS)
/* 363 */     .slab(Blocks.GRANITE_SLAB)
/* 364 */     .polished(Blocks.POLISHED_GRANITE)
/* 365 */     .generateStonecutterRecipe()
/* 366 */     .getFamily();
/*     */   
/* 368 */   public static final BlockFamily SULFUR = familyBuilder(Blocks.SULFUR)
/* 369 */     .wall(Blocks.SULFUR_WALL)
/* 370 */     .stairs(Blocks.SULFUR_STAIRS)
/* 371 */     .slab(Blocks.SULFUR_SLAB)
/* 372 */     .chiseled(Blocks.CHISELED_SULFUR)
/* 373 */     .polished(Blocks.POLISHED_SULFUR)
/* 374 */     .generateStonecutterRecipe()
/* 375 */     .getFamily();
/*     */   
/* 377 */   public static final BlockFamily POLISHED_SULFUR = familyBuilder(Blocks.POLISHED_SULFUR)
/* 378 */     .wall(Blocks.POLISHED_SULFUR_WALL)
/* 379 */     .stairs(Blocks.POLISHED_SULFUR_STAIRS)
/* 380 */     .slab(Blocks.POLISHED_SULFUR_SLAB)
/* 381 */     .bricks(Blocks.SULFUR_BRICKS)
/* 382 */     .generateStonecutterRecipe()
/* 383 */     .getFamily();
/*     */   
/* 385 */   public static final BlockFamily SULFUR_BRICKS = familyBuilder(Blocks.SULFUR_BRICKS)
/* 386 */     .wall(Blocks.SULFUR_BRICK_WALL)
/* 387 */     .stairs(Blocks.SULFUR_BRICK_STAIRS)
/* 388 */     .slab(Blocks.SULFUR_BRICK_SLAB)
/* 389 */     .generateStonecutterRecipe()
/* 390 */     .getFamily();
/*     */   
/* 392 */   public static final BlockFamily CINNABAR = familyBuilder(Blocks.CINNABAR)
/* 393 */     .wall(Blocks.CINNABAR_WALL)
/* 394 */     .stairs(Blocks.CINNABAR_STAIRS)
/* 395 */     .slab(Blocks.CINNABAR_SLAB)
/* 396 */     .chiseled(Blocks.CHISELED_CINNABAR)
/* 397 */     .polished(Blocks.POLISHED_CINNABAR)
/* 398 */     .generateStonecutterRecipe()
/* 399 */     .getFamily();
/*     */   
/* 401 */   public static final BlockFamily POLISHED_CINNABAR = familyBuilder(Blocks.POLISHED_CINNABAR)
/* 402 */     .wall(Blocks.POLISHED_CINNABAR_WALL)
/* 403 */     .stairs(Blocks.POLISHED_CINNABAR_STAIRS)
/* 404 */     .slab(Blocks.POLISHED_CINNABAR_SLAB)
/* 405 */     .bricks(Blocks.CINNABAR_BRICKS)
/* 406 */     .generateStonecutterRecipe()
/* 407 */     .getFamily();
/*     */   
/* 409 */   public static final BlockFamily CINNABAR_BRICKS = familyBuilder(Blocks.CINNABAR_BRICKS)
/* 410 */     .wall(Blocks.CINNABAR_BRICK_WALL)
/* 411 */     .stairs(Blocks.CINNABAR_BRICK_STAIRS)
/* 412 */     .slab(Blocks.CINNABAR_BRICK_SLAB)
/* 413 */     .generateStonecutterRecipe()
/* 414 */     .getFamily();
/*     */   
/* 416 */   public static final BlockFamily POLISHED_GRANITE = familyBuilder(Blocks.POLISHED_GRANITE)
/* 417 */     .stairs(Blocks.POLISHED_GRANITE_STAIRS)
/* 418 */     .slab(Blocks.POLISHED_GRANITE_SLAB)
/* 419 */     .generateStonecutterRecipe()
/* 420 */     .getFamily();
/*     */   
/* 422 */   public static final BlockFamily TUFF = familyBuilder(Blocks.TUFF)
/* 423 */     .wall(Blocks.TUFF_WALL)
/* 424 */     .stairs(Blocks.TUFF_STAIRS)
/* 425 */     .slab(Blocks.TUFF_SLAB)
/* 426 */     .chiseled(Blocks.CHISELED_TUFF)
/* 427 */     .polished(Blocks.POLISHED_TUFF)
/* 428 */     .generateStonecutterRecipe()
/* 429 */     .getFamily();
/*     */   
/* 431 */   public static final BlockFamily POLISHED_TUFF = familyBuilder(Blocks.POLISHED_TUFF)
/* 432 */     .wall(Blocks.POLISHED_TUFF_WALL)
/* 433 */     .stairs(Blocks.POLISHED_TUFF_STAIRS)
/* 434 */     .slab(Blocks.POLISHED_TUFF_SLAB)
/* 435 */     .bricks(Blocks.TUFF_BRICKS)
/* 436 */     .generateStonecutterRecipe()
/* 437 */     .getFamily();
/*     */   
/* 439 */   public static final BlockFamily TUFF_BRICKS = familyBuilder(Blocks.TUFF_BRICKS)
/* 440 */     .wall(Blocks.TUFF_BRICK_WALL)
/* 441 */     .stairs(Blocks.TUFF_BRICK_STAIRS)
/* 442 */     .slab(Blocks.TUFF_BRICK_SLAB)
/* 443 */     .chiseled(Blocks.CHISELED_TUFF_BRICKS)
/* 444 */     .generateStonecutterRecipe()
/* 445 */     .getFamily();
/*     */   
/* 447 */   public static final BlockFamily RESIN_BRICKS = familyBuilder(Blocks.RESIN_BRICKS)
/* 448 */     .wall(Blocks.RESIN_BRICK_WALL)
/* 449 */     .stairs(Blocks.RESIN_BRICK_STAIRS)
/* 450 */     .slab(Blocks.RESIN_BRICK_SLAB)
/* 451 */     .chiseled(Blocks.CHISELED_RESIN_BRICKS)
/* 452 */     .generateStonecutterRecipe()
/* 453 */     .getFamily();
/*     */   
/* 455 */   public static final BlockFamily NETHER_BRICKS = familyBuilder(Blocks.NETHER_BRICKS)
/* 456 */     .fence(Blocks.NETHER_BRICK_FENCE)
/* 457 */     .wall(Blocks.NETHER_BRICK_WALL)
/* 458 */     .stairs(Blocks.NETHER_BRICK_STAIRS)
/* 459 */     .slab(Blocks.NETHER_BRICK_SLAB)
/* 460 */     .chiseled(Blocks.CHISELED_NETHER_BRICKS)
/* 461 */     .cracked(Blocks.CRACKED_NETHER_BRICKS)
/* 462 */     .generateStonecutterRecipe()
/* 463 */     .getFamily();
/*     */   
/* 465 */   public static final BlockFamily RED_NETHER_BRICKS = familyBuilder(Blocks.RED_NETHER_BRICKS)
/* 466 */     .slab(Blocks.RED_NETHER_BRICK_SLAB)
/* 467 */     .stairs(Blocks.RED_NETHER_BRICK_STAIRS)
/* 468 */     .wall(Blocks.RED_NETHER_BRICK_WALL)
/* 469 */     .generateStonecutterRecipe()
/* 470 */     .getFamily();
/*     */   
/* 472 */   public static final BlockFamily PRISMARINE = familyBuilder(Blocks.PRISMARINE)
/* 473 */     .wall(Blocks.PRISMARINE_WALL)
/* 474 */     .stairs(Blocks.PRISMARINE_STAIRS)
/* 475 */     .slab(Blocks.PRISMARINE_SLAB)
/* 476 */     .generateStonecutterRecipe()
/* 477 */     .getFamily();
/*     */   
/* 479 */   public static final BlockFamily PRISMARINE_BRICKS = familyBuilder(Blocks.PRISMARINE_BRICKS)
/* 480 */     .stairs(Blocks.PRISMARINE_BRICK_STAIRS)
/* 481 */     .slab(Blocks.PRISMARINE_BRICK_SLAB)
/* 482 */     .generateStonecutterRecipe()
/* 483 */     .getFamily();
/*     */   
/* 485 */   public static final BlockFamily DARK_PRISMARINE = familyBuilder(Blocks.DARK_PRISMARINE)
/* 486 */     .stairs(Blocks.DARK_PRISMARINE_STAIRS)
/* 487 */     .slab(Blocks.DARK_PRISMARINE_SLAB)
/* 488 */     .generateStonecutterRecipe()
/* 489 */     .getFamily();
/*     */   
/* 491 */   public static final BlockFamily PURPUR = familyBuilder(Blocks.PURPUR_BLOCK)
/* 492 */     .stairs(Blocks.PURPUR_STAIRS)
/* 493 */     .slab(Blocks.PURPUR_SLAB)
/* 494 */     .pillar(Blocks.PURPUR_PILLAR)
/* 495 */     .dontGenerateCraftingRecipe()
/* 496 */     .generateStonecutterRecipe()
/* 497 */     .getFamily();
/*     */   
/* 499 */   public static final BlockFamily QUARTZ = familyBuilder(Blocks.QUARTZ_BLOCK)
/* 500 */     .stairs(Blocks.QUARTZ_STAIRS)
/* 501 */     .slab(Blocks.QUARTZ_SLAB)
/* 502 */     .chiseled(Blocks.CHISELED_QUARTZ_BLOCK)
/* 503 */     .bricks(Blocks.QUARTZ_BRICKS)
/* 504 */     .pillar(Blocks.QUARTZ_PILLAR)
/* 505 */     .dontGenerateCraftingRecipe()
/* 506 */     .generateStonecutterRecipe()
/* 507 */     .getFamily();
/*     */   
/* 509 */   public static final BlockFamily SMOOTH_QUARTZ = familyBuilder(Blocks.SMOOTH_QUARTZ)
/* 510 */     .stairs(Blocks.SMOOTH_QUARTZ_STAIRS)
/* 511 */     .slab(Blocks.SMOOTH_QUARTZ_SLAB)
/* 512 */     .generateStonecutterRecipe()
/* 513 */     .getFamily();
/*     */   
/* 515 */   public static final BlockFamily SANDSTONE = familyBuilder(Blocks.SANDSTONE)
/* 516 */     .wall(Blocks.SANDSTONE_WALL)
/* 517 */     .stairs(Blocks.SANDSTONE_STAIRS)
/* 518 */     .slab(Blocks.SANDSTONE_SLAB)
/* 519 */     .chiseled(Blocks.CHISELED_SANDSTONE)
/* 520 */     .cut(Blocks.CUT_SANDSTONE)
/* 521 */     .dontGenerateCraftingRecipe()
/* 522 */     .generateStonecutterRecipe()
/* 523 */     .getFamily();
/*     */   
/* 525 */   public static final BlockFamily CUT_SANDSTONE = familyBuilder(Blocks.CUT_SANDSTONE)
/* 526 */     .slab(Blocks.CUT_SANDSTONE_SLAB)
/* 527 */     .generateStonecutterRecipe()
/* 528 */     .getFamily();
/*     */   
/* 530 */   public static final BlockFamily SMOOTH_SANDSTONE = familyBuilder(Blocks.SMOOTH_SANDSTONE)
/* 531 */     .slab(Blocks.SMOOTH_SANDSTONE_SLAB)
/* 532 */     .stairs(Blocks.SMOOTH_SANDSTONE_STAIRS)
/* 533 */     .generateStonecutterRecipe()
/* 534 */     .getFamily();
/*     */   
/* 536 */   public static final BlockFamily RED_SANDSTONE = familyBuilder(Blocks.RED_SANDSTONE)
/* 537 */     .wall(Blocks.RED_SANDSTONE_WALL)
/* 538 */     .stairs(Blocks.RED_SANDSTONE_STAIRS)
/* 539 */     .slab(Blocks.RED_SANDSTONE_SLAB)
/* 540 */     .chiseled(Blocks.CHISELED_RED_SANDSTONE)
/* 541 */     .cut(Blocks.CUT_RED_SANDSTONE)
/* 542 */     .dontGenerateCraftingRecipe()
/* 543 */     .generateStonecutterRecipe()
/* 544 */     .getFamily();
/*     */   
/* 546 */   public static final BlockFamily CUT_RED_SANDSTONE = familyBuilder(Blocks.CUT_RED_SANDSTONE)
/* 547 */     .slab(Blocks.CUT_RED_SANDSTONE_SLAB)
/* 548 */     .generateStonecutterRecipe()
/* 549 */     .getFamily();
/*     */   
/* 551 */   public static final BlockFamily SMOOTH_RED_SANDSTONE = familyBuilder(Blocks.SMOOTH_RED_SANDSTONE)
/* 552 */     .slab(Blocks.SMOOTH_RED_SANDSTONE_SLAB)
/* 553 */     .stairs(Blocks.SMOOTH_RED_SANDSTONE_STAIRS)
/* 554 */     .generateStonecutterRecipe()
/* 555 */     .getFamily();
/*     */   
/* 557 */   public static final BlockFamily STONE = familyBuilder(Blocks.STONE)
/* 558 */     .slab(Blocks.STONE_SLAB)
/* 559 */     .pressurePlate(Blocks.STONE_PRESSURE_PLATE)
/* 560 */     .button(Blocks.STONE_BUTTON)
/* 561 */     .stairs(Blocks.STONE_STAIRS)
/* 562 */     .bricks(Blocks.STONE_BRICKS)
/* 563 */     .cobbled(Blocks.COBBLESTONE)
/* 564 */     .generateStonecutterRecipe()
/* 565 */     .getFamily();
/*     */   
/* 567 */   public static final BlockFamily STONE_BRICK = familyBuilder(Blocks.STONE_BRICKS)
/* 568 */     .wall(Blocks.STONE_BRICK_WALL)
/* 569 */     .stairs(Blocks.STONE_BRICK_STAIRS)
/* 570 */     .slab(Blocks.STONE_BRICK_SLAB)
/* 571 */     .chiseled(Blocks.CHISELED_STONE_BRICKS)
/* 572 */     .cracked(Blocks.CRACKED_STONE_BRICKS)
/* 573 */     .dontGenerateCraftingRecipe()
/* 574 */     .generateStonecutterRecipe()
/* 575 */     .getFamily();
/*     */   
/* 577 */   public static final BlockFamily DEEPSLATE = familyBuilder(Blocks.DEEPSLATE)
/* 578 */     .cobbled(Blocks.COBBLED_DEEPSLATE)
/* 579 */     .generateStonecutterRecipe()
/* 580 */     .getFamily();
/*     */   
/* 582 */   public static final BlockFamily COBBLED_DEEPSLATE = familyBuilder(Blocks.COBBLED_DEEPSLATE)
/* 583 */     .slab(Blocks.COBBLED_DEEPSLATE_SLAB)
/* 584 */     .stairs(Blocks.COBBLED_DEEPSLATE_STAIRS)
/* 585 */     .wall(Blocks.COBBLED_DEEPSLATE_WALL)
/* 586 */     .chiseled(Blocks.CHISELED_DEEPSLATE)
/* 587 */     .polished(Blocks.POLISHED_DEEPSLATE)
/* 588 */     .generateStonecutterRecipe()
/* 589 */     .getFamily();
/*     */   
/* 591 */   public static final BlockFamily POLISHED_DEEPSLATE = familyBuilder(Blocks.POLISHED_DEEPSLATE)
/* 592 */     .slab(Blocks.POLISHED_DEEPSLATE_SLAB)
/* 593 */     .stairs(Blocks.POLISHED_DEEPSLATE_STAIRS)
/* 594 */     .wall(Blocks.POLISHED_DEEPSLATE_WALL)
/* 595 */     .bricks(Blocks.DEEPSLATE_BRICKS)
/* 596 */     .generateStonecutterRecipe()
/* 597 */     .getFamily();
/*     */   
/* 599 */   public static final BlockFamily DEEPSLATE_BRICKS = familyBuilder(Blocks.DEEPSLATE_BRICKS)
/* 600 */     .slab(Blocks.DEEPSLATE_BRICK_SLAB)
/* 601 */     .stairs(Blocks.DEEPSLATE_BRICK_STAIRS)
/* 602 */     .wall(Blocks.DEEPSLATE_BRICK_WALL)
/* 603 */     .cracked(Blocks.CRACKED_DEEPSLATE_BRICKS)
/* 604 */     .tiles(Blocks.DEEPSLATE_TILES)
/* 605 */     .generateStonecutterRecipe()
/* 606 */     .getFamily();
/*     */   
/* 608 */   public static final BlockFamily DEEPSLATE_TILES = familyBuilder(Blocks.DEEPSLATE_TILES)
/* 609 */     .slab(Blocks.DEEPSLATE_TILE_SLAB)
/* 610 */     .stairs(Blocks.DEEPSLATE_TILE_STAIRS)
/* 611 */     .wall(Blocks.DEEPSLATE_TILE_WALL)
/* 612 */     .cracked(Blocks.CRACKED_DEEPSLATE_TILES)
/* 613 */     .generateStonecutterRecipe()
/* 614 */     .getFamily();
/*     */   
/*     */   private static BlockFamily.Builder familyBuilder(Block base) {
/* 617 */     BlockFamily.Builder builder = new BlockFamily.Builder(base);
/* 618 */     BlockFamily blockFamily = MAP.put(base, builder.getFamily());
/* 619 */     if (blockFamily != null)
/* 620 */       throw new IllegalStateException("Duplicate family definition for " + String.valueOf(BuiltInRegistries.BLOCK.getKey(base))); 
/* 622 */     return builder;
/*     */   }
/*     */   
/*     */   public static Stream<BlockFamily> getAllFamilies() {
/* 626 */     return MAP.values().stream();
/*     */   }
/*     */   
/*     */   public static BlockFamily getFamily(Block base) {
/* 630 */     return MAP.get(base);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\BlockFamilies.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */