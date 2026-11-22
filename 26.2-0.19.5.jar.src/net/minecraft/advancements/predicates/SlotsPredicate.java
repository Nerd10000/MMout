/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.entity.SlotAccess;
/*    */ import net.minecraft.world.entity.SlotProvider;
/*    */ import net.minecraft.world.inventory.SlotRange;
/*    */ import net.minecraft.world.inventory.SlotRanges;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ 
/*    */ public final class SlotsPredicate extends Record {
/*    */   private final Map<SlotRange, ItemPredicate> slots;
/*    */   
/*    */   public SlotsPredicate(Map<SlotRange, ItemPredicate> slots) {
/* 12 */     this.slots = slots;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/SlotsPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/SlotsPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/SlotsPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/SlotsPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/SlotsPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/SlotsPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<SlotRange, ItemPredicate> slots() {
/* 12 */     return this.slots;
/*    */   }
/*    */   
/* 15 */   public static final Codec<SlotsPredicate> CODEC = Codec.unboundedMap(SlotRanges.CODEC, ItemPredicate.CODEC).xmap(SlotsPredicate::new, SlotsPredicate::slots);
/*    */   
/*    */   public boolean matches(SlotProvider slotProvider) {
/* 18 */     for (Map.Entry<SlotRange, ItemPredicate> entry : this.slots.entrySet()) {
/* 19 */       if (!matchSlots(slotProvider, entry.getValue(), ((SlotRange)entry.getKey()).slots()))
/* 20 */         return false; 
/*    */     } 
/* 24 */     return true;
/*    */   }
/*    */   
/*    */   private static boolean matchSlots(SlotProvider slotProvider, ItemPredicate test, IntList slots) {
/* 28 */     for (int i = 0; i < slots.size(); i++) {
/* 29 */       int slotId = slots.getInt(i);
/* 30 */       SlotAccess slot = slotProvider.getSlot(slotId);
/* 31 */       if (slot != null && test.test((ItemInstance)slot.get()))
/* 32 */         return true; 
/*    */     } 
/* 35 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\SlotsPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */