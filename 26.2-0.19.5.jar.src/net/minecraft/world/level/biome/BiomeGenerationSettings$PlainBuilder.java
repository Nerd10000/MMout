/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ 
/*     */ public class PlainBuilder {
/*  76 */   private final List<Holder<ConfiguredWorldCarver<?>>> carvers = new ArrayList<>();
/*     */   
/*  77 */   private final List<List<Holder<PlacedFeature>>> features = new ArrayList<>();
/*     */   
/*     */   public PlainBuilder addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> feature) {
/*  83 */     return addFeature(step.ordinal(), feature);
/*     */   }
/*     */   
/*     */   public PlainBuilder addFeature(int index, Holder<PlacedFeature> feature) {
/*  87 */     addFeatureStepsUpTo(index);
/*  88 */     ((List<Holder<PlacedFeature>>)this.features.get(index)).add(feature);
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public PlainBuilder addCarver(Holder<ConfiguredWorldCarver<?>> carver) {
/*  93 */     this.carvers.add(carver);
/*  94 */     return this;
/*     */   }
/*     */   
/*     */   private void addFeatureStepsUpTo(int index) {
/*  98 */     while (this.features.size() <= index)
/*  99 */       this.features.add(Lists.newArrayList()); 
/*     */   }
/*     */   
/*     */   public BiomeGenerationSettings build() {
/* 104 */     return new BiomeGenerationSettings(
/* 105 */         (HolderSet<ConfiguredWorldCarver<?>>)HolderSet.direct(this.carvers), (List<HolderSet<PlacedFeature>>)this.features
/* 106 */         .stream().map(HolderSet::direct).collect(ImmutableList.toImmutableList()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\BiomeGenerationSettings$PlainBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */