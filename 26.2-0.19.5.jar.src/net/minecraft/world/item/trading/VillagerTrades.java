/*      */ package net.minecraft.world.item.trading;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Optional;
/*      */ import net.minecraft.advancements.predicates.DataComponentMatchers;
/*      */ import net.minecraft.advancements.predicates.EnchantmentPredicate;
/*      */ import net.minecraft.advancements.predicates.ItemPredicate;
/*      */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*      */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderGetter;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.component.DataComponentExactPredicate;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*      */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*      */ import net.minecraft.core.component.predicates.EnchantmentsPredicate;
/*      */ import net.minecraft.core.component.predicates.VillagerTypePredicate;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.data.worldgen.BootstrapContext;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.tags.EnchantmentTags;
/*      */ import net.minecraft.tags.PotionTags;
/*      */ import net.minecraft.tags.StructureTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.npc.villager.VillagerType;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStackTemplate;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.alchemy.Potion;
/*      */ import net.minecraft.world.item.alchemy.PotionContents;
/*      */ import net.minecraft.world.item.alchemy.Potions;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.block.ColorCollection;
/*      */ import net.minecraft.world.level.levelgen.structure.Structure;
/*      */ import net.minecraft.world.level.saveddata.maps.MapDecorationType;
/*      */ import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
/*      */ import net.minecraft.world.level.storage.loot.LootContext;
/*      */ import net.minecraft.world.level.storage.loot.functions.DiscardItem;
/*      */ import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.FilteredFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetRandomDyesFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetRandomPotionFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.Sum;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*      */ 
/*      */ public class VillagerTrades {
/*   63 */   public static final ResourceKey<VillagerTrade> FARMER_1_WHEAT_EMERALD = resourceKey("farmer/1/wheat_emerald");
/*      */   
/*   64 */   public static final ResourceKey<VillagerTrade> FARMER_1_POTATO_EMERALD = resourceKey("farmer/1/potato_emerald");
/*      */   
/*   65 */   public static final ResourceKey<VillagerTrade> FARMER_1_CARROT_EMERALD = resourceKey("farmer/1/carrot_emerald");
/*      */   
/*   66 */   public static final ResourceKey<VillagerTrade> FARMER_1_BEETROOT_EMERALD = resourceKey("farmer/1/beetroot_emerald");
/*      */   
/*   67 */   public static final ResourceKey<VillagerTrade> FARMER_1_EMERALD_BREAD = resourceKey("farmer/1/emerald_bread");
/*      */   
/*   68 */   public static final ResourceKey<VillagerTrade> FARMER_2_PUMPKIN_EMERALD = resourceKey("farmer/2/pumpkin_emerald");
/*      */   
/*   69 */   public static final ResourceKey<VillagerTrade> FARMER_2_EMERALD_PUMPKIN_PIE = resourceKey("farmer/2/emerald_pumpkin_pie");
/*      */   
/*   70 */   public static final ResourceKey<VillagerTrade> FARMER_2_EMERALD_APPLE = resourceKey("farmer/2/emerald_apple");
/*      */   
/*   71 */   public static final ResourceKey<VillagerTrade> FARMER_3_EMERALD_COOKIE = resourceKey("farmer/3/emerald_cookie");
/*      */   
/*   72 */   public static final ResourceKey<VillagerTrade> FARMER_3_MELON_EMERALD = resourceKey("farmer/3/melon_emerald");
/*      */   
/*   73 */   public static final ResourceKey<VillagerTrade> FARMER_4_EMERALD_CAKE = resourceKey("farmer/4/emerald_cake");
/*      */   
/*   74 */   public static final ResourceKey<VillagerTrade> FARMER_4_EMERALD_SUSPICIOUS_STEW = resourceKey("farmer/4/emerald_suspicious_stew");
/*      */   
/*   75 */   public static final ResourceKey<VillagerTrade> FARMER_5_EMERALD_GOLDEN_CARROT = resourceKey("farmer/5/emerald_golden_carrot");
/*      */   
/*   76 */   public static final ResourceKey<VillagerTrade> FARMER_5_EMERALD_GLISTENING_MELON_SLICE = resourceKey("farmer/5/emerald_glistening_melon_slice");
/*      */   
/*   78 */   public static final ResourceKey<VillagerTrade> FISHERMAN_1_STRING_EMERALD = resourceKey("fisherman/1/string_emerald");
/*      */   
/*   79 */   public static final ResourceKey<VillagerTrade> FISHERMAN_1_COAL_EMERALD = resourceKey("fisherman/1/coal_emerald");
/*      */   
/*   80 */   public static final ResourceKey<VillagerTrade> FISHERMAN_1_RAW_COD_AND_EMERALD_COOKED_COD = resourceKey("fisherman/1/raw_cod_and_emerald_cooked_cod");
/*      */   
/*   81 */   public static final ResourceKey<VillagerTrade> FISHERMAN_1_EMERALD_COD_BUCKET = resourceKey("fisherman/1/emerald_cod_bucket");
/*      */   
/*   82 */   public static final ResourceKey<VillagerTrade> FISHERMAN_2_COD_EMERALD = resourceKey("fisherman/2/cod_emerald");
/*      */   
/*   83 */   public static final ResourceKey<VillagerTrade> FISHERMAN_2_SALMON_AND_EMERALD_COOKED_SALMON = resourceKey("fisherman/2/salmon_and_emerald_cooked_salmon");
/*      */   
/*   84 */   public static final ResourceKey<VillagerTrade> FISHERMAN_2_EMERALD_CAMPFIRE = resourceKey("fisherman/2/emerald_campfire");
/*      */   
/*   85 */   public static final ResourceKey<VillagerTrade> FISHERMAN_3_SALMON_EMERALD = resourceKey("fisherman/3/salmon_emerald");
/*      */   
/*   86 */   public static final ResourceKey<VillagerTrade> FISHERMAN_3_EMERALD_ENCHANTED_FISHING_ROD = resourceKey("fisherman/3/emerald_enchanted_fishing_rod");
/*      */   
/*   87 */   public static final ResourceKey<VillagerTrade> FISHERMAN_4_TROPICAL_FISH_EMERALD = resourceKey("fisherman/4/tropical_fish_emerald");
/*      */   
/*   88 */   public static final ResourceKey<VillagerTrade> FISHERMAN_5_PUFFERFISH_EMERALD = resourceKey("fisherman/5/pufferfish_emerald");
/*      */   
/*   89 */   public static final ResourceKey<VillagerTrade> FISHERMAN_5_OAK_BOAT_EMERALD = resourceKey("fisherman/5/oak_boat_emerald");
/*      */   
/*   90 */   public static final ResourceKey<VillagerTrade> FISHERMAN_5_SPRUCE_BOAT_EMERALD = resourceKey("fisherman/5/spruce_boat_emerald");
/*      */   
/*   91 */   public static final ResourceKey<VillagerTrade> FISHERMAN_5_JUNGLE_BOAT_EMERALD = resourceKey("fisherman/5/jungle_boat_emerald");
/*      */   
/*   92 */   public static final ResourceKey<VillagerTrade> FISHERMAN_5_ACACIA_BOAT_EMERALD = resourceKey("fisherman/5/acacia_boat_emerald");
/*      */   
/*   93 */   public static final ResourceKey<VillagerTrade> FISHERMAN_5_DARK_OAK_BOAT_EMERALD = resourceKey("fisherman/5/dark_oak_boat_emerald");
/*      */   
/*   95 */   public static final ResourceKey<VillagerTrade> SHEPHERD_1_WHITE_WOOL_EMERALD = resourceKey("shepherd/1/white_wool_emerald");
/*      */   
/*   96 */   public static final ResourceKey<VillagerTrade> SHEPHERD_1_BROWN_WOOL_EMERALD = resourceKey("shepherd/1/brown_wool_emerald");
/*      */   
/*   97 */   public static final ResourceKey<VillagerTrade> SHEPHERD_1_GRAY_WOOL_EMERALD = resourceKey("shepherd/1/gray_wool_emerald");
/*      */   
/*   98 */   public static final ResourceKey<VillagerTrade> SHEPHERD_1_BLACK_WOOL_EMERALD = resourceKey("shepherd/1/black_wool_emerald");
/*      */   
/*   99 */   public static final ResourceKey<VillagerTrade> SHEPHERD_1_EMERALD_SHEARS = resourceKey("shepherd/1/emerald_shears");
/*      */   
/*  100 */   public static final ResourceKey<VillagerTrade> SHEPHERD_2_WHITE_DYE_EMERALD = resourceKey("shepherd/2/white_dye_emerald");
/*      */   
/*  101 */   public static final ResourceKey<VillagerTrade> SHEPHERD_2_GRAY_DYE_EMERALD = resourceKey("shepherd/2/gray_dye_emerald");
/*      */   
/*  102 */   public static final ResourceKey<VillagerTrade> SHEPHERD_2_BLACK_DYE_EMERALD = resourceKey("shepherd/2/black_dye_emerald");
/*      */   
/*  103 */   public static final ResourceKey<VillagerTrade> SHEPHERD_2_LIGHT_BLUE_DYE_EMERALD = resourceKey("shepherd/2/light_blue_dye_emerald");
/*      */   
/*  104 */   public static final ResourceKey<VillagerTrade> SHEPHERD_2_LIME_DYE_EMERALD = resourceKey("shepherd/2/lime_dye_emerald");
/*      */   
/*      */   public static final ColorCollection<ResourceKey<VillagerTrade>> SHEPHERD_2_EMERALD_WOOL;
/*      */   
/*      */   public static final ColorCollection<ResourceKey<VillagerTrade>> SHEPHERD_2_EMERALD_CARPETS;
/*      */   
/*      */   static {
/*  105 */     SHEPHERD_2_EMERALD_WOOL = ColorCollection.NAMES.map(color -> resourceKey("shepherd/2/emerald_" + color + "_wool"));
/*  106 */     SHEPHERD_2_EMERALD_CARPETS = ColorCollection.NAMES.map(color -> resourceKey("shepherd/2/emerald_" + color + "_carpet"));
/*      */   }
/*      */   
/*  107 */   public static final ResourceKey<VillagerTrade> SHEPHERD_3_YELLOW_DYE_EMERALD = resourceKey("shepherd/3/yellow_dye_emerald");
/*      */   
/*  108 */   public static final ResourceKey<VillagerTrade> SHEPHERD_3_LIGHT_GRAY_DYE_EMERALD = resourceKey("shepherd/3/light_gray_dye_emerald");
/*      */   
/*  109 */   public static final ResourceKey<VillagerTrade> SHEPHERD_3_ORANGE_DYE_EMERALD = resourceKey("shepherd/3/orange_dye_emerald");
/*      */   
/*  110 */   public static final ResourceKey<VillagerTrade> SHEPHERD_3_RED_DYE_EMERALD = resourceKey("shepherd/3/red_dye_emerald");
/*      */   
/*  111 */   public static final ResourceKey<VillagerTrade> SHEPHERD_3_PINK_DYE_EMERALD = resourceKey("shepherd/3/pink_dye_emerald");
/*      */   
/*      */   public static final ColorCollection<ResourceKey<VillagerTrade>> SHEPHERD_3_EMERALD_BED;
/*      */   
/*      */   static {
/*  112 */     SHEPHERD_3_EMERALD_BED = ColorCollection.NAMES.map(color -> resourceKey("shepherd/3/emerald_" + color + "_bed"));
/*      */   }
/*      */   
/*  113 */   public static final ResourceKey<VillagerTrade> SHEPHERD_4_BROWN_DYE_EMERALD = resourceKey("shepherd/4/brown_dye_emerald");
/*      */   
/*  114 */   public static final ResourceKey<VillagerTrade> SHEPHERD_4_PURPLE_DYE_EMERALD = resourceKey("shepherd/4/purple_dye_emerald");
/*      */   
/*  115 */   public static final ResourceKey<VillagerTrade> SHEPHERD_4_BLUE_DYE_EMERALD = resourceKey("shepherd/4/blue_dye_emerald");
/*      */   
/*  116 */   public static final ResourceKey<VillagerTrade> SHEPHERD_4_GREEN_DYE_EMERALD = resourceKey("shepherd/4/green_dye_emerald");
/*      */   
/*  117 */   public static final ResourceKey<VillagerTrade> SHEPHERD_4_MAGENTA_DYE_EMERALD = resourceKey("shepherd/4/magenta_dye_emerald");
/*      */   
/*  118 */   public static final ResourceKey<VillagerTrade> SHEPHERD_4_CYAN_DYE_EMERALD = resourceKey("shepherd/4/cyan_dye_emerald");
/*      */   
/*      */   public static final ColorCollection<ResourceKey<VillagerTrade>> SHEPHERD_4_EMERALD_BANNER;
/*      */   
/*      */   static {
/*  119 */     SHEPHERD_4_EMERALD_BANNER = ColorCollection.NAMES.map(color -> resourceKey("shepherd/4/emerald_" + color + "_banner"));
/*      */   }
/*      */   
/*  120 */   public static final ResourceKey<VillagerTrade> SHEPHERD_5_EMERALD_PAINTING = resourceKey("shepherd/5/emerald_painting");
/*      */   
/*  122 */   public static final ResourceKey<VillagerTrade> FLETCHER_1_STICK_EMERALD = resourceKey("fletcher/1/stick_emerald");
/*      */   
/*  123 */   public static final ResourceKey<VillagerTrade> FLETCHER_1_EMERALD_ARROW = resourceKey("fletcher/1/emerald_arrow");
/*      */   
/*  124 */   public static final ResourceKey<VillagerTrade> FLETCHER_1_GRAVEL_AND_EMERALD_FLINT = resourceKey("fletcher/1/gravel_and_emerald_flint");
/*      */   
/*  125 */   public static final ResourceKey<VillagerTrade> FLETCHER_2_FLINT_EMERALD = resourceKey("fletcher/2/flint_emerald");
/*      */   
/*  126 */   public static final ResourceKey<VillagerTrade> FLETCHER_2_EMERALD_BOW = resourceKey("fletcher/2/emerald_bow");
/*      */   
/*  127 */   public static final ResourceKey<VillagerTrade> FLETCHER_3_STRING_EMERALD = resourceKey("fletcher/3/string_emerald");
/*      */   
/*  128 */   public static final ResourceKey<VillagerTrade> FLETCHER_3_EMERALD_CROSSBOW = resourceKey("fletcher/3/emerald_crossbow");
/*      */   
/*  129 */   public static final ResourceKey<VillagerTrade> FLETCHER_4_FEATHER_EMERALD = resourceKey("fletcher/4/feather_emerald");
/*      */   
/*  130 */   public static final ResourceKey<VillagerTrade> FLETCHER_4_EMERALD_ENCHANTED_BOW = resourceKey("fletcher/4/emerald_enchanted_bow");
/*      */   
/*  131 */   public static final ResourceKey<VillagerTrade> FLETCHER_5_TRIPWIRE_HOOK_EMERALD = resourceKey("fletcher/5/tripwire_hook_emerald");
/*      */   
/*  132 */   public static final ResourceKey<VillagerTrade> FLETCHER_5_EMERALD_ENCHANTED_CROSSBOW = resourceKey("fletcher/5/emerald_enchanted_crossbow");
/*      */   
/*  133 */   public static final ResourceKey<VillagerTrade> FLETCHER_5_ARROW_AND_EMERALD_TIPPED_ARROW = resourceKey("fletcher/5/arrow_and_emerald_tipped_arrow");
/*      */   
/*  135 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_1_PAPER_EMERALD = resourceKey("librarian/1/paper_emerald");
/*      */   
/*  136 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_1_EMERALD_AND_BOOK_ENCHANTED_BOOK = resourceKey("librarian/1/emerald_and_book_enchanted_book");
/*      */   
/*  137 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_1_EMERALD_BOOKSHELF = resourceKey("librarian/1/emerald_bookshelf");
/*      */   
/*  138 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_2_BOOK_EMERALD = resourceKey("librarian/2/book_emerald");
/*      */   
/*  139 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_2_EMERALD_AND_BOOK_ENCHANTED_BOOK = resourceKey("librarian/2/emerald_and_book_enchanted_book");
/*      */   
/*  140 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_2_EMERALD_LANTERN = resourceKey("librarian/2/emerald_lantern");
/*      */   
/*  141 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_3_INK_SAC_EMERALD = resourceKey("librarian/3/ink_sac_emerald");
/*      */   
/*  142 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_3_EMERALD_AND_BOOK_ENCHANTED_BOOK = resourceKey("librarian/3/emerald_and_book_enchanted_book");
/*      */   
/*  143 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_3_EMERALD_GLASS = resourceKey("librarian/3/emerald_glass");
/*      */   
/*  144 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_4_WRITABLE_BOOK_EMERALD = resourceKey("librarian/4/writable_book_emerald");
/*      */   
/*  145 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_4_EMERALD_AND_BOOK_ENCHANTED_BOOK = resourceKey("librarian/4/emerald_book_and_enchanted_book");
/*      */   
/*  146 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_4_EMERALD_CLOCK = resourceKey("librarian/4/emerald_clock");
/*      */   
/*  147 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_4_EMERALD_COMPASS = resourceKey("librarian/4/emerald_compass");
/*      */   
/*  148 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_5_EMERALD_YELLOW_CANDLE = resourceKey("librarian/5/emerald_yellow_candle");
/*      */   
/*  149 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_5_EMERALD_RED_CANDLE = resourceKey("librarian/5/emerald_red_candle");
/*      */   
/*  151 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_1_PAPER_EMERALD = resourceKey("cartographer/1/paper_emerald");
/*      */   
/*  152 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_1_EMERALD_MAP = resourceKey("cartographer/1/emerald_map");
/*      */   
/*  153 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_2_GLASS_PANE_EMERALD = resourceKey("cartographer/2/glass_pane_emerald");
/*      */   
/*  154 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_TAIGA_MAP = resourceKey("cartographer/2/emerald_and_compass_village_taiga_map");
/*      */   
/*  155 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_2_EMERALD_AND_COMPASS_EXPLORER_SWAMP_MAP = resourceKey("cartographer/2/emerald_and_compass_explorer_swamp_map");
/*      */   
/*  156 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_SNOWY_MAP = resourceKey("cartographer/2/emerald_and_compass_village_snowy_map");
/*      */   
/*  157 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_SAVANNA_MAP = resourceKey("cartographer/2/emerald_and_compass_village_savanna_map");
/*      */   
/*  158 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_PLAINS_MAP = resourceKey("cartographer/2/emerald_and_compass_village_plains_map");
/*      */   
/*  159 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_2_EMERALD_AND_COMPASS_EXPLORER_JUNGLE_MAP = resourceKey("cartographer/2/emerald_and_compass_explorer_jungle_map");
/*      */   
/*  160 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_DESERT_MAP = resourceKey("cartographer/2/emerald_and_compass_village_desert_map");
/*      */   
/*  161 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_3_COMPASS_EMERALD = resourceKey("cartographer/3/compass_emerald");
/*      */   
/*  162 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_3_EMERALD_AND_COMPASS_OCEAN_EXPLORER_MAP = resourceKey("cartographer/3/emerald_and_compass_ocean_explorer_map");
/*      */   
/*  163 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_3_EMERALD_AND_COMPASS_TRIAL_CHAMBER_MAP = resourceKey("cartographer/3/emerald_and_compass_trial_chamber_map");
/*      */   
/*  164 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_ITEM_FRAME = resourceKey("cartographer/4/emerald_item_frame");
/*      */   
/*  165 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_WHITE_BANNER = resourceKey("cartographer/4/emerald_white_banner");
/*      */   
/*  166 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_ORANGE_BANNER = resourceKey("cartographer/4/emerald_orange_banner");
/*      */   
/*  167 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_MAGENTA_BANNER = resourceKey("cartographer/4/emerald_magenta_banner");
/*      */   
/*  168 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_BLUE_BANNER = resourceKey("cartographer/4/emerald_blue_banner");
/*      */   
/*  169 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_LIGHT_BLUE_BANNER = resourceKey("cartographer/4/emerald_light_blue_banner");
/*      */   
/*  170 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_YELLOW_BANNER = resourceKey("cartographer/4/emerald_yellow_banner");
/*      */   
/*  171 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_LIME_BANNER = resourceKey("cartographer/4/emerald_lime_banner");
/*      */   
/*  172 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_PINK_BANNER = resourceKey("cartographer/4/emerald_pink_banner");
/*      */   
/*  173 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_GRAY_BANNER = resourceKey("cartographer/4/emerald_gray_banner");
/*      */   
/*  175 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_CYAN_BANNER = resourceKey("cartographer/4/emerald_cyan_banner");
/*      */   
/*  176 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_PURPLE_BANNER = resourceKey("cartographer/4/emerald_purple_banner");
/*      */   
/*  177 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_BROWN_BANNER = resourceKey("cartographer/4/emerald_brown_banner");
/*      */   
/*  178 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_GREEN_BANNER = resourceKey("cartographer/4/emerald_green_banner");
/*      */   
/*  179 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_RED_BANNER = resourceKey("cartographer/4/emerald_red_banner");
/*      */   
/*  180 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_4_EMERALD_BLACK_BANNER = resourceKey("cartographer/4/emerald_black_banner");
/*      */   
/*  181 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_5_EMERALD_GLOBE_BANNER_PATTERN = resourceKey("cartographer/5/emerald_globe_banner_pattern");
/*      */   
/*  182 */   public static final ResourceKey<VillagerTrade> CARTOGRAPHER_5_EMERALD_AND_COMPASS_WOODLAND_MANSION_MAP = resourceKey("cartographer/5/emerald_and_compass_woodland_mansion_map");
/*      */   
/*  184 */   public static final ResourceKey<VillagerTrade> CLERIC_1_ROTTEN_FLESH_EMERALD = resourceKey("cleric/1/rotten_flesh_emerald");
/*      */   
/*  185 */   public static final ResourceKey<VillagerTrade> CLERIC_1_EMERALD_REDSTONE = resourceKey("cleric/1/emerald_redstone");
/*      */   
/*  186 */   public static final ResourceKey<VillagerTrade> CLERIC_2_GOLD_INGOT_EMERALD = resourceKey("cleric/2/gold_ingot_emerald");
/*      */   
/*  187 */   public static final ResourceKey<VillagerTrade> CLERIC_2_EMERALD_LAPIS_LAZULI = resourceKey("cleric/2/emerald_lapis_lazuli");
/*      */   
/*  188 */   public static final ResourceKey<VillagerTrade> CLERIC_3_RABBIT_FOOT_EMERALD = resourceKey("cleric/3/rabbit_foot_emerald");
/*      */   
/*  189 */   public static final ResourceKey<VillagerTrade> CLERIC_3_EMERALD_GLOWSTONE = resourceKey("cleric/3/emerald_glowstone");
/*      */   
/*  190 */   public static final ResourceKey<VillagerTrade> CLERIC_4_TURTLE_SCUTE_EMERALD = resourceKey("cleric/4/turtle_scute_emerald");
/*      */   
/*  191 */   public static final ResourceKey<VillagerTrade> CLERIC_4_GLASS_BOTTLE_EMERALD = resourceKey("cleric/4/glass_bottle_emerald");
/*      */   
/*  192 */   public static final ResourceKey<VillagerTrade> CLERIC_4_EMERALD_ENDER_PEARL = resourceKey("cleric/4/emerald_ender_pearl");
/*      */   
/*  193 */   public static final ResourceKey<VillagerTrade> CLERIC_5_NETHER_WART_EMERALD = resourceKey("cleric/5/nether_wart_emerald");
/*      */   
/*  194 */   public static final ResourceKey<VillagerTrade> CLERIC_5_EMERALD_EXPERIENCE_BOTTLE = resourceKey("cleric/5/emerald_experience_bottle");
/*      */   
/*  196 */   public static final ResourceKey<VillagerTrade> COMMON_SMITH_1_COAL_EMERALD = resourceKey("smith/1/coal_emerald");
/*      */   
/*  197 */   public static final ResourceKey<VillagerTrade> COMMON_SMITH_2_IRON_INGOT_EMERALD = resourceKey("smith/2/iron_ingot_emerald");
/*      */   
/*  198 */   public static final ResourceKey<VillagerTrade> COMMON_SMITH_2_EMERALD_BELL = resourceKey("smith/2/emerald_bell");
/*      */   
/*  200 */   public static final ResourceKey<VillagerTrade> ARMORER_1_EMERALD_IRON_LEGGINGS = resourceKey("armorer/1/emerald_iron_leggings");
/*      */   
/*  201 */   public static final ResourceKey<VillagerTrade> ARMORER_1_EMERALD_IRON_BOOTS = resourceKey("armorer/1/emerald_iron_boots");
/*      */   
/*  202 */   public static final ResourceKey<VillagerTrade> ARMORER_1_EMERALD_IRON_HELMET = resourceKey("armorer/1/emerald_iron_helmet");
/*      */   
/*  203 */   public static final ResourceKey<VillagerTrade> ARMORER_1_EMERALD_IRON_CHESTPLATE = resourceKey("armorer/1/emerald_iron_chestplate");
/*      */   
/*  205 */   public static final ResourceKey<VillagerTrade> ARMORER_2_EMERALD_CHAINMAIL_BOOTS = resourceKey("armorer/2/emerald_chainmail_boots");
/*      */   
/*  206 */   public static final ResourceKey<VillagerTrade> ARMORER_2_EMERALD_CHAINMAIL_LEGGINGS = resourceKey("armorer/2/emerald_chainmail_leggings");
/*      */   
/*  208 */   public static final ResourceKey<VillagerTrade> ARMORER_3_LAVA_BUCKET_EMERALD = resourceKey("armorer/3/lava_bucket_emerald");
/*      */   
/*  209 */   public static final ResourceKey<VillagerTrade> ARMORER_3_EMERALD_CHAINMAIL_HELMET = resourceKey("armorer/3/emerald_chainmail_helmet");
/*      */   
/*  210 */   public static final ResourceKey<VillagerTrade> ARMORER_3_EMERALD_CHAINMAIL_CHESTPLATE = resourceKey("armorer/3/emerald_chainmail_chestplate");
/*      */   
/*  211 */   public static final ResourceKey<VillagerTrade> ARMORER_3_EMERALD_SHIELD = resourceKey("armorer/3/emerald_shield");
/*      */   
/*  212 */   public static final ResourceKey<VillagerTrade> ARMORER_3_DIAMOND_EMERALD = resourceKey("armorer/3/diamond_emerald");
/*      */   
/*  214 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_DIAMOND_LEGGINGS = resourceKey("armorer/4/emerald_enchanted_diamond_leggings");
/*      */   
/*  215 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_DIAMOND_BOOTS = resourceKey("armorer/4/emerald_enchanted_diamond_boots");
/*      */   
/*  216 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_ENCHANTED_DIAMOND_HELMET = resourceKey("armorer/5/emerald_enchanted_diamond_helmet");
/*      */   
/*  217 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_ENCHANTED_DIAMOND_CHESTPLATE = resourceKey("armorer/5/emerald_enchanted_diamond_chestplate");
/*      */   
/*  219 */   public static final ResourceKey<VillagerTrade> WEAPONSMITH_1_EMERALD_IRON_AXE = resourceKey("weaponsmith/1/emerald_iron_axe");
/*      */   
/*  220 */   public static final ResourceKey<VillagerTrade> WEAPONSMITH_1_EMERALD_ENCHANTED_IRON_SWORD = resourceKey("weaponsmith/1/emerald_enchanted_iron_sword");
/*      */   
/*  221 */   public static final ResourceKey<VillagerTrade> WEAPONSMITH_3_FLINT_EMERALD = resourceKey("weaponsmith/3/flint_emerald");
/*      */   
/*  222 */   public static final ResourceKey<VillagerTrade> WEAPONSMITH_4_DIAMOND_EMERALD = resourceKey("weaponsmith/4/diamond_emerald");
/*      */   
/*  223 */   public static final ResourceKey<VillagerTrade> WEAPONSMITH_4_EMERALD_ENCHANTED_DIAMOND_AXE = resourceKey("weaponsmith/4/emerald_enchanted_diamond_axe");
/*      */   
/*  224 */   public static final ResourceKey<VillagerTrade> WEAPONSMITH_5_EMERALD_ENCHANTED_DIAMOND_SWORD = resourceKey("weaponsmith/5/emerald_enchanted_diamond_sword");
/*      */   
/*  226 */   public static final ResourceKey<VillagerTrade> TOOLSMITH_1_EMERALD_STONE_AXE = resourceKey("toolsmith/1/emerald_stone_axe");
/*      */   
/*  227 */   public static final ResourceKey<VillagerTrade> TOOLSMITH_1_EMERALD_STONE_SHOVEL = resourceKey("toolsmith/1/emerald_stone_shovel");
/*      */   
/*  228 */   public static final ResourceKey<VillagerTrade> TOOLSMITH_1_EMERALD_STONE_PICKAXE = resourceKey("toolsmith/1/emerald_stone_pickaxe");
/*      */   
/*  229 */   public static final ResourceKey<VillagerTrade> TOOLSMITH_1_EMERALD_STONE_HOE = resourceKey("toolsmith/1/emerald_stone_hoe");
/*      */   
/*  230 */   public static final ResourceKey<VillagerTrade> TOOLSMITH_3_FLINT_EMERALD = resourceKey("toolsmith/3/flint_emerald");
/*      */   
/*  231 */   public static final ResourceKey<VillagerTrade> TOOLSMITH_3_EMERALD_IRON_AXE = resourceKey("toolsmith/3/emerald_enchanted_iron_axe");
/*      */   
/*  232 */   public static final ResourceKey<VillagerTrade> TOOLSMITH_3_EMERALD_IRON_SHOVEL = resourceKey("toolsmith/3/emerald_enchanted_iron_shovel");
/*      */   
/*  233 */   public static final ResourceKey<VillagerTrade> TOOLSMITH_3_EMERALD_IRON_PICKAXE = resourceKey("toolsmith/3/emerald_enchanted_iron_pickaxe");
/*      */   
/*  234 */   public static final ResourceKey<VillagerTrade> TOOLSMITH_3_EMERALD_DIAMOND_HOE = resourceKey("toolsmith/3/emerald_diamond_hoe");
/*      */   
/*  235 */   public static final ResourceKey<VillagerTrade> TOOLSMITH_4_DIAMOND_EMERALD = resourceKey("toolsmith/4/diamond_emerald");
/*      */   
/*  236 */   public static final ResourceKey<VillagerTrade> TOOLSMITH_4_EMERALD_DIAMOND_AXE = resourceKey("toolsmith/4/emerald_enchanted_diamond_axe");
/*      */   
/*  237 */   public static final ResourceKey<VillagerTrade> TOOLSMITH_4_EMERALD_DIAMOND_SHOVEL = resourceKey("toolsmith/4/emerald_enchanted_diamond_shovel");
/*      */   
/*  238 */   public static final ResourceKey<VillagerTrade> TOOLSMITH_5_EMERALD_DIAMOND_PICKAXE = resourceKey("toolsmith/5/emerald_enchanted_diamond_pickaxe");
/*      */   
/*  240 */   public static final ResourceKey<VillagerTrade> BUTCHER_1_CHICKEN_EMERALD = resourceKey("butcher/1/chicken_emerald");
/*      */   
/*  241 */   public static final ResourceKey<VillagerTrade> BUTCHER_1_PORKCHOP_EMERALD = resourceKey("butcher/1/porkchop_emerald");
/*      */   
/*  242 */   public static final ResourceKey<VillagerTrade> BUTCHER_1_RABBIT_EMERALD = resourceKey("butcher/1/rabbit_emerald");
/*      */   
/*  243 */   public static final ResourceKey<VillagerTrade> BUTCHER_1_EMERALD_RABBIT_STEW = resourceKey("butcher/1/emerald_rabbit_stew");
/*      */   
/*  244 */   public static final ResourceKey<VillagerTrade> BUTCHER_2_COAL_EMERALD = resourceKey("butcher/2/coal_emerald");
/*      */   
/*  245 */   public static final ResourceKey<VillagerTrade> BUTCHER_2_EMERALD_COOKED_PORKCHOP = resourceKey("butcher/2/emerald_cooked_porkchop");
/*      */   
/*  246 */   public static final ResourceKey<VillagerTrade> BUTCHER_2_EMERALD_COOKED_CHICKEN = resourceKey("butcher/2/emerald_cooked_chicken");
/*      */   
/*  247 */   public static final ResourceKey<VillagerTrade> BUTCHER_3_MUTTON_EMERALD = resourceKey("butcher/3/mutton_emerald");
/*      */   
/*  248 */   public static final ResourceKey<VillagerTrade> BUTCHER_3_BEEF_EMERALD = resourceKey("butcher/3/beef_emerald");
/*      */   
/*  249 */   public static final ResourceKey<VillagerTrade> BUTCHER_4_DRIED_KELP_BLOCK_EMERALD = resourceKey("butcher/4/dried_kelp_block_emerald");
/*      */   
/*  250 */   public static final ResourceKey<VillagerTrade> BUTCHER_5_SWEET_BERRIES_EMERALD = resourceKey("butcher/5/sweet_berries_emerald");
/*      */   
/*  252 */   public static final ResourceKey<VillagerTrade> LEATHERWORKER_1_LEATHER_EMERALD = resourceKey("leatherworker/1/leather_emerald");
/*      */   
/*  253 */   public static final ResourceKey<VillagerTrade> LEATHERWORKER_1_EMERALD_DYED_LEATHER_LEGGINGS = resourceKey("leatherworker/1/emerald_dyed_leather_leggings");
/*      */   
/*  254 */   public static final ResourceKey<VillagerTrade> LEATHERWORKER_1_EMERALD_DYED_LEATHER_CHESTPLATE = resourceKey("leatherworker/1/emerald_dyed_leather_chestplate");
/*      */   
/*  255 */   public static final ResourceKey<VillagerTrade> LEATHERWORKER_2_FLINT_EMERALD = resourceKey("leatherworker/2/flint_emerald");
/*      */   
/*  256 */   public static final ResourceKey<VillagerTrade> LEATHERWORKER_2_EMERALD_DYED_LEATHER_HELMET = resourceKey("leatherworker/2/emerald_dyed_leather_helmet");
/*      */   
/*  257 */   public static final ResourceKey<VillagerTrade> LEATHERWORKER_2_EMERALD_DYED_LEATHER_BOOTS = resourceKey("leatherworker/2/emerald_dyed_leather_boots");
/*      */   
/*  258 */   public static final ResourceKey<VillagerTrade> LEATHERWORKER_3_RABBIT_HIDE_EMERALD = resourceKey("leatherworker/3/rabbit_hide_emerald");
/*      */   
/*  259 */   public static final ResourceKey<VillagerTrade> LEATHERWORKER_3_EMERALD_DYED_LEATHER_CHESTPLATE = resourceKey("leatherworker/3/emerald_dyed_leather_chestplate");
/*      */   
/*  260 */   public static final ResourceKey<VillagerTrade> LEATHERWORKER_4_TURTLE_SCUTE_EMERALD = resourceKey("leatherworker/4/turtle_scute_emerald");
/*      */   
/*  261 */   public static final ResourceKey<VillagerTrade> LEATHERWORKER_4_EMERALD_DYED_LEATHER_HORSE_ARMOR = resourceKey("leatherworker/4/emerald_dyed_leather_horse_armor");
/*      */   
/*  262 */   public static final ResourceKey<VillagerTrade> LEATHERWORKER_5_EMERALD_SADDLE = resourceKey("leatherworker/5/emerald_saddle");
/*      */   
/*  263 */   public static final ResourceKey<VillagerTrade> LEATHERWORKER_5_EMERALD_DYED_LEATHER_HELMET = resourceKey("leatherworker/5/emerald_dyed_leather_helmet");
/*      */   
/*  265 */   public static final ResourceKey<VillagerTrade> MASON_1_CLAY_BALL_EMERALD = resourceKey("mason/1/clay_ball_emerald");
/*      */   
/*  266 */   public static final ResourceKey<VillagerTrade> MASON_1_EMERALD_BRICK = resourceKey("mason/1/emerald_brick");
/*      */   
/*  267 */   public static final ResourceKey<VillagerTrade> MASON_2_STONE_EMERALD = resourceKey("mason/2/stone_emerald");
/*      */   
/*  268 */   public static final ResourceKey<VillagerTrade> MASON_2_EMERALD_CHISELED_STONE_BRICKS = resourceKey("mason/2/emerald_chiseled_stone_bricks");
/*      */   
/*  269 */   public static final ResourceKey<VillagerTrade> MASON_3_GRANITE_EMERALD = resourceKey("mason/3/granite_emerald");
/*      */   
/*  270 */   public static final ResourceKey<VillagerTrade> MASON_3_ANDESITE_EMERALD = resourceKey("mason/3/andesite_emerald");
/*      */   
/*  271 */   public static final ResourceKey<VillagerTrade> MASON_3_DIORITE_EMERALD = resourceKey("mason/3/diorite_emerald");
/*      */   
/*  272 */   public static final ResourceKey<VillagerTrade> MASON_3_EMERALD_DRIPSTONE_BLOCK = resourceKey("mason/3/emerald_dripstone_block");
/*      */   
/*  273 */   public static final ResourceKey<VillagerTrade> MASON_3_EMERALD_POLISHED_ANDESITE = resourceKey("mason/3/emerald_polished_andesite");
/*      */   
/*  274 */   public static final ResourceKey<VillagerTrade> MASON_3_EMERALD_POLISHED_DIORITE = resourceKey("mason/3/emerald_polished_diorite");
/*      */   
/*  275 */   public static final ResourceKey<VillagerTrade> MASON_3_EMERALD_POLISHED_GRANTITE = resourceKey("mason/3/emerald_polished_granite");
/*      */   
/*  276 */   public static final ResourceKey<VillagerTrade> MASON_4_QUARTZ_EMERALD = resourceKey("mason/4/quartz_emerald");
/*      */   
/*      */   public static final ColorCollection<ResourceKey<VillagerTrade>> MASON_4_EMERALD_TERRACOTTA;
/*      */   
/*      */   public static final ColorCollection<ResourceKey<VillagerTrade>> MASON_4_EMERALD_GLAZED_TERRACOTTA;
/*      */   
/*      */   static {
/*  277 */     MASON_4_EMERALD_TERRACOTTA = ColorCollection.NAMES.map(color -> resourceKey("mason/4/emerald_" + color + "_terracotta"));
/*  278 */     MASON_4_EMERALD_GLAZED_TERRACOTTA = ColorCollection.NAMES.map(color -> resourceKey("mason/4/emerald_" + color + "_glazed_terracotta"));
/*      */   }
/*      */   
/*  279 */   public static final ResourceKey<VillagerTrade> MASON_5_EMERALD_QUARTZ_PILLAR = resourceKey("mason/5/emerald_quartz_pillar");
/*      */   
/*  280 */   public static final ResourceKey<VillagerTrade> MASON_5_EMERALD_QUARTZ_BLOCK = resourceKey("mason/5/emerald_quartz_block");
/*      */   
/*  282 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_WATER_BOTTLE_EMERALD = resourceKey("wandering_trader/water_bottle_emerald");
/*      */   
/*  283 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_WATER_BUCKET_EMERALD = resourceKey("wandering_trader/water_bucket_emerald");
/*      */   
/*  284 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_MILK_BUCKET_EMERALD = resourceKey("wandering_trader/milk_bucket_emerald");
/*      */   
/*  285 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_FERMENTED_SPIDER_EYE_EMERALD = resourceKey("wandering_trader/fermented_spider_eye_emerald");
/*      */   
/*  286 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_BAKED_POTATO_EMERALD = resourceKey("wandering_trader/baked_potato_emerald");
/*      */   
/*  287 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_HAY_BLOCK_EMERALD = resourceKey("wandering_trader/hay_block_emerald");
/*      */   
/*  289 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_PACKED_ICE = resourceKey("wandering_trader/emerald_packed_ice");
/*      */   
/*  290 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_BLUE_ICE = resourceKey("wandering_trader/emerald_blue_ice");
/*      */   
/*  291 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_GUNPOWDER = resourceKey("wandering_trader/emerald_gunpowder");
/*      */   
/*  292 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_PODZOL = resourceKey("wandering_trader/emerald_podzol");
/*      */   
/*  293 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_ACACIA_LOG = resourceKey("wandering_trader/emerald_acacia_log");
/*      */   
/*  294 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_BIRCH_LOG = resourceKey("wandering_trader/emerald_birch_log");
/*      */   
/*  295 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_DARK_OAK_LOG = resourceKey("wandering_trader/emerald_dark_oak_log");
/*      */   
/*  296 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_JUNGLE_LOG = resourceKey("wandering_trader/emerald_jungle_log");
/*      */   
/*  297 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_OAK_LOG = resourceKey("wandering_trader/emerald_oak_log");
/*      */   
/*  298 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_SPRUCE_LOG = resourceKey("wandering_trader/emerald_spruce_log");
/*      */   
/*  299 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_CHERRY_LOG = resourceKey("wandering_trader/emerald_cherry_log");
/*      */   
/*  300 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_MANGROVE_LOG = resourceKey("wandering_trader/emerald_mangrove_log");
/*      */   
/*  301 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_PALE_OAK_LOG = resourceKey("wandering_trader/emerald_pale_oak_log");
/*      */   
/*  302 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_ENCHANTED_IRON_PICKAXE = resourceKey("wandering_trader/emerald_enchanted_iron_pickaxe");
/*      */   
/*  303 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_LONG_INVISIBILITY_POTION = resourceKey("wandering_trader/emerald_long_invisibility_potion");
/*      */   
/*  305 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_TROPICAL_FISH_BUCKET = resourceKey("wandering_trader/emerald_fish_bucket");
/*      */   
/*  306 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_PUFFERFISH_BUCKET = resourceKey("wandering_trader/emerald_pufferfish_bucket");
/*      */   
/*  307 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_SEA_PICKLE = resourceKey("wandering_trader/emerald_sea_pickle");
/*      */   
/*  308 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_SLIME_BALL = resourceKey("wandering_trader/emerald_slime_ball");
/*      */   
/*  309 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_GLOWSTONE = resourceKey("wandering_trader/emerald_glowstone");
/*      */   
/*  310 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_NAUTILUS_SHELL = resourceKey("wandering_trader/emerald_nautilus_shell");
/*      */   
/*  311 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_FERN = resourceKey("wandering_trader/emerald_fern");
/*      */   
/*  312 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_SUGAR_CANE = resourceKey("wandering_trader/emerald_sugar_cane");
/*      */   
/*  313 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_PUMPKIN = resourceKey("wandering_trader/emerald_pumpkin");
/*      */   
/*  314 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_KELP = resourceKey("wandering_trader/emerald_kelp");
/*      */   
/*  315 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_CACTUS = resourceKey("wandering_trader/emerald_cactus");
/*      */   
/*  316 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_DANDELION = resourceKey("wandering_trader/emerald_dandelion");
/*      */   
/*  317 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_GOLDEN_DANDELION = resourceKey("wandering_trader/emerald_golden_dandelion");
/*      */   
/*  318 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_POPPY = resourceKey("wandering_trader/emerald_poppy");
/*      */   
/*  319 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_BLUE_ORCHID = resourceKey("wandering_trader/emerald_blue_orchid");
/*      */   
/*  320 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_ALLIUM = resourceKey("wandering_trader/emerald_allium");
/*      */   
/*  321 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_AZURE_BLUET = resourceKey("wandering_trader/emerald_azure_bluet");
/*      */   
/*  322 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_RED_TULIP = resourceKey("wandering_trader/emerald_red_tulip");
/*      */   
/*  323 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_ORANGE_TULIP = resourceKey("wandering_trader/emerald_orange_tulip");
/*      */   
/*  324 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_WHITE_TULIP = resourceKey("wandering_trader/emerald_white_tulip");
/*      */   
/*  325 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_PINK_TULIP = resourceKey("wandering_trader/emerald_pink_tulip");
/*      */   
/*  326 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_OXEYE_DAISY = resourceKey("wandering_trader/emerald_oxeye_daisy");
/*      */   
/*  327 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_CORNFLOWER = resourceKey("wandering_trader/emerald_cornflower");
/*      */   
/*  328 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_LILY_OF_THE_VALLEY = resourceKey("wandering_trader/emerald_lily_of_the_valley");
/*      */   
/*  329 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_OPEN_EYEBLOSSOM = resourceKey("wandering_trader/emerald_open_eyeblossom");
/*      */   
/*  330 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_WHEAT_SEEDS = resourceKey("wandering_trader/emerald_wheat_seeds");
/*      */   
/*  331 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_BEETROOT_SEEDS = resourceKey("wandering_trader/emerald_beetroot_seeds");
/*      */   
/*  332 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_PUMPKIN_SEEDS = resourceKey("wandering_trader/emerald_pumpkin_seeds");
/*      */   
/*  333 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_MELON_SEEDS = resourceKey("wandering_trader/emerald_melon_seeds");
/*      */   
/*  334 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_ACACIA_SAPLING = resourceKey("wandering_trader/emerald_acacia_sapling");
/*      */   
/*  335 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_BIRCH_SAPLING = resourceKey("wandering_trader/emerald_birch_sapling");
/*      */   
/*  336 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_DARK_OAK_SAPLING = resourceKey("wandering_trader/emerald_dark_oak_sapling");
/*      */   
/*  337 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_JUNGLE_SAPLING = resourceKey("wandering_trader/emerald_jungle_sapling");
/*      */   
/*  338 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_OAK_SAPLING = resourceKey("wandering_trader/emerald_oak_sapling");
/*      */   
/*  339 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_SPRUCE_SAPLING = resourceKey("wandering_trader/emerald_spruce_sapling");
/*      */   
/*  340 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_CHERRY_SAPLING = resourceKey("wandering_trader/emerald_cherry_sapling");
/*      */   
/*  341 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_PALE_OAK_SAPLING = resourceKey("wandering_trader/emerald_pale_oak_sapling");
/*      */   
/*  342 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_MANGROVE_PROPAGULE = resourceKey("wandering_trader/emerald_mangrove_propagule");
/*      */   
/*      */   public static final ColorCollection<ResourceKey<VillagerTrade>> WANDERING_TRADER_EMERALD_DYE;
/*      */   
/*      */   static {
/*  343 */     WANDERING_TRADER_EMERALD_DYE = ColorCollection.NAMES.map(color -> resourceKey("wandering_trader/emerald_" + color + "_dye"));
/*      */   }
/*      */   
/*  344 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_BRAIN_CORAL_BLOCK = resourceKey("wandering_trader/emerald_brain_coral_block");
/*      */   
/*  345 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_BUBBLE_CORAL_BLOCK = resourceKey("wandering_trader/emerald_bubble_coral_block");
/*      */   
/*  346 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_FIRE_CORAL_BLOCK = resourceKey("wandering_trader/emerald_fire_coral_block");
/*      */   
/*  347 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_HORN_CORAL_BLOCK = resourceKey("wandering_trader/emerald_horn_coral_block");
/*      */   
/*  348 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_TUBE_CORAL_BLOCK = resourceKey("wandering_trader/emerald_tube_coral_block");
/*      */   
/*  349 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_VINE = resourceKey("wandering_trader/emerald_vine");
/*      */   
/*  350 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_PALE_HANGING_MOSS = resourceKey("wandering_trader/emerald_pale_hanging_moss");
/*      */   
/*  351 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_BROWN_MUSHROOM = resourceKey("wandering_trader/emerald_brown_mushroom");
/*      */   
/*  352 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_RED_MUSHROOM = resourceKey("wandering_trader/emerald_red_mushroom");
/*      */   
/*  353 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_LILY_PAD = resourceKey("wandering_trader/emerald_lily_pad");
/*      */   
/*  354 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_SMALL_DRIPLEAF = resourceKey("wandering_trader/emerald_small_dripleaf");
/*      */   
/*  355 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_SAND = resourceKey("wandering_trader/emerald_sand");
/*      */   
/*  356 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_RED_SAND = resourceKey("wandering_trader/emerald_red_sand");
/*      */   
/*  357 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_POINTED_DRIPSTONE = resourceKey("wandering_trader/emerald_pointed_dripstone");
/*      */   
/*  358 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_SULFUR_SPIKE = resourceKey("wandering_trader/emerald_sulfur_spike");
/*      */   
/*  359 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_ROOTED_DIRT = resourceKey("wandering_trader/emerald_rooted_dirt");
/*      */   
/*  360 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_MOSS_BLOCK = resourceKey("wandering_trader/emerald_moss_block");
/*      */   
/*  361 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_PALE_MOSS_BLOCK = resourceKey("wandering_trader/emerald_pale_moss_block");
/*      */   
/*  362 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_WILDFLOWERS = resourceKey("wandering_trader/emerald_wildflowers");
/*      */   
/*  363 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_DRY_TALL_GRASS = resourceKey("wandering_trader/emerald_dry_tall_grass");
/*      */   
/*  364 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_FIREFLY_BUSH = resourceKey("wandering_trader/emerald_firefly_bush");
/*      */   
/*  365 */   public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_NAME_TAG = resourceKey("wandering_trader/emerald_name_tag");
/*      */   
/*      */   public static Holder<VillagerTrade> bootstrap(BootstrapContext<VillagerTrade> context) {
/*  368 */     HolderGetter<Item> items = context.lookup(Registries.ITEM);
/*  369 */     HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
/*  370 */     HolderSet.Named named1 = enchantments.getOrThrow(EnchantmentTags.ON_TRADED_EQUIPMENT);
/*  371 */     HolderSet.Named named2 = enchantments.getOrThrow(EnchantmentTags.TRADEABLE);
/*  372 */     HolderSet.Named named3 = enchantments.getOrThrow(EnchantmentTags.DOUBLE_TRADE_PRICE);
/*  374 */     HolderGetter<Potion> potions = context.lookup(Registries.POTION);
/*  375 */     HolderSet.Named named4 = potions.getOrThrow(PotionTags.TRADEABLE);
/*  376 */     HolderGetter<VillagerType> villagerVariants = context.lookup(Registries.VILLAGER_TYPE);
/*  379 */     register(context, FARMER_1_WHEAT_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.WHEAT, 20), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/*  385 */           Optional.empty(), 
/*  386 */           List.of()));
/*  388 */     register(context, FARMER_1_POTATO_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.POTATO, 26), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/*  394 */           Optional.empty(), 
/*  395 */           List.of()));
/*  397 */     register(context, FARMER_1_CARROT_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.CARROT, 22), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/*  403 */           Optional.empty(), 
/*  404 */           List.of()));
/*  406 */     register(context, FARMER_1_BEETROOT_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.BEETROOT, 15), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/*  412 */           Optional.empty(), 
/*  413 */           List.of()));
/*  415 */     register(context, FARMER_1_EMERALD_BREAD, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.BREAD, 6), 16, 1, 0.05F, 
/*      */           
/*  421 */           Optional.empty(), 
/*  422 */           List.of()));
/*  425 */     register(context, FARMER_2_PUMPKIN_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.PUMPKIN, 6), new ItemStackTemplate(Items.EMERALD), 12, 10, 0.05F, 
/*      */           
/*  431 */           Optional.empty(), 
/*  432 */           List.of()));
/*  434 */     register(context, FARMER_2_EMERALD_PUMPKIN_PIE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.PUMPKIN_PIE, 4), 12, 5, 0.05F, 
/*      */           
/*  440 */           Optional.empty(), 
/*  441 */           List.of()));
/*  443 */     register(context, FARMER_2_EMERALD_APPLE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.APPLE, 4), 16, 5, 0.05F, 
/*      */           
/*  449 */           Optional.empty(), 
/*  450 */           List.of()));
/*  453 */     register(context, FARMER_3_EMERALD_COOKIE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.COOKIE, 18), 12, 10, 0.05F, 
/*      */           
/*  459 */           Optional.empty(), 
/*  460 */           List.of()));
/*  462 */     register(context, FARMER_3_MELON_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.MELON, 4), new ItemStackTemplate(Items.EMERALD), 12, 20, 0.05F, 
/*      */           
/*  468 */           Optional.empty(), 
/*  469 */           List.of()));
/*  472 */     register(context, FARMER_4_EMERALD_CAKE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.CAKE), 12, 15, 0.05F, 
/*      */           
/*  478 */           Optional.empty(), 
/*  479 */           List.of()));
/*  481 */     register(context, FARMER_4_EMERALD_SUSPICIOUS_STEW, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.SUSPICIOUS_STEW), 12, 15, 0.05F, 
/*      */           
/*  487 */           Optional.empty(), 
/*  488 */           List.of((new SetStewEffectFunction.Builder())
/*  489 */             .withEffect(MobEffects.NIGHT_VISION, (NumberProvider)new ConstantValue(5.0F))
/*  490 */             .withEffect(MobEffects.JUMP_BOOST, (NumberProvider)new ConstantValue(8.0F))
/*  491 */             .withEffect(MobEffects.WEAKNESS, (NumberProvider)new ConstantValue(7.0F))
/*  492 */             .withEffect(MobEffects.BLINDNESS, (NumberProvider)new ConstantValue(6.0F))
/*  493 */             .withEffect(MobEffects.POISON, (NumberProvider)new ConstantValue(14.0F))
/*  494 */             .withEffect(MobEffects.SATURATION, (NumberProvider)new ConstantValue(7.0F))
/*  495 */             .build())));
/*  499 */     register(context, FARMER_5_EMERALD_GOLDEN_CARROT, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.GOLDEN_CARROT, 3), 12, 30, 0.05F, 
/*      */           
/*  505 */           Optional.empty(), 
/*  506 */           List.of()));
/*  508 */     register(context, FARMER_5_EMERALD_GLISTENING_MELON_SLICE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 4), new ItemStackTemplate(Items.GLISTERING_MELON_SLICE, 3), 12, 30, 0.05F, 
/*      */           
/*  514 */           Optional.empty(), 
/*  515 */           List.of()));
/*  518 */     register(context, FISHERMAN_1_STRING_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.STRING, 20), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/*  524 */           Optional.empty(), 
/*  525 */           List.of()));
/*  527 */     register(context, FISHERMAN_1_COAL_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.COAL, 10), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/*  533 */           Optional.empty(), 
/*  534 */           List.of()));
/*  536 */     register(context, FISHERMAN_1_RAW_COD_AND_EMERALD_COOKED_COD, new VillagerTrade(new TradeCost((ItemLike)Items.COD, 6), 
/*      */           
/*  538 */           Optional.of(new TradeCost((ItemLike)Items.EMERALD, 1)), new ItemStackTemplate(Items.COOKED_COD, 6), 16, 1, 0.05F, 
/*      */           
/*  543 */           Optional.empty(), 
/*  544 */           List.of()));
/*  546 */     register(context, FISHERMAN_1_EMERALD_COD_BUCKET, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.COD_BUCKET), 16, 1, 0.05F, 
/*      */           
/*  552 */           Optional.empty(), 
/*  553 */           List.of()));
/*  556 */     register(context, FISHERMAN_2_COD_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.COD, 15), new ItemStackTemplate(Items.EMERALD), 16, 10, 0.05F, 
/*      */           
/*  562 */           Optional.empty(), 
/*  563 */           List.of()));
/*  565 */     register(context, FISHERMAN_2_SALMON_AND_EMERALD_COOKED_SALMON, new VillagerTrade(new TradeCost((ItemLike)Items.SALMON, 6), 
/*      */           
/*  567 */           Optional.of(new TradeCost((ItemLike)Items.EMERALD, 1)), new ItemStackTemplate(Items.COOKED_SALMON, 6), 16, 5, 0.05F, 
/*      */           
/*  572 */           Optional.empty(), 
/*  573 */           List.of()));
/*  575 */     register(context, FISHERMAN_2_EMERALD_CAMPFIRE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), new ItemStackTemplate(Items.CAMPFIRE), 12, 5, 0.05F, 
/*      */           
/*  581 */           Optional.empty(), 
/*  582 */           List.of()));
/*  585 */     register(context, FISHERMAN_3_SALMON_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.SALMON, 13), new ItemStackTemplate(Items.EMERALD), 16, 20, 0.05F, 
/*      */           
/*  591 */           Optional.empty(), 
/*  592 */           List.of()));
/*  594 */     register(context, FISHERMAN_3_EMERALD_ENCHANTED_FISHING_ROD, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.FISHING_ROD), 3, 10, 0.2F, 
/*      */           
/*  600 */           Optional.empty(), 
/*  601 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.FISHING_ROD)));
/*  604 */     register(context, FISHERMAN_4_TROPICAL_FISH_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.TROPICAL_FISH, 6), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */           
/*  610 */           Optional.empty(), 
/*  611 */           List.of()));
/*  614 */     register(context, FISHERMAN_5_PUFFERFISH_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.PUFFERFISH, 4), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */           
/*  620 */           Optional.empty(), 
/*  621 */           List.of()));
/*  623 */     registerBoatTrades(context, villagerVariants);
/*  625 */     registerShepherdWoolSales(context);
/*  626 */     register(context, SHEPHERD_1_EMERALD_SHEARS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), new ItemStackTemplate(Items.SHEARS), 12, 1, 0.05F, 
/*      */           
/*  632 */           Optional.empty(), 
/*  633 */           List.of()));
/*  636 */     registerShepherdLevelTwoDyeTrades(context);
/*  637 */     registerWoolPurchases(context);
/*  638 */     registerCarpetPurchases(context);
/*  640 */     registerShepherdLevelThreeDyeTrades(context);
/*  641 */     registerBedTrades(context);
/*  643 */     registerLevelFourDyeTrades(context);
/*  644 */     registerShepherdBannerTrades(context);
/*  646 */     register(context, SHEPHERD_5_EMERALD_PAINTING, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), new ItemStackTemplate(Items.PAINTING, 3), 12, 30, 0.05F, 
/*      */           
/*  652 */           Optional.empty(), 
/*  653 */           List.of()));
/*  656 */     register(context, FLETCHER_1_STICK_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.STICK, 32), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/*  662 */           Optional.empty(), 
/*  663 */           List.of()));
/*  665 */     register(context, FLETCHER_1_EMERALD_ARROW, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.ARROW, 16), 12, 1, 0.05F, 
/*      */           
/*  671 */           Optional.empty(), 
/*  672 */           List.of()));
/*  674 */     register(context, FLETCHER_1_GRAVEL_AND_EMERALD_FLINT, new VillagerTrade(new TradeCost((ItemLike)Items.GRAVEL, 10), 
/*      */           
/*  676 */           Optional.of(new TradeCost((ItemLike)Items.EMERALD, 1)), new ItemStackTemplate(Items.FLINT, 10), 12, 1, 0.05F, 
/*      */           
/*  681 */           Optional.empty(), 
/*  682 */           List.of()));
/*  685 */     register(context, FLETCHER_2_FLINT_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.FLINT, 26), new ItemStackTemplate(Items.EMERALD), 12, 10, 0.05F, 
/*      */           
/*  691 */           Optional.empty(), 
/*  692 */           List.of()));
/*  694 */     register(context, FLETCHER_2_EMERALD_BOW, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), new ItemStackTemplate(Items.BOW), 12, 5, 0.05F, 
/*      */           
/*  700 */           Optional.empty(), 
/*  701 */           List.of()));
/*  704 */     register(context, FLETCHER_3_STRING_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.STRING, 14), new ItemStackTemplate(Items.EMERALD), 16, 20, 0.05F, 
/*      */           
/*  710 */           Optional.empty(), 
/*  711 */           List.of()));
/*  713 */     register(context, FLETCHER_3_EMERALD_CROSSBOW, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.CROSSBOW), 12, 10, 0.05F, 
/*      */           
/*  719 */           Optional.empty(), 
/*  720 */           List.of()));
/*  723 */     register(context, FLETCHER_4_FEATHER_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.FEATHER, 24), new ItemStackTemplate(Items.EMERALD), 16, 30, 0.05F, 
/*      */           
/*  729 */           Optional.empty(), 
/*  730 */           List.of()));
/*  732 */     register(context, FLETCHER_4_EMERALD_ENCHANTED_BOW, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), new ItemStackTemplate(Items.BOW), 3, 15, 0.05F, 
/*      */           
/*  738 */           Optional.empty(), 
/*  739 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.BOW)));
/*  742 */     register(context, FLETCHER_5_TRIPWIRE_HOOK_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.TRIPWIRE_HOOK, 8), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */           
/*  748 */           Optional.empty(), 
/*  749 */           List.of()));
/*  751 */     register(context, FLETCHER_5_EMERALD_ENCHANTED_CROSSBOW, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.CROSSBOW), 3, 15, 0.05F, 
/*      */           
/*  757 */           Optional.empty(), 
/*  758 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.CROSSBOW)));
/*  760 */     register(context, FLETCHER_5_ARROW_AND_EMERALD_TIPPED_ARROW, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), 
/*      */           
/*  762 */           Optional.of(new TradeCost((ItemLike)Items.ARROW, 5)), new ItemStackTemplate(Items.TIPPED_ARROW, 5), 12, 30, 0.05F, 
/*      */           
/*  767 */           Optional.empty(), 
/*  768 */           List.of(SetRandomPotionFunction.fromTagKey((HolderSet)named4).build())));
/*  771 */     register(context, LIBRARIAN_1_PAPER_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.PAPER, 24), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/*  777 */           Optional.empty(), 
/*  778 */           List.of()));
/*  780 */     register(context, LIBRARIAN_1_EMERALD_AND_BOOK_ENCHANTED_BOOK, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 0), 
/*      */           
/*  782 */           Optional.of(new TradeCost((ItemLike)Items.BOOK, 1)), new ItemStackTemplate(Items.ENCHANTED_BOOK), 12, 1, 0.2F, 
/*      */           
/*  787 */           Optional.empty(), 
/*  788 */           enchantedBook(items, (HolderSet<Enchantment>)named2), (HolderSet<Enchantment>)named3));
/*  791 */     register(context, LIBRARIAN_1_EMERALD_BOOKSHELF, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 9), new ItemStackTemplate(Items.BOOKSHELF), 12, 1, 0.05F, 
/*      */           
/*  797 */           Optional.empty(), 
/*  798 */           List.of()));
/*  801 */     register(context, LIBRARIAN_2_BOOK_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.BOOK, 4), new ItemStackTemplate(Items.EMERALD), 12, 10, 0.05F, 
/*      */           
/*  807 */           Optional.empty(), 
/*  808 */           List.of()));
/*  810 */     register(context, LIBRARIAN_2_EMERALD_AND_BOOK_ENCHANTED_BOOK, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 0), 
/*      */           
/*  812 */           Optional.of(new TradeCost((ItemLike)Items.BOOK, 1)), new ItemStackTemplate(Items.ENCHANTED_BOOK), 12, 5, 0.2F, 
/*      */           
/*  817 */           Optional.empty(), 
/*  818 */           enchantedBook(items, (HolderSet<Enchantment>)named2), (HolderSet<Enchantment>)named3));
/*  821 */     register(context, LIBRARIAN_2_EMERALD_LANTERN, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.LANTERN), 12, 5, 0.05F, 
/*      */           
/*  827 */           Optional.empty(), 
/*  828 */           List.of()));
/*  831 */     register(context, LIBRARIAN_3_INK_SAC_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.INK_SAC, 5), new ItemStackTemplate(Items.EMERALD), 12, 20, 0.05F, 
/*      */           
/*  837 */           Optional.empty(), 
/*  838 */           List.of()));
/*  840 */     register(context, LIBRARIAN_3_EMERALD_AND_BOOK_ENCHANTED_BOOK, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 0), 
/*      */           
/*  842 */           Optional.of(new TradeCost((ItemLike)Items.BOOK, 1)), new ItemStackTemplate(Items.ENCHANTED_BOOK), 12, 10, 0.2F, 
/*      */           
/*  847 */           Optional.empty(), 
/*  848 */           enchantedBook(items, (HolderSet<Enchantment>)named2), (HolderSet<Enchantment>)named3));
/*  851 */     register(context, LIBRARIAN_3_EMERALD_GLASS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.GLASS, 4), 12, 10, 0.05F, 
/*      */           
/*  857 */           Optional.empty(), 
/*  858 */           List.of()));
/*  861 */     register(context, LIBRARIAN_4_WRITABLE_BOOK_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.WRITABLE_BOOK, 2), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */           
/*  867 */           Optional.empty(), 
/*  868 */           List.of()));
/*  870 */     register(context, LIBRARIAN_4_EMERALD_AND_BOOK_ENCHANTED_BOOK, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 0), 
/*      */           
/*  872 */           Optional.of(new TradeCost((ItemLike)Items.BOOK, 1)), new ItemStackTemplate(Items.ENCHANTED_BOOK), 12, 15, 0.2F, 
/*      */           
/*  877 */           Optional.empty(), 
/*  878 */           enchantedBook(items, (HolderSet<Enchantment>)named2), (HolderSet<Enchantment>)named3));
/*  881 */     register(context, LIBRARIAN_4_EMERALD_CLOCK, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 5), new ItemStackTemplate(Items.CLOCK), 12, 15, 0.05F, 
/*      */           
/*  887 */           Optional.empty(), 
/*  888 */           List.of()));
/*  890 */     register(context, LIBRARIAN_4_EMERALD_COMPASS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 4), new ItemStackTemplate(Items.COMPASS), 12, 15, 0.05F, 
/*      */           
/*  896 */           Optional.empty(), 
/*  897 */           List.of()));
/*  900 */     register(context, LIBRARIAN_5_EMERALD_YELLOW_CANDLE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate((Item)Items.DYED_CANDLE
/*      */             
/*  902 */             .yellow()), 12, 30, 0.05F, 
/*      */           
/*  906 */           Optional.empty(), 
/*  907 */           List.of()));
/*  909 */     register(context, LIBRARIAN_5_EMERALD_RED_CANDLE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate((Item)Items.DYED_CANDLE
/*      */             
/*  911 */             .red()), 12, 30, 0.05F, 
/*      */           
/*  915 */           Optional.empty(), 
/*  916 */           List.of()));
/*  919 */     register(context, CARTOGRAPHER_1_PAPER_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.PAPER, 24), new ItemStackTemplate(Items.EMERALD), 12, 2, 0.05F, 
/*      */           
/*  925 */           Optional.empty(), 
/*  926 */           List.of()));
/*  928 */     register(context, CARTOGRAPHER_1_EMERALD_MAP, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 7), new ItemStackTemplate(Items.MAP), 12, 1, 0.05F, 
/*      */           
/*  934 */           Optional.empty(), 
/*  935 */           List.of()));
/*  938 */     register(context, CARTOGRAPHER_2_GLASS_PANE_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.GLASS_PANE, 11), new ItemStackTemplate(Items.EMERALD), 12, 10, 0.05F, 
/*      */           
/*  944 */           Optional.empty(), 
/*  945 */           List.of()));
/*  947 */     registerBasicExplorerMapTrades(context, items, villagerVariants);
/*  949 */     register(context, CARTOGRAPHER_3_COMPASS_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.COMPASS, 1), new ItemStackTemplate(Items.EMERALD), 12, 20, 0.05F, 
/*      */           
/*  955 */           Optional.empty(), 
/*  956 */           List.of()));
/*  958 */     register(context, CARTOGRAPHER_3_EMERALD_AND_COMPASS_OCEAN_EXPLORER_MAP, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 13), 
/*      */           
/*  960 */           Optional.of(new TradeCost((ItemLike)Items.COMPASS, 1)), new ItemStackTemplate(Items.MAP), 12, 10, 0.2F, 
/*      */           
/*  965 */           Optional.empty(), 
/*  966 */           List.of(
/*  967 */             ExplorationMapFunction.makeExplorationMap()
/*  968 */             .setDestination(StructureTags.ON_OCEAN_EXPLORER_MAPS)
/*  969 */             .setMapDecoration(MapDecorationTypes.OCEAN_MONUMENT)
/*  970 */             .setSearchRadius(100)
/*  971 */             .setSkipKnownStructures(true)
/*  972 */             .build(), 
/*      */             
/*  974 */             SetNameFunction.setName((Component)Component.translatable("filled_map.monument"), SetNameFunction.Target.ITEM_NAME)
/*  975 */             .build(), 
/*  976 */             FilteredFunction.filtered((new ItemPredicate.Builder())
/*      */               
/*  978 */               .of(items, new ItemLike[] { (ItemLike)Items.FILLED_MAP }).withComponents(
/*  980 */                 DataComponentMatchers.Builder.components()
/*  981 */                 .any(DataComponents.MAP_ID)
/*  982 */                 .build())
/*      */               
/*  984 */               .build())
/*  985 */             .onFail(Optional.of(DiscardItem.discardItem().build())).build())));
/*  988 */     register(context, CARTOGRAPHER_3_EMERALD_AND_COMPASS_TRIAL_CHAMBER_MAP, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 12), 
/*      */           
/*  990 */           Optional.of(new TradeCost((ItemLike)Items.COMPASS, 1)), new ItemStackTemplate(Items.MAP), 12, 10, 0.2F, 
/*      */           
/*  995 */           Optional.empty(), 
/*  996 */           List.of(
/*  997 */             ExplorationMapFunction.makeExplorationMap()
/*  998 */             .setDestination(StructureTags.ON_TRIAL_CHAMBERS_MAPS)
/*  999 */             .setMapDecoration(MapDecorationTypes.TRIAL_CHAMBERS)
/* 1000 */             .setSearchRadius(100)
/* 1001 */             .setSkipKnownStructures(true)
/* 1002 */             .build(), 
/*      */             
/* 1004 */             SetNameFunction.setName((Component)Component.translatable("filled_map.trial_chambers"), SetNameFunction.Target.ITEM_NAME)
/* 1005 */             .build(), 
/* 1006 */             FilteredFunction.filtered((new ItemPredicate.Builder())
/*      */               
/* 1008 */               .of(items, new ItemLike[] { (ItemLike)Items.FILLED_MAP }).withComponents(
/* 1010 */                 DataComponentMatchers.Builder.components()
/* 1011 */                 .any(DataComponents.MAP_ID)
/* 1012 */                 .build())
/*      */               
/* 1014 */               .build())
/* 1015 */             .onFail(Optional.of(DiscardItem.discardItem().build())).build())));
/* 1019 */     register(context, CARTOGRAPHER_4_EMERALD_ITEM_FRAME, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 7), new ItemStackTemplate(Items.ITEM_FRAME), 12, 15, 0.05F, 
/*      */           
/* 1025 */           Optional.empty(), 
/* 1026 */           List.of()));
/* 1028 */     registerCartographerBannerTrades(context, villagerVariants);
/* 1030 */     register(context, CARTOGRAPHER_5_EMERALD_GLOBE_BANNER_PATTERN, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 8), new ItemStackTemplate(Items.GLOBE_BANNER_PATTERN), 12, 30, 0.05F, 
/*      */           
/* 1036 */           Optional.empty(), 
/* 1037 */           List.of()));
/* 1039 */     register(context, CARTOGRAPHER_5_EMERALD_AND_COMPASS_WOODLAND_MANSION_MAP, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 14), 
/*      */           
/* 1041 */           Optional.of(new TradeCost((ItemLike)Items.COMPASS, 1)), new ItemStackTemplate(Items.MAP), 12, 30, 0.2F, 
/*      */           
/* 1046 */           Optional.empty(), 
/* 1047 */           List.of(
/* 1048 */             ExplorationMapFunction.makeExplorationMap()
/* 1049 */             .setDestination(StructureTags.ON_WOODLAND_EXPLORER_MAPS)
/* 1050 */             .setMapDecoration(MapDecorationTypes.WOODLAND_MANSION)
/* 1051 */             .setSearchRadius(100)
/* 1052 */             .setSkipKnownStructures(true)
/* 1053 */             .build(), 
/*      */             
/* 1055 */             SetNameFunction.setName((Component)Component.translatable("filled_map.mansion"), SetNameFunction.Target.ITEM_NAME)
/* 1056 */             .build(), 
/* 1057 */             FilteredFunction.filtered((new ItemPredicate.Builder())
/*      */               
/* 1059 */               .of(items, new ItemLike[] { (ItemLike)Items.FILLED_MAP }).withComponents(
/* 1061 */                 DataComponentMatchers.Builder.components()
/* 1062 */                 .any(DataComponents.MAP_ID)
/* 1063 */                 .build())
/*      */               
/* 1065 */               .build())
/* 1066 */             .onFail(Optional.of(DiscardItem.discardItem().build())).build())));
/* 1070 */     register(context, CLERIC_1_ROTTEN_FLESH_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.ROTTEN_FLESH, 32), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/* 1076 */           Optional.empty(), 
/* 1077 */           List.of()));
/* 1079 */     register(context, CLERIC_1_EMERALD_REDSTONE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.REDSTONE, 2), 12, 1, 0.05F, 
/*      */           
/* 1085 */           Optional.empty(), 
/* 1086 */           List.of()));
/* 1089 */     register(context, CLERIC_2_GOLD_INGOT_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.GOLD_INGOT, 3), new ItemStackTemplate(Items.EMERALD), 12, 10, 0.05F, 
/*      */           
/* 1095 */           Optional.empty(), 
/* 1096 */           List.of()));
/* 1098 */     register(context, CLERIC_2_EMERALD_LAPIS_LAZULI, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.LAPIS_LAZULI), 12, 5, 0.05F, 
/*      */           
/* 1104 */           Optional.empty(), 
/* 1105 */           List.of()));
/* 1108 */     register(context, CLERIC_3_RABBIT_FOOT_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.RABBIT_FOOT, 2), new ItemStackTemplate(Items.EMERALD), 12, 20, 0.05F, 
/*      */           
/* 1114 */           Optional.empty(), 
/* 1115 */           List.of()));
/* 1117 */     register(context, CLERIC_3_EMERALD_GLOWSTONE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 4), new ItemStackTemplate(Items.GLOWSTONE), 12, 10, 0.05F, 
/*      */           
/* 1123 */           Optional.empty(), 
/* 1124 */           List.of()));
/* 1127 */     register(context, CLERIC_4_TURTLE_SCUTE_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.TURTLE_SCUTE, 4), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */           
/* 1133 */           Optional.empty(), 
/* 1134 */           List.of()));
/* 1136 */     register(context, CLERIC_4_GLASS_BOTTLE_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.GLASS_BOTTLE, 9), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */           
/* 1142 */           Optional.empty(), 
/* 1143 */           List.of()));
/* 1145 */     register(context, CLERIC_4_EMERALD_ENDER_PEARL, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 5), new ItemStackTemplate(Items.ENDER_PEARL), 12, 15, 0.05F, 
/*      */           
/* 1151 */           Optional.empty(), 
/* 1152 */           List.of()));
/* 1155 */     register(context, CLERIC_5_NETHER_WART_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.NETHER_WART, 22), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */           
/* 1161 */           Optional.empty(), 
/* 1162 */           List.of()));
/* 1164 */     register(context, CLERIC_5_EMERALD_EXPERIENCE_BOTTLE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.EXPERIENCE_BOTTLE), 12, 30, 0.05F, 
/*      */           
/* 1170 */           Optional.empty(), 
/* 1171 */           List.of()));
/* 1174 */     register(context, COMMON_SMITH_1_COAL_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.COAL, 15), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/* 1180 */           Optional.empty(), 
/* 1181 */           List.of()));
/* 1183 */     register(context, COMMON_SMITH_2_IRON_INGOT_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.IRON_INGOT, 4), new ItemStackTemplate(Items.EMERALD), 12, 10, 0.05F, 
/*      */           
/* 1189 */           Optional.empty(), 
/* 1190 */           List.of()));
/* 1192 */     register(context, COMMON_SMITH_2_EMERALD_BELL, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 36), new ItemStackTemplate(Items.BELL), 12, 5, 0.2F, 
/*      */           
/* 1198 */           Optional.empty(), 
/* 1199 */           List.of()));
/* 1202 */     register(context, ARMORER_1_EMERALD_IRON_LEGGINGS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 7), new ItemStackTemplate(Items.IRON_LEGGINGS), 12, 1, 0.2F, 
/*      */           
/* 1208 */           Optional.empty(), 
/* 1209 */           List.of()));
/* 1211 */     register(context, ARMORER_1_EMERALD_IRON_BOOTS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 4), new ItemStackTemplate(Items.IRON_BOOTS), 12, 1, 0.2F, 
/*      */           
/* 1217 */           Optional.empty(), 
/* 1218 */           List.of()));
/* 1220 */     register(context, ARMORER_1_EMERALD_IRON_HELMET, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 5), new ItemStackTemplate(Items.IRON_HELMET), 12, 1, 0.2F, 
/*      */           
/* 1226 */           Optional.empty(), 
/* 1227 */           List.of()));
/* 1229 */     register(context, ARMORER_1_EMERALD_IRON_CHESTPLATE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 9), new ItemStackTemplate(Items.IRON_CHESTPLATE), 12, 1, 0.2F, 
/*      */           
/* 1235 */           Optional.empty(), 
/* 1236 */           List.of()));
/* 1239 */     register(context, ARMORER_2_EMERALD_CHAINMAIL_BOOTS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.CHAINMAIL_BOOTS), 12, 5, 0.2F, 
/*      */           
/* 1245 */           Optional.empty(), 
/* 1246 */           List.of()));
/* 1248 */     register(context, ARMORER_2_EMERALD_CHAINMAIL_LEGGINGS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.CHAINMAIL_LEGGINGS), 12, 5, 0.2F, 
/*      */           
/* 1254 */           Optional.empty(), 
/* 1255 */           List.of()));
/* 1258 */     register(context, ARMORER_3_LAVA_BUCKET_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.LAVA_BUCKET, 1), new ItemStackTemplate(Items.EMERALD), 12, 20, 0.05F, 
/*      */           
/* 1264 */           Optional.empty(), 
/* 1265 */           List.of()));
/* 1267 */     register(context, ARMORER_3_EMERALD_CHAINMAIL_HELMET, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.CHAINMAIL_HELMET), 12, 10, 0.2F, 
/*      */           
/* 1273 */           Optional.empty(), 
/* 1274 */           List.of()));
/* 1276 */     register(context, ARMORER_3_EMERALD_CHAINMAIL_CHESTPLATE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 4), new ItemStackTemplate(Items.CHAINMAIL_CHESTPLATE), 12, 10, 0.2F, 
/*      */           
/* 1282 */           Optional.empty(), 
/* 1283 */           List.of()));
/* 1285 */     register(context, ARMORER_3_EMERALD_SHIELD, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 5), new ItemStackTemplate(Items.SHIELD), 12, 10, 0.2F, 
/*      */           
/* 1291 */           Optional.empty(), 
/* 1292 */           List.of()));
/* 1294 */     register(context, ARMORER_3_DIAMOND_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.DIAMOND, 1), new ItemStackTemplate(Items.EMERALD), 12, 20, 0.05F, 
/*      */           
/* 1300 */           Optional.empty(), 
/* 1301 */           List.of()));
/* 1304 */     register(context, ARMORER_4_EMERALD_ENCHANTED_DIAMOND_LEGGINGS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 14), new ItemStackTemplate(Items.DIAMOND_LEGGINGS), 3, 15, 0.2F, 
/*      */           
/* 1310 */           Optional.empty(), 
/* 1311 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.DIAMOND_LEGGINGS)));
/* 1313 */     register(context, ARMORER_4_EMERALD_ENCHANTED_DIAMOND_BOOTS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 8), new ItemStackTemplate(Items.DIAMOND_BOOTS), 3, 15, 0.2F, 
/*      */           
/* 1319 */           Optional.empty(), 
/* 1320 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.DIAMOND_BOOTS)));
/* 1323 */     register(context, ARMORER_5_EMERALD_ENCHANTED_DIAMOND_HELMET, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 8), new ItemStackTemplate(Items.DIAMOND_HELMET), 3, 30, 0.2F, 
/*      */           
/* 1329 */           Optional.empty(), 
/* 1330 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.DIAMOND_HELMET)));
/* 1332 */     register(context, ARMORER_5_EMERALD_ENCHANTED_DIAMOND_CHESTPLATE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 16), new ItemStackTemplate(Items.DIAMOND_CHESTPLATE), 3, 30, 0.2F, 
/*      */           
/* 1338 */           Optional.empty(), 
/* 1339 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.DIAMOND_CHESTPLATE)));
/* 1342 */     register(context, WEAPONSMITH_1_EMERALD_IRON_AXE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.IRON_AXE), 12, 1, 0.2F, 
/*      */           
/* 1348 */           Optional.empty(), 
/* 1349 */           List.of()));
/* 1351 */     register(context, WEAPONSMITH_1_EMERALD_ENCHANTED_IRON_SWORD, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), new ItemStackTemplate(Items.IRON_SWORD), 12, 1, 0.2F, 
/*      */           
/* 1357 */           Optional.empty(), 
/* 1358 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.IRON_SWORD)));
/* 1362 */     register(context, WEAPONSMITH_3_FLINT_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.FLINT, 24), new ItemStackTemplate(Items.EMERALD), 12, 20, 0.05F, 
/*      */           
/* 1368 */           Optional.empty(), 
/* 1369 */           List.of()));
/* 1372 */     register(context, WEAPONSMITH_4_EMERALD_ENCHANTED_DIAMOND_AXE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 12), new ItemStackTemplate(Items.DIAMOND_AXE), 3, 15, 0.2F, 
/*      */           
/* 1378 */           Optional.empty(), 
/* 1379 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.DIAMOND_AXE)));
/* 1381 */     register(context, WEAPONSMITH_4_DIAMOND_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.DIAMOND, 1), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */           
/* 1387 */           Optional.empty(), 
/* 1388 */           List.of()));
/* 1391 */     register(context, WEAPONSMITH_5_EMERALD_ENCHANTED_DIAMOND_SWORD, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 8), new ItemStackTemplate(Items.DIAMOND_SWORD), 3, 30, 0.2F, 
/*      */           
/* 1397 */           Optional.empty(), 
/* 1398 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.DIAMOND_SWORD)));
/* 1401 */     register(context, TOOLSMITH_1_EMERALD_STONE_AXE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.STONE_AXE), 12, 1, 0.2F, 
/*      */           
/* 1407 */           Optional.empty(), 
/* 1408 */           List.of()));
/* 1410 */     register(context, TOOLSMITH_1_EMERALD_STONE_SHOVEL, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.STONE_SHOVEL), 12, 1, 0.2F, 
/*      */           
/* 1416 */           Optional.empty(), 
/* 1417 */           List.of()));
/* 1419 */     register(context, TOOLSMITH_1_EMERALD_STONE_PICKAXE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.STONE_PICKAXE), 12, 1, 0.2F, 
/*      */           
/* 1425 */           Optional.empty(), 
/* 1426 */           List.of()));
/* 1428 */     register(context, TOOLSMITH_1_EMERALD_STONE_HOE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.STONE_HOE), 12, 1, 0.2F, 
/*      */           
/* 1434 */           Optional.empty(), 
/* 1435 */           List.of()));
/* 1439 */     register(context, TOOLSMITH_3_FLINT_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.FLINT, 30), new ItemStackTemplate(Items.EMERALD), 12, 20, 0.05F, 
/*      */           
/* 1445 */           Optional.empty(), 
/* 1446 */           List.of()));
/* 1448 */     register(context, TOOLSMITH_3_EMERALD_IRON_AXE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.IRON_AXE), 3, 10, 0.2F, 
/*      */           
/* 1454 */           Optional.empty(), 
/* 1455 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.IRON_AXE)));
/* 1457 */     register(context, TOOLSMITH_3_EMERALD_IRON_SHOVEL, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), new ItemStackTemplate(Items.IRON_SHOVEL), 3, 10, 0.2F, 
/*      */           
/* 1463 */           Optional.empty(), 
/* 1464 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.IRON_SHOVEL)));
/* 1466 */     register(context, TOOLSMITH_3_EMERALD_IRON_PICKAXE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.IRON_PICKAXE), 3, 10, 0.2F, 
/*      */           
/* 1472 */           Optional.empty(), 
/* 1473 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.IRON_PICKAXE)));
/* 1475 */     register(context, TOOLSMITH_3_EMERALD_DIAMOND_HOE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 4), new ItemStackTemplate(Items.DIAMOND_HOE), 3, 10, 0.2F, 
/*      */           
/* 1481 */           Optional.empty(), 
/* 1482 */           List.of()));
/* 1485 */     register(context, TOOLSMITH_4_EMERALD_DIAMOND_AXE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 12), new ItemStackTemplate(Items.DIAMOND_AXE), 3, 15, 0.2F, 
/*      */           
/* 1491 */           Optional.empty(), 
/* 1492 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.DIAMOND_AXE)));
/* 1494 */     register(context, TOOLSMITH_4_EMERALD_DIAMOND_SHOVEL, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 5), new ItemStackTemplate(Items.DIAMOND_SHOVEL), 3, 15, 0.2F, 
/*      */           
/* 1500 */           Optional.empty(), 
/* 1501 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.DIAMOND_SHOVEL)));
/* 1503 */     register(context, TOOLSMITH_4_DIAMOND_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.DIAMOND, 1), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */           
/* 1509 */           Optional.empty(), 
/* 1510 */           List.of()));
/* 1513 */     register(context, TOOLSMITH_5_EMERALD_DIAMOND_PICKAXE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 13), new ItemStackTemplate(Items.DIAMOND_PICKAXE), 3, 30, 0.2F, 
/*      */           
/* 1519 */           Optional.empty(), 
/* 1520 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.DIAMOND_PICKAXE)));
/* 1523 */     register(context, BUTCHER_1_CHICKEN_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.CHICKEN, 14), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/* 1529 */           Optional.empty(), 
/* 1530 */           List.of()));
/* 1532 */     register(context, BUTCHER_1_PORKCHOP_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.PORKCHOP, 7), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/* 1538 */           Optional.empty(), 
/* 1539 */           List.of()));
/* 1541 */     register(context, BUTCHER_1_RABBIT_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.RABBIT, 4), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/* 1547 */           Optional.empty(), 
/* 1548 */           List.of()));
/* 1550 */     register(context, BUTCHER_1_EMERALD_RABBIT_STEW, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.RABBIT_STEW), 12, 1, 0.05F, 
/*      */           
/* 1556 */           Optional.empty(), 
/* 1557 */           List.of()));
/* 1560 */     register(context, BUTCHER_2_COAL_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.COAL, 15), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/* 1566 */           Optional.empty(), 
/* 1567 */           List.of()));
/* 1569 */     register(context, BUTCHER_2_EMERALD_COOKED_PORKCHOP, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.COOKED_PORKCHOP, 5), 16, 5, 0.05F, 
/*      */           
/* 1575 */           Optional.empty(), 
/* 1576 */           List.of()));
/* 1578 */     register(context, BUTCHER_2_EMERALD_COOKED_CHICKEN, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.COOKED_CHICKEN, 8), 16, 5, 0.05F, 
/*      */           
/* 1584 */           Optional.empty(), 
/* 1585 */           List.of()));
/* 1588 */     register(context, BUTCHER_3_MUTTON_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.MUTTON, 7), new ItemStackTemplate(Items.EMERALD), 16, 20, 0.05F, 
/*      */           
/* 1594 */           Optional.empty(), 
/* 1595 */           List.of()));
/* 1597 */     register(context, BUTCHER_3_BEEF_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.BEEF, 10), new ItemStackTemplate(Items.EMERALD), 16, 20, 0.05F, 
/*      */           
/* 1603 */           Optional.empty(), 
/* 1604 */           List.of()));
/* 1607 */     register(context, BUTCHER_4_DRIED_KELP_BLOCK_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.DRIED_KELP_BLOCK, 10), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */           
/* 1613 */           Optional.empty(), 
/* 1614 */           List.of()));
/* 1617 */     register(context, BUTCHER_5_SWEET_BERRIES_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.SWEET_BERRIES, 10), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */           
/* 1623 */           Optional.empty(), 
/* 1624 */           List.of()));
/* 1627 */     register(context, LEATHERWORKER_1_LEATHER_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.LEATHER, 6), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/* 1633 */           Optional.empty(), 
/* 1634 */           List.of()));
/* 1636 */     register(context, LEATHERWORKER_1_EMERALD_DYED_LEATHER_LEGGINGS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.LEATHER_LEGGINGS), 12, 1, 0.2F, 
/*      */           
/* 1642 */           Optional.empty(), 
/* 1643 */           dyedItem(items, Items.LEATHER_LEGGINGS)));
/* 1645 */     register(context, LEATHERWORKER_1_EMERALD_DYED_LEATHER_CHESTPLATE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 7), new ItemStackTemplate(Items.LEATHER_CHESTPLATE), 12, 1, 0.2F, 
/*      */           
/* 1651 */           Optional.empty(), 
/* 1652 */           dyedItem(items, Items.LEATHER_CHESTPLATE)));
/* 1655 */     register(context, LEATHERWORKER_2_FLINT_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.FLINT, 26), new ItemStackTemplate(Items.EMERALD), 12, 10, 0.05F, 
/*      */           
/* 1661 */           Optional.empty(), 
/* 1662 */           List.of()));
/* 1664 */     register(context, LEATHERWORKER_2_EMERALD_DYED_LEATHER_HELMET, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 5), new ItemStackTemplate(Items.LEATHER_HELMET), 12, 5, 0.2F, 
/*      */           
/* 1670 */           Optional.empty(), 
/* 1671 */           dyedItem(items, Items.LEATHER_HELMET)));
/* 1673 */     register(context, LEATHERWORKER_2_EMERALD_DYED_LEATHER_BOOTS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 4), new ItemStackTemplate(Items.LEATHER_BOOTS), 12, 5, 0.2F, 
/*      */           
/* 1679 */           Optional.empty(), 
/* 1680 */           dyedItem(items, Items.LEATHER_BOOTS)));
/* 1683 */     register(context, LEATHERWORKER_3_RABBIT_HIDE_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.RABBIT_HIDE, 9), new ItemStackTemplate(Items.EMERALD), 12, 20, 0.05F, 
/*      */           
/* 1689 */           Optional.empty(), 
/* 1690 */           List.of()));
/* 1692 */     register(context, LEATHERWORKER_3_EMERALD_DYED_LEATHER_CHESTPLATE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 7), new ItemStackTemplate(Items.LEATHER_CHESTPLATE), 12, 1, 0.2F, 
/*      */           
/* 1699 */           Optional.empty(), 
/* 1700 */           dyedItem(items, Items.LEATHER_CHESTPLATE)));
/* 1703 */     register(context, LEATHERWORKER_4_TURTLE_SCUTE_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.TURTLE_SCUTE, 4), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */           
/* 1709 */           Optional.empty(), 
/* 1710 */           List.of()));
/* 1712 */     register(context, LEATHERWORKER_4_EMERALD_DYED_LEATHER_HORSE_ARMOR, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 6), new ItemStackTemplate(Items.LEATHER_HORSE_ARMOR), 12, 15, 0.2F, 
/*      */           
/* 1718 */           Optional.empty(), 
/* 1719 */           dyedItem(items, Items.LEATHER_HORSE_ARMOR)));
/* 1722 */     register(context, LEATHERWORKER_5_EMERALD_SADDLE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 6), new ItemStackTemplate(Items.SADDLE), 12, 30, 0.2F, 
/*      */           
/* 1728 */           Optional.empty(), 
/* 1729 */           List.of()));
/* 1731 */     register(context, LEATHERWORKER_5_EMERALD_DYED_LEATHER_HELMET, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 5), new ItemStackTemplate(Items.LEATHER_HELMET), 12, 5, 0.2F, 
/*      */           
/* 1738 */           Optional.empty(), 
/* 1739 */           dyedItem(items, Items.LEATHER_HELMET)));
/* 1742 */     register(context, MASON_1_CLAY_BALL_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.CLAY_BALL, 10), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */           
/* 1748 */           Optional.empty(), 
/* 1749 */           List.of()));
/* 1751 */     register(context, MASON_1_EMERALD_BRICK, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.BRICK, 10), 16, 1, 0.05F, 
/*      */           
/* 1757 */           Optional.empty(), 
/* 1758 */           List.of()));
/* 1761 */     register(context, MASON_2_STONE_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.STONE, 20), new ItemStackTemplate(Items.EMERALD), 16, 10, 0.05F, 
/*      */           
/* 1767 */           Optional.empty(), 
/* 1768 */           List.of()));
/* 1770 */     register(context, MASON_2_EMERALD_CHISELED_STONE_BRICKS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.CHISELED_STONE_BRICKS, 4), 16, 5, 0.05F, 
/*      */           
/* 1776 */           Optional.empty(), 
/* 1777 */           List.of()));
/* 1780 */     registerMasonLevelThreeStones(context);
/* 1781 */     registerMasonLevelThreeBlocks(context);
/* 1783 */     register(context, MASON_4_QUARTZ_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.QUARTZ, 12), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */           
/* 1789 */           Optional.empty(), 
/* 1790 */           List.of()));
/* 1792 */     registerMasonLevelFourTerracotta(context);
/* 1794 */     register(context, MASON_5_EMERALD_QUARTZ_PILLAR, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.QUARTZ_PILLAR), 12, 30, 0.05F, 
/*      */           
/* 1800 */           Optional.empty(), 
/* 1801 */           List.of()));
/* 1803 */     register(context, MASON_5_EMERALD_QUARTZ_BLOCK, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.QUARTZ_BLOCK), 12, 30, 0.05F, 
/*      */           
/* 1809 */           Optional.empty(), 
/* 1810 */           List.of()));
/* 1814 */     context.register(WANDERING_TRADER_WATER_BOTTLE_EMERALD, new VillagerTrade(new TradeCost((Holder<Item>)Items.POTION
/* 1815 */             .builtInRegistryHolder(), (NumberProvider)ConstantValue.exactly(1.0F), DataComponentExactPredicate.expect(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER))), new ItemStackTemplate(Items.EMERALD), 2, 1, 0.05F, 
/*      */           
/* 1820 */           Optional.empty(), 
/* 1821 */           List.of()));
/* 1823 */     context.register(WANDERING_TRADER_WATER_BUCKET_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.WATER_BUCKET, 1), new ItemStackTemplate(Items.EMERALD, 2), 2, 1, 0.05F, 
/*      */           
/* 1829 */           Optional.empty(), 
/* 1830 */           List.of()));
/* 1832 */     context.register(WANDERING_TRADER_MILK_BUCKET_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.MILK_BUCKET, 1), new ItemStackTemplate(Items.EMERALD, 2), 2, 1, 0.05F, 
/*      */           
/* 1838 */           Optional.empty(), 
/* 1839 */           List.of()));
/* 1841 */     context.register(WANDERING_TRADER_FERMENTED_SPIDER_EYE_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.FERMENTED_SPIDER_EYE, 1), new ItemStackTemplate(Items.EMERALD, 3), 2, 1, 0.05F, 
/*      */           
/* 1847 */           Optional.empty(), 
/* 1848 */           List.of()));
/* 1850 */     context.register(WANDERING_TRADER_BAKED_POTATO_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.BAKED_POTATO, 4), new ItemStackTemplate(Items.EMERALD, 1), 2, 1, 0.05F, 
/*      */           
/* 1856 */           Optional.empty(), 
/* 1857 */           List.of()));
/* 1859 */     context.register(WANDERING_TRADER_HAY_BLOCK_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.HAY_BLOCK, 1), new ItemStackTemplate(Items.EMERALD), 2, 1, 0.05F, 
/*      */           
/* 1865 */           Optional.empty(), 
/* 1866 */           List.of()));
/* 1869 */     context.register(WANDERING_TRADER_EMERALD_PACKED_ICE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.PACKED_ICE), 6, 1, 0.05F, 
/*      */           
/* 1875 */           Optional.empty(), 
/* 1876 */           List.of()));
/* 1878 */     context.register(WANDERING_TRADER_EMERALD_BLUE_ICE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 6), new ItemStackTemplate(Items.BLUE_ICE), 6, 1, 0.05F, 
/*      */           
/* 1884 */           Optional.empty(), 
/* 1885 */           List.of()));
/* 1887 */     context.register(WANDERING_TRADER_EMERALD_GUNPOWDER, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.GUNPOWDER, 4), 2, 1, 0.05F, 
/*      */           
/* 1893 */           Optional.empty(), 
/* 1894 */           List.of()));
/* 1896 */     context.register(WANDERING_TRADER_EMERALD_PODZOL, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.PODZOL, 3), 6, 1, 0.05F, 
/*      */           
/* 1902 */           Optional.empty(), 
/* 1903 */           List.of()));
/* 1906 */     context.register(WANDERING_TRADER_EMERALD_ACACIA_LOG, createWanderingTraderLogSell(Items.ACACIA_LOG));
/* 1907 */     context.register(WANDERING_TRADER_EMERALD_BIRCH_LOG, createWanderingTraderLogSell(Items.BIRCH_LOG));
/* 1908 */     context.register(WANDERING_TRADER_EMERALD_DARK_OAK_LOG, createWanderingTraderLogSell(Items.DARK_OAK_LOG));
/* 1909 */     context.register(WANDERING_TRADER_EMERALD_JUNGLE_LOG, createWanderingTraderLogSell(Items.JUNGLE_LOG));
/* 1910 */     context.register(WANDERING_TRADER_EMERALD_OAK_LOG, createWanderingTraderLogSell(Items.OAK_LOG));
/* 1911 */     context.register(WANDERING_TRADER_EMERALD_SPRUCE_LOG, createWanderingTraderLogSell(Items.SPRUCE_LOG));
/* 1912 */     context.register(WANDERING_TRADER_EMERALD_CHERRY_LOG, createWanderingTraderLogSell(Items.CHERRY_LOG));
/* 1913 */     context.register(WANDERING_TRADER_EMERALD_MANGROVE_LOG, createWanderingTraderLogSell(Items.MANGROVE_LOG));
/* 1914 */     context.register(WANDERING_TRADER_EMERALD_PALE_OAK_LOG, createWanderingTraderLogSell(Items.PALE_OAK_LOG));
/* 1916 */     context.register(WANDERING_TRADER_EMERALD_ENCHANTED_IRON_PICKAXE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.IRON_PICKAXE), 1, 1, 0.2F, 
/*      */           
/* 1922 */           Optional.empty(), 
/* 1923 */           enchantedItem(items, (HolderSet<Enchantment>)named1, Items.IRON_PICKAXE)));
/* 1925 */     context.register(WANDERING_TRADER_EMERALD_LONG_INVISIBILITY_POTION, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 5), new ItemStackTemplate(Items.POTION), 1, 1, 0.05F, 
/*      */           
/* 1931 */           Optional.empty(), 
/* 1932 */           List.of(SetPotionFunction.setPotion(Potions.LONG_INVISIBILITY).build())));
/* 1935 */     context.register(WANDERING_TRADER_EMERALD_TROPICAL_FISH_BUCKET, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.TROPICAL_FISH_BUCKET), 4, 1, 0.05F, 
/*      */           
/* 1941 */           Optional.empty(), 
/* 1942 */           List.of()));
/* 1944 */     context.register(WANDERING_TRADER_EMERALD_PUFFERFISH_BUCKET, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.PUFFERFISH_BUCKET), 4, 1, 0.05F, 
/*      */           
/* 1950 */           Optional.empty(), 
/* 1951 */           List.of()));
/* 1953 */     context.register(WANDERING_TRADER_EMERALD_SEA_PICKLE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), new ItemStackTemplate(Items.SEA_PICKLE), 5, 1, 0.05F, 
/*      */           
/* 1959 */           Optional.empty(), 
/* 1960 */           List.of()));
/* 1962 */     context.register(WANDERING_TRADER_EMERALD_SLIME_BALL, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 4), new ItemStackTemplate(Items.SLIME_BALL), 5, 1, 0.05F, 
/*      */           
/* 1968 */           Optional.empty(), 
/* 1969 */           List.of()));
/* 1971 */     context.register(WANDERING_TRADER_EMERALD_GLOWSTONE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), new ItemStackTemplate(Items.GLOWSTONE), 5, 1, 0.05F, 
/*      */           
/* 1977 */           Optional.empty(), 
/* 1978 */           List.of()));
/* 1980 */     context.register(WANDERING_TRADER_EMERALD_NAUTILUS_SHELL, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 5), new ItemStackTemplate(Items.NAUTILUS_SHELL), 5, 1, 0.05F, 
/*      */           
/* 1986 */           Optional.empty(), 
/* 1987 */           List.of()));
/* 1989 */     context.register(WANDERING_TRADER_EMERALD_FERN, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.FERN), 12, 1, 0.05F, 
/*      */           
/* 1995 */           Optional.empty(), 
/* 1996 */           List.of()));
/* 1998 */     context.register(WANDERING_TRADER_EMERALD_SUGAR_CANE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.SUGAR_CANE), 8, 1, 0.05F, 
/*      */           
/* 2004 */           Optional.empty(), 
/* 2005 */           List.of()));
/* 2007 */     context.register(WANDERING_TRADER_EMERALD_PUMPKIN, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.PUMPKIN), 4, 1, 0.05F, 
/*      */           
/* 2013 */           Optional.empty(), 
/* 2014 */           List.of()));
/* 2016 */     context.register(WANDERING_TRADER_EMERALD_KELP, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.KELP), 12, 1, 0.05F, 
/*      */           
/* 2022 */           Optional.empty(), 
/* 2023 */           List.of()));
/* 2025 */     context.register(WANDERING_TRADER_EMERALD_CACTUS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.CACTUS), 8, 1, 0.05F, 
/*      */           
/* 2031 */           Optional.empty(), 
/* 2032 */           List.of()));
/* 2034 */     registerWanderingTraderFlowers(context);
/* 2035 */     registerWanderingTraderSeeds(context);
/* 2036 */     registerWanderingTraderSaplings(context);
/* 2037 */     registerWanderingTraderDyes(context);
/* 2039 */     context.register(WANDERING_TRADER_EMERALD_BRAIN_CORAL_BLOCK, createWanderingTraderCoralBlockSell(Items.BRAIN_CORAL_BLOCK));
/* 2040 */     context.register(WANDERING_TRADER_EMERALD_BUBBLE_CORAL_BLOCK, createWanderingTraderCoralBlockSell(Items.BUBBLE_CORAL_BLOCK));
/* 2041 */     context.register(WANDERING_TRADER_EMERALD_FIRE_CORAL_BLOCK, createWanderingTraderCoralBlockSell(Items.FIRE_CORAL_BLOCK));
/* 2042 */     context.register(WANDERING_TRADER_EMERALD_HORN_CORAL_BLOCK, createWanderingTraderCoralBlockSell(Items.HORN_CORAL_BLOCK));
/* 2043 */     context.register(WANDERING_TRADER_EMERALD_TUBE_CORAL_BLOCK, createWanderingTraderCoralBlockSell(Items.TUBE_CORAL_BLOCK));
/* 2045 */     context.register(WANDERING_TRADER_EMERALD_VINE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.VINE, 3), 4, 1, 0.05F, 
/*      */           
/* 2051 */           Optional.empty(), 
/* 2052 */           List.of()));
/* 2054 */     context.register(WANDERING_TRADER_EMERALD_PALE_HANGING_MOSS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.PALE_HANGING_MOSS, 3), 4, 1, 0.05F, 
/*      */           
/* 2060 */           Optional.empty(), 
/* 2061 */           List.of()));
/* 2063 */     context.register(WANDERING_TRADER_EMERALD_BROWN_MUSHROOM, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.BROWN_MUSHROOM, 3), 4, 1, 0.05F, 
/*      */           
/* 2069 */           Optional.empty(), 
/* 2070 */           List.of()));
/* 2072 */     context.register(WANDERING_TRADER_EMERALD_RED_MUSHROOM, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.RED_MUSHROOM, 3), 4, 1, 0.05F, 
/*      */           
/* 2078 */           Optional.empty(), 
/* 2079 */           List.of()));
/* 2081 */     context.register(WANDERING_TRADER_EMERALD_LILY_PAD, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.LILY_PAD, 5), 2, 1, 0.05F, 
/*      */           
/* 2087 */           Optional.empty(), 
/* 2088 */           List.of()));
/* 2090 */     context.register(WANDERING_TRADER_EMERALD_SMALL_DRIPLEAF, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.SMALL_DRIPLEAF, 2), 5, 1, 0.05F, 
/*      */           
/* 2096 */           Optional.empty(), 
/* 2097 */           List.of()));
/* 2099 */     context.register(WANDERING_TRADER_EMERALD_SAND, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.SAND, 8), 8, 1, 0.05F, 
/*      */           
/* 2105 */           Optional.empty(), 
/* 2106 */           List.of()));
/* 2108 */     context.register(WANDERING_TRADER_EMERALD_RED_SAND, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.RED_SAND, 4), 6, 1, 0.05F, 
/*      */           
/* 2114 */           Optional.empty(), 
/* 2115 */           List.of()));
/* 2117 */     context.register(WANDERING_TRADER_EMERALD_POINTED_DRIPSTONE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.POINTED_DRIPSTONE, 2), 5, 1, 0.05F, 
/*      */           
/* 2123 */           Optional.empty(), 
/* 2124 */           List.of()));
/* 2126 */     context.register(WANDERING_TRADER_EMERALD_SULFUR_SPIKE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.SULFUR_SPIKE, 2), 5, 1, 0.05F, 
/*      */           
/* 2132 */           Optional.empty(), 
/* 2133 */           List.of()));
/* 2135 */     context.register(WANDERING_TRADER_EMERALD_ROOTED_DIRT, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.ROOTED_DIRT, 2), 5, 1, 0.05F, 
/*      */           
/* 2141 */           Optional.empty(), 
/* 2142 */           List.of()));
/* 2144 */     context.register(WANDERING_TRADER_EMERALD_MOSS_BLOCK, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.MOSS_BLOCK, 2), 5, 1, 0.05F, 
/*      */           
/* 2150 */           Optional.empty(), 
/* 2151 */           List.of()));
/* 2153 */     context.register(WANDERING_TRADER_EMERALD_PALE_MOSS_BLOCK, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.PALE_MOSS_BLOCK, 2), 5, 1, 0.05F, 
/*      */           
/* 2159 */           Optional.empty(), 
/* 2160 */           List.of()));
/* 2162 */     context.register(WANDERING_TRADER_EMERALD_FIREFLY_BUSH, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.FIREFLY_BUSH), 12, 1, 0.05F, 
/*      */           
/* 2168 */           Optional.empty(), 
/* 2169 */           List.of()));
/* 2172 */     return (Holder<VillagerTrade>)context.register(WANDERING_TRADER_EMERALD_NAME_TAG, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(Items.NAME_TAG), 5, 1, 0.05F, 
/*      */           
/* 2178 */           Optional.empty(), 
/* 2179 */           List.of()));
/*      */   }
/*      */   
/*      */   private static VillagerTrade createWanderingTraderCoralBlockSell(Item item) {
/* 2185 */     return new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(item), 8, 1, 0.05F, 
/*      */         
/* 2191 */         Optional.empty(), 
/* 2192 */         List.of());
/*      */   }
/*      */   
/*      */   private static VillagerTrade createWanderingTraderLogSell(Item item) {
/* 2197 */     return new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(item, 8), 4, 1, 0.05F, 
/*      */         
/* 2203 */         Optional.empty(), 
/* 2204 */         List.of());
/*      */   }
/*      */   
/*      */   private static void registerWanderingTraderFlowers(BootstrapContext<VillagerTrade> context) {
/* 2209 */     context.register(WANDERING_TRADER_EMERALD_BLUE_ORCHID, createWanderingTraderFlowerSell(Items.BLUE_ORCHID, 8));
/* 2210 */     context.register(WANDERING_TRADER_EMERALD_DANDELION, createWanderingTraderFlowerSell(Items.DANDELION, 12));
/* 2211 */     context.register(WANDERING_TRADER_EMERALD_POPPY, createWanderingTraderFlowerSell(Items.POPPY, 12));
/* 2212 */     context.register(WANDERING_TRADER_EMERALD_ALLIUM, createWanderingTraderFlowerSell(Items.ALLIUM, 12));
/* 2213 */     context.register(WANDERING_TRADER_EMERALD_AZURE_BLUET, createWanderingTraderFlowerSell(Items.AZURE_BLUET, 12));
/* 2214 */     context.register(WANDERING_TRADER_EMERALD_RED_TULIP, createWanderingTraderFlowerSell(Items.RED_TULIP, 12));
/* 2215 */     context.register(WANDERING_TRADER_EMERALD_ORANGE_TULIP, createWanderingTraderFlowerSell(Items.ORANGE_TULIP, 12));
/* 2216 */     context.register(WANDERING_TRADER_EMERALD_WHITE_TULIP, createWanderingTraderFlowerSell(Items.WHITE_TULIP, 12));
/* 2217 */     context.register(WANDERING_TRADER_EMERALD_PINK_TULIP, createWanderingTraderFlowerSell(Items.PINK_TULIP, 12));
/* 2218 */     context.register(WANDERING_TRADER_EMERALD_OXEYE_DAISY, createWanderingTraderFlowerSell(Items.OXEYE_DAISY, 12));
/* 2219 */     context.register(WANDERING_TRADER_EMERALD_CORNFLOWER, createWanderingTraderFlowerSell(Items.CORNFLOWER, 12));
/* 2220 */     context.register(WANDERING_TRADER_EMERALD_WILDFLOWERS, createWanderingTraderFlowerSell(Items.WILDFLOWERS, 12));
/* 2221 */     context.register(WANDERING_TRADER_EMERALD_DRY_TALL_GRASS, createWanderingTraderFlowerSell(Items.DRY_TALL_GRASS, 12));
/* 2222 */     context.register(WANDERING_TRADER_EMERALD_LILY_OF_THE_VALLEY, createWanderingTraderFlowerSell(Items.LILY_OF_THE_VALLEY, 7));
/* 2223 */     context.register(WANDERING_TRADER_EMERALD_OPEN_EYEBLOSSOM, createWanderingTraderFlowerSell(Items.OPEN_EYEBLOSSOM, 7));
/* 2225 */     context.register(WANDERING_TRADER_EMERALD_GOLDEN_DANDELION, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), new ItemStackTemplate(Items.GOLDEN_DANDELION), 12, 1, 0.05F, 
/*      */           
/* 2231 */           Optional.empty(), 
/* 2232 */           List.of()));
/*      */   }
/*      */   
/*      */   private static VillagerTrade createWanderingTraderFlowerSell(Item item, int maxUses) {
/* 2237 */     return new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(item), maxUses, 1, 0.05F, 
/*      */         
/* 2243 */         Optional.empty(), 
/* 2244 */         List.of());
/*      */   }
/*      */   
/*      */   private static void registerWanderingTraderSeeds(BootstrapContext<VillagerTrade> context) {
/* 2249 */     context.register(WANDERING_TRADER_EMERALD_WHEAT_SEEDS, createWanderingTraderSeedSell(Items.WHEAT_SEEDS));
/* 2250 */     context.register(WANDERING_TRADER_EMERALD_BEETROOT_SEEDS, createWanderingTraderSeedSell(Items.BEETROOT_SEEDS));
/* 2251 */     context.register(WANDERING_TRADER_EMERALD_PUMPKIN_SEEDS, createWanderingTraderSeedSell(Items.PUMPKIN_SEEDS));
/* 2252 */     context.register(WANDERING_TRADER_EMERALD_MELON_SEEDS, createWanderingTraderSeedSell(Items.MELON_SEEDS));
/*      */   }
/*      */   
/*      */   private static VillagerTrade createWanderingTraderSeedSell(Item item) {
/* 2256 */     return new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(item), 12, 1, 0.05F, 
/*      */         
/* 2262 */         Optional.empty(), 
/* 2263 */         List.of());
/*      */   }
/*      */   
/*      */   private static void registerWanderingTraderSaplings(BootstrapContext<VillagerTrade> context) {
/* 2268 */     context.register(WANDERING_TRADER_EMERALD_ACACIA_SAPLING, createWanderingTraderSaplingSell(Items.ACACIA_SAPLING));
/* 2269 */     context.register(WANDERING_TRADER_EMERALD_BIRCH_SAPLING, createWanderingTraderSaplingSell(Items.BIRCH_SAPLING));
/* 2270 */     context.register(WANDERING_TRADER_EMERALD_DARK_OAK_SAPLING, createWanderingTraderSaplingSell(Items.DARK_OAK_SAPLING));
/* 2271 */     context.register(WANDERING_TRADER_EMERALD_JUNGLE_SAPLING, createWanderingTraderSaplingSell(Items.JUNGLE_SAPLING));
/* 2272 */     context.register(WANDERING_TRADER_EMERALD_OAK_SAPLING, createWanderingTraderSaplingSell(Items.OAK_SAPLING));
/* 2273 */     context.register(WANDERING_TRADER_EMERALD_SPRUCE_SAPLING, createWanderingTraderSaplingSell(Items.SPRUCE_SAPLING));
/* 2274 */     context.register(WANDERING_TRADER_EMERALD_CHERRY_SAPLING, createWanderingTraderSaplingSell(Items.CHERRY_SAPLING));
/* 2275 */     context.register(WANDERING_TRADER_EMERALD_PALE_OAK_SAPLING, createWanderingTraderSaplingSell(Items.PALE_OAK_SAPLING));
/* 2276 */     context.register(WANDERING_TRADER_EMERALD_MANGROVE_PROPAGULE, createWanderingTraderSaplingSell(Items.MANGROVE_PROPAGULE));
/*      */   }
/*      */   
/*      */   private static VillagerTrade createWanderingTraderSaplingSell(Item item) {
/* 2280 */     return new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 5), new ItemStackTemplate(item), 8, 1, 0.05F, 
/*      */         
/* 2286 */         Optional.empty(), 
/* 2287 */         List.of());
/*      */   }
/*      */   
/*      */   private static void registerWanderingTraderDyes(BootstrapContext<VillagerTrade> context) {
/* 2292 */     ColorCollection.zipApply(WANDERING_TRADER_EMERALD_DYE, Items.DYE, (name, dye) -> context.register(name, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(dye, 3), 12, 1, 0.05F, Optional.empty(), List.of())));
/*      */   }
/*      */   
/*      */   private static void registerMasonLevelFourTerracotta(BootstrapContext<VillagerTrade> context) {
/* 2306 */     registerWanderingTraderTerracottaSellTrades(context, MASON_4_EMERALD_TERRACOTTA, Items.DYED_TERRACOTTA);
/* 2307 */     registerWanderingTraderTerracottaSellTrades(context, MASON_4_EMERALD_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA);
/*      */   }
/*      */   
/*      */   private static void registerWanderingTraderTerracottaSellTrades(BootstrapContext<VillagerTrade> context, ColorCollection<ResourceKey<VillagerTrade>> trades, ColorCollection<Item> items) {
/* 2311 */     ColorCollection.zipApply(trades, items, (trade, item) -> register(context, trade, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(item), 12, 15, 0.05F, Optional.empty(), List.of())));
/*      */   }
/*      */   
/*      */   private static void registerMasonLevelThreeBlocks(BootstrapContext<VillagerTrade> context) {
/* 2325 */     register(context, MASON_3_EMERALD_DRIPSTONE_BLOCK, createMasonStoneSell(Items.DRIPSTONE_BLOCK));
/* 2326 */     register(context, MASON_3_EMERALD_POLISHED_ANDESITE, createMasonStoneSell(Items.POLISHED_ANDESITE));
/* 2327 */     register(context, MASON_3_EMERALD_POLISHED_DIORITE, createMasonStoneSell(Items.POLISHED_DIORITE));
/* 2328 */     register(context, MASON_3_EMERALD_POLISHED_GRANTITE, createMasonStoneSell(Items.POLISHED_GRANITE));
/*      */   }
/*      */   
/*      */   private static VillagerTrade createMasonStoneSell(Item item) {
/* 2332 */     return new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(item, 4), 16, 10, 0.05F, 
/*      */         
/* 2338 */         Optional.empty(), 
/* 2339 */         List.of());
/*      */   }
/*      */   
/*      */   private static void registerMasonLevelThreeStones(BootstrapContext<VillagerTrade> context) {
/* 2344 */     register(context, MASON_3_GRANITE_EMERALD, createMasonStoneBuy(Items.GRANITE));
/* 2345 */     register(context, MASON_3_ANDESITE_EMERALD, createMasonStoneBuy(Items.ANDESITE));
/* 2346 */     register(context, MASON_3_DIORITE_EMERALD, createMasonStoneBuy(Items.DIORITE));
/*      */   }
/*      */   
/*      */   private static VillagerTrade createMasonStoneBuy(Item item) {
/* 2350 */     return new VillagerTrade(new TradeCost((ItemLike)item, 16), new ItemStackTemplate(Items.EMERALD), 16, 20, 0.05F, 
/*      */         
/* 2356 */         Optional.empty(), 
/* 2357 */         List.of());
/*      */   }
/*      */   
/*      */   private static void registerBoatTrades(BootstrapContext<VillagerTrade> context, HolderGetter<VillagerType> villagerVariants) {
/* 2362 */     register(context, FISHERMAN_5_OAK_BOAT_EMERALD, createMasonBoatBuyTrade(villagerVariants, Items.OAK_BOAT, List.of(VillagerType.PLAINS)));
/* 2363 */     register(context, FISHERMAN_5_SPRUCE_BOAT_EMERALD, createMasonBoatBuyTrade(villagerVariants, Items.SPRUCE_BOAT, List.of(VillagerType.TAIGA, VillagerType.SNOW)));
/* 2364 */     register(context, FISHERMAN_5_JUNGLE_BOAT_EMERALD, createMasonBoatBuyTrade(villagerVariants, Items.JUNGLE_BOAT, List.of(VillagerType.DESERT, VillagerType.JUNGLE)));
/* 2365 */     register(context, FISHERMAN_5_ACACIA_BOAT_EMERALD, createMasonBoatBuyTrade(villagerVariants, Items.ACACIA_BOAT, List.of(VillagerType.SAVANNA)));
/* 2366 */     register(context, FISHERMAN_5_DARK_OAK_BOAT_EMERALD, createMasonBoatBuyTrade(villagerVariants, Items.DARK_OAK_BOAT, List.of(VillagerType.SWAMP)));
/*      */   }
/*      */   
/*      */   private static VillagerTrade createMasonBoatBuyTrade(HolderGetter<VillagerType> villagerVariants, Item item, List<ResourceKey<VillagerType>> villagerTypes) {
/* 2370 */     return new VillagerTrade(new TradeCost((ItemLike)item, 1), new ItemStackTemplate(Items.EMERALD), 12, 30, 0.05F, 
/*      */         
/* 2376 */         villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, villagerTypes)), 
/* 2377 */         List.of());
/*      */   }
/*      */   
/*      */   private static void registerShepherdWoolSales(BootstrapContext<VillagerTrade> context) {
/* 2382 */     register(context, SHEPHERD_1_WHITE_WOOL_EMERALD, createShepherdWoolBuy((Item)Items.WOOL.white()));
/* 2383 */     register(context, SHEPHERD_1_BROWN_WOOL_EMERALD, createShepherdWoolBuy((Item)Items.WOOL.brown()));
/* 2384 */     register(context, SHEPHERD_1_GRAY_WOOL_EMERALD, createShepherdWoolBuy((Item)Items.WOOL.gray()));
/* 2385 */     register(context, SHEPHERD_1_BLACK_WOOL_EMERALD, createShepherdWoolBuy((Item)Items.WOOL.black()));
/*      */   }
/*      */   
/*      */   private static VillagerTrade createShepherdWoolBuy(Item item) {
/* 2389 */     return new VillagerTrade(new TradeCost((ItemLike)item, 18), new ItemStackTemplate(Items.EMERALD), 16, 2, 0.05F, 
/*      */         
/* 2395 */         Optional.empty(), 
/* 2396 */         List.of());
/*      */   }
/*      */   
/*      */   private static void registerShepherdLevelTwoDyeTrades(BootstrapContext<VillagerTrade> context) {
/* 2401 */     register(context, SHEPHERD_2_WHITE_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.white(), 10));
/* 2402 */     register(context, SHEPHERD_2_GRAY_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.gray(), 10));
/* 2403 */     register(context, SHEPHERD_2_BLACK_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.black(), 10));
/* 2404 */     register(context, SHEPHERD_2_LIGHT_BLUE_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.lightBlue(), 10));
/* 2405 */     register(context, SHEPHERD_2_LIME_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.lime(), 10));
/*      */   }
/*      */   
/*      */   private static void registerShepherdLevelThreeDyeTrades(BootstrapContext<VillagerTrade> context) {
/* 2409 */     register(context, SHEPHERD_3_YELLOW_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.yellow(), 20));
/* 2410 */     register(context, SHEPHERD_3_LIGHT_GRAY_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.lightGray(), 20));
/* 2411 */     register(context, SHEPHERD_3_ORANGE_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.orange(), 20));
/* 2412 */     register(context, SHEPHERD_3_RED_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.red(), 20));
/* 2413 */     register(context, SHEPHERD_3_PINK_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.pink(), 20));
/*      */   }
/*      */   
/*      */   private static void registerLevelFourDyeTrades(BootstrapContext<VillagerTrade> context) {
/* 2417 */     register(context, SHEPHERD_4_BROWN_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.brown(), 30));
/* 2418 */     register(context, SHEPHERD_4_PURPLE_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.purple(), 30));
/* 2419 */     register(context, SHEPHERD_4_BLUE_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.blue(), 30));
/* 2420 */     register(context, SHEPHERD_4_GREEN_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.green(), 30));
/* 2421 */     register(context, SHEPHERD_4_MAGENTA_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.magenta(), 30));
/* 2422 */     register(context, SHEPHERD_4_CYAN_DYE_EMERALD, createShepherdDyeBuy((Item)Items.DYE.cyan(), 30));
/*      */   }
/*      */   
/*      */   private static VillagerTrade createShepherdDyeBuy(Item item, int xp) {
/* 2426 */     return new VillagerTrade(new TradeCost((ItemLike)item, 12), new ItemStackTemplate(Items.EMERALD), 16, xp, 0.05F, 
/*      */         
/* 2432 */         Optional.empty(), 
/* 2433 */         List.of());
/*      */   }
/*      */   
/*      */   private static void registerWoolPurchases(BootstrapContext<VillagerTrade> context) {
/* 2438 */     ColorCollection.zipApply(SHEPHERD_2_EMERALD_WOOL, Items.WOOL, (name, wool) -> register(context, name, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(wool), 16, 5, 0.05F, Optional.empty(), List.of())));
/*      */   }
/*      */   
/*      */   private static void registerCarpetPurchases(BootstrapContext<VillagerTrade> context) {
/* 2452 */     ColorCollection.zipApply(SHEPHERD_2_EMERALD_CARPETS, Items.CARPET, (name, carpet) -> register(context, name, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 1), new ItemStackTemplate(carpet, 4), 16, 5, 0.05F, Optional.empty(), List.of())));
/*      */   }
/*      */   
/*      */   private static void registerBedTrades(BootstrapContext<VillagerTrade> context) {
/* 2466 */     ColorCollection.zipApply(SHEPHERD_3_EMERALD_BED, Items.BED, (name, bed) -> register(context, name, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(bed), 12, 10, 0.05F, Optional.empty(), List.of())));
/*      */   }
/*      */   
/*      */   private static void registerShepherdBannerTrades(BootstrapContext<VillagerTrade> context) {
/* 2480 */     ColorCollection.zipApply(SHEPHERD_4_EMERALD_BANNER, Items.BANNER, (name, banner) -> register(context, name, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(banner), 12, 15, 0.05F, Optional.empty(), List.of())));
/*      */   }
/*      */   
/*      */   private static void registerCartographerBannerTrades(BootstrapContext<VillagerTrade> context, HolderGetter<VillagerType> villagerVariants) {
/* 2494 */     register(context, CARTOGRAPHER_4_EMERALD_WHITE_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.white(), List.of(VillagerType.SNOW, VillagerType.PLAINS)));
/* 2495 */     register(context, CARTOGRAPHER_4_EMERALD_ORANGE_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.orange(), List.of(VillagerType.SAVANNA, VillagerType.DESERT)));
/* 2496 */     register(context, CARTOGRAPHER_4_EMERALD_MAGENTA_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.magenta(), List.of(VillagerType.SAVANNA)));
/* 2497 */     register(context, CARTOGRAPHER_4_EMERALD_BLUE_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.blue(), List.of(VillagerType.SNOW, VillagerType.TAIGA)));
/* 2498 */     register(context, CARTOGRAPHER_4_EMERALD_LIGHT_BLUE_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.lightBlue(), List.of(VillagerType.SNOW, VillagerType.SWAMP)));
/* 2499 */     register(context, CARTOGRAPHER_4_EMERALD_YELLOW_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.yellow(), List.of(VillagerType.PLAINS, VillagerType.JUNGLE)));
/* 2500 */     register(context, CARTOGRAPHER_4_EMERALD_LIME_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.lime(), List.of(VillagerType.DESERT, VillagerType.TAIGA)));
/* 2501 */     register(context, CARTOGRAPHER_4_EMERALD_PINK_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.pink(), List.of(VillagerType.TAIGA, VillagerType.PLAINS)));
/* 2502 */     register(context, CARTOGRAPHER_4_EMERALD_GRAY_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.gray(), List.of(VillagerType.DESERT)));
/* 2504 */     register(context, CARTOGRAPHER_4_EMERALD_CYAN_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.cyan(), List.of(VillagerType.DESERT, VillagerType.SNOW)));
/* 2505 */     register(context, CARTOGRAPHER_4_EMERALD_PURPLE_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.purple(), List.of(VillagerType.TAIGA, VillagerType.SWAMP)));
/* 2506 */     register(context, CARTOGRAPHER_4_EMERALD_BROWN_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.brown(), List.of(VillagerType.PLAINS, VillagerType.JUNGLE)));
/* 2507 */     register(context, CARTOGRAPHER_4_EMERALD_GREEN_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.green(), List.of(VillagerType.DESERT, VillagerType.SAVANNA, VillagerType.JUNGLE)));
/* 2508 */     register(context, CARTOGRAPHER_4_EMERALD_RED_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.red(), List.of(VillagerType.SNOW, VillagerType.SAVANNA)));
/* 2509 */     register(context, CARTOGRAPHER_4_EMERALD_BLACK_BANNER, createCartographerBannerSell(villagerVariants, (Item)Items.BANNER.black(), List.of(VillagerType.SWAMP)));
/*      */   }
/*      */   
/*      */   private static VillagerTrade createCartographerBannerSell(HolderGetter<VillagerType> villagerVariants, Item item, List<ResourceKey<VillagerType>> villagerTypes) {
/* 2513 */     return new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), new ItemStackTemplate(item), 12, 15, 0.05F, 
/*      */         
/* 2519 */         villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, villagerTypes)), 
/* 2520 */         List.of());
/*      */   }
/*      */   
/*      */   private static void registerBasicExplorerMapTrades(BootstrapContext<VillagerTrade> context, HolderGetter<Item> items, HolderGetter<VillagerType> villagerVariants) {
/* 2525 */     register(context, CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_TAIGA_MAP, createBasicExplorerMapTrades(items, villagerVariants, StructureTags.ON_TAIGA_VILLAGE_MAPS, MapDecorationTypes.TAIGA_VILLAGE, "village_taiga", List.of(VillagerType.SWAMP, VillagerType.SNOW, VillagerType.PLAINS)));
/* 2526 */     register(context, CARTOGRAPHER_2_EMERALD_AND_COMPASS_EXPLORER_SWAMP_MAP, createBasicExplorerMapTrades(items, villagerVariants, StructureTags.ON_SWAMP_EXPLORER_MAPS, MapDecorationTypes.SWAMP_HUT, "explorer_swamp", List.of(VillagerType.TAIGA, VillagerType.SNOW, VillagerType.JUNGLE)));
/* 2527 */     register(context, CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_SNOWY_MAP, createBasicExplorerMapTrades(items, villagerVariants, StructureTags.ON_SNOWY_VILLAGE_MAPS, MapDecorationTypes.SNOWY_VILLAGE, "village_snowy", List.of(VillagerType.TAIGA, VillagerType.SWAMP)));
/* 2528 */     register(context, CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_SAVANNA_MAP, createBasicExplorerMapTrades(items, villagerVariants, StructureTags.ON_SAVANNA_VILLAGE_MAPS, MapDecorationTypes.SAVANNA_VILLAGE, "village_savanna", List.of(VillagerType.PLAINS, VillagerType.JUNGLE, VillagerType.DESERT)));
/* 2529 */     register(context, CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_PLAINS_MAP, createBasicExplorerMapTrades(items, villagerVariants, StructureTags.ON_PLAINS_VILLAGE_MAPS, MapDecorationTypes.PLAINS_VILLAGE, "village_plains", List.of(VillagerType.TAIGA, VillagerType.SNOW, VillagerType.SAVANNA, VillagerType.DESERT)));
/* 2530 */     register(context, CARTOGRAPHER_2_EMERALD_AND_COMPASS_EXPLORER_JUNGLE_MAP, createBasicExplorerMapTrades(items, villagerVariants, StructureTags.ON_JUNGLE_EXPLORER_MAPS, MapDecorationTypes.JUNGLE_TEMPLE, "explorer_jungle", List.of(VillagerType.SWAMP, VillagerType.SAVANNA, VillagerType.DESERT)));
/* 2531 */     register(context, CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_DESERT_MAP, createBasicExplorerMapTrades(items, villagerVariants, StructureTags.ON_DESERT_VILLAGE_MAPS, MapDecorationTypes.DESERT_VILLAGE, "village_desert", List.of(VillagerType.SAVANNA, VillagerType.JUNGLE)));
/*      */   }
/*      */   
/*      */   private static VillagerTrade createBasicExplorerMapTrades(HolderGetter<Item> items, HolderGetter<VillagerType> villagerVariants, TagKey<Structure> structureTagKey, Holder<MapDecorationType> mapDecorationType, String translationKey, List<ResourceKey<VillagerType>> villagerTypes) {
/* 2535 */     return new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 8), 
/*      */         
/* 2537 */         Optional.of(new TradeCost((ItemLike)Items.COMPASS, 1)), new ItemStackTemplate(Items.MAP), 12, 5, 0.2F, 
/*      */         
/* 2542 */         villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, villagerTypes)), 
/* 2543 */         List.of(
/* 2544 */           ExplorationMapFunction.makeExplorationMap()
/* 2545 */           .setDestination(structureTagKey)
/* 2546 */           .setMapDecoration(mapDecorationType)
/* 2547 */           .setSearchRadius(100)
/* 2548 */           .setSkipKnownStructures(true)
/* 2549 */           .build(), 
/*      */           
/* 2551 */           SetNameFunction.setName((Component)Component.translatable("filled_map." + translationKey), SetNameFunction.Target.ITEM_NAME)
/* 2552 */           .build(), 
/* 2553 */           FilteredFunction.filtered((new ItemPredicate.Builder())
/*      */             
/* 2555 */             .of(items, new ItemLike[] { (ItemLike)Items.FILLED_MAP }).withComponents(
/* 2557 */               DataComponentMatchers.Builder.components()
/* 2558 */               .any(DataComponents.MAP_ID)
/* 2559 */               .build())
/*      */             
/* 2561 */             .build())
/* 2562 */           .onFail(Optional.of(DiscardItem.discardItem().build())).build()));
/*      */   }
/*      */   
/*      */   public static Holder.Reference<VillagerTrade> register(BootstrapContext<VillagerTrade> context, ResourceKey<VillagerTrade> resourceKey, VillagerTrade villagerTrade) {
/* 2568 */     return context.register(resourceKey, villagerTrade);
/*      */   }
/*      */   
/*      */   public static ResourceKey<VillagerTrade> resourceKey(String path) {
/* 2572 */     return ResourceKey.create(Registries.VILLAGER_TRADE, Identifier.withDefaultNamespace(path));
/*      */   }
/*      */   
/*      */   public static HolderSet<VillagerType> villagerTypeHolderSet(HolderGetter<VillagerType> villagerVariants, ResourceKey<VillagerType> resourceKey) {
/* 2576 */     return (HolderSet<VillagerType>)HolderSet.direct(new Holder[] { (Holder)villagerVariants.getOrThrow(resourceKey) });
/*      */   }
/*      */   
/*      */   public static HolderSet<VillagerType> villagerTypeHolderSet(HolderGetter<VillagerType> villagerVariants, List<ResourceKey<VillagerType>> resourceKeys) {
/* 2580 */     List<Holder<VillagerType>> villagerTypes = new ArrayList<>();
/* 2581 */     for (ResourceKey<VillagerType> resourceKey : resourceKeys)
/* 2582 */       villagerTypes.add(villagerVariants.getOrThrow(resourceKey)); 
/* 2584 */     return (HolderSet<VillagerType>)HolderSet.direct(villagerTypes);
/*      */   }
/*      */   
/*      */   public static Optional<LootItemCondition> villagerTypeRestriction(HolderSet<VillagerType> villagerTypes) {
/* 2588 */     return (Optional)Optional.of(new LootItemEntityPropertyCondition(Optional.of(
/* 2589 */             EntityPredicate.Builder.entity().components(
/* 2590 */               DataComponentMatchers.Builder.components()
/* 2591 */               .partial(DataComponentPredicates.VILLAGER_VARIANT, 
/*      */                 
/* 2593 */                 (DataComponentPredicate)VillagerTypePredicate.villagerTypes(villagerTypes))
/* 2594 */               .build())
/* 2595 */             .build()), LootContext.EntityTarget.THIS));
/*      */   }
/*      */   
/*      */   private static List<LootItemFunction> dyedItem(HolderGetter<Item> items, Item expectedItem) {
/* 2600 */     return List.of(
/* 2601 */         addRandomDye(), 
/* 2602 */         FilteredFunction.filtered((new ItemPredicate.Builder())
/*      */           
/* 2604 */           .of(items, new ItemLike[] { (ItemLike)expectedItem }).withComponents(
/* 2606 */             DataComponentMatchers.Builder.components()
/* 2607 */             .any(DataComponents.DYED_COLOR)
/* 2608 */             .build())
/*      */           
/* 2610 */           .build())
/* 2611 */         .onFail(Optional.of(DiscardItem.discardItem().build())).build());
/*      */   }
/*      */   
/*      */   private static LootItemFunction addRandomDye() {
/* 2616 */     return 
/* 2617 */       SetRandomDyesFunction.withCount((NumberProvider)Sum.sum(new NumberProvider[] { (NumberProvider)ConstantValue.exactly(1.0F), (NumberProvider)new BinomialDistributionGenerator(
/* 2619 */               (NumberProvider)ConstantValue.exactly(2.0F), (NumberProvider)ConstantValue.exactly(0.75F)) })).build();
/*      */   }
/*      */   
/*      */   public static List<LootItemFunction> enchantedBook(HolderGetter<Item> items, HolderSet<Enchantment> options) {
/* 2625 */     return List.of((new EnchantRandomlyFunction.Builder())
/*      */         
/* 2627 */         .withOptions(options)
/* 2628 */         .allowingIncompatibleEnchantments()
/* 2629 */         .includeAdditionalCostComponent()
/* 2630 */         .build(), 
/* 2631 */         FilteredFunction.filtered((new ItemPredicate.Builder())
/*      */           
/* 2633 */           .of(items, new ItemLike[] { (ItemLike)Items.ENCHANTED_BOOK }).withComponents(
/* 2635 */             DataComponentMatchers.Builder.components()
/* 2636 */             .partial(DataComponentPredicates.STORED_ENCHANTMENTS, 
/*      */               
/* 2638 */               (DataComponentPredicate)EnchantmentsPredicate.storedEnchantments(List.of(new EnchantmentPredicate(Optional.empty(), MinMaxBounds.Ints.ANY))))
/* 2639 */             .build())
/*      */           
/* 2641 */           .build())
/* 2642 */         .onFail(Optional.of(DiscardItem.discardItem().build())).build());
/*      */   }
/*      */   
/*      */   public static List<LootItemFunction> enchantedBook(HolderGetter<Item> items, Holder<Enchantment> enchantment, int level) {
/* 2647 */     return List.of((new SetEnchantmentsFunction.Builder())
/*      */         
/* 2649 */         .withEnchantment(enchantment, (NumberProvider)ConstantValue.exactly(level))
/* 2650 */         .build(), 
/* 2651 */         FilteredFunction.filtered((new ItemPredicate.Builder())
/*      */           
/* 2653 */           .of(items, new ItemLike[] { (ItemLike)Items.ENCHANTED_BOOK }).withComponents(
/* 2655 */             DataComponentMatchers.Builder.components()
/* 2656 */             .partial(DataComponentPredicates.STORED_ENCHANTMENTS, 
/*      */               
/* 2658 */               (DataComponentPredicate)EnchantmentsPredicate.storedEnchantments(List.of(new EnchantmentPredicate(Optional.empty(), MinMaxBounds.Ints.exactly(level)))))
/* 2659 */             .build())
/*      */           
/* 2661 */           .build())
/* 2662 */         .onFail(Optional.of(DiscardItem.discardItem().build())).build());
/*      */   }
/*      */   
/*      */   public static List<LootItemFunction> enchantedItem(HolderGetter<Item> items, HolderSet<Enchantment> options, Item expectedItem) {
/* 2667 */     return List.of((new EnchantWithLevelsFunction.Builder(
/* 2668 */           (NumberProvider)UniformGenerator.between(5.0F, 19.0F)))
/* 2669 */         .withOptions(options)
/* 2670 */         .includeAdditionalCostComponent()
/* 2671 */         .build(), 
/* 2672 */         FilteredFunction.filtered((new ItemPredicate.Builder())
/*      */           
/* 2674 */           .of(items, new ItemLike[] { (ItemLike)expectedItem }).withComponents(
/* 2676 */             DataComponentMatchers.Builder.components()
/* 2677 */             .partial(DataComponentPredicates.ENCHANTMENTS, 
/*      */               
/* 2679 */               (DataComponentPredicate)EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(Optional.empty(), MinMaxBounds.Ints.ANY))))
/* 2680 */             .build())
/*      */           
/* 2682 */           .build())
/* 2683 */         .onFail(Optional.of(DiscardItem.discardItem().build())).build());
/*      */   }
/*      */   
/*      */   public static List<LootItemFunction> enchantedItem(HolderGetter<Item> items, Holder<Enchantment> enchantment, int level, Item expectedItem) {
/* 2688 */     return List.of((new SetEnchantmentsFunction.Builder())
/* 2689 */         .withEnchantment(enchantment, (NumberProvider)ConstantValue.exactly(level)).build(), 
/* 2690 */         FilteredFunction.filtered((new ItemPredicate.Builder())
/*      */           
/* 2692 */           .of(items, new ItemLike[] { (ItemLike)expectedItem }).withComponents(
/* 2694 */             DataComponentMatchers.Builder.components()
/* 2695 */             .partial(DataComponentPredicates.ENCHANTMENTS, 
/*      */               
/* 2697 */               (DataComponentPredicate)EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(Optional.empty(), MinMaxBounds.Ints.exactly(level)))))
/* 2698 */             .build())
/*      */           
/* 2700 */           .build())
/* 2701 */         .onFail(Optional.of(DiscardItem.discardItem().build())).build());
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\trading\VillagerTrades.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */