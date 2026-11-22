/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.TypedInstance;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ 
/*    */ public interface ItemInstance extends TypedInstance<Item>, DataComponentGetter {
/*    */   public static final String FIELD_ID = "id";
/*    */   
/*    */   public static final String FIELD_COUNT = "count";
/*    */   
/*    */   public static final String FIELD_COMPONENTS = "components";
/*    */   
/*    */   int count();
/*    */   
/*    */   default int getMaxStackSize() {
/* 16 */     return ((Integer)getOrDefault(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1))).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ItemInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */