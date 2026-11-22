/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.item.slot.SlotCollection;
/*    */ 
/*    */ public interface SlotProvider {
/*    */   SlotAccess getSlot(int paramInt);
/*    */   
/*    */   default SlotCollection getSlotsFromRange(IntList slots) {
/* 17 */     List<SlotAccess> slotList = slots.intStream().<SlotAccess>mapToObj(this::getSlot).filter(Objects::nonNull).toList();
/* 18 */     return SlotCollection.of(slotList);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SlotProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */