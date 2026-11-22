/*     */ package net.minecraft.client.data.models.model;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class ModelTemplates {
/*   9 */   public static final ModelTemplate CUBE = create("cube", new TextureSlot[] { TextureSlot.PARTICLE, TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN });
/*     */   
/*  10 */   public static final ModelTemplate CUBE_DIRECTIONAL = create("cube_directional", new TextureSlot[] { TextureSlot.PARTICLE, TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN });
/*     */   
/*  11 */   public static final ModelTemplate CUBE_ALL = create("cube_all", new TextureSlot[] { TextureSlot.ALL });
/*     */   
/*  12 */   public static final ModelTemplate CUBE_ALL_INNER_FACES = create("cube_all_inner_faces", new TextureSlot[] { TextureSlot.ALL });
/*     */   
/*  13 */   public static final ModelTemplate CUBE_MIRRORED_ALL = create("cube_mirrored_all", "_mirrored", new TextureSlot[] { TextureSlot.ALL });
/*     */   
/*  14 */   public static final ModelTemplate CUBE_NORTH_WEST_MIRRORED_ALL = create("cube_north_west_mirrored_all", "_north_west_mirrored", new TextureSlot[] { TextureSlot.ALL });
/*     */   
/*  15 */   public static final ModelTemplate CUBE_COLUMN_UV_LOCKED_X = create("cube_column_uv_locked_x", "_x", new TextureSlot[] { TextureSlot.END, TextureSlot.SIDE });
/*     */   
/*  16 */   public static final ModelTemplate CUBE_COLUMN_UV_LOCKED_Y = create("cube_column_uv_locked_y", "_y", new TextureSlot[] { TextureSlot.END, TextureSlot.SIDE });
/*     */   
/*  17 */   public static final ModelTemplate CUBE_COLUMN_UV_LOCKED_Z = create("cube_column_uv_locked_z", "_z", new TextureSlot[] { TextureSlot.END, TextureSlot.SIDE });
/*     */   
/*  18 */   public static final ModelTemplate CUBE_COLUMN = create("cube_column", new TextureSlot[] { TextureSlot.END, TextureSlot.SIDE });
/*     */   
/*  19 */   public static final ModelTemplate CUBE_COLUMN_HORIZONTAL = create("cube_column_horizontal", "_horizontal", new TextureSlot[] { TextureSlot.END, TextureSlot.SIDE });
/*     */   
/*  20 */   public static final ModelTemplate CUBE_COLUMN_MIRRORED = create("cube_column_mirrored", "_mirrored", new TextureSlot[] { TextureSlot.END, TextureSlot.SIDE });
/*     */   
/*  21 */   public static final ModelTemplate CUBE_TOP = create("cube_top", new TextureSlot[] { TextureSlot.TOP, TextureSlot.SIDE });
/*     */   
/*  22 */   public static final ModelTemplate CUBE_BOTTOM_TOP = create("cube_bottom_top", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE });
/*     */   
/*  23 */   public static final ModelTemplate CUBE_BOTTOM_TOP_INNER_FACES = create("cube_bottom_top_inner_faces", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE });
/*     */   
/*  24 */   public static final ModelTemplate CUBE_ORIENTABLE = create("orientable", new TextureSlot[] { TextureSlot.TOP, TextureSlot.FRONT, TextureSlot.SIDE });
/*     */   
/*  25 */   public static final ModelTemplate CUBE_ORIENTABLE_TOP_BOTTOM = create("orientable_with_bottom", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.FRONT });
/*     */   
/*  26 */   public static final ModelTemplate CUBE_ORIENTABLE_VERTICAL = create("orientable_vertical", "_vertical", new TextureSlot[] { TextureSlot.FRONT, TextureSlot.SIDE });
/*     */   
/*  28 */   public static final ModelTemplate BUTTON = create("button", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  29 */   public static final ModelTemplate BUTTON_PRESSED = create("button_pressed", "_pressed", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  30 */   public static final ModelTemplate BUTTON_INVENTORY = create("button_inventory", "_inventory", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  32 */   public static final ModelTemplate SIGN_ROT_0 = create("template_sign_rot_0", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  33 */   public static final ModelTemplate SIGN_ROT_1 = create("template_sign_rot_1", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  34 */   public static final ModelTemplate SIGN_ROT_2 = create("template_sign_rot_2", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  35 */   public static final ModelTemplate SIGN_ROT_3 = create("template_sign_rot_3", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  36 */   public static final ModelTemplate WALL_SIGN = create("template_wall_sign", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  37 */   public static final ModelTemplate HANGING_SIGN_ROT_0 = create("template_hanging_sign_rot_0", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  38 */   public static final ModelTemplate HANGING_SIGN_ROT_1 = create("template_hanging_sign_rot_1", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  39 */   public static final ModelTemplate HANGING_SIGN_ROT_2 = create("template_hanging_sign_rot_2", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  40 */   public static final ModelTemplate HANGING_SIGN_ROT_3 = create("template_hanging_sign_rot_3", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  41 */   public static final ModelTemplate ATTACHED_HANGING_SIGN_ROT_0 = create("template_attached_hanging_sign_rot_0", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  42 */   public static final ModelTemplate ATTACHED_HANGING_SIGN_ROT_1 = create("template_attached_hanging_sign_rot_1", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  43 */   public static final ModelTemplate ATTACHED_HANGING_SIGN_ROT_2 = create("template_attached_hanging_sign_rot_2", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  44 */   public static final ModelTemplate ATTACHED_HANGING_SIGN_ROT_3 = create("template_attached_hanging_sign_rot_3", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  45 */   public static final ModelTemplate WALL_HANGING_SIGN = create("template_wall_hanging_sign", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/*  47 */   public static final ModelTemplate DOOR_BOTTOM_LEFT = create("door_bottom_left", "_bottom_left", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM });
/*     */   
/*  48 */   public static final ModelTemplate DOOR_BOTTOM_LEFT_OPEN = create("door_bottom_left_open", "_bottom_left_open", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM });
/*     */   
/*  49 */   public static final ModelTemplate DOOR_BOTTOM_RIGHT = create("door_bottom_right", "_bottom_right", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM });
/*     */   
/*  50 */   public static final ModelTemplate DOOR_BOTTOM_RIGHT_OPEN = create("door_bottom_right_open", "_bottom_right_open", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM });
/*     */   
/*  51 */   public static final ModelTemplate DOOR_TOP_LEFT = create("door_top_left", "_top_left", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM });
/*     */   
/*  52 */   public static final ModelTemplate DOOR_TOP_LEFT_OPEN = create("door_top_left_open", "_top_left_open", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM });
/*     */   
/*  53 */   public static final ModelTemplate DOOR_TOP_RIGHT = create("door_top_right", "_top_right", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM });
/*     */   
/*  54 */   public static final ModelTemplate DOOR_TOP_RIGHT_OPEN = create("door_top_right_open", "_top_right_open", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM });
/*     */   
/*  56 */   public static final ModelTemplate CUSTOM_FENCE_POST = create("custom_fence_post", "_post", new TextureSlot[] { TextureSlot.TEXTURE, TextureSlot.PARTICLE });
/*     */   
/*  57 */   public static final ModelTemplate CUSTOM_FENCE_SIDE_NORTH = create("custom_fence_side_north", "_side_north", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  58 */   public static final ModelTemplate CUSTOM_FENCE_SIDE_EAST = create("custom_fence_side_east", "_side_east", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  59 */   public static final ModelTemplate CUSTOM_FENCE_SIDE_SOUTH = create("custom_fence_side_south", "_side_south", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  60 */   public static final ModelTemplate CUSTOM_FENCE_SIDE_WEST = create("custom_fence_side_west", "_side_west", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  61 */   public static final ModelTemplate CUSTOM_FENCE_INVENTORY = create("custom_fence_inventory", "_inventory", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  64 */   public static final ModelTemplate FENCE_POST = create("fence_post", "_post", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  65 */   public static final ModelTemplate FENCE_SIDE = create("fence_side", "_side", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  66 */   public static final ModelTemplate FENCE_INVENTORY = create("fence_inventory", "_inventory", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  68 */   public static final ModelTemplate WALL_POST = create("template_wall_post", "_post", new TextureSlot[] { TextureSlot.WALL });
/*     */   
/*  69 */   public static final ModelTemplate WALL_LOW_SIDE = create("template_wall_side", "_side", new TextureSlot[] { TextureSlot.WALL });
/*     */   
/*  70 */   public static final ModelTemplate WALL_TALL_SIDE = create("template_wall_side_tall", "_side_tall", new TextureSlot[] { TextureSlot.WALL });
/*     */   
/*  71 */   public static final ModelTemplate WALL_INVENTORY = create("wall_inventory", "_inventory", new TextureSlot[] { TextureSlot.WALL });
/*     */   
/*  73 */   public static final ModelTemplate CUSTOM_FENCE_GATE_CLOSED = create("template_custom_fence_gate", new TextureSlot[] { TextureSlot.TEXTURE, TextureSlot.PARTICLE });
/*     */   
/*  74 */   public static final ModelTemplate CUSTOM_FENCE_GATE_OPEN = create("template_custom_fence_gate_open", "_open", new TextureSlot[] { TextureSlot.TEXTURE, TextureSlot.PARTICLE });
/*     */   
/*  75 */   public static final ModelTemplate CUSTOM_FENCE_GATE_WALL_CLOSED = create("template_custom_fence_gate_wall", "_wall", new TextureSlot[] { TextureSlot.TEXTURE, TextureSlot.PARTICLE });
/*     */   
/*  76 */   public static final ModelTemplate CUSTOM_FENCE_GATE_WALL_OPEN = create("template_custom_fence_gate_wall_open", "_wall_open", new TextureSlot[] { TextureSlot.TEXTURE, TextureSlot.PARTICLE });
/*     */   
/*  79 */   public static final ModelTemplate FENCE_GATE_CLOSED = create("template_fence_gate", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  80 */   public static final ModelTemplate FENCE_GATE_OPEN = create("template_fence_gate_open", "_open", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  81 */   public static final ModelTemplate FENCE_GATE_WALL_CLOSED = create("template_fence_gate_wall", "_wall", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  82 */   public static final ModelTemplate FENCE_GATE_WALL_OPEN = create("template_fence_gate_wall_open", "_wall_open", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  84 */   public static final ModelTemplate PRESSURE_PLATE_UP = create("pressure_plate_up", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  85 */   public static final ModelTemplate PRESSURE_PLATE_DOWN = create("pressure_plate_down", "_down", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  87 */   public static final ModelTemplate PARTICLE_ONLY = create(new TextureSlot[] { TextureSlot.PARTICLE });
/*     */   
/*  89 */   public static final ModelTemplate SLAB_BOTTOM = create("slab", new TextureSlot[] { TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE });
/*     */   
/*  90 */   public static final ModelTemplate SLAB_TOP = create("slab_top", "_top", new TextureSlot[] { TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE });
/*     */   
/*  92 */   public static final ModelTemplate LEAVES = create("leaves", new TextureSlot[] { TextureSlot.ALL });
/*     */   
/*  94 */   public static final ModelTemplate STAIRS_STRAIGHT = create("stairs", new TextureSlot[] { TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE });
/*     */   
/*  95 */   public static final ModelTemplate STAIRS_INNER = create("inner_stairs", "_inner", new TextureSlot[] { TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE });
/*     */   
/*  96 */   public static final ModelTemplate STAIRS_OUTER = create("outer_stairs", "_outer", new TextureSlot[] { TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE });
/*     */   
/*  98 */   public static final ModelTemplate TRAPDOOR_TOP = create("template_trapdoor_top", "_top", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  99 */   public static final ModelTemplate TRAPDOOR_BOTTOM = create("template_trapdoor_bottom", "_bottom", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 100 */   public static final ModelTemplate TRAPDOOR_OPEN = create("template_trapdoor_open", "_open", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 102 */   public static final ModelTemplate BED_HEAD = create("template_bed_head", new TextureSlot[] { TextureSlot.UP, TextureSlot.EAST, TextureSlot.WEST });
/*     */   
/* 103 */   public static final ModelTemplate BED_FOOT = create("template_bed_foot", new TextureSlot[] { TextureSlot.UP, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.SOUTH });
/*     */   
/* 105 */   public static final ModelTemplate ORIENTABLE_TRAPDOOR_TOP = create("template_orientable_trapdoor_top", "_top", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 106 */   public static final ModelTemplate ORIENTABLE_TRAPDOOR_BOTTOM = create("template_orientable_trapdoor_bottom", "_bottom", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 107 */   public static final ModelTemplate ORIENTABLE_TRAPDOOR_OPEN = create("template_orientable_trapdoor_open", "_open", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 109 */   public static final ModelTemplate POINTED_DRIPSTONE = create("pointed_dripstone", new TextureSlot[] { TextureSlot.CROSS });
/*     */   
/* 110 */   public static final ModelTemplate CROSS = create("cross", new TextureSlot[] { TextureSlot.CROSS });
/*     */   
/* 111 */   public static final ModelTemplate TINTED_CROSS = create("tinted_cross", new TextureSlot[] { TextureSlot.CROSS });
/*     */   
/* 112 */   public static final ModelTemplate CROSS_EMISSIVE = create("cross_emissive", new TextureSlot[] { TextureSlot.CROSS, TextureSlot.CROSS_EMISSIVE });
/*     */   
/* 114 */   public static final ModelTemplate FLOWER_POT_CROSS = create("flower_pot_cross", new TextureSlot[] { TextureSlot.PLANT });
/*     */   
/* 115 */   public static final ModelTemplate TINTED_FLOWER_POT_CROSS = create("tinted_flower_pot_cross", new TextureSlot[] { TextureSlot.PLANT });
/*     */   
/* 116 */   public static final ModelTemplate FLOWER_POT_CROSS_EMISSIVE = create("flower_pot_cross_emissive", new TextureSlot[] { TextureSlot.PLANT, TextureSlot.CROSS_EMISSIVE });
/*     */   
/* 118 */   public static final ModelTemplate RAIL_FLAT = create("rail_flat", new TextureSlot[] { TextureSlot.RAIL });
/*     */   
/* 119 */   public static final ModelTemplate RAIL_CURVED = create("rail_curved", "_corner", new TextureSlot[] { TextureSlot.RAIL });
/*     */   
/* 120 */   public static final ModelTemplate RAIL_RAISED_NE = create("template_rail_raised_ne", "_raised_ne", new TextureSlot[] { TextureSlot.RAIL });
/*     */   
/* 121 */   public static final ModelTemplate RAIL_RAISED_SW = create("template_rail_raised_sw", "_raised_sw", new TextureSlot[] { TextureSlot.RAIL });
/*     */   
/* 123 */   public static final ModelTemplate CARPET = create("carpet", new TextureSlot[] { TextureSlot.WOOL });
/*     */   
/* 124 */   public static final ModelTemplate MOSSY_CARPET_SIDE = create("mossy_carpet_side", new TextureSlot[] { TextureSlot.SIDE });
/*     */   
/* 126 */   public static final ModelTemplate FLOWERBED_1 = create("flowerbed_1", "_1", new TextureSlot[] { TextureSlot.FLOWERBED, TextureSlot.STEM });
/*     */   
/* 127 */   public static final ModelTemplate FLOWERBED_2 = create("flowerbed_2", "_2", new TextureSlot[] { TextureSlot.FLOWERBED, TextureSlot.STEM });
/*     */   
/* 128 */   public static final ModelTemplate FLOWERBED_3 = create("flowerbed_3", "_3", new TextureSlot[] { TextureSlot.FLOWERBED, TextureSlot.STEM });
/*     */   
/* 129 */   public static final ModelTemplate FLOWERBED_4 = create("flowerbed_4", "_4", new TextureSlot[] { TextureSlot.FLOWERBED, TextureSlot.STEM });
/*     */   
/* 131 */   public static final ModelTemplate LEAF_LITTER_1 = create("template_leaf_litter_1", "_1", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 132 */   public static final ModelTemplate LEAF_LITTER_2 = create("template_leaf_litter_2", "_2", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 133 */   public static final ModelTemplate LEAF_LITTER_3 = create("template_leaf_litter_3", "_3", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 134 */   public static final ModelTemplate LEAF_LITTER_4 = create("template_leaf_litter_4", "_4", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 136 */   public static final ModelTemplate CORAL_FAN = create("coral_fan", new TextureSlot[] { TextureSlot.FAN });
/*     */   
/* 137 */   public static final ModelTemplate CORAL_WALL_FAN = create("coral_wall_fan", new TextureSlot[] { TextureSlot.FAN });
/*     */   
/* 138 */   public static final ModelTemplate GLAZED_TERRACOTTA = create("template_glazed_terracotta", new TextureSlot[] { TextureSlot.PATTERN });
/*     */   
/* 139 */   public static final ModelTemplate CHORUS_FLOWER = create("template_chorus_flower", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 140 */   public static final ModelTemplate DAYLIGHT_DETECTOR = create("template_daylight_detector", new TextureSlot[] { TextureSlot.TOP, TextureSlot.SIDE });
/*     */   
/* 142 */   public static final ModelTemplate STAINED_GLASS_PANE_NOSIDE = create("template_glass_pane_noside", "_noside", new TextureSlot[] { TextureSlot.PANE });
/*     */   
/* 143 */   public static final ModelTemplate STAINED_GLASS_PANE_NOSIDE_ALT = create("template_glass_pane_noside_alt", "_noside_alt", new TextureSlot[] { TextureSlot.PANE });
/*     */   
/* 144 */   public static final ModelTemplate STAINED_GLASS_PANE_POST = create("template_glass_pane_post", "_post", new TextureSlot[] { TextureSlot.PANE, TextureSlot.EDGE });
/*     */   
/* 145 */   public static final ModelTemplate STAINED_GLASS_PANE_SIDE = create("template_glass_pane_side", "_side", new TextureSlot[] { TextureSlot.PANE, TextureSlot.EDGE });
/*     */   
/* 146 */   public static final ModelTemplate STAINED_GLASS_PANE_SIDE_ALT = create("template_glass_pane_side_alt", "_side_alt", new TextureSlot[] { TextureSlot.PANE, TextureSlot.EDGE });
/*     */   
/* 148 */   public static final ModelTemplate COMMAND_BLOCK = create("template_command_block", new TextureSlot[] { TextureSlot.FRONT, TextureSlot.BACK, TextureSlot.SIDE });
/*     */   
/* 150 */   public static final ModelTemplate CHISELED_BOOKSHELF_SLOT_TOP_LEFT = create("template_chiseled_bookshelf_slot_top_left", "_slot_top_left", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 151 */   public static final ModelTemplate CHISELED_BOOKSHELF_SLOT_TOP_MID = create("template_chiseled_bookshelf_slot_top_mid", "_slot_top_mid", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 152 */   public static final ModelTemplate CHISELED_BOOKSHELF_SLOT_TOP_RIGHT = create("template_chiseled_bookshelf_slot_top_right", "_slot_top_right", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 153 */   public static final ModelTemplate CHISELED_BOOKSHELF_SLOT_BOTTOM_LEFT = create("template_chiseled_bookshelf_slot_bottom_left", "_slot_bottom_left", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 154 */   public static final ModelTemplate CHISELED_BOOKSHELF_SLOT_BOTTOM_MID = create("template_chiseled_bookshelf_slot_bottom_mid", "_slot_bottom_mid", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 155 */   public static final ModelTemplate CHISELED_BOOKSHELF_SLOT_BOTTOM_RIGHT = create("template_chiseled_bookshelf_slot_bottom_right", "_slot_bottom_right", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 157 */   public static final ModelTemplate SHELF_BODY = create("template_shelf_body", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/* 158 */   public static final ModelTemplate SHELF_INVENTORY = create("template_shelf_inventory", "_inventory", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/* 159 */   public static final ModelTemplate SHELF_UNPOWERED = create("template_shelf_unpowered", "_unpowered", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/* 160 */   public static final ModelTemplate SHELF_UNCONNECTED = create("template_shelf_unconnected", "_unconnected", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/* 161 */   public static final ModelTemplate SHELF_LEFT = create("template_shelf_left", "_left", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/* 162 */   public static final ModelTemplate SHELF_CENTER = create("template_shelf_center", "_center", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/* 163 */   public static final ModelTemplate SHELF_RIGHT = create("template_shelf_right", "_right", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/* 165 */   public static final ModelTemplate ANVIL = create("template_anvil", new TextureSlot[] { TextureSlot.TOP });
/*     */   
/*     */   public static final ModelTemplate[] STEMS;
/*     */   
/*     */   static {
/* 166 */     STEMS = (ModelTemplate[])IntStream.range(0, 8).mapToObj(i -> create("stem_growth" + i, "_stage" + i, new TextureSlot[] { TextureSlot.STEM })).toArray(x$0 -> new ModelTemplate[x$0]);
/*     */   }
/*     */   
/* 167 */   public static final ModelTemplate ATTACHED_STEM = create("stem_fruit", new TextureSlot[] { TextureSlot.STEM, TextureSlot.UPPER_STEM });
/*     */   
/* 168 */   public static final ModelTemplate CROP = create("crop", new TextureSlot[] { TextureSlot.CROP });
/*     */   
/* 169 */   public static final ModelTemplate FARMLAND = create("template_farmland", new TextureSlot[] { TextureSlot.DIRT, TextureSlot.TOP });
/*     */   
/* 171 */   public static final ModelTemplate FIRE_FLOOR = create("template_fire_floor", new TextureSlot[] { TextureSlot.FIRE });
/*     */   
/* 172 */   public static final ModelTemplate FIRE_SIDE = create("template_fire_side", new TextureSlot[] { TextureSlot.FIRE });
/*     */   
/* 173 */   public static final ModelTemplate FIRE_SIDE_ALT = create("template_fire_side_alt", new TextureSlot[] { TextureSlot.FIRE });
/*     */   
/* 174 */   public static final ModelTemplate FIRE_UP = create("template_fire_up", new TextureSlot[] { TextureSlot.FIRE });
/*     */   
/* 175 */   public static final ModelTemplate FIRE_UP_ALT = create("template_fire_up_alt", new TextureSlot[] { TextureSlot.FIRE });
/*     */   
/* 177 */   public static final ModelTemplate CAMPFIRE = create("template_campfire", new TextureSlot[] { TextureSlot.FIRE, TextureSlot.LIT_LOG });
/*     */   
/* 179 */   public static final ModelTemplate LANTERN = create("template_lantern", new TextureSlot[] { TextureSlot.LANTERN });
/*     */   
/* 180 */   public static final ModelTemplate HANGING_LANTERN = create("template_hanging_lantern", "_hanging", new TextureSlot[] { TextureSlot.LANTERN });
/*     */   
/* 181 */   public static final ModelTemplate CHAIN = create("template_chain", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 183 */   public static final ModelTemplate BARS_CAP = create("template_bars_cap", "_cap", new TextureSlot[] { TextureSlot.BARS, TextureSlot.EDGE });
/*     */   
/* 184 */   public static final ModelTemplate BARS_CAP_ALT = create("template_bars_cap_alt", "_cap_alt", new TextureSlot[] { TextureSlot.BARS, TextureSlot.EDGE });
/*     */   
/* 185 */   public static final ModelTemplate BARS_POST = create("template_bars_post", "_post", new TextureSlot[] { TextureSlot.BARS, TextureSlot.EDGE });
/*     */   
/* 186 */   public static final ModelTemplate BARS_POST_ENDS = create("template_bars_post_ends", "_post_ends", new TextureSlot[] { TextureSlot.BARS, TextureSlot.EDGE });
/*     */   
/* 187 */   public static final ModelTemplate BARS_POST_SIDE = create("template_bars_side", "_side", new TextureSlot[] { TextureSlot.BARS, TextureSlot.EDGE });
/*     */   
/* 188 */   public static final ModelTemplate BARS_POST_SIDE_ALT = create("template_bars_side_alt", "_side_alt", new TextureSlot[] { TextureSlot.BARS, TextureSlot.EDGE });
/*     */   
/* 190 */   public static final ModelTemplate TORCH = create("template_torch", new TextureSlot[] { TextureSlot.TORCH });
/*     */   
/* 191 */   public static final ModelTemplate TORCH_UNLIT = create("template_torch_unlit", new TextureSlot[] { TextureSlot.TORCH });
/*     */   
/* 192 */   public static final ModelTemplate WALL_TORCH = create("template_torch_wall", new TextureSlot[] { TextureSlot.TORCH });
/*     */   
/* 193 */   public static final ModelTemplate WALL_TORCH_UNLIT = create("template_torch_wall_unlit", new TextureSlot[] { TextureSlot.TORCH });
/*     */   
/* 194 */   public static final ModelTemplate REDSTONE_TORCH = create("template_redstone_torch", new TextureSlot[] { TextureSlot.TORCH });
/*     */   
/* 195 */   public static final ModelTemplate REDSTONE_WALL_TORCH = create("template_redstone_torch_wall", new TextureSlot[] { TextureSlot.TORCH });
/*     */   
/* 197 */   public static final ModelTemplate PISTON = create("template_piston", new TextureSlot[] { TextureSlot.PLATFORM, TextureSlot.BOTTOM, TextureSlot.SIDE });
/*     */   
/* 198 */   public static final ModelTemplate PISTON_HEAD = create("template_piston_head", new TextureSlot[] { TextureSlot.PLATFORM, TextureSlot.SIDE, TextureSlot.UNSTICKY });
/*     */   
/* 199 */   public static final ModelTemplate PISTON_HEAD_SHORT = create("template_piston_head_short", new TextureSlot[] { TextureSlot.PLATFORM, TextureSlot.SIDE, TextureSlot.UNSTICKY });
/*     */   
/* 200 */   public static final ModelTemplate SEAGRASS = create("template_seagrass", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 201 */   public static final ModelTemplate TURTLE_EGG = create("template_turtle_egg", new TextureSlot[] { TextureSlot.ALL });
/*     */   
/* 202 */   public static final ModelTemplate DRIED_GHAST = create("dried_ghast", new TextureSlot[] { TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.TENTACLES });
/*     */   
/* 203 */   public static final ModelTemplate TWO_TURTLE_EGGS = create("template_two_turtle_eggs", new TextureSlot[] { TextureSlot.ALL });
/*     */   
/* 204 */   public static final ModelTemplate THREE_TURTLE_EGGS = create("template_three_turtle_eggs", new TextureSlot[] { TextureSlot.ALL });
/*     */   
/* 205 */   public static final ModelTemplate FOUR_TURTLE_EGGS = create("template_four_turtle_eggs", new TextureSlot[] { TextureSlot.ALL });
/*     */   
/* 206 */   public static final ModelTemplate SINGLE_FACE = create("template_single_face", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 207 */   public static final ModelTemplate CAULDRON_LEVEL1 = create("template_cauldron_level1", new TextureSlot[] { TextureSlot.CONTENT, TextureSlot.INSIDE, TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE });
/*     */   
/* 208 */   public static final ModelTemplate CAULDRON_LEVEL2 = create("template_cauldron_level2", new TextureSlot[] { TextureSlot.CONTENT, TextureSlot.INSIDE, TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE });
/*     */   
/* 209 */   public static final ModelTemplate CAULDRON_FULL = create("template_cauldron_full", new TextureSlot[] { TextureSlot.CONTENT, TextureSlot.INSIDE, TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE });
/*     */   
/* 210 */   public static final ModelTemplate AZALEA = create("template_azalea", new TextureSlot[] { TextureSlot.TOP, TextureSlot.SIDE });
/*     */   
/* 211 */   public static final ModelTemplate POTTED_AZALEA = create("template_potted_azalea_bush", new TextureSlot[] { TextureSlot.PLANT, TextureSlot.TOP, TextureSlot.SIDE });
/*     */   
/* 212 */   public static final ModelTemplate POTTED_FLOWERING_AZALEA = create("template_potted_azalea_bush", new TextureSlot[] { TextureSlot.PLANT, TextureSlot.TOP, TextureSlot.SIDE });
/*     */   
/* 213 */   public static final ModelTemplate SNIFFER_EGG = create("sniffer_egg", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST });
/*     */   
/* 215 */   public static final ModelTemplate FLAT_ITEM = createItem("generated", new TextureSlot[] { TextureSlot.LAYER0 });
/*     */   
/* 216 */   public static final ModelTemplate MUSIC_DISC = createItem("template_music_disc", new TextureSlot[] { TextureSlot.LAYER0 });
/*     */   
/* 217 */   public static final ModelTemplate FLAT_HANDHELD_ITEM = createItem("handheld", new TextureSlot[] { TextureSlot.LAYER0 });
/*     */   
/* 218 */   public static final ModelTemplate FLAT_HANDHELD_ROD_ITEM = createItem("handheld_rod", new TextureSlot[] { TextureSlot.LAYER0 });
/*     */   
/* 219 */   public static final ModelTemplate TWO_LAYERED_ITEM = createItem("generated", new TextureSlot[] { TextureSlot.LAYER0, TextureSlot.LAYER1 });
/*     */   
/* 220 */   public static final ModelTemplate THREE_LAYERED_ITEM = createItem("generated", new TextureSlot[] { TextureSlot.LAYER0, TextureSlot.LAYER1, TextureSlot.LAYER2 });
/*     */   
/* 221 */   public static final ModelTemplate SHULKER_BOX_INVENTORY = createItem("template_shulker_box", new TextureSlot[] { TextureSlot.PARTICLE });
/*     */   
/* 222 */   public static final ModelTemplate CHEST_INVENTORY = createItem("template_chest", new TextureSlot[] { TextureSlot.PARTICLE });
/*     */   
/* 223 */   public static final ModelTemplate BUNDLE_OPEN_FRONT_INVENTORY = createItem("template_bundle_open_front", "_open_front", new TextureSlot[] { TextureSlot.LAYER0 });
/*     */   
/* 224 */   public static final ModelTemplate BUNDLE_OPEN_BACK_INVENTORY = createItem("template_bundle_open_back", "_open_back", new TextureSlot[] { TextureSlot.LAYER0 });
/*     */   
/* 225 */   public static final ModelTemplate BOW = createItem("bow", new TextureSlot[] { TextureSlot.LAYER0 });
/*     */   
/* 226 */   public static final ModelTemplate CROSSBOW = createItem("crossbow", new TextureSlot[] { TextureSlot.LAYER0 });
/*     */   
/* 227 */   public static final ModelTemplate SPEAR_IN_HAND = createItem("spear_in_hand", "_in_hand", new TextureSlot[] { TextureSlot.LAYER0 });
/*     */   
/* 229 */   public static final ModelTemplate CANDLE = create("template_candle", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/* 230 */   public static final ModelTemplate TWO_CANDLES = create("template_two_candles", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/* 231 */   public static final ModelTemplate THREE_CANDLES = create("template_three_candles", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/* 232 */   public static final ModelTemplate FOUR_CANDLES = create("template_four_candles", new TextureSlot[] { TextureSlot.ALL, TextureSlot.PARTICLE });
/*     */   
/* 234 */   public static final ModelTemplate CANDLE_CAKE = create("template_cake_with_candle", new TextureSlot[] { TextureSlot.CANDLE, TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.TOP, TextureSlot.PARTICLE });
/*     */   
/* 235 */   public static final ModelTemplate SCULK_SHRIEKER = create("template_sculk_shrieker", new TextureSlot[] { TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.TOP, TextureSlot.PARTICLE, TextureSlot.INNER_TOP });
/*     */   
/* 236 */   public static final ModelTemplate VAULT = create("template_vault", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.FRONT });
/*     */   
/* 238 */   public static final ModelTemplate FLAT_HANDHELD_MACE_ITEM = createItem("handheld_mace", new TextureSlot[] { TextureSlot.LAYER0 });
/*     */   
/* 240 */   public static final ModelTemplate LIGHTNING_ROD = create("template_lightning_rod", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*     */   private static ModelTemplate create(TextureSlot... slots) {
/* 243 */     return new ModelTemplate(Optional.empty(), Optional.empty(), slots);
/*     */   }
/*     */   
/*     */   private static ModelTemplate create(String id, TextureSlot... slots) {
/* 247 */     return new ModelTemplate(Optional.of(Identifier.withDefaultNamespace("block/" + id)), Optional.empty(), slots);
/*     */   }
/*     */   
/*     */   private static ModelTemplate createItem(String id, TextureSlot... slots) {
/* 251 */     return new ModelTemplate(Optional.of(Identifier.withDefaultNamespace("item/" + id)), Optional.empty(), slots);
/*     */   }
/*     */   
/*     */   private static ModelTemplate createItem(String id, String suffix, TextureSlot... slots) {
/* 255 */     return new ModelTemplate(Optional.of(Identifier.withDefaultNamespace("item/" + id)), Optional.of(suffix), slots);
/*     */   }
/*     */   
/*     */   private static ModelTemplate create(String id, String suffix, TextureSlot... slots) {
/* 259 */     return new ModelTemplate(Optional.of(Identifier.withDefaultNamespace("block/" + id)), Optional.of(suffix), slots);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\model\ModelTemplates.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */