/*     */ package net.minecraft.data.tags;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.references.BlockItemId;
/*     */ import net.minecraft.references.BlockItemIds;
/*     */ import net.minecraft.tags.BlockItemTagId;
/*     */ import net.minecraft.tags.BlockItemTags;
/*     */ 
/*     */ public class VanillaBlockItemTagsProvider extends BlockItemTagsProvider {
/*     */   protected VanillaBlockItemTagsProvider(Function<BlockItemTagId, BlockItemTagsProvider.CombinedAppender> tagSupplier) {
/*  12 */     super(tagSupplier);
/*     */   }
/*     */   
/*     */   protected void run() {
/*  19 */     BlockItemId[] smallFlowersInteractingWithBees = { 
/*  19 */         BlockItemIds.DANDELION, BlockItemIds.OPEN_EYEBLOSSOM, BlockItemIds.POPPY, BlockItemIds.BLUE_ORCHID, BlockItemIds.ALLIUM, BlockItemIds.AZURE_BLUET, BlockItemIds.RED_TULIP, BlockItemIds.ORANGE_TULIP, BlockItemIds.WHITE_TULIP, BlockItemIds.PINK_TULIP, 
/*  19 */         BlockItemIds.OXEYE_DAISY, BlockItemIds.CORNFLOWER, BlockItemIds.LILY_OF_THE_VALLEY, BlockItemIds.WITHER_ROSE, BlockItemIds.TORCHFLOWER };
/*  25 */     BlockItemId[] otherFlowers = { 
/*  25 */         BlockItemIds.SUNFLOWER, BlockItemIds.LILAC, BlockItemIds.PEONY, BlockItemIds.ROSE_BUSH, BlockItemIds.PITCHER_PLANT, BlockItemIds.FLOWERING_AZALEA_LEAVES, BlockItemIds.FLOWERING_AZALEA, BlockItemIds.MANGROVE_PROPAGULE, BlockItemIds.CHERRY_LEAVES, BlockItemIds.PINK_PETALS, 
/*  25 */         BlockItemIds.WILDFLOWERS, BlockItemIds.CHORUS_FLOWER, BlockItemIds.SPORE_BLOSSOM, BlockItemIds.CACTUS_FLOWER };
/*  31 */     tag(BlockItemTags.WOOL).addAll(BlockItemIds.WOOL.asList());
/*  32 */     tag(BlockItemTags.PLANKS).add(new BlockItemId[] { 
/*  32 */           BlockItemIds.OAK_PLANKS, BlockItemIds.SPRUCE_PLANKS, BlockItemIds.BIRCH_PLANKS, BlockItemIds.JUNGLE_PLANKS, BlockItemIds.ACACIA_PLANKS, BlockItemIds.DARK_OAK_PLANKS, BlockItemIds.PALE_OAK_PLANKS, BlockItemIds.CRIMSON_PLANKS, BlockItemIds.WARPED_PLANKS, BlockItemIds.MANGROVE_PLANKS, 
/*  32 */           BlockItemIds.BAMBOO_PLANKS, BlockItemIds.CHERRY_PLANKS });
/*  33 */     tag(BlockItemTags.STONE_BRICKS).add(new BlockItemId[] { BlockItemIds.STONE_BRICKS, BlockItemIds.MOSSY_STONE_BRICKS, BlockItemIds.CRACKED_STONE_BRICKS, BlockItemIds.CHISELED_STONE_BRICKS });
/*  34 */     tag(BlockItemTags.WOODEN_BUTTONS).add(new BlockItemId[] { 
/*  34 */           BlockItemIds.OAK_BUTTON, BlockItemIds.SPRUCE_BUTTON, BlockItemIds.BIRCH_BUTTON, BlockItemIds.JUNGLE_BUTTON, BlockItemIds.ACACIA_BUTTON, BlockItemIds.DARK_OAK_BUTTON, BlockItemIds.PALE_OAK_BUTTON, BlockItemIds.CRIMSON_BUTTON, BlockItemIds.WARPED_BUTTON, BlockItemIds.MANGROVE_BUTTON, 
/*  34 */           BlockItemIds.BAMBOO_BUTTON, BlockItemIds.CHERRY_BUTTON });
/*  35 */     tag(BlockItemTags.STONE_BUTTONS).add(new BlockItemId[] { BlockItemIds.STONE_BUTTON, BlockItemIds.POLISHED_BLACKSTONE_BUTTON });
/*  36 */     tag(BlockItemTags.BUTTONS).addTag(BlockItemTags.WOODEN_BUTTONS).addTag(BlockItemTags.STONE_BUTTONS);
/*  37 */     tag(BlockItemTags.WOOL_CARPETS).addAll(BlockItemIds.CARPET.asList());
/*  38 */     tag(BlockItemTags.WOODEN_DOORS).add(new BlockItemId[] { 
/*  38 */           BlockItemIds.OAK_DOOR, BlockItemIds.SPRUCE_DOOR, BlockItemIds.BIRCH_DOOR, BlockItemIds.JUNGLE_DOOR, BlockItemIds.ACACIA_DOOR, BlockItemIds.DARK_OAK_DOOR, BlockItemIds.PALE_OAK_DOOR, BlockItemIds.CRIMSON_DOOR, BlockItemIds.WARPED_DOOR, BlockItemIds.MANGROVE_DOOR, 
/*  38 */           BlockItemIds.BAMBOO_DOOR, BlockItemIds.CHERRY_DOOR });
/*  39 */     tag(BlockItemTags.WOODEN_STAIRS).add(new BlockItemId[] { 
/*  39 */           BlockItemIds.OAK_STAIRS, BlockItemIds.SPRUCE_STAIRS, BlockItemIds.BIRCH_STAIRS, BlockItemIds.JUNGLE_STAIRS, BlockItemIds.ACACIA_STAIRS, BlockItemIds.DARK_OAK_STAIRS, BlockItemIds.PALE_OAK_STAIRS, BlockItemIds.CRIMSON_STAIRS, BlockItemIds.WARPED_STAIRS, BlockItemIds.MANGROVE_STAIRS, 
/*  39 */           BlockItemIds.BAMBOO_STAIRS, BlockItemIds.CHERRY_STAIRS });
/*  40 */     tag(BlockItemTags.WOODEN_SLABS).add(new BlockItemId[] { 
/*  40 */           BlockItemIds.OAK_SLAB, BlockItemIds.SPRUCE_SLAB, BlockItemIds.BIRCH_SLAB, BlockItemIds.JUNGLE_SLAB, BlockItemIds.ACACIA_SLAB, BlockItemIds.DARK_OAK_SLAB, BlockItemIds.PALE_OAK_SLAB, BlockItemIds.CRIMSON_SLAB, BlockItemIds.WARPED_SLAB, BlockItemIds.MANGROVE_SLAB, 
/*  40 */           BlockItemIds.BAMBOO_SLAB, BlockItemIds.CHERRY_SLAB });
/*  41 */     tag(BlockItemTags.WOODEN_FENCES).add(new BlockItemId[] { 
/*  41 */           BlockItemIds.OAK_FENCE, BlockItemIds.ACACIA_FENCE, BlockItemIds.DARK_OAK_FENCE, BlockItemIds.PALE_OAK_FENCE, BlockItemIds.SPRUCE_FENCE, BlockItemIds.BIRCH_FENCE, BlockItemIds.JUNGLE_FENCE, BlockItemIds.CRIMSON_FENCE, BlockItemIds.WARPED_FENCE, BlockItemIds.MANGROVE_FENCE, 
/*  41 */           BlockItemIds.BAMBOO_FENCE, BlockItemIds.CHERRY_FENCE });
/*  42 */     tag(BlockItemTags.FENCE_GATES).add(new BlockItemId[] { 
/*  42 */           BlockItemIds.ACACIA_FENCE_GATE, BlockItemIds.BIRCH_FENCE_GATE, BlockItemIds.DARK_OAK_FENCE_GATE, BlockItemIds.PALE_OAK_FENCE_GATE, BlockItemIds.JUNGLE_FENCE_GATE, BlockItemIds.OAK_FENCE_GATE, BlockItemIds.SPRUCE_FENCE_GATE, BlockItemIds.CRIMSON_FENCE_GATE, BlockItemIds.WARPED_FENCE_GATE, BlockItemIds.MANGROVE_FENCE_GATE, 
/*  42 */           BlockItemIds.BAMBOO_FENCE_GATE, BlockItemIds.CHERRY_FENCE_GATE });
/*  43 */     tag(BlockItemTags.WOODEN_PRESSURE_PLATES).add(new BlockItemId[] { 
/*  43 */           BlockItemIds.OAK_PRESSURE_PLATE, BlockItemIds.SPRUCE_PRESSURE_PLATE, BlockItemIds.BIRCH_PRESSURE_PLATE, BlockItemIds.JUNGLE_PRESSURE_PLATE, BlockItemIds.ACACIA_PRESSURE_PLATE, BlockItemIds.DARK_OAK_PRESSURE_PLATE, BlockItemIds.PALE_OAK_PRESSURE_PLATE, BlockItemIds.CRIMSON_PRESSURE_PLATE, BlockItemIds.WARPED_PRESSURE_PLATE, BlockItemIds.MANGROVE_PRESSURE_PLATE, 
/*  43 */           BlockItemIds.BAMBOO_PRESSURE_PLATE, BlockItemIds.CHERRY_PRESSURE_PLATE });
/*  44 */     tag(BlockItemTags.DOORS).addTag(BlockItemTags.WOODEN_DOORS).addAll(BlockItemIds.COPPER_DOOR.asList()).add(new BlockItemId[] { BlockItemIds.IRON_DOOR });
/*  45 */     tag(BlockItemTags.SAPLINGS).add(new BlockItemId[] { 
/*  45 */           BlockItemIds.OAK_SAPLING, BlockItemIds.SPRUCE_SAPLING, BlockItemIds.BIRCH_SAPLING, BlockItemIds.JUNGLE_SAPLING, BlockItemIds.ACACIA_SAPLING, BlockItemIds.DARK_OAK_SAPLING, BlockItemIds.PALE_OAK_SAPLING, BlockItemIds.AZALEA, BlockItemIds.FLOWERING_AZALEA, BlockItemIds.MANGROVE_PROPAGULE, 
/*  45 */           BlockItemIds.CHERRY_SAPLING });
/*  46 */     tag(BlockItemTags.BAMBOO_BLOCKS).add(new BlockItemId[] { BlockItemIds.BAMBOO_BLOCK, BlockItemIds.STRIPPED_BAMBOO_BLOCK });
/*  47 */     tag(BlockItemTags.OAK_LOGS).add(new BlockItemId[] { BlockItemIds.OAK_LOG, BlockItemIds.OAK_WOOD, BlockItemIds.STRIPPED_OAK_LOG, BlockItemIds.STRIPPED_OAK_WOOD });
/*  48 */     tag(BlockItemTags.DARK_OAK_LOGS).add(new BlockItemId[] { BlockItemIds.DARK_OAK_LOG, BlockItemIds.DARK_OAK_WOOD, BlockItemIds.STRIPPED_DARK_OAK_LOG, BlockItemIds.STRIPPED_DARK_OAK_WOOD });
/*  49 */     tag(BlockItemTags.PALE_OAK_LOGS).add(new BlockItemId[] { BlockItemIds.PALE_OAK_LOG, BlockItemIds.PALE_OAK_WOOD, BlockItemIds.STRIPPED_PALE_OAK_LOG, BlockItemIds.STRIPPED_PALE_OAK_WOOD });
/*  50 */     tag(BlockItemTags.BIRCH_LOGS).add(new BlockItemId[] { BlockItemIds.BIRCH_LOG, BlockItemIds.BIRCH_WOOD, BlockItemIds.STRIPPED_BIRCH_LOG, BlockItemIds.STRIPPED_BIRCH_WOOD });
/*  51 */     tag(BlockItemTags.ACACIA_LOGS).add(new BlockItemId[] { BlockItemIds.ACACIA_LOG, BlockItemIds.ACACIA_WOOD, BlockItemIds.STRIPPED_ACACIA_LOG, BlockItemIds.STRIPPED_ACACIA_WOOD });
/*  52 */     tag(BlockItemTags.SPRUCE_LOGS).add(new BlockItemId[] { BlockItemIds.SPRUCE_LOG, BlockItemIds.SPRUCE_WOOD, BlockItemIds.STRIPPED_SPRUCE_LOG, BlockItemIds.STRIPPED_SPRUCE_WOOD });
/*  53 */     tag(BlockItemTags.MANGROVE_LOGS).add(new BlockItemId[] { BlockItemIds.MANGROVE_LOG, BlockItemIds.MANGROVE_WOOD, BlockItemIds.STRIPPED_MANGROVE_LOG, BlockItemIds.STRIPPED_MANGROVE_WOOD });
/*  54 */     tag(BlockItemTags.JUNGLE_LOGS).add(new BlockItemId[] { BlockItemIds.JUNGLE_LOG, BlockItemIds.JUNGLE_WOOD, BlockItemIds.STRIPPED_JUNGLE_LOG, BlockItemIds.STRIPPED_JUNGLE_WOOD });
/*  55 */     tag(BlockItemTags.CHERRY_LOGS).add(new BlockItemId[] { BlockItemIds.CHERRY_LOG, BlockItemIds.CHERRY_WOOD, BlockItemIds.STRIPPED_CHERRY_LOG, BlockItemIds.STRIPPED_CHERRY_WOOD });
/*  56 */     tag(BlockItemTags.CRIMSON_STEMS).add(new BlockItemId[] { BlockItemIds.CRIMSON_STEM, BlockItemIds.STRIPPED_CRIMSON_STEM, BlockItemIds.CRIMSON_HYPHAE, BlockItemIds.STRIPPED_CRIMSON_HYPHAE });
/*  57 */     tag(BlockItemTags.WARPED_STEMS).add(new BlockItemId[] { BlockItemIds.WARPED_STEM, BlockItemIds.STRIPPED_WARPED_STEM, BlockItemIds.WARPED_HYPHAE, BlockItemIds.STRIPPED_WARPED_HYPHAE });
/*  58 */     tag(BlockItemTags.WART_BLOCKS).add(new BlockItemId[] { BlockItemIds.NETHER_WART_BLOCK, BlockItemIds.WARPED_WART_BLOCK });
/*  59 */     tag(BlockItemTags.LOGS_THAT_BURN).addTag(BlockItemTags.DARK_OAK_LOGS).addTag(BlockItemTags.PALE_OAK_LOGS).addTag(BlockItemTags.OAK_LOGS).addTag(BlockItemTags.ACACIA_LOGS).addTag(BlockItemTags.BIRCH_LOGS).addTag(BlockItemTags.JUNGLE_LOGS).addTag(BlockItemTags.SPRUCE_LOGS).addTag(BlockItemTags.MANGROVE_LOGS).addTag(BlockItemTags.CHERRY_LOGS);
/*  60 */     tag(BlockItemTags.LOGS).addTag(BlockItemTags.LOGS_THAT_BURN).addTag(BlockItemTags.CRIMSON_STEMS).addTag(BlockItemTags.WARPED_STEMS);
/*  61 */     tag(BlockItemTags.SAND).add(new BlockItemId[] { BlockItemIds.SAND, BlockItemIds.RED_SAND, BlockItemIds.SUSPICIOUS_SAND });
/*  62 */     tag(BlockItemTags.SMELTS_TO_GLASS).add(new BlockItemId[] { BlockItemIds.SAND, BlockItemIds.RED_SAND });
/*  63 */     tag(BlockItemTags.SLABS).addTag(BlockItemTags.WOODEN_SLABS).add(new BlockItemId[] { BlockItemIds.BAMBOO_MOSAIC_SLAB }).add(new BlockItemId[] { 
/*  63 */           BlockItemIds.STONE_SLAB, BlockItemIds.SMOOTH_STONE_SLAB, BlockItemIds.STONE_BRICK_SLAB, BlockItemIds.SANDSTONE_SLAB, BlockItemIds.PURPUR_SLAB, BlockItemIds.QUARTZ_SLAB, BlockItemIds.RED_SANDSTONE_SLAB, BlockItemIds.BRICK_SLAB, BlockItemIds.COBBLESTONE_SLAB, BlockItemIds.NETHER_BRICK_SLAB, 
/*  63 */           BlockItemIds.PETRIFIED_OAK_SLAB, BlockItemIds.PRISMARINE_SLAB, BlockItemIds.PRISMARINE_BRICK_SLAB, BlockItemIds.DARK_PRISMARINE_SLAB, BlockItemIds.POLISHED_GRANITE_SLAB, BlockItemIds.SMOOTH_RED_SANDSTONE_SLAB, BlockItemIds.MOSSY_STONE_BRICK_SLAB, BlockItemIds.POLISHED_DIORITE_SLAB, BlockItemIds.MOSSY_COBBLESTONE_SLAB, BlockItemIds.END_STONE_BRICK_SLAB, 
/*  63 */           BlockItemIds.SMOOTH_SANDSTONE_SLAB, BlockItemIds.SMOOTH_QUARTZ_SLAB, BlockItemIds.GRANITE_SLAB, BlockItemIds.ANDESITE_SLAB, BlockItemIds.RED_NETHER_BRICK_SLAB, BlockItemIds.POLISHED_ANDESITE_SLAB, BlockItemIds.DIORITE_SLAB, BlockItemIds.CUT_SANDSTONE_SLAB, BlockItemIds.CUT_RED_SANDSTONE_SLAB, BlockItemIds.BLACKSTONE_SLAB, 
/*  63 */           BlockItemIds.POLISHED_BLACKSTONE_BRICK_SLAB, BlockItemIds.POLISHED_BLACKSTONE_SLAB, BlockItemIds.COBBLED_DEEPSLATE_SLAB, BlockItemIds.POLISHED_DEEPSLATE_SLAB, BlockItemIds.DEEPSLATE_TILE_SLAB, BlockItemIds.DEEPSLATE_BRICK_SLAB, BlockItemIds.MUD_BRICK_SLAB, BlockItemIds.TUFF_SLAB, BlockItemIds.POLISHED_TUFF_SLAB, BlockItemIds.TUFF_BRICK_SLAB, 
/*  63 */           BlockItemIds.RESIN_BRICK_SLAB, BlockItemIds.CINNABAR_SLAB, BlockItemIds.POLISHED_CINNABAR_SLAB, BlockItemIds.CINNABAR_BRICK_SLAB, BlockItemIds.SULFUR_SLAB, BlockItemIds.POLISHED_SULFUR_SLAB, BlockItemIds.SULFUR_BRICK_SLAB }).addAll(BlockItemIds.CUT_COPPER_SLAB.asList());
/*  64 */     tag(BlockItemTags.WALLS).add(new BlockItemId[] { 
/*  64 */           BlockItemIds.COBBLESTONE_WALL, BlockItemIds.MOSSY_COBBLESTONE_WALL, BlockItemIds.BRICK_WALL, BlockItemIds.PRISMARINE_WALL, BlockItemIds.RED_SANDSTONE_WALL, BlockItemIds.MOSSY_STONE_BRICK_WALL, BlockItemIds.GRANITE_WALL, BlockItemIds.STONE_BRICK_WALL, BlockItemIds.NETHER_BRICK_WALL, BlockItemIds.ANDESITE_WALL, 
/*  64 */           BlockItemIds.RED_NETHER_BRICK_WALL, BlockItemIds.SANDSTONE_WALL, BlockItemIds.END_STONE_BRICK_WALL, BlockItemIds.DIORITE_WALL, BlockItemIds.BLACKSTONE_WALL, BlockItemIds.POLISHED_BLACKSTONE_BRICK_WALL, BlockItemIds.POLISHED_BLACKSTONE_WALL, BlockItemIds.COBBLED_DEEPSLATE_WALL, BlockItemIds.POLISHED_DEEPSLATE_WALL, BlockItemIds.DEEPSLATE_TILE_WALL, 
/*  64 */           BlockItemIds.DEEPSLATE_BRICK_WALL, BlockItemIds.MUD_BRICK_WALL, BlockItemIds.TUFF_WALL, BlockItemIds.POLISHED_TUFF_WALL, BlockItemIds.TUFF_BRICK_WALL, BlockItemIds.RESIN_BRICK_WALL, BlockItemIds.CINNABAR_WALL, BlockItemIds.POLISHED_CINNABAR_WALL, BlockItemIds.CINNABAR_BRICK_WALL, BlockItemIds.SULFUR_WALL, 
/*  64 */           BlockItemIds.POLISHED_SULFUR_WALL, BlockItemIds.SULFUR_BRICK_WALL });
/*  65 */     tag(BlockItemTags.STAIRS).addTag(BlockItemTags.WOODEN_STAIRS).add(new BlockItemId[] { BlockItemIds.BAMBOO_MOSAIC_STAIRS }).add(new BlockItemId[] { 
/*  65 */           BlockItemIds.COBBLESTONE_STAIRS, BlockItemIds.SANDSTONE_STAIRS, BlockItemIds.NETHER_BRICK_STAIRS, BlockItemIds.STONE_BRICK_STAIRS, BlockItemIds.BRICK_STAIRS, BlockItemIds.PURPUR_STAIRS, BlockItemIds.QUARTZ_STAIRS, BlockItemIds.RED_SANDSTONE_STAIRS, BlockItemIds.PRISMARINE_BRICK_STAIRS, BlockItemIds.PRISMARINE_STAIRS, 
/*  65 */           BlockItemIds.DARK_PRISMARINE_STAIRS, BlockItemIds.POLISHED_GRANITE_STAIRS, BlockItemIds.SMOOTH_RED_SANDSTONE_STAIRS, BlockItemIds.MOSSY_STONE_BRICK_STAIRS, BlockItemIds.POLISHED_DIORITE_STAIRS, BlockItemIds.MOSSY_COBBLESTONE_STAIRS, BlockItemIds.END_STONE_BRICK_STAIRS, BlockItemIds.STONE_STAIRS, BlockItemIds.SMOOTH_SANDSTONE_STAIRS, BlockItemIds.SMOOTH_QUARTZ_STAIRS, 
/*  65 */           BlockItemIds.GRANITE_STAIRS, BlockItemIds.ANDESITE_STAIRS, BlockItemIds.RED_NETHER_BRICK_STAIRS, BlockItemIds.POLISHED_ANDESITE_STAIRS, BlockItemIds.DIORITE_STAIRS, BlockItemIds.BLACKSTONE_STAIRS, BlockItemIds.POLISHED_BLACKSTONE_BRICK_STAIRS, BlockItemIds.POLISHED_BLACKSTONE_STAIRS, BlockItemIds.COBBLED_DEEPSLATE_STAIRS, BlockItemIds.POLISHED_DEEPSLATE_STAIRS, 
/*  65 */           BlockItemIds.DEEPSLATE_TILE_STAIRS, BlockItemIds.DEEPSLATE_BRICK_STAIRS, BlockItemIds.MUD_BRICK_STAIRS, BlockItemIds.TUFF_STAIRS, BlockItemIds.POLISHED_TUFF_STAIRS, BlockItemIds.TUFF_BRICK_STAIRS, BlockItemIds.RESIN_BRICK_STAIRS, BlockItemIds.CINNABAR_STAIRS, BlockItemIds.POLISHED_CINNABAR_STAIRS, BlockItemIds.CINNABAR_BRICK_STAIRS, 
/*  65 */           BlockItemIds.SULFUR_STAIRS, BlockItemIds.POLISHED_SULFUR_STAIRS, BlockItemIds.SULFUR_BRICK_STAIRS }).addAll(BlockItemIds.CUT_COPPER_STAIRS.asList());
/*  66 */     tag(BlockItemTags.ANVIL).add(new BlockItemId[] { BlockItemIds.ANVIL, BlockItemIds.CHIPPED_ANVIL, BlockItemIds.DAMAGED_ANVIL });
/*  67 */     tag(BlockItemTags.RAILS).add(new BlockItemId[] { BlockItemIds.RAIL, BlockItemIds.POWERED_RAIL, BlockItemIds.DETECTOR_RAIL, BlockItemIds.ACTIVATOR_RAIL });
/*  68 */     tag(BlockItemTags.LEAVES).add(new BlockItemId[] { 
/*  68 */           BlockItemIds.JUNGLE_LEAVES, BlockItemIds.OAK_LEAVES, BlockItemIds.SPRUCE_LEAVES, BlockItemIds.PALE_OAK_LEAVES, BlockItemIds.DARK_OAK_LEAVES, BlockItemIds.ACACIA_LEAVES, BlockItemIds.BIRCH_LEAVES, BlockItemIds.AZALEA_LEAVES, BlockItemIds.FLOWERING_AZALEA_LEAVES, BlockItemIds.MANGROVE_LEAVES, 
/*  68 */           BlockItemIds.CHERRY_LEAVES });
/*  69 */     tag(BlockItemTags.WOODEN_TRAPDOORS).add(new BlockItemId[] { 
/*  69 */           BlockItemIds.ACACIA_TRAPDOOR, BlockItemIds.BIRCH_TRAPDOOR, BlockItemIds.DARK_OAK_TRAPDOOR, BlockItemIds.PALE_OAK_TRAPDOOR, BlockItemIds.JUNGLE_TRAPDOOR, BlockItemIds.OAK_TRAPDOOR, BlockItemIds.SPRUCE_TRAPDOOR, BlockItemIds.CRIMSON_TRAPDOOR, BlockItemIds.WARPED_TRAPDOOR, BlockItemIds.MANGROVE_TRAPDOOR, 
/*  69 */           BlockItemIds.BAMBOO_TRAPDOOR, BlockItemIds.CHERRY_TRAPDOOR });
/*  70 */     tag(BlockItemTags.TRAPDOORS).addTag(BlockItemTags.WOODEN_TRAPDOORS).add(new BlockItemId[] { BlockItemIds.IRON_TRAPDOOR }).addAll(BlockItemIds.COPPER_TRAPDOOR.asList());
/*  71 */     tag(BlockItemTags.SMALL_FLOWERS).add(smallFlowersInteractingWithBees).add(new BlockItemId[] { BlockItemIds.CLOSED_EYEBLOSSOM, BlockItemIds.GOLDEN_DANDELION });
/*  72 */     tag(BlockItemTags.FLOWERS).addTag(BlockItemTags.SMALL_FLOWERS).add(otherFlowers);
/*  73 */     tag(BlockItemTags.BEDS).addAll(BlockItemIds.BED.asList());
/*  74 */     tag(BlockItemTags.FENCES).addTag(BlockItemTags.WOODEN_FENCES).add(new BlockItemId[] { BlockItemIds.NETHER_BRICK_FENCE });
/*  75 */     tag(BlockItemTags.SOUL_FIRE_BASE_BLOCKS).add(new BlockItemId[] { BlockItemIds.SOUL_SAND, BlockItemIds.SOUL_SOIL });
/*  76 */     tag(BlockItemTags.CANDLES).add(new BlockItemId[] { BlockItemIds.CANDLE }).addAll(BlockItemIds.DYED_CANDLE.asList());
/*  77 */     tag(BlockItemTags.DAMPENS_VIBRATIONS).addTag(BlockItemTags.WOOL).addTag(BlockItemTags.WOOL_CARPETS);
/*  78 */     tag(BlockItemTags.GOLD_ORES).add(new BlockItemId[] { BlockItemIds.GOLD_ORE, BlockItemIds.NETHER_GOLD_ORE, BlockItemIds.DEEPSLATE_GOLD_ORE });
/*  79 */     tag(BlockItemTags.IRON_ORES).add(new BlockItemId[] { BlockItemIds.IRON_ORE, BlockItemIds.DEEPSLATE_IRON_ORE });
/*  80 */     tag(BlockItemTags.DIAMOND_ORES).add(new BlockItemId[] { BlockItemIds.DIAMOND_ORE, BlockItemIds.DEEPSLATE_DIAMOND_ORE });
/*  81 */     tag(BlockItemTags.REDSTONE_ORES).add(new BlockItemId[] { BlockItemIds.REDSTONE_ORE, BlockItemIds.DEEPSLATE_REDSTONE_ORE });
/*  82 */     tag(BlockItemTags.LAPIS_ORES).add(new BlockItemId[] { BlockItemIds.LAPIS_ORE, BlockItemIds.DEEPSLATE_LAPIS_ORE });
/*  83 */     tag(BlockItemTags.COAL_ORES).add(new BlockItemId[] { BlockItemIds.COAL_ORE, BlockItemIds.DEEPSLATE_COAL_ORE });
/*  84 */     tag(BlockItemTags.EMERALD_ORES).add(new BlockItemId[] { BlockItemIds.EMERALD_ORE, BlockItemIds.DEEPSLATE_EMERALD_ORE });
/*  85 */     tag(BlockItemTags.COPPER_ORES).add(new BlockItemId[] { BlockItemIds.COPPER_ORE, BlockItemIds.DEEPSLATE_COPPER_ORE });
/*  86 */     tag(BlockItemTags.DIRT).add(new BlockItemId[] { BlockItemIds.DIRT, BlockItemIds.COARSE_DIRT, BlockItemIds.ROOTED_DIRT });
/*  87 */     tag(BlockItemTags.MUD).add(new BlockItemId[] { BlockItemIds.MUD, BlockItemIds.MUDDY_MANGROVE_ROOTS });
/*  88 */     tag(BlockItemTags.MOSS_BLOCKS).add(new BlockItemId[] { BlockItemIds.MOSS_BLOCK, BlockItemIds.PALE_MOSS_BLOCK });
/*  89 */     tag(BlockItemTags.GRASS_BLOCKS).add(new BlockItemId[] { BlockItemIds.GRASS_BLOCK, BlockItemIds.PODZOL, BlockItemIds.MYCELIUM });
/*  90 */     tag(BlockItemTags.TERRACOTTA).add(new BlockItemId[] { BlockItemIds.TERRACOTTA }).addAll(BlockItemIds.DYED_TERRACOTTA.asList());
/*  91 */     tag(BlockItemTags.GLAZED_TERRACOTTA).addAll(BlockItemIds.GLAZED_TERRACOTTA.asList());
/*  92 */     tag(BlockItemTags.CONCRETE).addAll(BlockItemIds.CONCRETE.asList());
/*  93 */     tag(BlockItemTags.CONCRETE_POWDERS).addAll(BlockItemIds.CONCRETE_POWDER.asList());
/*  94 */     tag(BlockItemTags.COMPLETES_FIND_TREE_TUTORIAL).addTag(BlockItemTags.LOGS).addTag(BlockItemTags.LEAVES).addTag(BlockItemTags.WART_BLOCKS);
/*  95 */     tag(BlockItemTags.SHULKER_BOXES).add(new BlockItemId[] { BlockItemIds.SHULKER_BOX }).addAll(BlockItemIds.DYED_SHULKER_BOX.asList());
/*  96 */     tag(BlockItemTags.COPPER_CHESTS).addAll(BlockItemIds.COPPER_CHEST.asList());
/*  97 */     tag(BlockItemTags.LIGHTNING_RODS).addAll(BlockItemIds.LIGHTNING_ROD.asList());
/*  98 */     tag(BlockItemTags.COPPER_GOLEM_STATUES).addAll(BlockItemIds.COPPER_GOLEM_STATUE.asList());
/*  99 */     tag(BlockItemTags.COPPER).addAll(BlockItemIds.COPPER_BLOCK.asList());
/* 100 */     tag(BlockItemTags.CHAINS).add(new BlockItemId[] { BlockItemIds.IRON_CHAIN }).addAll(BlockItemIds.COPPER_CHAIN.asList());
/* 101 */     tag(BlockItemTags.WOODEN_SHELVES).add(new BlockItemId[] { 
/* 101 */           BlockItemIds.ACACIA_SHELF, BlockItemIds.BAMBOO_SHELF, BlockItemIds.BIRCH_SHELF, BlockItemIds.CHERRY_SHELF, BlockItemIds.CRIMSON_SHELF, BlockItemIds.DARK_OAK_SHELF, BlockItemIds.JUNGLE_SHELF, BlockItemIds.MANGROVE_SHELF, BlockItemIds.OAK_SHELF, BlockItemIds.PALE_OAK_SHELF, 
/* 101 */           BlockItemIds.SPRUCE_SHELF, BlockItemIds.WARPED_SHELF });
/* 102 */     tag(BlockItemTags.LANTERNS).add(new BlockItemId[] { BlockItemIds.LANTERN, BlockItemIds.SOUL_LANTERN }).addAll(BlockItemIds.COPPER_LANTERN.asList());
/* 103 */     tag(BlockItemTags.BARS).add(new BlockItemId[] { BlockItemIds.IRON_BARS }).addAll(BlockItemIds.COPPER_BARS.asList());
/* 105 */     tag(BlockItemTags.SIGNS).add(new BlockItemId[] { 
/* 105 */           BlockItemIds.OAK_SIGN, BlockItemIds.SPRUCE_SIGN, BlockItemIds.BIRCH_SIGN, BlockItemIds.ACACIA_SIGN, BlockItemIds.JUNGLE_SIGN, BlockItemIds.DARK_OAK_SIGN, BlockItemIds.PALE_OAK_SIGN, BlockItemIds.CRIMSON_SIGN, BlockItemIds.WARPED_SIGN, BlockItemIds.MANGROVE_SIGN, 
/* 105 */           BlockItemIds.BAMBOO_SIGN, BlockItemIds.CHERRY_SIGN });
/* 106 */     tag(BlockItemTags.HANGING_SIGNS).add(new BlockItemId[] { 
/* 106 */           BlockItemIds.OAK_HANGING_SIGN, BlockItemIds.SPRUCE_HANGING_SIGN, BlockItemIds.BIRCH_HANGING_SIGN, BlockItemIds.ACACIA_HANGING_SIGN, BlockItemIds.CHERRY_HANGING_SIGN, BlockItemIds.JUNGLE_HANGING_SIGN, BlockItemIds.DARK_OAK_HANGING_SIGN, BlockItemIds.PALE_OAK_HANGING_SIGN, BlockItemIds.CRIMSON_HANGING_SIGN, BlockItemIds.WARPED_HANGING_SIGN, 
/* 106 */           BlockItemIds.MANGROVE_HANGING_SIGN, BlockItemIds.BAMBOO_HANGING_SIGN });
/* 107 */     tag(BlockItemTags.BEE_FOOD).add(smallFlowersInteractingWithBees).add(otherFlowers);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\VanillaBlockItemTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */