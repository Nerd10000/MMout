/*   */ package net.minecraft.world.level.levelgen.feature.configurations;
/*   */ 
/*   */ import com.mojang.datafixers.kinds.App;
/*   */ import com.mojang.datafixers.kinds.Applicative;
/*   */ import com.mojang.datafixers.util.Function4;
/*   */ import com.mojang.serialization.Codec;
/*   */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*   */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*   */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*   */ 
/*   */ public final class HugeMushroomFeatureConfiguration extends Record implements FeatureConfiguration {
/*   */   private final BlockStateProvider capProvider;
/*   */   
/*   */   private final BlockStateProvider stemProvider;
/*   */   
/*   */   private final int foliageRadius;
/*   */   
/*   */   private final BlockPredicate canPlaceOn;
/*   */   
/*   */   public static final Codec<HugeMushroomFeatureConfiguration> CODEC;
/*   */   
/*   */   public HugeMushroomFeatureConfiguration(BlockStateProvider capProvider, BlockStateProvider stemProvider, int foliageRadius, BlockPredicate canPlaceOn) {
/* 8 */     this.capProvider = capProvider;
/* 8 */     this.stemProvider = stemProvider;
/* 8 */     this.foliageRadius = foliageRadius;
/* 8 */     this.canPlaceOn = canPlaceOn;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/HugeMushroomFeatureConfiguration;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/HugeMushroomFeatureConfiguration;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/HugeMushroomFeatureConfiguration;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/HugeMushroomFeatureConfiguration;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/HugeMushroomFeatureConfiguration;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/HugeMushroomFeatureConfiguration;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public BlockStateProvider capProvider() {
/* 8 */     return this.capProvider;
/*   */   }
/*   */   
/*   */   public BlockStateProvider stemProvider() {
/* 8 */     return this.stemProvider;
/*   */   }
/*   */   
/*   */   public int foliageRadius() {
/* 8 */     return this.foliageRadius;
/*   */   }
/*   */   
/*   */   public BlockPredicate canPlaceOn() {
/* 8 */     return this.canPlaceOn;
/*   */   }
/*   */   
/*   */   static {
/* 9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter(()), (App)Codec.INT.optionalFieldOf("foliage_radius", Integer.valueOf(2)).forGetter(()), (App)BlockPredicate.CODEC.fieldOf("can_place_on").forGetter(())).apply((Applicative)i, HugeMushroomFeatureConfiguration::new));
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\HugeMushroomFeatureConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */