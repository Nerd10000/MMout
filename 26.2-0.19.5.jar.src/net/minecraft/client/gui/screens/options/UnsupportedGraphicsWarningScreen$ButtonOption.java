/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public final class ButtonOption {
/*    */   private final Component message;
/*    */   
/*    */   private final Button.OnPress onPress;
/*    */   
/*    */   public ButtonOption(Component message, Button.OnPress onPress) {
/* 83 */     this.message = message;
/* 84 */     this.onPress = onPress;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\UnsupportedGraphicsWarningScreen$ButtonOption.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */