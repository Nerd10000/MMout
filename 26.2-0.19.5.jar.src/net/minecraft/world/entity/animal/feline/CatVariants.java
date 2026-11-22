/*    */ package net.minecraft.world.entity.animal.feline;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.StructureTags;
/*    */ import net.minecraft.world.entity.variant.MoonBrightnessCheck;
/*    */ import net.minecraft.world.entity.variant.PriorityProvider;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ import net.minecraft.world.entity.variant.StructureCheck;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ 
/*    */ public interface CatVariants {
/* 20 */   public static final ResourceKey<CatVariant> TABBY = createKey("tabby");
/*    */   
/* 21 */   public static final ResourceKey<CatVariant> BLACK = createKey("black");
/*    */   
/* 22 */   public static final ResourceKey<CatVariant> RED = createKey("red");
/*    */   
/* 23 */   public static final ResourceKey<CatVariant> SIAMESE = createKey("siamese");
/*    */   
/* 24 */   public static final ResourceKey<CatVariant> BRITISH_SHORTHAIR = createKey("british_shorthair");
/*    */   
/* 25 */   public static final ResourceKey<CatVariant> CALICO = createKey("calico");
/*    */   
/* 26 */   public static final ResourceKey<CatVariant> PERSIAN = createKey("persian");
/*    */   
/* 27 */   public static final ResourceKey<CatVariant> RAGDOLL = createKey("ragdoll");
/*    */   
/* 28 */   public static final ResourceKey<CatVariant> WHITE = createKey("white");
/*    */   
/* 29 */   public static final ResourceKey<CatVariant> JELLIE = createKey("jellie");
/*    */   
/* 30 */   public static final ResourceKey<CatVariant> ALL_BLACK = createKey("all_black");
/*    */   
/*    */   private static ResourceKey<CatVariant> createKey(String name) {
/* 33 */     return ResourceKey.create(Registries.CAT_VARIANT, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/*    */   static void bootstrap(BootstrapContext<CatVariant> context) {
/* 37 */     HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
/* 39 */     registerForAnyConditions(context, TABBY, "entity/cat/cat_tabby", "entity/cat/cat_tabby_baby");
/* 40 */     registerForAnyConditions(context, BLACK, "entity/cat/cat_black", "entity/cat/cat_black_baby");
/* 41 */     registerForAnyConditions(context, RED, "entity/cat/cat_red", "entity/cat/cat_red_baby");
/* 42 */     registerForAnyConditions(context, SIAMESE, "entity/cat/cat_siamese", "entity/cat/cat_siamese_baby");
/* 43 */     registerForAnyConditions(context, BRITISH_SHORTHAIR, "entity/cat/cat_british_shorthair", "entity/cat/cat_british_shorthair_baby");
/* 44 */     registerForAnyConditions(context, CALICO, "entity/cat/cat_calico", "entity/cat/cat_calico_baby");
/* 45 */     registerForAnyConditions(context, PERSIAN, "entity/cat/cat_persian", "entity/cat/cat_persian_baby");
/* 46 */     registerForAnyConditions(context, RAGDOLL, "entity/cat/cat_ragdoll", "entity/cat/cat_ragdoll_baby");
/* 47 */     registerForAnyConditions(context, WHITE, "entity/cat/cat_white", "entity/cat/cat_white_baby");
/* 48 */     registerForAnyConditions(context, JELLIE, "entity/cat/cat_jellie", "entity/cat/cat_jellie_baby");
/* 50 */     register(context, ALL_BLACK, "entity/cat/cat_all_black", "entity/cat/cat_all_black_baby", new SpawnPrioritySelectors(
/* 51 */           List.of(new PriorityProvider.Selector((PriorityProvider.SelectorCondition)new StructureCheck((HolderSet)structures
/*    */                 
/* 53 */                 .getOrThrow(StructureTags.CATS_SPAWN_AS_BLACK)), 1), new PriorityProvider.Selector((PriorityProvider.SelectorCondition)new MoonBrightnessCheck(
/*    */                 
/* 55 */                 MinMaxBounds.Doubles.atLeast(0.9D)), 0))));
/*    */   }
/*    */   
/*    */   private static void registerForAnyConditions(BootstrapContext<CatVariant> context, ResourceKey<CatVariant> name, String adultTexture, String babyTexture) {
/* 61 */     register(context, name, adultTexture, babyTexture, SpawnPrioritySelectors.fallback(0));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<CatVariant> context, ResourceKey<CatVariant> name, String adultTexture, String babyTexture, SpawnPrioritySelectors spawnConditions) {
/* 65 */     context.register(name, new CatVariant(new ClientAsset.ResourceTexture(
/* 66 */             Identifier.withDefaultNamespace(adultTexture)), new ClientAsset.ResourceTexture(
/* 67 */             Identifier.withDefaultNamespace(babyTexture)), spawnConditions));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\feline\CatVariants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */