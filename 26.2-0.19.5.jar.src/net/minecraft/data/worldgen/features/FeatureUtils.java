/*    */ package net.minecraft.data.worldgen.features;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class FeatureUtils {
/*    */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
/* 15 */     AquaticFeatures.bootstrap(context);
/* 16 */     CaveFeatures.bootstrap(context);
/* 17 */     EndFeatures.bootstrap(context);
/* 18 */     MiscOverworldFeatures.bootstrap(context);
/* 19 */     NetherFeatures.bootstrap(context);
/* 20 */     OreFeatures.bootstrap(context);
/* 21 */     PileFeatures.bootstrap(context);
/* 22 */     TreeFeatures.bootstrap(context);
/* 23 */     VegetationFeatures.bootstrap(context);
/*    */   }
/*    */   
/*    */   public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
/* 27 */     return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/*    */   public static void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> id, Feature<NoneFeatureConfiguration> feature) {
/* 31 */     register(context, id, feature, FeatureConfiguration.NONE);
/*    */   }
/*    */   
/*    */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> id, F feature, FC config) {
/* 35 */     context.register(id, new ConfiguredFeature((Feature)feature, (FeatureConfiguration)config));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\features\FeatureUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */