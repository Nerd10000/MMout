/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ public abstract class CustomRecipe implements CraftingRecipe {
/*    */   public boolean isSpecial() {
/*  9 */     return true;
/*    */   }
/*    */   
/*    */   public boolean showNotification() {
/* 14 */     return false;
/*    */   }
/*    */   
/*    */   public String group() {
/* 19 */     return "";
/*    */   }
/*    */   
/*    */   public CraftingBookCategory category() {
/* 25 */     return CraftingBookCategory.MISC;
/*    */   }
/*    */   
/*    */   public PlacementInfo placementInfo() {
/* 30 */     return PlacementInfo.NOT_PLACEABLE;
/*    */   }
/*    */   
/*    */   public abstract RecipeSerializer<? extends CustomRecipe> getSerializer();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\CustomRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */