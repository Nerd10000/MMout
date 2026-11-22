/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.StackedItemContents;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ 
/*    */ public abstract class RecipeBookMenu extends AbstractContainerMenu {
/*    */   public RecipeBookMenu(MenuType<?> menuType, int containerId) {
/* 10 */     super(menuType, containerId);
/*    */   }
/*    */   
/*    */   public abstract PostPlaceAction handlePlacement(boolean paramBoolean1, boolean paramBoolean2, RecipeHolder<?> paramRecipeHolder, ServerLevel paramServerLevel, Inventory paramInventory);
/*    */   
/*    */   public abstract void fillCraftSlotsStackedContents(StackedItemContents paramStackedItemContents);
/*    */   
/*    */   public abstract RecipeBookType getRecipeBookType();
/*    */   
/*    */   public enum PostPlaceAction {
/* 20 */     NOTHING, PLACE_GHOST_RECIPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\RecipeBookMenu.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */