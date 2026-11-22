/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public interface ForStacks<T> extends DisplayContentsFactory<T> {
/*    */   default T forStack(Holder<Item> item) {
/* 15 */     return forStack(new ItemStack(item));
/*    */   }
/*    */   
/*    */   default T forStack(Item item) {
/* 19 */     return forStack(new ItemStack((ItemLike)item));
/*    */   }
/*    */   
/*    */   T forStack(ItemStack paramItemStack);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\DisplayContentsFactory$ForStacks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */