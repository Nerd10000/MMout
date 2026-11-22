/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.FeatureTags;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class BiomeGenerationSettings {
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  30 */   public static final BiomeGenerationSettings EMPTY = new BiomeGenerationSettings(
/*  31 */       HolderSet.empty(), 
/*  32 */       List.of());
/*     */   
/*     */   public static final MapCodec<BiomeGenerationSettings> CODEC;
/*     */   
/*     */   private final HolderSet<ConfiguredWorldCarver<?>> carvers;
/*     */   
/*     */   private final List<HolderSet<PlacedFeature>> features;
/*     */   
/*     */   private final Supplier<List<ConfiguredFeature<?, ?>>> boneMealFeatures;
/*     */   
/*     */   private final Supplier<Set<PlacedFeature>> featureSet;
/*     */   
/*     */   static {
/*  35 */     CODEC = RecordCodecBuilder.mapCodec(i -> {
/*     */           Objects.requireNonNull(LOGGER);
/*     */           Objects.requireNonNull(LOGGER);
/*     */           return (Function)i.group((App)ConfiguredWorldCarver.LIST_CODEC.promotePartial(Util.prefix("Carver: ", LOGGER::error)).fieldOf("carvers").forGetter(()), (App)PlacedFeature.LIST_OF_LISTS_CODEC.promotePartial(Util.prefix("Features: ", LOGGER::error)).fieldOf("features").forGetter(())).apply((Applicative)i, BiomeGenerationSettings::new);
/*     */         });
/*     */   }
/*     */   
/*     */   private BiomeGenerationSettings(HolderSet<ConfiguredWorldCarver<?>> carvers, List<HolderSet<PlacedFeature>> features) {
/*  47 */     this.carvers = carvers;
/*  48 */     this.features = features;
/*  51 */     this.boneMealFeatures = (Supplier<List<ConfiguredFeature<?, ?>>>)Suppliers.memoize(() -> (List)features.stream().flatMap(HolderSet::stream).flatMap(()).filter(()).map(Holder::value).collect(ImmutableList.toImmutableList()));
/*  56 */     this.featureSet = (Supplier<Set<PlacedFeature>>)Suppliers.memoize(() -> (Set)features.stream().flatMap(HolderSet::stream).map(Holder::value).collect(Collectors.toSet()));
/*     */   }
/*     */   
/*     */   public Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers() {
/*  60 */     return (Iterable)this.carvers;
/*     */   }
/*     */   
/*     */   public List<ConfiguredFeature<?, ?>> getBoneMealFeatures() {
/*  64 */     return this.boneMealFeatures.get();
/*     */   }
/*     */   
/*     */   public List<HolderSet<PlacedFeature>> features() {
/*  68 */     return this.features;
/*     */   }
/*     */   
/*     */   public boolean hasFeature(PlacedFeature feature) {
/*  72 */     return ((Set)this.featureSet.get()).contains(feature);
/*     */   }
/*     */   
/*     */   public static class PlainBuilder {
/*  76 */     private final List<Holder<ConfiguredWorldCarver<?>>> carvers = new ArrayList<>();
/*     */     
/*  77 */     private final List<List<Holder<PlacedFeature>>> features = new ArrayList<>();
/*     */     
/*     */     public PlainBuilder addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> feature) {
/*  83 */       return addFeature(step.ordinal(), feature);
/*     */     }
/*     */     
/*     */     public PlainBuilder addFeature(int index, Holder<PlacedFeature> feature) {
/*  87 */       addFeatureStepsUpTo(index);
/*  88 */       ((List<Holder<PlacedFeature>>)this.features.get(index)).add(feature);
/*  89 */       return this;
/*     */     }
/*     */     
/*     */     public PlainBuilder addCarver(Holder<ConfiguredWorldCarver<?>> carver) {
/*  93 */       this.carvers.add(carver);
/*  94 */       return this;
/*     */     }
/*     */     
/*     */     private void addFeatureStepsUpTo(int index) {
/*  98 */       while (this.features.size() <= index)
/*  99 */         this.features.add(Lists.newArrayList()); 
/*     */     }
/*     */     
/*     */     public BiomeGenerationSettings build() {
/* 104 */       return new BiomeGenerationSettings(
/* 105 */           (HolderSet<ConfiguredWorldCarver<?>>)HolderSet.direct(this.carvers), (List<HolderSet<PlacedFeature>>)this.features
/* 106 */           .stream().map(HolderSet::direct).collect(ImmutableList.toImmutableList()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder extends PlainBuilder {
/*     */     private final HolderGetter<PlacedFeature> placedFeatures;
/*     */     
/*     */     private final HolderGetter<ConfiguredWorldCarver<?>> worldCarvers;
/*     */     
/*     */     public Builder(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
/* 116 */       this.placedFeatures = placedFeatures;
/* 117 */       this.worldCarvers = worldCarvers;
/*     */     }
/*     */     
/*     */     public Builder addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature) {
/* 121 */       addFeature(step.ordinal(), (Holder<PlacedFeature>)this.placedFeatures.getOrThrow(feature));
/* 122 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addCarver(ResourceKey<ConfiguredWorldCarver<?>> carver) {
/* 126 */       addCarver((Holder<ConfiguredWorldCarver<?>>)this.worldCarvers.getOrThrow(carver));
/* 127 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\BiomeGenerationSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */