/*     */ package net.minecraft.data.tags;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.references.BlockItemId;
/*     */ import net.minecraft.references.BlockItemIds;
/*     */ import net.minecraft.references.ItemIds;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockItemTagId;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.ColorCollection;
/*     */ import net.minecraft.world.level.block.WeatheringCopperCollection;
/*     */ 
/*     */ public class VanillaItemTagsProvider extends TagsProvider<Item> {
/*     */   public VanillaItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/*  20 */     super(output, Registries.ITEM, lookupProvider);
/*     */   }
/*     */   
/*     */   protected BlockItemTagAppender<Item> tag(TagKey<Item> tag) {
/*  25 */     return new BlockItemTagAppender<Item>(this, super.tag(tag)) {
/*     */         protected ResourceKey<Item> convertElement(BlockItemId element) {
/*  28 */           return element.item();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected void addTags(HolderLookup.Provider registries) {
/*  35 */     (new VanillaBlockItemTagsProvider(tagId -> BlockItemTagsProvider.wrapForItems(tag(tagId.item())))).run();
/*  37 */     tag(ItemTags.BANNERS).addAll(toIds(BlockItemIds.BANNER));
/*  38 */     tag(ItemTags.BOATS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.OAK_BOAT, ItemIds.SPRUCE_BOAT, ItemIds.BIRCH_BOAT, ItemIds.JUNGLE_BOAT, ItemIds.ACACIA_BOAT, ItemIds.DARK_OAK_BOAT, ItemIds.PALE_OAK_BOAT, ItemIds.MANGROVE_BOAT, ItemIds.BAMBOO_RAFT, ItemIds.CHERRY_BOAT }).addTag(ItemTags.CHEST_BOATS);
/*  39 */     tag(ItemTags.BUNDLES).add(ItemIds.BUNDLE).addAll(ItemIds.DYED_BUNDLE);
/*  40 */     tag(ItemTags.CHEST_BOATS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.OAK_CHEST_BOAT, ItemIds.SPRUCE_CHEST_BOAT, ItemIds.BIRCH_CHEST_BOAT, ItemIds.JUNGLE_CHEST_BOAT, ItemIds.ACACIA_CHEST_BOAT, ItemIds.DARK_OAK_CHEST_BOAT, ItemIds.PALE_OAK_CHEST_BOAT, ItemIds.MANGROVE_CHEST_BOAT, ItemIds.BAMBOO_CHEST_RAFT, ItemIds.CHERRY_CHEST_BOAT });
/*  41 */     tag(ItemTags.EGGS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.EGG, ItemIds.BLUE_EGG, ItemIds.BROWN_EGG });
/*  42 */     tag(ItemTags.FISHES).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.COD, ItemIds.COOKED_COD, ItemIds.SALMON, ItemIds.COOKED_SALMON, ItemIds.PUFFERFISH, ItemIds.TROPICAL_FISH });
/*  43 */     tag(ItemTags.CREEPER_DROP_MUSIC_DISCS).add((ResourceKey<Item>[])new ResourceKey[] { 
/*  43 */           ItemIds.MUSIC_DISC_13, ItemIds.MUSIC_DISC_CAT, ItemIds.MUSIC_DISC_BLOCKS, ItemIds.MUSIC_DISC_CHIRP, ItemIds.MUSIC_DISC_FAR, ItemIds.MUSIC_DISC_MALL, ItemIds.MUSIC_DISC_MELLOHI, ItemIds.MUSIC_DISC_STAL, ItemIds.MUSIC_DISC_STRAD, ItemIds.MUSIC_DISC_WARD, 
/*  43 */           ItemIds.MUSIC_DISC_11, ItemIds.MUSIC_DISC_WAIT });
/*  44 */     tag(ItemTags.COALS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.COAL, ItemIds.CHARCOAL });
/*  45 */     tag(ItemTags.ARROWS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.ARROW, ItemIds.TIPPED_ARROW, ItemIds.SPECTRAL_ARROW });
/*  46 */     tag(ItemTags.LECTERN_BOOKS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.WRITTEN_BOOK, ItemIds.WRITABLE_BOOK });
/*  47 */     tag(ItemTags.BEACON_PAYMENT_ITEMS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.NETHERITE_INGOT, ItemIds.EMERALD, ItemIds.DIAMOND, ItemIds.GOLD_INGOT, ItemIds.IRON_INGOT });
/*  48 */     tag(ItemTags.PIGLIN_REPELLENTS).add(new BlockItemId[] { BlockItemIds.SOUL_TORCH }).add(new BlockItemId[] { BlockItemIds.SOUL_LANTERN }).add(new BlockItemId[] { BlockItemIds.SOUL_CAMPFIRE });
/*  49 */     tag(ItemTags.PIGLIN_LOVED).addTag(ItemTags.GOLD_ORES)
/*  50 */       .add(new BlockItemId[] { BlockItemIds.GOLD_BLOCK, BlockItemIds.GILDED_BLACKSTONE, BlockItemIds.LIGHT_WEIGHTED_PRESSURE_PLATE }).add(ItemIds.GOLD_INGOT).add(new BlockItemId[] { BlockItemIds.BELL }).add(new ResourceKey[] { 
/*     */           ItemIds.CLOCK, ItemIds.GOLDEN_CARROT, ItemIds.GLISTERING_MELON_SLICE, ItemIds.GOLDEN_APPLE, ItemIds.ENCHANTED_GOLDEN_APPLE, ItemIds.GOLDEN_HELMET, ItemIds.GOLDEN_CHESTPLATE, ItemIds.GOLDEN_LEGGINGS, ItemIds.GOLDEN_BOOTS, ItemIds.GOLDEN_HORSE_ARMOR, 
/*  54 */           ItemIds.GOLDEN_NAUTILUS_ARMOR, ItemIds.GOLDEN_SWORD, ItemIds.GOLDEN_SPEAR, ItemIds.GOLDEN_PICKAXE, ItemIds.GOLDEN_SHOVEL, ItemIds.GOLDEN_AXE, ItemIds.GOLDEN_HOE, ItemIds.RAW_GOLD }).add(new BlockItemId[] { BlockItemIds.RAW_GOLD_BLOCK, BlockItemIds.GOLDEN_DANDELION });
/*  55 */     tag(ItemTags.IGNORED_BY_PIGLIN_BABIES).add(ItemIds.LEATHER);
/*  56 */     tag(ItemTags.PIGLIN_FOOD).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.PORKCHOP, ItemIds.COOKED_PORKCHOP });
/*  57 */     tag(ItemTags.PIGLIN_SAFE_ARMOR).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.GOLDEN_HELMET, ItemIds.GOLDEN_CHESTPLATE, ItemIds.GOLDEN_LEGGINGS, ItemIds.GOLDEN_BOOTS });
/*  58 */     tag(ItemTags.FOX_FOOD).add(new BlockItemId[] { BlockItemIds.SWEET_BERRY_CROP, BlockItemIds.GLOW_BERRY_CROP });
/*  59 */     tag(ItemTags.DUPLICATES_ALLAYS).add(ItemIds.AMETHYST_SHARD);
/*  60 */     tag(ItemTags.BREWING_FUEL).add(ItemIds.BLAZE_POWDER);
/*  62 */     tag(ItemTags.NON_FLAMMABLE_WOOD).add(new BlockItemId[] { 
/*  62 */           BlockItemIds.WARPED_STEM, BlockItemIds.STRIPPED_WARPED_STEM, BlockItemIds.WARPED_HYPHAE, BlockItemIds.STRIPPED_WARPED_HYPHAE, BlockItemIds.CRIMSON_STEM, BlockItemIds.STRIPPED_CRIMSON_STEM, BlockItemIds.CRIMSON_HYPHAE, BlockItemIds.STRIPPED_CRIMSON_HYPHAE, BlockItemIds.CRIMSON_PLANKS, BlockItemIds.WARPED_PLANKS, 
/*  62 */           BlockItemIds.CRIMSON_SLAB, BlockItemIds.WARPED_SLAB, BlockItemIds.CRIMSON_PRESSURE_PLATE, BlockItemIds.WARPED_PRESSURE_PLATE, BlockItemIds.CRIMSON_FENCE, BlockItemIds.WARPED_FENCE, BlockItemIds.CRIMSON_TRAPDOOR, BlockItemIds.WARPED_TRAPDOOR, BlockItemIds.CRIMSON_FENCE_GATE, BlockItemIds.WARPED_FENCE_GATE, 
/*  62 */           BlockItemIds.CRIMSON_STAIRS, BlockItemIds.WARPED_STAIRS, BlockItemIds.CRIMSON_BUTTON, BlockItemIds.WARPED_BUTTON, BlockItemIds.CRIMSON_DOOR, BlockItemIds.WARPED_DOOR, BlockItemIds.CRIMSON_SIGN, BlockItemIds.WARPED_SIGN, BlockItemIds.WARPED_HANGING_SIGN, BlockItemIds.CRIMSON_HANGING_SIGN, 
/*  62 */           BlockItemIds.WARPED_SHELF, BlockItemIds.CRIMSON_SHELF });
/*  63 */     tag(ItemTags.WOODEN_TOOL_MATERIALS).addTag(ItemTags.PLANKS);
/*  64 */     tag(ItemTags.STONE_TOOL_MATERIALS).add(new BlockItemId[] { BlockItemIds.COBBLESTONE, BlockItemIds.BLACKSTONE, BlockItemIds.COBBLED_DEEPSLATE });
/*  65 */     tag(ItemTags.COPPER_TOOL_MATERIALS).add(ItemIds.COPPER_INGOT);
/*  66 */     tag(ItemTags.IRON_TOOL_MATERIALS).add(ItemIds.IRON_INGOT);
/*  67 */     tag(ItemTags.GOLD_TOOL_MATERIALS).add(ItemIds.GOLD_INGOT);
/*  68 */     tag(ItemTags.DIAMOND_TOOL_MATERIALS).add(ItemIds.DIAMOND);
/*  69 */     tag(ItemTags.NETHERITE_TOOL_MATERIALS).add(ItemIds.NETHERITE_INGOT);
/*  70 */     tag(ItemTags.REPAIRS_LEATHER_ARMOR).add(ItemIds.LEATHER);
/*  71 */     tag(ItemTags.REPAIRS_COPPER_ARMOR).add(ItemIds.COPPER_INGOT);
/*  72 */     tag(ItemTags.REPAIRS_CHAIN_ARMOR).add(ItemIds.IRON_INGOT);
/*  73 */     tag(ItemTags.REPAIRS_IRON_ARMOR).add(ItemIds.IRON_INGOT);
/*  74 */     tag(ItemTags.REPAIRS_GOLD_ARMOR).add(ItemIds.GOLD_INGOT);
/*  75 */     tag(ItemTags.REPAIRS_DIAMOND_ARMOR).add(ItemIds.DIAMOND);
/*  76 */     tag(ItemTags.REPAIRS_NETHERITE_ARMOR).add(ItemIds.NETHERITE_INGOT);
/*  77 */     tag(ItemTags.REPAIRS_TURTLE_HELMET).add(ItemIds.TURTLE_SCUTE);
/*  78 */     tag(ItemTags.REPAIRS_WOLF_ARMOR).add(ItemIds.ARMADILLO_SCUTE);
/*  79 */     tag(ItemTags.STONE_CRAFTING_MATERIALS).add(new BlockItemId[] { BlockItemIds.COBBLESTONE, BlockItemIds.BLACKSTONE, BlockItemIds.COBBLED_DEEPSLATE });
/*  80 */     tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.LEATHER_BOOTS, ItemIds.LEATHER_LEGGINGS, ItemIds.LEATHER_CHESTPLATE, ItemIds.LEATHER_HELMET, ItemIds.LEATHER_HORSE_ARMOR });
/*  81 */     tag(ItemTags.AXOLOTL_FOOD).add(ItemIds.TROPICAL_FISH_BUCKET);
/*  82 */     tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.DIAMOND_PICKAXE, ItemIds.GOLDEN_PICKAXE, ItemIds.IRON_PICKAXE, ItemIds.NETHERITE_PICKAXE, ItemIds.STONE_PICKAXE, ItemIds.WOODEN_PICKAXE, ItemIds.COPPER_PICKAXE });
/*  83 */     tag(ItemTags.COMPASSES).add(ItemIds.COMPASS).add(ItemIds.RECOVERY_COMPASS);
/*  84 */     tag(ItemTags.CREEPER_IGNITERS).add(ItemIds.FLINT_AND_STEEL).add(ItemIds.FIRE_CHARGE);
/*  86 */     tag(ItemTags.SWORDS).add(ItemIds.DIAMOND_SWORD).add(ItemIds.STONE_SWORD).add(ItemIds.GOLDEN_SWORD).add(ItemIds.NETHERITE_SWORD).add(ItemIds.WOODEN_SWORD).add(ItemIds.IRON_SWORD).add(ItemIds.COPPER_SWORD);
/*  87 */     tag(ItemTags.AXES).add(ItemIds.DIAMOND_AXE).add(ItemIds.STONE_AXE).add(ItemIds.GOLDEN_AXE).add(ItemIds.NETHERITE_AXE).add(ItemIds.WOODEN_AXE).add(ItemIds.IRON_AXE).add(ItemIds.COPPER_AXE);
/*  88 */     tag(ItemTags.PICKAXES).add(ItemIds.DIAMOND_PICKAXE).add(ItemIds.STONE_PICKAXE).add(ItemIds.GOLDEN_PICKAXE).add(ItemIds.NETHERITE_PICKAXE).add(ItemIds.WOODEN_PICKAXE).add(ItemIds.IRON_PICKAXE).add(ItemIds.COPPER_PICKAXE);
/*  89 */     tag(ItemTags.SHOVELS).add(ItemIds.DIAMOND_SHOVEL).add(ItemIds.STONE_SHOVEL).add(ItemIds.GOLDEN_SHOVEL).add(ItemIds.NETHERITE_SHOVEL).add(ItemIds.WOODEN_SHOVEL).add(ItemIds.IRON_SHOVEL).add(ItemIds.COPPER_SHOVEL);
/*  90 */     tag(ItemTags.HOES).add(ItemIds.DIAMOND_HOE).add(ItemIds.STONE_HOE).add(ItemIds.GOLDEN_HOE).add(ItemIds.NETHERITE_HOE).add(ItemIds.WOODEN_HOE).add(ItemIds.IRON_HOE).add(ItemIds.COPPER_HOE);
/*  91 */     tag(ItemTags.SPEARS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.DIAMOND_SPEAR, ItemIds.STONE_SPEAR, ItemIds.GOLDEN_SPEAR, ItemIds.NETHERITE_SPEAR, ItemIds.WOODEN_SPEAR, ItemIds.IRON_SPEAR, ItemIds.COPPER_SPEAR });
/*  93 */     tag(ItemTags.BREAKS_DECORATED_POTS).addTag(ItemTags.SWORDS).addTag(ItemTags.AXES).addTag(ItemTags.PICKAXES).addTag(ItemTags.SHOVELS).addTag(ItemTags.HOES).add(ItemIds.TRIDENT).add(ItemIds.MACE);
/*  95 */     tag(ItemTags.SKELETON_PREFERRED_WEAPONS).add(ItemIds.BOW);
/*  96 */     tag(ItemTags.DROWNED_PREFERRED_WEAPONS).add(ItemIds.TRIDENT);
/*  97 */     tag(ItemTags.PIGLIN_PREFERRED_WEAPONS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.CROSSBOW, ItemIds.GOLDEN_SPEAR });
/*  98 */     tag(ItemTags.PILLAGER_PREFERRED_WEAPONS).add(ItemIds.CROSSBOW);
/* 100 */     tag(ItemTags.WITHER_SKELETON_DISLIKED_WEAPONS).add(ItemIds.BOW).add(ItemIds.CROSSBOW);
/* 102 */     tag(ItemTags.DECORATED_POT_SHERDS).add((ResourceKey<Item>[])new ResourceKey[] { 
/* 102 */           ItemIds.ANGLER_POTTERY_SHERD, ItemIds.ARCHER_POTTERY_SHERD, ItemIds.ARMS_UP_POTTERY_SHERD, ItemIds.BLADE_POTTERY_SHERD, ItemIds.BREWER_POTTERY_SHERD, ItemIds.BURN_POTTERY_SHERD, ItemIds.DANGER_POTTERY_SHERD, ItemIds.EXPLORER_POTTERY_SHERD, ItemIds.FRIEND_POTTERY_SHERD, ItemIds.HEART_POTTERY_SHERD, 
/* 102 */           ItemIds.HEARTBREAK_POTTERY_SHERD, ItemIds.HOWL_POTTERY_SHERD, ItemIds.MINER_POTTERY_SHERD, ItemIds.MOURNER_POTTERY_SHERD, ItemIds.PLENTY_POTTERY_SHERD, ItemIds.PRIZE_POTTERY_SHERD, ItemIds.SHEAF_POTTERY_SHERD, ItemIds.SHELTER_POTTERY_SHERD, ItemIds.SKULL_POTTERY_SHERD, ItemIds.SNORT_POTTERY_SHERD, 
/* 102 */           ItemIds.FLOW_POTTERY_SHERD, ItemIds.GUSTER_POTTERY_SHERD, ItemIds.SCRAPE_POTTERY_SHERD });
/* 128 */     tag(ItemTags.DECORATED_POT_INGREDIENTS)
/* 129 */       .add(ItemIds.BRICK)
/* 130 */       .addTag(ItemTags.DECORATED_POT_SHERDS);
/* 132 */     tag(ItemTags.FOOT_ARMOR).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.LEATHER_BOOTS, ItemIds.COPPER_BOOTS, ItemIds.CHAINMAIL_BOOTS, ItemIds.GOLDEN_BOOTS, ItemIds.IRON_BOOTS, ItemIds.DIAMOND_BOOTS, ItemIds.NETHERITE_BOOTS });
/* 133 */     tag(ItemTags.LEG_ARMOR).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.LEATHER_LEGGINGS, ItemIds.COPPER_LEGGINGS, ItemIds.CHAINMAIL_LEGGINGS, ItemIds.GOLDEN_LEGGINGS, ItemIds.IRON_LEGGINGS, ItemIds.DIAMOND_LEGGINGS, ItemIds.NETHERITE_LEGGINGS });
/* 134 */     tag(ItemTags.CHEST_ARMOR).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.LEATHER_CHESTPLATE, ItemIds.COPPER_CHESTPLATE, ItemIds.CHAINMAIL_CHESTPLATE, ItemIds.GOLDEN_CHESTPLATE, ItemIds.IRON_CHESTPLATE, ItemIds.DIAMOND_CHESTPLATE, ItemIds.NETHERITE_CHESTPLATE });
/* 135 */     tag(ItemTags.HEAD_ARMOR).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.LEATHER_HELMET, ItemIds.COPPER_HELMET, ItemIds.CHAINMAIL_HELMET, ItemIds.GOLDEN_HELMET, ItemIds.IRON_HELMET, ItemIds.DIAMOND_HELMET, ItemIds.NETHERITE_HELMET, ItemIds.TURTLE_HELMET });
/* 136 */     tag(ItemTags.SKULLS).add(new BlockItemId[] { BlockItemIds.PLAYER_HEAD, BlockItemIds.CREEPER_HEAD, BlockItemIds.ZOMBIE_HEAD, BlockItemIds.SKELETON_SKULL, BlockItemIds.WITHER_SKELETON_SKULL, BlockItemIds.DRAGON_HEAD, BlockItemIds.PIGLIN_HEAD });
/* 138 */     tag(ItemTags.TRIMMABLE_ARMOR).addTag(ItemTags.FOOT_ARMOR).addTag(ItemTags.LEG_ARMOR).addTag(ItemTags.CHEST_ARMOR).addTag(ItemTags.HEAD_ARMOR);
/* 140 */     tag(ItemTags.TRIM_MATERIALS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.AMETHYST_SHARD, ItemIds.COPPER_INGOT, ItemIds.DIAMOND, ItemIds.EMERALD, ItemIds.GOLD_INGOT, ItemIds.IRON_INGOT, ItemIds.LAPIS_LAZULI, ItemIds.NETHERITE_INGOT, ItemIds.QUARTZ }).add(new BlockItemId[] { BlockItemIds.REDSTONE_DUST }).add(ItemIds.RESIN_BRICK);
/* 142 */     tag(ItemTags.BOOKSHELF_BOOKS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.BOOK, ItemIds.WRITTEN_BOOK, ItemIds.ENCHANTED_BOOK, ItemIds.WRITABLE_BOOK, ItemIds.KNOWLEDGE_BOOK });
/* 143 */     tag(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).add(new BlockItemId[] { BlockItemIds.ZOMBIE_HEAD, BlockItemIds.SKELETON_SKULL, BlockItemIds.CREEPER_HEAD, BlockItemIds.DRAGON_HEAD, BlockItemIds.WITHER_SKELETON_SKULL, BlockItemIds.PIGLIN_HEAD, BlockItemIds.PLAYER_HEAD });
/* 144 */     tag(ItemTags.SNIFFER_FOOD).add(new BlockItemId[] { BlockItemIds.TORCHFLOWER_CROP });
/* 145 */     tag(ItemTags.VILLAGER_PLANTABLE_SEEDS).add(new BlockItemId[] { BlockItemIds.WHEAT_CROP, BlockItemIds.POTATO_CROP, BlockItemIds.CARROT_CROP, BlockItemIds.BEETROOT_CROP, BlockItemIds.TORCHFLOWER_CROP, BlockItemIds.PITCHER_CROP });
/* 146 */     tag(ItemTags.VILLAGER_PICKS_UP).addTag(ItemTags.VILLAGER_PLANTABLE_SEEDS).add(new ResourceKey[] { ItemIds.BREAD, ItemIds.WHEAT, ItemIds.BEETROOT });
/* 147 */     tag(ItemTags.BOOK_CLONING_TARGET).add(ItemIds.WRITABLE_BOOK);
/* 150 */     tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).addTag(ItemTags.FOOT_ARMOR);
/* 151 */     tag(ItemTags.LEG_ARMOR_ENCHANTABLE).addTag(ItemTags.LEG_ARMOR);
/* 152 */     tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).addTag(ItemTags.CHEST_ARMOR);
/* 153 */     tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).addTag(ItemTags.HEAD_ARMOR);
/* 154 */     tag(ItemTags.ARMOR_ENCHANTABLE).addTag(ItemTags.FOOT_ARMOR_ENCHANTABLE).addTag(ItemTags.LEG_ARMOR_ENCHANTABLE).addTag(ItemTags.CHEST_ARMOR_ENCHANTABLE).addTag(ItemTags.HEAD_ARMOR_ENCHANTABLE);
/* 155 */     tag(ItemTags.SWEEPING_ENCHANTABLE).addTag(ItemTags.SWORDS);
/* 156 */     tag(ItemTags.MELEE_WEAPON_ENCHANTABLE).addTag(ItemTags.SWORDS).addTag(ItemTags.SPEARS);
/* 157 */     tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).addTag(ItemTags.MELEE_WEAPON_ENCHANTABLE).add(ItemIds.MACE);
/* 158 */     tag(ItemTags.SHARP_WEAPON_ENCHANTABLE).addTag(ItemTags.MELEE_WEAPON_ENCHANTABLE).addTag(ItemTags.AXES);
/* 159 */     tag(ItemTags.WEAPON_ENCHANTABLE).addTag(ItemTags.SHARP_WEAPON_ENCHANTABLE).add(ItemIds.MACE);
/* 160 */     tag(ItemTags.MACE_ENCHANTABLE).add(ItemIds.MACE);
/* 161 */     tag(ItemTags.MINING_ENCHANTABLE).addTag(ItemTags.AXES).addTag(ItemTags.PICKAXES).addTag(ItemTags.SHOVELS).addTag(ItemTags.HOES).add(ItemIds.SHEARS);
/* 162 */     tag(ItemTags.MINING_LOOT_ENCHANTABLE).addTag(ItemTags.AXES).addTag(ItemTags.PICKAXES).addTag(ItemTags.SHOVELS).addTag(ItemTags.HOES);
/* 163 */     tag(ItemTags.FISHING_ENCHANTABLE).add(ItemIds.FISHING_ROD);
/* 164 */     tag(ItemTags.TRIDENT_ENCHANTABLE).add(ItemIds.TRIDENT);
/* 165 */     tag(ItemTags.LUNGE_ENCHANTABLE).addTag(ItemTags.SPEARS);
/* 166 */     tag(ItemTags.DURABILITY_ENCHANTABLE)
/* 167 */       .addTag(ItemTags.FOOT_ARMOR).addTag(ItemTags.LEG_ARMOR).addTag(ItemTags.CHEST_ARMOR).addTag(ItemTags.HEAD_ARMOR)
/* 168 */       .add(ItemIds.ELYTRA).add(ItemIds.SHIELD)
/* 169 */       .addTag(ItemTags.SWORDS).addTag(ItemTags.AXES).addTag(ItemTags.PICKAXES).addTag(ItemTags.SHOVELS).addTag(ItemTags.HOES)
/* 170 */       .add(ItemIds.BOW).add(ItemIds.CROSSBOW).add(ItemIds.TRIDENT)
/* 171 */       .add(ItemIds.FLINT_AND_STEEL).add(ItemIds.SHEARS).add(ItemIds.BRUSH)
/* 172 */       .add(ItemIds.FISHING_ROD).add(new ResourceKey[] { ItemIds.CARROT_ON_A_STICK, ItemIds.WARPED_FUNGUS_ON_A_STICK }).add(ItemIds.MACE).addTag(ItemTags.SPEARS);
/* 175 */     tag(ItemTags.BOW_ENCHANTABLE).add(ItemIds.BOW);
/* 176 */     tag(ItemTags.EQUIPPABLE_ENCHANTABLE)
/* 177 */       .addTag(ItemTags.FOOT_ARMOR).addTag(ItemTags.LEG_ARMOR).addTag(ItemTags.CHEST_ARMOR).addTag(ItemTags.HEAD_ARMOR)
/* 178 */       .add(ItemIds.ELYTRA)
/* 179 */       .addTag(ItemTags.SKULLS)
/* 180 */       .add(new BlockItemId[] { BlockItemIds.CARVED_PUMPKIN });
/* 182 */     tag(ItemTags.CROSSBOW_ENCHANTABLE).add(ItemIds.CROSSBOW);
/* 183 */     tag(ItemTags.VANISHING_ENCHANTABLE)
/* 184 */       .addTag(ItemTags.DURABILITY_ENCHANTABLE)
/* 185 */       .add(ItemIds.COMPASS)
/* 186 */       .add(new BlockItemId[] { BlockItemIds.CARVED_PUMPKIN }).addTag(ItemTags.SKULLS);
/* 190 */     tag(ItemTags.DYES).addAll(ItemIds.DYE);
/* 192 */     tag(ItemTags.CAULDRON_CAN_REMOVE_DYE).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.LEATHER_HELMET, ItemIds.LEATHER_CHESTPLATE, ItemIds.LEATHER_LEGGINGS, ItemIds.LEATHER_BOOTS, ItemIds.LEATHER_HORSE_ARMOR, ItemIds.WOLF_ARMOR });
/* 194 */     tag(ItemTags.FURNACE_MINECART_FUEL).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.COAL, ItemIds.CHARCOAL });
/* 196 */     tag(ItemTags.MEAT).add((ResourceKey<Item>[])new ResourceKey[] { 
/* 196 */           ItemIds.BEEF, ItemIds.CHICKEN, ItemIds.COOKED_BEEF, ItemIds.COOKED_CHICKEN, ItemIds.COOKED_MUTTON, ItemIds.COOKED_PORKCHOP, ItemIds.COOKED_RABBIT, ItemIds.MUTTON, ItemIds.PORKCHOP, ItemIds.RABBIT, 
/* 196 */           ItemIds.ROTTEN_FLESH });
/* 197 */     tag(ItemTags.WOLF_FOOD).addTag(ItemTags.MEAT).add(new ResourceKey[] { ItemIds.COD, ItemIds.COOKED_COD, ItemIds.SALMON, ItemIds.COOKED_SALMON, ItemIds.TROPICAL_FISH, ItemIds.PUFFERFISH, ItemIds.RABBIT_STEW });
/* 198 */     tag(ItemTags.OCELOT_FOOD).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.COD, ItemIds.SALMON });
/* 199 */     tag(ItemTags.CAT_FOOD).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.COD, ItemIds.SALMON });
/* 200 */     tag(ItemTags.HORSE_FOOD).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.WHEAT, ItemIds.SUGAR }).add(new BlockItemId[] { BlockItemIds.HAY_BLOCK }).add(ItemIds.APPLE).add(new BlockItemId[] { BlockItemIds.CARROT_CROP }).add(new ResourceKey[] { ItemIds.GOLDEN_CARROT, ItemIds.GOLDEN_APPLE, ItemIds.ENCHANTED_GOLDEN_APPLE });
/* 201 */     tag(ItemTags.ZOMBIE_HORSE_FOOD).add(new BlockItemId[] { BlockItemIds.RED_MUSHROOM });
/* 202 */     tag(ItemTags.HORSE_TEMPT_ITEMS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.GOLDEN_CARROT, ItemIds.GOLDEN_APPLE, ItemIds.ENCHANTED_GOLDEN_APPLE });
/* 203 */     tag(ItemTags.HARNESSES).addAll(ItemIds.HARNESS);
/* 204 */     tag(ItemTags.HAPPY_GHAST_FOOD).add(ItemIds.SNOWBALL);
/* 205 */     tag(ItemTags.HAPPY_GHAST_TEMPT_ITEMS).addTag(ItemTags.HAPPY_GHAST_FOOD).addTag(ItemTags.HARNESSES);
/* 206 */     tag(ItemTags.CAMEL_FOOD).add(new BlockItemId[] { BlockItemIds.CACTUS });
/* 207 */     tag(ItemTags.CAMEL_HUSK_FOOD).add(ItemIds.RABBIT_FOOT);
/* 208 */     tag(ItemTags.ARMADILLO_FOOD).add(ItemIds.SPIDER_EYE);
/* 209 */     tag(ItemTags.CHICKEN_FOOD).add(new BlockItemId[] { BlockItemIds.WHEAT_CROP, BlockItemIds.MELON_CROP, BlockItemIds.PUMPKIN_CROP, BlockItemIds.BEETROOT_CROP, BlockItemIds.TORCHFLOWER_CROP, BlockItemIds.PITCHER_CROP });
/* 210 */     tag(ItemTags.FROG_FOOD).add(ItemIds.SLIME_BALL);
/* 211 */     tag(ItemTags.HOGLIN_FOOD).add(new BlockItemId[] { BlockItemIds.CRIMSON_FUNGUS });
/* 212 */     tag(ItemTags.LLAMA_FOOD).add(ItemIds.WHEAT).add(new BlockItemId[] { BlockItemIds.HAY_BLOCK });
/* 213 */     tag(ItemTags.LLAMA_TEMPT_ITEMS).add(new BlockItemId[] { BlockItemIds.HAY_BLOCK });
/* 214 */     tag(ItemTags.NAUTILUS_TAMING_ITEMS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.PUFFERFISH_BUCKET, ItemIds.PUFFERFISH });
/* 215 */     tag(ItemTags.NAUTILUS_BUCKET_FOOD).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.PUFFERFISH_BUCKET, ItemIds.COD_BUCKET, ItemIds.SALMON_BUCKET, ItemIds.TROPICAL_FISH_BUCKET });
/* 216 */     tag(ItemTags.NAUTILUS_FOOD).addTag(ItemTags.FISHES).addTag(ItemTags.NAUTILUS_BUCKET_FOOD);
/* 217 */     tag(ItemTags.PANDA_FOOD).add(new BlockItemId[] { BlockItemIds.BAMBOO });
/* 218 */     tag(ItemTags.PANDA_EATS_FROM_GROUND).addTag(ItemTags.PANDA_FOOD).add(new BlockItemId[] { BlockItemIds.CAKE });
/* 219 */     tag(ItemTags.PIG_FOOD).add(new BlockItemId[] { BlockItemIds.CARROT_CROP, BlockItemIds.POTATO_CROP }).add(ItemIds.BEETROOT);
/* 220 */     tag(ItemTags.RABBIT_FOOD).add(new BlockItemId[] { BlockItemIds.CARROT_CROP }).add(ItemIds.GOLDEN_CARROT).add(new BlockItemId[] { BlockItemIds.DANDELION });
/* 221 */     tag(ItemTags.STRIDER_FOOD).add(new BlockItemId[] { BlockItemIds.WARPED_FUNGUS });
/* 222 */     tag(ItemTags.STRIDER_TEMPT_ITEMS).addTag(ItemTags.STRIDER_FOOD).add(ItemIds.WARPED_FUNGUS_ON_A_STICK);
/* 223 */     tag(ItemTags.TURTLE_FOOD).add(new BlockItemId[] { BlockItemIds.SEAGRASS });
/* 224 */     tag(ItemTags.PARROT_FOOD).add(new BlockItemId[] { BlockItemIds.WHEAT_CROP }).add(new BlockItemId[] { BlockItemIds.MELON_CROP, BlockItemIds.PUMPKIN_CROP, BlockItemIds.BEETROOT_CROP, BlockItemIds.TORCHFLOWER_CROP, BlockItemIds.PITCHER_CROP });
/* 225 */     tag(ItemTags.PARROT_POISONOUS_FOOD).add(ItemIds.COOKIE);
/* 226 */     tag(ItemTags.COW_FOOD).add(ItemIds.WHEAT);
/* 227 */     tag(ItemTags.SHEEP_FOOD).add(ItemIds.WHEAT);
/* 228 */     tag(ItemTags.SULFUR_CUBE_FOOD).add(ItemIds.SLIME_BALL);
/* 229 */     tag(ItemTags.GOAT_FOOD).add(ItemIds.WHEAT);
/* 230 */     tag(ItemTags.MAP_INVISIBILITY_EQUIPMENT).add(new BlockItemId[] { BlockItemIds.CARVED_PUMPKIN });
/* 231 */     tag(ItemTags.GAZE_DISGUISE_EQUIPMENT).add(new BlockItemId[] { BlockItemIds.CARVED_PUMPKIN });
/* 232 */     tag(ItemTags.SHEARABLE_FROM_COPPER_GOLEM).add(new BlockItemId[] { BlockItemIds.POPPY });
/* 233 */     tag(ItemTags.METAL_NUGGETS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.COPPER_NUGGET, ItemIds.IRON_NUGGET, ItemIds.GOLD_NUGGET });
/* 235 */     tag(ItemTags.SULFUR_CUBE_ARCHETYPE_BOUNCY)
/* 236 */       .addTag(ItemTags.PLANKS).add(new BlockItemId[] { BlockItemIds.BAMBOO_MOSAIC }).addTag(ItemTags.LOGS).addTag(ItemTags.BAMBOO_BLOCKS);
/* 239 */     tag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY)
/* 240 */       .add(new BlockItemId[] { BlockItemIds.AMETHYST_BLOCK, BlockItemIds.ANDESITE, BlockItemIds.BASALT, BlockItemIds.BLACKSTONE, BlockItemIds.BRICKS, BlockItemIds.CALCITE }).add(new BlockItemId[] { 
/*     */           BlockItemIds.CHISELED_CINNABAR, BlockItemIds.CHISELED_DEEPSLATE, BlockItemIds.CHISELED_NETHER_BRICKS, BlockItemIds.CHISELED_POLISHED_BLACKSTONE, BlockItemIds.CHISELED_QUARTZ_BLOCK, BlockItemIds.CHISELED_RED_SANDSTONE, BlockItemIds.CHISELED_SANDSTONE, BlockItemIds.CHISELED_STONE_BRICKS, BlockItemIds.CHISELED_SULFUR, BlockItemIds.CHISELED_TUFF, 
/* 242 */           BlockItemIds.CHISELED_TUFF_BRICKS }).add(new BlockItemId[] { BlockItemIds.CINNABAR, BlockItemIds.CINNABAR_BRICKS, BlockItemIds.COBBLED_DEEPSLATE, BlockItemIds.COBBLESTONE }).add(new BlockItemId[] { BlockItemIds.CRACKED_DEEPSLATE_BRICKS, BlockItemIds.CRACKED_DEEPSLATE_TILES, BlockItemIds.CRACKED_NETHER_BRICKS, BlockItemIds.CRACKED_POLISHED_BLACKSTONE_BRICKS, BlockItemIds.CRACKED_STONE_BRICKS }).add(new BlockItemId[] { BlockItemIds.CRIMSON_NYLIUM, BlockItemIds.CRYING_OBSIDIAN, BlockItemIds.CUT_RED_SANDSTONE, BlockItemIds.CUT_SANDSTONE, BlockItemIds.DARK_PRISMARINE }).add(new BlockItemId[] { BlockItemIds.DEEPSLATE, BlockItemIds.DEEPSLATE_BRICKS, BlockItemIds.DEEPSLATE_TILES }).add(new BlockItemId[] { BlockItemIds.DIAMOND_BLOCK, BlockItemIds.DIORITE, BlockItemIds.DRIPSTONE_BLOCK, BlockItemIds.EMERALD_BLOCK, BlockItemIds.END_STONE, BlockItemIds.END_STONE_BRICKS, BlockItemIds.GILDED_BLACKSTONE, BlockItemIds.GLOWSTONE, BlockItemIds.GRANITE, BlockItemIds.LAPIS_BLOCK }).add(new BlockItemId[] { BlockItemIds.MOSSY_COBBLESTONE, BlockItemIds.MOSSY_STONE_BRICKS, BlockItemIds.MUD_BRICKS, BlockItemIds.NETHER_BRICKS, BlockItemIds.NETHERRACK, BlockItemIds.OBSERVER, BlockItemIds.OBSIDIAN }).add(new BlockItemId[] { BlockItemIds.POLISHED_ANDESITE, BlockItemIds.POLISHED_BASALT, BlockItemIds.POLISHED_BLACKSTONE, BlockItemIds.POLISHED_BLACKSTONE_BRICKS, BlockItemIds.POLISHED_CINNABAR, BlockItemIds.POLISHED_DEEPSLATE, BlockItemIds.POLISHED_DIORITE, BlockItemIds.POLISHED_GRANITE, BlockItemIds.POLISHED_SULFUR, BlockItemIds.POLISHED_TUFF }).add(new BlockItemId[] { BlockItemIds.PRISMARINE, BlockItemIds.PRISMARINE_BRICKS, BlockItemIds.PURPUR_BLOCK, BlockItemIds.PURPUR_PILLAR, BlockItemIds.QUARTZ_BLOCK, BlockItemIds.QUARTZ_BRICKS, BlockItemIds.NETHER_QUARTZ_ORE, BlockItemIds.QUARTZ_PILLAR }).add(new BlockItemId[] { BlockItemIds.RED_NETHER_BRICKS, BlockItemIds.RED_SANDSTONE, BlockItemIds.REDSTONE_LAMP, BlockItemIds.SANDSTONE, BlockItemIds.SEA_LANTERN }).add(new BlockItemId[] { BlockItemIds.SMOOTH_BASALT, BlockItemIds.SMOOTH_QUARTZ, BlockItemIds.SMOOTH_RED_SANDSTONE, BlockItemIds.SMOOTH_SANDSTONE, BlockItemIds.SMOOTH_STONE }).add(new BlockItemId[] { BlockItemIds.STONE, BlockItemIds.STONE_BRICKS, BlockItemIds.SULFUR, BlockItemIds.SULFUR_BRICKS, BlockItemIds.TUFF, BlockItemIds.TUFF_BRICKS, BlockItemIds.WARPED_NYLIUM }).addTag(ItemTags.CONCRETE)
/* 254 */       .addTag(ItemTags.COAL_ORES).addTag(ItemTags.LAPIS_ORES).addTag(ItemTags.REDSTONE_ORES).addTag(ItemTags.DIAMOND_ORES).addTag(ItemTags.EMERALD_ORES)
/* 255 */       .addTag(ItemTags.TERRACOTTA)
/* 256 */       .addTag(ItemTags.GLAZED_TERRACOTTA);
/* 258 */     tag(ItemTags.SULFUR_CUBE_ARCHETYPE_REGULAR)
/* 259 */       .addTag(ItemTags.CONCRETE_POWDERS)
/* 260 */       .add(new BlockItemId[] { BlockItemIds.MUD, BlockItemIds.MUDDY_MANGROVE_ROOTS, BlockItemIds.PACKED_MUD }).add(new BlockItemId[] { BlockItemIds.COAL_BLOCK }).add(new BlockItemId[] { BlockItemIds.DIRT, BlockItemIds.COARSE_DIRT, BlockItemIds.ROOTED_DIRT, BlockItemIds.PODZOL, BlockItemIds.GRASS_BLOCK, BlockItemIds.CLAY }).add(new BlockItemId[] { BlockItemIds.BONE_BLOCK });
/* 265 */     tag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_FLAT)
/* 266 */       .add(new BlockItemId[] { BlockItemIds.IRON_BLOCK, BlockItemIds.GOLD_BLOCK, BlockItemIds.RAW_COPPER_BLOCK, BlockItemIds.RAW_GOLD_BLOCK, BlockItemIds.RAW_IRON_BLOCK }).addTag(ItemTags.GOLD_ORES).addTag(ItemTags.IRON_ORES).addTag(ItemTags.COPPER_ORES)
/* 268 */       .add(new BlockItemId[] { BlockItemIds.NETHERITE_BLOCK, BlockItemIds.ANCIENT_DEBRIS }).addAll(toIds(BlockItemIds.COPPER_BLOCK))
/* 270 */       .addAll(toIds(BlockItemIds.COPPER_BULB))
/* 271 */       .addAll(toIds(BlockItemIds.CUT_COPPER))
/* 272 */       .addAll(toIds(BlockItemIds.CHISELED_COPPER));
/* 274 */     tag(ItemTags.SULFUR_CUBE_ARCHETYPE_FAST_FLAT)
/* 275 */       .add(new BlockItemId[] { BlockItemIds.TUBE_CORAL_BLOCK, BlockItemIds.BRAIN_CORAL_BLOCK, BlockItemIds.BUBBLE_CORAL_BLOCK, BlockItemIds.FIRE_CORAL_BLOCK, BlockItemIds.HORN_CORAL_BLOCK }).add(new BlockItemId[] { BlockItemIds.DEAD_TUBE_CORAL_BLOCK, BlockItemIds.DEAD_BRAIN_CORAL_BLOCK, BlockItemIds.DEAD_BUBBLE_CORAL_BLOCK, BlockItemIds.DEAD_FIRE_CORAL_BLOCK, BlockItemIds.DEAD_HORN_CORAL_BLOCK }).add(new BlockItemId[] { BlockItemIds.SPONGE, BlockItemIds.WET_SPONGE, BlockItemIds.DRIED_KELP_BLOCK }).addTag(ItemTags.MOSS_BLOCKS)
/* 279 */       .add(new BlockItemId[] { BlockItemIds.RESIN_BLOCK, BlockItemIds.RESIN_BRICKS, BlockItemIds.CHISELED_RESIN_BRICKS }).add(new BlockItemId[] { BlockItemIds.MELON, BlockItemIds.HAY_BLOCK, BlockItemIds.PUMPKIN, BlockItemIds.CARVED_PUMPKIN, BlockItemIds.JACK_O_LANTERN }).add(new BlockItemId[] { BlockItemIds.OCHRE_FROGLIGHT, BlockItemIds.PEARLESCENT_FROGLIGHT, BlockItemIds.VERDANT_FROGLIGHT });
/* 283 */     tag(ItemTags.SULFUR_CUBE_ARCHETYPE_LIGHT).addTag(ItemTags.WOOL);
/* 284 */     tag(ItemTags.SULFUR_CUBE_ARCHETYPE_FAST_SLIDING).add(new BlockItemId[] { BlockItemIds.BLUE_ICE, BlockItemIds.PACKED_ICE, BlockItemIds.SNOW_BLOCK });
/* 285 */     tag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_SLIDING)
/* 286 */       .add(new BlockItemId[] { BlockItemIds.BROWN_MUSHROOM_BLOCK, BlockItemIds.RED_MUSHROOM_BLOCK, BlockItemIds.MUSHROOM_STEM, BlockItemIds.MYCELIUM }).addTag(ItemTags.WART_BLOCKS).add(new BlockItemId[] { BlockItemIds.SHROOMLIGHT });
/* 288 */     tag(ItemTags.SULFUR_CUBE_ARCHETYPE_STICKY).add(new BlockItemId[] { BlockItemIds.HONEYCOMB_BLOCK });
/* 289 */     tag(ItemTags.SULFUR_CUBE_ARCHETYPE_HIGH_RESISTANCE).add(new BlockItemId[] { BlockItemIds.SOUL_SAND, BlockItemIds.SOUL_SOIL });
/* 290 */     tag(ItemTags.SULFUR_CUBE_ARCHETYPE_EXPLOSIVE).add(new BlockItemId[] { BlockItemIds.TNT });
/* 291 */     tag(ItemTags.SULFUR_CUBE_ARCHETYPE_HOT).add(new BlockItemId[] { BlockItemIds.MAGMA_BLOCK });
/* 293 */     tag(ItemTags.SULFUR_CUBE_SWALLOWABLE).addTag(ItemTags.SULFUR_CUBE_ARCHETYPE_BOUNCY).addTag(ItemTags.SULFUR_CUBE_ARCHETYPE_REGULAR).addTag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_FLAT)
/* 294 */       .addTag(ItemTags.SULFUR_CUBE_ARCHETYPE_FAST_FLAT).addTag(ItemTags.SULFUR_CUBE_ARCHETYPE_LIGHT).addTag(ItemTags.SULFUR_CUBE_ARCHETYPE_FAST_SLIDING).addTag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_SLIDING)
/* 295 */       .addTag(ItemTags.SULFUR_CUBE_ARCHETYPE_STICKY).addTag(ItemTags.SULFUR_CUBE_ARCHETYPE_HIGH_RESISTANCE).addTag(ItemTags.SULFUR_CUBE_ARCHETYPE_EXPLOSIVE).addTag(ItemTags.SULFUR_CUBE_ARCHETYPE_HOT).addTag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY);
/* 297 */     tag(ItemTags.LOOM_DYES).addTag(ItemTags.DYES);
/* 298 */     tag(ItemTags.LOOM_PATTERNS).add((ResourceKey<Item>[])new ResourceKey[] { ItemIds.FLOWER_BANNER_PATTERN, ItemIds.CREEPER_BANNER_PATTERN, ItemIds.SKULL_BANNER_PATTERN, ItemIds.MOJANG_BANNER_PATTERN, ItemIds.GLOBE_BANNER_PATTERN, ItemIds.PIGLIN_BANNER_PATTERN, ItemIds.FLOW_BANNER_PATTERN, ItemIds.GUSTER_BANNER_PATTERN, ItemIds.FIELD_MASONED_BANNER_PATTERN, ItemIds.BORDURE_INDENTED_BANNER_PATTERN });
/* 300 */     tag(ItemTags.CAT_COLLAR_DYES).addTag(ItemTags.DYES);
/* 301 */     tag(ItemTags.WOLF_COLLAR_DYES).addTag(ItemTags.DYES);
/*     */   }
/*     */   
/*     */   private static ColorCollection<ResourceKey<Item>> toIds(ColorCollection<BlockItemId> ids) {
/* 305 */     return ids.map(BlockItemId::item);
/*     */   }
/*     */   
/*     */   private static WeatheringCopperCollection<ResourceKey<Item>> toIds(WeatheringCopperCollection<BlockItemId> ids) {
/* 309 */     return ids.map(BlockItemId::item);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\VanillaItemTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */