/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class RecipeSerializers {
/*    */   public static Object bootstrap(Registry<RecipeSerializer<?>> registry) {
/*  7 */     Registry.register(registry, "crafting_shaped", ShapedRecipe.SERIALIZER);
/*  8 */     Registry.register(registry, "crafting_shapeless", ShapelessRecipe.SERIALIZER);
/*  9 */     Registry.register(registry, "crafting_dye", DyeRecipe.SERIALIZER);
/* 10 */     Registry.register(registry, "crafting_imbue", ImbueRecipe.SERIALIZER);
/* 11 */     Registry.register(registry, "crafting_transmute", TransmuteRecipe.SERIALIZER);
/* 13 */     Registry.register(registry, "crafting_decorated_pot", DecoratedPotRecipe.SERIALIZER);
/* 14 */     Registry.register(registry, "crafting_special_bookcloning", BookCloningRecipe.SERIALIZER);
/* 15 */     Registry.register(registry, "crafting_special_mapextending", MapExtendingRecipe.SERIALIZER);
/* 16 */     Registry.register(registry, "crafting_special_firework_rocket", FireworkRocketRecipe.SERIALIZER);
/* 17 */     Registry.register(registry, "crafting_special_firework_star", FireworkStarRecipe.SERIALIZER);
/* 18 */     Registry.register(registry, "crafting_special_firework_star_fade", FireworkStarFadeRecipe.SERIALIZER);
/* 19 */     Registry.register(registry, "crafting_special_bannerduplicate", BannerDuplicateRecipe.SERIALIZER);
/* 20 */     Registry.register(registry, "crafting_special_shielddecoration", ShieldDecorationRecipe.SERIALIZER);
/* 21 */     Registry.register(registry, "crafting_special_repairitem", RepairItemRecipe.SERIALIZER);
/* 23 */     Registry.register(registry, "smelting", SmeltingRecipe.SERIALIZER);
/* 24 */     Registry.register(registry, "blasting", BlastingRecipe.SERIALIZER);
/* 25 */     Registry.register(registry, "smoking", SmokingRecipe.SERIALIZER);
/* 26 */     Registry.register(registry, "campfire_cooking", CampfireCookingRecipe.SERIALIZER);
/* 27 */     Registry.register(registry, "stonecutting", StonecutterRecipe.SERIALIZER);
/* 28 */     Registry.register(registry, "smithing_transform", SmithingTransformRecipe.SERIALIZER);
/* 29 */     return Registry.register(registry, "smithing_trim", SmithingTrimRecipe.SERIALIZER);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\RecipeSerializers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */