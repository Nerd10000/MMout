/*    */ package net.minecraft.world.entity.animal.cow;
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
/*    */ public class CowVariants {
/* 18 */   public static final ResourceKey<CowVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
/*    */   
/* 19 */   public static final ResourceKey<CowVariant> WARM = createKey(TemperatureVariants.WARM);
/*    */   
/* 20 */   public static final ResourceKey<CowVariant> COLD = createKey(TemperatureVariants.COLD);
/*    */   
/* 21 */   public static final ResourceKey<CowVariant> DEFAULT = TEMPERATE;
/*    */   
/*    */   private static ResourceKey<CowVariant> createKey(Identifier id) {
/* 24 */     return ResourceKey.create(Registries.COW_VARIANT, id);
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<CowVariant> context) {
/* 28 */     register(context, TEMPERATE, CowVariant.ModelType.NORMAL, "cow_temperate", "cow_temperate_baby", SpawnPrioritySelectors.fallback(0));
/* 29 */     register(context, WARM, CowVariant.ModelType.WARM, "cow_warm", "cow_warm_baby", BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
/* 30 */     register(context, COLD, CowVariant.ModelType.COLD, "cow_cold", "cow_cold_baby", BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<CowVariant> context, ResourceKey<CowVariant> name, CowVariant.ModelType modelType, String textureName, String babyTextureName, TagKey<Biome> spawnBiome) {
/* 34 */     HolderSet.Named named = context.lookup(Registries.BIOME).getOrThrow(spawnBiome);
/* 35 */     register(context, name, modelType, textureName, babyTextureName, SpawnPrioritySelectors.single((SpawnCondition)new BiomeCheck((HolderSet)named), 1));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<CowVariant> context, ResourceKey<CowVariant> name, CowVariant.ModelType modelType, String textureName, String babyTextureName, SpawnPrioritySelectors selectors) {
/* 39 */     Identifier textureId = Identifier.withDefaultNamespace("entity/cow/" + textureName);
/* 40 */     Identifier babyTextureId = Identifier.withDefaultNamespace("entity/cow/" + babyTextureName);
/* 41 */     context.register(name, new CowVariant(new ModelAndTexture(modelType, textureId), new ClientAsset.ResourceTexture(babyTextureId), selectors));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\cow\CowVariants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */