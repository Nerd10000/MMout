/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class RecipeDisplays {
/*    */   public static RecipeDisplay.Type<?> bootstrap(Registry<RecipeDisplay.Type<?>> registry) {
/*  7 */     Registry.register(registry, "crafting_shapeless", ShapelessCraftingRecipeDisplay.TYPE);
/*  8 */     Registry.register(registry, "crafting_shaped", ShapedCraftingRecipeDisplay.TYPE);
/*  9 */     Registry.register(registry, "furnace", FurnaceRecipeDisplay.TYPE);
/* 10 */     Registry.register(registry, "stonecutter", StonecutterRecipeDisplay.TYPE);
/* 11 */     return (RecipeDisplay.Type)Registry.register(registry, "smithing", SmithingRecipeDisplay.TYPE);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\RecipeDisplays.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */