/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class FeatureTags {
/* 11 */   public static final TagKey<ConfiguredFeature<?, ?>> CAN_SPAWN_FROM_BONE_MEAL = create("can_spawn_from_bone_meal");
/*    */   
/*    */   private static TagKey<ConfiguredFeature<?, ?>> create(String name) {
/* 14 */     return TagKey.create(Registries.CONFIGURED_FEATURE, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\FeatureTags.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */