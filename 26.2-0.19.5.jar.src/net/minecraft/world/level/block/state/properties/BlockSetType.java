/*     */ package net.minecraft.world.level.block.state.properties;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ 
/*     */ public final class BlockSetType extends Record {
/*     */   private final String name;
/*     */   
/*     */   private final boolean canOpenByHand;
/*     */   
/*     */   private final boolean canOpenByWindCharge;
/*     */   
/*     */   private final boolean canButtonBeActivatedByArrows;
/*     */   
/*     */   private final PressurePlateSensitivity pressurePlateSensitivity;
/*     */   
/*     */   private final SoundType soundType;
/*     */   
/*     */   private final SoundEvent doorClose;
/*     */   
/*     */   private final SoundEvent doorOpen;
/*     */   
/*     */   private final SoundEvent trapdoorClose;
/*     */   
/*     */   private final SoundEvent trapdoorOpen;
/*     */   
/*     */   private final SoundEvent pressurePlateClickOff;
/*     */   
/*     */   private final SoundEvent pressurePlateClickOn;
/*     */   
/*     */   private final SoundEvent buttonClickOff;
/*     */   
/*     */   private final SoundEvent buttonClickOn;
/*     */   
/*     */   public BlockSetType(String name, boolean canOpenByHand, boolean canOpenByWindCharge, boolean canButtonBeActivatedByArrows, PressurePlateSensitivity pressurePlateSensitivity, SoundType soundType, SoundEvent doorClose, SoundEvent doorOpen, SoundEvent trapdoorClose, SoundEvent trapdoorOpen, SoundEvent pressurePlateClickOff, SoundEvent pressurePlateClickOn, SoundEvent buttonClickOff, SoundEvent buttonClickOn) {
/*  12 */     this.name = name;
/*  12 */     this.canOpenByHand = canOpenByHand;
/*  12 */     this.canOpenByWindCharge = canOpenByWindCharge;
/*  12 */     this.canButtonBeActivatedByArrows = canButtonBeActivatedByArrows;
/*  12 */     this.pressurePlateSensitivity = pressurePlateSensitivity;
/*  12 */     this.soundType = soundType;
/*  12 */     this.doorClose = doorClose;
/*  12 */     this.doorOpen = doorOpen;
/*  12 */     this.trapdoorClose = trapdoorClose;
/*  12 */     this.trapdoorOpen = trapdoorOpen;
/*  12 */     this.pressurePlateClickOff = pressurePlateClickOff;
/*  12 */     this.pressurePlateClickOn = pressurePlateClickOn;
/*  12 */     this.buttonClickOff = buttonClickOff;
/*  12 */     this.buttonClickOn = buttonClickOn;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/state/properties/BlockSetType;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/BlockSetType;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/state/properties/BlockSetType;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/BlockSetType;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/state/properties/BlockSetType;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/state/properties/BlockSetType;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String name() {
/*  12 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean canOpenByHand() {
/*  12 */     return this.canOpenByHand;
/*     */   }
/*     */   
/*     */   public boolean canOpenByWindCharge() {
/*  12 */     return this.canOpenByWindCharge;
/*     */   }
/*     */   
/*     */   public boolean canButtonBeActivatedByArrows() {
/*  12 */     return this.canButtonBeActivatedByArrows;
/*     */   }
/*     */   
/*     */   public PressurePlateSensitivity pressurePlateSensitivity() {
/*  12 */     return this.pressurePlateSensitivity;
/*     */   }
/*     */   
/*     */   public SoundType soundType() {
/*  12 */     return this.soundType;
/*     */   }
/*     */   
/*     */   public SoundEvent doorClose() {
/*  12 */     return this.doorClose;
/*     */   }
/*     */   
/*     */   public SoundEvent doorOpen() {
/*  12 */     return this.doorOpen;
/*     */   }
/*     */   
/*     */   public SoundEvent trapdoorClose() {
/*  12 */     return this.trapdoorClose;
/*     */   }
/*     */   
/*     */   public SoundEvent trapdoorOpen() {
/*  12 */     return this.trapdoorOpen;
/*     */   }
/*     */   
/*     */   public SoundEvent pressurePlateClickOff() {
/*  12 */     return this.pressurePlateClickOff;
/*     */   }
/*     */   
/*     */   public SoundEvent pressurePlateClickOn() {
/*  12 */     return this.pressurePlateClickOn;
/*     */   }
/*     */   
/*     */   public SoundEvent buttonClickOff() {
/*  12 */     return this.buttonClickOff;
/*     */   }
/*     */   
/*     */   public SoundEvent buttonClickOn() {
/*  12 */     return this.buttonClickOn;
/*     */   }
/*     */   
/*  28 */   private static final Map<String, BlockSetType> TYPES = (Map<String, BlockSetType>)new Object2ObjectArrayMap();
/*     */   
/*  29 */   public static final Codec<BlockSetType> CODEC = Codec.stringResolver(BlockSetType::name, TYPES::get);
/*     */   
/*     */   static {
/*  29 */     Objects.requireNonNull(TYPES);
/*     */   }
/*     */   
/*  31 */   public static final BlockSetType IRON = register(new BlockSetType("iron", false, false, false, PressurePlateSensitivity.EVERYTHING, SoundType.IRON, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
/*     */   
/*  50 */   public static final BlockSetType COPPER = register(new BlockSetType("copper", true, true, false, PressurePlateSensitivity.EVERYTHING, SoundType.COPPER, SoundEvents.COPPER_DOOR_CLOSE, SoundEvents.COPPER_DOOR_OPEN, SoundEvents.COPPER_TRAPDOOR_CLOSE, SoundEvents.COPPER_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
/*     */   
/*  70 */   public static final BlockSetType GOLD = register(new BlockSetType("gold", false, true, false, PressurePlateSensitivity.EVERYTHING, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
/*     */   
/*  94 */   public static final BlockSetType STONE = register(new BlockSetType("stone", true, true, false, PressurePlateSensitivity.MOBS, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
/*     */   
/* 115 */   public static final BlockSetType POLISHED_BLACKSTONE = register(new BlockSetType("polished_blackstone", true, true, false, PressurePlateSensitivity.MOBS, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
/*     */   
/* 139 */   public static final BlockSetType OAK = register(new BlockSetType("oak"));
/*     */   
/* 140 */   public static final BlockSetType SPRUCE = register(new BlockSetType("spruce"));
/*     */   
/* 141 */   public static final BlockSetType BIRCH = register(new BlockSetType("birch"));
/*     */   
/* 142 */   public static final BlockSetType ACACIA = register(new BlockSetType("acacia"));
/*     */   
/* 143 */   public static final BlockSetType CHERRY = register(new BlockSetType("cherry", true, true, true, PressurePlateSensitivity.EVERYTHING, SoundType.CHERRY_WOOD, SoundEvents.CHERRY_WOOD_DOOR_CLOSE, SoundEvents.CHERRY_WOOD_DOOR_OPEN, SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE, SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN, SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.CHERRY_WOOD_BUTTON_CLICK_OFF, SoundEvents.CHERRY_WOOD_BUTTON_CLICK_ON));
/*     */   
/* 159 */   public static final BlockSetType JUNGLE = register(new BlockSetType("jungle"));
/*     */   
/* 160 */   public static final BlockSetType DARK_OAK = register(new BlockSetType("dark_oak"));
/*     */   
/* 161 */   public static final BlockSetType PALE_OAK = register(new BlockSetType("pale_oak"));
/*     */   
/* 162 */   public static final BlockSetType CRIMSON = register(new BlockSetType("crimson", true, true, true, PressurePlateSensitivity.EVERYTHING, SoundType.NETHER_WOOD, SoundEvents.NETHER_WOOD_DOOR_CLOSE, SoundEvents.NETHER_WOOD_DOOR_OPEN, SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE, SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF, SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON));
/*     */   
/* 178 */   public static final BlockSetType WARPED = register(new BlockSetType("warped", true, true, true, PressurePlateSensitivity.EVERYTHING, SoundType.NETHER_WOOD, SoundEvents.NETHER_WOOD_DOOR_CLOSE, SoundEvents.NETHER_WOOD_DOOR_OPEN, SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE, SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF, SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON));
/*     */   
/* 194 */   public static final BlockSetType MANGROVE = register(new BlockSetType("mangrove"));
/*     */   
/* 195 */   public static final BlockSetType BAMBOO = register(new BlockSetType("bamboo", true, true, true, PressurePlateSensitivity.EVERYTHING, SoundType.BAMBOO_WOOD, SoundEvents.BAMBOO_WOOD_DOOR_CLOSE, SoundEvents.BAMBOO_WOOD_DOOR_OPEN, SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE, SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN, SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_OFF, SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON));
/*     */   
/*     */   public BlockSetType(String name) {
/* 213 */     this(name, true, true, true, PressurePlateSensitivity.EVERYTHING, SoundType.WOOD, SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON);
/*     */   }
/*     */   
/*     */   private static BlockSetType register(BlockSetType type) {
/* 232 */     TYPES.put(type.name, type);
/* 233 */     return type;
/*     */   }
/*     */   
/*     */   public static Stream<BlockSetType> values() {
/* 237 */     return TYPES.values().stream();
/*     */   }
/*     */   
/*     */   public enum PressurePlateSensitivity {
/* 241 */     EVERYTHING, MOBS;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\BlockSetType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */