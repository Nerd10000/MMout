/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.item.alchemy.Potions;
/*    */ 
/*    */ public class TippedArrowItem extends ArrowItem {
/*    */   public TippedArrowItem(Item.Properties properties) {
/* 10 */     super(properties);
/*    */   }
/*    */   
/*    */   public ItemStack getDefaultInstance() {
/* 15 */     ItemStack itemStack = super.getDefaultInstance();
/* 16 */     itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.POISON));
/* 17 */     return itemStack;
/*    */   }
/*    */   
/*    */   public Component getName(ItemStack itemStack) {
/* 22 */     PotionContents potion = (PotionContents)itemStack.get(DataComponents.POTION_CONTENTS);
/* 23 */     return (potion != null) ? potion.getName(this.descriptionId + ".effect.") : super.getName(itemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\TippedArrowItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */