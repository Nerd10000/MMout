/*    */ package net.minecraft.client;
/*    */ 
/*    */ public enum InputType {
/*  4 */   NONE, MOUSE, KEYBOARD_ARROW, KEYBOARD_TAB;
/*    */   
/*    */   public boolean isMouse() {
/* 11 */     return (this == MOUSE);
/*    */   }
/*    */   
/*    */   public boolean isKeyboard() {
/* 15 */     return (this == KEYBOARD_ARROW || this == KEYBOARD_TAB);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\InputType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */