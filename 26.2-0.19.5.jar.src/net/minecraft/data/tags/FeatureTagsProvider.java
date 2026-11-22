/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.data.worldgen.features.VegetationFeatures;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.FeatureTags;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class FeatureTagsProvider extends TagsProvider<ConfiguredFeature<?, ?>> {
/*    */   public FeatureTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 14 */     super(output, Registries.CONFIGURED_FEATURE, lookupProvider);
/*    */   }
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(FeatureTags.CAN_SPAWN_FROM_BONE_MEAL).add((ResourceKey<ConfiguredFeature<?, ?>>[])new ResourceKey[] { VegetationFeatures.FLOWER_DEFAULT, VegetationFeatures.FLOWER_FLOWER_FOREST, VegetationFeatures.FLOWER_SWAMP, VegetationFeatures.FLOWER_PLAIN, VegetationFeatures.FLOWER_MEADOW, VegetationFeatures.FLOWER_CHERRY, VegetationFeatures.WILDFLOWER, VegetationFeatures.FLOWER_PALE_GARDEN });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\FeatureTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */