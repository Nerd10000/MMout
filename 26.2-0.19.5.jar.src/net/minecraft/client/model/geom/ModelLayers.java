/*     */ package net.minecraft.client.model.geom;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.renderer.entity.ArmorModelSet;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class ModelLayers {
/*     */   private static final String DEFAULT_LAYER = "main";
/*     */   
/*  12 */   private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
/*     */   
/*  14 */   public static final ModelLayerLocation ACACIA_BOAT = register("boat/acacia");
/*     */   
/*  15 */   public static final ModelLayerLocation ACACIA_CHEST_BOAT = register("chest_boat/acacia");
/*     */   
/*  16 */   public static final ModelLayerLocation ALLAY = register("allay");
/*     */   
/*  17 */   public static final ModelLayerLocation ARMADILLO = register("armadillo");
/*     */   
/*  18 */   public static final ModelLayerLocation ARMADILLO_BABY = register("armadillo_baby");
/*     */   
/*  19 */   public static final ModelLayerLocation ARMOR_STAND = register("armor_stand");
/*     */   
/*  20 */   public static final ArmorModelSet<ModelLayerLocation> ARMOR_STAND_ARMOR = registerArmorSet("armor_stand");
/*     */   
/*  21 */   public static final ModelLayerLocation ARMOR_STAND_SMALL = register("armor_stand_small");
/*     */   
/*  22 */   public static final ArmorModelSet<ModelLayerLocation> ARMOR_STAND_SMALL_ARMOR = registerArmorSet("armor_stand_small");
/*     */   
/*  23 */   public static final ModelLayerLocation ARROW = register("arrow");
/*     */   
/*  24 */   public static final ModelLayerLocation AXOLOTL = register("axolotl");
/*     */   
/*  25 */   public static final ModelLayerLocation AXOLOTL_BABY = register("axolotl_baby");
/*     */   
/*  26 */   public static final ModelLayerLocation BAMBOO_CHEST_RAFT = register("chest_boat/bamboo");
/*     */   
/*  27 */   public static final ModelLayerLocation BAMBOO_RAFT = register("boat/bamboo");
/*     */   
/*  28 */   public static final ModelLayerLocation STANDING_BANNER = register("standing_banner");
/*     */   
/*  29 */   public static final ModelLayerLocation STANDING_BANNER_FLAG = register("standing_banner", "flag");
/*     */   
/*  30 */   public static final ModelLayerLocation WALL_BANNER = register("wall_banner");
/*     */   
/*  31 */   public static final ModelLayerLocation WALL_BANNER_FLAG = register("wall_banner", "flag");
/*     */   
/*  32 */   public static final ModelLayerLocation BAT = register("bat");
/*     */   
/*  33 */   public static final ModelLayerLocation BEE = register("bee");
/*     */   
/*  34 */   public static final ModelLayerLocation BEE_BABY = register("bee_baby");
/*     */   
/*  35 */   public static final ModelLayerLocation BEE_STINGER = register("bee_stinger");
/*     */   
/*  36 */   public static final ModelLayerLocation BELL = register("bell");
/*     */   
/*  37 */   public static final ModelLayerLocation BIRCH_BOAT = register("boat/birch");
/*     */   
/*  38 */   public static final ModelLayerLocation BIRCH_CHEST_BOAT = register("chest_boat/birch");
/*     */   
/*  39 */   public static final ModelLayerLocation BLAZE = register("blaze");
/*     */   
/*  40 */   public static final ModelLayerLocation BOAT_WATER_PATCH = register("boat", "water_patch");
/*     */   
/*  41 */   public static final ModelLayerLocation BOGGED = register("bogged");
/*     */   
/*  42 */   public static final ArmorModelSet<ModelLayerLocation> BOGGED_ARMOR = registerArmorSet("bogged");
/*     */   
/*  43 */   public static final ModelLayerLocation BOGGED_OUTER_LAYER = register("bogged", "outer");
/*     */   
/*  44 */   public static final ModelLayerLocation BOOK = register("book");
/*     */   
/*  45 */   public static final ModelLayerLocation BREEZE = register("breeze");
/*     */   
/*  46 */   public static final ModelLayerLocation BREEZE_WIND = register("breeze", "wind");
/*     */   
/*  47 */   public static final ModelLayerLocation BREEZE_EYES = register("breeze", "eyes");
/*     */   
/*  48 */   public static final ModelLayerLocation CAMEL = register("camel");
/*     */   
/*  49 */   public static final ModelLayerLocation CAMEL_BABY = register("camel_baby");
/*     */   
/*  50 */   public static final ModelLayerLocation CAMEL_SADDLE = register("camel", "saddle");
/*     */   
/*  51 */   public static final ModelLayerLocation CAMEL_HUSK_SADDLE = register("camel_husk", "saddle");
/*     */   
/*  52 */   public static final ModelLayerLocation CAT = register("cat");
/*     */   
/*  53 */   public static final ModelLayerLocation CAT_BABY = register("cat_baby");
/*     */   
/*  54 */   public static final ModelLayerLocation CAT_BABY_COLLAR = register("cat_baby", "collar");
/*     */   
/*  55 */   public static final ModelLayerLocation CAT_COLLAR = register("cat", "collar");
/*     */   
/*  56 */   public static final ModelLayerLocation CAVE_SPIDER = register("cave_spider");
/*     */   
/*  57 */   public static final ModelLayerLocation CHERRY_BOAT = register("boat/cherry");
/*     */   
/*  58 */   public static final ModelLayerLocation CHERRY_CHEST_BOAT = register("chest_boat/cherry");
/*     */   
/*  59 */   public static final ModelLayerLocation CHEST = register("chest");
/*     */   
/*  60 */   public static final ModelLayerLocation CHEST_MINECART = register("chest_minecart");
/*     */   
/*  61 */   public static final ModelLayerLocation CHICKEN = register("chicken");
/*     */   
/*  62 */   public static final ModelLayerLocation CHICKEN_BABY = register("chicken_baby");
/*     */   
/*  63 */   public static final ModelLayerLocation COD = register("cod");
/*     */   
/*  64 */   public static final ModelLayerLocation COLD_CHICKEN = register("cold_chicken");
/*     */   
/*  65 */   public static final ModelLayerLocation COLD_COW = register("cold_cow");
/*     */   
/*  66 */   public static final ModelLayerLocation COLD_COW_BABY = register("cold_cow_baby");
/*     */   
/*  67 */   public static final ModelLayerLocation COLD_PIG = register("cold_pig");
/*     */   
/*  68 */   public static final ModelLayerLocation COMMAND_BLOCK_MINECART = register("command_block_minecart");
/*     */   
/*  69 */   public static final ModelLayerLocation CONDUIT_CAGE = register("conduit", "cage");
/*     */   
/*  70 */   public static final ModelLayerLocation CONDUIT_EYE = register("conduit", "eye");
/*     */   
/*  71 */   public static final ModelLayerLocation CONDUIT_SHELL = register("conduit", "shell");
/*     */   
/*  72 */   public static final ModelLayerLocation CONDUIT_WIND = register("conduit", "wind");
/*     */   
/*  73 */   public static final ModelLayerLocation COPPER_GOLEM = register("copper_golem");
/*     */   
/*  74 */   public static final ModelLayerLocation COPPER_GOLEM_EYES = register("copper_golem", "eyes");
/*     */   
/*  75 */   public static final ModelLayerLocation COPPER_GOLEM_RUNNING = register("copper_golem_running");
/*     */   
/*  76 */   public static final ModelLayerLocation COPPER_GOLEM_SITTING = register("copper_golem_sitting");
/*     */   
/*  77 */   public static final ModelLayerLocation COPPER_GOLEM_STAR = register("copper_golem_star");
/*     */   
/*  78 */   public static final ModelLayerLocation ZOMBIE_NAUTILUS_CORAL = register("zombie_nautilus_coral");
/*     */   
/*  79 */   public static final ModelLayerLocation COW = register("cow");
/*     */   
/*  80 */   public static final ModelLayerLocation COW_BABY = register("cow_baby");
/*     */   
/*  81 */   public static final ModelLayerLocation CREAKING = register("creaking");
/*     */   
/*  82 */   public static final ModelLayerLocation CREAKING_EYES = register("creaking", "eyes");
/*     */   
/*  83 */   public static final ModelLayerLocation CREEPER = register("creeper");
/*     */   
/*  84 */   public static final ModelLayerLocation CREEPER_ARMOR = register("creeper", "armor");
/*     */   
/*  85 */   public static final ModelLayerLocation CREEPER_HEAD = register("creeper_head");
/*     */   
/*  86 */   public static final ModelLayerLocation DARK_OAK_BOAT = register("boat/dark_oak");
/*     */   
/*  87 */   public static final ModelLayerLocation DARK_OAK_CHEST_BOAT = register("chest_boat/dark_oak");
/*     */   
/*  88 */   public static final ModelLayerLocation DECORATED_POT_BASE = register("decorated_pot_base");
/*     */   
/*  89 */   public static final ModelLayerLocation DECORATED_POT_SIDES = register("decorated_pot_sides");
/*     */   
/*  90 */   public static final ModelLayerLocation DOLPHIN = register("dolphin");
/*     */   
/*  91 */   public static final ModelLayerLocation DOLPHIN_BABY = register("dolphin_baby");
/*     */   
/*  92 */   public static final ModelLayerLocation DONKEY = register("donkey");
/*     */   
/*  93 */   public static final ModelLayerLocation DONKEY_BABY = register("donkey_baby");
/*     */   
/*  94 */   public static final ModelLayerLocation DONKEY_SADDLE = register("donkey", "saddle");
/*     */   
/*  95 */   public static final ModelLayerLocation DOUBLE_CHEST_LEFT = register("double_chest_left");
/*     */   
/*  96 */   public static final ModelLayerLocation DOUBLE_CHEST_RIGHT = register("double_chest_right");
/*     */   
/*  97 */   public static final ModelLayerLocation DRAGON_SKULL = register("dragon_skull");
/*     */   
/*  98 */   public static final ModelLayerLocation DROWNED = register("drowned");
/*     */   
/*  99 */   public static final ModelLayerLocation DROWNED_BABY = register("drowned_baby");
/*     */   
/* 100 */   public static final ArmorModelSet<ModelLayerLocation> DROWNED_BABY_ARMOR = registerArmorSet("drowned_baby");
/*     */   
/* 101 */   public static final ModelLayerLocation DROWNED_BABY_OUTER_LAYER = register("drowned_baby", "outer");
/*     */   
/* 102 */   public static final ArmorModelSet<ModelLayerLocation> DROWNED_ARMOR = registerArmorSet("drowned");
/*     */   
/* 103 */   public static final ModelLayerLocation DROWNED_OUTER_LAYER = register("drowned", "outer");
/*     */   
/* 104 */   public static final ModelLayerLocation ELDER_GUARDIAN = register("elder_guardian");
/*     */   
/* 105 */   public static final ModelLayerLocation ELYTRA = register("elytra");
/*     */   
/* 106 */   public static final ModelLayerLocation ELYTRA_BABY = register("elytra_baby");
/*     */   
/* 107 */   public static final ModelLayerLocation ENDERMAN = register("enderman");
/*     */   
/* 108 */   public static final ModelLayerLocation ENDERMITE = register("endermite");
/*     */   
/* 109 */   public static final ModelLayerLocation ENDER_DRAGON = register("ender_dragon");
/*     */   
/* 110 */   public static final ModelLayerLocation END_CRYSTAL = register("end_crystal");
/*     */   
/* 111 */   public static final ModelLayerLocation EVOKER = register("evoker");
/*     */   
/* 112 */   public static final ModelLayerLocation EVOKER_FANGS = register("evoker_fangs");
/*     */   
/* 113 */   public static final ModelLayerLocation FOX = register("fox");
/*     */   
/* 114 */   public static final ModelLayerLocation FOX_BABY = register("fox_baby");
/*     */   
/* 115 */   public static final ModelLayerLocation FROG = register("frog");
/*     */   
/* 116 */   public static final ModelLayerLocation FURNACE_MINECART = register("furnace_minecart");
/*     */   
/* 117 */   public static final ModelLayerLocation GHAST = register("ghast");
/*     */   
/* 118 */   public static final ModelLayerLocation GIANT = register("giant");
/*     */   
/* 119 */   public static final ArmorModelSet<ModelLayerLocation> GIANT_ARMOR = registerArmorSet("giant");
/*     */   
/* 120 */   public static final ModelLayerLocation GLOW_SQUID = register("glow_squid");
/*     */   
/* 121 */   public static final ModelLayerLocation GLOW_SQUID_BABY = register("glow_squid_baby");
/*     */   
/* 122 */   public static final ModelLayerLocation GOAT = register("goat");
/*     */   
/* 123 */   public static final ModelLayerLocation GOAT_BABY = register("goat_baby");
/*     */   
/* 124 */   public static final ModelLayerLocation GUARDIAN = register("guardian");
/*     */   
/* 125 */   public static final ModelLayerLocation HAPPY_GHAST = register("happy_ghast");
/*     */   
/* 126 */   public static final ModelLayerLocation HAPPY_GHAST_BABY = register("happy_ghast_baby");
/*     */   
/* 127 */   public static final ModelLayerLocation HAPPY_GHAST_HARNESS = register("happy_ghast_harness");
/*     */   
/* 128 */   public static final ModelLayerLocation HAPPY_GHAST_BABY_HARNESS = register("happy_ghast_baby_harness");
/*     */   
/* 129 */   public static final ModelLayerLocation HAPPY_GHAST_ROPES = register("happy_ghast_ropes");
/*     */   
/* 130 */   public static final ModelLayerLocation HAPPY_GHAST_BABY_ROPES = register("happy_ghast_baby_ropes");
/*     */   
/* 131 */   public static final ModelLayerLocation HOGLIN = register("hoglin");
/*     */   
/* 132 */   public static final ModelLayerLocation HOGLIN_BABY = register("hoglin_baby");
/*     */   
/* 133 */   public static final ModelLayerLocation HOPPER_MINECART = register("hopper_minecart");
/*     */   
/* 134 */   public static final ModelLayerLocation HORSE = register("horse");
/*     */   
/* 135 */   public static final ModelLayerLocation HORSE_ARMOR = register("horse_armor");
/*     */   
/* 136 */   public static final ModelLayerLocation HORSE_SADDLE = register("horse", "saddle");
/*     */   
/* 137 */   public static final ModelLayerLocation HORSE_BABY = register("horse_baby");
/*     */   
/* 138 */   public static final ModelLayerLocation HUSK = register("husk");
/*     */   
/* 139 */   public static final ModelLayerLocation HUSK_BABY = register("husk_baby");
/*     */   
/* 140 */   public static final ArmorModelSet<ModelLayerLocation> HUSK_BABY_ARMOR = registerArmorSet("husk_baby");
/*     */   
/* 141 */   public static final ArmorModelSet<ModelLayerLocation> HUSK_ARMOR = registerArmorSet("husk");
/*     */   
/* 142 */   public static final ModelLayerLocation ILLUSIONER = register("illusioner");
/*     */   
/* 143 */   public static final ModelLayerLocation IRON_GOLEM = register("iron_golem");
/*     */   
/* 144 */   public static final ModelLayerLocation JUNGLE_BOAT = register("boat/jungle");
/*     */   
/* 145 */   public static final ModelLayerLocation JUNGLE_CHEST_BOAT = register("chest_boat/jungle");
/*     */   
/* 146 */   public static final ModelLayerLocation LEASH_KNOT = register("leash_knot");
/*     */   
/* 147 */   public static final ModelLayerLocation LLAMA = register("llama");
/*     */   
/* 148 */   public static final ModelLayerLocation LLAMA_BABY = register("llama_baby");
/*     */   
/* 149 */   public static final ModelLayerLocation LLAMA_BABY_DECOR = register("llama_baby", "decor");
/*     */   
/* 150 */   public static final ModelLayerLocation LLAMA_DECOR = register("llama", "decor");
/*     */   
/* 151 */   public static final ModelLayerLocation LLAMA_SPIT = register("llama_spit");
/*     */   
/* 152 */   public static final ModelLayerLocation MAGMA_CUBE = register("magma_cube");
/*     */   
/* 153 */   public static final ModelLayerLocation MANGROVE_BOAT = register("boat/mangrove");
/*     */   
/* 154 */   public static final ModelLayerLocation MANGROVE_CHEST_BOAT = register("chest_boat/mangrove");
/*     */   
/* 155 */   public static final ModelLayerLocation MINECART = register("minecart");
/*     */   
/* 156 */   public static final ModelLayerLocation MOOSHROOM = register("mooshroom");
/*     */   
/* 157 */   public static final ModelLayerLocation MOOSHROOM_BABY = register("mooshroom_baby");
/*     */   
/* 158 */   public static final ModelLayerLocation MULE = register("mule");
/*     */   
/* 159 */   public static final ModelLayerLocation MULE_BABY = register("mule_baby");
/*     */   
/* 160 */   public static final ModelLayerLocation MULE_SADDLE = register("mule", "saddle");
/*     */   
/* 161 */   public static final ModelLayerLocation NAUTILUS = register("nautilus");
/*     */   
/* 162 */   public static final ModelLayerLocation NAUTILUS_BABY = register("nautilus_baby");
/*     */   
/* 163 */   public static final ModelLayerLocation NAUTILUS_SADDLE = register("nautilus", "saddle");
/*     */   
/* 164 */   public static final ModelLayerLocation NAUTILUS_ARMOR = register("nautilus_armor");
/*     */   
/* 165 */   public static final ModelLayerLocation OAK_BOAT = register("boat/oak");
/*     */   
/* 166 */   public static final ModelLayerLocation OAK_CHEST_BOAT = register("chest_boat/oak");
/*     */   
/* 167 */   public static final ModelLayerLocation OCELOT = register("ocelot");
/*     */   
/* 168 */   public static final ModelLayerLocation OCELOT_BABY = register("ocelot_baby");
/*     */   
/* 169 */   public static final ModelLayerLocation PALE_OAK_BOAT = register("boat/pale_oak");
/*     */   
/* 170 */   public static final ModelLayerLocation PALE_OAK_CHEST_BOAT = register("chest_boat/pale_oak");
/*     */   
/* 171 */   public static final ModelLayerLocation PANDA = register("panda");
/*     */   
/* 172 */   public static final ModelLayerLocation PANDA_BABY = register("panda_baby");
/*     */   
/* 173 */   public static final ModelLayerLocation PARCHED = register("parched");
/*     */   
/* 174 */   public static final ArmorModelSet<ModelLayerLocation> PARCHED_ARMOR = registerArmorSet("parched");
/*     */   
/* 175 */   public static final ModelLayerLocation PARCHED_OUTER_LAYER = register("parched", "outer");
/*     */   
/* 176 */   public static final ModelLayerLocation PARROT = register("parrot");
/*     */   
/* 177 */   public static final ModelLayerLocation PHANTOM = register("phantom");
/*     */   
/* 178 */   public static final ModelLayerLocation PIG = register("pig");
/*     */   
/* 179 */   public static final ModelLayerLocation PIGLIN = register("piglin");
/*     */   
/* 180 */   public static final ModelLayerLocation PIGLIN_BABY = register("piglin_baby");
/*     */   
/* 181 */   public static final ArmorModelSet<ModelLayerLocation> PIGLIN_BABY_ARMOR = registerArmorSet("piglin_baby");
/*     */   
/* 182 */   public static final ModelLayerLocation PIGLIN_BRUTE = register("piglin_brute");
/*     */   
/* 183 */   public static final ArmorModelSet<ModelLayerLocation> PIGLIN_BRUTE_ARMOR = registerArmorSet("piglin_brute");
/*     */   
/* 184 */   public static final ModelLayerLocation PIGLIN_HEAD = register("piglin_head");
/*     */   
/* 185 */   public static final ArmorModelSet<ModelLayerLocation> PIGLIN_ARMOR = registerArmorSet("piglin");
/*     */   
/* 186 */   public static final ModelLayerLocation PIG_BABY = register("pig_baby");
/*     */   
/* 187 */   public static final ModelLayerLocation PIG_SADDLE = register("pig", "saddle");
/*     */   
/* 188 */   public static final ModelLayerLocation PILLAGER = register("pillager");
/*     */   
/* 189 */   public static final ModelLayerLocation PLAYER = register("player");
/*     */   
/* 190 */   public static final ModelLayerLocation PLAYER_CAPE = register("player", "cape");
/*     */   
/* 191 */   public static final ModelLayerLocation PLAYER_EARS = register("player", "ears");
/*     */   
/* 192 */   public static final ModelLayerLocation PLAYER_HEAD = register("player_head");
/*     */   
/* 193 */   public static final ArmorModelSet<ModelLayerLocation> PLAYER_ARMOR = registerArmorSet("player");
/*     */   
/* 194 */   public static final ModelLayerLocation PLAYER_SLIM = register("player_slim");
/*     */   
/* 195 */   public static final ArmorModelSet<ModelLayerLocation> PLAYER_SLIM_ARMOR = registerArmorSet("player_slim");
/*     */   
/* 196 */   public static final ModelLayerLocation PLAYER_SPIN_ATTACK = register("spin_attack");
/*     */   
/* 197 */   public static final ModelLayerLocation POLAR_BEAR = register("polar_bear");
/*     */   
/* 198 */   public static final ModelLayerLocation POLAR_BEAR_BABY = register("polar_bear_baby");
/*     */   
/* 199 */   public static final ModelLayerLocation PUFFERFISH_BIG = register("pufferfish_big");
/*     */   
/* 200 */   public static final ModelLayerLocation PUFFERFISH_MEDIUM = register("pufferfish_medium");
/*     */   
/* 201 */   public static final ModelLayerLocation PUFFERFISH_SMALL = register("pufferfish_small");
/*     */   
/* 202 */   public static final ModelLayerLocation RABBIT = register("rabbit");
/*     */   
/* 203 */   public static final ModelLayerLocation RABBIT_BABY = register("rabbit_baby");
/*     */   
/* 204 */   public static final ModelLayerLocation RAVAGER = register("ravager");
/*     */   
/* 205 */   public static final ModelLayerLocation SALMON = register("salmon");
/*     */   
/* 206 */   public static final ModelLayerLocation SALMON_LARGE = register("salmon_large");
/*     */   
/* 207 */   public static final ModelLayerLocation SALMON_SMALL = register("salmon_small");
/*     */   
/* 208 */   public static final ModelLayerLocation SHEEP = register("sheep");
/*     */   
/* 209 */   public static final ModelLayerLocation SHEEP_BABY = register("sheep_baby");
/*     */   
/* 210 */   public static final ModelLayerLocation SHEEP_BABY_WOOL = register("sheep_baby", "wool");
/*     */   
/* 211 */   public static final ModelLayerLocation SHEEP_WOOL = register("sheep", "wool");
/*     */   
/* 212 */   public static final ModelLayerLocation SHEEP_WOOL_UNDERCOAT = register("sheep", "wool_undercoat");
/*     */   
/* 213 */   public static final ModelLayerLocation SHIELD = register("shield");
/*     */   
/* 214 */   public static final ModelLayerLocation SHULKER = register("shulker");
/*     */   
/* 215 */   public static final ModelLayerLocation SHULKER_BOX = register("shulker_box");
/*     */   
/* 216 */   public static final ModelLayerLocation SHULKER_BULLET = register("shulker_bullet");
/*     */   
/* 217 */   public static final ModelLayerLocation SILVERFISH = register("silverfish");
/*     */   
/* 218 */   public static final ModelLayerLocation SKELETON = register("skeleton");
/*     */   
/* 219 */   public static final ModelLayerLocation SKELETON_HORSE = register("skeleton_horse");
/*     */   
/* 220 */   public static final ModelLayerLocation SKELETON_HORSE_BABY = register("skeleton_horse_baby");
/*     */   
/* 221 */   public static final ModelLayerLocation SKELETON_HORSE_SADDLE = register("skeleton_horse", "saddle");
/*     */   
/* 222 */   public static final ArmorModelSet<ModelLayerLocation> SKELETON_ARMOR = registerArmorSet("skeleton");
/*     */   
/* 223 */   public static final ModelLayerLocation SKELETON_SKULL = register("skeleton_skull");
/*     */   
/* 224 */   public static final ModelLayerLocation SLIME = register("slime");
/*     */   
/* 225 */   public static final ModelLayerLocation SLIME_OUTER = register("slime", "outer");
/*     */   
/* 226 */   public static final ModelLayerLocation SNIFFER = register("sniffer");
/*     */   
/* 227 */   public static final ModelLayerLocation SNIFFER_BABY = register("sniffer_baby");
/*     */   
/* 228 */   public static final ModelLayerLocation SNOW_GOLEM = register("snow_golem");
/*     */   
/* 229 */   public static final ModelLayerLocation SPAWNER_MINECART = register("spawner_minecart");
/*     */   
/* 230 */   public static final ModelLayerLocation SPIDER = register("spider");
/*     */   
/* 231 */   public static final ModelLayerLocation SPRUCE_BOAT = register("boat/spruce");
/*     */   
/* 232 */   public static final ModelLayerLocation SPRUCE_CHEST_BOAT = register("chest_boat/spruce");
/*     */   
/* 233 */   public static final ModelLayerLocation SQUID = register("squid");
/*     */   
/* 234 */   public static final ModelLayerLocation SQUID_BABY = register("squid_baby");
/*     */   
/* 235 */   public static final ModelLayerLocation STRAY = register("stray");
/*     */   
/* 236 */   public static final ArmorModelSet<ModelLayerLocation> STRAY_ARMOR = registerArmorSet("stray");
/*     */   
/* 237 */   public static final ModelLayerLocation STRAY_OUTER_LAYER = register("stray", "outer");
/*     */   
/* 238 */   public static final ModelLayerLocation STRIDER = register("strider");
/*     */   
/* 239 */   public static final ModelLayerLocation STRIDER_SADDLE = register("strider", "saddle");
/*     */   
/* 240 */   public static final ModelLayerLocation STRIDER_BABY = register("strider_baby");
/*     */   
/* 241 */   public static final ModelLayerLocation SULFUR_CUBE = register("sulfur_cube");
/*     */   
/* 242 */   public static final ModelLayerLocation SULFUR_CUBE_INNER = register("sulfur_cube", "inner");
/*     */   
/* 243 */   public static final ModelLayerLocation SULFUR_CUBE_SMALL = register("sulfur_cube_small");
/*     */   
/* 244 */   public static final ModelLayerLocation SULFUR_CUBE_SMALL_INNER = register("sulfur_cube_small", "inner");
/*     */   
/* 245 */   public static final ModelLayerLocation TADPOLE = register("tadpole");
/*     */   
/* 246 */   public static final ModelLayerLocation TNT_MINECART = register("tnt_minecart");
/*     */   
/* 247 */   public static final ModelLayerLocation TRADER_LLAMA = register("trader_llama");
/*     */   
/* 248 */   public static final ModelLayerLocation TRADER_LLAMA_BABY = register("trader_llama_baby");
/*     */   
/* 249 */   public static final ModelLayerLocation TRIDENT = register("trident");
/*     */   
/* 250 */   public static final ModelLayerLocation TROPICAL_FISH_LARGE = register("tropical_fish_large");
/*     */   
/* 251 */   public static final ModelLayerLocation TROPICAL_FISH_LARGE_PATTERN = register("tropical_fish_large", "pattern");
/*     */   
/* 252 */   public static final ModelLayerLocation TROPICAL_FISH_SMALL = register("tropical_fish_small");
/*     */   
/* 253 */   public static final ModelLayerLocation TROPICAL_FISH_SMALL_PATTERN = register("tropical_fish_small", "pattern");
/*     */   
/* 254 */   public static final ModelLayerLocation TURTLE = register("turtle");
/*     */   
/* 255 */   public static final ModelLayerLocation TURTLE_BABY = register("turtle_baby");
/*     */   
/* 256 */   public static final ModelLayerLocation UNDEAD_HORSE_ARMOR = register("undead_horse_armor");
/*     */   
/* 257 */   public static final ModelLayerLocation VEX = register("vex");
/*     */   
/* 258 */   public static final ModelLayerLocation VILLAGER = register("villager");
/*     */   
/* 259 */   public static final ModelLayerLocation VILLAGER_NO_HAT = register("villager_no_hat");
/*     */   
/* 260 */   public static final ModelLayerLocation VILLAGER_BABY = register("villager_baby");
/*     */   
/* 261 */   public static final ModelLayerLocation VILLAGER_BABY_NO_HAT = register("villager_baby_no_hat");
/*     */   
/* 262 */   public static final ModelLayerLocation VINDICATOR = register("vindicator");
/*     */   
/* 263 */   public static final ModelLayerLocation WANDERING_TRADER = register("wandering_trader");
/*     */   
/* 264 */   public static final ModelLayerLocation WARDEN = register("warden");
/*     */   
/* 265 */   public static final ModelLayerLocation WARDEN_BIOLUMINESCENT = register("warden", "bioluminescent");
/*     */   
/* 266 */   public static final ModelLayerLocation WARDEN_PULSATING_SPOTS = register("warden", "pulsating_spots");
/*     */   
/* 267 */   public static final ModelLayerLocation WARDEN_TENDRILS = register("warden", "tendrils");
/*     */   
/* 268 */   public static final ModelLayerLocation WARDEN_HEART = register("warden", "heart");
/*     */   
/* 269 */   public static final ModelLayerLocation WARM_COW = register("warm_cow");
/*     */   
/* 270 */   public static final ModelLayerLocation WARM_COW_BABY = register("warm_cow_baby");
/*     */   
/* 271 */   public static final ModelLayerLocation WIND_CHARGE = register("wind_charge");
/*     */   
/* 272 */   public static final ModelLayerLocation WITCH = register("witch");
/*     */   
/* 273 */   public static final ModelLayerLocation WITHER = register("wither");
/*     */   
/* 274 */   public static final ModelLayerLocation WITHER_ARMOR = register("wither", "armor");
/*     */   
/* 275 */   public static final ModelLayerLocation WITHER_SKELETON = register("wither_skeleton");
/*     */   
/* 276 */   public static final ArmorModelSet<ModelLayerLocation> WITHER_SKELETON_ARMOR = registerArmorSet("wither_skeleton");
/*     */   
/* 277 */   public static final ModelLayerLocation WITHER_SKELETON_SKULL = register("wither_skeleton_skull");
/*     */   
/* 278 */   public static final ModelLayerLocation WITHER_SKULL = register("wither_skull");
/*     */   
/* 279 */   public static final ModelLayerLocation WOLF = register("wolf");
/*     */   
/* 280 */   public static final ModelLayerLocation WOLF_ARMOR = register("wolf_armor");
/*     */   
/* 281 */   public static final ModelLayerLocation WOLF_BABY = register("wolf_baby");
/*     */   
/* 282 */   public static final ModelLayerLocation ZOGLIN = register("zoglin");
/*     */   
/* 283 */   public static final ModelLayerLocation ZOGLIN_BABY = register("zoglin_baby");
/*     */   
/* 284 */   public static final ModelLayerLocation ZOMBIE = register("zombie");
/*     */   
/* 285 */   public static final ModelLayerLocation ZOMBIE_BABY = register("zombie_baby");
/*     */   
/* 286 */   public static final ArmorModelSet<ModelLayerLocation> ZOMBIE_BABY_ARMOR = registerArmorSet("zombie_baby");
/*     */   
/* 287 */   public static final ModelLayerLocation ZOMBIE_HEAD = register("zombie_head");
/*     */   
/* 288 */   public static final ModelLayerLocation ZOMBIE_HORSE = register("zombie_horse");
/*     */   
/* 289 */   public static final ModelLayerLocation ZOMBIE_HORSE_BABY = register("zombie_horse_baby");
/*     */   
/* 290 */   public static final ModelLayerLocation ZOMBIE_HORSE_SADDLE = register("zombie_horse", "saddle");
/*     */   
/* 291 */   public static final ArmorModelSet<ModelLayerLocation> ZOMBIE_ARMOR = registerArmorSet("zombie");
/*     */   
/* 292 */   public static final ModelLayerLocation ZOMBIE_VILLAGER = register("zombie_villager");
/*     */   
/* 293 */   public static final ModelLayerLocation ZOMBIE_VILLAGER_NO_HAT = register("zombie_villager_no_hat");
/*     */   
/* 294 */   public static final ModelLayerLocation ZOMBIE_VILLAGER_BABY = register("zombie_villager_baby");
/*     */   
/* 295 */   public static final ModelLayerLocation ZOMBIE_VILLAGER_BABY_NO_HAT = register("zombie_villager_baby_no_hat");
/*     */   
/* 296 */   public static final ArmorModelSet<ModelLayerLocation> ZOMBIE_VILLAGER_BABY_ARMOR = registerArmorSet("zombie_villager_baby");
/*     */   
/* 297 */   public static final ArmorModelSet<ModelLayerLocation> ZOMBIE_VILLAGER_ARMOR = registerArmorSet("zombie_villager");
/*     */   
/* 298 */   public static final ModelLayerLocation ZOMBIFIED_PIGLIN = register("zombified_piglin");
/*     */   
/* 299 */   public static final ModelLayerLocation ZOMBIFIED_PIGLIN_BABY = register("zombified_piglin_baby");
/*     */   
/* 300 */   public static final ArmorModelSet<ModelLayerLocation> ZOMBIFIED_PIGLIN_BABY_ARMOR = registerArmorSet("zombified_piglin_baby");
/*     */   
/* 301 */   public static final ArmorModelSet<ModelLayerLocation> ZOMBIFIED_PIGLIN_ARMOR = registerArmorSet("zombified_piglin");
/*     */   
/* 302 */   public static final ModelLayerLocation ZOMBIE_NAUTILUS = register("zombie_nautilus");
/*     */   
/*     */   private static ModelLayerLocation register(String model) {
/* 305 */     return register(model, "main");
/*     */   }
/*     */   
/*     */   private static ModelLayerLocation register(String model, String layer) {
/* 309 */     ModelLayerLocation result = createLocation(model, layer);
/* 310 */     if (!ALL_MODELS.add(result))
/* 311 */       throw new IllegalStateException("Duplicate registration for " + String.valueOf(result)); 
/* 313 */     return result;
/*     */   }
/*     */   
/*     */   private static ModelLayerLocation createLocation(String model, String layer) {
/* 317 */     return new ModelLayerLocation(Identifier.withDefaultNamespace(model), layer);
/*     */   }
/*     */   
/*     */   private static ArmorModelSet<ModelLayerLocation> registerArmorSet(String modelId) {
/* 321 */     return new ArmorModelSet(
/* 322 */         register(modelId, "helmet"), 
/* 323 */         register(modelId, "chestplate"), 
/* 324 */         register(modelId, "leggings"), 
/* 325 */         register(modelId, "boots"));
/*     */   }
/*     */   
/*     */   public static Stream<ModelLayerLocation> getKnownLocations() {
/* 330 */     return ALL_MODELS.stream();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\geom\ModelLayers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */