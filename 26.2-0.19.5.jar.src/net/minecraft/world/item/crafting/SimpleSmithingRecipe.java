/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ public abstract class SimpleSmithingRecipe implements SmithingRecipe {
/*    */   protected final Recipe.CommonInfo commonInfo;
/*    */   
/*    */   private PlacementInfo placementInfo;
/*    */   
/*    */   protected SimpleSmithingRecipe(Recipe.CommonInfo commonInfo) {
/* 11 */     this.commonInfo = commonInfo;
/*    */   }
/*    */   
/*    */   public abstract RecipeSerializer<? extends SimpleSmithingRecipe> getSerializer();
/*    */   
/*    */   public PlacementInfo placementInfo() {
/* 19 */     if (this.placementInfo == null)
/* 20 */       this.placementInfo = createPlacementInfo(); 
/* 22 */     return this.placementInfo;
/*    */   }
/*    */   
/*    */   protected abstract PlacementInfo createPlacementInfo();
/*    */   
/*    */   public String group() {
/* 29 */     return "";
/*    */   }
/*    */   
/*    */   public final boolean showNotification() {
/* 34 */     return this.commonInfo.showNotification();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\SimpleSmithingRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */