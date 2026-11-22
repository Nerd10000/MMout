/*    */ package net.minecraft.client.gui.screens.dialog.body;
/*    */ 
/*    */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.screens.dialog.DialogScreen;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.server.dialog.body.DialogBody;
/*    */ import net.minecraft.server.dialog.body.PlainMessage;
/*    */ 
/*    */ class PlainMessageHandler implements DialogBodyHandler<PlainMessage> {
/*    */   public LayoutElement createControls(DialogScreen<?> parent, PlainMessage message) {
/* 66 */     return (LayoutElement)FocusableTextWidget.builder(message.contents(), parent.getFont()).maxWidth(message.width()).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.NEVER).build()
/* 67 */       .setCentered(true)
/* 68 */       .setComponentClickHandler(style -> DialogBodyHandlers.runActionOnParent(parent, style));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\body\DialogBodyHandlers$PlainMessageHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */