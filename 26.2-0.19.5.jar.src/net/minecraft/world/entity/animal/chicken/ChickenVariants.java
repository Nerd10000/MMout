/*    */ package net.minecraft.world.entity.animal.chicken;
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
/*    */ public class ChickenVariants {
/* 18 */   public static final ResourceKey<ChickenVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
/*    */   
/* 19 */   public static final ResourceKey<ChickenVariant> WARM = createKey(TemperatureVariants.WARM);
/*    */   
/* 20 */   public static final ResourceKey<ChickenVariant> COLD = createKey(TemperatureVariants.COLD);
/*    */   
/* 21 */   public static final ResourceKey<ChickenVariant> DEFAULT = TEMPERATE;
/*    */   
/*    */   private static ResourceKey<ChickenVariant> createKey(Identifier id) {
/* 24 */     return ResourceKey.create(Registries.CHICKEN_VARIANT, id);
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<ChickenVariant> context) {
/* 28 */     register(context, TEMPERATE, ChickenVariant.ModelType.NORMAL, "chicken_temperate", "chicken_temperate_baby", SpawnPrioritySelectors.fallback(0));
/* 29 */     register(context, WARM, ChickenVariant.ModelType.NORMAL, "chicken_warm", "chicken_warm_baby", BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
/* 30 */     register(context, COLD, ChickenVariant.ModelType.COLD, "chicken_cold", "chicken_cold_baby", BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<ChickenVariant> context, ResourceKey<ChickenVariant> name, ChickenVariant.ModelType modelType, String textureName, String babyTextureName, TagKey<Biome> spawnBiome) {
/* 34 */     HolderSet.Named named = context.lookup(Registries.BIOME).getOrThrow(spawnBiome);
/* 35 */     register(context, name, modelType, textureName, babyTextureName, SpawnPrioritySelectors.single((SpawnCondition)new BiomeCheck((HolderSet)named), 1));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<ChickenVariant> context, ResourceKey<ChickenVariant> name, ChickenVariant.ModelType modelType, String textureName, String babyTextureName, SpawnPrioritySelectors selectors) {
/* 39 */     Identifier textureId = Identifier.withDefaultNamespace("entity/chicken/" + textureName);
/* 40 */     Identifier babyTextureId = Identifier.withDefaultNamespace("entity/chicken/" + babyTextureName);
/* 41 */     context.register(name, new ChickenVariant(new ModelAndTexture(modelType, textureId), new ClientAsset.ResourceTexture(babyTextureId), selectors));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\chicken\ChickenVariants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */