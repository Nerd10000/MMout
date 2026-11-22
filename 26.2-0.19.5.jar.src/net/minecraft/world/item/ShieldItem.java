/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ShieldItem extends Item {
/*    */   public ShieldItem(Item.Properties properties) {
/*  8 */     super(properties);
/*    */   }
/*    */   
/*    */   public Component getName(ItemStack itemStack) {
/* 13 */     DyeColor baseColor = (DyeColor)itemStack.get(DataComponents.BASE_COLOR);
/* 14 */     if (baseColor != null)
/* 15 */       return (Component)Component.translatable(this.descriptionId + "." + this.descriptionId); 
/* 17 */     return super.getName(itemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ShieldItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */