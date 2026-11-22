/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ItemStackContentsFactory implements DisplayContentsFactory.ForStacks<ItemStack> {
/* 60 */   public static final ItemStackContentsFactory INSTANCE = new ItemStackContentsFactory();
/*    */   
/*    */   public ItemStack forStack(ItemStack stack) {
/* 64 */     return stack;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$ItemStackContentsFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */