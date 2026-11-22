/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*    */ 
/*    */ public class FurnaceMenu extends AbstractFurnaceMenu {
/*    */   public FurnaceMenu(int containerId, Inventory inventory) {
/*  9 */     super(MenuType.FURNACE, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, containerId, inventory);
/*    */   }
/*    */   
/*    */   public FurnaceMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
/* 13 */     super(MenuType.FURNACE, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, containerId, inventory, container, data);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\FurnaceMenu.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */