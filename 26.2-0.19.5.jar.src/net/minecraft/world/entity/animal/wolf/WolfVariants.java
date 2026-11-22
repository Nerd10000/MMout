/*    */ package net.minecraft.world.entity.animal.wolf;
/*    */ 
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.BiomeTags;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.entity.variant.BiomeCheck;
/*    */ import net.minecraft.world.entity.variant.SpawnCondition;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.Biomes;
/*    */ 
/*    */ public class WolfVariants {
/* 17 */   public static final ResourceKey<WolfVariant> PALE = createKey("pale");
/*    */   
/* 18 */   public static final ResourceKey<WolfVariant> SPOTTED = createKey("spotted");
/*    */   
/* 19 */   public static final ResourceKey<WolfVariant> SNOWY = createKey("snowy");
/*    */   
/* 20 */   public static final ResourceKey<WolfVariant> BLACK = createKey("black");
/*    */   
/* 21 */   public static final ResourceKey<WolfVariant> ASHEN = createKey("ashen");
/*    */   
/* 22 */   public static final ResourceKey<WolfVariant> RUSTY = createKey("rusty");
/*    */   
/* 23 */   public static final ResourceKey<WolfVariant> WOODS = createKey("woods");
/*    */   
/* 24 */   public static final ResourceKey<WolfVariant> CHESTNUT = createKey("chestnut");
/*    */   
/* 25 */   public static final ResourceKey<WolfVariant> STRIPED = createKey("striped");
/*    */   
/* 27 */   public static final ResourceKey<WolfVariant> DEFAULT = PALE;
/*    */   
/*    */   private static ResourceKey<WolfVariant> createKey(String name) {
/* 30 */     return ResourceKey.create(Registries.WOLF_VARIANT, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<WolfVariant> context, ResourceKey<WolfVariant> name, String fileName, ResourceKey<Biome> spawnBiome) {
/* 34 */     register(context, name, fileName, highPrioBiome((HolderSet<Biome>)HolderSet.direct(new Holder[] { (Holder)context.lookup(Registries.BIOME).getOrThrow(spawnBiome) })));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<WolfVariant> context, ResourceKey<WolfVariant> name, String fileName, TagKey<Biome> spawnBiome) {
/* 38 */     register(context, name, fileName, highPrioBiome((HolderSet<Biome>)context.lookup(Registries.BIOME).getOrThrow(spawnBiome)));
/*    */   }
/*    */   
/*    */   private static SpawnPrioritySelectors highPrioBiome(HolderSet<Biome> biomes) {
/* 42 */     return SpawnPrioritySelectors.single((SpawnCondition)new BiomeCheck(biomes), 1);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<WolfVariant> context, ResourceKey<WolfVariant> name, String fileName, SpawnPrioritySelectors selectors) {
/* 46 */     Identifier wildTexture = Identifier.withDefaultNamespace("entity/wolf/" + fileName);
/* 47 */     Identifier tameTexture = Identifier.withDefaultNamespace("entity/wolf/" + fileName + "_tame");
/* 48 */     Identifier angryTexture = Identifier.withDefaultNamespace("entity/wolf/" + fileName + "_angry");
/* 50 */     Identifier babyTexture = Identifier.withDefaultNamespace("entity/wolf/" + fileName + "_baby");
/* 51 */     Identifier tameBabyTexture = Identifier.withDefaultNamespace("entity/wolf/" + fileName + "_tame_baby");
/* 52 */     Identifier angryBabyTexture = Identifier.withDefaultNamespace("entity/wolf/" + fileName + "_angry_baby");
/* 54 */     context.register(name, new WolfVariant(new WolfVariant.AssetInfo(new ClientAsset.ResourceTexture(wildTexture), new ClientAsset.ResourceTexture(tameTexture), new ClientAsset.ResourceTexture(angryTexture)), new WolfVariant.AssetInfo(new ClientAsset.ResourceTexture(babyTexture), new ClientAsset.ResourceTexture(tameBabyTexture), new ClientAsset.ResourceTexture(angryBabyTexture)), selectors));
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<WolfVariant> context) {
/* 70 */     register(context, PALE, "wolf", SpawnPrioritySelectors.fallback(0));
/* 71 */     register(context, SPOTTED, "wolf_spotted", BiomeTags.IS_SAVANNA);
/* 72 */     register(context, SNOWY, "wolf_snowy", Biomes.GROVE);
/* 73 */     register(context, BLACK, "wolf_black", Biomes.OLD_GROWTH_PINE_TAIGA);
/* 74 */     register(context, ASHEN, "wolf_ashen", Biomes.SNOWY_TAIGA);
/* 75 */     register(context, RUSTY, "wolf_rusty", BiomeTags.IS_JUNGLE);
/* 76 */     register(context, WOODS, "wolf_woods", Biomes.FOREST);
/* 77 */     register(context, CHESTNUT, "wolf_chestnut", Biomes.OLD_GROWTH_SPRUCE_TAIGA);
/* 78 */     register(context, STRIPED, "wolf_striped", BiomeTags.IS_BADLANDS);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\wolf\WolfVariants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */