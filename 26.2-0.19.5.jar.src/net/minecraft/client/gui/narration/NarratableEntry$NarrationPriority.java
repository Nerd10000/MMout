/*    */ package net.minecraft.client.gui.narration;
/*    */ 
/*    */ public enum NarrationPriority {
/* 21 */   NONE, HOVERED, FOCUSED;
/*    */   
/*    */   public boolean isTerminal() {
/* 27 */     return (this == FOCUSED);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\narration\NarratableEntry$NarrationPriority.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */