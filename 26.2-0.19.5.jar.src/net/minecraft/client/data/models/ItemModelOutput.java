/*   */ package net.minecraft.client.data.models;
/*   */ 
/*   */ import net.minecraft.client.renderer.item.ClientItem;
/*   */ import net.minecraft.client.renderer.item.ItemModel;
/*   */ import net.minecraft.world.item.Item;
/*   */ 
/*   */ public interface ItemModelOutput {
/*   */   default void accept(Item item, ItemModel.Unbaked generator) {
/* 9 */     accept(item, generator, ClientItem.Properties.DEFAULT);
/*   */   }
/*   */   
/*   */   void accept(Item paramItem, ItemModel.Unbaked paramUnbaked, ClientItem.Properties paramProperties);
/*   */   
/*   */   void copy(Item paramItem1, Item paramItem2);
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\ItemModelOutput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */