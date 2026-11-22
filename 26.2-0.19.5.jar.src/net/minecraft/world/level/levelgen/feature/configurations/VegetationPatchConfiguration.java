/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function10;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.placement.CaveSurface;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public final class VegetationPatchConfiguration extends Record implements FeatureConfiguration {
/*    */   private final HolderSet<Block> replaceable;
/*    */   
/*    */   private final BlockStateProvider groundState;
/*    */   
/*    */   private final Holder<PlacedFeature> vegetationFeature;
/*    */   
/*    */   private final CaveSurface surface;
/*    */   
/*    */   private final IntProvider depth;
/*    */   
/*    */   private final float extraBottomBlockChance;
/*    */   
/*    */   private final int verticalRange;
/*    */   
/*    */   private final float vegetationChance;
/*    */   
/*    */   private final IntProvider xzRadius;
/*    */   
/*    */   private final float extraEdgeColumnChance;
/*    */   
/*    */   public static final Codec<VegetationPatchConfiguration> CODEC;
/*    */   
/*    */   public VegetationPatchConfiguration(HolderSet<Block> replaceable, BlockStateProvider groundState, Holder<PlacedFeature> vegetationFeature, CaveSurface surface, IntProvider depth, float extraBottomBlockChance, int verticalRange, float vegetationChance, IntProvider xzRadius, float extraEdgeColumnChance) {
/* 17 */     this.replaceable = replaceable;
/* 17 */     this.groundState = groundState;
/* 17 */     this.vegetationFeature = vegetationFeature;
/* 17 */     this.surface = surface;
/* 17 */     this.depth = depth;
/* 17 */     this.extraBottomBlockChance = extraBottomBlockChance;
/* 17 */     this.verticalRange = verticalRange;
/* 17 */     this.vegetationChance = vegetationChance;
/* 17 */     this.xzRadius = xzRadius;
/* 17 */     this.extraEdgeColumnChance = extraEdgeColumnChance;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/VegetationPatchConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/VegetationPatchConfiguration;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/VegetationPatchConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/VegetationPatchConfiguration;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/VegetationPatchConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/VegetationPatchConfiguration;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderSet<Block> replaceable() {
/* 17 */     return this.replaceable;
/*    */   }
/*    */   
/*    */   public BlockStateProvider groundState() {
/* 17 */     return this.groundState;
/*    */   }
/*    */   
/*    */   public Holder<PlacedFeature> vegetationFeature() {
/* 17 */     return this.vegetationFeature;
/*    */   }
/*    */   
/*    */   public CaveSurface surface() {
/* 17 */     return this.surface;
/*    */   }
/*    */   
/*    */   public IntProvider depth() {
/* 17 */     return this.depth;
/*    */   }
/*    */   
/*    */   public float extraBottomBlockChance() {
/* 17 */     return this.extraBottomBlockChance;
/*    */   }
/*    */   
/*    */   public int verticalRange() {
/* 17 */     return this.verticalRange;
/*    */   }
/*    */   
/*    */   public float vegetationChance() {
/* 17 */     return this.vegetationChance;
/*    */   }
/*    */   
/*    */   public IntProvider xzRadius() {
/* 17 */     return this.xzRadius;
/*    */   }
/*    */   
/*    */   public float extraEdgeColumnChance() {
/* 17 */     return this.extraEdgeColumnChance;
/*    */   }
/*    */   
/*    */   static {
/* 30 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("replaceable").forGetter(VegetationPatchConfiguration::replaceable), (App)BlockStateProvider.CODEC.fieldOf("ground_state").forGetter(VegetationPatchConfiguration::groundState), (App)PlacedFeature.CODEC.fieldOf("vegetation_feature").forGetter(VegetationPatchConfiguration::vegetationFeature), (App)CaveSurface.CODEC.fieldOf("surface").forGetter(VegetationPatchConfiguration::surface), (App)IntProviders.codec(1, 128).fieldOf("depth").forGetter(VegetationPatchConfiguration::depth), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("extra_bottom_block_chance").forGetter(VegetationPatchConfiguration::extraBottomBlockChance), (App)Codec.intRange(1, 256).fieldOf("vertical_range").forGetter(VegetationPatchConfiguration::verticalRange), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("vegetation_chance").forGetter(VegetationPatchConfiguration::vegetationChance), (App)IntProviders.CODEC.fieldOf("xz_radius").forGetter(VegetationPatchConfiguration::xzRadius), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("extra_edge_column_chance").forGetter(VegetationPatchConfiguration::extraEdgeColumnChance)).apply((Applicative)i, VegetationPatchConfiguration::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\VegetationPatchConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */