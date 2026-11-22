/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public final class Configuration extends Record implements FeatureConfiguration {
/*    */   private final BlockStateProvider fluid;
/*    */   
/*    */   private final BlockStateProvider barrier;
/*    */   
/*    */   private final BlockPredicate canPlaceFeature;
/*    */   
/*    */   private final BlockPredicate canReplaceWithAirOrFluid;
/*    */   
/*    */   private final BlockPredicate canReplaceWithBarrier;
/*    */   
/*    */   public static final Codec<Configuration> CODEC;
/*    */   
/*    */   public Configuration(BlockStateProvider fluid, BlockStateProvider barrier, BlockPredicate canPlaceFeature, BlockPredicate canReplaceWithAirOrFluid, BlockPredicate canReplaceWithBarrier) {
/* 19 */     this.fluid = fluid;
/* 19 */     this.barrier = barrier;
/* 19 */     this.canPlaceFeature = canPlaceFeature;
/* 19 */     this.canReplaceWithAirOrFluid = canReplaceWithAirOrFluid;
/* 19 */     this.canReplaceWithBarrier = canReplaceWithBarrier;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockStateProvider fluid() {
/* 19 */     return this.fluid;
/*    */   }
/*    */   
/*    */   public BlockStateProvider barrier() {
/* 19 */     return this.barrier;
/*    */   }
/*    */   
/*    */   public BlockPredicate canPlaceFeature() {
/* 19 */     return this.canPlaceFeature;
/*    */   }
/*    */   
/*    */   public BlockPredicate canReplaceWithAirOrFluid() {
/* 19 */     return this.canReplaceWithAirOrFluid;
/*    */   }
/*    */   
/*    */   public BlockPredicate canReplaceWithBarrier() {
/* 19 */     return this.canReplaceWithBarrier;
/*    */   }
/*    */   
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("fluid").forGetter(Configuration::fluid), (App)BlockStateProvider.CODEC.fieldOf("barrier").forGetter(Configuration::barrier), (App)BlockPredicate.CODEC.fieldOf("can_place_feature").forGetter(Configuration::canPlaceFeature), (App)BlockPredicate.CODEC.fieldOf("can_replace_with_air_or_fluid").forGetter(Configuration::canReplaceWithAirOrFluid), (App)BlockPredicate.CODEC.fieldOf("can_replace_with_barrier").forGetter(Configuration::canReplaceWithBarrier)).apply((Applicative)i, Configuration::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\LakeFeature$Configuration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */