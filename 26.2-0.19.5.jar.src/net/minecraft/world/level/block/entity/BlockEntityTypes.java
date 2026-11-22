/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
/*    */ import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class BlockEntityTypes {
/* 22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 24 */   public static final BlockEntityType<FurnaceBlockEntity> FURNACE = register(BlockEntityTypeIds.FURNACE, FurnaceBlockEntity::new, new Block[] { Blocks.FURNACE });
/*    */   
/* 25 */   public static final BlockEntityType<ChestBlockEntity> CHEST = register(BlockEntityTypeIds.CHEST, ChestBlockEntity::new, Util.copyAndAdd(Blocks.COPPER_CHEST.asList(), Blocks.CHEST));
/*    */   
/* 26 */   public static final BlockEntityType<TrappedChestBlockEntity> TRAPPED_CHEST = register(BlockEntityTypeIds.TRAPPED_CHEST, TrappedChestBlockEntity::new, new Block[] { Blocks.TRAPPED_CHEST });
/*    */   
/* 27 */   public static final BlockEntityType<EnderChestBlockEntity> ENDER_CHEST = register(BlockEntityTypeIds.ENDER_CHEST, EnderChestBlockEntity::new, new Block[] { Blocks.ENDER_CHEST });
/*    */   
/* 28 */   public static final BlockEntityType<JukeboxBlockEntity> JUKEBOX = register(BlockEntityTypeIds.JUKEBOX, JukeboxBlockEntity::new, new Block[] { Blocks.JUKEBOX });
/*    */   
/* 29 */   public static final BlockEntityType<DispenserBlockEntity> DISPENSER = register(BlockEntityTypeIds.DISPENSER, DispenserBlockEntity::new, new Block[] { Blocks.DISPENSER });
/*    */   
/* 30 */   public static final BlockEntityType<DropperBlockEntity> DROPPER = register(BlockEntityTypeIds.DROPPER, DropperBlockEntity::new, new Block[] { Blocks.DROPPER });
/*    */   
/* 31 */   public static final BlockEntityType<SignBlockEntity> SIGN = register(BlockEntityTypeIds.SIGN, SignBlockEntity::new, new Block[] { 
/* 31 */         Blocks.OAK_SIGN, Blocks.SPRUCE_SIGN, Blocks.BIRCH_SIGN, Blocks.ACACIA_SIGN, Blocks.CHERRY_SIGN, Blocks.JUNGLE_SIGN, Blocks.DARK_OAK_SIGN, Blocks.PALE_OAK_SIGN, Blocks.OAK_WALL_SIGN, Blocks.SPRUCE_WALL_SIGN, 
/* 31 */         Blocks.BIRCH_WALL_SIGN, Blocks.ACACIA_WALL_SIGN, Blocks.CHERRY_WALL_SIGN, Blocks.JUNGLE_WALL_SIGN, Blocks.DARK_OAK_WALL_SIGN, Blocks.PALE_OAK_WALL_SIGN, Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN, Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN, 
/* 31 */         Blocks.MANGROVE_SIGN, Blocks.MANGROVE_WALL_SIGN, Blocks.BAMBOO_SIGN, Blocks.BAMBOO_WALL_SIGN });
/*    */   
/* 32 */   public static final BlockEntityType<HangingSignBlockEntity> HANGING_SIGN = register(BlockEntityTypeIds.HANGING_SIGN, HangingSignBlockEntity::new, new Block[] { 
/* 32 */         Blocks.OAK_HANGING_SIGN, Blocks.SPRUCE_HANGING_SIGN, Blocks.BIRCH_HANGING_SIGN, Blocks.ACACIA_HANGING_SIGN, Blocks.CHERRY_HANGING_SIGN, Blocks.JUNGLE_HANGING_SIGN, Blocks.DARK_OAK_HANGING_SIGN, Blocks.PALE_OAK_HANGING_SIGN, Blocks.CRIMSON_HANGING_SIGN, Blocks.WARPED_HANGING_SIGN, 
/* 32 */         Blocks.MANGROVE_HANGING_SIGN, Blocks.BAMBOO_HANGING_SIGN, Blocks.OAK_WALL_HANGING_SIGN, Blocks.SPRUCE_WALL_HANGING_SIGN, Blocks.BIRCH_WALL_HANGING_SIGN, Blocks.ACACIA_WALL_HANGING_SIGN, Blocks.CHERRY_WALL_HANGING_SIGN, Blocks.JUNGLE_WALL_HANGING_SIGN, Blocks.DARK_OAK_WALL_HANGING_SIGN, Blocks.PALE_OAK_WALL_HANGING_SIGN, 
/* 32 */         Blocks.CRIMSON_WALL_HANGING_SIGN, Blocks.WARPED_WALL_HANGING_SIGN, Blocks.MANGROVE_WALL_HANGING_SIGN, Blocks.BAMBOO_WALL_HANGING_SIGN });
/*    */   
/* 33 */   public static final BlockEntityType<SpawnerBlockEntity> MOB_SPAWNER = register(BlockEntityTypeIds.MOB_SPAWNER, SpawnerBlockEntity::new, new Block[] { Blocks.SPAWNER });
/*    */   
/* 34 */   public static final BlockEntityType<CreakingHeartBlockEntity> CREAKING_HEART = register(BlockEntityTypeIds.CREAKING_HEART, CreakingHeartBlockEntity::new, new Block[] { Blocks.CREAKING_HEART });
/*    */   
/* 35 */   public static final BlockEntityType<PistonMovingBlockEntity> PISTON = register(BlockEntityTypeIds.PISTON, PistonMovingBlockEntity::new, new Block[] { Blocks.MOVING_PISTON });
/*    */   
/* 36 */   public static final BlockEntityType<BrewingStandBlockEntity> BREWING_STAND = register(BlockEntityTypeIds.BREWING_STAND, BrewingStandBlockEntity::new, new Block[] { Blocks.BREWING_STAND });
/*    */   
/* 37 */   public static final BlockEntityType<EnchantingTableBlockEntity> ENCHANTING_TABLE = register(BlockEntityTypeIds.ENCHANTING_TABLE, EnchantingTableBlockEntity::new, new Block[] { Blocks.ENCHANTING_TABLE });
/*    */   
/* 38 */   public static final BlockEntityType<TheEndPortalBlockEntity> END_PORTAL = register(BlockEntityTypeIds.END_PORTAL, TheEndPortalBlockEntity::new, new Block[] { Blocks.END_PORTAL });
/*    */   
/* 39 */   public static final BlockEntityType<BeaconBlockEntity> BEACON = register(BlockEntityTypeIds.BEACON, BeaconBlockEntity::new, new Block[] { Blocks.BEACON });
/*    */   
/* 40 */   public static final BlockEntityType<SkullBlockEntity> SKULL = register(BlockEntityTypeIds.SKULL, SkullBlockEntity::new, new Block[] { 
/* 40 */         Blocks.SKELETON_SKULL, Blocks.SKELETON_WALL_SKULL, Blocks.CREEPER_HEAD, Blocks.CREEPER_WALL_HEAD, Blocks.DRAGON_HEAD, Blocks.DRAGON_WALL_HEAD, Blocks.ZOMBIE_HEAD, Blocks.ZOMBIE_WALL_HEAD, Blocks.WITHER_SKELETON_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL, 
/* 40 */         Blocks.PLAYER_HEAD, Blocks.PLAYER_WALL_HEAD, Blocks.PIGLIN_HEAD, Blocks.PIGLIN_WALL_HEAD });
/*    */   
/* 41 */   public static final BlockEntityType<DaylightDetectorBlockEntity> DAYLIGHT_DETECTOR = register(BlockEntityTypeIds.DAYLIGHT_DETECTOR, DaylightDetectorBlockEntity::new, new Block[] { Blocks.DAYLIGHT_DETECTOR });
/*    */   
/* 42 */   public static final BlockEntityType<HopperBlockEntity> HOPPER = register(BlockEntityTypeIds.HOPPER, HopperBlockEntity::new, new Block[] { Blocks.HOPPER });
/*    */   
/* 43 */   public static final BlockEntityType<ComparatorBlockEntity> COMPARATOR = register(BlockEntityTypeIds.COMPARATOR, ComparatorBlockEntity::new, new Block[] { Blocks.COMPARATOR });
/*    */   
/* 44 */   public static final BlockEntityType<BannerBlockEntity> BANNER = register(BlockEntityTypeIds.BANNER, BannerBlockEntity::new, Util.join(Blocks.BANNER.asList(), Blocks.WALL_BANNER.asList()));
/*    */   
/* 45 */   public static final BlockEntityType<StructureBlockEntity> STRUCTURE_BLOCK = register(BlockEntityTypeIds.STRUCTURE_BLOCK, StructureBlockEntity::new, new Block[] { Blocks.STRUCTURE_BLOCK });
/*    */   
/* 46 */   public static final BlockEntityType<TheEndGatewayBlockEntity> END_GATEWAY = register(BlockEntityTypeIds.END_GATEWAY, TheEndGatewayBlockEntity::new, new Block[] { Blocks.END_GATEWAY });
/*    */   
/* 47 */   public static final BlockEntityType<CommandBlockEntity> COMMAND_BLOCK = register(BlockEntityTypeIds.COMMAND_BLOCK, CommandBlockEntity::new, new Block[] { Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.REPEATING_COMMAND_BLOCK });
/*    */   
/* 48 */   public static final BlockEntityType<ShulkerBoxBlockEntity> SHULKER_BOX = register(BlockEntityTypeIds.SHULKER_BOX, ShulkerBoxBlockEntity::new, Util.copyAndAdd(Blocks.DYED_SHULKER_BOX.asList(), Blocks.SHULKER_BOX));
/*    */   
/* 49 */   public static final BlockEntityType<ConduitBlockEntity> CONDUIT = register(BlockEntityTypeIds.CONDUIT, ConduitBlockEntity::new, new Block[] { Blocks.CONDUIT });
/*    */   
/* 50 */   public static final BlockEntityType<BarrelBlockEntity> BARREL = register(BlockEntityTypeIds.BARREL, BarrelBlockEntity::new, new Block[] { Blocks.BARREL });
/*    */   
/* 51 */   public static final BlockEntityType<SmokerBlockEntity> SMOKER = register(BlockEntityTypeIds.SMOKER, SmokerBlockEntity::new, new Block[] { Blocks.SMOKER });
/*    */   
/* 52 */   public static final BlockEntityType<BlastFurnaceBlockEntity> BLAST_FURNACE = register(BlockEntityTypeIds.BLAST_FURNACE, BlastFurnaceBlockEntity::new, new Block[] { Blocks.BLAST_FURNACE });
/*    */   
/* 53 */   public static final BlockEntityType<LecternBlockEntity> LECTERN = register(BlockEntityTypeIds.LECTERN, LecternBlockEntity::new, new Block[] { Blocks.LECTERN });
/*    */   
/* 54 */   public static final BlockEntityType<BellBlockEntity> BELL = register(BlockEntityTypeIds.BELL, BellBlockEntity::new, new Block[] { Blocks.BELL });
/*    */   
/* 55 */   public static final BlockEntityType<JigsawBlockEntity> JIGSAW = register(BlockEntityTypeIds.JIGSAW, JigsawBlockEntity::new, new Block[] { Blocks.JIGSAW });
/*    */   
/* 56 */   public static final BlockEntityType<CampfireBlockEntity> CAMPFIRE = register(BlockEntityTypeIds.CAMPFIRE, CampfireBlockEntity::new, new Block[] { Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE });
/*    */   
/* 57 */   public static final BlockEntityType<BeehiveBlockEntity> BEEHIVE = register(BlockEntityTypeIds.BEEHIVE, BeehiveBlockEntity::new, new Block[] { Blocks.BEE_NEST, Blocks.BEEHIVE });
/*    */   
/* 58 */   public static final BlockEntityType<SculkSensorBlockEntity> SCULK_SENSOR = register(BlockEntityTypeIds.SCULK_SENSOR, SculkSensorBlockEntity::new, new Block[] { Blocks.SCULK_SENSOR });
/*    */   
/* 59 */   public static final BlockEntityType<CalibratedSculkSensorBlockEntity> CALIBRATED_SCULK_SENSOR = register(BlockEntityTypeIds.CALIBRATED_SCULK_SENSOR, CalibratedSculkSensorBlockEntity::new, new Block[] { Blocks.CALIBRATED_SCULK_SENSOR });
/*    */   
/* 60 */   public static final BlockEntityType<SculkCatalystBlockEntity> SCULK_CATALYST = register(BlockEntityTypeIds.SCULK_CATALYST, SculkCatalystBlockEntity::new, new Block[] { Blocks.SCULK_CATALYST });
/*    */   
/* 61 */   public static final BlockEntityType<SculkShriekerBlockEntity> SCULK_SHRIEKER = register(BlockEntityTypeIds.SCULK_SHRIEKER, SculkShriekerBlockEntity::new, new Block[] { Blocks.SCULK_SHRIEKER });
/*    */   
/* 62 */   public static final BlockEntityType<ChiseledBookShelfBlockEntity> CHISELED_BOOKSHELF = register(BlockEntityTypeIds.CHISELED_BOOKSHELF, ChiseledBookShelfBlockEntity::new, new Block[] { Blocks.CHISELED_BOOKSHELF });
/*    */   
/* 63 */   public static final BlockEntityType<ShelfBlockEntity> SHELF = register(BlockEntityTypeIds.SHELF, ShelfBlockEntity::new, new Block[] { 
/* 63 */         Blocks.ACACIA_SHELF, Blocks.BAMBOO_SHELF, Blocks.BIRCH_SHELF, Blocks.CHERRY_SHELF, Blocks.CRIMSON_SHELF, Blocks.DARK_OAK_SHELF, Blocks.JUNGLE_SHELF, Blocks.MANGROVE_SHELF, Blocks.OAK_SHELF, Blocks.PALE_OAK_SHELF, 
/* 63 */         Blocks.SPRUCE_SHELF, Blocks.WARPED_SHELF });
/*    */   
/* 64 */   public static final BlockEntityType<BrushableBlockEntity> BRUSHABLE_BLOCK = register(BlockEntityTypeIds.BRUSHABLE_BLOCK, BrushableBlockEntity::new, new Block[] { Blocks.SUSPICIOUS_SAND, Blocks.SUSPICIOUS_GRAVEL });
/*    */   
/* 65 */   public static final BlockEntityType<DecoratedPotBlockEntity> DECORATED_POT = register(BlockEntityTypeIds.DECORATED_POT, DecoratedPotBlockEntity::new, new Block[] { Blocks.DECORATED_POT });
/*    */   
/* 66 */   public static final BlockEntityType<CrafterBlockEntity> CRAFTER = register(BlockEntityTypeIds.CRAFTER, CrafterBlockEntity::new, new Block[] { Blocks.CRAFTER });
/*    */   
/* 67 */   public static final BlockEntityType<TrialSpawnerBlockEntity> TRIAL_SPAWNER = register(BlockEntityTypeIds.TRIAL_SPAWNER, TrialSpawnerBlockEntity::new, new Block[] { Blocks.TRIAL_SPAWNER });
/*    */   
/* 68 */   public static final BlockEntityType<VaultBlockEntity> VAULT = register(BlockEntityTypeIds.VAULT, VaultBlockEntity::new, new Block[] { Blocks.VAULT });
/*    */   
/* 69 */   public static final BlockEntityType<TestBlockEntity> TEST_BLOCK = register(BlockEntityTypeIds.TEST_BLOCK, TestBlockEntity::new, new Block[] { Blocks.TEST_BLOCK });
/*    */   
/* 70 */   public static final BlockEntityType<TestInstanceBlockEntity> TEST_INSTANCE_BLOCK = register(BlockEntityTypeIds.TEST_INSTANCE_BLOCK, TestInstanceBlockEntity::new, new Block[] { Blocks.TEST_INSTANCE_BLOCK });
/*    */   
/* 71 */   public static final BlockEntityType<CopperGolemStatueBlockEntity> COPPER_GOLEM_STATUE = register(BlockEntityTypeIds.COPPER_GOLEM_STATUE, CopperGolemStatueBlockEntity::new, Blocks.COPPER_GOLEM_STATUE.asList());
/*    */   
/* 72 */   public static final BlockEntityType<PotentSulfurBlockEntity> POTENT_SULFUR = register(BlockEntityTypeIds.POTENT_SULFUR, PotentSulfurBlockEntity::new, new Block[] { Blocks.POTENT_SULFUR });
/*    */   
/*    */   private static <T extends BlockEntity> BlockEntityType<T> register(ResourceKey<BlockEntityType<?>> key, BlockEntityType.BlockEntitySupplier<? extends T> factory, Block... validBlocks) {
/* 75 */     Identifier id = key.identifier();
/* 76 */     if (validBlocks.length == 0)
/* 77 */       LOGGER.warn("Block entity type {} requires at least one valid block to be defined!", id); 
/* 79 */     if (id.getNamespace().equals("minecraft"))
/* 82 */       Util.fetchChoiceType(References.BLOCK_ENTITY, id.getPath()); 
/* 84 */     return (BlockEntityType<T>)Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, key, new BlockEntityType<>(factory, Set.of(validBlocks)));
/*    */   }
/*    */   
/*    */   private static <T extends BlockEntity> BlockEntityType<T> register(ResourceKey<BlockEntityType<?>> id, BlockEntityType.BlockEntitySupplier<? extends T> factory, List<Block> validBlocks) {
/* 88 */     return register(id, factory, validBlocks.<Block>toArray(new Block[0]));
/*    */   }
/*    */   
/* 93 */   static final Set<BlockEntityType<?>> OP_ONLY_CUSTOM_DATA = Set.of(COMMAND_BLOCK, LECTERN, SIGN, HANGING_SIGN, MOB_SPAWNER, TRIAL_SPAWNER);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BlockEntityTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */