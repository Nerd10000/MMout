/*     */ package net.minecraft.data.tags;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.VillagerTradeTags;
/*     */ import net.minecraft.world.item.trading.VillagerTrade;
/*     */ import net.minecraft.world.item.trading.VillagerTrades;
/*     */ 
/*     */ public class VillagerTradesTagsProvider extends TagsProvider<VillagerTrade> {
/*     */   public VillagerTradesTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/*  14 */     super(output, Registries.VILLAGER_TRADE, lookupProvider);
/*     */   }
/*     */   
/*     */   protected void addTags(HolderLookup.Provider registries) {
/*  19 */     tag(VillagerTradeTags.FARMER_LEVEL_1)
/*  20 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FARMER_1_WHEAT_EMERALD, VillagerTrades.FARMER_1_POTATO_EMERALD, VillagerTrades.FARMER_1_CARROT_EMERALD, VillagerTrades.FARMER_1_BEETROOT_EMERALD, VillagerTrades.FARMER_1_EMERALD_BREAD });
/*  21 */     tag(VillagerTradeTags.FARMER_LEVEL_2)
/*  22 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FARMER_2_PUMPKIN_EMERALD, VillagerTrades.FARMER_2_EMERALD_PUMPKIN_PIE, VillagerTrades.FARMER_2_EMERALD_APPLE });
/*  23 */     tag(VillagerTradeTags.FARMER_LEVEL_3)
/*  24 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FARMER_3_EMERALD_COOKIE, VillagerTrades.FARMER_3_MELON_EMERALD });
/*  25 */     tag(VillagerTradeTags.FARMER_LEVEL_4)
/*  26 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FARMER_4_EMERALD_CAKE, VillagerTrades.FARMER_4_EMERALD_SUSPICIOUS_STEW });
/*  27 */     tag(VillagerTradeTags.FARMER_LEVEL_5)
/*  28 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FARMER_5_EMERALD_GOLDEN_CARROT, VillagerTrades.FARMER_5_EMERALD_GLISTENING_MELON_SLICE });
/*  29 */     tag(VillagerTradeTags.FISHERMAN_LEVEL_1)
/*  30 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FISHERMAN_1_STRING_EMERALD, VillagerTrades.FISHERMAN_1_COAL_EMERALD, VillagerTrades.FISHERMAN_1_RAW_COD_AND_EMERALD_COOKED_COD, VillagerTrades.FISHERMAN_1_EMERALD_COD_BUCKET });
/*  31 */     tag(VillagerTradeTags.FISHERMAN_LEVEL_2)
/*  32 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FISHERMAN_2_COD_EMERALD, VillagerTrades.FISHERMAN_2_SALMON_AND_EMERALD_COOKED_SALMON, VillagerTrades.FISHERMAN_2_EMERALD_CAMPFIRE });
/*  33 */     tag(VillagerTradeTags.FISHERMAN_LEVEL_3)
/*  34 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FISHERMAN_3_SALMON_EMERALD, VillagerTrades.FISHERMAN_3_EMERALD_ENCHANTED_FISHING_ROD });
/*  35 */     tag(VillagerTradeTags.FISHERMAN_LEVEL_4)
/*  36 */       .add(VillagerTrades.FISHERMAN_4_TROPICAL_FISH_EMERALD);
/*  37 */     tag(VillagerTradeTags.FISHERMAN_LEVEL_5)
/*  38 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FISHERMAN_5_PUFFERFISH_EMERALD, VillagerTrades.FISHERMAN_5_OAK_BOAT_EMERALD, VillagerTrades.FISHERMAN_5_SPRUCE_BOAT_EMERALD, VillagerTrades.FISHERMAN_5_JUNGLE_BOAT_EMERALD, VillagerTrades.FISHERMAN_5_ACACIA_BOAT_EMERALD, VillagerTrades.FISHERMAN_5_DARK_OAK_BOAT_EMERALD });
/*  39 */     tag(VillagerTradeTags.SHEPHERD_LEVEL_1)
/*  40 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.SHEPHERD_1_WHITE_WOOL_EMERALD, VillagerTrades.SHEPHERD_1_BROWN_WOOL_EMERALD, VillagerTrades.SHEPHERD_1_GRAY_WOOL_EMERALD, VillagerTrades.SHEPHERD_1_BLACK_WOOL_EMERALD, VillagerTrades.SHEPHERD_1_EMERALD_SHEARS });
/*  41 */     tag(VillagerTradeTags.SHEPHERD_LEVEL_2)
/*  42 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.SHEPHERD_2_WHITE_DYE_EMERALD, VillagerTrades.SHEPHERD_2_GRAY_DYE_EMERALD, VillagerTrades.SHEPHERD_2_BLACK_DYE_EMERALD, VillagerTrades.SHEPHERD_2_LIGHT_BLUE_DYE_EMERALD, VillagerTrades.SHEPHERD_2_LIME_DYE_EMERALD }).addAll(VillagerTrades.SHEPHERD_2_EMERALD_WOOL.asList()).addAll(VillagerTrades.SHEPHERD_2_EMERALD_CARPETS.asList());
/*  43 */     tag(VillagerTradeTags.SHEPHERD_LEVEL_3)
/*  44 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.SHEPHERD_3_YELLOW_DYE_EMERALD, VillagerTrades.SHEPHERD_3_LIGHT_GRAY_DYE_EMERALD, VillagerTrades.SHEPHERD_3_ORANGE_DYE_EMERALD, VillagerTrades.SHEPHERD_3_RED_DYE_EMERALD, VillagerTrades.SHEPHERD_3_PINK_DYE_EMERALD }).addAll(VillagerTrades.SHEPHERD_3_EMERALD_BED.asList());
/*  45 */     tag(VillagerTradeTags.SHEPHERD_LEVEL_4)
/*  46 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.SHEPHERD_4_BROWN_DYE_EMERALD, VillagerTrades.SHEPHERD_4_PURPLE_DYE_EMERALD, VillagerTrades.SHEPHERD_4_BLUE_DYE_EMERALD, VillagerTrades.SHEPHERD_4_GREEN_DYE_EMERALD, VillagerTrades.SHEPHERD_4_MAGENTA_DYE_EMERALD, VillagerTrades.SHEPHERD_4_CYAN_DYE_EMERALD }).addAll(VillagerTrades.SHEPHERD_4_EMERALD_BANNER.asList());
/*  47 */     tag(VillagerTradeTags.SHEPHERD_LEVEL_5)
/*  48 */       .add(VillagerTrades.SHEPHERD_5_EMERALD_PAINTING);
/*  49 */     tag(VillagerTradeTags.FLETCHER_LEVEL_1)
/*  50 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FLETCHER_1_STICK_EMERALD, VillagerTrades.FLETCHER_1_EMERALD_ARROW, VillagerTrades.FLETCHER_1_GRAVEL_AND_EMERALD_FLINT });
/*  51 */     tag(VillagerTradeTags.FLETCHER_LEVEL_2)
/*  52 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FLETCHER_2_FLINT_EMERALD, VillagerTrades.FLETCHER_2_EMERALD_BOW });
/*  53 */     tag(VillagerTradeTags.FLETCHER_LEVEL_3)
/*  54 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FLETCHER_3_STRING_EMERALD, VillagerTrades.FLETCHER_3_EMERALD_CROSSBOW });
/*  55 */     tag(VillagerTradeTags.FLETCHER_LEVEL_4)
/*  56 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FLETCHER_4_FEATHER_EMERALD, VillagerTrades.FLETCHER_4_EMERALD_ENCHANTED_BOW });
/*  57 */     tag(VillagerTradeTags.FLETCHER_LEVEL_5)
/*  58 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.FLETCHER_5_TRIPWIRE_HOOK_EMERALD, VillagerTrades.FLETCHER_5_EMERALD_ENCHANTED_CROSSBOW, VillagerTrades.FLETCHER_5_ARROW_AND_EMERALD_TIPPED_ARROW });
/*  59 */     tag(VillagerTradeTags.LIBRARIAN_LEVEL_1)
/*  60 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.LIBRARIAN_1_PAPER_EMERALD, VillagerTrades.LIBRARIAN_1_EMERALD_AND_BOOK_ENCHANTED_BOOK, VillagerTrades.LIBRARIAN_1_EMERALD_BOOKSHELF });
/*  61 */     tag(VillagerTradeTags.LIBRARIAN_LEVEL_2)
/*  62 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.LIBRARIAN_2_BOOK_EMERALD, VillagerTrades.LIBRARIAN_2_EMERALD_AND_BOOK_ENCHANTED_BOOK, VillagerTrades.LIBRARIAN_2_EMERALD_LANTERN });
/*  63 */     tag(VillagerTradeTags.LIBRARIAN_LEVEL_3)
/*  64 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.LIBRARIAN_3_INK_SAC_EMERALD, VillagerTrades.LIBRARIAN_3_EMERALD_AND_BOOK_ENCHANTED_BOOK, VillagerTrades.LIBRARIAN_3_EMERALD_GLASS });
/*  65 */     tag(VillagerTradeTags.LIBRARIAN_LEVEL_4)
/*  66 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.LIBRARIAN_4_WRITABLE_BOOK_EMERALD, VillagerTrades.LIBRARIAN_4_EMERALD_AND_BOOK_ENCHANTED_BOOK, VillagerTrades.LIBRARIAN_4_EMERALD_CLOCK, VillagerTrades.LIBRARIAN_4_EMERALD_COMPASS });
/*  67 */     tag(VillagerTradeTags.LIBRARIAN_LEVEL_5)
/*  68 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.LIBRARIAN_5_EMERALD_YELLOW_CANDLE, VillagerTrades.LIBRARIAN_5_EMERALD_RED_CANDLE });
/*  69 */     tag(VillagerTradeTags.CARTOGRAPHER_LEVEL_1)
/*  70 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.CARTOGRAPHER_1_PAPER_EMERALD, VillagerTrades.CARTOGRAPHER_1_EMERALD_MAP });
/*  71 */     tag(VillagerTradeTags.CARTOGRAPHER_LEVEL_2)
/*  72 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.CARTOGRAPHER_2_GLASS_PANE_EMERALD, VillagerTrades.CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_TAIGA_MAP, VillagerTrades.CARTOGRAPHER_2_EMERALD_AND_COMPASS_EXPLORER_SWAMP_MAP, VillagerTrades.CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_SNOWY_MAP, VillagerTrades.CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_SAVANNA_MAP, VillagerTrades.CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_PLAINS_MAP, VillagerTrades.CARTOGRAPHER_2_EMERALD_AND_COMPASS_EXPLORER_JUNGLE_MAP, VillagerTrades.CARTOGRAPHER_2_EMERALD_AND_COMPASS_VILLAGE_DESERT_MAP });
/*  73 */     tag(VillagerTradeTags.CARTOGRAPHER_LEVEL_3)
/*  74 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.CARTOGRAPHER_3_COMPASS_EMERALD, VillagerTrades.CARTOGRAPHER_3_EMERALD_AND_COMPASS_OCEAN_EXPLORER_MAP, VillagerTrades.CARTOGRAPHER_3_EMERALD_AND_COMPASS_TRIAL_CHAMBER_MAP });
/*  75 */     tag(VillagerTradeTags.CARTOGRAPHER_LEVEL_4)
/*  76 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { 
/*     */           VillagerTrades.CARTOGRAPHER_4_EMERALD_ITEM_FRAME, VillagerTrades.CARTOGRAPHER_4_EMERALD_WHITE_BANNER, VillagerTrades.CARTOGRAPHER_4_EMERALD_ORANGE_BANNER, VillagerTrades.CARTOGRAPHER_4_EMERALD_MAGENTA_BANNER, VillagerTrades.CARTOGRAPHER_4_EMERALD_BLUE_BANNER, VillagerTrades.CARTOGRAPHER_4_EMERALD_LIGHT_BLUE_BANNER, VillagerTrades.CARTOGRAPHER_4_EMERALD_YELLOW_BANNER, VillagerTrades.CARTOGRAPHER_4_EMERALD_LIME_BANNER, VillagerTrades.CARTOGRAPHER_4_EMERALD_PINK_BANNER, VillagerTrades.CARTOGRAPHER_4_EMERALD_GRAY_BANNER, 
/*     */           VillagerTrades.CARTOGRAPHER_4_EMERALD_CYAN_BANNER, VillagerTrades.CARTOGRAPHER_4_EMERALD_PURPLE_BANNER, VillagerTrades.CARTOGRAPHER_4_EMERALD_BROWN_BANNER, VillagerTrades.CARTOGRAPHER_4_EMERALD_GREEN_BANNER, VillagerTrades.CARTOGRAPHER_4_EMERALD_RED_BANNER, VillagerTrades.CARTOGRAPHER_4_EMERALD_BLACK_BANNER });
/*  77 */     tag(VillagerTradeTags.CARTOGRAPHER_LEVEL_5)
/*  78 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.CARTOGRAPHER_5_EMERALD_GLOBE_BANNER_PATTERN, VillagerTrades.CARTOGRAPHER_5_EMERALD_AND_COMPASS_WOODLAND_MANSION_MAP });
/*  79 */     tag(VillagerTradeTags.CLERIC_LEVEL_1)
/*  80 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.CLERIC_1_ROTTEN_FLESH_EMERALD, VillagerTrades.CLERIC_1_EMERALD_REDSTONE });
/*  81 */     tag(VillagerTradeTags.CLERIC_LEVEL_2)
/*  82 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.CLERIC_2_GOLD_INGOT_EMERALD, VillagerTrades.CLERIC_2_EMERALD_LAPIS_LAZULI });
/*  83 */     tag(VillagerTradeTags.CLERIC_LEVEL_3)
/*  84 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.CLERIC_3_RABBIT_FOOT_EMERALD, VillagerTrades.CLERIC_3_EMERALD_GLOWSTONE });
/*  85 */     tag(VillagerTradeTags.CLERIC_LEVEL_4)
/*  86 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.CLERIC_4_TURTLE_SCUTE_EMERALD, VillagerTrades.CLERIC_4_GLASS_BOTTLE_EMERALD, VillagerTrades.CLERIC_4_EMERALD_ENDER_PEARL });
/*  87 */     tag(VillagerTradeTags.CLERIC_LEVEL_5)
/*  88 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.CLERIC_5_NETHER_WART_EMERALD, VillagerTrades.CLERIC_5_EMERALD_EXPERIENCE_BOTTLE });
/*  89 */     tag(VillagerTradeTags.COMMON_SMITH_LEVEL_1)
/*  90 */       .add(VillagerTrades.COMMON_SMITH_1_COAL_EMERALD);
/*  91 */     tag(VillagerTradeTags.COMMON_SMITH_LEVEL_2)
/*  92 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.COMMON_SMITH_2_IRON_INGOT_EMERALD, VillagerTrades.COMMON_SMITH_2_EMERALD_BELL });
/*  93 */     tag(VillagerTradeTags.COMMON_SMITH_LEVEL_3);
/*  94 */     tag(VillagerTradeTags.COMMON_SMITH_LEVEL_4);
/*  95 */     tag(VillagerTradeTags.COMMON_SMITH_LEVEL_5);
/*  96 */     tag(VillagerTradeTags.ARMORER_LEVEL_1)
/*  97 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_1)
/*  98 */       .add(new ResourceKey[] { VillagerTrades.ARMORER_1_EMERALD_IRON_LEGGINGS, VillagerTrades.ARMORER_1_EMERALD_IRON_BOOTS, VillagerTrades.ARMORER_1_EMERALD_IRON_HELMET, VillagerTrades.ARMORER_1_EMERALD_IRON_CHESTPLATE });
/*  99 */     tag(VillagerTradeTags.ARMORER_LEVEL_2)
/* 100 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_2)
/* 101 */       .add(new ResourceKey[] { VillagerTrades.ARMORER_2_EMERALD_CHAINMAIL_BOOTS, VillagerTrades.ARMORER_2_EMERALD_CHAINMAIL_LEGGINGS });
/* 102 */     tag(VillagerTradeTags.ARMORER_LEVEL_3)
/* 103 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_3)
/* 104 */       .add(new ResourceKey[] { VillagerTrades.ARMORER_3_LAVA_BUCKET_EMERALD, VillagerTrades.ARMORER_3_EMERALD_CHAINMAIL_HELMET, VillagerTrades.ARMORER_3_EMERALD_CHAINMAIL_CHESTPLATE, VillagerTrades.ARMORER_3_EMERALD_SHIELD, VillagerTrades.ARMORER_3_DIAMOND_EMERALD });
/* 105 */     tag(VillagerTradeTags.ARMORER_LEVEL_4)
/* 106 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_4)
/* 107 */       .add(new ResourceKey[] { VillagerTrades.ARMORER_4_EMERALD_ENCHANTED_DIAMOND_LEGGINGS, VillagerTrades.ARMORER_4_EMERALD_ENCHANTED_DIAMOND_BOOTS });
/* 108 */     tag(VillagerTradeTags.ARMORER_LEVEL_5)
/* 109 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_5)
/* 110 */       .add(new ResourceKey[] { VillagerTrades.ARMORER_5_EMERALD_ENCHANTED_DIAMOND_HELMET, VillagerTrades.ARMORER_5_EMERALD_ENCHANTED_DIAMOND_CHESTPLATE });
/* 111 */     tag(VillagerTradeTags.WEAPONSMITH_LEVEL_1)
/* 112 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_1)
/* 113 */       .add(new ResourceKey[] { VillagerTrades.WEAPONSMITH_1_EMERALD_IRON_AXE, VillagerTrades.WEAPONSMITH_1_EMERALD_ENCHANTED_IRON_SWORD });
/* 114 */     tag(VillagerTradeTags.WEAPONSMITH_LEVEL_2)
/* 115 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_2);
/* 116 */     tag(VillagerTradeTags.WEAPONSMITH_LEVEL_3)
/* 117 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_3)
/* 118 */       .add(VillagerTrades.WEAPONSMITH_3_FLINT_EMERALD);
/* 119 */     tag(VillagerTradeTags.WEAPONSMITH_LEVEL_4)
/* 120 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_4)
/* 121 */       .add(new ResourceKey[] { VillagerTrades.WEAPONSMITH_4_EMERALD_ENCHANTED_DIAMOND_AXE, VillagerTrades.WEAPONSMITH_4_DIAMOND_EMERALD });
/* 122 */     tag(VillagerTradeTags.WEAPONSMITH_LEVEL_5)
/* 123 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_5)
/* 124 */       .add(VillagerTrades.WEAPONSMITH_5_EMERALD_ENCHANTED_DIAMOND_SWORD);
/* 125 */     tag(VillagerTradeTags.TOOLSMITH_LEVEL_1)
/* 126 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_1)
/* 127 */       .add(new ResourceKey[] { VillagerTrades.TOOLSMITH_1_EMERALD_STONE_AXE, VillagerTrades.TOOLSMITH_1_EMERALD_STONE_SHOVEL, VillagerTrades.TOOLSMITH_1_EMERALD_STONE_PICKAXE, VillagerTrades.TOOLSMITH_1_EMERALD_STONE_HOE });
/* 128 */     tag(VillagerTradeTags.TOOLSMITH_LEVEL_2)
/* 129 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_2);
/* 130 */     tag(VillagerTradeTags.TOOLSMITH_LEVEL_3)
/* 131 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_3)
/* 132 */       .add(new ResourceKey[] { VillagerTrades.TOOLSMITH_3_FLINT_EMERALD, VillagerTrades.TOOLSMITH_3_EMERALD_IRON_AXE, VillagerTrades.TOOLSMITH_3_EMERALD_IRON_SHOVEL, VillagerTrades.TOOLSMITH_3_EMERALD_IRON_PICKAXE, VillagerTrades.TOOLSMITH_3_EMERALD_DIAMOND_HOE });
/* 133 */     tag(VillagerTradeTags.TOOLSMITH_LEVEL_4)
/* 134 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_4)
/* 135 */       .add(new ResourceKey[] { VillagerTrades.TOOLSMITH_4_EMERALD_DIAMOND_AXE, VillagerTrades.TOOLSMITH_4_EMERALD_DIAMOND_SHOVEL, VillagerTrades.TOOLSMITH_4_DIAMOND_EMERALD });
/* 136 */     tag(VillagerTradeTags.TOOLSMITH_LEVEL_5)
/* 137 */       .addTag(VillagerTradeTags.COMMON_SMITH_LEVEL_5)
/* 138 */       .add(VillagerTrades.TOOLSMITH_5_EMERALD_DIAMOND_PICKAXE);
/* 139 */     tag(VillagerTradeTags.BUTCHER_LEVEL_1)
/* 140 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.BUTCHER_1_CHICKEN_EMERALD, VillagerTrades.BUTCHER_1_PORKCHOP_EMERALD, VillagerTrades.BUTCHER_1_RABBIT_EMERALD, VillagerTrades.BUTCHER_1_EMERALD_RABBIT_STEW });
/* 141 */     tag(VillagerTradeTags.BUTCHER_LEVEL_2)
/* 142 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.BUTCHER_2_COAL_EMERALD, VillagerTrades.BUTCHER_2_EMERALD_COOKED_PORKCHOP, VillagerTrades.BUTCHER_2_EMERALD_COOKED_CHICKEN });
/* 143 */     tag(VillagerTradeTags.BUTCHER_LEVEL_3)
/* 144 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.BUTCHER_3_MUTTON_EMERALD, VillagerTrades.BUTCHER_3_BEEF_EMERALD });
/* 145 */     tag(VillagerTradeTags.BUTCHER_LEVEL_4)
/* 146 */       .add(VillagerTrades.BUTCHER_4_DRIED_KELP_BLOCK_EMERALD);
/* 147 */     tag(VillagerTradeTags.BUTCHER_LEVEL_5)
/* 148 */       .add(VillagerTrades.BUTCHER_5_SWEET_BERRIES_EMERALD);
/* 149 */     tag(VillagerTradeTags.LEATHERWORKER_LEVEL_1)
/* 150 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.LEATHERWORKER_1_LEATHER_EMERALD, VillagerTrades.LEATHERWORKER_1_EMERALD_DYED_LEATHER_LEGGINGS, VillagerTrades.LEATHERWORKER_1_EMERALD_DYED_LEATHER_CHESTPLATE });
/* 151 */     tag(VillagerTradeTags.LEATHERWORKER_LEVEL_2)
/* 152 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.LEATHERWORKER_2_FLINT_EMERALD, VillagerTrades.LEATHERWORKER_2_EMERALD_DYED_LEATHER_HELMET, VillagerTrades.LEATHERWORKER_2_EMERALD_DYED_LEATHER_BOOTS });
/* 153 */     tag(VillagerTradeTags.LEATHERWORKER_LEVEL_3)
/* 154 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.LEATHERWORKER_3_RABBIT_HIDE_EMERALD, VillagerTrades.LEATHERWORKER_3_EMERALD_DYED_LEATHER_CHESTPLATE });
/* 155 */     tag(VillagerTradeTags.LEATHERWORKER_LEVEL_4)
/* 156 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.LEATHERWORKER_4_TURTLE_SCUTE_EMERALD, VillagerTrades.LEATHERWORKER_4_EMERALD_DYED_LEATHER_HORSE_ARMOR });
/* 157 */     tag(VillagerTradeTags.LEATHERWORKER_LEVEL_5)
/* 158 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.LEATHERWORKER_5_EMERALD_SADDLE, VillagerTrades.LEATHERWORKER_5_EMERALD_DYED_LEATHER_HELMET });
/* 159 */     tag(VillagerTradeTags.MASON_LEVEL_1)
/* 160 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.MASON_1_CLAY_BALL_EMERALD, VillagerTrades.MASON_1_EMERALD_BRICK });
/* 161 */     tag(VillagerTradeTags.MASON_LEVEL_2)
/* 162 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.MASON_2_STONE_EMERALD, VillagerTrades.MASON_2_EMERALD_CHISELED_STONE_BRICKS });
/* 163 */     tag(VillagerTradeTags.MASON_LEVEL_3)
/* 164 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.MASON_3_GRANITE_EMERALD, VillagerTrades.MASON_3_ANDESITE_EMERALD, VillagerTrades.MASON_3_DIORITE_EMERALD, VillagerTrades.MASON_3_EMERALD_DRIPSTONE_BLOCK, VillagerTrades.MASON_3_EMERALD_POLISHED_ANDESITE, VillagerTrades.MASON_3_EMERALD_POLISHED_DIORITE, VillagerTrades.MASON_3_EMERALD_POLISHED_GRANTITE });
/* 165 */     tag(VillagerTradeTags.MASON_LEVEL_4)
/* 166 */       .add(VillagerTrades.MASON_4_QUARTZ_EMERALD)
/* 167 */       .addAll(VillagerTrades.MASON_4_EMERALD_TERRACOTTA.asList())
/* 168 */       .addAll(VillagerTrades.MASON_4_EMERALD_GLAZED_TERRACOTTA.asList());
/* 169 */     tag(VillagerTradeTags.MASON_LEVEL_5)
/* 170 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.MASON_5_EMERALD_QUARTZ_PILLAR, VillagerTrades.MASON_5_EMERALD_QUARTZ_BLOCK });
/* 171 */     tag(VillagerTradeTags.WANDERING_TRADER_BUYING)
/* 172 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { VillagerTrades.WANDERING_TRADER_WATER_BOTTLE_EMERALD, VillagerTrades.WANDERING_TRADER_WATER_BUCKET_EMERALD, VillagerTrades.WANDERING_TRADER_MILK_BUCKET_EMERALD, VillagerTrades.WANDERING_TRADER_FERMENTED_SPIDER_EYE_EMERALD, VillagerTrades.WANDERING_TRADER_BAKED_POTATO_EMERALD, VillagerTrades.WANDERING_TRADER_HAY_BLOCK_EMERALD });
/* 175 */     tag(VillagerTradeTags.WANDERING_TRADER_UNCOMMON)
/* 176 */       .add((ResourceKey<VillagerTrade>[])new ResourceKey[] { 
/*     */           VillagerTrades.WANDERING_TRADER_EMERALD_PACKED_ICE, VillagerTrades.WANDERING_TRADER_EMERALD_BLUE_ICE, VillagerTrades.WANDERING_TRADER_EMERALD_GUNPOWDER, VillagerTrades.WANDERING_TRADER_EMERALD_PODZOL, VillagerTrades.WANDERING_TRADER_EMERALD_ACACIA_LOG, VillagerTrades.WANDERING_TRADER_EMERALD_BIRCH_LOG, VillagerTrades.WANDERING_TRADER_EMERALD_DARK_OAK_LOG, VillagerTrades.WANDERING_TRADER_EMERALD_JUNGLE_LOG, VillagerTrades.WANDERING_TRADER_EMERALD_OAK_LOG, VillagerTrades.WANDERING_TRADER_EMERALD_SPRUCE_LOG, 
/*     */           VillagerTrades.WANDERING_TRADER_EMERALD_CHERRY_LOG, VillagerTrades.WANDERING_TRADER_EMERALD_MANGROVE_LOG, VillagerTrades.WANDERING_TRADER_EMERALD_PALE_OAK_LOG, VillagerTrades.WANDERING_TRADER_EMERALD_ENCHANTED_IRON_PICKAXE, VillagerTrades.WANDERING_TRADER_EMERALD_LONG_INVISIBILITY_POTION });
/* 185 */     tag(VillagerTradeTags.WANDERING_TRADER_COMMON)
/* 186 */       .addAll(VillagerTrades.WANDERING_TRADER_EMERALD_DYE.asList())
/* 187 */       .add(new ResourceKey[] { 
/*     */           VillagerTrades.WANDERING_TRADER_EMERALD_TROPICAL_FISH_BUCKET, VillagerTrades.WANDERING_TRADER_EMERALD_PUFFERFISH_BUCKET, VillagerTrades.WANDERING_TRADER_EMERALD_SEA_PICKLE, VillagerTrades.WANDERING_TRADER_EMERALD_SLIME_BALL, VillagerTrades.WANDERING_TRADER_EMERALD_GLOWSTONE, VillagerTrades.WANDERING_TRADER_EMERALD_NAUTILUS_SHELL, VillagerTrades.WANDERING_TRADER_EMERALD_FERN, VillagerTrades.WANDERING_TRADER_EMERALD_SUGAR_CANE, VillagerTrades.WANDERING_TRADER_EMERALD_PUMPKIN, VillagerTrades.WANDERING_TRADER_EMERALD_KELP, 
/*     */           VillagerTrades.WANDERING_TRADER_EMERALD_CACTUS, VillagerTrades.WANDERING_TRADER_EMERALD_DANDELION, VillagerTrades.WANDERING_TRADER_EMERALD_POPPY, VillagerTrades.WANDERING_TRADER_EMERALD_BLUE_ORCHID, VillagerTrades.WANDERING_TRADER_EMERALD_ALLIUM, VillagerTrades.WANDERING_TRADER_EMERALD_AZURE_BLUET, VillagerTrades.WANDERING_TRADER_EMERALD_RED_TULIP, VillagerTrades.WANDERING_TRADER_EMERALD_ORANGE_TULIP, VillagerTrades.WANDERING_TRADER_EMERALD_WHITE_TULIP, VillagerTrades.WANDERING_TRADER_EMERALD_PINK_TULIP, 
/*     */           VillagerTrades.WANDERING_TRADER_EMERALD_OXEYE_DAISY, VillagerTrades.WANDERING_TRADER_EMERALD_CORNFLOWER, VillagerTrades.WANDERING_TRADER_EMERALD_LILY_OF_THE_VALLEY, VillagerTrades.WANDERING_TRADER_EMERALD_OPEN_EYEBLOSSOM, VillagerTrades.WANDERING_TRADER_EMERALD_WHEAT_SEEDS, VillagerTrades.WANDERING_TRADER_EMERALD_BEETROOT_SEEDS, VillagerTrades.WANDERING_TRADER_EMERALD_PUMPKIN_SEEDS, VillagerTrades.WANDERING_TRADER_EMERALD_MELON_SEEDS, VillagerTrades.WANDERING_TRADER_EMERALD_ACACIA_SAPLING, VillagerTrades.WANDERING_TRADER_EMERALD_BIRCH_SAPLING, 
/*     */           VillagerTrades.WANDERING_TRADER_EMERALD_DARK_OAK_SAPLING, VillagerTrades.WANDERING_TRADER_EMERALD_JUNGLE_SAPLING, VillagerTrades.WANDERING_TRADER_EMERALD_OAK_SAPLING, VillagerTrades.WANDERING_TRADER_EMERALD_SPRUCE_SAPLING, VillagerTrades.WANDERING_TRADER_EMERALD_CHERRY_SAPLING, VillagerTrades.WANDERING_TRADER_EMERALD_PALE_OAK_SAPLING, VillagerTrades.WANDERING_TRADER_EMERALD_MANGROVE_PROPAGULE, VillagerTrades.WANDERING_TRADER_EMERALD_BRAIN_CORAL_BLOCK, VillagerTrades.WANDERING_TRADER_EMERALD_BUBBLE_CORAL_BLOCK, VillagerTrades.WANDERING_TRADER_EMERALD_FIRE_CORAL_BLOCK, 
/*     */           VillagerTrades.WANDERING_TRADER_EMERALD_HORN_CORAL_BLOCK, VillagerTrades.WANDERING_TRADER_EMERALD_TUBE_CORAL_BLOCK, VillagerTrades.WANDERING_TRADER_EMERALD_VINE, VillagerTrades.WANDERING_TRADER_EMERALD_PALE_HANGING_MOSS, VillagerTrades.WANDERING_TRADER_EMERALD_BROWN_MUSHROOM, VillagerTrades.WANDERING_TRADER_EMERALD_RED_MUSHROOM, VillagerTrades.WANDERING_TRADER_EMERALD_LILY_PAD, VillagerTrades.WANDERING_TRADER_EMERALD_SMALL_DRIPLEAF, VillagerTrades.WANDERING_TRADER_EMERALD_SAND, VillagerTrades.WANDERING_TRADER_EMERALD_RED_SAND, 
/*     */           VillagerTrades.WANDERING_TRADER_EMERALD_POINTED_DRIPSTONE, VillagerTrades.WANDERING_TRADER_EMERALD_SULFUR_SPIKE, VillagerTrades.WANDERING_TRADER_EMERALD_ROOTED_DIRT, VillagerTrades.WANDERING_TRADER_EMERALD_MOSS_BLOCK, VillagerTrades.WANDERING_TRADER_EMERALD_PALE_MOSS_BLOCK, VillagerTrades.WANDERING_TRADER_EMERALD_WILDFLOWERS, VillagerTrades.WANDERING_TRADER_EMERALD_DRY_TALL_GRASS, VillagerTrades.WANDERING_TRADER_EMERALD_FIREFLY_BUSH, VillagerTrades.WANDERING_TRADER_EMERALD_GOLDEN_DANDELION, VillagerTrades.WANDERING_TRADER_EMERALD_NAME_TAG });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\VillagerTradesTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */