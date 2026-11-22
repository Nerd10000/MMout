/*     */ package net.minecraft.references;
/*     */ 
/*     */ import net.minecraft.world.level.block.ColorCollection;
/*     */ import net.minecraft.world.level.block.WeatheringCopperCollection;
/*     */ 
/*     */ public class BlockItemIds {
/*   7 */   public static final BlockItemId AIR = BlockItemId.create("air");
/*     */   
/*   8 */   public static final BlockItemId DIRT = BlockItemId.create("dirt");
/*     */   
/*   9 */   public static final BlockItemId PUMPKIN = BlockItemId.create("pumpkin");
/*     */   
/*  10 */   public static final BlockItemId MELON = BlockItemId.create("melon");
/*     */   
/*  11 */   public static final BlockItemId STONE = BlockItemId.create("stone");
/*     */   
/*  12 */   public static final BlockItemId GRANITE = BlockItemId.create("granite");
/*     */   
/*  13 */   public static final BlockItemId POLISHED_GRANITE = BlockItemId.create("polished_granite");
/*     */   
/*  14 */   public static final BlockItemId DIORITE = BlockItemId.create("diorite");
/*     */   
/*  15 */   public static final BlockItemId POLISHED_DIORITE = BlockItemId.create("polished_diorite");
/*     */   
/*  16 */   public static final BlockItemId ANDESITE = BlockItemId.create("andesite");
/*     */   
/*  17 */   public static final BlockItemId POLISHED_ANDESITE = BlockItemId.create("polished_andesite");
/*     */   
/*  18 */   public static final BlockItemId DEEPSLATE = BlockItemId.create("deepslate");
/*     */   
/*  19 */   public static final BlockItemId COBBLED_DEEPSLATE = BlockItemId.create("cobbled_deepslate");
/*     */   
/*  20 */   public static final BlockItemId POLISHED_DEEPSLATE = BlockItemId.create("polished_deepslate");
/*     */   
/*  21 */   public static final BlockItemId CALCITE = BlockItemId.create("calcite");
/*     */   
/*  22 */   public static final BlockItemId TUFF = BlockItemId.create("tuff");
/*     */   
/*  23 */   public static final BlockItemId TUFF_SLAB = BlockItemId.create("tuff_slab");
/*     */   
/*  24 */   public static final BlockItemId TUFF_STAIRS = BlockItemId.create("tuff_stairs");
/*     */   
/*  25 */   public static final BlockItemId TUFF_WALL = BlockItemId.create("tuff_wall");
/*     */   
/*  26 */   public static final BlockItemId CHISELED_TUFF = BlockItemId.create("chiseled_tuff");
/*     */   
/*  27 */   public static final BlockItemId POLISHED_TUFF = BlockItemId.create("polished_tuff");
/*     */   
/*  28 */   public static final BlockItemId POLISHED_TUFF_SLAB = BlockItemId.create("polished_tuff_slab");
/*     */   
/*  29 */   public static final BlockItemId POLISHED_TUFF_STAIRS = BlockItemId.create("polished_tuff_stairs");
/*     */   
/*  30 */   public static final BlockItemId POLISHED_TUFF_WALL = BlockItemId.create("polished_tuff_wall");
/*     */   
/*  31 */   public static final BlockItemId TUFF_BRICKS = BlockItemId.create("tuff_bricks");
/*     */   
/*  32 */   public static final BlockItemId TUFF_BRICK_SLAB = BlockItemId.create("tuff_brick_slab");
/*     */   
/*  33 */   public static final BlockItemId TUFF_BRICK_STAIRS = BlockItemId.create("tuff_brick_stairs");
/*     */   
/*  34 */   public static final BlockItemId TUFF_BRICK_WALL = BlockItemId.create("tuff_brick_wall");
/*     */   
/*  35 */   public static final BlockItemId CHISELED_TUFF_BRICKS = BlockItemId.create("chiseled_tuff_bricks");
/*     */   
/*  36 */   public static final BlockItemId SULFUR = BlockItemId.create("sulfur");
/*     */   
/*  37 */   public static final BlockItemId POTENT_SULFUR = BlockItemId.create("potent_sulfur");
/*     */   
/*  38 */   public static final BlockItemId SULFUR_SLAB = BlockItemId.create("sulfur_slab");
/*     */   
/*  39 */   public static final BlockItemId SULFUR_STAIRS = BlockItemId.create("sulfur_stairs");
/*     */   
/*  40 */   public static final BlockItemId SULFUR_WALL = BlockItemId.create("sulfur_wall");
/*     */   
/*  41 */   public static final BlockItemId POLISHED_SULFUR = BlockItemId.create("polished_sulfur");
/*     */   
/*  42 */   public static final BlockItemId POLISHED_SULFUR_SLAB = BlockItemId.create("polished_sulfur_slab");
/*     */   
/*  43 */   public static final BlockItemId POLISHED_SULFUR_STAIRS = BlockItemId.create("polished_sulfur_stairs");
/*     */   
/*  44 */   public static final BlockItemId POLISHED_SULFUR_WALL = BlockItemId.create("polished_sulfur_wall");
/*     */   
/*  45 */   public static final BlockItemId SULFUR_BRICKS = BlockItemId.create("sulfur_bricks");
/*     */   
/*  46 */   public static final BlockItemId SULFUR_BRICK_SLAB = BlockItemId.create("sulfur_brick_slab");
/*     */   
/*  47 */   public static final BlockItemId SULFUR_BRICK_STAIRS = BlockItemId.create("sulfur_brick_stairs");
/*     */   
/*  48 */   public static final BlockItemId SULFUR_BRICK_WALL = BlockItemId.create("sulfur_brick_wall");
/*     */   
/*  49 */   public static final BlockItemId CHISELED_SULFUR = BlockItemId.create("chiseled_sulfur");
/*     */   
/*  50 */   public static final BlockItemId CINNABAR = BlockItemId.create("cinnabar");
/*     */   
/*  51 */   public static final BlockItemId CINNABAR_SLAB = BlockItemId.create("cinnabar_slab");
/*     */   
/*  52 */   public static final BlockItemId CINNABAR_STAIRS = BlockItemId.create("cinnabar_stairs");
/*     */   
/*  53 */   public static final BlockItemId CINNABAR_WALL = BlockItemId.create("cinnabar_wall");
/*     */   
/*  54 */   public static final BlockItemId POLISHED_CINNABAR = BlockItemId.create("polished_cinnabar");
/*     */   
/*  55 */   public static final BlockItemId POLISHED_CINNABAR_SLAB = BlockItemId.create("polished_cinnabar_slab");
/*     */   
/*  56 */   public static final BlockItemId POLISHED_CINNABAR_STAIRS = BlockItemId.create("polished_cinnabar_stairs");
/*     */   
/*  57 */   public static final BlockItemId POLISHED_CINNABAR_WALL = BlockItemId.create("polished_cinnabar_wall");
/*     */   
/*  58 */   public static final BlockItemId CINNABAR_BRICKS = BlockItemId.create("cinnabar_bricks");
/*     */   
/*  59 */   public static final BlockItemId CINNABAR_BRICK_SLAB = BlockItemId.create("cinnabar_brick_slab");
/*     */   
/*  60 */   public static final BlockItemId CINNABAR_BRICK_STAIRS = BlockItemId.create("cinnabar_brick_stairs");
/*     */   
/*  61 */   public static final BlockItemId CINNABAR_BRICK_WALL = BlockItemId.create("cinnabar_brick_wall");
/*     */   
/*  62 */   public static final BlockItemId CHISELED_CINNABAR = BlockItemId.create("chiseled_cinnabar");
/*     */   
/*  63 */   public static final BlockItemId DRIPSTONE_BLOCK = BlockItemId.create("dripstone_block");
/*     */   
/*  64 */   public static final BlockItemId GRASS_BLOCK = BlockItemId.create("grass_block");
/*     */   
/*  65 */   public static final BlockItemId COARSE_DIRT = BlockItemId.create("coarse_dirt");
/*     */   
/*  66 */   public static final BlockItemId PODZOL = BlockItemId.create("podzol");
/*     */   
/*  67 */   public static final BlockItemId ROOTED_DIRT = BlockItemId.create("rooted_dirt");
/*     */   
/*  68 */   public static final BlockItemId MUD = BlockItemId.create("mud");
/*     */   
/*  69 */   public static final BlockItemId CRIMSON_NYLIUM = BlockItemId.create("crimson_nylium");
/*     */   
/*  70 */   public static final BlockItemId WARPED_NYLIUM = BlockItemId.create("warped_nylium");
/*     */   
/*  71 */   public static final BlockItemId COBBLESTONE = BlockItemId.create("cobblestone");
/*     */   
/*  72 */   public static final BlockItemId OAK_PLANKS = BlockItemId.create("oak_planks");
/*     */   
/*  73 */   public static final BlockItemId SPRUCE_PLANKS = BlockItemId.create("spruce_planks");
/*     */   
/*  74 */   public static final BlockItemId BIRCH_PLANKS = BlockItemId.create("birch_planks");
/*     */   
/*  75 */   public static final BlockItemId JUNGLE_PLANKS = BlockItemId.create("jungle_planks");
/*     */   
/*  76 */   public static final BlockItemId ACACIA_PLANKS = BlockItemId.create("acacia_planks");
/*     */   
/*  77 */   public static final BlockItemId CHERRY_PLANKS = BlockItemId.create("cherry_planks");
/*     */   
/*  78 */   public static final BlockItemId DARK_OAK_PLANKS = BlockItemId.create("dark_oak_planks");
/*     */   
/*  79 */   public static final BlockItemId PALE_OAK_PLANKS = BlockItemId.create("pale_oak_planks");
/*     */   
/*  80 */   public static final BlockItemId MANGROVE_PLANKS = BlockItemId.create("mangrove_planks");
/*     */   
/*  81 */   public static final BlockItemId BAMBOO_PLANKS = BlockItemId.create("bamboo_planks");
/*     */   
/*  82 */   public static final BlockItemId CRIMSON_PLANKS = BlockItemId.create("crimson_planks");
/*     */   
/*  83 */   public static final BlockItemId WARPED_PLANKS = BlockItemId.create("warped_planks");
/*     */   
/*  84 */   public static final BlockItemId BAMBOO_MOSAIC = BlockItemId.create("bamboo_mosaic");
/*     */   
/*  85 */   public static final BlockItemId OAK_SAPLING = BlockItemId.create("oak_sapling");
/*     */   
/*  86 */   public static final BlockItemId SPRUCE_SAPLING = BlockItemId.create("spruce_sapling");
/*     */   
/*  87 */   public static final BlockItemId BIRCH_SAPLING = BlockItemId.create("birch_sapling");
/*     */   
/*  88 */   public static final BlockItemId JUNGLE_SAPLING = BlockItemId.create("jungle_sapling");
/*     */   
/*  89 */   public static final BlockItemId ACACIA_SAPLING = BlockItemId.create("acacia_sapling");
/*     */   
/*  90 */   public static final BlockItemId CHERRY_SAPLING = BlockItemId.create("cherry_sapling");
/*     */   
/*  91 */   public static final BlockItemId DARK_OAK_SAPLING = BlockItemId.create("dark_oak_sapling");
/*     */   
/*  92 */   public static final BlockItemId PALE_OAK_SAPLING = BlockItemId.create("pale_oak_sapling");
/*     */   
/*  93 */   public static final BlockItemId MANGROVE_PROPAGULE = BlockItemId.create("mangrove_propagule");
/*     */   
/*  94 */   public static final BlockItemId BEDROCK = BlockItemId.create("bedrock");
/*     */   
/*  95 */   public static final BlockItemId SAND = BlockItemId.create("sand");
/*     */   
/*  96 */   public static final BlockItemId SUSPICIOUS_SAND = BlockItemId.create("suspicious_sand");
/*     */   
/*  97 */   public static final BlockItemId SUSPICIOUS_GRAVEL = BlockItemId.create("suspicious_gravel");
/*     */   
/*  98 */   public static final BlockItemId RED_SAND = BlockItemId.create("red_sand");
/*     */   
/*  99 */   public static final BlockItemId GRAVEL = BlockItemId.create("gravel");
/*     */   
/* 100 */   public static final BlockItemId COAL_ORE = BlockItemId.create("coal_ore");
/*     */   
/* 101 */   public static final BlockItemId DEEPSLATE_COAL_ORE = BlockItemId.create("deepslate_coal_ore");
/*     */   
/* 102 */   public static final BlockItemId IRON_ORE = BlockItemId.create("iron_ore");
/*     */   
/* 103 */   public static final BlockItemId DEEPSLATE_IRON_ORE = BlockItemId.create("deepslate_iron_ore");
/*     */   
/* 104 */   public static final BlockItemId COPPER_ORE = BlockItemId.create("copper_ore");
/*     */   
/* 105 */   public static final BlockItemId DEEPSLATE_COPPER_ORE = BlockItemId.create("deepslate_copper_ore");
/*     */   
/* 106 */   public static final BlockItemId GOLD_ORE = BlockItemId.create("gold_ore");
/*     */   
/* 107 */   public static final BlockItemId DEEPSLATE_GOLD_ORE = BlockItemId.create("deepslate_gold_ore");
/*     */   
/* 108 */   public static final BlockItemId REDSTONE_ORE = BlockItemId.create("redstone_ore");
/*     */   
/* 109 */   public static final BlockItemId DEEPSLATE_REDSTONE_ORE = BlockItemId.create("deepslate_redstone_ore");
/*     */   
/* 110 */   public static final BlockItemId EMERALD_ORE = BlockItemId.create("emerald_ore");
/*     */   
/* 111 */   public static final BlockItemId DEEPSLATE_EMERALD_ORE = BlockItemId.create("deepslate_emerald_ore");
/*     */   
/* 112 */   public static final BlockItemId LAPIS_ORE = BlockItemId.create("lapis_ore");
/*     */   
/* 113 */   public static final BlockItemId DEEPSLATE_LAPIS_ORE = BlockItemId.create("deepslate_lapis_ore");
/*     */   
/* 114 */   public static final BlockItemId DIAMOND_ORE = BlockItemId.create("diamond_ore");
/*     */   
/* 115 */   public static final BlockItemId DEEPSLATE_DIAMOND_ORE = BlockItemId.create("deepslate_diamond_ore");
/*     */   
/* 116 */   public static final BlockItemId NETHER_GOLD_ORE = BlockItemId.create("nether_gold_ore");
/*     */   
/* 117 */   public static final BlockItemId NETHER_QUARTZ_ORE = BlockItemId.create("nether_quartz_ore");
/*     */   
/* 118 */   public static final BlockItemId ANCIENT_DEBRIS = BlockItemId.create("ancient_debris");
/*     */   
/* 119 */   public static final BlockItemId COAL_BLOCK = BlockItemId.create("coal_block");
/*     */   
/* 120 */   public static final BlockItemId RAW_IRON_BLOCK = BlockItemId.create("raw_iron_block");
/*     */   
/* 121 */   public static final BlockItemId RAW_COPPER_BLOCK = BlockItemId.create("raw_copper_block");
/*     */   
/* 122 */   public static final BlockItemId RAW_GOLD_BLOCK = BlockItemId.create("raw_gold_block");
/*     */   
/* 123 */   public static final BlockItemId HEAVY_CORE = BlockItemId.create("heavy_core");
/*     */   
/* 124 */   public static final BlockItemId AMETHYST_BLOCK = BlockItemId.create("amethyst_block");
/*     */   
/* 125 */   public static final BlockItemId BUDDING_AMETHYST = BlockItemId.create("budding_amethyst");
/*     */   
/* 126 */   public static final BlockItemId IRON_BLOCK = BlockItemId.create("iron_block");
/*     */   
/* 127 */   public static final BlockItemId GOLD_BLOCK = BlockItemId.create("gold_block");
/*     */   
/* 128 */   public static final BlockItemId DIAMOND_BLOCK = BlockItemId.create("diamond_block");
/*     */   
/* 129 */   public static final BlockItemId NETHERITE_BLOCK = BlockItemId.create("netherite_block");
/*     */   
/* 130 */   public static final BlockItemId OAK_LOG = BlockItemId.create("oak_log");
/*     */   
/* 131 */   public static final BlockItemId SPRUCE_LOG = BlockItemId.create("spruce_log");
/*     */   
/* 132 */   public static final BlockItemId BIRCH_LOG = BlockItemId.create("birch_log");
/*     */   
/* 133 */   public static final BlockItemId JUNGLE_LOG = BlockItemId.create("jungle_log");
/*     */   
/* 134 */   public static final BlockItemId ACACIA_LOG = BlockItemId.create("acacia_log");
/*     */   
/* 135 */   public static final BlockItemId CHERRY_LOG = BlockItemId.create("cherry_log");
/*     */   
/* 136 */   public static final BlockItemId PALE_OAK_LOG = BlockItemId.create("pale_oak_log");
/*     */   
/* 137 */   public static final BlockItemId DARK_OAK_LOG = BlockItemId.create("dark_oak_log");
/*     */   
/* 138 */   public static final BlockItemId MANGROVE_LOG = BlockItemId.create("mangrove_log");
/*     */   
/* 139 */   public static final BlockItemId MANGROVE_ROOTS = BlockItemId.create("mangrove_roots");
/*     */   
/* 140 */   public static final BlockItemId MUDDY_MANGROVE_ROOTS = BlockItemId.create("muddy_mangrove_roots");
/*     */   
/* 141 */   public static final BlockItemId CRIMSON_STEM = BlockItemId.create("crimson_stem");
/*     */   
/* 142 */   public static final BlockItemId WARPED_STEM = BlockItemId.create("warped_stem");
/*     */   
/* 143 */   public static final BlockItemId BAMBOO_BLOCK = BlockItemId.create("bamboo_block");
/*     */   
/* 144 */   public static final BlockItemId STRIPPED_OAK_LOG = BlockItemId.create("stripped_oak_log");
/*     */   
/* 145 */   public static final BlockItemId STRIPPED_SPRUCE_LOG = BlockItemId.create("stripped_spruce_log");
/*     */   
/* 146 */   public static final BlockItemId STRIPPED_BIRCH_LOG = BlockItemId.create("stripped_birch_log");
/*     */   
/* 147 */   public static final BlockItemId STRIPPED_JUNGLE_LOG = BlockItemId.create("stripped_jungle_log");
/*     */   
/* 148 */   public static final BlockItemId STRIPPED_ACACIA_LOG = BlockItemId.create("stripped_acacia_log");
/*     */   
/* 149 */   public static final BlockItemId STRIPPED_CHERRY_LOG = BlockItemId.create("stripped_cherry_log");
/*     */   
/* 150 */   public static final BlockItemId STRIPPED_DARK_OAK_LOG = BlockItemId.create("stripped_dark_oak_log");
/*     */   
/* 151 */   public static final BlockItemId STRIPPED_PALE_OAK_LOG = BlockItemId.create("stripped_pale_oak_log");
/*     */   
/* 152 */   public static final BlockItemId STRIPPED_MANGROVE_LOG = BlockItemId.create("stripped_mangrove_log");
/*     */   
/* 153 */   public static final BlockItemId STRIPPED_CRIMSON_STEM = BlockItemId.create("stripped_crimson_stem");
/*     */   
/* 154 */   public static final BlockItemId STRIPPED_WARPED_STEM = BlockItemId.create("stripped_warped_stem");
/*     */   
/* 155 */   public static final BlockItemId STRIPPED_OAK_WOOD = BlockItemId.create("stripped_oak_wood");
/*     */   
/* 156 */   public static final BlockItemId STRIPPED_SPRUCE_WOOD = BlockItemId.create("stripped_spruce_wood");
/*     */   
/* 157 */   public static final BlockItemId STRIPPED_BIRCH_WOOD = BlockItemId.create("stripped_birch_wood");
/*     */   
/* 158 */   public static final BlockItemId STRIPPED_JUNGLE_WOOD = BlockItemId.create("stripped_jungle_wood");
/*     */   
/* 159 */   public static final BlockItemId STRIPPED_ACACIA_WOOD = BlockItemId.create("stripped_acacia_wood");
/*     */   
/* 160 */   public static final BlockItemId STRIPPED_CHERRY_WOOD = BlockItemId.create("stripped_cherry_wood");
/*     */   
/* 161 */   public static final BlockItemId STRIPPED_DARK_OAK_WOOD = BlockItemId.create("stripped_dark_oak_wood");
/*     */   
/* 162 */   public static final BlockItemId STRIPPED_PALE_OAK_WOOD = BlockItemId.create("stripped_pale_oak_wood");
/*     */   
/* 163 */   public static final BlockItemId STRIPPED_MANGROVE_WOOD = BlockItemId.create("stripped_mangrove_wood");
/*     */   
/* 164 */   public static final BlockItemId STRIPPED_CRIMSON_HYPHAE = BlockItemId.create("stripped_crimson_hyphae");
/*     */   
/* 165 */   public static final BlockItemId STRIPPED_WARPED_HYPHAE = BlockItemId.create("stripped_warped_hyphae");
/*     */   
/* 166 */   public static final BlockItemId STRIPPED_BAMBOO_BLOCK = BlockItemId.create("stripped_bamboo_block");
/*     */   
/* 167 */   public static final BlockItemId OAK_WOOD = BlockItemId.create("oak_wood");
/*     */   
/* 168 */   public static final BlockItemId SPRUCE_WOOD = BlockItemId.create("spruce_wood");
/*     */   
/* 169 */   public static final BlockItemId BIRCH_WOOD = BlockItemId.create("birch_wood");
/*     */   
/* 170 */   public static final BlockItemId JUNGLE_WOOD = BlockItemId.create("jungle_wood");
/*     */   
/* 171 */   public static final BlockItemId ACACIA_WOOD = BlockItemId.create("acacia_wood");
/*     */   
/* 172 */   public static final BlockItemId CHERRY_WOOD = BlockItemId.create("cherry_wood");
/*     */   
/* 173 */   public static final BlockItemId PALE_OAK_WOOD = BlockItemId.create("pale_oak_wood");
/*     */   
/* 174 */   public static final BlockItemId DARK_OAK_WOOD = BlockItemId.create("dark_oak_wood");
/*     */   
/* 175 */   public static final BlockItemId MANGROVE_WOOD = BlockItemId.create("mangrove_wood");
/*     */   
/* 176 */   public static final BlockItemId CRIMSON_HYPHAE = BlockItemId.create("crimson_hyphae");
/*     */   
/* 177 */   public static final BlockItemId WARPED_HYPHAE = BlockItemId.create("warped_hyphae");
/*     */   
/* 178 */   public static final BlockItemId OAK_LEAVES = BlockItemId.create("oak_leaves");
/*     */   
/* 179 */   public static final BlockItemId SPRUCE_LEAVES = BlockItemId.create("spruce_leaves");
/*     */   
/* 180 */   public static final BlockItemId BIRCH_LEAVES = BlockItemId.create("birch_leaves");
/*     */   
/* 181 */   public static final BlockItemId JUNGLE_LEAVES = BlockItemId.create("jungle_leaves");
/*     */   
/* 182 */   public static final BlockItemId ACACIA_LEAVES = BlockItemId.create("acacia_leaves");
/*     */   
/* 183 */   public static final BlockItemId CHERRY_LEAVES = BlockItemId.create("cherry_leaves");
/*     */   
/* 184 */   public static final BlockItemId DARK_OAK_LEAVES = BlockItemId.create("dark_oak_leaves");
/*     */   
/* 185 */   public static final BlockItemId PALE_OAK_LEAVES = BlockItemId.create("pale_oak_leaves");
/*     */   
/* 186 */   public static final BlockItemId MANGROVE_LEAVES = BlockItemId.create("mangrove_leaves");
/*     */   
/* 187 */   public static final BlockItemId AZALEA_LEAVES = BlockItemId.create("azalea_leaves");
/*     */   
/* 188 */   public static final BlockItemId FLOWERING_AZALEA_LEAVES = BlockItemId.create("flowering_azalea_leaves");
/*     */   
/* 189 */   public static final BlockItemId SPONGE = BlockItemId.create("sponge");
/*     */   
/* 190 */   public static final BlockItemId WET_SPONGE = BlockItemId.create("wet_sponge");
/*     */   
/* 191 */   public static final BlockItemId GLASS = BlockItemId.create("glass");
/*     */   
/* 192 */   public static final BlockItemId TINTED_GLASS = BlockItemId.create("tinted_glass");
/*     */   
/* 193 */   public static final BlockItemId LAPIS_BLOCK = BlockItemId.create("lapis_block");
/*     */   
/* 194 */   public static final BlockItemId SANDSTONE = BlockItemId.create("sandstone");
/*     */   
/* 195 */   public static final BlockItemId CHISELED_SANDSTONE = BlockItemId.create("chiseled_sandstone");
/*     */   
/* 196 */   public static final BlockItemId CUT_SANDSTONE = BlockItemId.create("cut_sandstone");
/*     */   
/* 197 */   public static final BlockItemId COBWEB = BlockItemId.create("cobweb");
/*     */   
/* 198 */   public static final BlockItemId SHORT_GRASS = BlockItemId.create("short_grass");
/*     */   
/* 199 */   public static final BlockItemId FERN = BlockItemId.create("fern");
/*     */   
/* 200 */   public static final BlockItemId BUSH = BlockItemId.create("bush");
/*     */   
/* 201 */   public static final BlockItemId AZALEA = BlockItemId.create("azalea");
/*     */   
/* 202 */   public static final BlockItemId FLOWERING_AZALEA = BlockItemId.create("flowering_azalea");
/*     */   
/* 203 */   public static final BlockItemId DEAD_BUSH = BlockItemId.create("dead_bush");
/*     */   
/* 204 */   public static final BlockItemId FIREFLY_BUSH = BlockItemId.create("firefly_bush");
/*     */   
/* 205 */   public static final BlockItemId SHORT_DRY_GRASS = BlockItemId.create("short_dry_grass");
/*     */   
/* 206 */   public static final BlockItemId TALL_DRY_GRASS = BlockItemId.create("tall_dry_grass");
/*     */   
/* 207 */   public static final BlockItemId SEAGRASS = BlockItemId.create("seagrass");
/*     */   
/* 208 */   public static final BlockItemId SEA_PICKLE = BlockItemId.create("sea_pickle");
/*     */   
/* 209 */   public static final BlockItemId DANDELION = BlockItemId.create("dandelion");
/*     */   
/* 210 */   public static final BlockItemId GOLDEN_DANDELION = BlockItemId.create("golden_dandelion");
/*     */   
/* 211 */   public static final BlockItemId OPEN_EYEBLOSSOM = BlockItemId.create("open_eyeblossom");
/*     */   
/* 212 */   public static final BlockItemId CLOSED_EYEBLOSSOM = BlockItemId.create("closed_eyeblossom");
/*     */   
/* 213 */   public static final BlockItemId POPPY = BlockItemId.create("poppy");
/*     */   
/* 214 */   public static final BlockItemId BLUE_ORCHID = BlockItemId.create("blue_orchid");
/*     */   
/* 215 */   public static final BlockItemId ALLIUM = BlockItemId.create("allium");
/*     */   
/* 216 */   public static final BlockItemId AZURE_BLUET = BlockItemId.create("azure_bluet");
/*     */   
/* 217 */   public static final BlockItemId RED_TULIP = BlockItemId.create("red_tulip");
/*     */   
/* 218 */   public static final BlockItemId ORANGE_TULIP = BlockItemId.create("orange_tulip");
/*     */   
/* 219 */   public static final BlockItemId WHITE_TULIP = BlockItemId.create("white_tulip");
/*     */   
/* 220 */   public static final BlockItemId PINK_TULIP = BlockItemId.create("pink_tulip");
/*     */   
/* 221 */   public static final BlockItemId OXEYE_DAISY = BlockItemId.create("oxeye_daisy");
/*     */   
/* 222 */   public static final BlockItemId CORNFLOWER = BlockItemId.create("cornflower");
/*     */   
/* 223 */   public static final BlockItemId LILY_OF_THE_VALLEY = BlockItemId.create("lily_of_the_valley");
/*     */   
/* 224 */   public static final BlockItemId WITHER_ROSE = BlockItemId.create("wither_rose");
/*     */   
/* 225 */   public static final BlockItemId TORCHFLOWER = BlockItemId.create("torchflower");
/*     */   
/* 226 */   public static final BlockItemId PITCHER_PLANT = BlockItemId.create("pitcher_plant");
/*     */   
/* 227 */   public static final BlockItemId SPORE_BLOSSOM = BlockItemId.create("spore_blossom");
/*     */   
/* 228 */   public static final BlockItemId BROWN_MUSHROOM = BlockItemId.create("brown_mushroom");
/*     */   
/* 229 */   public static final BlockItemId RED_MUSHROOM = BlockItemId.create("red_mushroom");
/*     */   
/* 230 */   public static final BlockItemId CRIMSON_FUNGUS = BlockItemId.create("crimson_fungus");
/*     */   
/* 231 */   public static final BlockItemId WARPED_FUNGUS = BlockItemId.create("warped_fungus");
/*     */   
/* 232 */   public static final BlockItemId CRIMSON_ROOTS = BlockItemId.create("crimson_roots");
/*     */   
/* 233 */   public static final BlockItemId WARPED_ROOTS = BlockItemId.create("warped_roots");
/*     */   
/* 234 */   public static final BlockItemId NETHER_SPROUTS = BlockItemId.create("nether_sprouts");
/*     */   
/* 235 */   public static final BlockItemId WEEPING_VINES = BlockItemId.create("weeping_vines");
/*     */   
/* 236 */   public static final BlockItemId TWISTING_VINES = BlockItemId.create("twisting_vines");
/*     */   
/* 237 */   public static final BlockItemId SUGAR_CANE = BlockItemId.create("sugar_cane");
/*     */   
/* 238 */   public static final BlockItemId KELP = BlockItemId.create("kelp");
/*     */   
/* 239 */   public static final BlockItemId PINK_PETALS = BlockItemId.create("pink_petals");
/*     */   
/* 240 */   public static final BlockItemId WILDFLOWERS = BlockItemId.create("wildflowers");
/*     */   
/* 241 */   public static final BlockItemId LEAF_LITTER = BlockItemId.create("leaf_litter");
/*     */   
/* 242 */   public static final BlockItemId MOSS_CARPET = BlockItemId.create("moss_carpet");
/*     */   
/* 243 */   public static final BlockItemId MOSS_BLOCK = BlockItemId.create("moss_block");
/*     */   
/* 244 */   public static final BlockItemId PALE_MOSS_CARPET = BlockItemId.create("pale_moss_carpet");
/*     */   
/* 245 */   public static final BlockItemId PALE_HANGING_MOSS = BlockItemId.create("pale_hanging_moss");
/*     */   
/* 246 */   public static final BlockItemId PALE_MOSS_BLOCK = BlockItemId.create("pale_moss_block");
/*     */   
/* 247 */   public static final BlockItemId HANGING_ROOTS = BlockItemId.create("hanging_roots");
/*     */   
/* 248 */   public static final BlockItemId BIG_DRIPLEAF = BlockItemId.create("big_dripleaf");
/*     */   
/* 249 */   public static final BlockItemId SMALL_DRIPLEAF = BlockItemId.create("small_dripleaf");
/*     */   
/* 250 */   public static final BlockItemId BAMBOO = BlockItemId.create("bamboo");
/*     */   
/* 251 */   public static final BlockItemId OAK_SLAB = BlockItemId.create("oak_slab");
/*     */   
/* 252 */   public static final BlockItemId SPRUCE_SLAB = BlockItemId.create("spruce_slab");
/*     */   
/* 253 */   public static final BlockItemId BIRCH_SLAB = BlockItemId.create("birch_slab");
/*     */   
/* 254 */   public static final BlockItemId JUNGLE_SLAB = BlockItemId.create("jungle_slab");
/*     */   
/* 255 */   public static final BlockItemId ACACIA_SLAB = BlockItemId.create("acacia_slab");
/*     */   
/* 256 */   public static final BlockItemId CHERRY_SLAB = BlockItemId.create("cherry_slab");
/*     */   
/* 257 */   public static final BlockItemId DARK_OAK_SLAB = BlockItemId.create("dark_oak_slab");
/*     */   
/* 258 */   public static final BlockItemId PALE_OAK_SLAB = BlockItemId.create("pale_oak_slab");
/*     */   
/* 259 */   public static final BlockItemId MANGROVE_SLAB = BlockItemId.create("mangrove_slab");
/*     */   
/* 260 */   public static final BlockItemId BAMBOO_SLAB = BlockItemId.create("bamboo_slab");
/*     */   
/* 261 */   public static final BlockItemId BAMBOO_MOSAIC_SLAB = BlockItemId.create("bamboo_mosaic_slab");
/*     */   
/* 262 */   public static final BlockItemId CRIMSON_SLAB = BlockItemId.create("crimson_slab");
/*     */   
/* 263 */   public static final BlockItemId WARPED_SLAB = BlockItemId.create("warped_slab");
/*     */   
/* 264 */   public static final BlockItemId STONE_SLAB = BlockItemId.create("stone_slab");
/*     */   
/* 265 */   public static final BlockItemId SMOOTH_STONE_SLAB = BlockItemId.create("smooth_stone_slab");
/*     */   
/* 266 */   public static final BlockItemId SANDSTONE_SLAB = BlockItemId.create("sandstone_slab");
/*     */   
/* 267 */   public static final BlockItemId CUT_SANDSTONE_SLAB = BlockItemId.create("cut_sandstone_slab");
/*     */   
/* 268 */   public static final BlockItemId PETRIFIED_OAK_SLAB = BlockItemId.create("petrified_oak_slab");
/*     */   
/* 269 */   public static final BlockItemId COBBLESTONE_SLAB = BlockItemId.create("cobblestone_slab");
/*     */   
/* 270 */   public static final BlockItemId BRICK_SLAB = BlockItemId.create("brick_slab");
/*     */   
/* 271 */   public static final BlockItemId STONE_BRICK_SLAB = BlockItemId.create("stone_brick_slab");
/*     */   
/* 272 */   public static final BlockItemId MUD_BRICK_SLAB = BlockItemId.create("mud_brick_slab");
/*     */   
/* 273 */   public static final BlockItemId NETHER_BRICK_SLAB = BlockItemId.create("nether_brick_slab");
/*     */   
/* 274 */   public static final BlockItemId QUARTZ_SLAB = BlockItemId.create("quartz_slab");
/*     */   
/* 275 */   public static final BlockItemId RED_SANDSTONE_SLAB = BlockItemId.create("red_sandstone_slab");
/*     */   
/* 276 */   public static final BlockItemId CUT_RED_SANDSTONE_SLAB = BlockItemId.create("cut_red_sandstone_slab");
/*     */   
/* 277 */   public static final BlockItemId PURPUR_SLAB = BlockItemId.create("purpur_slab");
/*     */   
/* 278 */   public static final BlockItemId PRISMARINE_SLAB = BlockItemId.create("prismarine_slab");
/*     */   
/* 279 */   public static final BlockItemId PRISMARINE_BRICK_SLAB = BlockItemId.create("prismarine_brick_slab");
/*     */   
/* 280 */   public static final BlockItemId DARK_PRISMARINE_SLAB = BlockItemId.create("dark_prismarine_slab");
/*     */   
/* 281 */   public static final BlockItemId SMOOTH_QUARTZ = BlockItemId.create("smooth_quartz");
/*     */   
/* 282 */   public static final BlockItemId SMOOTH_RED_SANDSTONE = BlockItemId.create("smooth_red_sandstone");
/*     */   
/* 283 */   public static final BlockItemId SMOOTH_SANDSTONE = BlockItemId.create("smooth_sandstone");
/*     */   
/* 284 */   public static final BlockItemId SMOOTH_STONE = BlockItemId.create("smooth_stone");
/*     */   
/* 285 */   public static final BlockItemId BRICKS = BlockItemId.create("bricks");
/*     */   
/* 286 */   public static final BlockItemId ACACIA_SHELF = BlockItemId.create("acacia_shelf");
/*     */   
/* 287 */   public static final BlockItemId BAMBOO_SHELF = BlockItemId.create("bamboo_shelf");
/*     */   
/* 288 */   public static final BlockItemId BIRCH_SHELF = BlockItemId.create("birch_shelf");
/*     */   
/* 289 */   public static final BlockItemId CHERRY_SHELF = BlockItemId.create("cherry_shelf");
/*     */   
/* 290 */   public static final BlockItemId CRIMSON_SHELF = BlockItemId.create("crimson_shelf");
/*     */   
/* 291 */   public static final BlockItemId DARK_OAK_SHELF = BlockItemId.create("dark_oak_shelf");
/*     */   
/* 292 */   public static final BlockItemId JUNGLE_SHELF = BlockItemId.create("jungle_shelf");
/*     */   
/* 293 */   public static final BlockItemId MANGROVE_SHELF = BlockItemId.create("mangrove_shelf");
/*     */   
/* 294 */   public static final BlockItemId OAK_SHELF = BlockItemId.create("oak_shelf");
/*     */   
/* 295 */   public static final BlockItemId PALE_OAK_SHELF = BlockItemId.create("pale_oak_shelf");
/*     */   
/* 296 */   public static final BlockItemId SPRUCE_SHELF = BlockItemId.create("spruce_shelf");
/*     */   
/* 297 */   public static final BlockItemId WARPED_SHELF = BlockItemId.create("warped_shelf");
/*     */   
/* 298 */   public static final BlockItemId BOOKSHELF = BlockItemId.create("bookshelf");
/*     */   
/* 299 */   public static final BlockItemId CHISELED_BOOKSHELF = BlockItemId.create("chiseled_bookshelf");
/*     */   
/* 300 */   public static final BlockItemId DECORATED_POT = BlockItemId.create("decorated_pot");
/*     */   
/* 301 */   public static final BlockItemId MOSSY_COBBLESTONE = BlockItemId.create("mossy_cobblestone");
/*     */   
/* 302 */   public static final BlockItemId OBSIDIAN = BlockItemId.create("obsidian");
/*     */   
/* 303 */   public static final BlockItemId TORCH = BlockItemId.create("torch");
/*     */   
/* 304 */   public static final BlockItemId END_ROD = BlockItemId.create("end_rod");
/*     */   
/* 305 */   public static final BlockItemId CHORUS_PLANT = BlockItemId.create("chorus_plant");
/*     */   
/* 306 */   public static final BlockItemId CHORUS_FLOWER = BlockItemId.create("chorus_flower");
/*     */   
/* 307 */   public static final BlockItemId PURPUR_BLOCK = BlockItemId.create("purpur_block");
/*     */   
/* 308 */   public static final BlockItemId PURPUR_PILLAR = BlockItemId.create("purpur_pillar");
/*     */   
/* 309 */   public static final BlockItemId PURPUR_STAIRS = BlockItemId.create("purpur_stairs");
/*     */   
/* 310 */   public static final BlockItemId SPAWNER = BlockItemId.create("spawner");
/*     */   
/* 311 */   public static final BlockItemId CREAKING_HEART = BlockItemId.create("creaking_heart");
/*     */   
/* 312 */   public static final BlockItemId CHEST = BlockItemId.create("chest");
/*     */   
/* 313 */   public static final BlockItemId CRAFTING_TABLE = BlockItemId.create("crafting_table");
/*     */   
/* 314 */   public static final BlockItemId FARMLAND = BlockItemId.create("farmland");
/*     */   
/* 315 */   public static final BlockItemId FURNACE = BlockItemId.create("furnace");
/*     */   
/* 316 */   public static final BlockItemId LADDER = BlockItemId.create("ladder");
/*     */   
/* 317 */   public static final BlockItemId COBBLESTONE_STAIRS = BlockItemId.create("cobblestone_stairs");
/*     */   
/* 318 */   public static final BlockItemId SNOW = BlockItemId.create("snow");
/*     */   
/* 319 */   public static final BlockItemId ICE = BlockItemId.create("ice");
/*     */   
/* 320 */   public static final BlockItemId SNOW_BLOCK = BlockItemId.create("snow_block");
/*     */   
/* 321 */   public static final BlockItemId CACTUS = BlockItemId.create("cactus");
/*     */   
/* 322 */   public static final BlockItemId CACTUS_FLOWER = BlockItemId.create("cactus_flower");
/*     */   
/* 323 */   public static final BlockItemId CLAY = BlockItemId.create("clay");
/*     */   
/* 324 */   public static final BlockItemId JUKEBOX = BlockItemId.create("jukebox");
/*     */   
/* 325 */   public static final BlockItemId OAK_FENCE = BlockItemId.create("oak_fence");
/*     */   
/* 326 */   public static final BlockItemId SPRUCE_FENCE = BlockItemId.create("spruce_fence");
/*     */   
/* 327 */   public static final BlockItemId BIRCH_FENCE = BlockItemId.create("birch_fence");
/*     */   
/* 328 */   public static final BlockItemId JUNGLE_FENCE = BlockItemId.create("jungle_fence");
/*     */   
/* 329 */   public static final BlockItemId ACACIA_FENCE = BlockItemId.create("acacia_fence");
/*     */   
/* 330 */   public static final BlockItemId CHERRY_FENCE = BlockItemId.create("cherry_fence");
/*     */   
/* 331 */   public static final BlockItemId DARK_OAK_FENCE = BlockItemId.create("dark_oak_fence");
/*     */   
/* 332 */   public static final BlockItemId PALE_OAK_FENCE = BlockItemId.create("pale_oak_fence");
/*     */   
/* 333 */   public static final BlockItemId MANGROVE_FENCE = BlockItemId.create("mangrove_fence");
/*     */   
/* 334 */   public static final BlockItemId BAMBOO_FENCE = BlockItemId.create("bamboo_fence");
/*     */   
/* 335 */   public static final BlockItemId CRIMSON_FENCE = BlockItemId.create("crimson_fence");
/*     */   
/* 336 */   public static final BlockItemId WARPED_FENCE = BlockItemId.create("warped_fence");
/*     */   
/* 337 */   public static final BlockItemId CARVED_PUMPKIN = BlockItemId.create("carved_pumpkin");
/*     */   
/* 338 */   public static final BlockItemId JACK_O_LANTERN = BlockItemId.create("jack_o_lantern");
/*     */   
/* 339 */   public static final BlockItemId NETHERRACK = BlockItemId.create("netherrack");
/*     */   
/* 340 */   public static final BlockItemId SOUL_SAND = BlockItemId.create("soul_sand");
/*     */   
/* 341 */   public static final BlockItemId SOUL_SOIL = BlockItemId.create("soul_soil");
/*     */   
/* 342 */   public static final BlockItemId BASALT = BlockItemId.create("basalt");
/*     */   
/* 343 */   public static final BlockItemId POLISHED_BASALT = BlockItemId.create("polished_basalt");
/*     */   
/* 344 */   public static final BlockItemId SMOOTH_BASALT = BlockItemId.create("smooth_basalt");
/*     */   
/* 345 */   public static final BlockItemId SOUL_TORCH = BlockItemId.create("soul_torch");
/*     */   
/* 346 */   public static final BlockItemId COPPER_TORCH = BlockItemId.create("copper_torch");
/*     */   
/* 347 */   public static final BlockItemId GLOWSTONE = BlockItemId.create("glowstone");
/*     */   
/* 348 */   public static final BlockItemId INFESTED_STONE = BlockItemId.create("infested_stone");
/*     */   
/* 349 */   public static final BlockItemId INFESTED_COBBLESTONE = BlockItemId.create("infested_cobblestone");
/*     */   
/* 350 */   public static final BlockItemId INFESTED_STONE_BRICKS = BlockItemId.create("infested_stone_bricks");
/*     */   
/* 351 */   public static final BlockItemId INFESTED_MOSSY_STONE_BRICKS = BlockItemId.create("infested_mossy_stone_bricks");
/*     */   
/* 352 */   public static final BlockItemId INFESTED_CRACKED_STONE_BRICKS = BlockItemId.create("infested_cracked_stone_bricks");
/*     */   
/* 353 */   public static final BlockItemId INFESTED_CHISELED_STONE_BRICKS = BlockItemId.create("infested_chiseled_stone_bricks");
/*     */   
/* 354 */   public static final BlockItemId INFESTED_DEEPSLATE = BlockItemId.create("infested_deepslate");
/*     */   
/* 355 */   public static final BlockItemId STONE_BRICKS = BlockItemId.create("stone_bricks");
/*     */   
/* 356 */   public static final BlockItemId MOSSY_STONE_BRICKS = BlockItemId.create("mossy_stone_bricks");
/*     */   
/* 357 */   public static final BlockItemId CRACKED_STONE_BRICKS = BlockItemId.create("cracked_stone_bricks");
/*     */   
/* 358 */   public static final BlockItemId CHISELED_STONE_BRICKS = BlockItemId.create("chiseled_stone_bricks");
/*     */   
/* 359 */   public static final BlockItemId PACKED_MUD = BlockItemId.create("packed_mud");
/*     */   
/* 360 */   public static final BlockItemId MUD_BRICKS = BlockItemId.create("mud_bricks");
/*     */   
/* 361 */   public static final BlockItemId DEEPSLATE_BRICKS = BlockItemId.create("deepslate_bricks");
/*     */   
/* 362 */   public static final BlockItemId CRACKED_DEEPSLATE_BRICKS = BlockItemId.create("cracked_deepslate_bricks");
/*     */   
/* 363 */   public static final BlockItemId DEEPSLATE_TILES = BlockItemId.create("deepslate_tiles");
/*     */   
/* 364 */   public static final BlockItemId CRACKED_DEEPSLATE_TILES = BlockItemId.create("cracked_deepslate_tiles");
/*     */   
/* 365 */   public static final BlockItemId CHISELED_DEEPSLATE = BlockItemId.create("chiseled_deepslate");
/*     */   
/* 366 */   public static final BlockItemId REINFORCED_DEEPSLATE = BlockItemId.create("reinforced_deepslate");
/*     */   
/* 367 */   public static final BlockItemId BROWN_MUSHROOM_BLOCK = BlockItemId.create("brown_mushroom_block");
/*     */   
/* 368 */   public static final BlockItemId RED_MUSHROOM_BLOCK = BlockItemId.create("red_mushroom_block");
/*     */   
/* 369 */   public static final BlockItemId MUSHROOM_STEM = BlockItemId.create("mushroom_stem");
/*     */   
/* 370 */   public static final BlockItemId IRON_BARS = BlockItemId.create("iron_bars");
/*     */   
/* 371 */   public static final BlockItemId IRON_CHAIN = BlockItemId.create("iron_chain");
/*     */   
/* 372 */   public static final BlockItemId GLASS_PANE = BlockItemId.create("glass_pane");
/*     */   
/* 373 */   public static final BlockItemId VINE = BlockItemId.create("vine");
/*     */   
/* 374 */   public static final BlockItemId GLOW_LICHEN = BlockItemId.create("glow_lichen");
/*     */   
/* 375 */   public static final BlockItemId RESIN_CLUMP = BlockItemId.create("resin_clump");
/*     */   
/* 376 */   public static final BlockItemId RESIN_BLOCK = BlockItemId.create("resin_block");
/*     */   
/* 377 */   public static final BlockItemId RESIN_BRICKS = BlockItemId.create("resin_bricks");
/*     */   
/* 378 */   public static final BlockItemId RESIN_BRICK_STAIRS = BlockItemId.create("resin_brick_stairs");
/*     */   
/* 379 */   public static final BlockItemId RESIN_BRICK_SLAB = BlockItemId.create("resin_brick_slab");
/*     */   
/* 380 */   public static final BlockItemId RESIN_BRICK_WALL = BlockItemId.create("resin_brick_wall");
/*     */   
/* 381 */   public static final BlockItemId CHISELED_RESIN_BRICKS = BlockItemId.create("chiseled_resin_bricks");
/*     */   
/* 382 */   public static final BlockItemId BRICK_STAIRS = BlockItemId.create("brick_stairs");
/*     */   
/* 383 */   public static final BlockItemId STONE_BRICK_STAIRS = BlockItemId.create("stone_brick_stairs");
/*     */   
/* 384 */   public static final BlockItemId MUD_BRICK_STAIRS = BlockItemId.create("mud_brick_stairs");
/*     */   
/* 385 */   public static final BlockItemId MYCELIUM = BlockItemId.create("mycelium");
/*     */   
/* 386 */   public static final BlockItemId LILY_PAD = BlockItemId.create("lily_pad");
/*     */   
/* 387 */   public static final BlockItemId NETHER_BRICKS = BlockItemId.create("nether_bricks");
/*     */   
/* 388 */   public static final BlockItemId CRACKED_NETHER_BRICKS = BlockItemId.create("cracked_nether_bricks");
/*     */   
/* 389 */   public static final BlockItemId CHISELED_NETHER_BRICKS = BlockItemId.create("chiseled_nether_bricks");
/*     */   
/* 390 */   public static final BlockItemId NETHER_BRICK_FENCE = BlockItemId.create("nether_brick_fence");
/*     */   
/* 391 */   public static final BlockItemId NETHER_BRICK_STAIRS = BlockItemId.create("nether_brick_stairs");
/*     */   
/* 392 */   public static final BlockItemId SCULK = BlockItemId.create("sculk");
/*     */   
/* 393 */   public static final BlockItemId SCULK_VEIN = BlockItemId.create("sculk_vein");
/*     */   
/* 394 */   public static final BlockItemId SCULK_CATALYST = BlockItemId.create("sculk_catalyst");
/*     */   
/* 395 */   public static final BlockItemId SCULK_SHRIEKER = BlockItemId.create("sculk_shrieker");
/*     */   
/* 396 */   public static final BlockItemId ENCHANTING_TABLE = BlockItemId.create("enchanting_table");
/*     */   
/* 397 */   public static final BlockItemId END_PORTAL_FRAME = BlockItemId.create("end_portal_frame");
/*     */   
/* 398 */   public static final BlockItemId END_STONE = BlockItemId.create("end_stone");
/*     */   
/* 399 */   public static final BlockItemId END_STONE_BRICKS = BlockItemId.create("end_stone_bricks");
/*     */   
/* 400 */   public static final BlockItemId DRAGON_EGG = BlockItemId.create("dragon_egg");
/*     */   
/* 401 */   public static final BlockItemId SANDSTONE_STAIRS = BlockItemId.create("sandstone_stairs");
/*     */   
/* 402 */   public static final BlockItemId ENDER_CHEST = BlockItemId.create("ender_chest");
/*     */   
/* 403 */   public static final BlockItemId EMERALD_BLOCK = BlockItemId.create("emerald_block");
/*     */   
/* 404 */   public static final BlockItemId OAK_STAIRS = BlockItemId.create("oak_stairs");
/*     */   
/* 405 */   public static final BlockItemId SPRUCE_STAIRS = BlockItemId.create("spruce_stairs");
/*     */   
/* 406 */   public static final BlockItemId BIRCH_STAIRS = BlockItemId.create("birch_stairs");
/*     */   
/* 407 */   public static final BlockItemId JUNGLE_STAIRS = BlockItemId.create("jungle_stairs");
/*     */   
/* 408 */   public static final BlockItemId ACACIA_STAIRS = BlockItemId.create("acacia_stairs");
/*     */   
/* 409 */   public static final BlockItemId CHERRY_STAIRS = BlockItemId.create("cherry_stairs");
/*     */   
/* 410 */   public static final BlockItemId DARK_OAK_STAIRS = BlockItemId.create("dark_oak_stairs");
/*     */   
/* 411 */   public static final BlockItemId PALE_OAK_STAIRS = BlockItemId.create("pale_oak_stairs");
/*     */   
/* 412 */   public static final BlockItemId MANGROVE_STAIRS = BlockItemId.create("mangrove_stairs");
/*     */   
/* 413 */   public static final BlockItemId BAMBOO_STAIRS = BlockItemId.create("bamboo_stairs");
/*     */   
/* 414 */   public static final BlockItemId BAMBOO_MOSAIC_STAIRS = BlockItemId.create("bamboo_mosaic_stairs");
/*     */   
/* 415 */   public static final BlockItemId CRIMSON_STAIRS = BlockItemId.create("crimson_stairs");
/*     */   
/* 416 */   public static final BlockItemId WARPED_STAIRS = BlockItemId.create("warped_stairs");
/*     */   
/* 417 */   public static final BlockItemId COMMAND_BLOCK = BlockItemId.create("command_block");
/*     */   
/* 418 */   public static final BlockItemId BEACON = BlockItemId.create("beacon");
/*     */   
/* 419 */   public static final BlockItemId COBBLESTONE_WALL = BlockItemId.create("cobblestone_wall");
/*     */   
/* 420 */   public static final BlockItemId MOSSY_COBBLESTONE_WALL = BlockItemId.create("mossy_cobblestone_wall");
/*     */   
/* 421 */   public static final BlockItemId BRICK_WALL = BlockItemId.create("brick_wall");
/*     */   
/* 422 */   public static final BlockItemId PRISMARINE_WALL = BlockItemId.create("prismarine_wall");
/*     */   
/* 423 */   public static final BlockItemId RED_SANDSTONE_WALL = BlockItemId.create("red_sandstone_wall");
/*     */   
/* 424 */   public static final BlockItemId MOSSY_STONE_BRICK_WALL = BlockItemId.create("mossy_stone_brick_wall");
/*     */   
/* 425 */   public static final BlockItemId GRANITE_WALL = BlockItemId.create("granite_wall");
/*     */   
/* 426 */   public static final BlockItemId STONE_BRICK_WALL = BlockItemId.create("stone_brick_wall");
/*     */   
/* 427 */   public static final BlockItemId MUD_BRICK_WALL = BlockItemId.create("mud_brick_wall");
/*     */   
/* 428 */   public static final BlockItemId NETHER_BRICK_WALL = BlockItemId.create("nether_brick_wall");
/*     */   
/* 429 */   public static final BlockItemId ANDESITE_WALL = BlockItemId.create("andesite_wall");
/*     */   
/* 430 */   public static final BlockItemId RED_NETHER_BRICK_WALL = BlockItemId.create("red_nether_brick_wall");
/*     */   
/* 431 */   public static final BlockItemId SANDSTONE_WALL = BlockItemId.create("sandstone_wall");
/*     */   
/* 432 */   public static final BlockItemId END_STONE_BRICK_WALL = BlockItemId.create("end_stone_brick_wall");
/*     */   
/* 433 */   public static final BlockItemId DIORITE_WALL = BlockItemId.create("diorite_wall");
/*     */   
/* 434 */   public static final BlockItemId BLACKSTONE_WALL = BlockItemId.create("blackstone_wall");
/*     */   
/* 435 */   public static final BlockItemId POLISHED_BLACKSTONE_WALL = BlockItemId.create("polished_blackstone_wall");
/*     */   
/* 436 */   public static final BlockItemId POLISHED_BLACKSTONE_BRICK_WALL = BlockItemId.create("polished_blackstone_brick_wall");
/*     */   
/* 437 */   public static final BlockItemId COBBLED_DEEPSLATE_WALL = BlockItemId.create("cobbled_deepslate_wall");
/*     */   
/* 438 */   public static final BlockItemId POLISHED_DEEPSLATE_WALL = BlockItemId.create("polished_deepslate_wall");
/*     */   
/* 439 */   public static final BlockItemId DEEPSLATE_BRICK_WALL = BlockItemId.create("deepslate_brick_wall");
/*     */   
/* 440 */   public static final BlockItemId DEEPSLATE_TILE_WALL = BlockItemId.create("deepslate_tile_wall");
/*     */   
/* 441 */   public static final BlockItemId ANVIL = BlockItemId.create("anvil");
/*     */   
/* 442 */   public static final BlockItemId CHIPPED_ANVIL = BlockItemId.create("chipped_anvil");
/*     */   
/* 443 */   public static final BlockItemId DAMAGED_ANVIL = BlockItemId.create("damaged_anvil");
/*     */   
/* 444 */   public static final BlockItemId CHISELED_QUARTZ_BLOCK = BlockItemId.create("chiseled_quartz_block");
/*     */   
/* 445 */   public static final BlockItemId QUARTZ_BLOCK = BlockItemId.create("quartz_block");
/*     */   
/* 446 */   public static final BlockItemId QUARTZ_BRICKS = BlockItemId.create("quartz_bricks");
/*     */   
/* 447 */   public static final BlockItemId QUARTZ_PILLAR = BlockItemId.create("quartz_pillar");
/*     */   
/* 448 */   public static final BlockItemId QUARTZ_STAIRS = BlockItemId.create("quartz_stairs");
/*     */   
/* 449 */   public static final BlockItemId BARRIER = BlockItemId.create("barrier");
/*     */   
/* 450 */   public static final BlockItemId LIGHT = BlockItemId.create("light");
/*     */   
/* 451 */   public static final BlockItemId HAY_BLOCK = BlockItemId.create("hay_block");
/*     */   
/* 452 */   public static final BlockItemId TERRACOTTA = BlockItemId.create("terracotta");
/*     */   
/* 453 */   public static final BlockItemId PACKED_ICE = BlockItemId.create("packed_ice");
/*     */   
/* 454 */   public static final BlockItemId DIRT_PATH = BlockItemId.create("dirt_path");
/*     */   
/* 455 */   public static final BlockItemId SUNFLOWER = BlockItemId.create("sunflower");
/*     */   
/* 456 */   public static final BlockItemId LILAC = BlockItemId.create("lilac");
/*     */   
/* 457 */   public static final BlockItemId ROSE_BUSH = BlockItemId.create("rose_bush");
/*     */   
/* 458 */   public static final BlockItemId PEONY = BlockItemId.create("peony");
/*     */   
/* 459 */   public static final BlockItemId TALL_GRASS = BlockItemId.create("tall_grass");
/*     */   
/* 460 */   public static final BlockItemId LARGE_FERN = BlockItemId.create("large_fern");
/*     */   
/* 461 */   public static final BlockItemId PRISMARINE = BlockItemId.create("prismarine");
/*     */   
/* 462 */   public static final BlockItemId PRISMARINE_BRICKS = BlockItemId.create("prismarine_bricks");
/*     */   
/* 463 */   public static final BlockItemId DARK_PRISMARINE = BlockItemId.create("dark_prismarine");
/*     */   
/* 464 */   public static final BlockItemId PRISMARINE_STAIRS = BlockItemId.create("prismarine_stairs");
/*     */   
/* 465 */   public static final BlockItemId PRISMARINE_BRICK_STAIRS = BlockItemId.create("prismarine_brick_stairs");
/*     */   
/* 466 */   public static final BlockItemId DARK_PRISMARINE_STAIRS = BlockItemId.create("dark_prismarine_stairs");
/*     */   
/* 467 */   public static final BlockItemId SEA_LANTERN = BlockItemId.create("sea_lantern");
/*     */   
/* 468 */   public static final BlockItemId RED_SANDSTONE = BlockItemId.create("red_sandstone");
/*     */   
/* 469 */   public static final BlockItemId CHISELED_RED_SANDSTONE = BlockItemId.create("chiseled_red_sandstone");
/*     */   
/* 470 */   public static final BlockItemId CUT_RED_SANDSTONE = BlockItemId.create("cut_red_sandstone");
/*     */   
/* 471 */   public static final BlockItemId RED_SANDSTONE_STAIRS = BlockItemId.create("red_sandstone_stairs");
/*     */   
/* 472 */   public static final BlockItemId REPEATING_COMMAND_BLOCK = BlockItemId.create("repeating_command_block");
/*     */   
/* 473 */   public static final BlockItemId CHAIN_COMMAND_BLOCK = BlockItemId.create("chain_command_block");
/*     */   
/* 474 */   public static final BlockItemId MAGMA_BLOCK = BlockItemId.create("magma_block");
/*     */   
/* 475 */   public static final BlockItemId NETHER_WART_BLOCK = BlockItemId.create("nether_wart_block");
/*     */   
/* 476 */   public static final BlockItemId WARPED_WART_BLOCK = BlockItemId.create("warped_wart_block");
/*     */   
/* 477 */   public static final BlockItemId RED_NETHER_BRICKS = BlockItemId.create("red_nether_bricks");
/*     */   
/* 478 */   public static final BlockItemId BONE_BLOCK = BlockItemId.create("bone_block");
/*     */   
/* 479 */   public static final BlockItemId STRUCTURE_VOID = BlockItemId.create("structure_void");
/*     */   
/* 480 */   public static final BlockItemId SHULKER_BOX = BlockItemId.create("shulker_box");
/*     */   
/* 481 */   public static final BlockItemId TURTLE_EGG = BlockItemId.create("turtle_egg");
/*     */   
/* 482 */   public static final BlockItemId SNIFFER_EGG = BlockItemId.create("sniffer_egg");
/*     */   
/* 483 */   public static final BlockItemId DRIED_GHAST = BlockItemId.create("dried_ghast");
/*     */   
/* 484 */   public static final BlockItemId DEAD_TUBE_CORAL_BLOCK = BlockItemId.create("dead_tube_coral_block");
/*     */   
/* 485 */   public static final BlockItemId DEAD_BRAIN_CORAL_BLOCK = BlockItemId.create("dead_brain_coral_block");
/*     */   
/* 486 */   public static final BlockItemId DEAD_BUBBLE_CORAL_BLOCK = BlockItemId.create("dead_bubble_coral_block");
/*     */   
/* 487 */   public static final BlockItemId DEAD_FIRE_CORAL_BLOCK = BlockItemId.create("dead_fire_coral_block");
/*     */   
/* 488 */   public static final BlockItemId DEAD_HORN_CORAL_BLOCK = BlockItemId.create("dead_horn_coral_block");
/*     */   
/* 489 */   public static final BlockItemId TUBE_CORAL_BLOCK = BlockItemId.create("tube_coral_block");
/*     */   
/* 490 */   public static final BlockItemId BRAIN_CORAL_BLOCK = BlockItemId.create("brain_coral_block");
/*     */   
/* 491 */   public static final BlockItemId BUBBLE_CORAL_BLOCK = BlockItemId.create("bubble_coral_block");
/*     */   
/* 492 */   public static final BlockItemId FIRE_CORAL_BLOCK = BlockItemId.create("fire_coral_block");
/*     */   
/* 493 */   public static final BlockItemId HORN_CORAL_BLOCK = BlockItemId.create("horn_coral_block");
/*     */   
/* 494 */   public static final BlockItemId TUBE_CORAL = BlockItemId.create("tube_coral");
/*     */   
/* 495 */   public static final BlockItemId BRAIN_CORAL = BlockItemId.create("brain_coral");
/*     */   
/* 496 */   public static final BlockItemId BUBBLE_CORAL = BlockItemId.create("bubble_coral");
/*     */   
/* 497 */   public static final BlockItemId FIRE_CORAL = BlockItemId.create("fire_coral");
/*     */   
/* 498 */   public static final BlockItemId HORN_CORAL = BlockItemId.create("horn_coral");
/*     */   
/* 499 */   public static final BlockItemId DEAD_BRAIN_CORAL = BlockItemId.create("dead_brain_coral");
/*     */   
/* 500 */   public static final BlockItemId DEAD_BUBBLE_CORAL = BlockItemId.create("dead_bubble_coral");
/*     */   
/* 501 */   public static final BlockItemId DEAD_FIRE_CORAL = BlockItemId.create("dead_fire_coral");
/*     */   
/* 502 */   public static final BlockItemId DEAD_HORN_CORAL = BlockItemId.create("dead_horn_coral");
/*     */   
/* 503 */   public static final BlockItemId DEAD_TUBE_CORAL = BlockItemId.create("dead_tube_coral");
/*     */   
/* 504 */   public static final BlockItemId TUBE_CORAL_FAN = BlockItemId.create("tube_coral_fan");
/*     */   
/* 505 */   public static final BlockItemId BRAIN_CORAL_FAN = BlockItemId.create("brain_coral_fan");
/*     */   
/* 506 */   public static final BlockItemId BUBBLE_CORAL_FAN = BlockItemId.create("bubble_coral_fan");
/*     */   
/* 507 */   public static final BlockItemId FIRE_CORAL_FAN = BlockItemId.create("fire_coral_fan");
/*     */   
/* 508 */   public static final BlockItemId HORN_CORAL_FAN = BlockItemId.create("horn_coral_fan");
/*     */   
/* 509 */   public static final BlockItemId DEAD_TUBE_CORAL_FAN = BlockItemId.create("dead_tube_coral_fan");
/*     */   
/* 510 */   public static final BlockItemId DEAD_BRAIN_CORAL_FAN = BlockItemId.create("dead_brain_coral_fan");
/*     */   
/* 511 */   public static final BlockItemId DEAD_BUBBLE_CORAL_FAN = BlockItemId.create("dead_bubble_coral_fan");
/*     */   
/* 512 */   public static final BlockItemId DEAD_FIRE_CORAL_FAN = BlockItemId.create("dead_fire_coral_fan");
/*     */   
/* 513 */   public static final BlockItemId DEAD_HORN_CORAL_FAN = BlockItemId.create("dead_horn_coral_fan");
/*     */   
/* 514 */   public static final BlockItemId BLUE_ICE = BlockItemId.create("blue_ice");
/*     */   
/* 515 */   public static final BlockItemId CONDUIT = BlockItemId.create("conduit");
/*     */   
/* 516 */   public static final BlockItemId POLISHED_GRANITE_STAIRS = BlockItemId.create("polished_granite_stairs");
/*     */   
/* 517 */   public static final BlockItemId SMOOTH_RED_SANDSTONE_STAIRS = BlockItemId.create("smooth_red_sandstone_stairs");
/*     */   
/* 518 */   public static final BlockItemId MOSSY_STONE_BRICK_STAIRS = BlockItemId.create("mossy_stone_brick_stairs");
/*     */   
/* 519 */   public static final BlockItemId POLISHED_DIORITE_STAIRS = BlockItemId.create("polished_diorite_stairs");
/*     */   
/* 520 */   public static final BlockItemId MOSSY_COBBLESTONE_STAIRS = BlockItemId.create("mossy_cobblestone_stairs");
/*     */   
/* 521 */   public static final BlockItemId END_STONE_BRICK_STAIRS = BlockItemId.create("end_stone_brick_stairs");
/*     */   
/* 522 */   public static final BlockItemId STONE_STAIRS = BlockItemId.create("stone_stairs");
/*     */   
/* 523 */   public static final BlockItemId SMOOTH_SANDSTONE_STAIRS = BlockItemId.create("smooth_sandstone_stairs");
/*     */   
/* 524 */   public static final BlockItemId SMOOTH_QUARTZ_STAIRS = BlockItemId.create("smooth_quartz_stairs");
/*     */   
/* 525 */   public static final BlockItemId GRANITE_STAIRS = BlockItemId.create("granite_stairs");
/*     */   
/* 526 */   public static final BlockItemId ANDESITE_STAIRS = BlockItemId.create("andesite_stairs");
/*     */   
/* 527 */   public static final BlockItemId RED_NETHER_BRICK_STAIRS = BlockItemId.create("red_nether_brick_stairs");
/*     */   
/* 528 */   public static final BlockItemId POLISHED_ANDESITE_STAIRS = BlockItemId.create("polished_andesite_stairs");
/*     */   
/* 529 */   public static final BlockItemId DIORITE_STAIRS = BlockItemId.create("diorite_stairs");
/*     */   
/* 530 */   public static final BlockItemId COBBLED_DEEPSLATE_STAIRS = BlockItemId.create("cobbled_deepslate_stairs");
/*     */   
/* 531 */   public static final BlockItemId POLISHED_DEEPSLATE_STAIRS = BlockItemId.create("polished_deepslate_stairs");
/*     */   
/* 532 */   public static final BlockItemId DEEPSLATE_BRICK_STAIRS = BlockItemId.create("deepslate_brick_stairs");
/*     */   
/* 533 */   public static final BlockItemId DEEPSLATE_TILE_STAIRS = BlockItemId.create("deepslate_tile_stairs");
/*     */   
/* 534 */   public static final BlockItemId POLISHED_GRANITE_SLAB = BlockItemId.create("polished_granite_slab");
/*     */   
/* 535 */   public static final BlockItemId SMOOTH_RED_SANDSTONE_SLAB = BlockItemId.create("smooth_red_sandstone_slab");
/*     */   
/* 536 */   public static final BlockItemId MOSSY_STONE_BRICK_SLAB = BlockItemId.create("mossy_stone_brick_slab");
/*     */   
/* 537 */   public static final BlockItemId POLISHED_DIORITE_SLAB = BlockItemId.create("polished_diorite_slab");
/*     */   
/* 538 */   public static final BlockItemId MOSSY_COBBLESTONE_SLAB = BlockItemId.create("mossy_cobblestone_slab");
/*     */   
/* 539 */   public static final BlockItemId END_STONE_BRICK_SLAB = BlockItemId.create("end_stone_brick_slab");
/*     */   
/* 540 */   public static final BlockItemId SMOOTH_SANDSTONE_SLAB = BlockItemId.create("smooth_sandstone_slab");
/*     */   
/* 541 */   public static final BlockItemId SMOOTH_QUARTZ_SLAB = BlockItemId.create("smooth_quartz_slab");
/*     */   
/* 542 */   public static final BlockItemId GRANITE_SLAB = BlockItemId.create("granite_slab");
/*     */   
/* 543 */   public static final BlockItemId ANDESITE_SLAB = BlockItemId.create("andesite_slab");
/*     */   
/* 544 */   public static final BlockItemId RED_NETHER_BRICK_SLAB = BlockItemId.create("red_nether_brick_slab");
/*     */   
/* 545 */   public static final BlockItemId POLISHED_ANDESITE_SLAB = BlockItemId.create("polished_andesite_slab");
/*     */   
/* 546 */   public static final BlockItemId DIORITE_SLAB = BlockItemId.create("diorite_slab");
/*     */   
/* 547 */   public static final BlockItemId COBBLED_DEEPSLATE_SLAB = BlockItemId.create("cobbled_deepslate_slab");
/*     */   
/* 548 */   public static final BlockItemId POLISHED_DEEPSLATE_SLAB = BlockItemId.create("polished_deepslate_slab");
/*     */   
/* 549 */   public static final BlockItemId DEEPSLATE_BRICK_SLAB = BlockItemId.create("deepslate_brick_slab");
/*     */   
/* 550 */   public static final BlockItemId DEEPSLATE_TILE_SLAB = BlockItemId.create("deepslate_tile_slab");
/*     */   
/* 551 */   public static final BlockItemId SCAFFOLDING = BlockItemId.create("scaffolding");
/*     */   
/* 552 */   public static final BlockItemId REDSTONE_TORCH = BlockItemId.create("redstone_torch");
/*     */   
/* 553 */   public static final BlockItemId REDSTONE_BLOCK = BlockItemId.create("redstone_block");
/*     */   
/* 554 */   public static final BlockItemId REPEATER = BlockItemId.create("repeater");
/*     */   
/* 555 */   public static final BlockItemId COMPARATOR = BlockItemId.create("comparator");
/*     */   
/* 556 */   public static final BlockItemId PISTON = BlockItemId.create("piston");
/*     */   
/* 557 */   public static final BlockItemId STICKY_PISTON = BlockItemId.create("sticky_piston");
/*     */   
/* 558 */   public static final BlockItemId SLIME_BLOCK = BlockItemId.create("slime_block");
/*     */   
/* 559 */   public static final BlockItemId HONEY_BLOCK = BlockItemId.create("honey_block");
/*     */   
/* 560 */   public static final BlockItemId OBSERVER = BlockItemId.create("observer");
/*     */   
/* 561 */   public static final BlockItemId HOPPER = BlockItemId.create("hopper");
/*     */   
/* 562 */   public static final BlockItemId DISPENSER = BlockItemId.create("dispenser");
/*     */   
/* 563 */   public static final BlockItemId DROPPER = BlockItemId.create("dropper");
/*     */   
/* 564 */   public static final BlockItemId LECTERN = BlockItemId.create("lectern");
/*     */   
/* 565 */   public static final BlockItemId TARGET = BlockItemId.create("target");
/*     */   
/* 566 */   public static final BlockItemId LEVER = BlockItemId.create("lever");
/*     */   
/* 567 */   public static final BlockItemId DAYLIGHT_DETECTOR = BlockItemId.create("daylight_detector");
/*     */   
/* 568 */   public static final BlockItemId SCULK_SENSOR = BlockItemId.create("sculk_sensor");
/*     */   
/* 569 */   public static final BlockItemId CALIBRATED_SCULK_SENSOR = BlockItemId.create("calibrated_sculk_sensor");
/*     */   
/* 570 */   public static final BlockItemId TRIPWIRE_HOOK = BlockItemId.create("tripwire_hook");
/*     */   
/* 571 */   public static final BlockItemId TRAPPED_CHEST = BlockItemId.create("trapped_chest");
/*     */   
/* 572 */   public static final BlockItemId TNT = BlockItemId.create("tnt");
/*     */   
/* 573 */   public static final BlockItemId REDSTONE_LAMP = BlockItemId.create("redstone_lamp");
/*     */   
/* 574 */   public static final BlockItemId NOTE_BLOCK = BlockItemId.create("note_block");
/*     */   
/* 575 */   public static final BlockItemId STONE_BUTTON = BlockItemId.create("stone_button");
/*     */   
/* 576 */   public static final BlockItemId POLISHED_BLACKSTONE_BUTTON = BlockItemId.create("polished_blackstone_button");
/*     */   
/* 577 */   public static final BlockItemId OAK_BUTTON = BlockItemId.create("oak_button");
/*     */   
/* 578 */   public static final BlockItemId SPRUCE_BUTTON = BlockItemId.create("spruce_button");
/*     */   
/* 579 */   public static final BlockItemId BIRCH_BUTTON = BlockItemId.create("birch_button");
/*     */   
/* 580 */   public static final BlockItemId JUNGLE_BUTTON = BlockItemId.create("jungle_button");
/*     */   
/* 581 */   public static final BlockItemId ACACIA_BUTTON = BlockItemId.create("acacia_button");
/*     */   
/* 582 */   public static final BlockItemId CHERRY_BUTTON = BlockItemId.create("cherry_button");
/*     */   
/* 583 */   public static final BlockItemId DARK_OAK_BUTTON = BlockItemId.create("dark_oak_button");
/*     */   
/* 584 */   public static final BlockItemId PALE_OAK_BUTTON = BlockItemId.create("pale_oak_button");
/*     */   
/* 585 */   public static final BlockItemId MANGROVE_BUTTON = BlockItemId.create("mangrove_button");
/*     */   
/* 586 */   public static final BlockItemId BAMBOO_BUTTON = BlockItemId.create("bamboo_button");
/*     */   
/* 587 */   public static final BlockItemId CRIMSON_BUTTON = BlockItemId.create("crimson_button");
/*     */   
/* 588 */   public static final BlockItemId WARPED_BUTTON = BlockItemId.create("warped_button");
/*     */   
/* 589 */   public static final BlockItemId STONE_PRESSURE_PLATE = BlockItemId.create("stone_pressure_plate");
/*     */   
/* 590 */   public static final BlockItemId POLISHED_BLACKSTONE_PRESSURE_PLATE = BlockItemId.create("polished_blackstone_pressure_plate");
/*     */   
/* 591 */   public static final BlockItemId LIGHT_WEIGHTED_PRESSURE_PLATE = BlockItemId.create("light_weighted_pressure_plate");
/*     */   
/* 592 */   public static final BlockItemId HEAVY_WEIGHTED_PRESSURE_PLATE = BlockItemId.create("heavy_weighted_pressure_plate");
/*     */   
/* 593 */   public static final BlockItemId OAK_PRESSURE_PLATE = BlockItemId.create("oak_pressure_plate");
/*     */   
/* 594 */   public static final BlockItemId SPRUCE_PRESSURE_PLATE = BlockItemId.create("spruce_pressure_plate");
/*     */   
/* 595 */   public static final BlockItemId BIRCH_PRESSURE_PLATE = BlockItemId.create("birch_pressure_plate");
/*     */   
/* 596 */   public static final BlockItemId JUNGLE_PRESSURE_PLATE = BlockItemId.create("jungle_pressure_plate");
/*     */   
/* 597 */   public static final BlockItemId ACACIA_PRESSURE_PLATE = BlockItemId.create("acacia_pressure_plate");
/*     */   
/* 598 */   public static final BlockItemId CHERRY_PRESSURE_PLATE = BlockItemId.create("cherry_pressure_plate");
/*     */   
/* 599 */   public static final BlockItemId DARK_OAK_PRESSURE_PLATE = BlockItemId.create("dark_oak_pressure_plate");
/*     */   
/* 600 */   public static final BlockItemId PALE_OAK_PRESSURE_PLATE = BlockItemId.create("pale_oak_pressure_plate");
/*     */   
/* 601 */   public static final BlockItemId MANGROVE_PRESSURE_PLATE = BlockItemId.create("mangrove_pressure_plate");
/*     */   
/* 602 */   public static final BlockItemId BAMBOO_PRESSURE_PLATE = BlockItemId.create("bamboo_pressure_plate");
/*     */   
/* 603 */   public static final BlockItemId CRIMSON_PRESSURE_PLATE = BlockItemId.create("crimson_pressure_plate");
/*     */   
/* 604 */   public static final BlockItemId WARPED_PRESSURE_PLATE = BlockItemId.create("warped_pressure_plate");
/*     */   
/* 605 */   public static final BlockItemId IRON_DOOR = BlockItemId.create("iron_door");
/*     */   
/* 606 */   public static final BlockItemId OAK_DOOR = BlockItemId.create("oak_door");
/*     */   
/* 607 */   public static final BlockItemId SPRUCE_DOOR = BlockItemId.create("spruce_door");
/*     */   
/* 608 */   public static final BlockItemId BIRCH_DOOR = BlockItemId.create("birch_door");
/*     */   
/* 609 */   public static final BlockItemId JUNGLE_DOOR = BlockItemId.create("jungle_door");
/*     */   
/* 610 */   public static final BlockItemId ACACIA_DOOR = BlockItemId.create("acacia_door");
/*     */   
/* 611 */   public static final BlockItemId CHERRY_DOOR = BlockItemId.create("cherry_door");
/*     */   
/* 612 */   public static final BlockItemId DARK_OAK_DOOR = BlockItemId.create("dark_oak_door");
/*     */   
/* 613 */   public static final BlockItemId PALE_OAK_DOOR = BlockItemId.create("pale_oak_door");
/*     */   
/* 614 */   public static final BlockItemId MANGROVE_DOOR = BlockItemId.create("mangrove_door");
/*     */   
/* 615 */   public static final BlockItemId BAMBOO_DOOR = BlockItemId.create("bamboo_door");
/*     */   
/* 616 */   public static final BlockItemId CRIMSON_DOOR = BlockItemId.create("crimson_door");
/*     */   
/* 617 */   public static final BlockItemId WARPED_DOOR = BlockItemId.create("warped_door");
/*     */   
/* 618 */   public static final BlockItemId IRON_TRAPDOOR = BlockItemId.create("iron_trapdoor");
/*     */   
/* 619 */   public static final BlockItemId OAK_TRAPDOOR = BlockItemId.create("oak_trapdoor");
/*     */   
/* 620 */   public static final BlockItemId SPRUCE_TRAPDOOR = BlockItemId.create("spruce_trapdoor");
/*     */   
/* 621 */   public static final BlockItemId BIRCH_TRAPDOOR = BlockItemId.create("birch_trapdoor");
/*     */   
/* 622 */   public static final BlockItemId JUNGLE_TRAPDOOR = BlockItemId.create("jungle_trapdoor");
/*     */   
/* 623 */   public static final BlockItemId ACACIA_TRAPDOOR = BlockItemId.create("acacia_trapdoor");
/*     */   
/* 624 */   public static final BlockItemId CHERRY_TRAPDOOR = BlockItemId.create("cherry_trapdoor");
/*     */   
/* 625 */   public static final BlockItemId DARK_OAK_TRAPDOOR = BlockItemId.create("dark_oak_trapdoor");
/*     */   
/* 626 */   public static final BlockItemId PALE_OAK_TRAPDOOR = BlockItemId.create("pale_oak_trapdoor");
/*     */   
/* 627 */   public static final BlockItemId MANGROVE_TRAPDOOR = BlockItemId.create("mangrove_trapdoor");
/*     */   
/* 628 */   public static final BlockItemId BAMBOO_TRAPDOOR = BlockItemId.create("bamboo_trapdoor");
/*     */   
/* 629 */   public static final BlockItemId CRIMSON_TRAPDOOR = BlockItemId.create("crimson_trapdoor");
/*     */   
/* 630 */   public static final BlockItemId WARPED_TRAPDOOR = BlockItemId.create("warped_trapdoor");
/*     */   
/* 631 */   public static final BlockItemId OAK_FENCE_GATE = BlockItemId.create("oak_fence_gate");
/*     */   
/* 632 */   public static final BlockItemId SPRUCE_FENCE_GATE = BlockItemId.create("spruce_fence_gate");
/*     */   
/* 633 */   public static final BlockItemId BIRCH_FENCE_GATE = BlockItemId.create("birch_fence_gate");
/*     */   
/* 634 */   public static final BlockItemId JUNGLE_FENCE_GATE = BlockItemId.create("jungle_fence_gate");
/*     */   
/* 635 */   public static final BlockItemId ACACIA_FENCE_GATE = BlockItemId.create("acacia_fence_gate");
/*     */   
/* 636 */   public static final BlockItemId CHERRY_FENCE_GATE = BlockItemId.create("cherry_fence_gate");
/*     */   
/* 637 */   public static final BlockItemId DARK_OAK_FENCE_GATE = BlockItemId.create("dark_oak_fence_gate");
/*     */   
/* 638 */   public static final BlockItemId PALE_OAK_FENCE_GATE = BlockItemId.create("pale_oak_fence_gate");
/*     */   
/* 639 */   public static final BlockItemId MANGROVE_FENCE_GATE = BlockItemId.create("mangrove_fence_gate");
/*     */   
/* 640 */   public static final BlockItemId BAMBOO_FENCE_GATE = BlockItemId.create("bamboo_fence_gate");
/*     */   
/* 641 */   public static final BlockItemId CRIMSON_FENCE_GATE = BlockItemId.create("crimson_fence_gate");
/*     */   
/* 642 */   public static final BlockItemId WARPED_FENCE_GATE = BlockItemId.create("warped_fence_gate");
/*     */   
/* 643 */   public static final BlockItemId POWERED_RAIL = BlockItemId.create("powered_rail");
/*     */   
/* 644 */   public static final BlockItemId DETECTOR_RAIL = BlockItemId.create("detector_rail");
/*     */   
/* 645 */   public static final BlockItemId RAIL = BlockItemId.create("rail");
/*     */   
/* 646 */   public static final BlockItemId ACTIVATOR_RAIL = BlockItemId.create("activator_rail");
/*     */   
/* 647 */   public static final BlockItemId STRUCTURE_BLOCK = BlockItemId.create("structure_block");
/*     */   
/* 648 */   public static final BlockItemId JIGSAW = BlockItemId.create("jigsaw");
/*     */   
/* 649 */   public static final BlockItemId TEST_BLOCK = BlockItemId.create("test_block");
/*     */   
/* 650 */   public static final BlockItemId TEST_INSTANCE_BLOCK = BlockItemId.create("test_instance_block");
/*     */   
/* 651 */   public static final BlockItemId OAK_SIGN = BlockItemId.create("oak_sign");
/*     */   
/* 652 */   public static final BlockItemId SPRUCE_SIGN = BlockItemId.create("spruce_sign");
/*     */   
/* 653 */   public static final BlockItemId BIRCH_SIGN = BlockItemId.create("birch_sign");
/*     */   
/* 654 */   public static final BlockItemId JUNGLE_SIGN = BlockItemId.create("jungle_sign");
/*     */   
/* 655 */   public static final BlockItemId ACACIA_SIGN = BlockItemId.create("acacia_sign");
/*     */   
/* 656 */   public static final BlockItemId CHERRY_SIGN = BlockItemId.create("cherry_sign");
/*     */   
/* 657 */   public static final BlockItemId DARK_OAK_SIGN = BlockItemId.create("dark_oak_sign");
/*     */   
/* 658 */   public static final BlockItemId PALE_OAK_SIGN = BlockItemId.create("pale_oak_sign");
/*     */   
/* 659 */   public static final BlockItemId MANGROVE_SIGN = BlockItemId.create("mangrove_sign");
/*     */   
/* 660 */   public static final BlockItemId BAMBOO_SIGN = BlockItemId.create("bamboo_sign");
/*     */   
/* 661 */   public static final BlockItemId CRIMSON_SIGN = BlockItemId.create("crimson_sign");
/*     */   
/* 662 */   public static final BlockItemId WARPED_SIGN = BlockItemId.create("warped_sign");
/*     */   
/* 663 */   public static final BlockItemId OAK_HANGING_SIGN = BlockItemId.create("oak_hanging_sign");
/*     */   
/* 664 */   public static final BlockItemId SPRUCE_HANGING_SIGN = BlockItemId.create("spruce_hanging_sign");
/*     */   
/* 665 */   public static final BlockItemId BIRCH_HANGING_SIGN = BlockItemId.create("birch_hanging_sign");
/*     */   
/* 666 */   public static final BlockItemId JUNGLE_HANGING_SIGN = BlockItemId.create("jungle_hanging_sign");
/*     */   
/* 667 */   public static final BlockItemId ACACIA_HANGING_SIGN = BlockItemId.create("acacia_hanging_sign");
/*     */   
/* 668 */   public static final BlockItemId CHERRY_HANGING_SIGN = BlockItemId.create("cherry_hanging_sign");
/*     */   
/* 669 */   public static final BlockItemId DARK_OAK_HANGING_SIGN = BlockItemId.create("dark_oak_hanging_sign");
/*     */   
/* 670 */   public static final BlockItemId PALE_OAK_HANGING_SIGN = BlockItemId.create("pale_oak_hanging_sign");
/*     */   
/* 671 */   public static final BlockItemId MANGROVE_HANGING_SIGN = BlockItemId.create("mangrove_hanging_sign");
/*     */   
/* 672 */   public static final BlockItemId BAMBOO_HANGING_SIGN = BlockItemId.create("bamboo_hanging_sign");
/*     */   
/* 673 */   public static final BlockItemId CRIMSON_HANGING_SIGN = BlockItemId.create("crimson_hanging_sign");
/*     */   
/* 674 */   public static final BlockItemId WARPED_HANGING_SIGN = BlockItemId.create("warped_hanging_sign");
/*     */   
/* 675 */   public static final BlockItemId DRIED_KELP_BLOCK = BlockItemId.create("dried_kelp_block");
/*     */   
/* 676 */   public static final BlockItemId CAKE = BlockItemId.create("cake");
/*     */   
/* 677 */   public static final BlockItemId CRAFTER = BlockItemId.create("crafter");
/*     */   
/* 678 */   public static final BlockItemId NETHER_WART = BlockItemId.create("nether_wart");
/*     */   
/* 679 */   public static final BlockItemId BREWING_STAND = BlockItemId.create("brewing_stand");
/*     */   
/* 680 */   public static final BlockItemId CAULDRON = BlockItemId.create("cauldron");
/*     */   
/* 681 */   public static final BlockItemId FLOWER_POT = BlockItemId.create("flower_pot");
/*     */   
/* 682 */   public static final BlockItemId SKELETON_SKULL = BlockItemId.create("skeleton_skull");
/*     */   
/* 683 */   public static final BlockItemId WITHER_SKELETON_SKULL = BlockItemId.create("wither_skeleton_skull");
/*     */   
/* 684 */   public static final BlockItemId PLAYER_HEAD = BlockItemId.create("player_head");
/*     */   
/* 685 */   public static final BlockItemId ZOMBIE_HEAD = BlockItemId.create("zombie_head");
/*     */   
/* 686 */   public static final BlockItemId CREEPER_HEAD = BlockItemId.create("creeper_head");
/*     */   
/* 687 */   public static final BlockItemId DRAGON_HEAD = BlockItemId.create("dragon_head");
/*     */   
/* 688 */   public static final BlockItemId PIGLIN_HEAD = BlockItemId.create("piglin_head");
/*     */   
/* 689 */   public static final BlockItemId LOOM = BlockItemId.create("loom");
/*     */   
/* 690 */   public static final BlockItemId COMPOSTER = BlockItemId.create("composter");
/*     */   
/* 691 */   public static final BlockItemId BARREL = BlockItemId.create("barrel");
/*     */   
/* 692 */   public static final BlockItemId SMOKER = BlockItemId.create("smoker");
/*     */   
/* 693 */   public static final BlockItemId BLAST_FURNACE = BlockItemId.create("blast_furnace");
/*     */   
/* 694 */   public static final BlockItemId CARTOGRAPHY_TABLE = BlockItemId.create("cartography_table");
/*     */   
/* 695 */   public static final BlockItemId FLETCHING_TABLE = BlockItemId.create("fletching_table");
/*     */   
/* 696 */   public static final BlockItemId GRINDSTONE = BlockItemId.create("grindstone");
/*     */   
/* 697 */   public static final BlockItemId SMITHING_TABLE = BlockItemId.create("smithing_table");
/*     */   
/* 698 */   public static final BlockItemId STONECUTTER = BlockItemId.create("stonecutter");
/*     */   
/* 699 */   public static final BlockItemId BELL = BlockItemId.create("bell");
/*     */   
/* 700 */   public static final BlockItemId LANTERN = BlockItemId.create("lantern");
/*     */   
/* 701 */   public static final BlockItemId SOUL_LANTERN = BlockItemId.create("soul_lantern");
/*     */   
/* 702 */   public static final BlockItemId CAMPFIRE = BlockItemId.create("campfire");
/*     */   
/* 703 */   public static final BlockItemId SOUL_CAMPFIRE = BlockItemId.create("soul_campfire");
/*     */   
/* 704 */   public static final BlockItemId SHROOMLIGHT = BlockItemId.create("shroomlight");
/*     */   
/* 705 */   public static final BlockItemId BEE_NEST = BlockItemId.create("bee_nest");
/*     */   
/* 706 */   public static final BlockItemId BEEHIVE = BlockItemId.create("beehive");
/*     */   
/* 707 */   public static final BlockItemId HONEYCOMB_BLOCK = BlockItemId.create("honeycomb_block");
/*     */   
/* 708 */   public static final BlockItemId LODESTONE = BlockItemId.create("lodestone");
/*     */   
/* 709 */   public static final BlockItemId CRYING_OBSIDIAN = BlockItemId.create("crying_obsidian");
/*     */   
/* 710 */   public static final BlockItemId BLACKSTONE = BlockItemId.create("blackstone");
/*     */   
/* 711 */   public static final BlockItemId BLACKSTONE_SLAB = BlockItemId.create("blackstone_slab");
/*     */   
/* 712 */   public static final BlockItemId BLACKSTONE_STAIRS = BlockItemId.create("blackstone_stairs");
/*     */   
/* 713 */   public static final BlockItemId GILDED_BLACKSTONE = BlockItemId.create("gilded_blackstone");
/*     */   
/* 714 */   public static final BlockItemId POLISHED_BLACKSTONE = BlockItemId.create("polished_blackstone");
/*     */   
/* 715 */   public static final BlockItemId POLISHED_BLACKSTONE_SLAB = BlockItemId.create("polished_blackstone_slab");
/*     */   
/* 716 */   public static final BlockItemId POLISHED_BLACKSTONE_STAIRS = BlockItemId.create("polished_blackstone_stairs");
/*     */   
/* 717 */   public static final BlockItemId CHISELED_POLISHED_BLACKSTONE = BlockItemId.create("chiseled_polished_blackstone");
/*     */   
/* 718 */   public static final BlockItemId POLISHED_BLACKSTONE_BRICKS = BlockItemId.create("polished_blackstone_bricks");
/*     */   
/* 719 */   public static final BlockItemId POLISHED_BLACKSTONE_BRICK_SLAB = BlockItemId.create("polished_blackstone_brick_slab");
/*     */   
/* 720 */   public static final BlockItemId POLISHED_BLACKSTONE_BRICK_STAIRS = BlockItemId.create("polished_blackstone_brick_stairs");
/*     */   
/* 721 */   public static final BlockItemId CRACKED_POLISHED_BLACKSTONE_BRICKS = BlockItemId.create("cracked_polished_blackstone_bricks");
/*     */   
/* 722 */   public static final BlockItemId RESPAWN_ANCHOR = BlockItemId.create("respawn_anchor");
/*     */   
/* 723 */   public static final BlockItemId CANDLE = BlockItemId.create("candle");
/*     */   
/* 724 */   public static final BlockItemId SMALL_AMETHYST_BUD = BlockItemId.create("small_amethyst_bud");
/*     */   
/* 725 */   public static final BlockItemId MEDIUM_AMETHYST_BUD = BlockItemId.create("medium_amethyst_bud");
/*     */   
/* 726 */   public static final BlockItemId LARGE_AMETHYST_BUD = BlockItemId.create("large_amethyst_bud");
/*     */   
/* 727 */   public static final BlockItemId AMETHYST_CLUSTER = BlockItemId.create("amethyst_cluster");
/*     */   
/* 728 */   public static final BlockItemId POINTED_DRIPSTONE = BlockItemId.create("pointed_dripstone");
/*     */   
/* 729 */   public static final BlockItemId SULFUR_SPIKE = BlockItemId.create("sulfur_spike");
/*     */   
/* 730 */   public static final BlockItemId OCHRE_FROGLIGHT = BlockItemId.create("ochre_froglight");
/*     */   
/* 731 */   public static final BlockItemId VERDANT_FROGLIGHT = BlockItemId.create("verdant_froglight");
/*     */   
/* 732 */   public static final BlockItemId PEARLESCENT_FROGLIGHT = BlockItemId.create("pearlescent_froglight");
/*     */   
/* 733 */   public static final BlockItemId FROGSPAWN = BlockItemId.create("frogspawn");
/*     */   
/* 734 */   public static final BlockItemId TRIAL_SPAWNER = BlockItemId.create("trial_spawner");
/*     */   
/* 735 */   public static final BlockItemId VAULT = BlockItemId.create("vault");
/*     */   
/* 738 */   public static final BlockItemId REDSTONE_DUST = BlockItemId.create("redstone_wire", "redstone");
/*     */   
/* 739 */   public static final BlockItemId TRIPWIRE = BlockItemId.create("tripwire", "string");
/*     */   
/* 740 */   public static final BlockItemId POWDER_SNOW = BlockItemId.create("powder_snow", "powder_snow_bucket");
/*     */   
/* 743 */   public static final BlockItemId WHEAT_CROP = BlockItemId.create("wheat", "wheat_seeds");
/*     */   
/* 744 */   public static final BlockItemId COCOA_CROP = BlockItemId.create("cocoa", "cocoa_beans");
/*     */   
/* 745 */   public static final BlockItemId CARROT_CROP = BlockItemId.create("carrots", "carrot");
/*     */   
/* 746 */   public static final BlockItemId POTATO_CROP = BlockItemId.create("potatoes", "potato");
/*     */   
/* 747 */   public static final BlockItemId TORCHFLOWER_CROP = BlockItemId.create("torchflower_crop", "torchflower_seeds");
/*     */   
/* 748 */   public static final BlockItemId PITCHER_CROP = BlockItemId.create("pitcher_crop", "pitcher_pod");
/*     */   
/* 749 */   public static final BlockItemId BEETROOT_CROP = BlockItemId.create("beetroots", "beetroot_seeds");
/*     */   
/* 750 */   public static final BlockItemId SWEET_BERRY_CROP = BlockItemId.create("sweet_berry_bush", "sweet_berries");
/*     */   
/* 751 */   public static final BlockItemId GLOW_BERRY_CROP = BlockItemId.create("cave_vines", "glow_berries");
/*     */   
/* 752 */   public static final BlockItemId PUMPKIN_CROP = BlockItemId.create("pumpkin_stem", "pumpkin_seeds");
/*     */   
/* 753 */   public static final BlockItemId MELON_CROP = BlockItemId.create("melon_stem", "melon_seeds");
/*     */   
/* 755 */   public static final ColorCollection<BlockItemId> BED = createSimpleColored("bed");
/*     */   
/* 756 */   public static final ColorCollection<BlockItemId> WOOL = createSimpleColored("wool");
/*     */   
/* 757 */   public static final ColorCollection<BlockItemId> STAINED_GLASS = createSimpleColored("stained_glass");
/*     */   
/* 758 */   public static final ColorCollection<BlockItemId> DYED_TERRACOTTA = createSimpleColored("terracotta");
/*     */   
/* 759 */   public static final ColorCollection<BlockItemId> STAINED_GLASS_PANE = createSimpleColored("stained_glass_pane");
/*     */   
/* 760 */   public static final ColorCollection<BlockItemId> CARPET = createSimpleColored("carpet");
/*     */   
/* 761 */   public static final ColorCollection<BlockItemId> BANNER = createSimpleColored("banner");
/*     */   
/* 762 */   public static final ColorCollection<BlockItemId> DYED_SHULKER_BOX = createSimpleColored("shulker_box");
/*     */   
/* 763 */   public static final ColorCollection<BlockItemId> GLAZED_TERRACOTTA = createSimpleColored("glazed_terracotta");
/*     */   
/* 764 */   public static final ColorCollection<BlockItemId> CONCRETE = createSimpleColored("concrete");
/*     */   
/* 765 */   public static final ColorCollection<BlockItemId> CONCRETE_POWDER = createSimpleColored("concrete_powder");
/*     */   
/* 766 */   public static final ColorCollection<BlockItemId> DYED_CANDLE = createSimpleColored("candle");
/*     */   
/* 768 */   private static final WeatheringCopperCollection.ByState<String> COPPER_BLOCK_SPECIAL_NAMES = new WeatheringCopperCollection.ByState("copper_block", "copper", "copper", "copper");
/*     */   
/* 775 */   public static final WeatheringCopperCollection<BlockItemId> COPPER_BLOCK = WeatheringCopperCollection.prefixWithState(WeatheringCopperCollection.same(COPPER_BLOCK_SPECIAL_NAMES)).map(BlockItemId::create);
/*     */   
/* 776 */   public static final WeatheringCopperCollection<BlockItemId> COPPER_BARS = createSimpleCopper("copper_bars");
/*     */   
/* 777 */   public static final WeatheringCopperCollection<BlockItemId> COPPER_CHAIN = createSimpleCopper("copper_chain");
/*     */   
/* 778 */   public static final WeatheringCopperCollection<BlockItemId> COPPER_LANTERN = createSimpleCopper("copper_lantern");
/*     */   
/* 779 */   public static final WeatheringCopperCollection<BlockItemId> CUT_COPPER = createSimpleCopper("cut_copper");
/*     */   
/* 780 */   public static final WeatheringCopperCollection<BlockItemId> CHISELED_COPPER = createSimpleCopper("chiseled_copper");
/*     */   
/* 781 */   public static final WeatheringCopperCollection<BlockItemId> CUT_COPPER_STAIRS = createSimpleCopper("cut_copper_stairs");
/*     */   
/* 782 */   public static final WeatheringCopperCollection<BlockItemId> CUT_COPPER_SLAB = createSimpleCopper("cut_copper_slab");
/*     */   
/* 783 */   public static final WeatheringCopperCollection<BlockItemId> COPPER_DOOR = createSimpleCopper("copper_door");
/*     */   
/* 784 */   public static final WeatheringCopperCollection<BlockItemId> COPPER_TRAPDOOR = createSimpleCopper("copper_trapdoor");
/*     */   
/* 785 */   public static final WeatheringCopperCollection<BlockItemId> COPPER_GRATE = createSimpleCopper("copper_grate");
/*     */   
/* 786 */   public static final WeatheringCopperCollection<BlockItemId> COPPER_BULB = createSimpleCopper("copper_bulb");
/*     */   
/* 787 */   public static final WeatheringCopperCollection<BlockItemId> COPPER_CHEST = createSimpleCopper("copper_chest");
/*     */   
/* 788 */   public static final WeatheringCopperCollection<BlockItemId> COPPER_GOLEM_STATUE = createSimpleCopper("copper_golem_statue");
/*     */   
/* 789 */   public static final WeatheringCopperCollection<BlockItemId> LIGHTNING_ROD = createSimpleCopper("lightning_rod");
/*     */   
/*     */   private static ColorCollection<BlockItemId> createSimpleColored(String baseName) {
/* 792 */     return ColorCollection.prefixWithColor(ColorCollection.create(baseName)).map(BlockItemId::create);
/*     */   }
/*     */   
/*     */   private static WeatheringCopperCollection<BlockItemId> createSimpleCopper(String baseName) {
/* 796 */     return WeatheringCopperCollection.prefixWithState(WeatheringCopperCollection.create(baseName)).map(BlockItemId::create);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\references\BlockItemIds.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */