/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class Limited extends Record implements SlotCollection {
/*    */   private final SlotCollection slots;
/*    */   
/*    */   private final int limit;
/*    */   
/*    */   public Limited(SlotCollection slots, int limit) {
/* 78 */     this.slots = slots;
/* 78 */     this.limit = limit;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/slot/SlotCollection$Limited;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Limited;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/slot/SlotCollection$Limited;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Limited;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/slot/SlotCollection$Limited;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Limited;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public SlotCollection slots() {
/* 78 */     return this.slots;
/*    */   }
/*    */   
/*    */   public int limit() {
/* 78 */     return this.limit;
/*    */   }
/*    */   
/*    */   public Stream<ItemStack> itemCopies() {
/* 81 */     return this.slots.itemCopies().limit(this.limit);
/*    */   }
/*    */   
/*    */   public SlotCollection limit(int limit) {
/* 86 */     return new Limited(this.slots, Math.min(this.limit, limit));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\slot\SlotCollection$Limited.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */