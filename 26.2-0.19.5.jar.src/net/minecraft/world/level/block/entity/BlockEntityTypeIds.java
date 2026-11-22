/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class BlockEntityTypeIds {
/*  8 */   public static final ResourceKey<BlockEntityType<?>> FURNACE = create("furnace");
/*    */   
/*  9 */   public static final ResourceKey<BlockEntityType<?>> CHEST = create("chest");
/*    */   
/* 10 */   public static final ResourceKey<BlockEntityType<?>> TRAPPED_CHEST = create("trapped_chest");
/*    */   
/* 11 */   public static final ResourceKey<BlockEntityType<?>> ENDER_CHEST = create("ender_chest");
/*    */   
/* 12 */   public static final ResourceKey<BlockEntityType<?>> JUKEBOX = create("jukebox");
/*    */   
/* 13 */   public static final ResourceKey<BlockEntityType<?>> DISPENSER = create("dispenser");
/*    */   
/* 14 */   public static final ResourceKey<BlockEntityType<?>> DROPPER = create("dropper");
/*    */   
/* 15 */   public static final ResourceKey<BlockEntityType<?>> SIGN = create("sign");
/*    */   
/* 16 */   public static final ResourceKey<BlockEntityType<?>> HANGING_SIGN = create("hanging_sign");
/*    */   
/* 17 */   public static final ResourceKey<BlockEntityType<?>> MOB_SPAWNER = create("mob_spawner");
/*    */   
/* 18 */   public static final ResourceKey<BlockEntityType<?>> CREAKING_HEART = create("creaking_heart");
/*    */   
/* 19 */   public static final ResourceKey<BlockEntityType<?>> PISTON = create("piston");
/*    */   
/* 20 */   public static final ResourceKey<BlockEntityType<?>> BREWING_STAND = create("brewing_stand");
/*    */   
/* 21 */   public static final ResourceKey<BlockEntityType<?>> ENCHANTING_TABLE = create("enchanting_table");
/*    */   
/* 22 */   public static final ResourceKey<BlockEntityType<?>> END_PORTAL = create("end_portal");
/*    */   
/* 23 */   public static final ResourceKey<BlockEntityType<?>> BEACON = create("beacon");
/*    */   
/* 24 */   public static final ResourceKey<BlockEntityType<?>> SKULL = create("skull");
/*    */   
/* 25 */   public static final ResourceKey<BlockEntityType<?>> DAYLIGHT_DETECTOR = create("daylight_detector");
/*    */   
/* 26 */   public static final ResourceKey<BlockEntityType<?>> HOPPER = create("hopper");
/*    */   
/* 27 */   public static final ResourceKey<BlockEntityType<?>> COMPARATOR = create("comparator");
/*    */   
/* 28 */   public static final ResourceKey<BlockEntityType<?>> BANNER = create("banner");
/*    */   
/* 29 */   public static final ResourceKey<BlockEntityType<?>> STRUCTURE_BLOCK = create("structure_block");
/*    */   
/* 30 */   public static final ResourceKey<BlockEntityType<?>> END_GATEWAY = create("end_gateway");
/*    */   
/* 31 */   public static final ResourceKey<BlockEntityType<?>> COMMAND_BLOCK = create("command_block");
/*    */   
/* 32 */   public static final ResourceKey<BlockEntityType<?>> SHULKER_BOX = create("shulker_box");
/*    */   
/* 33 */   public static final ResourceKey<BlockEntityType<?>> CONDUIT = create("conduit");
/*    */   
/* 34 */   public static final ResourceKey<BlockEntityType<?>> BARREL = create("barrel");
/*    */   
/* 35 */   public static final ResourceKey<BlockEntityType<?>> SMOKER = create("smoker");
/*    */   
/* 36 */   public static final ResourceKey<BlockEntityType<?>> BLAST_FURNACE = create("blast_furnace");
/*    */   
/* 37 */   public static final ResourceKey<BlockEntityType<?>> LECTERN = create("lectern");
/*    */   
/* 38 */   public static final ResourceKey<BlockEntityType<?>> BELL = create("bell");
/*    */   
/* 39 */   public static final ResourceKey<BlockEntityType<?>> JIGSAW = create("jigsaw");
/*    */   
/* 40 */   public static final ResourceKey<BlockEntityType<?>> CAMPFIRE = create("campfire");
/*    */   
/* 41 */   public static final ResourceKey<BlockEntityType<?>> BEEHIVE = create("beehive");
/*    */   
/* 42 */   public static final ResourceKey<BlockEntityType<?>> SCULK_SENSOR = create("sculk_sensor");
/*    */   
/* 43 */   public static final ResourceKey<BlockEntityType<?>> CALIBRATED_SCULK_SENSOR = create("calibrated_sculk_sensor");
/*    */   
/* 44 */   public static final ResourceKey<BlockEntityType<?>> SCULK_CATALYST = create("sculk_catalyst");
/*    */   
/* 45 */   public static final ResourceKey<BlockEntityType<?>> SCULK_SHRIEKER = create("sculk_shrieker");
/*    */   
/* 46 */   public static final ResourceKey<BlockEntityType<?>> CHISELED_BOOKSHELF = create("chiseled_bookshelf");
/*    */   
/* 47 */   public static final ResourceKey<BlockEntityType<?>> SHELF = create("shelf");
/*    */   
/* 48 */   public static final ResourceKey<BlockEntityType<?>> BRUSHABLE_BLOCK = create("brushable_block");
/*    */   
/* 49 */   public static final ResourceKey<BlockEntityType<?>> DECORATED_POT = create("decorated_pot");
/*    */   
/* 50 */   public static final ResourceKey<BlockEntityType<?>> CRAFTER = create("crafter");
/*    */   
/* 51 */   public static final ResourceKey<BlockEntityType<?>> TRIAL_SPAWNER = create("trial_spawner");
/*    */   
/* 52 */   public static final ResourceKey<BlockEntityType<?>> VAULT = create("vault");
/*    */   
/* 53 */   public static final ResourceKey<BlockEntityType<?>> TEST_BLOCK = create("test_block");
/*    */   
/* 54 */   public static final ResourceKey<BlockEntityType<?>> TEST_INSTANCE_BLOCK = create("test_instance_block");
/*    */   
/* 55 */   public static final ResourceKey<BlockEntityType<?>> COPPER_GOLEM_STATUE = create("copper_golem_statue");
/*    */   
/* 56 */   public static final ResourceKey<BlockEntityType<?>> POTENT_SULFUR = create("potent_sulfur");
/*    */   
/*    */   private static ResourceKey<BlockEntityType<?>> create(String name) {
/* 59 */     return ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BlockEntityTypeIds.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */