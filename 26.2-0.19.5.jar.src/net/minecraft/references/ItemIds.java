/*     */ package net.minecraft.references;
/*     */ 
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypeIds;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.JukeboxSong;
/*     */ import net.minecraft.world.item.JukeboxSongs;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPatterns;
/*     */ import net.minecraft.world.level.block.ColorCollection;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotPattern;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
/*     */ 
/*     */ public class ItemIds {
/*  18 */   public static final ResourceKey<Item> SADDLE = create("saddle");
/*     */   
/*  19 */   public static final ResourceKey<Item> MINECART = create("minecart");
/*     */   
/*  20 */   public static final ResourceKey<Item> CHEST_MINECART = create("chest_minecart");
/*     */   
/*  21 */   public static final ResourceKey<Item> FURNACE_MINECART = create("furnace_minecart");
/*     */   
/*  22 */   public static final ResourceKey<Item> TNT_MINECART = create("tnt_minecart");
/*     */   
/*  23 */   public static final ResourceKey<Item> HOPPER_MINECART = create("hopper_minecart");
/*     */   
/*  24 */   public static final ResourceKey<Item> CARROT_ON_A_STICK = create("carrot_on_a_stick");
/*     */   
/*  25 */   public static final ResourceKey<Item> WARPED_FUNGUS_ON_A_STICK = create("warped_fungus_on_a_stick");
/*     */   
/*  26 */   public static final ResourceKey<Item> PHANTOM_MEMBRANE = create("phantom_membrane");
/*     */   
/*  27 */   public static final ResourceKey<Item> ELYTRA = create("elytra");
/*     */   
/*  28 */   public static final ResourceKey<Item> OAK_BOAT = create("oak_boat");
/*     */   
/*  29 */   public static final ResourceKey<Item> OAK_CHEST_BOAT = create("oak_chest_boat");
/*     */   
/*  30 */   public static final ResourceKey<Item> SPRUCE_BOAT = create("spruce_boat");
/*     */   
/*  31 */   public static final ResourceKey<Item> SPRUCE_CHEST_BOAT = create("spruce_chest_boat");
/*     */   
/*  32 */   public static final ResourceKey<Item> BIRCH_BOAT = create("birch_boat");
/*     */   
/*  33 */   public static final ResourceKey<Item> BIRCH_CHEST_BOAT = create("birch_chest_boat");
/*     */   
/*  34 */   public static final ResourceKey<Item> JUNGLE_BOAT = create("jungle_boat");
/*     */   
/*  35 */   public static final ResourceKey<Item> JUNGLE_CHEST_BOAT = create("jungle_chest_boat");
/*     */   
/*  36 */   public static final ResourceKey<Item> ACACIA_BOAT = create("acacia_boat");
/*     */   
/*  37 */   public static final ResourceKey<Item> ACACIA_CHEST_BOAT = create("acacia_chest_boat");
/*     */   
/*  38 */   public static final ResourceKey<Item> CHERRY_BOAT = create("cherry_boat");
/*     */   
/*  39 */   public static final ResourceKey<Item> CHERRY_CHEST_BOAT = create("cherry_chest_boat");
/*     */   
/*  40 */   public static final ResourceKey<Item> DARK_OAK_BOAT = create("dark_oak_boat");
/*     */   
/*  41 */   public static final ResourceKey<Item> DARK_OAK_CHEST_BOAT = create("dark_oak_chest_boat");
/*     */   
/*  42 */   public static final ResourceKey<Item> PALE_OAK_BOAT = create("pale_oak_boat");
/*     */   
/*  43 */   public static final ResourceKey<Item> PALE_OAK_CHEST_BOAT = create("pale_oak_chest_boat");
/*     */   
/*  44 */   public static final ResourceKey<Item> MANGROVE_BOAT = create("mangrove_boat");
/*     */   
/*  45 */   public static final ResourceKey<Item> MANGROVE_CHEST_BOAT = create("mangrove_chest_boat");
/*     */   
/*  46 */   public static final ResourceKey<Item> BAMBOO_RAFT = create("bamboo_raft");
/*     */   
/*  47 */   public static final ResourceKey<Item> BAMBOO_CHEST_RAFT = create("bamboo_chest_raft");
/*     */   
/*  48 */   public static final ResourceKey<Item> TURTLE_HELMET = create("turtle_helmet");
/*     */   
/*  49 */   public static final ResourceKey<Item> TURTLE_SCUTE = create("turtle_scute");
/*     */   
/*  50 */   public static final ResourceKey<Item> ARMADILLO_SCUTE = create("armadillo_scute");
/*     */   
/*  51 */   public static final ResourceKey<Item> WOLF_ARMOR = create("wolf_armor");
/*     */   
/*  52 */   public static final ResourceKey<Item> FLINT_AND_STEEL = create("flint_and_steel");
/*     */   
/*  53 */   public static final ResourceKey<Item> BOWL = create("bowl");
/*     */   
/*  54 */   public static final ResourceKey<Item> APPLE = create("apple");
/*     */   
/*  55 */   public static final ResourceKey<Item> BOW = create("bow");
/*     */   
/*  56 */   public static final ResourceKey<Item> ARROW = create("arrow");
/*     */   
/*  57 */   public static final ResourceKey<Item> COAL = create("coal");
/*     */   
/*  58 */   public static final ResourceKey<Item> CHARCOAL = create("charcoal");
/*     */   
/*  59 */   public static final ResourceKey<Item> DIAMOND = create("diamond");
/*     */   
/*  60 */   public static final ResourceKey<Item> EMERALD = create("emerald");
/*     */   
/*  61 */   public static final ResourceKey<Item> LAPIS_LAZULI = create("lapis_lazuli");
/*     */   
/*  62 */   public static final ResourceKey<Item> QUARTZ = create("quartz");
/*     */   
/*  63 */   public static final ResourceKey<Item> AMETHYST_SHARD = create("amethyst_shard");
/*     */   
/*  64 */   public static final ResourceKey<Item> RAW_IRON = create("raw_iron");
/*     */   
/*  65 */   public static final ResourceKey<Item> IRON_INGOT = create("iron_ingot");
/*     */   
/*  66 */   public static final ResourceKey<Item> RAW_COPPER = create("raw_copper");
/*     */   
/*  67 */   public static final ResourceKey<Item> COPPER_INGOT = create("copper_ingot");
/*     */   
/*  68 */   public static final ResourceKey<Item> RAW_GOLD = create("raw_gold");
/*     */   
/*  69 */   public static final ResourceKey<Item> GOLD_INGOT = create("gold_ingot");
/*     */   
/*  70 */   public static final ResourceKey<Item> NETHERITE_INGOT = create("netherite_ingot");
/*     */   
/*  71 */   public static final ResourceKey<Item> NETHERITE_SCRAP = create("netherite_scrap");
/*     */   
/*  72 */   public static final ResourceKey<Item> WOODEN_SWORD = create("wooden_sword");
/*     */   
/*  73 */   public static final ResourceKey<Item> WOODEN_SHOVEL = create("wooden_shovel");
/*     */   
/*  74 */   public static final ResourceKey<Item> WOODEN_PICKAXE = create("wooden_pickaxe");
/*     */   
/*  75 */   public static final ResourceKey<Item> WOODEN_AXE = create("wooden_axe");
/*     */   
/*  76 */   public static final ResourceKey<Item> WOODEN_HOE = create("wooden_hoe");
/*     */   
/*  77 */   public static final ResourceKey<Item> COPPER_SWORD = create("copper_sword");
/*     */   
/*  78 */   public static final ResourceKey<Item> COPPER_SHOVEL = create("copper_shovel");
/*     */   
/*  79 */   public static final ResourceKey<Item> COPPER_PICKAXE = create("copper_pickaxe");
/*     */   
/*  80 */   public static final ResourceKey<Item> COPPER_AXE = create("copper_axe");
/*     */   
/*  81 */   public static final ResourceKey<Item> COPPER_HOE = create("copper_hoe");
/*     */   
/*  82 */   public static final ResourceKey<Item> STONE_SWORD = create("stone_sword");
/*     */   
/*  83 */   public static final ResourceKey<Item> STONE_SHOVEL = create("stone_shovel");
/*     */   
/*  84 */   public static final ResourceKey<Item> STONE_PICKAXE = create("stone_pickaxe");
/*     */   
/*  85 */   public static final ResourceKey<Item> STONE_AXE = create("stone_axe");
/*     */   
/*  86 */   public static final ResourceKey<Item> STONE_HOE = create("stone_hoe");
/*     */   
/*  87 */   public static final ResourceKey<Item> GOLDEN_SWORD = create("golden_sword");
/*     */   
/*  88 */   public static final ResourceKey<Item> GOLDEN_SHOVEL = create("golden_shovel");
/*     */   
/*  89 */   public static final ResourceKey<Item> GOLDEN_PICKAXE = create("golden_pickaxe");
/*     */   
/*  90 */   public static final ResourceKey<Item> GOLDEN_AXE = create("golden_axe");
/*     */   
/*  91 */   public static final ResourceKey<Item> GOLDEN_HOE = create("golden_hoe");
/*     */   
/*  92 */   public static final ResourceKey<Item> IRON_SWORD = create("iron_sword");
/*     */   
/*  93 */   public static final ResourceKey<Item> IRON_SHOVEL = create("iron_shovel");
/*     */   
/*  94 */   public static final ResourceKey<Item> IRON_PICKAXE = create("iron_pickaxe");
/*     */   
/*  95 */   public static final ResourceKey<Item> IRON_AXE = create("iron_axe");
/*     */   
/*  96 */   public static final ResourceKey<Item> IRON_HOE = create("iron_hoe");
/*     */   
/*  97 */   public static final ResourceKey<Item> DIAMOND_SWORD = create("diamond_sword");
/*     */   
/*  98 */   public static final ResourceKey<Item> DIAMOND_SHOVEL = create("diamond_shovel");
/*     */   
/*  99 */   public static final ResourceKey<Item> DIAMOND_PICKAXE = create("diamond_pickaxe");
/*     */   
/* 100 */   public static final ResourceKey<Item> DIAMOND_AXE = create("diamond_axe");
/*     */   
/* 101 */   public static final ResourceKey<Item> DIAMOND_HOE = create("diamond_hoe");
/*     */   
/* 102 */   public static final ResourceKey<Item> NETHERITE_SWORD = create("netherite_sword");
/*     */   
/* 103 */   public static final ResourceKey<Item> NETHERITE_SHOVEL = create("netherite_shovel");
/*     */   
/* 104 */   public static final ResourceKey<Item> NETHERITE_PICKAXE = create("netherite_pickaxe");
/*     */   
/* 105 */   public static final ResourceKey<Item> NETHERITE_AXE = create("netherite_axe");
/*     */   
/* 106 */   public static final ResourceKey<Item> NETHERITE_HOE = create("netherite_hoe");
/*     */   
/* 107 */   public static final ResourceKey<Item> STICK = create("stick");
/*     */   
/* 108 */   public static final ResourceKey<Item> MUSHROOM_STEW = create("mushroom_stew");
/*     */   
/* 109 */   public static final ResourceKey<Item> FEATHER = create("feather");
/*     */   
/* 110 */   public static final ResourceKey<Item> GUNPOWDER = create("gunpowder");
/*     */   
/* 111 */   public static final ResourceKey<Item> WHEAT = create("wheat");
/*     */   
/* 112 */   public static final ResourceKey<Item> BREAD = create("bread");
/*     */   
/* 113 */   public static final ResourceKey<Item> LEATHER_HELMET = create("leather_helmet");
/*     */   
/* 114 */   public static final ResourceKey<Item> LEATHER_CHESTPLATE = create("leather_chestplate");
/*     */   
/* 115 */   public static final ResourceKey<Item> LEATHER_LEGGINGS = create("leather_leggings");
/*     */   
/* 116 */   public static final ResourceKey<Item> LEATHER_BOOTS = create("leather_boots");
/*     */   
/* 117 */   public static final ResourceKey<Item> COPPER_HELMET = create("copper_helmet");
/*     */   
/* 118 */   public static final ResourceKey<Item> COPPER_CHESTPLATE = create("copper_chestplate");
/*     */   
/* 119 */   public static final ResourceKey<Item> COPPER_LEGGINGS = create("copper_leggings");
/*     */   
/* 120 */   public static final ResourceKey<Item> COPPER_BOOTS = create("copper_boots");
/*     */   
/* 121 */   public static final ResourceKey<Item> CHAINMAIL_HELMET = create("chainmail_helmet");
/*     */   
/* 122 */   public static final ResourceKey<Item> CHAINMAIL_CHESTPLATE = create("chainmail_chestplate");
/*     */   
/* 123 */   public static final ResourceKey<Item> CHAINMAIL_LEGGINGS = create("chainmail_leggings");
/*     */   
/* 124 */   public static final ResourceKey<Item> CHAINMAIL_BOOTS = create("chainmail_boots");
/*     */   
/* 125 */   public static final ResourceKey<Item> IRON_HELMET = create("iron_helmet");
/*     */   
/* 126 */   public static final ResourceKey<Item> IRON_CHESTPLATE = create("iron_chestplate");
/*     */   
/* 127 */   public static final ResourceKey<Item> IRON_LEGGINGS = create("iron_leggings");
/*     */   
/* 128 */   public static final ResourceKey<Item> IRON_BOOTS = create("iron_boots");
/*     */   
/* 129 */   public static final ResourceKey<Item> DIAMOND_HELMET = create("diamond_helmet");
/*     */   
/* 130 */   public static final ResourceKey<Item> DIAMOND_CHESTPLATE = create("diamond_chestplate");
/*     */   
/* 131 */   public static final ResourceKey<Item> DIAMOND_LEGGINGS = create("diamond_leggings");
/*     */   
/* 132 */   public static final ResourceKey<Item> DIAMOND_BOOTS = create("diamond_boots");
/*     */   
/* 133 */   public static final ResourceKey<Item> GOLDEN_HELMET = create("golden_helmet");
/*     */   
/* 134 */   public static final ResourceKey<Item> GOLDEN_CHESTPLATE = create("golden_chestplate");
/*     */   
/* 135 */   public static final ResourceKey<Item> GOLDEN_LEGGINGS = create("golden_leggings");
/*     */   
/* 136 */   public static final ResourceKey<Item> GOLDEN_BOOTS = create("golden_boots");
/*     */   
/* 137 */   public static final ResourceKey<Item> NETHERITE_HELMET = create("netherite_helmet");
/*     */   
/* 138 */   public static final ResourceKey<Item> NETHERITE_CHESTPLATE = create("netherite_chestplate");
/*     */   
/* 139 */   public static final ResourceKey<Item> NETHERITE_LEGGINGS = create("netherite_leggings");
/*     */   
/* 140 */   public static final ResourceKey<Item> NETHERITE_BOOTS = create("netherite_boots");
/*     */   
/* 141 */   public static final ResourceKey<Item> FLINT = create("flint");
/*     */   
/* 142 */   public static final ResourceKey<Item> PORKCHOP = create("porkchop");
/*     */   
/* 143 */   public static final ResourceKey<Item> COOKED_PORKCHOP = create("cooked_porkchop");
/*     */   
/* 144 */   public static final ResourceKey<Item> PAINTING = create("painting");
/*     */   
/* 145 */   public static final ResourceKey<Item> GOLDEN_APPLE = create("golden_apple");
/*     */   
/* 146 */   public static final ResourceKey<Item> ENCHANTED_GOLDEN_APPLE = create("enchanted_golden_apple");
/*     */   
/* 147 */   public static final ResourceKey<Item> BUCKET = create("bucket");
/*     */   
/* 148 */   public static final ResourceKey<Item> WATER_BUCKET = create("water_bucket");
/*     */   
/* 149 */   public static final ResourceKey<Item> LAVA_BUCKET = create("lava_bucket");
/*     */   
/* 150 */   public static final ResourceKey<Item> SNOWBALL = create("snowball");
/*     */   
/* 151 */   public static final ResourceKey<Item> LEATHER = create("leather");
/*     */   
/* 152 */   public static final ResourceKey<Item> MILK_BUCKET = create("milk_bucket");
/*     */   
/* 153 */   public static final ResourceKey<Item> PUFFERFISH_BUCKET = create("pufferfish_bucket");
/*     */   
/* 154 */   public static final ResourceKey<Item> SALMON_BUCKET = create("salmon_bucket");
/*     */   
/* 155 */   public static final ResourceKey<Item> COD_BUCKET = create("cod_bucket");
/*     */   
/* 156 */   public static final ResourceKey<Item> TROPICAL_FISH_BUCKET = create("tropical_fish_bucket");
/*     */   
/* 157 */   public static final ResourceKey<Item> AXOLOTL_BUCKET = create("axolotl_bucket");
/*     */   
/* 158 */   public static final ResourceKey<Item> SULFUR_CUBE_BUCKET = create("sulfur_cube_bucket");
/*     */   
/* 159 */   public static final ResourceKey<Item> TADPOLE_BUCKET = create("tadpole_bucket");
/*     */   
/* 160 */   public static final ResourceKey<Item> BRICK = create("brick");
/*     */   
/* 161 */   public static final ResourceKey<Item> CLAY_BALL = create("clay_ball");
/*     */   
/* 162 */   public static final ResourceKey<Item> PAPER = create("paper");
/*     */   
/* 163 */   public static final ResourceKey<Item> BOOK = create("book");
/*     */   
/* 164 */   public static final ResourceKey<Item> SLIME_BALL = create("slime_ball");
/*     */   
/* 165 */   public static final ResourceKey<Item> EGG = create("egg");
/*     */   
/* 166 */   public static final ResourceKey<Item> BLUE_EGG = create("blue_egg");
/*     */   
/* 167 */   public static final ResourceKey<Item> BROWN_EGG = create("brown_egg");
/*     */   
/* 168 */   public static final ResourceKey<Item> COMPASS = create("compass");
/*     */   
/* 169 */   public static final ResourceKey<Item> RECOVERY_COMPASS = create("recovery_compass");
/*     */   
/* 170 */   public static final ResourceKey<Item> BUNDLE = create("bundle");
/*     */   
/* 171 */   public static final ResourceKey<Item> FISHING_ROD = create("fishing_rod");
/*     */   
/* 172 */   public static final ResourceKey<Item> CLOCK = create("clock");
/*     */   
/* 173 */   public static final ResourceKey<Item> SPYGLASS = create("spyglass");
/*     */   
/* 174 */   public static final ResourceKey<Item> GLOWSTONE_DUST = create("glowstone_dust");
/*     */   
/* 175 */   public static final ResourceKey<Item> COD = create("cod");
/*     */   
/* 176 */   public static final ResourceKey<Item> SALMON = create("salmon");
/*     */   
/* 177 */   public static final ResourceKey<Item> TROPICAL_FISH = create("tropical_fish");
/*     */   
/* 178 */   public static final ResourceKey<Item> PUFFERFISH = create("pufferfish");
/*     */   
/* 179 */   public static final ResourceKey<Item> COOKED_COD = create("cooked_cod");
/*     */   
/* 180 */   public static final ResourceKey<Item> COOKED_SALMON = create("cooked_salmon");
/*     */   
/* 181 */   public static final ResourceKey<Item> INK_SAC = create("ink_sac");
/*     */   
/* 182 */   public static final ResourceKey<Item> GLOW_INK_SAC = create("glow_ink_sac");
/*     */   
/* 183 */   public static final ResourceKey<Item> BONE_MEAL = create("bone_meal");
/*     */   
/* 184 */   public static final ResourceKey<Item> BONE = create("bone");
/*     */   
/* 185 */   public static final ResourceKey<Item> SUGAR = create("sugar");
/*     */   
/* 186 */   public static final ResourceKey<Item> COOKIE = create("cookie");
/*     */   
/* 187 */   public static final ResourceKey<Item> FILLED_MAP = create("filled_map");
/*     */   
/* 188 */   public static final ResourceKey<Item> SHEARS = create("shears");
/*     */   
/* 189 */   public static final ResourceKey<Item> MELON_SLICE = create("melon_slice");
/*     */   
/* 190 */   public static final ResourceKey<Item> DRIED_KELP = create("dried_kelp");
/*     */   
/* 191 */   public static final ResourceKey<Item> BEEF = create("beef");
/*     */   
/* 192 */   public static final ResourceKey<Item> COOKED_BEEF = create("cooked_beef");
/*     */   
/* 193 */   public static final ResourceKey<Item> CHICKEN = create("chicken");
/*     */   
/* 194 */   public static final ResourceKey<Item> COOKED_CHICKEN = create("cooked_chicken");
/*     */   
/* 195 */   public static final ResourceKey<Item> ROTTEN_FLESH = create("rotten_flesh");
/*     */   
/* 196 */   public static final ResourceKey<Item> ENDER_PEARL = create("ender_pearl");
/*     */   
/* 197 */   public static final ResourceKey<Item> BLAZE_ROD = create("blaze_rod");
/*     */   
/* 198 */   public static final ResourceKey<Item> GHAST_TEAR = create("ghast_tear");
/*     */   
/* 199 */   public static final ResourceKey<Item> GOLD_NUGGET = create("gold_nugget");
/*     */   
/* 200 */   public static final ResourceKey<Item> GLASS_BOTTLE = create("glass_bottle");
/*     */   
/* 201 */   public static final ResourceKey<Item> POTION = create("potion");
/*     */   
/* 202 */   public static final ResourceKey<Item> SPIDER_EYE = create("spider_eye");
/*     */   
/* 203 */   public static final ResourceKey<Item> FERMENTED_SPIDER_EYE = create("fermented_spider_eye");
/*     */   
/* 204 */   public static final ResourceKey<Item> BLAZE_POWDER = create("blaze_powder");
/*     */   
/* 205 */   public static final ResourceKey<Item> MAGMA_CREAM = create("magma_cream");
/*     */   
/* 206 */   public static final ResourceKey<Item> ENDER_EYE = create("ender_eye");
/*     */   
/* 207 */   public static final ResourceKey<Item> GLISTERING_MELON_SLICE = create("glistering_melon_slice");
/*     */   
/* 209 */   public static final ResourceKey<Item> CHICKEN_SPAWN_EGG = createSpawnEgg(EntityTypeIds.CHICKEN);
/*     */   
/* 210 */   public static final ResourceKey<Item> COW_SPAWN_EGG = createSpawnEgg(EntityTypeIds.COW);
/*     */   
/* 211 */   public static final ResourceKey<Item> PIG_SPAWN_EGG = createSpawnEgg(EntityTypeIds.PIG);
/*     */   
/* 212 */   public static final ResourceKey<Item> SHEEP_SPAWN_EGG = createSpawnEgg(EntityTypeIds.SHEEP);
/*     */   
/* 213 */   public static final ResourceKey<Item> CAMEL_SPAWN_EGG = createSpawnEgg(EntityTypeIds.CAMEL);
/*     */   
/* 214 */   public static final ResourceKey<Item> DONKEY_SPAWN_EGG = createSpawnEgg(EntityTypeIds.DONKEY);
/*     */   
/* 215 */   public static final ResourceKey<Item> HORSE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.HORSE);
/*     */   
/* 216 */   public static final ResourceKey<Item> MULE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.MULE);
/*     */   
/* 217 */   public static final ResourceKey<Item> CAT_SPAWN_EGG = createSpawnEgg(EntityTypeIds.CAT);
/*     */   
/* 218 */   public static final ResourceKey<Item> PARROT_SPAWN_EGG = createSpawnEgg(EntityTypeIds.PARROT);
/*     */   
/* 219 */   public static final ResourceKey<Item> WOLF_SPAWN_EGG = createSpawnEgg(EntityTypeIds.WOLF);
/*     */   
/* 220 */   public static final ResourceKey<Item> ARMADILLO_SPAWN_EGG = createSpawnEgg(EntityTypeIds.ARMADILLO);
/*     */   
/* 221 */   public static final ResourceKey<Item> BAT_SPAWN_EGG = createSpawnEgg(EntityTypeIds.BAT);
/*     */   
/* 222 */   public static final ResourceKey<Item> BEE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.BEE);
/*     */   
/* 223 */   public static final ResourceKey<Item> FOX_SPAWN_EGG = createSpawnEgg(EntityTypeIds.FOX);
/*     */   
/* 224 */   public static final ResourceKey<Item> GOAT_SPAWN_EGG = createSpawnEgg(EntityTypeIds.GOAT);
/*     */   
/* 225 */   public static final ResourceKey<Item> LLAMA_SPAWN_EGG = createSpawnEgg(EntityTypeIds.LLAMA);
/*     */   
/* 226 */   public static final ResourceKey<Item> OCELOT_SPAWN_EGG = createSpawnEgg(EntityTypeIds.OCELOT);
/*     */   
/* 227 */   public static final ResourceKey<Item> PANDA_SPAWN_EGG = createSpawnEgg(EntityTypeIds.PANDA);
/*     */   
/* 228 */   public static final ResourceKey<Item> POLAR_BEAR_SPAWN_EGG = createSpawnEgg(EntityTypeIds.POLAR_BEAR);
/*     */   
/* 229 */   public static final ResourceKey<Item> RABBIT_SPAWN_EGG = createSpawnEgg(EntityTypeIds.RABBIT);
/*     */   
/* 230 */   public static final ResourceKey<Item> AXOLOTL_SPAWN_EGG = createSpawnEgg(EntityTypeIds.AXOLOTL);
/*     */   
/* 231 */   public static final ResourceKey<Item> COD_SPAWN_EGG = createSpawnEgg(EntityTypeIds.COD);
/*     */   
/* 232 */   public static final ResourceKey<Item> DOLPHIN_SPAWN_EGG = createSpawnEgg(EntityTypeIds.DOLPHIN);
/*     */   
/* 233 */   public static final ResourceKey<Item> FROG_SPAWN_EGG = createSpawnEgg(EntityTypeIds.FROG);
/*     */   
/* 234 */   public static final ResourceKey<Item> GLOW_SQUID_SPAWN_EGG = createSpawnEgg(EntityTypeIds.GLOW_SQUID);
/*     */   
/* 235 */   public static final ResourceKey<Item> NAUTILUS_SPAWN_EGG = createSpawnEgg(EntityTypeIds.NAUTILUS);
/*     */   
/* 236 */   public static final ResourceKey<Item> PUFFERFISH_SPAWN_EGG = createSpawnEgg(EntityTypeIds.PUFFERFISH);
/*     */   
/* 237 */   public static final ResourceKey<Item> SALMON_SPAWN_EGG = createSpawnEgg(EntityTypeIds.SALMON);
/*     */   
/* 238 */   public static final ResourceKey<Item> SQUID_SPAWN_EGG = createSpawnEgg(EntityTypeIds.SQUID);
/*     */   
/* 239 */   public static final ResourceKey<Item> TADPOLE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.TADPOLE);
/*     */   
/* 240 */   public static final ResourceKey<Item> TROPICAL_FISH_SPAWN_EGG = createSpawnEgg(EntityTypeIds.TROPICAL_FISH);
/*     */   
/* 241 */   public static final ResourceKey<Item> TURTLE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.TURTLE);
/*     */   
/* 242 */   public static final ResourceKey<Item> ALLAY_SPAWN_EGG = createSpawnEgg(EntityTypeIds.ALLAY);
/*     */   
/* 243 */   public static final ResourceKey<Item> MOOSHROOM_SPAWN_EGG = createSpawnEgg(EntityTypeIds.MOOSHROOM);
/*     */   
/* 244 */   public static final ResourceKey<Item> SNIFFER_SPAWN_EGG = createSpawnEgg(EntityTypeIds.SNIFFER);
/*     */   
/* 245 */   public static final ResourceKey<Item> SULFUR_CUBE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.SULFUR_CUBE);
/*     */   
/* 246 */   public static final ResourceKey<Item> COPPER_GOLEM_SPAWN_EGG = createSpawnEgg(EntityTypeIds.COPPER_GOLEM);
/*     */   
/* 247 */   public static final ResourceKey<Item> IRON_GOLEM_SPAWN_EGG = createSpawnEgg(EntityTypeIds.IRON_GOLEM);
/*     */   
/* 248 */   public static final ResourceKey<Item> SNOW_GOLEM_SPAWN_EGG = createSpawnEgg(EntityTypeIds.SNOW_GOLEM);
/*     */   
/* 249 */   public static final ResourceKey<Item> TRADER_LLAMA_SPAWN_EGG = createSpawnEgg(EntityTypeIds.TRADER_LLAMA);
/*     */   
/* 250 */   public static final ResourceKey<Item> VILLAGER_SPAWN_EGG = createSpawnEgg(EntityTypeIds.VILLAGER);
/*     */   
/* 251 */   public static final ResourceKey<Item> WANDERING_TRADER_SPAWN_EGG = createSpawnEgg(EntityTypeIds.WANDERING_TRADER);
/*     */   
/* 252 */   public static final ResourceKey<Item> BOGGED_SPAWN_EGG = createSpawnEgg(EntityTypeIds.BOGGED);
/*     */   
/* 253 */   public static final ResourceKey<Item> CAMEL_HUSK_SPAWN_EGG = createSpawnEgg(EntityTypeIds.CAMEL_HUSK);
/*     */   
/* 254 */   public static final ResourceKey<Item> DROWNED_SPAWN_EGG = createSpawnEgg(EntityTypeIds.DROWNED);
/*     */   
/* 255 */   public static final ResourceKey<Item> HUSK_SPAWN_EGG = createSpawnEgg(EntityTypeIds.HUSK);
/*     */   
/* 256 */   public static final ResourceKey<Item> PARCHED_SPAWN_EGG = createSpawnEgg(EntityTypeIds.PARCHED);
/*     */   
/* 257 */   public static final ResourceKey<Item> SKELETON_SPAWN_EGG = createSpawnEgg(EntityTypeIds.SKELETON);
/*     */   
/* 258 */   public static final ResourceKey<Item> SKELETON_HORSE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.SKELETON_HORSE);
/*     */   
/* 259 */   public static final ResourceKey<Item> STRAY_SPAWN_EGG = createSpawnEgg(EntityTypeIds.STRAY);
/*     */   
/* 260 */   public static final ResourceKey<Item> WITHER_SPAWN_EGG = createSpawnEgg(EntityTypeIds.WITHER);
/*     */   
/* 261 */   public static final ResourceKey<Item> WITHER_SKELETON_SPAWN_EGG = createSpawnEgg(EntityTypeIds.WITHER_SKELETON);
/*     */   
/* 262 */   public static final ResourceKey<Item> ZOMBIE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.ZOMBIE);
/*     */   
/* 263 */   public static final ResourceKey<Item> ZOMBIE_HORSE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.ZOMBIE_HORSE);
/*     */   
/* 264 */   public static final ResourceKey<Item> ZOMBIE_NAUTILUS_SPAWN_EGG = createSpawnEgg(EntityTypeIds.ZOMBIE_NAUTILUS);
/*     */   
/* 265 */   public static final ResourceKey<Item> ZOMBIE_VILLAGER_SPAWN_EGG = createSpawnEgg(EntityTypeIds.ZOMBIE_VILLAGER);
/*     */   
/* 266 */   public static final ResourceKey<Item> CAVE_SPIDER_SPAWN_EGG = createSpawnEgg(EntityTypeIds.CAVE_SPIDER);
/*     */   
/* 267 */   public static final ResourceKey<Item> SPIDER_SPAWN_EGG = createSpawnEgg(EntityTypeIds.SPIDER);
/*     */   
/* 268 */   public static final ResourceKey<Item> BREEZE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.BREEZE);
/*     */   
/* 269 */   public static final ResourceKey<Item> CREAKING_SPAWN_EGG = createSpawnEgg(EntityTypeIds.CREAKING);
/*     */   
/* 270 */   public static final ResourceKey<Item> CREEPER_SPAWN_EGG = createSpawnEgg(EntityTypeIds.CREEPER);
/*     */   
/* 271 */   public static final ResourceKey<Item> ELDER_GUARDIAN_SPAWN_EGG = createSpawnEgg(EntityTypeIds.ELDER_GUARDIAN);
/*     */   
/* 272 */   public static final ResourceKey<Item> GUARDIAN_SPAWN_EGG = createSpawnEgg(EntityTypeIds.GUARDIAN);
/*     */   
/* 273 */   public static final ResourceKey<Item> PHANTOM_SPAWN_EGG = createSpawnEgg(EntityTypeIds.PHANTOM);
/*     */   
/* 274 */   public static final ResourceKey<Item> SILVERFISH_SPAWN_EGG = createSpawnEgg(EntityTypeIds.SILVERFISH);
/*     */   
/* 275 */   public static final ResourceKey<Item> SLIME_SPAWN_EGG = createSpawnEgg(EntityTypeIds.SLIME);
/*     */   
/* 276 */   public static final ResourceKey<Item> WARDEN_SPAWN_EGG = createSpawnEgg(EntityTypeIds.WARDEN);
/*     */   
/* 277 */   public static final ResourceKey<Item> WITCH_SPAWN_EGG = createSpawnEgg(EntityTypeIds.WITCH);
/*     */   
/* 278 */   public static final ResourceKey<Item> EVOKER_SPAWN_EGG = createSpawnEgg(EntityTypeIds.EVOKER);
/*     */   
/* 279 */   public static final ResourceKey<Item> PILLAGER_SPAWN_EGG = createSpawnEgg(EntityTypeIds.PILLAGER);
/*     */   
/* 280 */   public static final ResourceKey<Item> RAVAGER_SPAWN_EGG = createSpawnEgg(EntityTypeIds.RAVAGER);
/*     */   
/* 281 */   public static final ResourceKey<Item> VINDICATOR_SPAWN_EGG = createSpawnEgg(EntityTypeIds.VINDICATOR);
/*     */   
/* 282 */   public static final ResourceKey<Item> VEX_SPAWN_EGG = createSpawnEgg(EntityTypeIds.VEX);
/*     */   
/* 283 */   public static final ResourceKey<Item> BLAZE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.BLAZE);
/*     */   
/* 284 */   public static final ResourceKey<Item> GHAST_SPAWN_EGG = createSpawnEgg(EntityTypeIds.GHAST);
/*     */   
/* 285 */   public static final ResourceKey<Item> HAPPY_GHAST_SPAWN_EGG = createSpawnEgg(EntityTypeIds.HAPPY_GHAST);
/*     */   
/* 286 */   public static final ResourceKey<Item> HOGLIN_SPAWN_EGG = createSpawnEgg(EntityTypeIds.HOGLIN);
/*     */   
/* 287 */   public static final ResourceKey<Item> MAGMA_CUBE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.MAGMA_CUBE);
/*     */   
/* 288 */   public static final ResourceKey<Item> PIGLIN_SPAWN_EGG = createSpawnEgg(EntityTypeIds.PIGLIN);
/*     */   
/* 289 */   public static final ResourceKey<Item> PIGLIN_BRUTE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.PIGLIN_BRUTE);
/*     */   
/* 290 */   public static final ResourceKey<Item> STRIDER_SPAWN_EGG = createSpawnEgg(EntityTypeIds.STRIDER);
/*     */   
/* 291 */   public static final ResourceKey<Item> ZOGLIN_SPAWN_EGG = createSpawnEgg(EntityTypeIds.ZOGLIN);
/*     */   
/* 292 */   public static final ResourceKey<Item> ZOMBIFIED_PIGLIN_SPAWN_EGG = createSpawnEgg(EntityTypeIds.ZOMBIFIED_PIGLIN);
/*     */   
/* 293 */   public static final ResourceKey<Item> ENDER_DRAGON_SPAWN_EGG = createSpawnEgg(EntityTypeIds.ENDER_DRAGON);
/*     */   
/* 294 */   public static final ResourceKey<Item> ENDERMAN_SPAWN_EGG = createSpawnEgg(EntityTypeIds.ENDERMAN);
/*     */   
/* 295 */   public static final ResourceKey<Item> ENDERMITE_SPAWN_EGG = createSpawnEgg(EntityTypeIds.ENDERMITE);
/*     */   
/* 296 */   public static final ResourceKey<Item> SHULKER_SPAWN_EGG = createSpawnEgg(EntityTypeIds.SHULKER);
/*     */   
/* 298 */   public static final ResourceKey<Item> EXPERIENCE_BOTTLE = create("experience_bottle");
/*     */   
/* 299 */   public static final ResourceKey<Item> FIRE_CHARGE = create("fire_charge");
/*     */   
/* 300 */   public static final ResourceKey<Item> WIND_CHARGE = create("wind_charge");
/*     */   
/* 301 */   public static final ResourceKey<Item> WRITABLE_BOOK = create("writable_book");
/*     */   
/* 302 */   public static final ResourceKey<Item> WRITTEN_BOOK = create("written_book");
/*     */   
/* 303 */   public static final ResourceKey<Item> BREEZE_ROD = create("breeze_rod");
/*     */   
/* 304 */   public static final ResourceKey<Item> MACE = create("mace");
/*     */   
/* 305 */   public static final ResourceKey<Item> ITEM_FRAME = create("item_frame");
/*     */   
/* 306 */   public static final ResourceKey<Item> GLOW_ITEM_FRAME = create("glow_item_frame");
/*     */   
/* 307 */   public static final ResourceKey<Item> BAKED_POTATO = create("baked_potato");
/*     */   
/* 308 */   public static final ResourceKey<Item> POISONOUS_POTATO = create("poisonous_potato");
/*     */   
/* 309 */   public static final ResourceKey<Item> MAP = create("map");
/*     */   
/* 310 */   public static final ResourceKey<Item> GOLDEN_CARROT = create("golden_carrot");
/*     */   
/* 311 */   public static final ResourceKey<Item> NETHER_STAR = create("nether_star");
/*     */   
/* 312 */   public static final ResourceKey<Item> PUMPKIN_PIE = create("pumpkin_pie");
/*     */   
/* 313 */   public static final ResourceKey<Item> FIREWORK_ROCKET = create("firework_rocket");
/*     */   
/* 314 */   public static final ResourceKey<Item> FIREWORK_STAR = create("firework_star");
/*     */   
/* 315 */   public static final ResourceKey<Item> ENCHANTED_BOOK = create("enchanted_book");
/*     */   
/* 316 */   public static final ResourceKey<Item> NETHER_BRICK = create("nether_brick");
/*     */   
/* 317 */   public static final ResourceKey<Item> RESIN_BRICK = create("resin_brick");
/*     */   
/* 318 */   public static final ResourceKey<Item> PRISMARINE_SHARD = create("prismarine_shard");
/*     */   
/* 319 */   public static final ResourceKey<Item> PRISMARINE_CRYSTALS = create("prismarine_crystals");
/*     */   
/* 320 */   public static final ResourceKey<Item> RABBIT = create("rabbit");
/*     */   
/* 321 */   public static final ResourceKey<Item> COOKED_RABBIT = create("cooked_rabbit");
/*     */   
/* 322 */   public static final ResourceKey<Item> RABBIT_STEW = create("rabbit_stew");
/*     */   
/* 323 */   public static final ResourceKey<Item> RABBIT_FOOT = create("rabbit_foot");
/*     */   
/* 324 */   public static final ResourceKey<Item> RABBIT_HIDE = create("rabbit_hide");
/*     */   
/* 325 */   public static final ResourceKey<Item> ARMOR_STAND = create("armor_stand");
/*     */   
/* 326 */   public static final ResourceKey<Item> COPPER_HORSE_ARMOR = create("copper_horse_armor");
/*     */   
/* 327 */   public static final ResourceKey<Item> IRON_HORSE_ARMOR = create("iron_horse_armor");
/*     */   
/* 328 */   public static final ResourceKey<Item> GOLDEN_HORSE_ARMOR = create("golden_horse_armor");
/*     */   
/* 329 */   public static final ResourceKey<Item> DIAMOND_HORSE_ARMOR = create("diamond_horse_armor");
/*     */   
/* 330 */   public static final ResourceKey<Item> NETHERITE_HORSE_ARMOR = create("netherite_horse_armor");
/*     */   
/* 331 */   public static final ResourceKey<Item> LEATHER_HORSE_ARMOR = create("leather_horse_armor");
/*     */   
/* 332 */   public static final ResourceKey<Item> LEAD = create("lead");
/*     */   
/* 333 */   public static final ResourceKey<Item> NAME_TAG = create("name_tag");
/*     */   
/* 334 */   public static final ResourceKey<Item> COMMAND_BLOCK_MINECART = create("command_block_minecart");
/*     */   
/* 335 */   public static final ResourceKey<Item> MUTTON = create("mutton");
/*     */   
/* 336 */   public static final ResourceKey<Item> COOKED_MUTTON = create("cooked_mutton");
/*     */   
/* 337 */   public static final ResourceKey<Item> END_CRYSTAL = create("end_crystal");
/*     */   
/* 338 */   public static final ResourceKey<Item> CHORUS_FRUIT = create("chorus_fruit");
/*     */   
/* 339 */   public static final ResourceKey<Item> POPPED_CHORUS_FRUIT = create("popped_chorus_fruit");
/*     */   
/* 340 */   public static final ResourceKey<Item> BEETROOT = create("beetroot");
/*     */   
/* 341 */   public static final ResourceKey<Item> BEETROOT_SOUP = create("beetroot_soup");
/*     */   
/* 342 */   public static final ResourceKey<Item> DRAGON_BREATH = create("dragon_breath");
/*     */   
/* 343 */   public static final ResourceKey<Item> SPLASH_POTION = create("splash_potion");
/*     */   
/* 344 */   public static final ResourceKey<Item> SPECTRAL_ARROW = create("spectral_arrow");
/*     */   
/* 345 */   public static final ResourceKey<Item> TIPPED_ARROW = create("tipped_arrow");
/*     */   
/* 346 */   public static final ResourceKey<Item> LINGERING_POTION = create("lingering_potion");
/*     */   
/* 347 */   public static final ResourceKey<Item> SHIELD = create("shield");
/*     */   
/* 348 */   public static final ResourceKey<Item> WOODEN_SPEAR = create("wooden_spear");
/*     */   
/* 349 */   public static final ResourceKey<Item> STONE_SPEAR = create("stone_spear");
/*     */   
/* 350 */   public static final ResourceKey<Item> COPPER_SPEAR = create("copper_spear");
/*     */   
/* 351 */   public static final ResourceKey<Item> IRON_SPEAR = create("iron_spear");
/*     */   
/* 352 */   public static final ResourceKey<Item> GOLDEN_SPEAR = create("golden_spear");
/*     */   
/* 353 */   public static final ResourceKey<Item> DIAMOND_SPEAR = create("diamond_spear");
/*     */   
/* 354 */   public static final ResourceKey<Item> NETHERITE_SPEAR = create("netherite_spear");
/*     */   
/* 355 */   public static final ResourceKey<Item> TOTEM_OF_UNDYING = create("totem_of_undying");
/*     */   
/* 356 */   public static final ResourceKey<Item> SHULKER_SHELL = create("shulker_shell");
/*     */   
/* 357 */   public static final ResourceKey<Item> IRON_NUGGET = create("iron_nugget");
/*     */   
/* 358 */   public static final ResourceKey<Item> COPPER_NUGGET = create("copper_nugget");
/*     */   
/* 359 */   public static final ResourceKey<Item> KNOWLEDGE_BOOK = create("knowledge_book");
/*     */   
/* 360 */   public static final ResourceKey<Item> DEBUG_STICK = create("debug_stick");
/*     */   
/* 362 */   public static final ResourceKey<Item> MUSIC_DISC_13 = createMusicDisc(JukeboxSongs.THIRTEEN);
/*     */   
/* 363 */   public static final ResourceKey<Item> MUSIC_DISC_CAT = createMusicDisc(JukeboxSongs.CAT);
/*     */   
/* 364 */   public static final ResourceKey<Item> MUSIC_DISC_BLOCKS = createMusicDisc(JukeboxSongs.BLOCKS);
/*     */   
/* 365 */   public static final ResourceKey<Item> MUSIC_DISC_BOUNCE = createMusicDisc(JukeboxSongs.BOUNCE);
/*     */   
/* 366 */   public static final ResourceKey<Item> MUSIC_DISC_CHIRP = createMusicDisc(JukeboxSongs.CHIRP);
/*     */   
/* 367 */   public static final ResourceKey<Item> MUSIC_DISC_CREATOR = createMusicDisc(JukeboxSongs.CREATOR);
/*     */   
/* 368 */   public static final ResourceKey<Item> MUSIC_DISC_CREATOR_MUSIC_BOX = createMusicDisc(JukeboxSongs.CREATOR_MUSIC_BOX);
/*     */   
/* 369 */   public static final ResourceKey<Item> MUSIC_DISC_FAR = createMusicDisc(JukeboxSongs.FAR);
/*     */   
/* 370 */   public static final ResourceKey<Item> MUSIC_DISC_LAVA_CHICKEN = createMusicDisc(JukeboxSongs.LAVA_CHICKEN);
/*     */   
/* 371 */   public static final ResourceKey<Item> MUSIC_DISC_MALL = createMusicDisc(JukeboxSongs.MALL);
/*     */   
/* 372 */   public static final ResourceKey<Item> MUSIC_DISC_MELLOHI = createMusicDisc(JukeboxSongs.MELLOHI);
/*     */   
/* 373 */   public static final ResourceKey<Item> MUSIC_DISC_STAL = createMusicDisc(JukeboxSongs.STAL);
/*     */   
/* 374 */   public static final ResourceKey<Item> MUSIC_DISC_STRAD = createMusicDisc(JukeboxSongs.STRAD);
/*     */   
/* 375 */   public static final ResourceKey<Item> MUSIC_DISC_WARD = createMusicDisc(JukeboxSongs.WARD);
/*     */   
/* 376 */   public static final ResourceKey<Item> MUSIC_DISC_11 = createMusicDisc(JukeboxSongs.ELEVEN);
/*     */   
/* 377 */   public static final ResourceKey<Item> MUSIC_DISC_WAIT = createMusicDisc(JukeboxSongs.WAIT);
/*     */   
/* 378 */   public static final ResourceKey<Item> MUSIC_DISC_OTHERSIDE = createMusicDisc(JukeboxSongs.OTHERSIDE);
/*     */   
/* 379 */   public static final ResourceKey<Item> MUSIC_DISC_RELIC = createMusicDisc(JukeboxSongs.RELIC);
/*     */   
/* 380 */   public static final ResourceKey<Item> MUSIC_DISC_5 = createMusicDisc(JukeboxSongs.FIVE);
/*     */   
/* 381 */   public static final ResourceKey<Item> MUSIC_DISC_PIGSTEP = createMusicDisc(JukeboxSongs.PIGSTEP);
/*     */   
/* 382 */   public static final ResourceKey<Item> MUSIC_DISC_PRECIPICE = createMusicDisc(JukeboxSongs.PRECIPICE);
/*     */   
/* 383 */   public static final ResourceKey<Item> MUSIC_DISC_TEARS = createMusicDisc(JukeboxSongs.TEARS);
/*     */   
/* 385 */   public static final ResourceKey<Item> DISC_FRAGMENT_5 = create("disc_fragment_5");
/*     */   
/* 386 */   public static final ResourceKey<Item> TRIDENT = create("trident");
/*     */   
/* 387 */   public static final ResourceKey<Item> NAUTILUS_SHELL = create("nautilus_shell");
/*     */   
/* 388 */   public static final ResourceKey<Item> IRON_NAUTILUS_ARMOR = create("iron_nautilus_armor");
/*     */   
/* 389 */   public static final ResourceKey<Item> GOLDEN_NAUTILUS_ARMOR = create("golden_nautilus_armor");
/*     */   
/* 390 */   public static final ResourceKey<Item> DIAMOND_NAUTILUS_ARMOR = create("diamond_nautilus_armor");
/*     */   
/* 391 */   public static final ResourceKey<Item> NETHERITE_NAUTILUS_ARMOR = create("netherite_nautilus_armor");
/*     */   
/* 392 */   public static final ResourceKey<Item> COPPER_NAUTILUS_ARMOR = create("copper_nautilus_armor");
/*     */   
/* 393 */   public static final ResourceKey<Item> HEART_OF_THE_SEA = create("heart_of_the_sea");
/*     */   
/* 394 */   public static final ResourceKey<Item> CROSSBOW = create("crossbow");
/*     */   
/* 395 */   public static final ResourceKey<Item> SUSPICIOUS_STEW = create("suspicious_stew");
/*     */   
/* 397 */   public static final ResourceKey<Item> FLOWER_BANNER_PATTERN = create("flower_banner_pattern");
/*     */   
/* 398 */   public static final ResourceKey<Item> CREEPER_BANNER_PATTERN = create("creeper_banner_pattern");
/*     */   
/* 399 */   public static final ResourceKey<Item> SKULL_BANNER_PATTERN = create("skull_banner_pattern");
/*     */   
/* 400 */   public static final ResourceKey<Item> MOJANG_BANNER_PATTERN = create("mojang_banner_pattern");
/*     */   
/* 401 */   public static final ResourceKey<Item> GLOBE_BANNER_PATTERN = create("globe_banner_pattern");
/*     */   
/* 402 */   public static final ResourceKey<Item> PIGLIN_BANNER_PATTERN = create("piglin_banner_pattern");
/*     */   
/* 403 */   public static final ResourceKey<Item> FLOW_BANNER_PATTERN = create("flow_banner_pattern");
/*     */   
/* 404 */   public static final ResourceKey<Item> GUSTER_BANNER_PATTERN = create("guster_banner_pattern");
/*     */   
/* 405 */   public static final ResourceKey<Item> FIELD_MASONED_BANNER_PATTERN = create("field_masoned_banner_pattern");
/*     */   
/* 406 */   public static final ResourceKey<Item> BORDURE_INDENTED_BANNER_PATTERN = create("bordure_indented_banner_pattern");
/*     */   
/* 408 */   public static final ResourceKey<Item> GOAT_HORN = create("goat_horn");
/*     */   
/* 409 */   public static final ResourceKey<Item> HONEYCOMB = create("honeycomb");
/*     */   
/* 410 */   public static final ResourceKey<Item> HONEY_BOTTLE = create("honey_bottle");
/*     */   
/* 411 */   public static final ResourceKey<Item> ECHO_SHARD = create("echo_shard");
/*     */   
/* 412 */   public static final ResourceKey<Item> BRUSH = create("brush");
/*     */   
/*     */   public static final String SMITHING_TEMPLATE_SUFFIX = "_smithing_template";
/*     */   
/* 415 */   public static final ResourceKey<Item> NETHERITE_UPGRADE_SMITHING_TEMPLATE = create("netherite_upgrade_smithing_template");
/*     */   
/* 416 */   public static final ResourceKey<Item> SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.SENTRY);
/*     */   
/* 417 */   public static final ResourceKey<Item> DUNE_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.DUNE);
/*     */   
/* 418 */   public static final ResourceKey<Item> COAST_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.COAST);
/*     */   
/* 419 */   public static final ResourceKey<Item> WILD_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.WILD);
/*     */   
/* 420 */   public static final ResourceKey<Item> WARD_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.WARD);
/*     */   
/* 421 */   public static final ResourceKey<Item> EYE_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.EYE);
/*     */   
/* 422 */   public static final ResourceKey<Item> VEX_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.VEX);
/*     */   
/* 423 */   public static final ResourceKey<Item> TIDE_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.TIDE);
/*     */   
/* 424 */   public static final ResourceKey<Item> SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.SNOUT);
/*     */   
/* 425 */   public static final ResourceKey<Item> RIB_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.RIB);
/*     */   
/* 426 */   public static final ResourceKey<Item> SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.SPIRE);
/*     */   
/* 427 */   public static final ResourceKey<Item> WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.WAYFINDER);
/*     */   
/* 428 */   public static final ResourceKey<Item> SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.SHAPER);
/*     */   
/* 429 */   public static final ResourceKey<Item> SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.SILENCE);
/*     */   
/* 430 */   public static final ResourceKey<Item> RAISER_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.RAISER);
/*     */   
/* 431 */   public static final ResourceKey<Item> HOST_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.HOST);
/*     */   
/* 432 */   public static final ResourceKey<Item> FLOW_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.FLOW);
/*     */   
/* 433 */   public static final ResourceKey<Item> BOLT_ARMOR_TRIM_SMITHING_TEMPLATE = createArmorTrimSmithingTemplate(TrimPatterns.BOLT);
/*     */   
/* 435 */   public static final ResourceKey<Item> ANGLER_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.ANGLER);
/*     */   
/* 436 */   public static final ResourceKey<Item> ARCHER_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.ARCHER);
/*     */   
/* 437 */   public static final ResourceKey<Item> ARMS_UP_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.ARMS_UP);
/*     */   
/* 438 */   public static final ResourceKey<Item> BLADE_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.BLADE);
/*     */   
/* 439 */   public static final ResourceKey<Item> BREWER_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.BREWER);
/*     */   
/* 440 */   public static final ResourceKey<Item> BURN_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.BURN);
/*     */   
/* 441 */   public static final ResourceKey<Item> DANGER_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.DANGER);
/*     */   
/* 442 */   public static final ResourceKey<Item> EXPLORER_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.EXPLORER);
/*     */   
/* 443 */   public static final ResourceKey<Item> FLOW_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.FLOW);
/*     */   
/* 444 */   public static final ResourceKey<Item> FRIEND_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.FRIEND);
/*     */   
/* 445 */   public static final ResourceKey<Item> GUSTER_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.GUSTER);
/*     */   
/* 446 */   public static final ResourceKey<Item> HEART_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.HEART);
/*     */   
/* 447 */   public static final ResourceKey<Item> HEARTBREAK_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.HEARTBREAK);
/*     */   
/* 448 */   public static final ResourceKey<Item> HOWL_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.HOWL);
/*     */   
/* 449 */   public static final ResourceKey<Item> MINER_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.MINER);
/*     */   
/* 450 */   public static final ResourceKey<Item> MOURNER_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.MOURNER);
/*     */   
/* 451 */   public static final ResourceKey<Item> PLENTY_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.PLENTY);
/*     */   
/* 452 */   public static final ResourceKey<Item> PRIZE_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.PRIZE);
/*     */   
/* 453 */   public static final ResourceKey<Item> SCRAPE_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.SCRAPE);
/*     */   
/* 454 */   public static final ResourceKey<Item> SHEAF_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.SHEAF);
/*     */   
/* 455 */   public static final ResourceKey<Item> SHELTER_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.SHELTER);
/*     */   
/* 456 */   public static final ResourceKey<Item> SKULL_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.SKULL);
/*     */   
/* 457 */   public static final ResourceKey<Item> SNORT_POTTERY_SHERD = createPotterySherd(DecoratedPotPatterns.SNORT);
/*     */   
/* 459 */   public static final ResourceKey<Item> TRIAL_KEY = create("trial_key");
/*     */   
/* 460 */   public static final ResourceKey<Item> OMINOUS_TRIAL_KEY = create("ominous_trial_key");
/*     */   
/* 461 */   public static final ResourceKey<Item> OMINOUS_BOTTLE = create("ominous_bottle");
/*     */   
/* 463 */   public static final ColorCollection<ResourceKey<Item>> HARNESS = createSimpleColored("harness");
/*     */   
/* 464 */   public static final ColorCollection<ResourceKey<Item>> DYED_BUNDLE = createSimpleColored("bundle");
/*     */   
/* 465 */   public static final ColorCollection<ResourceKey<Item>> DYE = createSimpleColored("dye");
/*     */   
/*     */   private static ResourceKey<Item> create(String name) {
/* 468 */     return ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(name));
/*     */   }
/*     */   
/*     */   private static ResourceKey<Item> createPotterySherd(ResourceKey<DecoratedPotPattern> sherd) {
/* 472 */     return sherd.dependent(Registries.ITEM, "_pottery_sherd");
/*     */   }
/*     */   
/*     */   private static ResourceKey<Item> createArmorTrimSmithingTemplate(ResourceKey<TrimPattern> template) {
/* 476 */     return template.dependent(Registries.ITEM, "_armor_trim_smithing_template");
/*     */   }
/*     */   
/*     */   private static ResourceKey<Item> createMusicDisc(ResourceKey<JukeboxSong> music) {
/* 480 */     return music.dependent(Registries.ITEM, path -> "music_disc_" + path);
/*     */   }
/*     */   
/*     */   private static ResourceKey<Item> createSpawnEgg(ResourceKey<EntityType<?>> entity) {
/* 484 */     return entity.dependent(Registries.ITEM, "_spawn_egg");
/*     */   }
/*     */   
/*     */   private static ColorCollection<ResourceKey<Item>> createSimpleColored(String baseName) {
/* 488 */     return ColorCollection.prefixWithColor(ColorCollection.create(baseName)).map(ItemIds::create);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\references\ItemIds.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */