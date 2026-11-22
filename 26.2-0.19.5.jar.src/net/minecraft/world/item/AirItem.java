/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class AirItem extends Item {
/*    */   public AirItem(Item.Properties properties) {
/* 10 */     super(properties);
/*    */   }
/*    */   
/*    */   public Component getName(ItemStack itemStack) {
/* 16 */     return (Component)itemStack.typeHolder().components().getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\AirItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */