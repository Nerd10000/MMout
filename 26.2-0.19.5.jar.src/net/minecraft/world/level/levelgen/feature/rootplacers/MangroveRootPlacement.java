/*    */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public final class MangroveRootPlacement extends Record {
/*    */   private final HolderSet<Block> canGrowThrough;
/*    */   
/*    */   private final HolderSet<Block> muddyRootsIn;
/*    */   
/*    */   private final BlockStateProvider muddyRootsProvider;
/*    */   
/*    */   private final int maxRootWidth;
/*    */   
/*    */   private final int maxRootLength;
/*    */   
/*    */   private final float randomSkewChance;
/*    */   
/*    */   public static final Codec<MangroveRootPlacement> CODEC;
/*    */   
/*    */   public MangroveRootPlacement(HolderSet<Block> canGrowThrough, HolderSet<Block> muddyRootsIn, BlockStateProvider muddyRootsProvider, int maxRootWidth, int maxRootLength, float randomSkewChance) {
/* 11 */     this.canGrowThrough = canGrowThrough;
/* 11 */     this.muddyRootsIn = muddyRootsIn;
/* 11 */     this.muddyRootsProvider = muddyRootsProvider;
/* 11 */     this.maxRootWidth = maxRootWidth;
/* 11 */     this.maxRootLength = maxRootLength;
/* 11 */     this.randomSkewChance = randomSkewChance;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderSet<Block> canGrowThrough() {
/* 11 */     return this.canGrowThrough;
/*    */   }
/*    */   
/*    */   public HolderSet<Block> muddyRootsIn() {
/* 11 */     return this.muddyRootsIn;
/*    */   }
/*    */   
/*    */   public BlockStateProvider muddyRootsProvider() {
/* 11 */     return this.muddyRootsProvider;
/*    */   }
/*    */   
/*    */   public int maxRootWidth() {
/* 11 */     return this.maxRootWidth;
/*    */   }
/*    */   
/*    */   public int maxRootLength() {
/* 11 */     return this.maxRootLength;
/*    */   }
/*    */   
/*    */   public float randomSkewChance() {
/* 11 */     return this.randomSkewChance;
/*    */   }
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_grow_through").forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("muddy_roots_in").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("muddy_roots_provider").forGetter(()), (App)Codec.intRange(1, 12).fieldOf("max_root_width").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("max_root_length").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("random_skew_chance").forGetter(())).apply((Applicative)i, MangroveRootPlacement::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\rootplacers\MangroveRootPlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */