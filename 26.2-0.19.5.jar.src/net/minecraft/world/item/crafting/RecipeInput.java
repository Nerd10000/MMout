/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface RecipeInput {
/*    */   ItemStack getItem(int paramInt);
/*    */   
/*    */   int size();
/*    */   
/*    */   default boolean isEmpty() {
/* 11 */     for (int i = 0; i < size(); i++) {
/* 12 */       if (!getItem(i).isEmpty())
/* 13 */         return false; 
/*    */     } 
/* 16 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\RecipeInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */