/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*    */ 
/*    */ public class SmokerMenu extends AbstractFurnaceMenu {
/*    */   public SmokerMenu(int containerId, Inventory inventory) {
/*  9 */     super(MenuType.SMOKER, RecipePropertySet.SMOKER_INPUT, RecipeBookType.SMOKER, containerId, inventory);
/*    */   }
/*    */   
/*    */   public SmokerMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
/* 13 */     super(MenuType.SMOKER, RecipePropertySet.SMOKER_INPUT, RecipeBookType.SMOKER, containerId, inventory, container, data);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\SmokerMenu.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */