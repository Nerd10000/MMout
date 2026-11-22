/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.level.block.SoundType;
/*    */ 
/*    */ public final class WoodType extends Record {
/*    */   private final String name;
/*    */   
/*    */   private final BlockSetType setType;
/*    */   
/*    */   private final SoundType soundType;
/*    */   
/*    */   private final SoundType hangingSignSoundType;
/*    */   
/*    */   private final SoundEvent fenceGateClose;
/*    */   
/*    */   private final SoundEvent fenceGateOpen;
/*    */   
/*    */   public WoodType(String name, BlockSetType setType, SoundType soundType, SoundType hangingSignSoundType, SoundEvent fenceGateClose, SoundEvent fenceGateOpen) {
/* 12 */     this.name = name;
/* 12 */     this.setType = setType;
/* 12 */     this.soundType = soundType;
/* 12 */     this.hangingSignSoundType = hangingSignSoundType;
/* 12 */     this.fenceGateClose = fenceGateClose;
/* 12 */     this.fenceGateOpen = fenceGateOpen;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/state/properties/WoodType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/WoodType;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/state/properties/WoodType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/WoodType;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/state/properties/WoodType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/state/properties/WoodType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String name() {
/* 12 */     return this.name;
/*    */   }
/*    */   
/*    */   public BlockSetType setType() {
/* 12 */     return this.setType;
/*    */   }
/*    */   
/*    */   public SoundType soundType() {
/* 12 */     return this.soundType;
/*    */   }
/*    */   
/*    */   public SoundType hangingSignSoundType() {
/* 12 */     return this.hangingSignSoundType;
/*    */   }
/*    */   
/*    */   public SoundEvent fenceGateClose() {
/* 12 */     return this.fenceGateClose;
/*    */   }
/*    */   
/*    */   public SoundEvent fenceGateOpen() {
/* 12 */     return this.fenceGateOpen;
/*    */   }
/*    */   
/* 20 */   private static final Map<String, WoodType> TYPES = (Map<String, WoodType>)new Object2ObjectArrayMap();
/*    */   
/* 21 */   public static final Codec<WoodType> CODEC = Codec.stringResolver(WoodType::name, TYPES::get);
/*    */   
/*    */   static {
/* 21 */     Objects.requireNonNull(TYPES);
/*    */   }
/*    */   
/* 23 */   public static final WoodType OAK = register(new WoodType("oak", BlockSetType.OAK));
/*    */   
/* 24 */   public static final WoodType SPRUCE = register(new WoodType("spruce", BlockSetType.SPRUCE));
/*    */   
/* 25 */   public static final WoodType BIRCH = register(new WoodType("birch", BlockSetType.BIRCH));
/*    */   
/* 26 */   public static final WoodType ACACIA = register(new WoodType("acacia", BlockSetType.ACACIA));
/*    */   
/* 27 */   public static final WoodType CHERRY = register(new WoodType("cherry", BlockSetType.CHERRY, SoundType.CHERRY_WOOD, SoundType.CHERRY_WOOD_HANGING_SIGN, SoundEvents.CHERRY_WOOD_FENCE_GATE_CLOSE, SoundEvents.CHERRY_WOOD_FENCE_GATE_OPEN));
/*    */   
/* 35 */   public static final WoodType JUNGLE = register(new WoodType("jungle", BlockSetType.JUNGLE));
/*    */   
/* 36 */   public static final WoodType DARK_OAK = register(new WoodType("dark_oak", BlockSetType.DARK_OAK));
/*    */   
/* 37 */   public static final WoodType PALE_OAK = register(new WoodType("pale_oak", BlockSetType.PALE_OAK));
/*    */   
/* 38 */   public static final WoodType CRIMSON = register(new WoodType("crimson", BlockSetType.CRIMSON, SoundType.NETHER_WOOD, SoundType.NETHER_WOOD_HANGING_SIGN, SoundEvents.NETHER_WOOD_FENCE_GATE_CLOSE, SoundEvents.NETHER_WOOD_FENCE_GATE_OPEN));
/*    */   
/* 46 */   public static final WoodType WARPED = register(new WoodType("warped", BlockSetType.WARPED, SoundType.NETHER_WOOD, SoundType.NETHER_WOOD_HANGING_SIGN, SoundEvents.NETHER_WOOD_FENCE_GATE_CLOSE, SoundEvents.NETHER_WOOD_FENCE_GATE_OPEN));
/*    */   
/* 54 */   public static final WoodType MANGROVE = register(new WoodType("mangrove", BlockSetType.MANGROVE));
/*    */   
/* 55 */   public static final WoodType BAMBOO = register(new WoodType("bamboo", BlockSetType.BAMBOO, SoundType.BAMBOO_WOOD, SoundType.BAMBOO_WOOD_HANGING_SIGN, SoundEvents.BAMBOO_WOOD_FENCE_GATE_CLOSE, SoundEvents.BAMBOO_WOOD_FENCE_GATE_OPEN));
/*    */   
/*    */   public WoodType(String name, BlockSetType setType) {
/* 65 */     this(name, setType, SoundType.WOOD, SoundType.HANGING_SIGN, SoundEvents.FENCE_GATE_CLOSE, SoundEvents.FENCE_GATE_OPEN);
/*    */   }
/*    */   
/*    */   private static WoodType register(WoodType type) {
/* 76 */     TYPES.put(type.name(), type);
/* 77 */     return type;
/*    */   }
/*    */   
/*    */   public static Stream<WoodType> values() {
/* 81 */     return TYPES.values().stream();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\WoodType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */