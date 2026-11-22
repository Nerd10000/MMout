/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public interface SlotRange extends StringRepresentable {
/*    */   IntList slots();
/*    */   
/*    */   default int size() {
/* 12 */     return slots().size();
/*    */   }
/*    */   
/*    */   static SlotRange of(final String name, final IntList slots) {
/* 16 */     return new SlotRange() {
/*    */         public IntList slots() {
/* 19 */           return slots;
/*    */         }
/*    */         
/*    */         public String getSerializedName() {
/* 24 */           return name;
/*    */         }
/*    */         
/*    */         public String toString() {
/* 29 */           return name;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\SlotRange.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */