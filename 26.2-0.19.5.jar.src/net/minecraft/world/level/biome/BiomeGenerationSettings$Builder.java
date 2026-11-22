/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ 
/*     */ public class Builder extends BiomeGenerationSettings.PlainBuilder {
/*     */   private final HolderGetter<PlacedFeature> placedFeatures;
/*     */   
/*     */   private final HolderGetter<ConfiguredWorldCarver<?>> worldCarvers;
/*     */   
/*     */   public Builder(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
/* 116 */     this.placedFeatures = placedFeatures;
/* 117 */     this.worldCarvers = worldCarvers;
/*     */   }
/*     */   
/*     */   public Builder addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature) {
/* 121 */     addFeature(step.ordinal(), (Holder<PlacedFeature>)this.placedFeatures.getOrThrow(feature));
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addCarver(ResourceKey<ConfiguredWorldCarver<?>> carver) {
/* 126 */     addCarver((Holder<ConfiguredWorldCarver<?>>)this.worldCarvers.getOrThrow(carver));
/* 127 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\BiomeGenerationSettings$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */