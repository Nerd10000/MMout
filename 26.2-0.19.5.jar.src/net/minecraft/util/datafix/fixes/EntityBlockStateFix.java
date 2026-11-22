/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.Tag;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class EntityBlockStateFix extends DataFix {
/*     */   private static final Map<String, Integer> MAP;
/*     */   
/*     */   public EntityBlockStateFix(Schema outputSchema, boolean changesType) {
/*  34 */     super(outputSchema, changesType);
/*     */   }
/*     */   
/*     */   static {
/*  37 */     MAP = (Map<String, Integer>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */           map.put("minecraft:air", Integer.valueOf(0));
/*     */           map.put("minecraft:stone", Integer.valueOf(1));
/*     */           map.put("minecraft:grass", Integer.valueOf(2));
/*     */           map.put("minecraft:dirt", Integer.valueOf(3));
/*     */           map.put("minecraft:cobblestone", Integer.valueOf(4));
/*     */           map.put("minecraft:planks", Integer.valueOf(5));
/*     */           map.put("minecraft:sapling", Integer.valueOf(6));
/*     */           map.put("minecraft:bedrock", Integer.valueOf(7));
/*     */           map.put("minecraft:flowing_water", Integer.valueOf(8));
/*     */           map.put("minecraft:water", Integer.valueOf(9));
/*     */           map.put("minecraft:flowing_lava", Integer.valueOf(10));
/*     */           map.put("minecraft:lava", Integer.valueOf(11));
/*     */           map.put("minecraft:sand", Integer.valueOf(12));
/*     */           map.put("minecraft:gravel", Integer.valueOf(13));
/*     */           map.put("minecraft:gold_ore", Integer.valueOf(14));
/*     */           map.put("minecraft:iron_ore", Integer.valueOf(15));
/*     */           map.put("minecraft:coal_ore", Integer.valueOf(16));
/*     */           map.put("minecraft:log", Integer.valueOf(17));
/*     */           map.put("minecraft:leaves", Integer.valueOf(18));
/*     */           map.put("minecraft:sponge", Integer.valueOf(19));
/*     */           map.put("minecraft:glass", Integer.valueOf(20));
/*     */           map.put("minecraft:lapis_ore", Integer.valueOf(21));
/*     */           map.put("minecraft:lapis_block", Integer.valueOf(22));
/*     */           map.put("minecraft:dispenser", Integer.valueOf(23));
/*     */           map.put("minecraft:sandstone", Integer.valueOf(24));
/*     */           map.put("minecraft:noteblock", Integer.valueOf(25));
/*     */           map.put("minecraft:bed", Integer.valueOf(26));
/*     */           map.put("minecraft:golden_rail", Integer.valueOf(27));
/*     */           map.put("minecraft:detector_rail", Integer.valueOf(28));
/*     */           map.put("minecraft:sticky_piston", Integer.valueOf(29));
/*     */           map.put("minecraft:web", Integer.valueOf(30));
/*     */           map.put("minecraft:tallgrass", Integer.valueOf(31));
/*     */           map.put("minecraft:deadbush", Integer.valueOf(32));
/*     */           map.put("minecraft:piston", Integer.valueOf(33));
/*     */           map.put("minecraft:piston_head", Integer.valueOf(34));
/*     */           map.put("minecraft:wool", Integer.valueOf(35));
/*     */           map.put("minecraft:piston_extension", Integer.valueOf(36));
/*     */           map.put("minecraft:yellow_flower", Integer.valueOf(37));
/*     */           map.put("minecraft:red_flower", Integer.valueOf(38));
/*     */           map.put("minecraft:brown_mushroom", Integer.valueOf(39));
/*     */           map.put("minecraft:red_mushroom", Integer.valueOf(40));
/*     */           map.put("minecraft:gold_block", Integer.valueOf(41));
/*     */           map.put("minecraft:iron_block", Integer.valueOf(42));
/*     */           map.put("minecraft:double_stone_slab", Integer.valueOf(43));
/*     */           map.put("minecraft:stone_slab", Integer.valueOf(44));
/*     */           map.put("minecraft:brick_block", Integer.valueOf(45));
/*     */           map.put("minecraft:tnt", Integer.valueOf(46));
/*     */           map.put("minecraft:bookshelf", Integer.valueOf(47));
/*     */           map.put("minecraft:mossy_cobblestone", Integer.valueOf(48));
/*     */           map.put("minecraft:obsidian", Integer.valueOf(49));
/*     */           map.put("minecraft:torch", Integer.valueOf(50));
/*     */           map.put("minecraft:fire", Integer.valueOf(51));
/*     */           map.put("minecraft:mob_spawner", Integer.valueOf(52));
/*     */           map.put("minecraft:oak_stairs", Integer.valueOf(53));
/*     */           map.put("minecraft:chest", Integer.valueOf(54));
/*     */           map.put("minecraft:redstone_wire", Integer.valueOf(55));
/*     */           map.put("minecraft:diamond_ore", Integer.valueOf(56));
/*     */           map.put("minecraft:diamond_block", Integer.valueOf(57));
/*     */           map.put("minecraft:crafting_table", Integer.valueOf(58));
/*     */           map.put("minecraft:wheat", Integer.valueOf(59));
/*     */           map.put("minecraft:farmland", Integer.valueOf(60));
/*     */           map.put("minecraft:furnace", Integer.valueOf(61));
/*     */           map.put("minecraft:lit_furnace", Integer.valueOf(62));
/*     */           map.put("minecraft:standing_sign", Integer.valueOf(63));
/*     */           map.put("minecraft:wooden_door", Integer.valueOf(64));
/*     */           map.put("minecraft:ladder", Integer.valueOf(65));
/*     */           map.put("minecraft:rail", Integer.valueOf(66));
/*     */           map.put("minecraft:stone_stairs", Integer.valueOf(67));
/*     */           map.put("minecraft:wall_sign", Integer.valueOf(68));
/*     */           map.put("minecraft:lever", Integer.valueOf(69));
/*     */           map.put("minecraft:stone_pressure_plate", Integer.valueOf(70));
/*     */           map.put("minecraft:iron_door", Integer.valueOf(71));
/*     */           map.put("minecraft:wooden_pressure_plate", Integer.valueOf(72));
/*     */           map.put("minecraft:redstone_ore", Integer.valueOf(73));
/*     */           map.put("minecraft:lit_redstone_ore", Integer.valueOf(74));
/*     */           map.put("minecraft:unlit_redstone_torch", Integer.valueOf(75));
/*     */           map.put("minecraft:redstone_torch", Integer.valueOf(76));
/*     */           map.put("minecraft:stone_button", Integer.valueOf(77));
/*     */           map.put("minecraft:snow_layer", Integer.valueOf(78));
/*     */           map.put("minecraft:ice", Integer.valueOf(79));
/*     */           map.put("minecraft:snow", Integer.valueOf(80));
/*     */           map.put("minecraft:cactus", Integer.valueOf(81));
/*     */           map.put("minecraft:clay", Integer.valueOf(82));
/*     */           map.put("minecraft:reeds", Integer.valueOf(83));
/*     */           map.put("minecraft:jukebox", Integer.valueOf(84));
/*     */           map.put("minecraft:fence", Integer.valueOf(85));
/*     */           map.put("minecraft:pumpkin", Integer.valueOf(86));
/*     */           map.put("minecraft:netherrack", Integer.valueOf(87));
/*     */           map.put("minecraft:soul_sand", Integer.valueOf(88));
/*     */           map.put("minecraft:glowstone", Integer.valueOf(89));
/*     */           map.put("minecraft:portal", Integer.valueOf(90));
/*     */           map.put("minecraft:lit_pumpkin", Integer.valueOf(91));
/*     */           map.put("minecraft:cake", Integer.valueOf(92));
/*     */           map.put("minecraft:unpowered_repeater", Integer.valueOf(93));
/*     */           map.put("minecraft:powered_repeater", Integer.valueOf(94));
/*     */           map.put("minecraft:stained_glass", Integer.valueOf(95));
/*     */           map.put("minecraft:trapdoor", Integer.valueOf(96));
/*     */           map.put("minecraft:monster_egg", Integer.valueOf(97));
/*     */           map.put("minecraft:stonebrick", Integer.valueOf(98));
/*     */           map.put("minecraft:brown_mushroom_block", Integer.valueOf(99));
/*     */           map.put("minecraft:red_mushroom_block", Integer.valueOf(100));
/*     */           map.put("minecraft:iron_bars", Integer.valueOf(101));
/*     */           map.put("minecraft:glass_pane", Integer.valueOf(102));
/*     */           map.put("minecraft:melon_block", Integer.valueOf(103));
/*     */           map.put("minecraft:pumpkin_stem", Integer.valueOf(104));
/*     */           map.put("minecraft:melon_stem", Integer.valueOf(105));
/*     */           map.put("minecraft:vine", Integer.valueOf(106));
/*     */           map.put("minecraft:fence_gate", Integer.valueOf(107));
/*     */           map.put("minecraft:brick_stairs", Integer.valueOf(108));
/*     */           map.put("minecraft:stone_brick_stairs", Integer.valueOf(109));
/*     */           map.put("minecraft:mycelium", Integer.valueOf(110));
/*     */           map.put("minecraft:waterlily", Integer.valueOf(111));
/*     */           map.put("minecraft:nether_brick", Integer.valueOf(112));
/*     */           map.put("minecraft:nether_brick_fence", Integer.valueOf(113));
/*     */           map.put("minecraft:nether_brick_stairs", Integer.valueOf(114));
/*     */           map.put("minecraft:nether_wart", Integer.valueOf(115));
/*     */           map.put("minecraft:enchanting_table", Integer.valueOf(116));
/*     */           map.put("minecraft:brewing_stand", Integer.valueOf(117));
/*     */           map.put("minecraft:cauldron", Integer.valueOf(118));
/*     */           map.put("minecraft:end_portal", Integer.valueOf(119));
/*     */           map.put("minecraft:end_portal_frame", Integer.valueOf(120));
/*     */           map.put("minecraft:end_stone", Integer.valueOf(121));
/*     */           map.put("minecraft:dragon_egg", Integer.valueOf(122));
/*     */           map.put("minecraft:redstone_lamp", Integer.valueOf(123));
/*     */           map.put("minecraft:lit_redstone_lamp", Integer.valueOf(124));
/*     */           map.put("minecraft:double_wooden_slab", Integer.valueOf(125));
/*     */           map.put("minecraft:wooden_slab", Integer.valueOf(126));
/*     */           map.put("minecraft:cocoa", Integer.valueOf(127));
/*     */           map.put("minecraft:sandstone_stairs", Integer.valueOf(128));
/*     */           map.put("minecraft:emerald_ore", Integer.valueOf(129));
/*     */           map.put("minecraft:ender_chest", Integer.valueOf(130));
/*     */           map.put("minecraft:tripwire_hook", Integer.valueOf(131));
/*     */           map.put("minecraft:tripwire", Integer.valueOf(132));
/*     */           map.put("minecraft:emerald_block", Integer.valueOf(133));
/*     */           map.put("minecraft:spruce_stairs", Integer.valueOf(134));
/*     */           map.put("minecraft:birch_stairs", Integer.valueOf(135));
/*     */           map.put("minecraft:jungle_stairs", Integer.valueOf(136));
/*     */           map.put("minecraft:command_block", Integer.valueOf(137));
/*     */           map.put("minecraft:beacon", Integer.valueOf(138));
/*     */           map.put("minecraft:cobblestone_wall", Integer.valueOf(139));
/*     */           map.put("minecraft:flower_pot", Integer.valueOf(140));
/*     */           map.put("minecraft:carrots", Integer.valueOf(141));
/*     */           map.put("minecraft:potatoes", Integer.valueOf(142));
/*     */           map.put("minecraft:wooden_button", Integer.valueOf(143));
/*     */           map.put("minecraft:skull", Integer.valueOf(144));
/*     */           map.put("minecraft:anvil", Integer.valueOf(145));
/*     */           map.put("minecraft:trapped_chest", Integer.valueOf(146));
/*     */           map.put("minecraft:light_weighted_pressure_plate", Integer.valueOf(147));
/*     */           map.put("minecraft:heavy_weighted_pressure_plate", Integer.valueOf(148));
/*     */           map.put("minecraft:unpowered_comparator", Integer.valueOf(149));
/*     */           map.put("minecraft:powered_comparator", Integer.valueOf(150));
/*     */           map.put("minecraft:daylight_detector", Integer.valueOf(151));
/*     */           map.put("minecraft:redstone_block", Integer.valueOf(152));
/*     */           map.put("minecraft:quartz_ore", Integer.valueOf(153));
/*     */           map.put("minecraft:hopper", Integer.valueOf(154));
/*     */           map.put("minecraft:quartz_block", Integer.valueOf(155));
/*     */           map.put("minecraft:quartz_stairs", Integer.valueOf(156));
/*     */           map.put("minecraft:activator_rail", Integer.valueOf(157));
/*     */           map.put("minecraft:dropper", Integer.valueOf(158));
/*     */           map.put("minecraft:stained_hardened_clay", Integer.valueOf(159));
/*     */           map.put("minecraft:stained_glass_pane", Integer.valueOf(160));
/*     */           map.put("minecraft:leaves2", Integer.valueOf(161));
/*     */           map.put("minecraft:log2", Integer.valueOf(162));
/*     */           map.put("minecraft:acacia_stairs", Integer.valueOf(163));
/*     */           map.put("minecraft:dark_oak_stairs", Integer.valueOf(164));
/*     */           map.put("minecraft:slime", Integer.valueOf(165));
/*     */           map.put("minecraft:barrier", Integer.valueOf(166));
/*     */           map.put("minecraft:iron_trapdoor", Integer.valueOf(167));
/*     */           map.put("minecraft:prismarine", Integer.valueOf(168));
/*     */           map.put("minecraft:sea_lantern", Integer.valueOf(169));
/*     */           map.put("minecraft:hay_block", Integer.valueOf(170));
/*     */           map.put("minecraft:carpet", Integer.valueOf(171));
/*     */           map.put("minecraft:hardened_clay", Integer.valueOf(172));
/*     */           map.put("minecraft:coal_block", Integer.valueOf(173));
/*     */           map.put("minecraft:packed_ice", Integer.valueOf(174));
/*     */           map.put("minecraft:double_plant", Integer.valueOf(175));
/*     */           map.put("minecraft:standing_banner", Integer.valueOf(176));
/*     */           map.put("minecraft:wall_banner", Integer.valueOf(177));
/*     */           map.put("minecraft:daylight_detector_inverted", Integer.valueOf(178));
/*     */           map.put("minecraft:red_sandstone", Integer.valueOf(179));
/*     */           map.put("minecraft:red_sandstone_stairs", Integer.valueOf(180));
/*     */           map.put("minecraft:double_stone_slab2", Integer.valueOf(181));
/*     */           map.put("minecraft:stone_slab2", Integer.valueOf(182));
/*     */           map.put("minecraft:spruce_fence_gate", Integer.valueOf(183));
/*     */           map.put("minecraft:birch_fence_gate", Integer.valueOf(184));
/*     */           map.put("minecraft:jungle_fence_gate", Integer.valueOf(185));
/*     */           map.put("minecraft:dark_oak_fence_gate", Integer.valueOf(186));
/*     */           map.put("minecraft:acacia_fence_gate", Integer.valueOf(187));
/*     */           map.put("minecraft:spruce_fence", Integer.valueOf(188));
/*     */           map.put("minecraft:birch_fence", Integer.valueOf(189));
/*     */           map.put("minecraft:jungle_fence", Integer.valueOf(190));
/*     */           map.put("minecraft:dark_oak_fence", Integer.valueOf(191));
/*     */           map.put("minecraft:acacia_fence", Integer.valueOf(192));
/*     */           map.put("minecraft:spruce_door", Integer.valueOf(193));
/*     */           map.put("minecraft:birch_door", Integer.valueOf(194));
/*     */           map.put("minecraft:jungle_door", Integer.valueOf(195));
/*     */           map.put("minecraft:acacia_door", Integer.valueOf(196));
/*     */           map.put("minecraft:dark_oak_door", Integer.valueOf(197));
/*     */           map.put("minecraft:end_rod", Integer.valueOf(198));
/*     */           map.put("minecraft:chorus_plant", Integer.valueOf(199));
/*     */           map.put("minecraft:chorus_flower", Integer.valueOf(200));
/*     */           map.put("minecraft:purpur_block", Integer.valueOf(201));
/*     */           map.put("minecraft:purpur_pillar", Integer.valueOf(202));
/*     */           map.put("minecraft:purpur_stairs", Integer.valueOf(203));
/*     */           map.put("minecraft:purpur_double_slab", Integer.valueOf(204));
/*     */           map.put("minecraft:purpur_slab", Integer.valueOf(205));
/*     */           map.put("minecraft:end_bricks", Integer.valueOf(206));
/*     */           map.put("minecraft:beetroots", Integer.valueOf(207));
/*     */           map.put("minecraft:grass_path", Integer.valueOf(208));
/*     */           map.put("minecraft:end_gateway", Integer.valueOf(209));
/*     */           map.put("minecraft:repeating_command_block", Integer.valueOf(210));
/*     */           map.put("minecraft:chain_command_block", Integer.valueOf(211));
/*     */           map.put("minecraft:frosted_ice", Integer.valueOf(212));
/*     */           map.put("minecraft:magma", Integer.valueOf(213));
/*     */           map.put("minecraft:nether_wart_block", Integer.valueOf(214));
/*     */           map.put("minecraft:red_nether_brick", Integer.valueOf(215));
/*     */           map.put("minecraft:bone_block", Integer.valueOf(216));
/*     */           map.put("minecraft:structure_void", Integer.valueOf(217));
/*     */           map.put("minecraft:observer", Integer.valueOf(218));
/*     */           map.put("minecraft:white_shulker_box", Integer.valueOf(219));
/*     */           map.put("minecraft:orange_shulker_box", Integer.valueOf(220));
/*     */           map.put("minecraft:magenta_shulker_box", Integer.valueOf(221));
/*     */           map.put("minecraft:light_blue_shulker_box", Integer.valueOf(222));
/*     */           map.put("minecraft:yellow_shulker_box", Integer.valueOf(223));
/*     */           map.put("minecraft:lime_shulker_box", Integer.valueOf(224));
/*     */           map.put("minecraft:pink_shulker_box", Integer.valueOf(225));
/*     */           map.put("minecraft:gray_shulker_box", Integer.valueOf(226));
/*     */           map.put("minecraft:silver_shulker_box", Integer.valueOf(227));
/*     */           map.put("minecraft:cyan_shulker_box", Integer.valueOf(228));
/*     */           map.put("minecraft:purple_shulker_box", Integer.valueOf(229));
/*     */           map.put("minecraft:blue_shulker_box", Integer.valueOf(230));
/*     */           map.put("minecraft:brown_shulker_box", Integer.valueOf(231));
/*     */           map.put("minecraft:green_shulker_box", Integer.valueOf(232));
/*     */           map.put("minecraft:red_shulker_box", Integer.valueOf(233));
/*     */           map.put("minecraft:black_shulker_box", Integer.valueOf(234));
/*     */           map.put("minecraft:white_glazed_terracotta", Integer.valueOf(235));
/*     */           map.put("minecraft:orange_glazed_terracotta", Integer.valueOf(236));
/*     */           map.put("minecraft:magenta_glazed_terracotta", Integer.valueOf(237));
/*     */           map.put("minecraft:light_blue_glazed_terracotta", Integer.valueOf(238));
/*     */           map.put("minecraft:yellow_glazed_terracotta", Integer.valueOf(239));
/*     */           map.put("minecraft:lime_glazed_terracotta", Integer.valueOf(240));
/*     */           map.put("minecraft:pink_glazed_terracotta", Integer.valueOf(241));
/*     */           map.put("minecraft:gray_glazed_terracotta", Integer.valueOf(242));
/*     */           map.put("minecraft:silver_glazed_terracotta", Integer.valueOf(243));
/*     */           map.put("minecraft:cyan_glazed_terracotta", Integer.valueOf(244));
/*     */           map.put("minecraft:purple_glazed_terracotta", Integer.valueOf(245));
/*     */           map.put("minecraft:blue_glazed_terracotta", Integer.valueOf(246));
/*     */           map.put("minecraft:brown_glazed_terracotta", Integer.valueOf(247));
/*     */           map.put("minecraft:green_glazed_terracotta", Integer.valueOf(248));
/*     */           map.put("minecraft:red_glazed_terracotta", Integer.valueOf(249));
/*     */           map.put("minecraft:black_glazed_terracotta", Integer.valueOf(250));
/*     */           map.put("minecraft:concrete", Integer.valueOf(251));
/*     */           map.put("minecraft:concrete_powder", Integer.valueOf(252));
/*     */           map.put("minecraft:structure_block", Integer.valueOf(255));
/*     */         });
/*     */   }
/*     */   
/*     */   public static int getBlockId(String name) {
/* 295 */     Integer result = MAP.get(name);
/* 296 */     return (result == null) ? 0 : result.intValue();
/*     */   }
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 301 */     Schema inputSchema = getInputSchema();
/* 302 */     Schema outputSchema = getOutputSchema();
/* 304 */     Function<Typed<?>, Typed<?>> minecartUpdater = input -> updateBlockToBlockState(input, "DisplayTile", "DisplayData", "DisplayState");
/* 305 */     Function<Typed<?>, Typed<?>> arrowUpdater = input -> updateBlockToBlockState(input, "inTile", "inData", "inBlockState");
/* 307 */     Type<Pair<Either<Pair<String, Either<Integer, String>>, Unit>, Dynamic<?>>> oldProjectileType = DSL.and(
/* 308 */         DSL.optional((Type)DSL.field("inTile", DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString())))), 
/* 309 */         DSL.remainderType());
/* 312 */     Function<Typed<?>, Typed<?>> removeInTile = input -> input.update(oldProjectileType.finder(), DSL.remainderType(), Pair::getSecond);
/* 314 */     return fixTypeEverywhereTyped("EntityBlockStateFix", inputSchema.getType(References.ENTITY), outputSchema.getType(References.ENTITY), input -> {
/*     */           input = updateEntity(input, "minecraft:falling_block", this::updateFallingBlock);
/*     */           input = updateEntity(input, "minecraft:enderman", ());
/*     */           input = updateEntity(input, "minecraft:arrow", arrowUpdater);
/*     */           input = updateEntity(input, "minecraft:spectral_arrow", arrowUpdater);
/*     */           input = updateEntity(input, "minecraft:egg", removeInTile);
/*     */           input = updateEntity(input, "minecraft:ender_pearl", removeInTile);
/*     */           input = updateEntity(input, "minecraft:fireball", removeInTile);
/*     */           input = updateEntity(input, "minecraft:potion", removeInTile);
/*     */           input = updateEntity(input, "minecraft:small_fireball", removeInTile);
/*     */           input = updateEntity(input, "minecraft:snowball", removeInTile);
/*     */           input = updateEntity(input, "minecraft:wither_skull", removeInTile);
/*     */           input = updateEntity(input, "minecraft:xp_bottle", removeInTile);
/*     */           input = updateEntity(input, "minecraft:commandblock_minecart", minecartUpdater);
/*     */           input = updateEntity(input, "minecraft:minecart", minecartUpdater);
/*     */           input = updateEntity(input, "minecraft:chest_minecart", minecartUpdater);
/*     */           input = updateEntity(input, "minecraft:furnace_minecart", minecartUpdater);
/*     */           input = updateEntity(input, "minecraft:tnt_minecart", minecartUpdater);
/*     */           input = updateEntity(input, "minecraft:hopper_minecart", minecartUpdater);
/*     */           return updateEntity(input, "minecraft:spawner_minecart", minecartUpdater);
/*     */         });
/*     */   }
/*     */   
/*     */   private Typed<?> updateFallingBlock(Typed<?> input) {
/* 339 */     Type<Either<Pair<String, Either<Integer, String>>, Unit>> oldType = DSL.optional((Type)DSL.field("Block", DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString()))));
/* 340 */     Type<Either<Pair<String, Dynamic<?>>, Unit>> newType = DSL.optional((Type)DSL.field("BlockState", DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType())));
/* 342 */     Dynamic<?> tag = (Dynamic)input.get(DSL.remainderFinder());
/* 344 */     return input.update(oldType.finder(), newType, tile -> {
/*     */           int block = ((Integer)tile.map((), ())).intValue();
/*     */           int data = tag.get("Data").asInt(0) & 0xF;
/*     */           return Either.left(Pair.of(References.BLOCK_STATE.typeName(), BlockStateData.getTag(block << 4 | data)));
/* 355 */         }).set(DSL.remainderFinder(), tag.remove("Data").remove("TileID").remove("Tile"));
/*     */   }
/*     */   
/*     */   private Typed<?> updateBlockToBlockState(Typed<?> input, String oldFieldName, String dataName, String newFieldName) {
/* 359 */     Tag.TagType tagType1 = DSL.field(oldFieldName, DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString())));
/* 360 */     Tag.TagType tagType2 = DSL.field(newFieldName, DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType()));
/* 362 */     Dynamic<?> tag = (Dynamic)input.getOrCreate(DSL.remainderFinder());
/* 364 */     return input.update(tagType1.finder(), (Type)tagType2, tile -> {
/*     */           int block = ((Integer)((Either)tile.getSecond()).map((), EntityBlockStateFix::getBlockId)).intValue();
/*     */           int data = tag.get(dataName).asInt(0) & 0xF;
/*     */           return Pair.of(References.BLOCK_STATE.typeName(), BlockStateData.getTag(block << 4 | data));
/* 369 */         }).set(DSL.remainderFinder(), tag.remove(dataName));
/*     */   }
/*     */   
/*     */   private Typed<?> updateEntity(Typed<?> input, String name, Function<Typed<?>, Typed<?>> function) {
/* 373 */     Type<?> oldType = getInputSchema().getChoiceType(References.ENTITY, name);
/* 374 */     Type<?> newType = getOutputSchema().getChoiceType(References.ENTITY, name);
/* 375 */     return input.updateTyped(DSL.namedChoice(name, oldType), newType, function);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityBlockStateFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */