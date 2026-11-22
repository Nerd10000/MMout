/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.BitSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ 
/*     */ class MappingConstants {
/*  53 */   private static final BitSet VIRTUAL = new BitSet(256);
/*     */   
/*  54 */   private static final BitSet FIX = new BitSet(256);
/*     */   
/*  55 */   private static final Dynamic<?> PUMPKIN = ExtraDataFixUtils.blockState("minecraft:pumpkin");
/*     */   
/*  56 */   private static final Dynamic<?> SNOWY_PODZOL = ExtraDataFixUtils.blockState("minecraft:podzol", Map.of("snowy", "true"));
/*     */   
/*  57 */   private static final Dynamic<?> SNOWY_GRASS = ExtraDataFixUtils.blockState("minecraft:grass_block", Map.of("snowy", "true"));
/*     */   
/*  58 */   private static final Dynamic<?> SNOWY_MYCELIUM = ExtraDataFixUtils.blockState("minecraft:mycelium", Map.of("snowy", "true"));
/*     */   
/*  59 */   private static final Dynamic<?> UPPER_SUNFLOWER = ExtraDataFixUtils.blockState("minecraft:sunflower", Map.of("half", "upper"));
/*     */   
/*  60 */   private static final Dynamic<?> UPPER_LILAC = ExtraDataFixUtils.blockState("minecraft:lilac", Map.of("half", "upper"));
/*     */   
/*  61 */   private static final Dynamic<?> UPPER_TALL_GRASS = ExtraDataFixUtils.blockState("minecraft:tall_grass", Map.of("half", "upper"));
/*     */   
/*  62 */   private static final Dynamic<?> UPPER_LARGE_FERN = ExtraDataFixUtils.blockState("minecraft:large_fern", Map.of("half", "upper"));
/*     */   
/*  63 */   private static final Dynamic<?> UPPER_ROSE_BUSH = ExtraDataFixUtils.blockState("minecraft:rose_bush", Map.of("half", "upper"));
/*     */   
/*  64 */   private static final Dynamic<?> UPPER_PEONY = ExtraDataFixUtils.blockState("minecraft:peony", Map.of("half", "upper"));
/*     */   
/*     */   private static final Map<String, Dynamic<?>> FLOWER_POT_MAP;
/*     */   
/*     */   private static final Map<String, Dynamic<?>> SKULL_MAP;
/*     */   
/*     */   private static final Map<String, Dynamic<?>> DOOR_MAP;
/*     */   
/*     */   private static final Map<String, Dynamic<?>> NOTE_BLOCK_MAP;
/*     */   
/*     */   private static final Int2ObjectMap<String> DYE_COLOR_MAP;
/*     */   
/*     */   private static final Map<String, Dynamic<?>> BED_BLOCK_MAP;
/*     */   
/*     */   private static final Map<String, Dynamic<?>> BANNER_BLOCK_MAP;
/*     */   
/*     */   static {
/*  66 */     FLOWER_POT_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */           map.put("minecraft:air0", ExtraDataFixUtils.blockState("minecraft:flower_pot"));
/*     */           map.put("minecraft:red_flower0", ExtraDataFixUtils.blockState("minecraft:potted_poppy"));
/*     */           map.put("minecraft:red_flower1", ExtraDataFixUtils.blockState("minecraft:potted_blue_orchid"));
/*     */           map.put("minecraft:red_flower2", ExtraDataFixUtils.blockState("minecraft:potted_allium"));
/*     */           map.put("minecraft:red_flower3", ExtraDataFixUtils.blockState("minecraft:potted_azure_bluet"));
/*     */           map.put("minecraft:red_flower4", ExtraDataFixUtils.blockState("minecraft:potted_red_tulip"));
/*     */           map.put("minecraft:red_flower5", ExtraDataFixUtils.blockState("minecraft:potted_orange_tulip"));
/*     */           map.put("minecraft:red_flower6", ExtraDataFixUtils.blockState("minecraft:potted_white_tulip"));
/*     */           map.put("minecraft:red_flower7", ExtraDataFixUtils.blockState("minecraft:potted_pink_tulip"));
/*     */           map.put("minecraft:red_flower8", ExtraDataFixUtils.blockState("minecraft:potted_oxeye_daisy"));
/*     */           map.put("minecraft:yellow_flower0", ExtraDataFixUtils.blockState("minecraft:potted_dandelion"));
/*     */           map.put("minecraft:sapling0", ExtraDataFixUtils.blockState("minecraft:potted_oak_sapling"));
/*     */           map.put("minecraft:sapling1", ExtraDataFixUtils.blockState("minecraft:potted_spruce_sapling"));
/*     */           map.put("minecraft:sapling2", ExtraDataFixUtils.blockState("minecraft:potted_birch_sapling"));
/*     */           map.put("minecraft:sapling3", ExtraDataFixUtils.blockState("minecraft:potted_jungle_sapling"));
/*     */           map.put("minecraft:sapling4", ExtraDataFixUtils.blockState("minecraft:potted_acacia_sapling"));
/*     */           map.put("minecraft:sapling5", ExtraDataFixUtils.blockState("minecraft:potted_dark_oak_sapling"));
/*     */           map.put("minecraft:red_mushroom0", ExtraDataFixUtils.blockState("minecraft:potted_red_mushroom"));
/*     */           map.put("minecraft:brown_mushroom0", ExtraDataFixUtils.blockState("minecraft:potted_brown_mushroom"));
/*     */           map.put("minecraft:deadbush0", ExtraDataFixUtils.blockState("minecraft:potted_dead_bush"));
/*     */           map.put("minecraft:tallgrass2", ExtraDataFixUtils.blockState("minecraft:potted_fern"));
/*     */           map.put("minecraft:cactus0", ExtraDataFixUtils.blockState("minecraft:potted_cactus"));
/*     */         });
/*  91 */     SKULL_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */           mapSkull(map, 0, "skeleton", "skull");
/*     */           mapSkull(map, 1, "wither_skeleton", "skull");
/*     */           mapSkull(map, 2, "zombie", "head");
/*     */           mapSkull(map, 3, "player", "head");
/*     */           mapSkull(map, 4, "creeper", "head");
/*     */           mapSkull(map, 5, "dragon", "head");
/*     */         });
/* 110 */     DOOR_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */           mapDoor(map, "oak_door");
/*     */           mapDoor(map, "iron_door");
/*     */           mapDoor(map, "spruce_door");
/*     */           mapDoor(map, "birch_door");
/*     */           mapDoor(map, "jungle_door");
/*     */           mapDoor(map, "acacia_door");
/*     */           mapDoor(map, "dark_oak_door");
/*     */         });
/* 188 */     NOTE_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */           for (int i = 0; i < 26; i++) {
/*     */             map.put("true" + i, ExtraDataFixUtils.blockState("minecraft:note_block", Map.of("powered", "true", "note", String.valueOf(i))));
/*     */             map.put("false" + i, ExtraDataFixUtils.blockState("minecraft:note_block", Map.of("powered", "false", "note", String.valueOf(i))));
/*     */           } 
/*     */         });
/* 195 */     DYE_COLOR_MAP = (Int2ObjectMap<String>)DataFixUtils.make(new Int2ObjectOpenHashMap(), map -> {
/*     */           map.put(0, "white");
/*     */           map.put(1, "orange");
/*     */           map.put(2, "magenta");
/*     */           map.put(3, "light_blue");
/*     */           map.put(4, "yellow");
/*     */           map.put(5, "lime");
/*     */           map.put(6, "pink");
/*     */           map.put(7, "gray");
/*     */           map.put(8, "light_gray");
/*     */           map.put(9, "cyan");
/*     */           map.put(10, "purple");
/*     */           map.put(11, "blue");
/*     */           map.put(12, "brown");
/*     */           map.put(13, "green");
/*     */           map.put(14, "red");
/*     */           map.put(15, "black");
/*     */         });
/* 214 */     BED_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */           ObjectIterator<Int2ObjectMap.Entry<String>> objectIterator = DYE_COLOR_MAP.int2ObjectEntrySet().iterator();
/*     */           while (objectIterator.hasNext()) {
/*     */             Int2ObjectMap.Entry<String> entry = objectIterator.next();
/*     */             if (!Objects.equals(entry.getValue(), "red"))
/*     */               addBeds(map, entry.getIntKey(), (String)entry.getValue()); 
/*     */           } 
/*     */         });
/* 237 */     BANNER_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */           ObjectIterator<Int2ObjectMap.Entry<String>> objectIterator = DYE_COLOR_MAP.int2ObjectEntrySet().iterator();
/*     */           while (objectIterator.hasNext()) {
/*     */             Int2ObjectMap.Entry<String> entry = objectIterator.next();
/*     */             if (!Objects.equals(entry.getValue(), "white"))
/*     */               addBanners(map, 15 - entry.getIntKey(), (String)entry.getValue()); 
/*     */           } 
/*     */         });
/* 257 */     FIX.set(2);
/* 258 */     FIX.set(3);
/* 259 */     FIX.set(110);
/* 261 */     FIX.set(140);
/* 262 */     FIX.set(144);
/* 264 */     FIX.set(25);
/* 266 */     FIX.set(86);
/* 269 */     FIX.set(26);
/* 270 */     FIX.set(176);
/* 271 */     FIX.set(177);
/* 273 */     FIX.set(175);
/* 275 */     FIX.set(64);
/* 276 */     FIX.set(71);
/* 277 */     FIX.set(193);
/* 278 */     FIX.set(194);
/* 279 */     FIX.set(195);
/* 280 */     FIX.set(196);
/* 281 */     FIX.set(197);
/* 283 */     VIRTUAL.set(54);
/* 284 */     VIRTUAL.set(146);
/* 286 */     VIRTUAL.set(25);
/* 288 */     VIRTUAL.set(26);
/* 290 */     VIRTUAL.set(51);
/* 292 */     VIRTUAL.set(53);
/* 293 */     VIRTUAL.set(67);
/* 294 */     VIRTUAL.set(108);
/* 295 */     VIRTUAL.set(109);
/* 296 */     VIRTUAL.set(114);
/* 297 */     VIRTUAL.set(128);
/* 298 */     VIRTUAL.set(134);
/* 299 */     VIRTUAL.set(135);
/* 300 */     VIRTUAL.set(136);
/* 301 */     VIRTUAL.set(156);
/* 302 */     VIRTUAL.set(163);
/* 303 */     VIRTUAL.set(164);
/* 304 */     VIRTUAL.set(180);
/* 305 */     VIRTUAL.set(203);
/* 307 */     VIRTUAL.set(55);
/* 309 */     VIRTUAL.set(85);
/* 310 */     VIRTUAL.set(113);
/* 311 */     VIRTUAL.set(188);
/* 312 */     VIRTUAL.set(189);
/* 313 */     VIRTUAL.set(190);
/* 314 */     VIRTUAL.set(191);
/* 315 */     VIRTUAL.set(192);
/* 317 */     VIRTUAL.set(93);
/* 318 */     VIRTUAL.set(94);
/* 320 */     VIRTUAL.set(101);
/* 321 */     VIRTUAL.set(102);
/* 322 */     VIRTUAL.set(160);
/* 324 */     VIRTUAL.set(106);
/* 327 */     VIRTUAL.set(107);
/* 328 */     VIRTUAL.set(183);
/* 329 */     VIRTUAL.set(184);
/* 330 */     VIRTUAL.set(185);
/* 331 */     VIRTUAL.set(186);
/* 332 */     VIRTUAL.set(187);
/* 334 */     VIRTUAL.set(132);
/* 335 */     VIRTUAL.set(139);
/* 337 */     VIRTUAL.set(199);
/*     */   }
/*     */   
/*     */   private static void mapSkull(Map<String, Dynamic<?>> map, int i, String name, String type) {
/*     */     map.put("" + i + "north", ExtraDataFixUtils.blockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "north")));
/*     */     map.put("" + i + "east", ExtraDataFixUtils.blockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "east")));
/*     */     map.put("" + i + "south", ExtraDataFixUtils.blockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "south")));
/*     */     map.put("" + i + "west", ExtraDataFixUtils.blockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "west")));
/*     */     for (int rot = 0; rot < 16; rot++)
/*     */       map.put("" + i + i, ExtraDataFixUtils.blockState("minecraft:" + name + "_" + type, Map.of("rotation", String.valueOf(rot)))); 
/*     */   }
/*     */   
/*     */   private static void mapDoor(Map<String, Dynamic<?>> map, String type) {
/*     */     String id = "minecraft:" + type;
/*     */     map.put("minecraft:" + type + "eastlowerleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "eastlowerleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "eastlowerlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "eastlowerlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "eastlowerrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "eastlowerrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "eastlowerrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "eastlowerrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "eastupperleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "eastupperleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "eastupperlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "eastupperlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "eastupperrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "eastupperrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "eastupperrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "eastupperrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "northlowerleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "northlowerleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "northlowerlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "northlowerlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "northlowerrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "northlowerrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "northlowerrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "northlowerrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "northupperleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "northupperleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "northupperlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "northupperlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "northupperrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "northupperrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "northupperrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "northupperrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "southlowerleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "southlowerleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "southlowerlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "southlowerlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "southlowerrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "southlowerrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "southlowerrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "southlowerrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "southupperleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "southupperleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "southupperlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "southupperlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "southupperrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "southupperrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "southupperrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "southupperrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "westlowerleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "westlowerleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "westlowerlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "westlowerlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "westlowerrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "westlowerrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "westlowerrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "westlowerrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "westupperleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "westupperleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "westupperlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "westupperlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "true", "powered", "true")));
/*     */     map.put("minecraft:" + type + "westupperrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "false", "powered", "false")));
/*     */     map.put("minecraft:" + type + "westupperrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "false", "powered", "true")));
/*     */     map.put("minecraft:" + type + "westupperrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "true", "powered", "false")));
/*     */     map.put("minecraft:" + type + "westupperrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "true", "powered", "true")));
/*     */   }
/*     */   
/*     */   private static void addBeds(Map<String, Dynamic<?>> map, int colorId, String color) {
/*     */     map.put("southfalsefoot" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "south", "occupied", "false", "part", "foot")));
/*     */     map.put("westfalsefoot" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "west", "occupied", "false", "part", "foot")));
/*     */     map.put("northfalsefoot" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "north", "occupied", "false", "part", "foot")));
/*     */     map.put("eastfalsefoot" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "east", "occupied", "false", "part", "foot")));
/*     */     map.put("southfalsehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "south", "occupied", "false", "part", "head")));
/*     */     map.put("westfalsehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "west", "occupied", "false", "part", "head")));
/*     */     map.put("northfalsehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "north", "occupied", "false", "part", "head")));
/*     */     map.put("eastfalsehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "east", "occupied", "false", "part", "head")));
/*     */     map.put("southtruehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "south", "occupied", "true", "part", "head")));
/*     */     map.put("westtruehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "west", "occupied", "true", "part", "head")));
/*     */     map.put("northtruehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "north", "occupied", "true", "part", "head")));
/*     */     map.put("easttruehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "east", "occupied", "true", "part", "head")));
/*     */   }
/*     */   
/*     */   private static void addBanners(Map<String, Dynamic<?>> map, int colorId, String color) {
/*     */     for (int i = 0; i < 16; i++)
/*     */       map.put("" + i + "_" + i, ExtraDataFixUtils.blockState("minecraft:" + color + "_banner", Map.of("rotation", String.valueOf(i)))); 
/*     */     map.put("north_" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_wall_banner", Map.of("facing", "north")));
/*     */     map.put("south_" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_wall_banner", Map.of("facing", "south")));
/*     */     map.put("west_" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_wall_banner", Map.of("facing", "west")));
/*     */     map.put("east_" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_wall_banner", Map.of("facing", "east")));
/*     */   }
/*     */   
/* 340 */   private static final Dynamic<?> AIR = ExtraDataFixUtils.blockState("minecraft:air");
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ChunkPalettedStorageFix$MappingConstants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */