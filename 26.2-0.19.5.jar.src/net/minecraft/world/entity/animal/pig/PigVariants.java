/*    */ package net.minecraft.world.entity.animal.pig;
/*    */ 
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.BiomeTags;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.entity.animal.TemperatureVariants;
/*    */ import net.minecraft.world.entity.variant.BiomeCheck;
/*    */ import net.minecraft.world.entity.variant.ModelAndTexture;
/*    */ import net.minecraft.world.entity.variant.SpawnCondition;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public class PigVariants {
/* 18 */   public static final ResourceKey<PigVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
/*    */   
/* 19 */   public static final ResourceKey<PigVariant> WARM = createKey(TemperatureVariants.WARM);
/*    */   
/* 20 */   public static final ResourceKey<PigVariant> COLD = createKey(TemperatureVariants.COLD);
/*    */   
/* 21 */   public static final ResourceKey<PigVariant> DEFAULT = TEMPERATE;
/*    */   
/*    */   private static ResourceKey<PigVariant> createKey(Identifier id) {
/* 24 */     return ResourceKey.create(Registries.PIG_VARIANT, id);
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<PigVariant> context) {
/* 28 */     register(context, TEMPERATE, PigVariant.ModelType.NORMAL, "pig_temperate", "pig_temperate_baby", SpawnPrioritySelectors.fallback(0));
/* 29 */     register(context, WARM, PigVariant.ModelType.NORMAL, "pig_warm", "pig_warm_baby", BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
/* 30 */     register(context, COLD, PigVariant.ModelType.COLD, "pig_cold", "pig_cold_baby", BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<PigVariant> context, ResourceKey<PigVariant> name, PigVariant.ModelType modelType, String textureName, String babyTextureName, TagKey<Biome> spawnBiome) {
/* 34 */     HolderSet.Named named = context.lookup(Registries.BIOME).getOrThrow(spawnBiome);
/* 35 */     register(context, name, modelType, textureName, babyTextureName, SpawnPrioritySelectors.single((SpawnCondition)new BiomeCheck((HolderSet)named), 1));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<PigVariant> context, ResourceKey<PigVariant> name, PigVariant.ModelType modelType, String textureName, String babyTextureName, SpawnPrioritySelectors selectors) {
/* 39 */     Identifier textureId = Identifier.withDefaultNamespace("entity/pig/" + textureName);
/* 40 */     Identifier babyTextureId = Identifier.withDefaultNamespace("entity/pig/" + babyTextureName);
/* 41 */     context.register(name, new PigVariant(new ModelAndTexture(modelType, textureId), new ClientAsset.ResourceTexture(babyTextureId), selectors));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\pig\PigVariants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */