/*    */ package net.minecraft.world.entity.animal.frog;
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
/*    */ import net.minecraft.world.entity.variant.SpawnCondition;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public interface FrogVariants {
/* 17 */   public static final ResourceKey<FrogVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
/*    */   
/* 18 */   public static final ResourceKey<FrogVariant> WARM = createKey(TemperatureVariants.WARM);
/*    */   
/* 19 */   public static final ResourceKey<FrogVariant> COLD = createKey(TemperatureVariants.COLD);
/*    */   
/*    */   private static ResourceKey<FrogVariant> createKey(Identifier id) {
/* 22 */     return ResourceKey.create(Registries.FROG_VARIANT, id);
/*    */   }
/*    */   
/*    */   static void bootstrap(BootstrapContext<FrogVariant> registry) {
/* 26 */     register(registry, TEMPERATE, "entity/frog/frog_temperate", SpawnPrioritySelectors.fallback(0));
/* 27 */     register(registry, WARM, "entity/frog/frog_warm", BiomeTags.SPAWNS_WARM_VARIANT_FROGS);
/* 28 */     register(registry, COLD, "entity/frog/frog_cold", BiomeTags.SPAWNS_COLD_VARIANT_FROGS);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<FrogVariant> context, ResourceKey<FrogVariant> name, String assetId, TagKey<Biome> limitToBiome) {
/* 32 */     HolderSet.Named named = context.lookup(Registries.BIOME).getOrThrow(limitToBiome);
/* 33 */     register(context, name, assetId, SpawnPrioritySelectors.single((SpawnCondition)new BiomeCheck((HolderSet)named), 1));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<FrogVariant> context, ResourceKey<FrogVariant> name, String assetId, SpawnPrioritySelectors selectors) {
/* 37 */     context.register(name, new FrogVariant(new ClientAsset.ResourceTexture(
/* 38 */             Identifier.withDefaultNamespace(assetId)), selectors));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\frog\FrogVariants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */