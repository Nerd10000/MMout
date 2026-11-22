/*    */ package net.minecraft.world.flag;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public interface FeatureElement {
/* 16 */   public static final Set<ResourceKey<? extends Registry<? extends FeatureElement>>> FILTERED_REGISTRIES = Set.of(Registries.ITEM, Registries.BLOCK, Registries.ENTITY_TYPE, Registries.GAME_RULE, Registries.MENU, Registries.POTION, Registries.MOB_EFFECT);
/*    */   
/*    */   FeatureFlagSet requiredFeatures();
/*    */   
/*    */   default boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 29 */     return requiredFeatures().isSubsetOf(enabledFeatures);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\flag\FeatureElement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */