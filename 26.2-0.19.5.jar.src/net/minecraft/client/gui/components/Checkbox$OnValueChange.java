/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ public interface OnValueChange {
/*    */   public static final OnValueChange NOP = (checkbox, value) -> {
/*    */     
/*    */     };
/*    */   
/*    */   void onValueChange(Checkbox paramCheckbox, boolean paramBoolean);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\Checkbox$OnValueChange.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */