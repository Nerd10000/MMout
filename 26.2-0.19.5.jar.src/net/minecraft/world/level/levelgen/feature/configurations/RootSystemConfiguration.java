/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function15;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public final class RootSystemConfiguration extends Record implements FeatureConfiguration {
/*    */   private final Holder<PlacedFeature> treeFeature;
/*    */   
/*    */   private final int requiredVerticalSpaceForTree;
/*    */   
/*    */   private final int levelTestDistance;
/*    */   
/*    */   private final int maxLevelDeviation;
/*    */   
/*    */   private final int rootRadius;
/*    */   
/*    */   private final HolderSet<Block> rootReplaceable;
/*    */   
/*    */   private final BlockStateProvider rootStateProvider;
/*    */   
/*    */   private final int rootPlacementAttempts;
/*    */   
/*    */   private final int rootColumnMaxHeight;
/*    */   
/*    */   private final int hangingRootRadius;
/*    */   
/*    */   private final int hangingRootsVerticalSpan;
/*    */   
/*    */   private final BlockStateProvider hangingRootStateProvider;
/*    */   
/*    */   private final int hangingRootPlacementAttempts;
/*    */   
/*    */   private final int allowedVerticalWaterForTree;
/*    */   
/*    */   private final BlockPredicate allowedTreePosition;
/*    */   
/*    */   public static final Codec<RootSystemConfiguration> CODEC;
/*    */   
/*    */   public RootSystemConfiguration(Holder<PlacedFeature> treeFeature, int requiredVerticalSpaceForTree, int levelTestDistance, int maxLevelDeviation, int rootRadius, HolderSet<Block> rootReplaceable, BlockStateProvider rootStateProvider, int rootPlacementAttempts, int rootColumnMaxHeight, int hangingRootRadius, int hangingRootsVerticalSpan, BlockStateProvider hangingRootStateProvider, int hangingRootPlacementAttempts, int allowedVerticalWaterForTree, BlockPredicate allowedTreePosition) {
/* 14 */     this.treeFeature = treeFeature;
/* 14 */     this.requiredVerticalSpaceForTree = requiredVerticalSpaceForTree;
/* 14 */     this.levelTestDistance = levelTestDistance;
/* 14 */     this.maxLevelDeviation = maxLevelDeviation;
/* 14 */     this.rootRadius = rootRadius;
/* 14 */     this.rootReplaceable = rootReplaceable;
/* 14 */     this.rootStateProvider = rootStateProvider;
/* 14 */     this.rootPlacementAttempts = rootPlacementAttempts;
/* 14 */     this.rootColumnMaxHeight = rootColumnMaxHeight;
/* 14 */     this.hangingRootRadius = hangingRootRadius;
/* 14 */     this.hangingRootsVerticalSpan = hangingRootsVerticalSpan;
/* 14 */     this.hangingRootStateProvider = hangingRootStateProvider;
/* 14 */     this.hangingRootPlacementAttempts = hangingRootPlacementAttempts;
/* 14 */     this.allowedVerticalWaterForTree = allowedVerticalWaterForTree;
/* 14 */     this.allowedTreePosition = allowedTreePosition;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/RootSystemConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/RootSystemConfiguration;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/RootSystemConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/RootSystemConfiguration;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/RootSystemConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/RootSystemConfiguration;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<PlacedFeature> treeFeature() {
/* 14 */     return this.treeFeature;
/*    */   }
/*    */   
/*    */   public int requiredVerticalSpaceForTree() {
/* 14 */     return this.requiredVerticalSpaceForTree;
/*    */   }
/*    */   
/*    */   public int levelTestDistance() {
/* 14 */     return this.levelTestDistance;
/*    */   }
/*    */   
/*    */   public int maxLevelDeviation() {
/* 14 */     return this.maxLevelDeviation;
/*    */   }
/*    */   
/*    */   public int rootRadius() {
/* 14 */     return this.rootRadius;
/*    */   }
/*    */   
/*    */   public HolderSet<Block> rootReplaceable() {
/* 14 */     return this.rootReplaceable;
/*    */   }
/*    */   
/*    */   public BlockStateProvider rootStateProvider() {
/* 14 */     return this.rootStateProvider;
/*    */   }
/*    */   
/*    */   public int rootPlacementAttempts() {
/* 14 */     return this.rootPlacementAttempts;
/*    */   }
/*    */   
/*    */   public int rootColumnMaxHeight() {
/* 14 */     return this.rootColumnMaxHeight;
/*    */   }
/*    */   
/*    */   public int hangingRootRadius() {
/* 14 */     return this.hangingRootRadius;
/*    */   }
/*    */   
/*    */   public int hangingRootsVerticalSpan() {
/* 14 */     return this.hangingRootsVerticalSpan;
/*    */   }
/*    */   
/*    */   public BlockStateProvider hangingRootStateProvider() {
/* 14 */     return this.hangingRootStateProvider;
/*    */   }
/*    */   
/*    */   public int hangingRootPlacementAttempts() {
/* 14 */     return this.hangingRootPlacementAttempts;
/*    */   }
/*    */   
/*    */   public int allowedVerticalWaterForTree() {
/* 14 */     return this.allowedVerticalWaterForTree;
/*    */   }
/*    */   
/*    */   public BlockPredicate allowedTreePosition() {
/* 14 */     return this.allowedTreePosition;
/*    */   }
/*    */   
/*    */   static {
/* 32 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)PlacedFeature.CODEC.fieldOf("feature").forGetter(RootSystemConfiguration::treeFeature), (App)Codec.intRange(1, 64).fieldOf("required_vertical_space_for_tree").forGetter(RootSystemConfiguration::requiredVerticalSpaceForTree), (App)Codec.intRange(0, 16).fieldOf("level_test_distance").forGetter(RootSystemConfiguration::levelTestDistance), (App)Codec.intRange(0, 64).fieldOf("max_level_deviation").forGetter(RootSystemConfiguration::maxLevelDeviation), (App)Codec.intRange(1, 64).fieldOf("root_radius").forGetter(RootSystemConfiguration::rootRadius), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("root_replaceable").forGetter(RootSystemConfiguration::rootReplaceable), (App)BlockStateProvider.CODEC.fieldOf("root_state_provider").forGetter(RootSystemConfiguration::rootStateProvider), (App)Codec.intRange(1, 256).fieldOf("root_placement_attempts").forGetter(RootSystemConfiguration::rootPlacementAttempts), (App)Codec.intRange(1, 4096).fieldOf("root_column_max_height").forGetter(RootSystemConfiguration::rootColumnMaxHeight), (App)Codec.intRange(1, 64).fieldOf("hanging_root_radius").forGetter(RootSystemConfiguration::hangingRootRadius), (App)Codec.intRange(1, 16).fieldOf("hanging_roots_vertical_span").forGetter(RootSystemConfiguration::hangingRootsVerticalSpan), (App)BlockStateProvider.CODEC.fieldOf("hanging_root_state_provider").forGetter(RootSystemConfiguration::hangingRootStateProvider), (App)Codec.intRange(1, 256).fieldOf("hanging_root_placement_attempts").forGetter(RootSystemConfiguration::hangingRootPlacementAttempts), (App)Codec.intRange(1, 64).fieldOf("allowed_vertical_water_for_tree").forGetter(RootSystemConfiguration::allowedVerticalWaterForTree), (App)BlockPredicate.CODEC.fieldOf("allowed_tree_position").forGetter(RootSystemConfiguration::allowedTreePosition)).apply((Applicative)i, RootSystemConfiguration::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\RootSystemConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */