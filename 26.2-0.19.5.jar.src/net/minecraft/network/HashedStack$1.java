/*    */ package net.minecraft.network;
/*    */ 
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class null implements HashedStack {
/*    */   public String toString() {
/* 17 */     return "<empty>";
/*    */   }
/*    */   
/*    */   public boolean matches(ItemStack stack, HashedPatchMap.HashGenerator hasher) {
/* 22 */     return stack.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\HashedStack$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */