/*    */ package net.minecraft.client.gui.screens.packs;
/*    */ 
/*    */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*    */ 
/*    */ public abstract class Entry extends ObjectSelectionList.Entry<TransferableSelectionList.Entry> {
/*    */   public int getWidth() {
/* 86 */     return super.getWidth() - (TransferableSelectionList.access$000(TransferableSelectionList.this) ? TransferableSelectionList.this.scrollbarWidth() : 0);
/*    */   }
/*    */   
/*    */   public abstract String getPackId();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\packs\TransferableSelectionList$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */