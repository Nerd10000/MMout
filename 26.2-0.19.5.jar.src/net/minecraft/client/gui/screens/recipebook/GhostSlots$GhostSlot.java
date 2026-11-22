/*    */ package net.minecraft.client.gui.screens.recipebook;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ final class GhostSlot extends Record {
/*    */   private final List<ItemStack> items;
/*    */   
/*    */   private final boolean isResultSlot;
/*    */   
/*    */   private GhostSlot(List<ItemStack> items, boolean isResultSlot) {
/* 78 */     this.items = items;
/* 78 */     this.isResultSlot = isResultSlot;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<ItemStack> items() {
/* 78 */     return this.items;
/*    */   }
/*    */   
/*    */   public boolean isResultSlot() {
/* 78 */     return this.isResultSlot;
/*    */   }
/*    */   
/*    */   public ItemStack getItem(int itemIndex) {
/* 80 */     int size = this.items.size();
/* 81 */     if (size == 0)
/* 82 */       return ItemStack.EMPTY; 
/* 84 */     return this.items.get(itemIndex % size);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\recipebook\GhostSlots$GhostSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */