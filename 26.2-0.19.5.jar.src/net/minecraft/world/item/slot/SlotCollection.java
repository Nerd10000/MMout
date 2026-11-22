/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.entity.SlotAccess;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface SlotCollection {
/* 14 */   public static final SlotCollection EMPTY = Stream::empty;
/*    */   
/*    */   default SlotCollection filter(Predicate<? super ItemStack> predicate) {
/* 19 */     return new Filtered(this, predicate);
/*    */   }
/*    */   
/*    */   default SlotCollection flatMap(Function<ItemStack, ? extends SlotCollection> mapper) {
/* 23 */     return new FlatMapped(this, mapper);
/*    */   }
/*    */   
/*    */   default SlotCollection limit(int limit) {
/* 27 */     return new Limited(this, limit);
/*    */   }
/*    */   
/*    */   static SlotCollection of(SlotAccess slotAccess) {
/* 31 */     return () -> Stream.of(slotAccess.get().copy());
/*    */   }
/*    */   
/*    */   static SlotCollection of(Collection<? extends SlotAccess> slots) {
/* 35 */     switch (slots.size()) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */     } 
/* 35 */     return () -> slots.stream().map(SlotAccess::get).map(ItemStack::copy);
/*    */   }
/*    */   
/*    */   static SlotCollection concat(SlotCollection first, SlotCollection second) {
/* 45 */     return () -> Stream.concat(first.itemCopies(), second.itemCopies());
/*    */   }
/*    */   
/*    */   static SlotCollection concat(List<? extends SlotCollection> terms) {
/* 49 */     switch (terms.size()) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */     } 
/* 49 */     return () -> terms.stream().flatMap(SlotCollection::itemCopies);
/*    */   }
/*    */   
/*    */   Stream<ItemStack> itemCopies();
/*    */   
/*    */   public static final class Filtered extends Record implements SlotCollection {
/*    */     private final SlotCollection slots;
/*    */     
/*    */     private final Predicate<? super ItemStack> filter;
/*    */     
/*    */     public Filtered(SlotCollection slots, Predicate<? super ItemStack> filter) {
/* 57 */       this.slots = slots;
/* 57 */       this.filter = filter;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/slot/SlotCollection$Filtered;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Filtered;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/slot/SlotCollection$Filtered;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Filtered;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/slot/SlotCollection$Filtered;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Filtered;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public SlotCollection slots() {
/* 57 */       return this.slots;
/*    */     }
/*    */     
/*    */     public Predicate<? super ItemStack> filter() {
/* 57 */       return this.filter;
/*    */     }
/*    */     
/*    */     public Stream<ItemStack> itemCopies() {
/* 60 */       return this.slots.itemCopies().filter(this.filter);
/*    */     }
/*    */     
/*    */     public SlotCollection filter(Predicate<? super ItemStack> predicate) {
/* 65 */       Objects.requireNonNull(predicate);
/* 66 */       return new Filtered(this.slots, t -> (this.filter.test(t) && predicate.test(t)));
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class FlatMapped extends Record implements SlotCollection {
/*    */     private final SlotCollection slots;
/*    */     
/*    */     private final Function<ItemStack, ? extends SlotCollection> mapper;
/*    */     
/*    */     public FlatMapped(SlotCollection slots, Function<ItemStack, ? extends SlotCollection> mapper) {
/* 70 */       this.slots = slots;
/* 70 */       this.mapper = mapper;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #70	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #70	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #70	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public SlotCollection slots() {
/* 70 */       return this.slots;
/*    */     }
/*    */     
/*    */     public Function<ItemStack, ? extends SlotCollection> mapper() {
/* 70 */       return this.mapper;
/*    */     }
/*    */     
/*    */     public Stream<ItemStack> itemCopies() {
/* 73 */       return this.slots.itemCopies().<SlotCollection>map(this.mapper)
/* 74 */         .flatMap(SlotCollection::itemCopies);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Limited extends Record implements SlotCollection {
/*    */     private final SlotCollection slots;
/*    */     
/*    */     private final int limit;
/*    */     
/*    */     public Limited(SlotCollection slots, int limit) {
/* 78 */       this.slots = slots;
/* 78 */       this.limit = limit;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/slot/SlotCollection$Limited;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Limited;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/slot/SlotCollection$Limited;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Limited;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/slot/SlotCollection$Limited;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Limited;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public SlotCollection slots() {
/* 78 */       return this.slots;
/*    */     }
/*    */     
/*    */     public int limit() {
/* 78 */       return this.limit;
/*    */     }
/*    */     
/*    */     public Stream<ItemStack> itemCopies() {
/* 81 */       return this.slots.itemCopies().limit(this.limit);
/*    */     }
/*    */     
/*    */     public SlotCollection limit(int limit) {
/* 86 */       return new Limited(this.slots, Math.min(this.limit, limit));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\slot\SlotCollection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */