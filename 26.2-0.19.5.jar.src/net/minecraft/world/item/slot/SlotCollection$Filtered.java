/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class Filtered extends Record implements SlotCollection {
/*    */   private final SlotCollection slots;
/*    */   
/*    */   private final Predicate<? super ItemStack> filter;
/*    */   
/*    */   public Filtered(SlotCollection slots, Predicate<? super ItemStack> filter) {
/* 57 */     this.slots = slots;
/* 57 */     this.filter = filter;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/slot/SlotCollection$Filtered;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Filtered;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/slot/SlotCollection$Filtered;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Filtered;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/slot/SlotCollection$Filtered;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Filtered;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public SlotCollection slots() {
/* 57 */     return this.slots;
/*    */   }
/*    */   
/*    */   public Predicate<? super ItemStack> filter() {
/* 57 */     return this.filter;
/*    */   }
/*    */   
/*    */   public Stream<ItemStack> itemCopies() {
/* 60 */     return this.slots.itemCopies().filter(this.filter);
/*    */   }
/*    */   
/*    */   public SlotCollection filter(Predicate<? super ItemStack> predicate) {
/* 65 */     Objects.requireNonNull(predicate);
/* 66 */     return new Filtered(this.slots, t -> (this.filter.test(t) && predicate.test(t)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\slot\SlotCollection$Filtered.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */