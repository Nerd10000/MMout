/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class SlotDefinition extends Record {
/*    */   private final int slotIndex;
/*    */   
/*    */   private final int x;
/*    */   
/*    */   private final int y;
/*    */   
/*    */   private final Predicate<ItemStack> mayPlace;
/*    */   
/*    */   public SlotDefinition(int slotIndex, int x, int y, Predicate<ItemStack> mayPlace) {
/* 78 */     this.slotIndex = slotIndex;
/* 78 */     this.x = x;
/* 78 */     this.y = y;
/* 78 */     this.mayPlace = mayPlace;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$SlotDefinition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$SlotDefinition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$SlotDefinition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$SlotDefinition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$SlotDefinition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$SlotDefinition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int slotIndex() {
/* 78 */     return this.slotIndex;
/*    */   }
/*    */   
/*    */   public int x() {
/* 78 */     return this.x;
/*    */   }
/*    */   
/*    */   public int y() {
/* 78 */     return this.y;
/*    */   }
/*    */   
/*    */   public Predicate<ItemStack> mayPlace() {
/* 78 */     return this.mayPlace;
/*    */   }
/*    */   
/* 79 */   private static final SlotDefinition EMPTY = new SlotDefinition(0, 0, 0, itemStack -> true);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\ItemCombinerMenuSlotDefinition$SlotDefinition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */