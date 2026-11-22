/*     */ package net.minecraft.world.item.trading;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.Sum;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public class TradeRebalanceVillagerTrades extends VillagerTrades {
/*  25 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_1_EMERALD_AND_BOOK_DESERT_ENCHANTED_BOOK = resourceKey("librarian/1/emerald_and_book_desert_enchanted_book");
/*     */   
/*  26 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_1_EMERALD_AND_BOOK_JUNGLE_ENCHANTED_BOOK = resourceKey("librarian/1/emerald_and_book_jungle_enchanted_book");
/*     */   
/*  27 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_1_EMERALD_AND_BOOK_PLAINS_ENCHANTED_BOOK = resourceKey("librarian/1/emerald_and_book_plains_enchanted_book");
/*     */   
/*  28 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_1_EMERALD_AND_BOOK_SAVANNA_ENCHANTED_BOOK = resourceKey("librarian/1/emerald_and_book_savanna_enchanted_book");
/*     */   
/*  29 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_1_EMERALD_AND_BOOK_SNOW_ENCHANTED_BOOK = resourceKey("librarian/1/emerald_and_book_snow_enchanted_book");
/*     */   
/*  30 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_1_EMERALD_AND_BOOK_SWAMP_ENCHANTED_BOOK = resourceKey("librarian/1/emerald_and_book_swamp_enchanted_book");
/*     */   
/*  31 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_1_EMERALD_AND_BOOK_TAIGA_ENCHANTED_BOOK = resourceKey("librarian/1/emerald_and_book_taiga_enchanted_book");
/*     */   
/*  33 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_2_EMERALD_AND_BOOK_DESERT_ENCHANTED_BOOK = resourceKey("librarian/2/emerald_and_book_desert_enchanted_book");
/*     */   
/*  34 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_2_EMERALD_AND_BOOK_JUNGLE_ENCHANTED_BOOK = resourceKey("librarian/2/emerald_and_book_jungle_enchanted_book");
/*     */   
/*  35 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_2_EMERALD_AND_BOOK_PLAINS_ENCHANTED_BOOK = resourceKey("librarian/2/emerald_and_book_plains_enchanted_book");
/*     */   
/*  36 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_2_EMERALD_AND_BOOK_SAVANNA_ENCHANTED_BOOK = resourceKey("librarian/2/emerald_and_book_savanna_enchanted_book");
/*     */   
/*  37 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_2_EMERALD_AND_BOOK_SNOW_ENCHANTED_BOOK = resourceKey("librarian/2/emerald_and_book_snow_enchanted_book");
/*     */   
/*  38 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_2_EMERALD_AND_BOOK_SWAMP_ENCHANTED_BOOK = resourceKey("librarian/2/emerald_and_book_swamp_enchanted_book");
/*     */   
/*  39 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_2_EMERALD_AND_BOOK_TAIGA_ENCHANTED_BOOK = resourceKey("librarian/2/emerald_and_book_taiga_enchanted_book");
/*     */   
/*  41 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_3_EMERALD_AND_BOOK_DESERT_ENCHANTED_BOOK = resourceKey("librarian/3/emerald_and_book_desert_enchanted_book");
/*     */   
/*  42 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_3_EMERALD_AND_BOOK_JUNGLE_ENCHANTED_BOOK = resourceKey("librarian/3/emerald_and_book_jungle_enchanted_book");
/*     */   
/*  43 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_3_EMERALD_AND_BOOK_PLAINS_ENCHANTED_BOOK = resourceKey("librarian/3/emerald_and_book_plains_enchanted_book");
/*     */   
/*  44 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_3_EMERALD_AND_BOOK_SAVANNA_ENCHANTED_BOOK = resourceKey("librarian/3/emerald_and_book_savanna_enchanted_book");
/*     */   
/*  45 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_3_EMERALD_AND_BOOK_SNOW_ENCHANTED_BOOK = resourceKey("librarian/3/emerald_and_book_snow_enchanted_book");
/*     */   
/*  46 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_3_EMERALD_AND_BOOK_SWAMP_ENCHANTED_BOOK = resourceKey("librarian/3/emerald_and_book_swamp_enchanted_book");
/*     */   
/*  47 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_3_EMERALD_AND_BOOK_TAIGA_ENCHANTED_BOOK = resourceKey("librarian/3/emerald_and_book_taiga_enchanted_book");
/*     */   
/*  49 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_5_EMERALD_AND_BOOK_DESERT_ENCHANTED_BOOK = resourceKey("librarian/5/emerald_and_book_desert_enchanted_book");
/*     */   
/*  50 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_5_EMERALD_AND_BOOK_JUNGLE_ENCHANTED_BOOK = resourceKey("librarian/5/emerald_and_book_jungle_enchanted_book");
/*     */   
/*  51 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_5_EMERALD_AND_BOOK_PLAINS_ENCHANTED_BOOK = resourceKey("librarian/5/emerald_and_book_plains_enchanted_book");
/*     */   
/*  52 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_5_EMERALD_AND_BOOK_SAVANNA_ENCHANTED_BOOK = resourceKey("librarian/5/emerald_and_book_savanna_enchanted_book");
/*     */   
/*  53 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_5_EMERALD_AND_BOOK_SNOW_ENCHANTED_BOOK = resourceKey("librarian/5/emerald_and_book_snow_enchanted_book");
/*     */   
/*  54 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_5_EMERALD_AND_BOOK_SWAMP_ENCHANTED_BOOK = resourceKey("librarian/5/emerald_and_book_swamp_enchanted_book");
/*     */   
/*  55 */   public static final ResourceKey<VillagerTrade> LIBRARIAN_5_EMERALD_AND_BOOK_TAIGA_ENCHANTED_BOOK = resourceKey("librarian/5/emerald_and_book_taiga_enchanted_book");
/*     */   
/*  57 */   public static final ResourceKey<VillagerTrade> ARMORER_1_IRON_INGOT_EMERALD = resourceKey("armorer/1/iron_ingot_emerald");
/*     */   
/*  59 */   public static final ResourceKey<VillagerTrade> ARMORER_2_EMERALD_IRON_BOOTS_GROUP_1 = resourceKey("armorer/2/emerald_iron_boots_group_1");
/*     */   
/*  60 */   public static final ResourceKey<VillagerTrade> ARMORER_2_EMERALD_IRON_HELMET_GROUP_1 = resourceKey("armorer/2/emerald_iron_helmet_group_1");
/*     */   
/*  61 */   public static final ResourceKey<VillagerTrade> ARMORER_2_EMERALD_IRON_LEGGINGS_GROUP_1 = resourceKey("armorer/2/emerald_iron_leggings_group_1");
/*     */   
/*  62 */   public static final ResourceKey<VillagerTrade> ARMORER_2_EMERALD_IRON_CHESTPLATE_GROUP_1 = resourceKey("armorer/2/emerald_iron_chestplate_group_1");
/*     */   
/*  63 */   public static final ResourceKey<VillagerTrade> ARMORER_2_EMERALD_CHAINMAIL_BOOTS_GROUP_2 = resourceKey("armorer/2/emerald_chainmail_boots_group_2");
/*     */   
/*  64 */   public static final ResourceKey<VillagerTrade> ARMORER_2_EMERALD_CHAINMAIL_HELMET_GROUP_2 = resourceKey("armorer/2/emerald_chainmail_helmet_group_2");
/*     */   
/*  65 */   public static final ResourceKey<VillagerTrade> ARMORER_2_EMERALD_CHAINMAIL_LEGGINGS_GROUP_2 = resourceKey("armorer/2/emerald_chainmail_leggings_group_2");
/*     */   
/*  66 */   public static final ResourceKey<VillagerTrade> ARMORER_2_EMERALD_CHAINMAIL_CHESTPLATE_GROUP_2 = resourceKey("armorer/2/emerald_chainmail_chainmail_group_2");
/*     */   
/*  68 */   public static final ResourceKey<VillagerTrade> ARMORER_3_EMERALD_BELL = resourceKey("armorer/3/emerald_bell");
/*     */   
/*  70 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_BOOTS_DESERT = resourceKey("armorer/4/emerald_enchanted_iron_boots_desert");
/*     */   
/*  71 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_HELMET_DESERT = resourceKey("armorer/4/emerald_enchanted_iron_helmet_desert");
/*     */   
/*  72 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_LEGGINGS_DESERT = resourceKey("armorer/4/emerald_enchanted_iron_leggings_desert");
/*     */   
/*  73 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_CHESTPLATE_DESERT = resourceKey("armorer/4/emerald_enchanted_iron_chestplate_desert");
/*     */   
/*  75 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_BOOTS_PLAINS = resourceKey("armorer/4/emerald_enchanted_iron_boots_plains");
/*     */   
/*  76 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_HELMET_PLAINS = resourceKey("armorer/4/emerald_enchanted_iron_helmet_plains");
/*     */   
/*  77 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_LEGGINGS_PLAINS = resourceKey("armorer/4/emerald_enchanted_iron_leggings_plains");
/*     */   
/*  78 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_CHESTPLATE_PLAINS = resourceKey("armorer/4/emerald_enchanted_iron_chestplate_plains");
/*     */   
/*  80 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_BOOTS_SAVANNA = resourceKey("armorer/4/emerald_enchanted_iron_boots_savanna");
/*     */   
/*  81 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_HELMET_SAVANNA = resourceKey("armorer/4/emerald_enchanted_iron_helmet_savanna");
/*     */   
/*  82 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_LEGGINGS_SAVANNA = resourceKey("armorer/4/emerald_enchanted_iron_leggings_savanna");
/*     */   
/*  83 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_CHESTPLATE_SAVANNA = resourceKey("armorer/4/emerald_enchanted_iron_chestplate_savanna");
/*     */   
/*  85 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_BOOTS_SNOW = resourceKey("armorer/4/emerald_enchanted_iron_boots_snow");
/*     */   
/*  86 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_IRON_HELMET_SNOW = resourceKey("armorer/4/emerald_enchanted_iron_helmet_snow");
/*     */   
/*  88 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_BOOTS_JUNGLE = resourceKey("armorer/4/emerald_enchanted_chainmail_boots_jungle");
/*     */   
/*  89 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_HELMET_JUNGLE = resourceKey("armorer/4/emerald_enchanted_chainmail_helmet_jungle");
/*     */   
/*  90 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_LEGGINGS_JUNGLE = resourceKey("armorer/4/emerald_enchanted_chainmail_leggings_jungle");
/*     */   
/*  91 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_CHESTPLATE_JUNGLE = resourceKey("armorer/4/emerald_enchanted_chainmail_chestplate_jungle");
/*     */   
/*  93 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_BOOTS_SWAMP = resourceKey("armorer/4/emerald_enchanted_chainmail_boots_swamp");
/*     */   
/*  94 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_HELMET_SWAMP = resourceKey("armorer/4/emerald_enchanted_chainmail_helmet_swamp");
/*     */   
/*  95 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_LEGGINGS_SWAMP = resourceKey("armorer/4/emerald_enchanted_chainmail_leggings_swamp");
/*     */   
/*  96 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_CHESTPLATE_SWAMP = resourceKey("armorer/4/emerald_enchanted_chainmail_chestplate_swamp");
/*     */   
/*  98 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_AND_DIAMOND_BOOTS_DIAMOND_LEGGINGS_TAIGA = resourceKey("armorer/4/emerald_and_diamond_boots_diamond_leggings_taiga");
/*     */   
/*  99 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_AND_DIAMOND_LEGGINGS_DIAMOND_CHESTPLATE_TAIGA = resourceKey("armorer/4/emerald_and_diamond_leggings_diamond_chestplate_taiga");
/*     */   
/* 100 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_AND_DIAMOND_HELMET_DIAMOND_BOOTS_TAIGA = resourceKey("armorer/4/emerald_and_diamond_helmet_diamond_boots_taiga");
/*     */   
/* 101 */   public static final ResourceKey<VillagerTrade> ARMORER_4_EMERALD_AND_DIAMOND_CHESTPLATE_DIAMOND_HELMET_TAIGA = resourceKey("armorer/4/emerald_and_diamond_chestplate_diamond_helmet_taiga");
/*     */   
/* 103 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_CHESTPLATE_DESERT = resourceKey("armorer/5/emerald_and_diamond_diamond_chestplate_desert");
/*     */   
/* 104 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_LEGGINGS_DESERT = resourceKey("armorer/5/emerald_and_diamond_diamond_leggings_desert");
/*     */   
/* 106 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_LEGGINGS_PLAINS = resourceKey("armorer/5/emerald_and_diamond_diamond_leggings_plains");
/*     */   
/* 107 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_BOOTS_PLAINS = resourceKey("armorer/5/emerald_and_diamond_diamond_boots_plains");
/*     */   
/* 109 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_HELMET_SAVANNA = resourceKey("armorer/5/emerald_and_diamond_diamond_helmet_savanna");
/*     */   
/* 110 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_CHESTPLATE_SAVANNA = resourceKey("armorer/5/emerald_and_diamond_diamond_chestplate_savanna");
/*     */   
/* 112 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_BOOTS_SNOW = resourceKey("armorer/5/emerald_and_diamond_diamond_boots_snow");
/*     */   
/* 113 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_HELMET_SNOW = resourceKey("armorer/5/emerald_and_diamond_diamond_helmet_snow");
/*     */   
/* 115 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_CHAINMAIL_HELMET_JUNGLE = resourceKey("armorer/5/emerald_chainmail_helmet_jungle");
/*     */   
/* 116 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_CHAINMAIL_BOOTS_JUNGLE = resourceKey("armorer/5/emerald_chainmail_boots_jungle");
/*     */   
/* 118 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_CHAINMAIL_HELMET_SWAMP = resourceKey("armorer/5/emerald_chainmail_helmet_swamp");
/*     */   
/* 119 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_CHAINMAIL_BOOTS_SWAMP = resourceKey("armorer/5/emerald_chainmail_boots_swamp");
/*     */   
/* 121 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_CHESTPLATE_TAIGA = resourceKey("armorer/5/emerald_and_diamond_diamond_chestplate_taiga");
/*     */   
/* 122 */   public static final ResourceKey<VillagerTrade> ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_LEGGINGS_TAIGA = resourceKey("armorer/5/emerald_and_diamond_diamond_leggings_taiga");
/*     */   
/* 124 */   public static final ResourceKey<VillagerTrade> ARMORER_5_DIAMOND_BLOCK_EMERALD_TAIGA = resourceKey("armorer/5/diamond_block_emerald_taiga");
/*     */   
/* 125 */   public static final ResourceKey<VillagerTrade> ARMORER_5_IRON_BLOCK_EMERALD_NON_TAIGA = resourceKey("armorer/5/iron_block_emerald_non_taiga");
/*     */   
/*     */   public static Holder<VillagerTrade> bootstrap(BootstrapContext<VillagerTrade> context) {
/* 128 */     HolderGetter<Item> items = context.lookup(Registries.ITEM);
/* 129 */     HolderGetter<VillagerType> villagerVariants = context.lookup(Registries.VILLAGER_TYPE);
/* 131 */     HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
/* 132 */     HolderSet.Named named1 = enchantments.getOrThrow(EnchantmentTags.DOUBLE_TRADE_PRICE);
/* 134 */     HolderSet<VillagerType> desertType = villagerTypeHolderSet(villagerVariants, VillagerType.DESERT);
/* 135 */     HolderSet.Named named2 = enchantments.getOrThrow(EnchantmentTags.TRADES_DESERT_COMMON);
/* 137 */     HolderSet<VillagerType> jungleType = villagerTypeHolderSet(villagerVariants, VillagerType.JUNGLE);
/* 138 */     HolderSet.Named named3 = enchantments.getOrThrow(EnchantmentTags.TRADES_JUNGLE_COMMON);
/* 140 */     HolderSet<VillagerType> plainsType = villagerTypeHolderSet(villagerVariants, VillagerType.PLAINS);
/* 141 */     HolderSet.Named named4 = enchantments.getOrThrow(EnchantmentTags.TRADES_PLAINS_COMMON);
/* 143 */     HolderSet<VillagerType> savannaType = villagerTypeHolderSet(villagerVariants, VillagerType.SAVANNA);
/* 144 */     HolderSet.Named named5 = enchantments.getOrThrow(EnchantmentTags.TRADES_SAVANNA_COMMON);
/* 146 */     HolderSet<VillagerType> snowType = villagerTypeHolderSet(villagerVariants, VillagerType.SNOW);
/* 147 */     HolderSet.Named named6 = enchantments.getOrThrow(EnchantmentTags.TRADES_SNOW_COMMON);
/* 149 */     HolderSet<VillagerType> swampType = villagerTypeHolderSet(villagerVariants, VillagerType.SWAMP);
/* 150 */     HolderSet.Named named7 = enchantments.getOrThrow(EnchantmentTags.TRADES_SWAMP_COMMON);
/* 152 */     HolderSet<VillagerType> taigaType = villagerTypeHolderSet(villagerVariants, VillagerType.TAIGA);
/* 153 */     HolderSet.Named named8 = enchantments.getOrThrow(EnchantmentTags.TRADES_TAIGA_COMMON);
/* 155 */     register(context, LIBRARIAN_1_EMERALD_AND_BOOK_DESERT_ENCHANTED_BOOK, createLibrarianLevel1EnchantmentTrade(items, (HolderSet<Enchantment>)named1, desertType, (HolderSet<Enchantment>)named2));
/* 156 */     register(context, LIBRARIAN_1_EMERALD_AND_BOOK_JUNGLE_ENCHANTED_BOOK, createLibrarianLevel1EnchantmentTrade(items, (HolderSet<Enchantment>)named1, jungleType, (HolderSet<Enchantment>)named3));
/* 157 */     register(context, LIBRARIAN_1_EMERALD_AND_BOOK_PLAINS_ENCHANTED_BOOK, createLibrarianLevel1EnchantmentTrade(items, (HolderSet<Enchantment>)named1, plainsType, (HolderSet<Enchantment>)named4));
/* 158 */     register(context, LIBRARIAN_1_EMERALD_AND_BOOK_SAVANNA_ENCHANTED_BOOK, createLibrarianLevel1EnchantmentTrade(items, (HolderSet<Enchantment>)named1, savannaType, (HolderSet<Enchantment>)named5));
/* 159 */     register(context, LIBRARIAN_1_EMERALD_AND_BOOK_SNOW_ENCHANTED_BOOK, createLibrarianLevel1EnchantmentTrade(items, (HolderSet<Enchantment>)named1, snowType, (HolderSet<Enchantment>)named6));
/* 160 */     register(context, LIBRARIAN_1_EMERALD_AND_BOOK_SWAMP_ENCHANTED_BOOK, createLibrarianLevel1EnchantmentTrade(items, (HolderSet<Enchantment>)named1, swampType, (HolderSet<Enchantment>)named7));
/* 161 */     register(context, LIBRARIAN_1_EMERALD_AND_BOOK_TAIGA_ENCHANTED_BOOK, createLibrarianLevel1EnchantmentTrade(items, (HolderSet<Enchantment>)named1, taigaType, (HolderSet<Enchantment>)named8));
/* 163 */     register(context, LIBRARIAN_2_BOOK_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.BOOK, 4), new ItemStackTemplate(Items.EMERALD), 12, 10, 0.05F, 
/*     */           
/* 169 */           Optional.empty(), 
/* 170 */           List.of()));
/* 172 */     register(context, LIBRARIAN_2_EMERALD_AND_BOOK_DESERT_ENCHANTED_BOOK, createLibrarianLevel2EnchantmentTrade(items, (HolderSet<Enchantment>)named1, desertType, (HolderSet<Enchantment>)named2));
/* 173 */     register(context, LIBRARIAN_2_EMERALD_AND_BOOK_JUNGLE_ENCHANTED_BOOK, createLibrarianLevel2EnchantmentTrade(items, (HolderSet<Enchantment>)named1, jungleType, (HolderSet<Enchantment>)named3));
/* 174 */     register(context, LIBRARIAN_2_EMERALD_AND_BOOK_PLAINS_ENCHANTED_BOOK, createLibrarianLevel2EnchantmentTrade(items, (HolderSet<Enchantment>)named1, plainsType, (HolderSet<Enchantment>)named4));
/* 175 */     register(context, LIBRARIAN_2_EMERALD_AND_BOOK_SAVANNA_ENCHANTED_BOOK, createLibrarianLevel2EnchantmentTrade(items, (HolderSet<Enchantment>)named1, savannaType, (HolderSet<Enchantment>)named5));
/* 176 */     register(context, LIBRARIAN_2_EMERALD_AND_BOOK_SNOW_ENCHANTED_BOOK, createLibrarianLevel2EnchantmentTrade(items, (HolderSet<Enchantment>)named1, snowType, (HolderSet<Enchantment>)named6));
/* 177 */     register(context, LIBRARIAN_2_EMERALD_AND_BOOK_SWAMP_ENCHANTED_BOOK, createLibrarianLevel2EnchantmentTrade(items, (HolderSet<Enchantment>)named1, swampType, (HolderSet<Enchantment>)named7));
/* 178 */     register(context, LIBRARIAN_2_EMERALD_AND_BOOK_TAIGA_ENCHANTED_BOOK, createLibrarianLevel2EnchantmentTrade(items, (HolderSet<Enchantment>)named1, taigaType, (HolderSet<Enchantment>)named8));
/* 180 */     register(context, LIBRARIAN_3_EMERALD_AND_BOOK_DESERT_ENCHANTED_BOOK, createLibrarianLevel3EnchantmentTrade(items, (HolderSet<Enchantment>)named1, desertType, (HolderSet<Enchantment>)named2));
/* 181 */     register(context, LIBRARIAN_3_EMERALD_AND_BOOK_JUNGLE_ENCHANTED_BOOK, createLibrarianLevel3EnchantmentTrade(items, (HolderSet<Enchantment>)named1, jungleType, (HolderSet<Enchantment>)named3));
/* 182 */     register(context, LIBRARIAN_3_EMERALD_AND_BOOK_PLAINS_ENCHANTED_BOOK, createLibrarianLevel3EnchantmentTrade(items, (HolderSet<Enchantment>)named1, plainsType, (HolderSet<Enchantment>)named4));
/* 183 */     register(context, LIBRARIAN_3_EMERALD_AND_BOOK_SAVANNA_ENCHANTED_BOOK, createLibrarianLevel3EnchantmentTrade(items, (HolderSet<Enchantment>)named1, savannaType, (HolderSet<Enchantment>)named5));
/* 184 */     register(context, LIBRARIAN_3_EMERALD_AND_BOOK_SNOW_ENCHANTED_BOOK, createLibrarianLevel3EnchantmentTrade(items, (HolderSet<Enchantment>)named1, snowType, (HolderSet<Enchantment>)named6));
/* 185 */     register(context, LIBRARIAN_3_EMERALD_AND_BOOK_SWAMP_ENCHANTED_BOOK, createLibrarianLevel3EnchantmentTrade(items, (HolderSet<Enchantment>)named1, swampType, (HolderSet<Enchantment>)named7));
/* 186 */     register(context, LIBRARIAN_3_EMERALD_AND_BOOK_TAIGA_ENCHANTED_BOOK, createLibrarianLevel3EnchantmentTrade(items, (HolderSet<Enchantment>)named1, taigaType, (HolderSet<Enchantment>)named8));
/* 188 */     register(context, LIBRARIAN_5_EMERALD_AND_BOOK_DESERT_ENCHANTED_BOOK, createLibrarianLevel5EnchantmentTrade(items, (HolderSet<Enchantment>)named1, desertType, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.EFFICIENCY), 3));
/* 189 */     register(context, LIBRARIAN_5_EMERALD_AND_BOOK_JUNGLE_ENCHANTED_BOOK, createLibrarianLevel5EnchantmentTrade(items, (HolderSet<Enchantment>)named1, jungleType, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.UNBREAKING), 2));
/* 190 */     register(context, LIBRARIAN_5_EMERALD_AND_BOOK_PLAINS_ENCHANTED_BOOK, createLibrarianLevel5EnchantmentTrade(items, (HolderSet<Enchantment>)named1, plainsType, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.PROTECTION), 3));
/* 191 */     register(context, LIBRARIAN_5_EMERALD_AND_BOOK_SAVANNA_ENCHANTED_BOOK, createLibrarianLevel5EnchantmentTrade(items, (HolderSet<Enchantment>)named1, savannaType, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.SHARPNESS), 3));
/* 192 */     register(context, LIBRARIAN_5_EMERALD_AND_BOOK_TAIGA_ENCHANTED_BOOK, createLibrarianLevel5EnchantmentTrade(items, (HolderSet<Enchantment>)named1, taigaType, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.FORTUNE), 2));
/* 194 */     register(context, LIBRARIAN_5_EMERALD_AND_BOOK_SNOW_ENCHANTED_BOOK, createLibrarianLevel5SpecialEnchantmentTrade(items, (HolderSet<Enchantment>)named1, snowType, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.SILK_TOUCH)));
/* 195 */     register(context, LIBRARIAN_5_EMERALD_AND_BOOK_SWAMP_ENCHANTED_BOOK, createLibrarianLevel5SpecialEnchantmentTrade(items, (HolderSet<Enchantment>)named1, swampType, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.MENDING)));
/* 197 */     register(context, ARMORER_1_IRON_INGOT_EMERALD, new VillagerTrade(new TradeCost((ItemLike)Items.IRON_INGOT, 5), new ItemStackTemplate(Items.EMERALD), 12, 5, 0.05F, 
/*     */           
/* 203 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.DESERT, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.TAIGA))), 
/* 204 */           List.of()));
/* 207 */     register(context, ARMORER_2_EMERALD_IRON_BOOTS_GROUP_1, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 4), new ItemStackTemplate(Items.IRON_BOOTS), 12, 5, 0.05F, 
/*     */           
/* 213 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.DESERT, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.TAIGA))), 
/* 214 */           List.of()));
/* 216 */     register(context, ARMORER_2_EMERALD_IRON_HELMET_GROUP_1, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 5), new ItemStackTemplate(Items.IRON_HELMET), 12, 5, 0.05F, 
/*     */           
/* 222 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.DESERT, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.TAIGA))), 
/* 223 */           List.of()));
/* 225 */     register(context, ARMORER_2_EMERALD_IRON_LEGGINGS_GROUP_1, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 7), new ItemStackTemplate(Items.IRON_LEGGINGS), 12, 5, 0.05F, 
/*     */           
/* 231 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.DESERT, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.TAIGA))), 
/* 232 */           List.of()));
/* 234 */     register(context, ARMORER_2_EMERALD_IRON_CHESTPLATE_GROUP_1, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 9), new ItemStackTemplate(Items.IRON_CHESTPLATE), 12, 5, 0.05F, 
/*     */           
/* 240 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.DESERT, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.TAIGA))), 
/* 241 */           List.of()));
/* 243 */     register(context, ARMORER_2_EMERALD_CHAINMAIL_BOOTS_GROUP_2, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 4), new ItemStackTemplate(Items.CHAINMAIL_BOOTS), 12, 5, 0.05F, 
/*     */           
/* 249 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.JUNGLE, VillagerType.SWAMP))), 
/* 250 */           List.of()));
/* 252 */     register(context, ARMORER_2_EMERALD_CHAINMAIL_HELMET_GROUP_2, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 5), new ItemStackTemplate(Items.CHAINMAIL_HELMET), 12, 5, 0.05F, 
/*     */           
/* 258 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.JUNGLE, VillagerType.SWAMP))), 
/* 259 */           List.of()));
/* 261 */     register(context, ARMORER_2_EMERALD_CHAINMAIL_LEGGINGS_GROUP_2, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 7), new ItemStackTemplate(Items.CHAINMAIL_LEGGINGS), 12, 5, 0.05F, 
/*     */           
/* 267 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.JUNGLE, VillagerType.SWAMP))), 
/* 268 */           List.of()));
/* 270 */     register(context, ARMORER_2_EMERALD_CHAINMAIL_CHESTPLATE_GROUP_2, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 9), new ItemStackTemplate(Items.CHAINMAIL_CHESTPLATE), 12, 5, 0.05F, 
/*     */           
/* 276 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.JUNGLE, VillagerType.SWAMP))), 
/* 277 */           List.of()));
/* 280 */     register(context, ARMORER_3_EMERALD_BELL, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 36), new ItemStackTemplate(Items.BELL), 12, 10, 0.2F, 
/*     */           
/* 286 */           Optional.empty(), 
/* 287 */           List.of()));
/* 290 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_BOOTS_DESERT, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 8), new ItemStackTemplate(Items.IRON_BOOTS), 3, 15, 0.05F, 
/*     */           
/* 296 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.DESERT))), 
/* 297 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.THORNS), 1, Items.IRON_BOOTS)));
/* 299 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_HELMET_DESERT, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 9), new ItemStackTemplate(Items.IRON_HELMET), 3, 15, 0.05F, 
/*     */           
/* 305 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.DESERT))), 
/* 306 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.THORNS), 1, Items.IRON_HELMET)));
/* 308 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_LEGGINGS_DESERT, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 11), new ItemStackTemplate(Items.IRON_LEGGINGS), 3, 15, 0.05F, 
/*     */           
/* 314 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.DESERT))), 
/* 315 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.THORNS), 1, Items.IRON_LEGGINGS)));
/* 317 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_CHESTPLATE_DESERT, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 13), new ItemStackTemplate(Items.IRON_CHESTPLATE), 3, 15, 0.05F, 
/*     */           
/* 323 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.DESERT))), 
/* 324 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.THORNS), 1, Items.IRON_CHESTPLATE)));
/* 326 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_BOOTS_PLAINS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 8), new ItemStackTemplate(Items.IRON_BOOTS), 3, 15, 0.05F, 
/*     */           
/* 332 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.PLAINS))), 
/* 333 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.PROTECTION), 1, Items.IRON_BOOTS)));
/* 335 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_HELMET_PLAINS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 9), new ItemStackTemplate(Items.IRON_HELMET), 3, 15, 0.05F, 
/*     */           
/* 341 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.PLAINS))), 
/* 342 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.PROTECTION), 1, Items.IRON_HELMET)));
/* 344 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_LEGGINGS_PLAINS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 11), new ItemStackTemplate(Items.IRON_LEGGINGS), 3, 15, 0.05F, 
/*     */           
/* 350 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.PLAINS))), 
/* 351 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.PROTECTION), 1, Items.IRON_LEGGINGS)));
/* 353 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_CHESTPLATE_PLAINS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 13), new ItemStackTemplate(Items.IRON_CHESTPLATE), 3, 15, 0.05F, 
/*     */           
/* 359 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.PLAINS))), 
/* 360 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.PROTECTION), 1, Items.IRON_CHESTPLATE)));
/* 362 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_BOOTS_SAVANNA, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), new ItemStackTemplate(Items.IRON_BOOTS), 3, 15, 0.05F, 
/*     */           
/* 368 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SAVANNA))), 
/* 369 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.BINDING_CURSE), 1, Items.IRON_BOOTS)));
/* 371 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_HELMET_SAVANNA, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 3), new ItemStackTemplate(Items.IRON_HELMET), 3, 15, 0.05F, 
/*     */           
/* 377 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SAVANNA))), 
/* 378 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.BINDING_CURSE), 1, Items.IRON_HELMET)));
/* 380 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_LEGGINGS_SAVANNA, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 5), new ItemStackTemplate(Items.IRON_LEGGINGS), 3, 15, 0.05F, 
/*     */           
/* 386 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SAVANNA))), 
/* 387 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.BINDING_CURSE), 1, Items.IRON_LEGGINGS)));
/* 389 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_CHESTPLATE_SAVANNA, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 7), new ItemStackTemplate(Items.IRON_CHESTPLATE), 3, 15, 0.05F, 
/*     */           
/* 395 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SAVANNA))), 
/* 396 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.BINDING_CURSE), 1, Items.IRON_CHESTPLATE)));
/* 398 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_BOOTS_SNOW, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 8), new ItemStackTemplate(Items.IRON_BOOTS), 3, 15, 0.05F, 
/*     */           
/* 404 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SNOW))), 
/* 405 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.FROST_WALKER), 1, Items.IRON_BOOTS)));
/* 407 */     register(context, ARMORER_4_EMERALD_ENCHANTED_IRON_HELMET_SNOW, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 9), new ItemStackTemplate(Items.IRON_HELMET), 3, 15, 0.05F, 
/*     */           
/* 413 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SNOW))), 
/* 414 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.AQUA_AFFINITY), 1, Items.IRON_HELMET)));
/* 416 */     register(context, ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_BOOTS_JUNGLE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 8), new ItemStackTemplate(Items.CHAINMAIL_BOOTS), 3, 15, 0.05F, 
/*     */           
/* 422 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.JUNGLE))), 
/* 423 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.UNBREAKING), 1, Items.CHAINMAIL_BOOTS)));
/* 425 */     register(context, ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_HELMET_JUNGLE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 9), new ItemStackTemplate(Items.CHAINMAIL_HELMET), 3, 15, 0.05F, 
/*     */           
/* 431 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.JUNGLE))), 
/* 432 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.UNBREAKING), 1, Items.CHAINMAIL_HELMET)));
/* 434 */     register(context, ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_LEGGINGS_JUNGLE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 11), new ItemStackTemplate(Items.CHAINMAIL_LEGGINGS), 3, 15, 0.05F, 
/*     */           
/* 440 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.JUNGLE))), 
/* 441 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.UNBREAKING), 1, Items.CHAINMAIL_LEGGINGS)));
/* 443 */     register(context, ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_CHESTPLATE_JUNGLE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 13), new ItemStackTemplate(Items.CHAINMAIL_CHESTPLATE), 3, 15, 0.05F, 
/*     */           
/* 449 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.JUNGLE))), 
/* 450 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.UNBREAKING), 1, Items.CHAINMAIL_CHESTPLATE)));
/* 452 */     register(context, ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_BOOTS_SWAMP, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 8), new ItemStackTemplate(Items.CHAINMAIL_BOOTS), 3, 15, 0.05F, 
/*     */           
/* 458 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SWAMP))), 
/* 459 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.MENDING), 1, Items.CHAINMAIL_BOOTS)));
/* 461 */     register(context, ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_HELMET_SWAMP, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 9), new ItemStackTemplate(Items.CHAINMAIL_HELMET), 3, 15, 0.05F, 
/*     */           
/* 467 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SWAMP))), 
/* 468 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.MENDING), 1, Items.CHAINMAIL_HELMET)));
/* 470 */     register(context, ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_LEGGINGS_SWAMP, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 11), new ItemStackTemplate(Items.CHAINMAIL_LEGGINGS), 3, 15, 0.05F, 
/*     */           
/* 476 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SWAMP))), 
/* 477 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.MENDING), 1, Items.CHAINMAIL_LEGGINGS)));
/* 479 */     register(context, ARMORER_4_EMERALD_ENCHANTED_CHAINMAIL_CHESTPLATE_SWAMP, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 13), new ItemStackTemplate(Items.CHAINMAIL_CHESTPLATE), 3, 15, 0.05F, 
/*     */           
/* 485 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SWAMP))), 
/* 486 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.MENDING), 1, Items.CHAINMAIL_CHESTPLATE)));
/* 488 */     register(context, ARMORER_4_EMERALD_AND_DIAMOND_BOOTS_DIAMOND_LEGGINGS_TAIGA, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 4), 
/*     */           
/* 490 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND_BOOTS, 1)), new ItemStackTemplate(Items.DIAMOND_LEGGINGS), 3, 15, 0.05F, 
/*     */           
/* 495 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.TAIGA))), 
/* 496 */           List.of()));
/* 498 */     register(context, ARMORER_4_EMERALD_AND_DIAMOND_LEGGINGS_DIAMOND_CHESTPLATE_TAIGA, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 4), 
/*     */           
/* 500 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND_LEGGINGS, 1)), new ItemStackTemplate(Items.DIAMOND_CHESTPLATE), 3, 15, 0.05F, 
/*     */           
/* 505 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.TAIGA))), 
/* 506 */           List.of()));
/* 508 */     register(context, ARMORER_4_EMERALD_AND_DIAMOND_HELMET_DIAMOND_BOOTS_TAIGA, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 4), 
/*     */           
/* 510 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND_HELMET, 1)), new ItemStackTemplate(Items.DIAMOND_BOOTS), 3, 15, 0.05F, 
/*     */           
/* 515 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.TAIGA))), 
/* 516 */           List.of()));
/* 518 */     register(context, ARMORER_4_EMERALD_AND_DIAMOND_CHESTPLATE_DIAMOND_HELMET_TAIGA, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 2), 
/*     */           
/* 520 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND_CHESTPLATE, 1)), new ItemStackTemplate(Items.DIAMOND_HELMET), 3, 15, 0.05F, 
/*     */           
/* 525 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.TAIGA))), 
/* 526 */           List.of()));
/* 529 */     register(context, ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_CHESTPLATE_DESERT, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 16), 
/*     */           
/* 531 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND, 4)), new ItemStackTemplate(Items.DIAMOND_CHESTPLATE), 3, 30, 0.05F, 
/*     */           
/* 536 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.DESERT))), 
/* 537 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.THORNS), 1, Items.DIAMOND_CHESTPLATE)));
/* 539 */     register(context, ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_LEGGINGS_DESERT, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 16), 
/*     */           
/* 541 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND, 3)), new ItemStackTemplate(Items.DIAMOND_LEGGINGS), 3, 30, 0.05F, 
/*     */           
/* 546 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.DESERT))), 
/* 547 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.THORNS), 1, Items.DIAMOND_LEGGINGS)));
/* 549 */     register(context, ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_LEGGINGS_PLAINS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 16), 
/*     */           
/* 551 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND, 3)), new ItemStackTemplate(Items.DIAMOND_LEGGINGS), 3, 30, 0.05F, 
/*     */           
/* 556 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.PLAINS))), 
/* 557 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.PROTECTION), 1, Items.DIAMOND_LEGGINGS)));
/* 559 */     register(context, ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_BOOTS_PLAINS, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 12), 
/*     */           
/* 561 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND, 2)), new ItemStackTemplate(Items.DIAMOND_BOOTS), 3, 30, 0.05F, 
/*     */           
/* 566 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.PLAINS))), 
/* 567 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.PROTECTION), 1, Items.DIAMOND_BOOTS)));
/* 569 */     register(context, ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_HELMET_SAVANNA, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 6), 
/*     */           
/* 571 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND, 2)), new ItemStackTemplate(Items.DIAMOND_HELMET), 3, 30, 0.05F, 
/*     */           
/* 576 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SAVANNA))), 
/* 577 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.BINDING_CURSE), 1, Items.DIAMOND_HELMET)));
/* 579 */     register(context, ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_CHESTPLATE_SAVANNA, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 8), 
/*     */           
/* 581 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND, 3)), new ItemStackTemplate(Items.DIAMOND_CHESTPLATE), 3, 30, 0.05F, 
/*     */           
/* 586 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SAVANNA))), 
/* 587 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.BINDING_CURSE), 1, Items.DIAMOND_CHESTPLATE)));
/* 589 */     register(context, ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_BOOTS_SNOW, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 12), 
/*     */           
/* 591 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND, 2)), new ItemStackTemplate(Items.DIAMOND_BOOTS), 3, 30, 0.05F, 
/*     */           
/* 596 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SNOW))), 
/* 597 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.FROST_WALKER), 1, Items.DIAMOND_BOOTS)));
/* 599 */     register(context, ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_HELMET_SNOW, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 12), 
/*     */           
/* 601 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND, 3)), new ItemStackTemplate(Items.DIAMOND_HELMET), 3, 30, 0.05F, 
/*     */           
/* 606 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SNOW))), 
/* 607 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.AQUA_AFFINITY), 1, Items.DIAMOND_HELMET)));
/* 609 */     register(context, ARMORER_5_EMERALD_CHAINMAIL_HELMET_JUNGLE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 9), new ItemStackTemplate(Items.CHAINMAIL_HELMET), 3, 30, 0.05F, 
/*     */           
/* 615 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.JUNGLE))), 
/* 616 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.PROJECTILE_PROTECTION), 1, Items.CHAINMAIL_HELMET)));
/* 618 */     register(context, ARMORER_5_EMERALD_CHAINMAIL_BOOTS_JUNGLE, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 8), new ItemStackTemplate(Items.CHAINMAIL_BOOTS), 3, 30, 0.05F, 
/*     */           
/* 624 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.JUNGLE))), 
/* 625 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.FEATHER_FALLING), 1, Items.CHAINMAIL_BOOTS)));
/* 627 */     register(context, ARMORER_5_EMERALD_CHAINMAIL_HELMET_SWAMP, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 9), new ItemStackTemplate(Items.CHAINMAIL_HELMET), 3, 30, 0.05F, 
/*     */           
/* 633 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SWAMP))), 
/* 634 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.RESPIRATION), 1, Items.CHAINMAIL_HELMET)));
/* 636 */     register(context, ARMORER_5_EMERALD_CHAINMAIL_BOOTS_SWAMP, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 8), new ItemStackTemplate(Items.CHAINMAIL_BOOTS), 3, 30, 0.05F, 
/*     */           
/* 642 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.SWAMP))), 
/* 643 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.DEPTH_STRIDER), 1, Items.CHAINMAIL_BOOTS)));
/* 645 */     register(context, ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_CHESTPLATE_TAIGA, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 18), 
/*     */           
/* 647 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND, 4)), new ItemStackTemplate(Items.DIAMOND_CHESTPLATE), 3, 30, 0.05F, 
/*     */           
/* 652 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.TAIGA))), 
/* 653 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.BLAST_PROTECTION), 1, Items.DIAMOND_CHESTPLATE)));
/* 655 */     register(context, ARMORER_5_EMERALD_AND_DIAMOND_DIAMOND_LEGGINGS_TAIGA, new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 18), 
/*     */           
/* 657 */           Optional.of(new TradeCost((ItemLike)Items.DIAMOND, 3)), new ItemStackTemplate(Items.DIAMOND_LEGGINGS), 3, 30, 0.05F, 
/*     */           
/* 662 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.TAIGA))), 
/* 663 */           enchantedItem(items, (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.BLAST_PROTECTION), 1, Items.DIAMOND_LEGGINGS)));
/* 665 */     register(context, ARMORER_5_DIAMOND_BLOCK_EMERALD_TAIGA, new VillagerTrade(new TradeCost((ItemLike)Items.DIAMOND_BLOCK, 1), new ItemStackTemplate(Items.EMERALD, 42), 12, 30, 0.05F, 
/*     */           
/* 671 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.TAIGA))), 
/* 672 */           List.of()));
/* 674 */     return (Holder<VillagerTrade>)register(context, ARMORER_5_IRON_BLOCK_EMERALD_NON_TAIGA, new VillagerTrade(new TradeCost((ItemLike)Items.IRON_BLOCK, 1), new ItemStackTemplate(Items.EMERALD, 4), 12, 30, 0.05F, 
/*     */           
/* 680 */           villagerTypeRestriction(villagerTypeHolderSet(villagerVariants, List.of(VillagerType.DESERT, VillagerType.JUNGLE, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.SWAMP))), 
/* 681 */           List.of()));
/*     */   }
/*     */   
/*     */   private static VillagerTrade createLibrarianLevel1EnchantmentTrade(HolderGetter<Item> items, HolderSet<Enchantment> doubleTradePrice, HolderSet<VillagerType> villagerTypes, HolderSet<Enchantment> enchantments) {
/* 686 */     return new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 0), 
/*     */         
/* 688 */         Optional.of(new TradeCost((ItemLike)Items.BOOK, 1)), new ItemStackTemplate(Items.ENCHANTED_BOOK), 12, 1, 0.2F, 
/*     */         
/* 693 */         villagerTypeRestriction(villagerTypes), 
/* 694 */         enchantedBook(items, enchantments), doubleTradePrice);
/*     */   }
/*     */   
/*     */   private static VillagerTrade createLibrarianLevel2EnchantmentTrade(HolderGetter<Item> items, HolderSet<Enchantment> doubleTradePrice, HolderSet<VillagerType> villagerTypes, HolderSet<Enchantment> enchantments) {
/* 700 */     return new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 0), 
/*     */         
/* 702 */         Optional.of(new TradeCost((ItemLike)Items.BOOK, 1)), new ItemStackTemplate(Items.ENCHANTED_BOOK), 12, 5, 0.2F, 
/*     */         
/* 707 */         villagerTypeRestriction(villagerTypes), 
/* 708 */         enchantedBook(items, enchantments), doubleTradePrice);
/*     */   }
/*     */   
/*     */   private static VillagerTrade createLibrarianLevel3EnchantmentTrade(HolderGetter<Item> items, HolderSet<Enchantment> doubleTradePrice, HolderSet<VillagerType> villagerTypes, HolderSet<Enchantment> enchantments) {
/* 714 */     return new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 0), 
/*     */         
/* 716 */         Optional.of(new TradeCost((ItemLike)Items.BOOK, 1)), new ItemStackTemplate(Items.ENCHANTED_BOOK), 12, 10, 0.2F, 
/*     */         
/* 721 */         villagerTypeRestriction(villagerTypes), 
/* 722 */         enchantedBook(items, enchantments), doubleTradePrice);
/*     */   }
/*     */   
/*     */   private static VillagerTrade createLibrarianLevel5EnchantmentTrade(HolderGetter<Item> items, HolderSet<Enchantment> doubleTradePrice, HolderSet<VillagerType> villagerTypes, Holder<Enchantment> enchantment, int level) {
/* 728 */     return new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 
/* 729 */           (NumberProvider)Sum.sum(new NumberProvider[] { (NumberProvider)ConstantValue.exactly((3 * level + 2)), (NumberProvider)UniformGenerator.between(0.0F, (5 + level * 10)) })), Optional.of(new TradeCost((ItemLike)Items.BOOK, 1)), new ItemStackTemplate(Items.ENCHANTED_BOOK), 12, 30, 0.2F, 
/*     */         
/* 735 */         villagerTypeRestriction(villagerTypes), 
/* 736 */         enchantedBook(items, enchantment, level), doubleTradePrice);
/*     */   }
/*     */   
/*     */   private static VillagerTrade createLibrarianLevel5SpecialEnchantmentTrade(HolderGetter<Item> items, HolderSet<Enchantment> doubleTradePrice, HolderSet<VillagerType> villagerTypes, Holder<Enchantment> enchantment) {
/* 742 */     return new VillagerTrade(new TradeCost((ItemLike)Items.EMERALD, 0), 
/*     */         
/* 744 */         Optional.of(new TradeCost((ItemLike)Items.BOOK, 1)), new ItemStackTemplate(Items.ENCHANTED_BOOK), 12, 30, 0.2F, 
/*     */         
/* 749 */         villagerTypeRestriction(villagerTypes), 
/* 750 */         enchantedBook(items, (HolderSet<Enchantment>)HolderSet.direct(new Holder[] { enchantment })), doubleTradePrice);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\trading\TradeRebalanceVillagerTrades.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */