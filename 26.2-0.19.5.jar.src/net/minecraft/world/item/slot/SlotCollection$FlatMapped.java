/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class FlatMapped extends Record implements SlotCollection {
/*    */   private final SlotCollection slots;
/*    */   
/*    */   private final Function<ItemStack, ? extends SlotCollection> mapper;
/*    */   
/*    */   public FlatMapped(SlotCollection slots, Function<ItemStack, ? extends SlotCollection> mapper) {
/* 70 */     this.slots = slots;
/* 70 */     this.mapper = mapper;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public SlotCollection slots() {
/* 70 */     return this.slots;
/*    */   }
/*    */   
/*    */   public Function<ItemStack, ? extends SlotCollection> mapper() {
/* 70 */     return this.mapper;
/*    */   }
/*    */   
/*    */   public Stream<ItemStack> itemCopies() {
/* 73 */     return this.slots.itemCopies().<SlotCollection>map(this.mapper)
/* 74 */       .flatMap(SlotCollection::itemCopies);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\slot\SlotCollection$FlatMapped.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */