/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ public abstract class NormalCraftingRecipe implements CraftingRecipe {
/*    */   protected final Recipe.CommonInfo commonInfo;
/*    */   
/*    */   protected final CraftingRecipe.CraftingBookInfo bookInfo;
/*    */   
/*    */   private PlacementInfo placementInfo;
/*    */   
/*    */   protected NormalCraftingRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo) {
/* 14 */     this.commonInfo = commonInfo;
/* 15 */     this.bookInfo = bookInfo;
/*    */   }
/*    */   
/*    */   public abstract RecipeSerializer<? extends NormalCraftingRecipe> getSerializer();
/*    */   
/*    */   public final String group() {
/* 23 */     return this.bookInfo.group();
/*    */   }
/*    */   
/*    */   public final CraftingBookCategory category() {
/* 28 */     return this.bookInfo.category();
/*    */   }
/*    */   
/*    */   public final boolean showNotification() {
/* 33 */     return this.commonInfo.showNotification();
/*    */   }
/*    */   
/*    */   protected abstract PlacementInfo createPlacementInfo();
/*    */   
/*    */   public final PlacementInfo placementInfo() {
/* 40 */     if (this.placementInfo == null)
/* 41 */       this.placementInfo = createPlacementInfo(); 
/* 43 */     return this.placementInfo;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\NormalCraftingRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */